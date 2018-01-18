package com.qualshore.etreasury.rest;

import java.util.ArrayList;
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
import com.qualshore.etreasury.dao.JuridictionGroupeRepository;
import com.qualshore.etreasury.dao.JuridictionRepository;
import com.qualshore.etreasury.dao.UserRepository;
import com.qualshore.etreasury.entity.Bank;
import com.qualshore.etreasury.entity.DocumentsHasRequest;
import com.qualshore.etreasury.entity.Groupe;
import com.qualshore.etreasury.entity.Juridiction;
import com.qualshore.etreasury.entity.JuridictionGroupe;
import com.qualshore.etreasury.entity.Offer;
import com.qualshore.etreasury.entity.Request;
import com.qualshore.etreasury.entity.RequestHasBank;
import com.qualshore.etreasury.entity.User;
import com.qualshore.etreasury.entity.UserEntreprise;
import com.qualshore.etreasury.model.JuridictionGroupesModel;
import com.qualshore.etreasury.model.RequestModel;
import com.qualshore.etreasury.service.UserService;

@RestController
@RequestMapping("/etreasury_project/admin/juridiction_groupe")
public class JuridictionGroupeRestController {
	
	@Autowired
	JuridictionGroupeRepository  jGRep;
	@Autowired
	GroupRepository gRep;
	@Autowired
	JuridictionRepository jRep;
	@Autowired
	UserService userService;
	@Autowired
	UserRepository userRep;
	
	/*
	 * add juridiction to group 
	 */
	@RequestMapping(value="/add/{idAdmin}", method=RequestMethod.POST)
	public HashMap<String, Object> addJuridictionToGroupe(@RequestBody JuridictionGroupesModel jGM, @PathVariable Integer idAdmin){
		
		HashMap<String, Object> h = new HashMap<>();
		if(userService.isAdminGeneral(idAdmin) || userService.isAdminBanque(idAdmin) || userService.isAdminEntreprise(idAdmin))
		{
			h.put("status", -1);
			h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
			return h;
		}
		
		/*
		
		List<Integer> idGroupes = jGM.getIdGroupes();
		Integer idJ = jGM.getIdJuridiction();
		List<Groupe> groupes = new ArrayList<>();
		*/
		
		List<Integer> idJuridictions = jGM.getIdJuridiction();
		Integer idG = jGM.getIdGroupes();
		//List<Juridiction> groupes = new ArrayList<>();
		Groupe g= gRep.findOne(idG);
		if (g == null) {
			h.put("message", "Le groupe n'existe pas.");
			h.put("status", -1);
			return h;
		}
		if (idJuridictions.size() <= 0) {
			h.put("message", "Vous n'avez pas selectionné de juridictions.");
			h.put("status", -1);
			return h;
		}
		User user = userRep.findOne(idAdmin);
		if(user == null)
		{
			h.put("status", -1);
			h.put("message", "Cet utilisateur n'existe pas.");
			return h;
		}
		//if(idJuridictions.contains(1) || idJuridictions.contains(2) || idJuridictions.contains(3) || idJuridictions.contains(4) || idJuridictions.contains(5) ) {
		List<JuridictionGroupe> juridictionGroupesFound = jGRep.findByGroupeAndInstituion(user.getGroupeIdGroupe().getInstitution());
		System.out.println("List found jg "+ juridictionGroupesFound.size());
		if (juridictionGroupesFound.size() > 0) {
			/*
			 for(int j=0; j<idJuridictions.size(); j++) {
			      	Juridiction jur = jRep.findOne(j);
				if (jur != null) {
			*/
			 for(int i=0; i<juridictionGroupesFound.size(); i++)
	            {
				 JuridictionGroupe jurGroupFound = juridictionGroupesFound.get(i);
				 	if(idJuridictions.contains(jurGroupFound.getJuridiction().getId()) ){
				 		/*
				 		h.put("message", "Vous avez défini  une juridiction de pré-validation pour un des produit dans votre instition.");
						h.put("status", -1);
						return h;
						*/
				 		if (jurGroupFound.getJuridiction().getId() == 1 ) {
							h.put("message", "Vous avez défini  une juridiction de pré-validation pour le prouit escompte.");
							h.put("status", -1);
							return h;
					}
					 	else if (jurGroupFound.getJuridiction().getId() == 2) {
							h.put("message", "Vous avez défini  une juridiction de pré-validation pour le prouit change.");
							h.put("status", -1);
							return h;
					}
					 	else if (jurGroupFound.getJuridiction().getId() == 3) {
							h.put("message", "Vous avez défini  une juridiction de pré-validation pour le prouit dépôt à terme.");
							h.put("status", -1);
							return h;
					}
					 	else if (jurGroupFound.getJuridiction().getId() == 4) {
							h.put("message", "Vous avez défini  une juridiction de pré-validation pour le prouit transfert.");
							h.put("status", -1);
							return h;
					}
					 	else if (jurGroupFound.getJuridiction().getId() == 5) {
							h.put("message", "Vous avez défini  une juridiction de pré-validation pour le prouit spot.");
							h.put("status", -1);
							return h;
					}
				 	}
				 
				 
				 /*
				 JuridictionGroupe jurGroupFound = juridictionGroupesFound.get(i);
				 if (jurGroupFound.getJuridiction().getId() == jur.getId()) {
				 	if (jurGroupFound.getJuridiction().getId() == 1 ) {
						h.put("message", "Vous avez défini  une juridiction de pré-validation pour le prouit escompte.");
						h.put("status", -1);
						return h;
				}
				 	else if (jurGroupFound.getJuridiction().getId() == 2) {
						h.put("message", "Vous avez défini  une juridiction de pré-validation pour le prouit change.");
						h.put("status", -1);
						return h;
				}
				 	else if (jurGroupFound.getJuridiction().getId() == 3) {
						h.put("message", "Vous avez défini  une juridiction de pré-validation pour le prouit dépôt à terme.");
						h.put("status", -1);
						return h;
				}
				 	else if (jurGroupFound.getJuridiction().getId() == 4) {
						h.put("message", "Vous avez défini  une juridiction de pré-validation pour le prouit transfert.");
						h.put("status", -1);
						return h;
				}
				 	else if (jurGroupFound.getJuridiction().getId() == 5) {
						h.put("message", "Vous avez défini  une juridiction de pré-validation pour le prouit spot.");
						h.put("status", -1);
						return h;
				}
			}
				*/
			}
			 /*
		}
	}
	*/
		}
	//	}
		/*
		if(jGRep.exitsByIdJuridiction(user.getGroupeIdGroupe().getInstitution().getIdInstitution())) {
			h.put("message", "Vous avez selectionné de juridictions.");
			h.put("status", -1);
			return h;
		}
		*/
		List<JuridictionGroupe> juridictionGroupes = new ArrayList<>();
		//Verifier que la juridiction par produit existe deja dans l'entreprise
		
		
		idJuridictions.forEach(id->{
			Juridiction jur = jRep.findOne(id);
			if (jur != null) {
				JuridictionGroupe jGroupe = new JuridictionGroupe();
				jGroupe = g.addJuridictionGroupe(jur);
				try {
					jGRep.saveAndFlush(jGroupe);
					juridictionGroupes.add(jGroupe);
				} catch (Exception e) {
					e.printStackTrace();
					h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
					h.put("status", -1);
				}
			}
		});
		try {
			Groupe grp= gRep.findOne(idG);
			
			grp.setHasJuridiction(true);
			grp = gRep.saveAndFlush(grp);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
		}
	
		h.put("message", "Les juridictions sont ajoutées avec succès.");
		h.put("juridiction_groupe", juridictionGroupes);
		h.put("status", 0);
		return h;
	}
	
	
	///Update des juridictions 
	

