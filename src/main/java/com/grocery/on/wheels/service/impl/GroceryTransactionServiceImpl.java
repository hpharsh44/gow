package com.grocery.on.wheels.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grocery.on.wheels.dao.GroceryTransactionMapper;
import com.grocery.on.wheels.model.AvailableStock;
import com.grocery.on.wheels.model.InventoryPurchaseTransaction;
import com.grocery.on.wheels.model.Invoice;
import com.grocery.on.wheels.model.InvoiceCorrection;
import com.grocery.on.wheels.model.ItemValidate;
import com.grocery.on.wheels.model.PendingItem;
import com.grocery.on.wheels.model.PurchaseTransactionItem;
import com.grocery.on.wheels.model.ValidStock;
import com.grocery.on.wheels.model.VanPurchaseTransaction;
import com.grocery.on.wheels.s3.configuration.S3Configuration;
import com.grocery.on.wheels.s3.service.AmazonClient;
import com.grocery.on.wheels.service.GroceryTransactionService;
import com.grocery.on.wheels.util.GroceryUtil;
import com.grocery.on.wheels.util.PdfBillUtil;
import com.grocery.on.wheels.util.PrinterUtil;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

@Service
@Transactional
public class GroceryTransactionServiceImpl implements GroceryTransactionService {

	private static final String DEBIT = "DEBIT";
	private static final String CREDIT = "CREDIT";
	private static final String PURCHASE = "PURCHASE";
	private static final String SALE = "SALE";
	private static final String VAN = "VAN";
	private static final String INVENTORY = "INVENTORY";
	

	@Autowired
	AmazonClient s3Client;

	@Autowired
	S3Configuration s3Config;

	@Autowired
	GroceryTransactionMapper transactionMapper;
	
	private String generateInvoiceId(String invoceType) {
		String prfeix = "";
		Date date = new Date();
		switch (invoceType) {
			case "inventory":
				prfeix = "INVENTORY_";
				break;
			case "van":
				prfeix = "VAN_";
				break;
			case "store":
				prfeix = "STORE_";
				break;
			default:
				prfeix = "INV_";
				break;
		}
		return prfeix + "" + GroceryUtil.getFormatDate(date);
	}
	
	private void saveInvoice(String invoiceId, String invoiceName) {
		Invoice invoice = new Invoice(invoiceId, invoiceName);
		transactionMapper.createInvoice(invoice);
	}
	
	private List<AvailableStock> getStockAvailability(List<PurchaseTransactionItem> purchaseTransItem, 
			String transactor, boolean purchase) {
		final List<AvailableStock> availableStocks = new ArrayList<>();
		if(purchaseTransItem != null && purchaseTransItem.size() > 0) {
			purchaseTransItem.forEach(transItem -> {
				if(VAN.equals(transactor)) {
					if(purchase) {
						availableStocks.add(transactionMapper.getVanAvailableStocksPurchase(
								transItem.getItemCount(), transItem.getItemExpId(), 
								transItem.getItemPriceId())); // check inventory stock and van purchase pending
					} else {
						availableStocks.add(transactionMapper.getVanAvailableStocksSale(
								transItem.getItemCount(), transItem.getItemExpId(), 
								transItem.getItemPriceId()));
					}
				} else if(INVENTORY.equals(transactor)) {
					if(purchase) {
						availableStocks.add(transactionMapper.getInventoryAvailableStocksPurchase(
								transItem.getItemCount(), transItem.getItemExpId(), 
								transItem.getItemPriceId()));
					} else {
						availableStocks.add(transactionMapper.getInventoryAvailableStocksSale(
								transItem.getItemCount(), transItem.getItemExpId(), 
								transItem.getItemPriceId()));
					}
				}
			});
		}
		return availableStocks;
	}
	
	@Override
	public ValidStock checkValidStock(List<PurchaseTransactionItem> purchaseTransItem, 
			String transactor, boolean purchase) {
		ValidStock validStock = new ValidStock(true);
		List<AvailableStock> availableStocks = getStockAvailability(purchaseTransItem, transactor, purchase);
		for(AvailableStock availableStock: availableStocks) {
			if(availableStock != null && !availableStock.isAvailable()) {
				validStock.setErrorMessage("Requested item count invalid " + availableStock.getRequestedStock()
				+ " available stock " + availableStock.getStockAvailable() + " pending stock " + availableStock.getPendingCount());
			}
		}
		return validStock;
	}
	
	private void printPurchItem(List<PurchaseTransactionItem> purcTransItem) {
		if(purcTransItem != null && purcTransItem.size() > 0) {
			purcTransItem.forEach(pTransItem -> {
				 System.out.println(pTransItem.getItemCount() + " purchase pending:: " + pTransItem.getPurchasePending() + " sale pending " +
			pTransItem.getSalePending());
		  });
		}
	}
	
	private void setItemCountToPending(List<PurchaseTransactionItem> purcTransItem, boolean purchase) {
		//printPurchItem(purcTransItem);
		if(purcTransItem != null && purcTransItem.size() > 0) {
			purcTransItem.forEach(pTransItem -> {
				if(purchase) {
				  pTransItem.setPurchasePending(pTransItem.getItemCount());
				} else {
					pTransItem.setSalePending(pTransItem.getItemCount());
				}
				pTransItem.setItemCount(0);
		  });
		}
		//printPurchItem(purcTransItem);
	}
	
