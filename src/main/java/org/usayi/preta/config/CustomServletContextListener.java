package org.usayi.preta.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.usayi.preta.dao.IPretaDAO;
import org.usayi.preta.service.GoogleDriveServiceBuilder;

public class CustomServletContextListener implements ServletContextListener
{
	@Autowired
	IPretaDAO dao;
	
	/* Use this to run some stuff after WAR deployment */
	/* Like Init DB for Example */
	@Override
	public void contextDestroyed(ServletContextEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0)
	{
		
		// TODO: Add InitDB and Rescheduling here 
		System.out.println( "DEBUG: WAR Deployed and Running");
		
		java.io.File file = new java.io.File( GoogleDriveServiceBuilder.pathToDriveSecret + "preta-svc-account.p12");
		System.out.println( "Drive Secret Exists ? => " + file.exists());
		/* DB INIT */
		/* Init EMPs */
//		if( dao.getEMoneyProviderByName( "MTN") == null)
//			dao.addEMoneyProvider( new EMoneyProvider( "MTN","\\+?(?:229)?(?:97|96|61|62|66|67)\\d{6}", null));
//		
//		if( dao.getEMoneyProviderByName( "Moov") == null)
//			dao.addEMoneyProvider( new EMoneyProvider( "Moov","\\+?(?:229)?(?:95|94)\\d{6}", null));
//		
//		/* Init roles */
//		Collection<String> roles = new ArrayList<String>();
//		roles.add( "ROLE_BUYER");
//		roles.add( "ROLE_MANAGER");
//		roles.add( "ROLE_ADMIN");
//		roles.add( "ROLE_SUPER_ADMIN");
//		
//		for( String role : roles)
//		{
//			if( dao.loadRole( role) == null)
//				dao.addRole( new Role( role));
//		}
//		
//		/* Init Payment Types */
//		if( dao.loadPaymentType( 1L) == null)
//		{
//			dao.addPaymentType( new PaymentType( "En ligne - Monnaie Numérique"));
//		}
//		
//		/* Create Default Admin User */
//		if( dao.loadUserByUsername( "admin0") == null)
//		{
//			try
//			{
//				//Register UserInfo for New User
//				UserInfo userInfo = new UserInfo();
//				userInfo.setEmail( "admin0@admins.com");
//				dao.addUserInfo( userInfo);
//				
//				EAccount eAccount = new EAccount();
//				eAccount.setAccount( "97000097");
//				eAccount.setRelId( userInfo.geteAccounts().size() + 1L);
//				dao.addEAccountToUserInfo(userInfo.getId(), dao.getEMoneyProviderByName( "MTN").getId(), eAccount);
//				
//				EAccount eAccount2 = new EAccount();
//				eAccount2.setAccount( "95000095");
//				eAccount2.setRelId( userInfo.geteAccounts().size() + 1L);
//				dao.addEAccountToUserInfo( userInfo.getId(), dao.getEMoneyProviderByName( "Moov").getId(), eAccount2);
//				
//				//Set base for User
//				User user = new User();
//				user.setUserInfo( userInfo);
//				user.setUsername( "admin0");
//				
//				//Adding Roles to user
//				user.addRole( dao.loadRole( "ROLE_ADMIN"));
//				
//				//Generating salt and confirmation token for user
//				user.setSalt( RandomStringUtils.random( 32, true, true));
//				//user.setConfToken( RandomStringUtils.random( 64, true, true));
//				
//				//Encoding password using CustomPasswordEncoder
//				CustomPasswordEncoder passwordEncoder = new CustomPasswordEncoder();
//				user.setPassword( passwordEncoder.encode( user.getSalt() + "admin0"));
//				
//				//Admin is enabled by default
//				user.setIsEnabled( true);
//				
//				dao.addUser( user);
//			}
//			catch( Exception e)
//			{
//				e.printStackTrace();
//			}
//		}
	}

	public IPretaDAO getDao()
	{
		return dao;
	}

	public void setDao(IPretaDAO dao)
	{
		this.dao = dao;
	}
	
}
