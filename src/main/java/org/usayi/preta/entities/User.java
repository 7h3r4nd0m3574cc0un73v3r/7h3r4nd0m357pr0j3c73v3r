package org.usayi.preta.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.usayi.preta.Views;
import org.usayi.preta.config.Tools;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Audited
@Table( uniqueConstraints = @UniqueConstraint( columnNames={ "username"} ))
public class User implements Serializable, UserDetails
{
	private static final long serialVersionUID = -1297056688392065776L;

	@Id
	@OneToOne
	@NotAudited
	@JsonView( Views.Admin.class)
	@JoinColumn( name="id", referencedColumnName="id")
	private UserInfo userInfo;

	@NotNull
	@Size( min = 4, max = 128)
	@JsonView( Views.Public.class)
	private String username;
	
	@NotNull
	@JsonIgnore
	private String salt;
	
	@NotNull
	@JsonIgnore
	private String password;
	
	@Transient
	private String confirmPassword;
	
	@Transient
	@Email
	private String email;

	@Transient
	private String mobile;

	@NotNull
	@JsonView( Views.Admin.class)
	private Boolean isEnabled = false;
	
	@NotAudited
	private String confToken;
	
	@JsonIgnore
	@ManyToMany( fetch=FetchType.EAGER, cascade=CascadeType.REFRESH)
	private Collection<Role> roles;

	@JsonIgnore
	@ManyToMany( fetch=FetchType.LAZY)
	@JoinTable( name="Manager_EShops")
	private Set<EShop> managedEShops;
	
	@JsonIgnore
	@OneToMany( mappedBy="user", fetch=FetchType.LAZY)
	private Collection<ArticleOrder> articleOrders;
	
	@JsonIgnore
	@OneToMany( fetch=FetchType.LAZY)
	private Collection<CartItem> cart;
	
	@JsonIgnore
	@NotAudited
	@ManyToMany( fetch=FetchType.LAZY)
	@JoinColumn( referencedColumnName="id")
	@JoinTable( name="Buyer_LastVisitedArticles")
	private Collection<Article> lastVisitedArticles;
	
	@JsonIgnore
	@NotAudited
	@ManyToMany( fetch=FetchType.LAZY)
	@JoinColumn( referencedColumnName="id")
	@JoinTable( name="Buyer_FavoriteEShops")
	private Set<EShop> favoritesEShop;
	
	@JsonIgnore
	@OneToMany( fetch=FetchType.LAZY)
	@JoinColumn( referencedColumnName="id")
	@JoinTable( name="Admin_SupervisedManagers")
	private Set<User> supervisedManagers;
	
	@Transient
	private boolean manager = false;
	
	@NotNull
	private boolean approved = false;
	
	@Transient
	@JsonView( Views.Public.class)
	private int profileCompletion;
	
	public Collection<CartItem> getCart()
	{
		return cart;
	}
	
	public void addCartItem( CartItem entity)
	{
		this.cart.add( entity);
	}
	
	public void removeCartItem( CartItem entity)
	{
		this.cart.remove( entity);
	}
	
	public final UserInfo getUserInfo()
	{
		return userInfo;
	}

	public final void setUserInfo(UserInfo userInfo)
	{
		this.userInfo = userInfo;
	}

	public final String getUsername()
	{
		return username;
	}

	public final void setUsername(String username)
	{
		this.username = username;
	}

	public final String getSalt()
	{
		return salt;
	}

	public final void setSalt(String salt)
	{
		this.salt = salt;
	}

	public final String getPassword()
	{
		return password;
	}

	public final void setPassword(String password)
	{
		this.password = password;
	}

	public final Boolean getIsEnabled()
	{
		return isEnabled;
	}

	public final void setIsEnabled(Boolean isEnabled)
	{
		this.isEnabled = isEnabled;
	}

	public final String getConfToken()
	{
		return confToken;
	}

	public final void setConfToken(String confToken)
	{
		this.confToken = confToken;
	}

	public final Collection<Role> getRoles()
	{
		return roles;
	}
	
	public final Boolean hasRole( final String role)
	{
		Boolean hasRole = false;
		
		for( Role userRole : this.roles)
		{
			if( userRole.getName().matches( role))
				hasRole = true;
		}
		
		return hasRole;
	}
	
