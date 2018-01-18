package com.qualshore.etreasury.rest;

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

import com.qualshore.etreasury.dao.DeviseRepository;
import com.qualshore.etreasury.dao.EntrepriseRepository;
import com.qualshore.etreasury.dao.GroupRepository;
import com.qualshore.etreasury.dao.OfferRepository;
import com.qualshore.etreasury.dao.ProductsRepository;
import com.qualshore.etreasury.dao.RequestHasBankRepository;
import com.qualshore.etreasury.dao.RequestRepository;
import com.qualshore.etreasury.dao.UserEntrepriseRepository;
import com.qualshore.etreasury.dao.UserRepository;
import com.qualshore.etreasury.dao.ValidationLevelGroupeRepository;
import com.qualshore.etreasury.dao.ValidationLevelRepository;
import com.qualshore.etreasury.dao.ValidationRequestRepository;
import com.qualshore.etreasury.dao.ValidationSelectRepository;
import com.qualshore.etreasury.entity.Groupe;
import com.qualshore.etreasury.entity.Institution;
import com.qualshore.etreasury.entity.Offer;
import com.qualshore.etreasury.entity.Products;
import com.qualshore.etreasury.entity.Request;
import com.qualshore.etreasury.entity.RequestHasBank;
import com.qualshore.etreasury.entity.User;
import com.qualshore.etreasury.entity.ValidationLevel;
import com.qualshore.etreasury.entity.ValidationLevelGroupe;
import com.qualshore.etreasury.entity.ValidationSelect;
import com.qualshore.etreasury.entity.ValidationSelectPK;
import com.qualshore.etreasury.service.UserService;
import com.qualshore.etreasury.service.ValidationService;

@RestController
@RequestMapping("/etreasury_project/pre_selection_offer")
public class ValidationSelectRestController {

	@Autowired
	ValidationSelectRepository validationSelectRep;
	
	@Autowired
	OfferRepository offerRep;
	
	@Autowired
	UserEntrepriseRepository userEntRep;
	
	@Autowired
	UserRepository userRep;
	
	@Autowired
	EntrepriseRepository entRep;
	
	@Autowired
	ValidationLevelRepository validationLevelRep;
	
	@Autowired
	ValidationLevelGroupeRepository vLevelGroupRep;
	
	@Autowired
	ValidationRequestRepository validationRequestRep;
	
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
	 * Get all selected validation
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object> list() {
	
		HashMap<String, Object> h = new HashMap<String, Object>();
		List<ValidationSelect> validationSelectList;
		
		validationSelectList = validationSelectRep.findAll();
		h.put("status", 0);
		h.put("validation_list", validationSelectList);
		h.put("message", "success");
		return h;
	}
		
