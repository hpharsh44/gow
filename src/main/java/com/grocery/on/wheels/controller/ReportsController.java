package com.grocery.on.wheels.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grocery.on.wheels.model.InvoiceReport;
import com.grocery.on.wheels.model.ItemReport;
import com.grocery.on.wheels.service.ReportService;

@RestController
@RequestMapping("/grocery/reports")

public class ReportsController {
	
	@Autowired
	ReportService reportService;
	
	@GetMapping("/inventory")
	public List<ItemReport> getInventoryReports(@RequestParam("inventory_id") String inventoryId,
			@RequestParam("from_date") String fromDate, @RequestParam("to_date") String toDate) {
		return reportService.getInventoryReports(inventoryId, fromDate, toDate);
	}
	
	@GetMapping("/van")
	public List<ItemReport> getVanReports(@RequestParam("van_id") String vanId,
			@RequestParam("from_date") String fromDate, @RequestParam("to_date") String toDate) {
		return reportService.getVanReports(vanId, fromDate, toDate);
	}
	
	@GetMapping("/inventory/invoice")
	public List<InvoiceReport> getInventoryInvoiceList(@RequestParam("inventory_id") String inventoryId,
			@RequestParam("from_date") String fromDate, @RequestParam("to_date") String toDate) {
		return reportService.getInventoryInvoiceList(inventoryId, fromDate, toDate);
	}
	
	@GetMapping("/van/invoice")
	public List<InvoiceReport> getVanInvoiceList(@RequestParam("van_id") String vanId,
			@RequestParam("from_date") String fromDate, @RequestParam("to_date") String toDate) {
		return reportService.getVanInvoiceList(vanId, fromDate, toDate);
	}
	
	@GetMapping("/inventory/{invoice_id}")
	public List<ItemReport> getInventoryInvoiceReport(@RequestParam("inventory_id") String inventoryId,
			@PathVariable("invoice_id") String invoiceId) {
		return reportService.getInventoryInvoiceReport(inventoryId, invoiceId);
	}
	
	@GetMapping("/van/{invoice_id}")
	public List<ItemReport> getVanInvoiceReport(@RequestParam("van_id") String vanId, 
			@PathVariable("invoice_id") String invoiceId) {
		return reportService.getVanInvoiceReport(vanId, invoiceId);
	}
}
