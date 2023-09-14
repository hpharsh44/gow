package com.grocery.on.wheels.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.grocery.on.wheels.model.InventoryPurchaseTransaction;
import com.grocery.on.wheels.model.Invoice;
import com.grocery.on.wheels.model.PurchaseTransactionItem;
import com.grocery.on.wheels.model.VanPurchaseTransaction;

public class PrinterUtil {
	/*
	 "[C]BILL OF SUPPLY\n[C]GROCERY ON WHEELS\n
	 [C] shop no. 208, c/o ASHISH SINGLA, JAGJIWAN NAGAR SHRI, HISAR, Hisar, Haryana, 125001, Haryana.\n
	 \n
	 [L]GSTIN: 06AAGCI0630K1ZR[R]Mobile: 9050092092\n
	 [C]============================================\n
	 [L]INV No: INVENTORY_20230413011512[R]Date: Thu Apr 13 13:15:12 UTC 2023\n
	 [L]Bill To: DEBIT\n[L]\n[C]============================================\n
	 [L]\n
	 [L]Qty[L]MRP[R]RATE[R]Amount\n
	 [C]============================================\n
	 [L]1 VIM BAR SMALL 100 Gm\n
	 \n
	 [L]1[L]3000[C][R]29.0[R]29.0\n
	 [C]--------------------------------------------\n
	 [L]Subtotal: [R]29.0\n
	 [C]--------------------------------------------\n
	 [C]--------------------------------------------\n
	 [L]Total Amount: [R]29.0\n
	 [L]Amount Paid: [R]29.0\n
	 [L]Balance Amount: [R]0.00\n
	 [C]--------------------------------\n
	 [C]Terms and Condition \n
	 [L] 1. Goods once sold will not be taken back or exchanged.\n
	 [L] 2. All disputes are subject to [ENTER_YOUR_CITY_NAME] juridisction only. \n
	 \n
	 [C] Thank you for your purchase\n"
	 */
	
	private static final String LEFT = "[L]";
	private static final String RIGHT = "[R]";
	private static final String CENTER = "[C]";
	private static final String NEW_LINE = "\n";
	private static final String LOGO_HEADER = "[C] BILL OF SUPPLY \n[L][C]GROCERY ON WHEELS[R] \n"
			+ "[C] Shop Number 210-211 Basement ,New Rishi Nagar, near Dhobi Ghat, Hisar, Haryana ,125001\n\n";
	
	
	private String getLineText(String alignment, String text, boolean newLine) {
		String appendedText = alignment + text; 
		if(newLine) {
			appendedText += NEW_LINE;
		}
		return appendedText;
	}
	
	private String getLineSeparateor() {
		return "[L]\n";
	}
	
	private String getDivider() {
		return "[C]============================================\n";
	}
	
	private String getInvoiceNo(Invoice invoice) {
		return "[C]<u><font size='big'>"+invoice.getInvoiceId()+"</font></u>\n";
	}
	
	private String getOrderDate(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:SS");
		if(date != null) {
			return "[C]<u type='double'>on " + dateFormat.format(date) + " at " + timeFormat.format(date)  + "</u>\n";
		} 
		return getLineSeparateor();
	}
	
	private String getProductLabel(String productName, String price) {
		return "[L]<b>"+productName+"</b>[R]Rs "+ price +"\n";
	}
	
	private String getProductSubLabel(String subLabeltext) {
		return "[L]  + "+ subLabeltext +"\n";
	}
	
	private String getProductFooterDivider() {
		return "[C]--------------------------------------------\n";     
	}
	
	private String getTotalDescription(String total, String tax) {
		return "[R]TOTAL PRICE :[R]Rs "+ total +"\n" +
               "[R]DISCOUNT :[R]"+ tax +"\n" +
               "[L]\n";
	}
	
	private String getCustomerDetails(String name, String address1, String address2, String phone) {
		return "[L]<u><font color='bg-black' size='tall'>Customer :</font></u>\n"
				+ 
				"[L]"+ name +"\n" +
               "[L]" + address1+ "\n" +
               "[L]"+address2+"\n" +
               "[L]Tel : " + phone + "\n" +
               "\n";
	}
	
	private String getBarQRCode(String barcodeNo, String qrCodeUrl) {
		return "[C]<barcode type='ean13' height='10'>"+barcodeNo+"</barcode>\n" +
               "[L]\n" +
               "[C]<qrcode size='20'>" + qrCodeUrl + "</qrcode>\n";
	}
	
