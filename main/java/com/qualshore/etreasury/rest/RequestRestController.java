package com.qualshore.etreasury.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qualshore.etreasury.dao.BankRepository;
import com.qualshore.etreasury.dao.DeviseRepository;
import com.qualshore.etreasury.dao.DocumentHasRequestRepository;
import com.qualshore.etreasury.dao.DocumentsRepository;
import com.qualshore.etreasury.dao.EntrepriseRepository;
import com.qualshore.etreasury.dao.NotificationsUserRepository;
import com.qualshore.etreasury.dao.OfferRepository;
import com.qualshore.etreasury.dao.ProductsRepository;
import com.qualshore.etreasury.dao.RequestHasBankRepository;
import com.qualshore.etreasury.dao.RequestRepository;
import com.qualshore.etreasury.dao.UserEntrepriseRepository;
import com.qualshore.etreasury.dao.ValidationLevelRepository;
import com.qualshore.etreasury.entity.Bank;
import com.qualshore.etreasury.entity.Devise;
import com.qualshore.etreasury.entity.Documents;
import com.qualshore.etreasury.entity.DocumentsHasRequest;
import com.qualshore.etreasury.entity.DocumentsHasRequestPK;
import com.qualshore.etreasury.entity.Enterprise;
import com.qualshore.etreasury.entity.Groupe;
import com.qualshore.etreasury.entity.Institution;
import com.qualshore.etreasury.entity.Offer;
import com.qualshore.etreasury.entity.Products;
import com.qualshore.etreasury.entity.Request;
import com.qualshore.etreasury.entity.RequestHasBank;
import com.qualshore.etreasury.entity.RequestHasBankPK;
import com.qualshore.etreasury.entity.User;
import com.qualshore.etreasury.entity.UserEntreprise;
import com.qualshore.etreasury.entity.ValidationLevel;
import com.qualshore.etreasury.entity.ValidationLevelGroupe;
import com.qualshore.etreasury.model.RequestModel;
import com.qualshore.etreasury.model.RequestModelDetails;
import com.qualshore.etreasury.service.ValidationService;

@RequestMapping("/etreasury_project/mes_operations/entreprise/request")
@RestController
public class RequestRestController {
	
