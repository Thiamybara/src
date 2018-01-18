package com.qualshore.etreasury.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.qualshore.etreasury.dao.DocumentHasRequestRepository;
import com.qualshore.etreasury.dao.DocumentsRepository;
import com.qualshore.etreasury.dao.RequestRepository;
import com.qualshore.etreasury.dao.UserRepository;
import com.qualshore.etreasury.entity.Documents;
import com.qualshore.etreasury.entity.DocumentsHasRequest;
import com.qualshore.etreasury.entity.Request;
import com.qualshore.etreasury.entity.User;

@RequestMapping("/etreasury_project/mes_operations/entreprise/document_request")
@CrossOrigin(origins = "*")
@RestController
public class DocumentHasRequestRestController {

	@Autowired
	DocumentHasRequestRepository drRepository;
	
	@Autowired
	RequestRepository requestRepository;
	
	@Autowired
	DocumentsRepository documentRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@RequestMapping(value = "/list/{idRequest}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> listByRequest(@PathVariable Integer idRequest) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(idRequest == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
		}
		try
		{
			Request request = requestRepository.findOne(idRequest);
			if(request == null)
			{
				h.put("message", "Cette demande n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			List<DocumentsHasRequest> listDR = drRepository.findAllDocumentByRequest(request);
			h.put("status", 0);
			h.put("documents_request_list", listDR);
			h.put("message", "list OK");
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		return h;
	}
	
	@RequestMapping(value = "/add/{idUser}/{idRequest}/{idDocument}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> add(@PathVariable Integer idUser,
								  @PathVariable Integer idRequest,
								  @PathVariable Integer idDocument) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		DocumentsHasRequest dr;
		if(idUser == null || idRequest == null || idDocument == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
		}
		try
		{
			User user = userRepository.findOne(idUser);
			if(user == null)
			{
				h.put("message", "Cet utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			Request request = requestRepository.findOne(idRequest);
			if(request == null)
			{
				h.put("message", "Cette demande n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			Documents document = documentRepository.findOne(idDocument);
			if(document == null)
			{
				h.put("message", "Ce document n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			dr = new DocumentsHasRequest(document.getIdDocuments(), request.getIdDemande());
			dr.setDateDocument(new Date());
			dr = drRepository.save(dr);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("document_has_request", dr);
		h.put("status", 0);
		h.put("message", "Le document est ajouté avec succès.");
		return h;
	}
	
	
	@RequestMapping(value = "/delete/{idUser}/{idRequest}/{idDocument}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> delete(@PathVariable Integer idUser,
								  	 @PathVariable Integer idRequest,
								  	 @PathVariable Integer idDocument) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		//DocumentsHasRequest dr;
		if(idUser == null || idRequest == null || idDocument == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
		}
		try
		{
			User user = userRepository.findOne(idUser);
			if(user == null)
			{
				h.put("message", "Cet utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			Request request = requestRepository.findOne(idRequest);
			if(request == null)
			{
				h.put("message", "Cette demande n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			Documents document = documentRepository.findOne(idDocument);
			if(document == null)
			{
				h.put("message", "Ce document n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			List<DocumentsHasRequest> listDR = drRepository.findByDocumentRequest(request, document);
			if(listDR == null)
			{
				h.put("message", "Le document n'existe pas.");
				h.put("status", -1);
				return h;
			}
			drRepository.delete(listDR.get(0));
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("status", 0);
		h.put("message", "Le document est supprimé avec succès.");
		return h;
	}
	
}
