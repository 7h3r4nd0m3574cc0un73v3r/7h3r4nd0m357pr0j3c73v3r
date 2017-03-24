package org.usayi.preta.buzlayer;

import java.sql.Timestamp;
import java.util.List;

import org.usayi.preta.dao.IPretaDAO;
import org.usayi.preta.entities.Article;
import org.usayi.preta.entities.ArticleFeature;
import org.usayi.preta.entities.ArticleOrder;
import org.usayi.preta.entities.CartItem;
import org.usayi.preta.entities.EAccount;
import org.usayi.preta.entities.EMoneyProvider;
import org.usayi.preta.entities.EShop;
import org.usayi.preta.entities.Feature;
import org.usayi.preta.entities.FeatureValue;
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
import org.usayi.preta.entities.User;
import org.usayi.preta.entities.UserInfo;
import org.usayi.preta.entities.json.PagedListJSON;

public class RESTAPI implements IRESTAPI
{
	//DAO Config
	private IPretaDAO pretaDao;
	
	public void setPretaDao( IPretaDAO pretaDao)
	{
		this.pretaDao = pretaDao;
	}
	
	//EMPs
	@Override
	public PagedListJSON listEMoneyProviders( Integer page, Integer pageSize)
	{
		return pretaDao.loadEMoneyProviders( page, pageSize);
	}
	@Override
	public Long addEMoneyProvider(EMoneyProvider entity)
	{
		return pretaDao.addEMoneyProvider( entity);
	}
	@Override
	public void updateEMoneyProvider( EMoneyProvider entity)
	{
		pretaDao.updateEMoneyProvider( entity);
	}
	@Override
	public void deleteEMoneyProvider(Long id)
	{
		pretaDao.deleteEMoneyProvider( id);
	}
	@Override
	public EMoneyProvider getEMoneyProvider(Long id)
	{
		return pretaDao.getEMoneyProvider( id);
	}
	@Override
	public EMoneyProvider getEMoneyProviderByName( final String name)
	{
		return pretaDao.getEMoneyProviderByName( name);
	}
	@Override
	public List<?> getEMoneyProviderRevs(Long id)
	{
		return pretaDao.getEMoneyProviderRevs(id);
	}
	
