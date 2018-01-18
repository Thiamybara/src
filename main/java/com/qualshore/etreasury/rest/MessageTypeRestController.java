package com.qualshore.etreasury.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.qualshore.etreasury.dao.MessageTypeRepository;
import com.qualshore.etreasury.entity.MessageType;

@RestController
public class MessageTypeRestController {

	@Autowired
	MessageTypeRepository messageTypeRepository;
	
	@RequestMapping(value = "/etreasury_project/message_type/add", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> addMessageType(@RequestParam("libelle_type") String libelleType,
									         @RequestParam("description") String description) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(libelleType.equals("") || description.equals(""))
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs parametres manquants");
		}
		else
		{
			MessageType messageType = new MessageType(libelleType, description);
			MessageType result = messageTypeRepository.save(messageType);
			if(result == null)
			{
				h.put("status", -1);
				h.put("message", "Erreur ajout");
			}
			else
			{
				h.put("status", 0);
				h.put("message", "ajout OK");
			}
		}
		return h;
	}
	
	@RequestMapping(value = "/etreasury_project/message_type/update", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> updateMessageType(@RequestParam("id_type_message") Integer idTypeMessage,
												@RequestParam("libelle_type") String libelleType,
	         									@RequestParam("description") String description) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(idTypeMessage == null || libelleType.equals("") || description.equals(""))
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs parametres manquants");
		}
		else if(!messageTypeRepository.existsByIdTypeMessage(idTypeMessage))
		{
			h.put("status", -1);
			h.put("message", "Le messageType n'existe pas");
		}
		else
		{
			int result = messageTypeRepository.updateMessageType(libelleType, description, idTypeMessage);
			if(result < 0)
			{
				h.put("status", -1);
				h.put("message", "Erreur lors de la modification");
			}
			else
			{
				h.put("status", 0);
				h.put("message", "La modification d'est effectuée avec succès.");
			}
		}
		return h;
	}
	
	@RequestMapping(value = "/etreasury_project/message_type/delete", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> deleteMessageType(@RequestParam("id_type_message") Integer idTypeMessage) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(idTypeMessage == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs parametres manquants");
		}
		else if(!messageTypeRepository.existsByIdTypeMessage(idTypeMessage))
		{
			h.put("status", -1);
			h.put("message", "Le messageType n'existe pas.");
		}
		else
		{
			messageTypeRepository.delete(idTypeMessage);
			h.put("status", 0);
			h.put("message", "Le message est bien supprimé.");
		}
		return h;
	}
	
	@RequestMapping(value = "/etreasury_project/message_type/list", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> listMessageType() {
		HashMap<String, Object> h = new HashMap<String, Object>();
		h.put("status", 0);
		h.put("user_list", messageTypeRepository.findAll());
		h.put("message", "list OK");
		return h;
	}
}
