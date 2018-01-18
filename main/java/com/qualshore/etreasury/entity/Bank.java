package com.qualshore.etreasury.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qualshore.etreasury.listener.BankListener;
import com.qualshore.etreasury.listener.InstitutionListener;

/**
 *
 * @author macbookpro
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@EntityListeners(BankListener.class)
@DiscriminatorValue("BA")
public class Bank extends Institution {
    private static final long serialVersionUID = 1L;
    
    @OneToMany(mappedBy = "bank")
    @JsonIgnore
    private List<RequestHasBank> requestHasBankList = new ArrayList<>();
    
    @OneToMany(mappedBy="bank", cascade= CascadeType.ALL, fetch= FetchType.LAZY)
    @JsonIgnore
    private List<BankCondition> bankConditions;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bank", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<RateDay> rateDayList;

	public Bank() {
    }
 
    public Bank(String nom, Date dateCreation, String nombreAgence, String telephone1, String telephone2,
			String capital, Locality locality) {
		super(nom, dateCreation, nombreAgence, telephone1, telephone2, capital, locality,false);
	}

	public List<RateDay> getRateDayList() {
		return rateDayList;
	}

	public void setRateDayList(List<RateDay> rateDayList) {
		this.rateDayList = rateDayList;
	}

	public List<RequestHasBank> getRequestHasBankList() {
		return requestHasBankList;
	}

	public void setRequestHasBankList(List<RequestHasBank> requestHasBankList) {
		this.requestHasBankList = requestHasBankList;
	}

	public List<BankCondition> getBankConditions() {
		return bankConditions;
	}

	public void setBankConditions(List<BankCondition> bankConditions) {
		this.bankConditions = bankConditions;
	}
}