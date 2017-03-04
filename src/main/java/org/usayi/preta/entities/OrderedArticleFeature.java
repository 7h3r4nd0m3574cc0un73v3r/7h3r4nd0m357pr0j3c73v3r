package org.usayi.preta.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.usayi.preta.Views;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Audited
public class OrderedArticleFeature implements Serializable
{
	private static final long serialVersionUID = -8672361484875275110L;

	@Id
	@GeneratedValue( strategy=GenerationType.IDENTITY)
	@NotAudited
	@JsonView( Views.Public.class)
	private Long id;

	@NotNull
	@ManyToOne
	@JoinColumn( referencedColumnName="id")
	@JsonView( Views.Public.class)
	private Feature feature;

	@NotNull
	@Fetch( FetchMode.SELECT)
	@JsonView( Views.Public.class)
	@ManyToMany( fetch=FetchType.EAGER)
	private Collection<FeatureValue> featureValues;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public OrderedArticleFeature()
	{
		super();
		this.featureValues = new ArrayList<FeatureValue>();
	}

	public OrderedArticleFeature( Collection<FeatureValue> values, Feature feature)
	{
		super();
		this.feature = feature;
		this.featureValues = new ArrayList<FeatureValue>( values);
	}

	public Feature getFeature()
	{
		return feature;
	}

	public void setFeature(Feature feature)
	{
		this.feature = feature;
	}
	
	public void addFeatureValue( FeatureValue featureValue)
	{
		this.featureValues.add( featureValue);
	}
	
	public void removeFeatureValue( FeatureValue featureValue)
	{
		this.featureValues.remove( featureValue);
	}

	public Collection<FeatureValue> getFeatureValues()
	{
		return featureValues;
	}

	public void setFeatureValues(Collection<FeatureValue> featureValues)
	{
		this.featureValues = featureValues;
	}
}
