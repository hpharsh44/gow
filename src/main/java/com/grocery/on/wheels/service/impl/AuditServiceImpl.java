package com.grocery.on.wheels.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grocery.on.wheels.dao.AuditMapper;
import com.grocery.on.wheels.model.Audit;
import com.grocery.on.wheels.model.AuditItem;
import com.grocery.on.wheels.model.NonAuditItems;
import com.grocery.on.wheels.service.AuditService;
import com.grocery.on.wheels.util.GroceryUtil;

@Service
public class AuditServiceImpl implements AuditService {

	@Autowired
	AuditMapper auditMapper;
	
	private String generateAuditId() {
		return "AUDIT_" + GroceryUtil.getFormatDate(new Date());
	}
	
	@Override
	public List<Audit> getAudits(String vanId) {
		return auditMapper.getAudits(vanId);
	}

	@Override
	public void addAudit(Audit audit) {
		String auditId = generateAuditId();
		audit.setAuditId(auditId);
		audit.setAuditName(auditId);
		audit.setState("INIT");
		auditMapper.insertAuditItems(auditId, audit.getAuditItem());
		auditMapper.addAudit(audit);
		audit = findAudit(auditId);
	}

	@Override
	public Audit findAudit(String auditId) {
		return auditMapper.findAudit(auditId);
	}

	@Override
	public List<Audit> getAudits(String vanId, String fromDate, String toDate) {
		return auditMapper.getRangeAudits(vanId, fromDate, toDate);
	}

	@Override
	public List<NonAuditItems> fixAudit(Audit audit) {
		List<String> itemExpIdList = audit.getAuditItem().stream()
				.map(AuditItem::getItemExpId).collect(Collectors.toList());
		
		List<NonAuditItems> notAuditItems = auditMapper.getNonAuditItems(itemExpIdList);
		
		auditMapper.updateAuditState(audit);
		auditMapper.updateVanItemCount(audit.getVanId(), audit.getAuditItem());
		
		auditMapper.setZeroNonAuditItems(itemExpIdList);
		
		return notAuditItems;
	}
}
