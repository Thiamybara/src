package com.qualshore.etreasury.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qualshore.etreasury.entity.Offer;
import com.qualshore.etreasury.entity.ValidationSelect;

public interface ValidationSelectRepository extends JpaRepository<ValidationSelect, Integer> {

	ValidationSelect findByOfferAndNiveau(Offer offer, Integer niveau);
	
	List<ValidationSelect> findByOffer(Offer offer);
	
	public boolean existsByOfferAndNiveau(Offer offer, Integer niveau);
	
	public boolean existsByOffer(Offer offer);
}
