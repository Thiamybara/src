package com.qualshore.etreasury.rest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.qualshore.etreasury.dao.ActionRepository;
import com.qualshore.etreasury.dao.BankRepository;
import com.qualshore.etreasury.dao.CommissionOfferRepository;
import com.qualshore.etreasury.dao.DocumentHasOfferRepository;
import com.qualshore.etreasury.dao.DocumentsRepository;
import com.qualshore.etreasury.dao.GroupRepository;
import com.qualshore.etreasury.dao.LogRepository;
import com.qualshore.etreasury.dao.NotificationsUserRepository;
import com.qualshore.etreasury.dao.OfferRepository;
import com.qualshore.etreasury.dao.ProductsRepository;
import com.qualshore.etreasury.dao.RequestHasBankRepository;
import com.qualshore.etreasury.dao.RequestRepository;
import com.qualshore.etreasury.dao.UserBankRepository;
import com.qualshore.etreasury.dao.UserRepository;
import com.qualshore.etreasury.dao.ValidationLevelRepository;
import com.qualshore.etreasury.entity.Bank;
import com.qualshore.etreasury.entity.CommissionOffer;
import com.qualshore.etreasury.entity.Documents;
import com.qualshore.etreasury.entity.DocumentsHasOffer;
import com.qualshore.etreasury.entity.DocumentsHasOfferPK;
import com.qualshore.etreasury.entity.Groupe;
import com.qualshore.etreasury.entity.Institution;
import com.qualshore.etreasury.entity.Log;
import com.qualshore.etreasury.entity.Offer;
import com.qualshore.etreasury.entity.Products;
import com.qualshore.etreasury.entity.Request;
import com.qualshore.etreasury.entity.RequestHasBank;
import com.qualshore.etreasury.entity.User;
import com.qualshore.etreasury.entity.ValidationLevel;
import com.qualshore.etreasury.model.OfferModel;
import com.qualshore.etreasury.service.ValidationService;

@RequestMapping("etreasury_project/mon_espace/bank/request/offer")
@RestController
public class OfferRestController {

	@Autowired
	OfferRepository offerRepository;
	
	@Autowired
	UserBankRepository userBankRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RequestRepository requestRepository;
	
	@Autowired
	BankRepository bankRepository;
	
	@Autowired
	LogRepository logRepository;
	
	@Autowired
	ActionRepository actionRepository;
	
	@Autowired
	DocumentsRepository documentsRepository;
	
	@Autowired
	CommissionOfferRepository commissionOfferRepository;
	
	@Autowired
	DocumentHasOfferRepository dhoRepository;
	
	@Autowired
	ValidationService validationService;
	
	@Autowired
	Environment env;
	
	@Autowired
	RequestHasBankRepository rhbRep;
	
	@Autowired
	ProductsRepository productRep;
	
	@Autowired
	ValidationLevelRepository validationLevelRep;
	
	@Autowired
	GroupRepository groupeRep;
	
	@Autowired
	NotificationsUserRepository notificationsUserRep;
	
	//List<CommissionOffer> commissionList = new ArrayList<>();
	
