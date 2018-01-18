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

import com.qualshore.etreasury.dao.DeviseRepository;
import com.qualshore.etreasury.dao.EntrepriseRepository;
import com.qualshore.etreasury.dao.GroupRepository;
import com.qualshore.etreasury.dao.OfferRepository;
import com.qualshore.etreasury.dao.ProductsRepository;
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
import com.qualshore.etreasury.entity.User;
import com.qualshore.etreasury.entity.ValidationLevel;
import com.qualshore.etreasury.entity.ValidationLevelGroupe;
import com.qualshore.etreasury.entity.ValidationRequest;
import com.qualshore.etreasury.entity.ValidationRequestPK;
import com.qualshore.etreasury.model.OfferModel;
import com.qualshore.etreasury.model.RequestModelDetails;
import com.qualshore.etreasury.service.UserService;
import com.qualshore.etreasury.service.ValidationService;


@RestController
@RequestMapping("/etreasury_project/validation_request")
public class ValidationRequestRestController {

	@Autowired
	ValidationRequestRepository validationRequestRep;
	
	@Autowired
	RequestRepository requestRep;
	
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
	ProductsRepository productRep;
	
	@Autowired
	GroupRepository groupRep;
	
	@Autowired
	ValidationService validationService;
	
	@Autowired
	Environment env;
	
	@Autowired
	ValidationSelectRepository validationSelectRep;
	
	@Autowired
	OfferRepository offerRep;
	
	@Autowired
	DeviseRepository deviseRepository;
	
	@Autowired
	UserService userService;
	
	/*
	 * Get the request's state
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object> list() {
	
		HashMap<String, Object> h = new HashMap<String, Object>();
		List<ValidationRequest> validationRequestList;
		
		validationRequestList = validationRequestRep.findAll();
		h.put("status", 0);
		h.put("validation_list", validationRequestList);
		h.put("message", "success");
		return h;
	}
		
	/*
	 * Get the request's state
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
	
	@RequestMapping(value = "/add/{idUser}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object> addValidation(@RequestBody ValidationRequest vl, @PathVariable Integer idUser)
	{
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(idUser == null || vl == null || vl.getRequest() == null )
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
			
			Request request = requestRep.findOne(vl.getRequest().getIdDemande());
			if (request == null)
			{
				h.put("message", "La demande n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			if (request.getIsValid())
			{
				h.put("message", "La validation de cette demande a déjà été effectuée.");
				h.put("status", -1);
				return h;
			}
			
			Products product = productRep.findOne(request.getProduct().getIdProduits());
			if (product == null)
			{
				h.put("message", "Le produit associé à la demande n'existe pas.");
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
			
			Institution entreprise = entRep.findOne(groupe.getInstitution().getIdInstitution());
			if(entreprise == null)
			{
				h.put("message", "L'entreprise de l'utilisateur n'existe pas.");
				h.put("status", -1);		
				return h;
			}
			
			ValidationLevel validationLevel = validationLevelRep.findByProductAndInstitutionAndSens(product, entreprise, "request");
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
			
			if(validationRequestRep.existsByRequestAndNiveau(request, vl.getNiveau()))
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
			if(!validationLevel.getAllsRequired())
			{
				if(userService.isMontantAvailable(request, vLevelGroup, deviseRepository))
				{
					vl.setUser(user);
					vl.setRequest(request);
					vl.setDate(new Date());
					vl.setStatus(0);
					vl.setValidationRequestPK(new ValidationRequestPK(request.getIdDemande(), vl.getNiveau()));
					vl = validationRequestRep.save(vl);
					request.setIsValid(true);
					//Update request
		            request.setEtat(1);
		            request.setNextValidationGroup("Demande envoyée");
					requestRep.saveAndFlush(request);
					
					//Notify group
					/*String emailSubject = env.getProperty("notification.request.email.already.validate.subject");
					String emailBody = env.getProperty("notification.request.email.already.validate.body");*/
					validationService.sendMailGroupeValid(request, user.getGroupeIdGroupe());
					
					//Notify chain that all validations are done
					/*emailSubject = env.getProperty("notification.request.email.all.validate.subject");
					emailBody = env.getProperty("notification.request.email.all.validate.body");*/
					