	@Override
	public InventoryPurchaseTransaction purchaseInventory(InventoryPurchaseTransaction inventoryPurchaseTransaction,
			boolean autoValidate) {
		String invoiceId = generateInvoiceId("inventory");
		saveInvoice(invoiceId, inventoryPurchaseTransaction.getInvoiceName());
		inventoryPurchaseTransaction.setInvoiceId(invoiceId);
		inventoryPurchaseTransaction.setTransactionDate(new Date());
		String transactionStatus = inventoryPurchaseTransaction.getTransactionStatus();
		switch (transactionStatus) {
			case "INVOICE":
				inventoryPurchaseTransaction.setTransactionType(CREDIT);
				break;
			case "REFUND":
				inventoryPurchaseTransaction.setTransactionType(DEBIT);
				break;
			default:
				break;
		}
		setItemCountToPending(inventoryPurchaseTransaction.getPurchaseTransItem(), true);
		transactionMapper.saveInventoryTransactionInfo(inventoryPurchaseTransaction);
		insertPendingItems(createPendingItems(inventoryPurchaseTransaction, true, INVENTORY, transactionStatus));
		if(autoValidate) {
			autoValidateInventory(inventoryPurchaseTransaction, invoiceId, transactionStatus, true);
		}
		return inventoryPurchaseTransaction;
	}
	
	private void autoValidateInventory(InventoryPurchaseTransaction inventoryPurchaseTransaction, String invoiceId,
			String transactionStatus, boolean purchase) {
		List<PurchaseTransactionItem> purchaseTransItems = inventoryPurchaseTransaction.getPurchaseTransItem();
		List<ItemValidate> itemValidateList = new ArrayList<>();
		for(PurchaseTransactionItem purchaseTransItem : purchaseTransItems) {
			ItemValidate itemValidate = new ItemValidate();
			itemValidate.setVanInvId(inventoryPurchaseTransaction.getInventoryId());
			itemValidate.setItemPriceId(purchaseTransItem.getItemPriceId());
			itemValidate.setItemExpId(purchaseTransItem.getItemExpId());
			itemValidate.setInvoiceId(invoiceId);
			switch (transactionStatus) {
				case "INVOICE":
					itemValidate.setValidateCount(purchaseTransItem.getPurchasePending() + purchaseTransItem.getSalePending());
					itemValidate.setRefundCount(0);
					break;
				case "REFUND":
					itemValidate.setValidateCount(0);
					itemValidate.setRefundCount(purchaseTransItem.getPurchasePending() + purchaseTransItem.getSalePending());
					break;
				default:
					break;
			}
			
			itemValidateList.add(itemValidate);
		}
		if(purchase) {
			validateInventoryPurchase(itemValidateList);
		} else {
			validateInventoryItemSale(itemValidateList);
		}
	}
	
	private List<PendingItem> createPendingItems(Invoice invoice, boolean purchase, String transactor, String transactionStatus) {
		List<PendingItem> pendingItemList = new ArrayList<>();
		final StringBuffer inventoryVanId = new StringBuffer("");
		final StringBuffer invoiceId = new StringBuffer("");
		List<PurchaseTransactionItem> purchaseTransItems = null;
		if(invoice instanceof InventoryPurchaseTransaction) {
			InventoryPurchaseTransaction invPurchTrans = (InventoryPurchaseTransaction) invoice;
			inventoryVanId.append(invPurchTrans.getInventoryId());
			invoiceId.append(invPurchTrans.getInvoiceId());
			purchaseTransItems = invPurchTrans.getPurchaseTransItem();
		} else if(invoice instanceof VanPurchaseTransaction) {
			VanPurchaseTransaction vanPurchTrans = (VanPurchaseTransaction) invoice;
			inventoryVanId.append(vanPurchTrans.getVanId());
			invoiceId.append(vanPurchTrans.getInvoiceId());
			purchaseTransItems = vanPurchTrans.getPurchaseTransItem();
		}
		 purchaseTransItems.forEach(purchaseTransItem -> {
			 PendingItem pendingItem = new PendingItem();
			 pendingItem.setVanInvId(inventoryVanId.toString());
			 pendingItem.setItemPriceId(purchaseTransItem.getItemPriceId());
			 pendingItem.setExpDate(purchaseTransItem.getExpDate());
			 pendingItem.setItemExpId(purchaseTransItem.getItemExpId());
			 pendingItem.setInvcId(invoiceId.toString());
			 pendingItem.setTransactionStatus(transactionStatus);
			 if(purchase) {
				 //pendingItem.setPendingCount(purchaseTransItem.getItemCount());
				 pendingItem.setPendingCount(purchaseTransItem.getPurchasePending());
				 pendingItem.setTransType(PURCHASE);
			 } else {
				 //pendingItem.setPendingCount(purchaseTransItem.getItemCount());
				 pendingItem.setPendingCount(purchaseTransItem.getSalePending());
				 pendingItem.setTransType(SALE);
			 }

			 pendingItem.setTransactor(transactor);
			 pendingItemList.add(pendingItem);
		 });
		 
		return pendingItemList;
	}
	
