package com.grocery.on.wheels.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grocery.on.wheels.dao.ReportMapper;
import com.grocery.on.wheels.model.InvoiceReport;
import com.grocery.on.wheels.model.ItemReport;
import com.grocery.on.wheels.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	ReportMapper reportMapper;
	
	@Override
	public List<ItemReport> getInventoryReports(String inventoryId, String fromDate, String toDate) {
		return reportMapper.getInventoryReports(inventoryId, fromDate, toDate);
	}

	@Override
	public List<ItemReport> getVanReports(String vanId, String fromDate, String toDate) {
		return reportMapper.getVanReports(vanId, fromDate, toDate);
	}

	@Override
	public List<InvoiceReport> getInventoryInvoiceList(String inventoryId, String fromDate, String toDate) {
		return reportMapper.getInventoryInvoiceList(inventoryId, fromDate, toDate);
	}

	@Override
	public List<InvoiceReport> getVanInvoiceList(String vanId, String fromDate, String toDate) {
		return reportMapper.getVanInvoiceList(vanId, fromDate, toDate);
	}

	@Override
	public List<ItemReport> getInventoryInvoiceReport(String inventoryId,
			String invoiceId) {
		return reportMapper.getInventoryInvoiceReport(inventoryId, invoiceId);
	}

	@Override
	public List<ItemReport> getVanInvoiceReport(String vanId, String invoiceId) {
		return reportMapper.getVanInvoiceReport(vanId, invoiceId);
	}

}
