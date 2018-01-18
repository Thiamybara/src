package com.qualshore.etreasury.rest;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.qualshore.etreasury.dao.ActionRepository;
import com.qualshore.etreasury.dao.BankRepository;
import com.qualshore.etreasury.dao.CategoryRepository;
import com.qualshore.etreasury.dao.DocumentsHasUserRepository;
import com.qualshore.etreasury.dao.DocumentsRepository;
import com.qualshore.etreasury.dao.InstitutionRepository;
import com.qualshore.etreasury.dao.LogRepository;
import com.qualshore.etreasury.dao.NotificationsUserRepository;
import com.qualshore.etreasury.dao.UserRepository;
import com.qualshore.etreasury.entity.Bank;
import com.qualshore.etreasury.entity.Category;
import com.qualshore.etreasury.entity.Documents;
import com.qualshore.etreasury.entity.DocumentsHasUser;
import com.qualshore.etreasury.entity.DocumentsHasUserPK;
import com.qualshore.etreasury.entity.Groupe;
import com.qualshore.etreasury.entity.Institution;
import com.qualshore.etreasury.entity.Log;
import com.qualshore.etreasury.entity.NotificationsUser;
import com.qualshore.etreasury.entity.User;
import com.qualshore.etreasury.mail.Value;
import com.qualshore.etreasury.model.DocumentListModel;
import com.qualshore.etreasury.model.InputPublishModel;
import com.qualshore.etreasury.model.InsUtilModel;
import com.qualshore.etreasury.model.PublishModel;
import com.qualshore.etreasury.model.SearchModel;
import com.qualshore.etreasury.service.FileStorageService;
import com.qualshore.etreasury.service.FileUploadService;
import com.qualshore.etreasury.service.UserLoginService;

@RequestMapping("etreasury_project/mon_espace/documents")

@RestController
public class DocumentsRestController {
	@Autowired
	DocumentsRepository documentsRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	UserRepository userRepository;
	
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
    DocumentsHasUserRepository dhuRepository;
	
	@Autowired
	InstitutionRepository institutionRepository;
	
	@Autowired
	BankRepository bRep;

	@Autowired
	NotificationsUserRepository notificationsUserRep;
	
	@Autowired
	UserLoginService userService;
	
	/*
	 * Store file
	 */
	/*@RequestMapping(value = "/add/{idUser}",method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> add(@PathVariable Integer idUser,
								  @RequestParam("nom") String nom,
								  @RequestParam("category") Integer idCategory,
							      @RequestParam("file") MultipartFile file) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		
			if(idUser == null || nom == null || nom.equals("") || idCategory == null || file == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		
		Documents document = new Documents();
		document.setNom(nom);
		document.setIsActive(true);
		//document.setMotsCles(motsCles);
		document.setIsRemove(false);
		document.setIsPrivate(false);
		document.setDateDocument(new Date());
		try
		{
			Category category = categoryRepository.findOne(idCategory);
			if(category == null)
			{
				h.put("message", "Cette catégorie n'existe pas.");
				h.put("status", -1);
				return h;
			}
			document.setCategory(category);
			
			User user = userRepository.findOne(idUser);
			if(!categoryRepository.isProprietaire(idCategory, user))
			{
				h.put("message", "Cet utilisateur n'est pas propriétaire de la catégorie.");
				h.put("status", -1);
				return h;
			}
			
			//Upload file
			if(file != null)
			{
				Pattern pExtension = Pattern.compile("([^\\s]+(\\.(?i)(pdf))$)");
	            Matcher mExtension = pExtension.matcher(file.getOriginalFilename());
	            if(! mExtension.matches())
	    		{
	            	h.put("message", "Le format du fichier est incorrect.");
	    			h.put("status", -1);
	    			return h;
	    		}
	            if(file.getSize() >= fileService.getMaxFileSize())
	    		{
	    			h.put("message", "Le fichier est trop volumineux.");
	    			h.put("status", -1);
	    			return h;
	    		}
	            document.setRepertoire(category.getRepertoire());
	            document.setUrlDocument(category.getRepertoire()+"/"+file.getOriginalFilename() );
	            String rootLocation = env.getProperty("root.location.store");
	            try {
					fileStorageService.store(file, document, rootLocation);
				} catch (Exception e) {
					h.put("message", "Ce fichier existe déjà.");
	    				h.put("status", -1);
	    				return h;
				}
			}
			
			document.setEtat("CREATED");
			document = documentsRepository.save(document);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("document", document);
		h.put("message", "Le document est ajouté avec succès.");
		h.put("status", 0);
		return h;
	}*/
	
	/*
    * Create document by user
    */
   @RequestMapping(value = "/add/{idUser}", method = RequestMethod.POST)
   @ResponseStatus(HttpStatus.OK)
   public Map<String,Object> add(@PathVariable Integer idUser,
                                 @RequestParam("nom") String nom,
                                 @RequestParam("mots_cles") String motsCles,
                                 @RequestParam("date_document") String dateDocument,
                                 @RequestParam("category") Integer idCategory,
                                 @RequestParam("file") MultipartFile file) {
       HashMap<String, Object> h = new HashMap<String, Object>();
       if(idUser == null || idCategory == null)
       {
           h.put("status", -1);
           h.put("message", "1 ou plusieurs paramètres manquants");
           return h;
       }
       try
       {
           Documents document = new Documents();
           document.setNom(nom);
           document.setIsActive(true);
           document.setMotsCles(motsCles);
           document.setIsRemove(false);
           document.setIsPrivate(false);
           
           SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
           document.setDateDocument(formatter.parse(dateDocument));
           document.setDateChargement(new Date());
       
           Category category = categoryRepository.findOne(idCategory);
           if(category == null)
           {
               h.put("message", "cette catégorie n'existe pas");
               h.put("status", -1);
               return h;
           }
           document.setCategory(category);
           
           User user = userRepository.findOne(idUser);
           if(!categoryRepository.isProprietaire(idCategory, user))
           {
               h.put("message", "cet utilisateur n'est pas propriétaire de la catégorie");
               h.put("status", -1);
               return h;
           }
           
           //Upload file
           if(file != null)
           {
               Pattern pExtension = Pattern.compile("([^\\s]+(\\.(?i)(pdf))$)");
               Matcher mExtension = pExtension.matcher(file.getOriginalFilename());
               if(! mExtension.matches())
               {
                   h.put("message", "Format de fichier incorrect");
                   h.put("status", -1);
                   return h;
               }
               if(file.getSize() >= fileService.getMaxFileSize())
               {
                   h.put("message", "fichier trop volumineux");
                   h.put("status", -1);
                   return h;
               }
               String [] originalName = file.getOriginalFilename().split("\\.pdf");
               String nomFic = originalName[0];
               SimpleDateFormat formater = new SimpleDateFormat("ddMMyyyy");
               String date1=formater.format(document.getDateDocument());
               String date2=formater.format(document.getDateChargement());

               document.setRepertoire(category.getRepertoire()+"/"+date1);
               document.setUrlDocument(document.getRepertoire()+"/"+nomFic+"_"+ date2+ ".pdf");
               String rootLocation = env.getProperty("root.location.store");
               fileStorageService.createLocation(document.getRepertoire(), env);
               fileStorageService.store(file, document, rootLocation);
               
               document = documentsRepository.save(document);
               journaliser(idUser, 6, document.getIdDocuments());
               h.put("document", document);
           }
           
       } catch (Exception e) {
           e.printStackTrace();
           h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
           h.put("status", -1);
           return h;
       }
       
       h.put("message", "document ajouté avec succès");
       h.put("status", 0);
       return h;
   }
	
