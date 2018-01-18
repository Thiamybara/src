package com.qualshore.etreasury.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qualshore.etreasury.dao.ProductTypeRepository;
import com.qualshore.etreasury.dao.ProductsRepository;
import com.qualshore.etreasury.entity.ProductType;
import com.qualshore.etreasury.entity.Products;

@RequestMapping("/etreasury_project/admin/product")
@RestController
public class ProductsRestController {
	
	@Autowired
	ProductsRepository pRep;
	@Autowired
	ProductTypeRepository proTypeRep;
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public HashMap<String, Object> getAllProducts(){
		
		HashMap<String, Object> h = new HashMap<>();
		List<Products> products = pRep.findAll();
		h.put("message", "success");
		h.put("list_products", products);
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value="/list/{id}", method=RequestMethod.GET)
	public HashMap<String, Object> getProduct(@PathVariable int id){
		
		HashMap<String, Object> h = new HashMap<>();
		Products product = pRep.findOne(id);
		
		if (product == null) {
			h.put("message", "Le produit n'existe pas.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "success");
		h.put("list_products", product);
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public HashMap<String, Object> add(@RequestBody Products product){
		
		HashMap<String, Object> h = new HashMap<>();
		/*
		if (product.getProductType() == null ) {
			h.put("message", "le champ productType est vide ou le type n'existe pas");
			h.put("status", -1);
			return h;
		}
		*/
		try {
			List<ProductType> listProductType = proTypeRep.findByLibelle("default_type");
			if(listProductType == null || listProductType.isEmpty())
			{
				h.put("message", "Le default product n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			if(pRep.existsByProductName(product.getNom())) {
				h.put("message", "Un produit avec le même nom existe déjà.");
				h.put("status", -1);
				return h;
			}
			product.setDate(new Date());
			product.setProductType(listProductType.get(0));
			product = pRep.save(product);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("product", product);
		h.put("message", "Le produit est ajouté avec succès.");
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value="/update/{id}", method=RequestMethod.PUT)
	public HashMap<String, Object> update(@RequestBody Products product, @PathVariable int id){
		
		HashMap<String, Object> h = new HashMap<>();
		Products oldPr = pRep.findOne(id);
		if (oldPr == null) {
			h.put("message", "Le produit n'existe pas.");
			h.put("status", -1);
			return h;
		}
		
		product.setDate(oldPr.getDate());
		product.setIdProduits(oldPr.getIdProduits());
		product = pRep.saveAndFlush(product);
		h.put("product", product);
		h.put("message", "success");
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value="/delete/{id}", method=RequestMethod.DELETE)
	public HashMap<String, Object> delete(@PathVariable int id){
		
		HashMap<String, Object> h = new HashMap<>();
		Products product = pRep.findOne(id);
		
		if (product == null) {
			h.put("message", "Le produit n'existe pas.");
			h.put("status", -1);
			return h;
		}
		pRep.delete(product);
		h.put("message", "success");
		h.put("status", 0);
		return h;
	}
}
