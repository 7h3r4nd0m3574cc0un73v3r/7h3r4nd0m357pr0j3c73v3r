package org.usayi.preta.entities.form;

public class PWRequest
{
	private String email;
	
	private String captcha;

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getCaptcha()
	{
		return captcha;
	}

	public void setCaptcha(String captcha)
	{
		this.captcha = captcha;
	}

	public PWRequest()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public PWRequest(String email, String captcha)
	{
		super();
		this.email = email;
		this.captcha = captcha;
	}
	
}
