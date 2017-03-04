package org.usayi.preta.entities.json;

import java.util.ArrayList;
import java.util.Collection;

import org.usayi.preta.Views;
import org.usayi.preta.entities.Category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

public class CategoryMultiSelectTreeJSON
{
	@JsonView( Views.Public.class)
	private Long id;
	
	@JsonView( Views.Public.class)
	private String name;
	
	@JsonView( Views.Public.class)
	private boolean selected = false;
	
	@JsonView( Views.Public.class)
	private boolean isExpanded = false;
	
	@JsonView( Views.Public.class)
	private boolean isActive = false;
	
	@JsonView( Views.Public.class)
	private Collection<CategoryMultiSelectTreeJSON> children;

	@JsonIgnore
	private Collection<Category> originalCategoryTree;
	
	public CategoryMultiSelectTreeJSON()
	{
		super();
		this.children = new ArrayList<CategoryMultiSelectTreeJSON>();
		this.originalCategoryTree = new ArrayList<Category>();
	}
	
	public CategoryMultiSelectTreeJSON( Category category) 
	{
		super();
		this.id = category.getId();
		this.name = category.getLabel();
		Collection<CategoryMultiSelectTreeJSON> children = new ArrayList<CategoryMultiSelectTreeJSON>();
		for( Category cat : category.getLeaves())
		{
			children.add( new CategoryMultiSelectTreeJSON( cat));
		}
		this.children = children;
	}
	
	public CategoryMultiSelectTreeJSON( Category category, Collection<Category> originalCategoryTree) 
	{
		super();
		this.id = category.getId();
		this.name = category.getLabel();
		this.originalCategoryTree = new ArrayList<Category>( originalCategoryTree);
		
		for( Category selectedCategory : this.originalCategoryTree) {
			if( selectedCategory.getId().longValue() == this.id.longValue())
				this.selected = true;
		}
		
		Collection<CategoryMultiSelectTreeJSON> children = new ArrayList<CategoryMultiSelectTreeJSON>();
		
		for( Category cat : category.getLeaves())
		{
			CategoryMultiSelectTreeJSON catMST = new CategoryMultiSelectTreeJSON( cat, originalCategoryTree);
			
			for( Category selectedCategory : this.originalCategoryTree) {
				if( selectedCategory.getId().longValue() == cat.getId().longValue())
					catMST.setSelected( true);
			}
			
			children.add( catMST);
		}
		
		this.children = children;
	}
	
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean isSelected()
	{
		return selected;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	public boolean isExpanded()
	{
		return isExpanded;
	}

	public void setExpanded(boolean isExpanded)
	{
		this.isExpanded = isExpanded;
	}

	public boolean isActive()
	{
		return isActive;
	}

	public void setActive(boolean isActive)
	{
		this.isActive = isActive;
	}

	public Collection<CategoryMultiSelectTreeJSON> getChildren()
	{
		return children;
	}

	public void setChildren(Collection<CategoryMultiSelectTreeJSON> children)
	{
		this.children = children;
	}

	public Collection<Category> getOriginalCategoryTree()
	{
		return originalCategoryTree;
	}

	public void setOriginalCategoryTree(Collection<Category> originalCategoryTree)
	{
		this.originalCategoryTree = originalCategoryTree;
	}
}
