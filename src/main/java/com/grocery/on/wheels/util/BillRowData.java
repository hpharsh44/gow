package com.grocery.on.wheels.util;

import java.util.Objects;

public class BillRowData {
	private String itemName;
	private int itemCount;
	private String mrp;
	private String rate;
	private String percentage;
	private double amount;
	private String itemDesc;

	@Override
	public int hashCode() {
		return Objects.hash(itemName, rate);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BillRowData other = (BillRowData) obj;
		return Objects.equals(itemName, other.itemName) && Objects.equals(rate, other.rate);
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
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
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getPercentage() {
		return percentage;
	}
	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	@Override
	public String toString() {
		return "BillRowData [itemName=" + itemName + ", itemCount=" + itemCount + ", mrp=" + mrp + ", rate=" + rate
				+ ", percentage=" + percentage + ", amount=" + amount + ", itemDesc=" + itemDesc + "]";
	}
	
}
