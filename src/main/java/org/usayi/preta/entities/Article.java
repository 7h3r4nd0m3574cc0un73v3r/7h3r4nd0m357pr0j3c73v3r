package org.usayi.preta.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.usayi.preta.Views;
import org.usayi.preta.entities.json.CategoryMultiSelectTreeJSON;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Audited
public class Article implements Serializable
{
	private static final long serialVersionUID = -949695489683775250L;
	
	@Id
	@GeneratedValue( strategy=GenerationType.IDENTITY)
	@NotAudited
	@JsonView( Views.Public.class)
	private Long id;
	
	@NotNull
	@NotAudited
	@JsonView( Views.Manager.class)
	private Long relId;
	
	@NotNull
	@Size( min = 3, max = 70)
	@JsonView( Views.Public.class)
	private String name;
	
	@NotNull
	@DecimalMin( value="1")
	@JsonView( Views.Public.class)
	private Float price;
	
	@DecimalMin( value="0")
	@JsonView( Views.Public.class)
	private Float discountPrice = 0F;
	
	@JsonView( Views.Public.class)
	private Boolean isDiscounted = false;

	@DecimalMin( value="1")
	@JsonView( Views.Public.class)
	private Integer stock = 0;
	
	@JsonView( Views.Manager.class)
	private Integer threshold = 1;
	
	@JsonView( Views.Public.class)
	private String description;
	
	@JsonView( Views.Public.class)
	private ArticleState state;
	
	@JsonView( Views.Public.class)
	private DeliveryMode deliveryMode;
	
	@JsonView( Views.Public.class)
	private String deliveryTerms;
	
	@JsonView( Views.Public.class)
	private Float deliveryFee = 0F;
	
	@JsonView( Views.Public.class)
	@Column( length=30)
	@ElementCollection( fetch=FetchType.EAGER)
	/* TODO On update */
	private Collection<String> keywords = new ArrayList<String>();
	
	@JsonView( Views.Manager.class)
	private Boolean isDisplayed = true;
	
	@JsonView( Views.Admin.class)
	private Boolean isDeleted = false;
	
	@ManyToMany( fetch=FetchType.LAZY)
	private Collection<Category> categories;
	
	@Transient
	private Collection<CategoryMultiSelectTreeJSON> selectedCategories;
	
	@OneToMany( orphanRemoval=true, fetch=FetchType.LAZY)
	private Collection<Picture> pictures;
	
	@ManyToMany( fetch=FetchType.LAZY)
	private Collection<ArticleFeature> features;
	
	@ManyToOne
	private EShop eShop;
	
	@Transient
	@JsonView( Views.Public.class)
	private Integer rating;
	
	@Transient
	@JsonView( Views.Public.class)
	private Long saleNumber;
	
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Float getPrice()
	{
		return price;
	}

	public void setPrice(Float price)
	{
		this.price = price;
	}

	public Float getDiscountPrice()
	{
		return discountPrice;
	}

	public void setDiscountPrice(Float discountPrice)
	{
		this.discountPrice = discountPrice;
	}

	public Integer getStock()
	{
		return stock;
	}

	public void setStock(Integer stock)
	{
		this.stock = stock;
	}

	public Integer getThreshold()
	{
		return threshold;
	}

