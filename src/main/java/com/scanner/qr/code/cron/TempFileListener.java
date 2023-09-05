package com.scanner.qr.code.cron;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.scanner.qr.code.constants.QrCodeConstants;

@Component
public class TempFileListener {

	private static final Logger logger = LoggerFactory.getLogger(TempFileListener.class);
	
	/**
	 * Runs a scheduler for every one hour interval
	 * to delete temporary storage for any QR code file generated
	 * over an hour ago
	 */
	@Scheduled(cron = "0 0 * * * *")
	public void scheduleTask() {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry into TempFileListener :: scheduleTask Method");
		}
		Long startTime = System.currentTimeMillis();

		File folder = new File(QrCodeConstants.QR_CODE_DIRECTORY);
		try {
			if (folder.list().length > 0) {
				for (File file : folder.listFiles()) {
					if (file.exists()) {
						long difference = (System.currentTimeMillis() - file.lastModified()) / 1000 / 60;
						if (difference > 60) {
							logger.debug("TempFileListener :: Modified time difference :: " + difference
									+ " Deleting :: " + file.getName());
							Files.deleteIfExists(file.toPath());
						}
					}
				}
			}
		} catch (IOException e) {
			logger.error("Exception in TempFileListener :: scheduleTask Method : ", e);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Exit from TempFileListener :: scheduleTask Method :: Time taken:: "
					+ TimeUnit.MILLISECONDS.toSeconds((System.currentTimeMillis() - startTime)) + " seconds");
		}
	}

}
