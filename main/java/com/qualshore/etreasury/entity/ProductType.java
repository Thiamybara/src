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
@Table(name = "product_type")
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@XmlRootElement
/*@NamedQueries({
    @NamedQuery(name = "ProductType.findAll", query = "SELECT p FROM ProductType p"),
    @NamedQuery(name = "ProductType.findByIdTypeProduits", query = "SELECT p FROM ProductType p WHERE p.idTypeProduits = :idTypeProduits"),
    @NamedQuery(name = "ProductType.findByLibelle", query = "SELECT p FROM ProductType p WHERE p.libelle = :libelle"),
    @NamedQuery(name = "ProductType.findByDescription", query = "SELECT p FROM ProductType p WHERE p.description = :description")})*/
public class ProductType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_type_produits")
    private Integer idTypeProduits;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "description")
    private String description;
    
    @JsonIgnore
    @OneToMany(mappedBy = "productType", cascade= CascadeType.ALL, fetch= FetchType.LAZY)
    private List<Products> products;
    
	public ProductType() {
    }

    public ProductType(Integer idTypeProduits) {
        this.idTypeProduits = idTypeProduits;
    }
    
    public List<Products> getProducts() {
		return products;
	}

	public void setProducts(List<Products> products) {
		this.products = products;
	}


    public Integer getIdTypeProduits() {
        return idTypeProduits;
    }

    public void setIdTypeProduits(Integer idTypeProduits) {
        this.idTypeProduits = idTypeProduits;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTypeProduits != null ? idTypeProduits.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductType)) {
            return false;
        }
        ProductType other = (ProductType) object;
        if ((this.idTypeProduits == null && other.idTypeProduits != null) || (this.idTypeProduits != null && !this.idTypeProduits.equals(other.idTypeProduits))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qualshore.etreasury.entity.ProductType[ idTypeProduits=" + idTypeProduits + " ]";
    }
    
}
