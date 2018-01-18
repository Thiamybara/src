package com.qualshore.etreasury.service;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.qualshore.etreasury.dao.ActionRepository;
import com.qualshore.etreasury.dao.DeviseRepository;
import com.qualshore.etreasury.dao.InstitutionRepository;
import com.qualshore.etreasury.dao.LogRepository;
import com.qualshore.etreasury.dao.UserRepository;
import com.qualshore.etreasury.entity.Devise;
import com.qualshore.etreasury.entity.Groupe;
import com.qualshore.etreasury.entity.Institution;
import com.qualshore.etreasury.entity.Log;
import com.qualshore.etreasury.entity.Profile;
import com.qualshore.etreasury.entity.Request;
import com.qualshore.etreasury.entity.User;
import com.qualshore.etreasury.entity.ValidationLevelGroupe;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	InstitutionRepository institutionRepository;
	
	@Autowired
	LogRepository logRepository;
	
	@Autowired
	ActionRepository actionRepository;
	
	@Autowired
	JavaMailSender mailSender;
	
	public void sendMail(String from, String to, String subject, String body) {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setFrom(from);
		mail.setTo(to);
		mail.setSubject(subject);
		mail.setText(body);
		mailSender.send(mail);
	}
	
	public void journaliser(Integer idUser, Integer idAction, String description) {
		Log log = new Log();
		log.setIdUser(idUser);
		log.setDate(new Date());
		log.setDescription(description);
		//log.setAncienneValeur(logRepository.findLastLogById(idLog));
		log.setActionIdAction(actionRepository.findOne(idAction));
		
		logRepository.save(log);
	}
	
	public User getUserById(Integer idUser) {
		return userRepository.findOne(idUser);
	}
	
	public Groupe getGroupeById(Integer idUser) {
		return getUserById(idUser).getGroupeIdGroupe();
	}
	
	public Profile getProfileById(Integer idUser) {
		return getUserById(idUser).getProfilIdProfil();
	}
	
	public Institution getInstitutionOfUser(Integer idUser) {
		return getUserById(idUser).getGroupeIdGroupe().getInstitution();
	}
	
	public boolean isUserAdminGeneral(Integer idUser) {
		User user = userRepository.findOne(idUser);
		if(user == null)
			return false;
		return user.getDiscriminatorValue().equals("ET");
	}
	
	public boolean isUserBanque(Integer idUser) {
		User user = userRepository.findOne(idUser);
		if(user == null)
			return false;
		if(!user.getDiscriminatorValue().equals("BA"))
			return false;
		return true;
	}
	
	public boolean isUserEntreprise(Integer idUser) {
		User user = userRepository.findOne(idUser);
		if(user == null)
			return false;
		if(!user.getDiscriminatorValue().equals("EN"))
			return false;
		return true;
	}
	
	public boolean isProfileValid(Integer idUser) {
		if(getProfileById(idUser) == null)
			return false;
		return true;
	}
	
	public boolean isAdminGeneral(Integer idUser) {
		if(!isUserAdminGeneral(idUser))
			return false;
		if(!isProfileValid(idUser))
			return false;
		if(getProfileById(idUser).getType().equals("SUPER_ADMIN"))
			return true;
		return false;
	}
	
	public boolean isAdminBanque(Integer idUser) {
		if(!isUserBanque(idUser))
			return false;
		if(isProfileValid(idUser))
			return false;
		if(!getProfileById(idUser).getType().equals("ADMIN_BANQUE"))
			return false;
		return true;
	}
	
	public boolean isAdminEntreprise(Integer idUser) {
		if(!isUserEntreprise(idUser))
			return false;
		if(isProfileValid(idUser))
			return false;
		if(!getProfileById(idUser).getType().equals("ADMIN_ENTREPRISE"))
			return false;
		return true;
	}
	
	public boolean isInstitutionValid(Integer idInstitution) {
		Institution institution = institutionRepository.findOne(idInstitution);
		if(institution == null)
			return false;
		return true;
	}
	
	public boolean isSameInstitution(Integer idUser1, Integer idUser2) {
		Institution iUser1 = getInstitutionOfUser(idUser1);
		Institution iUser2 = getInstitutionOfUser(idUser2);
		if(iUser1 != null && iUser2 != null && iUser1.getIdInstitution().equals(iUser2.getIdInstitution()))
		{
			return true;
		}
		return false;
	}
	
	public boolean isPhoneValid(String number) {
        String phone_pattern = "^\\+(?:[0-9-]?){6,14}[0-9]$";
        Pattern pattern = Pattern.compile(phone_pattern);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }
	
	public boolean isEmailValid(String email) {
    	String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    	Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    	Matcher matcher = pattern.matcher(email);
		return matcher.matches();
    }
	
	public boolean isMontantAvailable(Request request, ValidationLevelGroupe vLevelGroup, DeviseRepository deviseRepository) {
		Devise deviseProduct = deviseRepository.findOne(request.getDevise().getIdDevise());
		if(deviseProduct == null)
		{
			return false;
		}
		Devise deviseChain = deviseRepository.findOne(vLevelGroup.getDevise().getIdDevise());
		if(deviseChain == null)
		{
			return false;
		}
		Float montant = Float.parseFloat(request.getMontant()) * deviseProduct.getValeur();
		Float valeur_max = Float.parseFloat(vLevelGroup.getValeurMax()) * deviseChain.getValeur();
		Float valeur_min = Float.parseFloat(vLevelGroup.getValeurMin()) * deviseChain.getValeur();
		if(valeur_min <= montant && montant <= valeur_max)
		{
			return true;
		}
		return false;
	}
}
