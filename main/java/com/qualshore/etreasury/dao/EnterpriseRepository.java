package com.qualshore.etreasury.dao;

import java.util.List;

import com.qualshore.etreasury.entity.Bank;
import com.qualshore.etreasury.entity.Enterprise;
import com.qualshore.etreasury.entity.Locality;

public interface EnterpriseRepository extends InstitutionBaseRepository<Enterprise>{
	
	List<Enterprise> findByLocalityIdLocalite(Locality loc);

}
