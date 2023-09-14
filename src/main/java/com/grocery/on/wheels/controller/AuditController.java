package com.grocery.on.wheels.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grocery.on.wheels.model.BaseRsp;
import com.grocery.on.wheels.model.NonAuditItems;
import com.grocery.on.wheels.model.Audit;
import com.grocery.on.wheels.service.AuditService;

@RestController
@RequestMapping("/grocery/audit")
public class AuditController {
	
	@Autowired
	AuditService auditService;
	
	@GetMapping("/list")
	public List<Audit> getAudits(@RequestParam("van_id") String vanId) {
		return auditService.getAudits(vanId);
	}
	
	@PostMapping("/add")
	public BaseRsp addAudit(@RequestBody Audit audit) {
		auditService.addAudit(audit);
		return new BaseRsp("success", audit);
	}
	
	@GetMapping("/find/{audit_id}")
	public BaseRsp findAudit(@PathVariable("audit_id") String auditId) {
		Audit audit = auditService.findAudit(auditId);
		return new BaseRsp("success", audit);
	}
	
	@GetMapping("/range/list")
	public List<Audit> getAudits(@RequestParam(value="van_id", required = false) String vanId,
			@RequestParam(value="from_date", required = true) String fromDate, 
			@RequestParam(value="to_date", required = true) String toDate) {
		return auditService.getAudits(vanId, fromDate, toDate);
	}

	@PostMapping("/settlement/fix")
	public List<NonAuditItems> fixAudit(@RequestBody Audit audit) {
		List<NonAuditItems> nonAuditItemsList = auditService.fixAudit(audit);
		return nonAuditItemsList;
	}
	
}
