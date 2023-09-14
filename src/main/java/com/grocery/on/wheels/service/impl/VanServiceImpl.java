package com.grocery.on.wheels.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grocery.on.wheels.dao.VanMapper;
import com.grocery.on.wheels.model.Item;
import com.grocery.on.wheels.model.Van;
import com.grocery.on.wheels.service.VanService;

@Service
@Transactional
public class VanServiceImpl implements VanService {

	@Autowired
	VanMapper vanMapper;
	
	@Override
	public List<Van> getVans(String inventoryId) {
		return vanMapper.getVans(inventoryId);
	}

	@Override
	public List<Item> getItems(String vanId) {
		return vanMapper.getItems(vanId);
	}

	@Override
	public void clearVan() {
		vanMapper.clearVan();
	}

	@Override
	public List<Item> getVanInvItemsStock(String vanId) {
		return vanMapper.getVanInvItemsStock(vanId);
	}

}
