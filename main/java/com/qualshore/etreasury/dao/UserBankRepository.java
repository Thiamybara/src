package com.qualshore.etreasury.dao;

import org.springframework.data.jpa.repository.Query;

import com.qualshore.etreasury.entity.Bank;
import com.qualshore.etreasury.entity.UserBanque;

public interface UserBankRepository extends UserBaseRepository<UserBanque> {

	@Query("SELECT g.institution FROM Groupe g, User u WHERE u.groupeIdGroupe = g.idGroupe AND u.idUtilisateur = ?1")
	public Bank getBankByUser(Integer idUser);
}
