package com.qualshore.etreasury.listener;

import javax.persistence.PrePersist;

import com.qualshore.etreasury.entity.User;

public class UserListener {
	
	@PrePersist
	public void userPrePersist(User u) {
		u.setIsActive(true);
	}
}