	@Autowired
	RequestRepository rRep;
	@Autowired
	UserEntrepriseRepository uERep;
	@Autowired
	ProductsRepository pRep;
	@Autowired
	EntrepriseRepository eRep;
	@Autowired
	DocumentHasRequestRepository drRepository;
	@Autowired
	DocumentsRepository dRep;
	@Autowired
	BankRepository bRep;
	@Autowired 
	RequestHasBankRepository rHBRep;
	@Autowired
	OfferRepository offerRep;
	@Autowired
	ValidationService validationService;
	@Autowired
	Environment env;
	@Autowired
	ValidationLevelRepository vlRep;
	@Autowired
	NotificationsUserRepository notificationsUserRep;
	@Autowired
	DeviseRepository deviseRepository;
	
	
	/*
	 * liste des offres sur les demandes de l'utilisateur (Notifications)
	 */
	@RequestMapping(value="/list_notification_offer/{idEnterprise}/{idUser}", method=RequestMethod.GET)
	public HashMap<String, Object> getRequestHasBankByBank(@PathVariable Integer idEnterprise,@PathVariable Integer idUser){
		HashMap<String, Object> h = new HashMap<String, Object>();
		List<Offer> listOffers;
		if(idEnterprise == null && idUser == null)
		{
           h.put("message", "1 ou plusieurs paramètres manquants.");
           h.put("status", 0);
           return h;
		}
		try
		{
			Enterprise ent = eRep.findOne(idEnterprise);
			if (ent == null)
			{
				h.put("message", "L'entreprise n'existe pas.");
				h.put("status", -1);
				return h;
			}
			User user = uERep.findOne(idUser);
			if (user == null)
			{
				h.put("message", "L'utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			listOffers = offerRep.findByEnterpriseAndIsValid(ent, user);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "Nouvelle offre d'une demande");
		h.put("list_offers_request", listOffers);
		h.put("status", 0);
		return h;
	}
	
	
	
	/*
	 * Select offer for request
	 */
	@RequestMapping(value="/select_offer/{idUser}/{idRequest}/{idOffer}", method=RequestMethod.POST)
	public HashMap<String, Object> selectOffer(@PathVariable Integer idUser,
											   @PathVariable Integer idRequest,
											   @PathVariable Integer idOffer) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		Offer offer;
		Request request;
		if(idUser == null || idRequest == null || idOffer == null)
        {
            h.put("status", -1);
            h.put("message", "1 ou plusieurs paramètres manquants");
            return h;
        }
		try
        {
			User user = uERep.findOne(idUser);
	        if(user == null)
	        {
	            h.put("status", -1);
	            h.put("message", "Cet utilisateur n'existe pas et/ou n'est pas d'une banque.");
	            return h;
	        }
	        request = rRep.findOne(idRequest);
            if(request == null)
            {
                h.put("status", -1);
                h.put("message", "Cette demande n'existe pas.");
                return h;
            }
	        offer = offerRep.findOne(idOffer);
            if(offer == null)
            {
                h.put("status", -1);
                h.put("message", "Cette offre n'existe pas.");
                return h;
            }
            //Verify if user in request's enterprise
            if(user.getGroupeIdGroupe().getInstitution().getIdInstitution() != request.getUserEntreprise().getGroupeIdGroupe().getInstitution().getIdInstitution())
            {
                h.put("status", -1);
                h.put("message", "L'entreprise de l'utilisateur n'a pas initié la demande.");
                return h;
            }
            //Verify if request correspond to offer's request
            if(offer.getDemandeIdDemande().getIdDemande() != request.getIdDemande())
            {
                h.put("status", -1);
                h.put("message", "Cette offre n'est pas rattachée à la demande.");
                return h;
            }
            if(request.getEtat() == 4)
            {
        	   h.put("status", -1);
               h.put("message", "Une offre est pré-selectionnée veuillez attendre la fin du processus.");
               return h;
            }
            if(request.getEtat() == 5)
            {
        	   h.put("status", -1);
               h.put("message", "Une offre est déjà selectionnée pour cette demande.");
               return h;
            }
            offer.setEtat(2);
            offer.setNextValidationGroup("Offre pré-selectionnée");
            request.setEtat(4);
            request.setNextValidationGroup("Offre en pré-sélection");
            offerRep.saveAndFlush(offer);
            rRep.saveAndFlush(request);
            List<Offer> offerList = offerRep.findByDemandeIdDemandeAndIsValid(request, true);
            //Others offers
            for(Offer of : offerList)
            {
        		if(of.getIdOffre() != offer.getIdOffre())
        		{
        			of.setNextValidationGroup("Offre non pré-selectionnée");
        			offerRep.saveAndFlush(of);
            	}
            }
            //Notify chain
            String emailSubject = "Pré-selection Offre";
			String emailBody = "Votre groupe fait partie de la chaine de validation.\\nVous recevrez un email de confirmation après validation des niveaux inférieurs";
			validationService.sendMailFirstPreSelection(offer, emailSubject, emailBody);
            //Notify banks that one is selected
            //validationService.sendMailToPreSelectedOfferInstitution(offer);  
        } catch (Exception e) {
            e.printStackTrace();
            h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
            h.put("status", -1);
            return h;
        }
		h.put("offer", offer);
        h.put("message", "L'offre est pré-sélectionnée.");
        h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value="/list/{idEntreprise}/{idProduct}", method=RequestMethod.GET)
	public HashMap<String, Object> getAllRequests(@PathVariable Integer idEntreprise, @PathVariable Integer idProduct) {
	       
	   HashMap<String, Object> h= new HashMap<String, Object>();
       ArrayList<RequestModelDetails> listRequestMD = new ArrayList<>();
       if (idEntreprise == null || idProduct == null)
       {
           h.put("message", "1 ou plusieurs paramètres manquants");
           h.put("status", 0);
           return h;
       }
       try {
           Enterprise e = eRep.findOne(idEntreprise);
           if (e == null) {
               h.put("message", "l'entreprise n'existe pas.");
               h.put("status", -1);
               return h;
           }
           Products pr = pRep.findOne(idProduct);
           if (pr == null) {
               h.put("message", "Le produit n'existe pas.");
               h.put("status", 0);
               return h;
           }
           
           List<Request> requests = rRep.findEnterpriseProduct(e, pr);
           for(Request request: requests)
           {
               ArrayList<Documents> listDoc = (ArrayList<Documents>) dRep.getRequestDocuments(request);
               ArrayList<Bank> listBank = (ArrayList<Bank>) bRep.getRequestBanks(request);
               
               RequestModelDetails requestMD = new RequestModelDetails();
               requestMD.setRequest(request);
               if(!listDoc.isEmpty())
                   requestMD.setDocuments(listDoc);
               if(!listBank.isEmpty())
                   requestMD.setIdsBank(listBank);
               listRequestMD.add(requestMD);
           }
       } catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
       h.put("message", "success");
       h.put("list_request_detail", listRequestMD);
       h.put("status", 0);
       return h;
	}
	
	/*
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public HashMap<String, Object> getAllRequests() {
		
		HashMap<String, Object> h= new HashMap<String, Object>();
		
		List<Request> requests = rRep.findAll();
		
		h.put("message", "success");
		h.put("list_request", requests);
		h.put("status", 0);
		return h;
	}
	*/
	
	@RequestMapping(value="/list/{id}", method=RequestMethod.GET)
	public HashMap<String, Object> getAllRequest(@PathVariable int id) {
		
		HashMap<String, Object> h= new HashMap<String, Object>();
		Request request = rRep.findOne(id);
		
		if (request == null) {
			h.put("message", "La demande n'existe pas.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "success");
		h.put("request", request);
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value="/lister/{id}", method=RequestMethod.GET)
	public HashMap<String, Object> getRequestByUserEntreprise(@PathVariable int id){
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		UserEntreprise user = uERep.findOne(id);
		if (user == null) {
			h.put("message", "L'utilisateur n'existe pas.");
			h.put("status", -1);
			return h;
		}
		
		List<Request> requests = rRep.findByUserEntreprise(user);
		h.put("demande_list", requests);
		h.put("message", "success");
		h.put("status", 0);
		return h;
	}

	/*@RequestMapping(value="/add/{id_user}/{idsBank}", method=RequestMethod.POST)
	public HashMap<String, Object> add(@RequestBody Request request, 
			@PathVariable("id_user") int idUser, 
			@PathVariable("idsBank") Integer[] idsBank
			) {
		
		HashMap<String, Object> h= new HashMap<String, Object>();
		UserEntreprise user = uERep.findOne(idUser);
		
		h = handleAdd(user, request);
		
		return h;
	}*/
	
	@RequestMapping(value="/add/{idUser}", method=RequestMethod.POST)
	public HashMap<String, Object> add(@RequestBody RequestModel requestModel, @PathVariable int idUser) {
		HashMap<String, Object> h= new HashMap<String, Object>();
		UserEntreprise user = uERep.findOne(idUser);
		System.out.println();
		h = handleAdd(user, requestModel.getRequest());
		for(Map.Entry entry : h.entrySet()){
			if (entry.getKey() == "status" && (int)entry.getValue() == -1 ) {
				return h;
			}
		}
		//Store documents in DocumentsHasRequest 
		//addDocuments(((Request) requestDocument.getRequest()).getIdDemande(), requestDocument.getDocuments());
		
		//Add banks in RequestHasBank
		if (h.containsKey("request")) {
			//&&  (int) entry.getValue() == 0
			List <RequestHasBank> rHBs = new ArrayList<>();
			for(Map.Entry entry : h.entrySet()){
				if (entry.getKey() == "request" ) {
					//HashMap<String, Object> hash = new HashMap<String, Object>();
					Request request = (Request) entry.getValue();
					try {
						ArrayList<Bank> banks = banks(requestModel.getIdsBank());
						rHBs = notifyBanque(request, banks);
						
						/*String emailSubject = env.getProperty("notification.request.email.subject");
						String emailBody = env.getProperty("notification.request.email.body");*/
						
						//Votre groupe fait partie de la chaine de validation
						validationService.sendMailFirst(request);
					} catch (Exception e) {
						e.printStackTrace();
						h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
						h.put("status", -1);
						return h;
					}
				}
			}
		}
		
		//Store documents in DocumentsHasRequest
		if(requestModel.getDocuments() != null)
		{
			addDocuments((requestModel.getRequest()).getIdDemande(), requestModel.getDocuments());
		}
		h.put("message", "La demande est ajoutée avec succès.");
		h.put("request", requestModel);
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value="/update/{idReq}/{id_user}", method=RequestMethod.PUT)
    public HashMap<String, Object> update(@RequestBody RequestModel requestModel, 
            @PathVariable("id_user") int idUser, 
            @PathVariable("idReq") int idReq){
        
        // on récupère dabord l'ancienne demande avec son id passé en param
        Request oldRequest = rRep.findOne(idReq);
        UserEntreprise user = uERep.findOne(idUser);
        // on récupère la demande modifier grace à RequestModel
        Request request = requestModel.getRequest();
        HashMap<String, Object> h = handleUpdate(oldRequest, request, user);
        
        // On teste si la demande a été bien modifié avant de passer
        for(Map.Entry entry : h.entrySet()){
            if (entry.getKey() == "status" && (int)entry.getValue() == -1 ) {
                return h;
            }
        }
        
        // on modifie les banques à positionner sur la demande
        if (h.containsKey("request")) {
            
            List <RequestHasBank> rHBs = new ArrayList<>();
            for(Map.Entry entry : h.entrySet()){
                if (entry.getKey() == "request" ) {
                    Request req = (Request) entry.getValue();
                    try {
                        
                        // On supprime ts ls banques positionnées sur la demande
                        List<RequestHasBank> hasBanks = rHBRep.findByRequest(req);
                        hasBanks.forEach(hasBank->{
                            rHBRep.delete(hasBank);
                        });
                        
                        // on repossitionne à nouveau les banques sur la demande
                        ArrayList<Bank> banks = banks(requestModel.getIdsBank());
                        rHBs = notifyBanque(request, banks);
                    } catch (Exception e) {
                        e.printStackTrace();
                        h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
                        h.put("status", -1);
                        return h;
                    }
                    
                    List<DocumentsHasRequest> documentsHasRequests = drRepository.findAllDocumentByRequest(req);
                    
                    documentsHasRequests.forEach(documentHasRequest->{
                        drRepository.delete(documentHasRequest);
                    });
                    
                    if(requestModel.getDocuments() != null)
            		{
                    	Integer[] idsDoc = requestModel.getDocuments();
                    	addDocuments(request.getIdDemande(), idsDoc);
            		}
                }
            }
        }
        
        h.put("message", "La modification de la demande s'est effectuée avec succès.");
        h.put("request", requestModel);
        h.put("status", 0);
        return h;
    }
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.DELETE)
	public HashMap<String, Object> delete(@PathVariable int id){
		HashMap<String, Object> h = new HashMap<>();
		Request request = rRep.findOne(id);
		if (request == null) {
			h.put("message", "La demande n'existe pas.");
			h.put("status", -1);
			return h;			
		}
		try {
			notificationsUserRep.deleteByIdRequest(request.getIdDemande());
			rRep.delete(request);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "La demande est supprimée avec succès.");
		h.put("status", 0);
		return h;
	}

	public HashMap<String, Object> handleAdd(UserEntreprise user, Request request) {
		HashMap<String, Object> h= new HashMap<String, Object>();
		System.out.println("88888");
		if (user == null) {
			h.put("message", "L'utilisateur n'existe pas");
			h.put("status", -1);
			return h;
		}
		System.out.println("999999");
		try {
			//
			Products product = pRep.findOne(request.getProduct().getIdProduits());
			if (product == null)
			{
				h.put("message", "Le produit associé à la demande n'existe pas.");
				h.put("status", -1);
				return h;
			}
			request.setUserEntreprise(user);
			Institution enterprise = eRep.findOne(request.getUserEntreprise().getGroupeIdGroupe().getInstitution().getIdInstitution());
			if(enterprise == null)
			{
				h.put("message", "L'entreprise de l'utilisateur n'existe pas.");
				h.put("status", -1);		
				return h;
			}
			
			//
			Devise devise = deviseRepository.findOne(request.getDevise().getIdDevise());
			if(devise == null)
			{
				h.put("message", "La devise n'existe pas.");
				h.put("status", -1);
				return h;
			}
			//
			
			ValidationLevel validationLevel = vlRep.findByProductAndInstitutionAndSens(product, enterprise, "request");
			if(validationLevel == null)
			{
				h.put("message", "Ce niveau de validation n'existe pas.");
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
			request.setIsValid(false);
			request.setNextValidationGroup("");
			request.setEtat(0);
			request = rRep.save(request);
			String id = String.valueOf(request.getIdDemande());
			String prefProduct = product.getNom().substring(0, 1);
			System.out.println("Prefixe "+ prefProduct + "Id "+id);
			String suffEnteprise = (request.getUserEntreprise().getGroupeIdGroupe().getInstitution().getNom()).substring(0, 3).toUpperCase();
			System.out.println("Suffixe "+ suffEnteprise); 
			request.setNumeroRequest(prefProduct+id+suffEnteprise);
			rRep.saveAndFlush(request);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "success");
		h.put("request", request);
		h.put("status", 0);
		
		return h;
	}
	
	public HashMap<String, Object> handleUpdate(Request oldRequest, Request request, UserEntreprise user) {
		
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		if (user == null) {
			h.put("message", "L'utilisateur n'existe pas.");
			h.put("status", -1);
			return h;
		}
		if (oldRequest == null) {
			h.put("message", "La demande n'existe pas.");
			h.put("status", -1);
			return h;
		}
		if (oldRequest.getEtat() == 1) {
            h.put("message", "La demande a déjà été validée.");
            h.put("status", -1);
            return h;
        }
		if(offerRep.existsByDemandeIdDemande(oldRequest))
		{
			h.put("message", "Cette demande ne peut être modifiée, une offre est déjà positionnée.");
			h.put("status", -1);
			return h;
		}
		/*
		if (nullable(request)) {
			h.put("message", "au moins un des champs obligatoires est vide");
			h.put("status", -1);
			return h;
		}
		if (!compareDate(request.getDateFinDemande(), request.getDateDebutDemande())) {
			h.put("message", "la date de fin doit être supérieur à la date de début");
			h.put("status", -1);
			return h;
		}
		*/
		try {
			request.setIsValid(oldRequest.getIsValid());
			request.setEtat(oldRequest.getEtat());
			request.setIdDemande(oldRequest.getIdDemande());
			request.setNumeroRequest(oldRequest.getNumeroRequest());
			request.setUserEntreprise(user);
			request.setNextValidationGroup(oldRequest.getNextValidationGroup());
			rRep.saveAndFlush(request);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "success");
		h.put("request", request);
		h.put("status", 0);
		return h;
	}
	
	/* récupère la liste des demandes par entreprise et par produit
    */
	/*
   @RequestMapping(value="/list/{idEnpr}/{idPr}", method= RequestMethod.GET)
   public HashMap<String, Object> getRequestByEnterpriseAndProduct(@PathVariable("idEnpr") int idEnpr,
           @PathVariable("idPr") int idPr){
       
       HashMap<String, Object> h = new HashMap<>();
       Enterprise e = eRep.findOne(idEnpr);
       System.out.println(e);
       if (e == null) {
           h.put("message", "l'entreprise n'existe pas");
           h.put("status", -1);
           return h;
       }
       Products pr = pRep.findOne(idPr);
       System.out.println(pr);
       if (pr == null) {
           h.put("message", "le produit n'existe pas");
           h.put("status", 0);
           return h;
       }
       List<Request> requests = rRep.findEnterpriseProduct(e, pr);
       h.put("message", "liste des demandes");
       h.put("requests", requests);
       h.put("status", 0);
       return h;
   	}
   */
/*
	public Boolean compareDate(Date dateFin, Date dateDebut){
		
		if(dateFin.compareTo(dateDebut) <= 0){
			return false;
		}
		return true;
	}
	
	public Boolean nullable(Request request) {
		
		if (request.getDateValeur().equals("") || request.getMontant().equals("") || request.getDevise().equals("")
				|| request.getDateDebutDemande().equals("") || request.getDateFinDemande().equals("")) {
			return true;
		}
		return false;
	}
	*/
	
	public void addDocuments(Integer idRequest, Integer[] tabDocument) {
		Request request = rRep.findOne(idRequest);
		if(request != null)
		{
			for(int i = 0; i < tabDocument.length; i++)
			{
				Documents document = dRep.findOne(tabDocument[i]);
				if(document != null)
				{
					DocumentsHasRequest dr = new DocumentsHasRequest();
					dr.setRequest(request);
					dr.setDocuments(document);
					dr.setDateDocument(new Date());
					dr.setDocumentsHasRequestPK(new DocumentsHasRequestPK(document.getIdDocuments(), request.getIdDemande()));
					dr = drRepository.save(dr);
				}
			}
		}
	}
	
	public ArrayList<Bank> banks(Integer [] ids){
		
		ArrayList<Bank> banks = new ArrayList<>();
		for (Integer i : ids) {
			Bank bank = bRep.findOne(i);
			if (bank != null) {
				banks.add(bank);
			}
		}
		return banks;
	}
	
	public List<RequestHasBank>  notifyBanque(Request req, List<Bank> banks){
		
		List<RequestHasBank> hasBanks = new ArrayList<>();
		banks.forEach(bank->{
			RequestHasBank rHB = new RequestHasBank();
			rHB = addRequestHasBank(req, bank);
			rHB = rHBRep.save(rHB);
			hasBanks.add(rHB);
		});
		return hasBanks;
	}
	
		public RequestHasBank addRequestHasBank(Request req, Bank bank) {
		
    	RequestHasBank rHB = new RequestHasBank();
    	rHB.setRequestHasBankPK(new RequestHasBankPK(req.getIdDemande(), bank.getIdInstitution()));
    	
    	rHB.setBank(bank);
    	rHB.setRequest(req);
    	bank.getRequestHasBankList().add(rHB);
    	req.getRequestHasBankList().add(rHB);
    	return rHB;
	}
	
		
}
