package com.qualshore.etreasury.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qualshore.etreasury.dao.EnterpriseRepository;
import com.qualshore.etreasury.dao.GroupRepository;
import com.qualshore.etreasury.dao.InstitutionRepository;
import com.qualshore.etreasury.dao.JuridictionGroupeRepository;
import com.qualshore.etreasury.dao.JuridictionRepository;
import com.qualshore.etreasury.dao.LocalityRepository;
import com.qualshore.etreasury.dao.ProfileRepository;
import com.qualshore.etreasury.dao.RequestRepository;
import com.qualshore.etreasury.dao.UserEntrepriseRepository;
import com.qualshore.etreasury.dao.UserRepository;
import com.qualshore.etreasury.entity.Bank;
import com.qualshore.etreasury.entity.Enterprise;
import com.qualshore.etreasury.entity.Groupe;
import com.qualshore.etreasury.entity.Juridiction;
import com.qualshore.etreasury.entity.JuridictionGroupe;
import com.qualshore.etreasury.entity.Locality;
import com.qualshore.etreasury.entity.Profile;
import com.qualshore.etreasury.entity.Request;
import com.qualshore.etreasury.entity.User;
import com.qualshore.etreasury.entity.UserEntreprise;
import com.qualshore.etreasury.service.UserService;

@RequestMapping("/etreasury_project/admin/entreprise")
@RestController
public class EnterpriseRestController {
	
