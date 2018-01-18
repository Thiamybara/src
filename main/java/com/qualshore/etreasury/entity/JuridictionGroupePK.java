package com.qualshore.etreasury.entity;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class JuridictionGroupePK implements Serializable{
	
	@Column(name = "groupe_id")
	private int groupeId;
	@Column(name="juridiction_id")
	private int juridictionId;
	
	public int getGroupeId() {
		return groupeId;
	}
	public void setGroupeId(int groupeId) {
		this.groupeId = groupeId;
	}
	public int getJuridictionId() {
		return juridictionId;
	}
	public void setJuridictionId(int juridictionId) {
		this.juridictionId = juridictionId;
	}
	
	@Override
	public int hashCode() {
		return (int) (juridictionId + groupeId);
	}
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof JuridictionGroupePK ) {
			JuridictionGroupePK otherPK = (JuridictionGroupePK) obj;
			return (otherPK.juridictionId == this.juridictionId) && (otherPK.groupeId == this.groupeId);
		}
		return false;
	}
	
	
}