	private String getTNCFooter() {
		return "[L] Terms and Condition \n" +
				"[L] 1. Goods once sold will not be taken back or exchanged.\n" +
				"[L] 2. All disputes are subject to [ENTER_YOUR_CITY_NAME] juridisction only. \n\n" +
				"[C] Thank you for your purchase\n" ;
	}
	
	public String generatePrinterText(Invoice invoice) {
		StringBuffer printerText = new StringBuffer(LOGO_HEADER);
		if(invoice != null) {
			printerText.append(getLineText(LEFT, "GSTIN: 06AAGCI0630K1ZR", false) );
			printerText.append(getLineText(RIGHT, "Mobile: 9050092092", true) );
			printerText.append(getDivider());
			printerText.append(getLineText(LEFT, "INV No: " + GroceryUtil.clearPrefix(invoice.getInvoiceId()), false) );
			
			if(invoice instanceof InventoryPurchaseTransaction) {
				InventoryPurchaseTransaction transactionInvoice = (InventoryPurchaseTransaction) invoice;
				printerText.append(getLineText(RIGHT, "Date: " + transactionInvoice.getTransactionDate(), true) );
				
				printerText.append(getLineText(LEFT, "Bill To: " + transactionInvoice.getTransactionType(), true) );
			}
			if(invoice instanceof VanPurchaseTransaction) {
				VanPurchaseTransaction transactionInvoice = (VanPurchaseTransaction) invoice;
				printerText.append(getLineText(RIGHT, "Date: " + GroceryUtil.getFormatDate(transactionInvoice.getTransactionDate()), true) );
				printerText.append(getLineText(LEFT, "Bill To: " + transactionInvoice.getTransactionType(), true) );
			}
			printerText.append(getLineSeparateor());
			printerText.append(getDivider());
			printerText.append(getLineSeparateor());
			//printerText.append(getLineText(LEFT, "No.", false) );
			//printerText.append(getLineText(LEFT, "Item", false) );
			//printerText.append(getLineText(LEFT, "Description", true) );

			
			printerText.append(getLineText(LEFT, "Qty", false) );
			printerText.append(getLineText(LEFT, "MRP", false) );
			printerText.append(getLineText(RIGHT, "RATE", false) );
			printerText.append(getLineText(RIGHT, "AMT", true) );
			printerText.append(getDivider());

			double subTotal = 0;
			String percentage = "";
			String transType = "";
			double totalMRP = 0;
			double totalRate = 0;
			if(invoice instanceof InventoryPurchaseTransaction) {
				InventoryPurchaseTransaction transactionInvoice = (InventoryPurchaseTransaction) invoice;
				List<PurchaseTransactionItem> purchaseTransItems = transactionInvoice.getPurchaseTransItem();
				if(purchaseTransItems != null && purchaseTransItems.size() > 0) {
					int itemCount = 0;
					transType = transactionInvoice.getTransactionType();
					List<BillRowData> billRowDataList = new ArrayList<>();
					for(PurchaseTransactionItem purchaseTransItem: purchaseTransItems) {
						double rate = 0;

						if("DEBIT".equals(transactionInvoice.getTransactionType())) {
							double mrpd = Double.parseDouble(purchaseTransItem.getMrp());
							totalMRP += (mrpd * purchaseTransItem.getItemCount());
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

						billRowDataList.add(getBillRowData(purchaseTransItem, amount, rate));
						
						/*printerText.append(getLineText(LEFT, (++seq) + " " + purchaseTransItem.getItemName() + " " + purchaseTransItem.getItemDesc(), true) );
						
						printerText.append(getLineText(LEFT, purchaseTransItem.getItemCount() + "", false) );
						printerText.append(getLineText(LEFT, 
								(purchaseTransItem.getMrp() == null) ? "" : GroceryUtil.format2Decimal(
										Double.parseDouble(purchaseTransItem.getMrp()) / 100)
								, false) );
						printerText.append(getLineText(CENTER, "", false) );
						printerText.append(getLineText(RIGHT, rate + "", false) );
						printerText.append(getLineText(RIGHT, amount + "", true) );*/
					}
					appendRowPrinterText(printerText, billRowDataList);
					
				}
			} else if(invoice instanceof VanPurchaseTransaction) {
				VanPurchaseTransaction transactionInvoice = (VanPurchaseTransaction) invoice;
				List<PurchaseTransactionItem> purchaseTransItems = transactionInvoice.getPurchaseTransItem();
				if(purchaseTransItems != null && purchaseTransItems.size() > 0) {
					transType = transactionInvoice.getTransactionType();
					List<BillRowData> billRowDataList = new ArrayList<>();
					for(PurchaseTransactionItem purchaseTransItem: purchaseTransItems) {
						double rate = 0;
						if("DEBIT".equals(transactionInvoice.getTransactionType())) {
							double mrpd = Double.parseDouble(purchaseTransItem.getMrp());
							double spd = Double.parseDouble(purchaseTransItem.getSp());
							totalMRP += (mrpd * purchaseTransItem.getItemCount());
							totalRate += (spd * purchaseTransItem.getItemCount());
							rate = spd / 100;
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
						billRowDataList.add(getBillRowData(purchaseTransItem, amount, rate));
						/*printerText.append(getLineText(LEFT, (++seq) + " " + purchaseTransItem.getItemName() + " " + purchaseTransItem.getItemDesc(), true) );
						
						printerText.append(getLineText(LEFT, purchaseTransItem.getItemCount() + "", false) );
						printerText.append(getLineText(LEFT, 
								(purchaseTransItem.getMrp() == null) ? "" : GroceryUtil.format2Decimal(
										Double.parseDouble(purchaseTransItem.getMrp()) / 100)
								, false) );
						printerText.append(getLineText(CENTER, "", false) );
						printerText.append(getLineText(RIGHT, rate + "", false) );
						printerText.append(getLineText(RIGHT, amount + "", true) );
						*/
					
					}
					appendRowPrinterText(printerText, billRowDataList);
				}
			}
			printerText.append(getProductFooterDivider());
			
			printerText.append(getLineText(LEFT, "Subtotal: ", false));
			printerText.append(getLineText(RIGHT, subTotal + "", true));
			
			printerText.append(getProductFooterDivider());
			printerText.append(getProductFooterDivider());
			
			printerText.append(getLineText(LEFT, "Total Amount: ", false));
			printerText.append(getLineText(RIGHT, subTotal + "", true));
			
			printerText.append(getLineText(LEFT, "Amount Paid: ", false));
			printerText.append(getLineText(RIGHT, subTotal + "", true));
			
			printerText.append(getLineText(LEFT, "Balance Amount: ", false));
			printerText.append(getLineText(RIGHT, "0.00", true));
			
			if("DEBIT".equals(transType)) {
				printerText.append(getLineText(LEFT, "Discount: ", false));
				printerText.append(getLineText(RIGHT, GroceryUtil.calcPercentage(totalMRP, totalRate) + "%", true));
			}
			
			printerText.append(getProductFooterDivider());
			printerText.append(getTNCFooter());
		}
		return printerText.toString();
	}
	
	private BillRowData getBillRowData(PurchaseTransactionItem item, double amount,  double rate) {
		BillRowData rowData = new BillRowData();
		rowData.setItemName(item.getItemName());
		rowData.setItemDesc(item.getItemDesc());
		rowData.setMrp((item.getMrp() == null) ? "" : GroceryUtil.format2Decimal(
				Double.parseDouble(item.getMrp()) / 100));
		rowData.setItemCount(item.getItemCount());
		rowData.setAmount(amount);
		rowData.setRate(rate + "");
		return rowData;
	}
	
	private void appendRowPrinterText(StringBuffer printerText, 
			List<BillRowData> billRowDataList) {
		billRowDataList = GroceryUtil.groupBillRowDataByName(billRowDataList);
		int seq = 0;
		for(BillRowData rowData: billRowDataList) {
			printerText.append(getLineText(LEFT, (++seq) + " " + rowData.getItemName() 
			+ " " + rowData.getItemDesc(), true) );
			
			printerText.append(getLineText(LEFT, rowData.getItemCount() + "", false) );
			printerText.append(getLineText(LEFT, 
					rowData.getMrp()
					, false) );
			printerText.append(getLineText(CENTER, "", false) );
			printerText.append(getLineText(RIGHT, rowData.getRate() + "", false) );
			printerText.append(getLineText(RIGHT, rowData.getAmount() + "", true) );
		}
		
	}
}
