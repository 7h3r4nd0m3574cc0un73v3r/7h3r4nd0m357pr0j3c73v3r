package org.usayi.preta.scheduling;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.scheduling.annotation.Async;
import org.usayi.preta.dao.IPretaDAO;
import org.usayi.preta.entities.AdvOffer;
import org.usayi.preta.entities.EShop;
import org.usayi.preta.entities.GenericStatus;
import org.usayi.preta.entities.ShopSub;
import org.usayi.preta.service.NotificationService;

@Description( value="Task to expire either and AdvOffer or an ShopSub")
public class ScheduledExpireTask implements Runnable
{
	@Autowired
	private IPretaDAO dao;
	
	@Autowired
	private NotificationService notifications;
	
	private Object entity;
	
	public ScheduledExpireTask(Object entity, IPretaDAO dao, NotificationService notifications)
	{
		super();
		this.entity = entity;
		this.notifications = notifications;
		this.dao = dao;
	}

	
	public ScheduledExpireTask(Object entity)
	{
		super();
		this.entity = entity;
	}

	@Override
	@Async
	public void run() {
		try
		{
			if( entity.getClass().equals( ShopSub.class)) {
				ShopSub sub = dao.loadShopSub( (( ShopSub) entity).getId());
				System.err.println( "DEBUG: Detected ShopSub #" + sub.getId());
				sub.setSubStatus(GenericStatus.EXPIRED);
				
				/* Remove the expired ShopSub from EShop */
				if( sub.equals( sub.geteShop().getCurrentShopSub())) {
					EShop e = dao.loadEShop( sub.geteShop().getId());
					e.setCurrentShopSub( null);
					
					dao.updateEShop( e);
				}
				
				dao.updateShopSub(sub);
				/* Notify Manager of Expired entity */
				notifications.managerExpiredEntity(sub);
				
				System.err.println( "DEBUG: ShopSub Expired => " + sub.getId() + " @ " + new SimpleDateFormat( "Y-M-d H:m:s").format( new Date( System.currentTimeMillis())));
			}
			
			if( entity.getClass().equals( AdvOffer.class)) {
				System.err.println( "DEBUG: Detected AdvOffer " + (( AdvOffer) entity).getId());
				
				AdvOffer ad = dao.loadAdvOffer( ((AdvOffer) entity).getId());
				ad.setStatus( GenericStatus.EXPIRED);
				
				dao.updateAdvOffer( ad);

				/* Notify Manager of expired entity */
				notifications.managerExpiredEntity(ad);
				
				System.err.println( "DEBUG: AdvOffer Expired => " + ad.getId() + " @ " + new SimpleDateFormat( "Y-M-d H:m:s").format( new Date( System.currentTimeMillis())));
			}
//			context.close();
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
	}

	public Object getEntity()
	{
		return entity;
	}

	public void setEntity(Object entity)
	{
		this.entity = entity;
	}


	public IPretaDAO getDao()
	{
		return dao;
	}


	public void setDao(IPretaDAO dao)
	{
		this.dao = dao;
	}


	public ScheduledExpireTask()
	{
		super();
	}


	public NotificationService getNotifications()
	{
		return notifications;
	}


	public void setNotifications(NotificationService notifications)
	{
		this.notifications = notifications;
	}
}