	private void insertPendingItems(List<PendingItem> pendingItemList) {
		if(pendingItemList != null && pendingItemList.size() > 0) {
			transactionMapper.insertPendingItems(pendingItemList);
		}
	}
	
	private List<PurchaseTransactionItem> groupSimilarItem(List<PurchaseTransactionItem> purchaseTransItems) {
		/*Map<String, TreeMap<LocalDate, Thing>> result = Arrays
	            .asList(new Thing("a", LocalDate.now().minusDays(1), 12f), new Thing("a", LocalDate.now(), 12f), new Thing("a", LocalDate.now(), 13f))
	            .stream()
	            .collect(Collectors.groupingBy(Thing::getKey,
	                    Collectors.toMap(Thing::getActivityDate, Function.identity(),
	                            (Thing left, Thing right) -> new Thing(left.getKey(), left.getActivityDate(), 
	                            left.getValue() + right.getValue()),
	                            TreeMap::new)));
		*/
		/*Map<String, TreeMap<String, PurchaseTransactionItem>> result = purchaseTransItems
				.stream()
				.collect(Collectors.groupingBy(PurchaseTransactionItem::getItemId, Function.identity(),
						(PurchaseTransactionItem left, PurchaseTransactionItem right) -> {
							return left;
						}, TreeMap::new));
	 System.out.println(result); // {a={2017-06-24= value = 12.0, 2017-06-25= value = 25.0}}
	 */
	 return purchaseTransItems;
	}
	
	public String createAndUploadInvoice(Invoice invoice) {
		return new PdfBillUtil().generatePDFInvoice(invoice, s3Client, s3Config);
	}
	
