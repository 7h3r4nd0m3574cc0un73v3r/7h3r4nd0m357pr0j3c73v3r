package org.usayi.preta.buzlayer;

import java.sql.Timestamp;
import java.util.List;

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

public interface IRESTAPI
{
	//EMoneyProvider
	public PagedListJSON listEMoneyProviders( final Integer page, final Integer pageSize);
	public EMoneyProvider getEMoneyProvider( final Long id);
	public EMoneyProvider getEMoneyProviderByName( final String name);
	public Long addEMoneyProvider( final EMoneyProvider entity);
	public void updateEMoneyProvider( final EMoneyProvider entity);
	public void deleteEMoneyProvider( final Long id);
	public List<?> getEMoneyProviderRevs( final Long id);
	
	//ShopStatus
	public PagedListJSON listShopStatus( final Integer page, final Integer pageSize);
	public ShopStatus getShopStatus( final Long id);
	public Long addShopStatus( final ShopStatus entity);
	public void updateShopStatus( final ShopStatus entity);
	public void deleteShopStatus( final Long id);
	
	//SubOffer
	public PagedListJSON listSubOffer( final Integer page, final Integer pageSize);
	public SubOffer getSubOffer( final Long id);
	public Long addSubOffer( final SubOffer entity);
	public void updateSubOffer( final SubOffer entity);
	public void deleteSubOffer( final Long id);

	//ShopSub
	public PagedListJSON listShopSub( final Integer page, final Integer pageSize);
	public Long addShopSub( final ShopSub entity);
	public void editShopSub( final ShopSub entity);
	public ShopSub getShopSub( final Long id);
	public PagedListJSON listPendingShopSub( final Integer page, final Integer pageSize);
	
	//EShop
	public PagedListJSON listEShop( final Integer page, final Integer pageSize);
	public Long addEShop( final EShop entity);
	public void editEShop( final EShop entity);
	public EShop getEShop( final Long id);
	public EShop getEShopByName( final String name);
	public void addArticleToEshop( final Long eShopId, final Article entity);
	public User getEShopManager( final Long id);

	//Roles
	public List<Role> listRole();
	public Role getRole( final String name);
	public Long addRole( final Role entity);
	
	//User
	public List<User> listManager();
	public List<User> listAdmin();
	public User getUser( final Long id);
	public UserInfo getUserInfo( final Long id);
	public Long addUserInfo( final UserInfo entity);
	public Long addUser( final User entity);
	public void updateUser( final User entity);
	public void editUserInfo( final UserInfo entity);
	public User getUserByConfToken( final String token);
	public User getUserByUsername( final String username);
	public User getUserByEMail( final String email);
	
	//Article
	//Fetch a list of the most recent articles
	public Article getArticle( final Long id);
	public Long addArticle( final Article entity);
	public void editArticle( final Article entity);
	public void deleteArticle( final Long id);
	public Picture getDefaultPicture( final Long id);
	public Picture getPicture( final Long id);
	public Integer getArticleRating( final Long id);
	
	//CartItem
	public CartItem getCartItem( final Long id);
	public void addCartItem( final CartItem entity);
	public void editCartItem( final CartItem entity);
	public void removeCartItem( final Long id);

	//Feature
	public Feature getFeature( final Long id);
	public Long addFeature( final Feature entity);
	public Feature getFeatureByLabel( final String label);
	public Long addArticleFeature( final ArticleFeature entity);

	//FeatureValue
	public Long addFeatureValue( final FeatureValue entity);
	public void removeFeatureValue( final Long id);
	public FeatureValue getFeatureValue( final Long id);
	
	//Picture
	public Long addPicture( final Picture entity);
	public void deletePicture( final Long id);
	
	//Payment
	public Payment getPayment( final Long id);
//	public void addPayment( final Payment entity);
	public void editPayment( final Payment entity);
	
	//ArticleOrder
	public ArticleOrder loadArticleOrder( final Long id);
	public void addArticleOrder( final ArticleOrder entity);
	public void updateArticleOrder( final ArticleOrder entity);
	
	public Timestamp getArticleOrderRegDate( final Long id);
	public List<ArticleOrder> listArticleOrderByEShop( final Long eShopId);
	public List<ArticleOrder> listArticleOrderByStatus( final OrderStatus status);
	public User getArticleOrderUser( final Long id);
	
	//OrderedArticle
	public OrderedArticle getOrderedArticle( final Long id);
	public void addOrderedArticle( final OrderedArticle entity);
	public List<OrderedArticle> listOrderedArticleByOrder( final Long id);
	public void editOrderedArticle( final OrderedArticle entity);
	public PagedListJSON listOrderedArticleToRate( final Long userId, final Integer page, final Integer pageSize);
	
	//OrderedArticleFeature
	public Long addOrderedArticleFeature( final OrderedArticleFeature entity);
	public OrderedArticleFeature getOrderedArticleFeature( final Long id);
	
	//EAccounts
	public EAccount getEAccount( final Long id);
	public void addEAccount( final EAccount entity);
	public void editEAccount( final EAccount entity);
	
	//PaymentType
	public PaymentType getPaymentType( final Long id);
	public void addPaymentType( final PaymentType entity);
	public void editPaymentType( final PaymentType entity);
	
}