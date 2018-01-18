package com.qualshore.etreasury.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.qualshore.etreasury.dao.DeviseRepository;
import com.qualshore.etreasury.dao.NotificationsRepository;
import com.qualshore.etreasury.dao.NotificationsUserRepository;
import com.qualshore.etreasury.dao.OfferRepository;
import com.qualshore.etreasury.dao.RequestHasBankRepository;
import com.qualshore.etreasury.dao.RequestRepository;
import com.qualshore.etreasury.dao.UserRepository;
import com.qualshore.etreasury.dao.ValidationLevelGroupeRepository;
import com.qualshore.etreasury.dao.ValidationLevelRepository;
import com.qualshore.etreasury.entity.Groupe;
import com.qualshore.etreasury.entity.Institution;
import com.qualshore.etreasury.entity.Notifications;
import com.qualshore.etreasury.entity.NotificationsUser;
import com.qualshore.etreasury.entity.Offer;
import com.qualshore.etreasury.entity.Products;
import com.qualshore.etreasury.entity.Request;
import com.qualshore.etreasury.entity.RequestHasBank;
import com.qualshore.etreasury.entity.User;
import com.qualshore.etreasury.entity.ValidationLevel;
import com.qualshore.etreasury.entity.ValidationLevelGroupe;
import com.qualshore.etreasury.mail.EmailService;
import com.qualshore.etreasury.mail.Mail;
import com.qualshore.etreasury.mail.Value;

@Service
public class ValidationService {

	@Autowired
	JavaMailSender mailSender;
	
	@Autowired
	ValidationLevelRepository validationLevelRepository;
	
	@Autowired
	ValidationLevelGroupeRepository validationLevelGroupeRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	Environment env;
	
	@Autowired
	NotificationsRepository notificationsRepository;
	
	@Autowired
	RequestHasBankRepository rhbRepository;
	
	@Autowired
	RequestRepository requestRepository;
	
	@Autowired
	OfferRepository offerRepository;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	NotificationsUserRepository notificationsUserRep;
	
	@Autowired
	DeviseRepository deviseRepository;
	
	@Autowired
	UserService userService;

