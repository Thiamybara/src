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

import com.qualshore.etreasury.dao.ActionRepository;
import com.qualshore.etreasury.dao.GroupRepository;
import com.qualshore.etreasury.dao.InstitutionRepository;
import com.qualshore.etreasury.dao.LogRepository;
import com.qualshore.etreasury.dao.UserAdminRepository;
import com.qualshore.etreasury.dao.UserBankRepository;
import com.qualshore.etreasury.dao.UserEntrepriseRepository;
import com.qualshore.etreasury.dao.UserRepository;
import com.qualshore.etreasury.entity.Groupe;
import com.qualshore.etreasury.entity.Institution;
import com.qualshore.etreasury.entity.Log;
import com.qualshore.etreasury.entity.Profile;
import com.qualshore.etreasury.entity.User;

@RequestMapping("etreasury_project/admin/groupe")
@RestController
public class GroupRestController {

	@Autowired
	GroupRepository groupRepository;
	
	@Autowired
	InstitutionRepository institutionRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserAdminRepository userAdminRepository;
	
	@Autowired
	UserBankRepository userBankRepository;
	
	@Autowired
	UserEntrepriseRepository userEntrepriseRepository;
	
	@Autowired
	LogRepository logRepository;
	
	@Autowired
	ActionRepository actionRepository;
	
