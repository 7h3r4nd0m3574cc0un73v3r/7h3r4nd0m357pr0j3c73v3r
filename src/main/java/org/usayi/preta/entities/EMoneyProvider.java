package org.usayi.preta.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.usayi.preta.Views;
import org.usayi.preta.service.GoogleDriveServiceImpl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Audited
public class EMoneyProvider implements Serializable
{
	private static final long serialVersionUID = 552887938176697424L;
	
	@Id
	@GeneratedValue( strategy= GenerationType.IDENTITY)
	@JsonView( Views.Public.class)
	@NotAudited
	private Long id;
	
	public EMoneyProvider()
	{
		super();
	}

	@NotNull( message="Ce champ ne peut pas être vide")
	@Size( min= 3 , max = 30, message="La taille doit être comprise etre {min} et {max}")
	@JsonView( Views.Public.class)
	private String name;
	
	@JsonView( Views.Admin.class)
	@NotNull( message="Ce champ ne peut pas être vide")
	private String numPattern;
	
	@JsonIgnore
	private String logoFile;
	
	@NotNull
	@JsonView( Views.Public.class)
	private String logoGoogleId = GoogleDriveServiceImpl.defaultEmpPicId;
	
	@JsonView( Views.Admin.class)
	private Boolean isDeleted = false;
	
	public final Long getId()
	{
		return id;
	}

	public final void setId(Long id)
	{
		this.id = id;
	}

	public final String getName()
	{
		return name;
	}

	public final void setName(String name)
	{
		this.name = name;
	}

	public final String getNumPattern()
	{
		return numPattern;
	}

	public final void setNumPattern(String numPattern)
	{
		this.numPattern = numPattern;
	}

	public final String getLogoFile()
	{
		return logoFile;
	}
	
	@Transient
	@JsonView( Views.Public.class)
	public final Boolean hasLogo()
	{
		return this.logoFile == null ? false : true;
	}
	
	public final Boolean getHasLogo()
	{
		return this.hasLogo();
	}
	
	public final void setHasLogo( Boolean hasLogo)
	{
		
	}

	public final void setLogoFile(String logoFile)
	{
		this.logoFile = logoFile;
	}

	public final Boolean getIsDeleted()
	{
		return isDeleted;
	}

	public final void setIsDeleted(Boolean isDeleted)
	{
		this.isDeleted = isDeleted;
	}

	public EMoneyProvider(String name, String numPattern, String logoFile)
	{
		super();
		this.name = name;
		this.numPattern = numPattern;
		this.logoFile = logoFile;
	}

	public String getLogoGoogleId()
	{
		return logoGoogleId;
	}

	public void setLogoGoogleId(String logoGoogleId)
	{
		this.logoGoogleId = logoGoogleId;
	}
}
 