package com.tek.cmf.exceptions;

public class NoWebcamException extends Exception {

	private static final long serialVersionUID = 731745037067421853L;

	@Override
	public String getMessage() {
		return "No webcam was found";
	}
	
}
