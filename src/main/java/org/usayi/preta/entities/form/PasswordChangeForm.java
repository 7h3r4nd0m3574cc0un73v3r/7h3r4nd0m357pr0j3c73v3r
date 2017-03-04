package org.usayi.preta.entities.form;

public class PasswordChangeForm
{
	private String currentPassword;
	
	private String password;
	
	private String confirmPassword;

	public String getCurrentPassword()
	{
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword)
	{
		this.currentPassword = currentPassword;
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

	public PasswordChangeForm()
	{
		super();
	}
}
