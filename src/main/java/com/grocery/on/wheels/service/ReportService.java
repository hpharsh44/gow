package com.grocery.on.wheels.service;

import java.util.List;

import com.grocery.on.wheels.model.InvoiceReport;
import com.grocery.on.wheels.model.ItemReport;

public interface ReportService {

	List<ItemReport> getInventoryReports(String inventoryId, String fromDate, String toDate);

	List<ItemReport> getVanReports(String vanId, String fromDate, String toDate);

	List<InvoiceReport> getInventoryInvoiceList(String inventoryId, String fromDate, String toDate);

	List<InvoiceReport> getVanInvoiceList(String vanId, String fromDate, String toDate);

	List<ItemReport> getInventoryInvoiceReport(String inventoryId, String invoiceId);

	List<ItemReport> getVanInvoiceReport(String vanId, String invoiceId);

	
	
}
