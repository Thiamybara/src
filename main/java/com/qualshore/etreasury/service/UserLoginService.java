package com.qualshore.etreasury.service;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.qualshore.etreasury.dao.ActionRepository;
import com.qualshore.etreasury.dao.LogRepository;
import com.qualshore.etreasury.dao.UserRepository;
import com.qualshore.etreasury.entity.Log;
import com.qualshore.etreasury.entity.User;
import com.qualshore.etreasury.mail.EmailService;
import com.qualshore.etreasury.mail.Mail;
import com.qualshore.etreasury.mail.Value;

@Service
public class UserLoginService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	JavaMailSender mailSender;
	
	@Autowired
	LogRepository logRepository;
	@Autowired
	EmailService emailService;
	
	@Autowired
	ActionRepository actionRepository;
	
	public void journaliser(Integer idUser, Integer idAction, String description) {
		Log log = new Log();
		log.setIdUser(idUser);
		log.setDate(new Date());
		log.setDescription(description);
		//log.setAncienneValeur(logRepository.findLastLogById(idLog));
		log.setActionIdAction(actionRepository.findOne(idAction));
		logRepository.save(log);
	}
	
	public boolean isAuthenticated(String login, String password){
		if(!userRepository.existsByLogin(login) || !userRepository.existsByPassword(password)) {
			return false;
		}
		return true;
	}
	public boolean isAuthenticatedLogin(String login, String password){
		if(!userRepository.existsByLogin(login) && !userRepository.existsByPassword(password)) {
			return false;
		}
		return true;
	}
	public User findOne(Integer id){
		return userRepository.findOne(id);
	}
	
	public User findByLogin(String login){
		return userRepository.findByLogin(login);
	}
	
	public String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 7) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
	
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
	public long tempsRestant(Date dateFin)
	{
		long result = 0;
		try {
			result = (dateFin.getTime() - new Date().getTime()) / 60000;
			System.out.print("TEMPS RESTANT  "+result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}	
	
	public com.qualshore.etreasury.entity.Session getLModel(String login, String token) {
		Date dateDebut = new Date();
		Date dateFin = new Date();
		dateFin.setTime(dateFin.getTime() + 10*30000);
		com.qualshore.etreasury.entity.Session session = new com.qualshore.etreasury.entity.Session();
		session.setDateDebut(dateDebut);
		session.setDateFin(dateFin);
		session.setIsActive(true);
		session.setToken(token);
		session.setUserIdUtilisateur(userRepository.findByLogin(login));
		return session;
	}
	
	public boolean isAuthenticatedEmail(String login){
        if(!userRepository.existsByEmail(login) ) {
            return false;
        }
        return true;
    }
    public boolean isAuthenticatedPassword(String login){
        if(!userRepository.existsByPassword(login) ) {
            return false;
        }
        return true;
    }
    
    public List<User> findByEmail(String login){
        return userRepository.findByEmail(login);
    }
    
    public String getPasswordString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789$&@?<>~!%#";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 9) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
    
    public  String encryptData(String data) {
        //   String data = "Hello World";
           MessageDigest md = null;
           try {
               md = MessageDigest.getInstance("MD5");
           } catch (NoSuchAlgorithmException e) {
               e.printStackTrace();
           }
           if (md == null) {
               return null;
           }
           md.update(data.getBytes());
           String dataEncoded = Base64.getEncoder().encodeToString(md.digest());
           return dataEncoded;
       }
}
