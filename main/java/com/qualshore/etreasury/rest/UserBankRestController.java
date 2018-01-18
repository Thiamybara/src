package com.qualshore.etreasury.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qualshore.etreasury.dao.GroupRepository;
import com.qualshore.etreasury.dao.InstitutionRepository;
import com.qualshore.etreasury.dao.ProfileRepository;
import com.qualshore.etreasury.dao.UserBankRepository;
import com.qualshore.etreasury.dao.UserRepository;
import com.qualshore.etreasury.entity.Bank;
import com.qualshore.etreasury.entity.Groupe;
import com.qualshore.etreasury.entity.Institution;
import com.qualshore.etreasury.entity.Locality;
import com.qualshore.etreasury.entity.Profile;
import com.qualshore.etreasury.entity.User;
import com.qualshore.etreasury.entity.UserBanque;
import com.qualshore.etreasury.mail.Value;
import com.qualshore.etreasury.service.UserLoginService;
import com.qualshore.etreasury.service.UserService;

@RequestMapping("etreasury_project/admin/user_bank")
@RestController
public class UserBankRestController {

	@Autowired
	UserBankRepository userBankRepository;
	
	@Autowired
	GroupRepository groupeRepository;
	
	@Autowired
	ProfileRepository profilRepository;
	
	@Autowired
	InstitutionRepository institutionRepository;
	
	@Autowired
	UserService userService;
	@Autowired
	UserRepository userRepository;
	
	@Autowired
    Environment env;
	
	@Autowired
	UserLoginService userLoginService;
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public Map<String,Object> getUserBanks(){
	
		List<UserBanque> userBanks = userBankRepository.findAll();
		HashMap<String, Object> h = new HashMap<String, Object>();
		h.put("message", "liste des users banks");
		h.put("status", 0);
		h.put("bank_list", userBanks);
		return h;
	}
	
	//Users de sa banque
	@RequestMapping(value="/list/{idInstitution}/{idAdmin}", method=RequestMethod.GET)
	public HashMap<String, Object> getUserInstitutionsBanks(@PathVariable Integer idInstitution,@PathVariable Integer idAdmin){
		HashMap<String, Object> h = new HashMap<String, Object>();
		List<User> userBanks ;
		
		//Fields Control
		if (idInstitution == null || idAdmin == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", 0);
			return h;
		}
		//
		 
		try {
			
			 Institution e = institutionRepository.findOne(idInstitution);
			 if (e == null)
			 {
				 h.put("message", "La banque n'existe pas.");
				 h.put("status", -1);
				 return h;
			 }
			if(userService.isAdminBanque(idAdmin))
			{
				h.put("status", -1);
				h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
				return h;
			}
			userBanks = userRepository.findUserByINstitution(e);
		
		}
		 catch (Exception e) {
			 	e.printStackTrace();
			 	h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
				h.put("status", -1);
				return h;
			}
		h.put("message", "liste des users banks");
		h.put("status", 0);
		h.put("bank_users_list", userBanks);
		return h;
	}
	
