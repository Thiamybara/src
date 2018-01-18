package com.qualshore.etreasury.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author macbookpro
 */
@Entity
@Table(name = "message")
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@XmlRootElement

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_message")
    private Integer idMessage;
    
    @Column(name = "date_message")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateMessage;

    @Column(name = "contenu_message")
    private String contenuMessage;
    
    @Column(name = "is_rattached_file")
    private Boolean isRattachedFile;

    @Column(name = "repertoire_file")
    private String repertoireFile;
    
    @Column(name = "is_visible")
    private Boolean isVisible;
    
    @Column(name = "is_accept")
    private Boolean isAccept;
    
    @Column(name = "is_group")
    private Boolean isGroup;
    
    @Column(name = "is_connected")
    private Boolean isConnected;

    @Column(name = "messagecol")
    private String messagecol;
    
    /*@OneToMany(cascade = CascadeType.ALL, mappedBy = "message", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserHasMessage> userHasMessageList;*/
    
    @JoinColumn(name = "type_message_id_type_message", referencedColumnName = "id_type_message")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MessageType typeMessageIdTypeMessage;
    
    @JoinColumn(name = "user", referencedColumnName = "id_utilisateur")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    public Message() {
    }

    public Message(Integer idMessage) {
        this.idMessage = idMessage;
    }

    public Message(Date dateMessage, String contenuMessage, Boolean isRattachedFile,
			String repertoireFile, Boolean isVisible, Boolean isAccept, Boolean isGroup, Boolean isConnected,
			String messagecol, MessageType typeMessageIdTypeMessage, User user) {
		super();
		this.dateMessage = dateMessage;
		this.contenuMessage = contenuMessage;
		this.isRattachedFile = isRattachedFile;
		this.repertoireFile = repertoireFile;
		this.isVisible = isVisible;
		this.isAccept = isAccept;
		this.isGroup = isGroup;
		this.isConnected = isConnected;
		this.messagecol = messagecol;
		this.typeMessageIdTypeMessage = typeMessageIdTypeMessage;
		this.user = user;
	}
    
    public Message(Integer idMessage, String contenuMessage, Date dateMessage, User user) {
		this.idMessage = idMessage;
		this.contenuMessage = contenuMessage;
		this.dateMessage = dateMessage;
		this.user = user;
	}

	public Integer getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(Integer idMessage) {
        this.idMessage = idMessage;
    }

    public Date getDateMessage() {
        return dateMessage;
    }

    public void setDateMessage(Date dateMessage) {
        this.dateMessage = dateMessage;
    }

    public String getContenuMessage() {
        return contenuMessage;
    }

    public void setContenuMessage(String contenuMessage) {
        this.contenuMessage = contenuMessage;
    }

    public Boolean getIsRattachedFile() {
        return isRattachedFile;
    }

    public void setIsRattachedFile(Boolean isRattachedFile) {
        this.isRattachedFile = isRattachedFile;
    }

    public String getRepertoireFile() {
        return repertoireFile;
    }

    public void setRepertoireFile(String repertoireFile) {
        this.repertoireFile = repertoireFile;
    }

    public Boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }

    public Boolean getIsAccept() {
        return isAccept;
    }

    public void setIsAccept(Boolean isAccept) {
        this.isAccept = isAccept;
    }

    public Boolean getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(Boolean isGroup) {
        this.isGroup = isGroup;
    }

    public Boolean getIsConnected() {
        return isConnected;
    }

    public void setIsConnected(Boolean isConnected) {
        this.isConnected = isConnected;
    }

    public String getMessagecol() {
        return messagecol;
    }

    public void setMessagecol(String messagecol) {
        this.messagecol = messagecol;
    }

    /*@XmlTransient
    public List<UserHasMessage> getUserHasMessageList() {
        return userHasMessageList;
    }

    public void setUserHasMessageList(List<UserHasMessage> userHasMessageList) {
        this.userHasMessageList = userHasMessageList;
    }*/

    public MessageType getTypeMessageIdTypeMessage() {
        return typeMessageIdTypeMessage;
    }

    public void setTypeMessageIdTypeMessage(MessageType typeMessageIdTypeMessage) {
        this.typeMessageIdTypeMessage = typeMessageIdTypeMessage;
    }
    
    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idMessage != null ? idMessage.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Message)) {
            return false;
        }
        Message other = (Message) object;
        if ((this.idMessage == null && other.idMessage != null) || (this.idMessage != null && !this.idMessage.equals(other.idMessage))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qualshore.etreasury.entity.Message[ idMessage=" + idMessage + " ]";
    }
    
}
