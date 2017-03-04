package org.usayi.preta.exceptions;

import org.usayi.preta.Views;

import com.fasterxml.jackson.annotation.JsonView;

public class CustomError
{
	@JsonView( Views.Public.class)
	private String code;
	
	@JsonView( Views.Public.class)
	private String message;

	public CustomError()
	{
		super();
	}
	
	public CustomError( String code, String message)
	{
		super();
		this.code = code;
		this.message = message;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}
}