	/*
	 * Load file
	 */
	@RequestMapping(value = "/load_file/{idUser}/{idDocument}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> loadFile(@PathVariable Integer idUser, @PathVariable Integer idDocument) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		try
		{
			Documents document = documentsRepository.findOne(idDocument);
			if(document == null)
			{
				h.put("message", "Ce document n'existe pas.");
				h.put("status", -1);
				return h;
			}
			User user = userRepository.findOne(idUser);
			if(!documentsRepository.isProprietaire(document.getIdDocuments(), user))
			{
				h.put("message", "Cet utilisateur n'est pas propriétaire du document.");
				h.put("status", -1);
				return h;
			}
			String rootLocation = env.getProperty("root.location.store");
			String r = fileStorageService.loadFile(document, rootLocation, document.getNom()+ ".pdf");
			if(r == null)
    		{
    			h.put("message", "Ce fichier n'existe pas.");
    			h.put("status", -1);
    			return h;
    		}
			h.put("url", env.getProperty("root.location.load")+ "/"+ r);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "Le document est chargé avec succès.");
		h.put("status", 0);
		return h;
	}
	
	public boolean hasSameInstitution(User user1, User user2) {
		return user1.getIdUtilisateur() != user2.getIdUtilisateur() && user1.getGroupeIdGroupe().getInstitution().getIdInstitution() == user2.getGroupeIdGroupe().getInstitution().getIdInstitution();
	}
	
	public boolean hasOpposedInstitution(User user1, User user2) {
		return !user1.getDiscriminatorValue().equals(user2.getDiscriminatorValue());
	}
	
	public boolean hasSameLocality(User user1, User user2) {
		return user1.getGroupeIdGroupe().getInstitution().getLocalityIdLocalite().getIdLocalite() == user2.getGroupeIdGroupe().getInstitution().getLocalityIdLocalite().getIdLocalite();
	}
	
