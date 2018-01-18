package com.qualshore.etreasury.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.qualshore.etreasury.entity.Category;
import com.qualshore.etreasury.entity.Documents;
import com.qualshore.etreasury.entity.Institution;
import com.qualshore.etreasury.entity.Request;
import com.qualshore.etreasury.entity.User;

public interface DocumentsRepository extends JpaRepository<Documents, Integer> {

	@Query("SELECT d FROM Documents d WHERE d.category = ?1")
	public List<Documents> findAllDocumentByCategory(Category category);
	
	@Query("SELECT d FROM Documents d, Category c WHERE d.category= c.idCategory AND c.userIdUtilisateur= ?1")
	public List<Documents> findAllDocumentByUser(User user);
	
	@Query("SELECT d FROM Documents d, Category c WHERE d.etat='CREATED' AND d.category = c.idCategory AND c.userIdUtilisateur= ?1")
	public List<Documents> findCreatedDocumentByUser(User user);
	
	@Query("SELECT d FROM Documents d, Category c WHERE d.etat='RECEIVED' AND d.category = c.idCategory AND c.userIdUtilisateur= ?1")
	public List<Documents> findReceivedDocumentByUser(User user);
	
	@Query("SELECT CASE WHEN COUNT(d) > 0 THEN 'true' ELSE 'false' END FROM Documents d, Category c WHERE d.category = c.idCategory AND d.idDocuments = ?1 AND c.userIdUtilisateur=?2")
	public boolean isProprietaire(Integer idDocument, User user);
	
    @Query("SELECT d FROM Documents d, DocumentsHasRequest dr WHERE d.idDocuments=dr.documents AND dr.request = ?1")
    public List<Documents> getRequestDocuments(Request request);

    public List<Documents> findByCategory(Category category);

    @Query("SELECT d FROM Documents d, Category c WHERE d.category= c.idCategory AND c.userIdUtilisateur= ?1")
	public List<Documents> findByUser(User user);
	

  //1st level
    @Query("SELECT d FROM Documents d WHERE d.nom = ?1 AND d.category.userIdUtilisateur.groupeIdGroupe.institution=?2")
    public List<Documents> findByName(String nom, Institution institution);
    @Query("SELECT d FROM Documents d WHERE d.nom like %?1% AND d.category.userIdUtilisateur.groupeIdGroupe.institution=?2")
    public List<Documents> findByNamePartial(String nom, Institution institution);
    
    @Query("SELECT d FROM Documents d WHERE d.category.libelle= ?1 AND d.category.userIdUtilisateur.groupeIdGroupe.institution=?2")
    public List<Documents> findByCategory(String libelle, Institution institution);
    @Query("SELECT d FROM Documents d WHERE d.category.libelle like %?1% AND d.category.userIdUtilisateur.groupeIdGroupe.institution=?2")
    public List<Documents> findByCategoryPartial(String libelle, Institution institution);
    
    @Query("SELECT d FROM Documents d WHERE d.motsCles= ?1 AND d.category.userIdUtilisateur.groupeIdGroupe.institution=?2")
    public List<Documents> findByMotsCles(String motCles, Institution institution);
    @Query("SELECT d FROM Documents d WHERE d.motsCles like %?1% AND d.category.userIdUtilisateur.groupeIdGroupe.institution=?2")
    public List<Documents> findByMotsClesPartial(String motCles, Institution institution);
    
    @Query("SELECT d FROM Documents d WHERE d.dateChargement= ?1 AND d.category.userIdUtilisateur.groupeIdGroupe.institution=?2")
    public List<Documents> findByDate(Date dateChargement, Institution institution);
    @Query("SELECT d FROM Documents d WHERE d.dateChargement= ?1 AND d.category.userIdUtilisateur.groupeIdGroupe.institution=?2")
    public List<Documents> findByDatePartial(Date dateChargement, Institution institution);
    
    //2nd level  
    @Query("SELECT d FROM Documents d WHERE d.nom= ?1 AND d.category.libelle= ?2 AND d.category.userIdUtilisateur.groupeIdGroupe.institution=?3")
    public List<Documents> findByNameAndCategory(String name, String libelle, Institution institution);
    @Query("SELECT d FROM Documents d WHERE d.nom like %?1% AND d.category.libelle like %?2% AND d.category.userIdUtilisateur.groupeIdGroupe.institution=?3")
    public List<Documents> findByNameAndCategoryPartial(String name, String libelle, Institution institution);
    
    @Query("SELECT d FROM Documents d WHERE d.nom = ?1 AND d.motsCles= ?2 AND d.category.userIdUtilisateur.groupeIdGroupe.institution=?3")
    public List<Documents> findByNameAndMotsCles(String nom, String motsCles, Institution institution);
    @Query("SELECT d FROM Documents d WHERE d.nom like %?1% AND d.motsCles like %?2% AND d.category.userIdUtilisateur.groupeIdGroupe.institution=?3")
    public List<Documents> findByNameAndMotsClesPartial(String nom, String motsCles, Institution institution);
    
    @Query("SELECT d FROM Documents d WHERE d.nom= ?1 AND d.dateChargement= ?2 AND d.category.userIdUtilisateur.groupeIdGroupe.institution=?3")
    public List<Documents> findByNameAndDate(String name, Date dateChargement, Institution institution);
    @Query("SELECT d FROM Documents d WHERE d.nom like %?1% AND d.dateChargement= ?2 AND d.category.userIdUtilisateur.groupeIdGroupe.institution=?3")
    public List<Documents> findByNameAndDatePartial(String name, Date dateChargement, Institution institution);
    
