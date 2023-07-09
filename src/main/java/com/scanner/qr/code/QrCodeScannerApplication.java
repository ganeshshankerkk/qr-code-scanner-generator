package com.scanner.qr.code;

import java.time.LocalDate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.scanner.qr.code.constants.QrCodeConstants;
import com.scanner.qr.code.model.FileStorageProperties;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({
	FileStorageProperties.class
})
public class QrCodeScannerApplication {

	public static void main(String[] args) {
		LocalDate date = java.time.LocalDate.now();
		System.setProperty(QrCodeConstants.LOG_FILE, date + QrCodeConstants.LOG_FILE_NAME + QrCodeConstants.LOG_FILE_EXTENSION);
		SpringApplication.run(QrCodeScannerApplication.class, args);
	}

}