					validationService.sendMailAllValidated(request);
					validationService.sendMailToInstitution(request);
				}
				else
				{
					h.put("message", "L'utilisateur n'est pas habilité à valider la demande avec ce montant.");
					h.put("status", -1);		
					return h;
				}
			}
			
			// AllRequired = true
			else
			{
				ValidationRequest vlTemp = validationRequestRep.findByRequestAndNiveau(request, vl.getNiveau());
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
						vl.setRequest(request);
						vl.setDate(new Date());
						vl.setStatus(0);
						vl.setValidationRequestPK(new ValidationRequestPK(request.getIdDemande(), vl.getNiveau()));
						vl = validationRequestRep.save(vl);
					}
					
					//Next validation
					else
					{
						if(validationRequestRep.findByRequestAndNiveau(request, vl.getNiveau()-1) == null)
						{
							int niveauInf =vl.getNiveau()-1;
							h.put("message", "En attente de validation du niveau "+ niveauInf);
							h.put("status", -1);		
							return h;
						}
						vl.setUser(user);
						vl.setDate(new Date());
						vl.setStatus(0);
						vl.setValidationRequestPK(new ValidationRequestPK(request.getIdDemande(), vl.getNiveau()));
						vl = validationRequestRep.save(vl);
					}
					
					//Notify group
					/*String emailSubject = env.getProperty("notification.request.email.already.validate.subject");
					String emailBody = env.getProperty("notification.request.email.already.validate.body");*/
					validationService.sendMailGroupeRequestAlreadyValidated(request, user.getGroupeIdGroupe());
					
					//All validations done
					if(vl.getNiveau() == validationLevel.getNombreValidation())
					{
						request.setIsValid(true);
						//Update request
			            request.setEtat(1);
			            request.setNextValidationGroup("Demande envoyée");
						requestRep.saveAndFlush(request);
						
						//Notify chain that all validations are done
						/*emailSubject = env.getProperty("notification.request.email.all.validate.subject");
						emailBody = env.getProperty("notification.request.email.all.validate.body");*/
						validationService.sendMailAllValidated(request);
						validationService.sendMailToInstitution(request);
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
						/*String emailSubjectNext = env.getProperty("notification.request.email.to.validate.subject");
						String emailBodyNext = env.getProperty("notification.request.email.to.validate.body");*/
						validationService.sendMailGroupeRequestToValidate(request, vLevelGroupNext.getGroupe());
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("validation_request", vl);
		h.put("message", "La validation est ajoutée avec succès.");
		h.put("status", 0);
		return h;
	}
	
	/*
	 * Notifications for user
	 */
	@RequestMapping(value = "/list_notification_attente/{idUser}", method = RequestMethod.GET)
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
                            if(!validationRequestRep.existsByRequest(request) && userService.isMontantAvailable(request, vlg, deviseRepository))
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
                            if(!validationSelectRep.existsByOffer(offer) && userService.isMontantAvailable(offer.getDemandeIdDemande(), vlg, deviseRepository))
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
    }
	
	// list des demandes non validés par un utilisateur sur un produit
	   /*@RequestMapping(value = "/list_notification_attente/{idUser}", method=RequestMethod.GET)
	   public HashMap<String, Object> getNotVavildateReqForUser(@PathVariable("idUser") int idUser){

	       HashMap<String, Object> h = new HashMap<String, Object>();
	       User user = userEntRep.findOne(idUser);

	       if (user == null) {
	           h.put("message", "L'utilisateur n'existe pas.");
	           h.put("status", -1);
	           return h;
	       }

	       Groupe gr = user.getGroupeIdGroupe();
	       Institution institution = user.getGroupeIdGroupe().getInstitution();
	       List<Request> requests = requestRep.findNotValidateRequest(institution, false);
	       //List<Request> listRequestNotify = new ArrayList<Request>();
	       List<RequestModelDetails> requestModels = new ArrayList<RequestModelDetails>();

	       requests.forEach(request->{
	           if (!request.getNextValidationGroup().equals("Demande envoyée")) {
	               Products pr = request.getProduct();
	               ValidationLevel vl = validationLevelRep.findByInstitutionAndProduct(institution, pr);
	               if (vl != null && vl.getIsValidChain()) {
	                   ValidationLevelGroupe vLG = vLevelGroupRep.findByValidationLevelAndGroupe(vl, gr);
	                   if (vLG != null) {
	                       if (vl.getAllsRequired()) {
	                           if (!validationRequestRep.existsByRequestAndNiveau(request, vLG.getNiveau())) {
	                               if (vLG.getNiveau() == 1 || vLG.getNiveau() > 1 &&
	                                       validationRequestRep.existsByRequestAndNiveau(request, vLG.getNiveau() -1)) {
	                                   RequestModelDetails rMD = new RequestModelDetails();
	                                   rMD.setRequest(request);
	                                   requestModels.add(rMD);
	                               }
	                           }

	                           String nameGroupeToValid = decoupeChaine(request.getNextValidationGroup(), " ");
	                           if (gr.getNom().equals(nameGroupeToValid)) {
	                               RequestModelDetails rMD = new RequestModelDetails();
	                               rMD.setRequest(request);
	                               requestModels.add(rMD);
	                           }
	                       } else {
	                           int montant = Integer.parseInt(request.getMontant());
	                           int valeurMin = Integer.parseInt(vLG.getValeurMin());
	                           int valeurMax = Integer.parseInt(vLG.getValeurMax());
	                           if (montant <= valeurMax && montant >= valeurMin) {
	                               RequestModelDetails rMD = new RequestModelDetails();
	                               rMD.setRequest(request);
	                               requestModels.add(rMD);
	                           }
	                       }
	                   }
	               }
	           }
	       });

	       h.put("message", "success");
	       h.put("list_notification_attente", requestModels);
	       h.put("status", 0);
	       return h;
	   }*/
	
	/*
	// list des demandes non validés par un utilisateur sur un produit
    @RequestMapping(value = "/list_notification_attente/{idUser}", method=RequestMethod.GET)
    public HashMap<String, Object> getNotValildateReqForUser(@PathVariable Integer idUser) {
        
        HashMap<String, Object> h = new HashMap<String, Object>();
        List<RequestModelDetails> requestModels = new ArrayList<RequestModelDetails>();
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
	            h.put("message", "l'utilisateur n'existe pas");
	            h.put("status", -1);
	            return h;
	        } 
	        Groupe gr = user.getGroupeIdGroupe();
	        Institution institution = user.getGroupeIdGroupe().getInstitution();
	        List<Request> requests = requestRep.findNotValidateRequest(institution, false);
	      
	        requests.forEach(request->{
	            Products pr = request.getProduct();
	            ValidationLevel vl = validationLevelRep.findByInstitutionAndProduct(institution, pr);
	            if (vl != null && vl.getIsValidChain()) {
	                ValidationLevelGroupe vLG = vLevelGroupRep.findByValidationLevelAndGroupe(vl, gr);
	                if (vLG != null) {
	                    if (vl.getAllsRequired())
	                    {
	                        if (!validationRequestRep.existsByRequestAndNiveau(request, vLG.getNiveau()))
	                        {
	                            if (vLG.getNiveau() == 1 || vLG.getNiveau() > 1 && 
	                            		validationRequestRep.existsByRequestAndNiveau(request, vLG.getNiveau() -1))
	                            {
	                                RequestModelDetails rMD = new RequestModelDetails();
	                                rMD.setRequest(request);
	                                requestModels.add(rMD);
	                            }
	                        }
	                    }
	                   int montant = Integer.parseInt(request.getMontant());
	                   int valeurMin = Integer.parseInt(vLG.getValeurMin());
	                   int valeurMax = Integer.parseInt(vLG.getValeurMax());
	                   
	                   if (montant <= valeurMax && montant >= valeurMin)
	                   {
	                	   RequestModelDetails rMD = new RequestModelDetails();
	                       rMD.setRequest(request);
	                       requestModels.add(rMD);
	                    }
	                }
	            }
	        });
        } catch (Exception e) {
            e.printStackTrace();
            h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
            h.put("status", -1);
            return h;
        }
        h.put("message", "success");
        h.put("list_notification_attente", requestModels);
        h.put("status", 0);
        return h;
    }
    */
    public String[] decoupe(String ligneEntree,String separateur) {
        if (ligneEntree == null) return null;
   
        int index = 0,i=0;      String[] lig=null; 
        String temporaire = ligneEntree;
   
		  if (temporaire != null) {
		              index = temporaire.indexOf(separateur);
		              System.out.println("ligneEntree : "+ligneEntree+" > premier index : "+index);
		              while (index >= 0) {
		                 lig[i] = temporaire.substring(0, index); 
		                 i++;
		                 System.out.println("\n index de la sous chaine"+index+"\n valeur de la sous chaine"+lig[i]);
		                temporaire = temporaire.substring(index + separateur.length(), temporaire.length());
		                index = temporaire.indexOf(separateur);
		              }
		              // Le dernier élément            
		  lig[i] = temporaire.substring(0, index);
		        }
        return lig;
      }
    public String decoupeChaine(String rep, String m){
        String[] listWord = rep.split(m);
        return listWord[listWord.length -1];
    }
}
