package com.qualshore.etreasury.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qualshore.etreasury.entity.Request;
import com.qualshore.etreasury.entity.ValidationRequest;

public interface ValidationRequestRepository extends JpaRepository<ValidationRequest, Integer> {

	ValidationRequest findByRequestAndNiveau(Request request, Integer niveau);
	
	public boolean existsByRequestAndNiveau(Request request, Integer niveau);
	
	public boolean existsByRequest(Request request);
}
