package org.usayi.preta.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.usayi.preta.Views;
import org.usayi.preta.service.GoogleDriveServiceImpl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Audited
public class Picture implements Serializable
{
	private static final long serialVersionUID = -7165143500812153923L;

	@Id
	@GeneratedValue( strategy=GenerationType.IDENTITY)
	@NotAudited
	@JsonView( Views.Public.class)
	private Long id;
	
	@NotNull
	@JsonIgnore
	private String filename;
	
	@JsonView( Views.Public.class)
	private String googleId = GoogleDriveServiceImpl.defaultArticlePicId;
	
	@JsonView( Views.Public.class)
	private Boolean isDefault = false;
	
	@JsonIgnore
	private Boolean isDeleted = false;

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

	public Boolean getIsDefault()
	{
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault)
	{
		this.isDefault = isDefault;
	}

	public Boolean getIsDeleted()
	{
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted)
	{
		this.isDeleted = isDeleted;
	}

	public Picture()
	{
		super();
	}

	public Picture(String filename)
	{
		super();
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
}
