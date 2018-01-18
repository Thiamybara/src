/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qualshore.etreasury.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author macbookpro
 */
@Entity
@Table(name = "validation")
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Validation.findAll", query = "SELECT v FROM Validation v"),
    @NamedQuery(name = "Validation.findByIdValidation", query = "SELECT v FROM Validation v WHERE v.idValidation = :idValidation"),
    @NamedQuery(name = "Validation.findByNombreValidite", query = "SELECT v FROM Validation v WHERE v.nombreValidite = :nombreValidite"),
    @NamedQuery(name = "Validation.findByDate", query = "SELECT v FROM Validation v WHERE v.date = :date"),
    @NamedQuery(name = "Validation.findByIsValide", query = "SELECT v FROM Validation v WHERE v.isValide = :isValide"),
    @NamedQuery(name = "Validation.findByGroupe", query = "SELECT v FROM Validation v WHERE v.groupe = :groupe"),
    @NamedQuery(name = "Validation.findByNom", query = "SELECT v FROM Validation v WHERE v.nom = :nom"),
    @NamedQuery(name = "Validation.findByPrenom", query = "SELECT v FROM Validation v WHERE v.prenom = :prenom"),
    @NamedQuery(name = "Validation.findByEmail", query = "SELECT v FROM Validation v WHERE v.email = :email"),
    @NamedQuery(name = "Validation.findByTelephone", query = "SELECT v FROM Validation v WHERE v.telephone = :telephone")})
public class Validation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_validation")
    private Integer idValidation;

    @Column(name = "nombre_validite")
    private String nombreValidite;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "is_valide")
    private Boolean isValide;

    @Column(name = "groupe")
    private String groupe;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation

    @Column(name = "email")
    private String email;

    @Column(name = "telephone")
    private String telephone;
    @JoinColumn(name = "offer_id_offre", referencedColumnName = "id_offre")
    @ManyToOne(fetch = FetchType.LAZY)
    private Offer offerIdOffre;
    
    @JoinColumn(name = "request_id_demande", referencedColumnName = "id_demande")
    @ManyToOne(fetch = FetchType.LAZY)
    private Request requestIdDemande;

    public Validation() {
    }

    public Validation(Integer idValidation) {
        this.idValidation = idValidation;
    }

    public Integer getIdValidation() {
        return idValidation;
    }

    public void setIdValidation(Integer idValidation) {
        this.idValidation = idValidation;
    }

    public String getNombreValidite() {
        return nombreValidite;
    }

    public void setNombreValidite(String nombreValidite) {
        this.nombreValidite = nombreValidite;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getIsValide() {
        return isValide;
    }

    public void setIsValide(Boolean isValide) {
        this.isValide = isValide;
    }

    public String getGroupe() {
        return groupe;
    }

    public void setGroupe(String groupe) {
        this.groupe = groupe;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Offer getOfferIdOffre() {
        return offerIdOffre;
    }

    public void setOfferIdOffre(Offer offerIdOffre) {
        this.offerIdOffre = offerIdOffre;
    }

    public Request getRequestIdDemande() {
        return requestIdDemande;
    }

    public void setRequestIdDemande(Request requestIdDemande) {
        this.requestIdDemande = requestIdDemande;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idValidation != null ? idValidation.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Validation)) {
            return false;
        }
        Validation other = (Validation) object;
        if ((this.idValidation == null && other.idValidation != null) || (this.idValidation != null && !this.idValidation.equals(other.idValidation))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qualshore.etreasury.entity.Validation[ idValidation=" + idValidation + " ]";
    }
    
}
