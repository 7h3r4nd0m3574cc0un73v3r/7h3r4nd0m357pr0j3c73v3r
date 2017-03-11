package org.usayi.preta.dao;

import java.sql.Timestamp;
import java.util.List;

import org.usayi.preta.entities.AdvOffer;
import org.usayi.preta.entities.AdvOption;
import org.usayi.preta.entities.Article;
import org.usayi.preta.entities.ArticleFeature;
import org.usayi.preta.entities.ArticleOrder;
import org.usayi.preta.entities.CartItem;
import org.usayi.preta.entities.Category;
import org.usayi.preta.entities.EAccount;
import org.usayi.preta.entities.EMoneyProvider;
import org.usayi.preta.entities.EShop;
import org.usayi.preta.entities.Feature;
import org.usayi.preta.entities.FeatureValue;
import org.usayi.preta.entities.GenericStatus;
import org.usayi.preta.entities.Slide;
import org.usayi.preta.entities.Notification;
import org.usayi.preta.entities.OrderStatus;
import org.usayi.preta.entities.OrderedArticle;
import org.usayi.preta.entities.OrderedArticleFeature;
import org.usayi.preta.entities.Payment;
import org.usayi.preta.entities.PaymentType;
import org.usayi.preta.entities.Picture;
import org.usayi.preta.entities.Role;
import org.usayi.preta.entities.ShopStatus;
import org.usayi.preta.entities.ShopSub;
import org.usayi.preta.entities.SubOffer;
import org.usayi.preta.entities.UpgradeRequest;
import org.usayi.preta.entities.User;
import org.usayi.preta.entities.UserInfo;
import org.usayi.preta.entities.json.PagedListJSON;

public interface IPretaDAO
{
	/* EMP */
	public PagedListJSON loadEMoneyProviders( final Integer page, final Integer pageSize);
	public Long addEMoneyProvider( final EMoneyProvider entity);
	public void updateEMoneyProvider( final EMoneyProvider entity);
	public void deleteEMoneyProvider( final Long id);
	public EMoneyProvider getEMoneyProvider( final Long id);
	public EMoneyProvider getEMoneyProviderByName( final String name);
	public List<?> getEMoneyProviderRevs( final Long id);
	/* End EMP */
	
	/* ShopStatus */
	public PagedListJSON loadShopStatuses( final Integer page, final Integer pageSize);
	public Long addShopStatus( final ShopStatus entity);
	public void updateShopStatus( final ShopStatus entity);
	public void deleteShopStatus( final Long id);
	public ShopStatus loadShopStatus( final Long id);
	public ShopStatus loadShopStatus( final String title);
	public boolean isShopStatusRegistered( final String title);
	/* End ShopStatus */
	
	/* SubOffer */
	public PagedListJSON loadSubOffers( final Integer page, final Integer pageSize);
	public Long addSubOffer( final SubOffer entity);
	public void updateSubOffer( final SubOffer entity);
	public void deleteSubOffer( final Long id);
	public SubOffer loadSubOffer( final Long id);
	public SubOffer loadSubOffer( final String title);
	public boolean isSubOfferRegistered( final String title);
	/* ENd SubOffer */
	
	/* ShopSub */
	public PagedListJSON loadShopSubs( final Integer page, final Integer pageSize);
	public PagedListJSON listPendingShopSub( final Integer page, final Integer pageSize);
	public Long addShopSub( final ShopSub entity);
	public void updateShopSub( final ShopSub entity);
	public ShopSub loadShopSub( final Long id);
	public PagedListJSON loadShopSubByStatus( final GenericStatus status, final Integer page, final Integer pageSize);
		/* Manager */
		public User loadShopSubManager( final Long id);
	/* End ShopSub */
	
	/* EShop */
	public PagedListJSON listEShop( final Integer page, final Integer pageSize);
	public Long addEshop( final EShop entity);
	public void updateEShop( final EShop entity);
	public EShop loadEShop( final Long id);
	public EShop loadEShop( final String name);
		/* Articles */
		public Long addArticleToEshop( final Long eShopId, final Article entity);
		public PagedListJSON loadEShopArticles( final Long id, final Integer page, final Integer pageSize);
		public PagedListJSON loadDiscountedArticlesByEShop( final Long id, final Integer page, final Integer pageSize);
		public PagedListJSON listRecentArticlesByEShop( final Long eShopId, final Integer page, final Integer pageSize);
		public PagedListJSON listTopArticlesByEShop( final Long eShopId, final Integer page, final Integer pageSize);
		public PagedListJSON listBestSellerArticlesByEShop( final Long eShopId, final Integer page, final Integer pageSize);
		/* Manager */
		public User loadEShopManager( final Long id);
		/* Shop Subs */
		public Long addShopSubToEShop( final Long id, final ShopSub entity);
		public PagedListJSON loadEShopShopSubs( final Long eShopId, final Integer page, final Integer pageSize, final boolean orderByIdAsc);
		public ShopSub getCurrentShopSub( final Long id);
		/* ArticleOrder */
		public PagedListJSON loadEShopArticleOrders( final Long id, final Integer page, final Integer pageSize, final OrderStatus status, final boolean orderByIdAsc);
	/* End EShop */
	
