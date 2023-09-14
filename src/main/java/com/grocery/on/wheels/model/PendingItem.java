package com.grocery.on.wheels.model;

public class PendingItem {
	private String vanInvId;
	private String itemPriceId;
	private String expDate;
	private String invcId;
	private int pendingCount;
	private String transType;//SALE/PURCHASE
	private String transactor;//VAN/INVENTORY
	private String itemExpId;
	private String transactionStatus;//INVOICE/REFUND
	public String getVanInvId() {
		return vanInvId;
	}
	public void setVanInvId(String vanInvId) {
		this.vanInvId = vanInvId;
	}
	public String getItemPriceId() {
		return itemPriceId;
	}
	public void setItemPriceId(String itemPriceId) {
		this.itemPriceId = itemPriceId;
	}
	public String getInvcId() {
		return invcId;
	}
	public void setInvcId(String invcId) {
		this.invcId = invcId;
	}
	public int getPendingCount() {
		return pendingCount;
	}
	public void setPendingCount(int pendingCount) {
		this.pendingCount = pendingCount;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getTransactor() {
		return transactor;
	}
	public void setTransactor(String transactor) {
		this.transactor = transactor;
	}
	public String getExpDate() {
		return expDate;
	}
	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}
	public String getItemExpId() {
		return itemExpId;
	}
	public void setItemExpId(String itemExpId) {
		this.itemExpId = itemExpId;
	}
	public String getTransactionStatus() {
		return transactionStatus;
	}
	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
}
