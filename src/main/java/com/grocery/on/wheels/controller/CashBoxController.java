package com.grocery.on.wheels.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grocery.on.wheels.model.BaseRsp;
import com.grocery.on.wheels.model.CashBox;
import com.grocery.on.wheels.service.CashBoxService;

@RestController
@RequestMapping("/grocery/cash")
public class CashBoxController {
	@Autowired
	CashBoxService cashBoxService;
	
	@GetMapping("/accumulated")
	public List<CashBox> getAccumulatedCashBox() {
		return cashBoxService.getAccumulatedCashBox();
	}
	
	@GetMapping("/list")
	public List<CashBox> getCashBoxes() {
		return cashBoxService.getCashBoxes();
	}
	
	@PostMapping("/add")
	public BaseRsp addCashBox(@RequestBody CashBox cashBox) {
		cashBoxService.addCashBox(cashBox);
		return new BaseRsp("success", cashBox);
	}
	
	@GetMapping("/van/cash")
	public List<CashBox> getVanCashBoxes(@RequestParam(value="van_id", required = false) String vanId,
			@RequestParam(value="from_date", required = true) String fromDate, 
			@RequestParam(value="to_date", required = true) String toDate) {
		return cashBoxService.getVanCashBoxes(vanId, fromDate, toDate);
	}
}
