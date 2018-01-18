package com.qualshore.etreasury.dao;

import com.qualshore.etreasury.entity.Groupe;
import com.qualshore.etreasury.entity.Institution;
import com.qualshore.etreasury.entity.Locality;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InstitutionRepository extends InstitutionBaseRepository<Institution> {

	@Query("SELECT i FROM Institution i WHERE type(i) = ?1")
	public List<Institution> findByType(String type);
	
	public List<Institution> findByIdInstitution(@Param("idInstitution")Integer idInstitution);
	
	@Query("SELECT CASE WHEN COUNT(i) > 0 THEN 'true' ELSE 'false' END FROM Institution i WHERE i.idInstitution= ?1")
	public boolean exitsByIdInstitution(Integer idInstitution);
	
	@Query("SELECT g FROM Groupe g, Institution i WHERE g.institution = i.idInstitution AND g.nom like 'default%' AND i.idInstitution = ?1 ")
	public Groupe getDefaultGroupByIdInstitution(Integer idInstitution);
	
	 @Query("SELECT CASE WHEN COUNT(i) > 0 THEN 'true' ELSE 'false' END FROM Institution i WHERE i.nom = ?1 AND i.localityIdLocalite = ?2 AND type(i) like 'BA' ")
		public boolean exitsByIdInstitutionName(String nomInstitution, Locality locality);
	 
	 @Query("SELECT CASE WHEN COUNT(i) > 0 THEN 'true' ELSE 'false' END FROM Institution i WHERE i.nom = ?1 AND i.localityIdLocalite = ?2 AND i.idInstitution <> ?3 AND type(i) like 'BA' ")
		public boolean exitsByIdInstitutionNameOthers(String nomInstitution, Locality locality, Integer idInstitution);
	 
	 
	 @Query("SELECT CASE WHEN COUNT(i) > 0 THEN 'true' ELSE 'false' END FROM Institution i WHERE i.nom = ?1 AND i.localityIdLocalite = ?2 AND type(i) like 'EN' ")
		public boolean exitsByIdInstitutionNameEtreprise(String nomInstitution, Locality locality);
	 @Query("SELECT CASE WHEN COUNT(i) > 0 THEN 'true' ELSE 'false' END FROM Institution i WHERE i.nom = ?1 AND i.localityIdLocalite = ?2 AND i.idInstitution <> ?3 AND type(i) like 'EN' ")
		public boolean exitsByIdInstitutionNameEtrepriseOthers(String nomInstitution, Locality locality, Integer idInstitution);
}