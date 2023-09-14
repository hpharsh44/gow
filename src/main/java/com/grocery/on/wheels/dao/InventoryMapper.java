package com.grocery.on.wheels.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.grocery.on.wheels.model.Inventory;
import com.grocery.on.wheels.model.Item;

@Mapper
public interface InventoryMapper {
	public List<Inventory> listInventory();

	public List<Item> getItems(String inventoryId);

	public List<Item> getItemsByName(@Param("inventoryId") String inventoryId,
			@Param("searchText")  String searchText);

	public void clearInventory();

	public List<Item> getInventoryVanStock(@Param("inventoryId") String inventoryId,
			@Param("vanId") String vanId);
}
