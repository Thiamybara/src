package com.qualshore.etreasury.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qualshore.etreasury.entity.Action;

public interface ActionRepository extends JpaRepository<Action, Integer> {

	public List<Action> findByIdAction(Integer idAction);
}
