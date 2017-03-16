package org.usayi.preta.buzlayer;

import org.usayi.preta.dao.IPretaDAO;
import org.usayi.preta.entities.ArticleOrder;
import org.usayi.preta.entities.CartItem;
import org.usayi.preta.entities.EShop;
import org.usayi.preta.entities.OrderStatus;
import org.usayi.preta.entities.OrderedArticle;
import org.usayi.preta.entities.OrderedArticleFeature;
import org.usayi.preta.entities.Payment;
import org.usayi.preta.entities.ShopSub;
import org.usayi.preta.entities.SubOffer;
import org.usayi.preta.entities.UpgradeRequest;
import org.usayi.preta.entities.VisitedArticle;
import org.usayi.preta.entities.json.PagedListJSON;

public class PublicRESTAPI extends SharedRESTAPI implements IPublicRESTAPI
{
	/* DAO */
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
	public PagedListJSON searchArticle( String name, Integer page, Integer pageSize, Float minPrice, Float maxPrice)
	{
		return pretaDao.searchArticle( name, page, pageSize, minPrice, maxPrice);
	}
	@Override
	public PagedListJSON listArticlesByCategory(Long categoryId, Integer page, Integer pageSize)
	{
		return pretaDao.loadArticlesByCategory( categoryId, page, pageSize);
	}
	@Override
	public PagedListJSON loadSimilarArticles( Long articleId, Integer page, Integer pageSize)
	{
		return pretaDao.listSimilarArticle( articleId, page, pageSize);
	}
	@Override
	public PagedListJSON loadLatestArticles(Integer page, Integer pageSize)
	{
		return pretaDao.loadLatestArticles(page, pageSize);
	}
	@Override
	public PagedListJSON loadTopArticles(Integer page, Integer pageSize)
	{
		return pretaDao.loadTopArticles(page, pageSize);
	}
	@Override
	public PagedListJSON loadBestSellers(Integer page, Integer pageSize)
	{
		return pretaDao.loadBestSellers(page, pageSize);
	}
	@Override
	public PagedListJSON loadDiscountedArticles( Integer page, Integer pageSize)
	{
		return pretaDao.loadDiscountedArticles(page, pageSize);
	}
		/* Advertised Articles */
		@Override
		public PagedListJSON loadAdvertisedArticles( Integer page, Integer pageSize, Integer code)
		{
			return pretaDao.loadAdvertisedArticles(page, pageSize, code);
		}

	/* Strict : Only if EShop is Subbed */
	@Override
	public PagedListJSON searchArticleStrict( String name, Integer page, Integer pageSize, Float minPrice, Float maxPrice)
	{
		return pretaDao.searchArticle( name, page, pageSize, minPrice, maxPrice);
	}
	@Override
	public PagedListJSON loadCategoryArticlesStrict( Long categoryId, Integer page, Integer pageSize)
	{
		return pretaDao.loadCategoryArticlesStrict(categoryId, page, pageSize);
	}
	@Override
	public PagedListJSON loadLatestArticlesStrict( Integer page, Integer pageSize)
	{
		return pretaDao.loadLatestArticlesStrict(page, pageSize);
	}
	@Override
	public PagedListJSON loadTopArticlesStrict( Integer page, Integer pageSize)
	{
		return pretaDao.loadTopArticlesStrict(page, pageSize);
	}
	@Override
	public PagedListJSON loadBestSellersStrict( Integer page, Integer pageSize)
	{
		return pretaDao.loadBestSellersStrict(page, pageSize);
	}
	@Override
	public PagedListJSON loadDiscountedArticlesStrict( Integer page, Integer pageSize)
	{
		return pretaDao.loadDiscountedArticlesStrict(page, pageSize);
	}
	/* End Articles */
	
	/* ArticleOrders */
	@Override
	public PagedListJSON loadBuyerArticleOrders(Long userId, Integer page, Integer pageSize, OrderStatus orderStatus, boolean orderByIdAsc)
	{
		return pretaDao.loadBuyerArticleOrders( userId, page, pageSize, orderStatus, orderByIdAsc);
	}
	@Override
	public EShop loadArticleOrderEShop(Long id)
	{
		return pretaDao.loadArticleOrderEShop( id);
	}
		/* Payments */
		@Override
		public Long addPaymentToArticleOrder( Long id, Long eAccountId, Long adminEAccountId, Payment entity)
		{
			return pretaDao.addPaymentToArticleOrder(id, eAccountId, adminEAccountId, entity);
		}
	/* End ArticleOrders */
		
	/* Ordered Articles */
	@Override
	public void addOrderedArticle( OrderedArticle entity)
	{
		pretaDao.addOrderedArticle(entity);
	}
	/* End Ordered Articles */
	

	/* OrderedArticleFeatures */
	@Override
	public Long addOrderedArticleFeature( OrderedArticleFeature entity)
	{
		return pretaDao.addOrderedArticleFeature(entity);
	}
	/* End OrderedArticlesFeatures */
	
	/* SubOffers */
	@Override
	public PagedListJSON listSubOffer( Integer page, Integer pageSize)
	{
		return pretaDao.loadSubOffers( page, pageSize);
	}
	@Override
	public SubOffer getSubOffer(Long id)
	{
		return pretaDao.loadSubOffer( id);
	}
	/* End SubOffers */
	
