package com.qualshore.etreasury.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "devise")
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@XmlRootElement
public class Devise implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_devise")
    private Integer idDevise;
    
    @Column(name = "description")
    private String description;

    @Column(name = "valeur")
    private Float valeur;
    
    @OneToMany(mappedBy = "devise", fetch = FetchType.LAZY, cascade= CascadeType.REMOVE)
    @JsonIgnore
    private List<Request> requestList;
    
    @OneToMany(mappedBy = "devise", fetch = FetchType.LAZY, cascade= CascadeType.REMOVE)
    @JsonIgnore
    private List<ValidationLevelGroupe> validationLevelGroupList;
    
    @OneToMany(mappedBy = "devise", fetch = FetchType.LAZY, cascade= CascadeType.REMOVE)
    @JsonIgnore
    private List<RateDay> rateDays;
    
    public Devise() {
		super();
	}
    
	public Devise(Integer idDevise) {
		super();
		this.idDevise = idDevise;
	}

	public Devise(Integer idDevise, String description, Float valeur, List<Request> requestList,
			List<ValidationLevelGroupe> validationLevelGroupList, List<RateDay> rateDays, Date dateDevise) {
		super();
		this.idDevise = idDevise;
		this.description = description;
		this.valeur = valeur;
		this.requestList = requestList;
		this.validationLevelGroupList = validationLevelGroupList;
		this.rateDays = rateDays;
		this.dateDevise = dateDevise;
	}

	public List<RateDay> getRateDays() {
		return rateDays;
	}

	public void setRateDays(List<RateDay> rateDays) {
		this.rateDays = rateDays;
	}

	public Float getValeur() {
		return valeur;
	}

	public void setValeur(Float valeur) {
		this.valeur = valeur;
	}

	@Column(name = "date_devise")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDevise;
    
	public Date getDateDevise() {
		return dateDevise;
	}

	public void setDateDevise(Date dateDevise) {
		this.dateDevise = dateDevise;
	}

	public Integer getIdDevise() {
		return idDevise;
	}

	public void setIdDevise(Integer idDevise) {
		this.idDevise = idDevise;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<Request> getRequestList() {
		return requestList;
	}

	public void setRequestList(List<Request> requestList) {
		this.requestList = requestList;
	}

	public List<ValidationLevelGroupe> getValidationLevelGroupList() {
		return validationLevelGroupList;
	}

	public void setValidationLevelGroupList(List<ValidationLevelGroupe> validationLevelGroupList) {
		this.validationLevelGroupList = validationLevelGroupList;
	}

	@Override
	    public int hashCode() {
	        int hash = 0;
	        hash += (idDevise != null ? idDevise.hashCode() : 0);
	        return hash;
	    }

	    @Override
	    public boolean equals(Object object) {
	        // TODO: Warning - this method won't work in the case the id fields are not set
	        if (!(object instanceof Devise)) {
	            return false;
	        }
	        Devise other = (Devise) object;
	        if ((this.idDevise == null && other.idDevise != null) || (this.idDevise != null && !this.idDevise.equals(other.idDevise))) {
	            return false;
	        }
	        return true;
	    }

	    @Override
	    public String toString() {
	        return "com.qualshore.etreasury.entity.Devise[ idDevise=" + idDevise + " ]";
	    }

   
}
