package com.qualshore.etreasury.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.qualshore.etreasury.entity.Groupe;
import com.qualshore.etreasury.entity.Institution;
import com.qualshore.etreasury.entity.Notifications;
import com.qualshore.etreasury.entity.NotificationsPK;
import com.qualshore.etreasury.entity.Offer;
import com.qualshore.etreasury.entity.Products;
import com.qualshore.etreasury.entity.Request;
import com.qualshore.etreasury.entity.User;

public interface NotificationsRepository extends JpaRepository<Notifications, Integer> {
	
	List<Notifications> findByInstitutionAndProducts(Institution institution, Products product);
	
	Notifications findByNotificationsPK(NotificationsPK i);

	List<Notifications> findByInstitution(Institution institution);
	
	@Query("SELECT n FROM Notifications n WHERE n.user.groupeIdGroupe.institution = ?1")
	List<Notifications> findByInstitutionUser(Institution institution);
	
	@Query("SELECT n FROM Notifications n WHERE n.notificationsPK.institution = ?1 AND n.notificationsPK.product = ?2 AND n.notificationsPK.user = ?3")
	Notifications findByNotification( Integer i, Integer p, Integer  u);

	//@Query("SELECT n FROM Notifications n WHERE n.user.groupeIdGroupe.institution = ?1 AND n.notificationsPK.product = ?2 AND n.notificationsPK.institution= ?3")
	@Query("SELECT n FROM Notifications n WHERE  n.notificationsPK.product = ?1 AND n.notificationsPK.institution= ?2")
	List<Notifications> findByBankAndProductAndEnterprise(Integer p, Integer idEnterprise);
	
	
	@Query("SELECT n FROM Notifications n WHERE  n.notificationsPK.product = ?1 AND n.notificationsPK.institution= ?2 AND n.user.groupeIdGroupe.institution = n.notificationsPK.institution ")
	List<Notifications> findByOwnProductAndEnterprise(Integer p, Integer idEnterprise);
	
	@Query("SELECT n FROM Notifications n WHERE n.user.groupeIdGroupe.institution = ?1 AND n.notificationsPK.product = ?2 AND n.notificationsPK.institution= ?3")
	List<Notifications> findByBankAndProductAndEnterprise(Institution i, Integer p, Integer idEnterprise);
	
	Notifications findByProductsAndInstitutionAndUser(Products product, Institution institution, User userOld);
	
	
	//public Boolean existByInstitutionAndProductAndUser(Institution i, Products p,User u);
	/*
	@Query("SELECT n FROM Notifications n, User u, Groupe g WHERE u.groupeIdGroupe=g.idGroupe AND n.notificationsPK.product = ?2")
	public List<Notifications> findByProductAndBank(Integer groupe, Products product);
	*/
}
