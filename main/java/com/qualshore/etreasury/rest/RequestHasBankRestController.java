package com.qualshore.etreasury.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qualshore.etreasury.dao.BankRepository;
import com.qualshore.etreasury.dao.CommissionOfferRepository;
import com.qualshore.etreasury.dao.DocumentHasOfferRepository;
import com.qualshore.etreasury.dao.DocumentsRepository;
import com.qualshore.etreasury.dao.EnterpriseRepository;
import com.qualshore.etreasury.dao.OfferRepository;
import com.qualshore.etreasury.dao.ProductsRepository;
import com.qualshore.etreasury.dao.RequestHasBankRepository;
import com.qualshore.etreasury.dao.RequestRepository;
import com.qualshore.etreasury.entity.Bank;
import com.qualshore.etreasury.entity.CommissionOffer;
import com.qualshore.etreasury.entity.Documents;
import com.qualshore.etreasury.entity.DocumentsHasOffer;
import com.qualshore.etreasury.entity.Enterprise;
import com.qualshore.etreasury.entity.Offer;
import com.qualshore.etreasury.entity.Products;
import com.qualshore.etreasury.entity.Request;
import com.qualshore.etreasury.entity.RequestHasBank;
import com.qualshore.etreasury.entity.RequestHasBankPK;
import com.qualshore.etreasury.model.OfferModelDisplay;
import com.qualshore.etreasury.model.OfferModelValidation;

@RequestMapping("/etreasury_project/mes_operations/entreprise/request_has_bank")
@RestController
public class RequestHasBankRestController {
	
	@Autowired
	RequestHasBankRepository rHBRep;
	@Autowired
	BankRepository bRep;
	@Autowired
	ProductsRepository pRep;
	@Autowired
	RequestRepository rRep;
	@Autowired
	OfferRepository offerRepository;
	@Autowired
	CommissionOfferRepository commissionOfferRepository;
	@Autowired
	DocumentHasOfferRepository dhoRepository;
	@Autowired
	DocumentsRepository documentsRepository;
	@Autowired
	EnterpriseRepository eRep;
	
	/*
	 * list of all request has bank
	 */
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public HashMap<String, Object> getAllRequestHasBank(){
			
		HashMap<String, Object> h = new HashMap<>();
		List<RequestHasBank> hasBanks = rHBRep.findAll();
		h.put("message", "success");
		h.put("list_req_has_bank", hasBanks);
		h.put("status", 0);
		return h;
	}
	
