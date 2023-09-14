package com.grocery.on.wheels.model;

public class PrinterResponseObj implements ResponseObject {
	private String printerText;
	
	public PrinterResponseObj(String printerText) {
		this.printerText = printerText;
	}

	public String getPrinterText() {
		return printerText;
	}

	public void setPrinterText(String printerText) {
		this.printerText = printerText;
	}
	
	
}
