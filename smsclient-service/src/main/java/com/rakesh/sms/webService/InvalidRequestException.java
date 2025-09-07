package com.rakesh.sms.webService;

public class InvalidRequestException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String errorDetails;

	public InvalidRequestException(String message, String errorDetails) {
		super(message);
		this.errorDetails = errorDetails;
	}

	public String getFaultInfo() {
		return this.errorDetails;
	}

}// End Of Class
