package com.qualshore.etreasury.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.qualshore.etreasury.entity.Bank;
import com.qualshore.etreasury.entity.Commission;

public interface CommissionRepository extends JpaRepository<Commission, Integer> {

	@Query("SELECT c FROM Commission c, RateDay r WHERE c.tauxJourIdTauxJour = r.idTauxJour AND r.bank = ?1")
	public List<Commission> findByBank(Bank idBanque);
}
