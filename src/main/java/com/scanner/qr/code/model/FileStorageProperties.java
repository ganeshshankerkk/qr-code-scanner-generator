package com.scanner.qr.code.model;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.scanner.qr.code.constants.QrCodeConstants;

@ConfigurationProperties(prefix = QrCodeConstants.FILE)
public class FileStorageProperties {

	private String uploadPath;

	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

}
