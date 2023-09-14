package com.grocery.on.wheels.model;

import java.util.List;

public class Customer implements ResponseObject {
	private String customerId;
	private String customerName;
	private List<CustomerAddress> address;
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public List<CustomerAddress> getAddress() {
		return address;
	}
	public void setAddress(List<CustomerAddress> address) {
		this.address = address;
	}
}
