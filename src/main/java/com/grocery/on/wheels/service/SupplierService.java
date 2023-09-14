package com.grocery.on.wheels.service;

import java.util.List;

import com.grocery.on.wheels.model.Supplier;

public interface SupplierService {

	List<Supplier> getSuppliers();

	void addSupplier(Supplier supplier);

	List<Supplier> findSuppliers(String searchText);

	void clearSupplier();

	void updateSupplier(Supplier supplier);

	void deleteSupplier(String supplierId);
	
}
