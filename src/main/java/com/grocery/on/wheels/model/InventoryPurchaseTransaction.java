package com.grocery.on.wheels.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class InventoryPurchaseTransaction extends Invoice implements ResponseObject {
	private String inventoryId;
	private List<PurchaseTransactionItem> purchaseTransItem;
	private String supplierId;
	private String discount;
	private String otherCharges;
	private String transactionType;//CREDIT/DEBIT
	private String transactionStatus;//INVOICE/CANCEL/REFUND
	private Date transactionDate;
	private String customerName;
	private String customerMob;
	private String transactionMode;//UPI/CASH/CARD
	private String latitude;
	private String longitude;
	
	public String getInventoryId() {
		return inventoryId;
	}
	public void setInventoryId(String inventoryId) {
		this.inventoryId = inventoryId;
	}
	public List<PurchaseTransactionItem> getPurchaseTransItem() {
		return purchaseTransItem;
	}
	public void setPurchaseTransItem(List<PurchaseTransactionItem> purchaseTransItem) {
		this.purchaseTransItem = purchaseTransItem;
	}
	public String getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
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
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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
	public String getTransactionMode() {
		return transactionMode;
	}
	public void setTransactionMode(String transactionMode) {
		this.transactionMode = transactionMode;
	}
	public String getCustomerMob() {
		return customerMob;
	}
	public void setCustomerMob(String customerMob) {
		this.customerMob = customerMob;
	}
	
}
