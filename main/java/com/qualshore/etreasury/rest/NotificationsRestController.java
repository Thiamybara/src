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

import com.qualshore.etreasury.dao.EntrepriseRepository;
import com.qualshore.etreasury.dao.InstitutionRepository;
import com.qualshore.etreasury.dao.NotificationsRepository;
import com.qualshore.etreasury.dao.ProductsRepository;
import com.qualshore.etreasury.dao.UserBankRepository;
import com.qualshore.etreasury.dao.UserEntrepriseRepository;
import com.qualshore.etreasury.dao.UserRepository;
import com.qualshore.etreasury.entity.Enterprise;
import com.qualshore.etreasury.entity.Institution;
import com.qualshore.etreasury.entity.Notifications;
import com.qualshore.etreasury.entity.NotificationsPK;
import com.qualshore.etreasury.entity.Products;
import com.qualshore.etreasury.entity.User;
import com.qualshore.etreasury.model.NotificationsModel;
import com.qualshore.etreasury.service.UserService;

@RequestMapping("/etreasury_project/validation/notifications")
@RestController
public class NotificationsRestController {
	@Autowired
	EntrepriseRepository eRep;
	@Autowired
	UserEntrepriseRepository uERep;
	@Autowired
	UserBankRepository uBRep;
	@Autowired
	InstitutionRepository insRep;
	@Autowired
	ProductsRepository pRep;
	@Autowired
	NotificationsRepository nRep;
	@Autowired
	UserService userService;
	@Autowired
	UserRepository uRep;
	
	
	//List des utilisateurs a notifier par instituttion
	@RequestMapping(value="/list/{idUser}/{idInstitution}", method=RequestMethod.GET)
	public HashMap<String, Object> getAllNotifications(@PathVariable Integer idUser, @PathVariable Integer idInstitution) {
		
		HashMap<String, Object> h= new HashMap<String, Object>();
		List<Notifications> notificationList;
		if(idUser == null ||  idInstitution == null )
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		try
		{
			User user = uRep.findOne(idUser);
			if (user == null)
			{
				h.put("message", "L'utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			
			Institution institution = insRep.findOne(idInstitution);
			if (institution == null)
			{
				h.put("message", "L'institution n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			// notificationList = nRep.findByInstitution(institution);
			notificationList = nRep.findByInstitutionUser(institution);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "success");
		h.put("notification", notificationList);
		h.put("status", 0);
		return h;
	}
	
	//List des users a notifier par banque et par produit
	

	@RequestMapping(value="/list_notification_users/{idUser}/{idEnterprise}/{idProduct}", method=RequestMethod.GET)
	public HashMap<String, Object> getAllNotificationsProduct(@PathVariable Integer idUser,
															  @PathVariable Integer idEnterprise,
															  @PathVariable Integer idProduct) {
		
		HashMap<String, Object> h= new HashMap<String, Object>();
		List<Notifications> notificationList;
		if(idUser == null ||  idEnterprise == null || idProduct == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		try
		{
			User user = uRep.findOne(idUser);
			if (user == null)
			{
				h.put("message", "L'utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			Institution institution = insRep.findOne(user.getGroupeIdGroupe().getInstitution().getIdInstitution());
			if (institution == null)
			{
				h.put("message", "L'institution n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			Enterprise enterprise = eRep.findOne(idEnterprise);
			if (enterprise == null)
			{
				h.put("message", "L'entreprise n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			Products products = pRep.findOne(idProduct);
			if (products == null) {
				h.put("message", "Le produit n'existe pas.");
				h.put("status", -1);
				return h;
			}
			// notificationList = nRep.findByBankAndProductAndEnterprise(institution, products.getIdProduits(),enterprise.getIdInstitution());
			 notificationList = nRep.findByBankAndProductAndEnterprise( products.getIdProduits(),institution.getIdInstitution());
				
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "Liste des utilisateurs a notifier dans la banque");
		h.put("notification", notificationList);
		h.put("status", 0);
		return h;
	}
	
	
	@RequestMapping(value="/add/{idUser}/{idInstitution}", method=RequestMethod.POST)
	public HashMap<String, Object> add(@RequestBody NotificationsModel notificationModel, @PathVariable Integer idUser, @PathVariable Integer idInstitution) {
		System.out.println("11111");
		//Products products;
		HashMap<String, Object> h= new HashMap<String, Object>();
		if(idUser == null || notificationModel == null || idInstitution == null )
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		System.out.println("22222");
		try
		{
			/*
			if(userService.isAdminEntreprise(idUser))
			{
				h.put("status", -1);
				h.put("message", "vous n'avez pas le profile pour effectuer cette action");
				return h;
			}
			*/
			User user = uRep.findOne(idUser);
			if (user == null)
			{
				h.put("message", "L'utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			System.out.println("333333");
			Products products = pRep.findOne(notificationModel.getIdProduct());
			if (products == null) {
				h.put("message", "Le produit n'existe pas.");
				h.put("status", -1);
				return h;
			}
			Institution institution = insRep.findOne(idInstitution);
			if (institution == null) {
				h.put("message", "L'institution n'existe pas.");
				h.put("status", -1);
				return h;
			}
			//Verifier l'unicite entreprise / produits / utilisateurs
			if(user.getDiscriminatorValue().equals("BA") && !institution.getDiscriminatorValue().equals("EN")) {
				
				h.put("message", "Veuillez sélectionner une entreprise.");
				h.put("status", -1);
				return h;
			}
			else if(user.getDiscriminatorValue().equals("EN") && institution.getDiscriminatorValue().equals("EN")) 
				
				{
					if(user.getGroupeIdGroupe().getInstitution().getIdInstitution() != institution.getIdInstitution()) {
					
					h.put("message", "Vous  n'êtes pas de cette institution.");
					h.put("status", -1);
					return h;
					}	
				}
			//Store documents in DocumentsHasOffer
			if(notificationModel.getIdsUser() != null) 
			{
				addNotifications((notificationModel.getIdProduct()), notificationModel.getIdsUser(), institution.getIdInstitution());
			}
			//journaliser(idUserBank, 6, offer.getIdOffre());
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		//h.put("notific", offer);
		h.put("message", "La notification est ajoutée avec succès.");
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value="/update/{idUser}/{idUserOld}", method=RequestMethod.PUT)
	public HashMap<String, Object> update(@RequestBody Notifications notification, @PathVariable Integer idUser, @PathVariable Integer idUserOld) {
		HashMap<String, Object> h= new HashMap<String, Object>();
		if(idUser == null || notification == null || idUserOld == null )
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		try
		{
			if(notification.getProducts() == null || notification.getProducts().getIdProduits() == null ||
			   notification.getInstitution() == null || notification.getInstitution().getIdInstitution() == null ||
			   notification.getUser() == null || notification.getUser().getIdUtilisateur() == null)
			{
				h.put("status", -1);
				h.put("message", "1 ou plusieurs paramètres vides");
				return h;
			}
			User user = uRep.findOne(idUser);
			if (user == null)
			{
				h.put("message", "L'utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			User userOld = uRep.findOne(idUserOld);
			if (userOld == null)
			{
				h.put("message", "L'utilisateur associé à cette notification n'existe pas.");
				h.put("status", -1);
				return h;
			}
			Products product = pRep.findOne(notification.getProducts().getIdProduits());
			if (product == null)
			{
				h.put("message", "Le produit associé à cette notification n'existe pas.");
				h.put("status", -1);
				return h;
			}
			Institution institution = insRep.findOne(notification.getInstitution().getIdInstitution());
			if (institution == null)
			{
				h.put("message", "L'institution associée à cette notification n'existe pas.");
				h.put("status", -1);
				return h;
			}
			User userNew = uRep.findOne(notification.getUser().getIdUtilisateur());
			if (userNew == null)
			{
				h.put("message", "Le nouveau utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			if(userNew.getGroupeIdGroupe().getInstitution().getIdInstitution() != institution.getIdInstitution())
			{
				h.put("message", "Le nouveau utilisateur n'est pas de cette institution.");
				h.put("status", -1);
				return h;
			}
			
			if (userOld.getIdUtilisateur() != userNew.getIdUtilisateur()) {
				Notifications notif = nRep.findByNotification(institution.getIdInstitution(), product.getIdProduits(),userNew.getIdUtilisateur());
                if(notif != null)
                {
                    h.put("message", "Cet utilisateur existe déja pour le même produit et la même entreprise.");
                    h.put("status", -1);
                    return h;
                }
            }
			
			Notifications notificationOld = nRep.findByProductsAndInstitutionAndUser(product, institution, userOld);
			if (notificationOld == null)
			{
				h.put("message", "Cette notification n'existe pas.");
				h.put("status", -1);
				return h;
			}
			notification.setUser(userNew);
			notification.setDate(notificationOld.getDate());
			notification.setNotificationsPK(new NotificationsPK(product.getIdProduits(), userNew.getIdUtilisateur(),institution.getIdInstitution()));
			
			nRep.delete(notificationOld);
			notification = nRep.save(notification);
			
			//journaliser(idUserBank, 6, offer.getIdOffre());
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("notification", notification);
		h.put("message", "La notification est mise à jour avec succès.");
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value="/delete/{idAdmin}/{idProduct}/{idUser}/{idInstitution}", method = RequestMethod.DELETE)
	public HashMap<String, Object> deleteUserNotification(@PathVariable Integer idAdmin, @PathVariable Integer idProduct,
			@PathVariable Integer idUser, @PathVariable Integer idInstitution){
		
		HashMap<String, Object> h = new HashMap<>();
		//List<Notifications> notificationList;
		if(idUser == null ||  idInstitution == null  || idAdmin == null ||  idProduct  == null )
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
	
		try {
			User user = uRep.findOne(idAdmin);
			if (user == null)
			{
				h.put("message", "L'utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			Products products = pRep.findOne(idProduct);
			if (products == null) {
				h.put("message", "Le produit n'existe pas.");
				h.put("status", -1);
				return h;
			}
			Institution institution = insRep.findOne(idInstitution);
			if (institution == null) {
				h.put("message", "L'institution n'existe pas.");
				h.put("status", -1);
				return h;
			}
			User users = uRep.findOne(idUser);
			if (users == null)
			{
				h.put("message", "L'utilisateur associé à la notification n'existe pas.");
				h.put("status", -1);
				return h;
			}
			Notifications nPk = nRep.findByProductsAndInstitutionAndUser(products, institution, users);
			if (nPk == null)
			{
				h.put("message", "Les données associées à la notification n'existent pas.");
				h.put("status", -1);
				return h;
			}
			
			nRep.delete(nPk); 
			//vLGRep.delete(vLG);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "success");
		h.put("status", 0);
		return h;
	}
	
	
	public void addNotifications(Integer idProduct, Integer[] tabUser,Integer idInstitution) {
		Products product = pRep.findOne(idProduct);
		Institution institution = insRep.findOne(idInstitution);
		if(product != null && institution != null )
		{
			System.out.println("notification size "+tabUser.length);
			for(int i = 0; i < tabUser.length; i++)
			{
				User user = uRep.findOne(tabUser[i]);
				if(user != null)
				{
					Notifications notifications = new Notifications();
					notifications.setProducts(product);
					notifications.setUser(user);
					notifications.setDate(new Date());
					notifications.setNotificationsPK(new NotificationsPK(product.getIdProduits(), user.getIdUtilisateur(),institution.getIdInstitution()));
					notifications = nRep.save(notifications);
				}
			}
		}
	}
}
