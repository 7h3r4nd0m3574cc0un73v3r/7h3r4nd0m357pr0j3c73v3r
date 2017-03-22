package org.usayi.preta.service;

import org.usayi.preta.entities.Article;
import org.usayi.preta.entities.ArticleOrder;
import org.usayi.preta.entities.Expense;
import org.usayi.preta.entities.Payment;

public interface NotificationService
{	
	/* Shared */
	void approvedEntity( final Object entity) throws Exception;
	void entityPaymentAltered( final Payment payment, final Object entity);
	void deliveredArticleOrder( final ArticleOrder entity);
	
	/* Buyer */
	void buyerUpReqApproved( final Long userId);
	void buyerOrderDelivering( final ArticleOrder entity);
	void buyerPendingRating( final Long userId);
	
	/* Manager */
	void managerExpiringEntity( final Object entity);
	void managerExpiredEntity( final Object entity);
	void managerArticleThreshold( final Article entity);
	void newExpenseRegistered( final Expense entity);
	
	/* Admin */
	void entityApprovalReady( final Object entity);
	void entityPayConfReady( final Object entity);
	void newRegisteredPayment( final Payment entity);
}
