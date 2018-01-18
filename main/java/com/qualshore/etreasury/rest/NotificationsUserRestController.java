package com.qualshore.etreasury.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.qualshore.etreasury.dao.InstitutionRepository;
import com.qualshore.etreasury.dao.NotificationsUserRepository;
import com.qualshore.etreasury.dao.OfferRepository;
import com.qualshore.etreasury.dao.RequestRepository;
import com.qualshore.etreasury.dao.UserRepository;
import com.qualshore.etreasury.entity.Institution;
import com.qualshore.etreasury.entity.NotificationsUser;
import com.qualshore.etreasury.entity.User;
//import com.qualshore.etreasury.entity.ValidationOffer;

@RestController
@RequestMapping("/etreasury_project/notifications_user")
public class NotificationsUserRestController {

	@Autowired
	NotificationsUserRepository notificationsUserRep;
	
	@Autowired
	UserRepository userRep;
	
	@Autowired
	RequestRepository requestRep;
	
	@Autowired
	OfferRepository offerRep;
	
	@Autowired
	InstitutionRepository institutionRep;
	
	/*
	 * Get all notifications for user
	 */
	@RequestMapping(value = "/list/{idUser}/{idInstitution}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object> listNotifications(@PathVariable Integer idUser, @PathVariable Integer idInstitution) {
	
		HashMap<String, Object> h = new HashMap<String, Object>();
		List<NotificationsUser> notificationsUserList;
		if(idUser == null || idInstitution == null)
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
			Institution institution = institutionRep.findOne(idInstitution);
			if (institution == null)
			{
				h.put("message", "L'institution n'existe pas.");
				h.put("status", -1);
				return h;
			}
			if(user.getGroupeIdGroupe().getInstitution().getIdInstitution() != idInstitution)
			{
				h.put("message", "L'utilisateur n'est pas de cette institution.");
				h.put("status", -1);
				return h;
			}
			notificationsUserList = notificationsUserRep.findByUser(user);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("notifications_list", notificationsUserList);
		h.put("message", "liste des notifications chargée avec succès.");
		h.put("status", 0);
		return h;
	}
	
	/*
	 * Delete Notification ?User
	 */
	@RequestMapping(value = "/delete/{idNotificationUser}/{idUser}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object> deleteNotification(@PathVariable Integer idNotificationUser, @PathVariable Integer idUser) {
	
		HashMap<String, Object> h = new HashMap<String, Object>();
		List<NotificationsUser> notificationsUserList;
		if(idNotificationUser == null || idUser == null)
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
			NotificationsUser notificationUser = notificationsUserRep.findOne(idNotificationUser);
			if (notificationUser == null)
			{
				h.put("message", "La notification n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			//NotificationsUser notificationsUser = 
			notificationsUserRep.delete(idNotificationUser);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "La notification est supprimée avec succès.");
		h.put("status", 0);
		return h;
	}
}
