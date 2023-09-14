package com.grocery.on.wheels.model;

import java.util.Date;

public class ItemReport {
	private String invoiceId;
	private String inventoryVanId;
	private String itemId;
	private String mrp;
	private String sp;
	private String cp;
	private Date itemExpDt;
	private String supplierCustId;
	private String quantity;
	private String transactionType;
	private String transactionStatus;
	private Date transactionDate; 
	private int salePending;
	private int purchasePending;
	private String transactionMode;
	private String itemName;
	private String itemPriceId;
	private String itemExpId;
	private String transactor;
	
	public String getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getMrp() {
		return mrp;
	}
	public void setMrp(String mrp) {
		this.mrp = mrp;
	}
	public Date getItemExpDt() {
		return itemExpDt;
	}
	public void setItemExpDt(Date itemExpDt) {
		this.itemExpDt = itemExpDt;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
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
	public int getSalePending() {
		return salePending;
	}
	public void setSalePending(int salePending) {
		this.salePending = salePending;
	}
	public int getPurchasePending() {
		return purchasePending;
	}
	public void setPurchasePending(int purchasePending) {
		this.purchasePending = purchasePending;
	}
	public String getInventoryVanId() {
		return inventoryVanId;
	}
	public void setInventoryVanId(String inventoryVanId) {
		this.inventoryVanId = inventoryVanId;
	}
	public String getSupplierCustId() {
		return supplierCustId;
	}
	public void setSupplierCustId(String supplierCustId) {
		this.supplierCustId = supplierCustId;
	}
	public String getTransactionMode() {
		return transactionMode;
	}
	public void setTransactionMode(String transactionMode) {
		this.transactionMode = transactionMode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getSp() {
		return sp;
	}
	public void setSp(String sp) {
		this.sp = sp;
	}
	public String getCp() {
		return cp;
	}
	public void setCp(String cp) {
		this.cp = cp;
	}
	public String getItemPriceId() {
		return itemPriceId;
	}
	public void setItemPriceId(String itemPriceId) {
		this.itemPriceId = itemPriceId;
	}
	public String getItemExpId() {
		return itemExpId;
	}
	public void setItemExpId(String itemExpId) {
		this.itemExpId = itemExpId;
	}
	public String getTransactor() {
		return transactor;
	}
	public void setTransactor(String transactor) {
		this.transactor = transactor;
	}
	
}
