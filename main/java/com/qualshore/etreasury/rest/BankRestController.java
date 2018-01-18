
package com.qualshore.etreasury.rest;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qualshore.etreasury.dao.BankConditionRepository;
import com.qualshore.etreasury.dao.BankRepository;
import com.qualshore.etreasury.dao.GroupRepository;
import com.qualshore.etreasury.dao.InstitutionRepository;
import com.qualshore.etreasury.dao.JuridictionGroupeRepository;
import com.qualshore.etreasury.dao.JuridictionRepository;
import com.qualshore.etreasury.dao.LocalityRepository;
import com.qualshore.etreasury.dao.ProductsRepository;
import com.qualshore.etreasury.dao.ProfileRepository;
import com.qualshore.etreasury.dao.UserBankRepository;
import com.qualshore.etreasury.dao.UserRepository;
import com.qualshore.etreasury.entity.Bank;
import com.qualshore.etreasury.entity.BankCondition;
import com.qualshore.etreasury.entity.Documents;
import com.qualshore.etreasury.entity.DocumentsHasRequest;
import com.qualshore.etreasury.entity.DocumentsHasRequestPK;
import com.qualshore.etreasury.entity.Groupe;
import com.qualshore.etreasury.entity.Juridiction;
import com.qualshore.etreasury.entity.JuridictionGroupe;
import com.qualshore.etreasury.entity.Locality;
import com.qualshore.etreasury.entity.Profile;
import com.qualshore.etreasury.entity.Request;
import com.qualshore.etreasury.entity.RequestHasBank;
import com.qualshore.etreasury.entity.RequestHasBankPK;
import com.qualshore.etreasury.entity.User;
import com.qualshore.etreasury.entity.UserBanque;
import com.qualshore.etreasury.service.UserLoginService;
import com.qualshore.etreasury.service.UserService;

@RequestMapping("/etreasury_project/admin")
@RestController
public class BankRestController {
	
	@Autowired
	BankRepository bRepository;
	@Autowired
	InstitutionRepository iRepository;
	@Autowired
	LocalityRepository localityRepository;
	@Autowired
	ProductsRepository prRep;
	@Autowired
	GroupRepository groupRepository;
	@Autowired
	BankConditionRepository bCRepository;
	@Autowired
	UserRepository uRep;
	@Autowired
	UserService userService;
	@Autowired
	UserBankRepository userBankRepository;
	@Autowired
	ProfileRepository profilRepository;
	@Autowired
	UserLoginService userLoginService;
	@Autowired
	JuridictionGroupeRepository  jGRep;
	@Autowired
	JuridictionRepository jRep;
	
    
	/*
	 * get all bank
	 */
	@RequestMapping(value="/bank/list", method=RequestMethod.GET)
 	public Map<String,Object> getBanks(){
	
		List<Bank> banks = bRepository.findAll();
		HashMap<String, Object> h = new HashMap<String, Object>();
		h.put("message", "liste des banks");
		h.put("status", 0);
		h.put("bank_list", banks);
		return h;
	}
	
