package com.qualshore.etreasury.rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.qualshore.etreasury.dao.ActionRepository;
import com.qualshore.etreasury.dao.BankConditionRepository;
import com.qualshore.etreasury.dao.BankRepository;
import com.qualshore.etreasury.dao.EnterpriseRepository;
import com.qualshore.etreasury.dao.LogRepository;
import com.qualshore.etreasury.dao.ProductsRepository;
import com.qualshore.etreasury.dao.UserEntrepriseRepository;
import com.qualshore.etreasury.entity.Bank;
import com.qualshore.etreasury.entity.BankCondition;
import com.qualshore.etreasury.entity.Category;
import com.qualshore.etreasury.entity.Documents;
import com.qualshore.etreasury.entity.Enterprise;
import com.qualshore.etreasury.entity.Institution;
import com.qualshore.etreasury.entity.Locality;
import com.qualshore.etreasury.entity.Log;
import com.qualshore.etreasury.entity.Products;
import com.qualshore.etreasury.entity.RateDay;
import com.qualshore.etreasury.entity.User;
import com.qualshore.etreasury.entity.UserEntreprise;
import com.qualshore.etreasury.model.BankConditionRechModel;
import com.qualshore.etreasury.service.FileStorageService;
import com.qualshore.etreasury.service.FileUploadService;
import com.qualshore.etreasury.service.UserLoginService;
import com.qualshore.etreasury.service.UserService;

@RequestMapping("/etreasury_project/admin/bank_conditions")
@RestController
public class BankConditionRestController {

	@Autowired
	BankConditionRepository bCRep;
	@Autowired
	BankRepository bRep;
	@Autowired
	EnterpriseRepository eRep;

	@Autowired
	UserService userService;
	@Autowired
	ProductsRepository productRep;
	@Autowired
	FileStorageService fileStorageService;

	@Autowired
	FileUploadService fileService;

	@Autowired
	LogRepository logRepository;

	@Autowired
	ActionRepository actionRepository;

	@Autowired
	Environment env;
	@Autowired
	UserLoginService userLoginService;

	@Autowired
	UserEntrepriseRepository uERep;


	@RequestMapping(value="/list", method=RequestMethod.GET)
	public HashMap<String, Object> getBankConditions(){

		HashMap<String, Object> h = new HashMap<>();
		List<BankCondition> bConditions = bCRep.findAll();

		h.put("message", "success");
		h.put("status", 0);
		h.put("list_bankConditions", bConditions);
		return h;
	}

	//conditionByBank
	@RequestMapping(value="/list_conditions/{idAdmin}", method=RequestMethod.GET)
	public HashMap<String, Object> getBankConditionsByBank(@PathVariable Integer idAdmin){

		HashMap<String, Object> h = new HashMap<>();
		if(idAdmin == null )
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}

