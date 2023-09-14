package com.grocery.on.wheels.model;

public class InvoiceReport {
	String invoiceId;
	String inventoryVanId;
	int totalMrp;
	int totalCp;
	int totalSp;
    int totalQuantity;
    int totalPurchasePending;
    int totalSsalePending;
    String supplierCustId;
    String transactionType;
    String transactionStatus;
    String transactionDate;
    String transactionMode;
    String transactor;
	public String getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}
	public String getInventoryVanId() {
		return inventoryVanId;
	}
	public void setInventoryVanId(String inventoryVanId) {
		this.inventoryVanId = inventoryVanId;
	}
	public int getTotalMrp() {
		return totalMrp;
	}
	public void setTotalMrp(int totalMrp) {
		this.totalMrp = totalMrp;
	}
	public int getTotalQuantity() {
		return totalQuantity;
	}
	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	public int getTotalPurchasePending() {
		return totalPurchasePending;
	}
	public void setTotalPurchasePending(int totalPurchasePending) {
		this.totalPurchasePending = totalPurchasePending;
	}
	public int getTotalSsalePending() {
		return totalSsalePending;
	}
	public void setTotalSsalePending(int totalSsalePending) {
		this.totalSsalePending = totalSsalePending;
	}
	public String getSupplierCustId() {
		return supplierCustId;
	}
	public void setSupplierCustId(String supplierCustId) {
		this.supplierCustId = supplierCustId;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getTransactionStatus() {
		return transactionStatus;
	}
	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getTransactionMode() {
		return transactionMode;
	}
	public void setTransactionMode(String transactionMode) {
		this.transactionMode = transactionMode;
	}
	public int getTotalCp() {
		return totalCp;
	}
	public void setTotalCp(int totalCp) {
		this.totalCp = totalCp;
	}
	public int getTotalSp() {
		return totalSp;
	}
	public void setTotalSp(int totalSp) {
		this.totalSp = totalSp;
	}
	public String getTransactor() {
		return transactor;
	}
	public void setTransactor(String transactor) {
		this.transactor = transactor;
	}
}
