package com.qualshore.etreasury.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qualshore.etreasury.entity.Groupe;
import com.qualshore.etreasury.entity.Institution;


public interface GroupRepository extends JpaRepository<Groupe, Integer> {

	public List<Groupe> findByInstitution(Institution institution);
	
	@Query("SELECT g FROM Groupe g, Institution i WHERE g.institution = i.idInstitution AND type(i) = 'ET'")
	public List<Groupe> findAllByTypeET();
	
	@Query("SELECT g FROM Groupe g, Institution i WHERE g.institution = i.idInstitution AND type(i) = 'BA'")
	public List<Groupe> findAllByTypeBA();
	
	@Query("SELECT g FROM Groupe g, Institution i WHERE g.institution = i.idInstitution AND type(i) = 'EN'")
	public List<Groupe> findAllByTypeEN();
	
	public List<Groupe> findByIdGroupe(@Param("idGroupe") Integer idGroupe);
	
	public List<Groupe> findByNom(@Param("nom") String nom);
	
	@Query("SELECT CASE WHEN COUNT(g) > 0 THEN 'true' ELSE 'false' END FROM Groupe g WHERE g.nom= ?1")
	public boolean existsByNom(String nom);
	
	@Query("SELECT CASE WHEN COUNT(g) > 0 THEN 'true' ELSE 'false' END FROM Groupe g WHERE g.nom= ?1 AND g.institution= ?2")
	public boolean existsByNom(String nom, Institution idInstitution);
	
	@Query("SELECT CASE WHEN COUNT(g) > 0 THEN 'true' ELSE 'false' END FROM Groupe g WHERE g.idGroupe <> ?3 AND g.nom= ?1 AND g.institution= ?2")
	public boolean existsByNom(String nom, Institution idInstitution,Integer idGroupe);
	
	@Query("SELECT g FROM Groupe g WHERE g.institution = ?1 AND nom like 'admin_%'")
	public Groupe findByInstition(Institution institution);
	
	@Query("SELECT g FROM Groupe g WHERE g.institution = ?1 AND nom like 'default%'")
	public Groupe findByInstitionDefault(Institution institution);
	
	@Query("SELECT g FROM Groupe g WHERE g.idGroupe = ?1 AND nom like 'default%'")
	public Groupe findByDefault(Integer idGroup);
	
	@Query("SELECT CASE WHEN COUNT(g) > 0 THEN 'true' ELSE 'false' END FROM Groupe g WHERE g.idGroupe = ?1")
	public boolean existsByIdGroup(Integer idGroup);
	
	@Modifying
	@Transactional
	@Query("UPDATE Groupe g SET g.nom = ?1 , g.description = ?2 , g.institution = ?3 WHERE g.idGroupe= ?4")
	public int updateGroupe(String nom, String description, Institution idInstitution, Integer idGroup);
}