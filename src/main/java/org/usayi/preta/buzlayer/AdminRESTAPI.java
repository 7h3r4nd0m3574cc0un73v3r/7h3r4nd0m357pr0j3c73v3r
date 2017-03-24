package org.usayi.preta.buzlayer;

import java.util.ArrayList;
import java.util.List;

import org.usayi.preta.dao.IPretaDAO;
import org.usayi.preta.entities.*;
import org.usayi.preta.entities.json.PagedListJSON;

public class AdminRESTAPI extends SharedRESTAPI implements IAdminRESTAPI
{
	/* DAO Config */
	private IPretaDAO pretaDao;
	
	public void setPretaDao( IPretaDAO pretaDao)
	{
		this.pretaDao = pretaDao;
	}
	public IPretaDAO getPretaDao()
	{
		return pretaDao;
	}
	/* End DAO Config */
	
	/* AdvOption */
	@Override
	public AdvOption loadAdvOption(Long id)
	{
		return pretaDao.loadAdvOption( id);
	}
	@Override
	public AdvOption loadAdvOption( String title)
	{
		return pretaDao.loadAdvOption(title);
	}
	@Override
	public AdvOption getAdvOptionByTitle( String title)
	{
		return pretaDao.loadAdvOption( title);
	}
	@Override
	public Long addAdvOption(AdvOption entity)
	{
		return pretaDao.addAdvOption(entity);
	}
	@Override
	public void updateAdvOption(AdvOption entity)
	{
		pretaDao.editAdvOption(entity);
	}
	@Override
	public void deleteAdvOption(Long id)
	{
		pretaDao.deleteAdvOption(id);
	}
	@Override
	public boolean isAdvOptionRegistered( String title)
	{
		return pretaDao.isAdvOptionRegistered(title);
	}
	/* End AdvOption */
	
