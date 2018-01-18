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
import javax.persistence.Convert;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qualshore.etreasury.configure.DateAttributConverter;
import com.qualshore.etreasury.listener.InstitutionListener;

/**
 *
 * @author macbookpro
 */
@Entity
@Table(name = "institution")
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@XmlRootElement
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType=DiscriminatorType.STRING, length=2)
public class Institution implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_institution")
    private Integer idInstitution;
   
    @Column(name = "nom")
    private String nom;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_creation")
    @Temporal(TemporalType.TIMESTAMP)
    @Convert(converter = DateAttributConverter.class)
    private Date dateCreation;
 
    @Column(name = "nombre_agence")
    private String nombreAgence;

    @Column(name = "telephone1")
    private String telephone1;

    @Column(name = "telephone2")
    private String telephone2;

    @Column(name = "capital")
    private String capital;
    
    @JoinColumn(name = "locality_id_localite", referencedColumnName = "id_localite")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Locality localityIdLocalite;

    @Column(name = "is_active")
    private Boolean isActive;
    
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "institution", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Groupe> groupeList;
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "institution", fetch = FetchType.LAZY)
	private List<Notifications> notificationsList;

    public Institution() {
    }

    public Institution(Integer idInstitution) {
        this.idInstitution = idInstitution;
    }

    public Institution(Integer idInstitution, Date dateCreation) {
        this.idInstitution = idInstitution;
        this.dateCreation = dateCreation;
    }
    
    public Institution(String nom, Date dateCreation, String nombreAgence, String telephone1, String telephone2,
			String capital, Locality localityIdLocalite, Boolean isActive) {
		super();
		this.nom = nom;
		this.dateCreation = dateCreation;
		this.nombreAgence = nombreAgence;
		this.telephone1 = telephone1;
		this.telephone2 = telephone2;
		this.capital = capital;
		this.localityIdLocalite = localityIdLocalite;
		this.isActive = isActive;
	}

    
    public Locality getLocalityIdLocalite() {
		return localityIdLocalite;
	}

	public void setLocalityIdLocalite(Locality localityIdLocalite) {
		this.localityIdLocalite = localityIdLocalite;
	}
    
	public Integer getIdInstitution() {
        return idInstitution;
    }

    public void setIdInstitution(Integer idInstitution) {
        this.idInstitution = idInstitution;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getNombreAgence() {
        return nombreAgence;
    }

    public void setNombreAgence(String nombreAgence) {
        this.nombreAgence = nombreAgence;
    }

    public String getTelephone1() {
        return telephone1;
    }

    public void setTelephone1(String telephone1) {
        this.telephone1 = telephone1;
    }

    public String getTelephone2() {
        return telephone2;
    }

    public void setTelephone2(String telephone2) {
        this.telephone2 = telephone2;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }
    
    public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	@XmlTransient
    public List<Groupe> getGroupeList() {
        return groupeList;
    }

	public void setGroupeList(List<Groupe> groupeList) {
        this.groupeList = groupeList;
    }

	 @XmlTransient
	    public List<Notifications> getNotificationsList() {
	        return notificationsList;
	    }

	    public void setNotificationsList(List<Notifications> notificationsList) {
	        this.notificationsList = notificationsList;
	    }
	    
	    @Transient
	    public String getDiscriminatorValue() {
	    	DiscriminatorValue type = this.getClass().getAnnotation(DiscriminatorValue.class);
	    	return type == null ? null : type.value();
	    }
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idInstitution != null ? idInstitution.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Institution)) {
            return false;
        }
        Institution other = (Institution) object;
        if ((this.idInstitution == null && other.idInstitution != null) || (this.idInstitution != null && !this.idInstitution.equals(other.idInstitution))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qualshore.etreasury.entity.Institution[ idInstitution=" + idInstitution + " ]";
    }
    
}
