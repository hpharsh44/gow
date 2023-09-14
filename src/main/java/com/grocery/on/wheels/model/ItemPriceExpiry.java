package com.grocery.on.wheels.model;

import java.util.List;

public class ItemPriceExpiry {
	private String itemExpId;
	private String expDate;
	private int itemCount;
	private boolean isUserCreated;
	private int selCount;
	private List<ItemPending> pendingItems;
	private List<VanItemStockPending> stockPendingItems;
	private boolean expired;
	public String getExpDate() {
		return expDate;
	}
	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}
	public int getItemCount() {
		return itemCount;
	}
	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}
	public boolean getIsUserCreated() {
		return isUserCreated;
	}
	public void setIsUserCreated(boolean isUserCreated) {
		this.isUserCreated = isUserCreated;
	}
	public List<ItemPending> getPendingItems() {
		if(pendingItems != null && pendingItems.size() > 0 &&
				isPendingExist(pendingItems)) {
			return pendingItems;
		} else {
			return null;
		}
	}
	
	private boolean isPendingExist(List<ItemPending> items) {
		boolean hasPending = false;
		for(ItemPending pending: items) {
			if(pending.getPurchasePending() > 0 || pending.getSalePending() > 0) {
				hasPending = true;
			}
		}
		return hasPending;
	}
	
	public void setPendingItems(List<ItemPending> pendingItems) {
		this.pendingItems = pendingItems;
	}
	public String getItemExpId() {
		return itemExpId;
	}
	public void setItemExpId(String itemExpId) {
		this.itemExpId = itemExpId;
	}
	public int getSelCount() {
		return selCount;
	}
	public void setSelCount(int selCount) {
		this.selCount = selCount;
	}
	public boolean isExpired() {
		return expired;
	}
	public void setExpired(boolean expired) {
		this.expired = expired;
	}
	public List<VanItemStockPending> getStockPendingItems() {
		return stockPendingItems;
	}
	public void setStockPendingItems(List<VanItemStockPending> stockPendingItems) {
		this.stockPendingItems = stockPendingItems;
	}
}
