package com.scanner.qr.code.util;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.scanner.qr.code.constants.QrCodeConstants;

public class QrCodeUtil {

	private static final Logger logger = LoggerFactory.getLogger(QrCodeUtil.class);

	public static void generateQrCodeImage(String text, int width, int height, String filePath) throws IOException {

		if (logger.isDebugEnabled()) {
			logger.debug("Entry into QrCodeUtil :: generateQrCodeImage Method :: ");
		}

		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = null;
		try {
			bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
			Path path = FileSystems.getDefault().getPath(filePath);
			// MatrixToImageConfig config = new MatrixToImageConfig(0xFFFF99CC, 0xFF0066FF);
			MatrixToImageWriter.writeToPath(bitMatrix, QrCodeConstants.PNG, path);
		} catch (WriterException e) {
			logger.debug("Exception in QrCodeUtil :: generateQrCodeImage Method :: WriterException :: ", e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Exit from QrCodeUtil :: generateQrCodeImage Method :: ");
		}
	}

	public static String convertPngToJpeg(String fileName) {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry into QrCodeUtil :: convertPngToJpeg Method :: fileName :: " + fileName);
		}
		String fname = null;
		String inputPath = QrCodeConstants.QR_CODE_DIRECTORY + fileName;
		String name = fileName.split(QrCodeConstants.PERIOD)[0];
		String outputPath = QrCodeConstants.QR_CODE_DIRECTORY + name + QrCodeConstants.JPEG_FILE_EXTENSION;
		boolean isImageCreated = Boolean.FALSE;
		try {
			FileInputStream inputStream = new FileInputStream(inputPath);

			FileOutputStream outputStream = new FileOutputStream(outputPath);
			BufferedImage inputImage = ImageIO.read(inputStream);
			isImageCreated = ImageIO.write(inputImage, QrCodeConstants.JPEG, outputStream);
		} catch (IOException e) {
			logger.error("Exception in QrCodeUtil :: convertPngToJpeg Method :: " , e);
			return null;
		} 
		if(isImageCreated) {
			fname =  name + QrCodeConstants.JPEG_FILE_EXTENSION;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Exit from QrCodeUtil :: convertPngToJpeg Method :: fileName :: " + fname);
		}
		return fname;
	}

}
