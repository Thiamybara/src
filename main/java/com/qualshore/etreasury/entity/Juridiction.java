package com.qualshore.etreasury.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = false)
public class Juridiction implements Serializable{
	
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private String libelle;
	private String description;
	private String produitName;
	private String groupage;
	

	@OneToMany(mappedBy="juridiction")
	@JsonIgnore
    private List<JuridictionGroupe> juridictionGroupes = new ArrayList<>();
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public String getGroupage() {
		return groupage;
	}

	public void setGroupage(String groupage) {
		this.groupage = groupage;
	}
	public List<JuridictionGroupe> getJuridictionGroupes() {
		return juridictionGroupes;
	}
	
	public String getProduitName() {
		return produitName;
	}

	public void setProduitName(String produitName) {
		this.produitName = produitName;
	}
}
