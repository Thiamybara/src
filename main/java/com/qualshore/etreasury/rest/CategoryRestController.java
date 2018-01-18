 package com.qualshore.etreasury.rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.qualshore.etreasury.dao.ActionRepository;
import com.qualshore.etreasury.dao.CategoryRepository;
import com.qualshore.etreasury.dao.DocumentsRepository;
import com.qualshore.etreasury.dao.InstitutionRepository;
import com.qualshore.etreasury.dao.LogRepository;
import com.qualshore.etreasury.dao.UserRepository;
import com.qualshore.etreasury.entity.Category;
import com.qualshore.etreasury.entity.Documents;
import com.qualshore.etreasury.entity.Institution;
import com.qualshore.etreasury.entity.Log;
import com.qualshore.etreasury.entity.User;

@RequestMapping("etreasury_project/mon_espace/category")
@RestController
public class CategoryRestController {

	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	InstitutionRepository institutionRepository;
	
	@Autowired
	LogRepository logRepository;
	
	@Autowired
	ActionRepository actionRepository;
	
	@Autowired
	Environment env;
	
	@Autowired
	DocumentsRepository documentsRepository;
	
	
	//Ajout de categorie
	@RequestMapping(value = "/add/{idUtilisateur}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> add(@RequestBody Category category, @PathVariable Integer idUtilisateur) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		
		//Fields Control
		if(category == null || idUtilisateur == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
			return h;
		}
		if(category.getLibelle().equals(""))
		{
			h.put("message", "1 ou plusieurs paramètres vides");
			h.put("status", -1);
			return h;
		}
		//
		
		try
		{
			User userSession = userRepository.findOne(idUtilisateur);
			if(userSession == null)
			{
				h.put("message", "Cet utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			Integer idUserProp = category.getUserIdUtilisateur().getIdUtilisateur();
			User userProp = userRepository.findOne(idUserProp);
			if(userProp == null)
			{
				h.put("message", "L'utilisateur propriétaire n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			if(categoryRepository.existsByUserCategory(category.getLibelle(), userSession))
			{
				h.put("message", "Une catégorie avec le même nom existe déjà pour cet utilisateur.");
				h.put("status", -1);
				return h;
			}
			
			if(categoryRepository.existsByInstitutionCategory(category.getLibelle(), userSession.getGroupeIdGroupe().getInstitution()))
			{
				h.put("message", "Une catégorie avec le même nom existe déjà pour l'institution de l'utilisateur.");
				h.put("status", -1);
				return h;
			}
			
			category.setUserIdUtilisateur(userProp);
			category.setDateCreation(new Date());
			if(category.getParent() == null)
			{
				category.setRepertoire(getCategoryRootLocation(category));
			}
			else
			{
				Category parent = categoryRepository.findOne(category.getParent().getIdCategory());
				if(parent == null)
				{
					h.put("message", "La catégorie parent n'existe pas.");
					h.put("status", -1);
					return h;
				}
				category.setRepertoire(parent.getRepertoire()+ "/"+ category.getLibelle());
			}
			category = categoryRepository.save(category);
			
			createCategoryLocation(category.getRepertoire());
			journaliser(idUtilisateur, 6, category.getIdCategory());
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("category", category);
		h.put("message", "Catégorie ajoutée avec succès");
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value = "/update/{idUtilisateur}/{idCategory}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> update(@RequestBody Category category,
									 @PathVariable Integer idUtilisateur,
									 @PathVariable Integer idCategory) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(category == null || idUtilisateur == null || idCategory == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
			return h;
		}
		try
		{
			User userSession = userRepository.findOne(idUtilisateur);
			if(userSession == null)
			{
				h.put("message", "Cet utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			Category categoryOld = categoryRepository.findOne(idCategory);
			if(categoryOld == null)
			{
				h.put("message", "Cette catégorie n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			Integer idUserProp = category.getUserIdUtilisateur().getIdUtilisateur();
			User userProp = userRepository.findOne(idUserProp);
			if(userProp == null)
			{
				h.put("message", "L'utilisateur propriétaire n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			if(!categoryRepository.isProprietaire(idCategory, userSession))
			{
				if(! userSession.getGroupeIdGroupe().getInstitution().getIdInstitution().equals(category.getUserIdUtilisateur().getGroupeIdGroupe().getInstitution().getIdInstitution()))
				{
					h.put("message", "Cet utilisateur et le propriétaire de la catégorie ne sont pas de la même institution.");
					h.put("status", -1);
					return h;
				}
				else if(userSession.getProfilIdProfil().getType().equals("USER_HABILITY"))
				{
					h.put("message", "Cet utilisateur n'a pas les droits pour modifier la catégorie.");
					h.put("status", -1);
					return h;
				}		
			}
			category.setUserIdUtilisateur(userProp);
			
			if(category.getDateCreation() == null)
			{
				category.setDateCreation(categoryOld.getDateCreation());
			}
			
			if(category.getLibelle() == null)
			{
				category.setLibelle(categoryOld.getLibelle());
			}
			else if(! category.getLibelle().equals(categoryOld.getLibelle()))
			{
				if(categoryRepository.existsByUserCategory(category.getLibelle(), category.getUserIdUtilisateur()))
				{
					h.put("message", "Une catégorie avec le même nom existe déjà pour cet utilisateur.");
					h.put("status", -1);
					return h;
				}
				
				if(categoryRepository.existsByInstitutionCategory(category.getLibelle(), category.getUserIdUtilisateur().getGroupeIdGroupe().getInstitution()))
				{
					h.put("message", "Une catégorie avec le même nom existe déjà pour l'institution de l'utilisateur.");
					h.put("status", -1);
					return h;
				}
			}
			if (category.getParent() == null) {
				//Update de categorie
				category.setRepertoire(categoryOld.getRepertoire());
			}
			else {
				
			//On  verifie le parent
				Category parent = categoryRepository.findOne(category.getParent().getIdCategory());
				if(parent == null)
				{
					h.put("message", "La catégorie parent n'existe pas.");
					h.put("status", -1);
					return h;
				}
				//On supprime physiquement la categorie
				//On recreee la sous categorie
				RemoveLocation(categoryOld.getRepertoire(), parent.getRepertoire()+"/"+ category.getLibelle());
				category.setRepertoire(parent.getRepertoire()+ "/"+ category.getLibelle());
				//Update des url_doc et des repertoire document
				List<Documents> listDocument = documentsRepository.findAllDocumentByCategory(categoryOld);
				if (listDocument.size() >0) {
					for(Documents doc : listDocument) {
						/*
						 *  document.setRepertoire(category.getRepertoire());
	            document.setUrlDocument(category.getRepertoire()+"/"+file.getOriginalFilename() );
	            
						 */
						String fileName = diff(doc.getUrlDocument(), doc.getRepertoire());
						String one =  doc.getRepertoire();
						String two = doc.getUrlDocument();
						//String fileName = String.Join(",",one.split('/').Except(two.split('/')).ToArray());
						System.out.println("Text retourne "+ fileName);
						doc.setRepertoire(category.getRepertoire());
						doc.setUrlDocument(category.getRepertoire()+fileName);
						documentsRepository.saveAndFlush(doc);
					}
				}
				
			}
			
			category.setIdCategory(idCategory);
			category = categoryRepository.saveAndFlush(category);
			journaliser(idUtilisateur, 7, category.getIdCategory());
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("category", category);
		h.put("message", "Mise a jour effectuée avec succès");
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value = "/delete/{idUtilisateur}/{idCategory}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> delete(@PathVariable Integer idUtilisateur,
									 @PathVariable Integer idCategory) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(idCategory == null || idUtilisateur == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		try
		{
			User userSession = userRepository.findOne(idUtilisateur);
			if(userSession == null)
			{
				h.put("message", "Cet utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			Category category = categoryRepository.findOne(idCategory);
			if(category == null)
			{
				h.put("message", "Cette catégorie n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			Integer idUserProp = category.getUserIdUtilisateur().getIdUtilisateur();
			User userProp = userRepository.findOne(idUserProp);
			if(userProp == null)
			{
				h.put("message", "L'utilisateur propriétaire n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			if(!categoryRepository.isProprietaire(idCategory, userSession))
			{
				if(! userSession.getGroupeIdGroupe().getInstitution().getIdInstitution().equals(category.getUserIdUtilisateur().getGroupeIdGroupe().getInstitution().getIdInstitution()))
				{
					h.put("message", "Cet utilisateur et le propriétaire de la catégorie ne sont pas de la même institution.");
					h.put("status", -1);
					return h;
				}
				else if(userSession.getProfilIdProfil().getType().equals("USER_HABILITY"))
				{
					h.put("message", "Cet utilisateur n'a pas les droits pour supprimer la catégorie.");
					h.put("status", -1);
					return h;
				}
			}
			categoryRepository.delete(category);
			deleteLocation(category.getRepertoire());
			if(categoryRepository.findAllByIdUtilisateur(userProp).size() <= 0)
			{
				String[] listDir = category.getRepertoire().split("/");
				if(listDir.length > 1)
				{
					deleteLocation(listDir[0]+ "/"+ listDir[1]);
				}
			}
			journaliser(idUtilisateur, 8, category.getIdCategory());
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "Catégorie supprimée avec succès");
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> list() {
		HashMap<String, Object> h = new HashMap<String, Object>();
		List<Category> listCategory;
		try
		{
			listCategory = categoryRepository.findAll();
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("status", 0);
		h.put("category_list", listCategory);
		h.put("message", "list OK");
		return h;
	}
	
	@RequestMapping(value = "/list_user/{idUtilisateur}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> listByUser(@PathVariable Integer idUtilisateur) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		List<Category> listCategory;
		if(idUtilisateur == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
			return h;
		}
		try
		{
			User user = userRepository.findOne(idUtilisateur);
			if(user == null)
			{
				h.put("message", "Cet utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			listCategory = categoryRepository.findAllByIdUtilisateur(user);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("status", 0);
		h.put("category_list", listCategory);
		h.put("message", "list OK");
		return h;
	}
	
	@RequestMapping(value = "/list_institution/{idInstitution}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> listByInstitution(@PathVariable Integer idInstitution) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		List<Category> listCategory;
		if(idInstitution == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
			return h;
		}
		try
		{
			Institution institution = institutionRepository.findOne(idInstitution);
			if(institution == null)
			{
				h.put("message", "Cette institution n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			listCategory = categoryRepository.findAllByInstitution(institution);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("status", 0);
		h.put("category_list", listCategory);
		h.put("message", "list OK");
		return h;
	}
	
	
	@RequestMapping(value = "/list_sous_category/{idCategory}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> listByParentCategory(@PathVariable Integer idCategory) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		List<Category> listCategory;
		if(idCategory == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
			return h;
		}
		try
		{
			Category parent = categoryRepository.findOne(idCategory);
			if(parent == null)
			{
				h.put("message", "Cette catégorie n'existe pas.");
				h.put("status", -1);
				return h;
			}
			listCategory = categoryRepository.findByParent(parent);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("status", 0);
		h.put("sous_category_list", listCategory);
		h.put("message", "list OK");
		return h;
	}
	
	public void deleteLocation(String rep) {
		FileSystemUtils.deleteRecursively(Paths.get(env.getProperty("root.location.store")+ "/"+ rep).toFile());
	}
	public void RemoveLocation(String repOrig, String repDes) {
		try {
			//FileSystemUtils.copyRecursively(src, dest);
			System.out.println("Rep origine "+ Paths.get(env.getProperty("root.location.store")+ "/"+ repOrig) );
			System.out.println("Rep origine "+ Paths.get(env.getProperty("root.location.store")+ "/"+ repDes) );
			FileUtils.moveDirectory(Paths.get(env.getProperty("root.location.store")+ "/"+ repOrig).toFile(),Paths.get(env.getProperty("root.location.store")+ "/"+ repDes).toFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createCategoryLocation(String rep) throws IOException {
		String[] listDir = rep.split("/");
		try {
			String location = env.getProperty("root.location.store");
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
	
	public final String getCategoryRootLocation(Category category) throws Exception {
        User user = category.getUserIdUtilisateur();
        String paysLocality = category.getUserIdUtilisateur().getGroupeIdGroupe().getInstitution().getLocalityIdLocalite().getPays();
        Institution institution = user.getGroupeIdGroupe().getInstitution();
		return paysLocality+ "/" +institution.getNom()+ "/"+ user.getPrenom()+ "-"+ user.getNom()+ "/"+ category.getLibelle();
	}
	
	public Date getDateString(String dateInString) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.parse(dateInString);
	}
	 public static String diff(String str1, String str2) {
		    int index = str1.lastIndexOf(str2);
		    if (index > -1) {
		      return str1.substring(str2.length());
		    }
		    return str1;
		  }
	
	public void journaliser(Integer idUser, Integer idAction, Integer idCategory) {
		Log log = new Log();
		log.setIdUser(idUser);
		log.setIdCategory(idCategory);
		log.setDate(new Date());
		log.setActionIdAction(actionRepository.findOne(idAction));
		logRepository.save(log);
	}
}
