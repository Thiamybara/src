package com.qualshore.etreasury.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.qualshore.etreasury.entity.Bank;
import com.qualshore.etreasury.entity.Institution;
import com.qualshore.etreasury.entity.Products;
import com.qualshore.etreasury.entity.Request;
import com.qualshore.etreasury.entity.RequestHasBank;
import com.qualshore.etreasury.entity.RequestHasBankPK;

public interface RequestHasBankRepository extends JpaRepository<RequestHasBank, Integer>{

	List<RequestHasBank> findByRequest(Request request);
	
	RequestHasBank findByBankAndRequest(Institution bank, Request request);
	
	RequestHasBank findByRequestHasBankPK(RequestHasBankPK req);
	
	@Query("SELECT rHB FROM RequestHasBank rHB WHERE  rHB.request.product= ?1 "
			+ "AND rHB.bank= ?2")
	List<RequestHasBank> findRequestHasBankByProductBank(Products pr, Bank b);
	
	@Query("SELECT rHB FROM RequestHasBank rHB WHERE  rHB.request.product= ?1 "
			+ "AND rHB.bank= ?2 AND rHB.request.isValid= 1")
	List<RequestHasBank> findRequestHasBankByProductAndBankAndIsValid(Products pr, Bank b);
	
	/*@Query("SELECT rHB FROM RequestHasBank rHB, Offer o WHERE rHB.request=o.demandeIdDemande AND o.userBanqueIdUserBanque.groupeIdGroupe.institution=rHB.bank AND rHB.request.product= ?1 "
			+ "AND rHB.bank= ?2 AND rHB.request.isValid= 1")
	List<RequestHasBank> findRequestHasBankByProductAndBankAndIsValid(Products pr, Bank b);*/
	
	@Query("SELECT rHB FROM RequestHasBank rHB WHERE rHB.request.isValid= 1 AND rHB.bank= ?1")
	List<RequestHasBank> findRequestHasBankByBankAndIsValid(Bank b);
	
	/*@Query("SELECT rHB FROM RequestHasBank rHB WHERE  rHB.request.product= ?1 "
			+ "AND rHB.bank= ?2")
	List<RequestHasBank> findRequestHasBankByProductBankAndIsValid(Products pr, Bank b);*/
}
