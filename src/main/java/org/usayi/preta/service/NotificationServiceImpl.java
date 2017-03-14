package org.usayi.preta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.usayi.preta.config.Tools;
import org.usayi.preta.dao.IPretaDAO;
import org.usayi.preta.entities.AdvOffer;
import org.usayi.preta.entities.Article;
import org.usayi.preta.entities.ArticleOrder;
import org.usayi.preta.entities.EShop;
import org.usayi.preta.entities.Notification;
import org.usayi.preta.entities.NotificationEntity;
import org.usayi.preta.entities.NotificationType;
import org.usayi.preta.entities.Payment;
import org.usayi.preta.entities.ShopSub;
import org.usayi.preta.entities.User;

@Service
public class NotificationServiceImpl implements NotificationService
{
	@Autowired
	private IPretaDAO pretaDao;
	
	/* Methods use abstraction:
	 * When called, the method act according to the passed
	 * argument's class. If there is a parameter declared
	 * as "entity", it means a check if done in the method
	 * to determine which class it comes from and make necessary
	 * operations according do that class.
	 */
	
	@Autowired
	private WebSocketService websockets;
	
	@Autowired
	EMailService emails;
	
	@Override
	public void approvedEntity( Object entity) throws Exception
	{	
		Notification n = null;
		
		if( entity.getClass().equals( User.class)) {
			
			n = new Notification( NotificationType.APPROVED, null, NotificationEntity.PROFILE,
								  null, null, null, null, true, true, true);
			
			pretaDao.addNotification( n, ((User) entity).getUserInfo().getId());
			
			websockets.newUserNotification( n);
		}
		
		if( entity.getClass().equals( EShop.class)) {
			EShop eShop = (EShop) entity;
			n = new Notification( NotificationType.APPROVED, null, NotificationEntity.ESHOP,
					  null, null, null, null, true, false, true);
			n.setEntAbsId( eShop.getId());
			n.setEntRelId( eShop.getRelId());
			
			pretaDao.addNotification( n, pretaDao.loadEShopManager( ((EShop) entity).getId()).getUserInfo().getId());
			
			websockets.newUserNotification( n);
		}
	}

	@Override
	public void entityPaymentAltered(Payment payment, Object entity)
	{
		/* TODO Optimise by rewriting Notification to allow constructor from partial entity */
		Notification n = null;
		
		if( entity.getClass().equals( ArticleOrder.class)) {
			ArticleOrder articleOrder = ( ArticleOrder) entity;
			/* Notify Buyer that ArticleOrder Payment was reviewed */
			n = new Notification( NotificationType.ALTERED_PAYMENT, null, NotificationEntity.ORDER, 
								  payment.getId(), payment.getId(), articleOrder.getId(), articleOrder.getBuyerRelId(), payment.getIsValid(),
								  true, false);
			pretaDao.addNotification( n, articleOrder.getUser().getUserInfo().getId());
			websockets.newUserNotification( n);
			
			/* Notify Manager that ArticleOrder is ready to be delivered */
			EShop eShop = pretaDao.loadArticleOrderEShop( articleOrder.getId());
			User manager = pretaDao.loadEShopManager( eShop.getId());

			n = new Notification( NotificationType.ALTERED_PAYMENT, null, NotificationEntity.ORDER, 
								  payment.getId(), payment.getId(), articleOrder.getId(), articleOrder.getBuyerRelId(), payment.getIsValid(),
								  false, true);
			
			pretaDao.addNotification( n, manager.getUserInfo().getId());
			websockets.newUserNotification( n);
		}
		
		if( entity.getClass().equals( ShopSub.class)) {
			ShopSub shopSub = (ShopSub) entity;
			
			n = new Notification( NotificationType.ALTERED_PAYMENT, null, NotificationEntity.SUB,
								  payment.getId(), payment.getId(), shopSub.getId(), shopSub.getRelId(),
								  payment.getIsValid(), false, true);
			
			EShop eShop = shopSub.geteShop();
			User manager = pretaDao.loadEShopManager( eShop.getId());
			pretaDao.addNotification(n, manager.getUserInfo().getId());
			websockets.newUserNotification( n);
		}
		
		if( entity.getClass().equals( AdvOffer.class)) {
			AdvOffer ad = (AdvOffer) entity;
			
			n = new Notification( NotificationType.ALTERED_PAYMENT, null, NotificationEntity.SUB,
					payment.getId(), payment.getId(), ad.getId(), ad.getRelId(),
					payment.getIsValid(), false, true);

			EShop eShop = pretaDao.loadAdvOfferEshop( ad.getId());
			User manager = pretaDao.loadEShopManager( eShop.getId());
			pretaDao.addNotification(n, manager.getUserInfo().getId());
			websockets.newUserNotification( n);
		}
	}

