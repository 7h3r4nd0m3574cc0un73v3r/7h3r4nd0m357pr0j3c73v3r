package org.usayi.preta.entities.json;

import java.util.ArrayList;
import java.util.List;

import org.usayi.preta.Views;

import com.fasterxml.jackson.annotation.JsonView;

public class PagedListJSON
{
	@JsonView( Views.Public.class)
	private Integer pagesNumber;
	
	@JsonView( Views.Public.class)
	private Integer itemsNumber;
	
	@JsonView( Views.Public.class)
	private List<?> entities;

	public Integer getPagesNumber()
	{
		return pagesNumber;
	}

	public void setPagesNumber(Integer pagesNumber)
	{
		this.pagesNumber = pagesNumber;
	}

	public List<?> getEntities()
	{
		return entities;
	}

	public void setEntities(List<?> entities)
	{
		this.entities = entities;
	}

	@SuppressWarnings("rawtypes")
	public PagedListJSON()
	{
		super();
		this.entities = new ArrayList();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PagedListJSON( Integer pagesNumber, List<?> entities)
	{
		super();
		this.entities = new ArrayList( entities);
		this.pagesNumber = pagesNumber;
	}

	public PagedListJSON(Integer pagesNumber, Integer itemsNumber, List<?> entities)
	{
		super();
		this.pagesNumber = pagesNumber;
		this.itemsNumber = itemsNumber;
		this.entities = entities;
	}

	public Integer getItemsNumber()
	{
		return itemsNumber;
	}

	public void setItemsNumber(Integer itemsNumber)
	{
		this.itemsNumber = itemsNumber;
	}
}
