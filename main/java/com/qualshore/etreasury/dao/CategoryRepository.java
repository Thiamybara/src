package com.qualshore.etreasury.dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qualshore.etreasury.entity.Category;
import com.qualshore.etreasury.entity.Institution;
import com.qualshore.etreasury.entity.User;

public interface CategoryRepository extends JpaRepository<Category, Integer>{

	public List<Category> findByParent(Category parent);
	
	public List<Category> findByIdCategory(@Param("idCategory") Integer idDossier);
	
	@Query("SELECT c FROM Category c WHERE c.userIdUtilisateur= ?1")
	public List<Category> findAllByIdUtilisateur(User user);
	
	@Query("SELECT c FROM Category c WHERE c.userIdUtilisateur=?1")
	public List<Category> findAllByUser(Institution institution);
	
	@Query("SELECT c FROM Category c, User u, Groupe g WHERE c.userIdUtilisateur=u.idUtilisateur AND u.groupeIdGroupe=g.idGroupe AND g.institution=?1")
	public List<Category> findAllByInstitution(Institution institution);
	
	@Query("SELECT u FROM Category c, User u WHERE c.userIdUtilisateur = u.idUtilisateur AND c.idCategory=?1")
	public User getPropietaire(Integer idCategory);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM Category c WHERE c.idCategory=?1 AND c.userIdUtilisateur=?2")
	public void deleteByIdUtilisateur(Integer idCategory, User user);
	
	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN 'true' ELSE 'false' END FROM Category c WHERE c.libelle= ?1 AND c.userIdUtilisateur= ?2")
	public boolean existsByUserCategory(String libelle, User user);
	
	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN 'true' ELSE 'false' END FROM Category c, User u, Groupe g WHERE c.userIdUtilisateur=u.idUtilisateur AND u.groupeIdGroupe=g.idGroupe AND g.institution= ?2 AND c.libelle= ?1")
	public boolean existsByInstitutionCategory(String libelle, Institution institution);
	
	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN 'true' ELSE 'false' END FROM Category c WHERE c.idCategory = ?1")
	public boolean existsByIdCategory(Integer idCategory);
	
	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN 'true' ELSE 'false' END FROM Category c WHERE c.idCategory = ?1 AND c.userIdUtilisateur=?2")
	public boolean isProprietaire(Integer idCategory, User user);
	
	@Modifying
	@Transactional
	@Query("UPDATE Category c SET c.libelle= ?1 , c.dateCreation = ?2 , c.repertoire = ?3 WHERE c.userIdUtilisateur = ?4 AND c.idCategory= ?5")
	public int updateCategory(String libelle, Date dateCreation, String repertoire, User userIdUtilisateur, Integer idDossier);

	@Query("SELECT c FROM Category c WHERE c.libelle= ?1 AND c.userIdUtilisateur.groupeIdGroupe.institution=?2")
    public List<Category> findByLibelleAndInstitution(String libelle, Institution institution);
}
