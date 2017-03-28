package org.usayi.preta.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

public class GoogleDriveServiceBuilder
{
	/* JSON FACTORY for JWT Exchange*/
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();
    
    /* Default HTTP_TRANSPORT for Token Exchange */
    private static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	/* Google Drive Secret P12 File */
	public static final String pathToDriveSecret = "C:/Hobossa/secrets/";
	
    /* Build Drive Service */
	public static Drive buildDriveService() {
		try
		{	
			/* Debug */
			System.out.println( "DEBUG: Loading Drive Secret from: " + pathToDriveSecret + "preta-svc-account.p12");
			File file = new File( GoogleDriveServiceBuilder.pathToDriveSecret + "preta-svc-account.p12");
			
			if( file.exists())
				System.out.println( "DEBUG: Secret Found");
			else
				System.out.println( "DEBUG: Secret Not Found");
			
			GoogleCredential credential = new GoogleCredential.Builder()
				    .setTransport( HTTP_TRANSPORT)
				    .setJsonFactory(JSON_FACTORY)
				    .setServiceAccountId( "preta-861@preta-149515.iam.gserviceaccount.com")
				    .setServiceAccountPrivateKeyFromP12File(new java.io.File( (GoogleDriveServiceBuilder.pathToDriveSecret + "preta-svc-account.p12").trim()))
				    .setServiceAccountScopes(Collections.singleton( DriveScopes.DRIVE))
				    .setServiceAccountUser("usayi.technology@gmail.com")
				    .build();
			
			return new Drive.Builder( HTTP_TRANSPORT, JSON_FACTORY, credential)
					.setApplicationName( "preta")
					.build();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		} 
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (GeneralSecurityException e)
		{
			e.printStackTrace();
			return null;
		}
		
	}
}
