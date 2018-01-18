package com.qualshore.etreasury.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.qualshore.etreasury.entity.Session;
import com.qualshore.etreasury.entity.User;

public interface SessionRepository extends CrudRepository<Session, Integer> {
	
	//public void addSession(Session session);
	
	public List<Session> findByIdSession(@Param("idSession") Integer idSession);
	
	//@Query("SELECT s FROM Session s, User u WHERE s.userIdUtilisateur = u.idUtilisateur AND u.idUtilisateur = ?1 ORDER BY date DESC")
	public List<Session> findByUserIdUtilisateurOrderByIdSessionDesc(User idutilisateur);
	
	@Modifying
	@Transactional
	@Query("UPDATE Session s SET s.compteur= ?1 WHERE s.idSession= ?2")
	public int updateCompteur(int compteur, int idSession);
	
	@Query("SELECT CASE WHEN COUNT(s) > 0 THEN 'true' ELSE 'false' END FROM Session s WHERE s.idSession= ?1")
	public boolean existsByIdSession(int idSession);
}