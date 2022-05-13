package org.ged.bean;

public class MyFileNotFoundException extends RuntimeException {

	public MyFileNotFoundException(String message) {
		super(message);
	}

	public MyFileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
