package com.grocery.on.wheels.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grocery.on.wheels.model.BaseRsp;
import com.grocery.on.wheels.model.Item;
import com.grocery.on.wheels.model.PendingItem;
import com.grocery.on.wheels.service.ItemService;

@RestController
@RequestMapping("/grocery/item")
public class ItemController {
	
	@Autowired
	ItemService itemService;
	
	@PostMapping(path = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseRsp addItem(@RequestParam(value="item", required = true) String itemJson,
						@RequestParam(value="inventoryId", required = true) String inventoryId,
                                         @RequestParam(value="icon", required = false) MultipartFile iconFile,
                                         @RequestParam(value="qrCode", required = false) MultipartFile qrFile
                                         ) {
		Item item = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			item = mapper.readValue(itemJson, Item.class);
			Item invItem = itemService.getItemByItemCode(inventoryId, item.getItemCode());
			if(invItem == null) {
				itemService.addItem(item, inventoryId, iconFile, qrFile);
			} else {
				return new BaseRsp("fail", "Item Code Aleardy Exist.");
			}
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return new BaseRsp("fail", e.getMessage());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return new BaseRsp("fail", e.getMessage());
		}
		return new BaseRsp("success", item);
	}
	

	@PostMapping(path = "/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseRsp editItem(@RequestParam(value="item", required = true) String itemJson,
						@RequestParam(value="inventoryId", required = true) String inventoryId,
                                         @RequestParam(value="icon", required = false) MultipartFile iconFile,
                                         @RequestParam(value="qrCode", required = false) MultipartFile qrFile
                                         ) {
		Item item = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			item = mapper.readValue(itemJson, Item.class);
			Item invItem = itemService.getItemByItemCode(inventoryId, item.getItemCode());
			//if(invItem == null) {
				itemService.editItem(item, inventoryId, iconFile, qrFile);
			//} else {
//				return new BaseRsp("fail", "Item Code Aleardy Exist.");
	//		}
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return new BaseRsp("fail", e.getMessage());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return new BaseRsp("fail", e.getMessage());
		}
		return new BaseRsp("success", item);
	}
	
	@GetMapping("/pending/items")
	public List<PendingItem> getItems(@RequestParam("inv_van_id") String inventoryVanId) {
		return itemService.getPendingItems(inventoryVanId);
	}

	@PostMapping("clear_item/{inv_id}")
	public BaseRsp clearItem(@PathVariable("inv_id") String inventoryId) {
		itemService.clearItem();
		return new BaseRsp("success", "delete");
	}
	
	
	@PostMapping("delete/{inv_id}/{item_id}")
	public BaseRsp deleteItem(@PathVariable("inv_id") String inventoryId, @PathVariable("item_id") String itemId) {
		itemService.deleteItem(inventoryId, itemId);
		return new BaseRsp("success", "delete");
	}
}