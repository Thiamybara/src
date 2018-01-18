package com.qualshore.etreasury.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qualshore.etreasury.entity.MessageType;

public interface MessageTypeRepository extends JpaRepository<MessageType, Integer> {

	public List<MessageType> findByIdTypeMessage(@Param("idTypeMessage") Integer idTypeMessage);
	
	@Query("SELECT CASE WHEN COUNT(mt) > 0 THEN 'true' ELSE 'false' END FROM MessageType mt WHERE mt.idTypeMessage = ?1")
	public boolean existsByIdTypeMessage(Integer idTypeMessage);
	
	@Modifying
	@Transactional
	@Query("UPDATE MessageType mt SET mt.libelleType = ?1 , mt.description = ?2 WHERE mt.idTypeMessage= ?3")
	public int updateMessageType(String libelleType, String description, Integer idTypeMessage);
}
