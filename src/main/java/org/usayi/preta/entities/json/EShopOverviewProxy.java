package org.usayi.preta.entities.json;

import java.util.ArrayList;
import java.util.List;

import org.usayi.preta.entities.Article;

//Used to return simplified JSON form a class
public class EShopOverviewProxy
{
	private Long id;
	
	private String name;
	
	private List<Article> articles;

	public EShopOverviewProxy(Long id, String name, List<Article> articles)
	{
		super();
		this.id = id;
		this.name = name;
		this.articles = articles;
	}

	public EShopOverviewProxy()
	{
		super();
		this.articles = new ArrayList<Article>();
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<Article> getArticles()
	{
		return articles;
	}

	public void setArticles(List<Article> articles)
	{
		this.articles = articles;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}
}
