package com.qualshore.etreasury.rest;

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

import com.qualshore.etreasury.dao.EtreasuryRepository;
import com.qualshore.etreasury.dao.GroupRepository;
import com.qualshore.etreasury.dao.LocalityRepository;
import com.qualshore.etreasury.dao.ProductsRepository;
import com.qualshore.etreasury.entity.Etreasury;
import com.qualshore.etreasury.entity.Groupe;
import com.qualshore.etreasury.entity.Locality;

@RequestMapping("/etreasury_project/admin/etreasury")
@RestController
public class EtreasuryRestController {
	@Autowired
	EtreasuryRepository bRepository;
	@Autowired
	LocalityRepository localityRepository;

	@Autowired
	ProductsRepository prRep;
	
	@Autowired
	GroupRepository groupRepository;
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public Map<String,Object> getBanks(){
	
		List<Etreasury> etreasurys = bRepository.findAll();
		HashMap<String, Object> h = new HashMap<String, Object>();
		h.put("message", "liste des institutions");
		h.put("status", 0);
		h.put("etreasury_list", etreasurys);
		return h;
	}
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public HashMap<String, Object> add(@RequestBody Etreasury etreasury){
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		try {
			etreasury.setIsActive(false);
			Integer idLoc = etreasury.getLocalityIdLocalite().getIdLocalite();
			Locality locality = localityRepository.findOne(idLoc);
			if(locality == null){
				h.put("message", "La localité n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			etreasury.setLocalityIdLocalite(locality);
			//LocalDate localDate = LocalDate.now(ZoneId.of("GMT+00:00"));
			//LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
			Date date = new Date();
			etreasury.setDateCreation(date);
			etreasury = bRepository.save(etreasury);
			/* Default group for users */
			groupRepository.save(new Groupe("default_"+etreasury.getNom(), "Default Group", etreasury));
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("bank", etreasury);
		h.put("message", "success");
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value="/update/{id}", method=RequestMethod.PUT)
	public HashMap<String, Object> update(@RequestBody Etreasury etreasury, @PathVariable Integer id){

		HashMap<String, Object> h = new HashMap<String, Object>();
		try {
			Etreasury etreasuryOld = bRepository.findOne(id);
			if(etreasuryOld == null ){
				h.put("message", "La banque n'existe pas.");
				h.put("status", -1);
				return h;
			}
			if(etreasury.getLocalityIdLocalite() == null){
				etreasury.setLocalityIdLocalite(etreasuryOld.getLocalityIdLocalite());
			} else {
				Integer idLoc = etreasury.getLocalityIdLocalite().getIdLocalite();
				Locality locality = localityRepository.findOne(idLoc);
				if(locality == null){
					h.put("message", "La localité n'existe pas.");
					h.put("status", -1);
					return h;
				}
				etreasury.setLocalityIdLocalite(locality);
			}
			
			etreasury.setIdInstitution(id);
			etreasury.setDateCreation(etreasuryOld.getDateCreation());
			
			if (etreasury.getIsActive() == null) {
				etreasury.setIsActive(etreasuryOld.getIsActive());
			}
			etreasury = bRepository.saveAndFlush(etreasury);
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("Etreasury", etreasury);
		h.put("message", "success");
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value="/list/{id}", method=RequestMethod.GET)
	public Map<String,Object> getBank(@PathVariable Integer id){
		
		HashMap<String, Object> h = new HashMap<String, Object>();
		Etreasury etreasury = bRepository.findOne(id);
		if (etreasury == null) {
			h.put("message", "L'institution n'existe pas.");
			h.put("status", -1);
			return h;
		}
		
		h.put("message", "success");
		h.put("status", 0);
		h.put("etreasury", etreasury);
		return h;
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.DELETE)
	public Map<String,Object> deleteBank(@PathVariable int id){
		HashMap<String, Object> h = new HashMap<String, Object>();
		Etreasury etreasury = bRepository.findOne(id);
		if (etreasury != null) {
			try {
				bRepository.delete(etreasury);
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
		h.put("message", "La banque n'existe pas.");
		h.put("status", -1);
		return h;
	}
}