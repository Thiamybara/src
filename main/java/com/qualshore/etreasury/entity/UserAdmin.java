/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qualshore.etreasury.entity;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author macbookpro
 */
@Entity
@Table(name = "user_admin")
@DiscriminatorValue("ET")
/*@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserAdmin.findAll", query = "SELECT u FROM UserAdmin u"),
    @NamedQuery(name = "UserAdmin.findByIdUserAdmin", query = "SELECT u FROM UserAdmin u WHERE u.idUserAdmin = :idUserAdmin")})*/
public class UserAdmin extends User {
    private static final long serialVersionUID = 1L;
    /*@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_user_admin")
    private Integer idUserAdmin;*/
    
	/*@JoinColumn(name = "user_id_utilisateur", referencedColumnName = "id_utilisateur")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User userIdUtilisateur;*/

    public UserAdmin() {
    }

    /*public UserAdmin(Integer idUserAdmin) {
        this.idUserAdmin = idUserAdmin;
    }

    public Integer getIdUserAdmin() {
        return idUserAdmin;
    }

    public void setIdUserAdmin(Integer idUserAdmin) {
        this.idUserAdmin = idUserAdmin;
    }*/

    

	public UserAdmin(String email, String login, String password, Date dateNaissance, Boolean isRemove, String nom,
			String prenom, String telephone, String photo, String telephoneFixe, Groupe groupeIdGroupe,
			Profile profilIdProfil) {
		super(email, login, password, dateNaissance, isRemove, nom, prenom, telephone, photo, telephoneFixe, null,
				groupeIdGroupe, profilIdProfil);
	}
    
    
    
    /*@Override
    public int hashCode() {
        int hash = 0;
        hash += (idUserAdmin != null ? idUserAdmin.hashCode() : 0);
        return hash;
    }*/

   /* @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserAdmin)) {
            return false;
        }
        UserAdmin other = (UserAdmin) object;
        if ((this.idUserAdmin == null && other.idUserAdmin != null) || (this.idUserAdmin != null && !this.idUserAdmin.equals(other.idUserAdmin))) {
            return false;
        }
        return true;
    }*/

    /*@Override
    public String toString() {
        return "com.qualshore.etreasury.entity.UserAdmin[ idUserAdmin=" + idUserAdmin + " ]";
    }*/
    
}
