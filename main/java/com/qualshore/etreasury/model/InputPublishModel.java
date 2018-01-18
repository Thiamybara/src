package com.qualshore.etreasury.model;

import com.qualshore.etreasury.entity.Documents;

public class InputPublishModel {

	private Documents document;
	private Integer [] tabUtils;
	private Integer [] tabIns;
	private InsUtilModel [] tabInsUtils;
	
	public InputPublishModel() {
	}

	public Documents getDocument() {
		return document;
	}

	public void setDocument(Documents document) {
		this.document = document;
	}

	public Integer[] getTabUtils() {
		return tabUtils;
	}

	public void setTabUtils(Integer[] tabUtils) {
		this.tabUtils = tabUtils;
	}

	public Integer[] getTabIns() {
		return tabIns;
	}

	public void setTabIns(Integer[] tabIns) {
		this.tabIns = tabIns;
	}

	public InsUtilModel[] getTabInsUtils() {
		return tabInsUtils;
	}

	public void setTabInsUtils(InsUtilModel[] tabInsUtils) {
		this.tabInsUtils = tabInsUtils;
	}
}
