package com.qualshore.etreasury.rest;

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

import com.qualshore.etreasury.dao.BankRepository;
import com.qualshore.etreasury.dao.CommissionRepository;
import com.qualshore.etreasury.dao.RateDayRepository;
import com.qualshore.etreasury.dao.UserBankRepository;
import com.qualshore.etreasury.entity.Bank;
import com.qualshore.etreasury.entity.Commission;
import com.qualshore.etreasury.entity.RateDay;
import com.qualshore.etreasury.entity.User;
import com.qualshore.etreasury.entity.UserBanque;

@RequestMapping("etreasury_project/mon_espace/bank/rate_of_day/commission")
@RestController
public class CommissionRestController {

	@Autowired
	CommissionRepository commissionRepository;
	
	@Autowired
	RateDayRepository rateDayRepository;
	
	@Autowired
	UserBankRepository userBankRepository;
	
	@Autowired
	BankRepository bankRepository;
	
	@RequestMapping(value = "/list/{idUserBank}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> list(@PathVariable Integer idUserBank)
	{
		HashMap<String, Object> h = new HashMap<String, Object>();
		try
		{
			User user = userBankRepository.findOne(idUserBank);
			if(user == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'existe pas et/ ou n'est pas de type banque.");
				return h;
			}
			
			Integer idBank = user.getGroupeIdGroupe().getInstitution().getIdInstitution();
			Bank bank = bankRepository.findOne(idBank);
			if(bank == null)
			{
				h.put("status", -1);
				h.put("message", "La banque de l'utilisateur n'existe pas.");
				return h;
			}
			List<Commission> commissionList = commissionRepository.findByBank(bank);
			
			h.put("status", 0);
			h.put("commission_list", commissionList);
			h.put("message", "list OK");
		} catch (Exception e) {
			e.printStackTrace();
			h.put("status", -1);
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			return h;
		}
		return h;
	}
	
	@RequestMapping(value = "/add/{idUserBank}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> add(@RequestBody Commission commission, @PathVariable Integer idUserBank) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(commission == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants.");
			h.put("status", -1);
			return h;
		}
		try
		{
			Integer idRateDay = commission.getTauxJourIdTauxJour().getIdTauxJour();
			RateDay rateDay = rateDayRepository.findOne(idRateDay);
			if(rateDay == null)
			{
				h.put("message", "Le taux du jour n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			UserBanque userBank = userBankRepository.findOne(idUserBank);
			if(userBank == null)
			{
				h.put("message", "Cet utilisateur n'existe pas et/ ou n'est pas de type banque.");
				h.put("status", -1);
				return h;
			}
			
			/* Get bank of user*/
			Bank bank = userBankRepository.getBankByUser(idUserBank);
			if(bank == null)
			{
				h.put("message", "La banque de l'utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			if(! rateDay.getBank().getIdInstitution().equals(bank.getIdInstitution()))
			{
				h.put("message", "La banque de l'utilisateur et celle du taux ne correspondent pas.");
				h.put("status", -1);
				return h;
			}
			
			commission.setTauxJourIdTauxJour(rateDay);
			commission = commissionRepository.save(commission);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("commission", commission);
		h.put("message", "Commission ajoutée avec succès");
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value = "/update/{idUserBank}/{idCommission}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> update(@RequestBody Commission commission, @PathVariable Integer idUserBank, @PathVariable Integer idCommission) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(commission == null || idCommission == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
			return h;
		}
		try
		{
			Commission commissionOld = commissionRepository.findOne(idCommission);
			if(commissionOld == null)
			{
				h.put("message", "Cette commission n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			UserBanque userBank = userBankRepository.findOne(idUserBank);
			if(userBank == null)
			{
				h.put("message", "Cet utilisateur n'existe pas et/ ou n'est pas de type banque.");
				h.put("status", -1);
				return h;
			}
			
			/* Get bank of user*/
			Bank bank = userBankRepository.getBankByUser(idUserBank);
			if(bank == null)
			{
				h.put("message", "La banque de l'utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			if(commission.getTauxJourIdTauxJour() == null)
			{
				commission.setTauxJourIdTauxJour(commissionOld.getTauxJourIdTauxJour());
			}
			else
			{
				Integer idRateDay = commission.getTauxJourIdTauxJour().getIdTauxJour();
				RateDay rateDay = rateDayRepository.findOne(idRateDay);
				if(! rateDay.getBank().getIdInstitution().equals(bank.getIdInstitution()))
				{
					h.put("message", "La banque de l'utilisateur et celle du taux ne correspondent pas.");
					h.put("status", -1);
					return h;
				}
				commission.setTauxJourIdTauxJour(rateDay);
			}
			
			if(commission.getCommissionTransfert() == null)
			{
				commission.setCommissionTransfert(commissionOld.getCommissionTransfert());
			}
			if(commission.getDescription() == null)
			{
				commission.setDescription(commissionOld.getDescription());
			}
			if(commission.getNature() == null)
			{
				commission.setNature(commissionOld.getNature());
			}
			if(commission.getType() == null)
			{
				commission.setType(commissionOld.getType());
			}
			
			commission.setIdCommission(idCommission);
			commission = commissionRepository.saveAndFlush(commission);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("commission", commission);
		h.put("message", "La commission est mise à jour avec succès.");
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value = "/delete/{idUserBank}/{idCommission}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> delete(@PathVariable Integer idCommission, @PathVariable Integer idUserBank) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		if(idCommission == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
			return h;
		}
		try
		{
			Commission commission = commissionRepository.findOne(idCommission);
			if(commission == null)
			{
				h.put("message", "Cette commission n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			Integer idRateDay = commission.getTauxJourIdTauxJour().getIdTauxJour();
			RateDay rateDay = rateDayRepository.findOne(idRateDay);
			if(rateDay == null)
			{
				h.put("message", "Le taux du jour n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			UserBanque userBank = userBankRepository.findOne(idUserBank);
			if(userBank == null)
			{
				h.put("message", "Cet utilisateur n'existe pas et/ ou n'est pas de type banque.");
				h.put("status", -1);
				return h;
			}
			
			/* Get bank of user*/
			Bank bank = userBankRepository.getBankByUser(idUserBank);
			if(bank == null)
			{
				h.put("message", "La banque de l'utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			if(! rateDay.getBank().getIdInstitution().equals(bank.getIdInstitution()))
			{
				h.put("message", "La banque de l'utilisateur et celle du taux ne correspondent pas.");
				h.put("status", -1);
				return h;
			}
			
			commissionRepository.delete(idCommission);
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("status", -1);
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			return h;
		}
		h.put("status", 0);
		h.put("message", "La commission est supprimée avec succès.");
		return h;
	}
}