	//Ajout d'un utilisateur banque (USER_HABILITY)
	@RequestMapping(value="/add/{idAdminBank}", method=RequestMethod.POST)
	public HashMap<String, Object> add(@RequestBody UserBanque userBank, @PathVariable Integer idAdminBank){
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		//Fields Control
		if(userBank == null || idAdminBank == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		if(userBank.getNom().equals("") || userBank.getPrenom().equals("") || userBank.getTelephone().equals("") ||
				userBank.getEmail().equals("") || userBank.getLogin().equals(""))
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres vides");
			return h;
		}
		if(! userService.isEmailValid(userBank.getEmail()))
		{
			h.put("status", -1);
			h.put("message", "L'email est incorrect.");
			return h;
		}
		if(! userService.isPhoneValid(userBank.getTelephone()))
		{
			h.put("status", -1);
			h.put("message", "Le numéro de téléphone mobile est incorrect.");
			return h;
		}
		if(userBank.getTelephoneFixe() != null && !userService.isPhoneValid(userBank.getTelephoneFixe()))
		{
			h.put("status", -1);
			h.put("message", "Le numéro de téléphone fixe est incorrect.");
			return h;
		}
		//
				
		try {
			if(userService.isAdminBanque(idAdminBank))
			{
				h.put("status", -1);
				h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
				return h;
			}
			
			Integer idGroupe = userBank.getGroupeIdGroupe().getIdGroupe();
			Groupe groupe = groupeRepository.findOne(idGroupe);
			if(groupe == null)
			{
				h.put("message", "Le groupe n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			List<Profile> listProfile = profilRepository.findByType("USER_HABILITY");
			if(listProfile == null || listProfile.isEmpty())
			{
				h.put("message", "Le pofil utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			// Boss
			if(userBank.getBoss() != null)
			{
				User bossUser = userBankRepository.findOne(userBank.getBoss().getIdUtilisateur());
				if(bossUser == null)
				{
					h.put("message", "L'utilisateur boss n'existe pas.");
					h.put("status", -1);
					return h;
				}
				
				if (! userBankRepository.hasSameInstitution(bossUser, groupe))
				{
					h.put("message", "Le boss et l'utilisateur ne sont pas de la même banque.");
					h.put("status", -1);
					return h;
				}
				userBank.setBoss(bossUser);
			}
			//
			
			if(userBankRepository.existsByLogin(userBank.getLogin()))
			{
				h.put("message", "Ce login existe déjà.");
				h.put("status", -1);
				return h;
			}
			
			if(userBankRepository.existsByEmail(userBank.getEmail()))
			{
				h.put("message", "Cet email existe déjà.");
				h.put("status", -1);
				return h;
			}
			
			//Envoie du mail pour user admin banque
            String emailFrom = env.getProperty("spring.mail.username");
            if(!EmailValidator.getInstance().isValid(emailFrom))
            {
                h.put("status", -1);
                h.put("message", "L'email d'envoi est incorrect.");
                return h;
            }
            
            String emailTo = userBank.getEmail();
            if(!EmailValidator.getInstance().isValid(emailTo))
            {
                h.put("status", -1);
                h.put("message", "L'email destinataire est incorrect.");
                return h;
            }
            
            String tokenString = userLoginService.getPasswordString();
            String loginUser = userBank.getLogin();
            String emailObject = env.getProperty("email.object.add");
            //
            
            String cryptPassword = userLoginService.encryptData(tokenString);
            System.out.println("password crypte "+cryptPassword );

            userBank.setPassword(cryptPassword);
			userBank.setGroupeIdGroupe(groupe);
			userBank.setProfilIdProfil(listProfile.get(0));
			userBank.setDateCreation(new Date());
			userBank = userBankRepository.save(userBank);
			
			//userLoginService.sendMail(emailFrom, emailTo, emailObject, "Vos identitifants eTreasury.\n login: "+ loginUser + " et password: "+ tokenString );
			 List<Value> values= new ArrayList<Value>();
             values.add(new Value("","\n Identifiant : "+ loginUser + "\n et  Mot de passe: "+ tokenString));
             userLoginService.sendMailNew(emailFrom, emailTo, "eTreasury  | Votre inscription est validée", "Bienvenue sur eTreasury, votre inscription est effective. Vous pouvez vous connecter avec les identifiants suivants :\n", userBank.getPrenom(), values,"Nous vous invitons à changer votre mot de passe dès votre première connexion.");
	        
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("userBank", userBank);
		h.put("message", "L'utilisateur est ajouté avec succès.");
		h.put("status", 0);
		
		userService.journaliser(idAdminBank, 6, "Ajout user_hability banque");
		
		return h;
	}
	//Ajout d'un utilisateur banque par institution
	@RequestMapping(value="/add/{idAdminBank}/{idInstitution}", method=RequestMethod.POST)
	public HashMap<String, Object> addByInstitution(@RequestBody UserBanque userBank, @PathVariable Integer idAdminBank, @PathVariable Integer idInstitution){
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		//Fields Control
		if(userBank == null || idAdminBank == null || idInstitution == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		if(userBank.getNom().equals("") || userBank.getPrenom().equals("") || userBank.getTelephone().equals("") ||
				userBank.getEmail().equals("") || userBank.getLogin().equals(""))
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres vides");
			return h;
		}
		if(! userService.isEmailValid(userBank.getEmail()))
		{
			h.put("status", -1);
			h.put("message", "L'email est incorrect.");
			return h;
		}
		if(! userService.isPhoneValid(userBank.getTelephone()))
		{
			h.put("status", -1);
			h.put("message", "Le numéro de téléphone mobile est incorrect.");
			return h;
		}
		if(userBank.getTelephoneFixe() != null && !userService.isPhoneValid(userBank.getTelephoneFixe()))
		{
			h.put("status", -1);
			h.put("message", "Le numéro de téléphone fixe est incorrect.");
			return h;
		}
		//
				
		try 
		{
			if(userService.isAdminBanque(idAdminBank))
			{
				h.put("status", -1);
				h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
				return h;
			}
			
			Institution institution = institutionRepository.findOne(idInstitution);
			if(institution == null)
			{
				h.put("message", "L'institution n'existe pas.");
				h.put("status", -1);
				return h;
			}
			Integer idGroupe = userBank.getGroupeIdGroupe().getIdGroupe();
			Groupe groupe = groupeRepository.findOne(idGroupe);
			if(groupe == null)
			{
				h.put("message", "Le groupe n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			List<Profile> listProfile = profilRepository.findByType("USER_HABILITY");
			if(listProfile == null || listProfile.isEmpty())
			{
				h.put("message", "Le pofile utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			// Boss
			if(userBank.getBoss() != null)
			{
				User bossUser = userBankRepository.findOne(userBank.getBoss().getIdUtilisateur());
				if(bossUser == null)
				{
					h.put("message", "L'utilisateur boss n'existe pas.");
					h.put("status", -1);
					return h;
				}
				
				if (! userBankRepository.hasSameInstitution(bossUser, groupe))
				{
					h.put("message", "Le boss et l'utilisateur ne sont pas de la même banque.");
					h.put("status", -1);
					return h;
				}
				userBank.setBoss(bossUser);
			}
			//
			
			if(userBankRepository.existsByLogin(userBank.getLogin()))
			{
				h.put("message", "Ce login existe déjà.");
				h.put("status", -1);
				return h;
			}
			
			if(userBankRepository.existsByEmail(userBank.getEmail()))
			{
				h.put("message", "Cet email existe déjà.");
				h.put("status", -1);
				return h;
			}
			
			//Envoie du mail pour user admin banque
            String emailFrom = env.getProperty("spring.mail.username");
            if(!EmailValidator.getInstance().isValid(emailFrom))
            {
                h.put("status", -1);
                h.put("message", "L'email d'envoi est incorrect.");
                return h;
            }
            
            String emailTo = userBank.getEmail();
            if(!EmailValidator.getInstance().isValid(emailTo))
            {
                h.put("status", -1);
                h.put("message", "L'email destinataire est incorrect.");
                return h;
            }
            
            String tokenString = userLoginService.getPasswordString();
            String loginUser = userBank.getLogin();
            String emailObject = env.getProperty("email.object.add");
            //
            String cryptPassword = userLoginService.encryptData(tokenString);
            System.out.println("password crypte "+cryptPassword );

            userBank.setPassword(cryptPassword);
			userBank.setGroupeIdGroupe(groupe);
			userBank.setProfilIdProfil(listProfile.get(0));
			userBank.setDateCreation(new Date());
			userBank = userBankRepository.save(userBank);
			
			//userLoginService.sendMail(emailFrom, emailTo, emailObject, "Vos identitifants eTreasury.\n login: "+ loginUser + " et password: "+ tokenString );
			 List<Value> values= new ArrayList<Value>();
             values.add(new Value("","\n Identifiant : "+ loginUser + "\n et  Mot de passe: "+ tokenString));
             userLoginService.sendMailNew(emailFrom, emailTo, "eTreasury  | Votre inscription est validée", "Bienvenue sur eTreasury, votre inscription est effective. Vous pouvez vous connecter avec les identifiants suivants :\n", userBank.getPrenom(), values,"Nous vous invitons à changer votre mot de passe dès votre première connexion.");
	        
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("userBank", userBank);
		h.put("message", "L'utilisateur est ajouté avec succès.");
		h.put("status", 0);
		
		userService.journaliser(idAdminBank, 6, "Ajout user_hability banque");
		
		return h;
	}
	
	@RequestMapping(value="/update/{idAdminBank}", method=RequestMethod.PUT)
	public HashMap<String, Object> update(@RequestBody UserBanque userBank, @PathVariable Integer idAdminBank){
		
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		//Fields Control
		if(userBank == null || idAdminBank == null){
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
			return h;
		}
		if(userBank.getIdUtilisateur() == null ){
			h.put("message", "L'identifiant de l'utilisateur n'est pas renseigné.");
			h.put("status", -1);
			return h;
		}
		//
		
		try
		{
			if(userService.isAdminBanque(idAdminBank))
			{
				h.put("status", -1);
				h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
				return h;
			}
			System.out.println("Value "+userService.isSameInstitution(idAdminBank, userBank.getIdUtilisateur()));
			if(!userService.isSameInstitution(idAdminBank, userBank.getIdUtilisateur()))
			{
				h.put("status", -1);
				h.put("message", "L'utilisateur n'est pas de votre banque.");
				return h;
			}
			
			UserBanque userBankOld = userBankRepository.findOne(userBank.getIdUtilisateur());
			if(userBankOld == null )
			{
				h.put("message", "Cet utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			if(userBank.getGroupeIdGroupe() == null)
			{
				userBank.setGroupeIdGroupe(userBankOld.getGroupeIdGroupe());
			} 
			else
			{
				Integer idGroupe = userBank.getGroupeIdGroupe().getIdGroupe();
				Groupe groupe = groupeRepository.findOne(idGroupe);
				if(groupe == null){
					h.put("message", "Le groupe n'existe pas.");
					h.put("status", -1);
					return h;
				}
				userBank.setGroupeIdGroupe(groupe);
			}
			
			if(userBank.getProfilIdProfil() == null)
			{
				userBank.setProfilIdProfil(userBankOld.getProfilIdProfil());
			}
			else
			{
				Integer idProfil = userBank.getProfilIdProfil().getIdProfil();
				Profile profil = profilRepository.findOne(idProfil);
				if(profil == null){
					h.put("message", "Le profil n'existe pas.");
					h.put("status", -1);
					return h;
				}
				userBank.setProfilIdProfil(profil);
			}
			if(userBank.getIsActive() == null)
				userBank.setIsActive(userBankOld.getIsActive());
			
			
			//Email Control
			if(userBank.getEmail() == null)
			{
				userBank.setEmail(userBankOld.getEmail());
			}
			/*
			//Verfiier 	que l'email n'existe
			else if(userBankRepository.existsByEmailOthers(userBank.getEmail(),userBankOld.getIdUtilisateur()))
			{
				//on verifie que l'email n'existe pas en BDD
				
				h.put("message", "Cet email existe déjà");
				h.put("status", -1);
				return h;
			}
			*/
			else if(!userService.isEmailValid(userBank.getEmail()))
			{
				h.put("status", -1);
				h.put("message", "L'email est incorrect.");
				return h;
			}
			//
			
			if(userBank.getBoss() == null)
				userBank.setBoss(userBankOld.getBoss());
			if(userBank.getDateCreation() == null)
				userBank.setDateCreation(userBankOld.getDateCreation());
			
			// Boss
			if(userBank.getBoss() == null)
				userBank.setBoss(userBankOld.getBoss());
			else
			{
				User bossUser = userBankRepository.findOne(userBank.getBoss().getIdUtilisateur());
				if(bossUser == null)
				{
					h.put("message", "L'utilisateur boss n'existe pas.");
					h.put("status", -1);
					return h;
				}
				
				if (! userBankRepository.hasSameInstitution(bossUser, userBank.getGroupeIdGroupe()))
				{
					h.put("message", "Le boss et l'utilisateur ne sont pas de la même banque.");
					h.put("status", -1);
					return h;
				}
				userBank.setBoss(bossUser);
			}
			//
			
			if(userBank.getLogin() == null)
			{
				userBank.setLogin(userBankOld.getLogin());
			}
			else
			{
				if (!(userBank.getLogin().equals(userBankOld.getLogin()))){
					
					/*//Verfiier 	que l'email n'existe
					 if(userBankRepository.existsByLoginOthers(userBank.getLogin(),userBankOld.getIdUtilisateur()))
					{
						//on verifie que l'email n'existe pas en BDD
						
						h.put("message", "Ce login existe déjà");
						h.put("status", -1);
						return h;
					}
					*/
	                //Envoie du mail pour user admin entreprise
	                String emailFrom = env.getProperty("spring.mail.username");
	                if(!EmailValidator.getInstance().isValid(emailFrom))
	                {
	                    h.put("status", -1);
	                    h.put("message", "L'email d'envoi est incorrect.");
	                    return h;
	                }
	                
	                String emailTo = userBank.getEmail();
	                if(!EmailValidator.getInstance().isValid(emailTo))
	                {
	                    h.put("status", -1);
	                    h.put("message", "L'email destinataire est incorrect.");
	                    return h;
	                }
	                
	                //    String tokenString = userService.getPasswordString();
	                String loginUser = userBank.getLogin();
	                String emailObject = env.getProperty("email.object.add");
	             //   userLoginService.sendMail(emailFrom, emailTo, emailObject, "Votre login a été modifié par l'administrateur.\n Votre nouveau login est : "+ loginUser );
	                List<Value> values= new ArrayList<Value>();
	                values.add(new Value("", loginUser));
	                userLoginService.sendMailNew(emailFrom, emailTo, "eTreasury  | Changement de login", "Votre login a été modifié par l'administrateur. Vous pouvez vous connecter avec l'identifiant suivant:", userBank.getPrenom(), values,"Nous vous invitions à utiliser lors de votre prochaine connexion.");
	   	           
				       
				}
			}
			
			if(userBank.getNom() == null)
				userBank.setNom(userBankOld.getNom());
			if(userBank.getPassword() == null)
				userBank.setPassword(userBankOld.getPassword());
			if(userBank.getPhoto() == null)
				userBank.setPhoto(userBankOld.getPhoto());
			if(userBank.getPrenom() == null)
				userBank.setPrenom(userBankOld.getPrenom());
			
			
			
			//Phone number Control
			if(userBank.getTelephone() == null)
			{
				userBank.setTelephone(userBankOld.getTelephone());
			}
			else if(!userService.isPhoneValid(userBank.getTelephone()))
			{
				h.put("status", -1);
				h.put("message", "Le numéro de téléphone mobile est incorrect.");
				return h;
			}
			if(userBank.getTelephoneFixe() == null)
			{
				userBank.setTelephoneFixe(userBankOld.getTelephoneFixe());
			}
			else if(!userService.isPhoneValid(userBank.getTelephoneFixe()))
			{
				h.put("status", -1);
				h.put("message", "Le numéro de téléphone fixe est incorrect.");
				return h;
			}
			//
			
			userBank.setIdUtilisateur(userBank.getIdUtilisateur());
			userBank = userBankRepository.saveAndFlush(userBank);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("userBanque", userBank);
		h.put("message", "La modification s'est effectuée avec succès.");
		h.put("status", 0);
		
		userService.journaliser(idAdminBank, 7, "Modification user_hability banque");
		
		return h;
	}
	
	@RequestMapping(value="/list/{id}", method=RequestMethod.GET)
	public Map<String,Object> getUserBank(@PathVariable Integer id){
		
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		//Fields Control
		if(id == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
			return h;
		}
		//
		
		try {
			UserBanque userBank = userBankRepository.findOne(id);
			if (userBank == null) {
				h.put("message", "L'utilisateur banque n'existe pas.");
				h.put("status", -1);
				return h;
			}
			h.put("message", "success");
			h.put("status", 0);
			h.put("bank", userBank);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		return h;
	}
	
	@RequestMapping(value="/delete/{idAdminBank}/{id}", method=RequestMethod.DELETE)
	public Map<String,Object> deleteUser(@PathVariable Integer idAdminBank, @PathVariable Integer id){
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		//Fields Control
		if (idAdminBank == null || id == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
			return h;
		}
		//
		
		try 
		{
			UserBanque userEntreprise = userBankRepository.findOne(id);
			if (userEntreprise == null)
			{
				h.put("message", "Cet utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			if(userService.isAdminBanque(idAdminBank))
			{
				h.put("status", -1);
				h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
				return h;
			}
			if(!userService.isSameInstitution(idAdminBank, id))
			{
				h.put("status", -1);
				h.put("message", "L'utilisateur n'est pas de votre banque.");
				return h;
			}
			
			userBankRepository.delete(userEntreprise);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "L'utilisateur est supprimé avec succès.");
		h.put("status", 0);
		
		userService.journaliser(idAdminBank, 8, "Suppression user_hability banque");
		
		return h;
	}
	
	@RequestMapping(value="/groupe/delete/{idAdminBank}/{id}", method=RequestMethod.DELETE)
	public Map<String,Object> deleteFromGroup(@PathVariable Integer idAdminBank, @PathVariable Integer id){
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		//Fields Control
		if (idAdminBank == null || id == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
			return h;
		}
		//
		
		try
		{
			if(userService.isAdminBanque(idAdminBank))
			{
				h.put("status", -1);
				h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
				return h;
			}
			if(!userService.isSameInstitution(idAdminBank, id))
			{
				h.put("status", -1);
				h.put("message", "L'utilisateur n'est pas de votre banque.");
				return h;
			}
			
			UserBanque user = userBankRepository.findOne(id);
			if(user == null)
			{
				h.put("message", "Cet utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			Groupe groupe = user.getGroupeIdGroupe();
			if(groupe == null)
			{
				h.put("message", "Cet utilisateur n'est rattaché à aucun groupe.");
				h.put("status", -1);
				return h;
			}
			Institution institution = user.getGroupeIdGroupe().getInstitution();
			if(institution == null)
			{
				h.put("message", "Cet utilisateur n'est rattaché à aucune institution.");
				h.put("status", -1);
				return h;
			}
			Groupe defaultGroupe = institutionRepository.getDefaultGroupByIdInstitution(institution.getIdInstitution());
			if(defaultGroupe == null)
			{
				h.put("message", "Cette institution n'a pas de goupe par défaut.");
				h.put("status", -1);
				return h;
			}
			userBankRepository.updateGroup(defaultGroupe, id);
		}catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "L'utilisateur entreprise a été bien supprimé de ce groupe.");
		h.put("status", 0);
		
		userService.journaliser(idAdminBank, 8, "Suppression user_hability banque dans groupe");
		
		return h;
	}
	
}