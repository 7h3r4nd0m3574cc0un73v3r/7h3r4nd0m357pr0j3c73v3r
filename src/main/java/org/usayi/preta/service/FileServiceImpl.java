package org.usayi.preta.service;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl implements FileService
{
	private final String sendGridApiKeyPath = "/random/preta/secrets/sg.key";

	@Override
	public String readFile(String path)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String readSendGridApiKey()
	{
		try
		{
			return IOUtils.toString( new FileInputStream( new File( sendGridApiKeyPath)));	
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
