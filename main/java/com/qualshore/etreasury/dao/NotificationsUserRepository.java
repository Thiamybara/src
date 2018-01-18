package com.qualshore.etreasury.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.qualshore.etreasury.entity.NotificationsUser;
import com.qualshore.etreasury.entity.User;

public interface NotificationsUserRepository extends JpaRepository<NotificationsUser, Integer> {

	//@Query("DELETE FROM NotificationsUser nu WHERE nu.user=?1 AND nu.idRequest=?2")
	@Modifying
	@Transactional
	public void deleteByUserAndIdRequest(User user, Integer idRequest);
	
	//@Query("DELETE FROM NotificationsUser nu WHERE nu.user=?1 AND nu.idOffer=?2")
	@Modifying
	@Transactional
	public void deleteByUserAndIdOffer(User user, Integer idRequest);
	
	//@Query("DELETE FROM NotificationsUser nu WHERE nu.user=?1 AND nu.idRequest=?2 AND nu.type=?2")
	@Modifying
	@Transactional
	public void deleteByUserAndIdRequestAndType(User user, Integer idRequest, Integer type);
	
	//@Query("DELETE FROM NotificationsUser nu WHERE nu.user=?1 AND nu.idOffer=?2 AND nu.type=?2")
	@Modifying
	@Transactional
	public void deleteByUserAndIdOfferAndType(User user, Integer idRequest, Integer type);
	
	@Modifying
	@Transactional
	public void deleteByIdRequest(Integer idRequest);
	
	@Modifying
	@Transactional
	public void deleteByIdOffer(Integer idRequest);
	
	public List<NotificationsUser> findByUser(User user);
}