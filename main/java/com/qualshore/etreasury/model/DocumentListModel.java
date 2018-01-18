package com.qualshore.etreasury.model;

import java.util.Date;
import com.qualshore.etreasury.entity.Category;

public class DocumentListModel {

	private Integer idDocuments;

    private String nom;
    
    private Date dateDocument;
    
    private Date dateChargement;
    
    private Date datePublication;
   
    private String repertoire;
    
    private Boolean isActive;
 
    private String motsCles;

    private String etat;
    
    private Boolean isRemove;
    
    private Boolean isPrivate;
    
    private Integer statusIdStatus;

    private String urlDocument;
    
    private Category category;
    
    private String chaine;
    
	public DocumentListModel() {
	}

	public DocumentListModel(Integer idDocuments, String nom, Date dateDocument, Date dateChargement, Date datePublication, String repertoire,
			Boolean isActive, String motsCles, String etat, Boolean isRemove, Boolean isPrivate, Integer statusIdStatus,
			String urlDocument, Category category) {
		this.idDocuments = idDocuments;
		this.nom = nom;
		this.dateDocument = dateDocument;
		this.dateChargement = dateChargement;
		this.datePublication = datePublication;
		this.repertoire = repertoire;
		this.isActive = isActive;
		this.motsCles = motsCles;
		this.etat = etat;
		this.isRemove = isRemove;
		this.isPrivate = isPrivate;
		this.statusIdStatus = statusIdStatus;
		this.urlDocument = urlDocument;
		this.category = category;
	}

	public Integer getIdDocuments() {
		return idDocuments;
	}

	public void setIdDocuments(Integer idDocuments) {
		this.idDocuments = idDocuments;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Date getDateDocument() {
		return dateDocument;
	}

	public void setDateDocument(Date dateDocument) {
		this.dateDocument = dateDocument;
	}

	public Date getDateChargement() {
		return dateChargement;
	}

	public void setDateChargement(Date dateChargement) {
		this.dateChargement = dateChargement;
	}

	public Date getDatePublication() {
		return datePublication;
	}

	public void setDatePublication(Date datePublication) {
		this.datePublication = datePublication;
	}

	public String getRepertoire() {
		return repertoire;
	}

	public void setRepertoire(String repertoire) {
		this.repertoire = repertoire;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public Boolean getIsRemove() {
		return isRemove;
	}

	public void setIsRemove(Boolean isRemove) {
		this.isRemove = isRemove;
	}

	public Boolean getIsPrivate() {
		return isPrivate;
	}

	public void setIsPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public Integer getStatusIdStatus() {
		return statusIdStatus;
	}

	public void setStatusIdStatus(Integer statusIdStatus) {
		this.statusIdStatus = statusIdStatus;
	}

	public String getUrlDocument() {
		return urlDocument;
	}

	public void setUrlDocument(String urlDocument) {
		this.urlDocument = urlDocument;
	}

	public String getMotsCles() {
		return motsCles;
	}

	public void setMotsCles(String motsCles) {
		this.motsCles = motsCles;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getChaine() {
		return chaine;
	}

	public void setChaine(String chaine) {
		this.chaine = chaine;
	}
}
