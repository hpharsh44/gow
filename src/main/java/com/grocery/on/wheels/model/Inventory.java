package com.grocery.on.wheels.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Inventory {
	@JsonProperty("inventroy_id")
	private String inventoryId;
	
	@JsonProperty("inventory_name")
	private String inventroyName;
	public String getInventoryId() {
		return inventoryId;
	}
	public void setInventoryId(String inventoryId) {
		this.inventoryId = inventoryId;
	}
	public String getInventroyName() {
		return inventroyName;
	}
	public void setInventroyName(String inventroyName) {
		this.inventroyName = inventroyName;
	}
	
}
