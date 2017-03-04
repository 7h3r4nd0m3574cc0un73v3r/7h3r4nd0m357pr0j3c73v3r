package org.usayi.preta.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.usayi.preta.dao.IPretaDAO;
import org.usayi.preta.entities.AdvOffer;
import org.usayi.preta.entities.ShopSub;
import org.usayi.preta.scheduling.ScheduledExpireTask;
import org.usayi.preta.scheduling.ScheduledExpiringReminderTask;

@Service
public class SchedulerServiceImpl implements SchedulerService
{
	/* DAO to act with Objects */
	@Autowired
	private IPretaDAO pretaDao;

	/* Notify Managers when their entities expire */
	@Autowired
	NotificationService notifications;
	
	/* Creates thread and shcedules exprining tasks*/
	@Autowired
	TaskScheduler scheduler;
	
	/* Margin to make sure the Entity is realy expired before updating it
	 * Given 1/2 Miute */
	private final long margin = 30000;
	
	@Override
	public void scheduleExpiring(Object entity)
	{
		/* Expire ShopSuh */
		if( entity.getClass().equals( ShopSub.class)) {
			ShopSub shopSub = pretaDao.loadShopSub( ((ShopSub) entity).getId());
			System.err.println( "DEBUG: ShopSub #" + shopSub.getId() + " scheduling expirying.");
			/* Schedule Expiry reminder */
			scheduler.schedule( new ScheduledExpiringReminderTask(entity, notifications), new Date( computeExpiringDelay( entity)));
			System.err.println( "DEBUG: Expiry Reminder Date : " + new SimpleDateFormat( "Y-M-d H:m:s").format( new Date( computeExpiringDelay( entity))));
			/* Give 30 seconds as delay to make sure the entity is really expired */
			scheduler.schedule( new ScheduledExpireTask( entity, pretaDao, notifications), new Date( shopSub.getEndDate().getTime() + margin));
			System.err.println( "DEBUG: Expired Date : " + new SimpleDateFormat( "Y-M-d H:m:s").format( new Date( shopSub.getEndDate().getTime() + margin)));
		}
		
		/* Expire AdvOffer */
		if( entity.getClass().equals( AdvOffer.class)) {
			AdvOffer ad = pretaDao.loadAdvOffer( ((AdvOffer) entity).getId());
			System.err.println( "DmEBUG: AdvOffer #" + ad.getId() + " scheduling expirying.");
			/* Schedule recall to Manager */
			scheduler.schedule( new ScheduledExpiringReminderTask(entity, notifications), new Date( computeExpiringDelay( entity)));
			System.err.println( "DEBUG: Expiry Reminder Date : " + new SimpleDateFormat( "Y-M-d H:m:s").format( new Date( computeExpiringDelay( entity))));
			/* Schedule Expiry */
			/* Give 30 seconds as delay to make sure the entity is really expired */
//			scheduler.schedule( new ScheduledExpireTask( entity, pretaDao, notifications), new Date( ad.getEndDate().getTime() + margin));
			ScheduledExpireTask t = new ScheduledExpireTask();
			t.setEntity(entity);
			scheduler.schedule( t, new Date( ad.getEndDate().getTime() + margin));
			System.err.println( "DEBUG: Expired Date : " + new SimpleDateFormat( "Y-M-d H:m:s").format( new Date( ad.getEndDate().getTime() + margin)));
		}
	}
	
	private static long computeExpiringDelay( final Object entity) {
		/* Computes the delay so the Manager is reminded when the duration reaches 75%
		 * of the defined time
		 */
		if( entity.getClass().equals( AdvOffer.class)) {
			AdvOffer ad = (AdvOffer) entity;
			
			return ((( long) 75 * ( ad.getEndDate().getTime() - ad.getStartDate().getTime())) / 100 + ad.getStartDate().getTime());
		}
		
		if( entity.getClass().equals( ShopSub.class)) {
			ShopSub sub = (ShopSub) entity;
			
			return ((( long) 75 * ( sub.getEndDate().getTime() - sub.getStartDate().getTime())) / 100 + sub.getStartDate().getTime());
		}
		
		/* Default */
		return System.currentTimeMillis();
	}
}
