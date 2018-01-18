package com.qualshore.etreasury.rest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.qualshore.etreasury.dao.BankRepository;
import com.qualshore.etreasury.dao.DeviseRepository;
import com.qualshore.etreasury.dao.EnterpriseRepository;
import com.qualshore.etreasury.dao.ProductsRepository;
import com.qualshore.etreasury.dao.RateDayRepository;
import com.qualshore.etreasury.dao.UserBankRepository;
import com.qualshore.etreasury.dao.UserEntrepriseRepository;
import com.qualshore.etreasury.entity.Bank;
import com.qualshore.etreasury.entity.Devise;
import com.qualshore.etreasury.entity.Enterprise;
import com.qualshore.etreasury.entity.Locality;
import com.qualshore.etreasury.entity.Products;
import com.qualshore.etreasury.entity.RateDay;
import com.qualshore.etreasury.entity.User;
import com.qualshore.etreasury.entity.UserBanque;
import com.qualshore.etreasury.model.RateDayModel;
@RequestMapping("etreasury_project/mon_espace/bank/rate_of_day")
@RestController
public class RateDayRestController {
	@Autowired
	RateDayRepository rateRepository;

	@Autowired
	UserBankRepository userBankRepository;
	@Autowired
	UserEntrepriseRepository userEnterpriseRepository;
	@Autowired
	DeviseRepository dRep;
	@Autowired
	BankRepository bankRepository;
	@Autowired
	EnterpriseRepository enterpriseRepository;
	@Autowired
	ProductsRepository productsRepository;

	@RequestMapping(value = "/list/{idUserBank}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> list(@PathVariable Integer idUserBank)
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
			List<RateDay> rateList = rateRepository.findByBank(bank);

			h.put("status", 0);
			h.put("rate_day_list", rateList);
			h.put("message", "list OK");
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		return h;
	}