	private String createAndUploadInvoiceBKP(Invoice invoice) {
		List<Paragraph> pdfHeadTextList = new ArrayList<>();
		List<Paragraph> pdfFootTextList = new ArrayList<>();
		PdfPTable headTable = new PdfPTable(2);
		PdfPTable bodyTable = new PdfPTable(4);
		PdfPTable footTable = new PdfPTable(1);
		
		Font tableHeadFootFont = new Font(FontFamily.HELVETICA, 12, Font.BOLD, new BaseColor(0, 0, 0));
		Font tableItemFont = new Font(FontFamily.HELVETICA, 10, Font.NORMAL, new BaseColor(75, 75, 75));
		String invoiceUrl = "";
		if(invoice != null) {
			 if(invoice instanceof InventoryPurchaseTransaction) {
				InventoryPurchaseTransaction transactionInvoice = (InventoryPurchaseTransaction) invoice;
				
				List<PurchaseTransactionItem> purchaseTransItems = transactionInvoice.getPurchaseTransItem();
				if(purchaseTransItems != null && purchaseTransItems.size() > 0) {
					purchaseTransItems = purchaseTransItems.stream().filter(t -> t.getItemCount() > 0).collect(Collectors.toList());
					if(DEBIT.equals(transactionInvoice.getTransactionType())) {
						bodyTable = new PdfPTable(6);
					} else {
						bodyTable = new PdfPTable(5);
					}
					headTable.addCell(geHeadFootCell("BILL TO",Element.ALIGN_LEFT, new Font(FontFamily.HELVETICA, 12, Font.NORMAL, new BaseColor(0, 0, 0))));
					headTable.addCell(geHeadFootCell(GroceryUtil.clearPrefix(invoice.getInvoiceId()),Element.ALIGN_RIGHT, new Font(FontFamily.HELVETICA, 12, Font.NORMAL, new BaseColor(0, 0, 0))));
					
					headTable.addCell(geHeadFootCell(transactionInvoice.getSupplierId(),Element.ALIGN_LEFT, new Font(FontFamily.HELVETICA, 10, Font.BOLD, new BaseColor(0, 0, 0))));
					headTable.addCell(geHeadFootCell("",Element.ALIGN_RIGHT, new Font(FontFamily.HELVETICA, 10, Font.NORMAL, new BaseColor(0, 0, 0))));
					
					headTable.addCell(geHeadFootCell("",Element.ALIGN_RIGHT, new Font(FontFamily.HELVETICA, 8, Font.BOLD, new BaseColor(0, 0, 0))));
					headTable.addCell(geHeadFootCell(transactionInvoice.getTransactionDate().toString(),Element.ALIGN_RIGHT, new Font(FontFamily.HELVETICA, 8, Font.NORMAL, new BaseColor(0, 0, 0))));
					
					headTable.addCell(getDividerCell("", Element.ALIGN_CENTER, tableHeadFootFont));
					headTable.addCell(getDividerCell("", Element.ALIGN_CENTER, tableHeadFootFont));
					
					bodyTable.addCell(getCell("ITEMS", Element.ALIGN_LEFT, tableHeadFootFont));
					bodyTable.addCell(getCell("QTY", Element.ALIGN_RIGHT, tableHeadFootFont));
					//if(DEBIT.equals(transactionInvoice.getTransactionType())) {
						bodyTable.addCell(getCell("MRP", Element.ALIGN_RIGHT, tableHeadFootFont));
					//}
					bodyTable.addCell(getCell("RATE", Element.ALIGN_RIGHT, tableHeadFootFont));
					if(DEBIT.equals(transactionInvoice.getTransactionType())) {
						bodyTable.addCell(getCell("DISC.", Element.ALIGN_RIGHT, tableHeadFootFont));
					}
					bodyTable.addCell(getCell("AMOUNT", Element.ALIGN_RIGHT, tableHeadFootFont));
					
					double subTotal = 0;
					int itemCount = 0;
					double totalMRP = 0;
					double totalRate = 0;
					String percentage = "";
					for(PurchaseTransactionItem purchaseTransItem: purchaseTransItems) {
						double rate = 0;
						double mrpd = Double.parseDouble(purchaseTransItem.getMrp());
						totalMRP += (mrpd * purchaseTransItem.getItemCount());
					
						if(DEBIT.equals(transactionInvoice.getTransactionType())) {
							double spd = Double.parseDouble(purchaseTransItem.getSp());
							rate = spd / 100;
							totalRate += (spd * purchaseTransItem.getItemCount());
							percentage = GroceryUtil.calcPercentage(mrpd, spd); 
							if("REFUND".equals(transactionInvoice.getTransactionStatus())) {
								rate = Double.parseDouble(purchaseTransItem.getCp()) / 100;
							} else {
								rate = Double.parseDouble(purchaseTransItem.getSp()) / 100;
							}
						} else {
							rate = Double.parseDouble(purchaseTransItem.getCp()) / 100;
						}
						double amount = purchaseTransItem.getItemCount() * rate;
						subTotal += amount;
						itemCount += purchaseTransItem.getItemCount();
						bodyTable.addCell(getCell(purchaseTransItem.getItemName(), Element.ALIGN_LEFT, tableItemFont));
						bodyTable.addCell(getCell(purchaseTransItem.getItemCount() + "", Element.ALIGN_RIGHT, tableItemFont));
						//if(DEBIT.equals(transactionInvoice.getTransactionType())) {
							bodyTable.addCell(getCell((Double.parseDouble(purchaseTransItem.getMrp()) / 100) + "", 
									Element.ALIGN_RIGHT, tableItemFont));
						//}
						bodyTable.addCell(getCell(rate + "", Element.ALIGN_RIGHT, tableItemFont));
						if(DEBIT.equals(transactionInvoice.getTransactionType())) {
							bodyTable.addCell(getCell(percentage + "%", Element.ALIGN_RIGHT, tableItemFont));
						}
						bodyTable.addCell(getCell(amount + "", Element.ALIGN_RIGHT, tableItemFont));
					}
					bodyTable.addCell(getCell("SUB TOTAL", Element.ALIGN_LEFT, tableHeadFootFont));
					bodyTable.addCell(getCell(itemCount+"", Element.ALIGN_RIGHT, tableHeadFootFont));
					
					
					bodyTable.addCell(getCell((totalMRP / 100) + "", Element.ALIGN_RIGHT, tableHeadFootFont));
					if(DEBIT.equals(transactionInvoice.getTransactionType())) {
						bodyTable.addCell(getCell((totalRate / 100) + "", Element.ALIGN_RIGHT, tableHeadFootFont));
						bodyTable.addCell(getCell(GroceryUtil.calcPercentage(totalMRP, totalRate) + "%", Element.ALIGN_RIGHT, tableHeadFootFont));
						bodyTable.addCell(getCell(subTotal+"", Element.ALIGN_RIGHT, tableHeadFootFont));
					} else {
						bodyTable.addCell(getCell("", Element.ALIGN_RIGHT, tableHeadFootFont));
						bodyTable.addCell(getCell(subTotal+"", Element.ALIGN_RIGHT, tableHeadFootFont));
					}
					bodyTable.addCell(getDividerCell("", Element.ALIGN_CENTER, tableHeadFootFont));
					bodyTable.addCell(getDividerCell("", Element.ALIGN_CENTER, tableHeadFootFont));
					
					PdfPCell colSpan = new PdfPCell();
					colSpan.setColspan(2);
					colSpan.setBorder(PdfPCell.NO_BORDER);
					bodyTable.addCell(colSpan);
					bodyTable.addCell(getCell("GRAND TOTAL", Element.ALIGN_CENTER, tableHeadFootFont));
					bodyTable.addCell(getCell(subTotal+"", Element.ALIGN_RIGHT, tableHeadFootFont));
				}
			} else if(invoice instanceof VanPurchaseTransaction) {
				VanPurchaseTransaction transactionInvoice = (VanPurchaseTransaction) invoice;
				List<PurchaseTransactionItem> purchaseTransItems = transactionInvoice.getPurchaseTransItem();
				if(purchaseTransItems != null && purchaseTransItems.size() > 0) {
					purchaseTransItems = purchaseTransItems.stream().filter(t -> t.getItemCount() > 0).collect(Collectors.toList());
					headTable.addCell(geHeadFootCell("BILL TO",Element.ALIGN_LEFT, new Font(FontFamily.HELVETICA, 12, Font.NORMAL, new BaseColor(0, 0, 0))));
					headTable.addCell(geHeadFootCell(GroceryUtil.clearPrefix(invoice.getInvoiceId()),Element.ALIGN_RIGHT, new Font(FontFamily.HELVETICA, 12, Font.NORMAL, new BaseColor(0, 0, 0))));
					
					
					headTable.addCell(geHeadFootCell(transactionInvoice.getCustomerName(),Element.ALIGN_LEFT, new Font(FontFamily.HELVETICA, 10, Font.BOLD, new BaseColor(0, 0, 0))));
					headTable.addCell(geHeadFootCell("",Element.ALIGN_RIGHT, new Font(FontFamily.HELVETICA, 10, Font.NORMAL, new BaseColor(0, 0, 0))));
					
					headTable.addCell(geHeadFootCell(transactionInvoice.getCustomerMob(),Element.ALIGN_RIGHT, new Font(FontFamily.HELVETICA, 8, Font.BOLD, new BaseColor(0, 0, 0))));
					headTable.addCell(geHeadFootCell(transactionInvoice.getTransactionDate().toString(),Element.ALIGN_RIGHT, new Font(FontFamily.HELVETICA, 8, Font.NORMAL, new BaseColor(0, 0, 0))));
					
					headTable.addCell(getDividerCell("", Element.ALIGN_CENTER, tableHeadFootFont));
					headTable.addCell(getDividerCell("", Element.ALIGN_CENTER, tableHeadFootFont));
					if(DEBIT.equals(transactionInvoice.getTransactionType())) {
						bodyTable = new PdfPTable(6);
					}
					
					bodyTable.addCell(getCell("ITEMS", Element.ALIGN_LEFT, tableHeadFootFont));
					bodyTable.addCell(getCell("QTY", Element.ALIGN_RIGHT, tableHeadFootFont));
					if(DEBIT.equals(transactionInvoice.getTransactionType())) {
						bodyTable.addCell(getCell("MRP", Element.ALIGN_RIGHT, tableHeadFootFont));
					}
					bodyTable.addCell(getCell("RATE", Element.ALIGN_RIGHT, tableHeadFootFont));
					if(DEBIT.equals(transactionInvoice.getTransactionType())) {
						bodyTable.addCell(getCell("DISC.", Element.ALIGN_RIGHT, tableHeadFootFont));
					}
					bodyTable.addCell(getCell("AMOUNT", Element.ALIGN_RIGHT, tableHeadFootFont));
					double subTotal = 0;
					int itemCount = 0;
					double totalMRP = 0;
					double totalRate = 0;
					for(PurchaseTransactionItem purchaseTransItem: purchaseTransItems) {
						double rate = 0;
						String percentage = "";
						if(DEBIT.equals(transactionInvoice.getTransactionType())) {
							double mrpd = Double.parseDouble(purchaseTransItem.getMrp());
							totalMRP += (mrpd * purchaseTransItem.getItemCount());
							double spd = Double.parseDouble(purchaseTransItem.getSp());
							rate = spd / 100;
							totalRate += (spd * purchaseTransItem.getItemCount());
							percentage = GroceryUtil.calcPercentage(mrpd, spd); 
						} else {
							if("REFUND".equals(transactionInvoice.getTransactionStatus())) {
								rate = Double.parseDouble(purchaseTransItem.getMrp()) / 100;
							} else {
								rate = Double.parseDouble(purchaseTransItem.getSp()) / 100;
							}
						}
						
						double amount = purchaseTransItem.getItemCount() * rate;
						subTotal += amount;
						itemCount += purchaseTransItem.getItemCount();
						bodyTable.addCell(getCell(purchaseTransItem.getItemName(), Element.ALIGN_LEFT, tableItemFont));
						bodyTable.addCell(getCell(purchaseTransItem.getItemCount() + "", Element.ALIGN_RIGHT, tableItemFont));
						if(DEBIT.equals(transactionInvoice.getTransactionType())) {
							bodyTable.addCell(getCell((Double.parseDouble(purchaseTransItem.getMrp()) / 100) + "", 
									Element.ALIGN_RIGHT, tableItemFont));
						}
						bodyTable.addCell(getCell(rate + "", Element.ALIGN_RIGHT, tableItemFont));
						if(DEBIT.equals(transactionInvoice.getTransactionType())) {
							bodyTable.addCell(getCell(percentage + "%", Element.ALIGN_RIGHT, tableItemFont));
						}
						bodyTable.addCell(getCell(amount + "", Element.ALIGN_RIGHT, tableItemFont));
					}
					bodyTable.addCell(getCell("SUB TOTAL", Element.ALIGN_LEFT, tableHeadFootFont));
					bodyTable.addCell(getCell(itemCount+"", Element.ALIGN_RIGHT, tableHeadFootFont));
					if(DEBIT.equals(transactionInvoice.getTransactionType())) {
						bodyTable.addCell(getCell((totalMRP / 100) + "", Element.ALIGN_RIGHT, tableHeadFootFont));
						bodyTable.addCell(getCell((totalRate / 100) + "", Element.ALIGN_RIGHT, tableHeadFootFont));
						bodyTable.addCell(getCell(GroceryUtil.calcPercentage(totalMRP, totalRate) + "%", Element.ALIGN_RIGHT, tableHeadFootFont));
						bodyTable.addCell(getCell(subTotal+"", Element.ALIGN_RIGHT, tableHeadFootFont));
					} else {
						bodyTable.addCell(getCell("", Element.ALIGN_RIGHT, tableHeadFootFont));
						bodyTable.addCell(getCell(subTotal+"", Element.ALIGN_RIGHT, tableHeadFootFont));
					}
					bodyTable.addCell(getDividerCell("", Element.ALIGN_CENTER, tableHeadFootFont));
					bodyTable.addCell(getDividerCell("", Element.ALIGN_CENTER, tableHeadFootFont));
					bodyTable.addCell(getDividerCell("", Element.ALIGN_CENTER, tableHeadFootFont));
					bodyTable.addCell(getDividerCell("", Element.ALIGN_CENTER, tableHeadFootFont));
					bodyTable.addCell(getDividerCell("", Element.ALIGN_CENTER, tableHeadFootFont));
					bodyTable.addCell(getDividerCell("", Element.ALIGN_CENTER, tableHeadFootFont));
										
					PdfPCell colSpan = new PdfPCell();
					colSpan.setColspan(2);
					colSpan.setBorder(PdfPCell.NO_BORDER);
					bodyTable.addCell(colSpan);
					bodyTable.addCell(getCell("GRAND TOTAL", Element.ALIGN_CENTER, tableHeadFootFont));
					bodyTable.addCell(getCell(subTotal+"", Element.ALIGN_RIGHT, tableHeadFootFont));
				}
			}
			 footTable.addCell(geHeadFootCell("TERMS AND CONDITIONS", Element.ALIGN_LEFT, new Font(FontFamily.HELVETICA, 8, Font.NORMAL, new BaseColor(0, 0, 0))));
			 footTable.addCell(geHeadFootCell("1. Goods once sold will not be taken back or exchanged", Element.ALIGN_LEFT, new Font(FontFamily.HELVETICA, 6, Font.NORMAL, new BaseColor(0, 0, 0))));
			 footTable.addCell(geHeadFootCell("Grocery on Wheel", Element.ALIGN_RIGHT, new Font(FontFamily.HELVETICA, 9, Font.BOLD, new BaseColor(0, 0, 0))));
			 footTable.addCell(geHeadFootCell("+919050092092", Element.ALIGN_RIGHT, new Font(FontFamily.HELVETICA, 8, Font.NORMAL, new BaseColor(0, 0, 0))));
			 
			File tempPdfFile = GroceryUtil.createAndUpload(pdfHeadTextList, pdfFootTextList, invoice.getInvoiceId(), 
					headTable, bodyTable, footTable);
			invoiceUrl = GroceryUtil.uploadFile(tempPdfFile, s3Config, s3Client);
			if(tempPdfFile != null) {
				tempPdfFile.delete();
				//invoiceUrl = tempPdfFile.getAbsolutePath();
			}
			
		}
		return invoiceUrl;
	}
	
