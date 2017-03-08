package org.usayi.preta.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.usayi.preta.config.Tools;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;

@Service
public class GoogleDriveServiceImpl implements GoogleDriveService
{	
	/* Google Drive Settings */
	/* Folders */
	public static final String uploadsFolderId = "0B0W0ktANhIydMWNiNVE0YUVZdVU";
	public static final String articleFolderId = "0B0W0ktANhIydV3Nxc19XUTczV2c";
	public static final String slidesFolderId = "0B0W0ktANhIydejZ5M3hBV29yMjg";
	public static final String eShopFolderId = "0B0W0ktANhIydRjVJaGVKMUJIMTg";
	public static final String statsFolderId = "0B0W0ktANhIydb0N1N1dGTlNCNGc";
	public static final String empFolderId = "0B0W0ktANhIydTy0tUDdJWENveU0";
	
	/* Files */
	public static final String defaultEShopPicId = "0B0W0ktANhIydQkhBQWcyUW5tZkU";
	public static final String defaultEmpPicId = "0B0W0ktANhIydOUN3aktmRW1iejA";
	public static final String defaultArticlePicId = "0B0W0ktANhIydZlk5dmxPOGotbXM";
	public static final String visitedArticleStatsId = "0B0W0ktANhIydSkpCbkxwa3lZaWc";
	public static final String defaultSlideId = "0B0W0ktANhIydQkhBQWcyUW5tZkU";
	
	/* Local Path Settings */
	public static final String uploadPath ="/random/preta/uploads/";
	
	private static final String articleUploadPath = uploadPath + "articles/";
	private static final String slideUploadPath = uploadPath + "slides/";
	private static final String eShopUploadPath = uploadPath + "e-shops/";
	private static final String empUploadPath = uploadPath + "emps/";
	
	private static Drive drive = GoogleDriveServiceBuilder.buildDriveService();
	
	@Override
	public String uploadFile( MultipartFile file, GoogleDriveUploadType type) throws IllegalStateException, IOException
	{
		
		/* Randomy generate filename */
		Random random = new Random();
		String generatedFilename = "";
		/* Create a Google File MetaData*/
		com.google.api.services.drive.model.File gFileMetaData = new com.google.api.services.drive.model.File();
		/* Insert the File into the GoogleDrive Service and get it's ID */
		String gId = "";
		
		switch (type)
		{
			case ARTICLE:
				generatedFilename = "article_";
				/* Set Containing Google Drive Folder */
				gFileMetaData.setParents( Collections.singletonList( articleFolderId));
				gId = defaultArticlePicId;
				break;
			case SLIDE:
				generatedFilename = "slide_";
				/* Set Containing Google Drive Folder */
				gFileMetaData.setParents( Collections.singletonList( slidesFolderId));
				gId = defaultSlideId;
				break;
			case EMP:
				generatedFilename = empUploadPath + "emp_";
				/* Set Containing Google Drive Folder */
				gFileMetaData.setParents( Collections.singletonList( empFolderId));
				gId = defaultEmpPicId;
				break;
			case ESHOP:
				generatedFilename = eShopUploadPath + "e-shop_";
				/* Set Containing Google Drive Folder */
				gFileMetaData.setParents( Collections.singletonList( eShopFolderId));
				gId = defaultEShopPicId;
				break;
			default:
				break;
		}
		
		generatedFilename += Math.abs(random.nextLong());
		
		/* Tmp local path */
		String path = Tools.uploadPath + generatedFilename;
		/* Set Google File Name */
		gFileMetaData.setName( generatedFilename);
		
		/* Temporarly save the file on server */
		File transferedFile = new File( path);
		file.transferTo( transferedFile);
		
		/* Read the saved file to Google File Content */
		FileContent gFileContent = new FileContent( "image/jpeg", transferedFile);
		
		gId = drive.files()
				  .create( gFileMetaData, gFileContent)
				  .execute().getId();
		
		return gId;
	}

