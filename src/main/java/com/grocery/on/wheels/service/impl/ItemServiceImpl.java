package com.grocery.on.wheels.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.grocery.on.wheels.dao.ItemMapper;
import com.grocery.on.wheels.model.Item;
import com.grocery.on.wheels.model.ItemPrice;
import com.grocery.on.wheels.model.ItemPriceExpiry;
import com.grocery.on.wheels.model.PendingItem;
import com.grocery.on.wheels.s3.configuration.S3Configuration;
import com.grocery.on.wheels.s3.service.AmazonClient;
import com.grocery.on.wheels.service.ItemService;
import com.grocery.on.wheels.util.GroceryUtil;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	AmazonClient s3Client;

	@Autowired
	S3Configuration s3Config;
	
	@Autowired
	ItemMapper itemMapper;
	
	@Override
	public void addItem(Item item,String inventoryId, MultipartFile iconFile, MultipartFile qrFile) {
		String iconPath = "";
		if(iconFile != null) {
			iconPath = uploadFile(iconFile);
		}
		String qrPath = "";
		if(qrFile != null) {
			qrPath = uploadFile(qrFile);
		}
		String itemId = "ITM_" + GroceryUtil.getFormatDate(new Date());
		item.setItemId(itemId);
		item.setItemIcon(iconPath);
		item.setItemQrBarCode(qrPath);
		itemMapper.addItem(item);
		addItemPriceAndExpiry(item, inventoryId);
	}
	
	private void addItemPriceAndExpiry(Item item, String inventoryId) {
		if(item != null && item.getItemPrice().size() > 0) {
			int count = 0;
			for(ItemPrice itemPrice: item.getItemPrice()) {
				String itemPriceId = itemPrice.getItemPriceId();
				if(itemPriceId == null) {
					itemPriceId = "IPRC_" + GroceryUtil.getFormatDate(new Date()) + "_" + (++count);
					itemPrice.setItemPriceId(itemPriceId);
				}
				itemMapper.addItemPrice(item.getItemId(), itemPrice);
				List<ItemPriceExpiry> itemPriceExp = itemPrice.getPriceExpiry();
				saveItemPriceExpiry(itemPriceExp, count);
				itemMapper.addInvItemMap(inventoryId, itemPriceId, itemPriceExp);
			}
		}
	}
	
	private void saveItemPriceExpiry(List<ItemPriceExpiry> itemPriceExps, int itemCount) {
		int count = 0;
		for(ItemPriceExpiry itemPriceExp: itemPriceExps) {
			if(itemPriceExp.getItemExpId() == null) {
				itemPriceExp.setItemExpId("IEXP_" + GroceryUtil.getFormatDate(new Date()) + "_" + (itemCount) + "_" + (++count));
			}
		}
		itemMapper.addItemPriceExpiry(itemPriceExps);
	}

	private void deleteItemPriceAndExpiry(Item item) {
		List<ItemPrice> itemPrices = item.getItemPrice();
		if(itemPrices != null && itemPrices.size() > 0) {
			itemMapper.deleteItemExpiry(itemPrices);
			itemMapper.deleteExpiry(itemPrices);
			itemMapper.deleteItemPrice(itemPrices);
		}
		
	}
	
	@Override
	public void editItem(Item item, String inventoryId, MultipartFile iconFile, MultipartFile qrFile) {
		boolean iconFileUpdate = false;
		boolean qrFileUpdate = false;
		String iconPath = null;
		String qrPath = null;
		if(iconFile != null) {
			iconFileUpdate = true;
			iconPath = uploadFile(iconFile);
		}
		if(qrFile != null) {
			qrFileUpdate = true;
			qrPath = uploadFile(qrFile);
		}
		item.setItemIcon(iconPath);
		item.setItemQrBarCode(qrPath);
		itemMapper.editItem(item, iconFileUpdate, qrFileUpdate);
		if(item != null && item.getItemPrice().size() > 0) {
			deleteItemPriceAndExpiry(item);
			addItemPriceAndExpiry(item, inventoryId);
		}
	}

	@Override
	public void clearItem() {
		itemMapper.clearItem();
	}

	private String uploadFile(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        String path = String.format("%s/%s", s3Config.getBucketName(), UUID.randomUUID());
        String fileName = String.format("%s", file.getOriginalFilename());
        String savedFilePath = String.format("%s/%s", path, fileName);
        try {
        	s3Client.upload(path, fileName, Optional.of(metadata), file.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to upload file", e);
        }
        return savedFilePath;
	}

	@Override
	public List<PendingItem> getPendingItems(String inventoryVanId) {
		return itemMapper.getPendingItems(inventoryVanId);
	}

	@Override
	public Item getItemByItemCode(String inventoryId, String itemCode) {
		return itemMapper.getItemByItemCode(inventoryId, itemCode);
	}

	@Override
	public void deleteItem(String inventoryId, String itemId) {
		itemMapper.deleteVanItemMap(inventoryId, itemId);
		itemMapper.deleteInventoryItemMap(inventoryId, itemId);
		itemMapper.deleteItemPriceMap(inventoryId, itemId);
		itemMapper.deleteItemInfo(inventoryId, itemId);
	}

}
