package com.qualshore.etreasury.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class ValidationLevelGroupePK implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Basic(optional = false)
    @NotNull
	@Column(name="groupe")
	private Integer groupeId;
	
	@Basic(optional = false)
    @NotNull
	@Column(name="validation_level")
	private Integer idNiveauValidation;
	
	
	public ValidationLevelGroupePK() {
	}

	public ValidationLevelGroupePK(Integer idNiveauValidation, Integer groupeId) {
		this.idNiveauValidation = idNiveauValidation;
		this.groupeId = groupeId;
	}
	
	public Integer getGroupeId() {
		return groupeId;
	}
	
	public void setGroupeId(Integer groupeId) {
		this.groupeId = groupeId;
	}
	
	public Integer getIdNiveauValidation() {
		return idNiveauValidation;
	}
	
	public void setIdNiveauValidation(Integer idNiveauValidation) {
		this.idNiveauValidation = idNiveauValidation;
	}
}
