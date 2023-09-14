package com.grocery.on.wheels.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grocery.on.wheels.dao.CustomerMapper;
import com.grocery.on.wheels.model.Customer;
import com.grocery.on.wheels.service.CustomerService;
import com.grocery.on.wheels.util.GroceryUtil;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerMapper customerMapper;
	
	@Override
	public List<Customer> getCustomers() {
		return customerMapper.getCustomers();
	}

	@Override
	public void addCustomer(Customer customer) {
		customer.setCustomerId("CUST_" + GroceryUtil.getFormatDate(new Date()));
		customerMapper.addCustomer(customer);
		customerMapper.addCustomerAddress(customer);
	}

	@Override
	public List<Customer> findCustomers(String searchText) {
		return customerMapper.findCustomers("%" + searchText + "%");
	}

	@Override
	public void clearCustomer() {
		customerMapper.clearCustomer();
	}

	@Override
	public List<Customer> findParamCustomers(String searchText, String paramText) {
		return customerMapper.findParamCustomers("%" + searchText + "%", paramText);
	}

}
