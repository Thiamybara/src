
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qualshore.etreasury.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author macbookpro
 */
@Entity
@Table(name = "user_entreprise")
@DiscriminatorValue("EN")
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)

public class UserEntreprise extends User {
    private static final long serialVersionUID = 1L;
    
    @OneToMany(mappedBy = "userEntreprise", cascade= CascadeType.ALL, fetch= FetchType.LAZY)
    @JsonIgnore
    private List<Request> requestList;
    
    public UserEntreprise() {
    }

    
    public List<Request> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<Request> requestList) {
        this.requestList = requestList;
    }
    
	public UserEntreprise(String email, String login, String password, Date dateNaissance, Boolean isRemove, String nom,
			String prenom, String telephone, String photo, String telephoneFixe, Groupe groupeIdGroupe,
			Profile profilIdProfil, User boss, Enterprise enterprise) {
		super(email, login, password, dateNaissance, isRemove, nom, prenom, telephone, photo, telephoneFixe, boss,
				groupeIdGroupe, profilIdProfil);
		//this.enterprise = enterprise;
	}
}