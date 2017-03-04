package org.usayi.preta.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OrderBy;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.Email;
import org.usayi.preta.Views;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Audited
@DynamicUpdate( true)
@Table( uniqueConstraints= @UniqueConstraint( columnNames = { "email"} ))
public class UserInfo implements Serializable
{
	private static final long serialVersionUID = 6919474862773551048L;

	@Id
	@NotAudited
	@GeneratedValue( strategy= GenerationType.IDENTITY)
	@JsonView( Views.Public.class)
	private Long id;
	
	@JsonView( Views.Public.class)
	private String firstName;
	
	@JsonView( Views.Public.class)
	private String middleName;

	@JsonView( Views.Public.class)
	private String lastName;

	@JsonView( Views.Public.class)
	private GenderType gender;

	@JsonView( Views.Public.class)
	private Timestamp birthDay;

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

	@JsonView( Views.Public.class)
	private String homeNumber;

	@Email
	@JsonView( Views.Public.class)
	@NotNull
	private String email;

	@JsonIgnore
	@Fetch( FetchMode.SELECT)
	@OrderBy( clause="relId ASC")
	@OneToMany( mappedBy="user", fetch=FetchType.EAGER)
	private Collection<EAccount> eAccounts;
	
	public final Long getId()
	{
		return id;
	}

	public final void setId(Long id)
	{
		this.id = id;
	}

	public final String getFirstName()
	{
		return firstName;
	}

	public final void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public final String getMiddleName()
	{
		return middleName;
	}

	public final void setMiddleName(String middleName)
	{
		this.middleName = middleName;
	}

	public final String getLastName()
	{
		return lastName;
	}

	public final void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public final GenderType getGender()
	{
		return gender;
	}

	public final void setGender(GenderType gender)
	{
		this.gender = gender;
	}

	public final Timestamp getBirthDay()
	{
		return birthDay;
	}

	public final void setBirthDay(Timestamp birthDay)
	{
		this.birthDay = birthDay;
	}

	public final String getCountry()
	{
		return country;
	}

	public final void setCountry(String country)
	{
		this.country = country;
	}

	public final String getDepartment()
	{
		return department;
	}

	public final void setDepartment(String department)
	{
		this.department = department;
	}

	public final String getCity()
	{
		return city;
	}

	public final void setCity(String city)
	{
		this.city = city;
	}

	public final String getBorough()
	{
		return borough;
	}

	public final void setBorough(String borough)
	{
		this.borough = borough;
	}

	public final String getDistrict()
	{
		return district;
	}

	public final void setDistrict(String district)
	{
		this.district = district;
	}

	public final String getStreet()
	{
		return street;
	}

	public final void setStreet(String street)
	{
		this.street = street;
	}

	public final String getFullAddress()
	{
		return fullAddress;
	}

	public final void setFullAddress(String fullAddress)
	{
		this.fullAddress = fullAddress;
	}

	public final String getHomeNumber()
	{
		return homeNumber;
	}

	public final void setHomeNumber(String homeNumber)
	{
		this.homeNumber = homeNumber;
	}

	public final String getEmail()
	{
		return email;
	}

	public final void setEmail(String email)
	{
		this.email = email;
	}

	public final Collection<EAccount> geteAccounts()
	{
		return eAccounts;
	}
	
	public final void addEAccount( final EAccount eAccount)
	{
		this.eAccounts.add( eAccount);
	}
	
	public final void removeEAccount( final EAccount eAccount)
	{
		this.eAccounts.remove(eAccount);
	}

	public UserInfo(String firstName, String middleName, String lastName, GenderType gender, Timestamp birthDay,
			String country, String department, String city, String borough, String district, String street,
			String fullAddress, String homeNumber, String email)
	{
		super();
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.gender = gender;
		this.birthDay = birthDay;
		this.country = country;
		this.department = department;
		this.city = city;
		this.borough = borough;
		this.district = district;
		this.street = street;
		this.fullAddress = fullAddress;
		this.homeNumber = homeNumber;
		this.email = email;
		this.eAccounts = new ArrayList<EAccount>();
	}
	
	public UserInfo()
	{
		super();
		this.eAccounts = new ArrayList<EAccount>();
	}

	public UserInfo( UserInfo userInfo)
	{
		super();
		this.firstName = userInfo.firstName;
		this.middleName = userInfo.middleName;
		this.lastName = userInfo.lastName;
		this.gender = userInfo.gender;
		this.birthDay = userInfo.birthDay;
		this.country = userInfo.country;
		this.department = userInfo.department;
		this.city = userInfo.city;
		this.borough = userInfo. borough;
		this.district = userInfo.district;
		this.street = userInfo.street;
		this.fullAddress =userInfo.fullAddress;
		this.homeNumber = userInfo.homeNumber;
		this.email = userInfo.email;
		this.eAccounts = new ArrayList<EAccount>();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		UserInfo other = (UserInfo) obj;
		if (id == null)
		{
			if (other.id != null) return false;
		}
		else if (!id.equals(other.id)) return false;
		return true;
	}
}
