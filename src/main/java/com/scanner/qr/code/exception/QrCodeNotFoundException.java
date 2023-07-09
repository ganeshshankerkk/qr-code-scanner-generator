package com.scanner.qr.code.exception;

public class QrCodeNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 8224277503378794170L;

	public QrCodeNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public QrCodeNotFoundException(String message) {
		super(message);
	}

}
