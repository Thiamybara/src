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
import com.qualshore.etreasury.dao.UserEntrepriseRepository;
import com.qualshore.etreasury.dao.UserRepository;
import com.qualshore.etreasury.entity.Groupe;
import com.qualshore.etreasury.entity.Institution;
import com.qualshore.etreasury.entity.Profile;
import com.qualshore.etreasury.entity.User;
import com.qualshore.etreasury.entity.UserEntreprise;
import com.qualshore.etreasury.mail.Value;
import com.qualshore.etreasury.service.UserLoginService;
import com.qualshore.etreasury.service.UserService;

@RequestMapping("etreasury_project/admin/user_entreprise")
@RestController
public class UserEntrepriseRestController {
	@Autowired
	UserEntrepriseRepository userEntrepriseRepository;
	
	@Autowired
	GroupRepository groupeRepository;
	
	@Autowired
	ProfileRepository profilRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	InstitutionRepository institutionRepository;
	
	@Autowired
	UserService userService;
	
	@Autowired
    Environment env;
	
	@Autowired
	UserLoginService userLoginService;
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public Map<String,Object> getUserBanks(){
	
		List<UserEntreprise> userEntreprises = userEntrepriseRepository.findAll();
		HashMap<String, Object> h = new HashMap<String, Object>();
		h.put("message", "liste des users entreprise");
		h.put("status", 0);
		h.put("user_list", userEntreprises);
		return h;
	}
	

