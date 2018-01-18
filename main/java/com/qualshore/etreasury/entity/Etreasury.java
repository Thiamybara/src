package com.qualshore.etreasury.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)

@DiscriminatorValue("ET")
public class Etreasury extends Institution {
	private static final long serialVersionUID = 1L;

	public Etreasury() {
	}
}