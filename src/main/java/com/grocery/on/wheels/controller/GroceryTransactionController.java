package com.grocery.on.wheels.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grocery.on.wheels.model.BaseRsp;
import com.grocery.on.wheels.model.InventoryPurchaseTransaction;
import com.grocery.on.wheels.model.InvoiceCorrection;
import com.grocery.on.wheels.model.ItemValidate;
import com.grocery.on.wheels.model.PrinterResponseObj;
import com.grocery.on.wheels.model.ValidStock;
import com.grocery.on.wheels.model.VanPurchaseTransaction;
import com.grocery.on.wheels.service.GroceryTransactionService;

@RestController
@RequestMapping("/grocery/transaction")
public class GroceryTransactionController {
	
	private static final String VAN = "VAN";
	private static final String INVENTORY = "INVENTORY";
	
	@Autowired
	GroceryTransactionService transactionService;
	
	@PostMapping("/purchase/inventory")
	public BaseRsp purchaseInventory(@RequestBody InventoryPurchaseTransaction inventoryPurchaseTransaction) {
		ValidStock validStock = transactionService.checkValidStock(
				inventoryPurchaseTransaction.getPurchaseTransItem(), INVENTORY, true);
		if(validStock != null && !validStock.isValid()) {
			return new BaseRsp("fail", validStock.getErrorMessage());
		}
		return new BaseRsp("success", transactionService.purchaseInventory(inventoryPurchaseTransaction, true));
	}
	
	@PostMapping("/inventory/item/sale")
	public BaseRsp inventoryItemSale(@RequestBody InventoryPurchaseTransaction inventoryPurchaseTransaction) {
		ValidStock validStock = transactionService.checkValidStock(
				inventoryPurchaseTransaction.getPurchaseTransItem(), INVENTORY, false);
		if(validStock != null && !validStock.isValid()) {
			return new BaseRsp("fail", validStock.getErrorMessage());
		}
		return new BaseRsp("success", transactionService.inventoryItemSale(inventoryPurchaseTransaction, true));
	}
	
	
	@PostMapping("/purchase/van")
	public BaseRsp purchaseVan(@RequestBody VanPurchaseTransaction vanPurchaseTransaction) {
		ValidStock validStock = transactionService.checkValidStock(
				vanPurchaseTransaction.getPurchaseTransItem(), VAN, true);
		if(validStock != null && !validStock.isValid()) {
			return new BaseRsp("fail", validStock.getErrorMessage());
		}
		boolean autoValidate = "REFUND".equals(vanPurchaseTransaction.getTransactionStatus());
		return new BaseRsp("success", transactionService.purchaseVan(vanPurchaseTransaction, autoValidate));
	}
	
	@PostMapping("/sell/van")
	public BaseRsp sellVan(@RequestBody VanPurchaseTransaction vanPurchaseTransaction) {
		ValidStock validStock = transactionService.checkValidStock(
				vanPurchaseTransaction.getPurchaseTransItem(), VAN, true);
		if(validStock != null && !validStock.isValid()) {
			return new BaseRsp("fail", validStock.getErrorMessage());
		}
		return new BaseRsp("success", transactionService.sellVan(vanPurchaseTransaction, true));
	}
	
	@PostMapping("/van/invoice/correction/{trans_type}")
	public BaseRsp vanInvoiceCorrection(@RequestBody List<InvoiceCorrection> invoiceCorrectionObj,
			@PathVariable("trans_type") String transType) {
		return new BaseRsp("success", transactionService.vanInvoiceCorrection(invoiceCorrectionObj, transType));
	}
	
	@PostMapping("/inventory/invoice/correction")
	public BaseRsp inventoryInvoiceCorrection(@RequestBody List<InvoiceCorrection> invoiceCorrectionObj) {
		return new BaseRsp("success", transactionService.inventoryInvoiceCorrection(invoiceCorrectionObj));
	}
	
	@PostMapping("/validate/inventory/item/sale")
	public BaseRsp validateInventoryItemSale(@RequestBody List<ItemValidate> itemValidateList) {
		transactionService.validateInventoryItemSale(itemValidateList);
		return new BaseRsp("success", "saved");
	}

	@PostMapping("/validate/inventory/purchase")
	public BaseRsp validateInventoryPurchase(@RequestBody List<ItemValidate> itemValidateList) {
		transactionService.validateInventoryPurchase(itemValidateList);
		return new BaseRsp("success", "saved");
	}
	
	//@PostMapping("validate/inventory/sale")
	public BaseRsp validateInventorySale(@RequestBody List<ItemValidate> itemValidateList) {
		transactionService.validateInventorySale(itemValidateList);
		return new BaseRsp("success", "saved");
	}

	@PostMapping("/validate/van/purchase")
	public BaseRsp validateVanPurchase(@RequestBody List<ItemValidate> itemValidateList) {
		transactionService.validateVanPurchase(itemValidateList, false);
		return new BaseRsp("success", "saved");
	}
	
	@PostMapping("/validate/van/sale")
	public BaseRsp validateVanSale(@RequestBody List<ItemValidate> itemValidateList) {
		transactionService.validateVanSale(itemValidateList);
		return new BaseRsp("success", "saved");
	}
	
	@PostMapping("/clear_transaction/{inv_id}")
	public BaseRsp clearTransaction(@PathVariable("inv_id") String inventoryId) {
		transactionService.clearTransaction();
		return new BaseRsp("success", "delete");
	}
	
	@GetMapping("/inventory/printer/text")
	public BaseRsp generateInventoryPrinterText(@RequestParam("invoice_id") String invoiceId, 
			@RequestParam("invc_gen") String invcGen) {
		return new BaseRsp("success", new PrinterResponseObj(transactionService.generateInventoryPrinterText(invoiceId, invcGen)));
	}
	
	@GetMapping("/inventory/invoice")
	public BaseRsp generateInventoryInvoice(@RequestParam("invoice_id") String invoiceId) {
		return new BaseRsp("success", transactionService.generateInventoryInvoice(invoiceId));
	}

	@GetMapping("/van/invoice")
	public BaseRsp generateVanInvoice(@RequestParam("invoice_id") String invoiceId) {
		return new BaseRsp("success", transactionService.generateVanInvoice(invoiceId));
	}
}
