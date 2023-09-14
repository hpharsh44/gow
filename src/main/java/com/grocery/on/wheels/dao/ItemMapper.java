package com.grocery.on.wheels.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.grocery.on.wheels.model.Item;
import com.grocery.on.wheels.model.ItemPrice;
import com.grocery.on.wheels.model.ItemPriceExpiry;
import com.grocery.on.wheels.model.PendingItem;

@Mapper
public interface ItemMapper {

	void addItem(Item item);

	void editItem(@Param("item") Item item, @Param("iconFileUpdate") boolean iconFileUpdate, @Param("qrFileUpdate") boolean qrFileUpdate);

	void deleteItemPrice(List<ItemPrice> itemPrices);

	void deleteExpiry(List<ItemPrice> itemPrices);

	void clearItem();

	void addItemPrice(@Param("itemId") String itemId, @Param("itemPrice") ItemPrice itemPrice);

	void addInvItemMap(@Param("inventoryId") String inventoryId, @Param("itemPriceId") String itemPriceId, @Param("itemPriceExp") List<ItemPriceExpiry> itemPriceExp);

	List<PendingItem> getPendingItems(String inventoryVanId);

	Item getItemByItemCode(@Param("inventoryId") String inventoryId, @Param("itemCode") String itemCode);

	void deleteInventoryItemMap(@Param("inventoryId") String inventoryId, @Param("itemId") String itemId);

	void deleteItemPriceMap(@Param("inventoryId") String inventoryId, @Param("itemId") String itemId);

	void deleteItemInfo(@Param("inventoryId") String inventoryId, @Param("itemId") String itemId);

	void deleteVanItemMap(@Param("inventoryId") String inventoryId, @Param("itemId") String itemId);

	void addItemPriceExpiry(@Param("itemPriceExp") List<ItemPriceExpiry> itemPriceExp);

	void deleteItemExpiry(List<ItemPrice> itemPrices);
}