	@RequestMapping(value = "/list/rate_day/{idUserEnterprise}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> listEnterprise(@PathVariable Integer idUserEnterprise)
	{
		HashMap<String, Object> h = new HashMap<String, Object>();

		if(idUserEnterprise == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		try
		{
			User user = userEnterpriseRepository.findOne(idUserEnterprise);
			if(user == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'existe pas et/ ou n'est pas de type entreprise.");
				return h;
			}
			Integer idEntreprise = user.getGroupeIdGroupe().getInstitution().getIdInstitution();
			Enterprise enterprise = enterpriseRepository.findOne(idEntreprise);
			if(enterprise == null)
			{
				h.put("status", -1);
				h.put("message", "L'entreprise de l'utilisateur n'existe pas.");
				return h;
			}
			List<RateDay> rateList = rateRepository.findByLocality(user.getGroupeIdGroupe().getInstitution().getLocalityIdLocalite());

			h.put("status", 0);
			h.put("rate_day_list_bank", rateList);
			h.put("message", "list OK");
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		return h;
	}


	/* @RequestMapping(value = "/add/{idUserBank}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Map<String,Object> add(@RequestBody RateDay rateDay, @PathVariable Integer idUserBank) {
        HashMap<String, Object> h = new HashMap<String, Object>();
        if(rateDay == null || idUserBank == null || rateDay.getDateValeur() == null || rateDay.getProduitsIdProduits().getIdProduits() == null)
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
                h.put("message", "Le produit n'existe pas");
                h.put("status", -1);
                return h;
            }

             Verify if user is user_bank 
            UserBanque userBank = userBankRepository.findOne(idUserBank);
            if(userBank == null)
            {
                h.put("message", "Cet utilisateur n'existe pas et/ ou n'est pas de type banque");
                h.put("status", -1);
                return h;
            }

             Get bank of user
            Bank bank = userBankRepository.getBankByUser(idUserBank);
            if(bank == null)
            {
                h.put("message", "la banque de l'utilisateur n'existe pas");
                h.put("status", -1);
                return h;
            }

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.now();
           System.out.println("Date now "+ new Date());
            List<RateDay> listRate = rateRepository.findByBankProductAndDateValeur(bank, product, new Date());
            if(!listRate.isEmpty() || listRate != null)
            {
                h.put("message", "Le taux du jour est déjà renseigné.");
                h.put("status", -1);
                return h;
            }



            if(! isDateSup(rateDay.getDateValeur(), new Date()))
            {
                h.put("message", "La date valeur ne doit pas être inférieure à la date du jour.");
                h.put("status", -1);
                return h;
            }

            if(! isDateSup(rateDay.getDateFinValidite(), rateDay.getDateValeur()))
            {
                h.put("message", "date_valeur ne doit pas être superieur à date_fin_de_validite");
                h.put("status", -1);
                return h;
            }

            rateDay.setDateDebutValidite(new Date());
            if(rateDay.getDuree() != null) {
                rateDay.setDuree(rateDay.getDuree());
                // calcul  date de fin
                rateDay.setDateFinValidite(getDateFin(rateDay.getDuree(),
                rateDay.getDateDebutValidite()));
            }

            rateDay.setProduitsIdProduits(product);
            rateDay.setBank(bank);
            rateDay = rateRepository.save(rateDay);
        } catch (Exception e) {
            e.printStackTrace();
            h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
            h.put("status", -1);
            return h;
        }
        h.put("rateDay", rateDay);
        h.put("message", "Le taux du jour est ajouté avec succès.");
        h.put("status", 0);
        return h;
    }*/

	// laye
	@RequestMapping(value = "/add/{idUserBank}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> add(@RequestBody RateDay rateDay, @PathVariable Integer idUserBank) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(rateDay == null || idUserBank == null /*|| rateDay.getDateValeur() == null*/)
		{
			h.put("message", "paramètres vides");
			h.put("status", -1);
			return h;
		}
		rateDay.setDateDebutValidite(new Date());
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

			if (rateDay.getDevise() == null) {
				Devise dev = dRep.findByDescription("CFA");
				if (dev == null) {
					h.put("message", "la dévise ne doit pas être null");
					h.put("status", -1);
					return h;
				}
				rateDay.setDevise(dev);
			} else {
				Integer idDev = rateDay.getDevise().getIdDevise();
				Devise devise = dRep.findOne(idDev);
				if ( devise == null) {
					h.put("message", "la dévise n'existe pas");
					h.put("status", -1);
					return h;
				}
				rateDay.setDevise(devise);
			}   

			/* Verify if user is user_bank */
			UserBanque userBank = userBankRepository.findOne(idUserBank);
			if(userBank == null)
			{
				h.put("message", "cet utilisateur n'existe pas et/ ou n'est pas de type banque");
				h.put("status", -1);
				return h;
			}

			/* Get bank of user*/
			Bank bank = userBankRepository.getBankByUser(idUserBank);
			if(bank == null)
			{
				h.put("message", "la banque de l'utilisateur n'existe pas");
				h.put("status", -1);
				return h;
			}           

			List<RateDay> rateDays = rateRepository.findLastByBank(product, rateDay.getDevise(), bank);
			if (!rateDays.isEmpty()) {
				// unicite du rateday selon le produit et la date
				RateDay exRateDay = new RateDay();
				if (!rateDays.isEmpty()) {
					exRateDay = rateDays.get(0);

					if (uniciteRateday(exRateDay, rateDay)) {
						h.put("message", "le taux du jour doit etre unique pour un produit donnee");
						h.put("status", -1);
						return h;
					}
				}
			}
			if(!rateDay.getDuree().equals("")) {
				//rateDay.setDuree(rateDay.getDuree());
				// calcul  date de fin
				rateDay.setDateFinValidite(getDateFin(rateDay.getDuree(),
						rateDay.getDateDebutValidite()));
			}

			//rateDay.setDevise(devise);
			rateDay.setProduitsIdProduits(product);
			rateDay.setBank(bank);
			rateDay = rateRepository.save(rateDay);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("rateDay", rateDay);
		h.put("message", "Le taux du jour est ajouté avec succès.");
		h.put("status", 0);
		return h;
	}

	/* @RequestMapping(value = "/update/{idUserBank}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public Map<String,Object> update(@RequestBody RateDay rateDay, @PathVariable Integer idUserBank) {
        HashMap<String, Object> h = new HashMap<String, Object>();
        if(rateDay == null || idUserBank == null || rateDay.getIdTauxJour() == null )
        {
            h.put("message", "1 ou plusieurs paramètres manquants");
            h.put("status", -1);
            return h;
        }
        if(rateDay.getIdTauxJour() == null)
        {
            h.put("message", "L'identifiant du taux_du_jour n'est pas renseigné.");
            h.put("status", -1);
            return h;
        }
        try
        {
             Verify if user is user_bank 
            UserBanque userBank = userBankRepository.findOne(idUserBank);
            if(userBank == null)
            {
                h.put("message", "Cet utilisateur n'existe pas et/ ou n'est pas de type banque.");
                h.put("status", -1);
                return h;
            }

            RateDay rateDayOld = rateRepository.findOne(rateDay.getIdTauxJour());
            if(rateDayOld == null)
            {
                h.put("message", "Ce taux du jour n'existe pas.");
                h.put("status", -1);
                return h;
            }

             Get bank of user
            Bank bank = userBankRepository.getBankByUser(idUserBank);
            if(bank == null)
            {
                h.put("message", "La banque de l'utilisateur n'existe pas.");
                h.put("status", -1);
                return h;
            }

            if(! rateDayOld.getBank().getIdInstitution().equals(bank.getIdInstitution()))
            {
                h.put("message", "La banque de l'utilisateur et celle du taux du jour ne correspondent pas.");
                h.put("status", -1);
                return h;
            }

            if(rateDay.getProduitsIdProduits() == null)
                rateDay.setProduitsIdProduits(rateDayOld.getProduitsIdProduits());
            else
            {
                Integer idProduct = rateDay.getProduitsIdProduits().getIdProduits();
                Products product = productsRepository.findOne(idProduct);
                rateDay.setProduitsIdProduits(product);
            }

            if(rateDay.getIsActive() == null)
                rateDay.setIsActive(rateDayOld.getIsActive());
            if(rateDay.getDateDebutValidite() == null)
                rateDay.getDateDebutValidite();
            if(rateDay.getDateFinValidite() == null)
                rateDay.setDateFinValidite(rateDayOld.getDateFinValidite());
            if(rateDay.getDateValeur() == null)
                rateDay.setDateValeur(rateDayOld.getDateValeur());
            if(rateDay.getDescription() == null)
                rateDay.setDescription(rateDayOld.getDescription());
            if(rateDay.getEcheance() == null)
                rateDay.setEcheance(rateDayOld.getEcheance());
            if(rateDay.getMontant() == null)
                rateDay.setMontant(rateDayOld.getMontant());
            if(rateDay.getMotsCles() == null)
                rateDay.setMotsCles(rateDayOld.getMotsCles());
            if(rateDay.getTauxMax() == null)
                rateDay.setTauxMax(rateDayOld.getTauxMax());
            if(rateDay.getType() == null)
                rateDay.setType(rateDayOld.getType());
            if(rateDay.getValeur() == null)
                rateDay.setValeur(rateDayOld.getValeur());
            if(rateDay.getDuree() == null)
                rateDay.setDuree(rateDayOld.getDuree());

            if(! isDateSup(rateDay.getDateValeur(), new Date()))
            {
                h.put("message", "La date de valeur ne doit pas être inférieure à la date du jour.");
                h.put("status", -1);
                return h;
            }

            if(! isDateSup(rateDay.getDateFinValidite(), rateDay.getDateValeur()))
            {
                h.put("message", "La date de valeur ne doit pas être supérieure à la date de fin de validité.");
                h.put("status", -1);
                return h;
            }

            rateDay.setIdTauxJour(rateDay.getIdTauxJour());
            rateDay.setBank(bank);
            rateDay = rateRepository.saveAndFlush(rateDay);
        } catch (Exception e) {
            e.printStackTrace();
            h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
            h.put("status", -1);
            return h;
        }
        h.put("rateDay", rateDay);
        h.put("message", "Le taux du jour est mis à jour avec succès.");
        h.put("status", 0);
        return h;
    }*/

	@RequestMapping(value = "/update/{idUserBank}", method = RequestMethod.PUT)
	public Map<String,Object> update(@RequestBody RateDay rateDay, @PathVariable Integer idUserBank) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(rateDay == null || idUserBank == null || rateDay.getIdTauxJour() == null )
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
			return h;
		}
		if(rateDay.getIdTauxJour() == null)
		{
			h.put("message", "L'identifiant du taux_du_jour n'est pas renseigné.");
			h.put("status", -1);
			return h;
		}
		try
		{
			/* Verify if user is user_bank */
			UserBanque userBank = userBankRepository.findOne(idUserBank);
			if(userBank == null)
			{
				h.put("message", "Cet utilisateur n'existe pas et/ ou n'est pas de type banque.");
				h.put("status", -1);
				return h;
			}

			RateDay rateDayOld = rateRepository.findOne(rateDay.getIdTauxJour());
			if(rateDayOld == null)
			{
				h.put("message", "Ce taux du jour n'existe pas.");
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
			if(! rateDayOld.getBank().getIdInstitution().equals(bank.getIdInstitution()))
			{
				h.put("message", "La banque de l'utilisateur et celle du taux du jour ne correspondent pas.");
				h.put("status", -1);
				return h;
			}

			if(rateDay.getProduitsIdProduits() == null)
				rateDay.setProduitsIdProduits(rateDayOld.getProduitsIdProduits());
			else
			{
				Integer idProduct = rateDay.getProduitsIdProduits().getIdProduits();
				Products product = productsRepository.findOne(idProduct);
				rateDay.setProduitsIdProduits(product);
			}

			if (rateDay.getDevise() == null) {
				rateDay.setDevise(rateDayOld.getDevise());
			}

			Integer idDev = rateDay.getDevise().getIdDevise();
			Devise devise = dRep.findOne(idDev);
			if (devise == null) {
				h.put("message", "la dévise n'existe pas");
				h.put("status", -1);
				return h;
			}

			if(rateDay.getIsActive() == null)
				rateDay.setIsActive(rateDayOld.getIsActive());
			if(rateDay.getDateDebutValidite() == null)
			{
				rateDay.setDateDebutValidite(rateDayOld.getDateDebutValidite());
				System.out.println("ddv " + rateDayOld.getDateDebutValidite());
			}

			if(rateDay.getDateFinValidite() == null)
				rateDay.setDateFinValidite(rateDayOld.getDateFinValidite());
			if(rateDay.getDateValeur() == null)
				rateDay.setDateValeur(rateDayOld.getDateValeur());
			if(rateDay.getDescription() == null)
				rateDay.setDescription(rateDayOld.getDescription());
			if(rateDay.getEcheance() == null)
				rateDay.setEcheance(rateDayOld.getEcheance());
			if(rateDay.getMontant() == null)
				rateDay.setMontant(rateDayOld.getMontant());
			if(rateDay.getMotsCles() == null)
				rateDay.setMotsCles(rateDayOld.getMotsCles());
			if(rateDay.getTauxMax() == null)
				rateDay.setTauxMax(rateDayOld.getTauxMax());
			if(rateDay.getType() == null)
				rateDay.setType(rateDayOld.getType());
			if(rateDay.getValeur() == null)
				rateDay.setValeur(rateDayOld.getValeur());
			if(rateDay.getDuree() == null)
				rateDay.setDuree(rateDayOld.getDuree());

			rateDay.setIdTauxJour(rateDay.getIdTauxJour());
			rateDay.setBank(bank);
			rateDay = rateRepository.saveAndFlush(rateDay);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("rateDay", rateDay);
		h.put("message", "Le taux du jour est mis à jour avec succès.");
		h.put("status", 0);
		return h;
	}

	@RequestMapping(value = "/delete/{idUserBank}/{idTauxDuJour}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> delete(@PathVariable Integer idUserBank, @PathVariable Integer idTauxDuJour) {
		HashMap<String, Object> h = new HashMap<String, Object>();

		if(idUserBank == null || idTauxDuJour == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
			return h;
		}
		try
		{
			UserBanque userBank = userBankRepository.findOne(idUserBank);
			if(userBank == null)
			{
				h.put("message", "Cet utilisateur n'existe pas et/ ou n'est pas de type banque.");
				h.put("status", -1);
				return h;
			}

			RateDay rateDayOld = rateRepository.findOne(idTauxDuJour);
			if(rateDayOld == null)
			{
				h.put("message", "Ce taux du jour n'existe pas.");
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

			if(! rateDayOld.getBank().getIdInstitution().equals(bank.getIdInstitution()))
			{
				h.put("message", "La banque de l'utilisateur et celle du taux du jour ne correspondent pas.");
				h.put("status", -1);
				return h;
			}

			rateRepository.delete(idTauxDuJour);

		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("status", 0);
		h.put("message", "Le taux du jour est supprimé avec succès.");
		return h;
	}

	/*@PostMapping(value="/rechercher/multicritere")
   public HashMap<String, Object> rechercheMulticritere(@RequestBody RateDayModel rateDayModel) {

       HashMap<String, Object> h = new HashMap<String, Object>();
       Set<RateDay> rateDays = new HashSet<RateDay>();
       List<RateDay> rDaysByProduct = new ArrayList<RateDay>();
       List<RateDay> rDaysByDevise = new ArrayList<RateDay>();
       List<RateDay> rDaysByDateDebut = new ArrayList<RateDay>();
       List<RateDay> rDaysByDateFin = new ArrayList<RateDay>();

       if (rateDayModel.getIdProduit() != null) {
            Products pr = productsRepository.findOne(rateDayModel.getIdProduit());
            if (pr != null) {
                rDaysByProduct = rateRepository.findByProduitsIdProduits(pr);
            }
        }
       if (rateDayModel.getIdDevise() != null) {
           rDaysByDevise = rateRepository.findByDevise(rateDayModel.getIdDevise());
        }
       SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
       if (!rateDayModel.getDateDebut().equals("")) {
           try {
               Date dateDebut = formatter.parse(rateDayModel.getDateDebut());
               rDaysByDateDebut = rateRepository.findByDateDebutValiditeLessThanEqual(dateDebut);
           } catch (ParseException e) {
               h.put("status", -1);
               h.put("message", "le format de la dâte de début n'est pas bonne");
               return h;
           }
        }
       if (!rateDayModel.getDateFin().equals("")) {
           try {
               Date dateFin = formatter.parse(rateDayModel.getDateFin());
               rDaysByDateFin = rateRepository.findByDateFinValiditeGreaterThanEqual(dateFin);
           } catch (ParseException e) {
               h.put("status", -1);
               h.put("message", "le format de la dâte de fin n'est pas bonne");
               return h;
           }
        }
       rateDays.addAll(rDaysByProduct);
       rateDays.addAll(rDaysByDevise);
       rateDays.addAll(rDaysByDateDebut);
       rateDays.addAll(rDaysByDateFin);

       h.put("status", 0);
       h.put("liste_rateDays", new ArrayList<RateDay>(rateDays));
       return h;

   }*/

	@PostMapping(value="/rechercher/multicritere/{idUser}")
	public HashMap<String, Object> rechercheMulticritere(@RequestBody RateDayModel rateDayModel, 
			@PathVariable("idUser") Integer idUser) {

		HashMap<String, Object> h = new HashMap<String, Object>();
		Set<RateDay> rateDays = new HashSet<RateDay>();	
		List<RateDay> rDaysByProduct = new ArrayList<RateDay>();
		List<RateDay> rDaysByDevise = new ArrayList<RateDay>();
		List<RateDay> rDaysByDateDebut = new ArrayList<RateDay>();
		List<RateDay> rDaysByDateFin = new ArrayList<RateDay>();
		List<RateDay> rDaysByBanks = new ArrayList<>();

		User user = userEnterpriseRepository.findOne(idUser);
		if (user == null) {
			h.put("message", "cet utilisateur n'existe pas et/ ou n'est pas de type entreprise");
			h.put("status", -1);
			return h;
		}
		Locality loc = user.getGroupeIdGroupe().getInstitution().getLocalityIdLocalite();
		List<Bank> banks = new ArrayList<>();
		Integer[] idsbank = rateDayModel.getIdsBank();

		if (idsbank.length == 0) {
			banks = bankRepository.findByLocalityIdLocalite(loc);
		} else {
			for (Integer id : idsbank) {
				Bank bank = bankRepository.findOne(id);
				if (bank != null) {
					banks.add(bank);
				}
			}
			rDaysByBanks = rateRepository.findBanks(banks);			
		}
		
		List<Products> prInSearch = new ArrayList<Products>();
		if (rateDayModel.getIdProduit() == null) {
			prInSearch = productsRepository.findAll();
		}
		else  {
			Products pr = productsRepository.findOne(rateDayModel.getIdProduit());
			if (pr == null) {
				h.put("message", "le produit n'existe pas");
				h.put("status", -1);
				return h;
			}
			else {
				prInSearch.add(pr);
			}
		}
		rDaysByProduct = rateRepository.findRateByProduitAndLocalite(prInSearch, banks);
		if (rateDayModel.getIdDevise() == null && rateDayModel.getDateDebut().equals("") && 
				rateDayModel.getDateFin().equals("") && rateDayModel.getIdProduit() != null) {
			rateDays.addAll(rDaysByProduct);
		}
		
		if (rateDayModel.getIdDevise() != null) {
			Devise dev = dRep.findOne(rateDayModel.getIdDevise());
			if (dev == null) {
				rDaysByDevise = rateRepository.findRateByDeviseAndLocalite(dev, banks);
				rateDays.addAll(intersection(rDaysByProduct, rDaysByDevise));
			}
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		if (!rateDayModel.getDateDebut().equals("")) {
			try {
				Date dateDebut = formatter.parse(rateDayModel.getDateDebut());
				rDaysByDateDebut = rateRepository.findDateDebutValiditeAndLocalite(dateDebut, banks);
				rateDays.addAll(intersection(rDaysByProduct, rDaysByDateDebut));			
			} catch (ParseException e) {
				h.put("status", -1);
				h.put("message", "le format de la dâte de début n'est pas bonne");
				return h;
			}
		}
		if (!rateDayModel.getDateFin().equals("")) {
			try {
				Date dateFin = formatter.parse(rateDayModel.getDateFin());
				rDaysByDateFin = rateRepository.findDateFinValiditeAndLocalite(dateFin, banks);
				rateDays.addAll(intersection(rDaysByProduct, rDaysByDateFin));
			} catch (ParseException e) {
				h.put("status", -1);
				h.put("message", "le format de la dâte de fin n'est pas bonne");
				return h;
			}
		}

		//rateDays.addAll(rDaysByProduct);
		//rateDays.addAll(rDaysByDevise);
		//rateDays.addAll(rDaysByDateDebut);
		//rateDays.addAll(rDaysByDateFin);
		//rateDays.addAll(rDaysByBanks);		

			
		/*if (!rDaysByDevise.isEmpty()) {
			rateDays.addAll(intersection(rDaysByProduct, rDaysByDevise));
		}*/
		/*if (!rDaysByDateDebut.isEmpty()) {
			rateDays.addAll(intersection(rDaysByProduct, rDaysByDateDebut));
		}
		if (!rDaysByDateFin.isEmpty()) {
			rateDays.addAll(intersection(rDaysByProduct, rDaysByDateFin));
		}
		if (!rDaysByBanks.isEmpty()) {
			rateDays.addAll(intersection(rDaysByProduct, rDaysByBanks));
		}
		
		if(rateDays.isEmpty() && rateDayModel.getIdProduit() != null) {
			
			rateDays.addAll(rDaysByProduct);
		}
*/
		h.put("status", 0);
		h.put("liste_rateDays", new ArrayList<RateDay>(rateDays));
		return h;

	}

	public boolean isDateSup(Date date1, Date date2)
	{
		long result = 0;
		try {
			result = (date2.getTime() - date1.getTime()) / 60000;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(result > 0)
			return true;
		return false;
	}

	public Date getDateFin(String duree, Date date){
		//Calendar date=new GregorianCalendar();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// d = dernié élément de la durée A(année) ou M(mois)
		String d = duree.substring(duree.length() -1);
		// dat la duréé en chiffre
		String dat = duree.substring(0, duree.length() -1);
		int realDat = Integer.parseInt(dat);
		if (d.equals("M")) {
			cal.add(Calendar.MONTH, realDat);
			return cal.getTime();
		} 
		cal.add(Calendar.YEAR, realDat);
		return cal.getTime();
	}

	public Boolean uniciteRateday(RateDay exRateDay, RateDay newRateday){

		if (exRateDay.getDateFinValidite().compareTo(newRateday.getDateDebutValidite()) >= 0 && 
				exRateDay.getProduitsIdProduits() == newRateday.getProduitsIdProduits() &&
				exRateDay.getDevise() == newRateday.getDevise()) {
			return false;
		}
		return true;
	}
	
	public List<RateDay> intersection(List<RateDay> list1, List<RateDay> list2) {
		List<RateDay> rateDays = new ArrayList<>();
		list1.forEach(l-> {
			if (list2.contains(l)) {
				rateDays.add(l);
			}
		});
		return rateDays;
	}
	
}