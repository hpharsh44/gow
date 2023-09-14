package com.grocery.on.wheels.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grocery.on.wheels.dao.SupplierMapper;
import com.grocery.on.wheels.model.Supplier;
import com.grocery.on.wheels.service.SupplierService;
import com.grocery.on.wheels.util.GroceryUtil;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {

	@Autowired
	SupplierMapper supplierMapper;
	
	@Override
	public List<Supplier> getSuppliers() {
		return supplierMapper.getSuppliers();
	}

	@Override
	public void addSupplier(Supplier supplier) {
		supplier.setSupplierId("SPLR_" + GroceryUtil.getFormatDate(new Date()) );
		supplierMapper.addSupplier(supplier);
	}

	@Override
	public List<Supplier> findSuppliers(String searchText) {
		return supplierMapper.findSuppliers(searchText);
	}

	@Override
	public void clearSupplier() {
		supplierMapper.clearSupplier();
	}

	@Override
	public void updateSupplier(Supplier supplier) {
		supplierMapper.updateSupplier(supplier);
	}

	@Override
	public void deleteSupplier(String supplierId) {
		supplierMapper.deleteSupplier(supplierId);
	}

}
