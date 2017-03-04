package org.usayi.preta.entities.json;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.usayi.preta.entities.Article;
import org.usayi.preta.entities.Category;

public class VisitedArticle
{
	private Timestamp visitTime = new Timestamp( System.currentTimeMillis());
	
	private long articleId;

	private String articleName = "";
	
	private Float articlePrice = 0F;
	
	private Set<Long> categories = new HashSet<Long>();
	
	private Long userId;
	
	public Timestamp getVisitTime()
	{
		return visitTime;
	}

	public void setVisitTime(Timestamp visitTime)
	{
		this.visitTime = visitTime;
	}

	public long getArticleId()
	{
		return articleId;
	}

	public void setArticleId(long articleId)
	{
		this.articleId = articleId;
	}

	public VisitedArticle()
	{
		super();
	}
	
	public VisitedArticle( Article entity, Long userId, List<Category> categories) {
		super();
		this.articleId = entity.getId();
		this.articleName = entity.getName();
		this.articlePrice = !entity.getIsDiscounted() ? entity.getPrice() : entity.getDiscountPrice();
		for( Category cat : categories)
		{
			this.categories.add( cat.getId());
		}
		this.userId = userId;
	}

	public String getArticleName()
	{
		return articleName;
	}

	public void setArticleName(String articleName)
	{
		this.articleName = articleName;
	}

	public Float getArticlePrice()
	{
		return articlePrice;
	}

	public void setArticlePrice(Float articlePrice)
	{
		this.articlePrice = articlePrice;
	}

	public Set<Long> getCategories()
	{
		return categories;
	}

	public void setCategories(Set<Long> categories)
	{
		this.categories = categories;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}
}