		try
		{
			if(userService.isAdminBanque(idAdmin))
			{
				h.put("status", -1);
				h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
				return h;
			}
			User user = userLoginService.findOne(idAdmin);
			if(user == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'existe pas.");
				return h;
			}
			Bank bank = bRep.findOne(user.getGroupeIdGroupe().getInstitution().getIdInstitution());
			if(bank == null)
			{
				h.put("message", "L'utilisateur n'est pas d'une banque n'existe pas.");
				h.put("status", -1);
				return h;
			}
			List<BankCondition> bConditions = bCRep.findByBank(bank);
			h.put("message", "success");
			h.put("status", 0);
			h.put("list_bankConditions", bConditions);
		}
		catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}



		return h;
	}
	//Conditions by locality
	@RequestMapping(value="/locality/list_conditions/{idAdmin}", method=RequestMethod.GET)
	public HashMap<String, Object> getBankConditionsByLocality(@PathVariable Integer idAdmin){

		HashMap<String, Object> h = new HashMap<>();
		if(idAdmin == null )
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}

		try
		{
			if(userService.isAdminEntreprise(idAdmin))
			{
				h.put("status", -1);
				h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
				return h;
			}
			User user = userLoginService.findOne(idAdmin);
			if(user == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'existe pas.");
				return h;
			}
			Enterprise entreprise = eRep.findOne(user.getGroupeIdGroupe().getInstitution().getIdInstitution());
			if(entreprise == null)
			{
				h.put("message", "L'utilisateur n'est pas d'une entreprise n'existe pas.");
				h.put("status", -1);
				return h;
			}
			List<BankCondition> bConditions = bCRep.findByLocality(entreprise.getLocalityIdLocalite().getIdLocalite());
			h.put("message", "success");
			h.put("status", 0);
			h.put("list_bankConditions", bConditions);
		}
		catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}



		return h;
	}


	@RequestMapping(value="/add/{idAdmin}", method=RequestMethod.POST)
	public HashMap<String, Object> add(
			@RequestParam(value = "nom" , required = false) String nom,
			@RequestParam(value = "famille" , required = false) String famille,
			@RequestParam(value = "categorie" , required = false) String category,
			@RequestParam(value = "taux_standard" , required = false) String tauxStandard,
			@RequestParam("id_banque") Integer idBanque,
			@RequestParam("id_product") Integer idProduct,
			@RequestParam(value = "file", required = false) MultipartFile file,
			@PathVariable Integer idAdmin){
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(idAdmin == null || idBanque == null || idProduct == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}

		try
		{
			if(userService.isAdminBanque(idAdmin))
			{
				h.put("status", -1);
				h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
				return h;
			}
			User user = userLoginService.findOne(idAdmin);
			if(user == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'existe pas.");
				return h;
			}
			Bank bank = bRep.findOne(idBanque);
			if(bank == null)
			{
				h.put("message", "Cette banque n'existe pas.");
				h.put("status", -1);
				return h;
			}
			Products product = productRep.findOne(idProduct);
			if (product == null) {
				h.put("message", "Le produit n'existe pas.");
				h.put("status", -1);
				return h;
			}
			if (bCRep.exitsByBankProductAndDate(bank, product, new Date()))
			{
				h.put("message", "La condition standard existe déjà pour cette banque avec le même produit pour l'année en cours.");
				h.put("status", -1);
				return h;
			}
			BankCondition banqueCondition = new BankCondition();
			banqueCondition.setNom(nom);
			banqueCondition.setCategorie(category);
			banqueCondition.setFamille(famille);
			banqueCondition.setTauxStandard(tauxStandard);
			banqueCondition.setDateCondition(new Date());
			banqueCondition.setBank(bank);
			banqueCondition.setProduct(product);
			LocalDate now = LocalDate.now(); // 2015-11-23
			LocalDate firstDay = now.with(firstDayOfYear()); // 2015-01-01
			LocalDate lastDay = now.with(lastDayOfYear()); // 2015-12-31

			System.out.println("Last date and first "+firstDay +"  "+lastDay);
			Date dateFirst = Date.from(firstDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
			Date dateLast = Date.from(lastDay.atStartOfDay(ZoneId.systemDefault()).toInstant());

			banqueCondition.setDateDebut(dateFirst);
			banqueCondition.setDateFin(dateLast);
			//Upload file
			if(file != null)
			{
				Pattern pExtension = Pattern.compile("([^\\s]+(\\.(?i)(csv|xls|xlsx))$)");
				Matcher mExtension = pExtension.matcher(file.getOriginalFilename());
				if(! mExtension.matches())
				{
					h.put("message", "Le format de fichier est incorrect.");
					h.put("status", -1);
					return h;
				}
				if(file.getSize() >= fileService.getMaxFileSize())
				{
					h.put("message", "Le fichier est trop volumineux.");
					h.put("status", -1);
					return h;
				}
				String [] originalName = file.getOriginalFilename().split("\\.csv");
				String nomFic = originalName[0];

				banqueCondition.setNomDocument(nomFic);
				SimpleDateFormat formater = new SimpleDateFormat("ddMMyyyy");
				String date1=formater.format(banqueCondition.getDateCondition());
				//  category.setRepertoire(getCategoryRootLocation(category));
				banqueCondition.setIsAttachedFile(true);
				banqueCondition.setRepertoireFile(getFileRootLocation(user,date1));
				banqueCondition.setUrlFile(banqueCondition.getRepertoireFile()+"/"+nomFic+".csv");
				String rootLocation = env.getProperty("root.location.store.file");
				fileStorageService.createFileLocation(banqueCondition.getRepertoireFile(), env);
				fileStorageService.storeFile(file, banqueCondition, rootLocation);


			}
			else {
				banqueCondition.setIsAttachedFile(false);

			}
			banqueCondition = bCRep.save(banqueCondition);
			journaliser(idAdmin, 6, banqueCondition.getIdConditionBanque());
			h.put("banque_condition", banqueCondition);

		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}

		h.put("message", "La condition standard de la banque est ajoutée avec succès");
		h.put("status", 0);
		return h;
	}

	/*
		@RequestMapping(value="/add/{id}", method=RequestMethod.POST)
		public HashMap<String, Object> add(@RequestBody BankCondition bc, @PathVariable int id){
			HashMap<String, Object> h= new HashMap<>();
			bc.setDateCondition(new Date());

			if(nullable(bc)){
				h.put("message", "Aucun des champs ne doit etre null");
				h.put("status", -1);
				return h;
			}

			Bank bank = bRep.findOne(id);
			BankCondition exBC = new BankCondition();			
			List<BankCondition> listBankConditions = bCRep.findLastByBank(bank);

			if (listBankConditions.isEmpty()) {
				exBC = null;
			} else{
				 exBC= bCRep.findLastByBank(bank).get(0);
			}

			h = handleAdd(exBC, bc, bank);
			return h;
		}
	 */

	/*
	 * Delete document
	 */
	@RequestMapping(value = "/delete/{idAdmin}/{idBankCondition}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> deleteFile(@PathVariable Integer idAdmin, @PathVariable Integer idBankCondition) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(idBankCondition == null || idAdmin == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		try
		{
			BankCondition bankCondition = bCRep.findOne(idBankCondition);
			if(bankCondition == null)
			{
				h.put("message", "Le document n'existe pas.");
				h.put("status", -1);
				return h;
			}
			if(userService.isAdminGeneral(idAdmin))
			{
				h.put("status", -1);
				h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
				return h;
			}
			User user = userLoginService.findOne(idAdmin);
			if(user == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'existe pas.");
				return h;
			}

			deleteLocationConditions(bankCondition.getUrlFile());
			//Supprimer doc en bdd
			bCRep.delete(bankCondition);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "Le document est supprimé avec succès.");
		h.put("status", 0);
		return h;
	}


	//Update bew version 
	@RequestMapping(value="/update/{idAdmin}/{idBankCondition}", method=RequestMethod.POST)
	public HashMap<String, Object> updateCondition(
			@RequestParam(value = "nom" , required = false) String nom,
			@RequestParam(value = "famille" , required = false) String famille,
			@RequestParam(value = "categorie" , required = false) String category,
			@RequestParam(value = "taux_standard" , required = false) String tauxStandard,
			@RequestParam("id_banque") Integer idBanque,
			@RequestParam("id_product") Integer idProduct,
			@RequestParam(value = "file", required = false) MultipartFile file,
			@PathVariable Integer idAdmin,
			@PathVariable Integer idBankCondition){
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(idBankCondition == null || idAdmin == null || idBanque == null || idProduct == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}

		try
		{
			if(userService.isAdminBanque(idAdmin))
			{
				h.put("status", -1);
				h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
				return h;
			}
			User user = userLoginService.findOne(idAdmin);
			if(user == null)
			{
				h.put("status", -1);
				h.put("message", "Cet utilisateur n'existe pas.");
				return h;
			}
			Bank bank = bRep.findOne(idBanque);
			if(bank == null)
			{
				h.put("message", "Cette banque n'existe pas.");
				h.put("status", -1);
				return h;
			}
			Products product = productRep.findOne(idProduct);
			if (product == null) {
				h.put("message", "Le produit n'existe pas.");
				h.put("status", -1);
				return h;
			}
			BankCondition oldBC = bCRep.findOne(idBankCondition);
			if (oldBC == null) {
				h.put("message", "Cette condition de banque n'existe pas.");
				h.put("status", -1);
			}
			if (bCRep.exitsByBankProductAndDateOthers(bank, product, new Date(),oldBC.getIdConditionBanque()))
			{
				h.put("message", "La condition standard existe déjà pour cette banque avec le même produit pour l'année en cours.");
				h.put("status", -1);
				return h;
			}


			/*
				   				if (nullable(bc)) {
				   					h.put("message", "Aucun des champs ne doit etre null.");
				   					h.put("status", -1);
				   					return h;
				   				} 
			 */

			BankCondition banqueCondition = new BankCondition();
			banqueCondition.setNom(nom);
			banqueCondition.setCategorie(category);
			banqueCondition.setFamille(famille);
			banqueCondition.setTauxStandard(tauxStandard);
			/*
			if(banqueCondition.getCategorie() == null) {
				banqueCondition.setCategorie(oldBC.getCategorie());
			}
			else {
				banqueCondition.setCategorie(category);
			}

			if(banqueCondition.getNom() == null) {
				banqueCondition.setNom(oldBC.getNom());
			}
			else {
				banqueCondition.setNom(nom);
			}

			if(banqueCondition.getFamille() == null) {
				banqueCondition.setFamille(oldBC.getFamille());
			}
			else {
				banqueCondition.setFamille(famille);
			}

			if(banqueCondition.getTauxStandard() == null) {
				banqueCondition.setTauxStandard(oldBC.getTauxStandard());
			}
			else {
				banqueCondition.setTauxStandard(tauxStandard);
			}
			*/

			banqueCondition.setDateCondition(new Date());
			banqueCondition.setBank(bank);
			banqueCondition.setProduct(product);
			/*
				    	           LocalDate now = LocalDate.now(); // 2015-11-23
				    	           LocalDate firstDay = now.with(firstDayOfYear()); // 2015-01-01
				    	           LocalDate lastDay = now.with(lastDayOfYear()); // 2015-12-31

				    	           System.out.println("Last date and first "+firstDay +"  "+lastDay);
				    	           Date dateFirst = Date.from(firstDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
				    	           Date dateLast = Date.from(lastDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
			 */
			banqueCondition.setDateDebut(oldBC.getDateDebut());
			banqueCondition.setDateFin(oldBC.getDateFin());
			//Upload file
			if(file != null)
			{
				//Supprimer l'ancien emplacement du document
				deleteLocationConditions(oldBC.getUrlFile());

				//Chargement du nouveau document
				Pattern pExtension = Pattern.compile("([^\\s]+(\\.(?i)(csv|xls|xlsx))$)");
				Matcher mExtension = pExtension.matcher(file.getOriginalFilename());
				if(! mExtension.matches())
				{
					h.put("message", "Le format de fichier est incorrect.");
					h.put("status", -1);
					return h;
				}
				if(file.getSize() >= fileService.getMaxFileSize())
				{
					h.put("message", "Le fichier est trop volumineux.");
					h.put("status", -1);
					return h;
				}
				String [] originalName = file.getOriginalFilename().split("\\.csv");
				String nomFic = originalName[0];
				banqueCondition.setNomDocument(nomFic);

				SimpleDateFormat formater = new SimpleDateFormat("ddMMyyyy");
				String date1=formater.format(banqueCondition.getDateCondition());
				//  category.setRepertoire(getCategoryRootLocation(category));
				banqueCondition.setIsAttachedFile(true);
				banqueCondition.setRepertoireFile(getFileRootLocation(user,date1));
				banqueCondition.setUrlFile(banqueCondition.getRepertoireFile()+"/"+nomFic+".csv");
				String rootLocation = env.getProperty("root.location.store.file");
				fileStorageService.createFileLocation(banqueCondition.getRepertoireFile(), env);
				fileStorageService.storeFile(file, banqueCondition, rootLocation);


			}
			else {
				banqueCondition.setIsAttachedFile(oldBC.getIsAttachedFile());
				banqueCondition.setRepertoireFile(oldBC.getRepertoireFile());
				banqueCondition.setNomDocument(oldBC.getNomDocument());

				banqueCondition.setUrlFile(oldBC.getUrlFile());

			}
			//banqueCondition = bCRep.save(banqueCondition);
			banqueCondition.setIdConditionBanque(oldBC.getIdConditionBanque());
			banqueCondition = bCRep.saveAndFlush(banqueCondition);
			journaliser(idAdmin, 6, banqueCondition.getIdConditionBanque());
			h.put("banque_condition", banqueCondition);

		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}

		h.put("message", "La condition standard de la banque est ajoutée avec succès");
		h.put("status", 0);
		return h;
	}
	/*
		@RequestMapping(value="/update/{idAdmin}/{idBankCondition}", method= RequestMethod.PUT)
		public HashMap<String, Object> update(@RequestBody BankCondition bc, @PathVariable Integer idAdmin, @PathVariable Integer idBankCondition){

			HashMap<String, Object> h= new HashMap<String, Object>();
			if(idBankCondition == null || idAdmin == null)
			{
				h.put("status", -1);
				h.put("message", "1 ou plusieurs paramètres manquants");
				return h;
			}
			try
			{
				if(userService.isAdminGeneral(idAdmin))
				{
					h.put("status", -1);
					h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
					return h;
				}
				User user = userLoginService.findOne(idAdmin);
                if(user == null)
                {
                    h.put("status", -1);
                    h.put("message", "Cet utilisateur n'existe pas.");
                    return h;
                }

				BankCondition oldBC = bCRep.findOne(idBankCondition);
				if (oldBC == null) {
					h.put("message", "Cette condition de banque n'existe pas.");
					h.put("status", -1);
				}


				bc.setIdConditionBanque(oldBC.getIdConditionBanque());
				bc.setDateCondition(oldBC.getDateCondition());
				bc.setBank(oldBC.getBank());
				bc = bCRep.saveAndFlush(bc);

			} catch (Exception e) {
				e.printStackTrace();
				h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
				h.put("status", -1);
				return h;
			}
				h.put("message", "La modification s'est bien effectuée.");
				h.put("bank_condition", bc);
				h.put("status", -1);
				return h;
			}
	 */
	@RequestMapping(value="/list/{id}", method= RequestMethod.GET)
	public HashMap<String, Object> getBankCondition(@PathVariable int id){

		HashMap<String, Object> h = new HashMap<>();
		BankCondition bCondition = bCRep.findOne(id);

		if (bCondition == null) {
			h.put("message", "La condition de banque n'existe pas.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "success");
		h.put("bank_condition", bCondition);
		h.put("status", -1);
		return h;
	}

	@RequestMapping(value="/delete/{id}", method= RequestMethod.DELETE)
	public HashMap<String, Object> deleteBankCondition(@PathVariable int id){
		HashMap<String, Object> h = new HashMap<>();
		BankCondition bCondition = bCRep.findOne(id);

		if (bCondition == null) {
			h.put("message", "La condition de banque n'existe pas.");
			h.put("status", -1);
			return h;
		}

		bCRep.delete(bCondition);
		h.put("message", "La suppression  s'est bien effectuée.");
		h.put("status", 0);
		return h;
	}

	@RequestMapping(value="/active/{id}", method= RequestMethod.PUT)
	public HashMap<String, Object> activeBankCondition(@PathVariable int id){
		HashMap<String, Object> h = new HashMap<>();
		BankCondition bCondition = bCRep.findOne(id);

		if (bCondition == null) {
			h.put("message", "La condition de banque n'existe pas.");
			h.put("status", -1);
			return h;
		}

		bCondition.setIsActive(true);
		bCRep.saveAndFlush(bCondition);
		h.put("message", "La condition de banque est activée.");
		h.put("bank_condition", bCondition);
		h.put("status", 0);
		return h;
	}

	@RequestMapping(value="/desactive/{id}", method= RequestMethod.PUT)
	public HashMap<String, Object> desactiveBankCondition(@PathVariable int id){
		HashMap<String, Object> h = new HashMap<>();
		BankCondition bCondition = bCRep.findOne(id);

		if (bCondition == null) {
			h.put("message", "La condition de banque n'existe pas.");
			h.put("status", -1);
			return h;
		}

		bCondition.setIsActive(false);
		bCRep.saveAndFlush(bCondition);
		h.put("message", "La condition de banque est bien désactivée.");
		h.put("bank_condition", bCondition);
		h.put("status", 0);
		return h;
	}

	public Boolean compareDate(Date date1, Date date2){

		if (date1.compareTo(date2) > 0){
			return true;
		}
		return false;
	}

	public HashMap<String, Object> handleAdd(BankCondition exBC, BankCondition bc, Bank bank){

		HashMap<String, Object> h = new HashMap<>();

		if (exBC == null) {

			if (compareDate(bc.getDateFin(), bc.getDateDebut())) {
				if(bc.getDateDebut().compareTo(bc.getDateCondition()) >= 0){
					try {
						if(bank == null){
							h.put("message", "La banque associée à cette condition n'existe pas.");
							h.put("status", -1);
							return h;
						}
						bc.setBank(bank);
						bc.setDateCondition(new Date());
						bc.setIsActive(false);
						bCRep.save(bc);
					} catch (Exception e) {
						e.printStackTrace();
						h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
						h.put("status", -1);
						return h;
					}
				} else {
					h.put("message", "La date de début doit être supérieure ou ègale à la date d'enregistrement.");
					h.put("status", -1);
					return h;		
				}
			} else {
				h.put("message", "La date de début doit être inférieure à la date de fin.");
				h.put("status", -1);
				return h;										
			}

			h.put("message", "La création de la condition de banque s'est bien effectuée.");
			h.put("status", -1);
			h.put("bankCondition", bc);
			return h;
		}

		if (compareDate(bc.getDateDebut(), exBC.getDateFin())) {
			if (compareDate(bc.getDateFin(), bc.getDateDebut())) {
				if(bc.getDateDebut().compareTo(bc.getDateCondition()) >= 0){
					try {
						if(bank == null){
							h.put("message", "La banque associée à cette condition n'existe pas.");
							h.put("status", -1);
							return h;
						}
						bc.setBank(bank);
						bc.setDateCondition(new Date());
						bc.setIsActive(false);
						bCRep.save(bc);
					} catch (Exception e) {
						e.printStackTrace();
						h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
						h.put("status", -1);
						return h;
					}
				} else {
					h.put("message", "La date de début doit être supérieure ou ègale à la date d'enregistrement.");
					h.put("status", -1);
					return h;		
				}
			} else {
				h.put("message", "La date de début doit être inférieure à la date de fin.");
				h.put("status", -1);
				return h;										
			}

			h.put("message", "success");
			h.put("status", -1);
			h.put("bankCondition", bc);
			return h;
		}
		h.put("message", "La date de début est inférieure à la date de fin de la denière banque condition.");
		h.put("status", -1);
		return h;
	}

	@PostMapping(value="/rechercher/multicritere/{idUser}")
	public HashMap<String, Object> rechercheMulticritere(@RequestBody BankConditionRechModel bRechModel, 
			@PathVariable("idUser") Integer idUser){

		HashMap<String, Object> h = new HashMap<String, Object>();

		UserEntreprise user = uERep.findOne(idUser);
		if (user == null) {
			h.put("message", "l'utilisateur n'existe pas ou n'est pas de type entreprise");
			h.put("status", -1);
			return h;
		}

		Set<BankCondition> bConditions = new HashSet<BankCondition>();
		List<BankCondition> bConditionsByDateDebut = new ArrayList<>();
		List<BankCondition> bConditionsByDateFin = new ArrayList<>();
		List<BankCondition> bConditionsByTStand = new ArrayList<>();
		List<BankCondition> bConditionsByPr = new ArrayList<>();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		String debut = bRechModel.getDateDebut();
		String fin = bRechModel.getDateFin();
		String tStandard = bRechModel.getTauxStandard();
		List<Integer> idsBank = bRechModel.getIdsBank();

		List<Bank> banks = new ArrayList<>();
		if (!idsBank.isEmpty()) {
			for (Integer id : idsBank) {
				Bank b = bRep.findOne(id);
				if (b != null) {
					banks.add(b);
				}
			}
		} else {
			Locality loc = user.getGroupeIdGroupe().getInstitution().getLocalityIdLocalite();
			banks = bRep.findByLocalityIdLocalite(loc);
		}
		
		try {			
			List<Products> prInSearch = new ArrayList<Products>();
			if (bRechModel.getIdPr() == null) {
				prInSearch = productRep.findAll();
			} else {
				Products pr = productRep.findOne(bRechModel.getIdPr());
	            if (pr == null) {
	                h.put("message", "le produit n'existe pas");
	                h.put("status", -1);
	                return h;
	            } else {
	                prInSearch.add(pr);
	            }
			}			
			bConditionsByPr = bCRep.findProducts(prInSearch, banks);

			if (!debut.equals("")) {
				Date dateDebut = formatter.parse(debut);
				bConditionsByDateDebut = bCRep.findDateDebut(dateDebut, banks);
				bConditions.addAll(intersection(bConditionsByPr, bConditionsByDateDebut));
			}
			if (!fin.equals("")) {
				Date dateFin = formatter.parse(fin);
				bConditionsByDateFin = bCRep.findDateFin(dateFin, banks);
				bConditions.addAll(intersection(bConditionsByPr, bConditionsByDateFin));
			}
			if (!tStandard.equals("")) {
				bConditionsByTStand = bCRep.findTauxStandard(tStandard, banks);
				bConditions.addAll(intersection(bConditionsByPr, bConditionsByTStand));
			}
			if (debut.equals("") && fin.equals("") && tStandard.equals("") && 
					bRechModel.getIdPr() != null) {
				bConditions.addAll(bConditionsByPr);
			}
			/*bConditions.addAll(bConditionsBanks);
			bConditions.addAll(bConditionsByPr);
			bConditions.addAll(bConditionsByTStand);
			bConditions.addAll(bConditionsByDateFin);
			bConditions.addAll(bConditionsByDateDebut);
			bConditions.addAll(bConditionsBanks2);*/

		} catch (Exception e) {
			h.put("message", e.getMessage());
			h.put("status", -1);
			return h;
		}

		h.put("list_bankConditions", bConditions);
		h.put("message", "success");
		h.put("status", 0);
		return h;
	}


	/*
		public Boolean nullable(BankCondition bc){
			if (bc.getNegociable().equals("")  || bc.getCategorie().equals("") || bc.getDateDebut().equals("") || 
					bc.getDateFin().equals("") || bc.getNom().equals("") || bc.getFamille().equals("") || 
					bc.getLibelleCondition().equals("")|| bc.getSpecificite().equals("")) {

				return true;
			}
			return false;
		}
	 */
	public final String getFileRootLocation(User user,String date1) throws Exception {
		// User user = category.getUserIdUtilisateur();
		String paysLocality = user.getGroupeIdGroupe().getInstitution().getLocalityIdLocalite().getPays();
		Institution institution = user.getGroupeIdGroupe().getInstitution();
		return paysLocality+ "/" +institution.getNom()+ "/"+ user.getPrenom()+ "-"+ user.getNom()+ "/"+date1;
	}
	public void createFileLocation(String rep) throws IOException {
		String[] listDir = rep.split("/");
		try {
			String location = env.getProperty("root.location.store.file");
			for(int i = 0; i < listDir.length; i++)
			{
				location += "/"+ listDir[i];
				if(Files.notExists(Paths.get(location)))
					Files.createDirectory(Paths.get(location));
			}
		} catch (IOException e) {
			throw new RuntimeException("Une erreur est survenue lors du stockage.");
		}
	}
	public void journaliser(Integer idUser, Integer idAction, Integer idDocument) {
		Log log = new Log();
		log.setIdUser(idUser);
		log.setIdDocument(idDocument);
		log.setDate(new Date());
		log.setActionIdAction(actionRepository.findOne(idAction));
		logRepository.save(log);
	}	

	public void deleteLocationConditions(String rep) {
		try {
			FileUtils.forceDelete(Paths.get(env.getProperty("root.location.store.file")+ "/"+ rep).toFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<BankCondition> intersection(List<BankCondition> list1, List<BankCondition> list2) {
		List<BankCondition> bConditions = new ArrayList<>();
		list1.forEach(l-> {
			if (list2.contains(l)) {
				bConditions.add(l);
			}
		});
		return bConditions;
	}
}
