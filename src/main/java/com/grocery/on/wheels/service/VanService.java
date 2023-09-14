package com.grocery.on.wheels.service;

import java.util.List;

import com.grocery.on.wheels.model.Item;
import com.grocery.on.wheels.model.Van;

public interface VanService {

	List<Van> getVans(String inventoryId);

	List<Item> getItems(String vanId);

	void clearVan();

	List<Item> getVanInvItemsStock(String vanId);

}
