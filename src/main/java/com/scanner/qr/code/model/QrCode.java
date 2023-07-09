package com.scanner.qr.code.model;

import java.io.Serializable;

public class QrCode implements Serializable{
	
	private static final long serialVersionUID = -7943641623680909625L;
	
	private String text;
	private int width;
	private int height;
	private String fileName;
	private String filePath;
	private String file;
	private String fileType;
	private String qrType;
	
	public String getText() {
		return text;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getQrType() {
		return qrType;
	}
	public void setQrType(String qrType) {
		this.qrType = qrType;
	}
	
	@Override
	public String toString() {
		return "QrCode [text=" + text + ", width=" + width + ", height=" + height + ", fileName=" + fileName
				+ ", filePath=" + filePath + ", fileType=" + fileType + ", qrType=" + qrType + "]";
	}
	
}
