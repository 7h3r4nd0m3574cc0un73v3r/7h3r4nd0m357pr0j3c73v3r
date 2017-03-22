package org.usayi.preta.buzlayer;

import org.usayi.preta.entities.AdvOffer;
import org.usayi.preta.entities.Article;
import org.usayi.preta.entities.ArticleFeature;
import org.usayi.preta.entities.EShop;
import org.usayi.preta.entities.Expense;
import org.usayi.preta.entities.Feature;
import org.usayi.preta.entities.FeatureValue;
import org.usayi.preta.entities.OrderStatus;
import org.usayi.preta.entities.Picture;
import org.usayi.preta.entities.ShopSub;
import org.usayi.preta.entities.json.PagedListJSON;

public interface IManagerRESTAPI extends ISharedRESTAPI
{	
	/* EShops */
	public Long addEShop( final EShop entity);
	public void updateEShop( final EShop entity);
		/* Shop Subs */
		public Long addShopSubToEShop( final Long id, final ShopSub entity);
		public PagedListJSON loadShopSubsByEShop( final Long id, final Integer page, final Integer pageSize, final boolean orderByIdAsc);
		public ShopSub getCurrentShopSub( final Long id);
	/* End EShops */
	
	/* Shop Sub */
	public Long addShopSub( final ShopSub entity);
	public void updateShopSub( final ShopSub entity);
	/* End SHop Sub */
	
	/* Manager */
		/* EShops */
		public Long addEShopToUser( final Long userId, final EShop entity);
		/* ArticleOrders */
		public PagedListJSON loadArticlesOrderByManagerAndByStatus( final Long id, final Integer page, final Integer pageSize, final boolean orderByIdAs, final OrderStatus orderStatus);
		public PagedListJSON loadManagerArticleOrders( final Long id, final Integer page, final Integer pageSize, final OrderStatus status, final boolean orderByIdAsc);
	/* End %anager */
		
	/* Feature */
	public Long addFeature( final Feature entity);
		/* Feature Values */
		public Long addFeatureValue( final FeatureValue entity);
	/* End Feature */
	
	/* Article Feature */
	public Long addArticleFeature( final ArticleFeature entity);
	/* End Article Feature */
	
	/* Picture */
	public Long addPicture ( final Picture entity);
	/* End Picture */
	
	/* Article */
	public Long addArticleToEShop( final Long id, final Article entity);
	public void updateArticle( final Article entity);
	/* End Article */
	
	/* AdvOffer */
	public Long addAdvOffer( final AdvOffer entity);
	/* End AdvOffer */
	
	/* Expense */
	public Expense loadExpense( final Long id);
	public PagedListJSON loadManagerExpenses( final Long id, final Integer page, final Integer pageSize, final boolean orderByIdAsc);
	/* End Expense */
}
