package com.qualshore.etreasury.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qualshore.etreasury.entity.Bank;
import com.qualshore.etreasury.entity.BankCondition;
import com.qualshore.etreasury.entity.Products;

public interface BankConditionRepository extends JpaRepository<BankCondition, Integer>{
	
	BankCondition findFirstByOrderByIdConditionBanqueDesc();
	
	List<BankCondition> findByBank(Bank bank);
	
	@Query("select bc from BankCondition bc where  bc.bank= :bank order by bc.dateFin desc")
	List<BankCondition> findLastByBank(@Param("bank") Bank bank);
	
	@Query("select bc from BankCondition bc where  bc.bank.localityIdLocalite.idLocalite=?1")
    List<BankCondition> findByLocality(Integer locality);
	
	 @Query("SELECT CASE WHEN COUNT(bc) > 0 THEN 'true' ELSE 'false' END FROM BankCondition bc WHERE bc.bank= ?1 AND bc.product = ?2 AND bc.dateDebut <= ?3 AND bc.dateFin >= ?3 ")
	 public boolean exitsByBankProductAndDate(Bank bank, Products product, Date now);
	 
	 @Query("SELECT CASE WHEN COUNT(bc) > 0 THEN 'true' ELSE 'false' END FROM BankCondition bc WHERE bc.bank= ?1 AND bc.product = ?2 AND bc.dateDebut <= ?3 AND bc.dateFin >= ?3  AND bc.idConditionBanque <> ?4")
     public boolean exitsByBankProductAndDateOthers(Bank bank, Products product,Date now, Integer idBankCondition);

	 @Query("SELECT bc FROM BankCondition bc WHERE bc.dateDebut = :dateDebut OR bc.dateFin = :dateFin OR"
	 		+ " bc.tauxStandard like :tauxStandard OR bc.product = :pr AND  bc.bank in :banks")
	 List<BankCondition> multicritere(@Param("dateDebut") Date dateDebut, @Param("dateFin") Date dateFin, 
			 @Param("tauxStandard") String tauxStandard, @Param("pr") Products pr, @Param("banks") List<Bank> banks);
	 
	 @Query("SELECT bc FROM BankCondition bc WHERE bc.dateDebut = :dateDebut AND  bc.bank in :banks")
	 List<BankCondition> findDateDebut(@Param("dateDebut") Date dateDebut, @Param("banks") List<Bank> banks);
	 
	 @Query("SELECT bc FROM BankCondition bc WHERE bc.dateFin = :dateFin AND  bc.bank in :banks")
	 List<BankCondition> findDateFin(@Param("dateFin") Date dateFin, @Param("banks") List<Bank> banks);	 
	 
	 @Query("SELECT bc FROM BankCondition bc WHERE bc.tauxStandard like :tauxStandard AND  bc.bank in :banks")
	 List<BankCondition> findTauxStandard (@Param("tauxStandard") String tauxStandard, @Param("banks") List<Bank> banks);
	 
	 @Query("SELECT bc FROM BankCondition bc WHERE bc.product in :prs AND  bc.bank in :banks")
	 List<BankCondition> findProducts (@Param("prs") List<Products> prs, @Param("banks") List<Bank> banks);
	 
	 @Query("SELECT bc FROM BankCondition bc WHERE bc.bank in :banks")
	 List<BankCondition> findBanks (@Param("banks") List<Bank> banks);
	 
	 	 
	 
	 
}