	@Override
	public void deliveredArticleOrder(ArticleOrder entity)
	{
		Notification n = new Notification( NotificationType.DELIVERED_ORDER, null,
										   NotificationEntity.ORDER, null, null,
										   entity.getId(), entity.geteShopRelId(), true,
										   false, true);
		n.setAdmin(true);
		
		/* Sending notification to Manager */
		EShop eShop = pretaDao.loadArticleOrderEShop( entity.getId());
		User manager = pretaDao.loadEShopManager(eShop.getId());
		pretaDao.addNotification(n, manager.getUserInfo().getId());
		websockets.newUserNotification( n);
		/* TODO Send Notifications to Admin */
	}

	@Override
	public void buyerUpReqApproved( Long userId)
	{
		/* TODO
		 * Make Upgraderequest more explicit 
		 */
		Notification n = new Notification( NotificationType.APPROVED_UPGRADE_REQUEST, null,
										   NotificationEntity.UPGRADE, null, null, null, null,
										   true, true, true);
		
		pretaDao.addNotification(n, userId);
		websockets.newUserNotification( n);
	}

	@Override
	public void buyerOrderDelivering(ArticleOrder entity)
	{
		Notification n = new Notification(NotificationType.DELIVERING_ORDER, null,
										  NotificationEntity.ORDER, null, null,
										  entity.getId(), entity.getBuyerRelId(),
										  true, true, false);
		
		pretaDao.addNotification(n, entity.getUser().getUserInfo().getId());
		websockets.newUserNotification( n);
	}

	@Override
	public void buyerPendingRating(Long userId)
	{
		Notification n = new Notification(NotificationType.PENDING_RATING,
										  (long) pretaDao.listOrderedArticleToRate(userId, 1, 0).getEntities().size(),
										  NotificationEntity.ARTICLE, null, null, null, null, true, true, false);
		
		pretaDao.addNotification(n, userId);
		websockets.newUserNotification( n);
	}

	/* Manager */
	@Override
	public void managerExpiredEntity( Object entity)
	{
		Notification n = new Notification();
		n.setnType( NotificationType.EXPIRED);
		
		if( entity.getClass().equals( ShopSub.class)) {
			ShopSub shopSub = ( ShopSub) entity;
			n.setEntType( NotificationEntity.SUB);
			n.setEntAbsId( shopSub.getId());
			n.setEntRelId( shopSub.getRelId());
			n.setManager(true);
			pretaDao.addNotification(n, pretaDao.loadShopSubManager(shopSub.getId()).getUserInfo().getId());
			websockets.newUserNotification( n);
		}
		
		if( entity.getClass().equals( AdvOffer.class)) {
			AdvOffer advOffer = ( AdvOffer) entity;
			n.setEntType( NotificationEntity.AD);
			n.setEntAbsId( advOffer.getId());
			n.setEntRelId( advOffer.getId());
			n.setManager(true);
			pretaDao.addNotification(n, pretaDao.loadAdvOfferManager(advOffer.getId()).getUserInfo().getId());
			websockets.newUserNotification( n);
		}
		
		/* Notify Expiration By EMail */
		emails.expiredEntity( entity);
	}
	
	@Override
	public void managerExpiringEntity(Object entity)
	{
		Notification n = new Notification();
		n.setnType( NotificationType.EXPIRING);
		n.setManager(true);
		
		if( entity.getClass().equals( ShopSub.class)) {
			ShopSub shopSub = ( ShopSub) entity;
			n.setEntType(NotificationEntity.SUB);
			n.setEntAbsId( shopSub.getId());
			n.setEntRelId( shopSub.getRelId());
			
			pretaDao.addNotification(n, pretaDao.loadShopSubManager(shopSub.getId()).getUserInfo().getId());
			websockets.newUserNotification( n);
		}
		
		if( entity.getClass().equals( AdvOffer.class)) {
			AdvOffer advOffer = ( AdvOffer) entity;
			n.setEntType( NotificationEntity.AD);
			n.setEntAbsId( advOffer.getId());
			n.setEntRelId( advOffer.getId());
			
			pretaDao.addNotification(n, pretaDao.loadAdvOfferManager(advOffer.getId()).getUserInfo().getId());
			websockets.newUserNotification( n);
		}
		
		/* Notify Expiration By EMail */
		emails.expiringEntity( entity);
	}

	@Override
	public void managerArticleThreshold(Article entity)
	{
		Notification n = new Notification();
		n.setnType( NotificationType.ARTICLE_THRESHOLD);
		n.setEntType( NotificationEntity.ARTICLE);
		n.setEntAbsId( entity.getId());
		n.setEntRelId( entity.getRelId());
		n.setManager( true);
		/* Find the Bank Manager ;) */
		EShop eShop = pretaDao.loadArticleEShop(entity.getId());
		User manager = pretaDao.loadEShopManager(eShop.getId());
		
		pretaDao.addNotification(n, manager.getUserInfo().getId());
		websockets.newUserNotification( n);
	}

