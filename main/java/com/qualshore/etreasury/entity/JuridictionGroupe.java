package com.qualshore.etreasury.entity;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@IdClass(JuridictionGroupePK.class)
public class JuridictionGroupe {
	
	 @Id
	 private int groupeId;
	 @Id
	 private int juridictionId;
	
	@ManyToOne
	@JoinColumn(name="groupe_id", insertable = false, updatable = false, referencedColumnName="id_groupe")
	private Groupe groupe;

	@ManyToOne
	@JoinColumn(name="juridiction_id", insertable = false, updatable = false, referencedColumnName="id")
	private Juridiction juridiction;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreation;

	public Groupe getGroupe() {
		return groupe;
	}

	public void setGroupe(Groupe groupe) {
		this.groupe = groupe;
	}

	public Juridiction getJuridiction() {
		return juridiction;
	}

	public void setJuridiction(Juridiction juridiction) {
		this.juridiction = juridiction;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}	
	
	public void setGroupeId(int groupeId) {
		this.groupeId = groupeId;
	}
	public int getGroupeId() {
		return groupeId;
	}

	public int getJuridictionId() {
		return juridictionId;
	}

	public void setJuridictionId(int juridictionId) {
		this.juridictionId = juridictionId;
	}
}
