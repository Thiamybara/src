package com.qualshore.etreasury.rest;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qualshore.etreasury.dao.DeviseRepository;
import com.qualshore.etreasury.dao.GroupRepository;
import com.qualshore.etreasury.dao.InstitutionRepository;
import com.qualshore.etreasury.dao.NotificationsRepository;
import com.qualshore.etreasury.dao.UserRepository;
import com.qualshore.etreasury.dao.ValidationLevelGroupeRepository;
import com.qualshore.etreasury.dao.ValidationLevelRepository;
import com.qualshore.etreasury.entity.Devise;
import com.qualshore.etreasury.entity.Groupe;
import com.qualshore.etreasury.entity.Institution;
import com.qualshore.etreasury.entity.Notifications;
import com.qualshore.etreasury.entity.Products;
import com.qualshore.etreasury.entity.User;
import com.qualshore.etreasury.entity.ValidationLevel;
import com.qualshore.etreasury.entity.ValidationLevelGroupe;
import com.qualshore.etreasury.entity.ValidationLevelGroupePK;

@RestController
@RequestMapping("/etreasury_project/validation_level_groupe")
public class ValidationLevelGroupeRestController {
	
	@Autowired
	ValidationLevelRepository vLRep;
	
	@Autowired
	GroupRepository gRep;
	
	@Autowired
	ValidationLevelGroupeRepository vLGRep;
	
	@Autowired
	InstitutionRepository iRep;
	
	@Autowired
	UserRepository uRep;
	
	@Autowired
	NotificationsRepository notificationRep;
	
	@Autowired
	DeviseRepository deviseRepository;
	
	/*
	 * get all validationLevelGroup per institution
	 */
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public HashMap<String, Object> getAlls(){
		
		HashMap<String, Object> h= new HashMap<>();
		
		List<ValidationLevelGroupe> list = vLGRep.findAll();
		h.put("message", "success");
		h.put("validation_level_groupe_list", list);
		h.put("status", 0);
		return h;
	}
	