	public PdfPCell getDividerCell(String text, int alignment, Font font) {
		PdfPCell cell = new PdfPCell();
		Paragraph p = new Paragraph(text, font);
		p.setAlignment(alignment);
		cell.addElement(p);
	    cell.setHorizontalAlignment(alignment);
	    cell.setPaddingTop(15);
	    cell.setBorder(PdfPCell.BOTTOM);
	    cell.setBorderWidth(2);
	    return cell;
	}
	
	public PdfPCell geHeadFootCell(String text, int alignment, Font font) {
		PdfPCell cell = new PdfPCell();
		Paragraph p = new Paragraph(text, font);
		p.setAlignment(alignment);
		cell.addElement(p);
	    cell.setPaddingBottom(2);
	    cell.setHorizontalAlignment(alignment);
	    cell.setBorder(PdfPCell.NO_BORDER);
	    return cell;
	}
	
	public PdfPCell getCell(String text, int alignment, Font font) {
		PdfPCell cell = new PdfPCell();
		Paragraph p = new Paragraph(text, font);
		p.setAlignment(alignment);
		cell.addElement(p);
	    cell.setPaddingBottom(10);
	    cell.setBorder(PdfPCell.BOTTOM);
	    cell.setBorderWidth(0.5f);
	    cell.setBorderColor(BaseColor.LIGHT_GRAY);
	    return cell;
	}
	
