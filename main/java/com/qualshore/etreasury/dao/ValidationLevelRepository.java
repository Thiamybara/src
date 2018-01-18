package com.qualshore.etreasury.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.qualshore.etreasury.entity.Institution;
import com.qualshore.etreasury.entity.Products;
import com.qualshore.etreasury.entity.ValidationLevel;

public interface ValidationLevelRepository  extends JpaRepository<ValidationLevel, Integer>{
	
	ValidationLevel findByProductAndInstitutionAndSens(Products product, Institution institution, String sens);
	
	List<ValidationLevel> findByInstitution(Institution institution);
	
	@Query("SELECT CASE WHEN COUNT(v) > 0 THEN 'true' ELSE 'false' END FROM ValidationLevel v WHERE v.product= ?1 AND v.institution= ?2 AND v.sens= ?3 ")
	public boolean existsByProductAndInstitution(Products product, Institution idInstitution,String sens);
	
	ValidationLevel findByInstitutionAndProduct(Institution i, Products p);
	
	public boolean existsByProductAndInstitutionAndSensAndAllsRequired(Products product, Institution institution, String sens, boolean allRequired);
}