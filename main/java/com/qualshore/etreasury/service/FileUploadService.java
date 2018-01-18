package com.qualshore.etreasury.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class FileUploadService {

	@Autowired
	Environment env;
	
	public long getMaxFileSize() {
		Long result = (long) 0;
		String s = env.getProperty("spring.http.multipart.max-file-size").replaceAll("[^0-9]", "");
		result = Long.parseLong(s) * 1048576;
		return result;
	}
}
