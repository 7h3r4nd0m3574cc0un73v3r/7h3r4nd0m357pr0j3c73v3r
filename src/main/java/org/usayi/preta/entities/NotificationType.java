package org.usayi.preta.entities;

public enum NotificationType
{
	/* Shared */
	WELCOME,
	APPROVED, /* EShop( entAbsId, entRelId) or Profile, Upgrade Request */
	ALTERED_PAYMENT, /* Infos: PaymentId, PaymentRelId, AbsoluteId, BuyerRelId, ManagerRelId, valid */
	
	/* Buyer */
	APPROVED_UPGRADE_REQUEST,
	DELIVERING_ORDER, /* Infos: AbsolutId, BuyerRelId */
	PENDING_RATING,
	
	/* Manager */
	DELIVERED_ORDER,
	EXPIRED, /* Info: Type: ShopSub || AdvOffer */
	EXPIRING, /* Info: Type: ShopSub || AdvOffer */
	ARTICLE_THRESHOLD, /* Infos: EShopId */
	
	/* Admin */
	APPROVAL_READY, /* EShop || Profile || UpgradeRequest */
	CONFIRMATION_READY, /* ArticleOrder ||  ShopSub || Advertisment */
	NEW_PAYMENT /* ArticleOrder || ShopSub | Advertsment */
}
