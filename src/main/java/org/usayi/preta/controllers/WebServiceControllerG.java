package org.usayi.preta.controllers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.usayi.preta.buzlayer.IRESTAPI;
import org.usayi.preta.config.CustomPasswordEncoder;
import org.usayi.preta.entities.User;
import org.usayi.preta.entities.form.UserLogin;

@RestController
@RequestMapping( value="/rest")
public class WebServiceControllerG
{
	@Autowired
	private IRESTAPI restBL;

	//Login WebServices
	@PostMapping
	@RequestMapping( value="/manager/login")
	public ResponseEntity<Map<String, Object>> managerLogin( @RequestBody UserLogin userLogin )
	{
		User user = restBL.getUserByUsername( userLogin.getUsername());

		Map<String, Object> result = new HashMap<String, Object>();

		CustomPasswordEncoder passwordEncoder = new CustomPasswordEncoder();

		if (user == null || !user.getUsername().equalsIgnoreCase(userLogin.getUsername()))
		{
			result.put( "code", 0);
			result.put( "message", "Invalid Username / Password");

			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.UNAUTHORIZED);
		}
		//Control on password
		if( !passwordEncoder.matches( user.getSalt() + userLogin.getPassword(), user.getPassword())) 
		{
			result.put( "code", 0);
			result.put( "errorMessage", "Invalid Username / Password");
			return new ResponseEntity<Map<String, Object>>( result, HttpStatus.UNAUTHORIZED);
		}
		//Checks is Account is enabled
		if( !user.hasRole( "ROLE_MANAGER"))
		{
			result.put( "code", -2);
			result.put( "message", "Unauthorized");

			return new ResponseEntity<Map<String, Object>>( result, HttpStatus.UNAUTHORIZED);
		}
		//Checks is Account is enabled
		if( !user.getIsEnabled())
		{
			result.put( "code", -1);
			result.put( "message", "Account disabled");

			return new ResponseEntity<Map<String, Object>>( result, HttpStatus.UNAUTHORIZED);
		}

		Collection<? extends GrantedAuthority> authorities = user.getRoles();

		Authentication auth = new UsernamePasswordAuthenticationToken(user, userLogin.getPassword(), authorities);

		SecurityContextHolder.getContext().setAuthentication( auth);

		result.put( "code", 1);
		result.put( "message", "Success");
		result.put( "user", auth);

		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

}
