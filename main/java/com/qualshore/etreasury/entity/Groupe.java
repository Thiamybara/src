/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qualshore.etreasury.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author macbookpro
 */
@Entity
@Table(name = "groupe")
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@XmlRootElement

public class Groupe implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_groupe")
    private Integer idGroupe;

    @Column(name = "nom")
    private String nom;

    @Column(name = "description")
    private String description;
    @Column(name = "has_juridiction")
    private boolean hasJuridiction;
    

	@JoinColumn(name = "institution_id_institution", referencedColumnName = "id_institution")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Institution institution;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "groupeIdGroupe", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> userList;
    
    /*@ManyToMany(mappedBy="groupes")
    private List<ValidationLevel> validationLevels;*/
    
    @OneToMany(mappedBy="groupe")
    @JsonIgnore
    private List<JuridictionGroupe> juridictionGroupes = new ArrayList<>();
    
    @OneToMany(mappedBy="groupe")
    @JsonIgnore
    private List<ValidationLevelGroupe> validationLevelGroupes = new ArrayList<>();

	public Groupe() {
    }

    public Groupe(Integer idGroupe) {
        this.idGroupe = idGroupe;
    }
    
    public Groupe(String nom, String description, Institution institution) {
		super();
		this.nom = nom;
		this.description = description;
		this.institution = institution;
	}

	public Integer getIdGroupe() {
        return idGroupe;
    }

    public void setIdGroupe(Integer idGroupe) {
        this.idGroupe = idGroupe;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public boolean isHasJuridiction() {
		return hasJuridiction;
	}

	public void setHasJuridiction(boolean hasJuridiction) {
		this.hasJuridiction = hasJuridiction;
	}
    /*@XmlTransient
    public List<ValidationLevelHasGroup> getValidationLevelHasGroupList() {
        return validationLevelHasGroupList;
    }

    public void setValidationLevelHasGroupList(List<ValidationLevelHasGroup> validationLevelHasGroupList) {
        this.validationLevelHasGroupList = validationLevelHasGroupList;
    }*/

    public Institution getInstitution() {
		return institution;
	}

	public void setInstitution(Institution institution) {
		this.institution = institution;
	}

	@XmlTransient
    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idGroupe != null ? idGroupe.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Groupe)) {
            return false;
        }
        Groupe other = (Groupe) object;
        if ((this.idGroupe == null && other.idGroupe != null) || (this.idGroupe != null && !this.idGroupe.equals(other.idGroupe))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qualshore.etreasury.entity.Groupe[ idGroupe=" + idGroupe + " ]";
    }
    
    public List<JuridictionGroupe> getJuridictionGroupes() {
		return juridictionGroupes;
	}

	
	public JuridictionGroupe addJuridictionGroupe(Juridiction j){
		
		JuridictionGroupe jGroupe = new JuridictionGroupe();
		jGroupe.setDateCreation(new Date());
		jGroupe.setJuridiction(j);
		jGroupe.setGroupe(this);
		jGroupe.setGroupeId(this.getIdGroupe());
		jGroupe.setJuridictionId(j.getId());
		this.getJuridictionGroupes().add(jGroupe);
		j.getJuridictionGroupes().add(jGroupe);
		
		return jGroupe;
	}
	
	public void setJuridictionGroupes(List<JuridictionGroupe> juridictionGroupes) {
		this.juridictionGroupes = juridictionGroupes;
	}
	
	public List<ValidationLevelGroupe> getValidationLevelGroupes() {
		return validationLevelGroupes;
	}

	public void setValidationLevelGroupes(List<ValidationLevelGroupe> validationLevelGroupes) {
		this.validationLevelGroupes = validationLevelGroupes;
	}

}
