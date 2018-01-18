package com.qualshore.etreasury.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.qualshore.etreasury.dao.ActionRepository;
import com.qualshore.etreasury.dao.LogRepository;
import com.qualshore.etreasury.entity.Log;

@RequestMapping("etreasury_project/login/logout")
@RestController
public class LogRestController {

	@Autowired
	LogRepository logRepository;
	
	@Autowired
	ActionRepository actionRepository;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> list()
	{
		HashMap<String, Object> h = new HashMap<String, Object>();
		try
		{
			List<Log> logList = logRepository.findAll();
			
			h.put("status", 0);
			h.put("log_list", logList);
			h.put("message", "list OK");
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		return h;
	}
	
	/*@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> add(@RequestBody RateDay rateDay, @PathVariable Integer idUserBank) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(rateDay == null || idUserBank == null || rateDay.getDateValeur() == null || rateDay.getValeur() == null)
		{
			h.put("message", "paramètres vides");
			h.put("status", -1);
			return h;
		}
		try
		{
			Integer idProduct = rateDay.getProduitsIdProduits().getIdProduits();
			Products product = productsRepository.findOne(idProduct);
			if(product == null)
			{
				h.put("message", "le produit n'existe pas");
				h.put("status", -1);
				return h;
			}
			UserBanque userBank = userBankRepository.findOne(idUserBank);
			if(userBank == null)
			{
				h.put("message", "cet utilisateur n'existe pas et/ ou n'est pas de type banque");
				h.put("status", -1);
				return h;
			}
			Bank bank = userBankRepository.getBankByUser(idUserBank);
			if(bank == null)
			{
				h.put("message", "la banque de l'utilisateur n'existe pas");
				h.put("status", -1);
				return h;
			}
			
			List<RateDay> listRate = rateRepository.findByDateDebutValidite(rateDay.getDateDebutValidite());
			if(listRate.isEmpty() || listRate == null)
			{
				h.put("message", "le taux_du_jour est déjà renseigné");
				h.put("status", -1);
				return h;
			}
			
			if(! isDateSup(rateDay.getDateValeur(), new Date()))
			{
				h.put("message", "date_valeur ne doit pas être inferieur à la date du jour");
				h.put("status", -1);
				return h;
			}
			
			if(! isDateSup(rateDay.getDateFinValidite(), rateDay.getDateValeur()))
			{
				h.put("message", "date_valeur ne doit pas être superieur à date_fin_de_validite");
				h.put("status", -1);
				return h;
			}
			
			rateDay.setProduitsIdProduits(product);
			rateDay.setBank(bank);
			rateDay = rateRepository.save(rateDay);
		} catch (Exception e) {
			h.put("message", e.getMessage());
			h.put("status", -1);
			return h;
		}
		h.put("rateDay", rateDay);
		h.put("message", "taux_du_jour ajouté avec succès");
		h.put("status", 0);
		return h;
	}*/
}