	/*
	 * Edit selected validation
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
	 * Add selected validation
	 */
	@RequestMapping(value = "/add/{idUser}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object> addValidation(@RequestBody ValidationSelect vs, @PathVariable Integer idUser)
	{
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(idUser == null || vs == null || vs.getOffer() == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		
		try {
			User user = userEntRep.findOne(idUser);
			if (user == null)
			{
				h.put("message", "L'utilisateur n'existe pas et/ou n'est pas d'une entreprise.");
				h.put("status", -1);
				return h;
			}
			
			Offer offer = offerRep.findOne(vs.getOffer().getIdOffre());
			if (offer == null)
			{
				h.put("message", "L'offre n'existe pas");
				h.put("status", -1);
				return h;
			}
			
			if (offer.getEtat() != 2)
			{
				h.put("message", "L'offre n'a pas été pré-sélectionnée.");
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
			
			Institution banque = entRep.findOne(groupe.getInstitution().getIdInstitution());
			if(banque == null)
			{
				h.put("message", "La banque de l'utilisateur n'existe pas.");
				h.put("status", -1);		
				return h;
			}
			
			ValidationLevel validationLevel = validationLevelRep.findByProductAndInstitutionAndSens(product, banque, "select");
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
			vs.setNiveau(niveau);
			
			if(! validationLevel.getIsValidChain())
			{
				h.put("message", "La chaine de validation n'a pas été définie pour la sélection de ce produit.");
				h.put("status", -1);		
				return h;
			}
			
			if(vs.getNiveau() > validationLevel.getNombreValidation())
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
			
			if(validationSelectRep.existsByOfferAndNiveau(offer, vs.getNiveau()))
			{
				h.put("message", "Cette validation a déjà été effectuée pour ce niveau.");
				h.put("status", -1);
				return h;
			}
			
			ValidationLevelGroupe vLevelGroup = vLevelGroupRep.findByValidationLevelAndGroupeAndNiveau(validationLevel, groupe, vs.getNiveau());
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
					vs.setUser(user);
					vs.setOffer(offer);
					vs.setDate(new Date());
					vs.setValidationSelectPK(new ValidationSelectPK(offer.getIdOffre(), vs.getNiveau()));
					vs = validationSelectRep.save(vs);
					
					offer.setNextValidationGroup("Offre selectionnée");
					offer.setEtat(3);
					offerRep.saveAndFlush(offer);
					
					//Update request
		            request.setEtat(5);
		            request.setNextValidationGroup("Offre selectionnée");
		            requestRep.saveAndFlush(request);
					
		            //Notify group
					/*String emailSubject = env.getProperty("notification.offer.pre.validation.email.already.validate.subject");
					String emailBody = env.getProperty("notification.offer.pre.validation.email.already.validate.body");*/
					validationService.sendMailGroupeAcceptPreValid(offer, user.getGroupeIdGroupe());
					
					//Notify chain that all validations are done
					/*emailSubject = env.getProperty("notification.offer.pre.validation.email.all.validate.subject");
					emailBody = env.getProperty("notification.offer.pre.validation.email.all.validate.body");*/
					validationService.sendMailAllValidatedPreSelection(offer);
					//validationService.sendMailToInstitution(offer);
					
					//Notify banks that one is selected
		            validationService.sendMailToSelectedOfferInstitution(offer);
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
				ValidationSelect vlTemp = validationSelectRep.findByOfferAndNiveau(offer, vs.getNiveau());
				if(vlTemp != null)
				{
					h.put("message", "Cette validation a déjà été effectuée à votre niveau.");
					h.put("status", -1);		
					return h;
				}
				else
				{
					//First validation
					if(vs.getNiveau() == 1)
					{
						vs.setUser(user);
						vs.setOffer(offer);
						vs.setDate(new Date());
						vs.setValidationSelectPK(new ValidationSelectPK(offer.getIdOffre(), vs.getNiveau()));
						vs = validationSelectRep.save(vs);
					}
					
					//Next validation
					else
					{
						if(validationSelectRep.findByOfferAndNiveau(offer, vs.getNiveau()-1) == null)
						{
							int niveauInf =vs.getNiveau()-1;
							h.put("message", "En attente de validation du niveau "+ niveauInf);
							h.put("status", -1);		
							return h;
						}
						vs.setUser(user);
						vs.setDate(new Date());
						vs.setValidationSelectPK(new ValidationSelectPK(offer.getIdOffre(), vs.getNiveau()));
						vs = validationSelectRep.save(vs);
					}
					
					//Notify group
					/*String emailSubject = env.getProperty("notification.offer.pre.validation.email.already.validate.subject");
					String emailBody = env.getProperty("notification.offer.pre.validation.email.already.validate.body");*/
					//validationService.sendMailGroupeOfferAlreadyPreValidated(offer, user.getGroupeIdGroupe(), emailSubject, emailBody);
					validationService.sendMailGroupeAcceptPreValid(offer, user.getGroupeIdGroupe());
					
					//VALID All validations done
					if(vs.getNiveau() == validationLevel.getNombreValidation())
					{
						offer.setValid(true);
						offer.setEtat(3);
						offer.setNextValidationGroup("Offre selectionnée");
			            //Update request
			            request.setEtat(5);
			            request.setNextValidationGroup("Offre sélectionnée");
			            requestRep.saveAndFlush(request);
						offerRep.saveAndFlush(offer);
						
						//Notify chain that all validations are done
						/*emailSubject = env.getProperty("notification.offer.pre.validation.email.all.validate.subject");
						emailBody = env.getProperty("notification.offer.pre.validation.email.all.validate.body");*/
						validationService.sendMailAllValidatedPreSelection(offer);
						//validationService.sendMailToInstitution(offer);
						
						//Notify banks that one is selected
			            validationService.sendMailToSelectedOfferInstitution(offer);
					}
					
					//Notify next group to validate
					else
					{
						ValidationLevelGroupe vLevelGroupNext = vLevelGroupRep.findByValidationLevelAndNiveau(validationLevel, vs.getNiveau()+1);
						if(vLevelGroupNext == null)
						{
							h.put("message", "Le next validation n'existe pas.");
							h.put("status", -1);
							return h;
						}
						/*String emailSubjectNext = env.getProperty("notification.offer.pre.validation.email.to.validate.subject");
						String emailBodyNext = env.getProperty("notification.offer.pre.validation.email.to.validate.body");*/
						validationService.sendMailGroupeOfferToPreValidate(offer, vLevelGroupNext.getGroupe());
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("validation_offer", vs);
		h.put("message", "La validation est ajoutée avec succès.");
		h.put("status", 0);
		return h;
	}
	
	/*
	 * Refuse selected validation
	 */
	@RequestMapping(value = "/refuse/{idUser}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object> refuseValidation(@RequestBody ValidationSelect vs, @PathVariable Integer idUser)
	{
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(idUser == null || vs == null || vs.getOffer() == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		
		try {
			User user = userEntRep.findOne(idUser);
			if (user == null)
			{
				h.put("message", "L'utilisateur n'existe pas et/ou n'est pas d'une entreprise.");
				h.put("status", -1);
				return h;
			}
			
			Offer offer = offerRep.findOne(vs.getOffer().getIdOffre());
			if (offer == null)
			{
				h.put("message", "L'offre n'existe pas");
				h.put("status", -1);
				return h;
			}
			
			if (offer.getEtat() != 2)
			{
				h.put("message", "L'offre n'a pas été pré-sélectionnée.");
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
			
			Institution banque = entRep.findOne(groupe.getInstitution().getIdInstitution());
			if(banque == null)
			{
				h.put("message", "La banque de l'utilisateur n'existe pas.");
				h.put("status", -1);		
				return h;
			}
			
			ValidationLevel validationLevel = validationLevelRep.findByProductAndInstitutionAndSens(product, banque, "select");
			if(validationLevel == null)
			{
				h.put("message", "Ce niveau de validation n'existe pas.");
				h.put("status", -1);		
				return h;
			}
			
			//ValidationLevelGroupe vlg = vLevelGroupRep.findByValidationLevelAndGroupeAndNiveau(validationLevel, groupe, vs.getNiveau());
			ValidationLevelGroupe vlg = vLevelGroupRep.findByValidationLevelAndGroupe(validationLevel, groupe);
			if(vlg == null)
			{
				h.put("message", "Aucun niveau de validation trouvé.");
				h.put("status", -1);		
				return h;
			}
			int niveau = vlg.getNiveau();
			vs.setNiveau(niveau);
			
			if(! validationLevel.getIsValidChain())
			{
				h.put("message", "La chaine de validation n'a pas été définie pour la selection de ce produit.");
				h.put("status", -1);		
				return h;
			}
			
			if(vs.getNiveau() > validationLevel.getNombreValidation())
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
			
			if(validationSelectRep.existsByOfferAndNiveau(offer, vs.getNiveau()))
			{
				h.put("message", "Cette validation a déjà été effectuée pour ce niveau.");
				h.put("status", -1);
				return h;
			}
			
			ValidationLevelGroupe vLevelGroup = vLevelGroupRep.findByValidationLevelAndGroupeAndNiveau(validationLevel, groupe, vs.getNiveau());
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
					offer.setNextValidationGroup("Offre valide");
					offer.setEtat(1);
					offerRep.saveAndFlush(offer);
					
					//Update request
		            request.setEtat(2);
		            request.setNextValidationGroup("Offre positionnée");
		            requestRep.saveAndFlush(request);
					
		            //Notify group
					/*String emailSubject = env.getProperty("notification.offer.pre.validation.email.already.refuse.subject");
					String emailBody = env.getProperty("notification.offer.pre.validation.email.already.refuse.body");*/
					//validationService.sendMailGroupeValid(offer, user.getGroupeIdGroupe(), emailSubject, emailBody);
					validationService.sendMailGroupeRefusePreValid(offer, groupe);
					
					//Notify chain that all validations are done
					/*emailSubject = env.getProperty("notification.offer.pre.validation.email.all.refuse.subject");
					emailBody = env.getProperty("notification.offer.pre.validation.email.all.refuse.body");*/
					validationService.sendMailAllValidatedRefusePreSelection(offer);
					//validationService.sendMailToInstitution(offer);
					
		            //All banks send valid offer for request
					List<RequestHasBank> rhbList = rhbRep.findByRequest(request);
					List<Offer> offerList = offerRep.findByDemandeIdDemandeAndIsValid(request, true);
					if(rhbList.size() == offerList.size())
					{
						request.setEtat(3);
						request.setNextValidationGroup("Demande clôturée");
			            requestRep.saveAndFlush(request);
					}
					
					//Delete previous validations
					List<ValidationSelect> validationList = validationSelectRep.findByOffer(offer);
					for(ValidationSelect val : validationList)
					{
						validationSelectRep.delete(val);
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
				ValidationSelect vlTemp = validationSelectRep.findByOfferAndNiveau(offer, vs.getNiveau());
				if(vlTemp != null)
				{
					h.put("message", "Cette validation a déjà été effectuée à votre niveau.");
					h.put("status", -1);		
					return h;
				}
				else
				{
					if(vs.getNiveau() != 1 && validationSelectRep.findByOfferAndNiveau(offer, vs.getNiveau()-1) == null)
					{
						int niveauInf =vs.getNiveau()-1;
						h.put("message", "En attente de validation du niveau "+ niveauInf);
						h.put("status", -1);		
						return h;
					}
					
					//Notify group
					validationService.sendMailGroupeRefusePreValid(offer, user.getGroupeIdGroupe() );
					
					offer.setValid(true);
					offer.setEtat(1);
					offer.setNextValidationGroup("Offre valide");
		            //Update request
		            request.setEtat(2);
		            request.setNextValidationGroup("Offre positionnée");
		            requestRep.saveAndFlush(request);
					offerRep.saveAndFlush(offer);
					
					//Notify chain that all validations are done
					/*emailSubject = env.getProperty("notification.offer.pre.validation.email.all.refuse.subject");
					emailBody = env.getProperty("notification.offer.pre.validation.email.all.refuse.body");*/
					validationService.sendMailAllValidatedRefusePreSelection(offer);
					//validationService.sendMailToInstitution(offer);
					
		            //All banks send valid offer for request
					List<RequestHasBank> rhbList = rhbRep.findByRequest(request);
					List<Offer> offerList = offerRep.findByDemandeIdDemandeAndIsValid(request, true);
					if(rhbList.size() == offerList.size())
					{
						request.setEtat(3);
						request.setNextValidationGroup("Demande cloturée");
			            requestRep.saveAndFlush(request);
					}
					//Delete previous validations
					List<ValidationSelect> validationList = validationSelectRep.findByOffer(offer);
					for(ValidationSelect val : validationList)
					{
						validationSelectRep.delete(val);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("validation_offer", vs);
		h.put("message", "La validation est ajoutée avec succès.");
		h.put("status", 0);
		return h;
	}
	
	/*
	 * Notifications for user
	 */
	/*@RequestMapping(value = "/list_notification_attente/{idUser}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> editNotification(@PathVariable Integer idUser)
    {
        HashMap<String, Object> h = new HashMap<String, Object>();
        List<RequestModelDetails> requestModelList = new ArrayList<RequestModelDetails>();
        if(idUser == null)
        {
            h.put("message", "1 ou plusieurs paramètres manquants");
            h.put("status", -1);
            return h;
        }
        
        try
        {
            User user = userEntRep.findOne(idUser);
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
            
            Institution enterprise = entRep.findOne(groupe.getInstitution().getIdInstitution());
            if(enterprise == null)
            {
                h.put("message", "L'entreprise de l'utilisateur n'existe pas.");
                h.put("status", -1);        
                return h;
            }
            
            //New request validation
            List<Request> requestList = requestRep.findByEnterpriseAndNotValid(enterprise);
            for(Request request : requestList)
            {
            	ValidationLevel validationLevel = validationLevelRep.findByProductAndInstitutionAndSens(request.getProduct(), enterprise, "request");
                if(validationLevel != null && validationLevel.getIsValidChain())
                {
                    ValidationLevelGroupe vlg = vLevelGroupRep.findByValidationLevelAndGroupe(validationLevel, groupe);
                    if(vlg != null)
                    {
                        if(validationLevel.getAllsRequired())
                        {
                            if(!validationRequestRep.existsByRequestAndNiveau(request, vlg.getNiveau()))
                            {
                                if(vlg.getNiveau() == 1 || (vlg.getNiveau() > 1 && validationRequestRep.existsByRequestAndNiveau(request, vlg.getNiveau()-1)))
                                {
                                	RequestModelDetails requestModel = new RequestModelDetails();
                                	requestModel.setRequest(request);
                                	requestModelList.add(requestModel);
                                }
                            }
                        }
                        else
                        {
                            int montant = Integer.parseInt(request.getMontant());
                            int valeurMin = Integer.parseInt(vlg.getValeurMin());
                            int valeurMax = Integer.parseInt(vlg.getValeurMax());
                            if(!validationRequestRep.existsByRequest(request) && montant >= valeurMin && montant <= valeurMax)
                            {
                            	RequestModelDetails requestModel = new RequestModelDetails();
                            	requestModel.setRequest(request);
                            	requestModelList.add(requestModel);
                            }
                        }
                    }
                }
            }
            // New offer pre-selection
            List<Offer> offerList = offerRep.findByEnterpriseAndPreSelection(enterprise);
            for(Offer offer : offerList)
            {
            	ValidationLevel validationLevel = validationLevelRep.findByProductAndInstitutionAndSens(offer.getDemandeIdDemande().getProduct(), enterprise, "select");
                if(validationLevel != null && validationLevel.getIsValidChain())
                {
                    ValidationLevelGroupe vlg = vLevelGroupRep.findByValidationLevelAndGroupe(validationLevel, groupe);
                    if(vlg != null)
                    {
                        if(validationLevel.getAllsRequired())
                        {
                            if(!validationSelectRep.existsByOfferAndNiveau(offer, vlg.getNiveau()))
                            {
                                if(vlg.getNiveau() == 1 || (vlg.getNiveau() > 1 && validationSelectRep.existsByOfferAndNiveau(offer, vlg.getNiveau()-1)))
                                {
                                	RequestModelDetails requestModel = new RequestModelDetails();
                                	OfferModel offerModel = new OfferModel();
                                	offerModel.setOffer(offer);
                                	requestModel.setOfferModel(offerModel);
                                	requestModelList.add(requestModel);
                                }
                            }
                        }
                        else
                        {
                            int montant = Integer.parseInt(offer.getDemandeIdDemande().getMontant());
                            int valeurMin = Integer.parseInt(vlg.getValeurMin());
                            int valeurMax = Integer.parseInt(vlg.getValeurMax());
                            if(!validationSelectRep.existsByOffer(offer) && montant >= valeurMin && montant <= valeurMax)
                            {
                            	RequestModelDetails requestModel = new RequestModelDetails();
                            	OfferModel offerModel = new OfferModel();
                            	offerModel.setOffer(offer);
                            	requestModel.setOfferModel(offerModel);
                            	requestModelList.add(requestModel);
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
        h.put("requestNotifyList", requestModelList);
        h.put("message", "En attente de validation.");
        h.put("status", 0);
        return h;
    }*/
}
