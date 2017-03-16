package org.usayi.preta.service;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.usayi.preta.dao.IPretaDAO;
import org.usayi.preta.entities.AdvOffer;
import org.usayi.preta.entities.ArticleOrder;
import org.usayi.preta.entities.EShop;
import org.usayi.preta.entities.Payment;
import org.usayi.preta.entities.ShopSub;
import org.usayi.preta.entities.UpgradeRequest;
import org.usayi.preta.entities.User;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

@Service
public class EMailServiceImpl implements EMailService
{
	@Autowired
	EMailTemplateService templates;
	
	/* DEBUG */
	private static final Logger logger = LoggerFactory.getLogger( EMailServiceImpl.class);
	
	@Autowired
	private IPretaDAO pretaDao;
	
	/* Vars */
	private final String defaultFrom = "no-reply@hobossa.com";
	private final Email from = new Email( defaultFrom, "Hôbossa - Achetez et vendez en ligne !");
	
	/* Sendgrid Profile */
	private final String sendGridApiKey = System.getenv( "SENDGRID_API_KEY");
	
	SendGrid sendGrid = new SendGrid( sendGridApiKey);
	
	/* EMails */
	private static String errorEMail = "usayi.technology@gmail.com";
	
	/* Change the URL displayed in the e-mails */
	public static final String urlForMails = "http://hobossa-usayi.44fs.preview.openshiftapps.com";