	@Override
	public VanPurchaseTransaction purchaseVan(VanPurchaseTransaction vanPurchaseTransaction, boolean autoValidate) {
		String invoiceId = generateInvoiceId("van");
		vanPurchaseTransaction.setInvoiceId(invoiceId);
		switch (vanPurchaseTransaction.getTransactionStatus()) {
			case "INVOICE":
				vanPurchaseTransaction.setTransactionType(CREDIT);
				break;
			case "REFUND":
				vanPurchaseTransaction.setTransactionType(DEBIT);
				break;
			default:
				break;
		}

		vanPurchaseTransaction.setTransactionDate(new Date());
		saveInvoice(invoiceId, vanPurchaseTransaction.getInvoiceName());
		setItemCountToPending(vanPurchaseTransaction.getPurchaseTransItem(), true);
		transactionMapper.saveVanTransactionInfo(vanPurchaseTransaction);
		transactionMapper.addVanItemMap(vanPurchaseTransaction);
		String transactionStatus = vanPurchaseTransaction.getTransactionStatus();
		insertPendingItems(createPendingItems(vanPurchaseTransaction, true, VAN, transactionStatus));
		switch (transactionStatus) {
			case "INVOICE":
				vanPurchaseTransaction.setTransactionType(DEBIT);
				transactionMapper.updateInventoryStockSale(vanPurchaseTransaction);
				break;
			case "REFUND":
				vanPurchaseTransaction.setTransactionType(CREDIT);
				break;
			default:
				break;
		}
		if(autoValidate) {
			autoValidateVanPurchase(vanPurchaseTransaction, invoiceId, transactionStatus);
		}
		return vanPurchaseTransaction;
	}
	
