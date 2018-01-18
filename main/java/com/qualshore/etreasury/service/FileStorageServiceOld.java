package com.qualshore.etreasury.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.qualshore.etreasury.entity.Documents;

//@Service
public class FileStorageServiceOld {
	
	Logger log = LoggerFactory.getLogger(this.getClass().getName());
	@Autowired
	Environment env;
	
	public void store(MultipartFile file, Documents document, String location) throws IOException{
		try {
			Path rootLocation = getRootDocument(document, location);
			Files.copy(file.getInputStream(), rootLocation.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
        	throw new RuntimeException("Ce fichier existe déjà");
        } 
	}
 
    /*public Resource loadFile(Documents document, String location, String fileName) throws Exception {
        try {
        	Path rootLocation = getRootDocument(document, location);
            Path file = new File(location+ "/"+ document.getRepertoire()+ "/" + fileName).toPath() ;
            rootLocation.resolve(location+ "/"+ document.getRepertoire()+ "/" + fileName);
            System.out.println("FILE "+ location+ "/"+ document.getRepertoire());
            System.out.println("ROOT  "+ location+ "/"+ document.getRepertoire()+ "/" + fileName);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }else{
            	throw new RuntimeException("FAIL 2");
            }
        } catch (MalformedURLException e) {
        	throw new RuntimeException("FAIL 3");
        }
    }*/
	
	public String loadFile(Documents document, String location, String fileName) throws Exception {
        try {
        	Path rootLocation = getRootDocument(document, location);
            Path file = new File(location+ "/"+ document.getRepertoire()+ "/" + fileName).toPath() ;
            rootLocation.resolve(location+ "/"+ document.getRepertoire()+ "/" + fileName);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return document.getRepertoire()+ "/" + fileName;
            }else{
            	throw new RuntimeException("FAIL 2");
            }
        } catch (MalformedURLException e) {
        	throw new RuntimeException("FAIL 3");
        }
    }
    
    public void delete(Documents document, String location) throws Exception {
    	try {
    		Path rootLocation = getRootDocument(document, location);
    		FileSystemUtils.deleteRecursively(rootLocation.toFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void deleteFile(Documents document, String fileName) throws Exception {
    	try {
    		Path rootLocation = getRootDeleteDocument(document, fileName);
    		rootLocation.toFile().delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
 
    /*public void init(Documents document) throws Exception {
        try {
        	System.out.println(getRootDocument(document).toString());
            Files.createDirectory(getRootDocument(document));
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!");
        }
    }*/
    public Path getRootDeleteDocument2(Documents document) throws Exception {
        return Paths.get(env.getProperty("root.location.load")+ "/" + document.getUrlDocument());
    }
    
    public void deleteFile2(Documents document) throws Exception {
       try {
           Path rootLocation = getRootDeleteDocument2(document);
           rootLocation.toFile().delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
   }
	public Path getRootDocument(Documents document, String location) throws Exception {
		return Paths.get(location+ "/"+ document.getRepertoire());
	}
	
	public Path getRootDeleteDocument(Documents document, String fileName) throws Exception {
		return Paths.get(document.getRepertoire()+ "/"+ fileName+ ".pdf");
	}
}
