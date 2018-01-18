package com.qualshore.etreasury.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qualshore.etreasury.entity.Documents;
import com.qualshore.etreasury.entity.DocumentsHasUser;
import com.qualshore.etreasury.entity.Institution;
import com.qualshore.etreasury.entity.User;

public interface DocumentsHasUserRepository extends JpaRepository<DocumentsHasUser, Integer> {

	List<DocumentsHasUser> findByUser(User user);
	
	@Modifying
	@Transactional
	public void deleteByDocuments(Documents document);
	
	public boolean existsByDocumentsAndUser(Documents document, User user);
	
	List<DocumentsHasUser> findByDocuments(Documents document);
	
	@Query("SELECT dhu FROM DocumentsHasUser dhu WHERE dhu.documents= ?1 AND dhu.user.groupeIdGroupe.institution= ?2")
	List<DocumentsHasUser> findByDocumentAndInstitution(Documents document, Institution institution);
	
	@Query("SELECT u FROM User u, DocumentsHasUser dhu WHERE u.idUtilisateur=dhu.user AND dhu.documents= ?1 AND dhu.user.groupeIdGroupe.institution= ?2")
	List<User> findUserByDocumentAndInstitution(Documents document, Institution institution);
}