	/*
	 * get request_has_bank by bank and request
	 */
	@RequestMapping(value="/list/{idDemande}/{idBank}", method = RequestMethod.GET)
	public HashMap<String, Object> getRequestHasBank(@PathVariable("idDemande") int idDemande,
			@PathVariable("idBank") int idBank){
			
		HashMap<String, Object> h = new HashMap<>();
		RequestHasBankPK req = new RequestHasBankPK(idDemande, idBank);
		RequestHasBank hasBank = rHBRep.findByRequestHasBankPK(req);
		if (hasBank == null) {
			h.put("message", "La banque associée à la demande n'existe pas.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "success");
		h.put("req_has_bank", hasBank);
		h.put("status", 0);
		return h;
	}
	
	/*
     * liste des offres par entreprise et demande
     */
    @RequestMapping(value="/list_offer_entreprise/{idRequest}/{idEntreprise}", method=RequestMethod.GET)
    public HashMap<String, Object> getOfferByRequestAndEntreprise(@PathVariable Integer idRequest, @PathVariable Integer idEntreprise){
        HashMap<String, Object> h = new HashMap<String, Object>();
        ArrayList<OfferModelDisplay> requestHBList = new ArrayList<OfferModelDisplay>();
        if(idRequest == null || idEntreprise == null)
        {
           h.put("message", "1 ou plusieurs paramètres manquants");
           h.put("status", 0);
           return h;
        }
        try
        {
            Request request = rRep.findOne(idRequest);
            if (request == null)
            {
                h.put("message", "Cette demande n'existe pas.");
                h.put("status", -1);
                return h;
            }
            Enterprise entreprise = eRep.findOne(idEntreprise);
            if (entreprise == null)
            {
                h.put("message", "Cette entreprise n'existe pas.");
                h.put("status", -1);
                return h;
            }
            if(request.getUserEntreprise().getGroupeIdGroupe().getInstitution().getIdInstitution() != entreprise.getIdInstitution())
            {
                h.put("message", "La demande n'est pas de cette entreprise.");
                h.put("status", -1);
                return h;
            }
            //Request req = rRep.findByEnterprise(e)
            List<Offer> offerList = offerRepository.findByDemandeIdDemandeAndIsValid(request,true);
            //List<Offer> offerList = offerRepository.findByDemandeIdDemande(request);
            for(int i=0; i<offerList.size(); i++)
            {
                Offer offer = offerList.get(i);
                OfferModelDisplay offerModel =  new OfferModelDisplay();
                offerModel.setOffer(offer);
                offerModel.setDocuments(getDocuments(offer.getIdOffre()));
                offerModel.setCommissions(getCommissions(offer.getIdOffre()));
            
                requestHBList.add(offerModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
            h.put("status", -1);
            return h;
        }
        h.put("message", "success");
        h.put("list_offer_by_request", requestHBList);
        h.put("status", 0);
        return h;
    }
    
	/*
	 * delete a request_has_bank
	 */
	@RequestMapping(value="/delete/{idDemande}/{idBank}", method = RequestMethod.DELETE)
	public HashMap<String, Object> delRequestHasBank(@PathVariable("idDemande") int idDemande,
			@PathVariable("idBank") int idBank){
			
		HashMap<String, Object> h = new HashMap<>();
		RequestHasBankPK req = new RequestHasBankPK(idDemande, idBank);
		RequestHasBank hasBank = rHBRep.findByRequestHasBankPK(req);
		if (hasBank == null) {
			h.put("message", "La banque associée à  la demande n'existe pas.");
			h.put("status", -1);
			return h;
		}
		try {
			rHBRep.delete(hasBank);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "success");
		h.put("status", 0);
		return h;
	}
	
	/*
	 * list of request_has_bank by product and bank
	 */
	@RequestMapping(value="/lists/{idBank}/{idProduct}", method=RequestMethod.GET)
	public HashMap<String, Object> getRequestHasBankByBankAndProduct(@PathVariable Integer idBank, @PathVariable Integer idProduct){
		HashMap<String, Object> h = new HashMap<String, Object>();
		ArrayList<OfferModelValidation> list = new ArrayList<>();
		List<RequestHasBank> hasBanks;
		if(idBank == null || idProduct == null)
		{
           h.put("message", "1 ou plusieurs paramètres manquants");
           h.put("status", 0);
           return h;
		}
		try
		{
			Bank b = bRep.findOne(idBank);
			if (b == null)
			{
				h.put("message", "Cette banque n'existe pas.");
				h.put("status", -1);
				return h;
			}
			Products p = pRep.findOne(idProduct);
			if (p == null)
			{
				h.put("message", "Ce produit n'existe pas.");
				h.put("status", -1);
				return h;
			}
			hasBanks = rHBRep.findRequestHasBankByProductAndBankAndIsValid(p, b);
			System.out.println("HAs bank "+hasBanks);
			for(RequestHasBank hasBank : hasBanks)
			{
				OfferModelValidation offerModel = new OfferModelValidation();
				offerModel.setRequestHasBank(hasBank);
				List<Offer> offerList = offerRepository.findByInstitutionAndDemande(b, hasBank.getRequest());
				if(offerList != null && !offerList.isEmpty())
					offerModel.setOffer(offerList.get(0));
				//System.out.println("offerRepository bank "+offerRepository.findByInstitutionAndDemande(b, hasBank.getRequest()).get(0));
				list.add(offerModel);
			}
			
			System.out.println("List offerModel "+list);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "List des banques associées a la demande");
		h.put("list_request_has_bank", list);
		h.put("status", 0);
		return h;
	}
	
	/*
	@RequestMapping(value="/lists/{idBank}/{idProduct}", method=RequestMethod.GET)
	public HashMap<String, Object> getRequestHasBankByBankAndProduct(@PathVariable Integer idBank, @PathVariable Integer idProduct){
		HashMap<String, Object> h = new HashMap<String, Object>();
		List<RequestHasBank> hasBanks;
		if(idBank == null || idProduct == null)
		{
           h.put("message", "1 ou plusieurs paramètres manquants");
           h.put("status", 0);
           return h;
		}
		try
		{
			Bank b = bRep.findOne(idBank);
			if (b == null)
			{
				h.put("message", "cette banque n'existe pas");
				h.put("status", -1);
				return h;
			}
			Products p = pRep.findOne(idProduct);
			if (p == null)
			{
				h.put("message", "ce produit n'existe pas");
				h.put("status", -1);
				return h;
			}
			hasBanks = rHBRep.findRequestHasBankByProductAndBankAndIsValid(p, b);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "success");
		h.put("list_request_has_bank", hasBanks);
		h.put("status", 0);
		return h;
	}*/
	
	/*
	 * list des demandes a traiter par l'entreprise 
	 */
	@RequestMapping(value="/list_by_enterprise/{idBank}", method=RequestMethod.GET)
	public HashMap<String, Object> getRequestHasBankByBank(@PathVariable Integer idBank){
		HashMap<String, Object> h = new HashMap<String, Object>();
		List<RequestHasBank> hasBanks;
		if(idBank == null)
		{
           h.put("message", "1 ou plusieurs paramètres manquants");
           h.put("status", 0);
           return h;
		}
		try
		{
			Bank b = bRep.findOne(idBank);
			if (b == null)
			{
				h.put("message", "Cette banque n'existe pas.");
				h.put("status", -1);
				return h;
			}
			hasBanks = rHBRep.findRequestHasBankByBankAndIsValid(b);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "Nouvelle demande à traiter");
		h.put("list_request_has_bank", hasBanks);
		h.put("status", 0);
		return h;
	}
	
	/*
	 * list of request_has_bank by product and bank
	 */
	@RequestMapping(value="/list_offer/{idRequest}/{idBank}", method=RequestMethod.GET)
	public HashMap<String, Object> getOfferByRequest(@PathVariable Integer idRequest, @PathVariable Integer idBank){
		HashMap<String, Object> h = new HashMap<String, Object>();
		ArrayList<OfferModelDisplay> requestHBList = new ArrayList<OfferModelDisplay>();
		if(idRequest == null || idBank == null)
		{
           h.put("message", "1 ou plusieurs paramètres manquants");
           h.put("status", 0);
           return h;
		}
		try
		{
			Request request = rRep.findOne(idRequest);
			if (request == null)
			{
				h.put("message", "Cette demande n'existe pas.");
				h.put("status", -1);
				return h;
			}
			Bank bank = bRep.findOne(idBank);
			if (bank == null)
			{
				h.put("message", "Cette banque n'existe pas.");
				h.put("status", -1);
				return h;
			}
			List<Offer> offerList = offerRepository.findByInstitutionAndDemande(bank, request);
			for(int i=0; i<offerList.size(); i++)
			{
				Offer offer = offerList.get(i);
				OfferModelDisplay offerModel =  new OfferModelDisplay();
				offerModel.setOffer(offer);
				offerModel.setDocuments(getDocuments(offer.getIdOffre()));
				offerModel.setCommissions(getCommissions(offer.getIdOffre()));
			
				requestHBList.add(offerModel);
			}
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "success");
		h.put("list_offer_by_request", requestHBList);
		h.put("status", 0);
		return h;
	}
	
	public ArrayList<CommissionOffer> getCommissions(Integer idOffer) {
		Offer offer = offerRepository.findOne(idOffer);
		ArrayList<CommissionOffer> result = new ArrayList<CommissionOffer>();
		if(offer != null)
		{
			List<CommissionOffer> commissionList = commissionOfferRepository.findByOffer(offer);
			for(int i = 0; i < commissionList.size(); i++)
			{
				result.add(commissionList.get(i));
			}
		}
		return result;
	}
	
	public ArrayList<Documents> getDocuments(Integer idOffer) {
		Offer offer = offerRepository.findOne(idOffer);
		ArrayList<Documents> result = new ArrayList<Documents>();
		if(offer != null)
		{
			List<DocumentsHasOffer> documentOffer = dhoRepository.findByOffer(offer);
			for(int i = 0; i < documentOffer.size(); i++)
			{
				result.add(documentOffer.get(i).getDocuments());
			}
		}
		return result;
	}
}