	@Override
	public HashMap<String, String> uploadFileDebug(MultipartFile file, GoogleDriveUploadType type)
			throws IllegalStateException, IOException
	{
		HashMap<String, String> fileNames = new HashMap<String, String>();
		
		/* Randomy generate filename */
		Random random = new Random();
		String generatedFilename = "";
		/* Create a Google File MetaData*/
		com.google.api.services.drive.model.File gFileMetaData = new com.google.api.services.drive.model.File();
		/* Insert the File into the GoogleDrive Service and get it's ID */
		String gId = "";
		String path = "";
		
		switch (type)
		{
			case ARTICLE:
				generatedFilename = "article_";
				/* Set Containing Google Drive Folder */
				gFileMetaData.setParents( Collections.singletonList( articleFolderId));
				gId = defaultArticlePicId;
				path = articleUploadPath;
				break;
			case SLIDE:
				generatedFilename = "slide_";
				/* Set Containing Google Drive Folder */
				gFileMetaData.setParents( Collections.singletonList( slidesFolderId));
				gId = defaultSlideId;
				path = slideUploadPath;
				break;
			case EMP:
				generatedFilename = "emp_";
				/* Set Containing Google Drive Folder */
				gFileMetaData.setParents( Collections.singletonList( empFolderId));
				gId = defaultEmpPicId;
				path = empUploadPath;
				break;
			case ESHOP:
				generatedFilename = "e-shop_";
				/* Set Containing Google Drive Folder */
				gFileMetaData.setParents( Collections.singletonList( eShopFolderId));
				gId = defaultEShopPicId;
				path = eShopUploadPath;
				break;
			default:
				break;
		}
		
		generatedFilename += Math.abs(random.nextLong());
		
		/* Set Google File Name */
		gFileMetaData.setName( generatedFilename);
		
		path += generatedFilename;
		/* Temporarly save the file on server */
		System.out.println( "Transfering File to:" + path);
		
		File transferedFile = new File( path);
		file.transferTo( transferedFile);
		
		/* Read the saved file to Google File Content */
		FileContent gFileContent = new FileContent( "image/jpeg", transferedFile);
		/* Tty => if it doesn't work, fallback to localfile storage */
		try
		{
			System.out.println( "DEBUG: Uploading to Google Drive");
			gId = drive.files()
					  .create( gFileMetaData, gFileContent)
					  .execute().getId();
			System.out.println( "DEBUG: Upload Success => " + gId);
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
		
		/* Delete files */
		if( !Tools.keepLocalFiles)
			transferedFile.delete();
		
		fileNames.put( "localFileName", path);
		fileNames.put( "googleId", gId);
		
		return fileNames;
	}

	@Override
	public List<String> loadArticlesPicturesTEST() {
		try
		{
			List<String> ids = new ArrayList<String>();
			
			System.err.println( "DEBUG: Creating Drive");
			
			FileList list = drive.files().list().setFields( "files(id, name, parents)").execute();
			
			System.out.println( "DEBUG: List Size: " + list.getFiles().size());
			
			for( int i = 0; i < list.getFiles().size(); i++ ) {
				com.google.api.services.drive.model.File file = list.getFiles().get(i);
				System.out.println( "DEBUG: File #" + i + "; Id => " + file.getId());
//				System.out.println( "DEBUG: File MIME: " + file.getMimeType());
				
				if( file.getParents() != null) {
					List<String> parents = file.getParents();
					System.out.println( "DEBUG: Parent count => " + file.getParents().size());
					for( String p : parents) {
						if( p.equals(articleFolderId))
						{
							ids.add(file.getId());
						}
					}
				}
			}
			
			return ids;
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return new ArrayList<String>();
		}
	}
	
	public static boolean isValidDriveFile(String id) {
	    try {
	        com.google.api.services.drive.model.File f = drive.files().get( id).execute();
	        return !f.getId().isEmpty();
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("Unable to check if file with id: " + id + " exists on the Drive");
	    }
	    return false;
	}
}
