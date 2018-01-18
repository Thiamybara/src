package com.qualshore.etreasury.model;

import java.io.Serializable;

public class SearchModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private String category;
	private String keyWord;
	private String date;
	
	public SearchModel() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
}
