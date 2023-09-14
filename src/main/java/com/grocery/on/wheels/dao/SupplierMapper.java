package com.grocery.on.wheels.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.grocery.on.wheels.model.Supplier;

@Mapper
public interface SupplierMapper {

	List<Supplier> getSuppliers();

	void addSupplier(Supplier supplier);

	List<Supplier> findSuppliers(String searchText);

	void clearSupplier();

	void updateSupplier(Supplier supplier);

	void deleteSupplier(String supplierId);

}
