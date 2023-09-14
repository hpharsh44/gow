package com.grocery.on.wheels.model;

public class AuditItem {
	private String itemId;
	private int itemCount;
	private String expDate;
	private String itemPriceId;
	private String itemExpId;
	private String itemName;
	private String mrp;
	private String cp;
	private String sp;
	private String itemIcon;
	private int vanStock;
	private int stockItemDiff;
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
	public String getExpDate() {
		return expDate;
	}
	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}
	public String getItemPriceId() {
		return itemPriceId;
	}
	public void setItemPriceId(String itemPriceId) {
		this.itemPriceId = itemPriceId;
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
	public String getItemIcon() {
		return itemIcon;
	}
	public void setItemIcon(String itemIcon) {
		this.itemIcon = itemIcon;
	}
	public int getVanStock() {
		return vanStock;
	}
	public void setVanStock(int vanStock) {
		this.vanStock = vanStock;
	}
	public int getStockItemDiff() {
		return stockItemDiff;
	}
	public void setStockItemDiff(int stockItemDiff) {
		this.stockItemDiff = stockItemDiff;
	}
}
