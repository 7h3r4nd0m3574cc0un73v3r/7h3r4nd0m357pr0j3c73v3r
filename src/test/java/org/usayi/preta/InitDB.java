package org.usayi.preta;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.usayi.preta.buzlayer.IAdminRESTAPI;
import org.usayi.preta.buzlayer.IPublicRESTAPI;
import org.usayi.preta.buzlayer.IRESTAPI;
import org.usayi.preta.config.CustomPasswordEncoder;
import org.usayi.preta.entities.AdvOption;
import org.usayi.preta.entities.DurationType;
import org.usayi.preta.entities.EAccount;
import org.usayi.preta.entities.EMoneyProvider;
import org.usayi.preta.entities.PaymentType;
import org.usayi.preta.entities.Role;
import org.usayi.preta.entities.ShopStatus;
import org.usayi.preta.entities.SubOffer;
import org.usayi.preta.entities.User;
import org.usayi.preta.entities.UserInfo;

public class InitDB
{
	private ClassPathXmlApplicationContext context;
	
	@Test
	public void initRoles()
	{
		try
		{	
			context = new ClassPathXmlApplicationContext( new String[] { "app-context.xml"});
			
			IRESTAPI restBL = (IRESTAPI) context.getBean( "restBL");
			
			IPublicRESTAPI pRESTAPI = (IPublicRESTAPI) context.getBean( "pRESTAPI");
			IAdminRESTAPI aRESTAPI = (IAdminRESTAPI) context.getBean( "aRESTAPI");
			
			System.err.println( "DEBUG: Adding EMPs");
			
			if( aRESTAPI.loadEMoneyProviderByName( "MTN") == null)
				aRESTAPI.addEMoneyProvider( new EMoneyProvider( "MTN","\\+?(?:229)?(?:97|96|61|62|66|67)\\d{6}", null));
			
			if( aRESTAPI.loadEMoneyProviderByName( "Moov") == null)
				aRESTAPI.addEMoneyProvider( new EMoneyProvider( "Moov","\\+?(?:229)?(?:95|94)\\d{6}", null));
			
			System.err.println( "DEBUG: Add PaymentType");
			
			if( restBL.getPaymentType( 1L) == null)
			{
				restBL.addPaymentType( new PaymentType( "En ligne"));
			}
			
			System.err.println( "DEBUG: Adding Roles");
			
			Collection<String> roles = new ArrayList<String>();
			roles.add( "ROLE_BUYER");
			roles.add( "ROLE_MANAGER");
			roles.add( "ROLE_ADMIN");
			
			for( String role : roles)
			{
				if( restBL.getRole( role) == null)
					restBL.addRole( new Role( role));
			}
			
			System.err.println( "DEBUG: Adding Root Admin");
			if( restBL.getUserByUsername( "admin0") == null)
			{
				try
				{
					//Register UserInfo for New User
					UserInfo userInfo = new UserInfo();
					userInfo.setEmail( "admin0@admins.com");
					restBL.addUserInfo( userInfo);
					
					EAccount eAccount = new EAccount();
					eAccount.setAccount( "97000097");
					eAccount.setRelId( userInfo.geteAccounts().size() + 1L);
					pRESTAPI.addEAccountToUserInfo(userInfo.getId(), restBL.getEMoneyProviderByName( "MTN").getId(), eAccount);
					
					EAccount eAccount2 = new EAccount();
					eAccount2.setAccount( "95000095");
					eAccount2.setRelId( userInfo.geteAccounts().size() + 1L);
					pRESTAPI.addEAccountToUserInfo( userInfo.getId(), restBL.getEMoneyProviderByName( "Moov").getId(), eAccount2);
					
					//Set base for User
					User user = new User();
					user.setUserInfo( userInfo);
					user.setUsername( "admin0");
					
					//Adding Roles to user
					user.addRole( restBL.getRole( "ROLE_ADMIN"));
					
					//Generating salt and confirmation token for user
					user.setSalt( RandomStringUtils.random( 32, true, true));
					//user.setConfToken( RandomStringUtils.random( 64, true, true));
					
					//Encoding password using CustomPasswordEncoder
					CustomPasswordEncoder passwordEncoder = new CustomPasswordEncoder();
					user.setPassword( passwordEncoder.encode( user.getSalt() + "admin0"));
					
					//Admin is enabled by default
					user.setIsEnabled( true);
					
					restBL.addUser( user);
				}
				catch( Exception e)
				{
					System.out.println( e.getMessage());
				}
				
			}
			
			try
			{
				if (!aRESTAPI.isSubOfferRegistered("Discovery Basic - 1 Semaine"))
				{
					System.out.print( "DEBUG: SubOffer Not Found");
					
					if (!aRESTAPI.isShopStatusRegistered("Basic"))
					{
						ShopStatus s = new ShopStatus();
						s.setArticleLimit(10);
						s.setDescription("Une boutique basique");
						s.setIsEnabled(true);
						s.setTitle("Basic");

						aRESTAPI.addShopStatus(s);
					}

					SubOffer s = new SubOffer();
					s.setDuration(1);
					s.setDurationType(DurationType.WEEK);
					s.setPrice(0F);
					s.setTitle("Discovery Basic - 1 Semaine");
					s.setIsEnabled(true);
					s.setShopStatus(aRESTAPI.loadShopStatus("Basic"));

					aRESTAPI.addSubOffer(s);
				} 
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			
			try
			{
				if (!aRESTAPI.isSubOfferRegistered("Discovery Basic - 1 Minute"))
				{
					if (!aRESTAPI.isShopStatusRegistered("Basic"))
					{
						ShopStatus s = new ShopStatus();
						s.setArticleLimit(10);
						s.setDescription("Une boutique basique");
						s.setIsEnabled(true);
						s.setTitle("Basic");

						aRESTAPI.addShopStatus(s);
					}

					SubOffer s = new SubOffer();
					s.setDuration(1);
					s.setDurationType(DurationType.MINUTE);
					s.setPrice(0F);
					s.setTitle("Discovery Basic - 1 Minute");
					s.setIsEnabled(true);
					s.setShopStatus(aRESTAPI.loadShopStatus("Basic"));

					aRESTAPI.addSubOffer(s);
				} 
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			
			try
			{
				if (!aRESTAPI.isAdvOptionRegistered("Pub Basic - 1 Minute - Code 1"))
				{
					AdvOption a = new AdvOption();
					a.setDuration(1);
					a.setDurationType(DurationType.MINUTE);
					a.setIsEnabled(true);
					a.setPrice(0F);
					a.setTitle("Pub Basic - 1 Minute - Code 1");

					aRESTAPI.addAdvOption(a);
				} 
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			
			assert (true);
			
			context.close();
		}
		catch (Exception e)
		{
			assertTrue( e.getMessage(), false);
		}
	}
}
