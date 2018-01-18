package com.qualshore.etreasury.dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.qualshore.etreasury.entity.Groupe;
import com.qualshore.etreasury.entity.Institution;
import com.qualshore.etreasury.entity.Locality;
import com.qualshore.etreasury.entity.Profile;
import com.qualshore.etreasury.entity.User;

public interface UserRepository extends UserBaseRepository<User> {
	
	@Modifying
	@Transactional
	@Query("UPDATE User u SET u.groupeIdGroupe = ?1 WHERE u.idUtilisateur = ?2")
	public int updateGroup(Groupe groupeIdGroupe, Integer idUtilisateur);
	
	@Modifying
	@Transactional
	@Query("UPDATE User u SET u.groupeIdGroupe = ?1 WHERE u.groupeIdGroupe = ?2")
	public int updateGroupDelete(Groupe newGroupe, Groupe oldGroup);
	
	
	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM User u WHERE u.idUtilisateur= ?1")
	public boolean existsByIdUtilisateur(Integer id);
	
	//@Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM User u WHERE u.idUtilisateur= ?1 AND u.type= ?2")
	//public boolean existsByAdminIdUtilisateur(Integer id, String type);
	
	/*@Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM User u WHERE u.login= ?1")
	public boolean existsByLogin(String login);
	
	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM User u WHERE u.password= ?1")
	public boolean existsByPassword(String password);*/
	
	@Modifying
	@Transactional
	@Query("UPDATE User u SET u.email=?1, u.login=?2, u.password=?3, u.dateCreation=?4, u.isActive=?5, u.nom=?6, u.prenom=?7, u.telephone=?8, u.photo=?9, u.telephoneFixe=?10, u.groupeIdGroupe=?11, profilIdProfil=?12 WHERE u.idUtilisateur= ?13")
	public int updateUser(String email, String login, String password, Date dateCreation, Boolean isRemove, String nom, String prenom,
			String telephone, String photo, String telephoneFixe, Groupe groupeIdGroupe,Profile profilIdProfil, Integer idUtilisateur);
	
	@Query("SELECT u FROM User u WHERE u.groupeIdGroupe.institution = ?1")
	List<User> findUserByINstitution(Institution i);
	
	@Query("SELECT u FROM User u, Institution i WHERE u.profilIdProfil.type = ?1 AND u.groupeIdGroupe.institution = i.idInstitution "
			+ " AND type(i) = 'EN'")
	List <User> findUserAdminByEntreprise(String typeProfile);
	
	@Query("SELECT u FROM User u, Institution i WHERE u.profilIdProfil.type = ?1 AND u.groupeIdGroupe.institution = i.idInstitution "
			+ " AND type(i) = 'BA'")
	List <User> findUserAdminByBank(String typeProfile);
	
	@Query("SELECT u FROM User u, Institution i WHERE u.groupeIdGroupe.institution = i.idInstitution "
			+ " AND type(i) = 'BA'")
	List <User> findUsersBank();
	
	@Query("SELECT u FROM User u, Institution i WHERE u.groupeIdGroupe.institution.localityIdLocalite = ?1 AND u.groupeIdGroupe.institution = i.idInstitution AND type(u) = 'BA'")
	List <User> findUsersContactBank(Locality idLocality );
	
	@Query("SELECT u FROM User u, Institution i WHERE u.groupeIdGroupe.institution.localityIdLocalite = ?1 AND u.groupeIdGroupe.institution = i.idInstitution AND type(u) = 'EN'")
	List <User> findUsersContactEnterprise(Locality idLocality );
	
	@Query("SELECT u FROM User u, Institution i WHERE u.groupeIdGroupe.institution = i.idInstitution "
			+ " AND type(i) = 'EN'")
	List <User> findUsersEntreprise();

	public List<User> findByGroupeIdGroupe(Groupe groupe);
	
	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM User u WHERE u.profilIdProfil.idProfil=1 AND u.idUtilisateur= ?1")
	public boolean isAdminGeneral(Integer idUser);
}
