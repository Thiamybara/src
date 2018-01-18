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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author macbookpro
 */
@Entity
@Table(name = "validation_level")
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@XmlRootElement
public class ValidationLevel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_niveau_validation")
    private Integer idNiveauValidation;
    
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "sens")
    private String sens;
    
    @Column(name = "nombre_validation")
    private Integer nombreValidation;
 
    @Column(name = "type_validation")
    private String typeValidation;
    
    @Column(name = "notification_sms")
    private Boolean notificationSms;
    
    @Column(name = "alls_required")
    private Boolean allsRequired;
    
    @Column(name = "is_valid_chain")
    private Boolean isValidChain;
        
    @OneToMany(mappedBy="validationLevel", cascade = {CascadeType.REMOVE})
    @JsonIgnore
    private List<ValidationLevelGroupe> validationLevelGroupes = new ArrayList<>();
    
    @JoinColumn(name = "product", referencedColumnName = "id_produits")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Products product;
    
    @JoinColumn(name = "institution", referencedColumnName = "id_institution")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Institution institution;

    public ValidationLevel() {
    }

    public ValidationLevel(Integer idNiveauValidation) {
        this.idNiveauValidation = idNiveauValidation;
    }

    public Integer getIdNiveauValidation() {
        return idNiveauValidation;
    }

    public void setIdNiveauValidation(Integer idNiveauValidation) {
        this.idNiveauValidation = idNiveauValidation;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSens() {
        return sens;
    }

    public void setSens(String sens) {
        this.sens = sens;
    }

    public Integer getNombreValidation() {
        return nombreValidation;
    }

    public void setNombreValidation(Integer nombreValidation) {
        this.nombreValidation = nombreValidation;
    }

    public String getTypeValidation() {
        return typeValidation;
    }

    public void setTypeValidation(String typeValidation) {
        this.typeValidation = typeValidation;
    }

    public Boolean getNotificationSms() {
        return notificationSms;
    }

    public void setNotificationSms(Boolean notificationSms) {
        this.notificationSms = notificationSms;
    }

    public Boolean getAllsRequired() {
        return allsRequired;
    }

    public void setAllsRequired(Boolean allsRequired) {
        this.allsRequired = allsRequired;
    }

    public Boolean getIsValidChain() {
		return isValidChain;
	}

	public void setIsValidChain(Boolean isValidChain) {
		this.isValidChain = isValidChain;
	}

	public Products getProduct() {
		return product;
	}

	public void setProduct(Products product) {
		this.product = product;
	}

	public Institution getInstitution() {
		return institution;
	}

	public void setInstitution(Institution institution) {
		this.institution = institution;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idNiveauValidation != null ? idNiveauValidation.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ValidationLevel)) {
            return false;
        }
        ValidationLevel other = (ValidationLevel) object;
        if ((this.idNiveauValidation == null && other.idNiveauValidation != null) || (this.idNiveauValidation != null && !this.idNiveauValidation.equals(other.idNiveauValidation))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qualshore.etreasury.entity.ValidationLevel[ idNiveauValidation=" + idNiveauValidation + " ]";
    }

	public List<ValidationLevelGroupe> getValidationLevelGroupes() {
		return validationLevelGroupes;
	}

	public void setValidationLevelGroupes(List<ValidationLevelGroupe> validationLevelGroupes) {
		this.validationLevelGroupes = validationLevelGroupes;
	}
	
	public ValidationLevelGroupe addValidationLevelGroupe(Groupe g, ValidationLevelGroupe vLGroupe){
		vLGroupe.setValidationLevelGroupePK(new ValidationLevelGroupePK(g.getIdGroupe(), this.getIdNiveauValidation()));
        /*vLGroupe.setGroupeId(g.getIdGroupe());
        vLGroupe.setIdNiveauValidation(this.getIdNiveauValidation());*/
        vLGroupe.setGroupe(g);
        vLGroupe.setValidationLevel(this);
        this.getValidationLevelGroupes().add(vLGroupe);
        g.getValidationLevelGroupes().add(vLGroupe);
        
        return vLGroupe;
    }
}
