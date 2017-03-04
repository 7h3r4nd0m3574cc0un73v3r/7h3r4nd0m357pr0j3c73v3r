package org.usayi.preta.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.usayi.preta.Views;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Audited
public class ShopStatus implements Serializable
{
	private static final long serialVersionUID = 7340260376947350523L;

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
	@Size( min = 3, max = 50, message="La taille doit être comprise etre {min} et {max}")
	@JsonView( Views.Public.class)
	private String description;
	
	@NotNull( message="Ce champ ne peut pas être vide")
	@JsonView( Views.Public.class)
	private Integer articleLimit;
	
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

	public final String getDescription()
	{
		return description;
	}

	public final void setDescription(String description)
	{
		this.description = description;
	}

	public final Integer getArticleLimit()
	{
		return articleLimit;
	}

	public final void setArticleLimit(Integer articleLimit)
	{
		this.articleLimit = articleLimit;
	}

	public final Boolean getIsDeleted()
	{
		return isDeleted;
	}

	public final void setIsDeleted(Boolean isDeleted)
	{
		this.isDeleted = isDeleted;
	}

	public ShopStatus(String title, String description, Integer articleLimit)
	{
		super();
		this.title = title;
		this.description = description;
		this.articleLimit = articleLimit;
	}

	public ShopStatus()
	{
		super();
	}
	
	public void cloneInstance( final ShopStatus shopStatus)
	{
		this.title = shopStatus.getTitle();
		this.description = shopStatus.getDescription();
		this.articleLimit = shopStatus.getArticleLimit();
	}

	public final Boolean getIsEnabled()
	{
		return isEnabled;
	}

	public final void setIsEnabled(Boolean isEnabled)
	{
		this.isEnabled = isEnabled;
	}
	
}
