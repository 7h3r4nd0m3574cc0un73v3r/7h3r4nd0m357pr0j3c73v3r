package org.usayi.preta.service;

import org.usayi.preta.entities.*;

public interface WebSocketService
{
	/* Shared */
	void newUserNotification( final Notification n);
	
	/* Buyer */
	void userProfileUpdated( final String username);
	void userCartUpdated( final String username);
	void userArticleOrdersUpdated( final String username);
	void userArticleOrderUpdated( final String username, final Long id);
	
	/* Admin */
	void buyerProfileUpdated();
	void articleOrderToConfirm( final Long id);
	void deliveredArticleOrder( final Long id);
	void updatedArticleOrder( final Long id);
	
	/* Manager */
	void confirmedArticleOrder( final Long id);
}