	public void sendMail(String from, String to, String subject, String body) {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setFrom(from);
		mail.setTo(to);
		mail.setSubject(subject);
		mail.setText(body);
		mailSender.send(mail);
	}

	
	public void sendMailNew(String from, String to, String subject, String body, String user, List<Value> values, String descriptif) {
        Mail mail = new Mail();
       mail.setFrom(from);
       mail.setTo(to);
       mail.setSubject(subject);

       Map<String, Object> model = new HashMap<String, Object>();
       model.put("nom", user);
       model.put("body", body);
       //model.put("sujet", title);
       model.put("values", values);
       model.put("descriptif", descriptif);
       Boolean ok=true;
       if(values.size()<2) {
               ok=false;
       }
       model.put("ok", ok);
       mail.setModel(model);

       try {
            emailService.sendSimpleMessage(mail);
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
	
	public long tempsRestant(Date dateFin) {
		long result = 0;
		try {
			result = (dateFin.getTime() - new Date().getTime()) / 60000;
			System.out.print("TEMPS RESTANT  "+result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}	
	
	public ArrayList<Groupe> getGroupeList(Object object) {
		ArrayList<Groupe> groupeList = new ArrayList<>();
		ValidationLevel vLevel = null;
		if(object instanceof Offer)
		{
			vLevel = validationLevelRepository.findByProductAndInstitutionAndSens(((Offer) object).getDemandeIdDemande().getProduct(), ((Offer) object).getUserBanqueIdUserBanque().getGroupeIdGroupe().getInstitution(), "offer");
		}
		else if(object instanceof Request)
		{
			vLevel = validationLevelRepository.findByProductAndInstitutionAndSens(((Request) object).getProduct(), ((Request) object).getUserEntreprise().getGroupeIdGroupe().getInstitution(), "request");
		}
		List<ValidationLevelGroupe> vLevelGroupe = validationLevelGroupeRepository.findByValidationLevel(vLevel);
		for(ValidationLevelGroupe vlg:vLevelGroupe)
		{
			Groupe groupe = vlg.getGroupe();
			groupeList.add(groupe);
		}
		return groupeList;
	}
	
	public void sendMailAllValidated(Object object) {
		ArrayList<Groupe> groupeList = getGroupeList(object);
		String emailFrom = env.getProperty("spring.mail.username");
		for(Groupe groupe:groupeList)
		{
			List<User> userList = userRepository.findByGroupeIdGroupe(groupe);
			for(User user : userList)
			{
				//Toute la chaine a validee
				//Demande validée par toute la chaine
				if(object instanceof Request)
				{
					createNotificationsUser(((Request) object).getIdDemande(), null, 3, ((Request) object).getUserEntreprise().getGroupeIdGroupe().getInstitution().getNom(), user);
					 List<Value> values= new ArrayList<Value>();
					 values.add(new Value("",((Request) object).getNumeroRequest()+""));
					sendMailNew(emailFrom,  user.getEmail(), "eTreasury  | Validation effective", "Toute la chaine a validé la demande : ", user.getPrenom(), values, "");
				
				}
				else if(object instanceof Offer)
				{
					createNotificationsUser(null, ((Offer) object).getIdOffre(), 8, ((Offer) object).getUserBanqueIdUserBanque().getGroupeIdGroupe().getInstitution().getNom(), user);
					 List<Value> values= new ArrayList<Value>();
		              values.add(new Value("",((Offer) object).getIdOffre()+""));
					sendMailNew(emailFrom,  user.getEmail(), "eTreasury  | Validation effective", "Toute la chaine a validé l'offre relative à la demande nº "+ ((Offer) object).getDemandeIdDemande().getNumeroRequest()+" : ", user.getPrenom(), values, "");
				
				}
			}
			
			//Send mail for offer first level validation
			if(object instanceof Offer)
			{
				if(!((Offer) object).isValid() && validationLevelRepository.existsByProductAndInstitutionAndSensAndAllsRequired(((Offer) object).getDemandeIdDemande().getProduct(),
						((Offer) object).getUserBanqueIdUserBanque().getGroupeIdGroupe().getInstitution(), "offer", true))
				{
					if(validationLevelGroupeRepository.existsByGroupeAndNiveau(groupe, 1))
					{
						//Get next group to validate
						((Offer) object).setNextValidationGroup("Val "+groupe.getNom());
						offerRepository.saveAndFlush((Offer) object);
						
						List<User> userListFirst = userRepository.findByGroupeIdGroupe(groupe);
						for(User user : userListFirst)
						{
							//Offre en attente de validation d'un membre de votre groupe
							createNotificationsUser(null, ((Offer) object).getIdOffre(), 6, ((Offer) object).getUserBanqueIdUserBanque().getGroupeIdGroupe().getInstitution().getNom(), user);
							List<Value> values= new ArrayList<Value>();
				            values.add(new Value("",((Offer) object).getIdOffre()+""));
							sendMailNew(emailFrom,  user.getEmail(), "eTreasury  | Validation en attente", "Vous avez reçu une nouvelle offre relative à la demande nº "+ ((Offer) object).getDemandeIdDemande().getNumeroRequest()+"  sur eTreasury: ", user.getPrenom(), values, "L'offre est en attente de validation d'un membre de votre groupe.");
					
						}
					}
				}
			}
			
			//Send mail for request first level validation
			if(object instanceof Request)
			{
				if(!((Request) object).getIsValid() && validationLevelRepository.existsByProductAndInstitutionAndSensAndAllsRequired(((Request) object).getProduct(),
						((Request) object).getUserEntreprise().getGroupeIdGroupe().getInstitution(), "request", true))
				{
					if(validationLevelGroupeRepository.existsByGroupeAndNiveau(groupe, 1))
					{
						//Get next group to validate
						((Request) object).setNextValidationGroup("Val "+groupe.getNom());
						requestRepository.saveAndFlush((Request) object);
						
						List<User> userListFirst = userRepository.findByGroupeIdGroupe(groupe);
						for(User user : userListFirst)
						{
							createNotificationsUser(((Request) object).getIdDemande(), null, 1, ((Request) object).getUserEntreprise().getGroupeIdGroupe().getInstitution().getNom(), user);
							List<Value> values= new ArrayList<Value>();
				            values.add(new Value("",((Request) object).getNumeroRequest()+""));
							sendMailNew(emailFrom,  user.getEmail(), "eTreasury  | Validation en attente", "Vous avez reçu une nouvelle demande de cotation sur eTreasury: ", user.getPrenom(), values, "La demande est en attente de validation d'un membre de votre groupe.");
						}
					}
				}
			}
		}
	}
	
	public void sendMailFirst(Object object) {
		ArrayList<Groupe> groupeList = getGroupeList(object);
		String emailFrom = env.getProperty("spring.mail.username");
		int n = 0;
		for(Groupe groupe:groupeList)
		{
			List<User> userList = userRepository.findByGroupeIdGroupe(groupe);
			for(User user : userList)
			{
				//votre groupe fait partie de la chaine de validation
				if(object instanceof Request)
				{
					createNotificationsUser(((Request) object).getIdDemande(), null, 0, ((Request) object).getUserEntreprise().getGroupeIdGroupe().getInstitution().getNom(), user);
					List<Value> values= new ArrayList<Value>();
		            values.add(new Value("",((Request) object).getNumeroRequest()+""));
					sendMailNew(emailFrom,  user.getEmail(), "eTreasury  | Nouvelle demande de validation", "Vous avez reçu une nouvelle demande de validation sur eTreasury: ", user.getPrenom(), values, "Votre groupe fait partie de la chaine de validation. Vous recevrez un email de confirmation après validation des niveaux inférieurs.");
				}
				else if(object instanceof Offer)
				{
					createNotificationsUser(null, ((Offer) object).getIdOffre(), 5, ((Offer) object).getUserBanqueIdUserBanque().getGroupeIdGroupe().getInstitution().getNom(), user);
					List<Value> values= new ArrayList<Value>();
		            values.add(new Value("",((Offer) object).getIdOffre()+""));
					sendMailNew(emailFrom,  user.getEmail(), "eTreasury  | Nouvelle offre de validation", "Vous avez reçu une nouvelle offre de cotation sur eTreasury: ", user.getPrenom(), values, "Votre groupe fait partie de la chaine de validation. Vous recevrez un email de confirmation après validation des niveaux inférieurs.");
				}
			}
			
			//Send mail for offer first level validation
			if(object instanceof Offer)
			{
				if(!((Offer) object).isValid())
				{
					ValidationLevel vLevel = validationLevelRepository.findByProductAndInstitutionAndSens(((Offer) object).getDemandeIdDemande().getProduct(), ((Offer) object).getUserBanqueIdUserBanque().getGroupeIdGroupe().getInstitution(), "offer");
					ValidationLevelGroupe vlg;
					if(vLevel.getAllsRequired())
						vlg = validationLevelGroupeRepository.findByProductAndInstitutionAndGroupeFirstLevel(((Offer) object).getDemandeIdDemande().getProduct(), ((Offer) object).getUserBanqueIdUserBanque().getGroupeIdGroupe().getInstitution(), groupe, "offer");
					else
						vlg = validationLevelGroupeRepository.findByProductAndInstitutionAndGroupe(((Offer) object).getDemandeIdDemande().getProduct(), ((Offer) object).getUserBanqueIdUserBanque().getGroupeIdGroupe().getInstitution(), groupe, "offer");
					if(vLevel != null && vlg != null && (vLevel.getAllsRequired() || (!vLevel.getAllsRequired() && userService.isMontantAvailable(((Offer) object).getDemandeIdDemande(), vlg, deviseRepository))))
					{
						Groupe groupe1 = vlg.getGroupe();
						//Get next group to validate
						((Offer) object).setNextValidationGroup("Val "+groupe1.getNom());
						offerRepository.saveAndFlush((Offer) object);
						List<User> userListFirst = userRepository.findByGroupeIdGroupe(groupe1);
						for(User user : userListFirst)
						{
							//Un membre de votre groupe doit valider
							createNotificationsUser(null, ((Offer) object).getIdOffre(), 6, ((Offer) object).getUserBanqueIdUserBanque().getGroupeIdGroupe().getInstitution().getNom(), user);
							List<Value> values= new ArrayList<Value>();
				            values.add(new Value("",((Offer) object).getIdOffre()+""));
							sendMailNew(emailFrom,  user.getEmail(), "eTreasury  | Validation en attente", "Vous avez reçu une nouvelle offre de cotation sur eTreasury: ", user.getPrenom(), values, "L'offre est en attente de validation d'un membre de votre groupe.");
						}
					}
				}
			}
			
			//Send mail for request first level validation
			if(object instanceof Request)
			{
				if(!((Request) object).getIsValid())
				{
					ValidationLevel vLevel = validationLevelRepository.findByProductAndInstitutionAndSens(((Request) object).getProduct(), ((Request) object).getUserEntreprise().getGroupeIdGroupe().getInstitution(), "request");
					ValidationLevelGroupe vlg;
					if(vLevel.getAllsRequired())
						vlg = validationLevelGroupeRepository.findByProductAndInstitutionAndGroupeFirstLevel(((Request) object).getProduct(), ((Request) object).getUserEntreprise().getGroupeIdGroupe().getInstitution(), groupe, "request");
					else
						vlg = validationLevelGroupeRepository.findByProductAndInstitutionAndGroupe(((Request) object).getProduct(), ((Request) object).getUserEntreprise().getGroupeIdGroupe().getInstitution(), groupe, "request");
					if(vLevel != null && vlg != null && (vLevel.getAllsRequired() || (!vLevel.getAllsRequired() && userService.isMontantAvailable(((Request) object), vlg, deviseRepository))))
					{
						Groupe groupe1 = vlg.getGroupe();
						//Get next group to validate
						((Request) object).setNextValidationGroup("Val "+groupe1.getNom());
						requestRepository.saveAndFlush((Request) object);
						List<User> userListFirst = userRepository.findByGroupeIdGroupe(groupe1);
						for(User user : userListFirst)
						{
							//"Un membre de votre groupe doit valider
							createNotificationsUser(((Request) object).getIdDemande(), null, 1, ((Request) object).getUserEntreprise().getGroupeIdGroupe().getInstitution().getNom(), user);
							List<Value> values= new ArrayList<Value>();
				            values.add(new Value("",((Request) object).getNumeroRequest()+""));
							sendMailNew(emailFrom,  user.getEmail(), "eTreasury  | Validation en attente", "Vous avez reçu une nouvelle demande de validation sur eTreasury: ", user.getPrenom(), values, "La demande est en attente de validation d'un membre de votre groupe.");
						}
					}
				}
			}
		}
	}
	
	public void sendMailGroupeRequestToValidate(Request request, Groupe groupe) {
		List<User> userList = userRepository.findByGroupeIdGroupe(groupe);
		String emailFrom = env.getProperty("spring.mail.username");
		//Get next group to validate
		request.setNextValidationGroup("Val "+groupe.getNom());
		requestRepository.saveAndFlush(request);
		for(User user : userList)
		{
			//Un membre de votre groupe doit valider
			Institution bank = request.getUserEntreprise().getGroupeIdGroupe().getInstitution();
			createNotificationsUser(request.getIdDemande(), null, 1, bank.getNom(), user);
			//sendMail(emailFrom, user.getEmail(), emailSubject, emailBody);
			List<Value> values= new ArrayList<Value>();
            values.add(new Value("",request.getNumeroRequest()+""));
			sendMailNew(emailFrom,  user.getEmail(), "eTreasury  | Validation en attente", "Vous avez reçu une nouvelle demande de validation sur eTreasury: ", user.getPrenom(), values, "La demande est en attente de validation d'un membre de votre groupe.");
		}
	}
	
	public void sendMailGroupeOfferToValidate(Offer offer, Groupe groupe) {
		List<User> userList = userRepository.findByGroupeIdGroupe(groupe);
		String emailFrom = env.getProperty("spring.mail.username");
		//Get next group to validate
		offer.setNextValidationGroup("Val "+groupe.getNom());
		offerRepository.saveAndFlush(offer);
		for(User user : userList)
		{
			//New Notification
			Institution enterprise = offer.getUserBanqueIdUserBanque().getGroupeIdGroupe().getInstitution();
			createNotificationsUser(null, offer.getIdOffre(), 6, enterprise.getNom(), user);
			List<Value> values= new ArrayList<Value>();
            values.add(new Value("",offer.getIdOffre()+""));
			sendMailNew(emailFrom,  user.getEmail(), "eTreasury  | Validation du groupe", "Un membre de votre groupe doit valider l'offre relative à la demande nº "+ offer.getDemandeIdDemande().getNumeroRequest()+" : ", user.getPrenom(), values, "");
		}
	}
	
	public void sendMailGroupeOfferToPreValidate(Offer offer, Groupe groupe) {
		List<User> userList = userRepository.findByGroupeIdGroupe(groupe);
		String emailFrom = env.getProperty("spring.mail.username");
		//Get next group to validate
		offer.setNextValidationGroup("Val "+groupe.getNom());
		offerRepository.saveAndFlush(offer);
		for(User user : userList)
		{
			//New Notification
			Institution enterprise = offer.getUserBanqueIdUserBanque().getGroupeIdGroupe().getInstitution();
			createNotificationsUser(offer.getDemandeIdDemande().getIdDemande(), offer.getIdOffre(), 12, enterprise.getNom(), user);
			List<Value> values= new ArrayList<Value>();
            values.add(new Value("",offer.getIdOffre()+""));
			sendMailNew(emailFrom,  user.getEmail(), "eTreasury  | Pré-validation du groupe", "Un membre de votre groupe doit pré-valider l'offre : ", user.getPrenom(), values, "");
		}
	}
	
	public void sendMailGroupeRequestAlreadyValidated(Request request, Groupe groupe) {
		List<User> userList = userRepository.findByGroupeIdGroupe(groupe);
		String emailFrom = env.getProperty("spring.mail.username");
		for(User user : userList)
		{
			//Demande validee par un membre de votre groupe
			Institution bank = request.getUserEntreprise().getGroupeIdGroupe().getInstitution();
			createNotificationsUser(request.getIdDemande(), null, 2, bank.getNom(), user);
			List<Value> values= new ArrayList<Value>();
            values.add(new Value("",request.getNumeroRequest()+""));
			sendMailNew(emailFrom,  user.getEmail(), "eTreasury  | Validation du groupe", "Un membre de votre groupe a validé la demande : ", user.getPrenom(), values, "");
		}
	}
	
	public void sendMailGroupeOfferAlreadyValidated(Offer offer, Groupe groupe, String emailSubject, String emailBody) {
		List<User> userList = userRepository.findByGroupeIdGroupe(groupe);
		String emailFrom = env.getProperty("spring.mail.username");
		for(User user : userList)
		{
			//Demande validee par un membre de votre groupe
			//New Notification
			Institution enterprise = offer.getUserBanqueIdUserBanque().getGroupeIdGroupe().getInstitution();
			createNotificationsUser(null, offer.getIdOffre(), 7, enterprise.getNom(), user);
			
			//sendMail(emailFrom, user.getEmail(), emailSubject, emailBody);
			List<Value> values= new ArrayList<Value>();
            values.add(new Value("",offer.getIdOffre()+""));
			sendMailNew(emailFrom,  user.getEmail(), "eTreasury  | Validation du groupe", "Un membre de votre groupe a validé l'offre relative à la demande nº "+ offer.getDemandeIdDemande().getNumeroRequest()+"  : ", user.getPrenom(), values, "");
	
		}
	}
	
	/*public void sendMailGroupeOfferAlreadyPreValidated(Offer offer, Groupe groupe, String emailSubject, String emailBody) {
		List<User> userList = userRepository.findByGroupeIdGroupe(groupe);
		String emailFrom = env.getProperty("spring.mail.username");
		for(User user : userList)
		{
			//Demande validee par un membre de votre groupe
			//New Notification
			Institution enterprise = offer.getUserBanqueIdUserBanque().getGroupeIdGroupe().getInstitution();
			createNotificationsUser(null, offer.getIdOffre(), 13, enterprise.getNom(), user);
			
			//sendMail(emailFrom, user.getEmail(), emailSubject, emailBody);
			List<Value> values= new ArrayList<Value>();
            values.add(new Value("",offer.getIdOffre()+""));
			sendMailNew(emailFrom,  user.getEmail(), "eTreasury  | Pré-validation du groupe", "Un membre de votre groupe a accepté l'offre : ", user.getPrenom(), values, "");
	
		}
	}*/
	
	public void sendMailGroupeValid(Object object, Groupe groupe) {
		List<User> userList = userRepository.findByGroupeIdGroupe(groupe);
		String emailFrom = env.getProperty("spring.mail.username");
		for(User user : userList)
		{
			if(object instanceof Request)
			{
				Institution enterprise = ((Request)object).getUserEntreprise().getGroupeIdGroupe().getInstitution();
				//Validation faite par un membre de votre groupe
				createNotificationsUser(((Request)object).getIdDemande(), null, 2, enterprise.getNom(), user);
				List<Value> values= new ArrayList<Value>();
				values.add(new Value("",((Request) object).getNumeroRequest()+""));
				sendMailNew(emailFrom,  user.getEmail(), "eTreasury  | Validation du groupe", "Un membre de votre groupe a validé la demande : ", user.getPrenom(), values, "");
			}
			else if(object instanceof Offer)
			{
				Institution bank =((Offer)object).getUserBanqueIdUserBanque().getGroupeIdGroupe().getInstitution();
				//Validation faite par un membre de votre groupe
				createNotificationsUser(null, ((Offer)object).getIdOffre(), 7, bank.getNom(), user);
				List<Value> values= new ArrayList<Value>();
				values.add(new Value("",((Offer) object).getIdOffre()+""));
				sendMailNew(emailFrom,  user.getEmail(), "eTreasury  | Validation du groupe", "Un membre de votre groupe a validé l'offre relative à la demande nº "+ ((Offer) object).getDemandeIdDemande().getNumeroRequest()+"  : ", user.getPrenom(), values, "");
		
			}
		}
	}
	
	public void sendMailGroupeAcceptPreValid(Offer offer, Groupe groupe) {
		List<User> userList = userRepository.findByGroupeIdGroupe(groupe);
		String emailFrom = env.getProperty("spring.mail.username");
		for(User user : userList)
		{
			Institution bank =offer.getUserBanqueIdUserBanque().getGroupeIdGroupe().getInstitution();
			createNotificationsUser(offer.getDemandeIdDemande().getIdDemande(), offer.getIdOffre(), 13, bank.getNom(), user);
			List<Value> values= new ArrayList<Value>();
			values.add(new Value("", offer.getIdOffre()+""));
			sendMailNew(emailFrom,  user.getEmail(), "eTreasury  | Pré-validation du groupe", "Un membre de votre groupe a accepté l'offre : ", user.getPrenom(), values, "");
		}
	}
	
	public void sendMailGroupeRefusePreValid(Offer offer, Groupe groupe) {
		List<User> userList = userRepository.findByGroupeIdGroupe(groupe);
		String emailFrom = env.getProperty("spring.mail.username");
		for(User user : userList)
		{
			//Demande validee par un membre de votre groupe
			//New Notification
			Institution enterprise = offer.getUserBanqueIdUserBanque().getGroupeIdGroupe().getInstitution();
			createNotificationsUser(null, offer.getIdOffre(), 14, enterprise.getNom(), user);
			List<Value> values= new ArrayList<Value>();
            values.add(new Value("",offer.getIdOffre()+""));
			sendMailNew(emailFrom,  user.getEmail(), "eTreasury  | Pré-validation du groupe", "Un membre de votre groupe a refusé l'offre : ", user.getPrenom(), values, "");
		}
	}
	
	public void sendMailToSelectedOfferInstitution(Offer offer) {
		String emailFrom = env.getProperty("spring.mail.username");
		Request request = offer.getDemandeIdDemande();
		Products product = request.getProduct();
		List<RequestHasBank> rhbList = rhbRepository.findByRequest(request);
		for(RequestHasBank rhb : rhbList)
		{
			List<Notifications> notificationsList = notificationsRepository.findByBankAndProductAndEnterprise(rhb.getBank(), product.getIdProduits(), request.getUserEntreprise().getGroupeIdGroupe().getInstitution().getIdInstitution());
		//	List<Notifications> notificationsList = notificationsRepository.findByBankAndProductAndEnterprise(product.getIdProduits(), rhb.getBank().getIdInstitution());
			
			for(Notifications notification : notificationsList)
			{
				if(offer.getUserBanqueIdUserBanque().getGroupeIdGroupe().getInstitution().getIdInstitution() == notification.getUser().getGroupeIdGroupe().getInstitution().getIdInstitution())
				{
					//Banque gagnante
					User user = notification.getUser();
					//Institution enterprise = request.getUserEntreprise().getGroupeIdGroupe().getInstitution();
					Institution bank = offer.getUserBanqueIdUserBanque().getGroupeIdGroupe().getInstitution();
					//New Notification
					
					//Offre selectionnee
					createNotificationsUser(request.getIdDemande(), offer.getIdOffre(), 17, bank.getNom(), user);
					List<Value> values= new ArrayList<Value>();
		            values.add(new Value("",request.getIdDemande()+""));
					sendMailNew(emailFrom,  user.getEmail(), "eTreasury  | Offre sélectionnée", "Votre offre de cotation relative à la demande nº "+ request.getNumeroRequest()+" a été acceptée : ", user.getPrenom(), values, "");
				}
				else
				{
					//Banque non acceptee
					User user = notification.getUser();
					//Institution enterprise = request.getUserEntreprise().getGroupeIdGroupe().getInstitution();
					Institution bank = offer.getUserBanqueIdUserBanque().getGroupeIdGroupe().getInstitution();
					//New Notification
					
					//Offre non selectionnee
					createNotificationsUser(request.getIdDemande(), offer.getIdOffre(), 18, bank.getNom(), user);List<Value> values= new ArrayList<Value>();
		            values.add(new Value("",offer.getIdOffre()+""));
					sendMailNew(emailFrom,  user.getEmail(), "eTreasury  | Offre non sélectionnée", "Votre offre de cotation relative à la demande nº "+ request.getNumeroRequest()+" n'a pas été acceptée : ", user.getPrenom(), values, "");
				}
			}
		}
	}
	
	public void sendMailToInstitution(Object object) {
		String emailFrom = env.getProperty("spring.mail.username");
		if(object instanceof Request)
		{
			Products product = ((Request) object).getProduct();
			List<RequestHasBank> rhbList = rhbRepository.findByRequest((Request) object);
			for(RequestHasBank rhb : rhbList)
			{
				List<Notifications> notificationsList = notificationsRepository.findByBankAndProductAndEnterprise(rhb.getBank(), product.getIdProduits(), ((Request) object).getUserEntreprise().getGroupeIdGroupe().getInstitution().getIdInstitution());
				//List<Notifications> notificationsList = notificationsRepository.findByBankAndProductAndEnterprise(product.getIdProduits(), rhb.getBank().getIdInstitution());
				for(Notifications notification : notificationsList)
				{
					User user = notification.getUser();
					Institution enterprise = ((Request) object).getUserEntreprise().getGroupeIdGroupe().getInstitution();createNotificationsUser(((Request) object).getIdDemande(), null, 4, enterprise.getNom(), user);List<Value> values= new ArrayList<Value>();
		            values.add(new Value("",((Request) object).getNumeroRequest()+""));
					sendMailNew(emailFrom,  user.getEmail(), "eTreasury  | Nouvelle demande reçue", "Vous avez reçu une nouvelle demande de cotation de l'entreprise "+enterprise.getNom()+" sur eTreasury: ", user.getPrenom(), values, "Merci de répondre via Etreasury.");
				}
			}
		}
		
		if(object instanceof Offer)
		{
			
			//List<Notifications> notificationsList = notificationsRepository.findByBankAndProductAndEnterprise(rhb.getBank(), product.getIdProduits(), ((Request) object).getUserEntreprise().getGroupeIdGroupe().getInstitution().getIdInstitution());
			List<Notifications> notificationsList = notificationsRepository.findByOwnProductAndEnterprise(((Offer) object).getDemandeIdDemande().getProduct().getIdProduits(), ((Offer) object).getDemandeIdDemande().getUserEntreprise().getGroupeIdGroupe().getInstitution().getIdInstitution());
			Institution bank = ((Offer) object).getUserBanqueIdUserBanque().getGroupeIdGroupe().getInstitution();
			
			for(Notifications notification : notificationsList)
			{
				User user = notification.getUser();
				createNotificationsUser(null, ((Offer) object).getIdOffre(), 9, bank.getNom(), user);List<Value> values= new ArrayList<Value>();
	            values.add(new Value("",((Offer) object).getIdOffre()+""));
				sendMailNew(emailFrom,  user.getEmail(), "eTreasury  | Nouvelle offre reçue", "Vous avez reçu une nouvelle offre de la banque "+bank.getNom()+" relative à la demande nº "+ ((Offer) object).getDemandeIdDemande().getNumeroRequest()+" sur eTreasury: ", user.getPrenom(), values, "Merci de répondre via Etreasury.");
		
			}
			
			
			
			/*
			User user = ((Offer) object).getDemandeIdDemande().getUserEntreprise();
			Institution bank = ((Offer) object).getUserBanqueIdUserBanque().getGroupeIdGroupe().getInstitution();
			createNotificationsUser(null, ((Offer) object).getIdOffre(), 9, bank.getNom(), user);List<Value> values= new ArrayList<Value>();
            values.add(new Value("",((Offer) object).getIdOffre()+""));
			sendMailNew(emailFrom,  user.getEmail(), "eTreasury  | Nouvelle offre reçue", "Vous avez reçu une nouvelle offre de la banque "+bank.getNom()+" relative à la demande nº "+ ((Offer) object).getDemandeIdDemande().getNumeroRequest()+" sur eTreasury: ", user.getPrenom(), values, "Merci de répondre via Etreasury.");
		*/
		}
	}
	
	public NotificationsUser createNotificationsUser(Integer idRequest, Integer idOffer, Integer type, String nomInstitution, User user ) {
		NotificationsUser notifUser = new NotificationsUser();
		notifUser.setIdRequest(idRequest);
		notifUser.setIdOffer(idOffer);
		notifUser.setType(type);
		notifUser.setUser(user);
		notifUser.setNomInstitution(nomInstitution);
		notifUser.setDateNotification(new Date());
		notificationsUserRep.save(notifUser);
		return notifUser;
	}
	
	public void sendMailAllValidatedPreSelection(Offer offer) {
		ArrayList<Groupe> groupeList = getGroupeListPreSelection(offer);
		String emailFrom = env.getProperty("spring.mail.username");
		for(Groupe groupe:groupeList)
		{
			List<User> userList = userRepository.findByGroupeIdGroupe(groupe);
			for(User user : userList)
			{
				createNotificationsUser(offer.getDemandeIdDemande().getIdDemande(), offer.getIdOffre(), 15, offer.getUserBanqueIdUserBanque().getGroupeIdGroupe().getInstitution().getNom(), user);
				List<Value> values= new ArrayList<Value>();
	            values.add(new Value("",offer.getIdOffre()+""));
				sendMailNew(emailFrom,  user.getEmail(), "eTreasury  | Pré-validation effective", "Toute la chaine a accepté l'offre pré-sélectionnée relative à la demande de cotation nº "+ offer.getDemandeIdDemande().getNumeroRequest()+" : ", user.getPrenom(), values, "");
			
			}
			
			//Send mail for offer first level validation
			/*List<ValidationLevel> vLevelList = validationLevelRepository.findByProductAndInstitutionAndSensAndAllsRequired(object.getDemandeIdDemande().getProduct(),
					 object.getUserBanqueIdUserBanque().getGroupeIdGroupe().getInstitution(), "select", true);*/
			/*if(!(object.isValid() && validationLevelRepository.existsByProductAndInstitutionAndSensAndAllsRequired(object.getDemandeIdDemande().getProduct(),
					 object.getUserBanqueIdUserBanque().getGroupeIdGroupe().getInstitution(), "select", true)))
			{
				if(validationLevelGroupeRepository.existsByGroupeAndNiveau(groupe, 1) && object.getEtat() < 3)
				{
					//Get next group to validate
					((Offer) object).setNextValidationGroup("Val "+groupe.getNom());
					offerRepository.saveAndFlush((Offer) object);
					
					List<User> userListFirst = userRepository.findByGroupeIdGroupe(groupe);
					String emailSubjectFirst = env.getProperty("notification.offer.pre.validation.email.to.validate.subject");
					String emailBodyFirst = env.getProperty("notification.offer.pre.validation.email.to.validate.body");
					for(User user : userListFirst)
					{
						createNotificationsUser(null, object.getIdOffre(), 12, object.getUserBanqueIdUserBanque().getGroupeIdGroupe().getInstitution().getNom(), user);
						//sendMail(emailFrom, user.getEmail(), emailSubjectFirst, emailBodyFirst);
						List<Value> values= new ArrayList<Value>();
			            values.add(new Value("",((Offer) object).getIdOffre()+""));
						sendMailNew(emailFrom,  user.getEmail(), "eTreasury  | Pré-validation du groupe", "Vous avez reçu sur eTreasury une pré-validation de l'offre: ", user.getPrenom(), values, "Elle est en attente de pré-validation d'un membre de votre groupe.");
					}
				}
			}*/
		}
	}
	
	public void sendMailAllValidatedRefusePreSelection(Offer object) {
		ArrayList<Groupe> groupeList = getGroupeListPreSelection(object);
		String emailFrom = env.getProperty("spring.mail.username");
		for(Groupe groupe:groupeList)
		{
			List<User> userList = userRepository.findByGroupeIdGroupe(groupe);
			for(User user : userList)
			{
				createNotificationsUser(object.getDemandeIdDemande().getIdDemande(), object.getIdOffre(), 16, object.getUserBanqueIdUserBanque().getGroupeIdGroupe().getInstitution().getNom(), user);
				List<Value> values= new ArrayList<Value>();
	            values.add(new Value("",((Offer) object).getIdOffre()+""));
				sendMailNew(emailFrom,  user.getEmail(), "eTreasury  | pré-validation refusée", "Un membre de la chaine a rejeté l'offre relative à la demande de cotation nº "+ object.getDemandeIdDemande().getNumeroRequest()+" : ", user.getPrenom(), values, "");
			}
		}
	}
	
	public void sendMailFirstPreSelection(Offer offer, String emailSubject, String emailBody) {
		ArrayList<Groupe> groupeList = getGroupeListSelect(offer);
		String emailFrom = env.getProperty("spring.mail.username");
		for(Groupe groupe:groupeList)
		{
			List<User> userList = userRepository.findByGroupeIdGroupe(groupe);
			for(User user : userList)
			{
				createNotificationsUser(offer.getDemandeIdDemande().getIdDemande(), offer.getIdOffre(), 11, offer.getDemandeIdDemande().getUserEntreprise().getGroupeIdGroupe().getInstitution().getNom(), user);
				List<Value> values= new ArrayList<Value>();
	            values.add(new Value("",offer.getIdOffre()+""));
				sendMailNew(emailFrom,  user.getEmail(), "eTreasury  | Pré-selection Offre", "Vous avez reçu une pré-sélection de l'offre relative à la demande nº "+ offer.getDemandeIdDemande().getNumeroRequest()+" sur eTreasury : ", user.getPrenom(), values, "Votre groupe fait partie de la chaine de validation. Vous recevrez un email de confirmation après validation des niveaux inférieurs.");
			}
			//ValidationLevelGroupe vlg = validationLevelGroupeRepository.findByProductAndInstitutionAndGroupeFirstLevel(offer.getDemandeIdDemande().getProduct(), offer.getDemandeIdDemande().getUserEntreprise().getGroupeIdGroupe().getInstitution(), groupe, "select");
			ValidationLevel vLevel = validationLevelRepository.findByProductAndInstitutionAndSens(offer.getDemandeIdDemande().getProduct(), offer.getDemandeIdDemande().getUserEntreprise().getGroupeIdGroupe().getInstitution(), "select");
			ValidationLevelGroupe vlg;
			if(vLevel.getAllsRequired())
				vlg = validationLevelGroupeRepository.findByProductAndInstitutionAndGroupeFirstLevel(offer.getDemandeIdDemande().getProduct(), offer.getDemandeIdDemande().getUserEntreprise().getGroupeIdGroupe().getInstitution(), groupe, "select");
			else
				vlg = validationLevelGroupeRepository.findByProductAndInstitutionAndGroupe(offer.getDemandeIdDemande().getProduct(), offer.getDemandeIdDemande().getUserEntreprise().getGroupeIdGroupe().getInstitution(), groupe, "select");
			
			if(vLevel != null && vlg != null && (vLevel.getAllsRequired() || (!vLevel.getAllsRequired() && userService.isMontantAvailable(offer.getDemandeIdDemande(), vlg, deviseRepository))))
			{
				Groupe groupe1 = vlg.getGroupe();
				//Get next group to validate
				offer.setNextValidationGroup("Val "+groupe1.getNom());
				offerRepository.saveAndFlush(offer);
				List<User> userListFirst = userRepository.findByGroupeIdGroupe(groupe1);
				for(User user : userListFirst)
				{
					//Un membre de votre groupe doit valider
					createNotificationsUser(offer.getDemandeIdDemande().getIdDemande(), offer.getIdOffre(), 12, offer.getUserBanqueIdUserBanque().getGroupeIdGroupe().getInstitution().getNom(), user);
					List<Value> values= new ArrayList<Value>();
		            values.add(new Value("",offer.getIdOffre()+""));
					sendMailNew(emailFrom,  user.getEmail(), "eTreasury  | Validation en attente", "Vous avez reçu une pré-sélection de l'offre relative à la demande nº "+ offer.getDemandeIdDemande().getNumeroRequest()+" sur eTreasury : ", user.getPrenom(), values, "Elle est en attente de pré-validation d'un membre de votre groupe.");
				}
			}	
		}
	}
	
	/*public void sendMailToPreSelectedOfferInstitution(Offer offer) {
		String emailFrom = env.getProperty("spring.mail.username");
		Request request = offer.getDemandeIdDemande();
		Products product = request.getProduct();
		List<RequestHasBank> rhbList = rhbRepository.findByRequest(request);
		for(RequestHasBank rhb : rhbList)
		{
			List<Notifications> notificationsList = notificationsRepository.findByBankAndProductAndEnterprise(rhb.getBank(), product.getIdProduits(), request.getUserEntreprise().getGroupeIdGroupe().getInstitution().getIdInstitution());
			for(Notifications notification : notificationsList)
			{
				if(offer.getUserBanqueIdUserBanque().getGroupeIdGroupe().getInstitution().getIdInstitution() == notification.getUser().getGroupeIdGroupe().getInstitution().getIdInstitution())
				{
					User user = notification.getUser();
					Institution bank = offer.getUserBanqueIdUserBanque().getGroupeIdGroupe().getInstitution();
					
					//Offre pré-selectionnée
					createNotificationsUser(request.getIdDemande(), offer.getIdOffre(), 10, bank.getNom(), user);
					
					String emailSubject = env.getProperty("notification.request.email.to.selected.bank.subject");
					//sendMail(emailFrom, user.getEmail(), emailSubject, "Votre offre de cotation relative à la demande nº "+ request.getIdDemande()+" a été pr-éselectionné.");
					System.out.println(emailFrom+ "-"+ user.getEmail()+ "-"+ emailSubject+ "-"+ "Votre offre de cotation relative à la demande nº "+ request.getIdDemande()+" a été pré-sélectionné.");
				}
				else
				{
					User user = notification.getUser();
					Institution bank = offer.getUserBanqueIdUserBanque().getGroupeIdGroupe().getInstitution();
					//New Notification
					createNotificationsUser(request.getIdDemande(), offer.getIdOffre(), 11, bank.getNom(), user);
					
					String emailSubject = env.getProperty("notification.request.email.to.selected.bank.subject");
					//sendMail(emailFrom, user.getEmail(), emailSubject, "Votre offre de cotation relative à la demande nº "+ request.getIdDemande()+" n'a pas été pré-sélectionnée.");
					System.out.println(emailFrom+ "-"+ user.getEmail()+ "-"+ emailSubject+ "-"+ "Votre offre de cotation relative à la demande nº "+ request.getIdDemande()+" n'a pas été pré-sélectionnée.");				
				}
			}
		}
	}*/
	
	public ArrayList<Groupe> getGroupeListPreSelection(Offer object) {
		ArrayList<Groupe> groupeList = new ArrayList<>();
		ValidationLevel vLevel = null;
		vLevel = validationLevelRepository.findByProductAndInstitutionAndSens(object.getDemandeIdDemande().getProduct(), object.getDemandeIdDemande().getUserEntreprise().getGroupeIdGroupe().getInstitution(), "select");
		List<ValidationLevelGroupe> vLevelGroupe = validationLevelGroupeRepository.findByValidationLevel(vLevel);
		for(ValidationLevelGroupe vlg:vLevelGroupe)
		{
			Groupe groupe = vlg.getGroupe();
			groupeList.add(groupe);
		}
		return groupeList;
	}
	
	public ArrayList<Groupe> getGroupeListSelect(Offer object) {
		ArrayList<Groupe> groupeList = new ArrayList<>();
		ValidationLevel vLevel = null;
		vLevel = validationLevelRepository.findByProductAndInstitutionAndSens(object.getDemandeIdDemande().getProduct(), object.getDemandeIdDemande().getUserEntreprise().getGroupeIdGroupe().getInstitution(), "select");
		
		List<ValidationLevelGroupe> vLevelGroupe = validationLevelGroupeRepository.findByValidationLevel(vLevel);
		for(ValidationLevelGroupe vlg:vLevelGroupe)
		{
			Groupe groupe = vlg.getGroupe();
			groupeList.add(groupe);
		}
		return groupeList;
	}
}
