package org.usayi.preta.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.usayi.preta.Views;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Audited
public class Payment implements Serializable
{
	private static final long serialVersionUID = 4550657204840824847L;

	@Id
	@NotAudited
	@JsonView( Views.Public.class)
	@GeneratedValue( strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@JsonView( Views.Public.class)
	private Long relId;
	
	@NotNull
	@JsonView( Views.Public.class)
	private Float amount;
	
	@NotNull
	@JsonView( Views.Public.class)
	private String paymentRef;
	
	@JsonView( Views.Public.class)
	private Boolean isValid = null;
	
	@JsonView( Views.Public.class)
	private Timestamp validationDate = null;
	
	@Fetch( FetchMode.SELECT)
	@JsonView( Views.Admin.class)
	@ManyToOne( fetch=FetchType.EAGER)
	@JoinColumn( referencedColumnName="id")
	private EAccount eAccount;
		
	@Fetch( FetchMode.SELECT)
	@JsonView( Views.Admin.class)
	@ManyToOne( fetch=FetchType.EAGER)
	@JoinColumn( referencedColumnName="id")
	private EAccount adminEAccount;
	
	@Fetch( FetchMode.SELECT)
	@ManyToOne( fetch=FetchType.EAGER)
	@JoinColumn( referencedColumnName="id")
	private PaymentType paymentType;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn( referencedColumnName="id", nullable=true)
	private AdvOffer advOffer = null;

	@JsonIgnore
	@ManyToMany( fetch=FetchType.LAZY)
	private Collection<ArticleOrder> articleOrders = new ArrayList<ArticleOrder>();
	
	@JsonIgnore
	@ManyToOne( fetch=FetchType.LAZY)
	@JoinColumn( referencedColumnName="id", nullable=true)
	private ShopSub shopSub = null;
	
	/* User who did the payment */
	@NotNull
	@ManyToOne
	@JsonIgnore
	private User user;
	
	@Transient
	@JsonView( Views.Admin.class)
	private String username;
	
	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	@Transient
	@JsonView( Views.Public.class)
	private Timestamp regDate;
	
	public AdvOffer getAdvOffer()
	{
		return advOffer;
	}

	public void setAdvOffer(AdvOffer advOffer)
	{
		this.advOffer = advOffer;
	}

	public ShopSub getShopSub()
	{
		return shopSub;
	}

	public void setShopSub(ShopSub shopSub)
	{
		this.shopSub = shopSub;
	}

	public final Long getId()
	{
		return id;
	}

	public final void setId(Long id)
	{
		this.id = id;
	}

	public Long getRelId()
	{
		return relId;
	}

	public void setRelId(Long relId)
	{
		this.relId = relId;
	}

	public final Float getAmount()
	{
		return amount;
	}

	public final void setAmount(Float amount)
	{
		this.amount = amount;
	}

	public final String getPaymentRef()
	{
		return paymentRef;
	}

	public final void setPaymentRef(String paymentRef)
	{
		this.paymentRef = paymentRef;
	}

	public final Boolean getIsValid()
	{
		return isValid;
	}

	public final void setIsValid(Boolean isValid)
	{
		this.isValid = isValid;
	}

	public final Timestamp getValidationDate()
	{
		return validationDate;
	}

	public final void setValidationDate(Timestamp validationDate)
	{
		this.validationDate = validationDate;
	}

	public final EAccount geteAccount()
	{
		return eAccount;
	}

	public final void seteAccount(EAccount eAccount)
	{
		this.eAccount = eAccount;
	}

	public final PaymentType getPaymentType()
	{
		return paymentType;
	}

	public final void setPaymentType(PaymentType paymentType)
	{
		this.paymentType = paymentType;
	}

	public Payment( Float amount, String paymentRef, PaymentType paymentType)
	{
		super();
		this.amount = amount;
		this.paymentRef = paymentRef;
		this.paymentType = paymentType;
		this.articleOrders = new ArrayList<ArticleOrder>();
	}
	
	public Payment(Payment payment)
	{
		super();
		this.amount = payment.amount;
		this.paymentRef = payment.paymentRef;
		this.eAccount = payment.eAccount;
		this.articleOrders = new ArrayList<ArticleOrder>();
	}

	public Payment()
	{
		super();
	}

	public Timestamp getRegDate()
	{
		return regDate;
	}

	public void setRegDate(Timestamp regDate)
	{
		this.regDate = regDate;
	}

	public EAccount getAdminEAccount()
	{
		return adminEAccount;
	}

	public void setAdminEAccount(EAccount adminEAccount)
	{
		this.adminEAccount = adminEAccount;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	public Collection<ArticleOrder> getArticleOrders()
	{
		return articleOrders;
	}
	
	public void addArticleOrder( ArticleOrder entity)
	{
		this.articleOrders.add( entity);
	}
}
