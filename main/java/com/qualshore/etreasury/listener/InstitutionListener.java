package com.qualshore.etreasury.listener;

import javax.persistence.PostPersist;

import org.springframework.beans.factory.annotation.Autowired;

import com.qualshore.etreasury.dao.GroupRepository;
import com.qualshore.etreasury.entity.Groupe;
import com.qualshore.etreasury.entity.Institution;

public class InstitutionListener {
	
	@Autowired
	GroupRepository gRep;

	/**
     * Méthode permettant d'ajouter un grpe default à l'institution
     */
    @PostPersist
    public void addGroupeToInstitution(Institution i){
    	Groupe groupe = new Groupe("default_" + i.getNom(), "groupe par défaut de " + i.getNom(), i);
    	gRep.save(groupe);
    }
}
