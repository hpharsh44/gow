package com.grocery.on.wheels.model;

import java.util.Date;

public class CashBox implements ResponseObject {
	private String boxId;
	private String vanId;
	private String boxInitialAmt;
	private String boxStlmtAmt;
	private String cashTrans;
	private Date settlementDate;
	public String getBoxId() {
		return boxId;
	}
	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}
	public String getVanId() {
		return vanId;
	}
	public void setVanId(String vanId) {
		this.vanId = vanId;
	}
	public String getBoxInitialAmt() {
		return boxInitialAmt;
	}
	public void setBoxInitialAmt(String boxInitialAmt) {
		this.boxInitialAmt = boxInitialAmt;
	}
	public String getBoxStlmtAmt() {
		return boxStlmtAmt;
	}
	public void setBoxStlmtAmt(String boxStlmtAmt) {
		this.boxStlmtAmt = boxStlmtAmt;
	}
	public String getCashTrans() {
		return cashTrans;
	}
	public void setCashTrans(String cashTrans) {
		this.cashTrans = cashTrans;
	}
	public Date getSettlementDate() {
		return settlementDate;
	}
	public void setSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
	}
}
