package org.usayi.preta;

import java.security.SecureRandom;
import java.util.List;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.usayi.preta.buzlayer.IRESTAPI;
import org.usayi.preta.entities.DurationType;
import org.usayi.preta.entities.ShopStatus;
import org.usayi.preta.entities.SubOffer;

public class SubOfferFlooding
{
	private ClassPathXmlApplicationContext context;
	@Test
	public void floodSubOffers()
	{
		try
		{	
			context = new ClassPathXmlApplicationContext( new String[] { "app-context.xml"});
			
			IRESTAPI restBL = (IRESTAPI) context.getBean( "restBL");
			
			String[] nameParts = { "Test", "Testos", "Testis", "Testang", "Testorino", "Testorazi",
									"Bastos", "Tarzos", "Torvald", "Titania", "Tiberius", "Tegaria"
								 };
			DurationType[] surationTypes = { DurationType.DAY, DurationType.WEEK,
											 DurationType.MONTH,
											 DurationType.YEAR };
			
			final int subOfferNumber = 500;
			
			@SuppressWarnings("unchecked")
			List<ShopStatus> shopStatuses = (List<ShopStatus>) restBL.listShopStatus( 1, 0).getEntities();
			
			SecureRandom randGen = new SecureRandom();
			
			for( int i = 0; i < subOfferNumber; i++)
			{
				SubOffer subOffer = new SubOffer();
				subOffer.setDuration( Math.abs( randGen.nextInt()) % 10 + 1);
				subOffer.setDurationType( surationTypes[ Math.abs( randGen.nextInt()) % surationTypes.length]);
				subOffer.setPrice( Math.abs( randGen.nextInt() % 100000F));
				subOffer.setTitle(
									nameParts[ Math.abs( randGen.nextInt()) % nameParts.length] + " " +
									nameParts[ Math.abs( randGen.nextInt()) % nameParts.length]);
				subOffer.setShopStatus( shopStatuses.get( Math.abs( randGen.nextInt()) % shopStatuses.size()));
				
				restBL.addSubOffer( subOffer);
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
