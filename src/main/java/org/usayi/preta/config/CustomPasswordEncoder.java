package org.usayi.preta.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomPasswordEncoder implements PasswordEncoder
{

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@Override
	public String encode(CharSequence charSequence)
	{	
		return passwordEncoder.encode( charSequence);
	}
	@Override
	public boolean matches(CharSequence charSequence, String password)
	{
		if( passwordEncoder.matches(charSequence, password))
			return true;
		
		return false;
	}

}