	/* Admin */
	@Override
	public void entityApprovalReady(Object entity)
	{
		if( entity.getClass().equals( EShop.class)) {
			EShop eShop = (EShop) entity;
			Notification n = new Notification();
			n.setnType(NotificationType.APPROVAL_READY);
			n.setEntAbsId( eShop.getId());
			n.setEntType(NotificationEntity.ESHOP);
			n.setAdmin( true);
			/* TODO Notify only supervising admins
			
			User manager = pretaDao.loadEShopManager(eShop.getId());
			Long supervisingAdminId = pretaDao.loadManagerSupervisingAdmin(manager.getUserInfo().getId()).getUserInfo().getId(); */

			/*
			 * TODO Send Notification only to supervising Manager
			 */
			for ( User admin : pretaDao.loadAdmins())
			{
				pretaDao.addNotification(n, admin.getUserInfo().getId());
				websockets.newUserNotification( n);
			}
		}
		
		/* TODO
		 * Incorpore WebSokcets
		 */
		if( entity.getClass().equals( User.class)) {
			User user = (User) entity;
			
			if( Tools.computeUserProfileComp( user) >= 100) {
				Notification n = new Notification();
				n.setnType(NotificationType.APPROVAL_READY);
				n.setEntAbsId( user.getUserInfo().getId());
				n.setEntType( NotificationEntity.PROFILE);
				n.setAdmin( true);
				
				for( User admin : pretaDao.loadAdmins()) {
					System.out.println( "Admn => "  +admin.getUsername());
					
					pretaDao.addNotification(n, admin.getUserInfo().getId());
					websockets.newUserNotification( n);
				}
			}
		}
		
		/* TODO Upgrade Request To */
	}

	@Override
	public void entityPayConfReady(Object entity)
	{
		/* When an entity is ready for it's payment to be confirmed */
		Notification n = new Notification();
		n.setnType( NotificationType.CONFIRMATION_READY);
		if( entity.getClass().equals( ShopSub.class)) {
			ShopSub shopSub = (ShopSub) entity;
			n.setEntType( NotificationEntity.SUB);
			n.setEntAbsId( shopSub.getId());
			n.setAdmin( true);
			/* TODO Broadcast to all admins 
			 * TODO Broadcasr to Admin wielding the destination EAccount */
			
			/* TODO If Manager doens't have manager yet, broadcast to all admins instead */
			/*Long managerId = pretaDao.loadShopSubManager( shopSub.getId()).getUserInfo().getId();
			Long adminId = pretaDao.loadManagerSupervisingAdmin(managerId).getUserInfo().getId();*/
			
			for( User admin : pretaDao.loadAdmins())
			{
				pretaDao.addNotification(n, admin.getUserInfo().getId());
				websockets.newUserNotification( n);
			}
		}
		
		if( entity.getClass().equals( ArticleOrder.class)) {
			System.out.println( "Registering Order Notification");
			
			ArticleOrder articleOrder = (ArticleOrder) entity;
			n.setEntType( NotificationEntity.ORDER);
			n.setEntAbsId( articleOrder.getId());
			n.setEntRelId( articleOrder.getBuyerRelId());
			n.setAdmin( true);
			/* TODO Broadcast to all admins 
			 * TODO Broadcasr to Admin wielding the destination EAccount */
			for( User admin : pretaDao.loadAdmins())
			{
				pretaDao.addNotification(n, admin.getUserInfo().getId());
				websockets.newUserNotification(n);
			}
		}
		
		if( entity.getClass().equals( AdvOffer.class)) {
			AdvOffer advOffer = (AdvOffer) entity;
			n.setEntType( NotificationEntity.AD);
			n.setEntAbsId( advOffer.getId());
			n.setEntRelId( advOffer.getRelId());
			n.setAdmin( true);
			/* TODO Broadcast to all admins 
			 * TODO Broadcasr to Admin wielding the destination EAccount or supervising the EShop */
			
			for( User admin : pretaDao.loadAdmins())
			{
				pretaDao.addNotification(n, admin.getUserInfo().getId());
				websockets.newUserNotification(n);
			}
		}
	}

	@Override
	public void newRegisteredPayment( Payment entity)
	{
		Notification n = new Notification();
		n.setAdmin(true);
		n.setBuyer(false);
		n.setManager(false);
		n.setnType( NotificationType.NEW_PAYMENT);
		n.setEntType( NotificationEntity.PAYMENT);
		n.setEntAbsId(entity.getId());
		n.setEntRelId(entity.getRelId());
		n.setPaymentAbsId(entity.getId());
		n.setPaymentRelId(n.getEntRelId());
		n.setUser( pretaDao.loadUser( entity.getAdminEAccount().getUser().getId()));
		
		/* Persist Notification and Braodast to WebSocket */
		pretaDao.addNotification(n, n.getUser().getUserInfo().getId());
		websockets.newUserNotification(n);
	}
	
	/* Get N Set */
	public IPretaDAO getPretaDao()
	{
		return pretaDao;
	}

	public void setPretaDao(IPretaDAO pretaDao)
	{
		this.pretaDao = pretaDao;
	}

	public WebSocketService getWebsockets()
	{
		return websockets;
	}

	public void setWebsockets(WebSocketService websockets)
	{
		this.websockets = websockets;
	}

	@ExceptionHandler( { Exception.class})
	private void defaultExceptionHandler( Exception e) {
		e.printStackTrace();
	}
}
