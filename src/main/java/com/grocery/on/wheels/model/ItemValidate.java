package com.grocery.on.wheels.model;

public class ItemValidate implements ResponseObject {
	private String itemPriceId;
	//private String expDate;
	private int validateCount;
	private int refundCount;
	private String vanInvId;
	private String invoiceId;
	private String itemId;
	private String itemExpId;
	private String clearPending;
	public String getItemPriceId() {
		return itemPriceId;
	}
	public void setItemPriceId(String itemPriceId) {
		this.itemPriceId = itemPriceId;
	}

	/*
	 * public String getExpDate() { return expDate; } public void setExpDate(String
	 * expDate) { this.expDate = expDate; }
	 */
	public int getValidateCount() {
		return validateCount;
	}
	public void setValidateCount(int validateCount) {
		this.validateCount = validateCount;
	}
	public String getVanInvId() {
		return vanInvId;
	}
	public void setVanInvId(String vanInvId) {
		this.vanInvId = vanInvId;
	}
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
	public int getRefundCount() {
		return refundCount;
	}
	public void setRefundCount(int refundCount) {
		this.refundCount = refundCount;
	}
	public String getItemExpId() {
		return itemExpId;
	}
	public void setItemExpId(String itemExpId) {
		this.itemExpId = itemExpId;
	}
	public String getClearPending() {
		return clearPending;
	}
	public void setClearPending(String clearPending) {
		this.clearPending = clearPending;
	}
	
}
