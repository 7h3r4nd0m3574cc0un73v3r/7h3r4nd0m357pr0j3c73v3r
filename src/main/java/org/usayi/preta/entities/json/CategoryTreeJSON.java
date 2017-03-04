package org.usayi.preta.entities.json;

import org.usayi.preta.Views;
import org.usayi.preta.entities.Category;

import com.fasterxml.jackson.annotation.JsonView;

public class CategoryTreeJSON
{
	@JsonView( Views.Public.class)
	private String id;
	
	@JsonView( Views.Public.class)
	private String parent;
	
	@JsonView( Views.Public.class)
	private String text;

	public CategoryTreeJSON( Category category)
	{
		super();
		this.id = category.getId().toString();
		if( category.getRoot() != null)
			this.parent = category.getRoot().getId().toString();
		else
			this.parent = "0";
		this.text = category.getLabel();
	}
	
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getParent()
	{
		return parent;
	}

	public void setParent(String parent)
	{
		this.parent = parent;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}
}
