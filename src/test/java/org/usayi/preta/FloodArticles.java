package org.usayi.preta;

import java.io.File;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.usayi.preta.buzlayer.IManagerRESTAPI;
import org.usayi.preta.buzlayer.IRESTAPI;
import org.usayi.preta.config.Tools;
import org.usayi.preta.entities.Article;
import org.usayi.preta.entities.ArticleState;
import org.usayi.preta.entities.EShop;
import org.usayi.preta.entities.Picture;

public class FloodArticles
{
	private ClassPathXmlApplicationContext context;
	@Test
	public void flood()
	{
		try
		{	
			context = new ClassPathXmlApplicationContext( new String[] { "app-context.xml"});
			
			IRESTAPI restBL = (IRESTAPI) context.getBean( "restBL");
			IManagerRESTAPI mRESTAPI = (IManagerRESTAPI) context.getBean( "mRESTAPI");
			
			String[] nameParts = { "Knob", "Bread", "Fanta", "Beatmania", "SDVX", "WOZ", "Apple", "IPhone", "Sam Sung Galaxios", "Rick", "Morty",
									"Bra", "Pantsu", "Deception", "Arduino", "Terraformars", "Hadoken", "Dakimakura", "だきまくら",  "Stuff", "Battlefield",
									"Call Of Duty", "かつらじゃない" };
			
			File[] files = (new File( Tools.uploadPath)).listFiles();
			
			List<String> pictureFileNames = new ArrayList<String>();
			for( File file : files) {
				if( !file.isDirectory())
					pictureFileNames.add( file.getAbsolutePath());
			}
			
//			String[] pictureFileNames = uploadDir.list();
			
			@SuppressWarnings({ "unchecked", "unused" })
			List<EShop> eShops = (List<EShop>) restBL.listEShop( 1, 0).getEntities();
			final Integer numArticleToInsert = 8;
			
			SecureRandom randGen = new SecureRandom();
			for( Integer i = 0; i < numArticleToInsert; ++i) {
				Article article = new Article();
				article.setName( nameParts[ Math.abs( randGen.nextInt()) % nameParts.length] + " " +
							     nameParts[ Math.abs( randGen.nextInt()) % nameParts.length] + " " +
							     nameParts[ Math.abs( randGen.nextInt()) % nameParts.length]);
				article.setPrice( (Math.abs( randGen.nextInt()) % 100000) + 1F);
				article.setState( ArticleState.NEW);
				/*article.seteShop( eShops.get( Math.abs( randGen.nextInt()) % eShops.size()));*/
				article.setStock( Math.abs( randGen.nextInt()) % 10000 + 11);
				article.setThreshold( article.getStock() / 10);
				
				Picture picture = new Picture( pictureFileNames.get(  Math.abs( randGen.nextInt()) % pictureFileNames.size()));
				picture.setIsDefault( true);
				restBL.addPicture( picture);
				article.addPicture(picture);
//				Long eShopId = new Long( Math.abs( randGen.nextInt()) % eShops.size());
				Long eShopId = 1L;
				article.setRelId( mRESTAPI.loadEShopArticles( eShopId, 1, 0).getItemsNumber() + 1L);
				restBL.addArticleToEshop( eShopId, article);
			}
			assert( true);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			assert( false);
		}
	}
}
