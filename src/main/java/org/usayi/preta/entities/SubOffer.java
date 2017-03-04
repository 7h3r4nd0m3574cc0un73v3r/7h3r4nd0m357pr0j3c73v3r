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
import org.usayi.preta.Views;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Audited
public class SubOffer implements Serializable
{
	private static final long serialVersionUID = 3745643437611535420L;

	@Id
	@GeneratedValue( strategy= GenerationType.IDENTITY)
	@NotAudited
	@JsonView( Views.Public.class)
	private Long id;
	
	@NotNull( message="Ce champ ne peut pas être vide")
	@Size( min = 3, max = 30, message="La taille doit être comprise etre {min} et {max}")
	@JsonView( Views.Public.class)
	private String title;
	
	@NotNull( message="Ce champ ne peut pas être vide")
	@JsonView( Views.Public.class)
	private Float price;
	
	@NotNull( message="Ce champ ne peut pas être vide")
	@JsonView( Views.Public.class)
	private Integer duration;
	
	@NotNull( message="Ce champ ne peut pas être vide")
	@JsonView( Views.Public.class)
	private DurationType durationType;
	
	@NotNull
	@JsonView( Views.Public.class)
	private Boolean isEnabled = true;
	
	@NotNull
	private Boolean isDeleted = false;
	
	@NotNull( message="Ce champ ne peut pas être vide")
	@ManyToOne
	@JoinColumn( referencedColumnName = "id", nullable = false)
	@JsonView( Views.Public.class)
	private ShopStatus shopStatus;

	public final Long getId()
	{
		return id;
	}

	public final void setId(Long id)
	{
		this.id = id;
	}

	public final String getTitle()
	{
		return title;
	}

	public final void setTitle(String title)
	{
		this.title = title;
	}

	public final Float getPrice()
	{
		return price;
	}

	public final void setPrice(Float price)
	{
		this.price = price;
	}

	public final Integer getDuration()
	{
		return duration;
	}

	public final void setDuration(Integer duration)
	{
		this.duration = duration;
	}

	public final DurationType getDurationType()
	{
		return durationType;
	}

	public final void setDurationType(DurationType durationType)
	{
		this.durationType = durationType;
	}

	public final Boolean getIsDeleted()
	{
		return isDeleted;
	}

	public final void setIsDeleted(Boolean isDeleted)
	{
		this.isDeleted = isDeleted;
	}

	public final ShopStatus getShopStatus()
	{
		return shopStatus;
	}

	public final void setShopStatus(ShopStatus shopStatus)
	{
		this.shopStatus = shopStatus;
	}

	public SubOffer(String title, Float price, Integer duration, DurationType durationType,
			ShopStatus shopStatus)
	{
		super();
		this.title = title;
		this.price = price;
		this.duration = duration;
		this.durationType = durationType;
		this.shopStatus = shopStatus;
	}

	public SubOffer()
	{
		super();
	}
	
	public void cloneInstance( final SubOffer entity)
	{
		this.title = entity.getTitle();
		this.duration = entity.getDuration();
		this.durationType = entity.getDurationType();
		this.price = entity.getPrice();
		this.shopStatus = entity.getShopStatus();
	}

	public Boolean getIsEnabled()
	{
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled)
	{
		this.isEnabled = isEnabled;
	}
}
