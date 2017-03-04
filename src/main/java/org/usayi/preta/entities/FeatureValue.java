package org.usayi.preta.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.Length;
import org.usayi.preta.Views;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Audited
@Table( uniqueConstraints= @UniqueConstraint( columnNames = { "value"} ))
public class FeatureValue implements Serializable
{
	private static final long serialVersionUID = 3475884577294344887L;

	@Id
	@GeneratedValue( strategy=GenerationType.IDENTITY)
	@NotAudited
	@JsonView( Views.Public.class)
	private Long id;
	
	@NotNull
	@Length( min=1, max=20)
	@JsonView( Views.Public.class)
	private String value;

	@Transient
	@JsonView( Views.Public.class)
	private Boolean isSelected = false;
	
	@JsonView( Views.Admin.class)
	private Boolean isDeleted = false;
	
	public FeatureValue(String value)
	{
		super();
		this.value = value;
	}

	public FeatureValue()
	{
		super();
	}

	public FeatureValue(Long id, String value, Boolean isSelected, Boolean isDeleted)
	{
		super();
		this.id = id;
		this.value = value;
		this.isSelected = isSelected;
		this.isDeleted = isDeleted;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	@JsonGetter
	public String getValue()
	{
		return value;
	}

	@JsonSetter
	public void setValue(String value)
	{
		this.value = value;
	}

	@JsonGetter
	public Boolean getIsSelected()
	{
		return isSelected;
	}

	@JsonSetter
	@JsonProperty( value="isSelected")
	public void setIsSelected(Boolean isSelected)
	{
		this.isSelected = isSelected;
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
