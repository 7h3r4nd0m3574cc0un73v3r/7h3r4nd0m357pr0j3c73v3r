package org.usayi.preta.scheduling;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.usayi.preta.entities.AdvOffer;
import org.usayi.preta.entities.Notification;
import org.usayi.preta.entities.NotificationType;
import org.usayi.preta.entities.ShopSub;
import org.usayi.preta.service.NotificationService;

public class ExpiryReminderTasklet implements Tasklet
{
	@Autowired
	private NotificationService notifications;

	private Object entity;
	
	public ExpiryReminderTasklet(Object entity)
	{
		super();
		this.entity = entity;
	}

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception
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
		
		return RepeatStatus.FINISHED;
	}
	
	
}
