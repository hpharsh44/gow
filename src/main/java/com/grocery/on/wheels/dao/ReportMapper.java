package com.grocery.on.wheels.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.grocery.on.wheels.model.InvoiceReport;
import com.grocery.on.wheels.model.ItemReport;

public interface ReportMapper {
	List<ItemReport> getInventoryReports(@Param("inventoryId") String inventoryId, @Param("fromDate") String fromDate,
			@Param("toDate") String toDate);

	List<ItemReport> getVanReports(@Param("vanId") String vanId, @Param("fromDate") String fromDate, 
			@Param("toDate") String toDate);

	List<InvoiceReport> getInventoryInvoiceList(@Param("inventoryId") String inventoryId, @Param("fromDate") String fromDate,
			@Param("toDate") String toDate);

	List<InvoiceReport> getVanInvoiceList(@Param("vanId") String vanId, @Param("fromDate") String fromDate, 
			@Param("toDate") String toDate);

	List<ItemReport> getInventoryInvoiceReport(@Param("inventoryId") String inventoryId, 
			@Param("invoiceId") String invoiceId);

	List<ItemReport> getVanInvoiceReport(@Param("vanId") String vanId, 
			@Param("invoiceId") String invoiceId);

}
