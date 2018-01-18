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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author macbookpro
 */
@Entity
@Table(name = "session")
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Session.findAll", query = "SELECT s FROM Session s"),
    @NamedQuery(name = "Session.findByIdSession", query = "SELECT s FROM Session s WHERE s.idSession = :idSession"),
    @NamedQuery(name = "Session.findByIsActive", query = "SELECT s FROM Session s WHERE s.isActive = :isActive"),
    @NamedQuery(name = "Session.findByDateDebut", query = "SELECT s FROM Session s WHERE s.dateDebut = :dateDebut"),
    @NamedQuery(name = "Session.findByDateFin", query = "SELECT s FROM Session s WHERE s.dateFin = :dateFin"),
    @NamedQuery(name = "Session.findByToken", query = "SELECT s FROM Session s WHERE s.token = :token")})
public class Session implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_session")
    private Integer idSession;
    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "date_debut")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFin;

    @Column(name = "token")
    private String token;
    @JoinColumn(name = "user_id_utilisateur", referencedColumnName = "id_utilisateur")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User userIdUtilisateur;
    @Column(name = "compteur")
    private int compteur;

    public Session() {
    }

    public Session(Integer idSession) {
        this.idSession = idSession;
    }

    public Integer getIdSession() {
        return idSession;
    }

    public void setIdSession(Integer idSession) {
        this.idSession = idSession;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUserIdUtilisateur() {
        return userIdUtilisateur;
    }

    public void setUserIdUtilisateur(User userIdUtilisateur) {
        this.userIdUtilisateur = userIdUtilisateur;
    }

    public int getCompteur() {
		return compteur;
	}

	public void setCompteur(int compteur) {
		this.compteur = compteur;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idSession != null ? idSession.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Session)) {
            return false;
        }
        Session other = (Session) object;
        if ((this.idSession == null && other.idSession != null) || (this.idSession != null && !this.idSession.equals(other.idSession))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qualshore.etreasury.entity.Session[ idSession=" + idSession + " ]";
    }
    
}
