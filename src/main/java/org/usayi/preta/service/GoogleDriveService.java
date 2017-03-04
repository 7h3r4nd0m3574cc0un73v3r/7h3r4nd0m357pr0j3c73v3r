package org.usayi.preta.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface GoogleDriveService
{
	/* Tries to upload and return the GoogleId of the FIle
	 * if succeeded
	 */
	String uploadFile( final MultipartFile file, final GoogleDriveUploadType type) throws IllegalStateException, IOException;
	/* Returns both the GoogleDriveId and the Local Path */
	HashMap<String, String> uploadFileDebug( final MultipartFile file, final GoogleDriveUploadType type) throws IllegalStateException, IOException;
	/* Tets */
	/* Returns List of Articles Pictures */
	public List<String> loadArticlesPicturesTEST();
}
