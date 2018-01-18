package com.qualshore.etreasury.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.qualshore.etreasury.entity.Log;

public interface LogRepository extends JpaRepository<Log, Integer> {

	@Query("SELECT l FROM Log l WHERE l.idLog= ?1 ORDER BY date DESC")
	public List<Log> findLastLogById(Integer idLog);
}
