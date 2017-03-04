package org.usayi.preta.buzlayer;

import org.usayi.preta.entities.*;
import org.usayi.preta.entities.json.PagedListJSON;

public interface IPublicRESTAPI extends ISharedRESTAPI
{
	/* Articles */
	public PagedListJSON searchArticle( final String name, final Integer page, final Integer pageSize, final Float minPrice, final Float maxPrice);
	public PagedListJSON listArticlesByCategory( final Long categoryId, final Integer page, final Integer pageSize);
	public PagedListJSON loadSimilarArticles( final Long articleId, final Integer page, final Integer pageSize);
	public PagedListJSON loadLatestArticles( final Integer page, final Integer pageSize);
	public PagedListJSON loadTopArticles( final Integer page, final Integer pageSize);
	public PagedListJSON loadBestSellers( final Integer page, final Integer pageSize);
	public PagedListJSON loadDiscountedArticles( final Integer page, final Integer pageSize);
		/* Advertised Articles */
		public PagedListJSON loadAdvertisedArticles( final Integer page, final Integer pageSize, final Integer code);
	/* Strict : Only if EShop is Subbed */
	public PagedListJSON searchArticleStrict( final String name, final Integer page, final Integer pageSize, final Float minPrice, final Float maxPrice);
	public PagedListJSON loadCategoryArticlesStrict( final Long categoryId, final Integer page, final Integer pageSize);
	public PagedListJSON loadLatestArticlesStrict( final Integer page, final Integer pageSize);
	public PagedListJSON loadTopArticlesStrict( final Integer page, final Integer pageSize);
	public PagedListJSON loadBestSellersStrict( final Integer page, final Integer pageSize);
	public PagedListJSON loadDiscountedArticlesStrict( final Integer page, final Integer pageSize);
	/* End Articles */
	
	/* ArticleOrders */
	public PagedListJSON loadBuyerArticleOrders( final Long userId, final Integer page, final Integer pageSize, final OrderStatus orderStatus, final boolean orderByIdAsc);
	public EShop loadArticleOrderEShop( final Long id);
		/* Payment */
		public Long addPaymentToArticleOrder( final Long id, final Long eAccountId, final Long adminEAccountId, final Payment entity);
	/* End ArticleOrders */
	
	/* Ordered Articles */
	public void addOrderedArticle( OrderedArticle entity);
	/* End Ordered Articles */
	
	/* SubOffer */
	public PagedListJSON listSubOffer( final Integer page, final Integer pageSize);
	public SubOffer getSubOffer( final Long id);
	/* End SubOffers */
	
	/* Shop Sub */
	public Long addShopSub( final ShopSub entity);
	public void editShopSub( final ShopSub entity);
	/* End Shop Sub */
	
	/* OrderedArticles */
	public PagedListJSON listOrderedArticleToRate( final Long userId, final Integer page, final Integer pageSize);
	public Long getNumberOfArticlesToRate( final Long userId);
	/* End OrderedArticles */
	
	/* OrderedArticleFeatures */
	public Long addOrderedArticleFeature( final OrderedArticleFeature entity);
	/* End OrderedArticlesFeatures */
	
	/* Cart Item */
	public PagedListJSON loadCartItems( final Long userId, final Integer page, final Integer pageSize);
	public void removeCartItemFromUserCart( final Long userId, final Long cartItemId);
	public void emptyUserCart( final Long userId);
	public CartItem loadCartItem( final Long id);
	public void updateCartItem( final CartItem entity);
	/* End Cart Item */
	
	/* EShop */
		/* Articles */
		public PagedListJSON loadRecentArticlesByEShop( final Long eShopId, final Integer page, final Integer pageSize);
		public PagedListJSON loadBestSellerArticlesByEShop( final Long eShopId, final Integer page, final Integer pageSize);
		public PagedListJSON loadTopArticlesByEShop( final Long eShopId, final Integer page, final Integer pageSize);
		public PagedListJSON loadDiscountedarticlesByEShop( final Long id, final Integer page, final Integer pageSize);
	/* End EShop */
	
	/* User */
		/* ArticleOrder */
		public Long addArticleOrderToUser( final Long id, final ArticleOrder entity);
		/* CartItem */
		public Long addCartItemToUserCart( final Long id, final CartItem entity);
		/* Articles */
		public PagedListJSON loadLastVisitedArticles( final Long id, final Integer page, final Integer pageSize);
		public void addVisitedArticle( final Long userId, final Long articleId);
		/* EShop */
		public PagedListJSON loadFavoritesEShops( final Long userId, final Integer page, final Integer pageSize);
		public void addEShopToUserFav( final Long userId, final Long eShopId);
		public void removeEShopFromUserFav( final Long userId, final Long eShopId);
	/* End User */
	
	/* EAccount */
	public boolean isEAccountNumberRegistered( final String account);
	public boolean isEAccountNumberTaken( final String account);
	/* End EAccount */
	
	/* Upgrade Request */
	public Long addUpgradeRequest( final Long userId, final UpgradeRequest entity);
	public boolean hasAlreadyRequested( final Long userId);
	/* End Upgrade Request */
	
	/* Home Picture */
	public PagedListJSON loadDisplayedHomePictures( final Integer page, final Integer pageSize);
	/* End Home Picture */
}
