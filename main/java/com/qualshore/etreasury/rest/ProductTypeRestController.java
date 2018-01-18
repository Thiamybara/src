package com.qualshore.etreasury.rest;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qualshore.etreasury.dao.ProductTypeRepository;
import com.qualshore.etreasury.entity.ProductType;

@RequestMapping("etreasury_project/admin/product_type")
@RestController
public class ProductTypeRestController {
	
	@Autowired
	ProductTypeRepository pTRep;
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public HashMap<String, Object> getProductTypes(){
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		List<ProductType> productTypes = pTRep.findAll();
		h.put("message", "liste des types de produits");
		h.put("product_types_list", productTypes);
		h.put("status", -1);
		return h;
	}
	
	@RequestMapping(value="/list/{id}", method=RequestMethod.GET)
	public HashMap<String, Object> getProductType(@PathVariable int id){
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		ProductType productType = pTRep.findOne(id);
		
		if(productType == null){
			h.put("message", "Le produit n'existe pas.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "success");
		h.put("product_types_list", productType);
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public HashMap<String, Object> add(@RequestBody ProductType pt){
		HashMap<String, Object> h = new HashMap<String, Object>();
		if (pt.getDescription().equals("") || pt.getLibelle().equals("")) {
			h.put("message", "Aucun des champs ne doit être vide.");
			h.put("status", -1);
			return h;
		}
		try {
			pt = pTRep.save(pt);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "success");
		h.put("product_type", pt);
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value="/update/{id}", method=RequestMethod.PUT)
	public HashMap<String, Object> update(@RequestBody ProductType pt, @PathVariable int id){
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		ProductType oldPT = pTRep.findOne(id);
		if (oldPT == null) {
			h.put("message", "Le type de produit n'existe pas.");
			h.put("status", -1);
			return h;
		}
		if (pt.getDescription().equals("") || pt.getLibelle().equals("")) {
			h.put("message", "Aucun des champs ne doit être vide.");
			h.put("status", -1);
			return h;
		}
		pt.setIdTypeProduits(oldPT.getIdTypeProduits());
		pt = pTRep.saveAndFlush(pt);
		h.put("message", "Le produit est modifié avec succès.");
		h.put("product_type", pt);
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.DELETE)
	public HashMap<String, Object> delete(@PathVariable int id){
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		ProductType productType = pTRep.findOne(id);
		
		if(productType == null){
			h.put("message", "Le produit n'existe pas.");
			h.put("status", -1);
			return h;
		}
		pTRep.delete(productType);
		h.put("message", "Le produit est supprimé avec succès.");
		h.put("status", 0);
		return h;
	}
	
	
}
