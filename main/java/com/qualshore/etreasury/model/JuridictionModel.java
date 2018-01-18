package com.qualshore.etreasury.model;

import java.io.Serializable;
import java.util.ArrayList;

public class JuridictionModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	
	public Integer[] getJuridictions() {
		return juridictions;
	}



	public void setJuridictions(Integer[] juridictions) {
		this.juridictions = juridictions;
	}



	private Integer[] juridictions;
	
	
	
	
	
}
