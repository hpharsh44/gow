package com.grocery.on.wheels.service;

import java.util.List;

import com.grocery.on.wheels.model.Customer;

public interface CustomerService {

	List<Customer> getCustomers();

	void addCustomer(Customer customer);

	List<Customer> findCustomers(String searchText);

	void clearCustomer();

	List<Customer> findParamCustomers(String searchText, String paramText);

}
