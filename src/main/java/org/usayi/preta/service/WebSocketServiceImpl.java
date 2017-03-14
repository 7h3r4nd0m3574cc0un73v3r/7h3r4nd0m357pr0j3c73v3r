package org.usayi.preta.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.usayi.preta.Views;
import org.usayi.preta.buzlayer.IAdminRESTAPI;
import org.usayi.preta.buzlayer.IManagerRESTAPI;
import org.usayi.preta.buzlayer.IPublicRESTAPI;
import org.usayi.preta.entities.ArticleOrder;
import org.usayi.preta.entities.Notification;
import org.usayi.preta.entities.OrderStatus;
import org.usayi.preta.entities.Payment;
import org.usayi.preta.entities.json.LoggedUser;

import com.fasterxml.jackson.annotation.JsonView;

@Service
public class WebSocketServiceImpl implements WebSocketService
{
	@SuppressWarnings( "unused")
	private static final Log logger = LogFactory.getLog(WebSocketServiceImpl.class);
	
	@Autowired
	private final IPublicRESTAPI pRESTAPI;
	
	private final SimpMessageSendingOperations messagingTemplate;
	
	@Autowired
	public WebSocketServiceImpl(SimpMessageSendingOperations messagingTemplate,
								IPublicRESTAPI pRESTAPI,
								IAdminRESTAPI aRESTAPI,
								IManagerRESTAPI mRESTAPI)
	{
		this.messagingTemplate = messagingTemplate;
		this.pRESTAPI = pRESTAPI;
		this.aRESTAPI = aRESTAPI;
		this.mRESTAPI = mRESTAPI;
	}

	/* Shared */
	@Override
	@JsonView( Views.Public.class)
	public void newUserNotification( final Notification n)
	{
		/* Destination */
		String d = "/topic/logged-user/";
		/* Order matters here */

		if( n.isManager())
			d += "manager";
		else if( n.isBuyer())
			d += "buyer";
		else if( n.isAdmin())
			d += "admin";
		
		d += "/new-notification";
		
		System.err.println( d);
		messagingTemplate.convertAndSendToUser(n.getUser().getUsername(),
								   d, n);
	}
	/* End Shared Section */
	
	/* Buyer Section */
	/* Updated Profile */
	@Override
	public void userProfileUpdated( final String username)
	{
		messagingTemplate.convertAndSendToUser( username,
												"/topic/logged-user",
												new LoggedUser( pRESTAPI.loadUserByUsername(username)));
	}
	/* User Cart Updated */
	@Override
	public void userCartUpdated(String username)
	{
		messagingTemplate.convertAndSendToUser( username,
												"/topic/logged-user/cart-items",
												pRESTAPI.loadCartItems( pRESTAPI.loadUserByUsername(username).getUserInfo().getId(), 1, 0));
	}
	/* User Orders */
	@Override
	public void userArticleOrdersUpdated( String username)
	{
		messagingTemplate.convertAndSendToUser( username,
												"/topic/logged-user/article-orders",
												pRESTAPI.loadBuyerArticleOrders( pRESTAPI.loadUserByUsername(username).getUserInfo().getId(), 1, 0, OrderStatus.ALL, true));
	}
	@Override
	public void userArticleOrderUpdated(String username, Long id)
	{
		messagingTemplate.convertAndSendToUser( username, "/topic/logged-user/article-order/" + id,
											    pRESTAPI.loadArticleOrder(id));
	}
	/* End Buyer Section */

	/* Admin Section */
	@Autowired
	private final IAdminRESTAPI aRESTAPI;
	
	@Override
	public void buyerProfileUpdated()
	{
		messagingTemplate.convertAndSend( "/topic/admin/profiles-pending-conf",
										  aRESTAPI.loadBuyerPendingProfileValidation(1, 0));
	}
	@Override
	public void articleOrderToConfirm( Long id) {
		messagingTemplate.convertAndSend( "/topic/admin/article-order-to-confirm",
										  aRESTAPI.loadArticleOrder(id));
	}
	@Override
	public void deliveredArticleOrder( Long id)
	{
		messagingTemplate.convertAndSend( "/topic/admin/delivered-article-order",
										  aRESTAPI.loadArticleOrder(id));
	}
	@Override
	public void updatedArticleOrder( Long id) {
		messagingTemplate.convertAndSend( "/topic/article-order/" + id,
										  aRESTAPI.loadArticleOrder(id));
	}
	@Override
	public void alteredPayment(Payment entity)
	{
		messagingTemplate.convertAndSend( "/topic/admin/payment/" + entity.getId(), aRESTAPI.loadPayment( entity.getId()));
	}
	/* End Admin Section */
	
	/* Manager */
	@Autowired
	private final IManagerRESTAPI mRESTAPI;
	

	@Override
	public void confirmedArticleOrder( Long id)
	{
		ArticleOrder articleOrder = mRESTAPI.loadArticleOrder(id);
		
		messagingTemplate.convertAndSendToUser( mRESTAPI.loadArticleOrderEShop(id).getManager().getUsername(),
										  "/topic/manager/confirmed-article-order",
										  articleOrder);
	}
	/* End Manager section */
}
