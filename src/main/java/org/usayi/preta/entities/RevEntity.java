package org.usayi.preta.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;
import org.usayi.preta.Views;
import org.usayi.preta.config.RevisionListener;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@RevisionEntity( RevisionListener.class)
@Table( name="revinfo")
public class RevEntity
{
	@Id
	@GeneratedValue( strategy=GenerationType.IDENTITY)
	@RevisionNumber
	@JsonView( Views.Public.class)
	@JsonSerialize
	private Long id;
	
	@RevisionTimestamp
	@JsonView( Views.Public.class)
	private Long timestamp;
	
	@ManyToOne
	@JoinColumn( referencedColumnName="id")
	@JsonView( Views.Public.class)
	private User user;
	
	public final Long getId()
	{
		return id;
	}

	public final void setId(Long id)
	{
		this.id = id;
	}

	public final Long getTimestamp()
	{
		return timestamp;
	}

	public final void setTimestamp(Long timestamp)
	{
		this.timestamp = timestamp;
	}

	public final User getUser()
	{
		return user;
	}

	public final void setUser(User user)
	{
		this.user = user;
	}

	public RevEntity()
	{
		super();
	}

	public RevEntity(Long id, Long timestamp, User user)
	{
		super();
		this.id = id;
		this.timestamp = timestamp;
		this.user = user;
	}
}
