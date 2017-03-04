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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.context.annotation.Description;
import org.usayi.preta.Views;
import org.usayi.preta.config.Tools;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Audited
public class ShopSub implements Serializable
{
	private static final long serialVersionUID = 4777237348488200113L;
	
	@Id
	@NotAudited
	@JsonView( { Views.Manager.class, Views.Admin.class})
	@GeneratedValue( strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@NotAudited
	@JsonView( { Views.Manager.class, Views.Admin.class})
	private Long relId;
	
	@NotNull
	@JsonView( Views.Public.class)
	private Timestamp startDate;
	
	@NotNull
	@JsonView( Views.Public.class)
	private GenericStatus subStatus;
	
	@ManyToOne
	@JsonView( Views.Public.class)
	@JoinColumn( referencedColumnName="id")
	private SubOffer subOffer;

	@NotNull
	@ManyToOne
	@JsonIgnore
	@JoinColumn( referencedColumnName="id")
	private EShop eShop;
	
	@Fetch( FetchMode.SELECT)
	@JsonView( Views.Public.class)
	@OneToMany( mappedBy="shopSub", fetch=FetchType.EAGER)
	private Collection<Payment> payments;
	
	@NotNull
	@JsonView( { Views.Admin.class, Views.Manager.class})
	private Timestamp endDate;
	
	public final Long getId()
	{
		return id;
	}

	public final void setId(Long id)
	{
		this.id = id;
	}

	public final Timestamp getStartDate()
	{
		return startDate;
	}

	public final void setStartDate(Timestamp startDate)
	{
		this.startDate = startDate;
	}
	
	public final GenericStatus getSubStatus()
	{
		return subStatus;
	}

	public final void setSubStatus(GenericStatus subStatus)
	{
		this.subStatus = subStatus;
	}

	public final SubOffer getSubOffer()
	{
		return subOffer;
	}

	public final void setSubOffer(SubOffer subOffer)
	{
		this.subOffer = subOffer;
	}

	public final EShop geteShop()
	{
		return eShop;
	}

	public final void seteShop(EShop eShop)
	{
		this.eShop = eShop;
	}

	public final Collection<Payment> getPayments()
	{
		return payments;
	}

	public final void setPayments(Collection<Payment> payments)
	{
		this.payments = payments;
	}

	public final void addPayment( final Payment payment)
	{
		this.payments.add(payment);
	}
	
	public final void removePayment( final Payment payment)
	{
		this.payments.remove(payment);
	}
	
	public ShopSub(Timestamp startDate, GenericStatus subStatus, SubOffer subOffer, EShop eShop, Long relId)
	{
		super();
		this.startDate = startDate;
		this.subOffer = subOffer;
		this.eShop = eShop;
		this.subStatus = GenericStatus.PENDING_PAYMENT;
		this.relId = relId;
		this.payments = new ArrayList<Payment>();
	}
	
	public ShopSub( ShopSub shopSub, EShop eShop)
	{
		super();
		this.startDate = shopSub.startDate;
		this.subOffer = shopSub.subOffer;
		this.eShop = eShop;
		this.subStatus = GenericStatus.PENDING_PAYMENT;
		this.payments = new ArrayList<Payment>();
	}	
	
	public ShopSub( ShopSub shopSub)
	{
		super();
		this.startDate = shopSub.startDate;
		this.subOffer = shopSub.subOffer;
		this.subStatus = GenericStatus.PENDING_PAYMENT;
		this.payments = new ArrayList<Payment>();
	}

	public ShopSub()
	{
		super();
		this.subStatus = GenericStatus.PENDING_PAYMENT;
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

	public Timestamp getEndDate()
	{
		return endDate;
	}

	public void setEndDate(Timestamp endDate)
	{
		this.endDate = endDate;
	}
	@Description( "Never call this if SubOffer and StartDate are not set. You have been warned.")
	public void setEndDate()
	{
		this.endDate = Tools.computeEndDate(startDate, subOffer.getDurationType(), subOffer.getDuration());
	}
}