	/* Roles */
	public List<Role> listRole();
	public Role loadRole( final String name);
	public Long addRole( final Role entity);
	
	/* User */
	public PagedListJSON loadBuyers( final Integer page, final Integer pageSize, final boolean orderByIdAsc);
	public List<User> listManager();
	public List<User> loadAdmins();
	public User loadUser( final Long id);
	public UserInfo getUserInfo( final Long id);
	public Long addUserInfo( final UserInfo entity);
	public Long addUser( final User entity);
	public void updateUser( final User entity);
	public void updateUserInfo( final UserInfo entity);
	public User loadUserByToken( final String token);
	public User loadUserByUsername( final String username);
	public User loadUserByEMail( final String email);
		/* Cart */
		public PagedListJSON loadCartItems( final Long userId, final Integer page, final Integer pageSize);
		public Long addCartItemToUserCart( final Long di, final CartItem entity);
		public void removeCartItemFromUserCart( final Long userId, final Long cartItemId);
		public void emptyUserCart( final Long userId);
		/* Managed EShops */
		public PagedListJSON listManagedEShops( final Long userId, final Integer page, final Integer pageSize);
		public Long addEShopToUser( final Long userId, final EShop entity);
		/* Articles Orders As Manager */
		public PagedListJSON loadArticleOrderByManagerAndByStatus( final Long userId, final Integer page, final Integer pageSize, final boolean orderByIdAsc,
																   final OrderStatus orderStatus);
		/* EAccount */
		public Long addEAccountToUserInfo( final Long id, final Long empId, final EAccount entity);
		public Long addExEAccountToUserInfo( final Long id, final Long eAccountId);
		/* Article Orders as Buyer */
		public Long addArticleOrderToUser( final Long id, final ArticleOrder entity);
		public PagedListJSON loadBuyerArticleOrders( final Long userId, final Integer page, final Integer pageSize, final OrderStatus orderStatus, boolean orderByIdAsc);
		/* Article */
		public PagedListJSON loadLastVisitedArticles( final Long userId, final Integer page, final Integer pageSize);
		public void addVisitedArticle( final Long userId, final Long articleId);
		/* EShop */
		public PagedListJSON loadFavoritesEShops( final Long userId, final Integer page, final Integer pageSize);
		public void addEShopAsFavToUser( final Long userId, final Long eShopId);
		public void removeEShopFromUserFav( final Long userId, final Long eShopId);
		/* Buyers Profile Validation */
		public PagedListJSON loadBuyerPendingProfileValidation( final Integer page, final Integer pageSize);
		/* Admin - Supervised Managers */
		public PagedListJSON loadSupervisedManagers( final Long id, final Integer page, final Integer pageSize);
		public void addSupervisedManagerToAdmin( final Long adminId, final Long managerId);
		public void removeSupervisedManagerFromAdmin( final Long adminId, final Long managerId);
		/* AdvOffer */
		public PagedListJSON loadManagerAdvOffers( final Long id, final Integer page, final Integer pageSize, final GenericStatus status, final boolean orderByIdAsc);
		/* Advertised Articles */
		public PagedListJSON loadAdvertisedArticles( final Integer page, final Integer pageSize, final Integer code);
		/* Manager - Supervisor */
		public User loadManagerSupervisingAdmin( final Long id);
		/* Manager - EShops */
		public PagedListJSON loadManagerEShops( final Long id, final Integer page, final Integer pageSize);
		/* Payment */
		public PagedListJSON loadUserPayments( final Long userId, final Integer page, final Integer pageSize, final int status, final boolean orderByIdAsc);
		/* Admin Payments */
		public PagedListJSON loadAdminPayments( final Long userId, final Integer page, final Integer pageSize, final int status, final boolean orderByIdAsc);
	/* End User */
	
	/* AdvOption */
	public PagedListJSON loadAdvOptions( final Integer page, final Integer pageSize);
	public Long addAdvOption( final AdvOption entity);
	public void editAdvOption( final AdvOption entity);
	public void deleteAdvOption( final Long id);
	public AdvOption loadAdvOption( final Long id);
	public AdvOption loadAdvOption( final String title);
	public boolean isAdvOptionRegistered( final String title);
	/* End AdvOption */
	