	@Override
	public VanPurchaseTransaction sellVan(VanPurchaseTransaction vanPurchaseTransaction, boolean autoValidate) {
		String invoiceId = generateInvoiceId("van");
		vanPurchaseTransaction.setInvoiceId(invoiceId);
		String transactionStatus = vanPurchaseTransaction.getTransactionStatus(); 
		switch (transactionStatus) {
			case "INVOICE":
				vanPurchaseTransaction.setTransactionType(DEBIT);
				break;
			case "REFUND":
				vanPurchaseTransaction.setTransactionType(CREDIT);
				break;
			default:
				break;
		}
		vanPurchaseTransaction.setTransactionDate(new Date());
		saveInvoice(invoiceId, vanPurchaseTransaction.getInvoiceName());
		setItemCountToPending(vanPurchaseTransaction.getPurchaseTransItem(), false);
		transactionMapper.saveVanTransactionInfo(vanPurchaseTransaction);
		transactionMapper.updateVanStockSale(vanPurchaseTransaction);
		insertPendingItems(createPendingItems(vanPurchaseTransaction, false, VAN, transactionStatus));
		if(autoValidate) {
			autoValidateVanSale(vanPurchaseTransaction, invoiceId, transactionStatus);
		}
		return vanPurchaseTransaction;
	}
	
	private void autoValidateVanPurchase(VanPurchaseTransaction vanPurchaseTransaction, String invoiceId,
			String transactionStatus) {
		List<PurchaseTransactionItem> purchaseTransItems = vanPurchaseTransaction.getPurchaseTransItem();
		List<ItemValidate> itemValidateList = new ArrayList<>();
		for(PurchaseTransactionItem purchaseTransItem : purchaseTransItems) {
			ItemValidate itemValidate = new ItemValidate();
			itemValidate.setVanInvId(vanPurchaseTransaction.getVanId());
			itemValidate.setItemPriceId(purchaseTransItem.getItemPriceId());
			itemValidate.setItemExpId(purchaseTransItem.getItemExpId());
			itemValidate.setInvoiceId(invoiceId);
			switch (transactionStatus) {
				case "INVOICE":
					itemValidate.setValidateCount(purchaseTransItem.getPurchasePending());
					itemValidate.setRefundCount(0);
					break;
				case "REFUND":
					itemValidate.setValidateCount(0);
					itemValidate.setRefundCount(purchaseTransItem.getPurchasePending());
					break;
				default:
					break;
			}
			
			itemValidateList.add(itemValidate);
		}
		validateVanPurchase(itemValidateList, true);
	}
	
	private void autoValidateVanSale(VanPurchaseTransaction vanPurchaseTransaction, String invoiceId,
			String transactionStatus) {
		List<PurchaseTransactionItem> purchaseTransItems = vanPurchaseTransaction.getPurchaseTransItem();
		List<ItemValidate> itemValidateList = new ArrayList<>();
		for(PurchaseTransactionItem purchaseTransItem : purchaseTransItems) {
			ItemValidate itemValidate = new ItemValidate();
			itemValidate.setVanInvId(vanPurchaseTransaction.getVanId());
			itemValidate.setItemPriceId(purchaseTransItem.getItemPriceId());
			itemValidate.setItemExpId(purchaseTransItem.getItemExpId());
			itemValidate.setInvoiceId(invoiceId);
			switch (transactionStatus) {
				case "INVOICE":
					itemValidate.setValidateCount(purchaseTransItem.getSalePending());
					itemValidate.setRefundCount(0);
					break;
				case "REFUND":
					itemValidate.setValidateCount(0);
					itemValidate.setRefundCount(purchaseTransItem.getSalePending());
					break;
				default:
					break;
			}
			
			itemValidateList.add(itemValidate);
		}
		validateVanSale(itemValidateList);
	}

	@Override
	public void validateInventoryPurchase(List<ItemValidate> itemValidateList) {
		transactionMapper.updateInventoryItemMap(itemValidateList);//update inventory_item_map 
		transactionMapper.updateInventoryPurchaseTransaction(itemValidateList);//update inventory_transaction 
		transactionMapper.updatePurchasePendingItem(itemValidateList);
	}
	
	@Override
	public void validateInventoryItemSale(List<ItemValidate> itemValidateList) {
		transactionMapper.updatePurchasePendingItem(itemValidateList);//pending_item
		transactionMapper.validateInventoryRefundPurchasePendingItem(itemValidateList);
		transactionMapper.validateInventorySaleTransaction(itemValidateList);
	}

	@Override
	public void validateInventorySale(List<ItemValidate> itemValidateList) {
		transactionMapper.validateInventorySale(itemValidateList);
	}

	@Override
	public void validateVanSale(List<ItemValidate> itemValidateList) {
		transactionMapper.validateVanSaleTransaction(itemValidateList);
		transactionMapper.validateInventoryPurchasePendingItem(itemValidateList);
		transactionMapper.validateVanRefundPurchasePendingItem(itemValidateList);
	}

	private boolean validList(List<ItemValidate> checkList) {
		if(checkList != null && checkList.size() > 0) {
			return true;
		}
		return false;
	}
	
