package com.grocery.on.wheels.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.grocery.on.wheels.model.CashBox;
import com.grocery.on.wheels.model.CashTrans;

@Mapper
public interface CashBoxMapper {

	List<CashBox> getAccumulatedCashBox();

	List<CashTrans> getCashTransaction(@Param("vanId") String vanId);

	void addCashBox(CashBox cashBox);

	List<CashBox> getCashBoxes();

	List<CashBox> getVanCashBoxes(@Param("vanId") String vanId, @Param("fromDate") String fromDate, @Param("toDate") String toDate);

}
