package com.grocery.on.wheels.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grocery.on.wheels.config.GroceryPropertyConfig;

@PropertySource("classpath:application.properties")
public class Item implements ResponseObject {
	
	@Autowired
	GroceryPropertyConfig config;
	
	@Autowired
	private Environment env;
	
	public Item() {
		super();
	}

	@Value( "${logging.level.root}" )
	public String s3Domain;
	
	public String getS3Domain() {
		return s3Domain;
	}
	public void setS3Domain(String s3Domain) {
		this.s3Domain = s3Domain;
	}
	public String domain;
	private String inventoryId;
	private String itemId;
	private String itemName;
	private String itemCode;
	private String itemUnit;
	private String itemUnitCount;
	private String itemIcon;
	private String itemQrBarCode;
	private List<ItemPrice> itemPrice;
	private String vanItemCount;
//	@JsonIgnore
	private String vanId;

	public String getVanItemCount() {
		return vanItemCount;
	}

	public void setVanItemCount(String vanItemCount) {
		this.vanItemCount = vanItemCount;
	}

	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getInventoryId() {
		return inventoryId;
	}
	public void setInventoryId(String inventoryId) {
		this.inventoryId = inventoryId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemUnit() {
		return itemUnit;
	}
	public void setItemUnit(String itemUnit) {
		this.itemUnit = itemUnit;
	}
	public String getItemUnitCount() {
		return itemUnitCount;
	}
	public void setItemUnitCount(String itemUnitCount) {
		this.itemUnitCount = itemUnitCount;
	}
	public String getItemIcon() {
		return itemIcon;
	}
	public void setItemIcon(String itemIcon) {
		this.itemIcon = itemIcon;
	}
	public String getItemQrBarCode() {
		return itemQrBarCode;
	}
	public void setItemQrBarCode(String itemQrBarCode) {
		this.itemQrBarCode = itemQrBarCode;
	}
	public List<ItemPrice> getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(List<ItemPrice> itemPrice) {
		this.itemPrice = itemPrice;
	}
	public String getVanId() {
		return vanId;
	}
	public void setVanId(String vanId) {
		this.vanId = vanId;
	}
}
