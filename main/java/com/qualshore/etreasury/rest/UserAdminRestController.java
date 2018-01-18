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
import com.qualshore.etreasury.dao.UserAdminRepository;
import com.qualshore.etreasury.dao.UserBankRepository;
import com.qualshore.etreasury.dao.UserEntrepriseRepository;
import com.qualshore.etreasury.dao.UserRepository;
import com.qualshore.etreasury.entity.Groupe;
import com.qualshore.etreasury.entity.Institution;
import com.qualshore.etreasury.entity.Profile;
import com.qualshore.etreasury.entity.User;
import com.qualshore.etreasury.entity.UserAdmin;
import com.qualshore.etreasury.entity.UserBanque;
import com.qualshore.etreasury.entity.UserEntreprise;
import com.qualshore.etreasury.mail.Value;
import com.qualshore.etreasury.service.UserLoginService;
import com.qualshore.etreasury.service.UserService;

@RequestMapping("etreasury_project/admin/user_admin")
@RestController
public class UserAdminRestController {
	
	@Autowired
	UserAdminRepository userAdminRepository;
	
	@Autowired
	UserBankRepository userBankRepository;
	
	@Autowired
	UserEntrepriseRepository userEntrepriseRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	GroupRepository groupeRepository;
	
	@Autowired
	ProfileRepository profilRepository;
	
	@Autowired
	InstitutionRepository institutionRepository;
	
	@Autowired
	UserService userService;
	
	@Autowired
    Environment env;
	
	@Autowired
	UserLoginService userLoginService;
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public Map<String,Object> getUserAdmins(){
	
		//List<UserAdmin> userAdmins = userAdminRepository.findAll();
		List<UserAdmin> userAdmins = userAdminRepository.findAllByOrderByIdUtilisateurDesc();
		HashMap<String, Object> h = new HashMap<String, Object>();
		h.put("message", "liste des users admin");
		h.put("status", 0);
		h.put("admin_list", userAdmins);
		return h;
	}
	