	/* AdvOffer */
	public PagedListJSON loadAdvOffers( final Integer page, final Integer pageSize, final GenericStatus status, final boolean orderByIdAsc);
	public Long addAdvOffer( final AdvOffer entity);
	public void updateAdvOffer( final AdvOffer entity);
	public AdvOffer loadAdvOffer( final Long id);
		/* Payments */
		public PagedListJSON loadAdvOfferPayments( final Long id, final Integer page, final Integer pageSize, final boolean orderByIdAsc);
		/* EShop */
		public EShop loadAdvOfferEshop( final Long id);
		/* Manager */
		public User loadAdvOfferManager( final Long id);
	/* Strict */
	public PagedListJSON loadAdvOffersStrict( final Integer page, final Integer pageSize, final GenericStatus status, final boolean orderByIdAsc);
	/* End AdvOffer */
	
	/* Articles */
	public PagedListJSON listSimilarArticle( final Long articleId, final Integer page, final Integer pageSize);
	public Article loadArticle( final Long id);
	public Long addArticle( final Article entity);
	public void updateArticle( final Article entity);
	public void deleteArticle( final Long id);
	public Picture getDefaultPicture( final Long id);
	public Picture getPicture( final Long id);
	public Integer getArticleRating( final Long id);
	public Long getArticleSaleNumber( final Long id);
	public PagedListJSON searchArticle( final String name, final Integer page, final Integer pageSize, final Float minPrice, final Float maxPrice);
	public PagedListJSON loadArticlesByCategory( final Long categoryId, final Integer page, final Integer pageSize);
	public PagedListJSON loadLatestArticles( final Integer page, final Integer pageSize);
	public PagedListJSON loadTopArticles( final Integer page, final Integer pageSize);
	public PagedListJSON loadBestSellers( final Integer page, final Integer pageSize);
	public PagedListJSON loadDiscountedArticles( final Integer page, final Integer pageSize);
		/* ArticleFeatures */
		public PagedListJSON listArticleFeaturesByArticle( final Long articleId, final Integer page, final Integer pageSize);
		/* Categories */
		public PagedListJSON listCategoriesByArticle( final Long articleId, final Integer page, final Integer pageSize);
		/* Pictures */
		public PagedListJSON listPicturesByArticle( final Long articleId, final Integer page, final Integer pageSize);
		public Picture getArticleDefaultPicture( final Long articleId);
		/* EShop */
		public EShop loadArticleEShop( final Long id);
		/* Stats */
		public Float getMinPrice();
		public Float getMaxPrice();
		/* AdvOffers */
		public PagedListJSON loadArticleAdvOffers( final Long id, final Integer page, final Integer pageSize, final GenericStatus status, final boolean orderByIdAsc);
		/* Category */
		public PagedListJSON loadArticleCategories( final Long id, final Integer page, final Integer pageSize);
	/* Strict : Only if EShop is Subbed */
	public PagedListJSON searchArticleStrict( final String name, final Integer page, final Integer pageSize, final Float minPrice, final Float maxPrice);
	public PagedListJSON loadCategoryArticlesStrict( final Long categoryId, final Integer page, final Integer pageSize);
	public PagedListJSON loadLatestArticlesStrict( final Integer page, final Integer pageSize);
	public PagedListJSON loadTopArticlesStrict( final Integer page, final Integer pageSize);
	public PagedListJSON loadBestSellersStrict( final Integer page, final Integer pageSize);
	public PagedListJSON loadDiscountedArticlesStrict( final Integer page, final Integer pageSize);
	/* End Articles */
	
	/* Cart Item */
	public CartItem loadCartItem( final Long id);
	public void addCartItem( final CartItem entity);
	public void updateCartItem( final CartItem entity);
	public void removeCartItem( final Long id);
	/* End Cart Item */
	
	/* Category */
	public void deleteCategory( final Long id);
	public Category loadCategory( final Long id);
	public Long addCategory( final Category entity);
	public void updateCategory( final Category entity);
	public PagedListJSON loadCategories( final Integer page, final Integer pageSize);
	public PagedListJSON loadCategories( final Integer level, final Integer page, final Integer pageSize );
	/* End Category */
	
	/* Feature */
	public Feature getFeature( final Long id);
	public Long addFeature( final Feature entity);
	public Feature getFeatureByLabel( final String label);
	public Long addArticleFeature( final ArticleFeature entity);
	/* End Feature */
	
	/* Feature Value */
	public Long addFeatureValue( final FeatureValue entity);
	public void removeFeatureValue( final Long id);
	public FeatureValue getFeatureValue( final Long id);
	public FeatureValue getFeatureValue( final String value);
	/* End Feature Value */
	