	@Override
	public void validateVanPurchase(List<ItemValidate> itemValidateList, boolean updateInventory) {
		List<ItemValidate> nonClearPendingItems = itemValidateList.stream().filter(itemValidate ->  
			!"YES".equals(itemValidate.getClearPending())
		).collect(Collectors.toList());
		if(validList(nonClearPendingItems)) {
			transactionMapper.validateVanPurchaseItemMap(nonClearPendingItems);
			if(updateInventory) {
				transactionMapper.validateInventoryRefundPurchasePendingItem(nonClearPendingItems);
			}
			transactionMapper.validateVanPurchaseTransaction(nonClearPendingItems);
			transactionMapper.validateInventoryPurchasePendingItem(nonClearPendingItems);
		}
		List<ItemValidate> clearPendingItems = itemValidateList.stream().filter(itemValidate ->  
			"YES".equals(itemValidate.getClearPending())
		).collect(Collectors.toList());
		if(validList(clearPendingItems)) {
			transactionMapper.validateInventoryPurchasePendingItem(clearPendingItems);
			transactionMapper.validateClearInventoryPendingItem(clearPendingItems);
			transactionMapper.validateClearVanTransactionPendingItem(clearPendingItems);
		}
	}

	@Override
	public void clearTransaction() {
		transactionMapper.clearTransaction();
	}
	
	@Override
	public String generateInventoryPrinterText(String invoiceId, String invcGen) {
		Invoice invoice = null;
		if("VAN".equals(invcGen)) {
			invoice = transactionMapper.getVanPurchaseTransaction(invoiceId);
		} else if("INV".equals(invcGen)) {
			invoice = transactionMapper.getInventoryPurchaseTransaction(invoiceId);
		}
		
		PrinterUtil printer = new PrinterUtil();
		String printerText = printer.generatePrinterText(invoice);
		return printerText;
	}
	
	@Override
	public String generateInventoryInvoice(String invoiceId) {
		InventoryPurchaseTransaction inventoryPurchaseTransaction = transactionMapper.getInventoryPurchaseTransaction(invoiceId);
		String invoiceUrl = createAndUploadInvoice(inventoryPurchaseTransaction);
		return invoiceUrl;
	}
	

	@Override
	public String generateVanInvoice(String invoiceId) {
		VanPurchaseTransaction vanPurchaseTransaction = transactionMapper.getVanPurchaseTransaction(invoiceId);
		String invoiceUrl = createAndUploadInvoice(vanPurchaseTransaction);
		return invoiceUrl;
	}

	@Override
	public InventoryPurchaseTransaction inventoryItemSale(InventoryPurchaseTransaction inventoryPurchaseTransaction,
			boolean autoValidate) {
		String invoiceId = generateInvoiceId("inventory");
		inventoryPurchaseTransaction.setInvoiceId(invoiceId);
		String transactionStatus = inventoryPurchaseTransaction.getTransactionStatus(); 
		switch (transactionStatus) {
			case "INVOICE":
				inventoryPurchaseTransaction.setTransactionType(DEBIT);
				break;
			case "REFUND":
				inventoryPurchaseTransaction.setTransactionType(CREDIT);
				break;
			default:
				break;
		}
		setItemCountToPending(inventoryPurchaseTransaction.getPurchaseTransItem(), false);
		inventoryPurchaseTransaction.setTransactionDate(new Date());
		saveInvoice(invoiceId, inventoryPurchaseTransaction.getInvoiceName());
		transactionMapper.saveInventoryTransactionInfo(inventoryPurchaseTransaction);
		insertPendingItems(createPendingItems(inventoryPurchaseTransaction, false, INVENTORY, transactionStatus));
		if(autoValidate) {
			autoValidateInventory(inventoryPurchaseTransaction, invoiceId, transactionStatus, false);
		}
		return inventoryPurchaseTransaction;
	}

	@Override
	public String vanInvoiceCorrection(List<InvoiceCorrection> invoiceCorrectionObj, String transType) {
		InvoiceCorrection firstItem = invoiceCorrectionObj.get(0);
		if(firstItem != null && ("VAN_PURCHASE_RETURN".equals(firstItem.getTransactor())
				|| "VAN_SALE_RETURN".equals(firstItem.getTransactor()))
				) {
			invoiceCorrectionObj.forEach(
					obj -> obj.setCorrectionCount(obj.getCorrectionCount() * -1)
			);  // THIS IS TO HANDLE EXCEPTION CASE
		}
		
		if(invoiceCorrectionObj != null && invoiceCorrectionObj.size() > 0) {
			transactionMapper.correctVanTransaction(invoiceCorrectionObj, transType);
			transactionMapper.correctVanItem(invoiceCorrectionObj);
			if(firstItem != null && "VAN_PURCHASE_RETURN".equals(firstItem.getTransactor())) {
				transactionMapper.correctVanInventoryItem(invoiceCorrectionObj);
			}
		}
		return "saved";
	}

	@Override
	public String inventoryInvoiceCorrection(List<InvoiceCorrection> invoiceCorrectionObj) {
		transactionMapper.correctInventoryTransaction(invoiceCorrectionObj);
		transactionMapper.correctInventoryItem(invoiceCorrectionObj);
		return "saved";
	}

}