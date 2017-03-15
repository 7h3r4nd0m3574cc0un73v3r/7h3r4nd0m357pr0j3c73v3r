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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.usayi.preta.Views;
import org.usayi.preta.entities.json.PreArticleOrder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Audited
public class ArticleOrder implements Serializable
{
	private static final long serialVersionUID = -8713646174411184090L;

	@Id
	@NotAudited
	@JsonView( Views.Public.class)
	@GeneratedValue( strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@JsonView( Views.Public.class)
	private Long buyerRelId;
	
	@NotNull
	@JsonView( Views.Manager.class)
	private Long eShopRelId;
	
	@NotNull
	@JsonView( Views.Public.class)
	private OrderStatus status;
	
	@JsonView( Views.Public.class)
	private Float deliveryFee;
	
	@JsonView( Views.Public.class)
	private String packageId;
	
	@JsonView( Views.Public.class)
	private Timestamp deliveryDate;
	
	@JsonView( Views.Public.class)
	private Timestamp receptionDate;
	
	@JsonView( Views.Public.class)
	private String deliveryAddress;
	
	@JsonView( Views.Public.class)
	private boolean useDefaultDeliveryAddress;
	
	@OneToOne
	@JsonView( Views.Admin.class)
	@JoinColumn( referencedColumnName="id")
	private Expense expense;
	
	@JsonIgnore
	@OneToMany( mappedBy="articleOrder")
	private Collection<OrderedArticle> orderedArticles;
	
	@JsonIgnore
	@ManyToMany( mappedBy="articleOrders")
	private Collection<Payment> payments;
	
	@ManyToOne
	@JsonIgnore
	private User user;

	@Transient
	@JsonView( Views.Public.class)
	private Timestamp regDate;
	
	public final Long getId()
	{
		return id;
	}

	public final void setId(Long id)
	{
		this.id = id;
	}

	public final OrderStatus getStatus()
	{
		return status;
	}

	public final void setStatus(OrderStatus status)
	{
		this.status = status;
	}

	public final Float getDeliveryFee()
	{
		return deliveryFee;
	}

	public final void setDeliveryFee(Float deliveryFee)
	{
		this.deliveryFee = deliveryFee;
	}

	public final String getPackageId()
	{
		return packageId;
	}

	public final void setPackageId(String packageId)
	{
		this.packageId = packageId;
	}

	public final Timestamp getDeliveryDate()
	{
		return deliveryDate;
	}

	public final void setDeliveryDate(Timestamp deliveryDate)
	{
		this.deliveryDate = deliveryDate;
	}

	public final Timestamp getReceptionDate()
	{
		return receptionDate;
	}

	public final void setReceptionDate(Timestamp receptionDate)
	{
		this.receptionDate = receptionDate;
	}

	public final Expense getExpense()
	{
		return expense;
	}

	public final void setExpense(Expense expense)
	{
		this.expense = expense;
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

	public ArticleOrder(OrderStatus status, Float deliveryFee)
	{
		super();
		this.status = status;
		this.deliveryFee = deliveryFee;
		this.status = OrderStatus.PENDING_PAYMENT;
		this.orderedArticles =  new ArrayList<OrderedArticle>();
		this.payments = new ArrayList<Payment>();
	}

	public ArticleOrder()
	{
		super();
		this.status = OrderStatus.PENDING_PAYMENT;
		this.orderedArticles =  new ArrayList<OrderedArticle>();
		this.payments = new ArrayList<Payment>();
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public Collection<OrderedArticle> getOrderedArticles()
	{
		return orderedArticles;
	}
	
	public void addOrderedArticle( final OrderedArticle entity)
	{
		this.orderedArticles.add( entity);
	}
	
	public void removeOrderedArticle( final OrderedArticle entity)
	{
		this.orderedArticles.remove( entity);
	}

	public Timestamp getRegDate()
	{
		return regDate;
	}

	public ArticleOrder( PreArticleOrder entity)
	{
		this.orderedArticles = entity.getOrderedArticles();
		this.payments = new ArrayList<Payment>();
		this.deliveryAddress = entity.getDeliveryAddress();
		this.useDefaultDeliveryAddress = entity.isUseDefaultDeliveryAddress();
	}

	public void setRegDate(Timestamp regDate)
	{
		this.regDate = regDate;
	}

	public Long getBuyerRelId()
	{
		return buyerRelId;
	}

	public void setBuyerRelId(Long buyerRelId)
	{
		this.buyerRelId = buyerRelId;
	}

	public Long geteShopRelId()
	{
		return eShopRelId;
	}

	public void seteShopRelId(Long eShopRelId)
	{
		this.eShopRelId = eShopRelId;
	}

	public boolean useDefaultDeliveryAddress()
	{
		return useDefaultDeliveryAddress;
	}

	public void setUseDefaultDeliveryAddress(boolean defaultDeliveryAddress)
	{
		this.useDefaultDeliveryAddress = defaultDeliveryAddress;
	}

	public String getDeliveryAddress()
	{
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress)
	{
		this.deliveryAddress = deliveryAddress;
	}
}