	//Add new group by SUPER_ADMIN
	@RequestMapping(value = "admin_general/add/{idAdmin}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> addByAdminGeneral(@RequestBody Groupe groupe, @PathVariable Integer idAdmin) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(idAdmin == null || groupe == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres nuls");
			return h;
		}
		if(groupe.getNom().equals("") || groupe.getDescription().equals("") || groupe.getInstitution() == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		try
		{
			User user = userAdminRepository.findOne(idAdmin);
			if(user == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'est pas un ADMIN_GENERAL.");
				return h;
			}
			
			Institution institution = institutionRepository.findOne(groupe.getInstitution().getIdInstitution());
			if(institution == null)
			{
				h.put("status", -1);
				h.put("message", "L'institution de ce groupe n'existe pas.");
				return h;
			}
			
			/*List<Groupe> listGroupeBefore = groupRepository.findByNom(groupe.getNom());
			if(listGroupeBefore != null)*/
			if(groupRepository.existsByNom(groupe.getNom(), institution))
			{
				h.put("status", -1);
				h.put("message", "Un groupe de la même institution avec le même nom existe déjà.");
				return h;
			}
			groupe.setHasJuridiction(false);
			groupe.setInstitution(institution);
			groupe = groupRepository.save(groupe);
			journaliser(idAdmin, 6, groupe.getIdGroupe(), "By super_admin");
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("groupe", groupe);
		h.put("status", 0);
		h.put("message", "Le groupe est ajouté avec succès.");
		return h;
	}
	
	//Add new group by ADMIN_BANQUE
	@RequestMapping(value = "admin_banque/add/{idAdminBank}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> addByAdminBank(@RequestBody Groupe groupe, @PathVariable Integer idAdminBank) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(idAdminBank == null || groupe == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres nuls");
			return h;
		}
		if(groupe.getNom().equals("") || groupe.getDescription().equals("") || groupe.getInstitution() == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		try
		{
			User userBank = userBankRepository.findOne(idAdminBank);
			if(userBank == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'est pas d'une banque.");
				return h;
			}	
			Profile profile = userBank.getProfilIdProfil();
			if(profile == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'a pas de profil.");
				return h;
			}
			if(!profile.getType().equals("ADMIN_BANQUE"))
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'a pas le profil admin.");
				return h;
			}	
			
			Institution institutionOfGroup = institutionRepository.findOne(groupe.getInstitution().getIdInstitution());
			if(institutionOfGroup == null)
			{
				h.put("status", -1);
				h.put("message", "L'institution n'existe pas.");
				return h;
			}
			if(!institutionOfGroup.getIdInstitution().equals(userBank.getGroupeIdGroupe().getInstitution().getIdInstitution()))
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'a pas la même institution que le groupe.");
				return h;
			}
			
			/*List<Groupe> listGroupeBefore = groupRepository.findByNom(groupe.getNom());
			if(listGroupeBefore != null)*/
			if(groupRepository.existsByNom(groupe.getNom(), institutionOfGroup))
			{
				h.put("status", -1);
				h.put("message", "Un groupe de la même institution avec le même nom existe déjà.");
				return h;
			}
			groupe.setHasJuridiction(false);
			groupe.setInstitution(institutionOfGroup);
			groupe = groupRepository.save(groupe);
			journaliser(idAdminBank, 6, groupe.getIdGroupe(), "By admin_banque");
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("groupe", groupe);
		h.put("status", 0);
		h.put("message", "Le groupe est ajouté avec succès.");
		return h;
	}
	
	//Add new group by ADMIN_ENTREPRISE
	@RequestMapping(value = "admin_entreprise/add/{idAdminEntreprise}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> addByAdminEntreprise(@RequestBody Groupe groupe, @PathVariable Integer idAdminEntreprise) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(idAdminEntreprise == null || groupe == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres nuls");
			return h;
		}
		if(groupe.getNom().equals("") || groupe.getDescription().equals("") || groupe.getInstitution() == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		try
		{
			User userEntreprise = userEntrepriseRepository.findOne(idAdminEntreprise);
			if(userEntreprise == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'est pas d'une entreprise.");
				return h;
			}	
			Profile profile = userEntreprise.getProfilIdProfil();
			if(profile == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'a pas de profil.");
				return h;
			}
			if(!profile.getType().equals("ADMIN_ENTREPRISE"))
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'a pas le profil admin.");
				return h;
			}	
			
			Institution institutionOfGroup = institutionRepository.findOne(groupe.getInstitution().getIdInstitution());
			if(institutionOfGroup == null)
			{
				h.put("status", -1);
				h.put("message", "L'institution n'existe pas.");
				return h;
			}
			if(!institutionOfGroup.getIdInstitution().equals(userEntreprise.getGroupeIdGroupe().getInstitution().getIdInstitution()))
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'est pas de la même institution que le groupe.");
				return h;
			}
			
			/*List<Groupe> listGroupeBefore = groupRepository.findByNom(groupe.getNom());
			if(listGroupeBefore != null)*/
			if(groupRepository.existsByNom(groupe.getNom(), institutionOfGroup))
			{
				h.put("status", -1);
				h.put("message", "Un groupe de la même institution avec le même nom existe déjà.");
				return h;
			}
			
			groupe.setHasJuridiction(false);
			groupe.setInstitution(institutionOfGroup);
			groupe = groupRepository.save(groupe);
			journaliser(idAdminEntreprise, 6, groupe.getIdGroupe(), "By admin_entreprise");
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("groupe", groupe);
		h.put("status", 0);
		h.put("message", "Le groupe est ajouté avec succès.");
		return h;
	}
	
	//Update group by ADMIN_GENERAL
	@RequestMapping(value = "/admin_general/update/{idAdmin}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> updateByAdmin(@RequestBody Groupe groupe, @PathVariable Integer idAdmin) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		if(idAdmin == null || groupe == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres nuls");
			return h;
		}
		if(groupe.getIdGroupe() == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres nuls");
			return h;
		}
		try
		{
			User user = userAdminRepository.findOne(idAdmin);
			if(user == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'est pas un ADMIN_GENERAL.");
				return h;
			}
			
			Groupe groupeOld = groupRepository.findOne(groupe.getIdGroupe());
			if(groupeOld == null)
			{
				h.put("status", -1);
				h.put("message", "Ce groupe n'existe pas.");
				return h;
			}
			
			if(groupe.getDescription() == null)
			{
				groupe.setDescription(groupeOld.getDescription());
			}
			
			if(groupe.getInstitution() == null)
			{
				groupe.setInstitution(groupeOld.getInstitution());
			}
			else
			{
				Institution institution = institutionRepository.findOne(groupe.getInstitution().getIdInstitution());
				groupe.setInstitution(institution);
			}
			
			if(groupe.getNom() == null)
			{
				groupe.setNom(groupeOld.getNom());
			}
			else if(!groupe.getNom().equals(groupeOld.getNom()))
			{
				//if(groupRepository.existsByNom(groupe.getNom(), groupe.getInstitution()))
			    if(groupRepository.existsByNom(groupe.getNom(), groupe.getInstitution(),groupe.getIdGroupe()))
				{
					h.put("status", -1);
					h.put("message", "Un groupe de la même institution avec le même nom existe déjà.");
					return h;
				}
			}
			
			groupe.setHasJuridiction(groupeOld.isHasJuridiction());
			groupe = groupRepository.saveAndFlush(groupe);
			journaliser(idAdmin, 7, groupe.getIdGroupe(), "By super_admin");
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("groupe", groupe);
		h.put("status", 0);
		h.put("message", "Le groupe est mis à jour avec succès.");
		return h;
	}
	
	//Update group by ADMIN_BANQUE
	@RequestMapping(value = "admin_banque/update/{idAdminBank}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> updateByAdminBanque(@RequestBody Groupe groupe, @PathVariable Integer idAdminBank) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		if(idAdminBank == null || groupe == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres nuls");
			return h;
		}
		if(groupe.getIdGroupe() == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres nuls");
			return h;
		}
		try
		{
			User userBank = userBankRepository.findOne(idAdminBank);
			if(userBank == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'est pas d'une banque.");
				return h;
			}	
			Profile profile = userBank.getProfilIdProfil();
			if(profile == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'a pas de profil.");
				return h;
			}
			if(!profile.getType().equals("ADMIN_BANQUE"))
			{
				h.put("status", -1);
				h.put("message", "cet utilisateur n'a pas le profil admin.");
				return h;
			}
			
			Groupe groupeOld = groupRepository.findOne(groupe.getIdGroupe());
			if(groupeOld == null)
			{
				h.put("status", -1);
				h.put("message", "Ce groupe n'existe pas.");
				return h;
			}
			
			if(!groupeOld.getInstitution().getIdInstitution().equals(userBank.getGroupeIdGroupe().getInstitution().getIdInstitution()))
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'est pas de la même banque que le groupe.");
				return h;
			}
			
			if(groupe.getDescription() == null)
			{
				groupe.setDescription(groupeOld.getDescription());
			}
			
			if(groupe.getInstitution() == null)
			{
				groupe.setInstitution(groupeOld.getInstitution());
			}
			else
			{
				Institution institution = institutionRepository.findOne(groupe.getInstitution().getIdInstitution());
				groupe.setInstitution(institution);
			}
			
			if(groupe.getNom() == null)
			{
				groupe.setNom(groupeOld.getNom());
			}
			else if(groupRepository.existsByNom(groupe.getNom(), groupe.getInstitution(),groupe.getIdGroupe()))
			{
				h.put("status", -1);
				h.put("message", "Un groupe de la même institution avec le même nom existe déjà.");
				return h;
			}
			groupe.setHasJuridiction(groupeOld.isHasJuridiction());
			groupe = groupRepository.saveAndFlush(groupe);
			journaliser(idAdminBank, 7, groupe.getIdGroupe(), "By admin_banque");
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("groupe", groupe);
		h.put("status", 0);
		h.put("message", "Le groupe est mis à jour avec succès.");
		return h;
	}
	
	//Update group by ADMIN_ENTREPRISE
	@RequestMapping(value = "/admin_entreprise/update/{idAdminEntreprise}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> updateByAdminEntreprise(@RequestBody Groupe groupe, @PathVariable Integer idAdminEntreprise) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		if(idAdminEntreprise == null || groupe == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres nuls");
			return h;
		}
		if(groupe.getIdGroupe() == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres nuls");
			return h;
		}
		try
		{
			User userEntreprise = userEntrepriseRepository.findOne(idAdminEntreprise);
			if(userEntreprise == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'est pas d'une entreprise.");
				return h;
			}	
			Profile profile = userEntreprise.getProfilIdProfil();
			if(profile == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'a pas de profil.");
				return h;
			}
			if(!profile.getType().equals("ADMIN_ENTREPRISE"))
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'a pas le profil admin.");
				return h;
			}
			
			Groupe groupeOld = groupRepository.findOne(groupe.getIdGroupe());
			if(groupeOld == null)
			{
				h.put("status", -1);
				h.put("message", "Ce groupe n'existe pas.");
				return h;
			}
			
			if(!groupeOld.getInstitution().getIdInstitution().equals(userEntreprise.getGroupeIdGroupe().getInstitution().getIdInstitution()))
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'est pas de la même entreprise que le groupe.");
				return h;
			}
			
			if(groupe.getDescription() == null)
			{
				groupe.setDescription(groupeOld.getDescription());
			}
			
			if(groupe.getInstitution() == null)
			{
				groupe.setInstitution(groupeOld.getInstitution());
			}
			else {
				Institution institution = institutionRepository.findOne(groupe.getInstitution().getIdInstitution());
				groupe.setInstitution(institution);
			}
			
			if(groupe.getNom() == null)
			{
				groupe.setNom(groupeOld.getNom());
			}
			
			else if(groupRepository.existsByNom(groupe.getNom(), groupe.getInstitution(),groupe.getIdGroupe()))
			{
				h.put("status", -1);
				h.put("message", "Un groupe de la même institution avec le même nom existe déjà.");
				return h;
			}
			groupe.setHasJuridiction(groupeOld.isHasJuridiction());
			groupe = groupRepository.saveAndFlush(groupe);
			journaliser(idAdminEntreprise, 7, groupe.getIdGroupe(), "By admin_entreprise");
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("groupe", groupe);
		h.put("status", 0);
		h.put("message", "Le groupe est mis à jour avec succès.");
		return h;
	}
	
	//Delete group by ADMIN_GENERAL
	@RequestMapping(value = "/admin_general/delete/{idAdmin}/{idGroupe}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> deleteByAdminGeneral(@PathVariable Integer idAdmin, @PathVariable Integer idGroupe) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		if(idAdmin == null || idGroupe == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		try
		{
			User user = userAdminRepository.findOne(idAdmin);
			if(user == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'est pas un ADMIN_GENERAL.");
				return h;
			}
			Groupe g = groupRepository.findOne(idGroupe);
			if(g == null)
			{
				h.put("status", -1);
				h.put("message", "Ce groupe n'existe pas.");
				return h;
			}
			Integer idInstitution = g.getInstitution().getIdInstitution();
			Groupe defaultGroupe = institutionRepository.getDefaultGroupByIdInstitution(idInstitution);
			int result = userRepository.updateGroupDelete(defaultGroupe, g);
			if(result >= 0)
			{
				groupRepository.delete(idGroupe);
				h.put("status", 0);
				h.put("message", "Le groupe est supprimé avec succès.");
				journaliser(idAdmin, 8, idGroupe, "By super_admin");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		return h;
	}
	
	//Delete group by ADMIN_BANQUE
	@RequestMapping(value = "/admin_banque/delete/{idAdminBank}/{idGroupe}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> deleteByAdminBanque(@PathVariable Integer idAdminBank, @PathVariable Integer idGroupe) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		if(idAdminBank == null || idGroupe == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		try
		{
			User userBank = userBankRepository.findOne(idAdminBank);
			if(userBank == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'est pas d'une banque.");
				return h;
			}	
			Profile profile = userBank.getProfilIdProfil();
			if(profile == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'a pas de profil.");
				return h;
			}
			if(!profile.getType().equals("ADMIN_BANQUE"))
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'a pas le profil admin.");
				return h;
			}
			Groupe g = groupRepository.findOne(idGroupe);
			if(g == null)
			{
				h.put("status", -1);
				h.put("message", "Ce groupe n'existe pas.");
				return h;
			}
			Institution institutionOfGroup = institutionRepository.findOne(g.getInstitution().getIdInstitution());
			if(institutionOfGroup == null)
			{
				h.put("status", -1);
				h.put("message", "L'institution n'existe pas.");
				return h;
			}
			if(!institutionOfGroup.getIdInstitution().equals(userBank.getGroupeIdGroupe().getInstitution().getIdInstitution()))
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'est pas de la même banque que le groupe.");
				return h;
			}
			
			Integer idInstitution = g.getInstitution().getIdInstitution();
			Groupe defaultGroupe = institutionRepository.getDefaultGroupByIdInstitution(idInstitution);
			int result = userRepository.updateGroupDelete(defaultGroupe, g);
			if(result >= 0)
			{
				groupRepository.delete(idGroupe);
				h.put("status", 0);
				h.put("message", "Le groupe est supprimé avec succès.");
				journaliser(idAdminBank, 8, idGroupe, "By admin_banque");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		return h;
	}
	
	//Delete group by ADMIN_ENTREPRISE
	@RequestMapping(value = "/admin_entreprise/delete/{idAdminEntreprise}/{idGroupe}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> deleteByAdminEntreprise(@PathVariable Integer idAdminEntreprise, @PathVariable Integer idGroupe) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		if(idAdminEntreprise == null || idGroupe == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		try
		{
			User userEntreprise = userEntrepriseRepository.findOne(idAdminEntreprise);
			if(userEntreprise == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'est pas d'une entreprise.");
				return h;
			}	
			Profile profile = userEntreprise.getProfilIdProfil();
			if(profile == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'a pas de profil.");
				return h;
			}
			if(!profile.getType().equals("ADMIN_ENTREPRISE"))
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'a pas le profil admin.");
				return h;
			}
			Groupe g = groupRepository.findOne(idGroupe);
			if(g == null)
			{
				h.put("status", -1);
				h.put("message", "Ce groupe n'existe pas.");
				return h;
			}
			Institution institutionOfGroup = institutionRepository.findOne(g.getInstitution().getIdInstitution());
			if(institutionOfGroup == null)
			{
				h.put("status", -1);
				h.put("message", "institution inexistante");
				return h;
			}
			if(!institutionOfGroup.getIdInstitution().equals(userEntreprise.getGroupeIdGroupe().getInstitution().getIdInstitution()))
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'est pas de la même entreprise que le groupe.");
				return h;
			}
			
			Integer idInstitution = g.getInstitution().getIdInstitution();
			Groupe defaultGroupe = institutionRepository.getDefaultGroupByIdInstitution(idInstitution);
			int result = userRepository.updateGroupDelete(defaultGroupe, g);
			if(result >= 0)
			{
				groupRepository.delete(idGroupe);
				h.put("status", 0);
				h.put("message", "Le groupe est supprimé avec succès.");
				journaliser(idAdminEntreprise, 8, idGroupe, "By admin_entreprise");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		return h;
	}
	
	// Etreasury group list by ADMIN_GENERAL
	@RequestMapping(value = "/admin_general/etreasury/list/{idAdmin}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> listAdminG(@PathVariable Integer idAdmin)
	{
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		if(idAdmin == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		try
		{
			User user = userAdminRepository.findOne(idAdmin);
			if(user == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'est pas un ADMIN_GENERAL.");
				return h;
			}
			List<Groupe> groupeList = groupRepository.findAllByTypeET();
			h.put("status", 0);
			h.put("group_list_ET", groupeList);
			h.put("message", "list OK");
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		return h;
	}
	
	// Bank group list by ADMIN_GENERAL
	@RequestMapping(value = "/admin_general/banque/list/{idAdmin}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> listAdminBank(@PathVariable Integer idAdmin)
	{
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		if(idAdmin == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		try
		{
			User user = userAdminRepository.findOne(idAdmin);
			if(user == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'est pas un ADMIN_GENERAL.");
				return h;
			}
			List<Groupe> groupeList = groupRepository.findAllByTypeBA();
			h.put("status", 0);
			h.put("group_list_ET", groupeList);
			h.put("message", "list OK");
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		return h;
	}
	
	// Entreprise group list by ADMIN_GENERAL
	@RequestMapping(value = "/admin_general/entreprise/list/{idAdmin}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> listAdminEntreprise(@PathVariable Integer idAdmin)
	{
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		if(idAdmin == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		try
		{
			User user = userAdminRepository.findOne(idAdmin);
			if(user == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'est pas un ADMIN_GENERAL.");
				return h;
			}
			List<Groupe> groupeList = groupRepository.findAllByTypeEN();
			h.put("status", 0);
			h.put("group_list_ET", groupeList);
			h.put("message", "list OK");
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		return h;
	}
	
	// Bank group list by ADMIN_BANQUE
	@RequestMapping(value = "/admin_banque/list/{idAdmin}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> listBank(@PathVariable Integer idAdmin)
	{
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		if(idAdmin == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		try
		{
			User userBank = userBankRepository.findOne(idAdmin);
			if(userBank == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'est pas d'une banque.");
				return h;
			}
			
			Profile profile = userBank.getProfilIdProfil();
			if(profile == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'a pas de profil.");
				return h;
			}
			if(!profile.getType().equals("ADMIN_BANQUE"))
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'a pas le profil admin.");
				return h;
			}
			
			Groupe groupe = userBank.getGroupeIdGroupe();
			if(groupe == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'a pas de groupe.");
				return h;
			}
			
			Institution institution = groupe.getInstitution();
			if(institution == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'a pas d'institution.");
				return h;
			}
			
			/*if(!userBank.getDiscriminatorValue().equals("BA"))
			{
				h.put("status", -1);
				h.put("message", "cet utilisateur n'a pas d'institution");
				return h;
			}*/
			
			List<Groupe> groupeList = groupRepository.findByInstitution(institution);
			h.put("status", 0);
			h.put("group_list", groupeList);
			h.put("message", "list OK");
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		return h;
	}
	
	// Enterprise group list by ADMIN_ENTREPRISE
	@RequestMapping(value = "/admin_entreprise/list/{idAdmin}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> listEntreprise(@PathVariable Integer idAdmin)
	{
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		if(idAdmin == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		try
		{
			User userEntreprise = userEntrepriseRepository.findOne(idAdmin);
			if(userEntreprise == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'est pas d'une entreprise.");
				return h;
			}
			
			Profile profile = userEntreprise.getProfilIdProfil();
			if(profile == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'a pas de profil.");
				return h;
			}
			if(!profile.getType().equals("ADMIN_ENTREPRISE"))
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'a pas le profil admin.");
				return h;
			}
			
			Groupe groupe = userEntreprise.getGroupeIdGroupe();
			if(groupe == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'a pas de groupe.");
				return h;
			}
			
			Institution institution = groupe.getInstitution();
			if(institution == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'a pas d'institution.");
				return h;
			}
			
			/*if(!userEntreprise.getDiscriminatorValue().equals("EN"))
			{
				h.put("status", -1);
				h.put("message", "cet utilisateur n'a pas d'institution");
				return h;
			}*/
			
			List<Groupe> groupeList = groupRepository.findByInstitution(institution);
			h.put("status", 0);
			h.put("group_list", groupeList);
			h.put("message", "list OK");
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		return h;
	}
	
	public void journaliser(Integer idUser, Integer idAction, Integer idGroup, String description) {
		Log log = new Log();
		log.setIdUser(idUser);
		log.setIdGroup(idGroup);
		log.setDescription(description);
		log.setDate(new Date());
		log.setActionIdAction(actionRepository.findOne(idAction));
		logRepository.save(log);
	}
	
}