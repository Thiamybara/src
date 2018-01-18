package com.qualshore.etreasury.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "validation_level_groupe")
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@XmlRootElement

public class ValidationLevelGroupe implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	protected ValidationLevelGroupePK validationLevelGroupePK;
	
	@JoinColumn(name = "groupe", referencedColumnName = "id_groupe", insertable = false,
    		updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Groupe groupe;

	@JoinColumn(name = "validation_level", referencedColumnName = "id_niveau_validation", insertable = false,
    		updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
	private ValidationLevel validationLevel;

    @Column(name = "valeur_min")
    private String valeurMin;
  
    @Column(name = "valeur_max")
    private String valeurMax;
	
    @Column(name = "niveau")
    private int niveau;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "devise")
    private Devise devise;

	public ValidationLevelGroupe() {
	}
	
	public ValidationLevelGroupePK getValidationLevelGroupePK() {
		return validationLevelGroupePK;
	}

	public void setValidationLevelGroupePK(ValidationLevelGroupePK validationLevelGroupePK) {
		this.validationLevelGroupePK = validationLevelGroupePK;
	}

	public Groupe getGroupe() {
		return groupe;
	}

	public void setGroupe(Groupe groupe) {
		this.groupe = groupe;
	}

	public ValidationLevel getValidationLevel() {
		return validationLevel;
	}

	public void setValidationLevel(ValidationLevel validationLevel) {
		this.validationLevel = validationLevel;
	}

	public String getValeurMin() {
		return valeurMin;
	}

	public void setValeurMin(String valeurMin) {
		this.valeurMin = valeurMin;
	}

	public String getValeurMax() {
		return valeurMax;
	}

	public void setValeurMax(String valeurMax) {
		this.valeurMax = valeurMax;
	}

	public int getNiveau() {
		return niveau;
	}

	public void setNiveau(int niveau) {
		this.niveau = niveau;
	}

	public Devise getDevise() {
		return devise;
	}

	public void setDevise(Devise devise) {
		this.devise = devise;
	}		
}
