package com.qualshore.etreasury.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.qualshore.etreasury.dao.MessageRepository;
import com.qualshore.etreasury.dao.MessageTypeRepository;
import com.qualshore.etreasury.dao.UserRepository;
import com.qualshore.etreasury.entity.Message;
import com.qualshore.etreasury.entity.MessageType;
import com.qualshore.etreasury.entity.User;

@RequestMapping("etreasury_project/message")

@RestController
public class MessageRestController {

	@Autowired
	MessageRepository messageRepository;
	
	@Autowired
	MessageTypeRepository messageTypeRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@RequestMapping(value = "/add/{idUser}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> addMessage(@RequestBody Message message, @PathVariable Integer idUser) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(message == null || idUser == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs parametres manquants");
		}
		try
		{
			User user = userRepository.findOne(idUser);
			if(user == null)
			{
				h.put("message", "L'utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			MessageType messageType = messageTypeRepository.findOne(message.getTypeMessageIdTypeMessage().getIdTypeMessage());
			if(messageType == null)
			{
				h.put("message", "Le type de message n'existe pas.");
				h.put("status", -1);
				return h;
			}
			message.setUser(user);
			message.setTypeMessageIdTypeMessage(messageType);
			message = messageRepository.save(message);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", message);
		h.put("status", 0);
		h.put("message", "Le message est ajouté avec succès.");
		return h;
	}
	
	@RequestMapping(value = "/delete/{idUser}/{idMessage}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> deleteMessage(@PathVariable Integer idUser, @PathVariable Integer idMessage) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(idUser == null || idMessage == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs parametres manquants");
		}
		try
		{
			User user = userRepository.findOne(idUser);
			if(user == null)
			{
				h.put("message", "L'utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			Message message = messageRepository.findOne(idMessage);
			if(message == null)
			{
				h.put("message", "Le message n'existe pas.");
				h.put("status", -1);
				return h;
			}
			messageRepository.delete(idMessage);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("status", 0);
		h.put("message", "Le message est supprimé avec succès.");
		return h;
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> listMessage() {
		HashMap<String, Object> h = new HashMap<String, Object>();
		List<Message> listMessage;
		try
		{
			listMessage = messageRepository.findAll();
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("list_message", listMessage);
		h.put("message", "Liste des messages.");
		h.put("status", 0);
		return h;
	}
	
	public Date getDateString(String dateInString) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.parse(dateInString);
	}
}