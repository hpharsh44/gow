package com.grocery.on.wheels.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.grocery.on.wheels.model.Item;
import com.grocery.on.wheels.model.PendingItem;

public interface ItemService {

	public void addItem(Item item, String inventoryId, MultipartFile iconFile, MultipartFile qrFile);

	public void editItem(Item item, String inventoryId, MultipartFile iconFile, MultipartFile qrFile);

	public void clearItem();

	public List<PendingItem> getPendingItems(String inventoryVanId);

	public Item getItemByItemCode(String inventoryId, String itemCode);

	public void deleteItem(String inventoryId, String itemId);

}
