package com.grocery.on.wheels.model;

import java.util.List;

public class BaseRsp {
	private String status;
	private String rspId;
	private ResponseObject obj;

	public BaseRsp(String status, String rspId, ResponseObject obj) {
		this.status = status;
		this.rspId = rspId;
		this.obj = obj;
	}
	public BaseRsp(String status, ResponseObject obj) {
		this.status = status;
		this.obj = obj;
	}
	
	public BaseRsp(String status, String rspId) {
		this.status = status;
		this.rspId = rspId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRspId() {
		return rspId;
	}
	public void setRspId(String rspId) {
		this.rspId = rspId;
	}
	public ResponseObject getObj() {
		return obj;
	}
	public void setObj(ResponseObject obj) {
		this.obj = obj;
	}
	
}
