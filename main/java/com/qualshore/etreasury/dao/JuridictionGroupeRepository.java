package com.qualshore.etreasury.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.qualshore.etreasury.entity.Bank;
import com.qualshore.etreasury.entity.Groupe;
import com.qualshore.etreasury.entity.Institution;
import com.qualshore.etreasury.entity.Juridiction;
import com.qualshore.etreasury.entity.JuridictionGroupe;
import com.qualshore.etreasury.entity.Products;
import com.qualshore.etreasury.entity.RequestHasBank;

public interface JuridictionGroupeRepository extends JpaRepository<JuridictionGroupe, Integer>{
	
	List<JuridictionGroupe> findByGroupe(Groupe groupe);
	
	JuridictionGroupe findByGroupeAndJuridiction(Groupe groupe, Juridiction j);
	

	/*
	  @Query("SELECT CASE WHEN COUNT(i) > 0 THEN 'true' ELSE 'false' END FROM JuridictionGroupe jg,Groupe g,Juridiction j,Institution i WHERE jg.groupe= g "
	  		+ "AND jg.juridiction = j AND g.institution = i AND i.idInstitution= ?1 AND jg.juridiction.id IN (1,2,3,4,5)  ")
	  public boolean exitsByIdJuridiction(Integer idInstitution);
	*/	
	
	
	  @Query("SELECT jg FROM JuridictionGroupe jg WHERE jg.groupe.institution = ?1 AND jg.juridiction.id IN (1,2,3,4,5)")
	  List<JuridictionGroupe> findByGroupeAndInstituion(Institution institution);
	  
}
