package com.grocery.on.wheels.model;

public class AvailableStock {
	boolean available;
	int stockAvailable;
	int requestedStock;
	int pendingCount;
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public int getStockAvailable() {
		return stockAvailable;
	}
	public void setStockAvailable(int stockAvailable) {
		this.stockAvailable = stockAvailable;
	}
	public int getRequestedStock() {
		return requestedStock;
	}
	public void setRequestedStock(int requestedStock) {
		this.requestedStock = requestedStock;
	}
	public int getPendingCount() {
		return pendingCount;
	}
	public void setPendingCount(int pendingCount) {
		this.pendingCount = pendingCount;
	}
}