	/* ArticleFeature */
	public ArticleFeature getArticleFeature( final Long id);
	/* End ArticleFeature */
	
	/* Picture */
	public Long addPicture( final Picture entity);
	public void deletePicture( final Long id);
	/* End Picture */
	
	/* Payment */
	public PagedListJSON loadPayments( final Integer page, final Integer pageSize, final int status, final boolean orderByIdAsc);
	public Payment loadPayment( final Long id);
	public void addPayment( final Payment entity, final Long eAccount, final Long adminEAccountId);
	public void updatePayment( final Payment entity);
		/* Shop Sub */
		public ShopSub loadPaymentShopSub( final Long id);
		/* AdvOffer */
		public AdvOffer loadPaymentAdvOffer( final Long id);
		/* ArtileOrders */
		public PagedListJSON loadPaymentArticleOrders( final Long id, final Integer page, final Integer pageSize);
	/* End Payments */
	
	/* ArticleOrder */
	public PagedListJSON loadArticleOrders( final Integer page, final Integer pageSize, final OrderStatus status, final boolean orderByIdAsc);
	public ArticleOrder loadArticleOrder( final Long id);
	public void addArticleOrder( final ArticleOrder entity);
	public void updateArticleOrder( final ArticleOrder entity);
	public Timestamp getArticleOrderRegDate( final Long id);
	public List<ArticleOrder> listArticleOrderByEShop( final Long eShopId);
	public List<ArticleOrder> listArticleOrderByStatus( final OrderStatus status);
	public User getArticleOrderUser( final Long id);
		/* EShop */
		public EShop loadArticleOrderEShop( final Long id);
		/* Payments */
		public Long addPaymentToArticleOrder( final Long id, final Long eAccountId, final Long adminEAccountId, final Payment entity);
		public PagedListJSON loadArticleOrderPayments( final Long id, final Integer page, final Integer pageSize);
	/* End ArticleOrder */
	
	//OrderedArticle
	public OrderedArticle getOrderedArticle( final Long id);
	public void addOrderedArticle( final OrderedArticle entity);
	public List<OrderedArticle> listOrderedArticleByOrder( final Long id);
	public void editOrderedArticle( final OrderedArticle entity);
	public PagedListJSON listOrderedArticleToRate( final Long userId, final Integer page, final Integer pageSize);
	public Long getNumberOfArticlesToRate( final Long userId);
	
	//OrderedArticleFeature
	public Long addOrderedArticleFeature( final OrderedArticleFeature entity);
	public OrderedArticleFeature getOrderedArticleFeature( final Long id);
	
	/* EAccounts */
	public EAccount loadEAccount( final Long id);
	public void addEAccount( final EAccount entity);
	public void updateEAccount( final EAccount entity);
	public EAccount loadEAccountByNumber( final String number);
	public boolean isEAccountNumberRegistered( final String account);
	public boolean isEAccountNumberTaken( final String account);
		/* Account for payment */
		public PagedListJSON loadAdminEAccountsForPayments( final Integer page, final Integer pageSize);
	/* End EAccounts */
	
	/* Upgrade Request */
	public UpgradeRequest loadUpgradeRequest( final Long id);
	public void updateUpgradeRequest( final UpgradeRequest entity);
	public Long addUpgradeRequest( final Long userId, final UpgradeRequest entity);
	public boolean hasAlreadyRequested( final Long userId);
	public PagedListJSON loadUpgradeRequests( final Integer page, final Integer pageSize, final boolean orderByIdAsc, final int status);
	/* End Upgrade Request */
	
	/* Payment Type */
	public PaymentType loadPaymentType( final Long id);
	public Long addPaymentType( final PaymentType entity);
	public void updatePaymentType( final PaymentType entity);
	
	/* Slide */
	public Long addSlide( final Slide entity);
	public Slide loadSlide( final Long id);
	public void updateSlide( final Slide entity);
	public PagedListJSON loadSlides( final Integer page, final Integer pageSize, final boolean orderByDiplayOrderAsc);
	public PagedListJSON loadDisplayedSlides( final Integer page, final Integer pageSize);
	public Slide loadSlide( final Integer dispOrder);
	/* End Slide */
	
	/* Notification */
	public Notification loadNotification( final Long id);
	public void updateNotification( final Notification entity);
	public Long countUserNotifications( final Long id, final int status);
	public Long addNotification( final Notification entity, final Long userId);
	public PagedListJSON loadUserNotifications( final Long id, final int status, final Integer page, final Integer pageSize, final boolean orderByIdAsc, final Integer restrict);
	/* End Notification */
	
	/* Generic */
	public Timestamp getRegDate( Class<?> entityClass, final Long id);
}
