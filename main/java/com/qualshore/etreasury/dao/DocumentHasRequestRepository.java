package com.qualshore.etreasury.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.qualshore.etreasury.entity.Documents;
import com.qualshore.etreasury.entity.DocumentsHasRequest;
import com.qualshore.etreasury.entity.Request;

public interface DocumentHasRequestRepository extends JpaRepository<DocumentsHasRequest, Integer> {

	@Query("SELECT dr FROM DocumentsHasRequest dr WHERE dr.request = ?1")
	public List<DocumentsHasRequest> findAllDocumentByRequest(Request request);
	
	@Query("SELECT dr FROM DocumentsHasRequest dr WHERE dr.request = ?1 AND dr.documents= ?2")
	public List<DocumentsHasRequest> findByDocumentRequest(Request request, Documents document);
}
