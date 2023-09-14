package com.grocery.on.wheels.model;

public class VanItemStockPending extends ItemPending {
	private int vanItemCount;
	private int invItemCount;
	private int stockDiff;
	public int getVanItemCount() {
		return vanItemCount;
	}
	public void setVanItemCount(int vanItemCount) {
		this.vanItemCount = vanItemCount;
	}
	public int getInvItemCount() {
		return invItemCount;
	}
	public void setInvItemCount(int invItemCount) {
		this.invItemCount = invItemCount;
	}
	public int getStockDiff() {
		return stockDiff;
	}
	public void setStockDiff(int stockDiff) {
		this.stockDiff = stockDiff;
	}
}
