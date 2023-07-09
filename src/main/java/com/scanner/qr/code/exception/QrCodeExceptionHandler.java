package com.scanner.qr.code.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class QrCodeExceptionHandler {
	
@ExceptionHandler(value = {QrCodeNotFoundException.class})
public ResponseEntity<Object> handleQrCodeNotFoundException (QrCodeNotFoundException codeNotFoundException){
	
	QrCodeException qrCodeException = new QrCodeException(codeNotFoundException.getMessage(), codeNotFoundException.getCause(), HttpStatus.NOT_FOUND);
	
	return new ResponseEntity<Object>(qrCodeException, HttpStatus.NOT_FOUND);
}

}
