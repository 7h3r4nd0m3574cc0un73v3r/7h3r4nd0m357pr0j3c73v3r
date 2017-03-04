package org.usayi.preta.exceptions;

import org.springframework.validation.FieldError;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InvalidEntityFieldError
{
	private String code;
	
	private String field;
	
	private String message;
	
	private String object;

	public InvalidEntityFieldError()
	{
		super();
	}
	
	public InvalidEntityFieldError( FieldError fieldError)
	{
		super();
		this.setCode( fieldError.getCode());
		this.setField( fieldError.getField());
		this.setMessage( fieldError.getDefaultMessage());
		this.setObject( fieldError.getObjectName());
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getField()
	{
		return field;
	}

	public void setField(String field)
	{
		this.field = field;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getObject()
	{
		return object;
	}

	public void setObject(String object)
	{
		this.object = object;
	}
}
