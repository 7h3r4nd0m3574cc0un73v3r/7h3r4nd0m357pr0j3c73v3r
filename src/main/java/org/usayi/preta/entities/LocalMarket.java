package org.usayi.preta.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.usayi.preta.Views;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Audited
public class LocalMarket implements Serializable
{
	private static final long serialVersionUID = -5838759569866226970L;
	
	@Id
	@GeneratedValue( strategy=GenerationType.IDENTITY)
	@JsonView( Views.Public.class)
	private Long id;
	
	@NotNull
	@JsonView( Views.Public.class)
	private String name;
	
	@JsonView( Views.Admin.class)
	private boolean displayed = true;
	
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public boolean isDisplayed()
	{
		return displayed;
	}

	public void setDisplayed(boolean displayed)
	{
		this.displayed = displayed;
	}

	@JsonView( Views.Public.class)
	private String country;
	
	@JsonView( Views.Public.class)
	private String department;
	
	@JsonView( Views.Public.class)
	private String city;
	
	@JsonView( Views.Public.class)
	private String borough;
	
	@JsonView( Views.Public.class)
	private String district;
	
	@JsonView( Views.Public.class)
	private String street;
	
	@JsonView( Views.Public.class)
	private String fullAddress;

	private boolean deleted;
	
	public boolean isDeleted()
	{
		return deleted;
	}

	public void setDeleted(boolean deleted)
	{
		this.deleted = deleted;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	public LocalMarket()
	{
		super();
	}

	public String getDepartment()
	{
		return department;
	}

	public LocalMarket(String name, String country, String department, String city, String borough, String district,
			String street, String fullAddress)
	{
		super();
		this.name = name;
		this.country = country;
		this.department = department;
		this.city = city;
		this.borough = borough;
		this.district = district;
		this.street = street;
		this.fullAddress = fullAddress;
	}

	public void setDepartment(String department)
	{
		this.department = department;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getBorough()
	{
		return borough;
	}

	public void setBorough(String borough)
	{
		this.borough = borough;
	}

	public String getDistrict()
	{
		return district;
	}

	public void setDistrict(String district)
	{
		this.district = district;
	}

	public String getStreet()
	{
		return street;
	}

	public void setStreet(String street)
	{
		this.street = street;
	}

	public String getFullAddress()
	{
		return fullAddress;
	}

	public void setFullAddress(String fullAddress)
	{
		this.fullAddress = fullAddress;
	}
}
