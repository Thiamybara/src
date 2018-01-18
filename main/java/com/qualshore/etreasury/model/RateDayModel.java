package com.qualshore.etreasury.model;

public class RateDayModel {
    
    String dateDebut;
   String dateFin;
   Integer idProduit;
   Integer idDevise;
   Integer[] idsBank;
   
   public String getDateDebut() {
       return dateDebut;
   }
   public void setDateDebut(String dateDebut) {
       this.dateDebut = dateDebut;
   }
   public String getDateFin() {
       return dateFin;
   }
   public void setDateFin(String dateFin) {
       this.dateFin = dateFin;
   }
   public Integer getIdProduit() {
       return idProduit;
   }
   public void setIdProduit(Integer idProduit) {
       this.idProduit = idProduit;
   }
    public Integer getIdDevise() {
        return idDevise;
    }
    public void setIdDevise(Integer idDevise) {
        this.idDevise = idDevise;
    }
    public Integer[] getIdsBank() {
        return idsBank;
    }
    public void setIdsBank(Integer[] idsBank) {
        this.idsBank = idsBank;
    }
}