package org.usayi.preta.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.usayi.preta.Views;
import org.usayi.preta.service.GoogleDriveServiceImpl;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Audited
public class Slide
{
	@Id
	@NotAudited
	@JsonView( { Views.Admin.class, Views.Public.class })
	@GeneratedValue( strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String filename;
	
	@JsonView( { Views.Admin.class, Views.Public.class })
	private String googleId = GoogleDriveServiceImpl.defaultSlideId;

	@NotNull
	@JsonView( Views.Admin.class)
	private boolean displayed = true;
	
	@NotNull
	private boolean deleted = false;
	
	@NotNull
	@JsonView( Views.Admin.class)
	private Integer displayOrder = 0;
	
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getFilename()
	{
		return filename;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public String getGoogleId()
	{
		return googleId;
	}

	public void setGoogleId(String googleId)
	{
		this.googleId = googleId;
	}

	public Slide()
	{
		super();
	}

	public Slide(String filename, String googleId, boolean displayed, boolean deleted, Integer displayOrder)
	{
		super();
		this.filename = filename;
		this.googleId = googleId;
		this.displayed = displayed;
		this.deleted = deleted;
		this.displayOrder = displayOrder;
	}

	public boolean isDisplayed()
	{
		return displayed;
	}

	public void setDisplayed(boolean displayed)
	{
		this.displayed = displayed;
	}

	public boolean isDeleted()
	{
		return deleted;
	}

	public void setDeleted(boolean deleted)
	{
		this.deleted = deleted;
	}

	public Integer getDisplayOrder()
	{
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder)
	{
		this.displayOrder = displayOrder;
	}
}
