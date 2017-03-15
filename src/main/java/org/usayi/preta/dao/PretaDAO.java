package org.usayi.preta.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.validator.constraints.Email;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;
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
import org.usayi.preta.entities.Expense;
import org.usayi.preta.entities.Feature;
import org.usayi.preta.entities.FeatureValue;
import org.usayi.preta.entities.GenericStatus;
import org.usayi.preta.entities.Notification;
import org.usayi.preta.entities.OrderStatus;
import org.usayi.preta.entities.OrderedArticle;
import org.usayi.preta.entities.OrderedArticleFeature;
import org.usayi.preta.entities.Payment;
import org.usayi.preta.entities.PaymentType;
import org.usayi.preta.entities.Picture;
import org.usayi.preta.entities.RevEntity;
import org.usayi.preta.entities.Role;
import org.usayi.preta.entities.ShopStatus;
import org.usayi.preta.entities.ShopSub;
import org.usayi.preta.entities.Slide;
import org.usayi.preta.entities.SubOffer;
import org.usayi.preta.entities.UpgradeRequest;
import org.usayi.preta.entities.User;
import org.usayi.preta.entities.UserInfo;
import org.usayi.preta.entities.json.PagedListJSON;

@Transactional
public class PretaDAO implements IPretaDAO
{
	@PersistenceContext
	private EntityManager em;
	
	/* EMoney Providers */
	@Override
	public PagedListJSON loadEMoneyProviders( Integer page, Integer pageSize)
	{
		Query query = em.createQuery( "SELECT emp FROM EMoneyProvider emp WHERE emp.isDeleted = false");
		
		return generatePagedList( query, page, pageSize);
	}
	@Override
	public Long addEMoneyProvider(EMoneyProvider entity)
	{
		em.persist( entity);
		
		return entity.getId();
	}
	@Override
	public void updateEMoneyProvider(EMoneyProvider entity)
	{
		em.merge( entity);
	}
	@Override
	public void deleteEMoneyProvider(Long id)
	{
		EMoneyProvider entity = em.find( EMoneyProvider.class, id);
		entity.setIsDeleted( true);
		
		em.merge( entity);
	}
	@Override
	public EMoneyProvider getEMoneyProvider(Long id)
	{
		EMoneyProvider entity = em.find(EMoneyProvider.class, id);
		
		if( entity == null || entity.getIsDeleted())
			return null;

		return entity;
	}
	public EMoneyProvider getEMoneyProviderByName( String name)
	{
		Query query = em.createQuery( "SELECT emp FROM EMoneyProvider emp WHERE emp.name = :name");
		query.setParameter( "name", name);
		
		if( query.getResultList().isEmpty())
			return null;
		EMoneyProvider entity = (EMoneyProvider) query.getSingleResult();
		
		return entity;
	}
	@Override
	public List<?> getEMoneyProviderRevs( Long id)
	{	
		//		AuditReader reader = AuditReaderFactory.get(em);
		//		
		//		
		//		AuditQuery query = reader.createQuery()
		//								 .forRevisionsOfEntity( EMoneyProvider.class,
		//										 				false, false)
		//								 .add( AuditEntity.property( "id").eq( id));
		//		return query.getResultList();
		return null;
	}
	/* End EMoney Provider */
	
	/* ShopStatus */
	@Override
	public PagedListJSON loadShopStatuses( Integer page, Integer pageSize)
	{
		Query query = em.createQuery( "SELECT entity FROM ShopStatus entity WHERE entity.isDeleted = false");
		
		return generatePagedList( query, page, pageSize);
	}
	@Override
	public Long addShopStatus(ShopStatus entity)
	{
		em.persist( entity);

		return entity.getId();
	}
	@Override
	public void updateShopStatus(ShopStatus entity)
	{
		em.merge( entity);
	}
	@Override
	public void deleteShopStatus(Long id)
	{
		ShopStatus entity = em.find( ShopStatus.class, id);
		entity.setIsDeleted( true);

		em.persist( entity);
	}
	@Override
	public ShopStatus loadShopStatus(Long id)
	{
		ShopStatus entity = em.find(ShopStatus.class, id);
		if( entity == null || entity.getIsDeleted())
			return null;

		return entity;
	}
	@Override
	public boolean isShopStatusRegistered( String title)
	{
		Query query = em.createQuery( "SELECT entity FROM ShopStatus entity WHERE entity.title = :title");
		query.setParameter( "title", title);
		
		if( query.getResultList().isEmpty())
			return false;
		else
			return true;
	}
	@Override
	public ShopStatus loadShopStatus( String title)
	{
		Query query = em.createQuery( "SELECT entity FROM ShopStatus entity WHERE entity.title = :title");
		query.setParameter( "title", title);
		
		if( query.getResultList().isEmpty())
			return null;
		else
			return (ShopStatus) query.getSingleResult();
	}
	/* End ShopStatus */
	
	/* SubOffer */
	@Override
	public PagedListJSON loadSubOffers( Integer page, Integer pageSize)
	{
		Query query = em.createQuery( "SELECT entity FROM SubOffer entity WHERE entity.isDeleted = false");
		
		return generatePagedList(query, page, pageSize);
	}
	@Override
	public Long addSubOffer(SubOffer entity)
	{
		em.persist( entity);

		return entity.getId();
	}
	@Override
	public void updateSubOffer(SubOffer entity)
	{
		em.merge( entity);
	}
	@Override
	public void deleteSubOffer(Long id)
	{
		SubOffer entity = em.find( SubOffer.class, id);
		entity.setIsDeleted( true);

		em.merge( entity);
	}
	@Override
	public SubOffer loadSubOffer(Long id)
	{
		SubOffer entity = em.find(SubOffer.class, id);
		if( entity == null || entity.getIsDeleted())
			return null;

		return entity;
	}
	@Override
	public boolean isSubOfferRegistered( String title)
	{
		Query query = em.createQuery( "SELECT entity FROM SubOffer entity WHERE entity.title = :title");
		query.setParameter( "title", title);
		
		if( query.getResultList().isEmpty())
			return false;
		else
			return true;
	}
	@Override
	public SubOffer loadSubOffer( String title)
	{
		Query query = em.createQuery( "SELECT entity FROM SubOffer entity WHERE entity.title = :title");
		query.setParameter( "title", title);
		
		if( query.getResultList().isEmpty())
			return null;
		else
			return (SubOffer) query.getSingleResult();
	}
	/* End SubOffer */
	
