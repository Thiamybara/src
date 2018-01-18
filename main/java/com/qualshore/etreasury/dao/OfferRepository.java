package com.qualshore.etreasury.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.qualshore.etreasury.entity.Institution;
import com.qualshore.etreasury.entity.Offer;
import com.qualshore.etreasury.entity.Products;
import com.qualshore.etreasury.entity.Request;
import com.qualshore.etreasury.entity.User;

public interface OfferRepository extends JpaRepository<Offer, Integer> {
	
	List<Offer> findByUserBanqueIdUserBanque(User user);
	
	List<Offer> findByDemandeIdDemande(Request request);
	
	List<Offer> findByDemandeIdDemandeAndIsValid(Request request, boolean isValid);
	
	public boolean existsByDemandeIdDemande(Request request);
	
	@Query("SELECT o FROM Offer o, User u, Groupe g WHERE o.userBanqueIdUserBanque=u.idUtilisateur AND u.groupeIdGroupe=g.idGroupe AND g.institution= ?1 AND o.demandeIdDemande= ?2")
	public List<Offer> findByInstitutionAndDemande(Institution institution, Request request);
	
	@Query("SELECT o FROM Offer o, User u, Groupe g WHERE o.userBanqueIdUserBanque=u.idUtilisateur AND u.groupeIdGroupe=g.idGroupe AND g.institution= ?1")
	List<Offer> findByBank(Institution bank);
	
	@Query("SELECT o FROM Offer o WHERE o.demandeIdDemande.etat= 4 AND o.etat =2 AND o.demandeIdDemande.userEntreprise.groupeIdGroupe.institution= ?1")
	List<Offer> findByEnterpriseAndPreSelection(Institution enterprise);
	
	@Query("SELECT o FROM Offer o, User u WHERE o.demandeIdDemande.userEntreprise=u.idUtilisateur AND u=?2 AND o.demandeIdDemande.etat >=2  AND u.groupeIdGroupe.institution=?1  ")
	List<Offer> findByEnterpriseAndIsValid(Institution enterprise, User user);
	/*
	@Query("SELECT o FROM Offer o, User u, Groupe g WHERE o.userBanqueIdUserBanque=u.idUtilisateur AND u.groupeIdGroupe=g.idGroupe AND g.institution= ?1 AND o.demandeIdDemande= ?2")
    boolean existsByRequestAndInstitution(Institution institution, Request request);
	*/
	@Query("SELECT CASE WHEN COUNT(o) > 0 THEN 'true' ELSE 'false' END FROM Offer o, User u, Groupe g WHERE o.userBanqueIdUserBanque=u.idUtilisateur AND u.groupeIdGroupe=g.idGroupe AND g.institution= ?1 AND o.demandeIdDemande= ?2")
    public boolean existsByRequestAndInstitution(Institution institution, Request request);
	
	@Query("SELECT CASE WHEN COUNT(o) > 0 THEN 'true' ELSE 'false' END FROM Offer o, User u, Groupe g, Products p, Request r WHERE o.userBanqueIdUserBanque=u.idUtilisateur AND u.groupeIdGroupe=g.idGroupe AND r.product=p.idProduits AND r.idDemande=o.demandeIdDemande AND g.institution= ?2 AND o.demandeIdDemande= ?1 AND p= ?3")
	public boolean existsByRequestAndInstitutionAndProduct(Request request, Institution institution, Products product);
}