	/* Shop Sub */
	@Override
	public Long addShopSub(ShopSub entity)
	{
		return pretaDao.addShopSub(entity);
	}
	@Override
	public void editShopSub(ShopSub entity)
	{
		pretaDao.updateShopSub(entity);
	}
	/* End Shop Sub */
	
	/* Rating */
	@Override
	public PagedListJSON listOrderedArticleToRate(Long userId, Integer page, Integer pageSize)
	{
		return pretaDao.listOrderedArticleToRate(userId, page, pageSize);
	}
	@Override
	public Long getNumberOfArticlesToRate(Long userId)
	{
		return pretaDao.getNumberOfArticlesToRate(userId);
	}
	/* End Rating */
	
	/* CartItem */
	@Override
	public PagedListJSON loadCartItems(Long userId, Integer page, Integer pageSize)
	{
		return pretaDao.loadCartItems(userId, page, pageSize);
	}
	@Override
	public void removeCartItemFromUserCart(Long userId, Long cartItemId)
	{
		pretaDao.removeCartItemFromUserCart(userId, cartItemId);
	}
	@Override
	public void emptyUserCart(Long userId)
	{
		pretaDao.emptyUserCart(userId);
	}
	@Override
	public CartItem loadCartItem( Long id)
	{
		return pretaDao.loadCartItem(id);
	}
	@Override
	public void updateCartItem( CartItem entity)
	{
		pretaDao.updateCartItem( entity);
	}
	/* End Cart Item */

	/* EShop */
		/* Articles */
	@Override
		public PagedListJSON loadRecentArticlesByEShop(Long eShopId, Integer page, Integer pageSize)
		{
			return pretaDao.listRecentArticlesByEShop(eShopId, page, pageSize);
		}
		@Override
		public PagedListJSON loadBestSellerArticlesByEShop(Long eShopId, Integer page, Integer pageSize)
		{
			return pretaDao.listBestSellerArticlesByEShop(eShopId, page, pageSize);
		}
		@Override
		public PagedListJSON loadTopArticlesByEShop(Long eShopId, Integer page, Integer pageSize)
	{
		return pretaDao.listTopArticlesByEShop(eShopId, page, pageSize);
	}
		@Override
		public PagedListJSON loadDiscountedarticlesByEShop( Long id, Integer page, Integer pageSize)
		{
			return pretaDao.loadDiscountedArticlesByEShop(id, page, pageSize);
		}
	/* End EShop */
	
	/* User */
		/* ArticleOrder */
		@Override
		public Long addArticleOrderToUser( Long id, ArticleOrder entity)
		{
			return pretaDao.addArticleOrderToUser(id, entity);
		}
		@Override
		public Long addCartItemToUserCart( Long id, CartItem entity)
		{
			return pretaDao.addCartItemToUserCart( id, entity);
		}
		/* Article */
		@Override
		public PagedListJSON loadLastVisitedArticles( Long id, Integer page, Integer pageSize)
		{
			return pretaDao.loadLastVisitedArticles( id, page, pageSize);
		}
		@Override
		public void addVisitedArticle( Long userId, Long articleId)
		{
			pretaDao.addVisitedArticle(userId, articleId);
		}
		/* EShop */
		@Override
		public PagedListJSON loadFavoritesEShops( Long userId, Integer page, Integer pageSize)
		{
			return pretaDao.loadFavoritesEShops(userId, page, pageSize);
		}
		@Override
		public void addEShopToUserFav( Long userId, Long eShopId)
		{
			pretaDao.addEShopAsFavToUser(userId, eShopId);
		}
		@Override
		public void removeEShopFromUserFav( Long userId, Long eShopId)
		{
			pretaDao.removeEShopFromUserFav(userId, eShopId);
		}
		/* Visited Articles */
		public PagedListJSON loadUserVisitedArticles( Long id, Integer page, Integer pageSize, boolean orderByIdAsc)
		{
			return pretaDao.loadUserVisitedArticles(id, page, pageSize, orderByIdAsc);
		}
	/* End User */
	
	/* EAccount */
	@Override
	public boolean isEAccountNumberRegistered( String account)
	{
		return pretaDao.isEAccountNumberRegistered(account);
	}
	@Override
	public boolean isEAccountNumberTaken( String account)
	{
		return pretaDao.isEAccountNumberTaken(account);
	}
	/* End EAccount */
		
	/* Upgrade Request */
	public Long addUpgradeRequest( Long userId, UpgradeRequest entity)
	{
		return pretaDao.addUpgradeRequest(userId, entity);
	}
	public boolean hasAlreadyRequested( Long userId)
	{
		return pretaDao.hasAlreadyRequested( userId);
	}
	/* End Upgrade Requests */
	
	/* HomePicture */
	@Override
	public PagedListJSON loadDisplayedHomePictures( Integer page, Integer pageSize)
	{
		return pretaDao.loadDisplayedSlides(page, pageSize);
	}
	/* End HomePicture */
	
	/* Visited Articles */
	public Long addVisitedArticle( VisitedArticle entity)
	{
		return pretaDao.addVisitedArticle(entity);
	}
	/* ENd Visited Articles */
}