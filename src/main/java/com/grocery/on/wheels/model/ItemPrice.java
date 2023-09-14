package com.grocery.on.wheels.model;

import java.util.List;

public class ItemPrice {
	private String itemPriceId;
	private String mrp;
	private String cp;
	private String sp;
	private List<ItemPriceExpiry> priceExpiry;
	
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
	public List<ItemPriceExpiry> getPriceExpiry() {
		return priceExpiry;
	}
	public void setPriceExpiry(List<ItemPriceExpiry> priceExpiry) {
		this.priceExpiry = priceExpiry;
	}
	public String getItemPriceId() {
		return itemPriceId;
	}
	public void setItemPriceId(String itemPriceId) {
		this.itemPriceId = itemPriceId;
	}
	@Override
	public String toString() {
		return "ItemPrice [itemPriceId=" + itemPriceId + ", mrp=" + mrp + ", cp=" + cp + ", sp=" + sp + ", priceExpiry="
				+ priceExpiry + "]";
	}
}
