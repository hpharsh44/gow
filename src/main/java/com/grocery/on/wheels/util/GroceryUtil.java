package com.grocery.on.wheels.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Collectors;

import com.grocery.on.wheels.model.PurchaseTransactionItem;
import com.grocery.on.wheels.s3.configuration.S3Configuration;
import com.grocery.on.wheels.s3.service.AmazonClient;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.util.Pair;  

public class GroceryUtil {
	private static final DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
	
	private static final DateFormat qrydf = new SimpleDateFormat("yyyy-MM-dd");
	
	public static String getQueryFormatDate(Date date) {
		return qrydf.format(date);
	}
	
	DateFormat gmtIstDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	public static String getFormatDate(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}
	
	
	public static String getFormatDate(Date date) {
		return df.format(date);
	}
	
	/*******************************************PDF 
	 * @param table **************************************/
	public static File createAndUpload(List<Paragraph> headText, List<Paragraph> footText, String invoiceId, 
			PdfPTable headTable, PdfPTable bodyTable, PdfPTable footTable)  
	{  
		
		File pdfFile = new File(invoiceId + ".pdf");
		Document doc = new Document();  
		headTable.setWidthPercentage(90);
		bodyTable.setWidthPercentage(90);
		footTable.setWidthPercentage(90);
		try  
		{  
			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(pdfFile));  
			doc.open();  
			for(Paragraph text: headText) {
				doc.add(text);
			}
			doc.add(headTable);
			doc.add(bodyTable);
			doc.add(footTable);
			for(Paragraph text: footText) {
				doc.add(text);
			}
			doc.close();  
			writer.close();  
		} catch (DocumentException e) {  
			e.printStackTrace();  
		} catch (FileNotFoundException e) {  
			e.printStackTrace();  
		}  
		return pdfFile;
	}  
	public static String uploadFile(File file, S3Configuration s3Config, AmazonClient s3Client) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Length", String.valueOf(file.length()));
        String path = String.format("%s/%s", s3Config.getBucketName(), UUID.randomUUID());
        String fileName = String.format("%s", file.getName());
        String savedFilePath = String.format("%s/%s", path, fileName);
        try {
        	s3Client.upload(path, fileName, Optional.of(metadata), new FileInputStream(file));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to upload file", e);
        }
        return savedFilePath;
	}
	
	public static String formatDateToString(Date date, String format,
			String timeZone) {//"GMT" , "Asia/Kolkata"
		if (date == null) return null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if (timeZone == null || "".equalsIgnoreCase(timeZone.trim())) {
			timeZone = Calendar.getInstance().getTimeZone().getID();
		}
		sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
		return sdf.format(date);
	}
	
	
	
	public Date stringToDate(String dateString) {
		try {
			return gmtIstDateFormat.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}				
		return null;
	}
	
	public String dateToString(Date date) {
		return gmtIstDateFormat.format(date);
	}
	
	public static String calcPercentage(double mrpd, double spd) {
		return format2Decimal((mrpd - spd) * 100 / mrpd);
	}
	
	public static String format2Decimal(double value) {
		 DecimalFormat df = new DecimalFormat("0.00");
		return df.format(value);
	}

	public static String clearPrefix(String text) {
		if(text == null) {
			return "";
		}
		return text.substring(text.indexOf("_") + 1, text.length());
	}
	
	
	public static List<BillRowData> groupBillRowDataByName(List<BillRowData> billRowDataList) {
		Map<BillRowData, BillRowData> pricePerNameAndRate = new HashMap<>();
		for(BillRowData data: billRowDataList) {
			BillRowData bill = pricePerNameAndRate.get(data);
			if(bill != null) {
				double amount = bill.getAmount() + data.getAmount();
				bill.setAmount(amount);
				int itemCnt = bill.getItemCount() + data.getItemCount();
				bill.setItemCount(itemCnt);
				pricePerNameAndRate.put(data, bill);
			} else {
				pricePerNameAndRate.put(data, data);
			}
			
		}
		List<BillRowData> groupedPurchaseTransItemList = new ArrayList<>();
		pricePerNameAndRate.forEach((k, v) ->groupedPurchaseTransItemList.add(v));
		return groupedPurchaseTransItemList;
		/*Map<String, BillRowData> pricePerName = 
				billRowDataList.stream().collect(
			Collectors.groupingBy( BillRowData::getItemName ,
					Collectors.collectingAndThen(Collectors.toList(), list -> {
						Optional<String> itemName = list.stream()
							      .map(BillRowData::getItemName).findAny();
						
						String itemCount = list.stream()
							      .map(BillRowData::getItemCount)
							      .collect(Collectors.joining(", "));
						String mrp = list.stream()
							      .map(BillRowData::getMrp)
							      .collect(Collectors.joining(", "));
						String rate = list.stream()
							      .map(BillRowData::getRate)
							      .collect(Collectors.joining(", "));
						String percentage = list.stream()
							      .map(BillRowData::getPercentage)
							      .collect(Collectors.joining(", "));
							
						double amount = list.stream()
							      .collect(Collectors.summingDouble(BillRowData::getAmount));
						
						BillRowData groupedItem = new BillRowData();
						groupedItem.setItemName(itemName.get());
						groupedItem.setItemCount(itemCount);
						groupedItem.setMrp(mrp);
						groupedItem.setRate(rate);
						groupedItem.setPercentage(percentage);
						groupedItem.setAmount(amount);
						return groupedItem;
					})
					));
		List<BillRowData> groupedPurchaseTransItemList = new ArrayList<>();
		pricePerName.forEach((k, v) ->groupedPurchaseTransItemList.add(v));
		return groupedPurchaseTransItemList;*/
	}
	
}
