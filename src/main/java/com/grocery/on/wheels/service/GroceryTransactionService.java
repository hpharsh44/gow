package com.grocery.on.wheels.service;

import java.util.List;

import com.grocery.on.wheels.model.InventoryPurchaseTransaction;
import com.grocery.on.wheels.model.InvoiceCorrection;
import com.grocery.on.wheels.model.ItemValidate;
import com.grocery.on.wheels.model.PurchaseTransactionItem;
import com.grocery.on.wheels.model.ResponseObject;
import com.grocery.on.wheels.model.ValidStock;
import com.grocery.on.wheels.model.VanPurchaseTransaction;

public interface GroceryTransactionService {

	InventoryPurchaseTransaction purchaseInventory(InventoryPurchaseTransaction inventoryPurchaseTransaction,
			boolean autoValidate);
	
	VanPurchaseTransaction purchaseVan(VanPurchaseTransaction vanPurchaseTransaction, boolean autoValidate);

	VanPurchaseTransaction sellVan(VanPurchaseTransaction vanPurchaseTransaction,
			boolean autoValidate);

	void validateInventoryPurchase(List<ItemValidate> itemValidateList);

	void validateInventorySale(List<ItemValidate> itemValidateList);

	void validateVanSale(List<ItemValidate> itemValidateList);

	void validateVanPurchase(List<ItemValidate> itemValidateList, boolean updateInventory);

	void clearTransaction();

	String generateInventoryInvoice(String invoiceId);

	String generateVanInvoice(String invoiceId);

	InventoryPurchaseTransaction inventoryItemSale(InventoryPurchaseTransaction inventoryPurchaseTransaction,
			boolean autoValidate);

	void validateInventoryItemSale(List<ItemValidate> itemValidateList);

	String vanInvoiceCorrection(List<InvoiceCorrection> invoiceCorrectionObj, String transType);

	String inventoryInvoiceCorrection(List<InvoiceCorrection> invoiceCorrectionObj);
	
	public ValidStock checkValidStock(List<PurchaseTransactionItem> purchaseTransItem, 
			String transactor, boolean purchase);

	String generateInventoryPrinterText(String invoiceId, String invcGen);

}
