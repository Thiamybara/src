package com.qualshore.etreasury.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qualshore.etreasury.entity.Category;

public interface CategoryOldRepository extends JpaRepository<Category, Integer> {

}
