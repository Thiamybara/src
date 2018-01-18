package com.qualshore.etreasury.rest;

import java.util.ArrayList;
import java.util.Date;
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

import com.qualshore.etreasury.dao.BankRepository;
import com.qualshore.etreasury.dao.DeviseRepository;
import com.qualshore.etreasury.dao.GroupRepository;
import com.qualshore.etreasury.dao.OfferRepository;
import com.qualshore.etreasury.dao.ProductsRepository;
import com.qualshore.etreasury.dao.RequestHasBankRepository;
import com.qualshore.etreasury.dao.RequestRepository;
import com.qualshore.etreasury.dao.UserBankRepository;
import com.qualshore.etreasury.dao.UserRepository;
import com.qualshore.etreasury.dao.ValidationLevelGroupeRepository;
import com.qualshore.etreasury.dao.ValidationLevelRepository;
import com.qualshore.etreasury.dao.ValidationOfferRepository;
import com.qualshore.etreasury.entity.Groupe;
import com.qualshore.etreasury.entity.Institution;
import com.qualshore.etreasury.entity.Offer;
import com.qualshore.etreasury.entity.Products;
import com.qualshore.etreasury.entity.Request;
import com.qualshore.etreasury.entity.RequestHasBank;
import com.qualshore.etreasury.entity.User;
import com.qualshore.etreasury.entity.ValidationLevel;
import com.qualshore.etreasury.entity.ValidationLevelGroupe;
import com.qualshore.etreasury.entity.ValidationOffer;
import com.qualshore.etreasury.entity.ValidationOfferPK;
import com.qualshore.etreasury.service.UserService;
import com.qualshore.etreasury.service.ValidationService;

@RestController
@RequestMapping("/etreasury_project/validation_offer")
public class ValidationOfferRestController {
	@Autowired
	ValidationOfferRepository validationOfferRep;
	
	@Autowired
	OfferRepository offerRep;
	
	@Autowired
	UserBankRepository userBankRep;
	
	@Autowired
	UserRepository userRep;
	
	@Autowired
	BankRepository bankRep;
	
	@Autowired
	ValidationLevelRepository validationLevelRep;
	
	@Autowired
	ValidationLevelGroupeRepository vLevelGroupRep;
	
	@Autowired
	ProductsRepository productRep;
	
	@Autowired
	GroupRepository groupRep;
	
	@Autowired
	ValidationService validationService;
	
	@Autowired
	Environment env;
	
	@Autowired
	RequestRepository requestRep;
	
	@Autowired
	RequestHasBankRepository rhbRep;
	
	@Autowired
	UserService userService;
	
	@Autowired
	DeviseRepository deviseRepository;
	
