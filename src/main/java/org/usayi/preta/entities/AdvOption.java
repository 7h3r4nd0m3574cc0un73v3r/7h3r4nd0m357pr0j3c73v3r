package org.usayi.preta.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.usayi.preta.Views;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Audited
@Table( uniqueConstraints= @UniqueConstraint( columnNames = { "title"} ))
public class AdvOption implements Serializable
{
	private static final long serialVersionUID = -12474521639410953L;

	@Id
	@GeneratedValue( strategy=GenerationType.IDENTITY)
	@NotAudited
	@JsonView( Views.Public.class)
	private Long id;
	
	@NotNull
	@JsonView( Views.Public.class)
	private String title;
	
	@NotNull
	@JsonView( Views.Public.class)
	private Integer duration;
	
	@NotNull
	@JsonView( Views.Public.class)
	private DurationType durationType;
	
	@NotNull
	@JsonView( Views.Public.class)
	private Float price;
	
	@NotNull
	private Boolean isDeleted = false;

	@NotNull
	@JsonView( Views.Public.class)
	private Boolean isEnabled = true;
	
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

	public final Float getPrice()
	{
		return price;
	}

	public final void setPrice(Float price)
	{
		this.price = price;
	}

	public final Boolean getIsDeleted()
	{
		return isDeleted;
	}

	public final void setIsDeleted(Boolean isDeleted)
	{
		this.isDeleted = isDeleted;
	}

	public AdvOption(String title, Integer duration, DurationType durationType, Float price)
	{
		super();
		this.title = title;
		this.duration = duration;
		this.durationType = durationType;
		this.price = price;
	}

	public AdvOption()
	{
		super();
	}

	public Boolean getIsEnabled()
	{
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled)
	{
		this.isEnabled = isEnabled;
	}
	
	public void cloneInstance( final AdvOption entity)
	{
		this.title = entity.getTitle();
		this.duration = entity.getDuration();
		this.durationType = entity.getDurationType();
		this.price = entity.getPrice();
		this.isEnabled = entity.getIsEnabled();
	}
}
