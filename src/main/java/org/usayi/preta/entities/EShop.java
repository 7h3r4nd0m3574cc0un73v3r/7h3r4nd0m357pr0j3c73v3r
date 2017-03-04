package org.usayi.preta.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.usayi.preta.Views;
import org.usayi.preta.config.Tools;
import org.usayi.preta.service.GoogleDriveServiceImpl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Audited
@Table( uniqueConstraints= @UniqueConstraint( columnNames= { "name"} ))
public class EShop implements Serializable
{
	private static final long serialVersionUID = 5650956030616330907L;

	@Id
	@GeneratedValue( strategy=GenerationType.IDENTITY)
	@NotAudited
	@JsonView( Views.Public.class)
	private Long id;
	
	@NotNull
	private Long relId = 0L;
	
	@NotNull
	@Size( min = 3, max = 150)
	@JsonView( Views.Public.class)
	private String name;
	
	@JsonView( Views.Public.class)
	private String description;
	
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
	
	@Column( nullable=true)
	@Size( min = 13, max = 13)
	@JsonView( Views.Public.class)
	private String ifu;
	
	@JsonView( Views.Public.class)
	private String rc;
	
	@JsonView( Views.Public.class)
	private Boolean isInMarket;
	
	@JsonView( Views.Public.class)
	private String email;
	
	@JsonView( Views.Public.class)
	private String customerNum;
	
	@NotNull
	@JsonView( Views.Public.class)
	private ShopType shopType = ShopType.LOCAL;
	
	@JsonView( { Views.Manager.class, Views.Admin.class })
	private Boolean isEnabled = false;
	
	private String logoFile;
	
	@JsonView( Views.Public.class)
	private String logoGoogleId = GoogleDriveServiceImpl.defaultEShopPicId;
	
	@Transient
	@JsonView( Views.Manager.class)
	private Integer profileCompletion;
	
	@OneToMany( mappedBy="eShop", fetch=FetchType.LAZY)
	private Collection<ShopSub> shopSubs;
	
	@JsonIgnore
	@Fetch( FetchMode.SELECT)
	@OneToMany( fetch=FetchType.EAGER, targetEntity=PaymentType.class)
	private Collection<PaymentType> paymentTypes;
	
	@JsonIgnore
	@Fetch( FetchMode.SELECT)
	@OneToMany( mappedBy="eShop", fetch=FetchType.LAZY)
	private Collection<Article> articles;
	
	@Transient
	@JsonView( Views.Public.class)
	private Timestamp regDate;

	@Transient
	private User manager;
	
	@Transient
	private Long articlesCount;
	
	@JsonIgnore
	@OneToOne( targetEntity=ShopSub.class)
	@JoinColumn( referencedColumnName="id")
	private ShopSub currentShopSub;
	
	public final Long getId()
	{
		return id;
	}

	public final void setId(Long id)
	{
		this.id = id;
	}

	public final String getName()
	{
		return name;
	}

	public final void setName(String name)
	{
		this.name = name;
	}

	public final String getDescription()
	{
		return description;
	}