	/* EMoney Provider */
	@Override
	public PagedListJSON loadEMoneyProviders( Integer page, Integer pageSize)
	{
		return pretaDao.loadEMoneyProviders( page, pageSize);
	}
	@Override
	public Long addEMoneyProvider(EMoneyProvider entity)
	{
		return pretaDao.addEMoneyProvider( entity);
	}
	@Override
	public void updateEMoneyProvider(EMoneyProvider entity)
	{
		pretaDao.updateEMoneyProvider( entity);
	}
	@Override
	public void deleteEMoneyProvider(Long id)
	{
		pretaDao.deleteEMoneyProvider( id);
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
	@Override
	public List<?> getEMoneyProviderRevs(Long id)
	{
		return pretaDao.getEMoneyProviderRevs(id);
	}
	/* End EMoney Provider */
	
	/* Shop Status */
	@Override
	public PagedListJSON listShopStatus( Integer page, Integer pageSize)
	{
		return pretaDao.loadShopStatuses( page, pageSize);
	}
	@Override
	public ShopStatus loadShopStatus(Long id)
	{
		return pretaDao.loadShopStatus( id);
	}
	@Override
	public ShopStatus loadShopStatus( String title)
	{
		return pretaDao.loadShopStatus(title);
	}
	@Override
	public Long addShopStatus(ShopStatus entity)
	{	
		return pretaDao.addShopStatus( entity);
	}
	@Override
	public void updateShopStatus(ShopStatus entity)
	{
		pretaDao.updateShopStatus( entity);
	}
	@Override
	public void deleteShopStatus(Long id)
	{
		pretaDao.deleteShopStatus( id);
	}
	@Override
	public boolean isShopStatusRegistered( String title)
	{
		return pretaDao.isShopStatusRegistered(title);
	}
	/* End Shop Status */
	
	/* SubOffers */
	@Override
	public PagedListJSON listSubOffer( Integer page, Integer pageSize)
	{
		return pretaDao.loadSubOffers( page, pageSize);
	}
	@Override
	public SubOffer loadSubOffer(Long id)
	{
		return pretaDao.loadSubOffer( id);
	}
	@Override
	public SubOffer loadSubOffer( String title)
	{
		return pretaDao.loadSubOffer(title);
	}
	@Override
	public Long addSubOffer(SubOffer entity)
	{	
		return pretaDao.addSubOffer( entity);
	}
	@Override
	public void updateSubOffer(SubOffer entity)
	{
		pretaDao.updateSubOffer( entity);
	}
	@Override
	public void deleteSubOffer(Long id)
	{
		pretaDao.deleteSubOffer( id);
	}
	@Override
	public boolean isSubOfferRegistered( String title)
	{
		return pretaDao.isSubOfferRegistered(title);
	}
	/* End SubOffers */

	/* ArticleOrder */
	@Override
	public PagedListJSON loadArticleOrders( Integer page, Integer pageSize, OrderStatus status, boolean orderByIdAsc)
	{
		return pretaDao.loadArticleOrders(page, pageSize, status, orderByIdAsc);
	}
	/* End Article Order */
		
	/* EShops */	
	@Override
	public PagedListJSON listEShops( Integer page, Integer pageSize)
	{
		return pretaDao.listEShop( page, pageSize);
	}
	@Override
	public PagedListJSON listEShopByUser(Long id, Integer page, Integer pageSize)
	{
		return pretaDao.loadManagerEShops(id, page, pageSize);
	}
	@Override
	public EShop loadEShop(Long id)
	{
		return pretaDao.loadEShop( id);
	}
	@Override
	public EShop getEShopByName(String name)
	{
		return pretaDao.loadEShop( name);
	}	
	@Override
	public void updateEShop(EShop entity)
	{
		pretaDao.updateEShop(entity);
	}
	public void addArticleToEshop( Long eShopId, Article entity)
	{
		pretaDao.addArticleToEshop( eShopId, entity);
	}
		/* Manager */
		@Override
		public User loadEShopManager( Long id)
		{
			return pretaDao.loadEShopManager( id);
		}
		/* Shop Sub */
		@Override
		public ShopSub getCurrentShopSub(Long id)
		{
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public PagedListJSON listShopSubsByEShop(Long eShopId, Integer page, Integer pageSize, boolean orderByIdAsc)
		{
			return pretaDao.loadEShopShopSubs(eShopId, page, pageSize, orderByIdAsc);
		}
	/* End EShops */
	
	/* ShopSub */
	@Override
	public PagedListJSON listShopSub( Integer page, Integer pageSize)
	{
		return pretaDao.loadShopSubs( page, pageSize);
	}
	@Override
	public void updateShopSub(ShopSub entity)
	{
		pretaDao.updateShopSub(entity);
	}
	@Override
	public ShopSub loadShopSub(Long id)
	{
		return pretaDao.loadShopSub(id);
	}
	@Override
	public PagedListJSON listShopSubByStatus(GenericStatus status, Integer page, Integer pageSize)
	{
		return pretaDao.loadShopSubByStatus(status, page, pageSize);
	}
	/* End Shop Sub */
	
	/* Upgrade Requests */
	@Override
	public UpgradeRequest loadUpgradeRequest( Long id)
	{
		UpgradeRequest entity = pretaDao.loadUpgradeRequest(id);
		entity.setRegDate( pretaDao.getRegDate( entity.getClass(), entity.getId()));
		
		return entity;
	}
	@Override
	public void updateUpgradeRequest( UpgradeRequest entity)
	{
		pretaDao.updateUpgradeRequest( entity);
	}
	@Override
	@SuppressWarnings( "unchecked")
	public PagedListJSON loadUpgradeRequests( Integer page, Integer pageSize, boolean orderByIdAsc, Integer status)
	{
		PagedListJSON result = pretaDao.loadUpgradeRequests(page, pageSize, orderByIdAsc, status);
		for( UpgradeRequest ur : (List<UpgradeRequest>) result.getEntities())
		{
			ur.setRegDate( pretaDao.getRegDate(ur.getClass(), ur.getId()));
		}
		return result;
	}
	/* End Upgrade Requests */
	
	/* Role */
	@Override
	public Long addRole( Role entity)
	{
		return pretaDao.addRole( entity);
	}
	/* End Role */

	/* Payment */
	@Override
	public PagedListJSON loadPayments( Integer page, Integer pageSize, int status, boolean orderByIdAsc)
	{
		return pretaDao.loadPayments( page, pageSize, status, orderByIdAsc);
	}
		/* Article Orders */
		@Override
		public PagedListJSON loadPaymentArticleOrders( Long id, Integer page, Integer pageSize)
		{
			return pretaDao.loadPaymentArticleOrders(id, page, pageSize);
		}
		/* Shop Sub */
		@Override
		public ShopSub loadPaymentShopSub( Long id)
		{
			return pretaDao.loadPaymentShopSub(id);
		}
		/* AdvOffer */
		@Override
		public AdvOffer loadPaymentAdvOffer( Long id)
		{
			return pretaDao.loadPaymentAdvOffer(id);
		}
	/* End Payment */
	
	/* Payment Type */
	@Override
	public Long addPaymentType( PaymentType entity)
	{
		return pretaDao.addPaymentType(entity);
	}
	/* End Payment type */
	
	/* Users */
		/* Buyers */
		@Override
		public PagedListJSON loadBuyers( Integer page, Integer pageSize, boolean orderByIdAsc)
		{
			return pretaDao.loadBuyers(page, pageSize, orderByIdAsc);
		}
		/* Buyer Profile Validation */
		@Override
		public PagedListJSON loadBuyerPendingProfileValidation( Integer page, Integer pageSize)
		{
			PagedListJSON tmp = pretaDao.loadBuyerPendingProfileValidation(page, pageSize);
			PagedListJSON result = new PagedListJSON();
			
			@SuppressWarnings("unchecked")
			List<User> entities = (List<User>) tmp.getEntities();
			List<User> resultEntities = new ArrayList<User>();
			
			for( User entity : entities)
			{
				if( entity.getProfileCompletion() == 100)
					resultEntities.add( entity);
			}
			
			if( pageSize != 0)
				result.setPagesNumber( resultEntities.size() % pageSize);
			else
				result.setPagesNumber(1);
			
			result.setItemsNumber( resultEntities.size());
			result.setEntities(resultEntities);
			
			return result;
		}
		/* Admin - Supervised Managers */
		@Override
		public PagedListJSON loadSupervisedManagers( Long id, Integer page, Integer pageSize)
		{
			return pretaDao.loadSupervisedManagers(id, page, pageSize);
		}
		@Override
		public void addSupervisedManagerToAdmin( Long adminId, Long managerId)
		{
			pretaDao.addSupervisedManagerToAdmin(adminId, managerId);
		}
		@Override
		public void removeSupervisedManagerFromAdmin( Long adminId, Long managerId)
		{
			pretaDao.removeSupervisedManagerFromAdmin(adminId, managerId);
		}
		/* Admin - ArticleOrder */
		@Override
		public PagedListJSON loadAdminArticleOrders( Long userId, Integer page, Integer pageSize, OrderStatus status, boolean orderByIdAsc)
		{
			return pretaDao.loadAdminArticleOrders(userId, page, pageSize, status, orderByIdAsc);
		}
	/* End Users */

	/* Logged User */
	@Override
	public PagedListJSON loadAdminPayments( Long userId, Integer page, Integer pageSize, int status, boolean orderByIdAsc)
	{
		return pretaDao.loadAdminPayments(userId, page, pageSize, status, orderByIdAsc);
	}
	@Override
	public PagedListJSON loadAdminExpensePendingOrders( Long userId, Integer page, Integer pageSize, boolean orderByIdAsc)
	{
		return pretaDao.loadAdminExpensePendingOrders(userId, page, pageSize, orderByIdAsc);
	}
	/* End Logged User */
		
	/* HomePicture */
	@Override
	public PagedListJSON loadSlides( Integer page, Integer pageSize, boolean orderByDiplayOrderAsc)
	{
		return pretaDao.loadSlides(page, pageSize, orderByDiplayOrderAsc);
	}
	@Override
	public Long addSlide( Slide entity)
	{
		return pretaDao.addSlide(entity);
	}
	@Override
	public void updateSlide( Slide entity)
	{
		pretaDao.updateSlide(entity);
	}
	@Override
	public Slide loadSlide( Integer dispOrder)
	{
		return pretaDao.loadSlide(dispOrder);
	}
	/* End HomePicture */

	/* Category */
	@Override
	public Long addCategory( Category entity)
	{
		return pretaDao.addCategory(entity);
	}
	@Override
	public void updateCategory( Category entity)
	{
		pretaDao.updateCategory(entity);
	}
	@Override
	public void deleteCategory( Long id)
	{
		pretaDao.deleteCategory(id);
	}
	/* End Category */

	/* Expense */
	@Override
	public Expense loadExpense( Long id)
	{
		return pretaDao.loadExpense(id);
	}
	@Override
	public PagedListJSON loadAdminExpenses( Long id, Integer page, Integer pageSize, boolean orderByIdAsc)
	{
		return pretaDao.loadAdminExpenses(id, page, pageSize, orderByIdAsc);
	}
	@Override
	public Long addExpense( Expense entity)
	{
		return pretaDao.addExpense(entity);
	}
		/* Manager Accounts for Expenses */
		@Override
		public PagedListJSON loadManagerEAccounts( Long id, Integer page, Integer pageSize)
		{
			return pretaDao.loadManagerEAccounts(id, page, pageSize);
		}
	/* End Expense */
}
