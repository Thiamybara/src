package com.qualshore.etreasury.dao;

import java.util.List;

import com.qualshore.etreasury.entity.UserAdmin;

public interface UserAdminRepository extends UserBaseRepository<UserAdmin> {

	List<UserAdmin> findAllByOrderByIdUtilisateurDesc();
}
