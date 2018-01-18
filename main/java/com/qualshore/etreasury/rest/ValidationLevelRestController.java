package com.qualshore.etreasury.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qualshore.etreasury.dao.GroupRepository;
import com.qualshore.etreasury.dao.InstitutionRepository;
import com.qualshore.etreasury.dao.ProductsRepository;
import com.qualshore.etreasury.dao.RequestRepository;
import com.qualshore.etreasury.dao.UserRepository;
import com.qualshore.etreasury.dao.ValidationLevelGroupeRepository;
import com.qualshore.etreasury.dao.ValidationLevelRepository;
import com.qualshore.etreasury.entity.Groupe;
import com.qualshore.etreasury.entity.Institution;
import com.qualshore.etreasury.entity.Products;
import com.qualshore.etreasury.entity.User;
import com.qualshore.etreasury.entity.ValidationLevel;
import com.qualshore.etreasury.entity.ValidationLevelGroupe;

@RestController
@RequestMapping("/etreasury_project/validation_level")
public class ValidationLevelRestController {
	
	@Autowired
	ValidationLevelRepository vLevelRep;
	
	@Autowired
	UserRepository userRep;
	
	@Autowired
	ValidationLevelGroupeRepository vLevelGroupRep;
	
	@Autowired
	InstitutionRepository institutionRep;
	
	@Autowired
	ProductsRepository productRep;
	
	@Autowired
	GroupRepository groupRep;
	
	@Autowired
	RequestRepository requestRep;	
	
	/*
	 * get all validationLevel
	 */
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public HashMap<String, Object> getValidationLevel(){
		
		HashMap<String, Object> h= new HashMap<>();
		List<ValidationLevel> vls = vLevelRep.findAll();
		h.put("message", "success");
		h.put("list_validation_level", vls);
		h.put("status", 0);		
		return h;
	}
	
	/*
	 * get one validationLevel
	 */
	@RequestMapping(value="/list/{idVl}", method=RequestMethod.GET)
	public HashMap<String, Object> getValidationLevelByid(@PathVariable Integer idVl){
		
		HashMap<String, Object> h= new HashMap<>();
		ValidationLevel vl = vLevelRep.findOne(idVl);
		if (vl == null) {
			h.put("message", "Le niveau de validation n'existe pas.");
			h.put("status", -1);		
			return h;
		}
		h.put("message", "success");
		h.put("validation_level", vl);
		h.put("status", 0);		
		return h;
	}
	
