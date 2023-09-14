package com.grocery.on.wheels.service;

import java.util.List;

import com.grocery.on.wheels.model.Inventory;
import com.grocery.on.wheels.model.Item;

public interface InventoryService {

	List<Inventory> listInventory();

	List<Item> getItems(String inventoryId);

	List<Item> getItemsByName(String inventoryId, String searchText);

	void clearInventory();

	List<Item> getInventoryStockList(String inventoryId, String vanId);

}
