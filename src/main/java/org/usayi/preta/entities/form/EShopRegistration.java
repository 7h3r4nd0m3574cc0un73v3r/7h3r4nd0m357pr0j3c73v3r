package org.usayi.preta.entities.form;

import org.hibernate.validator.constraints.Email;

//Model class for ESHop Registration from the WebSite
public class EShopRegistration
{
	private String eshopName;
	
	@Email
	private String email;
	
	private String mobile;
	
	private Boolean isUserRegistered;
	
	private String username;
	
	private String password;
	
	private String confirmPassword;
	
	@Email
	private String userEmail;
	
	private String userMobile;

	private String captcha;

	public String getCaptcha()
	{
		return captcha;
	}

	public void setCaptcha(String captcha)
	{
		this.captcha = captcha;
	}

	public String getEshopName()
	{
		return eshopName;
	}

	public void setEshopName(String eshopName)
	{
		this.eshopName = eshopName;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public Boolean getIsUserRegistered()
	{
		return isUserRegistered;
	}

	public void setIsUserRegistered(Boolean isUserRegistered)
	{
		this.isUserRegistered = isUserRegistered;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getConfirmPassword()
	{
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword)
	{
		this.confirmPassword = confirmPassword;
	}

	public String getUserEmail()
	{
		return userEmail;
	}

	public void setUserEmail(String userEmail)
	{
		this.userEmail = userEmail;
	}

	public String getUserMobile()
	{
		return userMobile;
	}

	public void setUserMobile(String userMobile)
	{
		this.userMobile = userMobile;
	}

	public EShopRegistration()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public EShopRegistration(String eshopName, String email, String mobile, Boolean isUserRegistered, String username,
			String password, String confirmPassword, String userEmail, String userMobile)
	{
		super();
		this.eshopName = eshopName;
		this.email = email;
		this.mobile = mobile;
		this.isUserRegistered = isUserRegistered;
		this.username = username;
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.userEmail = userEmail;
		this.userMobile = userMobile;
	}
}
