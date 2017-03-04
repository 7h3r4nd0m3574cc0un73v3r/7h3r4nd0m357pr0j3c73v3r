package org.usayi.preta.service;

import org.springframework.stereotype.Service;

@Service
public interface FileService
{
	public String readFile( final String path);
	
	/* SendGrid */
	public String readSendGridApiKey();
}
