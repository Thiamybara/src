package com.qualshore.etreasury.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.qualshore.etreasury.dao.GroupRepository;
import com.qualshore.etreasury.dao.ProfileRepository;
import com.qualshore.etreasury.dao.UserBaseRepository;
import com.qualshore.etreasury.dao.UserRepository;
import com.qualshore.etreasury.entity.Groupe;
import com.qualshore.etreasury.entity.Profile;
import com.qualshore.etreasury.entity.User;
import com.qualshore.etreasury.entity.UserLogin;
import com.qualshore.etreasury.entity.UserUpdatePassword;
import com.qualshore.etreasury.mail.Value;
import com.qualshore.etreasury.service.UserLoginService;
import com.qualshore.etreasury.service.UserService;

@RestController
public class UserRestController {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ProfileRepository profileRepository;
	
	@Autowired
	GroupRepository groupRepository;
	
	@Autowired
	Environment env;
	    
    @Autowired
    UserLoginService userService;
	
	@Autowired
	UserService usService;
    
    @Autowired
    UserBaseRepository<User> userBaseRepository;
    @Autowired
	UserLoginService userLoginService;
	
	
	@RequestMapping(value = "/etreasury_project/admin/user/add/admin", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> addUser(@RequestParam("id_user_admin") Integer userAdmin,
									  @RequestParam("email") String email,
									  @RequestParam("login") String login,
									  @RequestParam("password") String password,
									  @RequestParam("is_remove") boolean isRemove,
									  @RequestParam("id_group") Integer idGroup,
									  @RequestParam("id_profile") Integer idProfile,
									  @RequestParam("nom") String nom,
									  @RequestParam("prenom") String prenom,
									  @RequestParam("telephone") String telephone,
									  @RequestParam("telephone_fixe") String telephoneFixe,
									  @RequestParam("photo") String photo,
									  @RequestParam("boss") Integer boss) throws ParseException {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(userAdmin == null || email.equals("") || login.equals("") || password.equals("") || idGroup == null ||
				idProfile == null || nom.equals("") || prenom.equals("") || telephone.equals(""))
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs parametres manquants");
		}
		else if(!groupRepository.existsByIdGroup(idGroup))
		{
			h.put("status", -1);
			h.put("message", "Le groupe n'existe pas.");
		}
		else if(!profileRepository.existsByIdProfile(idProfile))
		{
			h.put("status", -1);
			h.put("message", "Le profil n'existe pas.");
		}
		else if(userRepository.findOne(boss) == null)
		{
			h.put("status", -1);
			h.put("message", "Le boss de l'utilisateur n'existe pas.");
		}
		else
		{
			Groupe groupe = groupRepository.findByIdGroupe(idGroup).get(0);
			Profile profile = profileRepository.findByIdProfil(idProfile).get(0);
			User user = new User(email, login, password, new Date(), isRemove, nom, prenom, telephone, photo, telephoneFixe, userRepository.findOne(boss), groupe, profile);
			//User user = new User(email, login, password, getDateString(dateNaissance), isRemove, nom, prenom, telephone, photo, telephoneFixe, groupe, profile);
			//UserAdmin user = new User(email, login, password, getDateString(dateNaissance), isRemove, nom, prenom, telephone, photo, telephone_fixe, groupe, profile);
			User result = userRepository.save(user);
			if(result == null)
			{
				h.put("status", -1);
				h.put("message", "Erreur lors de l'ajout.");
			}
			else
			{
				h.put("status", 0);
				h.put("message", "L'utilisateur est ajouté avec succès.");
			}
		}
		
		return h;
	}
	
	@RequestMapping(value = "/etreasury_project/admin/user/update", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> updateUser(@RequestParam("id_user_admin") Integer userAdmin,
										 @RequestParam("id_utilisateur") Integer idUtilisateur,
									  	 @RequestParam("email") String email,
									  	 @RequestParam("login") String login,
									  	 @RequestParam("password") String password,
									  	 @RequestParam("is_remove") boolean isRemove,
									  	 @RequestParam("id_group") Integer idGroup,
									  	 @RequestParam("id_profile") Integer idProfile,
									  	 @RequestParam("nom") String nom,
									  	 @RequestParam("prenom") String prenom,
									  	 @RequestParam("telephone") String telephone,
									  	 @RequestParam("telephone_fixe") String telephone_fixe,
									  	 @RequestParam("photo") String photo) throws ParseException {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(userAdmin == null || idUtilisateur == null || email.equals("") || login.equals("") || password.equals("") ||
		   idGroup == null || idProfile == null || nom.equals("") || prenom.equals("") || telephone.equals(""))
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs parametres manquants");
		}
		else if(!groupRepository.existsByIdGroup(idGroup))
		{
			h.put("status", -1);
			h.put("message", "Le groupe n'existe pas.");
		}
		else if(!profileRepository.existsByIdProfile(idProfile))
		{
			h.put("status", -1);
			h.put("message", "Le profil n'existe pas.");
		}
		else
		{
			Groupe groupe = groupRepository.findByIdGroupe(idGroup).get(0);
			Profile profile = profileRepository.findByIdProfil(idProfile).get(0);
			int result = userRepository.updateUser(email, login, password, new Date(), isRemove, nom, prenom, telephone, photo, telephone_fixe, groupe, profile, idUtilisateur);
			if(result < 0)
			{
				h.put("status", -1);
				h.put("message", "Une erreur est survenue lors de la modification des informations.");
			}
			else
			{
				h.put("status", 0);
				h.put("message", "update OK");
			}
		}
		
		return h;
	}
	
