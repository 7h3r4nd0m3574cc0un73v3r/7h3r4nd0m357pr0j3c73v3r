package org.usayi.preta.exceptions;

import org.springframework.validation.Errors;

@SuppressWarnings( "serial")
public class InvalidRequestException extends RuntimeException
{
	private Errors errors;
	
	public InvalidRequestException( String message, Errors errors)
	{
		super( message);
		this.errors = errors;
	}

	public Errors getErrors()
	{
		return errors;
	}

	public void setErrors(Errors errors)
	{
		this.errors = errors;
	}
}
