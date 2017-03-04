package org.usayi.preta.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Pattern;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.usayi.preta.Views;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Audited
@Table( uniqueConstraints = @UniqueConstraint( columnNames={"name"}))
public class Role implements GrantedAuthority, Serializable
{
	private static final long serialVersionUID = 3212262086398120239L;
	
	@Id
	@GeneratedValue( strategy= GenerationType.IDENTITY)
	@NotAudited
	private Long id;
	
	@NotEmpty
	@Pattern( regexp="^ROLE_.*")
	@JsonView( Views.Public.class)
	private String name;

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

	public Role(String name)
	{
		super();
		this.name = name;
	}

	public Role()
	{
		super();
	}

	@Override
	public String getAuthority()
	{
		return this.name;
	}
}
