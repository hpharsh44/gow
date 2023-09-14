package com.grocery.on.wheels.model;

public class ItemPending {
	private String invoiceId;
	private int purchasePending;
	private int salePending;
	private String transactionStatus;
	public String getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}
	public int getPurchasePending() {
		return purchasePending;
	}
	public void setPurchasePending(int purchasePending) {
		this.purchasePending = purchasePending;
	}
	public int getSalePending() {
		return salePending;
	}
	public void setSalePending(int salePending) {
		this.salePending = salePending;
	}
	public String getTransactionStatus() {
		return transactionStatus;
	}
	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
}
