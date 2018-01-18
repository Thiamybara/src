package com.qualshore.etreasury.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qualshore.etreasury.dao.GroupRepository;
import com.qualshore.etreasury.dao.InstitutionRepository;
import com.qualshore.etreasury.entity.Institution;
import com.qualshore.etreasury.service.UserService;

@RequestMapping("/etreasury_project/admin/institution")
@RestController
public class InstitutionRestController {
	
	@Autowired
	InstitutionRepository iRepository;
	
	@Autowired
	GroupRepository gRepository;
	
	@Autowired
	UserService userService;
	
	/*
	 * add an institution
	 */
	@RequestMapping(value ="/add", method=RequestMethod.POST)
	public HashMap<String, Object> add(@RequestBody Institution institution){
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		//Fields Control
		if(institution == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		//
		if(institution.getNom().equals("") || institution.getTelephone1().equals("") || institution.getLocalityIdLocalite() == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres vides");
			return h;
		}
		if(! userService.isPhoneValid(institution.getTelephone1()))
		{
			h.put("status", -1);
			h.put("message", "Le numéro de téléphone mobile est incorrect.");
			return h;
		}
		if(institution.getTelephone2() != null && !userService.isPhoneValid(institution.getTelephone2()))
		{
			h.put("status", -1);
			h.put("message", "Le numéro de téléphone fixe est incorrect.");
			return h;
		}
		//
				
		try {
			institution = iRepository.save(institution);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("institution", institution);
		h.put("message", "L'institution est ajoutée avec succès.");
		h.put("status", 0);
		return h;
	}
	
	/**
	 * get all institutions
	 * @return
	 */
	@RequestMapping(value="/list", method=RequestMethod.GET)
		public Map<String,Object> getInstitutions(){
		
		List<Institution> institutions = iRepository.findAll();
		HashMap<String, Object> h = new HashMap<String, Object>();
		h.put("message", "liste des institutions");
		h.put("status", 0);
		h.put("institution_list", institutions);
		return h;
	}
	
	/*
	 * activate an instituion
	 */
	@RequestMapping(value="/active/{id}", method=RequestMethod.PUT)
	public HashMap<String, Object> activeInstitution(@PathVariable Integer id){
		HashMap<String, Object> h = new HashMap<>();
		
		//Fields Control
		if(id == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		//
		
		try {
			Institution institution = iRepository.findOne(id);
			if (institution == null) {
				h.put("message", "L'institution n'existe pas.");
				h.put("status", -1);
				return h;
			}
			institution.setIsActive(true);
			iRepository.saveAndFlush(institution);
			h.put("message", "success");
			h.put("status", "0");
			h.put("institution", institution);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		return h;
	}
	
	@RequestMapping(value="/desactive/{id}", method=RequestMethod.PUT)
	public HashMap<String, Object> desactiveInstitution(@PathVariable Integer id){
		HashMap<String, Object> h = new HashMap<>();
		
		//Fields Control
		if(id == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		//
		try {
			Institution institution = iRepository.findOne(id);
			if (institution == null) {
				h.put("message", "L'institution n'existe pas.");
				h.put("status", -1);
				return h;
			}
			institution.setIsActive(false);
			iRepository.saveAndFlush(institution);
			h.put("message", "success");
			h.put("status", "0");
			h.put("institution", institution);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		return h;
	}

	
}
