package com.qualshore.etreasury.entity;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

public class DocumentFile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Documents document;
	private String fileName;
	
	public DocumentFile() {
	}
	
	public Documents getDocument() {
		return document;
	}
	
	public void setDocument(Documents document) {
		this.document = document;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
