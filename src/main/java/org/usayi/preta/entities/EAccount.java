package org.usayi.preta.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.usayi.preta.Views;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Audited
@Table( uniqueConstraints= @UniqueConstraint( columnNames = { "account"} ))
public class EAccount implements Serializable
{
	private static final long serialVersionUID = -8628238749027607644L;

	@Id
	@JsonView( Views.Public.class)
	@NotAudited
	@GeneratedValue( strategy= GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@JsonView( Views.Public.class)
	private Long relId;
	
	@NotNull
	@JsonView( Views.Public.class)
	private String account;
	
	@JsonView( Views.Public.class)
	private Boolean isDefault;

	@OneToOne
	@JsonIgnore
	@JoinColumn( name="userInfoId", nullable=true)
	private UserInfo user;
	
	@OneToOne
	@JsonProperty( value="emp")
	@JsonView( Views.Public.class)
	@JoinColumn( name="eMoneyProviderId", nullable=false)
	private EMoneyProvider eMoneyProvider;
	
	@JsonIgnore
	@OneToMany( mappedBy="eAccount", fetch=FetchType.LAZY)
	private Collection<Payment> payments;
	
	@JsonIgnore
	@OneToMany( mappedBy="eAccount", fetch=FetchType.LAZY)
	private Collection<Expense> expenses;
	
	public final Long getId()
	{
		return id;
	}

	public final void setId(Long id)
	{
		this.id = id;
	}

	public final String getAccount()
	{
		return account;
	}

	public final void setAccount(String account)
	{
		this.account = account;
	}

	public final Boolean getIsDefault()
	{
		return isDefault;
	}

	public final void setIsDefault(Boolean isDefault)
	{
		this.isDefault = isDefault;
	}

	public final UserInfo getUserId()
	{
		return user;
	}

	public final void setUserId(UserInfo user)
	{
		this.user = user;
	}

	public final EMoneyProvider geteMoneyProvider()
	{
		return eMoneyProvider;
	}

	public final void seteMoneyProvider(EMoneyProvider eMoneyProvider)
	{
		this.eMoneyProvider = eMoneyProvider;
	}

	public final UserInfo getUser()
	{
		return user;
	}

	public final void setUser(UserInfo user)
	{
		this.user = user;
	}

	public final Collection<Payment> getPayments()
	{
		return payments;
	}
	
	public final void addPayment( final Payment payment)
	{
		this.payments.add( payment);
	}
	
	public final void removePayement( final Payment payment)
	{
		this.payments.remove( payment);
	}

	public final Collection<Expense> getExpenses()
	{
		return expenses;
	}
	
	public final void addExpense( final Expense expense)
	{
		this.expenses.add( expense);
	}
	
	public final void removeExpense( final Expense expense)
	{
		this.expenses.remove( expense);
	}

	public EAccount(String account, UserInfo user, EMoneyProvider eMoneyProvider)
	{
		super();
		this.account = account;
		this.user = user;
		this.eMoneyProvider = eMoneyProvider;
		this.payments = new ArrayList<Payment>();
	}

	public EAccount()
	{
		super();
		this.payments = new ArrayList<Payment>();
	}

	public Long getRelId()
	{
		return relId;
	}

	public void setRelId(Long relId)
	{
		this.relId = relId;
	}
}