	/* Account Management EMail */
	@Override
	public void activation( User user)
	{
		try
		{
			final String confUrl = urlForMails + "/#/account-validation?token=" + user.getConfToken();

			String subject = "Hôbossa - Activation de votre compte";
			Email to = new Email( user.getUserInfo().getEmail());

			String html = templates.activationTmpl().replaceAll( "%mailtitle%", "Activation de votre compte")
								   .replaceAll( "%activationlink%", confUrl)
								   .replaceAll( "%homelink%", urlForMails);
		
			Content content = new Content("text/html", html);
			
			Mail mail = new Mail(from, subject, to, content);
			
			sendMail(mail);
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
	}	
	@Override
	public void accountValidated(User user)
	{
		try
		{
			
			String subject = "Hôbossa - Compte Activé !";
			Email to = new Email( user.getUserInfo().getEmail());
			
			String html = templates.activatedAccountTmpl().replaceAll( "%mailtitle%", "Compte Activé !")
								   .replaceAll( "%homelink%", urlForMails);
			
			Content content = new Content("text/html", html);
			
			com.sendgrid.Mail mail = new com.sendgrid.Mail(from, subject, to, content);
			sendMail(mail);
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
	}
	@Override
	public void passwordResetRequest(User user)
	{
		try
		{
			
			String subject = "Hôbossa - Récupération du mot de passe";
			Email to = new Email( user.getUserInfo().getEmail());
			
			final String recupUrl = urlForMails + "/#/password-reset?token=" + user.getConfToken();

			String html = templates.passwordRequestTmpl().replaceAll( "%mailtitle%", "Récupération du mot de passe !")
								   .replaceAll( "%homelink%", urlForMails)
								   .replaceAll( "%link", recupUrl);

			Content content = new Content("text/html", html);
			
			com.sendgrid.Mail mail = new com.sendgrid.Mail(from, subject, to, content);
			sendMail(mail);
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
	}
	@Override
	public void passwordUpdated(User user)
	{
		try
		{
			String subject = "Hôbossa - Mot de passe réinitialisé avec succès";
			Email to = new Email( user.getUserInfo().getEmail());

			String html = templates.passwordUpdatedTmpl().replaceAll( "%mailtitle%", "Mot de passe réinitialisé")
								   .replaceAll( "%homelink%", urlForMails);

			Content content = new Content("text/html", html);
			
			com.sendgrid.Mail mail = new com.sendgrid.Mail(from, subject, to, content);
			sendMail(mail);
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
	}
	/* End Account Management */
	
	/* Approved / validated Entity ( Profile, EShop, Upgrade rquest */
	@Override
	public void approvedEntity(Object entity)
	{
		try
		{
			if( entity.getClass().equals( EShop.class)) {
				EShop eShop = (EShop) entity;
				User manager = pretaDao.loadEShopManager( eShop.getId());

				String subject = "Hôbossa - E-Boutique " + eShop.getName() + " approuvée par l'administration !";
				Email to = new Email( manager.getUserInfo().getEmail());

				String html = templates.approvedEShopTmpl()
									   .replaceAll( "%mailtitle%", "Boutique " + eShop.getName() + " approuvée.")
									   .replaceAll( "%eshopname%", eShop.getName())
									   .replaceAll( "%homelink%", urlForMails);
				
				Content content = new Content("text/html", html);
				
				Mail mail = new Mail(from, subject, to, content);
				
				sendMail(mail);
			}
			
			if( entity.getClass().equals( User.class)) {
				User user = ( User) entity;				

				
				String subject = "Hôbossa - Profil validé par l'administration !";
				Email to = new Email( user.getUserInfo().getEmail());

				String html = templates.approvedProfileTmpl()
									   .replaceAll( "%mailtitle%", "Profil validé par l'administration.")
									   .replaceAll( "%homelink%", urlForMails);
				
				Content content = new Content("text/html", html);
				
				com.sendgrid.Mail mail = new com.sendgrid.Mail(from, subject, to, content);
				sendMail(mail);
			}
			
			if( entity.getClass().equals( UpgradeRequest.class)) {
				UpgradeRequest request = ( UpgradeRequest) entity;

				String html = templates.approvedUpgradeRequestTmpl()
									   .replaceAll( "%mailtitle%", "Migration vers un Compte Gestionnaire d'E-Boutiques.")
									   .replaceAll( "%homelink%", urlForMails);
				
				String subject = "Hôbossa - Félicitations! Vous êtes désormais un Manager";
				Email to = new Email( request.getBuyer().getUserInfo().getEmail());

				Content content = new Content("text/html", html);
				
				com.sendgrid.Mail mail = new com.sendgrid.Mail(from, subject, to, content);
				sendMail(mail);
			}
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
	}
	/* ENd Approved | Validated Entity */
	
	/* ExpiryReminder and Expiry */	
	@Override
	public void expiringEntity(Object entity)
	{
		/* TODO Add precision regarding when it expires
		 */
		try
		{
			if( entity.getClass().equals( AdvOffer.class)) {
				AdvOffer ad = (AdvOffer) entity;
				User manager = pretaDao.loadAdvOfferManager( ad.getId());
				
				
				String subject = "Hôbossa - La ublicité N° " + ad.getRelId() + " de l'article " + ad.getArticle().getName() + " expire bientôt !";
				Email to = new Email( manager.getUserInfo().getEmail());

				String html = templates.expiryReminderAdvOffer()
									   .replaceAll( "%mailtitle%", "La pubicité N° " + ad.getRelId().toString() + " de l'article " + ad.getArticle().getName() + " expire bientôt !")
									   .replaceAll( "%advofferid%", ad.getRelId().toString())
									   .replaceAll( "%articlename%", ad.getArticle().getName())
									   .replaceAll( "%eshopname%", pretaDao.loadArticleEShop( ad.getArticle().getId()).getName())
									   .replaceAll( "%homelink%", urlForMails);
				
				Content content = new Content("text/html", html);
				
				com.sendgrid.Mail mail = new com.sendgrid.Mail(from, subject, to, content);
				sendMail(mail);
			}
			
			if( entity.getClass().equals( ShopSub.class)) {
				ShopSub sub = (ShopSub) entity;
				User manager = pretaDao.loadShopSubManager( sub.getId());
				
				
				String subject = "Hôbossa - L'abonnement N° " + sub.getRelId() + " de votre boutique " + sub.geteShop().getName() + " expire bientôt !";
				Email to = new Email( manager.getUserInfo().getEmail());

				String html = templates.expiryReminderShopSub()
									   .replaceAll( "%mailtitle%", "L'abonnement N° " + sub.getRelId().toString() + " de l'E-Boutique " + sub.geteShop().getName() + " expire bientôt !")
									   .replaceAll( "%shopsubid%", sub.getRelId().toString())
									   .replaceAll( "%eshopname%", sub.geteShop().getName())
									   .replaceAll( "%homelink%", urlForMails);
				
				Content content = new Content("text/html", html);
				
				com.sendgrid.Mail mail = new com.sendgrid.Mail(from, subject, to, content);
				sendMail(mail);
			}
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
	}
	@Override
	public void expiredEntity(Object entity)
	{
		try
		{
			if( entity.getClass().equals( AdvOffer.class)) {
				AdvOffer ad = (AdvOffer) entity;
				User manager = pretaDao.loadAdvOfferManager( ad.getId());
				
				
				String subject = "Hôbossa - La publicté N° " + ad.getRelId() + " de l'article " + ad.getArticle().getName() + " a expiré !";
				Email to = new Email( manager.getUserInfo().getEmail());

				String html = templates.expiredAdvOffer()
									   .replaceAll( "%mailtitle%", "La pubicité N° " + ad.getRelId().toString() + " de l'article " + ad.getArticle().getName() + "  expiré !")
									   .replaceAll( "%advofferid%", ad.getRelId().toString())
									   .replaceAll( "%articlename%", ad.getArticle().getName())
									   .replaceAll( "%eshopname%", pretaDao.loadArticleEShop( ad.getArticle().getId()).getName())
									   .replaceAll( "%homelink%", urlForMails);
				
				Content content = new Content("text/html", html);
				
				com.sendgrid.Mail mail = new com.sendgrid.Mail(from, subject, to, content);
				sendMail(mail);
			}
			
			if( entity.getClass().equals( ShopSub.class)) {
				ShopSub sub = (ShopSub) entity;
				User manager = pretaDao.loadShopSubManager( sub.getId());
				
				
				String subject = "Hôbossa - L'abonnement N° " + sub.getRelId() + " de votre boutique " + sub.geteShop().getName() + " a expiré !";
				Email to = new Email( manager.getUserInfo().getEmail());
				
				String html = templates.expiredShopSubTmpl()
									   .replaceAll( "%mailtitle%", "L'abonnement N° " + sub.getRelId().toString() + " de l'E-Boutique " + sub.geteShop().getName() + " a expiré !")
									   .replaceAll( "%shopsubid%", sub.getRelId().toString())
									   .replaceAll( "%eshopname%", sub.geteShop().getName())
									   .replaceAll( "%homelink%", urlForMails);
				
				Content content = new Content("text/html", html);
				
				com.sendgrid.Mail mail = new com.sendgrid.Mail(from, subject, to, content);
				sendMail(mail);
			}
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
		
	}
	/* End ExpiryReminder and Expiry */
	
	/* ArticleOrder */
	@Override
	public void registeredArticleOrder(ArticleOrder entity)
	{
		try
		{
			String subject = "Hôbossa - Commande #" + entity.getBuyerRelId() + " enregistrée avec succès !";
			Email to = new Email( entity.getUser().getUserInfo().getEmail());

			String html = templates.registeredOrderTmpl()
								   .replaceAll( "%mailtitle%", "Commande N° " + entity.getBuyerRelId() + " enregistrée !")
								   .replaceAll( "%orderid%", entity.getBuyerRelId().toString())
								   .replaceAll( "%homelink%", urlForMails);
			
			Content content = new Content("text/html", html);
			
			com.sendgrid.Mail mail = new com.sendgrid.Mail(from, subject, to, content);
			sendMail(mail);
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
	}
	@Override
	public void deliveringArticleOrder(ArticleOrder entity)
	{
		try
		{
			
			String subject = "Hôbossa - Commande #" + entity.getBuyerRelId() + " en cours de livraison !";
			Email to = new Email( entity.getUser().getUserInfo().getEmail());
			
			String html = templates.registeredOrderTmpl()
								   .replaceAll( "%mailtitle%", "Commande N° " + entity.getBuyerRelId() + " en cours de livraison !")
								   .replaceAll( "%orderid%", entity.getBuyerRelId().toString())
								   .replaceAll( "%homelink%", urlForMails);
			
			Content content = new Content("text/html", html);
			
			com.sendgrid.Mail mail = new com.sendgrid.Mail(from, subject, to, content);
			sendMail(mail);
			
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
	}
	@Override
	public void deliveredArticleOrder(ArticleOrder entity)
	{
		try
		{
			String subject = "Hôbossa - Commande #" + entity.getBuyerRelId() + " reçue par le client !";
			Email to = new Email( pretaDao.loadEShopManager( pretaDao.loadArticleOrderEShop(entity.getId()).getId()).getUserInfo().getEmail());

			String html = templates.registeredOrderTmpl()
								   .replaceAll( "%mailtitle%", "Commande N° " + entity.getBuyerRelId() + " reçue par le client !")
								   .replaceAll( "%orderid%", entity.getBuyerRelId().toString())
								   .replaceAll( "%homelink%", urlForMails)
								   .replaceAll( "%eshopname%", pretaDao.loadArticleOrderEShop(entity.getId()).getName());
			Content content = new Content("text/html", html);
			
			com.sendgrid.Mail mail = new com.sendgrid.Mail(from, subject, to, content);
			sendMail(mail);
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
	}
	/* End ArticleOrder */
	
	/* On Payment Alteration EMail */
	@Override
	public void alteredPayment(Payment payment, Object entity)
	{
		try
		{
			if( entity.getClass().equals( ArticleOrder.class)) {
				ArticleOrder articleOrder = (ArticleOrder) entity;
				
				/* Notify Buyer */
				if( payment.getIsValid()) {
					
					String subject = "Hôbossa - Paiement Commande N° " + articleOrder.getBuyerRelId() + " validé.";
					Email to = new Email( articleOrder.getUser().getUserInfo().getEmail());
					
					String html = templates.validOrderPaymentTmpl()
										   .replaceAll( "%mailtitle%", "Paiement Commande N°" + articleOrder.getBuyerRelId().toString() + " validé !")
										   .replaceAll( "%paymentid%", payment.getId().toString())
										   .replaceAll( "%orderid%", articleOrder.getBuyerRelId().toString())
										   .replaceAll( "%homelink%", urlForMails);
					
					Content content = new Content("text/html", html);
					
					com.sendgrid.Mail mail = new com.sendgrid.Mail(from, subject, to, content);
					sendMail(mail);
				}
				else 
				{
					String subject = "Hôbossa - Paiement Commande N° " + articleOrder.getBuyerRelId() + " rejeté.";
					Email to = new Email( articleOrder.getUser().getUserInfo().getEmail());

					String html = templates.invalidOrderPaymentTmpl()
										   .replaceAll( "%homelink%", urlForMails)
										   .replaceAll( "%mailtitle%", "Paiement Commande N°" + articleOrder.getBuyerRelId().toString() + " rejeté !")
										   .replaceAll( "%paymentid%", payment.getId().toString())
										   .replaceAll( "%orderid%", articleOrder.getBuyerRelId().toString());
					
					Content content = new Content("text/html", html);
					
					com.sendgrid.Mail mail = new com.sendgrid.Mail(from, subject, to, content);
					sendMail(mail);
				}
				
				EShop eShop = pretaDao.loadArticleOrderEShop( articleOrder.getId());
				User manager = pretaDao.loadEShopManager( eShop.getId());
				
				if( payment.getIsValid()) {
					/* Notify Manager */
					
					String subject = "Hôbossa - Paiement Commande N° " + articleOrder.geteShopRelId() + "de l' E-Boutique " + eShop.getName() +" validé.";
					Email to = new Email( manager.getUserInfo().getEmail());

					String html = templates.orderStartDeliveryTmpl()
										   .replaceAll( "%homelink%", urlForMails)
										   .replaceAll( "%mailtitle%", "Paiement Commande N°" + articleOrder.geteShopRelId().toString() + " validé !")
										   .replaceAll( "%paymentid%", payment.getId().toString())
										   .replaceAll( "%orderid%", articleOrder.geteShopRelId().toString())
										   .replaceAll( "%eshopname%", eShop.getName());

					Content content = new Content("text/html", html);
					
					com.sendgrid.Mail mail = new com.sendgrid.Mail(from, subject, to, content);
					sendMail(mail);
				}
			}
			
			/* TODO Invalid paiement mail */
			if( entity.getClass().equals( ShopSub.class)) {
				ShopSub shopSub = ( ShopSub) entity;
				EShop eShop = shopSub.geteShop();
				
				User manager = pretaDao.loadShopSubManager(shopSub.getId());

				String subject = "Hôbossa - Paiement Abonnement de votre boutique: " + eShop.getName() + " validé !";
				Email to = new Email( manager.getUserInfo().getEmail());
				String html ="";
				
				if( payment.getIsValid())
				{
					html = templates.validShopSubPaymentTmpl()
								   .replaceAll( "%homelink%", urlForMails)
								   .replaceAll( "%mailtitle%", "Abonnement N° " + shopSub.getRelId().toString() + " de l'E-Boutique %eshopname% validé !" )
								   .replaceAll( "%paymentid%", payment.getId().toString())
								   .replaceAll( "%orderid%", shopSub.getRelId().toString())
								   .replaceAll( "%eshopname%", eShop.getName());
				}
				else
				{
					html = templates.invalidShopSubPaymentTmpl()
							   .replaceAll( "%homelink%", urlForMails)
							   .replaceAll( "%mailtitle%", "Abonnement N° " + shopSub.getRelId().toString() + " de l'E-Boutique %eshopname% rejeté !")
							   .replaceAll( "%paymentid%", payment.getId().toString())
							   .replaceAll( "%orderid%", shopSub.getRelId().toString())
							   .replaceAll( "%eshopname%", eShop.getName());
				}
				

				Content content = new Content("text/html", html);
				
				com.sendgrid.Mail mail = new com.sendgrid.Mail(from, subject, to, content);
				sendMail(mail);
			}
			
			if( entity.getClass().equals( AdvOffer.class)) {
				AdvOffer ad = ( AdvOffer) entity;
				
				User manager = pretaDao.loadAdvOfferManager( ad.getId());
				EShop eShop = pretaDao.loadArticleEShop( ad.getArticle().getId());
				
				String subject = "Hôbossa - Paiement Publicité de l'article: " + ad.getArticle().getName() + " validé !";
				Email to = new Email( manager.getUserInfo().getEmail());

				String html = "";
				
				if( payment.getIsValid())
				{
					html = templates.validAdvOfferPaymentTmpl()
							   .replaceAll( "%homelink%", urlForMails)
							   .replaceAll( "%mailtitle%", "Publicité N° " + ad.getRelId().toString() + " de l'article %articlename% validé !")
								   .replaceAll( "%paymentid%", payment.getId().toString())
								   .replaceAll( "%orderid%", ad.getRelId().toString())
								   .replaceAll( "%eshopname%", eShop.getName())
								   .replaceAll( "%articlename%", ad.getArticle().getName());
				}
				else
				{
					html = templates.invalidAdvOfferPaymentTmpl()
							   .replaceAll( "%homelink%", urlForMails)
							   .replaceAll( "%mailtitle%", "Publicité N° " + ad.getRelId().toString() + " de l'article %articlename% rejeté !")
							   .replaceAll( "%paymentid%", payment.getId().toString())
							   .replaceAll( "%orderid%", ad.getRelId().toString())
							   .replaceAll( "%eshopname%", eShop.getName())
							   .replaceAll( "%articlename%", ad.getArticle().getName());
				}

				Content content = new Content("text/html", html);
				
				com.sendgrid.Mail mail = new com.sendgrid.Mail(from, subject, to, content);
				sendMail(mail);
			}
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/* Tools & Refactor */
	private void sendMail( com.sendgrid.Mail mail) {
		Request r = new Request();
	
		try
		{
			r.method = Method.POST;
			r.endpoint = "mail/send";
			r.body = mail.build();
			Response resp = sendGrid.api(r);
			logger.info( "Response Code => " + resp.statusCode);
			logger.info( "Body => " + resp.body);
		}
		catch( Exception e)
		{
			/* TODO Redirect to Admin if Error */
			e.printStackTrace();
		}
	}
	
	/* DEBUG & Error Handling */
	@Override
	public void sendErrorReport( Exception e)
	{
		/* TODO More detailled maybe */
		try
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			
			
			String subject = "Hôbossa - Erreur Détectée !";
			Email to = new Email( errorEMail);
			
			String html = sw.toString();

			Content content = new Content("text/html", html);
			
			com.sendgrid.Mail mail = new com.sendgrid.Mail(from, subject, to, content);
			sendMail(mail);
		}
		catch( Exception ex)
		{
			ex.printStackTrace();
		}
	}
}