package com.grocery.on.wheels.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.grocery.on.wheels.model.AvailableStock;
import com.grocery.on.wheels.model.InventoryPurchaseTransaction;
import com.grocery.on.wheels.model.Invoice;
import com.grocery.on.wheels.model.InvoiceCorrection;
import com.grocery.on.wheels.model.ItemValidate;
import com.grocery.on.wheels.model.PendingItem;
import com.grocery.on.wheels.model.VanPurchaseTransaction;

@Mapper
public interface GroceryTransactionMapper {

	void createInvoice(Invoice invoice);

	void saveInventoryTransactionInfo(InventoryPurchaseTransaction inventoryPurchaseTransaction);

	void updateInventoryStockPurchase(InventoryPurchaseTransaction inventoryPurchaseTransaction);

	void saveVanTransactionInfo(VanPurchaseTransaction vanPurchaseTransaction);

	void updateVanStockPurchase(VanPurchaseTransaction vanPurchaseTransaction);

	void updateVanStockSale(VanPurchaseTransaction vanPurchaseTransaction);

	void updateInventoryStockSale(VanPurchaseTransaction vanPurchaseTransaction);

	void addVanItemMap(VanPurchaseTransaction vanPurchaseTransaction);

	void validateVanPurchase(@Param("itemValidateList") List<ItemValidate> itemValidateList);

	void validateVanSale(@Param("itemValidateList") List<ItemValidate> itemValidateList);

	void validateInventorySale(@Param("itemValidateList") List<ItemValidate> itemValidateList);

	void validateInventoryPurchase(@Param("itemValidateList") List<ItemValidate> itemValidateList);
	
	
	void clearTransaction();

	void insertPendingItems(List<PendingItem> pendingItemList);

	void validateInventoryPurchaseItemMap(List<ItemValidate> itemValidateList);

	void validateInventoryPurchaseTransaction(List<ItemValidate> itemValidateList);

	void validateInventoryPurchasePendingItem(@Param("itemValidateList") List<ItemValidate> itemValidateList);

	void validateVanPurchaseItemMap(@Param("itemValidateList") List<ItemValidate> itemValidateList);

	void validateVanPurchaseTransaction(@Param("itemValidateList") List<ItemValidate> itemValidateList);

	void validateVanPurchasePendingItem(List<ItemValidate> itemValidateList);

	void validateVanInventoryPurchasePendingItem(List<ItemValidate> itemValidateList);

	void validateInventoryRefundPurchasePendingItem(List<ItemValidate> itemValidateList);

	void validateVanRefundPurchasePendingItem(List<ItemValidate> itemValidateList);

	void validateVanSaleTransaction(List<ItemValidate> itemValidateList);

	InventoryPurchaseTransaction getInventoryPurchaseTransaction(String invoiceId);

	VanPurchaseTransaction getVanPurchaseTransaction(String invoiceId);

	void correctVanTransaction(@Param("invoiceCorrectionObj") List<InvoiceCorrection> invoiceCorrectionObj,
			@Param("transType") String transType);

	void correctVanItem(List<InvoiceCorrection> invoiceCorrectionObj);

	void correctInventoryTransaction(List<InvoiceCorrection> invoiceCorrectionObj);

	void correctInventoryItem(List<InvoiceCorrection> invoiceCorrectionObj);

	void validateInventorySaleTransaction(@Param("itemValidateList") List<ItemValidate> itemValidateList);

	void validateClearInventoryPendingItem(@Param("itemValidateList") List<ItemValidate> clearPendingItems);

	void validateClearVanTransactionPendingItem(@Param("itemValidateList") List<ItemValidate> itemValidateList);

	AvailableStock getVanAvailableStocksPurchase(@Param("itemCount") int itemCount,
			@Param("itemExpId") String itemExpId, @Param("itemPriceId") String itemPriceId);

	AvailableStock getVanAvailableStocksSale(@Param("itemCount") int itemCount,
			@Param("itemExpId") String itemExpId, @Param("itemPriceId") String itemPriceId);

	AvailableStock getInventoryAvailableStocksPurchase(@Param("itemCount") int itemCount,
			@Param("itemExpId") String itemExpId, @Param("itemPriceId") String itemPriceId);

	AvailableStock getInventoryAvailableStocksSale(@Param("itemCount") int itemCount,
			@Param("itemExpId") String itemExpId, @Param("itemPriceId") String itemPriceId);

	void updateInventoryItemMap(List<ItemValidate> itemValidateList);

	void updateInventoryPurchaseTransaction(List<ItemValidate> itemValidateList);

	void updatePurchasePendingItem(List<ItemValidate> itemValidateList);

	void correctVanInventoryItem(List<InvoiceCorrection> invoiceCorrectionObj);
}
