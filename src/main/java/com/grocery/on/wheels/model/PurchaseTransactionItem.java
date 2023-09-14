package com.grocery.on.wheels.model;

public class PurchaseTransactionItem {
	private String itemId;
	private int itemCount;
	private String mrp;
	private String cp;
	private String sp;
	private String expDate;
	private String itemPriceId;
	private int purchasePending;
	private int salePending;
	private String itemName;
	private String itemExpId;
	private String itemDesc;
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public int getItemCount() {
		return itemCount;
	}
	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}
	public String getMrp() {
		return mrp;
	}
	public void setMrp(String mrp) {
		this.mrp = mrp;
	}
	public String getCp() {
		return cp;
	}
	public void setCp(String cp) {
		this.cp = cp;
	}
	public String getSp() {
		return sp;
	}
	public void setSp(String sp) {
		this.sp = sp;
	}
	public String getItemPriceId() {
		return itemPriceId;
	}
	public void setItemPriceId(String itemPriceId) {
		this.itemPriceId = itemPriceId;
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
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemExpId() {
		return itemExpId;
	}
	public void setItemExpId(String itemExpId) {
		this.itemExpId = itemExpId;
	}
	public String getExpDate() {
		return expDate;
	}
	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	@Override
	public String toString() {
		return "PurchaseTransactionItem [itemId=" + itemId + ", itemCount=" + itemCount + ", mrp=" + mrp + ", cp=" + cp
				+ ", sp=" + sp + ", expDate=" + expDate + ", itemPriceId=" + itemPriceId + ", purchasePending="
				+ purchasePending + ", salePending=" + salePending + ", itemName=" + itemName + ", itemExpId="
				+ itemExpId + ", itemDesc=" + itemDesc + "]";
	}
}