	@RequestMapping(value="/update/{idAdmin}", method=RequestMethod.PUT)
    public HashMap<String, Object> update(@RequestBody JuridictionGroupesModel jGM, @PathVariable Integer idAdmin){
        
     
		HashMap<String, Object> h = new HashMap<>();
		if(userService.isAdminGeneral(idAdmin) || userService.isAdminBanque(idAdmin) || userService.isAdminEntreprise(idAdmin))
		{
			h.put("status", -1);
			h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
			return h;
		}
		
		List<Integer> idJuridictions = jGM.getIdJuridiction();
		Integer idG = jGM.getIdGroupes();
		//List<Juridiction> groupes = new ArrayList<>();
		Groupe g= gRep.findOne(idG);
		if (g == null) {
			h.put("message", "le groupe n'existe pas");
			h.put("status", -1);
			return h;
		}
		if (idJuridictions.size() <= 0) {
			h.put("message", "Vous n'avez pas sélectionné de juridictions.");
			h.put("status", -1);
			return h;
		}
			//find old juridictions for group
		
		 try {
             
             // On supprime ts ls banques positionnées sur la demande
              List<JuridictionGroupe> jGroupes = jGRep.findByGroupe(g);
     		
              jGroupes.forEach(jGroup->{
            	  jGRep.delete(jGroup);
             });
             
             // on repossitionne à nouveau les banques sur la demande
              List<JuridictionGroupe> juridictionGroupes = new ArrayList<>();
      			idJuridictions.forEach(id->{
      			Juridiction jur = jRep.findOne(id);
      			if (jur != null) {
      				JuridictionGroupe jGroupe = new JuridictionGroupe();
      				jGroupe = g.addJuridictionGroupe(jur);
      				try {
      					jGRep.saveAndFlush(jGroupe);
      					juridictionGroupes.add(jGroupe);
      				} catch (Exception e) {
      					e.printStackTrace();
      					h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
      					h.put("status", -1);
      				}
      			}
      		});
         } catch (Exception e) {
             e.printStackTrace();
             h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
             h.put("status", -1);
             return h;
         }
         
        

        
        h.put("message", "La modification des juridictions s'est faite avec succès.");
        h.put("status", 0);
        return h;
    }
	
