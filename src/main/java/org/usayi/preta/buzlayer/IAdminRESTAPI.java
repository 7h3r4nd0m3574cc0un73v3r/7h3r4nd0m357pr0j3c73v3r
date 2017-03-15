package org.usayi.preta.buzlayer;

import java.util.List;

import org.usayi.preta.entities.AdvOffer;
import org.usayi.preta.entities.AdvOption;
import org.usayi.preta.entities.Category;
import org.usayi.preta.entities.EMoneyProvider;
import org.usayi.preta.entities.EShop;
import org.usayi.preta.entities.GenericStatus;
import org.usayi.preta.entities.OrderStatus;
import org.usayi.preta.entities.PaymentType;
import org.usayi.preta.entities.Role;
import org.usayi.preta.entities.ShopStatus;
import org.usayi.preta.entities.ShopSub;
import org.usayi.preta.entities.Slide;
import org.usayi.preta.entities.SubOffer;
import org.usayi.preta.entities.UpgradeRequest;
import org.usayi.preta.entities.User;
import org.usayi.preta.entities.json.PagedListJSON;

public interface IAdminRESTAPI extends ISharedRESTAPI
{
	/* AdvOption */
	public AdvOption loadAdvOption( final Long id);
	public AdvOption loadAdvOption( final String title);
	public AdvOption getAdvOptionByTitle( final String title);
	public Long addAdvOption( final AdvOption entity);
	public void updateAdvOption( final AdvOption entity);
	public void deleteAdvOption( final Long id);
	public boolean isAdvOptionRegistered( final String title);
	/* End AdvOption */
	
	/* EMoney Provider */
	public PagedListJSON loadEMoneyProviders( final Integer page, final Integer pageSize);
	public Long addEMoneyProvider(EMoneyProvider entity);
	public void updateEMoneyProvider(EMoneyProvider entity);
	public void deleteEMoneyProvider(Long id);
	public EMoneyProvider loadEMoneyProvider(Long id);
	public EMoneyProvider loadEMoneyProviderByName( final String name);
	/* Revisions */
	public List<?> getEMoneyProviderRevs(Long id);
	/* End EMoney Provider */
	
	/* ShopStatus */
	public PagedListJSON listShopStatus( final Integer page, final Integer pageSize);
	public ShopStatus loadShopStatus( final Long id);
	public ShopStatus loadShopStatus( final String title);
	public Long addShopStatus( final ShopStatus entity);
	public void updateShopStatus( final ShopStatus entity);
	public void deleteShopStatus( final Long id);
	public boolean isShopStatusRegistered( final String title);
	/* End ShopStatus */
	
	/* SubOffer */
	public PagedListJSON listSubOffer( final Integer page, final Integer pageSize);
	public SubOffer loadSubOffer( final Long id);
	public SubOffer loadSubOffer( final String title);
	public boolean isSubOfferRegistered( final String title);
	public Long addSubOffer( final SubOffer entity);
	public void updateSubOffer( final SubOffer entity);
	public void deleteSubOffer( final Long id);
	/* End SubOffer */
	
	/* ShopSub */	
	public PagedListJSON listShopSub( final Integer page, final Integer pageSize);
	public void updateShopSub( final ShopSub entity);
	public ShopSub loadShopSub( final Long id);
	public PagedListJSON listPendingShopSub( final Integer page, final Integer pageSize);
	/* End ShopSub */
	
	/* ArticleOrder */
	public PagedListJSON loadArticleOrders( final Integer page, final Integer pageSize, final OrderStatus status, final boolean orderByIdAsc);
	/* End Article Order */
	
	/* EShop */
	public PagedListJSON listEShops( final Integer page, final Integer pageSize);
	public EShop loadEShop( final Long id);
	public EShop getEShopByName( final String name);
	public PagedListJSON listEShopByUser( final Long id, final Integer page, final Integer pageSize);
	public ShopSub getCurrentShopSub( final Long id);
	public void updateEShop( final EShop entity);
		/* Manager */
		public User loadEShopManager( final Long id);
		public PagedListJSON listShopSubsByEShop( final Long eShopId, final Integer page, final Integer pageSize, final boolean orderByIdAsc);
	/* End EShops */
	
	/* ShopSub */
	public PagedListJSON listShopSubByStatus( final GenericStatus status, final Integer page, final Integer pageSize);
	/* End ShopSub */
	
	/* Upgrade Requests */
	public UpgradeRequest loadUpgradeRequest( final Long id);
	public void updateUpgradeRequest( final UpgradeRequest entity);
	public PagedListJSON loadUpgradeRequests( final Integer page, final Integer pageSize, final boolean orderByIdAsc, final Integer status);
	/* End Upgrade Requests */
	
	/* Role */
	public Long addRole( final Role entity);
	/* End Role */
	
	/* Payment Type */
	public Long addPaymentType( final PaymentType entity);
	/* End Payment Type */
	
	/* Payment */
	public PagedListJSON loadPayments( final Integer page, final Integer pageSize, final int status, final boolean orderByIdAsc);
		/* Article Orders */
		public PagedListJSON loadPaymentArticleOrders( final Long id, final Integer page, final Integer pageSize);
		/* Shop Sub */
		public ShopSub loadPaymentShopSub( final Long id);
		/* AdvOffer */
		public AdvOffer loadPaymentAdvOffer( final Long id);
	/* End Payment */
	
	/* Users */
		/* Buyers */
		public PagedListJSON loadBuyers( final Integer page, final Integer pageSize, final boolean orderByIdAsc);
		/* Buyer Profile Validation */
		public PagedListJSON loadBuyerPendingProfileValidation( final Integer page, final Integer pageSize);
		/* Admin - Supervised Managers */
		public PagedListJSON loadSupervisedManagers( final Long id, final Integer page, final Integer pageSize);
		public void addSupervisedManagerToAdmin( final Long adminId, final Long managerId);
		public void removeSupervisedManagerFromAdmin( final Long adminId, final Long managerId);
		/* Admin - ArticleOrder */
		public PagedListJSON loadAdminArticleOrder( final Long userId, final Integer page, final Integer pageSize, final OrderStatus status, final boolean orderByIdAsc);
	/* End Users */
	
	/* Logged User */
	public PagedListJSON loadAdminPayments( final Long userId, final Integer page, final Integer pageSize, final int status, final boolean orderByIdAsc);
	public PagedListJSON loadAdminExpensePendingOrders( final Long userId, final Integer page, final Integer pageSize, final boolean orderByIdAsc);
	/* End Logged User */
	
	/* HomePicture */
	public PagedListJSON loadSlides( final Integer page, final Integer pageSize, final boolean orderByDiplayOrderAsc);
	public Long addSlide( final Slide entity);
	public void updateSlide( final Slide entity);
	public Slide loadSlide( final Integer dispOrder);
	/* End HomePicture */
	
	/* Category */
	public Long addCategory( final Category entity);
	public void updateCategory( final Category entity);
	public void deleteCategory( final Long id);
	/* End Category */
}
