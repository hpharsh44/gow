package com.grocery.on.wheels.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grocery.on.wheels.dao.CashBoxMapper;
import com.grocery.on.wheels.model.CashBox;
import com.grocery.on.wheels.model.CashTrans;
import com.grocery.on.wheels.service.CashBoxService;
import com.grocery.on.wheels.util.GroceryUtil;

@Service
public class CashBoxServiceImpl implements CashBoxService {
	
	@Autowired
	CashBoxMapper cashBoxMapper;

	private double doubleParse(String data) {
		return (data == null) ? 0.0 : Double.parseDouble(data);
	}
	
	@Override
	public List<CashBox> getAccumulatedCashBox() {
		List<CashBox> accumulatedCashBoxes = cashBoxMapper.getAccumulatedCashBox();
		applyCashAmount(accumulatedCashBoxes);
		return accumulatedCashBoxes;
	}
	
	private void applyCashAmount(List<CashBox> cashBoxes) {
		for(CashBox cashBox: cashBoxes) {
			List<CashTrans> cashTransList = getCashTransAmt(cashBox.getVanId());
			for(CashTrans cashTrans: cashTransList) {
				if("CREDIT".equals(cashTrans.getTransType())) {
					cashBox.setCashTrans((doubleParse(cashBox.getCashTrans()) + doubleParse(cashTrans.getAmt())) + "");
				} else {
					cashBox.setCashTrans((doubleParse(cashBox.getCashTrans()) - doubleParse(cashTrans.getAmt())) + "");
				}
			}
		}
	}

	@Override
	public List<CashBox> getCashBoxes() {
		List<CashBox> cashBoxes = cashBoxMapper.getCashBoxes();
		applyCashAmount(cashBoxes);
		return cashBoxes;
	}

	@Override
	public void addCashBox(CashBox cashBox) {
		cashBox.setBoxId(generateCashBoxId());
		cashBoxMapper.addCashBox(cashBox);
	}

	private List<CashTrans> getCashTransAmt(String vanId) {
		List<CashTrans> cashTrans = cashBoxMapper.getCashTransaction(vanId);
		return cashTrans;
	}
		
	private String generateCashBoxId() {
		String prfeix = "CSH_";
		Date date = new Date();
		return prfeix + "" + GroceryUtil.getFormatDate(date);
	}

	@Override
	public List<CashBox> getVanCashBoxes(String vanId, String fromDate, String toDate) {
		List<CashBox> cashBoxes = cashBoxMapper.getVanCashBoxes(vanId, fromDate, toDate);
		applyCashAmount(cashBoxes);
		return cashBoxes;
	}
}
