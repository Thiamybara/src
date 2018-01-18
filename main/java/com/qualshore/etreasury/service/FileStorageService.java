package com.qualshore.etreasury.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.qualshore.etreasury.entity.BankCondition;
import com.qualshore.etreasury.entity.Documents;

@Service
public class FileStorageService {
    
    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    
    public void store(MultipartFile file, Documents document, String location) throws IOException {
        try
        {
            Path rootLocation = getRootDocument(document, location);
            String [] originalName = file.getOriginalFilename().split("\\.pdf");
            String nomFic = originalName[0];
           SimpleDateFormat formater = new SimpleDateFormat("ddMMyyyy");
           String date2=formater.format(document.getDateChargement());
         
           Files.copy(file.getInputStream(), rootLocation.resolve(nomFic+"_"+ date2+ ".pdf"));
       } catch (Exception e) {
           throw new RuntimeException("Ce fichier existe déjà.");
       } 
    }
    public void storeFile(MultipartFile file, BankCondition bankCond, String location) throws IOException {
        try
        {
            Path rootLocation = getRootFileDocument(bankCond, location);
            String [] originalName = file.getOriginalFilename().split("\\.csv");
            String nomFic = originalName[0];
           //SimpleDateFormat formater = new SimpleDateFormat("ddMMyyyy");
         //  String date2=formater.format(document.getDateChargement());
         
           Files.copy(file.getInputStream(), rootLocation.resolve(nomFic+ ".csv"));
       } catch (Exception e) {
           throw new RuntimeException("Ce fichier existe déjà.");
       } 
    }
    
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
    
    public Path getRootDocument(Documents document, String location) throws Exception {
        return Paths.get(location+ "/"+ document.getRepertoire());
    }
    public Path getRootFileDocument(BankCondition banqueCond, String location) throws Exception {
        return Paths.get(location+ "/"+ banqueCond.getRepertoireFile());
    }
    
    public Path getRootDeleteDocument(Documents document, String location) throws Exception {
        return Paths.get(location+ "/"+ document.getUrlDocument());
    }
    
    public void createLocation(String rep, Environment env) throws IOException {
        String[] listDir = rep.split("/");
        try {
            String location = env.getProperty("root.location.store");
            for(int i = 0; i < listDir.length; i++)
            {
                location += "/"+ listDir[i];
                if(Files.notExists(Paths.get(location)))
                    Files.createDirectory(Paths.get(location));
            }
       } catch (IOException e) {
           throw new RuntimeException("Une erreur est survenue lors du stockage!");
       }
    }
    public void createFileLocation(String rep, Environment env) throws IOException {
        String[] listDir = rep.split("/");
        try {
            String location = env.getProperty("root.location.store.file");
            for(int i = 0; i < listDir.length; i++)
            {
                location += "/"+ listDir[i];
                if(Files.notExists(Paths.get(location)))
                    Files.createDirectory(Paths.get(location));
            }
       } catch (IOException e) {
           throw new RuntimeException("Une erreur est survenue lors du stockage!");
       }
    }
}