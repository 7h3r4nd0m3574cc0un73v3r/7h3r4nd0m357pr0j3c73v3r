package org.usayi.preta.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Audited
public class PaymentType implements Serializable
{
	private static final long serialVersionUID = 8044499349120058798L;

	@Id
	@GeneratedValue( strategy=GenerationType.IDENTITY)
	@NotAudited
	private Long id;
	
	@NotEmpty
	@NotNull
	private String title;
	
	@NotNull
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

	public final Boolean getIsEnabled()
	{
		return isEnabled;
	}

	public final void setIsEnabled(Boolean isEnabled)
	{
		this.isEnabled = isEnabled;
	}

	public PaymentType(String title)
	{
		super();
		this.title = title;
	}

	public PaymentType()
	{
		super();
	}
}
