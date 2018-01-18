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
@Table(name = "message_type")
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MessageType.findAll", query = "SELECT m FROM MessageType m"),
    @NamedQuery(name = "MessageType.findByIdTypeMessage", query = "SELECT m FROM MessageType m WHERE m.idTypeMessage = :idTypeMessage"),
    @NamedQuery(name = "MessageType.findByLibelleType", query = "SELECT m FROM MessageType m WHERE m.libelleType = :libelleType"),
    @NamedQuery(name = "MessageType.findByDescription", query = "SELECT m FROM MessageType m WHERE m.description = :description")})
public class MessageType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_type_message")
    private Integer idTypeMessage;

    @Column(name = "libelle_type")
    private String libelleType;

    @Column(name = "description")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "typeMessageIdTypeMessage", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Message> messageList;

    public MessageType() {
    }

    public MessageType(Integer idTypeMessage) {
        this.idTypeMessage = idTypeMessage;
    }

    public MessageType(String libelleType, String description) {
		super();
		this.libelleType = libelleType;
		this.description = description;
	}

	public Integer getIdTypeMessage() {
        return idTypeMessage;
    }

    public void setIdTypeMessage(Integer idTypeMessage) {
        this.idTypeMessage = idTypeMessage;
    }

    public String getLibelleType() {
        return libelleType;
    }

    public void setLibelleType(String libelleType) {
        this.libelleType = libelleType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTypeMessage != null ? idTypeMessage.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MessageType)) {
            return false;
        }
        MessageType other = (MessageType) object;
        if ((this.idTypeMessage == null && other.idTypeMessage != null) || (this.idTypeMessage != null && !this.idTypeMessage.equals(other.idTypeMessage))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qualshore.etreasury.entity.MessageType[ idTypeMessage=" + idTypeMessage + " ]";
    }
    
}
