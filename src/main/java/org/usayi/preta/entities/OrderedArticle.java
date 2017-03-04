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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.usayi.preta.Views;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Audited
public class OrderedArticle implements Serializable
{
	private static final long serialVersionUID = -1241240092504845126L;

	@Id	
	@NotAudited
	@JsonView( Views.Public.class)
	@GeneratedValue( strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@JsonView( Views.Public.class)
	private Integer quantity = 1;
	
	@Min( value=0)
	@Max( value=5)
	@JsonView( Views.Public.class)
	private Integer rating;
	
	@ManyToOne
	@JsonView( Views.Public.class)
	@JoinColumn( referencedColumnName="id")
	private Article article;
	
	@Fetch( FetchMode.SELECT)
	@JsonView( Views.Public.class)
	@ManyToMany( fetch=FetchType.EAGER)
	private Collection<OrderedArticleFeature> orderedArticleFeatures;
	
	@ManyToOne
	@JsonView( Views.Public.class)
	private ArticleOrder articleOrder;
	
	public OrderedArticle(Integer quantity, Integer rating, ArticleOrder articleOrder, Article article)
	{
		super();
		this.quantity = quantity;
		this.rating = rating;
		this.article = article;
		this.orderedArticleFeatures = new ArrayList<OrderedArticleFeature>();
	}

	public OrderedArticle()
	{
		super();
		this.orderedArticleFeatures = new ArrayList<OrderedArticleFeature>();
	}
	
	public OrderedArticle( final CartItem entity)
	{
		super();
		this.article = entity.getArticle();
		this.quantity = entity.getQuantity();
		this.orderedArticleFeatures = new ArrayList<OrderedArticleFeature>();
	}
	
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Integer getRating()
	{
		return rating;
	}

	public void setRating(Integer rating)
	{
		this.rating = rating;
	}

	public Article getArticle()
	{
		return article;
	}

	public void setArticle(Article article)
	{
		this.article = article;
	}

	public ArticleOrder getArticleOrder()
	{
		return articleOrder;
	}

	public void setArticleOrder(ArticleOrder articleOrder)
	{
		this.articleOrder = articleOrder;
	}

	public Integer getQuantity()
	{
		return quantity;
	}

	public void setQuantity(Integer quantity)
	{
		this.quantity = quantity;
	}

	public Collection<OrderedArticleFeature> getOrderedArticleFeatures()
	{
		return orderedArticleFeatures;
	}

	public void setOrderedArticleFeatures(Collection<OrderedArticleFeature> orderedArticleFeatures)
	{
		this.orderedArticleFeatures = orderedArticleFeatures;
	}
}
