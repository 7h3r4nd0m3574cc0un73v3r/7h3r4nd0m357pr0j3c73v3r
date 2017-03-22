package org.usayi.preta.buzlayer;

import org.usayi.preta.entities.*;
import org.usayi.preta.entities.json.PagedListJSON;

public interface ISharedRESTAPI
{
	/* Articles */
		public Article loadArticle( final Long id);
		/* ArticleFeatures */
		public PagedListJSON listArticleFeaturesByArticle( final Long articleId, final Integer page, final Integer pageSize);
		/* Categories */
		public PagedListJSON listCategoriesByArticle( final Long articleId, final Integer page, final Integer pageSize);
		/* Pictures */
		public PagedListJSON listPicturesByArticle( final Long articleId, final Integer page, final Integer pageSize);
		public Picture getArticleDefaultPicture( final Long articleId);
		/* EShop */
		public EShop getArticleEShop( final Long articleId);
		/* Stats */
		public Float getMinPrice();
		public Float getMaxPrice();
		/* AdvOffers */
		public PagedListJSON loadArticleAdvOffers( final Long id, final Integer page, final Integer pageSize, final GenericStatus status, final boolean orderByIdAsc);
		/* Category */
		public PagedListJSON loadArticleCategories( final Long id, final Integer page, final Integer pageSize);
	/* End Articles */
	
	/* ArticleOrder */
		public ArticleOrder loadArticleOrder( final Long id);
		public void updateArticleOrder( final ArticleOrder entity);
		/* EShop */
		public EShop loadArticleOrderEShop( final Long id);
		/* Payments */
		public PagedListJSON loadArticleOrderPayments( final Long id, final Integer page, final Integer pageSize);
		/* AAdmin */
		public User loadArticleOrderAdmin( final Long id);
	/* End ArticleOrders */
		
	/* EMoneyProviders */
	public EMoneyProvider loadEMoneyProvider( final Long id);
	public EMoneyProvider loadEMoneyProviderByName( final String name);
	public PagedListJSON loadEMoneyProviders( final Integer page, final Integer pageSize);
	/* End EMoney Providers */
	
	/* SubOffer */
	public SubOffer loadSubOffer( final Long id);
	/* End SubOffer */
	
	/* ShopSubs */
	public ShopSub loadShopSub( final Long id);
	/* End ShppSubs */
	
	/* User & UserInfo */
	public User loadUser( final Long id);
	public UserInfo loadUserInfo( final Long id);
	public User loadUserByEMail( final String email);
	public User loadUserByToken( final String token);
	public User loadUserByUsername( final String username);
	public Long addUserInfo( final UserInfo entity);
	public Long addUser( final User entity);
	public void updateUser( final User entity);
	public void updateUserInfo( final UserInfo entity);
		/* EAccount */
		public Long addEAccountToUserInfo( final Long id, final Long empId, final EAccount entity);
		public Long addExEAccountToUserInfo( final Long id, final Long eAccountId);
		/* Manager - EShop */
		public PagedListJSON loadManagedEShops( final Long userId, final Integer page, final Integer pageSize);
		/* Manager - AdvOffer */
		public PagedListJSON loadManagerAdvOffers( final Long id, final Integer page, final Integer pageSize, final GenericStatus status, final boolean orderByIdAsc);
		/* Notification */
		public PagedListJSON loadNotifications( final Long userId, final int status, final Integer page, final Integer pageSize, final boolean orderByIdAsc, final Integer restrict);
		/* Payment */
		public PagedListJSON loadUserPayments( final Long userId,  final Integer page, final Integer pageSize, final int status, final boolean orderByIdAsc);
	/* End User & UserInfo */
	
	/* Payments */
	public Payment loadPayment( final Long id);
	public void updatePayment( final Payment entity);
	public void addPayment( final Payment entity, final Long eAccountId, final Long adminEAccountId);
	/* End Payments */
	
	/* EShops */
	public Long addEShop( final EShop entity);
	public EShop loadEShop( final Long id);
	public EShop loadEShop( final String name);
		/* Articles */
		public PagedListJSON loadEShopArticles( final Long id, final Integer page, final Integer pageSize);
		/* ArticleOrder */
		public PagedListJSON loadEShopArticleOrders( final Long id, final Integer page, final Integer pageSize, final OrderStatus status, final boolean orderByIdAsc);
	/* End EShops */
		
	/* Feature */
	public Feature getFeatureByLabel( final String label);
	/* End Feature */
	
	/* Feature Value */
	public FeatureValue getFeatureValue( final String value);
	/* End Feature Value */
	
	/* Article Feature */
	public ArticleFeature getArticleFeature( final Long id);
	/* End Article Feature */
	
	/* AdvOffer */
	public AdvOffer loadAdvOffer( final Long id);
	public void updateAdvOffer( final AdvOffer entity);
	public PagedListJSON loadAdvOffers( final Integer page, final Integer pageSize, final GenericStatus status, final boolean orderByIdAsc);
		/* Payments */
		public PagedListJSON loadAdvOfferPayments( final Long id, final Integer page, final Integer pageSize, final boolean orderByIdAsc);
	/* End AdvOffer */
	
	/* EAccount */
	public EAccount loadEAccount( final Long id);
	public EAccount loadEAccountByNumber( final String number);
	public boolean isEAccountNumberRegistered( final String account);
	public void updateEAccount( final EAccount entity);
		/* Account for payment */
		public PagedListJSON loadAdminEAccountsForPayments( final Integer page, final Integer pageSize);
	/* End EAccount */
	
	/* Upgrade Request */
	public UpgradeRequest loadUpgradeRequest( final Long id);
	/* End Upgrade Request */
	
	/* Payment Type */
	public PaymentType loadPaymentType( final Long id);
	/* End Payment Type */
	
	/* HomePicture */
	public Slide loadSlide( final Long id);
	public PagedListJSON loadDisplayedSlides( final Integer page, final Integer pageSize);
	/* End HomePicture */
	
	/* Role */
	public Role loadRole( final String name);
	/* End Role */
	
	/* AdvOptions */
	public AdvOption loadAdvOption( final Long id);
	public PagedListJSON loadAdvOptions( final Integer page, final Integer pageSize);
	/* End AdvOptions */
	
	/* Category */
	public Category loadCategory( final Long id);
	public Category loadCategoryRoot( final Long id);
	public PagedListJSON loadCategories( final Integer page, final Integer pageSize);
	public PagedListJSON loadCategories( final Integer level, final Integer page, final Integer pageSize);
	/* End Category */
	
	/* Notifications */
	public Notification loadNotification( final Long id);
	public void updateNotification( final Notification entity);
	/* End Notifications */
	
	/* Expense */
		/* ArticleOrders */
		public PagedListJSON loadExpenseArticleOrders( final Long id, final Integer page, final Integer pageSize);
		/* Manager */
		public User loadExpenseManager( final Long id);
	/* End Expense */
	
}
