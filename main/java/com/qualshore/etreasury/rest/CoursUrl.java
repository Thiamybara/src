package com.qualshore.etreasury.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@RequestMapping("etreasury_project/cours")
@RestController
public class CoursUrl {
	  
	    private URL url = null;
	    private InputStream inputStream = null; 
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> list() {
		HashMap<String, Object> h = new HashMap<String, Object>();
		//JSONObject soapDatainJsonObject;
		//List<Category> listCategory;
		try
		{/*
		try{	
				String pre_apiURL = "http://lookup.dbpedia.org/api/search.asmx/KeywordSearch?QueryClass=person&MaxHits=1&QueryString=Adam%20Sandler";        
				System.out.println("url "+ pre_apiURL);
				URL url = new URL(pre_apiURL);

				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				doc = db.parse(url.openStream());
				
				printDocument(doc, System.out);
				
			}catch(Exception e){
				
			}
			//"https://rates.fxcm.com/RatesXML"
			try 
			{
	           url = "https://rates.fxcm.com/RatesXML";
	            inputStream = url.openStream();
	            String xml = IOUtils.toString(inputStream);
	            JSON objJson = new XMLSerializer().read(xml);
	            System.out.println("JSON data : " + objJson);
			        }catch(Exception e){
			            e.printStackTrace();
			        }finally{
				     try {
				                if (inputStream != null) {
				                    inputStream.close();
				                }
				                url = null;
				            } catch (IOException ex) {
				            	
				            }
			        }  */
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("status", 0);
		//h.put("cours_list", soapDatainJsonObject);
		h.put("message", "list OK");
		return h;
	}
}
