package org.usayi.preta.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.usayi.preta.Views;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Audited
public class Category implements Serializable
{
	private static final long serialVersionUID = -7766364074326650038L;

	@Id
	@NotAudited
	@JsonView( Views.Public.class)
	@GeneratedValue( strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@JsonView( Views.Public.class)
	private String label;
	
	@NotNull
	@JsonView( Views.Public.class)
	private String description;
	
	@NotNull
	@JsonView( Views.Public.class)
	private Boolean isDisplayed;
	
	@NotNull
	private Boolean isDeleted = false;
	
	@Fetch( FetchMode.JOIN)
	@OneToMany( mappedBy="root")
	@JsonView( Views.Public.class)
	private Collection<Category> leaves;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn( referencedColumnName="id", nullable = true)
	private Category root;

	@NotNull
	@JsonView( Views.Public.class)
	private Integer level = 0;
	
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public Boolean getIsDisplayed()
	{
		return isDisplayed;
	}

	public void setIsDisplayed(Boolean isDisplayed)
	{
		this.isDisplayed = isDisplayed;
	}

	public Boolean getIsDeleted()
	{
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted)
	{
		this.isDeleted = isDeleted;
	}

	public Category getRoot()
	{
		return root;
	}

	public void setRoot(Category root)
	{
		this.root = root;
	}

	public Collection<Category> getLeaves()
	{
		return leaves;
	}
	
	public void addLeaf( Category category)
	{
		this.leaves.add( category);
	}
	
	public void removeLeaf( Category category)
	{
		this.leaves.remove( category);
	}

	public Category()
	{
		super();
		this.leaves = new ArrayList<Category>();
		this.isDeleted = false;
		this.isDisplayed = true;
	}

	public Category(String label, Boolean isDisplayed)
	{
		super();
		this.label = label;
		this.isDisplayed = isDisplayed;
		this.leaves = new ArrayList<Category>();
	}

	public Integer getLevel()
	{
		return level;
	}

	public void setLevel(Integer level)
	{
		this.level = level;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
}
