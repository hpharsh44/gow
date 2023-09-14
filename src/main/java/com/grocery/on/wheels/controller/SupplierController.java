package com.grocery.on.wheels.controller;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grocery.on.wheels.model.BaseRsp;
import com.grocery.on.wheels.model.Supplier;
import com.grocery.on.wheels.service.SupplierService;

@RestController
@RequestMapping("/grocery/supplier")
public class SupplierController {

	@Autowired
	SupplierService supplierService;
	
	@GetMapping("/list")
	public List<Supplier> getSuppliers() {
		return supplierService.getSuppliers();
	}
	
	@PostMapping("/add")
	public BaseRsp addSupplier(@RequestBody Supplier supplier) {
		supplierService.addSupplier(supplier);
		return new BaseRsp("success", supplier);
	}
	
	@PostMapping("/edit")
	public BaseRsp updateSupplier(@RequestBody Supplier supplier) {
		supplierService.updateSupplier(supplier);
		return new BaseRsp("success", supplier);
	}
	
	@DeleteMapping("/delete")
	public BaseRsp deleteSupplier(@RequestParam String supplierId) {
		supplierService.deleteSupplier(supplierId);
		return new BaseRsp("success", "deleted");
	}
	
	@GetMapping("/list/{searchText}")
	public List<Supplier> findSuppliers(@PathVariable("searchText") String searchText) {
		return supplierService.findSuppliers(searchText);
	}
	
	@PostMapping("clear_supplier/{inv_id}")
	public BaseRsp clearSupplier(@PathVariable("inv_id") String inventoryId) {
		supplierService.clearSupplier();
		return new BaseRsp("success", "delete");
	}
}
