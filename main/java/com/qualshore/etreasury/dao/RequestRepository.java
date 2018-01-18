
package com.qualshore.etreasury.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.qualshore.etreasury.entity.Enterprise;
import com.qualshore.etreasury.entity.Institution;
import com.qualshore.etreasury.entity.Products;
import com.qualshore.etreasury.entity.Request;
import com.qualshore.etreasury.entity.UserEntreprise;

public interface RequestRepository extends JpaRepository<Request, Integer>{
	
	List<Request> findByUserEntreprise(UserEntreprise user);
	
	@Query("SELECT r FROM Request r WHERE r.userEntreprise.groupeIdGroupe.institution = ?1 ")
	List<Request> findByEnterprise(Enterprise e);
	
	@Query("SELECT r FROM Request r, UserEntreprise e, Groupe g  WHERE r.userEntreprise= e.idUtilisateur"
            + " AND e.groupeIdGroupe = g.idGroupe" + " AND g.institution = ?1 AND r.product = ?2 ")
    List<Request> findEnterpriseProduct(Enterprise i, Products p);
	
	@Query("SELECT r FROM Request r, RequestHasBank rhb WHERE rhb.request= r.idDemande AND rhb.bank= ?1")
	   List<Request> findByBank(Institution bank);
	
	@Query("SELECT r FROM Request r where r.userEntreprise.groupeIdGroupe.institution = ?1 AND"
            + "  r.isValid = ?2 ")
    List<Request> findNotValidateRequest(Institution i, boolean b);
	
	@Query("SELECT r FROM Request r WHERE r.etat = 0 AND r.userEntreprise.groupeIdGroupe.institution = ?1")
	List<Request> findByEnterpriseAndNotValid(Institution e);
}
