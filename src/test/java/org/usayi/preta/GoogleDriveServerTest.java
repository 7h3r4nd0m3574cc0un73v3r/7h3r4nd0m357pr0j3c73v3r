package org.usayi.preta;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.usayi.preta.service.GoogleDriveServiceBuilder;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class GoogleDriveServerTest
{
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();
    private static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    
	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void test()
	{
//			GoogleCredential credential = GoogleCredential.fromStream( new FileInputStream( "/random/CustomWares/EclipseJEENeonWorkSpace/Preta/src/test/resources/client_secret.json"))
//														  .createScoped( Collections.singleton( DriveScopes.DRIVE));
			GoogleCredential credential;
			try
			{

//				credential = GoogleCredential.fromStream( new FileInputStream( "/home/z3r0/Downloads/preta-svc-account.json"))
//											 .createScoped( Collections.singleton( DriveScopes.DRIVE));
				
						credential = new GoogleCredential.Builder()
					    .setTransport( HTTP_TRANSPORT)
					    .setJsonFactory(JSON_FACTORY)
					    .setServiceAccountId( "preta-861@preta-149515.iam.gserviceaccount.com")
					    .setServiceAccountPrivateKeyFromP12File(new java.io.File( GoogleDriveServiceBuilder.pathToDriveSecret + "preta-svc-account.p12"))
					    .setServiceAccountScopes(Collections.singleton( DriveScopes.DRIVE))
					    .setServiceAccountUser("usayi.technology@gmail.com")
					    .build();
				
				Drive driveService = new Drive.Builder( HTTP_TRANSPORT, JSON_FACTORY, credential)
						.setApplicationName( "preta")
						.build();
				

				FileList result = driveService.files().list()
			             .setPageSize(10)
			             .setFields("nextPageToken, files(id, name)")
			             .execute();
				
		        List<File> files = result.getFiles();
		        if (files == null || files.size() == 0) {
		            System.out.println("No files found.");
		        } else {
		            System.out.println("Files:");
		            for (File file : files) {
		                System.out.printf("%s (%s)\n", file.getName(), file.getId());
		            }
		        }
		        assert( true);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			assert( false);
		} catch (IOException e)
		{
			e.printStackTrace();
			assert( false);
		} catch (GeneralSecurityException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
