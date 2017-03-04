package org.usayi.preta.entities.json;

import java.util.ArrayList;
import java.util.List;

import org.usayi.preta.Views;
import org.usayi.preta.entities.Article;

import com.fasterxml.jackson.annotation.JsonView;

public class PagedArticleListJSON
{
	@JsonView( Views.Public.class)
	private Integer pagesNumber;
	
	@JsonView( Views.Public.class)
	private List<Article> articles;

	public Integer getPagesNumber()
	{
		return pagesNumber;
	}

	public void setPagesNumber(Integer pagesNumber)
	{
		this.pagesNumber = pagesNumber;
	}

	public List<Article> getArticles()
	{
		return articles;
	}

	public void setArticles(List<Article> articles)
	{
		this.articles = articles;
	}

	public PagedArticleListJSON()
	{
		super();
		this.articles = new ArrayList<Article>();
	}

	public PagedArticleListJSON( Integer pagesNumber, List<Article> articles)
	{
		super();
		this.articles = new ArrayList<Article>( articles);
		this.pagesNumber = pagesNumber;
	}
}
