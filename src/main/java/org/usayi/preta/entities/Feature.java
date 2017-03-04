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
public class Feature implements Serializable
{
	private static final long serialVersionUID = -7426697668456855046L;

	@Id
	@NotAudited
	@JsonView( Views.Public.class)
	@GeneratedValue( strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Size( max = 30, min = 3)
	@JsonView( Views.Public.class)
	private String label;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public Feature()
	{
		super();
	}

	public Feature(String label)
	{
		super();
		this.label = label;
	}
}