	/*
	 * Get all offer's validation
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object> list() {
	
		HashMap<String, Object> h = new HashMap<String, Object>();
		List<ValidationOffer> validationOfferList;
		
		validationOfferList = validationOfferRep.findAll();
		h.put("status", 0);
		h.put("validation_list", validationOfferList);
		h.put("message", "success");
		return h;
	}
		
	/*
	 * Edit offer's validation
	 */
	@RequestMapping(value = "/edit/{idUser}/{idRequest}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object> editRequest(@PathVariable Integer idUser, @PathVariable Integer idRequest)
	{
		HashMap<String, Object> h = new HashMap<String, Object>();
		/*List<ValidationRequest> validationList;
		try
		{
			validationList = validationRepository.findAll();
			
		} catch (Exception e) {
			h.put("status", -1);
			h.put("message", e.getMessage());
			return h;
		}
		h.put("status", 0);
		h.put("validation_list", validationList);
		h.put("message", "success");*/
		return h;
	}
	
	/*
	 * Add offer's validation
	 */
	@RequestMapping(value = "/add/{idUser}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object> addValidation(@RequestBody ValidationOffer vl, @PathVariable Integer idUser)
	{
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(idUser == null || vl == null || vl.getOffer() == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		
		try {
			User user = userBankRep.findOne(idUser);
			if (user == null)
			{
				h.put("message", "L'utilisateur n'existe pas et/ou n'est pas d'une entreprise.");
				h.put("status", -1);
				return h;
			}
			
			Offer offer = offerRep.findOne(vl.getOffer().getIdOffre());
			if (offer == null)
			{
				h.put("message", "L'offre n'existe pas");
				h.put("status", -1);
				return h;
			}
			
			if (offer.isValid())
			{
				h.put("message", "La validation de cette offre a déjà été effectuée.");
				h.put("status", -1);
				return h;
			}
			Request request = requestRep.findOne(offer.getDemandeIdDemande().getIdDemande());
			if(request == null)
			{
				h.put("status", -1);
				h.put("message", "La demande associée à l'offre n'existe pas.");
				return h;
			}
			Products product = productRep.findOne(offer.getDemandeIdDemande().getProduct().getIdProduits());
			if (product == null)
			{
				h.put("message", "Le produit associé à l'offre n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			Groupe groupe = groupRep.findOne(user.getGroupeIdGroupe().getIdGroupe());
			if(groupe == null)
			{
				h.put("message", "Le groupe de l'utilisateur n'existe pas.");
				h.put("status", -1);		
				return h;
			}
			
			Institution banque = bankRep.findOne(groupe.getInstitution().getIdInstitution());
			if(banque == null)
			{
				h.put("message", "La banque de l'utilisateur n'existe pas.");
				h.put("status", -1);		
				return h;
			}
			
			ValidationLevel validationLevel = validationLevelRep.findByProductAndInstitutionAndSens(product, banque, "offer");
			if(validationLevel == null)
			{
				h.put("message", "Ce niveau de validation n'existe pas.");
				h.put("status", -1);		
				return h;
			}
			
			ValidationLevelGroupe vlg = vLevelGroupRep.findByValidationLevelAndGroupe(validationLevel, groupe);
			if(vlg == null)
			{
				h.put("message", "Aucun niveau de validation trouvé.");
				h.put("status", -1);		
				return h;
			}
			int niveau = vlg.getNiveau();
			vl.setNiveau(niveau);
			
			if(! validationLevel.getIsValidChain())
			{
				h.put("message", "La chaine de validation n'a pas été définie pour ce produit.");
				h.put("status", -1);		
				return h;
			}
			
			if(vl.getNiveau() > validationLevel.getNombreValidation())
			{
				h.put("status", -1);
				h.put("message", "Le niveau de validation est incorrect.");
				return h;
			}
			
			if(! vLevelGroupRep.isGroupInProductChain(groupe, product))
			{
				h.put("message", "Votre groupe ne fait pas partie de la chaine.");
				h.put("status", -1);
				return h;
			}
			
			if(validationOfferRep.existsByOfferAndNiveau(offer, vl.getNiveau()))
			{
				h.put("message", "Cette validation a déjà été effectuée pour ce niveau.");
				h.put("status", -1);
				return h;
			}
			
			ValidationLevelGroupe vLevelGroup = vLevelGroupRep.findByValidationLevelAndGroupeAndNiveau(validationLevel, groupe, vl.getNiveau());
			if(vLevelGroup == null)
			{
				h.put("message", "Cette validation n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			// AllRequired = false
			if(! validationLevel.getAllsRequired())
			{
				if(userService.isMontantAvailable(offer.getDemandeIdDemande(), vLevelGroup, deviseRepository))
				{
					vl.setUser(user);
					vl.setOffer(offer);
					vl.setDate(new Date());
					vl.setValidationOfferPK(new ValidationOfferPK(offer.getIdOffre(), vl.getNiveau()));
					vl = validationOfferRep.save(vl);
					
					offer.setNextValidationGroup("Offre envoyée");
					offer.setValid(true);
					//Offre validee etat=1
					offer.setEtat(1);
					offerRep.saveAndFlush(offer);
					
					//Update request
		            request.setEtat(2);
		            request.setNextValidationGroup("Offre reçue");
		            requestRep.saveAndFlush(request);
					
		            //Notify group
					/*String emailSubject = env.getProperty("notification.offer.email.already.validate.subject");
					String emailBody = env.getProperty("notification.offer.email.already.validate.body");*/
					validationService.sendMailGroupeValid(offer, user.getGroupeIdGroupe());
					
					//Notify chain that all validations are done
					/*emailSubject = env.getProperty("notification.offer.email.all.validate.subject");
					emailBody = env.getProperty("notification.offer.email.all.validate.body");*/
					validationService.sendMailAllValidated(offer);
					validationService.sendMailToInstitution(offer);
					
		            //All banks send valid offer for request
					List<RequestHasBank> rhbList = rhbRep.findByRequest(request);
					List<Offer> offerList = offerRep.findByDemandeIdDemandeAndIsValid(request, true);
					if(rhbList.size() == offerList.size())
					{
						request.setEtat(3);
						request.setNextValidationGroup("Demande cloturée");
			            requestRep.saveAndFlush(request);
					}
				}
				else
				{
					h.put("message", "L'utilisateur n'est pas habilité à valider l'offre avec ce montant.");
					h.put("status", -1);		
					return h;
				}
			}
			
			// AllRequired = true
			else
			{
				ValidationOffer vlTemp = validationOfferRep.findByOfferAndNiveau(offer, vl.getNiveau());
				if(vlTemp != null)
				{
					h.put("message", "Cette validation a déjà été effectuée à votre niveau.");
					h.put("status", -1);		
					return h;
				}
				else
				{
					//First validation
					if(vl.getNiveau() == 1)
					{
						vl.setUser(user);
						vl.setOffer(offer);
						vl.setDate(new Date());
						vl.setValidationOfferPK(new ValidationOfferPK(offer.getIdOffre(), vl.getNiveau()));
						vl = validationOfferRep.save(vl);
					}
					
					//Next validation
					else
					{
						if(validationOfferRep.findByOfferAndNiveau(offer, vl.getNiveau()-1) == null)
						{
							int niveauInf =vl.getNiveau()-1;
							h.put("message", "En attente de validation du niveau "+ niveauInf);
							h.put("status", -1);		
							return h;
						}
						vl.setUser(user);
						vl.setDate(new Date());
						vl.setValidationOfferPK(new ValidationOfferPK(offer.getIdOffre(), vl.getNiveau()));
						vl = validationOfferRep.save(vl);
					}
					
					//Notify group
					String emailSubject = env.getProperty("notification.offer.email.already.validate.subject");
					String emailBody = env.getProperty("notification.offer.email.already.validate.body");
					validationService.sendMailGroupeOfferAlreadyValidated(offer, user.getGroupeIdGroupe(), emailSubject, emailBody);
					
					//VALID All validations done
					if(vl.getNiveau() == validationLevel.getNombreValidation())
					{
						offer.setValid(true);
						//Offre validee etat=1
						offer.setEtat(1);
						offer.setNextValidationGroup("Offre envoyée");
			            //Update request
			            request.setEtat(2);
			            request.setNextValidationGroup("Offre reçue");
			            requestRep.saveAndFlush(request);
						offerRep.saveAndFlush(offer);
						
						//Notify chain that all validations are done
						/*emailSubject = env.getProperty("notification.offer.email.all.validate.subject");
						emailBody = env.getProperty("notification.offer.email.all.validate.body");*/
						validationService.sendMailAllValidated(offer);
						validationService.sendMailToInstitution(offer);

						//All banks send valid offer for request
						List<RequestHasBank> rhbList = rhbRep.findByRequest(request);
						List<Offer> offerList = offerRep.findByDemandeIdDemandeAndIsValid(request, true);
						if(rhbList.size() == offerList.size())
						{
							request.setEtat(3);
							request.setNextValidationGroup("Demande cloturée");
				            requestRep.saveAndFlush(request);
						}
					}
					
					//Notify next group to validate
					else
					{
						ValidationLevelGroupe vLevelGroupNext = vLevelGroupRep.findByValidationLevelAndNiveau(validationLevel, vl.getNiveau()+1);
						if(vLevelGroupNext == null)
						{
							h.put("message", "Le next validation n'existe pas.");
							h.put("status", -1);
							return h;
						}
						/*String emailSubjectNext = env.getProperty("notification.offer.email.to.validate.subject");
						String emailBodyNext = env.getProperty("notification.offer.email.to.validate.body");*/
						validationService.sendMailGroupeOfferToValidate(offer, vLevelGroupNext.getGroupe());
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("validation_offer", vl);
		h.put("message", "La validation est ajoutée avec succès.");
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value = "/list_notification_attente/{idUser}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> editNotification(@PathVariable Integer idUser)
    {
        HashMap<String, Object> h = new HashMap<String, Object>();
        ArrayList<Offer> offerNotifyList = new ArrayList<Offer>();
        if(idUser == null)
        {
            h.put("message", "1 ou plusieurs paramètres manquants");
            h.put("status", -1);
            return h;
        }
        
        try {
            User user = userBankRep.findOne(idUser);
            if (user == null)
            {
                h.put("message", "L'utilisateur n'existe pas et/ou n'est pas d'une entreprise.");
                h.put("status", -1);
                return h;
            }
            
            Groupe groupe = groupRep.findOne(user.getGroupeIdGroupe().getIdGroupe());
            if(groupe == null)
            {
                h.put("message", "Le groupe de l'utilisateur n'existe pas.");
                h.put("status", -1);        
                return h;
            }
            
            Institution bank = bankRep.findOne(groupe.getInstitution().getIdInstitution());
            if(bank == null)
            {
                h.put("message", "La banque de l'utilisateur n'existe pas.");
                h.put("status", -1);        
                return h;
            }
            
            List<Request> requestList = requestRep.findByBank(user.getGroupeIdGroupe().getInstitution());
            for(Request request : requestList)
            {
                ValidationLevel validationLevel = validationLevelRep.findByInstitutionAndProduct(bank, request.getProduct());
                if(validationLevel != null && validationLevel.getIsValidChain())
                {
                    ValidationLevelGroupe vlg = vLevelGroupRep.findByValidationLevelAndGroupe(validationLevel, groupe);
                    if(vlg != null)
                    {
                        List<Offer> offerList = offerRep.findByInstitutionAndDemande(bank, request);
                        if(offerList != null && !offerList.isEmpty())
                        {
                            Offer offer = offerList.get(0);
                            if(offer != null)
                            {
                                if(validationLevel.getAllsRequired())
                                {
                                    if(!validationOfferRep.existsByOfferAndNiveau(offer, vlg.getNiveau()))
                                    {
                                        if(vlg.getNiveau() == 1 || (vlg.getNiveau() > 1 && validationOfferRep.existsByOfferAndNiveau(offer, vlg.getNiveau()-1)))
                                        {
                                        	offerNotifyList.add(offer);
                                        }
                                    }
                                }
                                else
                                {
                                    if(!validationOfferRep.existsByOffer(offer) && userService.isMontantAvailable(request, vlg, deviseRepository))
                                    {
                                    	offerNotifyList.add(offer);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
            h.put("status", -1);
            return h;
        }
        h.put("requestNotifyList", offerNotifyList);
        h.put("message", "L'offre est en attente de validation.");
        h.put("status", 0);
        return h;
    }
}