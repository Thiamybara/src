package com.qualshore.etreasury.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.qualshore.etreasury.dao.DeviseRepository;
import com.qualshore.etreasury.dao.UserRepository;
import com.qualshore.etreasury.entity.Devise;
import com.qualshore.etreasury.service.UserService;

@RequestMapping("etreasury_project/admin/devise")
@RestController
public class DeviseRestController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	DeviseRepository deviseRepository;

	@Autowired
	UserRepository userRepository;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> list()
	{
		HashMap<String, Object> h = new HashMap<String, Object>();
		try
		{
			List<Devise> deviseList = deviseRepository.findAll();
			
			h.put("status", 0);
			h.put("devise_list", deviseList);
			h.put("message", "Liste des devises");
		} catch (Exception e) {
			e.printStackTrace();
			h.put("status", -1);
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			return h;
		}
		return h;
	}
	
	@RequestMapping(value = "/add/{idAdmin}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> add(@RequestBody Devise devise, @PathVariable Integer idAdmin) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(devise == null || idAdmin == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants.");
			h.put("status", -1);
			return h;
		}
		try
		{
			if(!userRepository.isAdminGeneral(idAdmin))
			{
				h.put("status", -1);
				h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
				return h;
			}
			if(deviseRepository.existsByDescription(devise.getDescription()))
			{
				h.put("message", "Le libellé existe déjà.");
				h.put("status", -1);
				return h;
			}
			devise.setDateDevise(new Date());;
			devise = deviseRepository.save(devise);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("devise", devise);
		h.put("message", "La devise est ajoutée avec succès.");
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value = "/update/{idAdmin}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> update(@RequestBody Devise devise, @PathVariable Integer idAdmin) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(devise == null || idAdmin == null )
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
			return h;
		}
		try
		{
			if(!userRepository.isAdminGeneral(idAdmin))
			{
				h.put("status", -1);
				h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
				return h;
			}
			Devise deviseOld = deviseRepository.findOne(devise.getIdDevise());
			if(deviseOld == null)
			{
				h.put("message", "Cette devise n'existe pas.");
				h.put("status", -1);
				return h;
			}
			if(devise.getIdDevise() == 1)
			{
				h.put("message", "La devise CFA ne peut pas être modifiée.");
				h.put("status", -1);
				return h;
			}	
			if(devise.getDescription() == null)
			{
				devise.setDescription(deviseOld.getDescription());
			}
			if(devise.getValeur() == null)
			{
				devise.setValeur(deviseOld.getValeur());
			}
			devise.setDateDevise(new Date());
			devise = deviseRepository.saveAndFlush(devise);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("devise", devise);
		h.put("message", "La devise est mise à jour avec succès.");
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value = "/delete/{idAdmin}/{idDevise}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> delete(@PathVariable Integer idAdmin , @PathVariable Integer idDevise) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		if(idDevise == null || idAdmin == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
			return h;
		}
		try
		{
			Devise devise = deviseRepository.findOne(idDevise);
			if(devise == null)
			{
				h.put("message", "Cette devise n'existe pas.");
				h.put("status", -1);
				return h;
			}
			if(!userRepository.isAdminGeneral(idAdmin))
			{
				h.put("status", -1);
				h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
				return h;
			}
			if(devise.getIdDevise() == 1)
			{
				h.put("message", "La devise CFA ne peut pas être supprimée.");
				h.put("status", -1);
				return h;
			}
			deviseRepository.delete(idDevise);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("status", -1);
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			return h;
		}
		h.put("status", 0);
		h.put("message", "La devise est supprimée avec succès.");
		return h;
	}
}

