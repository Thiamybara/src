package com.qualshore.etreasury.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qualshore.etreasury.entity.DocumentsHasOffer;
import com.qualshore.etreasury.entity.Offer;

public interface DocumentHasOfferRepository extends JpaRepository<DocumentsHasOffer, Integer> {

	public List<DocumentsHasOffer> findByOffer(Offer offer);
}
