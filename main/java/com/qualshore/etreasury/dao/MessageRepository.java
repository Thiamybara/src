package com.qualshore.etreasury.dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qualshore.etreasury.entity.Message;
import com.qualshore.etreasury.entity.MessageType;

public interface MessageRepository extends JpaRepository<Message, Integer> {

	public List<MessageType> findByIdMessage(@Param("idMessage") Integer idMessage);
	
	@Query("SELECT CASE WHEN COUNT(m) > 0 THEN 'true' ELSE 'false' END FROM Message m WHERE m.idMessage = ?1")
	public boolean existsByIdMessage(Integer idTypeMessage);
	
	@Modifying
	@Transactional
	@Query("UPDATE Message m SET m.dateMessage = ?1 , m.contenuMessage = ?2 , m.isRattachedFile = ?3 , m.repertoireFile = ?4 , m.isVisible = ?5 ,"
			+ "m.isAccept = ?6 , m.isGroup = ?7 , m.isConnected = ?8 , m.messagecol = ?9 , m.typeMessageIdTypeMessage = ?10 WHERE m.idMessage= ?11")
	public int updateMessageType(Date dateMessage, String contenuMessage, Boolean isRattachedFile,
			String repertoireFile, Boolean isVisible, Boolean isAccept, Boolean isGroup, Boolean isConnected,
			String messagecol, MessageType typeMessageIdTypeMessage, Integer idMessage);
}
