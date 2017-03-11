package org.usayi.preta.buzlayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.usayi.preta.dao.IPretaDAO;
import org.usayi.preta.entities.*;
import org.usayi.preta.entities.json.PagedListJSON;

public abstract class SharedRESTAPI implements ISharedRESTAPI
{
	/* DAO */
	@Autowired
	private IPretaDAO pretaDao;
	public IPretaDAO getPretaDao()
	{
		return pretaDao;
	}
	public void setPretaDao(IPretaDAO pretaDao)
	{
		this.pretaDao = pretaDao;
	}
	/* End DAO */
	
	/* Articles */
		@Override
		public Article loadArticle( Long id)
		{
			return pretaDao.loadArticle(id);
		}
		/* ArticleFeatures */
		@Override	
		public PagedListJSON listArticleFeaturesByArticle( Long articleId, Integer page, Integer pageSize)
		{
			return pretaDao.listArticleFeaturesByArticle(articleId, page, pageSize);
		}
		/* Categories */
		@Override
		public PagedListJSON listCategoriesByArticle(Long articleId, Integer page, Integer pageSize)
		{
			return pretaDao.listCategoriesByArticle(articleId, page, pageSize);
		}
		/* Pictures */
		@Override
		public PagedListJSON listPicturesByArticle(Long articleId, Integer page, Integer pageSize)
		{
			return pretaDao.listPicturesByArticle(articleId, page, pageSize);
		}
		@Override
		public Picture getArticleDefaultPicture(Long articleId)
		{
			return pretaDao.getArticleDefaultPicture(articleId);
		}
		/* EShop */
		@Override
		public EShop getArticleEShop(Long articleId)
		{
			return pretaDao.loadArticleEShop( articleId);
		}
		/* Stats */
		@Override
		public Float getMinPrice()
		{
			return pretaDao.getMinPrice();
		}
		@Override
		public Float getMaxPrice()
		{
			return pretaDao.getMaxPrice();
		}
		/* AdvOffers */
		@Override
		public PagedListJSON loadArticleAdvOffers( Long id, Integer page, Integer pageSize, GenericStatus status, boolean orderByIdAsc)
		{
			return pretaDao.loadArticleAdvOffers(id, page, pageSize, status, orderByIdAsc);
		}
		/* Category */
		@Override
		public PagedListJSON loadArticleCategories( Long id, Integer page, Integer pageSize)
		{
			return pretaDao.loadArticleCategories(id, page, pageSize);
		}
	/* End Articles */

	/* ArticleOrder */
		@Override
		public ArticleOrder loadArticleOrder( Long id)
		{
			return pretaDao.loadArticleOrder(id);
		}
		@Override
		public void updateArticleOrder( ArticleOrder entity)
		{
			pretaDao.updateArticleOrder(entity);
		}
		/* EShop */
		public EShop loadArticleOrderEShop( Long id)
		{
			return pretaDao.loadArticleOrderEShop(id);
		}
		/* Payments */
		@Override
		public PagedListJSON loadArticleOrderPayments( Long id, Integer page, Integer pageSize)
		{
			return pretaDao.loadArticleOrderPayments( id, page, pageSize);
		}
	/* End ArticleOrders */
		
	/* EMoneyProvider */
	@Override
	public PagedListJSON loadEMoneyProviders( Integer page, Integer pageSize)
	{
		return pretaDao.loadEMoneyProviders( page, pageSize);
	}
	@Override
	public EMoneyProvider loadEMoneyProvider(Long id)
	{
		return pretaDao.getEMoneyProvider( id);
	}
	@Override
	public EMoneyProvider loadEMoneyProviderByName( String name)
	{
		return pretaDao.getEMoneyProviderByName( name);
	}
	/* End EMoney Provider */

	/* Category */
	@Override
	public Category loadCategory( Long id)
	{
		return pretaDao.loadCategory(id);
	}
	@Override
	public Category loadCategoryRoot( Long id)
	{
		Category entity = pretaDao.loadCategory(id);
		
		if( entity == null)
			return null;
		
		return entity.getRoot();
	}
	@Override
	public PagedListJSON loadCategories( Integer page, Integer pageSize)
	{
		return pretaDao.loadCategories(page, pageSize);
	}
	@Override
	public PagedListJSON loadCategories( Integer level, Integer page, Integer pageSize)
	{
		return pretaDao.loadCategories(level, page, pageSize);
	}
	/* End Category */

