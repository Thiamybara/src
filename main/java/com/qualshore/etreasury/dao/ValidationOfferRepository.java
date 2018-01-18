package com.qualshore.etreasury.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qualshore.etreasury.entity.Offer;
import com.qualshore.etreasury.entity.ValidationOffer;

public interface ValidationOfferRepository extends JpaRepository<ValidationOffer, Integer> {

	ValidationOffer findByOfferAndNiveau(Offer offer, Integer niveau);
	
	public boolean existsByOfferAndNiveau(Offer offer, Integer niveau);
	public boolean existsByOffer(Offer offer);
}
