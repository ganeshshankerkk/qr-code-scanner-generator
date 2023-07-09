package com.scanner.qr.code.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.common.HybridBinarizer;
import com.scanner.qr.code.constants.QrCodeConstants;
import com.scanner.qr.code.exception.QrCodeNotFoundException;
import com.scanner.qr.code.model.FileStorageProperties;
import com.scanner.qr.code.model.QrCode;
import com.scanner.qr.code.model.Response;
import com.scanner.qr.code.service.QrCodeService;

@Service
public class QrCodeServiceImpl implements QrCodeService {
	private static final Logger logger = LoggerFactory.getLogger(QrCodeService.class);

	private final Path fileStorageLocation;

	@Autowired
	public QrCodeServiceImpl(FileStorageProperties fileStorageProperties) {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry into QrCodeServiceImpl :: path :: " + fileStorageProperties.getUploadPath());
		}
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadPath()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception e) {
			logger.error("Exception :: Could not create directory to save QR Code :: ", e);
		}
	}

	public Resource loadFileAsResource(String fileName) {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry into QrCodeServiceImpl :: loadFileAsResource method :: fileName :: " + fileName);
		}
		try {
			Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			}
		} catch (MalformedURLException ex) {
			logger.error("Exception in QrCodeServiceImpl :: loadFileAsResource method ::  ", ex);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Exit from QrCodeServiceImpl :: loadFileAsResource method :: Resource do not found");
		}
		return null;
	}

	public Response decodeQR(byte[] QrCodeBytes) {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry into QrCodeServiceImpl :: decodeQR method ::");
		}
		Response response = new Response();
		QrCode qrCode = new QrCode();
		String decodedQrData = QrCodeConstants.BLANK;
		try {
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(QrCodeBytes);
			BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
			BufferedImageLuminanceSource bufferedImageLuminanceSource = new BufferedImageLuminanceSource(bufferedImage);
			HybridBinarizer hybridBinarizer = new HybridBinarizer(bufferedImageLuminanceSource);
			BinaryBitmap binaryBitmap = new BinaryBitmap(hybridBinarizer);
			MultiFormatReader multiFormatReader = new MultiFormatReader();
			Result result = multiFormatReader.decode(binaryBitmap);
			decodedQrData = result.getText();
			String qrType = ResultParser.parseResult(result).getType().toString();
			
			qrCode.setText(decodedQrData);
			qrCode.setQrType(qrType);
			response.setQrCode(qrCode);
		} catch (NotFoundException e) {
			response.setStatus("204");
			response.setMessage(QrCodeConstants.NO_QR_CODE_FOUND_ERROR_MESSAGE);
			//throw new QrCodeNotFoundException(QrCodeConstants.NO_QR_CODE_FOUND_ERROR_MESSAGE);
		}catch(IOException e) {
			response.setStatus("500");
			response.setMessage("Failure");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Exit from QrCodeServiceImpl :: decodeQR method :: decodedQrData :: " + decodedQrData);
		}
		return response;
	}
}