	public final void addRole( final Role role)
	{
		this.roles.add( role);
	}
	
	public final void removeRole( final Role role)
	{
		this.roles.remove( role);
	}

	public User(UserInfo userInfo, String username, String salt, String password, Boolean isEnabled, String confToken)
	{
		super();
		this.userInfo = userInfo;
		this.username = username;
		this.salt = salt;
		this.password = password;
		this.isEnabled = isEnabled;
		this.confToken = confToken;
		this.roles = new ArrayList<Role>();
		this.managedEShops = new HashSet<EShop>();
		this.favoritesEShop = new HashSet<EShop>();
		this.supervisedManagers = new HashSet<User>();
	}

	public User()
	{
		super();
		this.roles = new ArrayList<Role>();
		this.managedEShops = new HashSet<EShop>();
		this.favoritesEShop = new HashSet<EShop>();
		this.supervisedManagers = new HashSet<User>();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userInfo == null) ? 0 : userInfo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		User other = (User) obj;
		if (userInfo == null)
		{
			if (other.userInfo != null) return false;
		}
		else if (!userInfo.equals(other.userInfo)) return false;
		return true;
	}

	@Override
	protected void finalize() throws Throwable
	{
		super.finalize();
	}

	public final Collection<EShop> getManagedEShops()
	{
		return managedEShops;
	}
	
	public final void addManagedEShop( final EShop eShop)
	{
		this.managedEShops.add( eShop);
	}
	
	public final void removeManagedEShop( final EShop eShop)
	{
		this.managedEShops.remove( eShop);
	}
	
	@Transient
	public String getMobile()
	{
		return mobile;
	}

	@Transient
	public String getEmail()
	{
		return email;
	}
	
	@Transient
	public String getConfirmPassword()
	{
		return confirmPassword;
	}
	
	@Transient
	public void setConfirmPassword(String confirmPassword)
	{
		this.confirmPassword = confirmPassword;
	}

	@Transient
	public void setEmail(String email)
	{
		this.email = email;
	}

	@Transient
	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}
	
	@Transient
	public String getFullName()
	{
		return this.getUserInfo().getFirstName() + " " + this.getUserInfo().getLastName();
	}
	
	public void addArticleOrder( final ArticleOrder entity)
	{
		this.articleOrders.add( entity);
	}
	
	public void removeArticleOrder( final ArticleOrder entity)
	{
		this.articleOrders.remove( entity);
	}

	public void setCart(Collection<CartItem> cart)
	{
		this.cart = cart;
	}

	public Collection<ArticleOrder> getArticleOrders()
	{
		return articleOrders;
	}

	@JsonGetter
	@JsonView( Views.Public.class)
	public boolean isManager()
	{
		return this.hasRole( "ROLE_MANAGER");
	}

	public int getProfileCompletion()
	{
		return Tools.computeUserProfileComp( this);
	}

	@JsonView( Views.Public.class)
	public boolean isApproved()
	{
		return this.approved;
	}

	public void setApproved(boolean approved)
	{
		this.approved = approved;
	}
	
	public void addVisitedArticle( Article article) 
	{	
		this.lastVisitedArticles.add(article);
	}

	public void removeVisitedArticle( Article article)
	{
		this.lastVisitedArticles.remove( article);
	}
	public Collection<Article> getLastVisitedArticles()
	{
		return lastVisitedArticles;
	}

	public Collection<EShop> getFavoritesEShop()
	{
		return favoritesEShop;
	}
	
	public void addEShopToFavorites( EShop entity)
	{
		this.favoritesEShop.add(entity);
	}
	
	public void removeEShopFromFavorites( EShop entity)
	{
		this.favoritesEShop.remove( entity);	
	}

	public Set<User> getSupervisedManagers()
	{
		return supervisedManagers;
	}
	
	public void addSupervisedManager( User entity)
	{
		this.supervisedManagers.add(entity);
	}
	
	public void removeSupervisedManager( User entity)
	{
		this.supervisedManagers.remove( entity);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAccountNonExpired()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled()
	{
		// TODO Auto-generated method stub
		return false;
	}
}
