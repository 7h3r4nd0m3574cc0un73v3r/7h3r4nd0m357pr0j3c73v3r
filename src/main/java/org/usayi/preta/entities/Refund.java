package org.usayi.preta.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Audited
public class Refund implements Serializable
{
	private static final long serialVersionUID = -2038068919230912904L;

	@Id
	@GeneratedValue( strategy=GenerationType.IDENTITY)
	@NotAudited
	private Long id;
	
	@NotEmpty
	@NotNull
	@Size( min  = 4, max = 50)
	private String motive;
	
	@NotNull
	@NotEmpty
	private Float amount;
	
	@ManyToOne
	@JoinColumn( referencedColumnName="id", nullable=false)
	private Payment payment;
	
	private String decision;
	
	private Boolean validated = null;

	public final Long getId()
	{
		return id;
	}

	public final void setId(Long id)
	{
		this.id = id;
	}

	public final String getMotive()
	{
		return motive;
	}

	public final void setMotive(String motive)
	{
		this.motive = motive;
	}

	public final Float getAmount()
	{
		return amount;
	}

	public final void setAmount(Float amount)
	{
		this.amount = amount;
	}

	public final Payment getPayment()
	{
		return payment;
	}

	public final void setPayment(Payment payment)
	{
		this.payment = payment;
	}

	public final String getDecision()
	{
		return decision;
	}

	public final void setDecision(String decision)
	{
		this.decision = decision;
	}

	public final Boolean getValidated()
	{
		return validated;
	}

	public final void setValidated(Boolean validated)
	{
		this.validated = validated;
	}

	public Refund(String motive, Float amount, Payment payment, String decision)
	{
		super();
		this.motive = motive;
		this.amount = amount;
		this.payment = payment;
		this.decision = decision;
	}

	public Refund()
	{
		super();
	}
}
