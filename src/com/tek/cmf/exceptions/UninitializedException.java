package com.tek.cmf.exceptions;

public class UninitializedException extends Exception {

	private static final long serialVersionUID = 731745037067421853L;

	public UninitializedException(String device) {
		super(device + " has not been initialized");
	}
	
}
