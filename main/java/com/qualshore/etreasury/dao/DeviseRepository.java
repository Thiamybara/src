package com.qualshore.etreasury.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.qualshore.etreasury.entity.Devise;

public interface DeviseRepository extends JpaRepository<Devise, Integer> {

	public boolean existsByDescription(String description);

	public Devise findByDescription(String desc);
}