    @Query("SELECT d FROM Documents d WHERE d.motsCles= ?1 AND d.dateChargement= ?2 AND d.category.userIdUtilisateur.groupeIdGroupe.institution=?3")
    public List<Documents> findByMotsClesAndDate(String motsCles, Date dateChargement, Institution institution);
    @Query("SELECT d FROM Documents d WHERE d.motsCles like %?1% AND d.dateChargement= ?2 AND d.category.userIdUtilisateur.groupeIdGroupe.institution=?3")
    public List<Documents> findByMotsClesAndDatePartial(String motsCles, Date dateChargement, Institution institution);

    @Query("SELECT d FROM Documents d WHERE d.category.libelle= ?1 AND d.dateChargement= ?2 AND d.category.userIdUtilisateur.groupeIdGroupe.institution=?3")
    public List<Documents> findByCategoryAndDate(String libelle, Date dateChargement, Institution institution);
    @Query("SELECT d FROM Documents d WHERE d.category.libelle like %?1% AND d.dateChargement= ?2 AND d.category.userIdUtilisateur.groupeIdGroupe.institution=?3")
    public List<Documents> findByCategoryAndDatePartial(String libelle, Date dateChargement, Institution institution);

    @Query("SELECT d FROM Documents d WHERE d.category.libelle= ?1 AND d.motsCles= ?2 AND d.category.userIdUtilisateur.groupeIdGroupe.institution=?3")
    public List<Documents> findByCategoryAndMotsCles(String libelle, String motsCles, Institution institution);
    @Query("SELECT d FROM Documents d WHERE d.category.libelle like %?1% AND d.motsCles like %?2% AND d.category.userIdUtilisateur.groupeIdGroupe.institution=?3")
    public List<Documents> findByCategoryAndMotsClesPartial(String libelle, String motsCles, Institution institution);
    
    //3rd level
    @Query("SELECT d FROM Documents d WHERE d.nom= ?1 AND d.category.libelle= ?2 AND d.motsCles= ?3 AND d.category.userIdUtilisateur.groupeIdGroupe.institution=?4")
    public List<Documents> findByNameAndCategoryAndMotsCles(String name, String libelle, String motsCles, Institution institution);
    @Query("SELECT d FROM Documents d WHERE d.nom like %?1% AND d.category.libelle like %?2% AND d.motsCles like %?3% AND d.category.userIdUtilisateur.groupeIdGroupe.institution=?4")
    public List<Documents> findByNameAndCategoryAndMotsClesPartial(String name, String libelle, String motsCles, Institution institution);
    
    @Query("SELECT d FROM Documents d WHERE d.nom = ?1 AND d.motsCles= ?2 AND d.dateChargement= ?3 AND d.category.userIdUtilisateur.groupeIdGroupe.institution=?4")
    public List<Documents> findByNameAndMotsClesAndDate(String nom, String motsCles, Date dateChargement, Institution institution);
    @Query("SELECT d FROM Documents d WHERE d.nom like %?1% AND d.motsCles like %?2% AND d.dateChargement= ?3 AND d.category.userIdUtilisateur.groupeIdGroupe.institution=?4")
    public List<Documents> findByNameAndMotsClesAndDatePartial(String nom, String motsCles, Date dateChargement, Institution institution);
    
    @Query("SELECT d FROM Documents d WHERE d.nom= ?1 AND d.category.libelle= ?2 AND d.dateChargement= ?3 AND d.category.userIdUtilisateur.groupeIdGroupe.institution=?4")
    public List<Documents> findByNameAndCategoryAndDate(String name, String libelle, Date dateChargement, Institution institution);
    @Query("SELECT d FROM Documents d WHERE d.nom like %?1% AND d.category.libelle like %?2% AND d.dateChargement= ?3 AND d.category.userIdUtilisateur.groupeIdGroupe.institution=?4")
    public List<Documents> findByNameAndCategoryAndDatePartial(String name, String libelle, Date dateChargement, Institution institution);

    @Query("SELECT d FROM Documents d WHERE d.category.libelle= ?1 AND d.motsCles= ?2 AND d.dateChargement= ?3 AND d.category.userIdUtilisateur.groupeIdGroupe.institution=?4")
    public List<Documents> findByCategoryAndMotsClesAndDate(String libelle, String motsCles, Date dateChargement, Institution institution);
    @Query("SELECT d FROM Documents d WHERE d.category.libelle like %?1% AND d.motsCles like %?2% AND d.dateChargement= ?3 AND d.category.userIdUtilisateur.groupeIdGroupe.institution=?4")
    public List<Documents> findByCategoryAndMotsClesAndDatePartial(String libelle, String motsCles, Date dateChargement, Institution institution);

    //4th Level
    @Query("SELECT d FROM Documents d WHERE d.nom= ?1 AND d.category.libelle= ?2 AND d.motsCles= ?3 AND d.dateChargement= ?4 AND d.category.userIdUtilisateur.groupeIdGroupe.institution=?5")
    public List<Documents> findByNameAndCategoryAndMotsClesAndDate(String name, String libelle, String motsCles, Date dateChargement, Institution institution);
    @Query("SELECT d FROM Documents d WHERE d.nom like %?1% AND d.category.libelle like %?2% AND d.motsCles like %?3% AND d.dateChargement= ?4 AND d.category.userIdUtilisateur.groupeIdGroupe.institution=?5")
    public List<Documents> findByNameAndCategoryAndMotsClesAndDatePartial(String name, String libelle, String motsCles, Date dateChargement, Institution institution);
    
   
}
