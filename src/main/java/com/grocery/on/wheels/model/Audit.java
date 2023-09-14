package com.grocery.on.wheels.model;

import java.util.Date;
import java.util.List;

public class Audit implements ResponseObject {

	private String auditId;
	private String auditName;
	private Date auditDate;
	private List<AuditItem> auditItem;
	private String vanId;
	private String state;
	public String getAuditId() {
		return auditId;
	}
	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}
	public String getAuditName() {
		return auditName;
	}
	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}
	public Date getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	public List<AuditItem> getAuditItem() {
		return auditItem;
	}
	public void setAuditItem(List<AuditItem> auditItem) {
		this.auditItem = auditItem;
	}
	public String getVanId() {
		return vanId;
	}
	public void setVanId(String vanId) {
		this.vanId = vanId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