	/* SubOffer */
	@Override
	public SubOffer loadSubOffer( Long id)
	{
		return pretaDao.loadSubOffer(id);
	}
	/* End SubOffer */
	
	/* ShopSubs */
	@Override
	public ShopSub loadShopSub(Long id)
	{
		return pretaDao.loadShopSub(id);
	}
	/* End ShopSubs */
	
	/* User */
	@Override
	public User loadUser(Long id)
	{
		return pretaDao.loadUser(id);
	}
	@Override
	public UserInfo loadUserInfo(Long id)
	{
		return pretaDao.getUserInfo(id);
	}
	@Override
	public User loadUserByEMail( String email)
	{
		return pretaDao.loadUserByEMail( email);
	}
	@Override
	public User loadUserByToken( String token)
	{
		return pretaDao.loadUserByToken( token);
	}
	@Override
	public User loadUserByUsername( String username)
	{
		return pretaDao.loadUserByUsername(username);
	}
	@Override
	public void updateUser(User entity)
	{
		pretaDao.updateUser(entity);
	}
	@Override
	public void updateUserInfo(UserInfo entity)
	{
		pretaDao.updateUserInfo(entity);
	}
	@Override
	public Long addUserInfo( UserInfo entity)
	{
		return pretaDao.addUserInfo(entity);
	}
	@Override
	public Long addUser( User entity)
	{
		return pretaDao.addUser(entity);
	}
		/* EAccount */
		@Override
		public Long addEAccountToUserInfo( Long id, Long empId, EAccount entity)
		{
			return pretaDao.addEAccountToUserInfo(id, empId, entity);
		}
		@Override
		public Long addExEAccountToUserInfo( Long id, Long eAccountId)
		{
			return pretaDao.addExEAccountToUserInfo(id, eAccountId);
		}
		/* Manager - EShop */
		@Override
		public PagedListJSON loadManagedEShops( Long userId, Integer page, Integer pageSize)
		{
			return pretaDao.listManagedEShops(userId, page, pageSize);
		}
		/* Manager - AdvOffers */
		@Override
		public PagedListJSON loadManagerAdvOffers(Long id, Integer page, Integer pageSize, GenericStatus status, boolean orderByIdAsc)
		{
			return pretaDao.loadManagerAdvOffers(id, page, pageSize, status, orderByIdAsc);
		}
		/* Notification */
		@Override
		public PagedListJSON loadNotifications( Long userId, int status, Integer page, Integer pageSize, boolean  orderByIdAsc, Integer restrict)
		{
			return pretaDao.loadUserNotifications(userId, status, page, pageSize, orderByIdAsc, restrict);
		}
		/* Payment */
		@Override
		public PagedListJSON loadUserPayments( Long userId, Integer page, Integer pageSize, int status, boolean orderByIdAsc)
		{
			return pretaDao.loadUserPayments(userId, page, pageSize, status, orderByIdAsc);
		}
	/* End User */
	
	/* Payments */
	@Override
	public Payment loadPayment( Long id)
	{
		return pretaDao.loadPayment(id);
	}
	@Override
	public void updatePayment( Payment entity)
	{
		pretaDao.updatePayment(entity);
	}
	@Override
	public void addPayment(Payment entity, Long eAccountId, Long adminEAccountId)
	{
		pretaDao.addPayment( entity, eAccountId, adminEAccountId);
	}
	/* End Payments */
	
	/* EShop */
	@Override
	public Long addEShop( EShop entity)
	{
		return pretaDao.addEshop(entity);
	}
	@Override
	public EShop loadEShop( Long id)
	{
		return pretaDao.loadEShop(id);
	}
	@Override
	public EShop loadEShop( String name)
	{
		return pretaDao.loadEShop(name);
	}
		/* Articles */
		@Override
		public PagedListJSON loadEShopArticles(Long id, Integer page, Integer pageSize)
		{
			return pretaDao.loadEShopArticles(id, page, pageSize);
		}
		/* ArticleOrder */
		@Override
		public PagedListJSON loadEShopArticleOrders( Long id, Integer page, Integer pageSize, OrderStatus status, boolean orderByIdAsc)
		{
			return pretaDao.loadEShopArticleOrders(id, page, pageSize, status, orderByIdAsc);
		}
	/* End EShop */
	
