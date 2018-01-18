package com.qualshore.etreasury.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.qualshore.etreasury.entity.Institution;

@NoRepositoryBean
public interface InstitutionBaseRepository<T extends Institution> extends JpaRepository<T, Integer>{

}
