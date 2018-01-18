
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
@Table(name = "user_banque")
@DiscriminatorValue("BA")
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@XmlRootElement
/*@NamedQueries({
    @NamedQuery(name = "UserBanque.findAll", query = "SELECT u FROM UserBanque u"),
    @NamedQuery(name = "UserBanque.findByIdUserBanque", query = "SELECT u FROM UserBanque u WHERE u.idUserBanque = :idUserBanque"),
    @NamedQuery(name = "UserBanque.findByBoss", query = "SELECT u FROM UserBanque u WHERE u.boss = :boss")})*/
public class UserBanque extends User {
    
	private static final long serialVersionUID = 1L;
	/*@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_user_banque")
    private Integer idUserBanque;*/

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userBanqueIdUserBanque", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Offer> offerList;
    
    /*@JoinColumn(name = "user_id_utilisateur", referencedColumnName = "id_utilisateur")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User userIdUtilisateur;*/
    
    /*@JoinColumn(name = "institution", referencedColumnName = "id_institution", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Bank bank;*/

    public UserBanque() {
    }
    
    public UserBanque(String email, String login, String password, Date dateNaissance, Boolean isRemove, String nom,
			String prenom, String telephone, String photo, String telephoneFixe, Groupe groupeIdGroupe,
			Profile profilIdProfil, User boss, List<Offer> offerList) {
		super(email, login, password, dateNaissance, isRemove, nom, prenom, telephone, photo, telephoneFixe, boss,
				groupeIdGroupe, profilIdProfil);
		this.offerList = offerList;
	}

	/*public UserBanque(Integer idUserBanque) {
        this.idUserBanque = idUserBanque;
    }

    public Integer getIdUserBanque() {
        return idUserBanque;
    }

    public void setIdUserBanque(Integer idUserBanque) {
        this.idUserBanque = idUserBanque;
    }*/

    @XmlTransient
    public List<Offer> getOfferList() {
        return offerList;
    }

    public void setOfferList(List<Offer> offerList) {
        this.offerList = offerList;
    }
/*
    public User getUserIdUtilisateur() {
        return userIdUtilisateur;
    }

    public void setUserIdUtilisateur(User userIdUtilisateur) {
        this.userIdUtilisateur = userIdUtilisateur;
    }*/

    /*public Bank getBanqueIdBanque() {
        return bank;
    }

    public void setBanqueIdBanque(Bank bank) {
        this.bank = bank;
    }*/
}