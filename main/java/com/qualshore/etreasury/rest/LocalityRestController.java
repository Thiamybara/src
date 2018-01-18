package com.qualshore.etreasury.rest;

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


import com.qualshore.etreasury.dao.LocalityRepository;
import com.qualshore.etreasury.entity.Locality;



@RequestMapping("/etreasury_project/locality")
@RestController
public class LocalityRestController {
	
	@Autowired
	public LocalityRepository localityRepository;
	 
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public Map<String,Object> getLocalities(){
		
		List<Locality> localities = localityRepository.findAll();
		HashMap<String, Object> h = new HashMap<String, Object>();
		h.put("message", "liste des localités");
		h.put("status", 0);
		h.put("locality_list", localities);
		return h;
	}
	
	@RequestMapping(value="/list/{id}", method=RequestMethod.GET)
	public HashMap<String, Object> getLocality(@PathVariable Integer id){
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		//Fields Control
		if (id == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", 0);
			return h;
		}
		//
		
		try {
			Locality locality = localityRepository.findOne(id);
			if (locality == null)
			{
				h.put("message", "Cette localité n'existe pas.");
				h.put("status", -1);
				return h;
			}
			h.put("message", "localité trouvé");
			h.put("status", 0);
			h.put("locality", locality);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		return h;
	}
	
	@RequestMapping(value="/add/code", method=RequestMethod.POST)
	public Map<String, Object> add(@RequestBody Locality locality){
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		//Fields Control
		if(locality == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
			return h;
		}
		if(locality.getVille().equals("") || locality.getPays().equals(""))
		{
			h.put("message", "1 ou plusieurs paramètres vides");
			h.put("status", -1);
			return h;
		}
		//
		
		try {
			String ville = locality.getVille();
			String code = locality.getCode();
			String pays = locality.getPays();
		
			if(!ville.equals("") && !code.equals("") && !pays.equals(""))
			{
				List<Locality> localities = localityRepository.findAll();
				for(Locality loc: localities)
				{
					if(loc.getPays().equals(pays) && loc.getVille().equals(ville))
					{
						h.put("message", "Le couple ville et pays doit être unique.");
						h.put("status", -1);
						return h;
					}
				}
				locality = localityRepository.save(locality);
			}
		}catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		if(locality.getIdLocalite() != null){
			h.put("message", "La localité est créée avec succés.");
			h.put("locality", locality);
			h.put("status", 0);
			return h;
		}
		h.put("message", "Aucun des champs ne doit être vide.");
		h.put("status", -1);
		return h;
	}
	
	//Insertion auto de code dans locality
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public Map<String, Object> addLocality(@RequestBody Locality locality){
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		//Fields Control
		if(locality == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
			return h;
		}
		if(locality.getVille().equals("") || locality.getPays().equals(""))
		{
			h.put("message", "1 ou plusieurs paramètres vides");
			h.put("status", -1);
			return h;
		}
		//
				
		String ville = locality.getVille();
		//String code = locality.getCode();
		String pays = locality.getPays();
		
		if(!ville.equals("")  && !pays.equals(""))
		{
			List<Locality> localities = localityRepository.findAll();
			for(Locality loc: localities)
			{
				if(loc.getPays().toLowerCase().equals(pays.toLowerCase()) && loc.getVille().toLowerCase().equals(ville.toLowerCase()))
				{
					h.put("message", "Le couple ville et pays doit être unique.");
					h.put("status", -1);
					return h;
				}
				/*
				String paysStrip = org.apache.commons.lang3.StringUtils.stripAccents(loc.getPays().toLowerCase());
				String VilleStrip = org.apache.commons.lang3.StringUtils.stripAccents(loc.getVille().toLowerCase());
				String vilStrip = org.apache.commons.lang3.StringUtils.stripAccents(ville.toLowerCase());
				String payStrip = org.apache.commons.lang3.StringUtils.stripAccents(pays.toLowerCase());
				*/
				//System.out.println("Comapraison "+paysStrip +" "+payStrip);
				if(compareChaine(loc.getPays(), pays) == true && compareChaine(loc.getVille(), ville) == true)
				{
					h.put("message", "Le couple ville et pays doit être unique.");
					h.put("status", -1);
					return h;
				}
			}
			
			try {
				System.out.println("code "+ ville.substring(0, 2)+"_"+pays.substring(0, 2));
				locality.setCode(ville.substring(0, 3).toLowerCase()+"_"+pays.substring(0, 3).toLowerCase());
				locality = localityRepository.save(locality);
			} catch (Exception e) {
				e.printStackTrace();
				h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
				h.put("status", -1);
				return h;
			}
			if(locality.getIdLocalite() != null){
				h.put("message", "La localité est créée avec succés.");
				h.put("locality", locality);
				h.put("status", 0);
				return h;
			}
		}
			h.put("message", "Aucun des champs ne doit être vide.");
			h.put("status", -1);
			return h;
	}
	
	@RequestMapping(value="/update/{id}", method=RequestMethod.PUT)
	public HashMap<String, Object> update(@RequestBody Locality locality, @PathVariable Integer id) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		//Fields Control
		if(locality == null || id == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
			return h;
		}
		//
		locality.setIdLocalite(id);
		
		List<Locality> localities = localityRepository.findAll();
		for(Locality loc: localities){
			/*
			if(loc.getPays().equals(locality.getPays().toLowerCase()) && loc.getVille().equals(locality.getVille().toLowerCase())){
				h.put("message", "le couple ville et pays doit être unique");
				h.put("status", -1);
				return h;
			}
			*/
			if(loc.getPays().toLowerCase().equals(locality.getPays().toLowerCase()) && loc.getVille().toLowerCase().equals(locality.getVille().toLowerCase()))
			{
				h.put("message", "Le couple ville et pays doit être unique.");
				h.put("status", -1);
				return h;
			}
			if(compareChaine(loc.getPays(), locality.getPays()) == true && compareChaine(loc.getVille(), locality.getVille()) == true)
			{
				h.put("message", "Le couple ville et pays doit être unique.");
				h.put("status", -1);
				return h;
			}
		}
		
		try {
			System.out.println("code "+ locality.getVille().substring(0, 2)+"_"+locality.getPays().substring(0, 2));
			locality.setCode(locality.getVille().substring(0, 3).toLowerCase()+"_"+locality.getPays().substring(0, 3).toLowerCase());
			locality = localityRepository.saveAndFlush(locality);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("locality", locality);
		h.put("message", "La modification s'est effectuée avec succés.");
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.DELETE)
	public HashMap<String, Object> delete(@PathVariable Integer id){
		HashMap<String, Object> h = new HashMap<String, Object>();
		//Fields Control
		if(id == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
			return h;
		}
		//
		
		try {
			localityRepository.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "La localité est supprimée avec succés.");
		h.put("status", 0);
		return h;
	}
	public boolean compareChaine(String chaine1, String chaine2) {
        String sChaine1 = StringUtils.stripAccents(chaine1.toLowerCase());
        String sChaine2 = StringUtils.stripAccents(chaine2.toLowerCase());
        if(sChaine1.equals(sChaine2))
            return true;
        return false;
    }
}
