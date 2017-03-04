package org.usayi.preta.entities.json;

import java.util.ArrayList;
import java.util.Collection;

import org.usayi.preta.Views;
import org.usayi.preta.entities.EAccount;
import org.usayi.preta.entities.User;
import org.usayi.preta.entities.UserInfo;

import com.fasterxml.jackson.annotation.JsonView;

public class LoggedUser
{
	@JsonView( Views.Public.class)
	private UserInfo userInfo;
	
	@JsonView( Views.Public.class)
	private String username;
	
	@JsonView( Views.Public.class)
	private int profileCompletion;
	
	@JsonView( Views.Public.class)
	private boolean approved;
	
	public LoggedUser( User user) {
		super();
		this.userInfo = user.getUserInfo();
		this.username = user.getUsername();
		this.approved = user.isApproved();
		this.eAccounts = user.getUserInfo().geteAccounts();
		this.profileCompletion = user.getProfileCompletion();
		this.isManager = user.isManager();
	}
	
	public LoggedUser(UserInfo userInfo, String username, int profileCompletion, boolean isApproved, Collection<EAccount> eAccounts, boolean isManager)
	{
		super();
		this.userInfo = userInfo;
		this.username = username;
		this.profileCompletion = profileCompletion;
		this.approved = isApproved;
		this.eAccounts = eAccounts;
		this.isManager = isManager;
	}
	
	@JsonView( Views.Public.class)
	private Collection<EAccount> eAccounts;
	
	@JsonView( Views.Public.class)
	private boolean isManager;
	
	public UserInfo getUserInfo()
	{
		return userInfo;
	}

	public LoggedUser(UserInfo userInfo, String username, Collection<EAccount> eAccounts,
			boolean isManager)
	{
		super();
		this.userInfo = userInfo;
		this.username = username;
		this.eAccounts = eAccounts;
		this.isManager = isManager;
	}

	public LoggedUser(UserInfo userInfo, String username, int profileCompletion,
			Collection<EAccount> eAccounts, boolean isManager)
	{
		super();
		this.userInfo = userInfo;
		this.username = username;
		this.profileCompletion = profileCompletion;
		this.eAccounts = eAccounts;
		this.isManager = isManager;
	}

	public void setUserInfo(UserInfo userInfo)
	{
		this.userInfo = userInfo;
	}

	public LoggedUser(UserInfo userInfo, String username, Collection<EAccount> eAccounts)
	{
		super();
		this.userInfo = userInfo;
		this.username = username;
		this.eAccounts = eAccounts;
	}

	public LoggedUser()
	{
		super();
		this.eAccounts = new ArrayList<EAccount>();
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public Collection<EAccount> geteAccounts()
	{
		return eAccounts;
	}

	public void seteAccounts(Collection<EAccount> eAccounts)
	{
		this.eAccounts = eAccounts;
	}

	public boolean isManager()
	{
		return isManager;
	}

	public void setManager(boolean isManager)
	{
		this.isManager = isManager;
	}

	public int getProfileCompletion()
	{
		return profileCompletion;
	}

	public void setProfileCompletion(int profileCompletion)
	{
		this.profileCompletion = profileCompletion;
	}

	public boolean isApproved()
	{
		return approved;
	}

	public void setApproved(boolean isApproved)
	{
		this.approved = isApproved;
	}
}
