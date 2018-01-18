package com.qualshore.etreasury.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qualshore.etreasury.listener.UserListener;

/**
 *
 * @author macbookpro
 */
@Entity
@Table(name = "user")
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType=DiscriminatorType.STRING, length=2)
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@XmlRootElement
@EntityListeners(UserListener.class)
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_utilisateur")
    private Integer idUtilisateur;

    @Column(name = "email")
    private String email;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;
    
    @Column(name = "date_creation")
    @Temporal(TemporalType.DATE)
    private Date dateCreation;
    
    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "photo")
    private String photo;

    @Column(name = "telephone_fixe")
    private String telephoneFixe;
    
    @Column(name = "is_connected")
    private Boolean isConnected;

    //@Size(max = 11)
    //@Column(name = "boss")
    //private Integer boss;
    
    public Boolean getIsConnected() {
		return isConnected;
	}

	public void setIsConnected(Boolean isConnected) {
		this.isConnected = isConnected;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<DocumentsHasUser> documentsHasUserList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userIdUtilisateur", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Session> sessionList;
    
    /*@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserHasMessage> userHasMessageList;*/
    
    @JoinColumn(name = "groupe_id_groupe", referencedColumnName = "id_groupe")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Groupe groupeIdGroupe;
    
    @JoinColumn(name = "profil_id_profil", referencedColumnName = "id_profil")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Profile profilIdProfil;
    
    @JoinColumn(name = "boss", referencedColumnName = "id_utilisateur")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
	private User boss;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "boss", fetch = FetchType.LAZY)
    @JsonIgnore
	private List<User> bossList;
    
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
    @JsonIgnore
    private List<Notifications> notificationsList;

   
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
    @JsonIgnore
    private List<ValidationOffer> validationOfferList;
	
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
    @JsonIgnore
    private List<ValidationRequest> validationRequestList;
	
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
	@JsonIgnore
	private List<NotificationsUser> notificationsUserList;
	
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
	@JsonIgnore
	private List<Message> messageList;
	
	public List<NotificationsUser> getNotificationsUserList() {
        return notificationsUserList;
    }

    public void setNotificationsUserList(List<NotificationsUser> notificationsUserList) {
        this.notificationsUserList = notificationsUserList;
    }
    
    public List<ValidationRequest> getValidationRequestList() {
		return validationRequestList;
	}

	public void setValidationRequestList(List<ValidationRequest> validationRequestList) {
		this.validationRequestList = validationRequestList;
	}

	public User() {
    }

    public List<ValidationOffer> getValidationOfferList() {
		return validationOfferList;
	}


	public void setValidationOfferList(List<ValidationOffer> validationOfferList) {
		this.validationOfferList = validationOfferList;
	}

	public User(String email, String login, String password, Date dateCreation, Boolean isRemove, String nom,
			String prenom, String telephone, String photo, String telephoneFixe, User boss, Groupe groupeIdGroupe,
			Profile profilIdProfil) {
		super();
		this.email = email;
		this.login = login;
		this.password = password;
		this.dateCreation = dateCreation;
		this.isActive = isRemove;
		this.nom = nom;
		this.prenom = prenom;
		this.telephone = telephone;
		this.photo = photo;
		this.telephoneFixe = telephoneFixe;
		this.boss = boss;
		this.groupeIdGroupe = groupeIdGroupe;
		this.profilIdProfil = profilIdProfil;
	}


	public User(Integer idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public Integer getIdUtilisateur() {
        return idUtilisateur;
    }

	public void setIdUtilisateur(Integer idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTelephoneFixe() {
        return telephoneFixe;
    }

    public void setTelephoneFixe(String telephoneFixe) {
        this.telephoneFixe = telephoneFixe;
    }
    
	public User getBoss() {
		return boss;
	}

	public void setBoss(User boss) {
		this.boss = boss;
	}

	@XmlTransient
	public List<User> getBossList() {
		return bossList;
	}

	public void setBossList(List<User> bossList) {
		this.bossList = bossList;
	}

	@XmlTransient
    public List<DocumentsHasUser> getDocumentsHasUserList() {
        return documentsHasUserList;
    }

    public void setDocumentsHasUserList(List<DocumentsHasUser> documentsHasUserList) {
        this.documentsHasUserList = documentsHasUserList;
    }

    @XmlTransient
    public List<Session> getSessionList() {
        return sessionList;
    }

    public void setSessionList(List<Session> sessionList) {
        this.sessionList = sessionList;
    }

    /*@XmlTransient
    public List<UserHasMessage> getUserHasMessageList() {
        return userHasMessageList;
    }

    public void setUserHasMessageList(List<UserHasMessage> userHasMessageList) {
        this.userHasMessageList = userHasMessageList;
    }*/

  
    public Groupe getGroupeIdGroupe() {
        return groupeIdGroupe;
    }

    public void setGroupeIdGroupe(Groupe groupeIdGroupe) {
        this.groupeIdGroupe = groupeIdGroupe;
    }

    public Profile getProfilIdProfil() {
        return profilIdProfil;
    }

    public void setProfilIdProfil(Profile profilIdProfil) {
        this.profilIdProfil = profilIdProfil;
    }
    @XmlTransient
    public List<Notifications> getNotificationsList() {
        return notificationsList;
    }

    public void setNotificationsList(List<Notifications> notificationsList) {
        this.notificationsList = notificationsList;
    }

    public List<Message> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<Message> messageList) {
		this.messageList = messageList;
	}

	@Transient
    public String getDiscriminatorValue() {
    	DiscriminatorValue type = this.getClass().getAnnotation(DiscriminatorValue.class);
    	return type == null ? null : type.value();
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUtilisateur != null ? idUtilisateur.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.idUtilisateur == null && other.idUtilisateur != null) || (this.idUtilisateur != null && !this.idUtilisateur.equals(other.idUtilisateur))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qualshore.etreasury.entity.User[ idUtilisateur=" + idUtilisateur + " ]";
    }
    
}
