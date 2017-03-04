package org.usayi.preta.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

@Service
public interface EMailTemplateService
{
	public final static String templatesPath = "/random/preta/templates/";
	public final static String baseTmplPath = templatesPath + "base-tmpl.html";
	
	/* Account Management */
	public final static String activationContentPath       = templatesPath + "activation-content.html";
	public final static String activatedAccountContentPath = templatesPath + "account-activated-content.html";
	public final static String passwordRequestContentPath  = templatesPath + "pw-req-content.html";
	public final static String passwordUpdatedContentPath  = templatesPath + "pw-updated-content.html";
	
	public String activationTmpl();
	public String activatedAccountTmpl() throws IOException;
	public String passwordRequestTmpl() throws IOException;
	public String passwordUpdatedTmpl() throws IOException;
	/* End Account Management */
	
	/* ArticleOrder */
	public final static String registeredOrderContentPath  = templatesPath + "registered-order-content.html";
	public final static String deliveredOrderContentPath   = templatesPath + "delivered-order-content.html";
	public final static String deliveringOrderContentPath  = templatesPath + "delivering-order-content.html";
	
	public String deliveredOrderTmpl() throws IOException;
	public String registeredOrderTmpl() throws IOException;
	public String deliveringOrderTmpl() throws IOException;
	/* End ArticleOrder */
	
	/* Approved */
	public final static String approvedEShopContentPath = templatesPath + "approved-e-shop-content.html";
	public final static String approvedProfileContentPath = templatesPath + "approved-profile-content.html";
	public final static String approvedUpgradeRequestContentPath = templatesPath + "approved-upgrade-request-content.html";
	
	public String approvedEShopTmpl() throws IOException;
	public String approvedProfileTmpl() throws IOException;
	public String approvedUpgradeRequestTmpl() throws IOException;
	/* End Approved */
	
	/* Expiry */
	public final static String expiredShopSubContentPath = templatesPath + "expired-shopsub-content.html";
	public final static String expiredAdvOfferContentPath = templatesPath + "expired-advoffer-content.html";
	public final static String expiryReminderShopSubContentPath = templatesPath + "exp-rem-shopsub-content.html";
	public final static String expiryReminderAdvOfferContentPath = templatesPath + "exp-rem-advoffer-content.html";

	public String expiredShopSubTmpl() throws IOException;
	public String expiredAdvOffer() throws IOException;
	public String expiryReminderShopSub() throws IOException;
	public String expiryReminderAdvOffer() throws IOException;
	/* End Expiry */
	
	/* Altered Payments */
	public final static String validOrderPaymentContentPath = templatesPath + "order-payment-ok-content.html";
	public final static String invalidOrderPaymentContentPath = templatesPath + "order-payment-nok-content.html";
	public final static String validShopSubPaymentContentPath = templatesPath + "shopsub-payment-ok-content.html";
	public final static String invalidShopSubPaymentContentPath = templatesPath + "shopsub-payment-nok-content.html";
	public final static String validAdvOfferPaymentContentPath = templatesPath + "advoffer-payment-ok-content.html";
	public final static String invalidAdvOfferPaymentContentPath = templatesPath + "advoffer-payment-nok-content.html";
	public final static String orderStartDeliveryContentPath = templatesPath + "order-start-delivery-content.html";
	
	public String orderStartDeliveryTmpl() throws IOException;
	public String validOrderPaymentTmpl() throws IOException;
	public String invalidOrderPaymentTmpl() throws IOException;
	public String validShopSubPaymentTmpl() throws IOException;
	public String invalidShopSubPaymentTmpl() throws IOException;
	public String validAdvOfferPaymentTmpl() throws IOException;
	public String invalidAdvOfferPaymentTmpl() throws IOException;
	/* End Altered Paymets */
}
