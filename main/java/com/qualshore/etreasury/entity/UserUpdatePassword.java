package com.qualshore.etreasury.entity;

public class UserUpdatePassword {
	
	public UserUpdatePassword() {}
	private Integer idUser;
	public Integer getIdUser() {
		return idUser;
	}
	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	private String password;
	private String newPassword;
	
}