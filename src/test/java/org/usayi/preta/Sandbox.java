package org.usayi.preta;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.usayi.preta.buzlayer.IAdminRESTAPI;
import org.usayi.preta.service.EMailTemplateService;

public class Sandbox
{
	
	private ClassPathXmlApplicationContext context;
	
	
//	private Logger logger = LoggerFactory.getLogger( Sandbox.class);
	
	@Test
	public void test()
	{
		try
		{	
			context = new ClassPathXmlApplicationContext( new String[] { "app-context.xml"});
			
//			IPublicRESTAPI pRESTAPI = (IPublicRESTAPI) context.getBean( "pRESTAPI");
			
			@SuppressWarnings("unused")
			IAdminRESTAPI aRESTAPI = (IAdminRESTAPI) context.getBean( "aRESTAPI");
//			User user = pRESTAPI.loadUser( 4L);
			
//			System.out.println( Tools.computeUserProfileComp( user));
			
//			user.addRole( pRESTAPI.loadRole( "ROLE_MANAGER"));
//			pRESTAPI.updateUser( user);
//			User admin = pRESTAPI.loadUser( 1L);
//			User manager = pRESTAPI.loadUser( 8L);
			
//			aRESTAPI.addSupervisedManagerToAdmin( admin.getUserInfo().getId(), manager.getUserInfo().getId());
			
//			EShop eshop = pRESTAPI.loadEShop(1L);
			
//			System.out.println( Tools.computeEShopProfileComp( eshop));
			
//			ExpireAgentServiceImpl police = new ExpireAgentServiceImpl();
			
//			police.test();
			
//			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler(); 
//
//            scheduler.start();
//
//            JobDetail job;
//            
//            scheduler.shutdown();
			
//			SchedulerService sch = (SchedulerService) new SchedulerServiceImpl();
			
//			sch.test();
//			FileService files = new FileServiceImpl();
			
//			System.out.println( files.readSendGridApiKey());
//			
//			GoogleDriveService drive = new GoogleDriveServiceImpl();
//			
//			java.util.List<String> ids = drive.loadArticlesPicturesTEST();
//			System.out.println( "Size: " + ids.size());
//			
//			for( String id : ids)
//			{
//				System.out.println( "list.add(\"" +  id + "\");");
//			}
			String s = FileUtils.readFileToString(new File( EMailTemplateService.baseTmplPath), "UTF-8");
			
//			ST st = new ST( s);
			s = s.replaceAll( "<mailtitle>", "Activation de votre compte")
				 .replaceAll( "<activationlink>", "Test");
			
			System.out.println( s);
			
			assert( true);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			assert( false);
		}
	}
}
