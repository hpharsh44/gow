package com.grocery.on.wheels.service;

import java.util.List;

import com.grocery.on.wheels.model.CashBox;

public interface CashBoxService {

	List<CashBox> getAccumulatedCashBox();

	List<CashBox> getCashBoxes();

	void addCashBox(CashBox cashBox);

	List<CashBox> getVanCashBoxes(String vanId, String fromDate, String toDate);

}
