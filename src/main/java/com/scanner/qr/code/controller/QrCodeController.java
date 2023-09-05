package com.scanner.qr.code.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.WriterException;
import com.scanner.qr.code.constants.QrCodeConstants;
import com.scanner.qr.code.exception.QrCodeNotFoundException;
import com.scanner.qr.code.model.QrCode;
import com.scanner.qr.code.model.Response;
import com.scanner.qr.code.service.QrCodeService;
import com.scanner.qr.code.util.QrCodeUtil;

@RestController
@CrossOrigin(origins = "${origin.base.url}")
@RequestMapping("/api/")
public class QrCodeController {

	private static final Logger logger = LoggerFactory.getLogger(QrCodeController.class);

	@Autowired
	private QrCodeService qrCodeService;

	/**
	 * Generates QR Code image for the given data
	 * @param qrCode qrCode request body model that holds QR code data
	 * @return generated QR Code file details
	 * @throws WriterException
	 * @throws IOException
	 */
	@PostMapping("create")
	public ResponseEntity<QrCode> generateQRCode(@RequestBody QrCode qrCode) throws WriterException, IOException {

		if (qrCode != null && qrCode.getText() != null && qrCode.getText().length() > 2900) {
			// do not allow to create
		}
		if (qrCode.getWidth() < 300 || qrCode.getHeight() < 300) {
			qrCode.setWidth(300);
			qrCode.setHeight(300);
		} else if (qrCode.getWidth() > 1000 || qrCode.getHeight() > 1000) {
			qrCode.setWidth(1000);
			qrCode.setHeight(1000);
		}
		
		if(qrCode.getWidth() != qrCode.getHeight()) {
			qrCode.setHeight(qrCode.getWidth());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Entry into QrCodeController :: generateQRCode Method :: " + qrCode);
		}
		String imageStr = QrCodeConstants.BLANK;
		Random random = new Random();
		int randomNumber = random.nextInt(100000);

		String fileName = QrCodeConstants.FILE_NAME_PREFIX + String.valueOf(randomNumber)
				+ QrCodeConstants.PNG_FILE_EXTENSION;
		String QR_CODE_IMAGE_PATH = QrCodeConstants.QR_CODE_DIRECTORY + fileName;

		QrCodeUtil.generateQrCodeImage(qrCode.getText(), qrCode.getWidth(), qrCode.getHeight(), QR_CODE_IMAGE_PATH);

		File file = new File(QR_CODE_IMAGE_PATH);

		if (file.exists()) {
			byte[] data = Files.readAllBytes(file.toPath());
			imageStr = java.util.Base64.getEncoder().encodeToString(data);
		}

		qrCode.setFileName(fileName);
		qrCode.setFile(imageStr);
		qrCode.setFileType(Files.probeContentType(file.toPath()));

		if (logger.isDebugEnabled()) {
			logger.debug("Exit from QrCodeController :: generateQRCode Method ::");
		}
		return new ResponseEntity<QrCode>(qrCode, HttpStatus.OK);
	}

	/**
	 * 
	 * @param qrCodeFile file to be scanned
	 * @return decoded data for the given file
	 */
	@PostMapping(path = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> readQrCode(@RequestParam("file") MultipartFile qrCodeFile) {
		Response response = new Response();
		if (qrCodeFile.isEmpty()) {
			response.setMessage("File is empty");
		}
		try {
			response = qrCodeService.decodeQR(qrCodeFile.getBytes());
			if (response.getStatus() == null) {
				response.setStatus("200");
				response.setMessage("Success");
			}
		} catch (IOException e) {
			response.setStatus("500");
			response.setMessage("Failure");
			logger.error("readQrCode :: Exception caught: ", e);
			return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	/**
	 * 
	 * @param fileName name of the file to be downloaded
	 * @param request
	 * @param fileType type of the file to be downloaded (e.g. JPEG/PNG)
	 * @return file to be downloaded
	 * @throws FileNotFoundException
	 */
	@GetMapping(path = "download/fileType={fileType}&filename={fileName:.+}")
	public ResponseEntity<Resource> downloadQrCode(@PathVariable String fileName, HttpServletRequest request,
			@PathVariable String fileType) throws FileNotFoundException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry into QrCodeController :: downloadQrCode Method :: fileName : " + fileName
					+ " : fileType:: " + fileType);
		}
		Resource resource = null;
		if (fileType.equalsIgnoreCase(QrCodeConstants.JPEG)) {
			fileName = QrCodeUtil.convertPngToJpeg(fileName);
		}

		// Load file as Resource
		if (fileName != null) {
			resource = qrCodeService.loadFileAsResource(fileName);
		}

		// Try to determine file's content type
		String contentType = null;

		if (resource != null) {
			try {
				contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
			} catch (IOException ex) {
				logger.error("Could not determine file type.", ex);
			}
			// Fallback to the default content type if type could not be determined
			if (contentType == null) {
				contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Exit from QrCodeController :: downloadQrCode Method ::");
			}
			return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
					// .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
					.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
					// .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
					// HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN)
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
					.body(resource);
		} else {
			logger.error("QrCodeController :: downloadQrCode Method :: resource is null");
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);

	}

}