	//Ajout Administrateur eTreasury
	@RequestMapping(value="/add/{idAdmin}", method=RequestMethod.POST)
	public HashMap<String, Object> addAdminE(@RequestBody UserAdmin userAdmin, @PathVariable Integer idAdmin){
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		//Fields Control
		if(userAdmin == null || idAdmin == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		if(userAdmin.getNom().equals("") || userAdmin.getPrenom().equals("") || userAdmin.getTelephone().equals("") ||
				userAdmin.getEmail().equals("") || userAdmin.getLogin().equals(""))
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres vides");
			return h;
		}
		if(! userService.isEmailValid(userAdmin.getEmail()))
		{
			h.put("status", -1);
			h.put("message", "L'email est incorrect.");
			return h;
		}
		if(! userService.isPhoneValid(userAdmin.getTelephone()))
		{
			h.put("status", -1);
			h.put("message", "Le numéro de téléphone mobile est incorrect.");
			return h;
		}
		if(userAdmin.getTelephoneFixe() != null && !userService.isPhoneValid(userAdmin.getTelephoneFixe()))
		{
			h.put("status", -1);
			h.put("message", "Le numéro de téléphone fixe est incorrect.");
			return h;
		}
		//
		
		try {
			if(userService.isAdminGeneral(idAdmin))
			{
				h.put("status", -1);
				h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
				return h;
			}
			
			UserAdmin userAd = userAdminRepository.findOne(idAdmin);
			System.out.println("Institution "+ userAd.getGroupeIdGroupe().getInstitution());
			
			Groupe grp = groupeRepository.findByInstitionDefault(userAd.getGroupeIdGroupe().getInstitution());
			if(grp == null )
			{
				h.put("message", "Le groupe admin n'existe pas.");
				h.put("status", -1);
				return h;
			}		
			System.out.println("2222");
			
			
			// Boss
			if(userAdmin.getBoss() != null)
			{
				User bossUser = userRepository.findOne(userAdmin.getBoss().getIdUtilisateur());
				if(bossUser == null)
				{
					h.put("message", "L'utilisateur boss n'existe pas.");
					h.put("status", -1);
					return h;
				}
				
				if (! userRepository.hasSameInstitution(bossUser, grp))
				{
					h.put("message", "Le boss et l'utilisateur ne sont pas de la même institution.");
					h.put("status", -1);
					return h;
				}
				userAdmin.setBoss(bossUser);
			}
			//
			
			List<Profile> listProfile = profilRepository.findByType("ROLE_SUPER_ADMIN");
			if(listProfile == null || listProfile.isEmpty())
			{
				h.put("message", "Le pofil SUPER_ADMIN  n'existe pas.");
				h.put("status", -1);
				return h;
			}
			System.out.println("33333");
			
			if(userAdminRepository.existsByLogin(userAdmin.getLogin()))
			{
				h.put("message", "Ce login existe déjà.");
				h.put("status", -1);
				return h;
			}
			
			if(userAdminRepository.existsByEmail(userAdmin.getEmail()))
			{
				h.put("message", "Cet email existe déjà.");
				h.put("status", -1);
				return h;
			}
			System.out.println("4444");
			
			//Envoie du mail pour user admin banque
            String emailFrom = env.getProperty("spring.mail.username");
            if(!EmailValidator.getInstance().isValid(emailFrom))
            {
                h.put("status", -1);
                h.put("message", "L'email d'envoi est incorrect.");
                return h;
            }
            
            String emailTo = userAdmin.getEmail();
            if(!EmailValidator.getInstance().isValid(emailTo))
            {
                h.put("status", -1);
                h.put("message", "L'email destinataire est incorrect.");
                return h;
            }
            
            String tokenString = userLoginService.getPasswordString();
            String loginUser = userAdmin.getLogin();
            String emailObject = env.getProperty("email.object.add");
            //
            String cryptPassword = userLoginService.encryptData(tokenString);
            System.out.println("password crypte "+cryptPassword );

            userAdmin.setPassword(cryptPassword);
			userAdmin.setGroupeIdGroupe(grp);
			userAdmin.setProfilIdProfil(listProfile.get(0));
			userAdmin.setDateCreation(new Date());
			userAdmin = userAdminRepository.save(userAdmin);
			
			//userLoginService.sendMail(emailFrom, emailTo, emailObject, "Vos identitifants eTreasury.\n login: "+ loginUser + " et password: "+ tokenString );
			  List<Value> values= new ArrayList<Value>();
              values.add(new Value("","\n Identifiant : "+ loginUser + "\n et  Mot de passe: "+ tokenString));
              userLoginService.sendMailNew(emailFrom, emailTo, "eTreasury  | Votre inscription est validée", "Bienvenue sur eTreasury, votre inscription est effective. Vous pouvez vous connecter avec les identifiants suivants :\n", userAdmin.getPrenom(), values,"Nous vous invitons à changer votre mot de passe dès votre première connexion.");
	          
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("userBank", userAdmin);
		h.put("message", "L'administrateur est ajouté avec succès.");
		h.put("status", 0);
		
		userService.journaliser(idAdmin, 6, "Ajout Administrateur eTreasury");
		
		return h;
	}
		
	//Ajout d'un utilisateur banque
	@RequestMapping(value="/add/bank/profile/{idAdmin}/{idInstitution}", method=RequestMethod.POST)
	public HashMap<String, Object> add(@RequestBody UserBanque userAdmin, @PathVariable Integer idAdmin, @PathVariable Integer idInstitution){
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		//Fields Control
		if(userAdmin == null || idAdmin == null || idInstitution == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		if(userAdmin.getNom().equals("") || userAdmin.getPrenom().equals("") || userAdmin.getTelephone().equals("") ||
				userAdmin.getEmail().equals("") || userAdmin.getLogin().equals(""))
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres vides");
			return h;
		}
		if(! userService.isEmailValid(userAdmin.getEmail()))
		{
			h.put("status", -1);
			h.put("message", "L'email est incorrect.");
			return h;
		}
		if(! userService.isPhoneValid(userAdmin.getTelephone()))
		{
			h.put("status", -1);
			h.put("message", "Le numéro de téléphone mobile est incorrect.");
			return h;
		}
		if(userAdmin.getTelephoneFixe() != null && !userService.isPhoneValid(userAdmin.getTelephoneFixe()))
		{
			h.put("status", -1);
			h.put("message", "Le numéro de téléphone fixe est incorrect.");
			return h;
		}
		//
		
		try
		{
			if(userService.isAdminGeneral(idAdmin))
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
			
			Groupe grp = groupeRepository.findByInstitionDefault(institution);
			if(grp == null )
			{
				h.put("message", "Le groupe admin n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			List<Profile> listProfile = profilRepository.findByType("ADMIN_BANQUE");
			if(listProfile == null || listProfile.isEmpty())
			{
				h.put("message", "Le pofil administrateur banque  n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			// Boss
			if(userAdmin.getBoss() != null)
			{
				User bossUser = userRepository.findOne(userAdmin.getBoss().getIdUtilisateur());
				if(bossUser == null)
				{
					h.put("message", "L'utilisateur boss n'existe pas.");
					h.put("status", -1);
					return h;
				}
				
				if (! userRepository.hasSameInstitution(bossUser, grp))
				{
					h.put("message", "Le boss et l'utilisateur ne sont pas de la même institution.");
					h.put("status", -1);
					return h;
				}
				userAdmin.setBoss(bossUser);
			}
			//
			
			if(userAdminRepository.existsByLogin(userAdmin.getLogin()))
			{
				h.put("message", "Ce login existe déjà.");
				h.put("status", -1);
				return h;
			}
			
			if(userAdminRepository.existsByEmail(userAdmin.getEmail()))
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
            
            String emailTo = userAdmin.getEmail();
            if(!EmailValidator.getInstance().isValid(emailTo))
            {
                h.put("status", -1);
                h.put("message", "L'email destinataire est incorrect.");
                return h;
            }
            
            String tokenString = userLoginService.getPasswordString();
            String loginUser = userAdmin.getLogin();
            String emailObject = env.getProperty("email.object.add");
            //
            String cryptPassword = userLoginService.encryptData(tokenString);
            System.out.println("password crypte "+cryptPassword );

            userAdmin.setPassword(cryptPassword);
			userAdmin.setGroupeIdGroupe(grp);
			userAdmin.setProfilIdProfil(listProfile.get(0));
			userAdmin.setDateCreation(new Date());
			userAdmin = userBankRepository.save(userAdmin);
			
			//userLoginService.sendMail(emailFrom, emailTo, emailObject, "Vos identitifants eTreasury.\n login: "+ loginUser + " et password: "+ tokenString );
			 List<Value> values= new ArrayList<Value>();
             values.add(new Value("","\n Identifiant : "+ loginUser + "\n et  Mot de passe: "+ tokenString));
             userLoginService.sendMailNew(emailFrom, emailTo, "eTreasury  | Votre inscription est validée", "Bienvenue sur eTreasury, votre inscription est effective. Vous pouvez vous connecter avec les identifiants suivants :\n", userAdmin.getPrenom(), values,"Nous vous invitons à changer votre mot de passe dès votre première connexion.");
	          
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("userBank", userAdmin);
		h.put("message", "L'ajout de l'administrateur banque s'est effectuée avec succès.");
		h.put("status", 0);
		
		userService.journaliser(idAdmin, 6, "Ajout Administrateur Banque");
		
		return h;
	}
		
		//Ajout d'un admin banque appartenant au groupe admin_banque
		@RequestMapping(value="/add/bank/profile/admin/{idAdmin}/{idInstitution}", method=RequestMethod.POST)
		public HashMap<String, Object> addAdminBank(@RequestBody UserBanque userAdmin, @PathVariable Integer idAdmin, @PathVariable Integer idInstitution){
			HashMap<String, Object> h = new HashMap<String, Object>();
			
			//Fields Control
			if(userAdmin == null || idAdmin == null || idInstitution == null)
			{
				h.put("status", -1);
				h.put("message", "1 ou plusieurs paramètres manquants");
				return h;
			}
			if(userAdmin.getNom().equals("") || userAdmin.getPrenom().equals("") || userAdmin.getTelephone().equals("") ||
					userAdmin.getEmail().equals("") || userAdmin.getLogin().equals(""))
			{
				h.put("status", -1);
				h.put("message", "1 ou plusieurs paramètres vides");
				return h;
			}
			if(! userService.isEmailValid(userAdmin.getEmail()))
			{
				h.put("status", -1);
				h.put("message", "L'email est incorrect.");
				return h;
			}
			if(! userService.isPhoneValid(userAdmin.getTelephone()))
			{
				h.put("status", -1);
				h.put("message", "Le numéro de téléphone mobile est incorrect.");
				return h;
			}
			if(userAdmin.getTelephoneFixe() != null && !userService.isPhoneValid(userAdmin.getTelephoneFixe()))
			{
				h.put("status", -1);
				h.put("message", "Le numéro de téléphone fixe est incorrect.");
				return h;
			}
			//
			
			try
			{
				if(userService.isAdminGeneral(idAdmin))
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
				
				Groupe grp = groupeRepository.findByInstition(institution);
				if(grp == null )
				{
					h.put("message", "Le groupe admin n'existe pas.");
					h.put("status", -1);
					return h;
				}
				
				List<Profile> listProfile = profilRepository.findByType("ADMIN_BANQUE");
				if(listProfile == null || listProfile.isEmpty())
				{
					h.put("message", "Le pofil administrateur banque n'existe pas.");
					h.put("status", -1);
					return h;
				}
				
				// Boss
				if(userAdmin.getBoss() != null)
				{
					User bossUser = userRepository.findOne(userAdmin.getBoss().getIdUtilisateur());
					if(bossUser == null)
					{
						h.put("message", "L'utilisateur boss n'existe pas.");
						h.put("status", -1);
						return h;
					}
					
					if (! userRepository.hasSameInstitution(bossUser, grp))
					{
						h.put("message", "Le boss et l'utilisateur ne sont pas de la même institution.");
						h.put("status", -1);
						return h;
					}
					userAdmin.setBoss(bossUser);
				}
				//
				
				if(userAdminRepository.existsByLogin(userAdmin.getLogin()))
				{
					h.put("message", "Ce login existe déjà.");
					h.put("status", -1);
					return h;
				}
				
				if(userAdminRepository.existsByEmail(userAdmin.getEmail()))
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
	            
	            String emailTo = userAdmin.getEmail();
	            if(!EmailValidator.getInstance().isValid(emailTo))
	            {
	                h.put("status", -1);
	                h.put("message", "L'email destinataire est incorrect.");
	                return h;
	            }
	            
	            String tokenString = userLoginService.getPasswordString();
	            String loginUser = userAdmin.getLogin();
	            String emailObject = env.getProperty("email.object.add");
	            //
	            String cryptPassword = userLoginService.encryptData(tokenString);
	            System.out.println("password crypte "+cryptPassword );

	            userAdmin.setPassword(cryptPassword);
				userAdmin.setGroupeIdGroupe(grp);
				userAdmin.setProfilIdProfil(listProfile.get(0));
				userAdmin.setDateCreation(new Date());
				userAdmin = userBankRepository.save(userAdmin);
				
				//userLoginService.sendMail(emailFrom, emailTo, emailObject, "Vos identitifants eTreasury.\n login: "+ loginUser + " et password: "+ tokenString );
				 List<Value> values= new ArrayList<Value>();
	             values.add(new Value("","\n Identifiant : "+ loginUser + "\n et  Mot de passe: "+ tokenString));
	             userLoginService.sendMailNew(emailFrom, emailTo, "eTreasury  | Votre inscription est validée", "Bienvenue sur eTreasury, votre inscription est effective. Vous pouvez vous connecter avec les identifiants suivants :\n", userAdmin.getPrenom(), values,"Nous vous invitons à changer votre mot de passe dès votre première connexion.");
		        
				
			} catch (Exception e) {
				e.printStackTrace();
				h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
				h.put("status", -1);
				return h;
			}
			h.put("userBank", userAdmin);
			h.put("message", "L'ajout de l'administrateur banque s'est effectuée avec succès.");
			h.put("status", 0);
			
			userService.journaliser(idAdmin, 6, "Ajout Administrateur Banque");
			
			return h;
		}
				
		
		//Ajout d'un admin banque avec des profile
		@RequestMapping(value="/add/bank/{idAdmin}/{idInstitution}", method=RequestMethod.POST)
		public HashMap<String, Object> addUseBank(@RequestBody UserBanque userAdmin, @PathVariable Integer idAdmin, @PathVariable Integer idInstitution){
			HashMap<String, Object> h = new HashMap<String, Object>();
			
			//Fields Control
			if(userAdmin == null || idAdmin == null || idInstitution == null)
			{
				h.put("status", -1);
				h.put("message", "1 ou plusieurs paramètres manquants");
				return h;
			}
			if(userAdmin.getNom().equals("") || userAdmin.getPrenom().equals("") || userAdmin.getTelephone().equals("") ||
					userAdmin.getEmail().equals("") || userAdmin.getLogin().equals(""))
			{
				h.put("status", -1);
				h.put("message", "1 ou plusieurs paramètres vides");
				return h;
			}
			if(! userService.isEmailValid(userAdmin.getEmail()))
			{
				h.put("status", -1);
				h.put("message", "L'email est incorrect.");
				return h;
			}
			if(! userService.isPhoneValid(userAdmin.getTelephone()))
			{
				h.put("status", -1);
				h.put("message", "Le numéro de téléphone mobile est incorrect.");
				return h;
			}
			if(userAdmin.getTelephoneFixe() != null && !userService.isPhoneValid(userAdmin.getTelephoneFixe()))
			{
				h.put("status", -1);
				h.put("message", "Le numéro de téléphone fixe est incorrect.");
				return h;
			}
			//
			
			try
			{
				if(userService.isAdminGeneral(idAdmin))
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
				
				Groupe grp = groupeRepository.findByInstitionDefault(institution);
				if(grp == null )
				{
					h.put("message", "Le groupe admin n'existe pas.");
					h.put("status", -1);
					return h;
				}
				
				List<Profile> listProfile = profilRepository.findByType("USER_HABILITY");
				if(listProfile == null || listProfile.isEmpty())
				{
					h.put("message", "Le pofil utilisateur  n'existe pas.");
					h.put("status", -1);
					return h;
				}
				
				// Boss
				if(userAdmin.getBoss() != null)
				{
					User bossUser = userRepository.findOne(userAdmin.getBoss().getIdUtilisateur());
					if(bossUser == null)
					{
						h.put("message", "L'utilisateur boss n'existe pas.");
						h.put("status", -1);
						return h;
					}
					
					if (! userRepository.hasSameInstitution(bossUser, grp))
					{
						h.put("message", "Le boss et l'utilisateur ne sont pas de la même institution.");
						h.put("status", -1);
						return h;
					}
					userAdmin.setBoss(bossUser);
				}
				//
				
				if(userAdminRepository.existsByLogin(userAdmin.getLogin()))
				{
					h.put("message", "Ce login existe déjà.");
					h.put("status", -1);
					return h;
				}
				
				if(userAdminRepository.existsByEmail(userAdmin.getEmail()))
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
	            
	            String emailTo = userAdmin.getEmail();
	            if(!EmailValidator.getInstance().isValid(emailTo))
	            {
	                h.put("status", -1);
	                h.put("message", "L'email destinataire est incorrect.");
	                return h;
	            }
	            
	            String tokenString = userLoginService.getPasswordString();
	            String loginUser = userAdmin.getLogin();
	            String emailObject = env.getProperty("email.object.add");
	            //
	            String cryptPassword = userLoginService.encryptData(tokenString);
	            System.out.println("password crypte "+cryptPassword );

	            userAdmin.setPassword(cryptPassword);
				userAdmin.setGroupeIdGroupe(grp);
				userAdmin.setProfilIdProfil(listProfile.get(0));
				//userAdmin.setProfilIdProfil(profil);
				userAdmin.setDateCreation(new Date());
				userAdmin = userBankRepository.save(userAdmin);
				
				//userLoginService.sendMail(emailFrom, emailTo, emailObject, "Vos identitifants eTreasury.\n login: "+ loginUser + " et password: "+ tokenString );
				 List<Value> values= new ArrayList<Value>();
	             values.add(new Value("","\n Identifiant : "+ loginUser + "\n et  Mot de passe: "+ tokenString));
	             userLoginService.sendMailNew(emailFrom, emailTo, "eTreasury  | Votre inscription est validée", "Bienvenue sur eTreasury, votre inscription est effective. Vous pouvez vous connecter avec les identifiants suivants :\n", userAdmin.getPrenom(), values,"Nous vous invitons à changer votre mot de passe dès votre première connexion.");
		        
			} catch (Exception e) {
				e.printStackTrace();
				h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
				h.put("status", -1);
				return h;
			}
			h.put("userBank", userAdmin);
			h.put("message", "L'ajout de l'administrateur banque s'est effectuée avec succès.");
			h.put("status", 0);
			
			userService.journaliser(idAdmin, 6, "Ajout Administrateur Banque");
			
			return h;
		}
		
				
		//Ajout d'un utilisateur banque avec le profile user_hability par admin eTreasury
		@RequestMapping(value="/add/bank/user_hability/{idAdmin}/{idInstitution}", method=RequestMethod.POST)
		public HashMap<String, Object> addUseHabilityBank(@RequestBody UserBanque userAdmin, @PathVariable Integer idAdmin, @PathVariable Integer idInstitution){
			HashMap<String, Object> h = new HashMap<String, Object>();
			
			//Fields Control
			if(userAdmin == null || idAdmin == null || idInstitution == null)
			{
				h.put("status", -1);
				h.put("message", "1 ou plusieurs paramètres manquants");
				return h;
			}
			if(userAdmin.getNom().equals("") || userAdmin.getPrenom().equals("") || userAdmin.getTelephone().equals("") ||
					userAdmin.getEmail().equals("") || userAdmin.getLogin().equals(""))
			{
				h.put("status", -1);
				h.put("message", "1 ou plusieurs paramètres vides");
				return h;
			}
			if(! userService.isEmailValid(userAdmin.getEmail()))
			{
				h.put("status", -1);
				h.put("message", "L'email est incorrect.");
				return h;
			}
			if(! userService.isPhoneValid(userAdmin.getTelephone()))
			{
				h.put("status", -1);
				h.put("message", "Le numéro de téléphone mobile est incorrect.");
				return h;
			}
			if(userAdmin.getTelephoneFixe() != null && !userService.isPhoneValid(userAdmin.getTelephoneFixe()))
			{
				h.put("status", -1);
				h.put("message", "Le numéro de téléphone fixe est incorrect.");
				return h;
			}
			//
			
			try
			{
				if(userService.isAdminGeneral(idAdmin))
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
				
				Groupe grp = groupeRepository.findByInstitionDefault(institution);
				if(grp == null )
				{
					h.put("message", "Le groupe administrateur n'existe pas.");
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
				if(userAdmin.getBoss() != null)
				{
					User bossUser = userRepository.findOne(userAdmin.getBoss().getIdUtilisateur());
					if(bossUser == null)
					{
						h.put("message", "L'utilisateur boss n'existe pas.");
						h.put("status", -1);
						return h;
					}
					
					if (! userRepository.hasSameInstitution(bossUser, grp))
					{
						h.put("message", "Le boss et l'utilisateur ne sont pas de la même institution.");
						h.put("status", -1);
						return h;
					}
					userAdmin.setBoss(bossUser);
				}
				//
				
				if(userAdminRepository.existsByLogin(userAdmin.getLogin()))
				{
					h.put("message", "Ce login existe déjà.");
					h.put("status", -1);
					return h;
				}
				
				if(userAdminRepository.existsByEmail(userAdmin.getEmail()))
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
	            
	            String emailTo = userAdmin.getEmail();
	            if(!EmailValidator.getInstance().isValid(emailTo))
	            {
	                h.put("status", -1);
	                h.put("message", "L'email destinataire est incorrect.");
	                return h;
	            }
	            
	            String tokenString = userLoginService.getPasswordString();
	            String loginUser = userAdmin.getLogin();
	            String emailObject = env.getProperty("email.object.add");
	            //
	            String cryptPassword = userLoginService.encryptData(tokenString);
	            System.out.println("password crypte "+cryptPassword );

	            userAdmin.setPassword(cryptPassword);
				userAdmin.setGroupeIdGroupe(grp);
				userAdmin.setProfilIdProfil(listProfile.get(0));
				userAdmin.setDateCreation(new Date());
				userAdmin = userBankRepository.save(userAdmin);
				
				//userLoginService.sendMail(emailFrom, emailTo, emailObject, "Vos identitifants eTreasury.\n login: "+ loginUser + " et password: "+ tokenString );
				 List<Value> values= new ArrayList<Value>();
	             values.add(new Value("","\n Identifiant : "+ loginUser + "\n et  Mot de passe: "+ tokenString));
	             userLoginService.sendMailNew(emailFrom, emailTo, "eTreasury  | Votre inscription est validée", "Bienvenue sur eTreasury, votre inscription est effective. Vous pouvez vous connecter avec les identifiants suivants :\n", userAdmin.getPrenom(), values,"Nous vous invitons à changer votre mot de passe dès votre première connexion.");
		        
			} catch (Exception e) {
				e.printStackTrace();
				h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
				h.put("status", -1);
				return h;
			}
			h.put("userBank", userAdmin);
			h.put("message", "L'utilisateur administrateur est ajouté avec succès.");
			h.put("status", 0);
			
			userService.journaliser(idAdmin, 6, "Ajout Administrateur Banque");
			
			return h;
		}
			
		//Ajout d'un admin Entreprise
		@RequestMapping(value="/add/entreprise/profile/{idAdmin}/{idInstitution}", method=RequestMethod.POST)
		public HashMap<String, Object> addEntreprise(@RequestBody UserEntreprise userAdmin, @PathVariable Integer idAdmin, @PathVariable Integer idInstitution){
			HashMap<String, Object> h = new HashMap<String, Object>();
			
			//Fields Control
			if(userAdmin == null || idAdmin == null || idInstitution == null)
			{
				h.put("status", -1);
				h.put("message", "1 ou plusieurs paramètres manquants");
				return h;
			}
			if(userAdmin.getNom().equals("") || userAdmin.getPrenom().equals("") || userAdmin.getTelephone().equals("") ||
					userAdmin.getEmail().equals("") || userAdmin.getLogin().equals(""))
			{
				h.put("status", -1);
				h.put("message", "1 ou plusieurs paramètres vides");
				return h;
			}
			if(! userService.isEmailValid(userAdmin.getEmail()))
			{
				h.put("status", -1);
				h.put("message", "L'email est incorrect.");
				return h;
			}
			if(! userService.isPhoneValid(userAdmin.getTelephone()))
			{
				h.put("status", -1);
				h.put("message", "Le numéro de téléphone mobile est incorrect.");
				return h;
			}
			if(userAdmin.getTelephoneFixe() != null && !userService.isPhoneValid(userAdmin.getTelephoneFixe()))
			{
				h.put("status", -1);
				h.put("message", "Le numéro de téléphone fixe est incorrect.");
				return h;
			}
			//
			
			try
			{
				if(userService.isAdminGeneral(idAdmin))
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
				
				Groupe grp = groupeRepository.findByInstitionDefault(institution);
				if(grp == null )
				{
					h.put("message", "Le groupe admin n'existe pas.");
					h.put("status", -1);
					return h;
				}
				
				List<Profile> listProfile = profilRepository.findByType("ADMIN_ENTREPRISE");
				if(listProfile == null || listProfile.isEmpty())
				{
					h.put("message", "Le pofil administrateur entreprise n'existe pas.");
					h.put("status", -1);
					return h;
				}
				
				// Boss
				if(userAdmin.getBoss() != null)
				{
					User bossUser = userRepository.findOne(userAdmin.getBoss().getIdUtilisateur());
					if(bossUser == null)
					{
						h.put("message", "L'utilisateur boss n'existe pas.");
						h.put("status", -1);
						return h;
					}
					
					if (! userRepository.hasSameInstitution(bossUser, grp))
					{
						h.put("message", "Le boss et l'utilisateur ne sont pas de la même institution.");
						h.put("status", -1);
						return h;
					}
					userAdmin.setBoss(bossUser);
				}
				//
				
				if(userAdminRepository.existsByLogin(userAdmin.getLogin()))
				{
					h.put("message", "Ce login existe déjà.");
					h.put("status", -1);
					return h;
				}
				
				if(userAdminRepository.existsByEmail(userAdmin.getEmail()))
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
	            
	            String emailTo = userAdmin.getEmail();
	            if(!EmailValidator.getInstance().isValid(emailTo))
	            {
	                h.put("status", -1);
	                h.put("message", "L'email destinataire est incorrect.");
	                return h;
	            }
	            
	            String tokenString = userLoginService.getPasswordString();
	            String loginUser = userAdmin.getLogin();
	            String emailObject = env.getProperty("email.object.add");
	            //
	            String cryptPassword = userLoginService.encryptData(tokenString);
	            System.out.println("password crypte "+cryptPassword );

	            userAdmin.setPassword(cryptPassword);
				userAdmin.setGroupeIdGroupe(grp);
				userAdmin.setProfilIdProfil(listProfile.get(0));
				userAdmin.setDateCreation(new Date());
				userAdmin = userEntrepriseRepository.save(userAdmin);
				
				//userLoginService.sendMail(emailFrom, emailTo, emailObject, "Vos identitifants eTreasury.\n login: "+ loginUser + " et password: "+ tokenString );
				 List<Value> values= new ArrayList<Value>();
	             values.add(new Value("","\n Identifiant : "+ loginUser + "\n et  Mot de passe: "+ tokenString));
	             userLoginService.sendMailNew(emailFrom, emailTo, "eTreasury  | Votre inscription est validée", "Bienvenue sur eTreasury, votre inscription est effective. Vous pouvez vous connecter avec les identifiants suivants :\n", userAdmin.getPrenom(), values,"Nous vous invitons à changer votre mot de passe dès votre première connexion.");
		        
			} catch (Exception e) {
				e.printStackTrace();
				h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
				h.put("status", -1);
				return h;
			}
			h.put("userBank", userAdmin);
			h.put("message", "L'administrateur est ajouté avec succès.");
			h.put("status", 0);
			
			userService.journaliser(idAdmin, 6, "Ajout Administrateur Entreprise");
			
			return h;
		}
		
		
		//Ajout d'un admin Entreprise appartenant au groupe admin_entreprise
		@RequestMapping(value="/add/entreprise/profile/admin/{idAdmin}/{idInstitution}", method=RequestMethod.POST)
		public HashMap<String, Object> addAdminEntreprise(@RequestBody UserEntreprise userAdmin, @PathVariable Integer idAdmin, @PathVariable Integer idInstitution){
			HashMap<String, Object> h = new HashMap<String, Object>();
			
			//Fields Control
			if(userAdmin == null || idAdmin == null || idInstitution == null)
			{
				h.put("status", -1);
				h.put("message", "1 ou plusieurs paramètres manquants");
				return h;
			}
			if(userAdmin.getNom().equals("") || userAdmin.getPrenom().equals("") || userAdmin.getTelephone().equals("") ||
					userAdmin.getEmail().equals("") || userAdmin.getLogin().equals(""))
			{
				h.put("status", -1);
				h.put("message", "1 ou plusieurs paramètres vides");
				return h;
			}
			if(! userService.isEmailValid(userAdmin.getEmail()))
			{
				h.put("status", -1);
				h.put("message", "L'email est incorrect.");
				return h;
			}
			if(! userService.isPhoneValid(userAdmin.getTelephone()))
			{
				h.put("status", -1);
				h.put("message", "Le numéro de téléphone mobile est incorrect.");
				return h;
			}
			if(userAdmin.getTelephoneFixe() != null && !userService.isPhoneValid(userAdmin.getTelephoneFixe()))
			{
				h.put("status", -1);
				h.put("message", "Le numéro de téléphone fixe est incorrect.");
				return h;
			}
			//
			
			try
			{
				if(userService.isAdminGeneral(idAdmin))
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
				
				Groupe grp = groupeRepository.findByInstitionDefault(institution);
				if(grp == null )
				{
					h.put("message", "Le groupe administrateur n'existe pas.");
					h.put("status", -1);
					return h;
				}
				
				List<Profile> listProfile = profilRepository.findByType("ADMIN_ENTREPRISE");
				if(listProfile == null || listProfile.isEmpty())
				{
					h.put("message", "Le pofil administrateur entreprise n'existe pas.");
					h.put("status", -1);
					return h;
				}
				
				// Boss
				if(userAdmin.getBoss() != null)
				{
					User bossUser = userRepository.findOne(userAdmin.getBoss().getIdUtilisateur());
					if(bossUser == null)
					{
						h.put("message", "L'utilisateur boss n'existe pas.");
						h.put("status", -1);
						return h;
					}
					
					if (! userRepository.hasSameInstitution(bossUser, grp))
					{
						h.put("message", "Le boss et l'utilisateur ne sont pas de la même institution.");
						h.put("status", -1);
						return h;
					}
					userAdmin.setBoss(bossUser);
				}
				//
				
				if(userAdminRepository.existsByLogin(userAdmin.getLogin()))
				{
					h.put("message", "Ce login existe déjà.");
					h.put("status", -1);
					return h;
				}
				
				if(userAdminRepository.existsByEmail(userAdmin.getEmail()))
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
	            
	            String emailTo = userAdmin.getEmail();
	            if(!EmailValidator.getInstance().isValid(emailTo))
	            {
	                h.put("status", -1);
	                h.put("message", "L'email destinataire est incorrect.");
	                return h;
	            }
	            
	            String tokenString = userLoginService.getPasswordString();
	            String loginUser = userAdmin.getLogin();
	            String emailObject = env.getProperty("email.object.add");
	            //
	            String cryptPassword = userLoginService.encryptData(tokenString);
	            System.out.println("password crypte "+cryptPassword );

	            userAdmin.setPassword(cryptPassword);
				userAdmin.setGroupeIdGroupe(grp);
				userAdmin.setProfilIdProfil(listProfile.get(0));
				userAdmin.setDateCreation(new Date());
				userAdmin = userEntrepriseRepository.save(userAdmin);
				
				//userLoginService.sendMail(emailFrom, emailTo, emailObject, "Vos identitifants eTreasury.\n login: "+ loginUser + " et password: "+ tokenString );
				 List<Value> values= new ArrayList<Value>();
	             values.add(new Value("","\n Identifiant : "+ loginUser + "\n et  Mot de passe: "+ tokenString));
	             userLoginService.sendMailNew(emailFrom, emailTo, "eTreasury  | Votre inscription est validée", "Bienvenue sur eTreasury, votre inscription est effective. Vous pouvez vous connecter avec les identifiants suivants :\n", userAdmin.getPrenom(), values,"Nous vous invitons à changer votre mot de passe dès votre première connexion.");
		        
			} catch (Exception e) {
				e.printStackTrace();
				h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
				h.put("status", -1);
				return h;
			}
			h.put("userBank", userAdmin);
			h.put("message", "L'utilisateur est ajouté avec succès.");
			h.put("status", 0);
			
			userService.journaliser(idAdmin, 6, "Ajout Administrateur Entreprise");
			
			return h;
		}
		
		//Ajout d'un admin Entreprise avec les 2 profiles
		@RequestMapping(value="/add/entreprise/{idAdmin}/{idInstitution}", method=RequestMethod.POST)
		public HashMap<String, Object> addUserEntreprise(@RequestBody UserEntreprise userAdmin, @PathVariable Integer idAdmin, @PathVariable Integer idInstitution){
			HashMap<String, Object> h = new HashMap<String, Object>();
			//Fields Control
			if(userAdmin == null || idAdmin == null || idInstitution == null)
			{
				h.put("status", -1);
				h.put("message", "1 ou plusieurs paramètres manquants");
				return h;
			}
			if(userAdmin.getNom().equals("") || userAdmin.getPrenom().equals("") || userAdmin.getTelephone().equals("") ||
					userAdmin.getEmail().equals("") || userAdmin.getLogin().equals(""))
			{
				h.put("status", -1);
				h.put("message", "1 ou plusieurs paramètres vides");
				return h;
			}
			if(! userService.isEmailValid(userAdmin.getEmail()))
			{
				h.put("status", -1);
				h.put("message", "L'email est incorrect.");
				return h;
			}
			if(! userService.isPhoneValid(userAdmin.getTelephone()))
			{
				h.put("status", -1);
				h.put("message", "Le numéro de téléphone mobile est incorrect.");
				return h;
			}
			if(userAdmin.getTelephoneFixe() != null  && !userService.isPhoneValid(userAdmin.getTelephoneFixe()))
			{
				h.put("status", -1);
				h.put("message", "Le numéro de téléphone fixe est incorrect.");
				return h;
			}
			//
			try
			{
				if(userService.isAdminGeneral(idAdmin))
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
				
				Groupe grp = groupeRepository.findByInstitionDefault(institution);
				if(grp == null )
				{
					h.put("message", "Le groupe admin n'existe pas.");
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
				if(userAdmin.getBoss() != null)
				{
					User bossUser = userRepository.findOne(userAdmin.getBoss().getIdUtilisateur());
					if(bossUser == null)
					{
						h.put("message", "L'utilisateur boss n'existe pas.");
						h.put("status", -1);
						return h;
					}
					
					if (! userRepository.hasSameInstitution(bossUser, grp))
					{
						h.put("message", "Le boss et l'utilisateur ne sont pas de la même institution.");
						h.put("status", -1);
						return h;
					}
					userAdmin.setBoss(bossUser);
				}
				//
				
				if(userAdminRepository.existsByLogin(userAdmin.getLogin()))
				{
					h.put("message", "Ce login existe déjà.");
					h.put("status", -1);
					return h;
				}
				
				if(userAdminRepository.existsByEmail(userAdmin.getEmail()))
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
	            
	            String emailTo = userAdmin.getEmail();
	            if(!EmailValidator.getInstance().isValid(emailTo))
	            {
	                h.put("status", -1);
	                h.put("message", "L'email destinataire est incorrect.");
	                return h;
	            }
	            
	            String tokenString = userLoginService.getPasswordString();
	            String loginUser = userAdmin.getLogin();
	            String emailObject = env.getProperty("email.object.add");
	            //
	            String cryptPassword = userLoginService.encryptData(tokenString);
	            System.out.println("password crypte "+cryptPassword );

	            userAdmin.setPassword(cryptPassword);
				userAdmin.setGroupeIdGroupe(grp);
				userAdmin.setProfilIdProfil(listProfile.get(0));
				//userAdmin.setProfilIdProfil(profil);
				userAdmin.setDateCreation(new Date());
				userAdmin = userEntrepriseRepository.save(userAdmin);
				
				//userLoginService.sendMail(emailFrom, emailTo, emailObject, "Vos identitifants eTreasury.\n login: "+ loginUser + " et password: "+ tokenString );
				 List<Value> values= new ArrayList<Value>();
	             values.add(new Value("","\n Identifiant : "+ loginUser + "\n et  Mot de passe: "+ tokenString));
	             userLoginService.sendMailNew(emailFrom, emailTo, "eTreasury  | Votre inscription est validée", "Bienvenue sur eTreasury, votre inscription est effective. Vous pouvez vous connecter avec les identifiants suivants :\n", userAdmin.getPrenom(), values,"Nous vous invitons à changer votre mot de passe dès votre première connexion.");
		        
			} catch (Exception e) {
				e.printStackTrace();
				h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
				h.put("status", -1);
				return h;
			}
			h.put("userEntreprise", userAdmin);
			h.put("message", "L'utilisateur est ajouté avec succès.");
			h.put("status", 0);
			
			userService.journaliser(idAdmin, 6, "Ajout Administrateur Entreprise");
			
			return h;
		}
		
		//Ajout d'un utilisateurs Entreprise avec le profiles user_hability par admin eTreasury
		@RequestMapping(value="/add/entreprise/user_hability/{idAdmin}/{idInstitution}", method=RequestMethod.POST)
		public HashMap<String, Object> addUserHabilityEntreprise(@RequestBody UserEntreprise userAdmin, @PathVariable Integer idAdmin, @PathVariable Integer idInstitution){
			HashMap<String, Object> h = new HashMap<String, Object>();
			
			//Fields Control
			if(userAdmin == null || idAdmin == null || idInstitution == null)
			{
				h.put("status", -1);
				h.put("message", "1 ou plusieurs paramètres manquants");
				return h;
			}
			if(userAdmin.getNom().equals("") || userAdmin.getPrenom().equals("") || userAdmin.getTelephone().equals("") ||
					userAdmin.getEmail().equals("") || userAdmin.getLogin().equals(""))
			{
				h.put("status", -1);
				h.put("message", "1 ou plusieurs paramètres vides");
				return h;
			}
			if(! userService.isEmailValid(userAdmin.getEmail()))
			{
				h.put("status", -1);
				h.put("message", "L'email est incorrect.");
				return h;
			}
			if(! userService.isPhoneValid(userAdmin.getTelephone()))
			{
				h.put("status", -1);
				h.put("message", "Le numéro de téléphone mobile est incorrect.");
				return h;
			}
			if(userAdmin.getTelephoneFixe() != null && !userService.isPhoneValid(userAdmin.getTelephoneFixe()))
			{
				h.put("status", -1);
				h.put("message", "Le numéro de téléphone fixe est incorrect.");
				return h;
			}
			//
			
			try
			{
				if(userService.isAdminGeneral(idAdmin))
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
				
				Groupe grp = groupeRepository.findByInstitionDefault(institution);
				if(grp == null )
				{
					h.put("message", "Le groupe administrateur n'existe pas.");
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
				if(userAdmin.getBoss() != null)
				{
					User bossUser = userRepository.findOne(userAdmin.getBoss().getIdUtilisateur());
					if(bossUser == null)
					{
						h.put("message", "L'utilisateur boss n'existe pas.");
						h.put("status", -1);
						return h;
					}
					
					if (! userRepository.hasSameInstitution(bossUser, grp))
					{
						h.put("message", "Le boss et l'utilisateur ne sont pas de la même institution.");
						h.put("status", -1);
						return h;
					}
					userAdmin.setBoss(bossUser);
				}
				//
				
				if(userAdminRepository.existsByLogin(userAdmin.getLogin()))
				{
					h.put("message", "Ce login existe déjà.");
					h.put("status", -1);
					return h;
				}
				
				if(userAdminRepository.existsByEmail(userAdmin.getEmail()))
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
	            
	            String emailTo = userAdmin.getEmail();
	            if(!EmailValidator.getInstance().isValid(emailTo))
	            {
	                h.put("status", -1);
	                h.put("message", "L'email destinataire est incorrect.");
	                return h;
	            }
	            
	            String tokenString = userLoginService.getPasswordString();
	            String loginUser = userAdmin.getLogin();
	            String emailObject = env.getProperty("email.object.add");
	            //
	            String cryptPassword = userLoginService.encryptData(tokenString);
	            System.out.println("password crypte "+cryptPassword );

	            userAdmin.setPassword(cryptPassword);
				userAdmin.setGroupeIdGroupe(grp);
				userAdmin.setProfilIdProfil(listProfile.get(0));
				userAdmin.setDateCreation(new Date());
				userAdmin = userEntrepriseRepository.save(userAdmin);
				
			//	userLoginService.sendMail(emailFrom, emailTo, emailObject, "Vos identitifants eTreasury.\n login: "+ loginUser + " et password: "+ tokenString );
				 List<Value> values= new ArrayList<Value>();
	             values.add(new Value("","\n Identifiant : "+ loginUser + "\n et  Mot de passe: "+ tokenString));
	             userLoginService.sendMailNew(emailFrom, emailTo, "eTreasury  | Votre inscription est validée", "Bienvenue sur eTreasury, votre inscription est effective. Vous pouvez vous connecter avec les identifiants suivants :\n", userAdmin.getPrenom(), values,"Nous vous invitons à changer votre mot de passe dès votre première connexion.");
		        
			} catch (Exception e) {
				e.printStackTrace();
				h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
				h.put("status", -1);
				return h;
			}
			h.put("userEntreprise", userAdmin);
			h.put("message", "L'utilisateur est ajouté avec succès.");
			h.put("status", 0);
			
			userService.journaliser(idAdmin, 6, "Ajout Administrateur Entreprise");
			
			return h;
		}	
				
		//Update user Etreasury
		@RequestMapping(value="/update/{idAdmin}", method=RequestMethod.PUT)
		public HashMap<String, Object> update(@RequestBody UserAdmin userAdmin, @PathVariable Integer idAdmin){
			
			HashMap<String, Object> h = new HashMap<String, Object>();
			
			//Fields Control
			if(userAdmin == null || idAdmin == null){
				h.put("message", "1 ou plusieurs paramètres manquants");
				h.put("status", -1);
				return h;
			}
			if(userAdmin.getIdUtilisateur() == null ){
				h.put("message", "L'identifiant de l'utilisateur n'est pas renseigné.");
				h.put("status", -1);
				return h;
			}
			//
			
			try 
			{
				if(userService.isAdminGeneral(idAdmin))
				{
					h.put("status", -1);
					h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
					return h;
				}
				
				UserAdmin userAdminOld = userAdminRepository.findOne(userAdmin.getIdUtilisateur());
				if(userAdminOld == null )
				{
					h.put("message", "Cet utilisateur n'existe pas.");
					h.put("status", -1);
					return h;
				}
				
				if(userAdmin.getGroupeIdGroupe() == null)
				{
					userAdmin.setGroupeIdGroupe(userAdminOld.getGroupeIdGroupe());
				}
				else
				{
					Integer idGroupe = userAdmin.getGroupeIdGroupe().getIdGroupe();
					Groupe groupe = groupeRepository.findOne(idGroupe);
					if(groupe == null){
						h.put("message", "Le groupe n'existe pas.");
						h.put("status", -1);
						return h;
					}
					userAdmin.setGroupeIdGroupe(groupe);
				}
				
				if(userAdmin.getProfilIdProfil() == null)
				{
					userAdmin.setProfilIdProfil(userAdminOld.getProfilIdProfil());
				}
				else
				{
					Integer idProfil = userAdmin.getProfilIdProfil().getIdProfil();
					Profile profil = profilRepository.findOne(idProfil);
					if(profil == null){
						h.put("message", "Le profil n'existe pas.");
						h.put("status", -1);
						return h;
					}
					userAdmin.setProfilIdProfil(profil);
				}
				if(userAdmin.getIsActive() == null)
					userAdmin.setIsActive(userAdminOld.getIsActive());
				
				//Email Control
				if(userAdmin.getEmail() == null)
				{
					userAdmin.setEmail(userAdminOld.getEmail());
				}
				else if(!userService.isEmailValid(userAdmin.getEmail()))
				{
					h.put("status", -1);
					h.put("message", "L'email est incorrect.");
					return h;
				}
				
				else if(userAdminRepository.existsByEmailOthers(userAdmin.getEmail(),userAdminOld.getIdUtilisateur()))
				{
					//on verifie que l'email n'existe pas en BDD
					
					h.put("message", "Cet email existe déjà.");
					h.put("status", -1);
					return h;
				}
				
				//
				
				if(userAdmin.getBoss() == null)
					userAdmin.setBoss(userAdminOld.getBoss());
				if(userAdmin.getDateCreation() == null)
					userAdmin.setDateCreation(userAdminOld.getDateCreation());
				
				// Boss
				if(userAdmin.getBoss() == null)
					userAdmin.setBoss(userAdminOld.getBoss());
				else
				{
					User bossUser = userRepository.findOne(userAdmin.getBoss().getIdUtilisateur());
					if(bossUser == null)
					{
						h.put("message", "L'utilisateur boss n'existe pas.");
						h.put("status", -1);
						return h;
					}
					
					if (! userRepository.hasSameInstitution(bossUser, userAdmin.getGroupeIdGroupe()))
					{
						h.put("message", "Le boss et l'utilisateur ne sont pas de la même institution.");
						h.put("status", -1);
						return h;
					}
					userAdmin.setBoss(bossUser);
				}
				//
				
				if(userAdmin.getLogin() == null)
					userAdmin.setLogin(userAdminOld.getLogin());
				else
				{
					if (!(userAdmin.getLogin().equals(userAdminOld.getLogin()))){
						
						//On verifie que le login n'existe pas en base de donnees
		                
		                if(userAdminRepository.existsByLoginOthers(userAdmin.getLogin(),userAdminOld.getIdUtilisateur()))
						{
							h.put("message", "Ce login existe déjà.");
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
		                
		                String emailTo = userAdmin.getEmail();
		                if(!EmailValidator.getInstance().isValid(emailTo))
		                {
		                    h.put("status", -1);
		                    h.put("message", "L'email destinataire est incorrect.");
		                    return h;
		                }
		                
		                //    String tokenString = userService.getPasswordString();
		                String loginUser = userAdmin.getLogin();
		                String emailObject = env.getProperty("email.object.add");
		             //   userLoginService.sendMail(emailFrom, emailTo, emailObject, "Votre login a été modifié par l'administrateur.\n Votre nouveau login est : "+ loginUser );
		                List<Value> values= new ArrayList<Value>();
		                values.add(new Value("", loginUser));
		                userLoginService.sendMailNew(emailFrom, emailTo, "eTreasury  | Changement de login", "Votre login a été modifié par l'administrateur. Vous pouvez vous connecter avec l'identifiant suivant:", userAdmin.getPrenom(), values,"Nous vous invitions de l'utiliser lors de votre prochaine connexion.");
		   	        
					}
				}
				
				if(userAdmin.getNom() == null)
					userAdmin.setNom(userAdminOld.getNom());
				if(userAdmin.getPassword() == null)
					userAdmin.setPassword(userAdminOld.getPassword());
				if(userAdmin.getPhoto() == null)
					userAdmin.setPhoto(userAdminOld.getPhoto());
				if(userAdmin.getPrenom() == null)
					userAdmin.setPrenom(userAdminOld.getPrenom());
				
				//Phone number Control
				if(userAdmin.getTelephone() == null)
				{
					userAdmin.setTelephone(userAdminOld.getTelephone());
				}
				else if(!userService.isPhoneValid(userAdmin.getTelephone()))
				{
					h.put("status", -1);
					h.put("message", "Le numéro de téléphone mobile est incorrect.");
					return h;
				}
				
				if(userAdmin.getTelephoneFixe() == null)
				{
					userAdmin.setTelephoneFixe(userAdminOld.getTelephoneFixe());
				}
				else if(!userService.isPhoneValid(userAdmin.getTelephoneFixe()))
				{
					h.put("status", -1);
					h.put("message", "Le numéro de téléphone fixe est incorrect.");
					return h;
				}
				//
				
				userAdmin.setIdUtilisateur(userAdmin.getIdUtilisateur());
				userAdmin = userAdminRepository.saveAndFlush(userAdmin);
			} catch (Exception e) {
				e.printStackTrace();
				h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
				h.put("status", -1);
				return h;
			}
			h.put("userAdmin", userAdmin);
			h.put("message", "La modification de l'utilisateur s'est effectuée avec succès.");
			h.put("status", 0);
			
			userService.journaliser(idAdmin, 7, "Modification User Etreasury");
			
			return h;
		}
		
		//Update user bank dans Admin eTreasury
		@RequestMapping(value="/update/bank/{idAdmin}", method=RequestMethod.PUT)
		public HashMap<String, Object> updateBank(@RequestBody UserBanque userAdmin, @PathVariable Integer idAdmin){
			
			HashMap<String, Object> h = new HashMap<String, Object>();
			
			//Fields Control
			if(userAdmin == null || idAdmin == null){
				h.put("message", "1 ou plusieurs paramètres manquants");
				h.put("status", -1);
				return h;
			}
			if(userAdmin.getIdUtilisateur() == null ){
				h.put("message", "L'identifiant de l'utilisateur n'est pas renseigné.");
				h.put("status", -1);
				return h;
			}
			//
			
			try 
			{
				if(userService.isAdminGeneral(idAdmin))
				{
					h.put("status", -1);
					h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
					return h;
				}
				
				UserBanque userAdminOld = userBankRepository.findOne(userAdmin.getIdUtilisateur());
				if(userAdminOld == null )
				{
					h.put("message", "Cet utilisateur n'existe pas.");
					h.put("status", -1);
					return h;
				}
				
				if(userAdmin.getGroupeIdGroupe() == null)
				{
					userAdmin.setGroupeIdGroupe(userAdminOld.getGroupeIdGroupe());
				}
				else
				{
					Integer idGroupe = userAdmin.getGroupeIdGroupe().getIdGroupe();
					Groupe groupe = groupeRepository.findOne(idGroupe);
					if(groupe == null){
						h.put("message", "Le groupe n'existe pas.");
						h.put("status", -1);
						return h;
					}
					userAdmin.setGroupeIdGroupe(groupe);
				}
				
				if(userAdmin.getProfilIdProfil() == null)
				{
					userAdmin.setProfilIdProfil(userAdminOld.getProfilIdProfil());
				}
				else
				{
					Integer idProfil = userAdmin.getProfilIdProfil().getIdProfil();
					Profile profil = profilRepository.findOne(idProfil);
					if(profil == null){
						h.put("message", "Le profil n'existe pas.");
						h.put("status", -1);
						return h;
					}
					userAdmin.setProfilIdProfil(profil);
				}
			
				if(userAdmin.getIsActive() == null)
					userAdmin.setIsActive(userAdminOld.getIsActive());
				
				//Email Control
				if(userAdmin.getEmail() == null)
				{
					userAdmin.setEmail(userAdminOld.getEmail());
				}
				else if(!userService.isEmailValid(userAdmin.getEmail()))
				{
					h.put("status", -1);
					h.put("message", "L'email est incorrect.");
					return h;
				}
				
				//Verfiier 	que l'email n'existe
				else if(userBankRepository.existsByEmailOthers(userAdmin.getEmail(),userAdminOld.getIdUtilisateur()))
				{
					//on verifie que l'email n'existe pas en BDD
					
					h.put("message", "Cet email existe déjà.");
					h.put("status", -1);
					return h;
				}
				
				if(userAdmin.getBoss() == null)
					userAdmin.setBoss(userAdminOld.getBoss());
				if(userAdmin.getDateCreation() == null)
					userAdmin.setDateCreation(userAdminOld.getDateCreation());
				
				// Boss
				if(userAdmin.getBoss() == null)
					userAdmin.setBoss(userAdminOld.getBoss());
				else
				{
					User bossUser = userRepository.findOne(userAdmin.getBoss().getIdUtilisateur());
					if(bossUser == null)
					{
						h.put("message", "L'utilisateur boss n'existe pas.");
						h.put("status", -1);
						return h;
					}
					
					if (! userRepository.hasSameInstitution(bossUser, userAdmin.getGroupeIdGroupe()))
					{
						h.put("message", "Le boss et l'utilisateur ne sont pas de la même institution.");
						h.put("status", -1);
						return h;
					}
					userAdmin.setBoss(bossUser);
				}
				//
				
				if(userAdmin.getLogin() == null)
					userAdmin.setLogin(userAdminOld.getLogin());
				else
				{
					if (!(userAdmin.getLogin().equals(userAdminOld.getLogin()))){
		                
						
						if(userBankRepository.existsByLoginOthers(userAdmin.getLogin(),userAdminOld.getIdUtilisateur()))
						{
							h.put("message", "Ce login existe déjà.");
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
		                
		                String emailTo = userAdmin.getEmail();
		                if(!EmailValidator.getInstance().isValid(emailTo))
		                {
		                    h.put("status", -1);
		                    h.put("message", "L'email destinataire est incorrect.");
		                    return h;
		                }
		                
		                //    String tokenString = userService.getPasswordString();
		                String loginUser = userAdmin.getLogin();
		                String emailObject = env.getProperty("email.object.add");
		             //   userLoginService.sendMail(emailFrom, emailTo, emailObject, "Votre login a été modifié par l'administrateur.\n Votre nouveau login est : "+ loginUser );
		                List<Value> values= new ArrayList<Value>();
		                values.add(new Value("", loginUser));
		                userLoginService.sendMailNew(emailFrom, emailTo, "eTreasury  | Changement de login", "Votre login a été modifié par l'administrateur. Vous pouvez vous connecter avec l'identifiant suivant:", userAdmin.getPrenom(), values,"Nous vous invitions à utiliser lors de votre prochaine connexion.");
		   	           
					}
				}
				
				if(userAdmin.getNom() == null)
					userAdmin.setNom(userAdminOld.getNom());
				System.out.println("userAdminOld.getPassword() "+userAdminOld.getPassword());
				if (userAdminOld.getPassword() == null)
				{
					//Envoie du mail pour user admin entreprise
	                String emailFrom = env.getProperty("spring.mail.username");
	                if(!EmailValidator.getInstance().isValid(emailFrom))
	                {
	                    h.put("status", -1);
	                    h.put("message", "L'email d'envoi est incorrect.");
	                    return h;
	                }
	                
	                String emailTo = userAdmin.getEmail();
	                if(!EmailValidator.getInstance().isValid(emailTo))
	                {
	                    h.put("status", -1);
	                    h.put("message", "L'email destinataire est incorrect.");
	                    return h;
	                }
	                String tokenString = userLoginService.getPasswordString();
		            String cryptPassword = userLoginService.encryptData(tokenString);
		            System.out.println("password crypte "+cryptPassword );
	               
	                String emailObject = env.getProperty("email.object.add");
	                String loginUser = userAdmin.getLogin();
	                //userLoginService.sendMail(emailFrom, emailTo, emailObject, "Votre nouveau mot de passe pour accéder  à eTreasury est : "+tokenString);
	                /*
	                List<Value> values= new ArrayList<Value>();
	                values.add(new Value("", tokenString));
	                userLoginService.sendMailNew(emailFrom, emailTo, "eTreasury  | Changement de mot de passe", "Votre mot de passe a été modifié par l'administrateur. Vous pouvez vous connecter avec le mot de passe suivant:", userAdmin.getPrenom(), values,"Nous vous invitions à utiliser lors de votre prochaine connexion.");
	   	        */
	                List<Value> values= new ArrayList<Value>();
		             values.add(new Value("","\n Identifiant : "+ loginUser + "\n et  Mot de passe: "+ tokenString));
		             userLoginService.sendMailNew(emailFrom, emailTo, "eTreasury  | Votre inscription est validée", "Bienvenue sur eTreasury, votre inscription est effective. Vous pouvez vous connecter avec les identifiants suivants :\n", userAdmin.getPrenom(), values,"Nous vous invitons à changer votre mot de passe dès votre première connexion.");
			        
	                userAdmin.setPassword(cryptPassword);
		               
				}
				else if(userAdmin.getPassword() == null) {
					userAdmin.setPassword(userAdminOld.getPassword());
				}
				if(userAdmin.getPhoto() == null)
					userAdmin.setPhoto(userAdminOld.getPhoto());
				if(userAdmin.getPrenom() == null)
					userAdmin.setPrenom(userAdminOld.getPrenom());
				
				//Phone number Control
				if(userAdmin.getTelephone() == null)
				{
					userAdmin.setTelephone(userAdminOld.getTelephone());
				}
				else if(!userService.isPhoneValid(userAdmin.getTelephone()))
				{
					h.put("status", -1);
					h.put("message", "Le numéro de téléphone mobile est incorrect.");
					return h;
				}
				
				if(userAdmin.getTelephoneFixe() == null)
				{
					userAdmin.setTelephoneFixe(userAdminOld.getTelephoneFixe());
				}
				else if(!userService.isPhoneValid(userAdmin.getTelephoneFixe()))
				{
					h.put("status", -1);
					h.put("message", "Le numéro de téléphone fixe est incorrect.");
					return h;
				}
				//
				
				userAdmin.setIdUtilisateur(userAdmin.getIdUtilisateur());
				userAdmin = userBankRepository.saveAndFlush(userAdmin);
			} catch (Exception e) {
				e.printStackTrace();
				h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
				h.put("status", -1);
				return h;
			}
			h.put("userAdmin", userAdmin);
			h.put("message", "La modification de l'utilisateur s'est effectuée avec succès.");
			h.put("status", 0);
			
			userService.journaliser(idAdmin, 7, "Modification User Banque");
			
			return h;
		}
		
		//Update user admin bank dans Admin eTreasury
				@RequestMapping(value="/update/bank/admin_bank/{idAdmin}", method=RequestMethod.PUT)
				public HashMap<String, Object> updateAdminBank(@RequestBody UserBanque userAdmin, @PathVariable Integer idAdmin){
					
					HashMap<String, Object> h = new HashMap<String, Object>();
					
					//Fields Control
					if(userAdmin == null || idAdmin == null){
						h.put("message", "1 ou plusieurs paramètres manquants");
						h.put("status", -1);
						return h;
					}
					if(userAdmin.getIdUtilisateur() == null ){
						h.put("message", "L'identifiant de l'utilisateur n'est pas renseigné.");
						h.put("status", -1);
						return h;
					}
					//
					
					try 
					{
						if(userService.isAdminGeneral(idAdmin))
						{
							h.put("status", -1);
							h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
							return h;
						}
						
						UserBanque userAdminOld = userBankRepository.findOne(userAdmin.getIdUtilisateur());
						if(userAdminOld == null )
						{
							h.put("message", "Cet utilisateur n'existe pas.");
							h.put("status", -1);
							return h;
						}
						
						if(userAdmin.getGroupeIdGroupe() == null)
						{
							userAdmin.setGroupeIdGroupe(userAdminOld.getGroupeIdGroupe());
						}
						else
						{
							Integer idGroupe = userAdmin.getGroupeIdGroupe().getIdGroupe();
							Groupe groupe = groupeRepository.findOne(idGroupe);
							if(groupe == null){
								h.put("message", "Le groupe n'existe pas.");
								h.put("status", -1);
								return h;
							}
							userAdmin.setGroupeIdGroupe(groupe);
						}
						
						if(userAdmin.getProfilIdProfil() == null)
						{
							userAdmin.setProfilIdProfil(userAdminOld.getProfilIdProfil());
						}
						else
						{
							Integer idProfil = userAdmin.getProfilIdProfil().getIdProfil();
							Profile profil = profilRepository.findOne(idProfil);
							if(profil == null){
								h.put("message", "Le profil n'existe pas.");
								h.put("status", -1);
								return h;
							}
							userAdmin.setProfilIdProfil(profil);
						}
					
						if(userAdmin.getIsActive() == null)
							userAdmin.setIsActive(userAdminOld.getIsActive());
						
						//Email Control
						if(userAdmin.getEmail() == null)
						{
							userAdmin.setEmail(userAdminOld.getEmail());
						}
						
						//Verfiier 	que l'email n'existe
						else if(userBankRepository.existsByEmailOthers(userAdmin.getEmail(),userAdminOld.getIdUtilisateur()))
						{
							//on verifie que l'email n'existe pas en BDD
							
							h.put("message", "Cet email existe déjà.");
							h.put("status", -1);
							return h;
						}
						
						else if(!userService.isEmailValid(userAdmin.getEmail()))
						{
							h.put("status", -1);
							h.put("message", "L'email est incorrect.");
							return h;
						}
						//
						
						if(userAdmin.getBoss() == null)
							userAdmin.setBoss(userAdminOld.getBoss());
						if(userAdmin.getDateCreation() == null)
							userAdmin.setDateCreation(userAdminOld.getDateCreation());
						
						// Boss
						if(userAdmin.getBoss() == null)
							userAdmin.setBoss(userAdminOld.getBoss());
						else
						{
							User bossUser = userRepository.findOne(userAdmin.getBoss().getIdUtilisateur());
							if(bossUser == null)
							{
								h.put("message", "L'utilisateur boss n'existe pas.");
								h.put("status", -1);
								return h;
							}
							
							if (! userRepository.hasSameInstitution(bossUser, userAdmin.getGroupeIdGroupe()))
							{
								h.put("message", "Le boss et l'utilisateur ne sont pas de la même institution.");
								h.put("status", -1);
								return h;
							}
							userAdmin.setBoss(bossUser);
						}
						//
						
						if(userAdmin.getLogin() == null)
							userAdmin.setLogin(userAdminOld.getLogin());
						else
						{
							if (!(userAdmin.getLogin().equals(userAdminOld.getLogin()))){
								
								if(userBankRepository.existsByLoginOthers(userAdmin.getLogin(),userAdminOld.getIdUtilisateur()))
								{
									h.put("message", "Ce login existe déjà.");
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
				                
				                String emailTo = userAdmin.getEmail();
				                if(!EmailValidator.getInstance().isValid(emailTo))
				                {
				                    h.put("status", -1);
				                    h.put("message", "L'email destinataire est incorrect.");
				                    return h;
				                }
				                
				                //    String tokenString = userService.getPasswordString();
				                String loginUser = userAdmin.getLogin();
				                String emailObject = env.getProperty("email.object.add");
				             //   userLoginService.sendMail(emailFrom, emailTo, emailObject, "Votre login a été modifié par l'administrateur.\n Votre nouveau login est : "+ loginUser );
				                List<Value> values= new ArrayList<Value>();
				                values.add(new Value("", loginUser));
				                userLoginService.sendMailNew(emailFrom, emailTo, "eTreasury  | Changement de login", "Votre login a été modifié par l'administrateur. Vous pouvez vous connecter avec l'identifiant suivant:", userAdmin.getPrenom(), values,"Nous vous invitions à utiliser lors de votre prochaine connexion.");
				   	           
							}
						}
						
						if(userAdmin.getNom() == null)
							userAdmin.setNom(userAdminOld.getNom());
						/*
						if(userAdmin.getPassword() == null) {
							userAdmin.setPassword(userAdminOld.getPassword());
						}
						*/
					
						if(userAdmin.getPhoto() == null)
							userAdmin.setPhoto(userAdminOld.getPhoto());
						if(userAdmin.getPrenom() == null)
							userAdmin.setPrenom(userAdminOld.getPrenom());
						
						//Phone number Control
						if(userAdmin.getTelephone() == null)
						{
							userAdmin.setTelephone(userAdminOld.getTelephone());
						}
						else if(!userService.isPhoneValid(userAdmin.getTelephone()))
						{
							h.put("status", -1);
							h.put("message", "Le numéro de téléphone mobile est incorrect.");
							return h;
						}
						
						if(userAdmin.getTelephoneFixe() == null)
						{
							userAdmin.setTelephoneFixe(userAdminOld.getTelephoneFixe());
						}
						else if(!userService.isPhoneValid(userAdmin.getTelephoneFixe()))
						{
							h.put("status", -1);
							h.put("message", "Le numéro de téléphone fixe est incorrect.");
							return h;
						}
						//
						
						userAdmin.setIdUtilisateur(userAdmin.getIdUtilisateur());
						userAdmin = userBankRepository.saveAndFlush(userAdmin);
					} catch (Exception e) {
						e.printStackTrace();
						h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
						h.put("status", -1);
						return h;
					}
					h.put("userAdmin", userAdmin);
					h.put("message", "La modification s'est effectuée avec succès.");
					h.put("status", 0);
					
					userService.journaliser(idAdmin, 7, "Modification User Banque");
					
					return h;
				}
			
			//Update user Entreprise dans Etreasury
					@RequestMapping(value="/update/entreprise/{idAdmin}", method=RequestMethod.PUT)
					public HashMap<String, Object> updateEntreprise(@RequestBody UserEntreprise userAdmin, @PathVariable Integer idAdmin){
						
						HashMap<String, Object> h = new HashMap<String, Object>();
						
						//Fields Control
						if(userAdmin == null || idAdmin == null){
							h.put("message", "1 ou plusieurs paramètres manquants");
							h.put("status", -1);
							return h;
						}
						if(userAdmin.getIdUtilisateur() == null ){
							h.put("message", "L'identifiant de l'utilisateur n'est pas renseigné.");
							h.put("status", -1);
							return h;
						}
						//
						
						try 
						{
							if(userService.isAdminGeneral(idAdmin))
							{
								h.put("status", -1);
								h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
								return h;
							}
							
							UserEntreprise userAdminOld = userEntrepriseRepository.findOne(userAdmin.getIdUtilisateur());
							if(userAdminOld == null )
							{
								h.put("message", "Cet utilisateur n'existe pas.");
								h.put("status", -1);
								return h;
							}
							
							if(userAdmin.getGroupeIdGroupe() == null)
							{
								userAdmin.setGroupeIdGroupe(userAdminOld.getGroupeIdGroupe());
							}
							else
							{
								Integer idGroupe = userAdmin.getGroupeIdGroupe().getIdGroupe();
								Groupe groupe = groupeRepository.findOne(idGroupe);
								if(groupe == null){
									h.put("message", "Le groupe n'existe pas.");
									h.put("status", -1);
									return h;
								}
								userAdmin.setGroupeIdGroupe(groupe);
							}
							
							if(userAdmin.getProfilIdProfil() == null)
							{
								userAdmin.setProfilIdProfil(userAdminOld.getProfilIdProfil());
							}
							else
							{
								Integer idProfil = userAdmin.getProfilIdProfil().getIdProfil();
								Profile profil = profilRepository.findOne(idProfil);
								if(profil == null){
									h.put("message", "Le profil n'existe pas.");
									h.put("status", -1);
									return h;
								}
								userAdmin.setProfilIdProfil(profil);
							}
						
							if(userAdmin.getIsActive() == null)
								userAdmin.setIsActive(userAdminOld.getIsActive());
							
							//Email Control
							if(userAdmin.getEmail() == null)
							{
								userAdmin.setEmail(userAdminOld.getEmail());
							}
							
							//Verfiier 	que l'email n'existe
							else if(userEntrepriseRepository.existsByEmailOthers(userAdmin.getEmail(),userAdminOld.getIdUtilisateur()))
							{
								//on verifie que l'email n'existe pas en BDD
								
								h.put("message", "Cet email existe déjà.");
								h.put("status", -1);
								return h;
							}
							
							else if(!userService.isEmailValid(userAdmin.getEmail()))
							{
								h.put("status", -1);
								h.put("message", "L'email est incorrect.");
								return h;
							}
							//
							
							if(userAdmin.getBoss() == null)
								userAdmin.setBoss(userAdminOld.getBoss());
							if(userAdmin.getDateCreation() == null)
								userAdmin.setDateCreation(userAdminOld.getDateCreation());
							
							// Boss
							if(userAdmin.getBoss() == null)
								userAdmin.setBoss(userAdminOld.getBoss());
							else
							{
								User bossUser = userRepository.findOne(userAdmin.getBoss().getIdUtilisateur());
								if(bossUser == null)
								{
									h.put("message", "L'utilisateur boss n'existe pas.");
									h.put("status", -1);
									return h;
								}
								
								if (! userRepository.hasSameInstitution(bossUser, userAdmin.getGroupeIdGroupe()))
								{
									h.put("message", "Le boss et l'utilisateur ne sont pas de la même institution.");
									h.put("status", -1);
									return h;
								}
								userAdmin.setBoss(bossUser);
							}
							//
							
							if(userAdmin.getLogin() == null)
								userAdmin.setLogin(userAdminOld.getLogin());
							else
							{
								if (!(userAdmin.getLogin().equals(userAdminOld.getLogin()))){
									
									if(userBankRepository.existsByLoginOthers(userAdmin.getLogin(),userAdminOld.getIdUtilisateur()))
									{
										h.put("message", "Ce login existe déjà.");
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
					                
					                String emailTo = userAdmin.getEmail();
					                if(!EmailValidator.getInstance().isValid(emailTo))
					                {
					                    h.put("status", -1);
					                    h.put("message", "L'email destinataire est incorrect.");
					                    return h;
					                }
					                
					                //    String tokenString = userService.getPasswordString();
					                String loginUser = userAdmin.getLogin();
					                String emailObject = env.getProperty("email.object.add");
					                //userLoginService.sendMail(emailFrom, emailTo, emailObject, "Votre login a été modifié par l'administrateur.\n Votre nouveau login est : "+ loginUser );
					                List<Value> values= new ArrayList<Value>();
					                values.add(new Value("", loginUser));
					                userLoginService.sendMailNew(emailFrom, emailTo, "eTreasury  | Changement de login", "Votre login a été modifié par l'administrateur. Vous pouvez vous connecter avec l'identifiant suivant:", userAdmin.getPrenom(), values,"Nous vous invitions à utiliser lors de votre prochaine connexion.");
					   	            
								}
							}
							
							if(userAdmin.getNom() == null)
								userAdmin.setNom(userAdminOld.getNom());
							System.out.println("userAdminOld.getPassword() "+userAdminOld.getPassword());
							if (userAdminOld.getPassword() == null)
							{
								//Envoie du mail pour user admin entreprise
				                String emailFrom = env.getProperty("spring.mail.username");
				                if(!EmailValidator.getInstance().isValid(emailFrom))
				                {
				                    h.put("status", -1);
				                    h.put("message", "L'email d'envoi est incorrect.");
				                    return h;
				                }
				                
				                String emailTo = userAdmin.getEmail();
				                if(!EmailValidator.getInstance().isValid(emailTo))
				                {
				                    h.put("status", -1);
				                    h.put("message", "L'email destinataire est incorrect.");
				                    return h;
				                }
				                String tokenString = userLoginService.getPasswordString();
					            String cryptPassword = userLoginService.encryptData(tokenString);
					            System.out.println("password crypte "+cryptPassword );
				               String loginUser = userAdmin.getLogin();
				                String emailObject = env.getProperty("email.object.add");
				              //  userLoginService.sendMail(emailFrom, emailTo, emailObject, "Votre nouveau mot de passe pour accédé  à eTreasury est : "+tokenString);
				              /*
				                List<Value> values= new ArrayList<Value>();
				                values.add(new Value("", tokenString));
				                userLoginService.sendMailNew(emailFrom, emailTo, "eTreasury  | Changement de mot de passe", "Votre mot de passe a été modifié par l'administrateur. Vous pouvez vous connecter avec le mot de passe suivant:", userAdmin.getPrenom(), values,"Nous vous invitions à utiliser lors de votre prochaine connexion.");
				   	        */
				                List<Value> values= new ArrayList<Value>();
					             values.add(new Value("","\n Identifiant : "+ loginUser + "\n et  Mot de passe: "+ tokenString));
					             userLoginService.sendMailNew(emailFrom, emailTo, "eTreasury  | Votre inscription est validée", "Bienvenue sur eTreasury, votre inscription est effective. Vous pouvez vous connecter avec les identifiants suivants :\n", userAdmin.getPrenom(), values,"Nous vous invitons à changer votre mot de passe dès votre première connexion.");
						       
				                userAdmin.setPassword(cryptPassword);
					               
							}
							else if(userAdmin.getPassword() == null) {
								userAdmin.setPassword(userAdminOld.getPassword());
							}
							if(userAdmin.getPhoto() == null)
								userAdmin.setPhoto(userAdminOld.getPhoto());
							if(userAdmin.getPrenom() == null)
								userAdmin.setPrenom(userAdminOld.getPrenom());
							
							//Phone number Control
							if(userAdmin.getTelephone() == null)
							{
								userAdmin.setTelephone(userAdminOld.getTelephone());
							}
							else if(!userService.isPhoneValid(userAdmin.getTelephone()))
							{
								h.put("status", -1);
								h.put("message", "Le numéro de téléphone mobile est incorrect.");
								return h;
							}
							
							if(userAdmin.getTelephoneFixe() == null)
							{
								userAdmin.setTelephoneFixe(userAdminOld.getTelephoneFixe());
							}
							else if(!userService.isPhoneValid(userAdmin.getTelephoneFixe()))
							{
								h.put("status", -1);
								h.put("message", "Le numéro de téléphone fixe est incorrect.");
								return h;
							}
							//
							
							userAdmin = userEntrepriseRepository.saveAndFlush(userAdmin);
						} catch (Exception e) {
							e.printStackTrace();
							h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
							h.put("status", -1);
							return h;
						}
						h.put("userAdmin", userAdmin);
						h.put("message", "La modification s'est bien effectuée avec succès.");
						h.put("status", 0);
						
						userService.journaliser(idAdmin, 7, "Modification User Entreprise");
						
						return h;
					}
		
					//informations d'un user de eTreasury
	
	@RequestMapping(value="/add/{idAdmin}/{id}", method=RequestMethod.POST)
	public HashMap<String, Object> add(@RequestBody UserAdmin userAdmin, @PathVariable Integer idAdmin, @PathVariable Integer id){
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		//Fields Control
		if(userAdmin == null || idAdmin == null || id == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		if(userAdmin.getNom().equals("") || userAdmin.getPrenom().equals("") || userAdmin.getTelephone().equals("") ||
				userAdmin.getEmail().equals("") || userAdmin.getLogin().equals(""))
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres vides");
			return h;
		}
		if(! userService.isEmailValid(userAdmin.getEmail()))
		{
			h.put("status", -1);
			h.put("message", "L'email est incorrect.");
			return h;
		}
		if(! userService.isPhoneValid(userAdmin.getTelephone()))
		{
			h.put("status", -1);
			h.put("message", "Le numéro de téléphone mobile est incorrect.");
			return h;
		}
		if(userAdmin.getTelephoneFixe() != null && !userService.isPhoneValid(userAdmin.getTelephoneFixe()))
		{
			h.put("status", -1);
			h.put("message", "Le numéro de téléphone fixe est incorrect.");
			return h;
		}
		//
				
		try
		{
			if(userService.isAdminGeneral(idAdmin))
			{
				h.put("status", -1);
				h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
				return h;
			}
			
			Institution institution = institutionRepository.findOne(id);
			if(institution == null)
			{
				h.put("message", "L'institution n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			Groupe grp = groupeRepository.findByInstitionDefault(institution);
			if(grp == null )
			{
				h.put("message", "Le groupe administrateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			Integer idProfil = userAdmin.getProfilIdProfil().getIdProfil();
			Profile profil = profilRepository.findOne(idProfil);
			if(profil == null){
				h.put("message", "Le profil n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			// Boss
			if(userAdmin.getBoss() != null)
			{
				User bossUser = userRepository.findOne(userAdmin.getBoss().getIdUtilisateur());
				if(bossUser == null)
				{
					h.put("message", "L'utilisateur boss n'existe pas.");
					h.put("status", -1);
					return h;
				}
				
				if (! userRepository.hasSameInstitution(bossUser, grp))
				{
					h.put("message", "Le boss et l'utilisateur ne sont pas de la même institution.");
					h.put("status", -1);
					return h;
				}
				userAdmin.setBoss(bossUser);
			}
			//
			
			//Envoie du mail
            String emailFrom = env.getProperty("spring.mail.username");
            if(!EmailValidator.getInstance().isValid(emailFrom))
            {
                h.put("status", -1);
                h.put("message", "L'email d'envoi est incorrect.");
                return h;
            }
            
            String emailTo = userAdmin.getEmail();
            if(!EmailValidator.getInstance().isValid(emailTo))
            {
                h.put("status", -1);
                h.put("message", "L'email destinataire est incorrect.");
                return h;
            }
            
            String tokenString = userLoginService.getPasswordString();
            String loginUser = userAdmin.getLogin();
            String emailObject = env.getProperty("email.object.add");
            //
            
            userAdmin.setPassword(tokenString);
			userAdmin.setGroupeIdGroupe(grp);
			userAdmin.setProfilIdProfil(profil);
			userAdmin = userAdminRepository.save(userAdmin);
			
			//userLoginService.sendMail(emailFrom, emailTo, emailObject, "Vos identitifants eTreasury.\n login: "+ loginUser + " et password: "+ tokenString );
			  List<Value> values= new ArrayList<Value>();
              values.add(new Value("","\n Identifiant : "+ loginUser + "\n et  Mot de passe: "+ tokenString));
              userLoginService.sendMailNew(emailFrom, emailTo, "eTreasury  | Votre inscription est validée", "Bienvenue sur eTreasury, votre inscription est effective. Vous pouvez vous connecter avec les identifiants suivants :\n", userAdmin.getPrenom(), values,"Nous vous invitons à changer votre mot de passe dès votre première connexion.");
	        
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("userBank", userAdmin);
		h.put("message", "L'utilisateur est ajouté avec succès.");
		h.put("status", 0);
		
		userService.journaliser(idAdmin, 6, "Ajout");
		
		return h;
	}
	
	@RequestMapping(value="/list/{idAdmin}", method=RequestMethod.GET)
	public Map<String,Object> getUserBank(@PathVariable Integer idAdmin){
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		//Fields Control
		if(idAdmin == null){
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
			return h;
		}
		//
		
		UserAdmin userAdmin = userAdminRepository.findOne(idAdmin);
		if (userAdmin == null) {
			h.put("message", "L'utilisateur n'existe pas.");
			h.put("status", -1);
			return h;
		}
		
		h.put("message", "success");
		h.put("status", 0);
		h.put("bank", userAdmin);
		return h;
	}
	
	@RequestMapping(value="/delete/{idAdmin}/{id}", method=RequestMethod.DELETE)
	public Map<String,Object> deleteUser(@PathVariable Integer id, @PathVariable Integer idAdmin){
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		//Fields Control
		if(idAdmin == null || id == null){
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
			return h;
		}
		//
		
		try 
		{
			if(userService.isAdminGeneral(idAdmin))
			{
				h.put("status", -1);
				h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
				return h;
			}
			
			User user = userRepository.findOne(id);
			if (user == null)
			{
				h.put("message", "Cet utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			userRepository.delete(user);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "L'utilisateur est supprimé avec succès.");
		h.put("status", 0);
		
		userService.journaliser(idAdmin, 8, "Suppression user");
		
		return h;
	}
	
	@RequestMapping(value="/groupe/delete/{idAdmin}/{id}", method=RequestMethod.DELETE)
	public Map<String,Object> deleteFromGroup(@PathVariable Integer id, @PathVariable Integer idAdmin){
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		//Fields Control
		if(idAdmin == null || id == null){
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
			return h;
		}
		//
		
		try
		{
			if(userService.isAdminGeneral(idAdmin))
			{
				h.put("status", -1);
				h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
				return h;
			}
			
			User user = userRepository.findOne(id);
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
				h.put("message", "Cet institution n'a pas de goupe par défaut.");
				h.put("status", -1);
				return h;
			}
			userRepository.updateGroup(defaultGroupe, id);
		}catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "L'utilisateur entreprise a été bien supprimé de ce groupe.");
		h.put("status", 0);
		
		userService.journaliser(idAdmin, 8, "Suppression user dans groupe");
		
		return h;
	}
	
}