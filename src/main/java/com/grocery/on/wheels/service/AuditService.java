package com.grocery.on.wheels.service;

import java.util.List;

import com.grocery.on.wheels.model.Audit;
import com.grocery.on.wheels.model.NonAuditItems;

public interface AuditService {

	List<Audit> getAudits(String vanId);

	void addAudit(Audit audit);

	List<Audit> getAudits(String vanId, String fromDate, String toDate);

	List<NonAuditItems> fixAudit(Audit audit);

	Audit findAudit(String auditId);

}
