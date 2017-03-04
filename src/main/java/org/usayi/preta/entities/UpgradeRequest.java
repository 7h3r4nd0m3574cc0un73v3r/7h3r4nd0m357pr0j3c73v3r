package org.usayi.preta.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.usayi.preta.Views;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Audited
public class UpgradeRequest implements Serializable
{
	private static final long serialVersionUID = 5279913297402592817L;

	@Id
	@NotAudited
	@GeneratedValue( strategy= GenerationType.IDENTITY)
	@JsonView( Views.Admin.class)
	private Long id;
	
	@NotNull
	@ManyToOne
	@JsonView( Views.Admin.class)
	@JoinColumn( referencedColumnName="id")
	private User buyer;
	
	@NotNull
	@JsonView( Views.Admin.class)
	private boolean seen = false;
	
	@Nullable
	@JsonView( Views.Admin.class)
	private Boolean validated;

	@Transient
	@JsonView( Views.Admin.class)
	private Timestamp regDate;
	
	public UpgradeRequest()
	{
		super();
	}

	public UpgradeRequest(User buyer)
	{
		super();
		this.buyer = buyer;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public User getBuyer()
	{
		return buyer;
	}

	public void setBuyer(User buyer)
	{
		this.buyer = buyer;
	}

	public boolean isSeen()
	{
		return seen;
	}

	public void setSeen(boolean seen)
	{
		this.seen = seen;
	}

	public boolean isValidated()
	{
		return validated;
	}

	public void setValidated(boolean validated)
	{
		this.validated = validated;
	}

	public Timestamp getRegDate()
	{
		return regDate;
	}

	public void setRegDate(Timestamp regDate)
	{
		this.regDate = regDate;
	}
	
}
