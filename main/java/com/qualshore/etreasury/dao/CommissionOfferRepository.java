package com.qualshore.etreasury.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qualshore.etreasury.entity.CommissionOffer;
import com.qualshore.etreasury.entity.Offer;

public interface CommissionOfferRepository extends JpaRepository<CommissionOffer, Integer> {

	List<CommissionOffer> findByOffer(Offer offer);
}
