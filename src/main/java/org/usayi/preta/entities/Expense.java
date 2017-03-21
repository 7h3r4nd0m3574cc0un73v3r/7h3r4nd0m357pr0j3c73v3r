package org.usayi.preta.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Audited
public class Expense implements Serializable
{
	private static final long serialVersionUID = 6330239536274900635L;

	@Id
	@NotAudited
	@GeneratedValue( strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private Float amount;
	
	@NotNull
	private String expenseRef;
	
	@NotNull
	@ManyToOne
	@JoinColumn( referencedColumnName="id")
	private EAccount eAccount;

	@NotNull
	@ManyToOne
	@JoinColumn( referencedColumnName="id")
	private EAccount adminEAccount;

	@Transient
	private Timestamp regDate;
	
	public Timestamp getRegDate()
	{
		return regDate;
	}

	public void setRegDate(Timestamp regDate)
	{
		this.regDate = regDate;
	}

	@NotEmpty
	@OneToMany( mappedBy="expense")
	private Collection<ArticleOrder> articleOrders = new ArrayList<ArticleOrder>();
	
	public Collection<ArticleOrder> getArticleOrders()
	{
		return articleOrders;
	}
	
	public EAccount getAdminEAccount()
	{
		return adminEAccount;
	}

	public void setAdminEAccount(EAccount adminEAccount)
	{
		this.adminEAccount = adminEAccount;
	}

	public final Long getId()
	{
		return id;
	}

	public final void setId(Long id)
	{
		this.id = id;
	}

	public final Float getAmount()
	{
		return amount;
	}

	public final void setAmount(Float amount)
	{
		this.amount = amount;
	}

	public final String getExpenseRef()
	{
		return expenseRef;
	}

	public final void setExpenseRef(String expenseRef)
	{
		this.expenseRef = expenseRef;
	}

	public final EAccount geteAccount()
	{
		return eAccount;
	}

	public final void seteAccount(EAccount eAccount)
	{
		this.eAccount = eAccount;
	}

	public Expense(Float amount, String expenseRef, EAccount eAccount)
	{
		super();
		this.amount = amount;
		this.expenseRef = expenseRef;
		this.eAccount = eAccount;
	}

	public Expense()
	{
		super();
	}
	
	public final void addArticleOrder( final ArticleOrder entity) {
		articleOrders.add(entity);
	}
}
