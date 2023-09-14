package com.grocery.on.wheels.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.grocery.on.wheels.model.Audit;
import com.grocery.on.wheels.model.AuditItem;
import com.grocery.on.wheels.model.NonAuditItems;

@Mapper
public interface AuditMapper {

	List<Audit> getAudits(@Param("vanId") String vanId);
	
	void insertAuditItems(@Param("auditId") String auditId, @Param("auditItems") List<AuditItem> auditItems);

	void addAudit(Audit audit);

	List<Audit> getRangeAudits(@Param("vanId") String vanId, @Param("fromDate") String fromDate, @Param("toDate") String toDate);

	void updateAuditState(Audit audit);

	void updateVanItemCount(@Param("vanId") String vanId, @Param("auditItems") List<AuditItem> auditItems);

	Audit findAudit(@Param("auditId") String auditId);

	List<NonAuditItems> getNonAuditItems(@Param("itemExpIdList") List<String> itemExpIdList);

	void setZeroNonAuditItems(@Param("itemExpIdList") List<String> itemExpIdList);

}