	/*
	 * get all validationLevelGroupe per institution
	 */
	@RequestMapping(value="/list/{idInstitution}", method=RequestMethod.GET)
	public HashMap<String, Object> getAllsByInstitution(@PathVariable Integer idInstitution){
		
		HashMap<String, Object> h= new HashMap<>();
		List<ValidationLevelGroupe> list;
		
		if(idInstitution == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);		
			return h;
		}
		try {
			Institution institution = iRep.findOne(idInstitution);
			if(institution == null)
			{
				h.put("message", "Cette instiutution n'existe pas.");
				h.put("status", -1);		
				return h;
			}
			list = vLGRep.findByInstitution(institution);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "success");
		h.put("validation_level_groupe_list", list);
		//h.put("validation_level_groupe", vLG);
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value="/add/{idUser}", method= RequestMethod.POST)
	public HashMap<String, Object> addValidationLevelGroupe(@RequestBody ValidationLevelGroupe vLG, @PathVariable Integer idUser){
		
		HashMap<String, Object> h= new HashMap<>();
		if(vLG == null || vLG.getValidationLevel() == null || vLG.getGroupe() == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);		
			return h;
		}
		try {
			User user = uRep.findOne(idUser);
			if (user == null)
			{
				h.put("message", "Cet utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			ValidationLevel validationLevel = vLRep.findOne(vLG.getValidationLevel().getIdNiveauValidation());
			if(validationLevel == null)
			{
				h.put("message", "Le niveau de validation associé n'existe pas.");
				h.put("status", -1);		
				return h;
			}
			if(validationLevel.getIsValidChain())
			{
				h.put("message", "Cette validation a déjà été effectuée.");
				h.put("status", -1);		
				return h;
			}
			Groupe groupe = gRep.findOne(vLG.getGroupe().getIdGroupe());
			if(groupe == null)
			{
				h.put("message", "Le groupe associé n'existe pas.");
				h.put("status", -1);		
				return h;
			}
			/*Devise devise = deviseRepository.findOne(vLG.getDevise().getIdDevise());
			if(devise == null)
			{
				h.put("message", "La devise n'existe pas.");
				h.put("status", -1);		
				return h;
			}
			vLG.setDevise(devise);*/
			if(validationLevel.getInstitution().getIdInstitution() != groupe.getInstitution().getIdInstitution())
			{
				h.put("message", "Le niveau de validation et le groupe associés ne sont pas de la même institution.");
				h.put("status", -1);		
				return h;
			}
			if(vLGRep.countByValidationLevel(validationLevel) >= validationLevel.getNombreValidation())
			{
				h.put("message", "Le nombre maximum de validations est atteint.");
				h.put("status", -1);		
				return h;
			}
			if(vLGRep.existsByValidationLevelAndNiveau(validationLevel, vLG.getNiveau()))
			{
				h.put("message", "La validation avec ce niveau existe déjà.");
				h.put("status", -1);		
				return h;
			}
			if(vLGRep.existsByValidationLevelAndGroupe(validationLevel, groupe))
			{
				h.put("message", "La validation avec ce groupe existe déjà.");
				h.put("status", -1);	
				return h;
			}
			if(vLG.getNiveau() == 0 || vLG.getNiveau() > validationLevel.getNombreValidation())
			{
				h.put("message", "Le niveau de validation doit etre > 0 et < "+ validationLevel.getNombreValidation());
				h.put("status", -1);
				return h;
			}
			/*if(! validationLevel.getAllsRequired())
			{
				validationLevel.setIsValidChain(true);
				vLRep.saveAndFlush(validationLevel);
			}
			else */
			if(vLGRep.countByValidationLevel(validationLevel)+1 == validationLevel.getNombreValidation())
			{
				validationLevel.setIsValidChain(true);
				vLRep.saveAndFlush(validationLevel);
			}
		
			/*vLG.setValidationLevel(validationLevel);
			vLG.setGroupe(groupe);*/
			vLG.setValidationLevelGroupePK(new ValidationLevelGroupePK(validationLevel.getIdNiveauValidation(), groupe.getIdGroupe()));
			
			//addNotifications(validationLevel.getInstitution(), validationLevel.getProduct(), groupe);
			
			vLG = vLGRep.save(vLG);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "La validation est ajoutée avec succès.");
		h.put("validation_level_group", vLG);
		h.put("status", 0);
		return h;
	}
	
	public void addNotifications(Institution institution, Products product,Groupe groupe) throws Exception {
        List<User> users = uRep.findByGroupeIdGroupe(groupe);
        for(User user:users)
        {
            Notifications notification = new Notifications(product, user, institution);
            notificationRep.save(notification);
            //
            //this.taskRepository.add(notification);
        }
    }
	
	@RequestMapping(value="/update/{idUser}/{idGroupeOld}", method= RequestMethod.PUT)
	public HashMap<String, Object> updateValidationLevelGroupe(@RequestBody ValidationLevelGroupe vLG,
															   @PathVariable Integer idUser,
															   @PathVariable Integer idGroupeOld) {
		
		HashMap<String, Object> h= new HashMap<>();
		if(vLG == null || idUser == null || idGroupeOld == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);		
			return h;
		}
		try {
			User user = uRep.findOne(idUser);
			if (user == null)
			{
				h.put("message", "Cet utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			Groupe groupeOld = gRep.findOne(idGroupeOld);
			if (groupeOld == null)
			{
				h.put("message", "Le groupe associé n'existe pas.");
				h.put("status", -1);
				return h;
			}
			ValidationLevel vl = vLRep.findOne(vLG.getValidationLevel().getIdNiveauValidation());
			if (vl == null)
			{
				h.put("message", "Le niveau de validation associé n'existe pas.");
				h.put("status", -1);
				return h;
			}
			ValidationLevelGroupe vLGOld = vLGRep.findByValidationLevelAndGroupe(vl, groupeOld);
			if (vLGOld == null)
			{
				h.put("message", "Le couple (niveau de validation, groupe) n'existe pas.");
				h.put("status", -1);
				return h;
			}
			Groupe groupeNew = gRep.findOne(vLG.getGroupe().getIdGroupe());
			if (groupeNew == null)
			{
				h.put("message", "Le nouveau groupe n'existe pas.");
				h.put("status", -1);
				return h;
			}
			/*if(vLGRep.existsByValidationLevelAndGroupe(vl, groupeNew))
			{
				h.put("message", "ce groupe existe deja avec un autre niveau");
				h.put("status", -1);
				return h;
			}*/
			if (groupeOld.getIdGroupe() != groupeNew.getIdGroupe()) {
                if(vLGRep.existsByValidationLevelAndGroupe(vl, groupeNew))
                {
                    h.put("message", "Ce groupe existe deja avec un autre niveau.");
                    h.put("status", -1);
                    return h;
                }
            }
			if(groupeNew.getInstitution().getIdInstitution() != vl.getInstitution().getIdInstitution())
			{
				h.put("message", "Le groupe et le niveau de validation ne sont pas de la meme institution.");
				h.put("status", -1);
				return h;
			}
			
			if(vLG.getDevise() == null)
				vLG.setDevise(vLGOld.getDevise());
			if(vLG.getValeurMax() == null)
				vLG.setValeurMax(vLGOld.getValeurMax());
			if(vLG.getValeurMin() == null)
				vLG.setValeurMin(vLGOld.getValeurMin());
			
			vLG.setNiveau(vLGOld.getNiveau());
			vLG.setGroupe(groupeNew);
			vLG.setValidationLevel(vl);
			vLG.setValidationLevelGroupePK(new ValidationLevelGroupePK(vl.getIdNiveauValidation(), groupeNew.getIdGroupe()));
			
			vLGRep.delete(vLGOld);
			vLG = vLGRep.saveAndFlush(vLG);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "La modification s'est effectuée avec succès.");
		h.put("validation_level_groupe", vLG);
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value="/groupe/{idGr}/{idVl}", method=RequestMethod.GET)
	public HashMap<String, Object> getValidationLevelGroupe(@PathVariable int idGr, @PathVariable int idVl){
		
		HashMap<String, Object> h= new HashMap<>();
		
		ValidationLevel vl = vLRep.findOne(idVl);
		if (vl == null) {
			h.put("message", "Le niveau de validation n'existe pas.");
			h.put("status", -1);
			return h;
		}
		Groupe gr = gRep.findOne(idGr);
		if (gr == null)
		{
			h.put("message", "Le groupe n'existe pas.");
			h.put("status", -1);
			return h;
		}
		ValidationLevelGroupe vLG = vLGRep.findByValidationLevelAndGroupe(vl, gr);
		if (vLG == null)
		{
			h.put("message", "Le groupe level validation n'existe pas.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "success");
		h.put("validation_level_groupe", vLG);
		h.put("status", 0);
		return h;
	}
	
	/*@RequestMapping(value="/delete/{idUser}/{idVLG}", method=RequestMethod.DELETE)
	public HashMap<String, Object> delete(@PathVariable Integer idUser, @PathVariable Integer idVLG){
		
		HashMap<String, Object> h= new HashMap<>();
		if(idUser == null || idVLG == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);		
			return h;
		}
		
		try {
			User user = uRep.findOne(idUser);
			if (user == null)
			{
				h.put("message", "l'utlisateur n'existe pas");
				h.put("status", -1);
				return h;
			}
			
			ValidationLevelGroupe validationLevelGroupe = vLGRep.findOne(idVLG);
			if (validationLevelGroupe == null)
			{
				h.put("message", "le niveau de validation n'existe pas");
				h.put("status", -1);
				return h;
			}
			ValidationLevel validationLevel = vLRep.findOne(validationLevelGroupe.getValidationLevel().getIdNiveauValidation());
			if(validationLevel == null)
			{
				h.put("message", "le niveau de validation associé n'existe pas");
				h.put("status", -1);		
				return h;
			}
			validationLevel.setIsValidChain(false);
			vLRep.saveAndFlush(validationLevel);
			
			vLGRep.delete(validationLevelGroupe);
		
		} catch (Exception e) {
			h.put("status", -1);
			h.put("message", e.getMessage());
			return h;
		}
		h.put("message", "niveau de validation supprimé avec succès");
		h.put("status", 0);		
		return h;
	}*/

	@RequestMapping(value="/delete/{idAdmin}/{idGr}/{idVl}", method = RequestMethod.DELETE)
	public HashMap<String, Object> deleteValidationLevelGroupe(@PathVariable Integer idAdmin, @PathVariable Integer idGr,@PathVariable Integer idVl){
		
		HashMap<String, Object> h = new HashMap<>();
		if(idAdmin == null || idGr == null || idVl == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);		
			return h;
		}
		try {
			ValidationLevel vl = vLRep.findOne(idVl);
			if (vl == null) {
				h.put("message", "Le niveau de validation n'existe pas.");
				h.put("status", -1);
				return h;
			}
		
			Groupe gr = gRep.findOne(idGr);
			if (gr == null) {
				h.put("message", "Le groupe n'existe pas.");
				h.put("status", -1);
				return h;
			}
			ValidationLevelGroupe vLG = vLGRep.findByValidationLevelAndGroupe(vl, gr);
			if (vLG == null) {
				h.put("message", "Le groupe level validation n'existe pas.");
				h.put("status", -1);
				return h;
			}
			vLGRep.delete(vLG);
			vl.setIsValidChain(false);
			vLRep.saveAndFlush(vl);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "Le niveau de validation est supprimé avec succès.");
		h.put("status", 0);
		return h;
	}
}
