package com.qualshore.etreasury.model;

import java.io.Serializable;

public class NotificationsModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	
	private Integer[] idsUser;
	public Integer[] getIdsUser() {
		return idsUser;
	}

	public void setIdsUser(Integer[] idsUser) {
		this.idsUser = idsUser;
	}

	public Integer getIdProduct() {
		return idProduct;
	}

	public void setIdProduct(Integer idProduct) {
		this.idProduct = idProduct;
	}

	private Integer idProduct;

	public NotificationsModel() {
	}

}
