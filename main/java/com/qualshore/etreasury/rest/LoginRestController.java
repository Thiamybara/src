package com.qualshore.etreasury.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.qualshore.etreasury.dao.SessionRepository;
import com.qualshore.etreasury.dao.UserRepository;
import com.qualshore.etreasury.entity.Session;
import com.qualshore.etreasury.entity.User;
import com.qualshore.etreasury.entity.UserConfirmation;
import com.qualshore.etreasury.entity.UserLogin;
import com.qualshore.etreasury.mail.Value;
import com.qualshore.etreasury.service.UserLoginService;

@RequestMapping("etreasury_project/login")
@RestController
public class LoginRestController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	SessionRepository sessionRepository;
	
	/*@Autowired
	JavaMailSender mailSender;*/
	
	@Autowired
	Environment env;
	
	@Autowired
	UserLoginService userService;
	
	// Authentification
	@RequestMapping(value = "/authentification", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> loginAthentication(@RequestBody UserLogin userLogin) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		if(userLogin.getUsername() == null || userLogin.getPassword() == null)
		{
			h.put("status", -1);
			h.put("message", "Le login et/ou le mot de passe est vide.");
			return h;
		}
		try
		{
			String username = userLogin.getUsername();
			String password = userLogin.getPassword();
			String cryptPassword = userService.encryptData(password);
            System.out.println("password crypte "+cryptPassword );
            System.out.println("login pass "+username + "  et "+ password );
            
            if(! userService.isAuthenticated(username, cryptPassword))
			{
				h.put("status", -1);
				h.put("message", "Le login et/ou mot de passe est incorrect.");
				return h;
			}
            if(!(userRepository.existsByLoginAndPassword(username, cryptPassword)))
			{
				h.put("status", -1);
				h.put("message", "Les identifiants sont incorrects.");
				return h;
			}
			if(userService.findByLogin(username) == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'existe pas.");
				return h;
			}
			User user = userService.findByLogin(username);
			if(user == null)
			{
				h.put("status", -1);
				h.put("message", "Erreur recuperation compte.");
				return h;
			}
			if(user.getIsActive() == false)
			{
				h.put("status", -1);
				h.put("message", "Le compte de cet utilisateur est désactivé, veuillez contacter l'administrateur eTreasury.");
				return h;
			}
			String emailFrom = env.getProperty("spring.mail.username");
			if(!EmailValidator.getInstance().isValid(emailFrom))
			{
				h.put("status", -1);
				h.put("message", "L'email d'envoi est incorrect.");
				return h;
			}
			
			String emailTo = user.getEmail();
			if(!EmailValidator.getInstance().isValid(emailTo))
			{
				h.put("status", -1);
				h.put("message", "L'email destinataire est incorrect.");
				return h;
			}
			
			String tokenString = userService.getSaltString();
			//String emailObject = env.getProperty("email.object");
			//userService.sendMail(emailFrom, emailTo, emailObject, tokenString);
			  List<Value> values= new ArrayList<Value>();
              values.add(new Value("",tokenString));
              try {
				userService.sendMailNew(emailFrom, emailTo, "eTreasury  | Token de connexion", "Pour vous connecter, veuillez saisir votre token valide les cinq prochaines minutes :", user.getPrenom(), values,"");
			} catch (Exception e) {
				e.printStackTrace();
				h.put("message", "Erreur lors de l'établissement de la connexion, veuillez réessayer ou vérifier votre connexion internet.");
				h.put("status", -1);
				return h;
			}
	          
			Session session = userService.getLModel(username, tokenString);
			sessionRepository.save(session);
			String type = user.getDiscriminatorValue();
			h.put("status", 0);
			h.put("user", userService.findByLogin(username));
			h.put("token", tokenString);
			h.put("session", session.getIdSession());
			h.put("type", type);
			h.put("user", user);
			h.put("message", "Le login et le mot de passe sont corrects.");
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		return h;
	}
	
	// Confirmation token and session
	@RequestMapping(value = "/token/validation", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> confirmToken(@RequestBody UserConfirmation userConfirmation) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		if(userConfirmation.getIdUser() == null || userConfirmation.getToken() == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		try
		{
			Integer idUser = userConfirmation.getIdUser();
			String token = userConfirmation.getToken();
			
			User user = userService.findOne(idUser);
			if(user == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'existe pas.");
				return h;
			}
			if(user.getIsConnected() == true)
			{
				h.put("status", -1);
				h.put("message", "Vous avez déjà une session ouverte.");
				return h;
			}
			else if(sessionRepository.findByUserIdUtilisateurOrderByIdSessionDesc(user) == null)
			{
				h.put("status", -1);
				h.put("message", "La session est nulle.");
				return h;
			}
			Session session = sessionRepository.findByUserIdUtilisateurOrderByIdSessionDesc(user).get(0);
			if(session == null)
			{
				h.put("status", -1);
				h.put("message", "La session n'existe pas.");
				return h;
			}
			else if(session.getCompteur() >= 3)
			{
				h.put("status", -1);
				h.put("message", "Le nombre maximum d'essais est atteint.");
				sessionRepository.delete(session);
				return h;
			}
			else if(!token.equals(session.getToken()))
			{
				sessionRepository.updateCompteur(session.getCompteur() + 1, session.getIdSession());
				int nbEssaiRestant = 2 - session.getCompteur();
				h.put("status", -1);
				h.put("message", "Le token est incorrect:"+ nbEssaiRestant+ " essais restant.");
				return h;
			}
			else
			{
				if(userService.tempsRestant(session.getDateFin()) > 0)
				{
					h.put("status", 0);
					h.put("user", user);
					h.put("message", "Le token est confirmé avec succès.");
					
					// Journaliser Connexion(idAction = 2)
					userService.journaliser(idUser, 2, "User connexion");
				}
				else
				{
					h.put("status", -1);
					h.put("message", "La session a expiré.");
				}
				sessionRepository.delete(session);
				user.setIsConnected(true);
				userRepository.saveAndFlush(user);
				return h;
			}
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
    }
	
	// Deconnexion
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> Deconnexion(@RequestBody UserConfirmation userConfirmation) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		if(userConfirmation.getIdUser() == null )
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		try
		{
			Integer idUser = userConfirmation.getIdUser();
			
			User user = userService.findOne(idUser);
			if(user == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'existe pas.");
				return h;
			}

			// Journaliser Connexion(idAction = 2)
			userService.journaliser(idUser, 5, "User deconnexion");
			user.setIsConnected(false);
			userRepository.saveAndFlush(user);
			h.put("status", 0);
		//	h.put("user", user);
			h.put("message", "Vous êtes déconnecté avec succès.");
			return h;
		//	
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		
    }
}
