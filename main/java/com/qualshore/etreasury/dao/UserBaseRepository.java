package com.qualshore.etreasury.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import com.qualshore.etreasury.entity.Groupe;
import com.qualshore.etreasury.entity.User;

@NoRepositoryBean
public interface UserBaseRepository<T extends User> extends JpaRepository<T, Integer> {

	public List<User> findByIdUtilisateur(Integer id);
	
	public User findByLogin(String login);
	
	public List<User> findByEmail(String email);
	
	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM User u WHERE u.login= ?1")
	public boolean existsByLogin(String login);
	
	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM User u WHERE u.login= ?1 AND u.idUtilisateur <> ?2")
	public boolean existsByLoginOthers(String login, Integer idUser);
	
	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM User u WHERE u.login= ?1 AND u.password= ?2")
	public boolean existsByLoginAndPassword(String username, String password);
	
	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM User u WHERE u.password= ?1")
	public boolean existsByPassword(String password);
	
	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM User u WHERE u.email= ?1")
	public boolean existsByEmail(String email);
	
	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM User u WHERE u.email= ?1 AND u.idUtilisateur <> ?2")
	public boolean existsByEmailOthers(String email, Integer idUser);
	
	@Modifying
	@Transactional
	@Query("UPDATE User u SET u.groupeIdGroupe = ?1 WHERE u.idUtilisateur = ?2")
	public int updateGroup(Groupe groupeIdGroupe, Integer idUtilisateur);
	
	@Query("SELECT CASE WHEN COUNT(boss) > 0 THEN 'true' ELSE 'false' END FROM User boss, Groupe g1, Groupe g2 WHERE boss.groupeIdGroupe= g1.idGroupe AND g1.institution=g2.institution AND boss= ?1 AND g2= ?2")
	public boolean hasSameInstitution(User boss, Groupe groupe);
}