	@Autowired
	EnterpriseRepository eRepository;
	@Autowired
	LocalityRepository localityRepository;
	@Autowired
	GroupRepository groupRepository;
	@Autowired
	RequestRepository rRep;
	@Autowired
	UserRepository uRep;
	@Autowired
	UserService userService;
	@Autowired
	InstitutionRepository iRepository;
	@Autowired
	UserEntrepriseRepository userEntrepriseRepository;
	@Autowired
	ProfileRepository profilRepository;
	@Autowired
	JuridictionGroupeRepository  jGRep;
	@Autowired
	JuridictionRepository jRep;
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public Map<String,Object> getEnterprises(){
		
		List<Enterprise> enterprises = eRepository.findAll();
		HashMap<String, Object> h = new HashMap<String, Object>();
		h.put("message", "liste des enterprises");
		h.put("status", 0);
		h.put("entreprise_list", enterprises);
		return h;
	}
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public HashMap<String, Object> add(@RequestBody Enterprise enterprise){
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		//Fields Control
		if(enterprise == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		if(enterprise.getNom().equals("") || enterprise.getTelephone1().equals("") || enterprise.getLocalityIdLocalite() == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres vides");
			return h;
		}
		if(! userService.isPhoneValid(enterprise.getTelephone1()))
		{
			h.put("status", -1);
			h.put("message", "Le numéro de téléphone mobile est incorrect.");
			return h;
		}
		if(enterprise.getTelephone2() != null && !userService.isPhoneValid(enterprise.getTelephone2()))
		{
			h.put("status", -1);
			h.put("message", "Le numéro de téléphone fixe est incorrect.");
			return h;
		}
		//
				
		try {
			enterprise.setIsActive(false);
			Integer idLoc = enterprise.getLocalityIdLocalite().getIdLocalite();
			Locality locality = localityRepository.findOne(idLoc);
			if(locality == null){
				h.put("message", "La localité n'existe pas.");
				h.put("status", -1);
				return h;
			}
			enterprise.setLocalityIdLocalite(locality);
			Date date = new Date();
			enterprise.setDateCreation(date);
			enterprise = eRepository.save(enterprise);
			/* Default group for users */
			groupRepository.save(new Groupe("default_"+enterprise.getNom(), "Default Group", enterprise));
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("enterprise", enterprise);
		h.put("message", "success");
		h.put("status", 0);
		return h;
	}
	
	
	@RequestMapping(value="/add/groupes/{idAdmin}", method=RequestMethod.POST)
	public HashMap<String, Object> addEntreprise(@RequestBody Enterprise enterprise,@PathVariable Integer idAdmin){
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		//Fields Control
		if(enterprise == null || idAdmin == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		if(enterprise.getNom().equals("") || enterprise.getTelephone1().equals("") || enterprise.getLocalityIdLocalite() == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres vides");
			return h;
		}
		if(! userService.isPhoneValid(enterprise.getTelephone1()))
		{
			h.put("status", -1);
			h.put("message", "Le numéro de téléphone mobile est incorrect.");
			return h;
		}
		if(enterprise.getTelephone2() != null && !userService.isPhoneValid(enterprise.getTelephone2()))
		{
			h.put("status", -1);
			h.put("message", "Le numéro de téléphone fixe est incorrect.");
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
			
			enterprise.setIsActive(false);
			Integer idLoc = enterprise.getLocalityIdLocalite().getIdLocalite();
			Locality locality = localityRepository.findOne(idLoc);
			if(locality == null){
				h.put("message", "La localité n'existe pas.");
				h.put("status", -1);
				return h;
			}
			System.out.println("nom bk existe "+iRepository.exitsByIdInstitutionName(enterprise.getNom(), locality));
			if(iRepository.exitsByIdInstitutionNameEtreprise(enterprise.getNom(), locality))
			{
				h.put("message", "Le nom pour cette entreprise existe déjà dans la localité.");
				h.put("status", -1);
				return h;
			}
			enterprise.setLocalityIdLocalite(locality);
			//LocalDate localDate = LocalDate.now(ZoneId.of("GMT+00:00"));
			//LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
			Date date = new Date();
			enterprise.setDateCreation(date);
			enterprise = eRepository.save(enterprise);
			/* Default group for users */
			groupRepository.save(new Groupe("default_"+enterprise.getNom(), "Default Group", enterprise));
			groupRepository.save(new Groupe("admin_"+enterprise.getNom(), "Admin Group", enterprise));
			
			/*
			List<Groupe> listGroupe = groupRepository.findByNom("admin_" +enterprise.getNom());
			if(listGroupe == null || listGroupe.isEmpty())
			{
				h.put("message", "le groupe admin n'existe pas");
				h.put("status", -1);
				return h;
			}
			*/
			Groupe grp = groupRepository.findByInstition(enterprise);
			if(grp == null )
			{
				h.put("message", "Le groupe admin n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			//Add juridiction to admin_groupe
			List<Integer> juridiction = Arrays.asList(8,9,10,11,12,13,14,15,16,17,18,19,20,21);
			System.out.println("List juri "+ juridiction.size());
			addJuridiction(juridiction, grp.getIdGroupe());
			
			List<Profile> listProfile = profilRepository.findByType("ADMIN_ENTREPRISE");
			if(listProfile == null || listProfile.isEmpty())
			{
				h.put("message", "Le pofil ADMIN_ENTREPRISE  n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			UserEntreprise userEntreprise = new UserEntreprise();
			userEntreprise.setEmail(enterprise.getNom()+"@finappli.net");
			userEntreprise.setLogin("admin_"+enterprise.getNom());
			userEntreprise.setPrenom("admin_"+enterprise.getNom());
			userEntreprise.setNom("admin_"+enterprise.getNom());
			userEntreprise.setTelephone(enterprise.getTelephone1());
			userEntreprise.setIsActive(false);
			userEntreprise.setGroupeIdGroupe(grp);
			userEntreprise.setProfilIdProfil(listProfile.get(0));
			userEntreprise = userEntrepriseRepository.save(userEntreprise);
			//userEntrepriseRepository.save(new User(, , grp, listProfile.get(0)));
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("enterprise", enterprise);
		h.put("message", "La création de l'entreprise s'est bien effectuée.");
		h.put("status", 0);
		return h;
	}
	@RequestMapping(value="/update/{id}", method=RequestMethod.PUT)
	public HashMap<String, Object> update(@RequestBody Enterprise enterprise, @PathVariable Integer id){	
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		//Fields Control
		if(enterprise == null || id == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		//
				
		try {
			Enterprise enterpriseOld = eRepository.findOne(id);
			if(enterpriseOld == null ){
				h.put("message", "L'entreprise n'existe pas.");
				h.put("status", -1);
				return h;
			}
			if(enterprise.getLocalityIdLocalite() == null){
				enterprise.setLocalityIdLocalite(enterpriseOld.getLocalityIdLocalite());
			} else {
				Integer idLoc = enterprise.getLocalityIdLocalite().getIdLocalite();
				Locality locality = localityRepository.findOne(idLoc);
				if(locality == null){
					h.put("message", "La localité n'existe pas.");
					h.put("status", -1);
					return h;
				}
				if(iRepository.exitsByIdInstitutionNameEtrepriseOthers(enterprise.getNom(), locality,enterpriseOld.getIdInstitution()))
				{
					h.put("message", "Le nom pour cette entreprise existe déjà dans la localité.");
					h.put("status", -1);
					return h;
				}
				enterprise.setLocalityIdLocalite(locality);
			}
			
			enterprise.setIdInstitution(id);
			enterprise.setDateCreation(enterpriseOld.getDateCreation());
			
			if (enterprise.getIsActive() == null) {
				enterprise.setIsActive(enterpriseOld.getIsActive());
			}
			enterprise = eRepository.saveAndFlush(enterprise);
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("Enterprise", enterprise);
		h.put("message", "La modification de la banque s'est bien effectuée.");
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value="/list/{id}", method=RequestMethod.GET)
	public Map<String,Object> getEnterprise(@PathVariable Integer id){
		
		HashMap<String, Object> h = new HashMap<String, Object>();
		Enterprise enterprise = eRepository.findOne(id);
		if (enterprise == null) {
			h.put("message", "L'entreprise n'existe pas.");
			h.put("status", -1);
			return h;
		}
		
		h.put("message", "donnees de l'entreprise");
		h.put("status", 0);
		h.put("Enterprise", enterprise);
		return h;
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.DELETE)
	public Map<String,Object> deleteEnterprise(@PathVariable int id){
		HashMap<String, Object> h = new HashMap<String, Object>();
		Enterprise enterprise = eRepository.findOne(id);
		if (enterprise != null) {
			try {
				eRepository.delete(enterprise);
			} catch (Exception e) {
				e.printStackTrace();
				h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
				h.put("status", -1);
				return h;
			}
			h.put("message", "L'entreprise a été bien supprimée.");
			h.put("status", 0);
			return h;
		}
		h.put("message", "L'entreprise n'existe pas.");
		h.put("status", -1);
		return h;
	}

	@RequestMapping("/request/list/{idEnterprise}")
	public HashMap<String, Object> getAllRequestByEntreprise(@PathVariable("idEnterprise") int id){
		
		HashMap<String, Object> h= new HashMap<>();
		Enterprise e = eRepository.findOne(id);
		if (e == null) {
			h.put("message", "L'entreprise n'existe pas.");
			h.put("status", -1);
			return h;
		}
		List<Request> requests = rRep.findByEnterprise(e);
		h.put("message", "La liste de banque.");
		h.put("list_request", requests);
		h.put("status", 0);
		return h;
	}
	
	/*
	 * fonction permettant de récupérer ts les utilisateur d'une entreprise
	 */
	@RequestMapping(value="/users/list/{idEnterprise}", method=RequestMethod.GET)
	public HashMap<String, Object> getAllUserByEnterprise(@PathVariable("idEnterprise") int id){
		
		HashMap<String, Object> h = new HashMap<>();
		Enterprise e = eRepository.findOne(id);
		if (e == null) {
			h.put("message", "L'entreprise n'existe pas.");
			h.put("status", -1);
			return h;
		}
		List<User> users = uRep.findUserByINstitution(e);
		h.put("message", "La liste des utilisateurs de l'entreprise");
		h.put("list_users", users);
		h.put("status", 0);
		return h;
	}
	/*
	 * get all banks by locality
	 */
	@RequestMapping(value="/locality/{idLoc}", method=RequestMethod.GET)
	public HashMap<String, Object> getEnterprisesByLocality(@PathVariable Integer idLoc) {
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
		List<Enterprise> entrepriseLists = eRepository.findByLocalityIdLocalite(loc);
		if(entrepriseLists.size() <= 0) {
			h.put("message", "Il n'y a pas d'entreprise dans votre localité.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "liste des entreprises de la localité");
		h.put("list_enterprises", entrepriseLists);
		h.put("status", 0);
		return h;
	}
	/*
	 * fonction permettant de récupérer ts les utilisateur d'une banque
	 */
	/*@RequestMapping(value="/bank/users/list/{idBank}", method=RequestMethod.GET)
	public HashMap<String, Object> getAllUserByBank(@PathVariable("idBank") int id){
		
		HashMap<String, Object> h = new HashMap<>();
		Bank b = bRepository.findOne(id);
		if (b == null) {
			h.put("message", "la banque n'existe pas");
			h.put("status", -1);
			return h;
		}
		List<User> users = uRep.findUserByINstitution(b);
		h.put("message", "success");
		h.put("list_users", users);
		h.put("status", 0);
		return h;
	}*/
	
	@RequestMapping(value="/users", method=RequestMethod.GET)
	public HashMap<String, Object> getUsersByEntreprise(){
		
		HashMap<String, Object> h= new HashMap<>();
		
		List<User> users = uRep.findUsersEntreprise();
		h.put("message", "success");
		h.put("list_users_entreprise", users);
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
		
}
