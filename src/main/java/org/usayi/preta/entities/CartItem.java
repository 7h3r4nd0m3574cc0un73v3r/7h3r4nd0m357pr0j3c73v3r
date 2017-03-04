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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.usayi.preta.Views;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Audited
public class CartItem implements Serializable
{
	private static final long serialVersionUID = -2596756135656877913L;

	@Id
	@GeneratedValue( strategy=GenerationType.IDENTITY)
	@NotAudited
	@JsonView( Views.Public.class)
	private Long id;

	@NotNull
	@JsonView( Views.Public.class)
	private Integer quantity = 1;
	
	@ManyToOne
	@JsonView( Views.Public.class)
	@JoinColumn( referencedColumnName="id", nullable=false)
	private Article article;
	
	@Fetch( FetchMode.SELECT)
	@JsonView( Views.Public.class)
	@ManyToMany( fetch=FetchType.EAGER)
	private Collection<OrderedArticleFeature> orderedArticleFeatures;

	@Transient
	private Boolean isSelected;
	
	@NotNull
	private Boolean isDeleted = false;
	
	public Article getArticle()
	{
		return article;
	}

	public void setArticle(Article article)
	{
		this.article = article;
	}

	public CartItem(Integer quantity, Article article)
	{
		super();
		this.quantity = quantity;
		this.article = article;
		this.isSelected = false;
		this.orderedArticleFeatures = new ArrayList<OrderedArticleFeature>();
	}

	public CartItem()
	{
		super();
		this.isSelected = false;
		this.orderedArticleFeatures = new ArrayList<OrderedArticleFeature>();
	}
	
	public void addOrderedArticleFeature( OrderedArticleFeature entity)
	{
		this.orderedArticleFeatures.add( entity);
	}

	public Integer getQuantity()
	{
		return quantity;
	}

	public void setQuantity(Integer quantity)
	{
		this.quantity = quantity;
	}
	
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Boolean getIsSelected()
	{
		return isSelected;
	}

	public void setIsSelected(Boolean isSelected)
	{
		this.isSelected = isSelected;
	}
	
	public void removeOrderedArticleFeature( OrderedArticleFeature entity)
	{
		this.orderedArticleFeatures.remove( entity);
	}

	public Collection<OrderedArticleFeature> getOrderedArticleFeatures()
	{
		return orderedArticleFeatures;
	}

	public Boolean getIsDeleted()
	{
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted)
	{
		this.isDeleted = isDeleted;
	}
}
