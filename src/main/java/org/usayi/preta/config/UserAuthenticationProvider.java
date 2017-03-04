package org.usayi.preta.config;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.usayi.preta.buzlayer.IRESTAPI;
import org.usayi.preta.entities.User;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider
{
	@Autowired
	private IRESTAPI restBL;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException
	{
		//Get input from login form
		String username = authentication.getName();
		String password = ( String) authentication.getCredentials();
		
		CustomPasswordEncoder passwordEncoder = new CustomPasswordEncoder();
		
		User user = restBL.getUserByUsername(username);
		
		//Check if username matches
        if (user == null || !user.getUsername().equalsIgnoreCase(username)) 
        {
        	System.out.println( "0");
            throw new BadCredentialsException("Username not found.");
        }
 
        //Check is Accout is enabled
        if( !user.getIsEnabled())
        {
        	System.out.println( "1");
        	throw new BadCredentialsException( "Compte désactivé.");
        }
        
        //Control on password
        if( !passwordEncoder.matches( user.getSalt() + password, user.getPassword())) 
        {
        	System.out.println( "2");
            throw new BadCredentialsException("Wrong password.");
        }
        
        //Load permission
        Collection<? extends GrantedAuthority> authorities = user.getRoles();
 
        return new UsernamePasswordAuthenticationToken(user, password, authorities);
	}

	@Override
	public boolean supports(Class<?> authentication)
	{
		return authentication.equals( UsernamePasswordAuthenticationToken.class);
	}

}
