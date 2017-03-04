package org.usayi.preta.service;
import org.usayi.preta.entities.*;
public interface EMailService
{
	/* Shared */
	void alteredPayment( final Payment payment, final Object entity);
	void approvedEntity( final Object entity);
	void activation( final User user);
	void passwordResetRequest( final User user);
	void accountValidated( final User user);
	void passwordUpdated( final User user);
	   
	/* Buyer */
	void registeredArticleOrder( final ArticleOrder entity);
	void deliveringArticleOrder( final ArticleOrder entity);
	
	/* Manager */
	void deliveredArticleOrder( final ArticleOrder entity);
	void expiringEntity( final Object entity);
	void expiredEntity( final Object entity);
	
	/* Admin */
	void sendErrorReport( final Exception e);
}
