package com.scanner.qr.code.model;

import java.io.Serializable;

public class Response implements Serializable{

	private static final long serialVersionUID = 3055659828238931886L;
	
	private String status;
	private String message;
	private QrCode qrCode;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public QrCode getQrCode() {
		return qrCode;
	}
	public void setQrCode(QrCode qrCode) {
		this.qrCode = qrCode;
	}
	
	@Override
	public String toString() {
		return "Response [status=" + status + ", message=" + message + ", qrCode=" + qrCode + "]";
	}
	
	

}
