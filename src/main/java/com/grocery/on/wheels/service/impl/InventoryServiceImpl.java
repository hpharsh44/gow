package com.grocery.on.wheels.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grocery.on.wheels.config.GroceryPropertyConfig;
import com.grocery.on.wheels.dao.InventoryMapper;
import com.grocery.on.wheels.model.Inventory;
import com.grocery.on.wheels.model.Item;
import com.grocery.on.wheels.service.InventoryService;

@Transactional
@Service
public class InventoryServiceImpl implements InventoryService {
	
	@Autowired
	GroceryPropertyConfig propertyConfig;

	@Autowired
	InventoryMapper inventoryMapper;
	
	@Override
	public List<Inventory> listInventory() {
		return inventoryMapper.listInventory();
	}

	@Override
	public List<Item> getItems(String inventoryId) {
		return inventoryMapper.getItems(inventoryId);
	}

	@Override
	public List<Item> getItemsByName(String inventoryId, String searchText) {
		return inventoryMapper.getItemsByName(inventoryId, searchText);
	}

	@Override
	public void clearInventory() {
		inventoryMapper.clearInventory();
	}

	@Override
	public List<Item> getInventoryStockList(String inventoryId, String vanId) {
		return inventoryMapper.getInventoryVanStock(inventoryId, vanId);
	}

}
