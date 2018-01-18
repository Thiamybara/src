package com.qualshore.etreasury.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.qualshore.etreasury.dao.ProfileRepository;
import com.qualshore.etreasury.dao.UserRepository;
import com.qualshore.etreasury.entity.Profile;

@RestController
public class ProfileRestController {

	@Autowired
	ProfileRepository profileRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@RequestMapping(value = "/etreasury_project/admin/profile/add", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> addProfile(@RequestParam("id_user_admin") Integer userAdmin,
										 @RequestParam("type") String type,
										 @RequestParam("description") String description) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(userAdmin == null || type.equals("") || description.equals(""))
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs parametres manquants");
		}
		try {
			if(!userRepository.existsByIdUtilisateur(userAdmin))
			{
				h.put("status", -1);
				h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
			}
			else
			{
				Profile profile = new Profile();
				profile.setType(type);
				profile.setDescription(description);
				Profile result = profileRepository.save(profile);
				if(result == null)
				{
					h.put("status", -1);
					h.put("message", "Erreur lors de l'ajout.");
				}
				else
				{
					h.put("status", 0);
					h.put("message", "L'ajout du profil s'est bien effectué.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		return h;
	}
	
	@RequestMapping(value = "/etreasury_project/admin/profile/update", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> updateProfile(@RequestParam("id_user_admin") Integer userAdmin,
											@RequestParam("id_profile") Integer idProfile,
										    @RequestParam("type") String type,
										    @RequestParam("description") String description) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(userAdmin == null || idProfile == null || type.equals("") || description.equals(""))
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs parametres manquants");
		}
		else if(!userRepository.existsByIdUtilisateur(userAdmin))
		{
			h.put("status", -1);
			h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
		}
		else if(!profileRepository.existsByIdProfile(idProfile))
		{
			h.put("status", -1);
			h.put("message", "Le profil n'existe pas.");
		}
		else
		{
			int resultUpdate = profileRepository.updateProfile(type, description, idProfile);
			if(resultUpdate < 0)
			{
				h.put("status", -1);
				h.put("message", "Une erreur est survenue lors de la modification.");
			}
			else
			{
				h.put("status", 0);
				h.put("message", "Le profil est mis à jour avec succès.");
			}
		}
		return h;
	}
	
	@RequestMapping(value = "/etreasury_project/admin/profile/delete", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> deleteProfile(@RequestParam("id_user_admin") Integer userAdmin,
											@RequestParam("id_profile") Integer idProfile) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(userAdmin == null || idProfile == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs parametres manquants");
		}
		else if(!userRepository.existsByIdUtilisateur(userAdmin))
		{
			h.put("status", -1);
			h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
		}
		else if(!profileRepository.existsByIdProfile(idProfile))
		{
			h.put("status", -1);
			h.put("message", "Le profile n'existe pas.");
		}
		else
		{
			profileRepository.delete(idProfile);
			h.put("status", 0);
			h.put("message", "La modification s'est effectuée avec succès.");
		}
		return h;
	}
	
	@RequestMapping(value = "/etreasury_project/admin/profile/list/{idAdmin}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> listProfile( @PathVariable int idAdmin) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		/*
		if(idAdmin = null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs parametres manquants");
		}
		else*/
			if(!userRepository.existsByIdUtilisateur(idAdmin))
		{
			h.put("status", -1);
			h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
		}
		else
		{
			h.put("status", 0);
			h.put("profile_list", profileRepository.findAll());
			h.put("message", "list OK");
		}
		return h;
	}
}
