package com.grocery.on.wheels.model;

public class Supplier implements ResponseObject {
	private String supplierId;
	private String supplierName;
	private String supplierMobile;
	public String getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getSupplierMobile() {
		return supplierMobile;
	}
	public void setSupplierMobile(String supplierMobile) {
		this.supplierMobile = supplierMobile;
	}
}