	/*
	 * get all validationLevel per institution
	 */
	@RequestMapping(value="/list_institution/{idInstitution}", method=RequestMethod.GET)
	public HashMap<String, Object> getValidationLevelByInstitution(@PathVariable Integer idInstitution){
		
		HashMap<String, Object> h= new HashMap<>();
		List<ValidationLevel> vlevelList;
		
		if(idInstitution == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);		
			return h;
		}
		try {
			Institution institution = institutionRep.findOne(idInstitution);
			if(institution == null)
			{
				h.put("message", "Cette institution n'existe pas.");
				h.put("status", -1);		
				return h;
			}
			
			vlevelList = vLevelRep.findByInstitution(institution);
			if (vlevelList == null || vlevelList.isEmpty()) {
				h.put("message", "Le niveau de validation n'existe pas.");
				h.put("status", -1);		
				return h;
			}
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "success");
		h.put("validation_level_list", vlevelList);
		h.put("status", 0);		
		return h;
	}
	
	/*
	 * get all validationLevel per institution
	 */
	@RequestMapping(value="/niveau_validation/{idUser}/{idProduct}", method=RequestMethod.GET)
	public HashMap<String, Object> getValidationLevelByUserAndProduct(@PathVariable Integer idUser, @PathVariable Integer idProduct){
		
		HashMap<String, Object> h= new HashMap<>();
		ValidationLevelGroupe vlg;
		
		if(idUser == null || idProduct == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);		
			return h;
		}
		try {
			User user = userRep.findOne(idUser);
			if (user == null)
            {
                h.put("message", "L'utilisateur n'existe pas.");
                h.put("status", -1);
                return h;
            }
			Groupe groupe = groupRep.findOne(user.getGroupeIdGroupe().getIdGroupe());
			if(groupe == null)
			{
				h.put("message", "Le groupe de l'utilisateur n'existe pas.");
				h.put("status", -1);		
				return h;
			}
			Products product = productRep.findOne(idProduct);
			if (product == null)
			{
				h.put("message", "Le produit n'existe pas.");
				h.put("status", -1);
				return h;
			}
			Institution institution = institutionRep.findOne(groupe.getInstitution().getIdInstitution());
			if(institution == null)
			{
				h.put("message", "L'institution n'existe pas.");
				h.put("status", -1);		
				return h;
			}
			ValidationLevel validationLevel = vLevelRep.findByInstitutionAndProduct(institution, product);
			if(validationLevel == null)
			{
				h.put("message", "Ce niveau de validation n'existe pas.");
				h.put("status", -1);		
				return h;
			}
			vlg = vLevelGroupRep.findByValidationLevelAndGroupe(validationLevel, groupe);
			if(vlg == null)
			{
				h.put("message", "Aucun niveau de validation trouvé.");
				h.put("status", -1);		
				return h;
			}
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "success");
		h.put("validation_level", vlg);
		h.put("status", 0);	
		return h;
	}
	
	/*
	 * add a validation level
	 */
	@RequestMapping(value="/add/{idUser}", method=RequestMethod.POST)
	public HashMap<String, Object> add(@RequestBody ValidationLevel validationLevel, @PathVariable Integer idUser){
		
		HashMap<String, Object> h= new HashMap<>();
		
		if(idUser == null || validationLevel == null || validationLevel.getInstitution() == null || validationLevel.getProduct() == null || validationLevel.getSens() == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);		
			return h;
		}
		
		try {
			User user = userRep.findOne(idUser);
			if (user == null)
			{
				h.put("message", "L'utilisateur associé n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			Institution institution = institutionRep.findOne(validationLevel.getInstitution().getIdInstitution());
			if(institution == null)
			{
				h.put("message", "L'institution associée n'existe pas.");
				h.put("status", -1);		
				return h;
			}
			
			Products product = productRep.findOne(validationLevel.getProduct().getIdProduits());
			if(product == null)
			{
				h.put("message", "Le produit associé n'existe pas.");
				h.put("status", -1);		
				return h;
			}
			
			if (vLevelRep.existsByProductAndInstitution(product, institution,validationLevel.getSens()))
			{
				h.put("message", "Le niveau de validation du produit pour cette entreprise  doit être unique.");
				h.put("status", -1);
				return h;
			}
			
			if (validationLevel.getNombreValidation() <= 0)
			{
				h.put("message", "Le nombre de validation doit être > 0.");
				h.put("status", -1);
				return h;
			}
		
			validationLevel.setDate(new Date());
			validationLevel.setIsValidChain(false);
			validationLevel = vLevelRep.save(validationLevel);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "La chaine de validation est ajoutée avec succès.");
		h.put("validation_level", validationLevel);
		h.put("status", 0);
		return h;
	}
	
	/*
	 * Update validation
	 */
	@RequestMapping(value="/update/{idUser}", method=RequestMethod.PUT)
	public HashMap<String, Object> addChaineValidation(@RequestBody ValidationLevel validationLevel, 
													   @PathVariable Integer idUser) {
		
		HashMap<String, Object> h= new HashMap<String, Object>();
		
		if(idUser == null || validationLevel == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);		
			return h;
		}
		try {
			User user = userRep.findOne(idUser);
			if (user == null)
			{
				h.put("message", "L'utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			ValidationLevel validationLevelOld = vLevelRep.findOne(validationLevel.getIdNiveauValidation());
			if (validationLevelOld == null)
			{
				h.put("message", "Le niveau de validation n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			if(validationLevel.getAllsRequired() == null)
				validationLevel.setAllsRequired(validationLevelOld.getAllsRequired());
			if(validationLevel.getNotificationSms() == null)
				validationLevel.setNotificationSms(validationLevelOld.getNotificationSms());
			if(validationLevel.getSens() == null)
				validationLevel.setSens(validationLevelOld.getSens());
			if(validationLevel.getTypeValidation() == null)
				validationLevel.setTypeValidation(validationLevelOld.getTypeValidation());
			if(validationLevel.getNombreValidation() == null)
				validationLevel.setNombreValidation(validationLevelOld.getNombreValidation());
			if(validationLevel.getIsValidChain() == null)
				validationLevel.setIsValidChain(validationLevelOld.getIsValidChain());
			if(validationLevel.getProduct() == null)
				validationLevel.setProduct(validationLevelOld.getProduct());
			else {
				Products product = productRep.findOne(validationLevel.getProduct().getIdProduits());
				if(product == null)
				{
					h.put("message", "Le produit associé n'existe pas.");
					h.put("status", -1);		
					return h;
				}
				validationLevel.setProduct(product);
			}
			
			if(validationLevel.getInstitution() == null)
				validationLevel.setInstitution(validationLevelOld.getInstitution());
			else {
				Institution institution = institutionRep.findOne(validationLevel.getInstitution().getIdInstitution());
				if(institution == null)
				{
					h.put("message", "L'institution associée n'existe pas.");
					h.put("status", -1);		
					return h;
				}
				validationLevel.setInstitution(institution);
			}
		
			validationLevel = vLevelRep.saveAndFlush(validationLevel);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "success");
		h.put("validation_level", validationLevel);
		h.put("status", 0);
		return h;
	}
	
	/*
	 * Delete validation
	 */
	@RequestMapping(value="/delete/{idUser}/{idValidationLevel}", method=RequestMethod.DELETE)
	public HashMap<String, Object> delete(@PathVariable Integer idUser, @PathVariable Integer idValidationLevel){
		
		HashMap<String, Object> h= new HashMap<>();
		if(idUser == null || idValidationLevel == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);		
			return h;
		}
		
		try {
			User user = userRep.findOne(idUser);
			if (user == null)
			{
				h.put("message", "L'utlisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			ValidationLevel validationLevel = vLevelRep.findOne(idValidationLevel);
			if (validationLevel == null)
			{
				h.put("message", "Le niveau de validation n'existe pas.");
				h.put("status", -1);
				return h;
			}
			vLevelRep.delete(validationLevel);
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
