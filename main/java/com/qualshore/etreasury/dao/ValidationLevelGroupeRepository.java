package com.qualshore.etreasury.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.qualshore.etreasury.entity.Groupe;
import com.qualshore.etreasury.entity.Institution;
import com.qualshore.etreasury.entity.Products;
import com.qualshore.etreasury.entity.ValidationLevel;
import com.qualshore.etreasury.entity.ValidationLevelGroupe;

public interface ValidationLevelGroupeRepository extends JpaRepository<ValidationLevelGroupe, Integer> {

	public boolean existsByValidationLevelAndGroupe(ValidationLevel vl, Groupe gr);
	
	public boolean existsByValidationLevelAndNiveau(ValidationLevel vl,int niveau);
	
	public boolean existsByGroupeAndNiveau(Groupe groupe,int niveau);
	
	@Query("SELECT vlg FROM ValidationLevelGroupe vlg, ValidationLevel vl WHERE vlg.validationLevel=vl.idNiveauValidation AND vlg.niveau=1 AND vl.product=?1 AND vl.institution= ?2 AND vlg.groupe=?3 AND vl.sens=?4")
	ValidationLevelGroupe findByProductAndInstitutionAndGroupeFirstLevel(Products product,Institution institution, Groupe groupe, String sens);
	
	@Query("SELECT vlg FROM ValidationLevelGroupe vlg, ValidationLevel vl WHERE vlg.validationLevel=vl.idNiveauValidation AND vl.product=?1 AND vl.institution= ?2 AND vlg.groupe=?3 AND vl.sens=?4")
	ValidationLevelGroupe findByProductAndInstitutionAndGroupe(Products product,Institution institution, Groupe groupe, String sens);
	
	ValidationLevelGroupe findByValidationLevelAndGroupeAndNiveau(ValidationLevel vl, Groupe gr, Integer niveau);
	
	ValidationLevelGroupe findByValidationLevelAndGroupe(ValidationLevel vl, Groupe gr);
	
	ValidationLevelGroupe findByValidationLevelAndNiveau(ValidationLevel vl,int niveau);
	
	@Query("SELECT CASE WHEN COUNT(vlg) > 0 THEN 'true' ELSE 'false' END FROM ValidationLevelGroupe vlg, ValidationLevel vl WHERE vlg.validationLevel=vl.idNiveauValidation AND vlg.groupe= ?1 AND vl.product= ?2")
	public boolean isGroupInProductChain(Groupe groupe, Products product);
			
	@Query("SELECT vlg FROM ValidationLevelGroupe vlg, ValidationLevel vl WHERE vlg.validationLevel=vl.idNiveauValidation AND vl.institution= ?1")
	List<ValidationLevelGroupe> findByInstitution(Institution institution);
	
	@Query("SELECT count(vlg) FROM ValidationLevelGroupe vlg WHERE vlg.validationLevel= ?1")
	int countByValidationLevel(ValidationLevel vl);
	
	List<ValidationLevelGroupe> findByValidationLevel(ValidationLevel vLevel);
}
