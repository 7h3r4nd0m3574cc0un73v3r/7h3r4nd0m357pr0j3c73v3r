package org.usayi.preta;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import org.aspectj.lang.annotation.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;


public class TestEntities
{
	ClassPathXmlApplicationContext context;
	
	@Before(value = "")
	public void setUp() throws Exception
	{
		 context = new ClassPathXmlApplicationContext( new String[] { "app-context.xml"});
	}
	
	@Test
	public void test()
	{
		try
		{	
			context = new ClassPathXmlApplicationContext( new String[] { "app-context.xml"});
			
			//IRESTAPI restBL = (IRESTAPI) context.getBean("restBL");
			
			String folderId = "0B3HIv-5mVtA4ZnlVeGhQN3JQZkU";
			File fileMetaData = new File();
			fileMetaData.setName( "pepper.jpg");
			fileMetaData.setParents( Collections.singletonList(folderId));
			java.io.File filePath = new java.io.File( "/home/z3r0/Documents/BES/Res/pepper.jpg");
			FileContent mediaContent = new FileContent( "image/jpeg", filePath);
			
			Drive driveService = getDriveService();
			
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

		        
			File file = driveService.files()
									.create( fileMetaData, mediaContent)
									.execute();
			System.out.println( "File ID: " + file.getId());
			
			context.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			assert( false);
		}
	}
	/** Application name. */
    private static final String APPLICATION_NAME =
        "Preta";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
        System.getProperty("user.home"), ".credentials/2/drive-java-quickstart");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
        JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/drive-java-quickstart
     */
//    private static final List<String> SCOPES =
//        Arrays.asList(DriveScopes.DRIVE_METADATA_READONLY);
    
    private static final List<String> SCOPES =
            Arrays.asList(DriveScopes.DRIVE);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
        		TestEntities.class.getResourceAsStream("/client_secret.json");
        
        //System.out.println( in.toString());
        
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
            flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Drive client service.
     * @return an authorized Drive client service
     * @throws IOException
     */
    public static Drive getDriveService() throws IOException {
        Credential credential = authorize();
        return new Drive.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

	
}