	/* Shop Sub */
	@Override
	public PagedListJSON loadShopSubs( Integer page, Integer pageSize)
	{
		Query query = em.createQuery( "SELECT entity FROM ShopSub entity");

		return generatePagedList(query, page, pageSize);
	}
	@Override
	public PagedListJSON listPendingShopSub( Integer page, Integer pageSize)
	{
		Query query = em.createQuery( "SELECT entity FROM ShopSub entity WHERE entity.subStatus = 0 OR entity.subStatus = 1");
		
		return generatePagedList(query, page, pageSize);
	}
	@Override
	public Long addShopSub(ShopSub entity)
	{
		/* Makes sure the endDate is set */
		entity.setEndDate();
		em.persist( entity);
		
		return entity.getId();
	}
	@Override
	public void updateShopSub(ShopSub entity)
	{	
		em.merge( entity);
	}
	@Override
	public ShopSub loadShopSub(Long id)
	{
		ShopSub entity = em.find(ShopSub.class, id);

		return entity;
	}
	@Override
	@SuppressWarnings( "unchecked")
	public PagedListJSON loadShopSubByStatus( GenericStatus status, Integer page, Integer pageSize)
	{
		String hql = "SELECT entity FROM ShopSub entity";
		if( status != null)
			hql += " WHERE entity.subStatus = :subStatus";
		
		Query query = em.createQuery( hql);
		if( status != null)
			query.setParameter( "subStatus", status);
		
		List<Article> entities = query.getResultList();
		
		PagedListJSON result = new PagedListJSON();
		result.setItemsNumber( entities.size());
		result.setPagesNumber( computePagesNumber( entities.size(), pageSize));
		
		query.setFirstResult( (page - 1) * pageSize);
		query.setMaxResults( pageSize);
		
		result.setEntities( query.getResultList());
		
		return result;
	}
		/* Manager */
		@Override
		public User loadShopSubManager( Long id)
		{
			try
			{
				Query query = em.createQuery( "SELECT entity.id FROM ShopSub shopSub JOIN shopSub.eShop entity WHERE shopSub.id = :id");
				query.setParameter( "id", id);
				
				Long eShopId = (Long) query.getSingleResult();
				
				return loadEShopManager(eShopId);
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
	/* Shop Sub */
	
	/* EShop */
	@Override
	@SuppressWarnings("unchecked")
	public PagedListJSON listEShop( Integer page, Integer pageSize)
	{
		Query query = em.createQuery( "SELECT eShop FROM EShop eShop");
		
		PagedListJSON result = generatePagedList(query, page, pageSize);
		
		for( EShop eShop : (List<EShop>) result.getEntities())
		{
			eShop.setRegDate( this.getRegDate( EShop.class, eShop.getId()));
		}

		return result;
	}
	@Override
	public PagedListJSON loadManagerEShops(Long id, Integer page, Integer pageSize)
	{
		Query query = em.createQuery( "SELECT DISTINCT entity FROM EShop entity, User user JOIN user.managedEShops managedEShops"
				+ " WHERE managedEShops.id = entity.id AND user = :user");
		query.setParameter( "user", this.loadUser( id));
		
		return generatePagedList(query, page, pageSize);
	}
	@Override
	public Long addEshop(EShop entity)
	{	
		try
		{
			em.persist( entity);
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}

		return entity.getId();
	}
	@Override
	public void updateEShop(EShop entity)
	{
		em.merge( entity);
	}
	@Override
	public EShop loadEShop(Long id)
	{
		EShop entity = em.find( EShop.class, id);
		if( entity == null)
			return null;
		entity.setRegDate( this.getRegDate( EShop.class, id));
		
		return entity;
	}
	@Override
	public EShop loadEShop(String name)
	{
		Query query = em.createQuery( "SELECT eShop FROM EShop eShop WHERE eShop.name = :name");
		query.setParameter( "name", name);

		if( query.getResultList().isEmpty())
			return null;

		return (EShop) query.getSingleResult();
	}
		/* Articles */
		@Override
		public Long addArticleToEshop( Long eShopId, Article entity)
		{	
			try
			{
				EShop eShop = em.find( EShop.class, eShopId);
				
				entity.seteShop( eShop);
				
				em.persist( entity);
				
				eShop.addArticle(entity);
				
				em.merge(eShop);
				
				return entity.getId();
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
		@Override
		public PagedListJSON loadEShopArticles( Long id, Integer page, Integer pageSize)
		{	
			try
			{
				Query query = em.createQuery( "SELECT article FROM Article article JOIN article.eShop eShop WHERE article.isDeleted = false AND eShop.id = :id"
						+ " ORDER BY article.id DESC");
				query.setParameter( "id", id);
				
				return generatePagedList(query, page, pageSize);
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
		@Override
		public PagedListJSON loadDiscountedArticlesByEShop( Long id, Integer page, Integer pageSize)
		{
			Query query = em.createQuery( "SELECT entity FROM Article entity JOIN entity.eShop eShop WHERE entity.isDiscounted = true AND eShop.id = :id");
			query.setParameter( "id", id);
			
			return generatePagedList(query, page, pageSize);
		}
		/* Manager */
		@Override
		public User loadEShopManager( Long id)
		{
			Query query = em.createQuery( "SELECT entity FROM User entity JOIN entity.managedEShops managedEShops WHERE managedEShops.id = :id");
			query.setParameter( "id", id);
			
			User manager = (User) query.getSingleResult();
			
			return manager;
		}
		/* ShopSub */
		@Override
		public Long addShopSubToEShop( Long id, ShopSub entity)
		{
			try
			{
				EShop eShop = em.find( EShop.class, id);
				
				entity.seteShop( eShop);

				/* Makes sure the endDate is set */
				entity.setEndDate();
				em.persist( entity);
				
				eShop.addShopSub( entity);
				
				em.merge(eShop);
				
				return entity.getId();
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
		@Override
		public ShopSub getCurrentShopSub( Long id)
		{
			Query query = em.createQuery( "SELECT entity FROM ShopSub entity JOIN entity.eShop eShop WHERE eShop.id = :id "
					+ "AND entity.subStatus = :subStatus");
			query.setParameter( "id", id);
			query.setParameter( "subStatus" , GenericStatus.ONGOING);
			
			if( query.getResultList().size() <= 0)
				return null;
			
			return (ShopSub) query.getSingleResult();
		}	
		@Override
		public PagedListJSON loadEShopShopSubs(Long eShopId, Integer page, Integer pageSize, boolean orderByIdAsc)
		{
			String hql = "SELECT entity FROM ShopSub entity JOIN entity.eShop eShop WHERE eShop.id = :eShopId ORDER BY entity.id ";
			if( orderByIdAsc)
				hql += "ASC";
			else
				hql += "DESC";
			
			Query query = em.createQuery( hql);
			query.setParameter("eShopId", eShopId);

			return generatePagedList(query, page, pageSize);
		}
		/* ArticleOrder */
		@SuppressWarnings("unchecked")
		public PagedListJSON loadEShopArticleOrders( Long id, Integer page, Integer pageSize, OrderStatus status, boolean orderByIdAsc)
		{
			String hql = "SELECT DISTINCT entity FROM ArticleOrder entity JOIN entity.orderedArticles orderedArticles JOIN orderedArticles.article article"
					+ " JOIN article.eShop eShop WHERE eShop.id = :id";
			if( status != OrderStatus.ALL)
				hql += " AND entity.status = :status";
			if( !orderByIdAsc)
				hql += " ORDER BY entity.id DESC";
			
			Query query = em.createQuery( hql);
			query.setParameter( "id", id);
			
			if( status != OrderStatus.ALL)
				query.setParameter( "status", status);
			
			PagedListJSON result = generatePagedList(query, page, pageSize);
			
			for( ArticleOrder entity : (List<ArticleOrder>) result.getEntities())
			{
				entity.setRegDate( getRegDate( ArticleOrder.class, entity.getId()));
			}
			
			return result;
		}
	/* End EShop */
	
	/* Roles */
	@Override
	public Role loadRole(String name)
	{
		Query query = em.createQuery( "SELECT role FROM Role role WHERE role.name = :name");
		query.setParameter( "name", name);

		if( query.getResultList().isEmpty())
			return null;
		
		Role entity = (Role) query.getSingleResult();
		
		return entity;
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<Role> listRole()
	{
		Query query = em.createQuery( "SELECT role FROM Role role");

		return query.getResultList();
	}
	@Override
	public Long addRole(Role entity)
	{
		em.persist( entity);

		return entity.getId();
	}

	/* User */
	@Override
	public PagedListJSON loadBuyers( Integer page, Integer pageSize, boolean orderByIdAsc) 
	{
		String hql = "SELECT user FROM User user JOIN user.userInfo userInfo JOIN user.roles role WHERE role.name = :name ORDER BY userInfo.id";
		if( !orderByIdAsc)
			hql += " DESC";
		Query query = em.createQuery( hql);
		query.setParameter( "name", "ROLE_BUYER");

		return generatePagedList(query, page, pageSize);
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<User> listManager() 
	{
		Query query = em.createQuery( "SELECT user FROM User user JOIN user.roles role WHERE role.name = :name");
		query.setParameter( "name", "ROLE_MANAGER");

		return query.getResultList();
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<User> loadAdmins() 
	{
		Query query = em.createQuery( "SELECT user FROM User user JOIN user.roles role WHERE role.name = :name");
		query.setParameter( "name", "ROLE_ADMIN");

		return query.getResultList();
	}
	@Override
	public User loadUser( Long id) 
	{
		User entity = em.find( User.class, id);
		
		return entity;
	}
	@Override
	public Long addUser(User entity) 
	{
		try
		{
			em.persist( entity);
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}

		return entity.getUserInfo().getId();
	}
	@Override
	public Long addUserInfo(UserInfo entity)
	{
		try
		{
			em.persist( entity);
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
		
		return entity.getId();
	}
	@Override
	public void updateUser(User entity)
	{
		try
		{
			em.merge( entity);
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
	}
	@Override
	public UserInfo getUserInfo(Long id)
	{
		Query query = em.createQuery( "SELECT userInfo FROM UserInfo userInfo WHERE userInfo.id = :id");
		query.setParameter( "id", id);
		if( query.getResultList().isEmpty())
			return null;
		UserInfo entity = (UserInfo) query.getSingleResult();
		
		return entity;
	}
	@Override
	public void updateUserInfo(UserInfo entity)
	{
		try
		{
			em.merge( entity);
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
	}
	@Override
	public User loadUserByToken(String token)
	{
		Query query = em.createQuery( "SELECT user FROM User user WHERE confToken = :token");
		query.setParameter( "token", token);

		if( query.getResultList().isEmpty())
			return null;

		return (User) query.getSingleResult();
	}
	@Override
	public User loadUserByUsername(String username)
	{
		Query query = em.createQuery( "SELECT user FROM User user WHERE username = :username");
		query.setParameter( "username", username);
		
		if( query.getResultList().isEmpty())
			return null;
		User entity = (User) query.getSingleResult();
		
		return entity;
	}
	@Override
	public User loadUserByEMail( @Email String email)
	{	
		try
		{
			Query query = em.createQuery( "SELECT user FROM User user JOIN user.userInfo userInfo WHERE userInfo.email = :email");
			query.setParameter( "email", email);
			
			if( query.getResultList().isEmpty())
				return null;

			return (User) query.getSingleResult();
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
		/* Cart */
		@Override
		public PagedListJSON loadCartItems( Long userId, Integer page, Integer pageSize)
		{
			Query query = em.createQuery( "SELECT entity FROM User user JOIN user.cart entity JOIN user.userInfo userInfo WHERE userInfo.id = :userId AND entity.isDeleted = false"
					+ " ORDER BY entity.id DESC");
			query.setParameter( "userId", userId);
			
			return generatePagedList(query, page, pageSize);
		}
		@Override
		public Long addCartItemToUserCart( Long id, CartItem entity)
		{
			try
			{
				User user = loadUser( id);
				
				em.persist( entity);
				
				user.addCartItem(entity);
				
				em.merge( user);
				
				return entity.getId();
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
		@Override
		public void removeCartItemFromUserCart( Long userId, Long cartItemId)
		{
			try
			{
				User user = loadUser( userId);
				user.removeCartItem( em.find( CartItem.class, cartItemId));

				em.merge( user);
			}
			catch( Exception e)
			{
				e.printStackTrace();
			}
		}
		@Override
		public void emptyUserCart( Long userId)
		{
			try
			{
				User user = loadUser( userId);
				user.setCart( new ArrayList<CartItem>());
				
				em.merge( user);
			}
			catch( Exception e)
			{
				e.printStackTrace();
			}
		}
		/* Managed EShops */
		@Override
		public PagedListJSON listManagedEShops( Long userId, Integer page, Integer pageSize)
		{
			Query query = em.createQuery( "SELECT entity FROM User user JOIN user.managedEShops entity JOIN user.userInfo userInfo WHERE userInfo.id = :userId");
			query.setParameter( "userId", userId);
			
			return generatePagedList(query, page, pageSize);
		}
		@Override
		public Long addEShopToUser( Long userId, EShop entity)
		{
			try
			{
				User user = loadUser( userId);
				
				em.persist( entity);
				
				user.addManagedEShop( entity);
				
				em.merge(user);
				
				return entity.getId();
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
		/* ArticleOrders as Manager */
		@Override
		public PagedListJSON loadArticleOrderByManagerAndByStatus( Long userId, Integer page, Integer pageSize, boolean orderByIdAsc, OrderStatus orderStatus)
		{
			String hql = "SELECT DISTINCT entity FROM ArticleOrder entity, User manager JOIN entity.orderedArticles orderedArticles JOIN orderedArticles.article article"
					+ " JOIN article.eShop articleEShop JOIN manager.managedEShops managedEShops WHERE manager = :manager AND articleEShop.id = managedEShops.id";
			if( orderStatus != OrderStatus.ALL)
				hql += " AND entity.status = :orderStatus";
			hql += " ORDER BY entity.id";
			if( !orderByIdAsc)
				hql += " DESC";
			
			Query query = em.createQuery( hql);
			query.setParameter( "manager", loadUser( userId));
			if( orderStatus != OrderStatus.ALL)
				query.setParameter( "orderStatus", orderStatus);
			
			return generatePagedList( query, page, pageSize);
		}
		@Override
		public Long addEAccountToUserInfo( Long id, Long empId, EAccount entity)
		{
			try
			{
				UserInfo userInfo = em.find( UserInfo.class, id);
				
				EMoneyProvider emp = em.find( EMoneyProvider.class, empId);
				entity.setUser( userInfo);
				entity.seteMoneyProvider( emp);
				
				em.persist( entity);
				
				userInfo.addEAccount( entity);
				
				em.merge( userInfo);
				
				return entity.getId();
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
		@Override
		public Long addExEAccountToUserInfo( Long id, Long eAccountId)
		{
			try
			{
				UserInfo userInfo = em.find( UserInfo.class, id);
				
				EAccount eAccount = em.find( EAccount.class, eAccountId);
				
				eAccount.setUser( userInfo);
				eAccount.setRelId( userInfo.geteAccounts().size() + 1L);
				
				em.merge( eAccount);
				
				userInfo.addEAccount( eAccount);
				
				em.merge( userInfo);
				
				return eAccount.getId();
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
		/* ArticleOrders as Buyer */
		@Override
		@SuppressWarnings( "unchecked")
		public PagedListJSON loadBuyerArticleOrders( Long userId, Integer page, Integer pageSize, OrderStatus orderStatus, boolean orderByIdAsc)
		{
			String hql = "SELECT entity FROM ArticleOrder entity JOIN entity.user user WHERE user = :user ";
			if( orderStatus != OrderStatus.ALL)
				hql += " AND entity.status = :orderStatus";
			if( !orderByIdAsc)
				hql += " ORDER BY entity.id DESC";
			
			Query query = em.createQuery( hql);
			query.setParameter( "user", this.loadUser(userId));
			
			if( orderStatus != OrderStatus.ALL)
				query.setParameter( "orderStatus", orderStatus);
			
			PagedListJSON result = generatePagedList(query, page, pageSize);
			
			for( ArticleOrder entity : (List<ArticleOrder>) result.getEntities())
			{
				entity.setRegDate( this.getArticleOrderRegDate(entity.getId()));
			}
			
			return result;
		}
		@Override
		public Long addArticleOrderToUser( Long id, ArticleOrder entity)
		{
			try
			{
				User user = this.loadUser(id);
				
				entity.setUser( user);
				
				em.persist( entity);
				
				user.addArticleOrder( entity);
				
				em.merge( user);
				
				return entity.getId();
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
		/* Article */
		@Override
		@SuppressWarnings( "unchecked")
		public PagedListJSON loadLastVisitedArticles( Long userId, Integer page, Integer pageSize)
		{
			try
			{
				Query query = em.createQuery( "SELECT entity FROM User user JOIN user.userInfo userInfo JOIN user.lastVisitedArticles entity WHERE userInfo.id = :userId");
				query.setParameter( "userId", userId);
				
				PagedListJSON result = generatePagedList(query, page, pageSize);
				
				for( Article article : ( List<Article>) result.getEntities())
				{
					article.setRating( getArticleRating( article.getId()));
					article.setSaleNumber( getArticleSaleNumber( article.getId()));
				}
				
				return result;
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return new PagedListJSON( 0, 0, new ArrayList<Article>());
			}
		}
		@Override
		public void addVisitedArticle( Long userId, Long articleId)
		{
			try
			{
				User user = loadUser( userId);
				
				Article article = loadArticle( articleId);
				
				if( user.getLastVisitedArticles().contains( article))
				{
					user.removeVisitedArticle( article);
				}
				
				user.addVisitedArticle( article);
				
				em.merge( user);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		/* EShop */
		@Override
		public PagedListJSON loadFavoritesEShops( Long userId, Integer page, Integer pageSize)
		{
			Query query = em.createQuery( "SELECT entity FROM User user JOIN user.favoritesEShop entity JOIN user.userInfo userInfo"
										+ " WHERE userInfo.id = :userId");
			query.setParameter( "userId", userId);
			
			return generatePagedList(query, page, pageSize);
		}
		@Override
		public void addEShopAsFavToUser( Long userId, Long eShopId)
		{
			try
			{
				User user = loadUser( userId);
				
				EShop eShop = loadEShop(eShopId);
				
				user.addEShopToFavorites(eShop);
				
				em.merge( user);
			}
			catch( Exception e)
			{
				e.printStackTrace();
			}
		}
		@Override
		public void removeEShopFromUserFav( Long userId, Long eShopId)
		{
			try
			{
				User user = loadUser( userId);
				
				EShop eShop = loadEShop(eShopId);
				
				user.removeEShopFromFavorites(eShop);
				
				em.merge( user);
			}
			catch( Exception e)
			{
				e.printStackTrace();
			}
		}
		/* Buyers Profile Validation */
		public PagedListJSON loadBuyerPendingProfileValidation( Integer page, Integer pageSize)
		{
			Query query = em.createQuery("SELECT entity FROM User entity JOIN entity.roles role WHERE role.name = :role AND entity.approved = false AND entity.isEnabled = true");
			query.setParameter( "role", "ROLE_BUYER");
			
			return generatePagedList(query, page, pageSize);
		}
		/* Admin - Supervised Managers */
		@Override
		public PagedListJSON loadSupervisedManagers( Long id, Integer page, Integer pageSize)
		{
			String hql = "SELECT entity FROM User user JOIN user.supervisedManagers entity JOIN user.userInfo userInfo WHERE userInfo.id = :id ";
			Query query = em.createQuery( hql);
			query.setParameter( "id", id);
			
			return generatePagedList(query, page, pageSize);
		}
		@Override
		public void addSupervisedManagerToAdmin( Long adminId, Long managerId)
		{
			try
			{
				User admin = loadUser( adminId);
				
				User manager = loadUser( managerId);
				
				admin.addSupervisedManager(manager);
			
//				em.merge( admin);
			}
			catch( Exception e)
			{
				e.printStackTrace();			
			}
		}
		@Override
		public void removeSupervisedManagerFromAdmin( Long adminId, Long managerId)
		{
			try
			{
				User admin = loadUser(adminId);
				
				User manager = loadUser( managerId);
				
				admin.removeSupervisedManager(manager);
				
				updateUser(admin);
			}
			catch( Exception e)
			{
				e.printStackTrace();
			}
		}
		/* Manager - AdvOffers */
		@Override
		@SuppressWarnings("unchecked")
		public PagedListJSON loadManagerAdvOffers(Long id, Integer page, Integer pageSize, GenericStatus status, boolean orderByIdAsc)
		{
			String hql = "SELECT DISTINCT entity FROM AdvOffer entity, User user JOIN entity.article article JOIN article.eShop eShop JOIN user.managedEShops managedEShops "
					+ " JOIN user.userInfo userInfo "
					+ " WHERE userInfo.id = :id AND eShop.id = managedEShops.id";
			
			if( status != GenericStatus.ALL)
				hql+= " AND entity.status = :status";
			Query query = em.createQuery( hql);
			query.setParameter( "id", id);
			
			if( status != GenericStatus.ALL)
				query.setParameter( "status", status);
			
			PagedListJSON result = generatePagedList(query, page, pageSize);
			
			for( AdvOffer advOffer : (List<AdvOffer>) result.getEntities())
			{
				advOffer.setRegDate( getRegDate( AdvOffer.class, advOffer.getId()));
			}
			
			return result;
		}
		/* Advertised Articles */
		@Override
		@SuppressWarnings( "unchecked")
		public PagedListJSON loadAdvertisedArticles( Integer page, Integer pageSize, Integer code)
		{	
			String hql = "SELECT article FROM AdvOffer advOffer JOIN advOffer.article article JOIN article.eShop eShop JOIN eShop.currentShopSub shopSub WHERE advOffer.status = :status"
					+ " AND :now BETWEEN advOffer.startDate AND advOffer.endDate"
					+ " AND :now BETWEEN shopSub.startDate AND shopSub.endDate";
			if( code != 0)
				hql += " AND advOffer.code = :code";
			
			Query query = em.createQuery( hql);
			query.setParameter( "status", GenericStatus.ONGOING);
			query.setParameter( "now", new Timestamp( System.currentTimeMillis()));
			
			if( code != 0)
				query.setParameter( "code", code);
			
			PagedListJSON result = generatePagedList(query, page, pageSize);
			
			for( Article article : (List<Article>) result.getEntities())
				prepareArticle( article);
			
			return result;
		}
		/* Category */
		@Override
		public PagedListJSON loadArticleCategories( Long id, Integer page, Integer pageSize)
		{
			Query query = em.createQuery( "SELECT entity FROM Article article JOIN article.categories entity WHERE article.id = :id");
			query.setParameter( "id", id);
			
			return generatePagedList(query, page, pageSize);
		}
		/* Manager - Supervisor */
		@Override
		public User loadManagerSupervisingAdmin( Long id)
		{
			try
			{
				Query query = em.createQuery( "SELECT DISTINCT admin FROM User admin JOIN admin.supervisedManagers manager WHERE manager.id = :id");
				query.setParameter( "id", id);
				
				if( query.getResultList().isEmpty())
					return null;
				return (User) query.getSingleResult();
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
		/* Admin Payments */
		@Override
		@SuppressWarnings("unchecked")
		public PagedListJSON loadAdminPayments( Long userId, Integer page, Integer pageSize, int status, boolean orderByIdAsc)
		{
			String hql = "SELECT DISTINCT entity FROM Payment entity JOIN entity.adminEAccount eAccount JOIN eAccount.user userInfo"
					+ " WHERE userInfo.id = :id";

			if( status == 1)
				hql += " AND entity.isValid is null";
			else if( status == 2)
				hql += " AND entity.isValid = true";
			else if( status == 3)
				hql += " AND entity.isValid = false";

			hql += " ORDER BY entity.id";
			if( !orderByIdAsc)
				hql += " DESC";
			
			Query query = em.createQuery( hql);
			query.setParameter( "id", userId);

			PagedListJSON result =  generatePagedList(query, page, pageSize);
			
			for( Payment entity : ( List<Payment>) result.getEntities())
			{
				entity.setUsername( entity.getUser().getUsername());
				entity.setRegDate( getRegDate( Payment.class, entity.getId()));
			}
			
			return result;
		}
		/* Admin - Orders ready for Expense */
		@Override
		@SuppressWarnings( "unchecked")
		public PagedListJSON loadAdminExpensePendingOrders( Long userId, Integer page, Integer pageSize, boolean orderByIdAsc)
		{
			Query query = em.createQuery( "SELECT DISTINCT entity FROM ArticleOrder entity JOIN entity.payments payment JOIN payment.adminEAccount adminEAccount"
					+ " JOIN adminEAccount.user userInfo"
					+ " WHERE entity.status = :status AND payment.isValid = true AND userInfo.id = :id");
			
			query.setParameter( "status", OrderStatus.DELIVERED);
			query.setParameter( "id", userId);
			
			PagedListJSON result = generatePagedList(query, page, pageSize);
			
			for( ArticleOrder entity : (List<ArticleOrder>) result.getEntities())
			{
				entity.setRegDate( getRegDate( ArticleOrder.class, entity.getId()));
			}
			
			return result;
		}
		/* Admin - Article Orders Addressed to Admin */
		@Override
		@SuppressWarnings( "unchecked")
		public PagedListJSON loadAdminArticleOrders( Long userId, Integer page,Integer pageSize,OrderStatus status,boolean orderByIdAsc)
		{
//			String hql = "SELECT DISTINCT entity FROM ArticleOrder entity JOIN entity.payments payment JOIN payment.adminEAccount adminEAccount"
//						+ " JOIN adminEAccount.user userInfo JOIN entity.expense expense WHERE userInfo.id = :id";

			String hql = "SELECT DISTINCT entity FROM ArticleOrder entity, Payment payment JOIN payment.adminEAccount adminEAccount"
						+ " JOIN adminEAccount.user userInfo JOIN entity.expense expense JOIN payment.articleOrders articleOrder WHERE userInfo.id = :id"
						+ " AND articleOrder.id = entity.id";
			
			if( !status.equals( OrderStatus.ALL))
				hql += " AND entity.status = :status";
			
			if( status.equals( OrderStatus.PENDING_EXPENSE))
			  hql += " AND payment.isValid = true AND expense is null";
			
			if( status.equals( OrderStatus.ESHOP_PAID))
				hql += " AND payment.isValid = true AND expense is not null";
			
			hql += " ORDER BY entity.id";
			if( !orderByIdAsc)
				hql += " DESC";
			
			System.err.println( hql);
			
			Query query = em.createQuery( hql);
			query.setParameter( "id", userId);
			
			if( !status.equals( OrderStatus.ALL))
				query.setParameter( "status", status);
			
			if( status.equals( OrderStatus.PENDING_EXPENSE) || status.equals( OrderStatus.ESHOP_PAID)) {
				query.setParameter( "status", OrderStatus.DELIVERED);
			}
			
			PagedListJSON result = generatePagedList(query, page, pageSize);
			
			for( ArticleOrder entity : (List<ArticleOrder>) result.getEntities())
			{
				entity.setRegDate( getRegDate( ArticleOrder.class, entity.getId()));
			}
			
			return result;
		}
	/* End User */

	/* AdvOffer */
	@SuppressWarnings("unchecked")
	@Override
	public PagedListJSON loadAdvOffers(Integer page, Integer pageSize, GenericStatus status, boolean orderByIdAsc)
	{
		String hql = "SELECT entity FROM AdvOffer entity";
		if( status != GenericStatus.ALL)
			hql += " WHERE status = :status";
		hql += " ORDER BY entity.id";
		if( !orderByIdAsc)
			hql += " DESC";
		
		Query query = em.createQuery( hql);
		if( status != GenericStatus.ALL)
			query.setParameter( "status", status);
		
		PagedListJSON result = generatePagedList(query, page, pageSize);
		
		for( AdvOffer advOffer : ( List<AdvOffer>) result.getEntities())
		{
			advOffer.setRegDate( getRegDate( AdvOffer.class, advOffer.getId()));
		}
		
		return result;
	}
	@Override
	public Long addAdvOffer( AdvOffer entity)
	{
		entity.setEndDate();
		em.persist( entity);
		
		return entity.getId();
	}
	@Override
	public void updateAdvOffer(AdvOffer entity)
	{
		try
		{
			em.merge( entity);	
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
	}
	@Override
	public AdvOffer loadAdvOffer(Long id)
	{
		AdvOffer entity = em.find( AdvOffer.class, id);
		entity.setRegDate( getRegDate( AdvOffer.class, id));
		
		return entity;
	}
		/* Payments */
		@Override
		@SuppressWarnings("unchecked")
		public PagedListJSON loadAdvOfferPayments( Long id, Integer page, Integer pageSize, boolean orderByIdAsc)
		{
			String hql =  "SELECT entity FROM AdvOffer advOffer JOIN advOffer.payments entity WHERE advOffer.id = :id"
					+ " ORDER BY entity.id";
			Query query = em.createQuery( hql);
			
			if( !orderByIdAsc)
				hql += " DESC";
			
			query.setParameter( "id", id);
			
			PagedListJSON result = generatePagedList(query, page, pageSize);
			
			for( Payment payment : (List<Payment>) result.getEntities())
			{
				payment.setRegDate( getRegDate( Payment.class, payment.getId()));
			}
			
			return result;
		}
		/* EShop */
		@Override
		public EShop loadAdvOfferEshop( Long id)
		{
			Query query = em.createQuery( "SELECT DISTINCT entity FROM AdvOffer advOffer JOIN advOffer.article article"
					+ " JOIN article.eShop entity WHERE advOffer.id = :id");
			query.setParameter( "id", id);
			
			return (EShop) query.getSingleResult();
		}
		/* Manager */
		@Override
		public User loadAdvOfferManager( Long id)
		{
			EShop eShop = loadAdvOfferEshop( id);
			return loadEShopManager(eShop.getId());
		}
	/* Strict */
	@SuppressWarnings("unchecked")
	@Override
	public PagedListJSON loadAdvOffersStrict(Integer page, Integer pageSize, GenericStatus status, boolean orderByIdAsc)
	{
		String hql = "SELECT entity FROM AdvOffer entity JOIN entity.article article JOIN article.eShop eShop JOIN eShop.currentShopSub shopSub"
				+ " WHERE :now BETWEEN shopSub.startDate AND shopSub.endDate";
		if( status != GenericStatus.ALL)
			hql += " WHERE status = :status";
		hql += " ORDER BY entity.id";
		if( !orderByIdAsc)
			hql += " DESC";
		
		Query query = em.createQuery( hql);
		if( status != GenericStatus.ALL)
			query.setParameter( "status", status);
		
		PagedListJSON result = generatePagedList(query, page, pageSize);
		
		for( AdvOffer advOffer : ( List<AdvOffer>) result.getEntities())
		{
			advOffer.setRegDate( getRegDate( AdvOffer.class, advOffer.getId()));
		}
		
		return result;
	}
	/* End AdvOffer */
	
	/* AdvOption */
	@Override
	public PagedListJSON loadAdvOptions( Integer page, Integer pageSize)
	{
		Query query = em.createQuery( "SELECT advOption FROM AdvOption advOption WHERE isDeleted = false");
		
		return generatePagedList(query, page, pageSize);
	}
	@Override
	public Long addAdvOption(AdvOption entity)
	{
		em.persist( entity);

		return entity.getId();
	}
	@Override
	public void editAdvOption(AdvOption entity)
	{
		em.merge( entity);
	}
	@Override
	public void deleteAdvOption(Long id)
	{
		AdvOption entity = em.find( AdvOption.class, id);

		em.remove( entity);
	}
	@Override
	public AdvOption loadAdvOption(Long id)
	{
		AdvOption entity = em.find( AdvOption.class, id);
		if( entity == null || entity.getIsDeleted())
			return null;

		return entity;
	}
	@Override
	public AdvOption loadAdvOption( String title)
	{
		Query query = em.createQuery( "SELECT entity FROM AdvOption entity WHERE entity.title = :title"
				+ " AND entity.isDeleted = false");
		AdvOption entity = (AdvOption) query.getSingleResult();
		
		return entity;
	}
	public boolean isAdvOptionRegistered( String title)
	{
		Query query = em.createQuery( "SELECT entity FROM AdvOption entity WHERE entity.title = :title");
		query.setParameter( "title", title);
		
		if( query.getResultList().isEmpty())
			return false;
		else
			return true;
	}
	/* End AdvOption */
	
	/* Articles */
	@Override
	@SuppressWarnings("unchecked")
	public PagedListJSON listRecentArticlesByEShop(Long eShopId, Integer page, Integer pageSize)
	{
		try
		{
			Query query = em.createQuery( "SELECT DISTINCT article FROM Article article JOIN article.eShop eShop WHERE eShop.id = :eShopId"
					   + " AND article.isDeleted = false ORDER BY article.id DESC");
			query.setParameter( "eShopId", eShopId);
			
			PagedListJSON result = new PagedListJSON();
			
			List<Article> entities = query.getResultList();
			result.setItemsNumber( entities.size());
			result.setPagesNumber( computePagesNumber( entities.size(), pageSize));
			
			query.setFirstResult( (page - 1) * pageSize);
			query.setMaxResults( pageSize);
			result.setEntities( query.getResultList());
			
			for( Article entity : (List<Article>) result.getEntities())
			{
				entity.setSaleNumber( this.getArticleSaleNumber( entity.getId()));
				entity.setRating( this.getArticleRating( entity.getId()));
			}
			
			return result;
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
	}
	@Override
	@SuppressWarnings( "unchecked")
	public PagedListJSON listTopArticlesByEShop( Long eShopId, Integer page, Integer pageSize)
	{
		try
		{
			Query query = em.createQuery( "SELECT article FROM Article article, OrderedArticle orderedArticle JOIN orderedArticle.article entity"
					+ " JOIN orderedArticle.articleOrder articleOrder JOIN article.eShop eShop"
					+ " WHERE article.id = entity.id AND articleOrder.status = :status AND article.isDeleted = false AND eShop.id = :eShopId GROUP BY article"
					+ " ORDER BY AVG( orderedArticle.rating) DESC");
			query.setParameter( "eShopId", eShopId);
			query.setParameter( "status", OrderStatus.DELIVERED);
			
			PagedListJSON result = new PagedListJSON();
			
			List<Article> entities = query.getResultList();
			result.setItemsNumber( entities.size());
			result.setPagesNumber( computePagesNumber( entities.size(), pageSize));

			query.setFirstResult( (page - 1) * pageSize);
			query.setMaxResults( pageSize);
			result.setEntities( query.getResultList());
			
			for( Article entity : (List<Article>) result.getEntities())
			{
				entity.setSaleNumber( this.getArticleSaleNumber( entity.getId()));
				entity.setRating( this.getArticleRating( entity.getId()));
			}
			
			return result;
			
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	@Override
	@SuppressWarnings( "unchecked")
	public PagedListJSON listSimilarArticle( Long articleId, Integer page, Integer pageSize)
	{
		try
		{
			Article article = this.loadArticle( articleId);
			
			/* TODO Similar article with keyword too */
			Query query = em.createQuery( "SELECT DISTINCT entity FROM Article entity WHERE entity.id != :articleId "
										   + "AND entity.name like '%" + article.getName().split(" ")[0] + "%' AND entity.isDeleted = false ");
			query.setParameter( "articleId", article.getId());
			
			PagedListJSON result = new PagedListJSON();

			List<Article> entities = query.getResultList();
			result.setItemsNumber( entities.size());
			result.setPagesNumber( computePagesNumber( entities.size(), pageSize));

			query.setFirstResult( (page - 1) * pageSize);
			query.setMaxResults( pageSize);
			result.setEntities( query.getResultList());
			
//			lem.getTransaction().commit();
			
			for( Article entity : (List<Article>) result.getEntities())
			{
				entity.setSaleNumber( this.getArticleSaleNumber( entity.getId()));
				entity.setRating( this.getArticleRating( entity.getId()));
			}
			
			return result;
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
	}
	@Override
	@SuppressWarnings( "unchecked")
	public PagedListJSON listBestSellerArticlesByEShop( Long eShopId, Integer page, Integer pageSize)	{
		try
		{
			Query query = em.createQuery( "SELECT article FROM Article article, OrderedArticle orderedArticle JOIN orderedArticle.article entity"
					+ " JOIN orderedArticle.articleOrder articleOrder JOIN article.eShop eShop"
					+ " WHERE article.id = entity.id AND articleOrder.status = :status AND eShop.id = :eShopId GROUP BY article"
					+ " ORDER BY COUNT( entity.id) DESC");
			query.setParameter( "eShopId", eShopId);
			query.setParameter( "status", OrderStatus.DELIVERED);
			
			PagedListJSON result = new PagedListJSON();
			
			List<Article> entities = query.getResultList();
			result.setItemsNumber( entities.size());
			result.setPagesNumber( computePagesNumber( entities.size(), pageSize));

			query.setFirstResult( (page - 1) * pageSize);
			query.setMaxResults( pageSize);
			result.setEntities( query.getResultList());
			
			for( Article entity : (List<Article>) result.getEntities())
			{
				entity.setSaleNumber( this.getArticleSaleNumber( entity.getId()));
				entity.setRating( this.getArticleRating( entity.getId()));
			}
			
			return result;	
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	@Override
	@SuppressWarnings( "unchecked")
	public PagedListJSON loadLatestArticles( Integer page, Integer pageSize)
	{
		try
		{
			Query query = em.createQuery("SELECT article FROM Article article WHERE isDeleted = false ORDER BY article.id DESC");
			
			PagedListJSON result = generatePagedList(query, page, pageSize);
			
			for( Article entity : (List<Article>) result.getEntities())
			{
				entity.setSaleNumber( this.getArticleSaleNumber( entity.getId()));
				entity.setRating( this.getArticleRating( entity.getId()));
			}
			
			return result;
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public Article loadArticle(Long id)
	{
		try
		{
			Article entity = em.find( Article.class, id);
			
			/*
			 * TODO CHeck if EShop has a valid EShop
			 */
			
			if( entity == null || entity.getIsDeleted())
				return null;
			
			entity.setRating( this.getArticleRating( entity.getId()));
			entity.setSaleNumber( this.getArticleSaleNumber( entity.getId()));
			
			return entity;
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public Long addArticle(Article entity)
	{	
		em.persist( entity);

		return entity.getId();
	}
	@Override
	public void updateArticle(Article entity)
	{
		em.merge( entity);
	}
	@Override
	public void deleteArticle(Long id)
	{
		Article entity = em.find( Article.class, id);

		entity.setIsDeleted( true);
		
		em.merge( entity);
	}
	@Override
	public Picture getDefaultPicture(Long id)
	{
		Article article = this.loadArticle(id);
		
		if( !article.getPictures().isEmpty())
		{
			for( Picture picture : article.getPictures())
			{
				if( picture.getIsDefault())
					return picture;
			}
		}
		
		return null;
	}
	@Override
	public Integer getArticleRating( Long id)
	{
		Query query = em.createQuery( "SELECT AVG( entity.rating) FROM OrderedArticle entity JOIN entity.article article "
				+ "WHERE article.id = :id" );
		query.setParameter( "id", id);
		if( query.getSingleResult() == null)
			return 0;
		
		Double ratingDouble = (Double) query.getSingleResult();

		Integer rating = ratingDouble.intValue();
		
		return rating;
	}
	@Override
	public Long getArticleSaleNumber( Long id)
	{
		Query query = em.createQuery( "SELECT COUNT( entity) FROM OrderedArticle entity JOIN entity.article article JOIN entity.articleOrder articleOrder WHERE article.id = :id"
				+ " AND articleOrder.status = :status");
		query.setParameter( "id", id);
		query.setParameter( "status", OrderStatus.DELIVERED);
		
		Long saleNumber = (Long) query.getSingleResult();
		
		return saleNumber;
	}
	@Override
	@SuppressWarnings( "unchecked")
	public PagedListJSON loadTopArticles( Integer page, Integer pageSize)
	{
		Query query = em.createQuery( "SELECT article FROM Article article, OrderedArticle orderedArticle JOIN orderedArticle.article entity"
				+ " JOIN orderedArticle.articleOrder articleOrder"
				+ " WHERE article.id = entity.id AND articleOrder.status = :status AND article.isDeleted = false GROUP BY article"
				+ " ORDER BY AVG( orderedArticle.rating) DESC");
		query.setParameter( "status", OrderStatus.DELIVERED);
		
		PagedListJSON result = generatePagedList(query, page, pageSize);
		
		for( Article entity : (List<Article>) result.getEntities())
		{
			entity.setSaleNumber( this.getArticleSaleNumber( entity.getId()));
			entity.setRating( this.getArticleRating( entity.getId()));
		}
		
		return result;
	}
	@Override
	@SuppressWarnings( "unchecked")
	public PagedListJSON loadBestSellers( Integer page, Integer pageSize)
	{
		Query query = em.createQuery( "SELECT article FROM Article article, OrderedArticle orderedArticle JOIN orderedArticle.article entity"
				+ " JOIN orderedArticle.articleOrder articleOrder"
				+ " WHERE article.id = entity.id AND articleOrder.status = :status GROUP BY article"
				+ " ORDER BY COUNT( entity.id) DESC");
		query.setParameter( "status", OrderStatus.DELIVERED);
		
		PagedListJSON result = generatePagedList(query, page, pageSize);
		
		for( Article entity : (List<Article>) result.getEntities())
		{
			entity.setRating( getArticleRating( entity.getId()));
			entity.setSaleNumber( getArticleSaleNumber( entity.getId()));
		}
		
		return result;
	}
	@SuppressWarnings("unchecked")
	@Override
	public PagedListJSON searchArticle( String name, Integer page, Integer pageSize, Float minPrice, Float maxPrice)
	{	
		Query query = em.createQuery( "SELECT DISTINCT entity FROM Article entity JOIN entity.keywords keywordx  JOIN entity.eShop eShop"
				+ " JOIN eShop.currentShopSub shopSub"
				+ " WHERE entity.name like '%" + name + "%'"
				+ " OR keywordx like '%" + name + "%' AND entity.isDeleted = false"
				+ " AND entity.price between :minPrice AND :maxPrice AND :now BETWEEN shopSub.startDate AND shopSub.endDate");
		
		query.setParameter( "minPrice", minPrice);
		query.setParameter( "maxPrice", maxPrice);
		query.setParameter( "now", new Timestamp( System.currentTimeMillis()));
		
		List<Article> entities = query.getResultList();
		
		PagedListJSON result = new PagedListJSON();
		result.setPagesNumber( computePagesNumber( entities.size(), pageSize) );
		result.setItemsNumber( entities.size());
		
		query.setFirstResult( (page - 1) * pageSize);
		query.setMaxResults( pageSize);
		
		result.setEntities( query.getResultList());
		
		for( Article entity : (List<Article>) result.getEntities())
		{
			entity.setSaleNumber( this.getArticleSaleNumber( entity.getId()));
			entity.setRating( this.getArticleRating( entity.getId()));
		}
		
		return result;
	}
	@Override
	@SuppressWarnings( "unchecked")
	public PagedListJSON loadArticlesByCategory( Long categoryId, Integer page, Integer pageSize)
	{
		Query query = em.createQuery( "SELECT DISTINCT entity FROM Article entity JOIN entity.categories categories"
				+ " WHERE categories = :category");
		query.setParameter( "category", this.loadCategory( categoryId));
		
		List<Article> entities = query.getResultList();
		
		PagedListJSON result = new PagedListJSON();
		result.setItemsNumber( entities.size());
		result.setPagesNumber( computePagesNumber( entities.size(), pageSize));

		
		query.setFirstResult( (page - 1) * pageSize);
		query.setMaxResults( pageSize);
		
		result.setEntities( query.getResultList());
		
		for( Article entity : (List<Article>) result.getEntities())
		{
			entity.setRating( getArticleRating( entity.getId()));
			entity.setSaleNumber( getArticleSaleNumber( entity.getId()));
		}
		
		return result;
	}
	@SuppressWarnings("unchecked")
	@Override
	public PagedListJSON loadDiscountedArticles( Integer page, Integer pageSize)
	{
		Query query = em.createQuery( "SELECT entity FROM Article entity WHERE isDiscounted = true");
		
		PagedListJSON result = generatePagedList(query, page, pageSize);
		

		for( Article entity : (List<Article>) result.getEntities())
		{
			entity.setSaleNumber( this.getArticleSaleNumber( entity.getId()));
			entity.setRating( this.getArticleRating( entity.getId()));
		}
		
		return result;
	}
		/* Article Features */
		@Override
		public PagedListJSON listArticleFeaturesByArticle( Long articleId, Integer page, Integer pageSize)
		{
			Query query = em.createQuery( "SELECT entity FROM Article article JOIN article.features entity"
					+ " WHERE article.id = :articleId");
			query.setParameter( "articleId", articleId);
			
			return generatePagedList(query, page, pageSize);
		}
		/* Categories */
		@Override
		public PagedListJSON listCategoriesByArticle( Long articleId, Integer page, Integer pageSize)
		{
			Query query = em.createQuery( "SELECT entity FROM Article article LEFT JOIN article.categories entity"
					+ " WHERE article.id = :articleId");
			query.setParameter( "articleId", articleId);
			
			return generatePagedList(query, page, pageSize);
		}
		/* Pictures */
		@Override
		public PagedListJSON listPicturesByArticle( Long articleId, Integer page, Integer pageSize)
		{
			Query query = em.createQuery( "SELECT entity FROM Article article RIGHT JOIN article.pictures entity"
					+ " WHERE article.id = :articleId");
			query.setParameter( "articleId", articleId);
			
			return generatePagedList(query, page, pageSize);
		}
		@Override
		public Picture getArticleDefaultPicture( Long id)
		{
			Query query = em.createQuery( "SELECT entity FROM Article article LEFT JOIN article.pictures entity"
					+ " WHERE article.id = :articleId AND entity.isDefault = true");
			query.setParameter( "articleId", id);
			
			return (Picture) query.getSingleResult();
		}
		/* EShop */
		@Override
		public EShop loadArticleEShop( Long id)
		{
			try
			{
				Article entity = loadArticle( id);
				
				return entity.geteShop();
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
		/* Stats */
		@Override
		public Float getMinPrice()
		{
			Query query = em.createQuery( "SELECT MIN(entity.price) FROM Article entity WHERE entity.isDeleted = false");
			
			return (Float) query.getSingleResult();
		}
		@Override
		public Float getMaxPrice()
		{
			Query query = em.createQuery( "SELECT MAX( entity.price) FROM Article entity WHERE entity.isDeleted = false");
			
			return ( Float) query.getSingleResult();
		}
		/* AdvOffers */
		@Override
		public PagedListJSON loadArticleAdvOffers( Long id, Integer page, Integer pageSize, GenericStatus status, boolean orderByIdAsc)
		{
			String hql = "SELECT entity FROM AdvOffer entity JOIN entity.article article WHERE article.id = :id";
			if( status != GenericStatus.ALL)
				hql += " AND entity.status = :status";
			hql += " ORDER BY entity.id";
			
			if( !orderByIdAsc)
				hql += " DESC";
			
			Query query = em.createQuery( hql);
			query.setParameter( "id", id);
			
			if( status !=GenericStatus.ALL)
				query.setParameter("status", status);
			
			return generatePagedList(query, page, pageSize);
		}
	/* Strict */
	@SuppressWarnings("unchecked")
	@Override
	public PagedListJSON loadCategoryArticlesStrict( Long categoryId, Integer page, Integer pageSize)
	{
		Query query = em.createQuery( "SELECT DISTINCT entity FROM Article entity JOIN entity.eShop eShop JOIN eShop.currentShopSub shopSub JOIN entity.categories categories"
				+ " WHERE categories = :category AND :now BETWEEN shopSub.startDate AND shopSub.endDate ORDER BY entity.id ASC");
		query.setParameter( "category", this.loadCategory( categoryId));
		query.setParameter( "now", System.currentTimeMillis());
		
		PagedListJSON result = generatePagedList(query, page, pageSize);
		
		result.setEntities( query.getResultList());
		
		for( Article entity : (List<Article>) result.getEntities())
		{
			entity.setRating( getArticleRating( entity.getId()));
			entity.setSaleNumber( getArticleSaleNumber( entity.getId()));
		}
		
		return result;
	}
	@Override
	@SuppressWarnings("unchecked")
	public PagedListJSON loadLatestArticlesStrict( Integer page, Integer pageSize)
	{
		Query query = em.createQuery("SELECT article FROM Article article JOIN article.eShop eShop JOIN eShop.currentShopSub shopSub"
					+ " WHERE article.isDeleted = false AND :now BETWEEN shopSub.startDate AND shopSub.endDate ORDER BY article.id DESC");
		query.setParameter( "now", new Timestamp( System.currentTimeMillis()));
		
		PagedListJSON result = generatePagedList(query, page, pageSize);
		
		for( Article entity : (List<Article>) result.getEntities())
		{
			entity.setSaleNumber( this.getArticleSaleNumber( entity.getId()));
			entity.setRating( this.getArticleRating( entity.getId()));
		}
		
		return result;
	}
	@Override
	@SuppressWarnings( "unchecked")
	public PagedListJSON loadTopArticlesStrict( Integer page, Integer pageSize)
	{
		Query query = em.createQuery( "SELECT article FROM Article article, OrderedArticle orderedArticle JOIN article.eShop eShop JOIN eShop.currentShopSub shopSub JOIN orderedArticle.article entity"
				+ " JOIN orderedArticle.articleOrder articleOrder"
				+ " WHERE article.id = entity.id AND articleOrder.status = :status AND article.isDeleted = false"
				+ " AND :now BETWEEN shopSub.startDate AND shopSub.endDate GROUP BY article"
				+ " ORDER BY AVG( orderedArticle.rating) DESC");
		
		query.setParameter( "status", OrderStatus.DELIVERED);
		query.setParameter( "now", new Timestamp( System.currentTimeMillis()));
		
		PagedListJSON result = generatePagedList(query, page, pageSize);
		
		for( Article entity : (List<Article>) result.getEntities())
		{
			entity.setSaleNumber( this.getArticleSaleNumber( entity.getId()));
			entity.setRating( this.getArticleRating( entity.getId()));
		}
		
		return result;
	}
	@Override
	@SuppressWarnings("unchecked")
	public PagedListJSON loadBestSellersStrict( Integer page, Integer pageSize)
	{
		Query query = em.createQuery( "SELECT article FROM Article article, OrderedArticle orderedArticle JOIN article.eShop eShop JOIN eShop.currentShopSub shopSub JOIN orderedArticle.article entity"
				+ " JOIN orderedArticle.articleOrder articleOrder"
				+ " WHERE article.id = entity.id AND articleOrder.status = :status "
				+ " AND :now BETWEEN shopSub.startDate AND shopSub.endDate GROUP BY article"
				+ " ORDER BY COUNT( entity.id) DESC");
		query.setParameter( "status", OrderStatus.DELIVERED);
		query.setParameter( "now", new Timestamp( System.currentTimeMillis()));
		
		PagedListJSON result = generatePagedList(query, page, pageSize);
		
		for( Article entity : (List<Article>) result.getEntities())
		{
			entity.setRating( getArticleRating( entity.getId()));
			entity.setSaleNumber( getArticleSaleNumber( entity.getId()));
		}
		
		return result;
	}
	@Override
	@SuppressWarnings("unchecked")
	public PagedListJSON loadDiscountedArticlesStrict( Integer page, Integer pageSize)
	{
		Query query = em.createQuery( "SELECT DISTINCT entity FROM Article entity JOIN entity.eShop eShop JOIN eShop.currentShopSub shopSub"
				+ " WHERE entity.isDiscounted = true AND :now BETWEEN shopSub.startDate AND shopSub.endDate");
		query.setParameter( "now", new Timestamp( System.currentTimeMillis()));
		
		PagedListJSON result = generatePagedList(query, page, pageSize);

		for( Article entity : (List<Article>) result.getEntities())
		{
			entity.setSaleNumber( this.getArticleSaleNumber( entity.getId()));
			entity.setRating( this.getArticleRating( entity.getId()));
		}
		
		return result;
	}
	@SuppressWarnings("unchecked")
	@Override
	public PagedListJSON searchArticleStrict( String name, Integer page, Integer pageSize, Float minPrice, Float maxPrice)
	{	
		Query query = em.createQuery( "SELECT DISTINCT entity FROM Article entity JOIN entity.keywords keywordx JOIN entity.eShop eShop"
				+ " JOIN eShop.currentShopSub shopSub"
				+ " WHERE entity.name like '%" + name + "%'"
				+ " OR keywordx like '%" + name + "%' AND entity.isDeleted = false"
				+ " AND entity.price between :minPrice AND :maxPrice AND :now BETWEEN shopSub.startDate AND shopSub.endDate");
		
		query.setParameter( "minPrice", minPrice);
		query.setParameter( "maxPrice", maxPrice);
		query.setParameter( "now", new Timestamp( System.currentTimeMillis()));
		
		List<Article> entities = query.getResultList();
		
		PagedListJSON result = new PagedListJSON();
		result.setPagesNumber( computePagesNumber( entities.size(), pageSize) );
		result.setItemsNumber( entities.size());
		
		query.setFirstResult( (page - 1) * pageSize);
		query.setMaxResults( pageSize);
		
		result.setEntities( query.getResultList());
		
		for( Article entity : (List<Article>) result.getEntities())
		{
			entity.setSaleNumber( this.getArticleSaleNumber( entity.getId()));
			entity.setRating( this.getArticleRating( entity.getId()));
		}
		
		return result;
	}
	/* End Articles */
	
	/* Cart */
	@Override
	public CartItem loadCartItem( Long id)
	{
		CartItem entity = em.find( CartItem.class, id);
		
		return entity;
	}
	@Override
	public void addCartItem( CartItem entity)
	{
		em.persist( entity);
	}
	@Override
	public void updateCartItem( CartItem entity)
	{
		em.merge( entity);
	}
	@Override
	public void removeCartItem(Long id)
	{
		CartItem entity = em.find( CartItem.class, id);
		entity.setIsDeleted( true);
	}
	/* End Cart */
	
	/* Category */
	@Override
	public PagedListJSON loadCategories( Integer page, Integer pageSize)
	{
		Query query = em.createQuery( "SELECT entity FROM Category entity WHERE isDeleted = false");
		
		return generatePagedList(query, page, pageSize);
	}
	@Override
	public Long addCategory(Category entity)
	{
		if( entity.getRoot() != null)
			entity.setLevel( entity.getRoot().getLevel()+1);

		em.persist( entity);

		return entity.getId();
	}
	@Override
	public void updateCategory(Category entity)
	{
		em.merge( entity);
	}
	@Override
	public void deleteCategory(Long id)
	{
		Category entity = em.find( Category.class, id);
		
		if( entity != null) {
			boolean areLeavesDeleted = true;
			for( Category leaf : entity.getLeaves())
			{
				if( !leaf.getIsDeleted())
					areLeavesDeleted = false;
			}
			if( areLeavesDeleted) 
			{
				entity.setIsDeleted( true);
				em.merge( entity);
			}
			
		}
	}
	@Override
	public Category loadCategory(Long id)
	{
		Category entity = em.find( Category.class, id);
		
		if( entity == null || entity.getIsDeleted())
			return null;
		return entity;
	}
	@Override
	public PagedListJSON loadCategories( Integer level, Integer page, Integer pageSize)
	{
		Query query = em.createQuery( "SELECT entity FROM Category entity WHERE entity.isDeleted = false AND entity.level = :level");
		query.setParameter( "level", level);
		
		return generatePagedList(query, page, pageSize);
	}
	/* End Category */
	
	/* Feature */
	@Override
	public Feature getFeature( Long id)
	{
		Feature entity = em.find( Feature.class, id);
		
		return entity;
	}
	@Override
	public Long addFeature(Feature entity)
	{
		em.persist( entity);
		
		return entity.getId();
	}
	@Override
	public Feature getFeatureByLabel(String label)
	{
		Query query = em.createQuery( "SELECT feature FROM Feature feature WHERE label = :label");
		query.setParameter( "label", label);

		if( query.getResultList().isEmpty())
			return null;

		return (Feature) query.getSingleResult();
	}
	@Override
	public Long addArticleFeature(ArticleFeature entity)
	{	
		em.persist( entity);
		
		return entity.getId();
	}
		/* TODO Wrong place;Relocate */
		/* Account for payment */
		@Override
		public PagedListJSON loadAdminEAccountsForPayments( Integer page, Integer pageSize)
		{
			Query query = em.createQuery( "SELECT entity FROM User admin JOIN admin.userInfo userInfo JOIN userInfo.eAccounts entity JOIN admin.roles roles WHERE roles = :role");
			query.setParameter( "role", loadRole( "ROLE_ADMIN"));
			
			return generatePagedList(query, page, pageSize);
		}
	/* End Feature */
	
	/* Feature Value */
	@Override
	public Long addFeatureValue( FeatureValue entity)
	{
		em.persist( entity);
		
		return entity.getId();
	}
	@Override
	public void removeFeatureValue( Long id)
	{
		FeatureValue entity = em.find( FeatureValue.class, id);
		em.remove( entity);
	}
	@Override
	public FeatureValue getFeatureValue( Long id)
	{
		FeatureValue entity = em.find( FeatureValue.class, id);
		em.close();
		
		return entity;
	}
	@Override
	public FeatureValue getFeatureValue(String value)
	{
		Query query = em.createQuery( "SELECT DISTINCT entity FROM FeatureValue entity WHERE entity.value = :value");
		query.setParameter( "value", value);
		
		if( query.getResultList().size() <= 0)
			return null;
		
		return (FeatureValue) query.getSingleResult();
	}
	/* End Feature Value */
	
	/* ArticleFeature */
	public ArticleFeature getArticleFeature( Long id)
	{
		return em.find( ArticleFeature.class, id);
	}
	/* End ArticleFeature */
	
	/* Picture */
	@Override
	public Long addPicture(Picture entity)
	{
		em.persist( entity);
		return entity.getId();
	}
	@Override
	public void deletePicture(Long id)
	{
		Picture entity = em.find( Picture.class, id);
		if( entity != null)
		{
			entity.setIsDeleted( true);
			em.merge( entity);
		}
	}
	@Override
	public Picture getPicture(Long id)
	{
		Picture entity = em.find( Picture.class, id);
		
		return entity;
	}
	/* Picture */
	
	/* Payment */
	@Override
	@SuppressWarnings("unchecked")
	@Description( "Status: 0 => All; 1 => Pending; 2 => Accepted; 3 => Rejected")
	public PagedListJSON loadPayments( Integer page, Integer pageSize, int status, boolean orderByIdAsc)
	{
		String hql = "SELECT entity FROM Payment entity";
		if( status == 1)
			hql += " WHERE entity.isValid is null";
		else if( status == 2)
			hql += " WHERE entity.isValid = true";
		else if( status == 3)
			hql += " WHERE entity.isValid = false";
		
		hql += " ORDER BY entity.id ";
		if( !orderByIdAsc)
			hql += " DESC";
		
		Query query = em.createQuery( hql);
		
		PagedListJSON result =  generatePagedList(query, page, pageSize);
		
		for( Payment entity : ( List<Payment>) result.getEntities())
		{
			entity.setUsername( entity.getUser().getUsername());
			entity.setRegDate( getRegDate( Payment.class, entity.getId()));
		}
		
		return result;
	}
	@Override
	public Payment loadPayment( Long id)
	{
		Payment entity = em.find(Payment.class, id);
		
		entity.setUsername( entity.getUser().getUsername());
		entity.setRegDate( getRegDate( Payment.class, entity.getId()));
		
		return entity;
	}
	@Override
	public void addPayment( Payment entity, Long eAccountId, Long adminEAccountId)
	{
		try
		{
			entity.setAdminEAccount( em.find( EAccount.class, adminEAccountId));
			entity.seteAccount( em.find( EAccount.class, eAccountId));
			
			em.persist( entity);
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
	}
	@Override
	public void updatePayment( Payment entity)
	{
		em.merge( entity);
	}
	@Override
	@Description( "Status: 0 => All; 1 => Pending; 2 => Accepted; 3 => Rejected")
	public PagedListJSON loadUserPayments( Long userId, Integer page, Integer pageSize, int status, boolean orderByIdAsc)
	{
		String hql = "SELECT entity FROM Payment entity JOIN entity.user user JOIN user.userInfo userInfo"
					+ " WHERE userInfo.id = :id";

		if( status == 1)
			hql += " AND entity.isValid is null";
		else if( status == 2)
			hql += " AND entity.isValid == true";
		else if( status == 3)
			hql += " AND entity.isValid == false";
		
		hql += " ORDER BY entity.id";
		
		if( !orderByIdAsc)
			hql += " DESC";
		
		Query query = em.createQuery( hql);
		query.setParameter( "id", userId);
		
		return generatePagedList( query, page, pageSize);
	}
		/* Article Orders */
		@Override
		public PagedListJSON loadPaymentArticleOrders( Long id, Integer page, Integer pageSize)
		{
			Query query = em.createQuery ("SELECT entity FROM Payment payment JOIN payment.articleOrders entity WHERE payment.id = :id");
			query.setParameter( "id", id);
			
			return generatePagedList(query, page, pageSize);
		}
		/* Shop Sub */
		@Override
		public ShopSub loadPaymentShopSub( Long id)
		{
			Query query = em.createQuery("SELECT entity FROM Payment payment JOIN payment.shopSub entity WHERE payment.id = :id");
			query.setParameter( "id", id);
			
			if( query.getResultList().isEmpty())
				return null;
			
			return (ShopSub) query.getSingleResult();
			//return getPayment( id).getShopSub();
		}
		/* AdvOffer */
		@Override
		public AdvOffer loadPaymentAdvOffer( Long id)
		{
			return loadPayment(id).getAdvOffer();
		}
	/* End Payment */
	
	/* ArticleOrders */
	@Override
	public ArticleOrder loadArticleOrder(Long id)
	{
		ArticleOrder entity = em.find( ArticleOrder.class, id);
		if( entity == null)
			return null;
		
		entity.setRegDate( this.getRegDate(ArticleOrder.class, id));
		
		return entity;
	}
	@Override
	public void addArticleOrder(ArticleOrder entity)
	{	
		em.persist( entity);
	}
	@Override
	public void updateArticleOrder(ArticleOrder entity)
	{	
		em.merge( entity);
	}
	@Override
	@SuppressWarnings( "unchecked")
	public PagedListJSON loadArticleOrders(Integer page,Integer pageSize,OrderStatus status,boolean orderByIdAsc)
	{
		String hql = "SELECT DISTINCT entity FROM ArticleOrder entity";
		
		if( !status.equals( OrderStatus.ALL) || !status.equals( OrderStatus.ESHOP_PAID) || !status.equals( OrderStatus.PENDING_EXPENSE))
			hql += " WHERE entity.status = :orderStatus";
		
		hql += " ORDER BY entity.id";
		if( !orderByIdAsc)
			hql += " DESC";
		
		Query query = em.createQuery( hql);
		
		if( !status.equals( OrderStatus.ALL) || !status.equals( OrderStatus.ESHOP_PAID) || !status.equals( OrderStatus.PENDING_EXPENSE))
			query.setParameter( "orderStatus", status);
		
		PagedListJSON result = generatePagedList(query, page, pageSize);
		
		for( ArticleOrder entity : (List<ArticleOrder>) result.getEntities())
		{
			entity.setRegDate( getRegDate( ArticleOrder.class, entity.getId()));
		}
		
		return result;
	}
	public Timestamp getArticleOrderRegDate( Long id)
	{
		ArticleOrder entity = this.loadArticleOrder( id);
		if( entity == null)
			return null;
		
		AuditReader reader = AuditReaderFactory.get( em);
		
		Number revNum = ( Number) reader.createQuery()
										  .forRevisionsOfEntity( ArticleOrder.class, false,true)
										  .addProjection( AuditEntity.revisionNumber().min())
										  .add(AuditEntity.revisionType().eq(RevisionType.ADD))
										  .add( AuditEntity.id().eq(id))
										  .getSingleResult();
		
		RevEntity rev = em.find( RevEntity.class, revNum);
		
		return new Timestamp( rev.getTimestamp());
	}
	@Override
	@SuppressWarnings( "unchecked")
	public List<ArticleOrder> listArticleOrderByEShop( Long eShopId)
	{
		Query query = em.createQuery( "SELECT entity FROM ArticleOrder entity JOIN entity.orderedArticles orderedArticles JOIN orderedArticles.article article JOIN "
				+ "article.eShop eShop WHERE eShop.id = :eShopId");
		query.setParameter("eShopId", eShopId);
		List<ArticleOrder> entities = query.getResultList();
		
		return entities;
	}
	@Override
	@SuppressWarnings( "unchecked")
	public List<ArticleOrder> listArticleOrderByStatus( OrderStatus status)
	{	
		Query query = em.createQuery( "SELECT entity FROM ArticleOrder entity WHERE entity.status = :status");
		query.setParameter( "status", status);
		
		List<ArticleOrder> entities = query.getResultList();
		for( ArticleOrder entity : entities)
		{
			entity.setRegDate( this.getRegDate( ArticleOrder.class, entity.getId()));
		}
		
		return entities;
	}
	@Override
	public User getArticleOrderUser( Long id) 
	{
		Query query = em.createQuery( "SELECT entity.user FROM ArticleOrder entity JOIN entity.user WHERE entity.id = :id");
		query.setParameter( "id", id);
		User entity = (User) query.getSingleResult();
		
		return entity;
	}
	@Override
	public EShop loadArticleOrderEShop( Long id)
	{
		Query query = em.createQuery( "SELECT DISTINCT entity FROM EShop eShop, ArticleOrder articleOrder JOIN articleOrder.orderedArticles orderedArticles "
									  + "JOIN orderedArticles.article article JOIN article.eShop entity WHERE articleOrder.id = :id");
		query.setParameter( "id", id);
		
		if( query.getResultList().size() == 0)
			return null;
		
		EShop entity = (EShop) query.getSingleResult();
		
		return entity;
	}
		/* Payments */
		@Override
		public Long addPaymentToArticleOrder( Long id, Long eAccountId, Long adminEAccountId, Payment entity)
		{
			try
			{
				ArticleOrder articleOrder = em.find( ArticleOrder.class, id);
				
				EAccount eAccount = em.find( EAccount.class, eAccountId);
				EAccount adminEAccount = em.find( EAccount.class, adminEAccountId);
				
				entity.addArticleOrder(articleOrder);
				entity.seteAccount( eAccount);
				entity.setAdminEAccount(adminEAccount);
				
				em.persist( entity);
				
				articleOrder.addPayment( entity);
				
				em.merge( articleOrder);
				
				return entity.getId();
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
		@Override
		public PagedListJSON loadArticleOrderPayments(Long id, Integer page, Integer pageSize)
		{
			Query query = em.createQuery( "SELECT entity FROM ArticleOrder articleOrder JOIN articleOrder.payments entity WHERE articleOrder.id = :id");
			query.setParameter( "id", id);
			
			return generatePagedList(query, page, pageSize);
		}
	/* End ArticleOrders */
	
	/* OrderedArticles */
	@Override
	public OrderedArticle getOrderedArticle(Long id)
	{
		OrderedArticle entity = em.find( OrderedArticle.class, id);
		
		return entity;
	}
	@Override
	public void addOrderedArticle(OrderedArticle entity)
	{	
		em.persist( entity);
	}
	@Override
	@SuppressWarnings( "unchecked")
	public List<OrderedArticle> listOrderedArticleByOrder( Long id)
	{
		Query  query = em.createQuery( "SELECT entity FROM OrderedArticle entity JOIN entity.articleOrder articleOrder WHERE articleOrder.id = :id");
		query.setParameter( "id", id);
		List<OrderedArticle> entities = query.getResultList();
		
		return entities;
	}
	@Override
	public void editOrderedArticle( OrderedArticle entity)
	{
		em.merge( entity);
	}
	@Override
	@SuppressWarnings( "unchecked")
	public PagedListJSON listOrderedArticleToRate( Long userId, Integer page, Integer pageSize)
	{
		Query query = em.createQuery( "SELECT entity FROM OrderedArticle entity JOIN entity.articleOrder articleOrder JOIN articleOrder.user user JOIN user.userInfo userInfo WHERE articleOrder.status = :status"
				+ " AND userInfo.id = :userId AND entity.rating = NULL");
		query.setParameter( "status", OrderStatus.DELIVERED).setParameter( "userId", userId);
		
		PagedListJSON result = generatePagedList(query, page, pageSize);
		
		for( OrderedArticle entity : (List<OrderedArticle>) result.getEntities())
		{
			entity.getArticleOrder().setRegDate( this.getArticleOrderRegDate( entity.getArticleOrder().getId()));
		}
		
		return result;
	}
	@Override
	public Long getNumberOfArticlesToRate( Long userId)
	{
		return (long) listOrderedArticleToRate(userId, 1, 0).getEntities().size();
	}
	/* End OrderedArticles */
	
	//OrderedArticlefeature
	public Long addOrderedArticleFeature( OrderedArticleFeature entity)
	{
		em.persist( entity);
		
		return entity.getId();
	}
	@Override
	public OrderedArticleFeature getOrderedArticleFeature( Long id)
	{
		OrderedArticleFeature entity = em.find( OrderedArticleFeature.class, id);
		
		return entity;
	}
	
	/* EAccount */
	@Override
	public EAccount loadEAccount(Long id)
	{
		EAccount entity = em.find( EAccount.class, id);
		
		return entity;
	}
	@Override
	public void addEAccount(EAccount entity)
	{
		em.persist( entity);
	}
	@Override
	public void updateEAccount(EAccount entity)
	{	
		em.merge( entity);
	}
	@Override
	public EAccount loadEAccountByNumber( String number)
	{
		Query query = em.createQuery( "SELECT entity FROM EAccount entity WHERE account like '%" + number + "'");
		
		if( query.getResultList().isEmpty())
			return null;
		
		return (EAccount) query.getSingleResult();
	}
	@Override
	public boolean isEAccountNumberRegistered( String account)
	{
		try
		{
			Query query = em.createQuery( "SELECT COUNT( entity) FROM EAccount entity WHERE account like '%" + account +"'");
			
			if( ( Long) query.getSingleResult() <= 0L)
				return false;
			else
				return true;
			
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return true;
		}
	}
	@Override
	public boolean isEAccountNumberTaken( String account)
	{
		try
		{
			Query query = em.createQuery( "SELECT COUNT( entity) FROM EAccount entity WHERE account like '%" + account + "' AND entity.user is not null");

			if( ( Long) query.getSingleResult() <= 0L)
				return false;
			else
				return true;
			
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return true;
		}
	}
	/* End Eaccounts */
	
	/* Upgrade Request */
	@Override
	public UpgradeRequest loadUpgradeRequest(Long id)
	{
		return em.find( UpgradeRequest.class, id);
	}
	@Override
	public void updateUpgradeRequest(UpgradeRequest entity)
	{
		em.merge( entity);
	}
	@Override
	public Long addUpgradeRequest(Long userId, UpgradeRequest entity)
	{
		try
		{
			User user = loadUser( userId);
			
			entity.setBuyer( user);
			
			em.persist( entity);
			
			return entity.getId();
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public boolean hasAlreadyRequested( Long userId)
	{
		Query query = em.createQuery( "SELECT entity FROM UpgradeRequest entity JOIN entity.buyer buyer JOIN buyer.userInfo userInfo"
				+ " WHERE userInfo.id = :userId AND entity.validated = null");
		query.setParameter( "userId", userId);
		
		if( query.getResultList().isEmpty())
			return false;
		else
			return true;
	}
	@Override
	public PagedListJSON loadUpgradeRequests( Integer page, Integer pageSize, boolean orderByIdAsc, int status)
	{
		String hql = "SELECT entity FROM UpgradeRequest entity";
		if( status == 1 )
			hql += " WHERE validated = false";
		if( status == 2 )
			hql += " WHERE validated = true";
		hql += " ORDER BY entity.id";
		
		if( !orderByIdAsc)
			hql += " DESC";
		
		Query query = em.createQuery( hql);
		
		return generatePagedList(query, page, pageSize);
	}
	/* End Upgrade Request */
	
	/* Home Picture */
	@Override
	public Long addSlide( Slide entity)
	{
		try
		{
			em.persist(entity);
			
			return entity.getId();
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public Slide loadSlide( Long id)
	{
		return em.find(Slide.class, id);
	}
	@Override
	public void updateSlide( Slide entity)
	{
		em.merge( entity);
	}
	@Override
	public PagedListJSON loadSlides( Integer page, Integer pageSize, boolean orderByDiplayOrderAsc)
	{
		String hql = "SELECT entity FROM Slide entity WHERE entity.deleted = false";
		
		if( orderByDiplayOrderAsc)
			hql += " ORDER BY entity.displayOrder";
		else
			hql += " ORDER BY entity.displayOrder";
		
		Query query = em.createQuery( hql);
		
		return generatePagedList(query, page, pageSize);
	}
	@Override
	public PagedListJSON loadDisplayedSlides( Integer page, Integer pageSize)
	{
		String hql = "SELECT entity FROM Slide entity WHERE entity.displayed = true AND entity.deleted = false"
				+ " ORDER BY entity.displayOrder";
		
		Query query = em.createQuery( hql);
		
		return generatePagedList(query, page, pageSize);
	}
	@Override
	public Slide loadSlide( Integer dispOrder)
	{
		try
		{
			Query query = em.createQuery( "SELECT DISTINCT entity From Slide entity WHERE entity.displayOrder = :dispOrder");
			query.setParameter( "dispOrder", dispOrder);
			
			if( query.getResultList().isEmpty())
				return null;
			else
				return (Slide) query.getSingleResult();
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	/* End Home Picture */
	
	//PaymentType
	@Override
	public PaymentType loadPaymentType(Long id)
	{
		return em.find( PaymentType.class, id);
	}
	@Override
	public Long addPaymentType(PaymentType entity)
	{
		em.persist( entity);
		
		return entity.getId();
	}
	@Override
	public void updatePaymentType(PaymentType entity)
	{
		em.merge( entity);
	}

	//Generic
	@Override
	public Timestamp getRegDate( Class<?> entityClass, Long id)
	{
		Object entity = em.find(entityClass, id);
		
		if( entity == null)
			return null;
		
		AuditReader reader = AuditReaderFactory.get( em);
		Number revNum = ( Number) reader.createQuery()
										  .forRevisionsOfEntity( entityClass, false,true)
										  .addProjection( AuditEntity.revisionNumber().min())
										  .add(AuditEntity.revisionType().eq(RevisionType.ADD))
										  .add( AuditEntity.id().eq(id))
										  .getSingleResult();
		
		RevEntity rev = em.find( RevEntity.class, revNum);
		
		return new Timestamp( rev.getTimestamp());
		
	}
	
	/* Notification */
	@Override
	public Notification loadNotification( Long id)
	{
		try
		{
			return em.find(Notification.class, id);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public void updateNotification( Notification entity)
	{
		try
		{
			em.merge(entity);
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
	}
	@Override
	public Long countUserNotifications( Long id, int status)
	{
		String hql = "SELECT COUNT( entity) FROM Notification entity WHERE user = :user";
		switch (status)
		{
			case 1:
				hql += " AND read = true";
				break;
			case 2:
				hql += " AND read = false";
				break;
			default:
				break;
		}
		
		Query query = em.createQuery( hql);
		query.setParameter( "user", loadUser(id));
		
		return (Long) query.getSingleResult();
	}
	@Override
	public Long addNotification( Notification entity, Long userId)
	{
		try
		{
			User user = loadUser(userId);
			
			entity.setUser(user);
			
			em.persist( entity);
			
			return entity.getId();
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public PagedListJSON loadUserNotifications( Long id, int status, Integer page, Integer pageSize, boolean orderByIdAsc, Integer restrict)
	{
		String hql = "SELECT entity FROM Notification entity WHERE user = :user";
		switch (status) {
			case 1:
				hql += " AND isRead = false";
				break;
			case 2:
				hql += " AND isRead = true";
				break;
			default:
				break;
		}
		
		switch( restrict) {
			case 1:
				hql += " AND buyer = true"; /* Display only Buyer related */
				break;
			case 2:
				hql += " AND manager = true"; /* Display only Manager related */
				break;
			default:
				break;
					
		}
		
		if( !orderByIdAsc)
			hql += " ORDER BY entity.id DESC";
		
		Query query = em.createQuery( hql);
		query.setParameter( "user", loadUser( id));
		
		return generatePagedList(query, page, pageSize);
	}
	/* End Notification */

	/* Expenses */
	@Override
	public PagedListJSON loadExpenses( Integer page, Integer pageSize)
	{
		Query query = em.createQuery( "SELECT entity FROM Expense entity");
		
		return generatePagedList(query, page, pageSize);
	}
	@Override
	public Long addExpense( Expense entity)
	{
		return null;
	}
	@Override
	public Expense loadExpense( Long id)
	{
		return null;
	}
	/* End Expenses */
	
	//Tools*
	@SuppressWarnings("rawtypes")
	private static PagedListJSON generatePagedList( Query query, Integer page, Integer pageSize)
	{
		try
		{
			List<?> entities = query.getResultList();
			
			if( entities.size() <= 0)
				return new PagedListJSON( 0, 0, new ArrayList());
			
			PagedListJSON result = new PagedListJSON();
			result.setItemsNumber( entities.size());
			
			if( pageSize > 0) {
				result.setPagesNumber( computePagesNumber( entities.size(), pageSize));
				query.setFirstResult( (page - 1) * pageSize);
				query.setMaxResults( pageSize);
			} else {
				result.setPagesNumber( 1);
			}
			result.setEntities( query.getResultList());
			
			return result;
			
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	private static int computePagesNumber( int itemsNumber, int pageSize)
	{
		if( pageSize > 0)
			return itemsNumber % pageSize > 0 ? itemsNumber / pageSize + 1 : itemsNumber / pageSize ;
		else
			return 1;
	}
	private void prepareArticle( Article entity) {
		entity.setRating( getArticleRating( entity.getId()));
		entity.setSaleNumber( getArticleSaleNumber( entity.getId()));
	}
}