	/*
	 * add juridiction to group 
	 */
	@RequestMapping(value="/add/{idGroupe}/{idJuridiction}", method=RequestMethod.POST)
	public HashMap<String, Object> addJuridictionToGroupe(@PathVariable("idGroupe") int idGroupe, 
			@PathVariable("idJuridiction") int idJuridiction){
		
		HashMap<String, Object> h = new HashMap<>();
		Groupe g = gRep.findOne(idGroupe);
		if (g == null) {
			h.put("message", "Le groupe n'existe pas.");
			h.put("status", -1);
			return h;
		}
		Juridiction j= jRep.findOne(idJuridiction);
		if (j == null) {
			h.put("message", "La juridiction n'existe pas.");
			h.put("status", -1);
			return h;
		}
		JuridictionGroupe jGroupe = g.addJuridictionGroupe(j);
		try {
			jGRep.saveAndFlush(jGroupe);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
		}
		
		h.put("message", "success");
		h.put("juridiction_groupe", jGroupe);
		h.put("status", 0);
		return h;
	}
	
	/**
	 * get all juridictions groupe
	 */
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public HashMap<String, Object> getAll(){
		
		HashMap<String, Object> h = new HashMap<String, Object>();
		List<JuridictionGroupe> jGroupes = jGRep.findAll();
		h.put("message", "success");
		h.put("list_juridiction_groupe", jGroupes);
		h.put("status", 0);
		return h;
	}
	
	/*
	 * get juridiction groupe by idJuridiction and idGroupe
	 */
	@RequestMapping(value="/list/{idGroupe}/{idJuridiction}", method=RequestMethod.GET)
	public HashMap<String, Object> getJuridictionGroupeById(@PathVariable("idGroupe") int idGr,
			@PathVariable("idJuridiction") int idJur){
		
		HashMap<String, Object> h = new HashMap<String, Object>();
		Groupe g = gRep.findOne(idGr);
		if (g == null) {
			h.put("message", "Le groupe n'existe pas.");
			h.put("status", -1);
			return h;
		}
		Juridiction j = jRep.findOne(idJur);
		if (j == null) {
			h.put("message", "La juridiction n'existe pas.");
			h.put("status", -1);
			return h;
		}
		JuridictionGroupe jGroupe = jGRep.findByGroupeAndJuridiction(g, j);
		if (jGroupe == null) {
			h.put("message", "La juridiction du groupe n'existe pas.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "success");
		h.put("juridiction_groupe", jGroupe);
		h.put("status", 0);
		return h;
	}
	
	/**
	 * get juridiction groupe by idGroupe
	 */
	@RequestMapping(value="/list/groupe/{id}", method=RequestMethod.GET)
	public HashMap<String, Object> getJuridictionGroupeByGroupe(@PathVariable int id){
		
		HashMap<String, Object> h= new HashMap<String, Object>();
		Groupe g = gRep.findOne(id);
		if (g == null) {
			h.put("message", "Le groupe n'existe pas.");
			h.put("status", -1);
			return h;
		}
		List<JuridictionGroupe> jGroupes = jGRep.findByGroupe(g);
		h.put("message", "success");
		h.put("list_juridiction_groupe", jGroupes);
		h.put("status", 0);
		return h;
	}
	
	/*
	 public void addJuridictions(Integer idGroupe, Integer[] tabJuridiction) {
		Groupe g = gRep.findOne(idGroupe);
		if(g != null)
		{
			for(int i = 0; i < tabJuridiction.length; i++)
			{
			//	Documents document = documentsRepository.findOne(tabJuridiction[i]);
				Juridiction jurid= jRep.findOne(tabJuridiction[i]);
				if(jurid != null)
				{
					JuridictionGroupe jGroupe = new JuridictionGroupe();
					jGroupe.setGroupe(g)
					jGroupe.se(document);
					jGroupe.setDateDocument(new Date());
					documentOffer.setDocumentsHasOfferPK(new DocumentsHasOfferPK(document.getIdDocuments(), offer.getIdOffre()));
					documentOffer = dhoRepository.save(documentOffer);
				}
			}
		}
	}
	 */
	
	
}
