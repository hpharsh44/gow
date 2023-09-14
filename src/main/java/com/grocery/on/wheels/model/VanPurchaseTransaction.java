package com.grocery.on.wheels.model;

import java.util.Date;
import java.util.List;

public class VanPurchaseTransaction extends Invoice implements ResponseObject {
	private String inventoryId;
	private String vanId;
	private List<PurchaseTransactionItem> purchaseTransItem;
	private String customerId;
	private String customerName;
	private String customerMob;
	private String discount;
	private String otherCharges;
	private String transactionType;//CREDIT/DEBIT
	private String transactionStatus;//INVOICE/CANCEL/REFUND
	private Date transactionDate;
	private String transactionMode;//UPI/CASH/CARD
	private String latitude;
	private String longitude;
	private String transactor;

	/*
	 * public String getInvoiceId() { return invoiceId; } public void
	 * setInvoiceId(String invoiceId) { this.invoiceId = invoiceId; }
	 */
	public String getVanId() {
		return vanId;
	}
	public void setVanId(String vanId) {
		this.vanId = vanId;
	}

	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getOtherCharges() {
		return otherCharges;
	}
	public void setOtherCharges(String otherCharges) {
		this.otherCharges = otherCharges;
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
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	/*
	 * public String getInvoiceName() { return invoiceName; } public void
	 * setInvoiceName(String invoiceName) { this.invoiceName = invoiceName; }
	 */
	public String getInventoryId() {
		return inventoryId;
	}
	public void setInventoryId(String inventoryId) {
		this.inventoryId = inventoryId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public List<PurchaseTransactionItem> getPurchaseTransItem() {
		return purchaseTransItem;
	}
	public void setPurchaseTransItem(List<PurchaseTransactionItem> purchaseTransItem) {
		this.purchaseTransItem = purchaseTransItem;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerMob() {
		return customerMob;
	}
	public void setCustomerMob(String customerMob) {
		this.customerMob = customerMob;
	}
	public String getTransactionMode() {
		return transactionMode;
	}
	public void setTransactionMode(String transactionMode) {
		this.transactionMode = transactionMode;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getTransactor() {
		return transactor;
	}
	public void setTransactor(String transactor) {
		this.transactor = transactor;
	}
}
