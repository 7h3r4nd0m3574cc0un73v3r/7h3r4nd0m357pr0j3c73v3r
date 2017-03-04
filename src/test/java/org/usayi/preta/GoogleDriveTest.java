package org.usayi.preta;


import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.usayi.preta.service.GoogleDriveServiceBuilder;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

@SuppressWarnings( "unused")
public class GoogleDriveTest
{
	@Before
	public void setUp() throws Exception {}

	@Test
	public void test()
	{
		try
		{
			Drive driveService = GoogleDriveServiceBuilder.buildDriveService();
			
			String articleFolderId = "0B0W0ktANhIydMWNiNVE0YUVZdVU";
		    
	        //driveService.files().delete( "0B0W0ktANhIydc3RhcnRlcl9maWxl").execute();

	        /* List all files in Google Drive */
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
	        
	        /*Create a file base on file date */
			File fileMetaData = new File();
			fileMetaData.setName( "pepper.jpg");
			fileMetaData.setParents( Collections.singletonList( articleFolderId));
			java.io.File filePath = new java.io.File( "/home/z3r0/Documents/BES/Res/pepper.jpg");
			FileContent mediaContent = new FileContent( "image/jpeg", filePath);
			
			/* Inserts a file in Google Drive */
			/*File file = driveService.files()
					.create( fileMetaData, mediaContent)
					.execute();*/
			
//			File file = driveService.files().get( "0B0W0ktANhIydcGg5MUdLWm5ZTms").execute();
//			
//			System.out.println( "File ID: " + file.getId());
//			System.out.println( "File WebContentLink: " + file.getWebContentLink());
//			System.out.println( "File WebViewLiink: " + file.getWebViewLink());
			
			assert( true);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			assert( false);
		}
	}

}
