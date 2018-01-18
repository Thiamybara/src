package com.qualshore.etreasury.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qualshore.etreasury.entity.Enterprise;

public interface EntrepriseRepository extends JpaRepository<Enterprise, Integer> {

}