	@RequestMapping(value = "/etreasury_project/admin/user/delete", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> deleteUser(@RequestParam("id_user_admin") Integer userAdmin,
										 @RequestParam("id_utilisateur") Integer idUtilisateur) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(userAdmin == null || idUtilisateur == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs parametres manquants");
		}
		else if(!userRepository.existsByIdUtilisateur(idUtilisateur))
		{
			h.put("status", -1);
			h.put("message", "L'utilisateur n'existe pas");
		}
		else
		{
			userRepository.delete(idUtilisateur);
			h.put("status", 0);
			h.put("message", "La suppression s'est effectué avec succès.");
		}
		
		return h;
	}
	
	@RequestMapping(value = "/etreasury_project/admin/user/list", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> listUser(@RequestParam("id_user_admin") Integer userAdmin) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(userAdmin == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs parametres manquants");
		}
		else
		{
			h.put("status", 0);
			h.put("user_list", userRepository.findAll());
			h.put("message", "list OK");
		}
		
		return h;
	}
	
	public Date getDateString(String dateInString) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.parse(dateInString);
	}
	
	/*
	 * retourne la liste de tous les admins entreprise
	 */
	@RequestMapping(value="/etreasury_project/admin/users/entreprise/list/{typeProfile}", method=RequestMethod.GET)
	public HashMap<String, Object> getUserAdminByEntreprise(@PathVariable("typeProfile") String typeProfile){
		
		HashMap<String, Object> h = new HashMap<>();
		List<User> users = userRepository.findUserAdminByEntreprise(typeProfile);
		
		if (users == null) {
			h.put("message", "Le type d'utilisateur n'existe pas.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "success");
		h.put("list_admin", users);
		h.put("status", 0);
		return h;
	}
	
	/*
	 * retourne la liste de tous les admins bank
	 */
	@RequestMapping(value="/etreasury_project/admin/users/bank/list/{typeProfile}")
	public HashMap<String, Object> getUserAdminByBank(@PathVariable("typeProfile") String typeProfile){
		
		HashMap<String, Object> h = new HashMap<>();
		List<User> users = userRepository.findUserAdminByBank(typeProfile);
		
		if (users == null) {
			h.put("message", "Le type d'utilisateur n'existe pas.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "success");
		h.put("list_admin", users);
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value="/etreasury_project/admin/activate/user/{idAdmin}/{id}", method=RequestMethod.PUT)
	public HashMap<String, Object> activeUser(@RequestBody User user, @PathVariable Integer idAdmin, @PathVariable Integer id){
		
		HashMap<String, Object> h = new HashMap<String, Object>();
		User userOld = userRepository.findOne(id);
		if(idAdmin == null || id == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres nuls");
			return h;
		}
		if (userOld == null) {
			h.put("message", "L'utilisateur n'existe pas.");
			h.put("status", -1);
			return h;
		}
		try {
			if(usService.isAdminGeneral(idAdmin))
			{
				h.put("status", -1);
				h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
				return h;
			}
			String value = "";
			if(user.getIsActive() == true) {
				value = "activé ";
			}
			else {
				value = "désactivé ";
			}
			userOld.setIsActive(user.getIsActive());
			userRepository.saveAndFlush(userOld);
			
			
            String emailFrom = env.getProperty("spring.mail.username");
            if(!EmailValidator.getInstance().isValid(emailFrom))
            {
                h.put("status", -1);
                h.put("message", "L'email d'envoi est incorrect.");
                return h;
            }
            System.out.println("emailFrom  "+emailFrom );
            String emailTo = userOld.getEmail();
            if(!EmailValidator.getInstance().isValid(emailTo))
            {
                h.put("status", -1);
                h.put("message", "L'email destinataire est incorrect.");
                return h;
            }
            System.out.println("emailTo  "+emailTo );
            System.out.println("active  "+value );
            

            String emailObject = env.getProperty("email.object.active");
            //userService.sendMail(emailFrom, emailTo, emailObject, "Votre compte est "+value +" par l'administrateur eTreasury.");
            List<Value> values= new ArrayList<Value>();
            values.add(new Value("",""));
            userService.sendMailNew(emailFrom, emailTo, "eTreasury  | Compte utilisateur ", "Votre compte est "+value +" par l'administrateur eTreasury.", user.getPrenom(), values,"");
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "success");
		h.put("user", userOld);
		h.put("status", 0);
		return h;
	}
	
	
	    // Mot de passe oublie
	    @RequestMapping(value = "/etreasury_project/admin/user/resetPassword", method = RequestMethod.POST)
	    @ResponseStatus(HttpStatus.OK)
	    public Map<String,Object> ForgetPassword(@RequestBody UserLogin userLogin) {
	        HashMap<String, Object> h = new HashMap<String, Object>();
	        
	        if(userLogin.getUsername() == null )
	        {
	            h.put("status", -1);
	            h.put("message", "Le login est vide.");
	            return h;
	        }
	        try
	        {
	            String username = userLogin.getUsername();
	        //  String password = userLogin.getPassword();
	            if(! userService.isAuthenticatedEmail(username))
	            {
	                h.put("status", -1);
	                h.put("message", "L'email est incorrect.");
	                return h;
	            }
	            if(userService.findByEmail(username) == null)
	            {
	                h.put("status", -1);
	                h.put("message", "Ce email n'existe pas.");
	                return h;
	            }
	            User user = userService.findByEmail(username).get(0);
	            String emailFrom = env.getProperty("spring.mail.username");
	            if(!EmailValidator.getInstance().isValid(emailFrom))
	            {
	                h.put("status", -1);
	                h.put("message", "L'email d'envoi est incorrect.");
	                return h;
	            }
	            System.out.println("emailFrom  "+emailFrom );
	            String emailTo = user.getEmail();
	            if(!EmailValidator.getInstance().isValid(emailTo))
	            {
	                h.put("status", -1);
	                h.put("message", "L'email destinataire est incorrect.");
	                return h;
	            }
	            System.out.println("emailTo  "+emailTo );
	            
	            String tokenString = userService.getPasswordString();
	            String cryptPassword = userLoginService.encryptData(tokenString);
	            System.out.println("password crypte "+cryptPassword );

	            String emailObject = env.getProperty("email.object.add");
	          //  userService.sendMail(emailFrom, emailTo, emailObject, "Votre nouveau mot de passe pour accédé  à eTreasury est : "+tokenString);
	            List<Value> values= new ArrayList<Value>();
                values.add(new Value("",tokenString));
                userService.sendMailNew(emailFrom, emailTo, "eTreasury  | Rappel de mot de passe", "Votre mot de passe est :", user.getPrenom(), values,"Nous vous invitons à le mettre à jour dès votre première connexion.");
	          
	            user.setPassword(cryptPassword);
	            
	            user = userBaseRepository.saveAndFlush(user);
	            /*
	        //  Session session = userService.getLModel(username, tokenString);
	        //  sessionRepository.save(session);
	            String type = user.getDiscriminatorValue();
	            h.put("status", 0);
	            h.put("user", userService.findByLogin(username));
	            h.put("token", tokenString);
	        //  h.put("session", session.getIdSession());
	            h.put("type", type);
	            h.put("user", user);
	            h.put("message", "login et mot de passe corrects");
	            */
	        } catch (Exception e) {
	        	e.printStackTrace();
	        	h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
				h.put("status", -1);
	            return h;
	        }
	        //h.put("userAdmin", userAdmin);
	        h.put("message", "Le nouveau mot de passe est envoyé.");
	        h.put("status", 0);
	        
	        return h;
	    }
	    
	    // Mot de passe update
	        @RequestMapping(value = "/etreasury_project/admin/user/updatePassword", method = RequestMethod.POST)
	        @ResponseStatus(HttpStatus.OK)
	        public Map<String,Object> UpdatePassword(@RequestBody UserUpdatePassword userLogin) {
	            HashMap<String, Object> h = new HashMap<String, Object>();
	            
	            if(userLogin.getPassword() == null || userLogin.getIdUser() == null || userLogin.getNewPassword() == null || userLogin.getPassword() == "" ||  userLogin.getNewPassword() == "")
	            {
	                h.put("status", -1);
	                h.put("message", "Le login est vide.");
	                return h;
	            }
	            try
	            {
	                Integer idUser = userLogin.getIdUser();
	                String userPassword = userLogin.getPassword();
	                String userPasswordCrypt = userLoginService.encryptData(userPassword);
	                String userNewPassword = userLogin.getNewPassword();
	                String cryptPassword = userLoginService.encryptData(userNewPassword);
		            System.out.println("password crypte "+cryptPassword );
	                
	                User user = userService.findOne(idUser);
	                if(user == null)
	                {
	                    h.put("status", -1);
	                    h.put("message", "Cet utilisateur n'existe pas.");
	                    return h;
	                }
	                else 
	                //if(userService.findByPassword(userPassword) == null)
	                if(!(user.getPassword().equals(userPasswordCrypt)))
	                {
	                    h.put("status", -1);
	                    h.put("message", "Ceci n'est pas votre mot de passe. Veuillez reessayer");
	                    return h;
	                }
	                
	                user.setPassword(cryptPassword);
	                
	                user = userBaseRepository.saveAndFlush(user);
		            
		            String emailFrom = env.getProperty("spring.mail.username");
		            if(!EmailValidator.getInstance().isValid(emailFrom))
		            {
		                h.put("status", -1);
		                h.put("message", "L'email d'envoi est incorrect.");
		                return h;
		            }
		            System.out.println("emailFrom  "+emailFrom );
		            String emailTo = user.getEmail();
		            if(!EmailValidator.getInstance().isValid(emailTo))
		            {
		                h.put("status", -1);
		                h.put("message", "L'email destinataire est incorrect.");
		                return h;
		            }
		            System.out.println("emailTo  "+emailTo );
		            

		            String emailObject = env.getProperty("email.object.add");
		           // userService.sendMail(emailFrom, emailTo, emailObject, "Votre nouveau mot de passe pour accéder  à eTreasury est : "+userNewPassword);
		            List<Value> values= new ArrayList<Value>();
                    values.add(new Value("",userNewPassword));
                    userService.sendMailNew(emailFrom, emailTo, "eTreasury  | Changement de mot de passe", "Votre nouveau mot de passe est :", user.getPrenom(), values,"Nous vous invitons à le mettre à jour dès votre première connexion.");
		            /*
		        //  Session session = userService.getLModel(username, tokenString);
		        //  sessionRepository.save(session);
		            String type = user.getDiscriminatorValue();
		            h.put("status", 0);
		            h.put("user", userService.findByLogin(username));
		            h.put("token", tokenString);
		        //  h.put("session", session.getIdSession());
		            h.put("type", type);
		            h.put("user", user);
		            h.put("message", "login et mot de passe corrects");
		            */
		        
	                /*
	            //  Session session = userService.getLModel(username, tokenString);
	            //  sessionRepository.save(session);
	                String type = user.getDiscriminatorValue();
	                h.put("status", 0);
	                h.put("user", userService.findByLogin(username));
	                h.put("token", tokenString);
	            //  h.put("session", session.getIdSession());
	                h.put("type", type);
	                h.put("user", user);
	                h.put("message", "login et mot de passe corrects");
	                */
	                
	            } catch (Exception e) {
	            	e.printStackTrace();
	            	h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
	    		    h.put("status", -1);
	                return h;
	            }
	            //h.put("userAdmin", userAdmin);
	            h.put("message", "Le mot de passe est modifié avec succès.");
	            h.put("status", 0);
	            
	            return h;
	        }
	        
	      //Users entreprise de sa localite
	    	@RequestMapping(value="/etreasury_project/admin/user/list_contacts/{idAdmin}", method=RequestMethod.GET)
	    	public HashMap<String, Object> getUserEnterprises(@PathVariable Integer idAdmin){
	    		HashMap<String, Object> h = new HashMap<String, Object>();
	    		List<User> userBanks =  new ArrayList<>();
	    		
	    		//Fields Control
	    		if ( idAdmin == null)
	    		{
	    			h.put("message", "1 ou plusieurs paramètres manquants");
	    			h.put("status", 0);
	    			return h;
	    		}
	    		//
	    		 
	    		try {
	    			 User user = userLoginService.findOne(idAdmin);
	                 if(user == null)
	                 {
	                     h.put("status", -1);
	                     h.put("message", "Cet utilisateur n'existe pas.");
	                     return h;
	                 }
	                 if(user.getDiscriminatorValue().equals("EN")) {
	                	 	userBanks = userRepository.findUsersContactBank(user.getGroupeIdGroupe().getInstitution().getLocalityIdLocalite());
	                 }
	                 else if(user.getDiscriminatorValue().equals("BA")) {
	                	 	userBanks = userRepository.findUsersContactEnterprise(user.getGroupeIdGroupe().getInstitution().getLocalityIdLocalite());
	                      
	                 }
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
}