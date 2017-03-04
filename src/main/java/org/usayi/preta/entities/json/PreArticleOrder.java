package org.usayi.preta.entities.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Transient;

import org.usayi.preta.Views;
import org.usayi.preta.entities.EShop;
import org.usayi.preta.entities.OrderedArticle;

import com.fasterxml.jackson.annotation.JsonView;

public class PreArticleOrder
{
	@JsonView( Views.Public.class)
	private Long id;
	
	@JsonView( Views.Public.class)
	private Collection<OrderedArticle> orderedArticles;

	@JsonView( Views.Public.class)
	private EShop eShop;

	@JsonView( Views.Public.class)
	private String deliveryAddress;
	
	@JsonView( Views.Public.class)
	private boolean useDefaultDeliveryAddress;
	
	@JsonView( Views.Public.class)
	private List<Long> cartItemIds;
	
	@Transient
	private Float total;
	
	public Long getId()
	{
		return id;
	}

	public PreArticleOrder()
	{
		super();
		this.orderedArticles = new ArrayList<OrderedArticle>();
		this.cartItemIds = new ArrayList<Long>();
	}

	public PreArticleOrder(Long id, Collection<OrderedArticle> orderedArticles)
	{
		super();
		this.id = id;
		this.orderedArticles = orderedArticles;
		this.cartItemIds = new ArrayList<Long>();
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Collection<OrderedArticle> getOrderedArticles()
	{
		return orderedArticles;
	}

	public void setOrderedArticles(Collection<OrderedArticle> orderedArticles)
	{
		this.orderedArticles = orderedArticles;
	}

	public void addOrderedArticle( OrderedArticle orderedArticle)
	{
		this.orderedArticles.add( orderedArticle);
	}

	public EShop geteShop()
	{
		return eShop;
	}

	public void seteShop(EShop eShop)
	{
		this.eShop = eShop;
	}

	public Float getTotal()
	{
		return total;
	}

	public void setTotal(Float total)
	{
		this.total = total;
	}

	public List<Long> getCartItemIds()
	{
		return cartItemIds;
	}
	
	public void addCartItemId( long id)
	{
		this.cartItemIds.add( id);
	}

	public String getDeliveryAddress()
	{
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress)
	{
		this.deliveryAddress = deliveryAddress;
	}

	public boolean isUseDefaultDeliveryAddress()
	{
		return useDefaultDeliveryAddress;
	}

	public void setUseDefaultDeliveryAddress(boolean useDefaultDeliveryAddress)
	{
		this.useDefaultDeliveryAddress = useDefaultDeliveryAddress;
	}
}