	//Users de son entreprise
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
	           if (e == null) {
	               h.put("message", "L'institution n'existe pas.");
	               h.put("status", -1);
	               return h;
	           }
			if(userService.isAdminEntreprise(idAdmin))
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
		h.put("entreprise_users_list", userBanks);
		return h;
	}
	
	//Ajout utilisateur entreprise
	@RequestMapping(value="/add/{idAdminEntreprise}", method=RequestMethod.POST)
	public HashMap<String, Object> add(@RequestBody UserEntreprise userEntreprise, @PathVariable Integer idAdminEntreprise) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		//Fields Control
		if(userEntreprise == null || idAdminEntreprise == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		if(userEntreprise.getNom().equals("") || userEntreprise.getPrenom().equals("") || userEntreprise.getTelephone().equals("") ||
				userEntreprise.getEmail().equals("") || userEntreprise.getLogin().equals(""))
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres vides");
			return h;
		}
		if(! userService.isEmailValid(userEntreprise.getEmail()))
		{
			h.put("status", -1);
			h.put("message", "L'email est incorrect.");
			return h;
		}
		if(! userService.isPhoneValid(userEntreprise.getTelephone()))
		{
			h.put("status", -1);
			h.put("message", "Le numéro de téléphone mobile est incorrect.");
			return h;
		}
		if(userEntreprise.getTelephoneFixe() != null && !userService.isPhoneValid(userEntreprise.getTelephoneFixe()))
		{
			h.put("status", -1);
			h.put("message", "Le numéro de téléphone fixe est incorrect.");
			return h;
		}
		//
		
		try {
			// User Admin Control
			if(userService.isAdminEntreprise(idAdminEntreprise))
			{
				h.put("status", -1);
				h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
				return h;
			}
			// End Control
			
			Integer idGroupe = userEntreprise.getGroupeIdGroupe().getIdGroupe();
			Groupe groupe = groupeRepository.findOne(idGroupe);
			if(groupe == null){
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
			if(userEntreprise.getBoss() != null)
			{
				User bossUser = userEntrepriseRepository.findOne(userEntreprise.getBoss().getIdUtilisateur());
				if(bossUser == null)
				{
					h.put("message", "L'utilisateur boss n'existe pas.");
					h.put("status", -1);
					return h;
				}
				
				if (! userEntrepriseRepository.hasSameInstitution(bossUser, groupe))
				{
					h.put("message", "Le boss et l'utilisateur ne sont pas de la même entreprise.");
					h.put("status", -1);
					return h;
				}
				userEntreprise.setBoss(bossUser);
			}
			//
			
			if(userEntrepriseRepository.existsByLogin(userEntreprise.getLogin()))
			{
				h.put("message", "Ce login existe déjà.");
				h.put("status", -1);
				return h;
			}
			
			if(userEntrepriseRepository.existsByEmail(userEntreprise.getEmail()))
			{
				h.put("message", "Cet email existe déjà.");
				h.put("status", -1);
				return h;
			}
			
			//Envoie du mail pour user admin entreprise
            String emailFrom = env.getProperty("spring.mail.username");
            if(!EmailValidator.getInstance().isValid(emailFrom))
            {
                h.put("status", -1);
                h.put("message", "L'email d'envoi est incorrect.");
                return h;
            }
            
            String emailTo = userEntreprise.getEmail();
            if(!EmailValidator.getInstance().isValid(emailTo))
            {
                h.put("status", -1);
                h.put("message", "L'email destinataire est incorrect.");
                return h;
            }
            
            String tokenString = userLoginService.getPasswordString();
            String loginUser = userEntreprise.getLogin();
            String emailObject = env.getProperty("email.object.add");
            //
            String cryptPassword = userLoginService.encryptData(tokenString);
            System.out.println("password crypte "+cryptPassword );

            userEntreprise.setPassword(cryptPassword);
			userEntreprise.setGroupeIdGroupe(groupe);
			userEntreprise.setProfilIdProfil(listProfile.get(0));
			userEntreprise.setDateCreation(new Date());
			userEntreprise = userEntrepriseRepository.save(userEntreprise);
			
			//userLoginService.sendMail(emailFrom, emailTo, emailObject, "Vos identitifants eTreasury.\n login: "+ loginUser + " et password: "+ tokenString );
			 List<Value> values= new ArrayList<Value>();
             values.add(new Value("","\n Identifiant : "+ loginUser + "\n et  Mot de passe: "+ tokenString));
             userLoginService.sendMailNew(emailFrom, emailTo, "eTreasury  | Votre inscription est validée", "Bienvenue sur eTreasury, votre inscription est effective. Vous pouvez vous connecter avec les identifiants suivants :\n", userEntreprise.getPrenom(), values,"Nous vous invitons à changer votre mot de passe dès votre première connexion.");
	        
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("userEntreprise", userEntreprise);
		h.put("message", "L'utilisateur est ajouté avec succès.");
		h.put("status", 0);

		userService.journaliser(idAdminEntreprise, 6, "Ajout user_hability entreprise");
		
		return h;
	}
		
	//Ajout utilisateur Entreprise par institution
	@RequestMapping(value="/add/{idAdminEntreprise}/{idInstitution}", method=RequestMethod.POST)
	public HashMap<String, Object> addByInstitution(@RequestBody UserEntreprise userEntreprise,
													@PathVariable Integer idAdminEntreprise,
													@PathVariable Integer idInstitution){
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		//Fields Control
		if(userEntreprise == null || idAdminEntreprise == null || idInstitution == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		if(userEntreprise.getNom().equals("") || userEntreprise.getPrenom().equals("") || userEntreprise.getTelephone().equals("") ||
				userEntreprise.getEmail().equals("") || userEntreprise.getLogin().equals(""))
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres vides");
			return h;
		}
		if(! userService.isEmailValid(userEntreprise.getEmail()))
		{
			h.put("status", -1);
			h.put("message", "L'email est incorrect.");
			return h;
		}
		if(! userService.isPhoneValid(userEntreprise.getTelephone()))
		{
			h.put("status", -1);
			h.put("message", "Le numéro de téléphone mobile est incorrect.");
			return h;
		}
		if(userEntreprise.getTelephoneFixe() != null && !userService.isPhoneValid(userEntreprise.getTelephoneFixe()))
		{
			h.put("status", -1);
			h.put("message", "Le numéro de téléphone fixe est incorrect.");
			return h;
		}
		//
		
		try
		{
			// User Admin Control
			if(userService.isAdminEntreprise(idAdminEntreprise))
			{
				h.put("status", -1);
				h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
				return h;
			}
			// End Control
			
			Institution institution = institutionRepository.findOne(idInstitution);
			if(institution == null)
			{
				h.put("message", "L'institution n'existe pas.");
				h.put("status", -1);
				return h;
			}
			Integer idGroupe = userEntreprise.getGroupeIdGroupe().getIdGroupe();
			Groupe groupe = groupeRepository.findOne(idGroupe);
			if(groupe == null){
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
			if(userEntreprise.getBoss() != null)
			{
				User bossUser = userEntrepriseRepository.findOne(userEntreprise.getBoss().getIdUtilisateur());
				if(bossUser == null)
				{
					h.put("message", "L'utilisateur boss n'existe pas.");
					h.put("status", -1);
					return h;
				}
				
				if (! userEntrepriseRepository.hasSameInstitution(bossUser, groupe))
				{
					h.put("message", "Le boss et l'utilisateur ne sont pas de la même entreprise.");
					h.put("status", -1);
					return h;
				}
				userEntreprise.setBoss(bossUser);
			}
			//
			
			if(userEntrepriseRepository.existsByLogin(userEntreprise.getLogin()))
			{
				h.put("message", "Ce login existe déjà.");
				h.put("status", -1);
				return h;
			}
			
			if(userEntrepriseRepository.existsByEmail(userEntreprise.getEmail()))
			{
				h.put("message", "Cet email existe déjà.");
				h.put("status", -1);
				return h;
			}
			
			//Envoie du mail pour user admin entreprise
            String emailFrom = env.getProperty("spring.mail.username");
            if(!EmailValidator.getInstance().isValid(emailFrom))
            {
                h.put("status", -1);
                h.put("message", "L'email d'envoi est incorrect.");
                return h;
            }
            
            String emailTo = userEntreprise.getEmail();
            if(!EmailValidator.getInstance().isValid(emailTo))
            {
                h.put("status", -1);
                h.put("message", "L'email destinataire est incorrect.");
                return h;
            }
            
            String tokenString = userLoginService.getPasswordString();
            String loginUser = userEntreprise.getLogin();
            String emailObject = env.getProperty("email.object.add");
            //
            String cryptPassword = userLoginService.encryptData(tokenString);
            System.out.println("password crypte "+cryptPassword );

            userEntreprise.setPassword(cryptPassword);
			userEntreprise.setGroupeIdGroupe(groupe);
			userEntreprise.setProfilIdProfil(listProfile.get(0));
			userEntreprise.setDateCreation(new Date());
			userEntreprise = userEntrepriseRepository.save(userEntreprise);
			
			//userLoginService.sendMail(emailFrom, emailTo, emailObject, "Vos identitifants eTreasury.\n login: "+ loginUser + " et password: "+ tokenString );
			 List<Value> values= new ArrayList<Value>();
             values.add(new Value("","\n Identifiant : "+ loginUser + "\n et  Mot de passe: "+ tokenString));
             userLoginService.sendMailNew(emailFrom, emailTo, "eTreasury  | Votre inscription est validée", "Bienvenue sur eTreasury, votre inscription est effective. Vous pouvez vous connecter avec les identifiants suivants :\n", userEntreprise.getPrenom(), values,"Nous vous invitons à changer votre mot de passe dès votre première connexion.");
	        
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("userBank", userEntreprise);
		h.put("message", "L'utilisateur est ajouté avec succès.");
		h.put("status", 0);
	
		userService.journaliser(idAdminEntreprise, 6, "Ajout user_hability entreprise");
		
		return h;
	}
		
	
	@RequestMapping(value="/update/{idAdminEntreprise}", method=RequestMethod.PUT)
	public HashMap<String, Object> update(@RequestBody UserEntreprise userEntreprise,
										  @PathVariable Integer idAdminEntreprise) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		//Fields Control
		if(userEntreprise == null || idAdminEntreprise == null){
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
			return h;
		}
		if(userEntreprise.getIdUtilisateur() == null ){
			h.put("message", "L'identifiant de l'utilisateur n'est pas renseigné.");
			h.put("status", -1);
			return h;
		}
		//
		
		try
		{
			// User Admin Control
			if(userService.isAdminEntreprise(idAdminEntreprise))
			{
				h.put("status", -1);
				h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
				return h;
			}
			if(!userService.isSameInstitution(idAdminEntreprise, userEntreprise.getIdUtilisateur()))
			{
				h.put("status", -1);
				h.put("message", "L'utilisateur n'est pas de votre entreprise.");
				return h;
			}
			// End Control
			
			UserEntreprise userEntrepriseOld = userEntrepriseRepository.findOne(userEntreprise.getIdUtilisateur());
			if(userEntrepriseOld == null )
			{
				h.put("message", "Cet utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			if(userEntreprise.getGroupeIdGroupe() == null)
			{
				userEntreprise.setGroupeIdGroupe(userEntrepriseOld.getGroupeIdGroupe());
			}
			else
			{
				Integer idGroupe = userEntreprise.getGroupeIdGroupe().getIdGroupe();
				Groupe groupe = groupeRepository.findOne(idGroupe);
				if(groupe == null){
					h.put("message", "Le groupe n'existe pas.");
					h.put("status", -1);
					return h;
				}
				userEntreprise.setGroupeIdGroupe(groupe);
			}
			
			if(userEntreprise.getProfilIdProfil() == null)
			{
				userEntreprise.setProfilIdProfil(userEntrepriseOld.getProfilIdProfil());
			}
			else
			{
				Integer idProfil = userEntreprise.getProfilIdProfil().getIdProfil();
				Profile profil = profilRepository.findOne(idProfil);
				if(profil == null){
					h.put("message", "Le profil n'existe pas.");
					h.put("status", -1);
					return h;
				}
				userEntreprise.setProfilIdProfil(profil);
			}
			if(userEntreprise.getIsActive() == null)
				userEntreprise.setIsActive(userEntrepriseOld.getIsActive());
			
			//Email Control
			if(userEntreprise.getEmail() == null)
			{
				userEntreprise.setEmail(userEntrepriseOld.getEmail());
			}
			/*
			//Verfiier 	que l'email n'existe
			else if(userEntrepriseRepository.existsByEmailOthers(userEntreprise.getEmail(),userEntrepriseOld.getIdUtilisateur()))
			{
				//on verifie que l'email n'existe pas en BDD
				
				h.put("message", "Cet email existe déjà");
				h.put("status", -1);
				return h;
			}
			
			*/
			else if(!userService.isEmailValid(userEntreprise.getEmail()))
			{
				h.put("status", -1);
				h.put("message", "L'email est incorrect.");
				return h;
			}
			//
			
			if(userEntreprise.getBoss() == null)
				userEntreprise.setBoss(userEntrepriseOld.getBoss());
			if(userEntreprise.getDateCreation() == null)
				userEntreprise.setDateCreation(userEntrepriseOld.getDateCreation());
			
			// Boss
			if(userEntreprise.getBoss() == null)
				userEntreprise.setBoss(userEntrepriseOld.getBoss());
			else
			{
				User bossUser = userEntrepriseRepository.findOne(userEntreprise.getBoss().getIdUtilisateur());
				if(bossUser == null)
				{
					h.put("message", "L'utilisateur boss n'existe pas.");
					h.put("status", -1);
					return h;
				}
				
				if (! userEntrepriseRepository.hasSameInstitution(bossUser, userEntreprise.getGroupeIdGroupe()))
				{
					h.put("message", "Le boss et l'utilisateur ne sont pas de la même banque.");
					h.put("status", -1);
					return h;
				}
				userEntreprise.setBoss(bossUser);
			}
			//
			
			if(userEntreprise.getLogin() == null)
				userEntreprise.setLogin(userEntrepriseOld.getLogin());
			else
			{
				if (!(userEntreprise.getLogin().equals(userEntrepriseOld.getLogin()))){
	                /*
					//Verfiier 	que l'email n'existe
					 if(userEntrepriseRepository.existsByLoginOthers(userEntreprise.getLogin(),userEntrepriseOld.getIdUtilisateur()))
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
	                
	                String emailTo = userEntreprise.getEmail();
	                if(!EmailValidator.getInstance().isValid(emailTo))
	                {
	                    h.put("status", -1);
	                    h.put("message", "L'email destinataire est incorrect.");
	                    return h;
	                }
	                
	                String loginUser = userEntreprise.getLogin();
	                String emailObject = env.getProperty("email.object.add");
	               // userLoginService.sendMail(emailFrom, emailTo, emailObject, "Votre login a été modifié par l'administrateur.\n Votre nouveau login est : "+ loginUser );
	                List<Value> values= new ArrayList<Value>();
	                values.add(new Value("", loginUser));
	                userLoginService.sendMailNew(emailFrom, emailTo, "eTreasury  | Changement de login", "Votre login a été modifié par l'administrateur. Vous pouvez vous connecter avec l'identifiant suivant:", userEntreprise.getPrenom(), values,"Nous vous invitions à utiliser lors de votre prochaine connexion.");
	   	           
				}
			}
			
			if(userEntreprise.getNom() == null)
				userEntreprise.setNom(userEntrepriseOld.getNom());
			if(userEntreprise.getPassword() == null)
				userEntreprise.setPassword(userEntrepriseOld.getPassword());
			if(userEntreprise.getPhoto() == null)
				userEntreprise.setPhoto(userEntrepriseOld.getPhoto());
			if(userEntreprise.getPrenom() == null)
				userEntreprise.setPrenom(userEntrepriseOld.getPrenom());
			
			//Phone number Control
			if(userEntreprise.getTelephone() == null)
			{
				userEntreprise.setTelephone(userEntrepriseOld.getTelephone());
			}
			else if(!userService.isPhoneValid(userEntreprise.getTelephone()))
			{
				h.put("status", -1);
				h.put("message", "Le numéro de téléphone mobile est incorrect.");
				return h;
			}
			if(userEntreprise.getTelephoneFixe() == null)
			{
				userEntreprise.setTelephoneFixe(userEntrepriseOld.getTelephoneFixe());
			}
			else if(!userService.isPhoneValid(userEntreprise.getTelephoneFixe()))
			{
				h.put("status", -1);
				h.put("message", "Le numéro de téléphone fixe est incorrect.");
				return h;
			}
			//
			
			userEntreprise.setIdUtilisateur(userEntreprise.getIdUtilisateur());
			userEntreprise = userEntrepriseRepository.saveAndFlush(userEntreprise);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("userEntreprise", userEntreprise);
		h.put("message", "La modification s'est effectuée avec succès.");
		h.put("status", 0);
		
		userService.journaliser(idAdminEntreprise, 7, "Modification user_hability entreprise");
		
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
			UserEntreprise userEntreprise = userEntrepriseRepository.findOne(id);
			if (userEntreprise == null) {
				h.put("message", "L'utilisateur entreprise n'existe pas.");
				h.put("status", -1);
				return h;
			}
			h.put("message", "success");
			h.put("status", 0);
			h.put("entreprise", userEntreprise);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		return h;
	}
	
	@RequestMapping(value="/delete/{idAdminEntreprise}/{id}", method=RequestMethod.DELETE)
	public Map<String,Object> deleteUser(@PathVariable Integer idAdminEntreprise,
										 @PathVariable Integer id) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		//Fields Control
		if (idAdminEntreprise == null || id == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
			return h;
		}
		//
		
		try 
		{
			UserEntreprise userEntreprise = userEntrepriseRepository.findOne(id);
			if (userEntreprise == null)
			{
				h.put("message", "Cet utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			// User Admin Control
			if(userService.isAdminEntreprise(idAdminEntreprise))
			{
				h.put("status", -1);
				h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
				return h;
			}
			if(!userService.isSameInstitution(idAdminEntreprise, userEntreprise.getIdUtilisateur()))
			{
				h.put("status", -1);
				h.put("message", "L'utilisateur n'est pas de votre entreprise.");
				return h;
			}
			// End Control
			
			userEntrepriseRepository.delete(userEntreprise);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "L'utilisateur est supprimé avec succès.");
		h.put("status", 0);
		
		userService.journaliser(idAdminEntreprise, 8, "Suppression user_hability entreprise");
		
		return h;
	}
	
	@RequestMapping(value="/groupe/delete/{idAdminEntreprise}/{id}", method=RequestMethod.DELETE)
	public Map<String,Object> deleteFromGroup(@PathVariable Integer idAdminEntreprise,
										      @PathVariable Integer id){
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		//Fields Control
		if (idAdminEntreprise == null || id == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
			return h;
		}
		//
		
		try
		{
			UserEntreprise user = userEntrepriseRepository.findOne(id);
			if(user == null)
			{
				h.put("message", "Cet utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			// User Admin Control
			if(userService.isAdminEntreprise(idAdminEntreprise))
			{
				h.put("status", -1);
				h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
				return h;
			}
			if(!userService.isSameInstitution(idAdminEntreprise, id))
			{
				h.put("status", -1);
				h.put("message", "L'utilisateur n'est pas de votre entreprise.");
				return h;
			}
			// End Control
			
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
			userEntrepriseRepository.updateGroup(defaultGroupe, id);
		}catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "L'utilisateur entreprise a été bien supprimé de ce groupe.");
		h.put("status", 0);
		
		userService.journaliser(idAdminEntreprise, 8, "Suppression user_hability entreprise dans groupe");
		
		return h;
	}
	
}