	public void setThreshold(Integer threshold)
	{
		this.threshold = threshold;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public ArticleState getState()
	{
		return state;
	}

	public void setState(ArticleState state)
	{
		this.state = state;
	}

	public DeliveryMode getDeliveryMode()
	{
		return deliveryMode;
	}

	public void setDeliveryMode(DeliveryMode deliveryMode)
	{
		this.deliveryMode = deliveryMode;
	}

	public String getDeliveryTerms()
	{
		return deliveryTerms;
	}

	public void setDeliveryTerms(String deliveryTerms)
	{
		this.deliveryTerms = deliveryTerms;
	}

	public Boolean getIsDisplayed()
	{
		return isDisplayed;
	}

	public void setIsDisplayed(Boolean isDisplayed)
	{
		this.isDisplayed = isDisplayed;
	}

	public Boolean getIsDeleted()
	{
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted)
	{
		this.isDeleted = isDeleted;
	}
	
	public Boolean getIsDiscounted()
	{
		return isDiscounted;
	}

	public void setIsDiscounted(Boolean isDiscounted)
	{
		this.isDiscounted = isDiscounted;
	}

	public Article(String name, Float price, Integer stock, Integer threshold, String description, ArticleState state,
			DeliveryMode deliveryMode, String deliveryTerms, Collection<String> keywords)
	{
		super();
		this.name = name;
		this.price = price;
		this.stock = stock;
		this.threshold = threshold;
		this.description = description;
		this.state = state;
		this.deliveryMode = deliveryMode;
		this.deliveryTerms = deliveryTerms;
		this.categories = new ArrayList<Category>();
		this.pictures = new ArrayList<Picture>();
		this.features = new ArrayList<ArticleFeature>();
		this.selectedCategories = new ArrayList<CategoryMultiSelectTreeJSON>();
		this.keywords = keywords;
	}

	public Article()
	{
		super();
		this.state = ArticleState.NEW;
		this.categories = new ArrayList<Category>();
		this.pictures = new ArrayList<Picture>();
		this.features = new ArrayList<ArticleFeature>();
		this.selectedCategories = new ArrayList<CategoryMultiSelectTreeJSON>();
	}

	@JsonIgnore
	public Collection<Category> getCategories()
	{
		return categories;
	}

	@JsonIgnore
	public Collection<Picture> getPictures()
	{
		return pictures;
	}
	
	public void addCategory( final Category category)
	{
		this.categories.add( category);
	}
	
	public void removecategory( final Category category)
	{
		this.categories.remove( category);
	}
	
	public void addPicture( final Picture picture)
	{
		this.pictures.add( picture);
	}
	
	public void removePicture( final Picture picture)
	{
		this.pictures.remove( picture);
	}

	@JsonIgnore
	@JsonGetter
	public EShop geteShop()
	{
		return eShop;
	}
	
	@JsonSetter
	public void seteShop(EShop eShop)
	{
		this.eShop = eShop;
	}
	
	public void addFeature( ArticleFeature feature)
	{
		this.features.add( feature);
	}

	@JsonSetter
	public void setFeatures(Collection<ArticleFeature> features)
	{
		this.features = features;
	}

	public Integer getRating()
	{
		return rating;
	}

	public void setRating(Integer rating)
	{
		this.rating = rating;
	}

	public Long getSaleNumber()
	{
		return saleNumber;
	}

	public void setSaleNumber(Long saleNumber)
	{
		this.saleNumber = saleNumber;
	}

	public Collection<CategoryMultiSelectTreeJSON> getSelectedCategories()
	{
		return selectedCategories;
	}

	public void setSelectedCategories(Collection<CategoryMultiSelectTreeJSON> selectedCategories)
	{
		this.selectedCategories = selectedCategories;
	}
	
	@Transient
	@JsonIgnore
	public Picture getPictureByIndex( Integer index)
	{
		Integer tmp = 0;
		
		for( Picture picture : this.pictures)
		{
			if( tmp == index) {
				return picture;
			}
			tmp++;
		}
		
		return null;
	}
	
	@Transient
	@JsonIgnore
	public Picture getDefaultPicture()
	{
		for( Picture picture : this.pictures)
			if( picture.getIsDefault())
				return picture;
		
		return null;
	}
	
	public void updateDefaultPicture()
	{
		if( !this.pictures.isEmpty())
		{
			getPictureByIndex( 0).setIsDefault( true);
		}
	}

	@JsonIgnore
	@JsonGetter
	public Collection<ArticleFeature> getFeatures()
	{
		return features;
	}

	@JsonSetter
	public void setCategories(Collection<Category> categories)
	{
		this.categories = categories;
	}

	@JsonSetter
	public void setPictures(Collection<Picture> pictures)
	{
		this.pictures = pictures;
	}

	public Long getRelId()
	{
		return relId;
	}

	public void setRelId(Long relId)
	{
		this.relId = relId;
	}

	public Float getDeliveryFee()
	{
		return deliveryFee;
	}

	public void setDeliveryFee(Float deliveryFee)
	{
		this.deliveryFee = deliveryFee;
	}
}