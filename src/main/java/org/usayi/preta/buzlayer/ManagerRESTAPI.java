package org.usayi.preta.buzlayer;

import java.util.List;

import org.usayi.preta.dao.IPretaDAO;
import org.usayi.preta.entities.AdvOffer;
import org.usayi.preta.entities.Article;
import org.usayi.preta.entities.ArticleFeature;
import org.usayi.preta.entities.ArticleOrder;
import org.usayi.preta.entities.EShop;
import org.usayi.preta.entities.Feature;
import org.usayi.preta.entities.FeatureValue;
import org.usayi.preta.entities.OrderStatus;
import org.usayi.preta.entities.Picture;
import org.usayi.preta.entities.ShopSub;
import org.usayi.preta.entities.json.PagedListJSON;

public class ManagerRESTAPI extends SharedRESTAPI implements IManagerRESTAPI
{
	//DAO Config
	private IPretaDAO pretaDao;
	
	public void setPretaDao( IPretaDAO pretaDao)
	{
		this.pretaDao = pretaDao;
	}
	public IPretaDAO getPretaDao()
	{
		return pretaDao;
	}
	
	/* Manager */
		/* EShops */
		@Override
		public Long addEShopToUser( Long userId, EShop entity)
		{
			return pretaDao.addEShopToUser(userId, entity);
		}
		/* ArticleOrders */
		@SuppressWarnings("unchecked")
		@Override
		public PagedListJSON loadArticlesOrderByManagerAndByStatus( Long id, Integer page, Integer pageSize, boolean orderByIdAsc,
																	OrderStatus orderStatus)
		{
			PagedListJSON result = pretaDao.loadArticleOrderByManagerAndByStatus( id, page, pageSize, orderByIdAsc, orderStatus);
			
			
			for( ArticleOrder entity : (List<ArticleOrder>) result.getEntities()) {
				entity.setRegDate( pretaDao.getRegDate( ArticleOrder.class, entity.getId()));
			}
			
			return result;
		}
	/* End Manager */
		
	/* EShops */
	@Override
	public Long addEShop(EShop entity)
	{
		return pretaDao.addEshop(entity);
	}
	@Override
	public void updateEShop(EShop entity)
	{
		pretaDao.updateEShop(entity);
	}
		/* ShopSub */
		@Override
		public Long addShopSubToEShop( Long id, ShopSub entity)
		{
			return pretaDao.addShopSubToEShop( id, entity);
		}
		@Override
		public PagedListJSON loadShopSubsByEShop(Long id, Integer page, Integer pageSize, boolean orderByIdAsc)
		{
			return pretaDao.loadEShopShopSubs( id, page, pageSize, orderByIdAsc);
		}
		@Override
		public ShopSub getCurrentShopSub( Long id)
		{
			return pretaDao.getCurrentShopSub(id);
		}
	/* End EShops */
	
	/* Shop Subs */
	@Override
	public Long addShopSub(ShopSub entity)
	{
		return pretaDao.addShopSub(entity);
	}
	@Override
	public void updateShopSub(ShopSub entity)
	{
		pretaDao.updateShopSub(entity);
	}
	/* End Shop Subs */
	
	/* Feature */
	@Override
	public Long addFeature(Feature entity)
	{
		return pretaDao.addFeature(entity);
	}
		/* Feature Value */
		@Override
		public Long addFeatureValue(FeatureValue entity)
		{
			return pretaDao.addFeatureValue( entity);
		}
	/* End Feature */
	
	/* Article Feature */
	@Override
	public Long addArticleFeature(ArticleFeature entity)
	{
		return pretaDao.addArticleFeature(entity);
	}
	/* End Article Feature */
	
	/* Pictures */
	@Override
	public Long addPicture(Picture entity)
	{
		return pretaDao.addPicture(entity);
	}
	/* End Pictures */
	
	/* Articles */
	@Override
	public Long addArticleToEShop(Long id, Article entity)
	{
		return pretaDao.addArticleToEshop( id, entity);
	}
	@Override
	public void updateArticle(Article entity)
	{
		pretaDao.updateArticle( entity);
	}
	/* End Articles */
	
	/* AdvOffer */
	@Override
	public Long addAdvOffer( AdvOffer entity)
	{
		return pretaDao.addAdvOffer(entity);
	}
	/* End Advoffer */
}
