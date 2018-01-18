package com.qualshore.etreasury.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qualshore.etreasury.entity.Bank;
import com.qualshore.etreasury.entity.BankCondition;
import com.qualshore.etreasury.entity.Devise;
import com.qualshore.etreasury.entity.Locality;
import com.qualshore.etreasury.entity.Products;
import com.qualshore.etreasury.entity.RateDay;

public interface RateDayRepository extends JpaRepository<RateDay, Integer> {

	@Query("SELECT r FROM RateDay r WHERE r.bank = ?1")
	public List<RateDay> findByBank(Bank idBanque);
	
	@Query("SELECT r FROM RateDay r WHERE r.bank.localityIdLocalite = ?1")
	public List<RateDay> findByLocality(Locality idLocality);
	
	//public List<RateDay> findByDateDebutValidite(@Param("dateDebutValidite") Date dateDebutValidite);
	@Query("SELECT r FROM RateDay r WHERE r.bank = ?1 AND r.produitsIdProduits= ?2 AND r.dateValeur = ?3")
	public List<RateDay> findByBankProductAndDateValeur(Bank idBanque, Products product, Date dateNow );
	
	public List<RateDay> findByProduitsIdProduits(Products pr);
    
    public List<RateDay> findByDevise(String devise);
    
    public List<RateDay> findByDateFinValiditeGreaterThanEqual(Date date);
    
    public List<RateDay> findByDateDebutValiditeLessThanEqual(Date date);
    
    @Query("SELECT r FROM RateDay r WHERE r.produitsIdProduits in :prs AND r.bank in :banks ")
    public List<RateDay> findRateByProduitAndLocalite(@Param("prs") List<Products> prs, 
    		@Param("banks") List<Bank> banks);
    
    @Query("SELECT r FROM RateDay r WHERE r.devise = :dev AND r.bank in :banks")
    public List<RateDay> findRateByDeviseAndLocalite(@Param("dev") Devise dev, 
    		@Param("banks") List<Bank> banks);
    
    @Query("SELECT r FROM RateDay r WHERE r.dateFinValidite = :dFVal AND r.bank in :banks")
    public List<RateDay> findDateFinValiditeAndLocalite(@Param("dFVal") Date dFVal, 
    		@Param("banks") List<Bank> banks);
    
    @Query("SELECT r FROM RateDay r WHERE r.dateDebutValidite = :dDVal AND r.bank in :banks ")
    public List<RateDay> findDateDebutValiditeAndLocalite(@Param("dDVal") Date dDVal,
    		@Param("banks") List<Bank> banks);

   @Query("SELECT r FROM RateDay r WHERE r.bank in :banks ")
   public List<RateDay> findBanks(@Param("banks") List<Bank> banks);
    
    @Query("select r from RateDay r where r.produitsIdProduits = :pr AND r.devise = :devise AND  r.bank= :bank order by r.dateFinValidite desc")
	List<RateDay> findLastByBank(@Param("pr") Products pr, @Param("devise") Devise devise,
			 @Param("bank") Bank bank);
}
