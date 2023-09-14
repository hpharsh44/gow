package com.grocery.on.wheels.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.grocery.on.wheels.model.InventoryPurchaseTransaction;
import com.grocery.on.wheels.model.Invoice;
import com.grocery.on.wheels.model.PurchaseTransactionItem;
import com.grocery.on.wheels.model.VanPurchaseTransaction;
import com.grocery.on.wheels.s3.configuration.S3Configuration;
import com.grocery.on.wheels.s3.service.AmazonClient;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class PdfBillUtil {
	private final Logger LOGGER = LoggerFactory.getLogger(PdfBillUtil.class);
	private static final String DEBIT = "DEBIT";

	Font tableHeadFootFont = new Font(FontFamily.HELVETICA, 12, Font.BOLD, new BaseColor(0, 0, 0));
	Font tableItemFont = new Font(FontFamily.HELVETICA, 10, Font.NORMAL, new BaseColor(75, 75, 75));

	boolean inventoryTransaction;
	private String transactionType;
	private String transactionStatus;
	private InventoryPurchaseTransaction inventoryPurcaseTransaction;
	private VanPurchaseTransaction vanPurchaseTransaction;
	
	public boolean isInventoryTransaction() {
		return inventoryTransaction;
	}

	public void setInventoryTransaction(boolean inventoryTransaction) {
		this.inventoryTransaction = inventoryTransaction;
	}

	public InventoryPurchaseTransaction getInventoryPurcaseTransaction() {
		return inventoryPurcaseTransaction;
	}

	public void setInventoryPurcaseTransaction(InventoryPurchaseTransaction inventoryPurcaseTransaction) {
		this.inventoryPurcaseTransaction = inventoryPurcaseTransaction;
	}

	public VanPurchaseTransaction getVanPurchaseTransaction() {
		return vanPurchaseTransaction;
	}

	public void setVanPurchaseTransaction(VanPurchaseTransaction vanPurchaseTransaction) {
		this.vanPurchaseTransaction = vanPurchaseTransaction;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
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


	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	private PdfPTable generateHeader() {
		PdfPTable headTable = new PdfPTable(2);
		InventoryPurchaseTransaction inventoryPurcaseTransaction = getInventoryPurcaseTransaction();
		VanPurchaseTransaction vanPurchaseTransaction = getVanPurchaseTransaction();
		
		String invoiceId = isInventoryTransaction() ? inventoryPurcaseTransaction.getInvoiceId() : vanPurchaseTransaction.getInvoiceId();
		System.out.println("invoiceId :: " + invoiceId);
		String supplierCustomerId = (isInventoryTransaction() && invoiceId != null) ?
				inventoryPurcaseTransaction.getSupplierId(): vanPurchaseTransaction.getCustomerName();
		String cusotmerMob = isInventoryTransaction() ? "" : vanPurchaseTransaction.getCustomerMob();
		Date transactionDate = isInventoryTransaction() ? inventoryPurcaseTransaction.getTransactionDate(): vanPurchaseTransaction.getTransactionDate();
		String formattedTransactionDate = GroceryUtil.formatDateToString(transactionDate, "dd/MM/yyyy hh:mm:ss", "Asia/Kolkata");
		
		headTable.addCell(geHeadFootCell("BILL TO",Element.ALIGN_LEFT, new Font(FontFamily.HELVETICA, 12, Font.NORMAL, new BaseColor(0, 0, 0))));
		headTable.addCell(geHeadFootCell(GroceryUtil.clearPrefix(invoiceId),Element.ALIGN_RIGHT, new Font(FontFamily.HELVETICA, 12, Font.NORMAL, new BaseColor(0, 0, 0))));
		
		headTable.addCell(geHeadFootCell(supplierCustomerId,Element.ALIGN_LEFT, new Font(FontFamily.HELVETICA, 10, Font.BOLD, new BaseColor(0, 0, 0))));
		headTable.addCell(geHeadFootCell("",Element.ALIGN_RIGHT, new Font(FontFamily.HELVETICA, 10, Font.NORMAL, new BaseColor(0, 0, 0))));
		
		headTable.addCell(geHeadFootCell(cusotmerMob,Element.ALIGN_RIGHT, new Font(FontFamily.HELVETICA, 8, Font.BOLD, new BaseColor(0, 0, 0))));
		headTable.addCell(geHeadFootCell(formattedTransactionDate,Element.ALIGN_RIGHT, new Font(FontFamily.HELVETICA, 8, Font.NORMAL, new BaseColor(0, 0, 0))));
		
		headTable.addCell(getDividerCell("", Element.ALIGN_CENTER, tableHeadFootFont));
		headTable.addCell(getDividerCell("", Element.ALIGN_CENTER, tableHeadFootFont));
		return headTable;
	}
	
	private PdfPTable generateFooter() {
		PdfPTable footTable = new PdfPTable(1);
		footTable.addCell(geHeadFootCell("TERMS AND CONDITIONS", Element.ALIGN_LEFT, new Font(FontFamily.HELVETICA, 8, Font.NORMAL, new BaseColor(0, 0, 0))));
		 footTable.addCell(geHeadFootCell("1. Goods once sold will not be taken back or exchanged", Element.ALIGN_LEFT, new Font(FontFamily.HELVETICA, 6, Font.NORMAL, new BaseColor(0, 0, 0))));
		 footTable.addCell(geHeadFootCell("Grocery on Wheel", Element.ALIGN_RIGHT, new Font(FontFamily.HELVETICA, 9, Font.BOLD, new BaseColor(0, 0, 0))));
		 footTable.addCell(geHeadFootCell("+919050092092", Element.ALIGN_RIGHT, new Font(FontFamily.HELVETICA, 8, Font.NORMAL, new BaseColor(0, 0, 0))));
		 return footTable;
	}
	
	private double getRate(PurchaseTransactionItem purchaseTransItem) {
		double rate = 0.0;
		if(DEBIT.equals(getTransactionType())) {
			if(isInventoryTransaction()) {
				if("REFUND".equals(getTransactionStatus())) {
					rate = Double.parseDouble(purchaseTransItem.getCp()) / 100;
				} else {
					rate = Double.parseDouble(purchaseTransItem.getSp()) / 100;
				}
			} else {
				rate = Double.parseDouble(purchaseTransItem.getSp()) / 100;
			}
		} else {
			if(isInventoryTransaction()) {
				rate = Double.parseDouble(purchaseTransItem.getCp()) / 100;
			} else {
				if("REFUND".equals(getTransactionStatus())) {
					rate = Double.parseDouble(purchaseTransItem.getMrp()) / 100;
				} else {
					rate = Double.parseDouble(purchaseTransItem.getSp()) / 100;
				}
			}
		}
		System.out.println("transaction type : " + getTransactionType() 
		+ " transaction status: " + getTransactionStatus()
		+ " inventory " + isInventoryTransaction() 
		+ " mrp " + purchaseTransItem.getMrp() + " sp " + purchaseTransItem.getSp()
		+ " cp " + purchaseTransItem.getCp() + " final rate " + rate);
		LOGGER.debug("transaction type : " + getTransactionType() 
		+ " transaction status: " + getTransactionStatus()
		+ " inventory " + isInventoryTransaction() 
		+ " mrp " + purchaseTransItem.getMrp() + " sp " + purchaseTransItem.getSp()
		+ " cp " + purchaseTransItem.getCp() + " final rate " + rate);
		return rate;
	}
	
	private PdfPTable generateInvoice(List<PurchaseTransactionItem> purchaseTransItems) {
		PdfPTable bodyTable = null;
		double subTotal = 0;
		int itemCount = 0;
		double totalMRP = 0;
		double totalRate = 0;
		String percentage = "";
		if(purchaseTransItems != null && purchaseTransItems.size() > 0) {
			purchaseTransItems = purchaseTransItems.stream().filter(t -> t.getItemCount() > 0).collect(Collectors.toList());
			if(DEBIT.equals(getTransactionType())) {
				bodyTable = new PdfPTable(6);
			} else {
				bodyTable = new PdfPTable(5);
			}

			bodyTable.addCell(getCell("ITEMS", Element.ALIGN_LEFT, tableHeadFootFont));
			bodyTable.addCell(getCell("QTY", Element.ALIGN_RIGHT, tableHeadFootFont));
			bodyTable.addCell(getCell("MRP", Element.ALIGN_RIGHT, tableHeadFootFont));
			bodyTable.addCell(getCell("RATE", Element.ALIGN_RIGHT, tableHeadFootFont));
			if(DEBIT.equals(getTransactionType())) {
				bodyTable.addCell(getCell("DISC.", Element.ALIGN_RIGHT, tableHeadFootFont));
			}
			bodyTable.addCell(getCell("AMOUNT", Element.ALIGN_RIGHT, tableHeadFootFont));
			List<BillRowData> billRowDataList = new ArrayList<>();
			for(PurchaseTransactionItem purchaseTransItem: purchaseTransItems) {
				BillRowData billRowData = new BillRowData();
				double rate = 0;
				double mrpd = Double.parseDouble(purchaseTransItem.getMrp());
				totalMRP += (mrpd * purchaseTransItem.getItemCount());
				double spd = Double.parseDouble(purchaseTransItem.getSp());
				rate = getRate(purchaseTransItem);
				System.out.println("rate " + rate + " mrp " + mrpd);
				totalRate += (rate * 100 * purchaseTransItem.getItemCount());
				percentage = GroceryUtil.calcPercentage(mrpd, spd);
				
				double amount = purchaseTransItem.getItemCount() * rate;
				subTotal += amount;
				itemCount += purchaseTransItem.getItemCount();
				
				billRowData.setItemName(purchaseTransItem.getItemName());
				billRowData.setItemCount(purchaseTransItem.getItemCount());
				billRowData.setMrp((Double.parseDouble(purchaseTransItem.getMrp()) / 100) + "");
				billRowData.setRate(rate + "");
				if(DEBIT.equals(getTransactionType())) {
					billRowData.setPercentage(percentage);
				}
				billRowData.setAmount(amount);
				billRowDataList.add(billRowData);
			}
			setBodyTableRow(bodyTable, billRowDataList);
			System.out.println("totalRate " + totalRate + " totalMRP " + totalMRP);
			generateTotalCalculation(bodyTable, itemCount, totalMRP, totalRate, subTotal);
		}
		return bodyTable;
	}
	
	private void setBodyTableRow(PdfPTable bodyTable, List<BillRowData> billRowDataList) {
		billRowDataList = GroceryUtil.groupBillRowDataByName(billRowDataList);
		for(BillRowData rowData: billRowDataList) {
			bodyTable.addCell(getCell(rowData.getItemName(), Element.ALIGN_LEFT, tableItemFont));
			bodyTable.addCell(getCell(rowData.getItemCount() + "", Element.ALIGN_RIGHT, tableItemFont));
			bodyTable.addCell(getCell(rowData.getMrp(), 
						Element.ALIGN_RIGHT, tableItemFont));
			bodyTable.addCell(getCell(rowData.getRate(), Element.ALIGN_RIGHT, tableItemFont));
			if(DEBIT.equals(getTransactionType())) {
				bodyTable.addCell(getCell(rowData.getPercentage() + "%", Element.ALIGN_RIGHT, tableItemFont));
			}
			bodyTable.addCell(getCell(GroceryUtil.format2Decimal(rowData.getAmount()) + "", Element.ALIGN_RIGHT, tableItemFont));
		}
	}
	
	private void generateTotalCalculation(PdfPTable bodyTable, int itemCount, double totalMRP, double totalRate,
			double subTotal) {
		bodyTable.addCell(getCell("SUB TOTAL", Element.ALIGN_LEFT, tableHeadFootFont));
		bodyTable.addCell(getCell(itemCount+"", Element.ALIGN_RIGHT, tableHeadFootFont));
		
		bodyTable.addCell(getCell((totalMRP / 100) + "", Element.ALIGN_RIGHT, tableHeadFootFont));
		if(DEBIT.equals(getTransactionType())) {
			bodyTable.addCell(getCell((totalRate / 100) + "", Element.ALIGN_RIGHT, tableHeadFootFont));
			bodyTable.addCell(getCell(GroceryUtil.calcPercentage(totalMRP, totalRate) + "%", Element.ALIGN_RIGHT, tableHeadFootFont));
			bodyTable.addCell(getCell(subTotal+"", Element.ALIGN_RIGHT, tableHeadFootFont));
		} else {
			bodyTable.addCell(getCell("", Element.ALIGN_RIGHT, tableHeadFootFont));
			bodyTable.addCell(getCell(subTotal+"", Element.ALIGN_RIGHT, tableHeadFootFont));
		}
		
		int noOfCols = bodyTable.getNumberOfColumns();
		for(int i = 0; i < noOfCols; i++) {
			bodyTable.addCell(getDividerCell("", Element.ALIGN_CENTER, tableHeadFootFont));
		}
		
		PdfPCell colSpan = new PdfPCell();
		colSpan.setColspan(noOfCols - 3); // 2 for "GRAND TOTAL" label, and 1 for subTotal
		colSpan.setBorder(PdfPCell.NO_BORDER);
		bodyTable.addCell(colSpan);
		PdfPCell grandTotalLabelCell = getCell("GRAND TOTAL", Element.ALIGN_CENTER, tableHeadFootFont);
		grandTotalLabelCell.setColspan(2);
		bodyTable.addCell(grandTotalLabelCell);
		bodyTable.addCell(getCell(subTotal+"", Element.ALIGN_RIGHT, tableHeadFootFont));
	}
	
	public String generatePDFInvoice(Invoice invoice,  AmazonClient s3Client,
			S3Configuration s3Config) {
		String invoiceUrl = "";
		boolean upload = true; //only for local debugging purpose set it to false
		if(invoice != null) {
			InventoryPurchaseTransaction inventoryTransactionInvoice = null;
			VanPurchaseTransaction vanTransactionInvoice = null;
			boolean isInventory = false;
			if(invoice instanceof InventoryPurchaseTransaction) {
				isInventory = true;
				inventoryTransactionInvoice = (InventoryPurchaseTransaction) invoice;
			} else if(invoice instanceof VanPurchaseTransaction) {
				isInventory = false;
				vanTransactionInvoice = (VanPurchaseTransaction) invoice;
			}
			setTransactionType(isInventory ? inventoryTransactionInvoice.getTransactionType() 
					: vanTransactionInvoice.getTransactionType());
			setInventoryTransaction(isInventory);
			setInventoryPurcaseTransaction(inventoryTransactionInvoice);
			setVanPurchaseTransaction(vanTransactionInvoice);
			setTransactionStatus(isInventory ? inventoryTransactionInvoice.getTransactionStatus()
					: vanTransactionInvoice.getTransactionStatus());
			
			PdfPTable headTable = generateHeader();
			PdfPTable bodyTable = generateInvoice(isInventory ? inventoryTransactionInvoice.getPurchaseTransItem() 
					: vanTransactionInvoice.getPurchaseTransItem());
			PdfPTable footTable = generateFooter();
			
			List<Paragraph> pdfFootTextList = new ArrayList<>();
			List<Paragraph> pdfHeadTextList = new ArrayList<>();
			
			  File tempPdfFile = GroceryUtil.createAndUpload(pdfHeadTextList,
					  pdfFootTextList, invoice.getInvoiceId(), headTable, bodyTable, footTable);
			  if(upload) { 
				  invoiceUrl = GroceryUtil.uploadFile(tempPdfFile, s3Config,  s3Client); 
			  } 
			  if(tempPdfFile != null) {
				  if(upload) { tempPdfFile.delete();}
				  else { invoiceUrl = tempPdfFile.getAbsolutePath(); } 
			  }
		}
		return invoiceUrl;
	}
}
