package com.grocery.on.wheels.model;

public class ValidStock {
	boolean valid;
	String errorMessage;
	
	public ValidStock() {}
	
	public ValidStock(boolean valid) {
		this.valid = valid;
	}
	
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
