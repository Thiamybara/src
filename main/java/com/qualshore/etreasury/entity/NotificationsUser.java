package com.qualshore.etreasury.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qualshore.etreasury.configure.DateAttributConverter;

@Entity
@Table(name = "notifications_user")
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@XmlRootElement
public class NotificationsUser implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_notifications_user")
	private Integer idNotificationsUser;
	
	@Column(name = "type")
	private Integer type;
	
	@Column(name = "nom_institution")
	private String nomInstitution;
	
	@Column(name = "id_request")
	private Integer idRequest;
	
	@Column(name = "id_offer")
	private Integer idOffer;
	
	@Column(name = "id_document")
	private Integer idDocument;
	
	public Integer getIdDocument() {
		return idDocument;
	}

	public void setIdDocument(Integer idDocument) {
		this.idDocument = idDocument;
	}

	@Column(name = "date_notification")
    @Temporal(TemporalType.TIMESTAMP)
   // @Convert(convernter = DateAttributConverter.class)
    private Date dateNotification;
	
	public Date getDateNotification() {
		return dateNotification;
	}

	public void setDateNotification(Date dateNotification) {
		this.dateNotification = dateNotification;
	}

	@JoinColumn(name = "user", referencedColumnName = "id_utilisateur")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;
	
	public NotificationsUser() {
	}

	public Integer getIdNotificationsUser() {
		return idNotificationsUser;
	}

	public void setIdNotificationsUser(Integer idNotificationsUser) {
		this.idNotificationsUser = idNotificationsUser;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getNomInstitution() {
		return nomInstitution;
	}

	public void setNomInstitution(String nomInstitution) {
		this.nomInstitution = nomInstitution;
	}

	public Integer getIdRequest() {
		return idRequest;
	}

	public void setIdRequest(Integer idRequest) {
		this.idRequest = idRequest;
	}

	public Integer getIdOffer() {
		return idOffer;
	}

	public void setIdOffer(Integer idOffer) {
		this.idOffer = idOffer;
	}

	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}