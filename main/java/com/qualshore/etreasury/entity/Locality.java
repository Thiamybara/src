/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qualshore.etreasury.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author macbookpro
 */
@Entity
@Table(name = "locality")
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Locality.findAll", query = "SELECT l FROM Locality l"),
    @NamedQuery(name = "Locality.findByIdLocalite", query = "SELECT l FROM Locality l WHERE l.idLocalite = :idLocalite"),
    @NamedQuery(name = "Locality.findByCode", query = "SELECT l FROM Locality l WHERE l.code = :code"),
    @NamedQuery(name = "Locality.findByVille", query = "SELECT l FROM Locality l WHERE l.ville = :ville"),
    @NamedQuery(name = "Locality.findByPays", query = "SELECT l FROM Locality l WHERE l.pays = :pays")})
public class Locality implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_localite")
    private Integer idLocalite;
   
    @Column(name = "code")
    private String code;

    @Column(name = "ville")
    private String ville;

    @Column(name = "pays")
    private String pays;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "localityIdLocalite", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Institution> institutionList;

    public Locality() {
    }

    public Locality(Integer idLocalite) {
        this.idLocalite = idLocalite;
    }

    public Integer getIdLocalite() {
        return idLocalite;
    }

    public void setIdLocalite(Integer idLocalite) {
        this.idLocalite = idLocalite;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    @XmlTransient
    public List<Institution> getInstitutionList() {
        return institutionList;
    }

    public void setInstitutionList(List<Institution> institutionList) {
        this.institutionList = institutionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLocalite != null ? idLocalite.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Locality)) {
            return false;
        }
        Locality other = (Locality) object;
        if ((this.idLocalite == null && other.idLocalite != null) || (this.idLocalite != null && !this.idLocalite.equals(other.idLocalite))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qualshore.etreasury.entity.Locality[ idLocalite=" + idLocalite + " ]";
    }
    
}
