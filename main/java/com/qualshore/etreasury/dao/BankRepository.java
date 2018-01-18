
package com.qualshore.etreasury.dao;


import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.qualshore.etreasury.entity.Bank;
import com.qualshore.etreasury.entity.Locality;
import com.qualshore.etreasury.entity.Request;

public interface BankRepository extends InstitutionBaseRepository<Bank>{

	List<Bank> findByLocalityIdLocalite(Locality loc);
	
	  @Query("SELECT b FROM Bank b, RequestHasBank rb WHERE rb.bank=b.idInstitution AND rb.request = ?1")
	   public List<Bank> getRequestBanks(Request request);
	  /*
	  @Query("SELECT CASE WHEN COUNT(i) > 0 THEN 'true' ELSE 'false' END FROM Institution i WHERE i.nom= ?1 AND i.localityIdLocalite = ?2 AND i.type like 'BA' ")
		public boolean exitsByIdInstitutionName(String nomInstitution, Integer locality);
		*/
}
