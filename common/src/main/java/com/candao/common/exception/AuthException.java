package com.candao.common.exception;

public class AuthException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2681201773658426048L;

	public AuthException() {
		super();
	}

	public AuthException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthException(String message) {
		super(message);
	}

	public AuthException(Throwable cause) {
		super(cause);
	}
}
