package org.usayi.preta.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.usayi.preta.Views;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class Notification implements Serializable
{
	 static final long serialVersionUID = -3714133204757927747L;

	@Id
	@JsonView( Views.Public.class)
	@GeneratedValue( strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@JsonView( Views.Public.class)
	private Timestamp regDate = new Timestamp( System.currentTimeMillis());
	
	@NotNull
	@JsonView( Views.Public.class)
	private boolean isRead = false;
	
	@NotNull
	@JsonView( Views.Public.class)
	private NotificationType nType;

	@NotNull
	@JsonIgnore
	@JsonView( Views.Public.class)
	@ManyToOne( fetch=FetchType.LAZY)
	@JoinColumn( referencedColumnName="id")
	private User user;
	
	/*
	 * Allows sepration between Buyer and Manager Side
	 */
	@NotNull
	@JsonIgnore
	private boolean buyer= false;
	
	@NotNull
	@JsonIgnore
	private boolean manager = false;
	
	@NotNull
	@JsonIgnore
	private boolean admin = false;
	
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	@JsonView( Views.Public.class)
	public Timestamp getRegDate()
	{
		return regDate;
	}

	public void setRegDate(Timestamp regDate)
	{
		this.regDate = regDate;
	}

	public boolean isRead()
	{
		return isRead;
	}

	/* Notification Info */
	/* Abstract Articles Count */
	@JsonView( Views.Public.class)
	private Long count = null;
	
	@NotNull
	@JsonView( Views.Public.class)
	private NotificationEntity entType = null;
	
	/* Altered Payment */
	@JsonView( Views.Public.class)
	private Long paymentAbsId = null;
	
	@JsonView( Views.Public.class)
	private Long paymentRelId = null;

	@JsonView( Views.Public.class)
	private Long entAbsId = null;
	
	@JsonView( Views.Public.class)
	private Long entRelId = null;
	
	@JsonView( Views.Public.class)
	private boolean valid = false;

	public NotificationType getType()
	{
		return nType;
	}

	public Notification()
	{
		super();
	}
	
	public Notification(NotificationType nType,
						Long count, NotificationEntity entType, Long paymentAbsId,
						Long paymentRelId, Long entAbsId, Long entRelId, boolean valid,
						boolean isBuyer, boolean isManager)
	{
		super();
		this.regDate = new Timestamp( System.currentTimeMillis());
		this.isRead = false;
		this.nType = nType;
		
		this.count = count;
		this.entType = entType;
		this.entAbsId = entAbsId;
		this.entRelId = entRelId;
		this.paymentAbsId = paymentAbsId;
		this.paymentRelId = paymentRelId;
		this.valid = valid;
		this.buyer = isBuyer;
		this.manager = isManager;
	}

	public void setType(NotificationType nType)
	{
		this.nType = nType;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}
	

	public NotificationType getnType()
	{
		return nType;
	}

	public void setnType(NotificationType nType)
	{
		this.nType = nType;
	}

	public Long getCount()
	{
		return count;
	}

	public void setCount(Long count)
	{
		this.count = count;
	}

	public NotificationEntity getEntType()
	{
		return entType;
	}

	public void setEntType(NotificationEntity entType)
	{
		this.entType = entType;
	}

	public Long getPaymentAbsId()
	{
		return paymentAbsId;
	}

	public void setPaymentAbsId(Long paymentAbsId)
	{
		this.paymentAbsId = paymentAbsId;
	}

	public Long getPaymentRelId()
	{
		return paymentRelId;
	}

	public void setPaymentRelId(Long paymentRelId)
	{
		this.paymentRelId = paymentRelId;
	}

	public Long getEntAbsId()
	{
		return entAbsId;
	}

	public void setEntAbsId(Long entAbsId)
	{
		this.entAbsId = entAbsId;
	}

	public Long getEntRelId()
	{
		return entRelId;
	}

	public void setEntRelId(Long entRelId)
	{
		this.entRelId = entRelId;
	}

	public boolean isValid()
	{
		return valid;
	}

	public void setValid(boolean valid)
	{
		this.valid = valid;
	}

	public boolean isBuyer()
	{
		return buyer;
	}

	public void setBuyer(boolean buyer)
	{
		this.buyer = buyer;
	}

	public boolean isManager()
	{
		return manager;
	}

	public void setManager(boolean manager)
	{
		this.manager = manager;
	}

	public void setRead(boolean isRead)
	{
		this.isRead = isRead;
	}

	public boolean isAdmin()
	{
		return admin;
	}

	public void setAdmin(boolean admin)
	{
		this.admin = admin;
	}
}