	// All offer's list
	@RequestMapping(value = "/list/all", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> listAll()
	{
		HashMap<String, Object> h = new HashMap<String, Object>();
		List<Offer> offerList;
		try
		{
			offerList = offerRepository.findAll();
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("status", 0);
		h.put("offer_list", offerList);
		h.put("message", "success");
		return h;
	}
	
	// Offer's list by user
	@RequestMapping(value = "/list/user_banque/{idUserBank}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> list(@PathVariable Integer idUserBank)
	{
		HashMap<String, Object> h = new HashMap<String, Object>();
		List<Offer> offerList;
		if(idUserBank == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		try
		{
			User user = userBankRepository.findOne(idUserBank);
			if(user == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'existe pas et/ou n'est pas d'une banque.");
				return h;
			}
			
			offerList = offerRepository.findByUserBanqueIdUserBanque(user);
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("status", 0);
		h.put("offer_list", offerList);
		h.put("message", "success");
		return h;
	}
	
	/*// Offer's list by request
	@RequestMapping(value = "/list_offer_by_request/{idRequest}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> listByRequest(@PathVariable Integer idRequest)
	{
		HashMap<String, Object> h = new HashMap<String, Object>();
		List<Offer> offerList;
		if(idRequest == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		try
		{
			Request request = requestRepository.findOne(idRequest);
			if(request == null)
			{
				h.put("status", -1);
				h.put("message", "cette demande n'existe pas");
				return h;
			}
			
			offerList = offerRepository.findByDemandeIdDemandeAndIsValid(request,true);
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("status", 0);
		h.put("offer_list", offerList);
		h.put("message", "success");
		return h;
	}*/
	
	// Offer's list by bank
	@RequestMapping(value = "/list/banque/{idBank}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> listByBank(@PathVariable Integer idBank)
	{
		HashMap<String, Object> h = new HashMap<String, Object>();
		List<Offer> offerList;
		if(idBank == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		try
		{
			Bank bank = bankRepository.findOne(idBank);
			if(bank == null)
			{
				h.put("status", -1);
				h.put("message", "Cette banque n'existe pas.");
				return h;
			}
			
			offerList = offerRepository.findByBank(bank);
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("status", 0);
		h.put("offer_list", offerList);
		h.put("message", "success");
		return h;
	}
	
	// Add offer
	@RequestMapping(value = "/add/{idUserBank}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> add(@RequestBody OfferModel offerModel, @PathVariable Integer idUserBank) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		Offer offer;
		if(idUserBank == null || offerModel == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		
		try
		{
			offer = offerModel.getOffer();
			if(offer == null)
			{
				h.put("status", -1);
				h.put("message", "Cette offre n'existe pas.");
				return h;
			}
			
			User user = userBankRepository.findOne(idUserBank);
			if(user == null)
			{
				h.put("status", -1);
				h.put("message", "cet utilisateur n'existe pas et/ou n'est pas d'une banque.");
				return h;
			}
			Request request = requestRepository.findOne(offer.getDemandeIdDemande().getIdDemande());
			if(request == null)
			{
				h.put("status", -1);
				h.put("message", "La demande associée à l'offre n'existe pas.");
				return h;
			}
			
			/*if(request.getDateFinDemande().compareTo(lendemain()) >= 0)
			{
                h.put("status", -1);
                h.put("message", "la demande a été cloturé");
                return h;
            }*/
            List<Offer> offers = request.getOfferList();
            Offer result = offers.stream()
                    .filter(of -> "3".equals(of.getDemandeIdDemande().getEtat()))
                    .findAny()
                    .orElse(null);
            
            if (result != null) {
                h.put("status", -1);
                h.put("message", "Toutes les offres ont été positionnées pour cette demande.");
                return h;
            }
            
			Products product = productRep.findOne(request.getProduct().getIdProduits());
			if (product == null)
			{
				h.put("message", "Le produit associé à l'offre n'existe pas.");
				h.put("status", -1);
				return h;
			}
			User userOffer = userBankRepository.findOne(offer.getUserBanqueIdUserBanque().getIdUtilisateur());
			if(userOffer == null)
			{
				h.put("status", -1);
				h.put("message", "L'utilisateur associé à l'offre n'existe pas.");
				return h;
			}
			Institution bank = bankRepository.findOne(userOffer.getGroupeIdGroupe().getInstitution().getIdInstitution());
			if(bank == null)
			{
				h.put("message", "La banque de l'utilisateur n'existe pas.");
				h.put("status", -1);		
				return h;
			}
			if(offerRepository.existsByRequestAndInstitutionAndProduct(request, bank, product))
            {
                h.put("status", -1);
                h.put("message", "Une offre est déjà positionnée avec cette demande et ce produit.");
                return h;
            }
			//Verificatfion si le niveau de validation existe pour l'institution et le produit et la chaine est valide
			ValidationLevel validationLevel = validationLevelRep.findByProductAndInstitutionAndSens(product, bank, "offer");
			if(validationLevel == null)
			{
				h.put("message", "Aucune chaine de validation n'est définie pour ce produit, veuillez contacter l'administrateur.");
				h.put("status", -1);		
				return h;
			}
			
			if(! validationLevel.getIsValidChain())
			{
				h.put("message", "La chaine de validation n'a pas été définie pour ce produit.");
				h.put("status", -1);		
				return h;
			}
			//
			
			offer.setDate(new Date());
			offer.setValid(false);
			offer.setEtat(0);
			offer.setNextValidationGroup("");
			offer.setDemandeIdDemande(request);
			offer.setUserBanqueIdUserBanque(userOffer);
			offer = offerRepository.save(offer);
			
			//Store documents in DocumentsHasOffer
			if(offerModel.getDocuments() != null) 
			{
				addDocuments((offerModel.getOffer()).getIdOffre(), offerModel.getDocuments());
			}
			
			//Store commissions in commissionList
			if(offerModel.getCommissions() != null) 
			{
				addCommissions(offerModel.getOffer().getIdOffre(), offerModel.getCommissions());
			}
			
			journaliser(idUserBank, 6, offer.getIdOffre());
			
			/*String emailSubject = env.getProperty("notification.offer.email.subject");
			String emailBody = env.getProperty("notification.offer.email.body");*/
			validationService.sendMailFirst(offer);
			
			//Update requestHasBank
            RequestHasBank rhb = rhbRep.findByBankAndRequest(userOffer.getGroupeIdGroupe().getInstitution(), offer.getDemandeIdDemande());
            if(rhb != null)
            {
                rhb.setHasOffer(true);
                rhbRep.saveAndFlush(rhb);
            }
            /*
            //if(!( offerRepository.existsByDemandeIdDemande(offer.getDemandeIdDemande()))) {
        	 request.setEtat(1);
             requestRepository.saveAndFlush(request);*/
           
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("offer", offer);
		h.put("message", "L'offre est ajoutée avec succès.");
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value="/list_validation/{idUser}", method=RequestMethod.GET)
	public HashMap<String, Object> getRequestHasBankByBankAndProduct(@PathVariable Integer idUser){
		HashMap<String, Object> h = new HashMap<String, Object>();
		//List<RequestHasBank> hasBanks;
		if(idUser == null)
		{
           h.put("message", "1 ou plusieurs paramètres manquants");
           h.put("status", 0);
           return h;
		}
		try
		{
			User user = userBankRepository.findOne(idUser);
			if(user == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'existe pas et/ou n'est pas d'une banque.");
				return h;
			}
			Groupe groupe = groupeRep.findOne(user.getGroupeIdGroupe().getIdGroupe());
			if(groupe == null)
			{
				h.put("message", "Le groupe de l'utilisateur n'existe pas.");
				h.put("status", -1);		
				return h;
			}
			
			/*Request request = requestRepository.findOne(offer.getDemandeIdDemande().getIdDemande());
			if(request == null)
			{
				h.put("status", -1);
				h.put("message", "la demande associée à l'offre n'existe pas");
				return h;
			}
			Products product = productRep.findOne(request.getProduct().getIdProduits());
			if (product == null)
			{
				h.put("message", "le produit associé à l'offre n'existe pas");
				h.put("status", -1);
				return h;
			}*/
			//hasBanks = rHBRep.findRequestHasBankByProductBank(p, b);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "success");
		//h.put("list_request_has_bank", hasBanks);
		h.put("status", 0);
		return h;
	}
	/*
	 @RequestMapping(value = "/add/{idUserBank}", method = RequestMethod.POST)
	    @ResponseStatus(HttpStatus.OK)
	    public Map<String,Object> add(@RequestBody OfferModel offerModel, @PathVariable Integer idUserBank) {
	        HashMap<String, Object> h = new HashMap<String, Object>();
	        Offer offer;
	        System.out.println("111");
	        if(idUserBank == null || offerModel == null)
	        {
	            h.put("status", -1);
	            h.put("message", "1 ou plusieurs paramètres manquants");
	            return h;
	        }
	        System.out.println("2222");
	        try
	        {
	            offer = offerModel.getOffer();
	            if(offer == null)
	            {
	                h.put("status", -1);
	                h.put("message", "cette offre n'existe pas");
	                return h;
	            }
	            System.out.println("3333");
	            User user = userBankRepository.findOne(idUserBank);
	            if(user == null)
	            {
	                h.put("status", -1);
	                h.put("message", "cet utilisateur n'existe pas et/ou n'est pas d'une banque");
	                return h;
	            }
	            System.out.println("44444");
	            User userOffer = userBankRepository.findOne(offer.getUserBanqueIdUserBanque().getIdUtilisateur());
	            if(userOffer == null)
	            {
	                h.put("status", -1);
	                h.put("message", "l'utilisateur associé à l'offre n'existe pas");
	                return h;
	            }
	            System.out.println("55555");
	            Request request = requestRepository.findOne(offer.getDemandeIdDemande().getIdDemande());
	            if(request == null)
	            {
	                h.put("status", -1);
	                h.put("message", "cette demande n'existe pas");
	                return h;
	            }
	            System.out.println("666666");
	            if(offerRepository.existsByRequestAndInstitution(userOffer.getGroupeIdGroupe().getInstitution(), request))
	            {
	                System.out.println("777777");
	                h.put("status", -1);
	                h.put("message", "une offre correspondante à la demande existe déjà pour cette institution");
	                return h;
	            }
	            System.out.println("888888");
	            offer.setDate(new Date());
	            offer.setDemandeIdDemande(request);
	            offer.setUserBanqueIdUserBanque(userOffer);
	            offer = offerRepository.save(offer);
	            
	            //Store documents in DocumentsHasOffer
	            if(offerModel.getDocuments() != null)
	            {
	                addDocuments((offerModel.getOffer()).getIdOffre(), offerModel.getDocuments());
	            }
	            System.out.println("999999");
	            //Store commissions in commissionList
	            if(offerModel.getCommissions() != null)
	            {
	                addCommissions(offerModel.getOffer().getIdOffre(), offerModel.getCommissions());
	            }
	            System.out.println("000000");
	            journaliser(idUserBank, 6, offer.getIdOffre());
	            
	        } catch (Exception e) {
	            h.put("message", e.getMessage());
	            h.put("status", -1);
	            return h;
	        }
	        h.put("offer", offer);
	        h.put("message", "offre ajoutée avec succès");
	        h.put("status", 0);
	        return h;
	    }
	 
	*/
	/*// Add offer
	@RequestMapping(value = "/add/{idUserBank}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> add(@RequestBody DocumentOfferModel offerDocument, @PathVariable Integer idUserBank) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		Offer offer;
		if(idUserBank == null || offerDocument == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		
		try
		{
			offer = offerDocument.getOffer();
			if(offer == null)
			{
				h.put("status", -1);
				h.put("message", "cette offre n'existe pas");
				return h;
			}
			
			User user = userBankRepository.findOne(idUserBank);
			if(user == null)
			{
				h.put("status", -1);
				h.put("message", "cet utilisateur n'existe pas et/ou n'est pas d'une banque");
				return h;
			}
			
			User userOffer = userBankRepository.findOne(offer.getUserBanqueIdUserBanque().getIdUtilisateur());
			if(userOffer == null)
			{
				h.put("status", -1);
				h.put("message", "l'utilisateur associé à l'offre n'existe pas");
				return h;
			}
			
			Request request = requestRepository.findOne(offer.getDemandeIdDemande().getIdDemande());
			if(request == null)
			{
				h.put("status", -1);
				h.put("message", "cette demande n'existe pas");
				return h;
			}
			
			offer.setDate(new Date());
			offer.setDemandeIdDemande(request);
			offer.setUserBanqueIdUserBanque(userOffer);
			offer = offerRepository.save(offer);
			
			//Store documents in DocumentsHasRequest
			if(offerDocument.getDocuments() != null) 
			{
				addDocuments((offerDocument.getOffer()).getIdOffre(), offerDocument.getDocuments());
			}
			
			journaliser(idUserBank, 6, offer.getIdOffre());
			
		} catch (Exception e) {
			h.put("message", e.getMessage());
			h.put("status", -1);
			return h;
		}
		h.put("offer", offer);
		h.put("message", "offre ajoutée avec succès");
		h.put("status", 0);
		return h;
	}*/
	
	//Update offer
    @RequestMapping(value = "/update/{idUserBank}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public Map<String,Object> update(@RequestBody OfferModel offerModel, @PathVariable Integer idUserBank) {
        HashMap<String, Object> h = new HashMap<String, Object>();
        Offer offer;
        if(idUserBank == null || offerModel == null)
        {
            h.put("status", -1);
            h.put("message", "1 ou plusieurs paramètres manquants");
            return h;
        }
        
        try
        {
            offer = offerRepository.findOne(offerModel.getOffer().getIdOffre());
            if(offer == null)
            {
                h.put("status", -1);
                h.put("message", "Cette offre n'existe pas.");
                return h;
            }
            if(offer.getEtat() == 1)
            {
                h.put("message", "L'offre a déjà été validée.");
                h.put("status", -1);
                return h;
            }
            
            User user = userBankRepository.findOne(idUserBank);
            if(user == null)
            {
                h.put("status", -1);
                h.put("message", "Cet utilisateur n'existe pas et/ou n'est pas d'une banque.");
                return h;
            }
            
            User userOffer = userBankRepository.findOne(offer.getUserBanqueIdUserBanque().getIdUtilisateur());
            if(userOffer == null)
            {
                h.put("status", -1);
                h.put("message", "L'utilisateur associé à l'offre n'existe pas.");
                return h;
            }
            
            Request request = requestRepository.findOne(offer.getDemandeIdDemande().getIdDemande());
            if(request == null)
            {
                h.put("status", -1);
                h.put("message", "La demande associée à l'offre n'existe pas.");
                return h;
            }
            
            offer.setDate(new Date());
            offer.setTaux(offerModel.getOffer().getTaux());
          //  offer.setValid(offer.getI);
            offer.setDemandeIdDemande(request);
            offer.setUserBanqueIdUserBanque(userOffer);
            offer = offerRepository.saveAndFlush(offer);
            
            //Purge documents and commissions
            purgeDocuments(offer);
            purgeCommissions(offer);
            
            //Update documents in DocumentsHasOffer
            if(offerModel.getDocuments() != null) 
            {
                addDocuments((offerModel.getOffer()).getIdOffre(), offerModel.getDocuments());
            }
            
            //Update commissions in commissionList
            if(offerModel.getCommissions() != null) 
            {
                addCommissions(offerModel.getOffer().getIdOffre(), offerModel.getCommissions());
            }
            
            journaliser(idUserBank, 6, offer.getIdOffre());
            /*
            String emailSubject = env.getProperty("notification.offer.email.subject");
            String emailBody = env.getProperty("notification.offer.email.body");
            validationService.sendMail(offer, emailSubject, emailBody);
            */
            //Update requestHasBank
            RequestHasBank rhb = rhbRep.findByBankAndRequest(userOffer.getGroupeIdGroupe().getInstitution(), offer.getDemandeIdDemande());
            if(rhb != null)
            {
                rhb.setHasOffer(true);
                rhbRep.saveAndFlush(rhb);
            }
        } catch (Exception e) {
            e.printStackTrace();
            h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
            h.put("status", -1);
            return h;
        }
        h.put("offer", offer);
        h.put("message", "L'offre est mise à jour avec succès.");
        h.put("status", 0);
        return h;
    }
    
	/*
	@RequestMapping(value = "/update/{idUserBank}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> update(@RequestBody Offer offer, @PathVariable Integer idUserBank) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(idUserBank == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		
		try
		{
			User user = userBankRepository.findOne(idUserBank);
			if(user == null)
			{
				h.put("status", -1);
				h.put("message", "cet utilisateur n'existe pas et/ou n'est pas d'une banque");
				return h;
			}
			
			Offer offerOld = offerRepository.findOne(offer.getIdOffre());
			if(offerOld == null)
			{
				h.put("status", -1);
				h.put("message", "l'offre correspondante à l'id n'existe pas");
				return h;
			}
			
			if(offerOld.isValid())
			{
				h.put("status", -1);
				h.put("message", "cette offre a déjà été validée et ne peut plus être modifiée");
				return h;
			}
			
			if(offer.getUserBanqueIdUserBanque() == null)
			{
				offer.setUserBanqueIdUserBanque(offerOld.getUserBanqueIdUserBanque());
			}
			else
			{
				Integer idUserOffer = offer.getUserBanqueIdUserBanque().getIdUtilisateur();
				User userOffer = userBankRepository.findOne(idUserOffer);
				if(userOffer == null)
				{
					h.put("status", -1);
					h.put("message", "l'utilisateur associé à l'offre n'existe pas");
					return h;
				}
				offer.setUserBanqueIdUserBanque(userOffer);
			}
			
			if(offer.getDemandeIdDemande() == null)
			{
				offer.setDemandeIdDemande(offerOld.getDemandeIdDemande());
			}
			else
			{
				Integer idRequestOffer = offer.getDemandeIdDemande().getIdDemande();
				Request requestrOffer = requestRepository.findOne(idRequestOffer);
				if(requestrOffer == null)
				{
					h.put("status", -1);
					h.put("message", "la demande associée à l'offre n'existe pas");
					return h;
				}
				offer.setDemandeIdDemande(requestrOffer);
			}
			
			if(offer.getIsActive() == null)
				offer.setIsActive(offerOld.getIsActive());
			if(offer.getIsChoiceOffre() == null)
				offer.setIsChoiceOffre(offerOld.getIsChoiceOffre());
			if(offer.getDate() == null)
				offer.setDate(new Date());
			if(offer.getHasDocument() == null)
				offer.setHasDocument(offerOld.getHasDocument());
			if(offer.getEtat() == null)
				offer.setEtat(offerOld.getEtat());
			if(offer.getFrais() == null)
				offer.setFrais(offerOld.getFrais());
			if(offer.isValid()) == null)
				offer.setIsValid(offerOld.getIsValid());
			if(offer.getMotsCles() == null)
				offer.setMotsCles(offerOld.getMotsCles());
			if(offer.getTaux() == null)
				offer.setTaux(offerOld.getTaux());
			
			offer = offerRepository.saveAndFlush(offer);
			journaliser(idUserBank, 7, offer.getIdOffre());
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("offer", offer);
		h.put("message", "offre mise à jour avec succès");
		h.put("status", 0);
		return h;
	}*/
	
	// Delete offer
	@RequestMapping(value = "/delete/{idUserBank}/{idOffer}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> delete(@PathVariable Integer idUserBank, @PathVariable Integer idOffer)
	{
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(idUserBank == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		try
		{
			User user = userBankRepository.findOne(idUserBank);
			if(user == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'existe pas et/ou n'est pas d'une banque.");
				return h;
			}
			
			Offer offer = offerRepository.findOne(idOffer);
			if(offer == null)
			{
				h.put("status", -1);
				h.put("message", "Cette offre n'existe pas.");
				return h;
			}
			notificationsUserRep.deleteByIdOffer(offer.getIdOffre());
			offerRepository.delete(offer);
			//Update requestHasBank
            RequestHasBank rhb = rhbRep.findByBankAndRequest(offer.getUserBanqueIdUserBanque().getGroupeIdGroupe().getInstitution(), offer.getDemandeIdDemande());
            if(rhb != null)
            {
                rhb.setHasOffer(false);
                rhbRep.saveAndFlush(rhb);
            }
            if(!( offerRepository.existsByDemandeIdDemande(offer.getDemandeIdDemande()))) {
            	Request request = requestRepository.findOne(offer.getDemandeIdDemande().getIdDemande());
            	 request.setEtat(0);
                 requestRepository.saveAndFlush(request);
            }
			journaliser(idUserBank, 8, offer.getIdOffre());
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "L'offre est supprimée avec succès.");
		h.put("status", 0);
		return h;
	}
	
	public void addCommissions(Integer idOffer, ArrayList<CommissionOffer> commissionList) {
		Offer offer = offerRepository.findOne(idOffer);
		if(offer != null)
		{
			for(int i = 0; i < commissionList.size(); i++)
			{
				CommissionOffer commission = commissionList.get(i);
				commission.setOffer(offer);
				commissionOfferRepository.save(commission);
			}
		}
	}
	
	public void addDocuments(Integer idOffer, Integer[] tabDocument) {
		Offer offer = offerRepository.findOne(idOffer);
		if(offer != null)
		{
			for(int i = 0; i < tabDocument.length; i++)
			{
				Documents document = documentsRepository.findOne(tabDocument[i]);
				if(document != null)
				{
					DocumentsHasOffer documentOffer = new DocumentsHasOffer();
					documentOffer.setOffer(offer);
					documentOffer.setDocuments(document);
					documentOffer.setDateDocument(new Date());
					documentOffer.setDocumentsHasOfferPK(new DocumentsHasOfferPK(document.getIdDocuments(), offer.getIdOffre()));
					documentOffer = dhoRepository.save(documentOffer);
				}
			}
		}
	}
	
	public void purgeCommissions(Offer offer) {
        if(offer != null)
        {
            List<CommissionOffer> commissionList = commissionOfferRepository.findByOffer(offer);
            for(int i = 0; i < commissionList.size(); i++)
            {
                commissionOfferRepository.delete(commissionList.get(i));
            }
        }
    }
    
    public void purgeDocuments(Offer offer) {
        if(offer != null)
        {
            List<DocumentsHasOffer> documentList = dhoRepository.findByOffer(offer);
            for(int i = 0; i < documentList.size(); i++)
            {
                dhoRepository.delete(documentList.get(i));
            }
        }
    }
    
	public void journaliser(Integer idUser, Integer idAction, Integer idOffre) {
		Log log = new Log();
		log.setIdUser(idUser);
		log.setIdOffer(idOffre);
		log.setDate(new Date());
		log.setActionIdAction(actionRepository.findOne(idAction));
		logRepository.save(log);
	}
	public Date lendemain()
	   {    
	        Calendar date=new GregorianCalendar();
	        date.add(Calendar.DATE, 1);
	        //DateFormat df=DateFormat.getDateInstance(DateFormat.SHORT);
	        //String dateDemain = df.format(date.getTime());
	        return date.getTime();
	   }
	
}
