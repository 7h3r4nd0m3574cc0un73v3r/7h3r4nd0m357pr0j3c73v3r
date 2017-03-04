package org.usayi.preta.exceptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice( "org.usayi.preta.controllers")
@RestControllerAdvice( "org.usayi.preta.controllers")
public class CustomExceptionHandler extends ResponseEntityExceptionHandler
{
	@ExceptionHandler( { InvalidRequestException.class})
	protected ResponseEntity<?> handleInvalidRequest( RuntimeException e, WebRequest request)
	{
		InvalidRequestException ire = (InvalidRequestException) e;
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		result.put( "code", "InvalidEntity");
		result.put( "message", "Invalid Entity");
		
		List<InvalidEntityFieldError> fieldErrors = new ArrayList<InvalidEntityFieldError>();
		
		for( FieldError fieldError : ire.getErrors().getFieldErrors())
		{
			fieldErrors.add( new InvalidEntityFieldError( fieldError));
		}
		
		result.put( "errors", fieldErrors);

		return new ResponseEntity<HashMap<String, Object>>( result, HttpStatus.UNPROCESSABLE_ENTITY);
	}
}