	//ShopStatus
	@Override
	public PagedListJSON listShopStatus( Integer page, Integer pageSize)
	{
		return pretaDao.loadShopStatuses( page, pageSize);
	}
	@Override
	public ShopStatus getShopStatus(Long id)
	{
		return pretaDao.loadShopStatus( id);
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
	
	//SubOffer
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

	//ShopSub
	@Override
	public PagedListJSON listShopSub( Integer page, Integer pageSize)
	{
		return pretaDao.loadShopSubs( page, pageSize);
	}
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
	@Override
	public ShopSub getShopSub(Long id)
	{
		return pretaDao.loadShopSub(id);
	}
	
	//EShop	
	@Override
	public PagedListJSON listEShop( Integer page, Integer pageSize)
	{
		return pretaDao.listEShop( page, pageSize);
	}
	@Override
	public Long addEShop(EShop entity)
	{
		return pretaDao.addEshop( entity);
	}
	@Override
	public void editEShop(EShop entity)
	{
		pretaDao.updateEShop( entity);
	}
	@Override
	public EShop getEShop(Long id)
	{
		return pretaDao.loadEShop( id);
	}
	@Override
	public EShop getEShopByName(String name)
	{
		return pretaDao.loadEShop( name);
	}
	public void addArticleToEshop( final Long eShopId, final Article entity)
	{
		pretaDao.addArticleToEshop( eShopId, entity);
	}
	@Override
	public User getEShopManager( Long id)
	{
		return pretaDao.loadEShopManager( id);
	}

	
	//Role
	@Override
	public List<Role> listRole()
	{
		return pretaDao.listRole();
	}
	@Override
	public Role getRole(String name)
	{
		return pretaDao.loadRole( name);
	}
	@Override
	public Long addRole(Role entity)
	{
		pretaDao.addRole( entity);
		
		return entity.getId();
	}

	//User
	@Override
	public List<User> listManager()
	{
		return pretaDao.listManager();
	}
	@Override
	public List<User> listAdmin()
	{
		return pretaDao.loadAdmins();
	}
	@Override
	public User getUser(Long id)
	{		
		return pretaDao.loadUser( id); 
	}
	@Override
	public Long addUserInfo(UserInfo entity)
	{
		return pretaDao.addUserInfo( entity);
	}
	@Override
	public Long addUser(User entity)
	{
		return pretaDao.addUser( entity);
	}
	@Override
	public void updateUser(User entity)
	{
		pretaDao.updateUser( entity);
	}
	@Override
	public void editUserInfo(UserInfo entity)
	{
		pretaDao.updateUserInfo(entity);
	}
	@Override
	public UserInfo getUserInfo(Long id)
	{
		return pretaDao.getUserInfo( id);
	}
	@Override
	public User getUserByConfToken(String token)
	{
		return pretaDao.loadUserByToken(token);
	}
	@Override
	public User getUserByUsername(String username)
	{
		return pretaDao.loadUserByUsername(username);
	}
	@Override
	public User getUserByEMail(String email)
	{
		return pretaDao.loadUserByEMail(email);
	}

	//Article
	@Override
	public Article getArticle(Long id)
	{
		return pretaDao.loadArticle(id);
	}
	@Override
	public Long addArticle(Article entity)
	{
		return pretaDao.addArticle(entity);
	}
	@Override
	public void editArticle(Article entity)
	{
		pretaDao.updateArticle(entity);
	}
	@Override
	public void deleteArticle(Long id)
	{
		pretaDao.deleteArticle(id);
	}
	@Override
	public Picture getDefaultPicture(Long id)
	{
		return pretaDao.getDefaultPicture(id);
	}
	@Override
	public Picture getPicture( Long id)
	{
		return pretaDao.getPicture( id);
	}
	@Override
	public Integer getArticleRating( Long id)
	{
		return pretaDao.getArticleRating( id);
	}

	//CartItem
	@Override
	public CartItem getCartItem( Long id)
	{
		return pretaDao.loadCartItem(id);
	}
	public void addCartItem( CartItem entity)
	{
		pretaDao.addCartItem(entity);
	}
	@Override
	public void editCartItem( final CartItem entity)
	{
		pretaDao.updateCartItem(entity);
	}
	@Override
	public void removeCartItem( Long id)
	{
		pretaDao.removeCartItem(id);;
	}
	
	//Feature
	@Override
	public Feature getFeature( Long id)
	{
		return pretaDao.getFeature( id);
	}
	@Override
	public Long addFeature(Feature entity)
	{
		return pretaDao.addFeature( entity);
	}
	@Override
	public Feature getFeatureByLabel(String label)
	{
		return pretaDao.getFeatureByLabel( label);
	}
	@Override
	public Long addArticleFeature(ArticleFeature entity)
	{
		return pretaDao.addArticleFeature(entity);
	}

	//FeatureValue
	@Override
	public Long addFeatureValue( FeatureValue entity)
	{
		return pretaDao.addFeatureValue( entity);
	}
	@Override
	public void removeFeatureValue( Long id)
	{
		pretaDao.removeFeatureValue( id);
	}
	@Override
	public FeatureValue getFeatureValue( Long id)
	{
		return pretaDao.getFeatureValue(id);
	}
	
	//Picture
	@Override
	public Long addPicture(Picture entity)
	{
		return pretaDao.addPicture(entity);
	}
	@Override
	public void deletePicture(Long id)
	{
		pretaDao.deletePicture(id);
	}	

	//Payment
	@Override
	public Payment getPayment( Long id)
	{
		return pretaDao.loadPayment(id);
	}
//	@Override
//	public void addPayment( Payment entity)
//	{
//		pretaDao.addPayment( entity);
//	}
	@Override
	public void editPayment( Payment entity)
	{
		pretaDao.updatePayment(entity);
	}
	
	//ArticleOrder
	@Override
	public ArticleOrder loadArticleOrder(Long id)
	{
		return pretaDao.loadArticleOrder(id);
	}
	@Override
	public void addArticleOrder(ArticleOrder entity)
	{
		pretaDao.addArticleOrder(entity);
	}
	@Override
	public void updateArticleOrder(ArticleOrder entity)
	{
		pretaDao.updateArticleOrder(entity);
	}
	@Override
	public Timestamp getArticleOrderRegDate( Long id)
	{
		return pretaDao.getArticleOrderRegDate(id);
	}
	@Override
	public List<ArticleOrder> listArticleOrderByEShop( final Long eShopId)
	{
		return pretaDao.listArticleOrderByEShop( eShopId);
	}
	@Override
	public List<ArticleOrder> listArticleOrderByStatus( OrderStatus status)
	{
		return pretaDao.listArticleOrderByStatus( status);
	}
	@Override
	public User getArticleOrderUser( Long id)
	{
		return pretaDao.loadArticleOrderBuyer( id);
	}
	
	//OrderedArticle
	@Override
	public OrderedArticle getOrderedArticle(Long id)
	{
		return pretaDao.getOrderedArticle(id);
	}
	@Override
	public void addOrderedArticle(OrderedArticle entity)
	{
		pretaDao.addOrderedArticle(entity);
	}
	@Override
	public List<OrderedArticle> listOrderedArticleByOrder( Long id)
	{
		return pretaDao.loadOrderedArticleByOrder( id);
	}
	@Override
	public void editOrderedArticle( OrderedArticle entity)
	{
		pretaDao.editOrderedArticle( entity);
	}
	@Override
	public PagedListJSON listOrderedArticleToRate( Long userId, Integer page, Integer pageSize)
	{
		return pretaDao.listOrderedArticleToRate(userId, page, pageSize);
	}
	
	//OrderedArticleFeature
	@Override
	public Long addOrderedArticleFeature( OrderedArticleFeature entity)
	{
		return pretaDao.addOrderedArticleFeature( entity);
	}
	@Override
	public OrderedArticleFeature getOrderedArticleFeature( Long id)
	{
		return pretaDao.getOrderedArticleFeature( id);
	}
	
	//EAccount
	@Override
	public EAccount getEAccount(Long id)
	{
		return pretaDao.loadEAccount(id);
	}
	@Override
	public void addEAccount(EAccount entity)
	{
		pretaDao.addEAccount( entity);
	}
	@Override
	public void editEAccount(EAccount entity)
	{
		pretaDao.updateEAccount( entity);
	}

	//PaymentType
	@Override
	public PaymentType getPaymentType( final Long id)
	{
		return pretaDao.loadPaymentType(id);
	}
	@Override
	public void addPaymentType( PaymentType entity)
	{
		pretaDao.addPaymentType( entity);
	}
	@Override
	public void editPaymentType( PaymentType entity)
	{
		pretaDao.updatePaymentType( entity);
	}

}
