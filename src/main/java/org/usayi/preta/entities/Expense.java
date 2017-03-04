package org.usayi.preta.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	@GeneratedValue( strategy=GenerationType.IDENTITY)
	@NotAudited
	private Long id;
	
	@NotNull
	@NotEmpty
	private Float amount;
	
	@NotNull
	@NotEmpty
	private String expenseRef;
	
	@ManyToOne
	@JoinColumn( referencedColumnName="id")
	private EAccount eAccount;

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
}