	/* Feature */
	@Override
	public Feature getFeatureByLabel(String label)
	{
		return pretaDao.getFeatureByLabel(label);
	}
	/* End Feature */
	
	/* Feature Value */
	@Override
	public FeatureValue getFeatureValue( String value) 
	{
		return pretaDao.getFeatureValue( value);
	}
	/* End Feature Value */
	
	/* Article Feature */
	@Override
	public ArticleFeature getArticleFeature( Long id)
	{
		return pretaDao.getArticleFeature( id);
	}
	/* End Article Feature */
	
	/* EAccount */
	@Override
	public EAccount loadEAccount( Long id)
	{
		return pretaDao.loadEAccount(id);
	}
	@Override
	public EAccount loadEAccountByNumber( String number)
	{
		return pretaDao.loadEAccountByNumber(number);
	}
	@Override
	public boolean isEAccountNumberRegistered( String account)
	{
		return pretaDao.isEAccountNumberRegistered(account);
	}
	@Override
	public void updateEAccount( EAccount entity)
	{
		pretaDao.updateEAccount(entity);
	}
		/* Account for payment */
		@Override
		public PagedListJSON loadAdminEAccountsForPayments( Integer page, Integer pageSize)
		{
			return pretaDao.loadAdminEAccountsForPayments(page, pageSize);
		}
	/* End EAccount */

	/* Upgrade Request */
	public UpgradeRequest loadUpgradeRequest( Long id)
	{
		return pretaDao.loadUpgradeRequest(id);
	}
	/* End Upgrade Request */
	
	/* Payment Type */
	@Override
	public PaymentType loadPaymentType( Long id)
	{
		return pretaDao.loadPaymentType(id);
	}
	/* End Payment Type */
	
	/* AdvOffer */
	@Override
	public AdvOffer loadAdvOffer( Long id)
	{
		return pretaDao.loadAdvOffer(id);
	}
	@Override
	public void updateAdvOffer( AdvOffer entity)
	{
		pretaDao.updateAdvOffer(entity);
	}
	@Override
	public PagedListJSON loadAdvOffers(Integer page, Integer pageSize, GenericStatus status, boolean orderByIdAsc)
	{
		return pretaDao.loadAdvOffers(page, pageSize, status, orderByIdAsc);
	}
		/* Payments */
		@Override
		public PagedListJSON loadAdvOfferPayments( Long id, Integer page, Integer pageSize, boolean orderByIdAsc)
		{
			return pretaDao.loadAdvOfferPayments(id, page, pageSize, orderByIdAsc);
		}
	/* End AdvOffer */
	
	/* Adv Option */
	@Override
	public PagedListJSON loadAdvOptions( Integer page, Integer pageSize)
	{
		return pretaDao.loadAdvOptions( page, pageSize);
	}
	public AdvOption loadAdvOption( Long id)
	{
		return pretaDao.loadAdvOption(id);
	}
	/* End AdvOption */
	
	/* Role */
	@Override
	public Role loadRole( String name)
	{
		return pretaDao.loadRole(name);
	}
	/* End Role */
	
	/* Notifications */
	@Override
	public Notification loadNotification( Long id)
	{
		return pretaDao.loadNotification(id);
	}
	@Override
	public void updateNotification( Notification entity)
	{
		pretaDao.updateNotification(entity);
	}
	/* End Notifications */
	
	/* HomePicture */
	@Override
	public Slide loadSlide( Long id)
	{
		return pretaDao.loadSlide(id);
	}
	@Override
	public PagedListJSON loadDisplayedSlides( Integer page, Integer pageSize)
	{
		return pretaDao.loadDisplayedSlides( page, pageSize);
	}
	/* End HomePicture */
}
