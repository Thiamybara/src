/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qualshore.etreasury.entity;

import java.io.Serializable;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
@Table(name = "category")
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@XmlRootElement

public class Category implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_category")
    private Integer idCategory;
    
    @Column(name = "libelle")
    private String libelle;
    
    @Column(name = "date_creation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    
    @Column(name = "repertoire")
    private String repertoire;
    
    @JoinColumn(name = "user_id_utilisateur", referencedColumnName = "id_utilisateur")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User userIdUtilisateur;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Documents> documentsList;
    
    @JoinColumn(name = "parent", referencedColumnName = "id_category")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Category parent;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", fetch = FetchType.LAZY)
    @JsonIgnore
	private List<Category> sousCategoryList;

    public Category() {
    }

    public Category(Integer idCategory) {
        this.idCategory = idCategory;
    }

    public Category(String libelle, Date dateCreation, String repertoire, User userIdUtilisateur) {
		super();
		this.libelle = libelle;
		this.dateCreation = dateCreation;
		this.repertoire = repertoire;
		this.userIdUtilisateur = userIdUtilisateur;
	}

	public Integer getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Integer idCategory) {
        this.idCategory = idCategory;
    }

    public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getRepertoire() {
        return repertoire;
    }

    public void setRepertoire(String repertoire) {
        this.repertoire = repertoire;
    }

    public User getUserIdUtilisateur() {
        return userIdUtilisateur;
    }

    public void setUserIdUtilisateur(User userIdUtilisateur) {
        this.userIdUtilisateur = userIdUtilisateur;
    }

    public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	@XmlTransient
	public List<Category> getSousCategoryList() {
		return sousCategoryList;
	}

	public void setSousCategoryList(List<Category> sousCategoryList) {
		this.sousCategoryList = sousCategoryList;
	}

	@XmlTransient
    public List<Documents> getDocumentsList() {
        return documentsList;
    }

    public void setDocumentsList(List<Documents> documentsList) {
        this.documentsList = documentsList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCategory != null ? idCategory.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Category)) {
            return false;
        }
        Category other = (Category) object;
        if ((this.idCategory == null && other.idCategory != null) || (this.idCategory != null && !this.idCategory.equals(other.idCategory))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qualshore.etreasury.entity.Folder[ idCategory=" + idCategory + " ]";
    }
    
}
