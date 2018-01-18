package com.qualshore.etreasury.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qualshore.etreasury.dao.GroupRepository;
import com.qualshore.etreasury.dao.InstitutionRepository;
import com.qualshore.etreasury.dao.JuridictionGroupeRepository;
import com.qualshore.etreasury.dao.JuridictionRepository;
import com.qualshore.etreasury.dao.ProductsRepository;
import com.qualshore.etreasury.dao.UserRepository;
import com.qualshore.etreasury.entity.Groupe;
import com.qualshore.etreasury.entity.Institution;
import com.qualshore.etreasury.entity.Juridiction;
import com.qualshore.etreasury.entity.JuridictionGroupe;
import com.qualshore.etreasury.entity.Products;
import com.qualshore.etreasury.entity.User;

@RestController
@RequestMapping("/etreasury_project/admin/juridictions")
public class JuridictionRestController {
	
	@Autowired
	JuridictionRepository jRep;
	@Autowired
	GroupRepository gRep;
	@Autowired
	InstitutionRepository iRep;
	@Autowired
	ProductsRepository pRep;
	@Autowired
	JuridictionGroupeRepository jGrep;
	@Autowired
	UserRepository userRep;
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public HashMap<String, Object> getAllJuridictions(){
		
		HashMap<String, Object> h = new HashMap<>();
		List<Juridiction> juridictions = jRep.findAll();
		h.put("message", "success");
		h.put("list_juridictions", juridictions);
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value="/list/{idUser}", method=RequestMethod.GET)
	public HashMap<String, Object> getAllJuridictionsByInstitution(@PathVariable Integer idUser){
		
		HashMap<String, Object> h = new HashMap<>();
		List<Juridiction> juridictions = new ArrayList<>();
		if(idUser == null )
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
		}
		try
		{
			User user = userRep.findOne(idUser);
			if(user == null)
			{
				h.put("message", "Cet utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			System.out.println("USer info "+ user.getDiscriminatorValue());
			if (user.getDiscriminatorValue().equals("BA")) {
				juridictions = jRep.findAllBankJuridiction();
				System.out.println("List size bank "+ juridictions.size());
			}
			else if(user.getDiscriminatorValue().equals("EN")) {
				juridictions = jRep.findAllEnterpriseJuridiction();
				System.out.println("List size enterprise "+ juridictions.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "success");
		h.put("list_juridictions", juridictions);
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value="/list_juridiction/{id}", method=RequestMethod.GET)
	public HashMap<String, Object> getJuridictionByid(@PathVariable int id){
		
		HashMap<String, Object> h = new HashMap<>();
		Juridiction juridiction = jRep.findOne(id);
		if (juridiction == null) {
			h.put("message", "La juridiction n'existe pas.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "success");
		h.put("juridiction", juridiction);
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value="/libelle/{libelle}", method=RequestMethod.GET)
	public HashMap<String, Object> getJuridictionByLibelle(@PathVariable String libelle){
		
		HashMap<String, Object> h = new HashMap<>();
		Juridiction juridiction = jRep.findByLibelle(libelle);
		if (juridiction == null) {
			h.put("message", "La juridiction n'existe pas.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "success");
		h.put("juridiction", juridiction);
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value="/list/groupe/produit/{idInst}/{idProd}", method=RequestMethod.GET)
	public HashMap<String, Object> getGroupesByProductJuridiction(@PathVariable("idProd") int idProd,
			 @PathVariable("idInst") int idInst){
		
		HashMap<String, Object> h = new HashMap<>();
		Institution institution = iRep.findOne(idInst);
		if (institution == null) {
			h.put("message", "L' institution n'existe pas.");
			h.put("status", "-1");
			return h;
		}
		Products p = pRep.findOne(idProd);
		if (p == null) {
			h.put("message", "Le produit n'existe pas.");
			h.put("status", "-1");
			return h;
		}
		List<Groupe> groupes = gRep.findByInstitution(institution);
		List<Groupe> habilitysGroupe = new ArrayList<>();
		
		for (Groupe g: groupes) {
			List<JuridictionGroupe> jGrs = jGrep.findByGroupe(g);
			if (jGrs == null) {
				h.put("message", "Le groupe n'a aucune habilitation.");
				h.put("status", "-1");
				return h;
			}
			System.out.println(g);
			for (JuridictionGroupe jGroupe : jGrs) {
				System.out.println(jGroupe.getJuridiction().getProduitName());
				if (jGroupe.getJuridiction().getProduitName().equals(p.getNom())) {
					habilitysGroupe.add(g);
				}
			}
		}		
		h.put("message", "liste des groupes");
		h.put("liste_groupes", habilitysGroupe);
		h.put("status", "0");
		return h;
	}
}
