package com.grocery.on.wheels.model;

public class InvoiceCorrection {
	private String invVanId;
	private String itemPriceId;
	private String itemExpId;
	private int correctionCount;
	private String invoiceId;
	private String transactor;
	public String getInvVanId() {
		return invVanId;
	}
	public void setInvVanId(String invVanId) {
		this.invVanId = invVanId;
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
	public int getCorrectionCount() {
		return correctionCount;
	}
	public void setCorrectionCount(int correctionCount) {
		this.correctionCount = correctionCount;
	}
	public String getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}
	public String getTransactor() {
		return transactor;
	}
	public void setTransactor(String transactor) {
		this.transactor = transactor;
	}
}