	/*
	 * add a bank
	 */
	@RequestMapping(value="/bank/add", method=RequestMethod.POST)
	public HashMap<String, Object> add(@RequestBody Bank bank){
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		//Fields Control
		if(bank == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		if(bank.getNom().equals("") || bank.getTelephone1().equals("") || bank.getLocalityIdLocalite() == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres vides");
			return h;
		}
		if(! userService.isPhoneValid(bank.getTelephone1()))
		{
			h.put("status", -1);
			h.put("message", "Le numéro de téléphone mobile n'est pas au bon format.");
			return h;
		}
		if(bank.getTelephone2() != null && !userService.isPhoneValid(bank.getTelephone2()))
		{
			h.put("status", -1);
			h.put("message", "Le numéro de téléphone fixe n'est pas au bon format.");
			return h;
		}
		//
				
		try {
			bank.setIsActive(false);
			Integer idLoc = bank.getLocalityIdLocalite().getIdLocalite();
			Locality locality = localityRepository.findOne(idLoc);
			if(locality == null){
				h.put("message", "La localité n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			bank.setLocalityIdLocalite(locality);
			Date date = new Date();
			bank.setDateCreation(date);
			bank = bRepository.save(bank);
			/* Default group for users */
			groupRepository.save(new Groupe("default_"+bank.getNom(), "Default Group", bank));
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("bank", bank);
		h.put("message", "La création de la banque s'est bien effectuée.");
		h.put("status", 0);
		return h;
	}
	
	
	//add a bank with 2 groupes default +admin
	@RequestMapping(value="/bank/add/groupes/{idAdmin}", method=RequestMethod.POST)
	public HashMap<String, Object> addBank(@RequestBody Bank bank,@PathVariable Integer idAdmin){
		HashMap<String, Object> h = new HashMap<String, Object>();
		//Fields Control
		if(bank == null || idAdmin == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		if(bank.getNom().equals("") || bank.getTelephone1().equals("") || bank.getLocalityIdLocalite() == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres vides");
			return h;
		}
		if(! userService.isPhoneValid(bank.getTelephone1()))
		{
			h.put("status", -1);
			h.put("message", "Le numéro de téléphone mobile n'est pas au bon format.");
			return h;
		}
		if(bank.getTelephone2() != null && !userService.isPhoneValid(bank.getTelephone2()))
		{
			h.put("status", -1);
			h.put("message", "Le numéro de téléphone fixe n'est pas au bon formt");
			return h;
		}
		//
		
		try {
			if(userService.isAdminGeneral(idAdmin))
			{
				h.put("status", -1);
				h.put("message", "Vous n'avez pas le profil pour effectuer cette action.");
				return h;
			}
			
			bank.setIsActive(false);
			Integer idLoc = bank.getLocalityIdLocalite().getIdLocalite();
			Locality locality = localityRepository.findOne(idLoc);
			if(locality == null){
				h.put("message", "La localité n'existe pas");
				h.put("status", -1);
				return h;
			}
			System.out.println("nom bk existe "+iRepository.exitsByIdInstitutionName(bank.getNom(), locality));
			if(iRepository.exitsByIdInstitutionName(bank.getNom(), locality))
			{
				h.put("message", "Le nom pour cette banque existe déjà dans la localité.");
				h.put("status", -1);
				return h;
			}
			
			
			bank.setLocalityIdLocalite(locality);
			//LocalDate localDate = LocalDate.now(ZoneId.of("GMT+00:00"));
			//LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
			Date date = new Date();
			bank.setDateCreation(date);
			bank = bRepository.save(bank);
			/* Default group for users */
			groupRepository.save(new Groupe("default_"+bank.getNom(), "Default Group", bank));
			groupRepository.save(new Groupe("admin_"+bank.getNom(), "Admin Group", bank));
			
			Groupe grp = groupRepository.findByInstition(bank);
			if(grp == null )
			{
				h.put("message", "Le groupe admin n'existe pas.");
				h.put("status", -1);
				return h;
			}
			//Add juridiction to admin_groupe
			List<Integer> juridiction = Arrays.asList(8,9,10,11,12,13,14,15,16,17,18);
			System.out.println("List juri "+ juridiction.size());
			addJuridiction(juridiction, grp.getIdGroupe());
			
			List<Profile> listProfile = profilRepository.findByType("ADMIN_BANQUE");
			if(listProfile == null || listProfile.isEmpty())
			{
				h.put("message", "Le pofile ADMIN_BANQUE  n'existe pas.");
				h.put("status", -1);
				return h;
			}
			 
			String tokenString = userLoginService.getPasswordString();
	            String cryptPassword = userLoginService.encryptData(tokenString);
	            System.out.println("password crypte "+cryptPassword );
            
			UserBanque userBank = new UserBanque();
			userBank.setEmail(bank.getNom()+"@finappli.net");
			userBank.setLogin("admin_"+bank.getNom());
			userBank.setPrenom("admin_"+bank.getNom());
			userBank.setNom("admin_"+bank.getNom());
			userBank.setTelephone(bank.getTelephone1());
			userBank.setGroupeIdGroupe(grp);
			userBank.setIsActive(false);
			//userBank.setPassword(cryptPassword);
			userBank.setProfilIdProfil(listProfile.get(0));
			userBank = userBankRepository.save(userBank);
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("bank", bank);
		h.put("message", "La création de la banque s'est bien effectuée.");
		h.put("status", 0);
		return h;
	}
	
	
	/*
	 *  update a bank by id
	 */
	@RequestMapping(value="/bank/update/{id}", method=RequestMethod.PUT)
	public HashMap<String, Object> update(@RequestBody Bank bank, @PathVariable Integer id){
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		//Fields Control
		if(bank == null || id == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		//
				
		try {
			Bank bankOld = bRepository.findOne(id);
			if(bankOld == null ){
				h.put("message", "La banque n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			if(bank.getLocalityIdLocalite() == null){
				bank.setLocalityIdLocalite(bankOld.getLocalityIdLocalite());
			} else {
				Integer idLoc = bank.getLocalityIdLocalite().getIdLocalite();
				Locality locality = localityRepository.findOne(idLoc);
				if(locality == null){
					h.put("message", "La localité n'existe pas.");
					h.put("status", -1);
					return h;
				}
				if(iRepository.exitsByIdInstitutionNameOthers(bank.getNom(), locality,bankOld.getIdInstitution()))
				{
					h.put("message", "Le nom pour cette banque existe déjà dans la localité");
					h.put("status", -1);
					return h;
				}
				
				bank.setLocalityIdLocalite(locality);
			}
			
			bank.setIdInstitution(id);
			bank.setDateCreation(bankOld.getDateCreation());
			
			if (bank.getIsActive() == null) {
				bank.setIsActive(bankOld.getIsActive());
			}
			bank = bRepository.saveAndFlush(bank);
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("Banque", bank);
		h.put("message", "La modification de la banque s'est bien effectuée.");
		h.put("status", 0);
		return h;
	}
	
	/*
	 * get a bank by id
	 */
	@RequestMapping(value="/bank/list/{id}", method=RequestMethod.GET)
	public Map<String,Object> getBank(@PathVariable Integer id){
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		//Fields Control
		if(id == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		//
		
		try {
			Bank bank = bRepository.findOne(id);
			if (bank == null) {
				h.put("message", "La banque n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			h.put("message", "liste de banque");
			h.put("status", 0);
			h.put("bank", bank);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		return h;
	}
	
	/*
	 * delete a bank
	 */
	@RequestMapping(value="/bank/delete/{id}", method=RequestMethod.DELETE)
	public Map<String,Object> deleteBank(@PathVariable Integer id){
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		//Fields Control
		if(id == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		//
		
		Bank bank = bRepository.findOne(id);
		if (bank != null) {
			try {
				bRepository.delete(bank);
			} catch (Exception e) {
				e.printStackTrace();
				h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
				h.put("status", -1);
				return h;
			}
			h.put("message", "La banque a été bien supprimée.");
			h.put("status", 0);
			return h;
		}
		h.put("message", "La banque n'existe pas");
		h.put("status", -1);
		return h;
	}
	
	@RequestMapping(value="/bank/bank_conditions/list/{idBank}")
	public HashMap<String, Object> getAllBankCobditionByBank(@PathVariable Integer id){
		// idBank l' id la bank
		HashMap<String, Object> h = new HashMap<>();
		
		//Fields Control
		if(id == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		//
		
		try {
			Bank bank = bRepository.findOne(id);
			if (bank == null) {
				h.put("message", "La banque n'existe pes.");
				h.put("status", -1);
				return h;
			} 
			
			List<BankCondition> bankConditions = bCRepository.findByBank(bank);
			h.put("message", " Liste Banque condition par banque");
			h.put("list_bank_conditions", bankConditions);
			h.put("status", -1);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		return h;
	}
	
	/*
	 * fonction permettant de récupérer ts les utilisateur d'une banque
	 */
	@RequestMapping(value="/bank/users/list/{id}", method=RequestMethod.GET)
	public HashMap<String, Object> getAllUserByBank(@PathVariable Integer id){
		HashMap<String, Object> h = new HashMap<>();
		
		//Fields Control
		if(id == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		//
				
		try {
			Bank b = bRepository.findOne(id);
			if (b == null) {
				h.put("message", "La banque n'existe pas.");
				h.put("status", -1);
				return h;
			}
			List<User> users = uRep.findUserByINstitution(b);
			h.put("message", "Liste des utilisateurs par banque");
			h.put("list_users", users);
			h.put("status", 0);
	} catch (Exception e) {
		e.printStackTrace();
		h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
		h.put("status", -1);
		return h;
	}
		return h;
	}
	
	@RequestMapping(value="/bank/users", method=RequestMethod.GET)
	public HashMap<String, Object> getUsersByBank(){
		
		HashMap<String, Object> h= new HashMap<>();
		
		List<User> users = uRep.findUsersBank();
		h.put("message", "Liste utilisateurs par banque");
		h.put("list_users_bank", users);
		h.put("status", 0);
		return h;
	}
	
	/*
	 * get all banks by locality
	 */
	@RequestMapping(value="/bank/locality/{idLoc}", method=RequestMethod.GET)
	public HashMap<String, Object> getBanksByLocality(@PathVariable Integer idLoc) {
		HashMap<String, Object> h = new HashMap<>();
		
		//Fields Control
		if(idLoc == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		//
				
		Locality loc = localityRepository.findOne(idLoc);
		if (loc == null) {
			h.put("message", "La loccality n'existe pas.");
			h.put("status", -1);
			return h;
		}
		List<Bank> banks = bRepository.findByLocalityIdLocalite(loc);
		h.put("message", "liste des banks de la localité");
		h.put("list_banks", banks);
		h.put("status", 0);
		return h;
	}
	
public void addJuridiction(List<Integer> juridiction, Integer idGroupe) {
		
	JuridictionGroupe rHB = new JuridictionGroupe();
		//List<Juridiction> groupes = new ArrayList<>();
	Groupe g= groupRepository.findOne(idGroupe);
	
	if (juridiction.size() > 0) {
		
	
			List<JuridictionGroupe> juridictionGroupes = new ArrayList<>();
			juridiction.forEach(id->{
				Juridiction jur = jRep.findOne(id);
				if (jur != null) {
					JuridictionGroupe jGroupe = new JuridictionGroupe();
					jGroupe = g.addJuridictionGroupe(jur);
					try {
						jGRep.saveAndFlush(jGroupe);
						juridictionGroupes.add(jGroupe);
					} catch (Exception e) {
						e.printStackTrace();
						
					}
				}
			});
	
			Groupe gr= groupRepository.findOne(idGroupe);
			gr.setHasJuridiction(true);
			gr = groupRepository.saveAndFlush(gr);
		}
    
	}
	
/*
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
*/
	/*
	 * 
	 */
	
	public boolean compareChaine(String chaine1, String chaine2) {
        String sChaine1 = StringUtils.stripAccents(chaine1.toLowerCase());
        String sChaine2 = StringUtils.stripAccents(chaine2.toLowerCase());
        if(sChaine1.equals(sChaine2))
            return true;
        return false;
    }
}