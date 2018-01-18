package com.qualshore.etreasury.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RestController;

import com.qualshore.etreasury.entity.ProductType;
import com.qualshore.etreasury.entity.Profile;

public interface ProductTypeRepository extends JpaRepository<ProductType, Integer>{
	public List<ProductType> findByLibelle(@Param("libelle") String nom);
}
