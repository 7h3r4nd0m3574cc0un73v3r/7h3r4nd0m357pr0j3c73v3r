package org.usayi.preta.entities;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.usayi.preta.Views;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class VisitedArticle
{
	@Id
	@GeneratedValue( strategy= GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@JsonView( Views.Public.class)
	private Timestamp visitTime = new Timestamp( System.currentTimeMillis());
	
	@NotNull
	@JsonView( Views.Public.class)
	private Long articleId;
	
	@NotNull
	@ManyToOne
	@JsonView( Views.Admin.class)
	private User user;
	
	public Long getId()
	{
		return id;
	}

	public Long getArticleId()
	{
		return articleId;
	}

	public void setArticleId(Long articleId)
	{
		this.articleId = articleId;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public Timestamp getVisitTime()
	{
		return visitTime;
	}

	public void setVisitTime(Timestamp visitTime)
	{
		this.visitTime = visitTime;
	}

	public VisitedArticle()
	{
		super();
	}

	public VisitedArticle(Timestamp visitTime, Long articleId, User user)
	{
		super();
		this.visitTime = visitTime;
		this.articleId = articleId;
		this.user = user;
	}
}