	public boolean hasInInstitution(User user, Institution institution) {
		return user.getGroupeIdGroupe().getInstitution().getIdInstitution() == institution.getIdInstitution();
	}
	/*
	 * Publish document
	 */
	@RequestMapping(value = "/publier/{idUser}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Map<String,Object> publish(@PathVariable Integer idUser, @RequestBody InputPublishModel docModel) {
		HashMap<String, Object> h = new HashMap<String, Object>();
        ArrayList<DocumentsHasUser> result = new ArrayList<>();
        if(idUser == null || docModel == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
        try
        {      
            User userSession = userRepository.findOne(idUser);
			if(userSession == null)
			{
				h.put("message", "L'utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			Institution institution = userSession.getGroupeIdGroupe().getInstitution();
			if(institution == null)
			{
				h.put("message", "L'institution de l'utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			Documents document = documentsRepository.findOne(docModel.getDocument().getIdDocuments());
            if(document == null)
            {
                h.put("message", "Le document n'existe pas.");
                h.put("status", -1);
                return h;
            }
            
            ArrayList<User> utilsToPublish = new ArrayList<>();
            Integer [] tab1 = docModel.getTabUtils();
            Integer [] tab2 = docModel.getTabIns();
            InsUtilModel [] tab3 = docModel.getTabInsUtils();
            for(int i=0; i<tab1.length; i++)
            {
            	User user = userRepository.findOne(tab1[i]);
            	if(user != null && !utilsToPublish.contains(user) && hasSameInstitution(user, userSession))
        		{
        			utilsToPublish.add(user);
        		}
            }
            
            for(int i=0; i<tab2.length; i++)
            {
            	Institution inst = institutionRepository.findOne(tab2[i]);
            	List<User> listUser = userRepository.findUserByINstitution(inst);
            	for(User user : listUser)
            	{
            		if(user != null && !utilsToPublish.contains(user) && hasOpposedInstitution(user, userSession) && hasSameLocality(user, userSession))
            		{
            			utilsToPublish.add(user);
            		}
            	}
            }
            
            for(int i=0; i<tab3.length; i++)
            {
            	InsUtilModel mod = tab3[i];
            	Institution inst = institutionRepository.findOne(mod.getIns());
            	for(Integer id : mod.getUtils())
            	{
            		User user = userRepository.findOne(id);
            		if(user != null && !utilsToPublish.contains(user) && hasInInstitution(user, inst) && hasOpposedInstitution(user, userSession))
            		{
            			utilsToPublish.add(user);
            		}
            	}
            }
			result = publier(docModel.getDocument().getIdDocuments(), utilsToPublish, userSession);
        } catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
        h.put("document_publish_list", result);
        h.put("message", "Le document est publié avec succès.");
        h.put("status", 0);
        return h;
    }
	
	/*
	 * Publish list
	 */
	@RequestMapping(value = "/publish_list/{idUser}/{idDocument}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Map<String,Object> publishList(@PathVariable Integer idUser, @PathVariable Integer idDocument) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		InputPublishModel docModel = new InputPublishModel();
        if(idUser == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
        try
        {      
            User userSession = userRepository.findOne(idUser);
			if(userSession == null)
			{
				h.put("message", "L'utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			Documents document = documentsRepository.findOne(idDocument);
            if(document == null)
            {
                h.put("message", "Le document n'existe pas.");
                h.put("status", -1);
                return h;
            }
            
            ArrayList<Integer> list1 = new ArrayList<Integer>();
            ArrayList<Integer> list2 = new ArrayList<Integer>();
            ArrayList<Integer> list3 = new ArrayList<Integer>();
            
            List<DocumentsHasUser> dhuList = dhuRepository.findByDocuments(document);
            for(DocumentsHasUser dhu : dhuList)
            {
            	User user = userRepository.findOne(dhu.getUser().getIdUtilisateur());
            	if(user != null)
            	{
	            	if(hasSameInstitution(user, userSession))
	            	{
	            		list1.add(user.getIdUtilisateur());
	            	}
	            	else
	            	{
	            		Institution institution = user.getGroupeIdGroupe().getInstitution();
	            		if(institution == null)
	                    {
	                        h.put("message", "L'institution d'un utilisateur n'existe pas.");
	                        h.put("status", -1);
	                        return h;
	                    }
	            		else if(isPublishAll(document, institution) && !list2.contains(institution.getIdInstitution()))
		            	{
		            		list2.add(institution.getIdInstitution());
		            	}
	            		else if(!isPublishAll(document, institution) && !list3.contains(user.getIdUtilisateur()))
	            		{
	            			list3.add(user.getIdUtilisateur());
	            		}
	            	}
            	}
            }
            
            Integer [] tab1 = new Integer[list1.size()];
            for(int i=0; i<list1.size(); i++)
            {
            	tab1[i] = list1.get(i);
            }
            
            Integer [] tab2 = new Integer[list2.size()];
            for(int i=0; i<list2.size(); i++)
            {
            	tab2[i] = list2.get(i);
            }
            
            Integer [] tab3 = new Integer[list3.size()];
            for(int i=0; i<list3.size(); i++)
            {
            	tab3[i] = list3.get(i);
            }
            docModel.setDocument(document);
            docModel.setTabUtils(tab1);
            docModel.setTabIns(tab2);
            docModel.setTabInsUtils(createInsList(document, tab3));
            
        } catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
        h.put("document_publish_list", docModel);
        h.put("message", "Liste des documents publiés.");
        h.put("status", 0);
        return h;
    }
	
	public ArrayList<Integer> getDistinctInstitution(Integer [] tab) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(Integer id : tab)
		{
			User user = userRepository.findOne(id);
			if(user != null) 
			{
				Institution institution = user.getGroupeIdGroupe().getInstitution();
				if(!result.contains(institution.getIdInstitution()))
				{
					result.add(institution.getIdInstitution());
				}
			}
		}
		return result;
	}
	
	public Integer [] convertToTab(List<User> list) {
		Integer [] tab = new Integer [list.size()];
		int i = 0;
		for(User user : list)
		{
			tab[i++] = user.getIdUtilisateur();
		}
		return tab;
	}
	
	public InsUtilModel [] createInsList(Documents document, Integer [] tab) {
		ArrayList<Integer> listIns = getDistinctInstitution(tab);
		InsUtilModel [] result = new InsUtilModel [listIns.size()];
		int cmp = 0;
		for(Integer idIns : listIns)
		{
			Institution institution = institutionRepository.findOne(idIns);
			if(institution != null)
			{
				InsUtilModel insUtil = new InsUtilModel();
				insUtil.setIns(idIns);
				insUtil.setUtils(convertToTab(dhuRepository.findUserByDocumentAndInstitution(document, institution)));
				result[cmp++] = insUtil;
			}
		}
		return result;
	}
	
	public boolean isPublishAll(Documents document, Institution institution) {
		List<User> listUser = userRepository.findUserByINstitution(institution);
		List<DocumentsHasUser> dhuList = dhuRepository.findByDocumentAndInstitution(document, institution);
		return listUser.size() == dhuList.size();
	}
	
	public ArrayList<DocumentsHasUser> publier(Integer idDocument, ArrayList<User> listUser, User userSession) {
		ArrayList<DocumentsHasUser> result = new ArrayList<>();
		Documents document = documentsRepository.findOne(idDocument);
		if(document != null)
		{
			dhuRepository.deleteByDocuments(document);
			for(User user : listUser)
			{
				DocumentsHasUser dhu = new DocumentsHasUser(idDocument, user.getIdUtilisateur());
				dhu.setDatePublication(new Date());
		        dhuRepository.save(dhu);
		        result.add(dhu);
		        
		    	//Notifier l'utilisateur 
				createNotificationsUser(null, null, 19, userSession.getGroupeIdGroupe().getInstitution().getNom(), user, idDocument);
				
				String emailFrom = env.getProperty("spring.mail.username");
				
				//sendMail(emailFrom, user.getEmail(), emailSubject, "Bonjour, la banque "+bank.getNom()+ " "+emailBody );
				List<Value> values= new ArrayList<Value>();
				values.add(new Value("",document.getNom()+".pdf"));
				userService.sendMailNew(emailFrom,  user.getEmail(), "eTreasury  | Document reçu", userSession.getPrenom() + " "+userSession.getNom()+" de "+user.getGroupeIdGroupe().getInstitution().getNom()+" a partagé avec vous le document: ", user.getPrenom(), values, "Merci de consulter via Etreasury.");
			}
		}
		return result;
	}
	
	/*
	 * Publish document to institution
	 */
	@RequestMapping(value = "/publish_institution_default/{idUser}/{idDocument}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Map<String,Object> publishOwnInstitutionDefault(@PathVariable Integer idUser, @PathVariable Integer idDocument) {
        HashMap<String, Object> h = new HashMap<String, Object>();
        if(idUser == null || idDocument == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
        try
        {
            Documents document = documentsRepository.findOne(idDocument);
            if(document == null)
            {
                h.put("message", "Ce document n'existe pas.");
                h.put("status", -1);
                return h;
            }
            
            User user = userRepository.findOne(idUser);
			if(user == null)
			{
				h.put("message", "L'utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			Institution institution = user.getGroupeIdGroupe().getInstitution();
			if(institution == null)
			{
				h.put("message", "L'institution de l'utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
            List<User> listUser = userRepository.findUserByINstitution(institution);
			for(User u : listUser)
			{
                DocumentsHasUser dhu = new DocumentsHasUser(idDocument, u.getIdUtilisateur());
                dhu.setDatePublication(new Date());
                dhuRepository.save(dhu);
                
              	//Notifier l'utilisateur 
    			createNotificationsUser(null, null, 14, institution.getNom(), u,document.getIdDocuments());
    			
    			String emailFrom = env.getProperty("spring.mail.username");
    			
    			//sendMail(emailFrom, user.getEmail(), emailSubject, "Bonjour, la banque "+bank.getNom()+ " "+emailBody );
    			List<Value> values= new ArrayList<Value>();
            values.add(new Value("",document.getNom()+".pdf"));
            userService.sendMailNew(emailFrom,  u.getEmail(), "eTreasury  | Document reçu", user.getPrenom() + " "+user.getNom()+" de "+institution.getNom()+" a partagé avec vous le document: ", u.getPrenom(), values, "Merci de consulter via Etreasury.");

			}
        } catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
        h.put("message", "Le document est publié avec succès.");
        h.put("status", 0);
        return h;
    }
	
	/*
	 * Update document
	 */
	@RequestMapping(value = "/ranger/{idUser}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> update(@RequestBody Documents document, @PathVariable Integer idUser) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(document == null || document.getIdDocuments() == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
			return h;
		}
		try
		{	
			User user = userRepository.findOne(idUser);
			if(user == null)
			{
				h.put("message", "L'utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			Documents documentOld = documentsRepository.findOne(document.getIdDocuments());
			if(documentOld == null)
			{
				h.put("message", "Le document n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			if(document.getCategory() == null)
			{
				document.setCategory(documentOld.getCategory());
				document.setRepertoire(documentOld.getRepertoire());
				document.setUrlDocument(documentOld.getUrlDocument());
			}
			else
			{
				Integer idCategory = document.getCategory().getIdCategory();
				Category category = categoryRepository.findOne(idCategory);
				if(category == null)
				{
					h.put("message", "La categorie n'existe pas.");
					h.put("status", -1);
					return h;
				}
				
				if(category.getIdCategory() != documentOld.getCategory().getIdCategory()) {
					//move document
							String fileName = diff(documentOld.getUrlDocument(), documentOld.getRepertoire());
							//String fileName = String.Join(",",one.split('/').Except(two.split('/')).ToArray());
							RemoveLocationDocument(documentOld.getUrlDocument(), category.getRepertoire()+fileName);
							
							System.out.println("Text retourne "+ fileName);
							document.setRepertoire(category.getRepertoire());
							document.setUrlDocument(category.getRepertoire()+fileName);
					
					//update de urldoc
				}
				else {
				document.setCategory(category);
				document.setRepertoire(documentOld.getRepertoire());
				document.setUrlDocument(documentOld.getUrlDocument());
			
				}
			}
			
			if(document.getIsActive() == null)
			{
				document.setIsActive(documentOld.getIsActive());
			}
			if(document.getIsPrivate() == null)
			{
				document.setIsPrivate(documentOld.getIsPrivate());
			}
			if(document.getIsRemove() == null)
			{
				document.setIsRemove(documentOld.getIsRemove());
			}
			if(document.getDateChargement() == null)
			{
				document.setDateChargement(documentOld.getDateChargement());
			}
			if(document.getDateDocument() == null)
			{
				document.setDateDocument(documentOld.getDateDocument());
			}
			
			if(document.getEtat() == null)
			{
				document.setEtat(documentOld.getEtat());
			}
			else if(! document.getEtat().equals("CREATED") || ! document.getEtat().equals("RECEIVED"))
			{
				h.put("message", "L'attribut état doit être égal à CREATED | RECEIVED.");
				h.put("status", -1);
				return h;
			}
			
			if(document.getMotsCles() == null)
			{
				document.setMotsCles(documentOld.getMotsCles());
			}
			if(document.getNom() == null)
			{
				document.setNom(documentOld.getNom());
			}
			/*
			if(document.getRepertoire() == null)
			{
				document.setRepertoire(documentOld.getRepertoire());
			}
			*/
			if(document.getStatusIdStatus() == null)
			{
				document.setStatusIdStatus(documentOld.getStatusIdStatus());
			}
			
			document = documentsRepository.saveAndFlush(document);
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("document", document);
		h.put("message", "La mise à jour s'est effectué avec succès.");
		h.put("status", 0);
		return h;
	}
	
	/*
	 * List per category and user
	 */
	@RequestMapping(value = "/list/{idUser}/{idCategory}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> listByCategory(@PathVariable Integer idUser, @PathVariable Integer idCategory) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(idUser == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
		}
		try
		{
			User user = userRepository.findOne(idUser);
			if(user == null)
			{
				h.put("message", "L'utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			Category category = categoryRepository.findOne(idCategory);
			if(category == null)
			{
				h.put("message", "La catégorie n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			List<Documents> listDocument = documentsRepository.findAllDocumentByCategory(category);
			h.put("status", 0);
			h.put("documents_list", listDocument);
			h.put("message", "list OK");
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		return h;
	}
	
	/*
	 * List per user
	 */
	@RequestMapping(value = "/list_user/{idUser}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> listAllCategory(@PathVariable Integer idUser) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		List<Documents> listDocuments;
		if(idUser == null)
		{
			h.put("message", "1 ou plusieurs paramètres manquants");
			h.put("status", -1);
		}
		try
		{
			User user = userRepository.findOne(idUser);
			if(user == null)
			{
				h.put("message", "Cet utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			
			listDocuments = documentsRepository.findAllDocumentByUser(user);
			
		} catch (Exception e) {
			e.printStackTrace();
			h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
			h.put("status", -1);
			return h;
		}
		h.put("status", 0);
		h.put("document_list", listDocuments);
		h.put("message", "list OK");
		return h;
	}

	/*
	 * Delete document
	 */
	@RequestMapping(value = "/delete_file/{idUser}/{idDocument}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public Map<String,Object> deleteFile(@PathVariable Integer idDocument, @PathVariable Integer idUser) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		if(idDocument == null || idUser == null)
		{
			h.put("status", -1);
			h.put("message", "1 ou plusieurs paramètres manquants");
			return h;
		}
		try
		{
			Documents document = documentsRepository.findOne(idDocument);
			if(document == null)
			{
				h.put("message", "Le document n'existe pas.");
				h.put("status", -1);
				return h;
			}
			User user = userRepository.findOne(idUser);
			if(user == null)
			{
				h.put("message", "L'utilisateur n'existe pas.");
				h.put("status", -1);
				return h;
			}
			//String rootLocation = env.getProperty("root.location.load");
			//fileStorageService.deleteFile2(document);
			//Supprimer document en local
			deleteLocationDocument(document.getUrlDocument());
			//Supprimer doc en bdd
			documentsRepository.delete(document);
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
	


	
	
	// manage document has user
	
	// publsh document to banks
	/*@RequestMapping(value = "/publier/bank", method = RequestMethod.POST)
	public Map<String,Object> publishFileToBank1(@RequestBody PublishModel publish){

		Map<String, Object> h = new HashMap<String, Object>(); 
		Documents doc = documentsRepository.findOne(publish.getIdDoc());
		if (doc == null) {
			h.put("message", "Le document n'existe pas");
			h.put("status", 0);
			return h;
		}
		List<Bank> banks = new ArrayList<Bank>();
		for (Integer idBank : publish.getIdsBank()) {
			Bank b = bRep.findOne(idBank);
			if ( b != null) {
				banks.add(b);
			}
		}
		List<DocumentsHasUser> dHUsers = new ArrayList<DocumentsHasUser>();
		try {
			banks.forEach(bank -> {
				List<Groupe> groupes = bank.getGroupeList();
				groupes.forEach(groupe->{
					List<User> users = groupe.getUserList();
					if (users.size() != 0) {
						//pulishToUsers(doc, users);
						users.forEach(user -> {
							DocumentsHasUserPK dHUPK = new DocumentsHasUserPK(doc.getIdDocuments(),
									user.getIdUtilisateur());
							DocumentsHasUser dhu = new DocumentsHasUser(dHUPK, doc, user);
							dhu = dhuRepository.save(dhu);
							dHUsers.add(dhu);
						});
					}
				});
			});
		} catch (Exception e) {
			h.put("message", "Le document n'existe pas");
			h.put("status", -1);
			return h;
		}
		banks.forEach(bank -> {
				List<Groupe> groupes = bank.getGroupeList();
				groupes.forEach(groupe->{
					List<User> users = groupe.getUserList();
					if (users.size() != 0) {
						pulishToUsers(doc, users);
					}
				});
			});
		h.put("message", "Le document a bien été partagé");
		h.put("publish_model", dHUsers);
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping("/shared/{idUser}")
	public HashMap<String, Object> getDocumentShared1(@PathVariable Integer idUser){
		
		HashMap<String, Object> h= new HashMap<String, Object>();
		User user = userRepository.findOne(idUser);
		if (user == null) {
			h.put("message", "l'utilisateur n'existe pas");
			h.put("status", -1);
			return h;
		}
		
		List<DocumentsHasUser> documentsHasUsers = dhuRepository.findByUser(user);
		h.put("message", "liste des documents reçu");
		h.put("liste_documents", documentsHasUsers);
		h.put("status", 0);
		return h;
	}
	
	@RequestMapping("/delete/document_has_user/{idUser}/{idDoc}")
	public HashMap<String, Object> deleteDocumentHasUser1(@PathVariable("idUser") Integer idUser,
			@PathVariable("idDoc") Integer idDoc){
		
		HashMap<String, Object> h= new HashMap<String, Object>();
		User user = userRepository.findOne(idUser);
		if (user == null) {
			h.put("message", "l'utilisateur n'existe pas");
			h.put("status", -1);
			return h;
		}
		Documents doc = documentsRepository.findOne(idDoc);
		if (doc == null) {
			h.put("message", "le document n'existe pas");
			h.put("status", -1);
			return h;
		}
		
		List<DocumentsHasUser> dHasUsers = dhuRepository.findByUser(user);
		try {
			dHasUsers.forEach(dHasUser->{
				if (dHasUser.getDocuments().getIdDocuments() == doc.getIdDocuments()) {
					dhuRepository.delete(dHasUser);
				}
			});
		} catch (Exception e) {
			h.put("message", "la suppression a échoué");
			h.put("status", -1);
			return h;
		}
		h.put("message", "success");
		h.put("status", 0);
		return h;
	}
*/	
	public void journaliser(Integer idUser, Integer idAction, Integer idDocument) {
		Log log = new Log();
		log.setIdUser(idUser);
		log.setIdDocument(idDocument);
		log.setDate(new Date());
		log.setActionIdAction(actionRepository.findOne(idAction));
		logRepository.save(log);
	}

	public HashMap<String, Object> pulishToUsers(Documents doc, List<User> users) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		List<DocumentsHasUser> dHUsers = new ArrayList<DocumentsHasUser>(); 
		try {
			users.forEach(user -> {
				DocumentsHasUserPK dHUPK = new DocumentsHasUserPK(doc.getIdDocuments(),
						user.getIdUtilisateur());
				DocumentsHasUser dhu = new DocumentsHasUser(dHUPK, doc, user);
				dhu.setDatePublication(new Date());
				dhu = dhuRepository.save(dhu);
				dHUsers.add(dhu);
			});
		} catch (Exception e) {
			h.put("message", "Le document n'existe pas.");
			h.put("status", -1);
			return h;
		}

		h.put("message", "Le document a ien été partagé.");
		h.put("publish_model", dHUsers);
		h.put("status", 0);
		return h;
	}

	// manage document has user
	
	// publsh document to banks
    @RequestMapping(value = "/publier/institution/{idUser}", method = RequestMethod.POST)
    public Map<String,Object> publishFileToInstitution(@RequestBody PublishModel publish,@PathVariable Integer idUser){

        Map<String, Object> h = new HashMap<String, Object>();
        Documents doc = documentsRepository.findOne(publish.getIdDoc());
        if (doc == null) {
            h.put("message", "Le document n'existe pas.");
            h.put("status", 0);
            return h;
        }
        if (idUser == null) {
            h.put("message", "L'utilisateur n'est pas renseigné.");
            h.put("status", 0);
            return h;
        }
        User userSession = userRepository.findOne(idUser);
		if(userSession == null)
		{
			h.put("message", "Cet utilisateur n'existe pas.");
			h.put("status", -1);
			return h;
		}
		if (userSession.getIdUtilisateur() != doc.getCategory().getUserIdUtilisateur().getIdUtilisateur()) {
			h.put("message", "Vous n'êtes pas propriétaire du document.");
			h.put("status", -1);
			return h;
		}
        List<Institution> institutions = new ArrayList<Institution>();
        for (Integer idInstitution : publish.getIdsInstitution()) {
            Institution i = institutionRepository.findOne(idInstitution);
            if (i != null) {
                institutions.add(i);
            }
        }
        List<DocumentsHasUser> dHUsers = new ArrayList<DocumentsHasUser>();
        try {
            institutions.forEach(institution -> {
                List<Groupe> groupes = institution.getGroupeList();
                groupes.forEach(groupe->{
                    List<User> users = groupe.getUserList();
                    if (users.size() != 0) {
                        //pulishToUsers(doc, users);
                        users.forEach(user -> {
                            System.out.println("user " + user);
                            DocumentsHasUserPK dHUPK = new DocumentsHasUserPK(doc.getIdDocuments(),
                                    user.getIdUtilisateur());
                            DocumentsHasUser dhu = new DocumentsHasUser(dHUPK, doc, user);
                            dhu.setDatePublication(new Date());
                            dhu = dhuRepository.save(dhu);
                            System.out.println("dhu  " + dhu);
                            dHUsers.add(dhu);
                            
                          	//Notifier l'utilisateur 
                			createNotificationsUser(null, null, 14, institution.getNom(), user,doc.getIdDocuments());
                			
                			String emailFrom = env.getProperty("spring.mail.username");
                			
                			//sendMail(emailFrom, user.getEmail(), emailSubject, "Bonjour, la banque "+bank.getNom()+ " "+emailBody );
                			List<Value> values= new ArrayList<Value>();
                        values.add(new Value("",doc.getNom()+".pdf"));
                        userService.sendMailNew(emailFrom,  user.getEmail(), "eTreasury  | Document reçu", userSession.getPrenom() + " "+userSession.getNom()+" de "+institution.getNom()+" a partagé avec vous le document: ", user.getPrenom(), values, "Merci de consulter via Etreasury.");

                            
                        });
                    }
                });
            });
        } catch (Exception e) {
            h.put("message", "Le document n'existe pas");
            h.put("status", -1);
            return h;
        }
        
        h.put("message", "Le document a bien été partagé");
        h.put("publish_model", dHUsers);
        h.put("status", 0);
        return h;
    }
	
	// publsh document to banks
	@RequestMapping(value = "/publier/bank", method = RequestMethod.POST)
	public Map<String,Object> publishFileToBank(@RequestBody PublishModel publish){

		Map<String, Object> h = new HashMap<String, Object>(); 
		Documents doc = documentsRepository.findOne(publish.getIdDoc());
		if (doc == null) {
			h.put("message", "Le document n'existe pas.");
			h.put("status", 0);
			return h;
		}
		List<Bank> banks = new ArrayList<Bank>();
		for (Integer idBank : publish.getIdsInstitution()) {
			Bank b = bRep.findOne(idBank);
			if ( b != null) {
				banks.add(b);
			}
		}
		List<DocumentsHasUser> dHUsers = new ArrayList<DocumentsHasUser>();
		try {
			banks.forEach(bank -> {
				List<Groupe> groupes = bank.getGroupeList();
				groupes.forEach(groupe->{
					List<User> users = groupe.getUserList();
					if (users.size() != 0) {
						//pulishToUsers(doc, users);
						users.forEach(user -> {
							DocumentsHasUserPK dHUPK = new DocumentsHasUserPK(doc.getIdDocuments(),
									user.getIdUtilisateur());
							DocumentsHasUser dhu = new DocumentsHasUser(dHUPK, doc, user);
							dhu.setDatePublication(new Date());
							dhu = dhuRepository.save(dhu);
							dHUsers.add(dhu);
						});
					}
				});
			});
		} catch (Exception e) {
			h.put("message", "Le document n'existe pas.");
			h.put("status", -1);
			return h;
		}
		banks.forEach(bank -> {
				List<Groupe> groupes = bank.getGroupeList();
				groupes.forEach(groupe->{
					List<User> users = groupe.getUserList();
					if (users.size() != 0) {
						pulishToUsers(doc, users);
					}
				});
			});
		h.put("message", "Le document a bien été partagé.");
		h.put("publish_model", dHUsers);
		h.put("status", 0);
		return h;
	}
	
	//Boite de reception
	@RequestMapping("/shares/{idUser}")
	public HashMap<String, Object> getDocumentShared(@PathVariable Integer idUser){
		
		HashMap<String, Object> h= new HashMap<String, Object>();
		User user = userRepository.findOne(idUser);
		if (user == null) {
			h.put("message", "L'utilisateur n'existe pas.");
			h.put("status", -1);
			return h;
		}
		
		List<DocumentsHasUser> documentsHasUsers = dhuRepository.findByUser(user);
		h.put("message", "liste des documents reçu");
		h.put("liste_documents", documentsHasUsers);
		h.put("status", 0);
		return h;
	}
	
	
	//Supprimer document partage
	@RequestMapping("/delete/document_has_user/{idUser}/{idDoc}")
	public HashMap<String, Object> deleteDocumentHasUser(@PathVariable("idUser") Integer idUser,
			@PathVariable("idDoc") Integer idDoc){
		
		HashMap<String, Object> h= new HashMap<String, Object>();
		User user = userRepository.findOne(idUser);
		if (user == null) {
			h.put("message", "l'utilisateur n'existe pas");
			h.put("status", -1);
			return h;
		}
		Documents doc = documentsRepository.findOne(idDoc);
		if (doc == null) {
			h.put("message", "le document n'existe pas");
			h.put("status", -1);
			return h;
		}
		
		List<DocumentsHasUser> dHasUsers = dhuRepository.findByUser(user);
		try {
			dHasUsers.forEach(dHasUser->{
				if (dHasUser.getDocuments().getIdDocuments() == doc.getIdDocuments()) {
					dhuRepository.delete(dHasUser);
				}
			});
		} catch (Exception e) {
			h.put("message", "La suppression a échoué.");
			h.put("status", -1);
			return h;
		}
		h.put("message", "success");
		h.put("status", 0);
		return h;
	}
	
	/*
    * Documents created by user
   */
  @RequestMapping(value = "/list_created/{idUser}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  public Map<String,Object> listCreated(@PathVariable Integer idUser) {
      HashMap<String, Object> h = new HashMap<String, Object>();
      ArrayList<DocumentListModel> result = new ArrayList<>();
      if(idUser == null)
      {
          h.put("message", "1 ou plusieurs paramètres manquants");
          h.put("status", -1);
          return h;
      }
      try
      {
          User user = userRepository.findOne(idUser);
          if(user == null)
          {
              h.put("message", "Cet utilisateur n'existe pas.");
              h.put("status", -1);
              return h;
          }
         
          List<Documents> listDocument = documentsRepository.findByUser(user);
          for(Documents doc : listDocument)
          {
              DocumentListModel docModel = new DocumentListModel(doc.getIdDocuments(), doc.getNom(), doc.getDateDocument(), doc.getDateChargement(), null, doc.getRepertoire(), doc.getIsActive(), doc.getMotsCles(), doc.getEtat(), doc.getIsRemove(), doc.getIsPrivate(), doc.getStatusIdStatus(), doc.getUrlDocument(), doc.getCategory());
              String chaine = "";
              chaine += doc.getNom().equals("")? "" : doc.getNom()+ " ";
              chaine += doc.getMotsCles().equals("")? "" : doc.getMotsCles()+ " ";
              chaine += doc.getCategory().getLibelle().equals("")? "" : doc.getCategory().getLibelle();
              docModel.setChaine(chaine);
              result.add(docModel);
          }
          h.put("status", 0);
          h.put("created_documents_list", result);
          h.put("message", "liste des documents créés");
      } catch (Exception e) {
          e.printStackTrace();
          h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
          h.put("status", -1);
          return h;
      }
      return h;
  }
   
   /*
    * Documents received by user
    */
   @RequestMapping(value = "/list_received/{idUser}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public Map<String,Object> listReceived(@PathVariable Integer idUser) {
       HashMap<String, Object> h = new HashMap<String, Object>();
       List<DocumentsHasUser> dhuList ;
       if(idUser == null)
       {
           h.put("message", "1 ou plusieurs paramètres manquants");
           h.put("status", -1);
           return h;
       }
       try
       {
           User user = userRepository.findOne(idUser);
           if(user == null)
           {
               h.put("message", "Cet utilisateur n'existe pas.");
               h.put("status", -1);
               return h;
           }
            dhuList = dhuRepository.findByUser(user);
       } catch (Exception e) {
           e.printStackTrace();
           h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
           h.put("status", -1);
           return h;
       }
       h.put("status", 0);
       h.put("received_documents_list", dhuList);
       h.put("message", "liste des documents reçus");
       return h;
   }
   
   /*
   * Documents received by user
   */
  @RequestMapping(value = "/list_received_search/{idUser}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  public Map<String,Object> listReceivedSearch(@PathVariable Integer idUser) {
      HashMap<String, Object> h = new HashMap<String, Object>();
      ArrayList<DocumentListModel> result = new ArrayList<>();
      if(idUser == null)
      {
          h.put("message", "1 ou plusieurs paramètres manquants");
          h.put("status", -1);
          return h;
      }
      try
      {
          User user = userRepository.findOne(idUser);
          if(user == null)
          {
              h.put("message", "Cet utilisateur n'existe pas.");
              h.put("status", -1);
              return h;
          }
          List<DocumentsHasUser> dhuList = dhuRepository.findByUser(user);
          for(DocumentsHasUser dhu : dhuList)
          {
              Documents doc = dhu.getDocuments();
              if(doc != null)
              {
                    DocumentListModel docModel = new DocumentListModel(doc.getIdDocuments(), doc.getNom(), doc.getDateDocument(), doc.getDateChargement(), dhu.getDatePublication(), doc.getRepertoire(), doc.getIsActive(), doc.getMotsCles(), doc.getEtat(), doc.getIsRemove(), doc.getIsPrivate(), doc.getStatusIdStatus(), doc.getUrlDocument(), doc.getCategory());
                    String chaine = "";
                    String nomInstittution = doc.getCategory().getUserIdUtilisateur().getGroupeIdGroupe().getInstitution().getNom();
                    String prenomUser = doc.getCategory().getUserIdUtilisateur().getPrenom();
                    String nomUser = doc.getCategory().getUserIdUtilisateur().getNom();
                    chaine += doc.getNom().equals("")? "" : doc.getNom()+ " ";
                    chaine += nomInstittution.equals("")? "" : nomInstittution+ " ";
                    chaine += prenomUser.equals("")? "" : prenomUser+ " ";
                    chaine += nomUser.equals("")? "" : nomUser;
                    docModel.setChaine(chaine);
                    result.add(docModel);
              }
          }
          h.put("status", 0);
          h.put("received_documents_list", result);
          h.put("message", "liste des documents reçus");
      } catch (Exception e) {
          e.printStackTrace();
          h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
          h.put("status", -1);
          return h;
      }
      return h;
  }
   
   /*
    * List institution shared publish document
    */
   @RequestMapping(value = "/list_institution_received/{idUser}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public Map<String,Object> listInstitutionReceived(@PathVariable Integer idUser) {
       HashMap<String, Object> h = new HashMap<String, Object>();
       List<DocumentsHasUser> dhuList ;
       ArrayList<Institution> institutionList = new ArrayList<>();
       if(idUser == null)
       {
           h.put("message", "1 ou plusieurs paramètres manquants");
           h.put("status", -1);
           return h;
       }
       try
       {
           User user = userRepository.findOne(idUser);
           if(user == null)
           {
               h.put("message", "Cet utilisateur n'existe pas.");
               h.put("status", -1);
               return h;
           }
           dhuList = dhuRepository.findByUser(user);
           for(DocumentsHasUser dhu : dhuList)
           {
        	   Institution inst = dhu.getDocuments().getCategory().getUserIdUtilisateur().getGroupeIdGroupe().getInstitution();
        	   if(!institutionList.contains(inst))
        	   {
        		   institutionList.add(inst);
        	   }
           }
       } catch (Exception e) {
           e.printStackTrace();
           h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
           h.put("status", -1);
           return h;
       }
       h.put("status", 0);
       h.put("received_documents_institution_list", institutionList);
       h.put("message", "liste des institutions émettrices");
       return h;
   }
   
   	/*
    * Search created document with full key word
   	*/
   	@RequestMapping(value = "/search_created_doc/{idUser}/{motsCles}", method = RequestMethod.POST)
 	@ResponseStatus(HttpStatus.OK)
 	public Map<String,Object> searchCreatedFull(@PathVariable Integer idUser, @PathVariable String motsCles) {
         HashMap<String, Object> h = new HashMap<String, Object>();
         List<Documents> listDocument;
         if(idUser == null)
         {
             h.put("message", "1 ou plusieurs paramètres manquants");
             h.put("status", -1);
             return h;
         }
         try
         {
             User user = userRepository.findOne(idUser);
             if(user == null)
             {
                 h.put("message", "Cet utilisateur n'existe pas.");
                 h.put("status", -1);
                 return h;
             } 
             Institution institution = user.getGroupeIdGroupe().getInstitution();
             listDocument = fullSearchCreated(motsCles, institution);
         } catch (Exception e) {
             e.printStackTrace();
             h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
             h.put("status", -1);
             return h;
         }
         h.put("status", 0);
         h.put("search_documents_list", listDocument);
         h.put("message", "liste des documents créés");
         return h;
     }
   	
   	/*
     * Search received document with full key word
    */
    @RequestMapping(value = "/search_received_doc/{idUser}/{motsCles}", method = RequestMethod.POST)
  	@ResponseStatus(HttpStatus.OK)
  	public Map<String,Object> searchReceiveFull(@PathVariable Integer idUser, @PathVariable String motsCles) {
          HashMap<String, Object> h = new HashMap<String, Object>();
          List<Documents> listDocument;
          if(idUser == null)
          {
              h.put("message", "1 ou plusieurs paramètres manquants");
              h.put("status", -1);
              return h;
          }
          try
          {
              User user = userRepository.findOne(idUser);
              if(user == null)
              {
                  h.put("message", "Cet utilisateur n'existe pas.");
                  h.put("status", -1);
                  return h;
              } 
              Institution institution = user.getGroupeIdGroupe().getInstitution();
              listDocument = fullSearchReceived(motsCles, institution);
          } catch (Exception e) {
              e.printStackTrace();
              h.put("message", "Oups, une erreur a été détectée \""+e.getMessage()+"\" ... veuillez réessayer ou contacter le support.");
              h.put("status", -1);
              return h;
          }
          h.put("status", 0);
          h.put("search_documents_list", listDocument);
          h.put("message", "liste des documents créés");
          return h;
      }
   
   	public ArrayList<Documents> fullSearchCreated(String motsCles, Institution institution) {
   		ArrayList<Documents> listDocument = new ArrayList<>();
        listDocument.addAll(documentsRepository.findByName(motsCles, institution));
        if(listDocument.isEmpty())
        {
        	List<Category> categoryList = categoryRepository.findByLibelleAndInstitution(motsCles, institution);
	        for(Category category:categoryList)
	        {
	        	listDocument.addAll(documentsRepository.findByCategory(category));
	        }
	        if(listDocument.isEmpty())
	        {
	        	listDocument.addAll(documentsRepository.findByMotsCles(motsCles, institution));
	        	/*if(listDocument.isEmpty())
               	{
                   	listDocument = documentsRepository.findByDateChargement(dateChargement);
               	}*/
	        }
        }
       return listDocument;
    }
   	
   	public ArrayList<Documents> fullSearchReceived(String motsCles, Institution institution) {
   		ArrayList<Documents> listDocument = new ArrayList<>();
        listDocument.addAll(documentsRepository.findByName(motsCles, institution));
        if(listDocument.isEmpty())
        {
        	List<Category> categoryList = categoryRepository.findByLibelleAndInstitution(motsCles, institution);
	        for(Category category:categoryList)
	        {
	        	listDocument.addAll(documentsRepository.findByCategory(category));
	        }
	        if(listDocument.isEmpty())
	        {
	        	listDocument.addAll(documentsRepository.findByMotsCles(motsCles, institution));
	        	/*if(listDocument.isEmpty())
               	{
                   	listDocument = documentsRepository.findByDateChargement(dateChargement);
               	}*/
	        }
        }
       return listDocument;
    }
   	
   public void deleteLocationDocument(String rep) {
	   try {
		FileUtils.forceDelete(Paths.get(env.getProperty("root.location.store")+ "/"+ rep).toFile());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
   
	public void RemoveLocationDocument(String repOrig, String repDes) {
		try {
			//FileSystemUtils.copyRecursively(src, dest);
			System.out.println("Rep origine "+ Paths.get(env.getProperty("root.location.store")+ "/"+ repOrig) );
			System.out.println("Rep origine "+ Paths.get(env.getProperty("root.location.store")+ "/"+ repDes) );
			FileUtils.moveFile(Paths.get(env.getProperty("root.location.store")+ "/"+ repOrig).toFile(),Paths.get(env.getProperty("root.location.store")+ "/"+ repDes).toFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
   public static String diff(String str1, String str2) {
	    int index = str1.lastIndexOf(str2);
	    if (index > -1) {
	      return str1.substring(str2.length());
	    }
	    return str1;
	  }
   public NotificationsUser createNotificationsUser(Integer idRequest, Integer idOffer, Integer type, String nomInstitution, User user, Integer idDocument) {
		NotificationsUser notifUser = new NotificationsUser();
		notifUser.setIdRequest(idRequest);
		notifUser.setIdOffer(idOffer);
		notifUser.setType(type);
		notifUser.setUser(user);
		notifUser.setNomInstitution(nomInstitution);
		notifUser.setDateNotification(new Date());
		notificationsUserRep.save(notifUser);
		return notifUser;
	}
	
	public String createInd(SearchModel model) {
		String result = "";
		result += model.getName().equals("") ? "0" : "1";
		result += model.getCategory().equals("") ? "0" : "1";
		result += model.getKeyWord().equals("") ? "0" : "1";
		result += model.getDate().equals("") ? "0" : "1";
		return result;
	}
}