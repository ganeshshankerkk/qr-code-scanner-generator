package com.scanner.qr.code.service;

import org.springframework.core.io.Resource;

import com.scanner.qr.code.model.Response;

public interface QrCodeService {

	Resource loadFileAsResource(String fileName);

	Response decodeQR(byte[] QrCodeBytes);

}
