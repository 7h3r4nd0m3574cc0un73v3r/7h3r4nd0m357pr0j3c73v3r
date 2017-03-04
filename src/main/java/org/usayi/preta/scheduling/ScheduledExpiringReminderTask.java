package org.usayi.preta.scheduling;

import org.springframework.context.annotation.Description;
import org.springframework.scheduling.annotation.Async;
import org.usayi.preta.entities.AdvOffer;
import org.usayi.preta.entities.Notification;
import org.usayi.preta.entities.NotificationType;
import org.usayi.preta.entities.ShopSub;
import org.usayi.preta.service.NotificationService;

@Description( value="Task to remind the manager when 25% of the duration is reached for a ShopSub / AdvOffer")
public class ScheduledExpiringReminderTask implements Runnable
{	
	NotificationService notifications;
	
	private Object entity;
	
	public ScheduledExpiringReminderTask(Object entity, NotificationService notifications )
	{
		super();
		this.entity = entity;
		this.notifications = notifications;
	}

	@Override
	@Async
	public void run() {
		try
		{
			Notification n = new Notification();
			n.setnType( NotificationType.EXPIRING);
			n.setManager( true);
			
			if( entity.getClass().equals( ShopSub.class)) {
				/* DEBUG: remobe */
				ShopSub sub = ( ShopSub) entity;
				notifications.managerExpiringEntity( entity);
				
				System.err.println( "DEBUG: ShopSub Expiring Reminded => " + sub.getId());
			}
			
			if( entity.getClass().equals( AdvOffer.class)) {
				/* DEBUG: remobe */
				AdvOffer ad = ( AdvOffer) entity;
				notifications.managerExpiringEntity( entity);
				
				System.err.println( "DEBUG: AdvOffer Expired Reminded => " + ad.getId());
			}
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
}
