
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qualshore.etreasury.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qualshore.etreasury.listener.EnterpriseListener;

/**
 *
 * @author macbookpro
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@EntityListeners(EnterpriseListener.class)
@DiscriminatorValue("EN")
public class Enterprise extends Institution{
    private static final long serialVersionUID = 1L;
    
    /*@Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;*/
    
    /*@OneToMany(cascade = CascadeType.ALL, mappedBy = "enterprise", fetch = FetchType.LAZY)
    @JsonIgnore	
    private List<User> users;*/
    
    /*@OneToMany(cascade = CascadeType.ALL, mappedBy = "enterpriseIdEntreprise", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Category> categoryList;*/

    public Enterprise() {
    }
    
    /*public Enterprise(String nom, Date dateCreation, String nombreAgence, String telephone1, String telephone2,
			String capital, Locality localityIdLocalite, List<Groupe> groupeList, boolean isActive, Date date,
			List<User> userEntrepriseList, List<Category> categoryList) {
		super(nom, dateCreation, nombreAgence, telephone1, telephone2, capital, localityIdLocalite, isActive, groupeList);
		//this.date = date;
		this.users = userEntrepriseList;
		this.categoryList = categoryList;
	}
*/
    /*public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
*/
/*    @XmlTransient
    public List<User> getUserEntrepriseList() {
        return users;
    }

    public void setUserEntrepriseList(List<User> users) {
        this.users = users;
    }*/

   /* @XmlTransient
    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }*/
}