	public final void setDescription(String description)
	{
		this.description = description;
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

	public final String getIfu()
	{
		return ifu;
	}

	public final void setIfu(String ifu)
	{
		this.ifu = ifu;
	}

	public final String getRc()
	{
		return rc;
	}

	public final void setRc(String rc)
	{
		this.rc = rc;
	}

	public final Boolean getIsInMarket()
	{
		return isInMarket;
	}

	public final void setIsInMarket(Boolean isInMarket)
	{
		this.isInMarket = isInMarket;
	}

	public final String getEmail()
	{
		return email;
	}

	public final void setEmail(String email)
	{
		this.email = email;
	}

	public final String getCustomerNum()
	{
		return customerNum;
	}

	public final void setCustomerNum(String customerNum)
	{
		this.customerNum = customerNum;
	}

	public final ShopType getShopType()
	{
		return shopType;
	}

	public final void setShopType(ShopType shopType)
	{
		this.shopType = shopType;
	}

	public final Boolean getIsEnabled()
	{
		return isEnabled;
	}

	public final void setIsEnabled(Boolean isEnabled)
	{
		this.isEnabled = isEnabled;
	}

	public final String getLogoFile()
	{
		return logoFile;
	}

	public final void setLogoFile(String logoFile)
	{
		this.logoFile = logoFile;
	}

	@JsonIgnore
	public final Collection<ShopSub> getShopSubs()
	{
		return shopSubs;
	}
	
	public final void addShopSub( final ShopSub shopSub)
	{
		this.shopSubs.add( shopSub);
	}
	
	public final void removeShopSub( final ShopSub shopSub)
	{
		this.shopSubs.remove(shopSub);
	}
	
	public final Collection<PaymentType> getPaymentTypes()
	{
		return paymentTypes;
	}
	
	public final void addPaymentType( final PaymentType paymentType)
	{
		this.paymentTypes.remove( paymentType);
	}
	
	public final void removePaymentType( final PaymentType paymentType)
	{
		this.paymentTypes.remove( paymentType);
	}
	
	public EShop( EShop eShop)
	{
		super();
		this.name = eShop.name;
		this.description = eShop.description;
		this.country = eShop.country;
		this.department = eShop.department;
		this.city = eShop.city;
		this.borough = eShop.borough;
		this.district = eShop.district;
		this.street = eShop.street;
		this.fullAddress = eShop.fullAddress;
		this.ifu = eShop.ifu;
		this.rc = eShop.rc;
		this.isInMarket = eShop.isInMarket;
		this.email = eShop.email;
		this.customerNum = eShop.customerNum;
		this.shopType = eShop.shopType;
		this.paymentTypes = new ArrayList<PaymentType>();
		this.articles = new ArrayList<Article>();
		this.shopSubs = new ArrayList<ShopSub>();
	}
	
	public EShop(String name, String description, String country, String department, String city, String borough,
			String district, String street, String fullAddress, String ifu, String rc, Boolean isInMarket, String email,
			String customerNum, ShopType shopType, Boolean isEnabled, String logoFile)
	{
		super();
		this.name = name;
		this.description = description;
		this.country = country;
		this.department = department;
		this.city = city;
		this.borough = borough;
		this.district = district;
		this.street = street;
		this.fullAddress = fullAddress;
		this.ifu = ifu;
		this.rc = rc;
		this.isInMarket = isInMarket;
		this.email = email;
		this.customerNum = customerNum;
		this.shopType = shopType;
		this.logoFile = logoFile;
		this.paymentTypes = new ArrayList<PaymentType>();
		this.articles = new ArrayList<Article>();
		this.shopSubs = new ArrayList<ShopSub>();
	}

	public EShop()
	{
		super();
		this.paymentTypes = new ArrayList<PaymentType>();
		this.articles = new ArrayList<Article>();
		this.shopSubs = new ArrayList<ShopSub>();
	}

	public Collection<Article> getArticles()
	{
		return articles;
	}
	
	public void addArticle( Article article) 
	{
		this.articles.add( article);
	}
	
	public void removeArticle( Article article)
	{
		this.articles.remove( article);
	}

	public Timestamp getRegDate()
	{
		return regDate;
	}

	public void setRegDate(Timestamp regDate)
	{
		this.regDate = regDate;
	}

	public String getLogoGoogleId()
	{
		return logoGoogleId;
	}

	public void setLogoGoogleId(String logoGoogleId)
	{
		this.logoGoogleId = logoGoogleId;
	}

	public Integer getProfileCompletion()
	{
		return Tools.computeEShopProfileComp(this);
	}

	public void setProfileCompletion(Integer profileCompletion)
	{
		this.profileCompletion = profileCompletion;
	}

	public User getManager()
	{
		return manager;
	}

	public void setManager(User manager)
	{
		this.manager = manager;
	}

	public Long getArticlesCount()
	{
		return articlesCount;
	}

	public void setArticlesCount(Long articlesCount)
	{
		this.articlesCount = articlesCount;
	}

	public ShopSub getCurrentShopSub()
	{
		return currentShopSub;
	}

	public void setCurrentShopSub(ShopSub currentShopSub)
	{
		this.currentShopSub = currentShopSub;
	}

	public Long getRelId()
	{
		return relId;
	}

	public void setRelId(Long relId)
	{
		this.relId = relId;
	}
}
