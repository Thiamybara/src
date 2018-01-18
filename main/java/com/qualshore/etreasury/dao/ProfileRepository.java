package com.qualshore.etreasury.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qualshore.etreasury.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile,Integer> {

	public List<Profile> findByIdProfil(@Param("idProfil") Integer idProfile);
	
	@Query("SELECT CASE WHEN COUNT(p) > 0 THEN 'true' ELSE 'false' END FROM Profile p WHERE p.idProfil = ?1")
	public boolean existsByIdProfile(Integer idProfile);
	
	@Modifying
	@Transactional
	@Query("UPDATE Profile p SET p.type = ?1 , p.description = ?2 WHERE p.idProfil= ?3")
	public int updateProfile(String type, String description, Integer idProfil);
	
	public List<Profile> findByType(@Param("type") String nom);
}
