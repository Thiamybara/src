package com.qualshore.etreasury.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.qualshore.etreasury.entity.Products;

public interface ProductsRepository extends JpaRepository<Products, Integer>{

	@Query("SELECT CASE WHEN COUNT(p) > 0 THEN 'true' ELSE 'false' END FROM Products p WHERE p.nom = ?1 ")
	public boolean existsByProductName(String libelle);
}
