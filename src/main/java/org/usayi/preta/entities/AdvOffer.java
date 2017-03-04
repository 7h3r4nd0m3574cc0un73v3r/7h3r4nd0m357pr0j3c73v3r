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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.usayi.preta.Views;
import org.usayi.preta.config.Tools;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Audited
public class AdvOffer implements Serializable
{
	private static final long serialVersionUID = 3360758388924553580L;

	@Id
	@NotAudited
	@GeneratedValue( strategy=GenerationType.IDENTITY)
	@JsonView( { Views.Manager.class, Views.Admin.class })
	private Long id;
	
	@NotNull
	@JsonView( { Views.Manager.class, Views.Admin.class })
	private Long relId;
	
	@NotNull
	@JsonView( { Views.Manager.class, Views.Admin.class })
	private GenericStatus status = GenericStatus.PENDING_PAYMENT_CONFIRMATION;
	
	@NotNull
	@JsonView( { Views.Manager.class, Views.Admin.class })
	private Timestamp startDate;
	
	@NotNull
	@JsonView( { Views.Manager.class, Views.Admin.class })
	private Timestamp endDate;

	@NotNull
	@JsonView( { Views.Manager.class, Views.Admin.class })
	private Integer code = 1;
	
	@ManyToOne( fetch=FetchType.EAGER)
	@JsonView( { Views.Manager.class, Views.Admin.class })
	@JoinColumn( referencedColumnName="id", nullable=false)
	private AdvOption advOption;
	
	@JsonIgnore
	@OneToMany( mappedBy="advOffer")
	@JsonView( { Views.Manager.class, Views.Admin.class })
	private Collection<Payment> payments;

	@ManyToOne
	@JsonView( { Views.Manager.class, Views.Admin.class })
	@JoinColumn( referencedColumnName="id", nullable=false)
	private Article article;
	
	@NotNull
	@JsonView( { Views.Manager.class, Views.Admin.class})
	private boolean autoEnabled = true;
	
	@Transient
	@JsonView( { Views.Manager.class, Views.Admin.class})
	private Timestamp regDate;
	
	public final Long getId()
	{
		return id;
	}

	public final void setId(Long id)
	{
		this.id = id;
	}

	public final GenericStatus getStatus()
	{
		return status;
	}

	public final void setStatus(GenericStatus status)
	{
		this.status = status;
	}

	public final Timestamp getStartDateTime()
	{
		return startDate;
	}

	public final void setStartDateTime(Timestamp startDateTime)
	{
		this.startDate = startDateTime;
	}

	public final Integer getCode()
	{
		return code;
	}

	public final void setCode(Integer code)
	{
		this.code = code;
	}

	public final AdvOption getAdvOption()
	{
		return advOption;
	}

	public final void setAdvOption(AdvOption advOption)
	{
		this.advOption = advOption;
	}

	public final Collection<Payment> getPayments()
	{
		return payments;
	}
	
	public final void addPayment( final Payment payment)
	{
		this.payments.add( payment);
	}
	
	public final void removePayment( final Payment payment)
	{
		this.payments.remove( payment);
	}

	public AdvOffer(Timestamp startDate, Integer code, AdvOption advOption)
	{
		super();
		this.startDate = startDate;
		this.code = code;
		this.advOption = advOption;
		this.payments = new ArrayList<Payment>();
	}

	public AdvOffer()
	{
		super();
		this.payments = new ArrayList<Payment>();
	}

	public Article getArticle()
	{
		return article;
	}

	public void setArticle(Article article)
	{
		this.article = article;
	}

	public Long getRelId()
	{
		return relId;
	}

	public void setRelId(Long relId)
	{
		this.relId = relId;
	}

	public Timestamp getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Timestamp startDate)
	{
		this.startDate = startDate;
	}

	public boolean isAutoEnabled()
	{
		return autoEnabled;
	}

	public void setAutoEnabled(boolean autoEnabled)
	{
		this.autoEnabled = autoEnabled;
	}

	public Timestamp getRegDate()
	{
		return regDate;
	}

	public void setRegDate(Timestamp regDate)
	{
		this.regDate = regDate;
	}

	public void setEndDate(Timestamp endDate)
	{
		this.endDate = endDate;
	}
	
	public void setEndDate() {
		this.endDate = Tools.computeEndDate(startDate, advOption.getDurationType(), advOption.getDuration());
	}
	
	/* TODO: Remove, DEBUG Only */
	public void setEndDateSpecial() {
		this.endDate = new Timestamp( startDate.getTime() + 180000L);
	}

	public Timestamp getEndDate()
	{
		return endDate;
	}
}
