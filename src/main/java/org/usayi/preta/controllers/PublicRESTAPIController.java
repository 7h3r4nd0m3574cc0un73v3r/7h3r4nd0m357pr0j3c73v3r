package org.usayi.preta.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.usayi.preta.Views;
import org.usayi.preta.buzlayer.IAdminRESTAPI;
import org.usayi.preta.buzlayer.IManagerRESTAPI;
import org.usayi.preta.buzlayer.IPublicRESTAPI;
import org.usayi.preta.buzlayer.IRESTAPI;
import org.usayi.preta.config.CustomPasswordEncoder;
import org.usayi.preta.config.Tools;
import org.usayi.preta.entities.AdvOption;
import org.usayi.preta.entities.Article;
import org.usayi.preta.entities.ArticleFeature;
import org.usayi.preta.entities.ArticleOrder;
import org.usayi.preta.entities.ArticleState;
import org.usayi.preta.entities.CartItem;
import org.usayi.preta.entities.Category;
import org.usayi.preta.entities.DeliveryMode;
import org.usayi.preta.entities.DurationType;
import org.usayi.preta.entities.EAccount;
import org.usayi.preta.entities.EMoneyProvider;
import org.usayi.preta.entities.EShop;
import org.usayi.preta.entities.FeatureValue;
import org.usayi.preta.entities.GenericStatus;
import org.usayi.preta.entities.Notification;
import org.usayi.preta.entities.OrderStatus;
import org.usayi.preta.entities.OrderedArticle;
import org.usayi.preta.entities.OrderedArticleFeature;
import org.usayi.preta.entities.Payment;
import org.usayi.preta.entities.PaymentType;
import org.usayi.preta.entities.Picture;
import org.usayi.preta.entities.Role;
import org.usayi.preta.entities.ShopStatus;
import org.usayi.preta.entities.ShopSub;
import org.usayi.preta.entities.Slide;
import org.usayi.preta.entities.SubOffer;
import org.usayi.preta.entities.UpgradeRequest;
import org.usayi.preta.entities.User;
import org.usayi.preta.entities.UserInfo;
import org.usayi.preta.entities.VisitedArticle;
import org.usayi.preta.entities.form.BuyerRegistration;
import org.usayi.preta.entities.form.EShopRegistration;
import org.usayi.preta.entities.form.PWRequest;
import org.usayi.preta.entities.form.PasswordChangeForm;
import org.usayi.preta.entities.form.UserLogin;
import org.usayi.preta.entities.json.CategoryTreeJSON;
import org.usayi.preta.entities.json.LoggedUser;
import org.usayi.preta.entities.json.PagedListJSON;
import org.usayi.preta.entities.json.PreArticleOrder;
import org.usayi.preta.exceptions.FieldErrorResource;
import org.usayi.preta.service.EMailService;
import org.usayi.preta.service.GoogleDriveService;
import org.usayi.preta.service.GoogleDriveServiceBuilder;
import org.usayi.preta.service.GoogleDriveServiceImpl;
import org.usayi.preta.service.NotificationService;
import org.usayi.preta.service.ReCaptchaVerificationServiceImpl;
import org.usayi.preta.service.SchedulerService;
import org.usayi.preta.service.WebSocketService;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping( "/rest-api")
public class PublicRESTAPIController
{
	@Autowired
	private IPublicRESTAPI pRESTAPI;

	/* Debug Only */
	@Autowired
	private IManagerRESTAPI mRESTAPI;

	@Autowired
	private IAdminRESTAPI aRESTAPI;
	
	@Autowired
	GoogleDriveService drive;
	/* End Dubg */
	
	@Autowired
	private IRESTAPI restBL;
	
	private final WebSocketService websocketService;
	
	@Autowired
	NotificationService notifications;
	
	@Autowired
	EMailService emails;
	
	@Autowired
	SchedulerService scheduler;
	
	public PublicRESTAPIController(IPublicRESTAPI pRESTAPI, IRESTAPI restBL, WebSocketService websocketService)
	{
		super();
		this.pRESTAPI = pRESTAPI;
		this.restBL = restBL;
		this.websocketService = websocketService;
	}
	
	/* Articles */
	@GetMapping
	@JsonView( Views.Public.class)
	@RequestMapping( method=RequestMethod.GET, value="/article/{id}")
	public ResponseEntity<?> loadArticle( @PathVariable( "id") final Long id)
	{
		try
		{
			Article entity = pRESTAPI.loadArticle(id);
			
			if( entity == null)
				return Tools.entityNotFound();
			
			return new ResponseEntity<Article>( entity, HttpStatus.OK);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
		
	}
 	@GetMapping
	@JsonView( Views.Public.class)
	@RequestMapping( method=RequestMethod.GET, value="/articles/search")
	public ResponseEntity<?> searchArticle( @RequestParam( name="name", defaultValue="") final String name,
											@RequestParam( name="page", defaultValue="1") final Integer page,
											@RequestParam( name="minPrice", defaultValue="1") final Float minPrice,
											@RequestParam( name="maxPrice", defaultValue="9999999999") final Float maxPrice,
											@RequestParam( name="pageSize", defaultValue="10") final Integer pageSize)
	{
		if( minPrice >= maxPrice)
			return new ResponseEntity<Void>( HttpStatus.BAD_REQUEST);
		try
		{
			PagedListJSON result = pRESTAPI.searchArticleStrict( name, page, pageSize, minPrice, maxPrice);
			
			return Tools.handlePagedListJSON( result);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<Void>( HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping
	@JsonView( Views.Public.class)
	@RequestMapping( value="/article/{id}/similar", method=RequestMethod.GET)
	public ResponseEntity<?> loadSimilarArticles( @PathVariable( "id") final Long id,
													   @RequestParam( name="pageSize", defaultValue="10") final Integer pageSize,
													   @RequestParam( name="page", defaultValue="1") final Integer page)
	{
		try
		{
			//Check if EShop exists ???
			PagedListJSON result = pRESTAPI.loadSimilarArticles( id, page, pageSize);
			
			return Tools.handlePagedListJSON( result);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<Void>( HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}	
	@GetMapping
	@JsonView( Views.Public.class)
	@RequestMapping( value="/articles/latest")
	public ResponseEntity<?> loadLatestArticles( @RequestParam( name="page", defaultValue="1") final Integer page,
												@RequestParam( name="pageSize", defaultValue="8") final Integer pageSize)
	{
		try
		{
			return Tools.handlePagedListJSON( pRESTAPI.loadLatestArticlesStrict(page, pageSize));
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@GetMapping
	@RequestMapping( value="/articles/best-sellers")
	public ResponseEntity<?> loadBestSellers( @RequestParam( name="page", defaultValue="1") final Integer page,
												@RequestParam( name="pageSize", defaultValue="8") final Integer pageSize)
	{
		try
		{
			return Tools.handlePagedListJSON( pRESTAPI.loadBestSellersStrict(page, pageSize));
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@GetMapping
	@RequestMapping( value="/articles/top-rated")
	public ResponseEntity<?> loadTopArticles( @RequestParam( name="page", defaultValue="1") final Integer page,
												@RequestParam( name="pageSize", defaultValue="8") final Integer pageSize)
	{
		try
		{
			return Tools.handlePagedListJSON( pRESTAPI.loadTopArticlesStrict(page, pageSize));
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@GetMapping
	@RequestMapping( value="/articles/discounted")
	public ResponseEntity<?> loadDiscountedArticles( @RequestParam( name="page", defaultValue="1") final Integer page,
												@RequestParam( name="pageSize", defaultValue="8") final Integer pageSize)
	{
		try
		{
			return Tools.handlePagedListJSON( pRESTAPI.loadDiscountedArticlesStrict(page, pageSize));
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
		/* ArticleFeatures */
		@GetMapping
		@RequestMapping( "/article/{id}/article-features")
		public ResponseEntity<?> listArticleFeaturesByArticle( @PathVariable( "id") final Long id,
															   @RequestParam( name="page", defaultValue="1") final Integer page,
															   @RequestParam( name="pageSize", defaultValue="0") final Integer pageSize) 
		{
			try
			{
				if( restBL.getArticle( id) == null)
				{
					return Tools.entityNotFound();
				}
				
				return Tools.handlePagedListJSON( pRESTAPI.listArticleFeaturesByArticle( id, page, pageSize));
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		/* Categories */
		@GetMapping
		@RequestMapping( "/article/{id}/categories")
		public ResponseEntity<?> listCategoriesByArticle( @PathVariable( "id") final Long id,
														   @RequestParam( name="page", defaultValue="1") final Integer page,
														   @RequestParam( name="pageSize", defaultValue="0") final Integer pageSize )
		{
			try
			{
				if( restBL.getArticle( id) == null)
				{
					return Tools.entityNotFound();
				}
				
				return Tools.handlePagedListJSON( pRESTAPI.listCategoriesByArticle( id, page, pageSize));
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		/* Pictures */
		@GetMapping
		@RequestMapping( "article/{id}/pictures")
		public ResponseEntity<?> listPicturesByArticle( @PathVariable( "id") final Long id,
													    @RequestParam( name="page", defaultValue="1") final Integer page,
													    @RequestParam( name="pageSize", defaultValue="0") final Integer pageSize)
		{
			try
			{
				if( restBL.getArticle( id) == null)
				{
					return Tools.entityNotFound();
				}
				
				return Tools.handlePagedListJSON( pRESTAPI.listPicturesByArticle(id, page, pageSize));
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		@GetMapping
		@RequestMapping( "article/{id}/default-picture")
		public ResponseEntity<?> getArticleDefaultPicture( @PathVariable( "id") final Long id) 
		{
			try
			{
				if( restBL.getArticle( id) == null)
				{
					return Tools.entityNotFound();
				}
				
				return new ResponseEntity<Picture>( pRESTAPI.getArticleDefaultPicture( id), HttpStatus.OK);
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		@GetMapping
		@RequestMapping( value="/article/fetch-picture/{id}", produces=MediaType.IMAGE_PNG_VALUE)
		public ResponseEntity<byte[]> fetchPictureById( @PathVariable Long id) throws Exception
		{	
			Picture entity = restBL.getPicture(id);
			
			/* TODO
			 * Fetch default picture if not found
			 */
			if( entity == null) {
				return new ResponseEntity<byte[]>( HttpStatus.NOT_FOUND);
			}

			File file = new File( entity.getFilename());

			return new ResponseEntity<byte[]>( IOUtils.toByteArray( new FileInputStream( file)), HttpStatus.OK);
		}
		/* EShop */
		@GetMapping
		@RequestMapping( "article/{id}/e-shop")
		public ResponseEntity<?> getArticleEShop( @PathVariable( "id") final Long id)
		{
			try
			{
				if( restBL.getArticle( id) == null)
				{
					return Tools.entityNotFound();
				}
				return new ResponseEntity<EShop>( pRESTAPI.getArticleEShop( id), HttpStatus.OK);
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		/* Stats */
		@GetMapping
		@RequestMapping( "/article/min-price")
		public ResponseEntity<?> getMinPrice()
		{
			try
			{
				return new ResponseEntity<Float>( pRESTAPI.getMinPrice(), HttpStatus.OK);
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		@GetMapping
		@RequestMapping( "/article/max-price")
		public ResponseEntity<?> getMaxPrice()
		{
			try
			{
				return new ResponseEntity<Float>( pRESTAPI.getMaxPrice(), HttpStatus.OK);
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		/* Advertised Articles */
		@GetMapping
		@JsonView( Views.Public.class)
		@RequestMapping( value="/articles/advertised")
		public ResponseEntity<?> loadAdvertisedArticles( @RequestParam( name="page", defaultValue="1") final Integer page,
													     @RequestParam( name="pageSize", defaultValue="8") final Integer pageSize,
													     @RequestParam( name="code", defaultValue="1") final Integer code)
		{
			try
			{
				return Tools.handlePagedListJSON( pRESTAPI.loadAdvertisedArticles(page, pageSize, code));
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
	/* End Articles */
	
	/* ArticleOrder */
	@GetMapping
	@JsonView( Views.Public.class)
	@SuppressWarnings("unchecked")
	@RequestMapping( "/article-order/{id}")
	public ResponseEntity<?> loadArticleOrder( @PathVariable( "id") final Long id) 
	{
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			
			if( !getLoggedUserFromPrincipal().hasRole( "ROLE_BUYER"))
				return Tools.unauthorized();
			
			ArticleOrder entity = pRESTAPI.loadArticleOrder( id);
			
			if( entity == null)
				return new ResponseEntity<Void>( HttpStatus.NOT_FOUND);
			
			boolean isFound = false;
			
			for( ArticleOrder articleOrder : ( List<ArticleOrder>) pRESTAPI.loadBuyerArticleOrders( getLoggedUserFromPrincipal().getUserInfo().getId(), 1, 0, OrderStatus.ALL, true).getEntities())
			{
				if( articleOrder.getId().longValue() == entity.getId().longValue())
					isFound = true;
			}
			
			if( !isFound)
				return Tools.forbidden();
			
			return new ResponseEntity<ArticleOrder>( entity, HttpStatus.OK);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@GetMapping
	@RequestMapping( "/article-order/{id}/e-shop")
 	public ResponseEntity<?> getArticleOrderEShop( @PathVariable( "id") final Long id)
	{
		try
		{
			/* Checks
			 * If Buyer, must be the author of the ArticleOrder
			 * If Manager, must be managing the EShop
			 *  */
			
			return new ResponseEntity<EShop>( pRESTAPI.loadArticleOrderEShop(id), HttpStatus.OK);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<Void>( HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PutMapping
	@RequestMapping( "/article-order/{id}/confirm-delivery")
	public ResponseEntity<?> confirmReception( @PathVariable( "id") final Long id)
	{
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			
			User user = getLoggedUserFromPrincipal();
			
			if( !user.hasRole( "ROLE_BUYER"))
				return Tools.forbidden();
			
			ArticleOrder entity = pRESTAPI.loadArticleOrder(id);
			
			if( entity == null)
				return Tools.entityNotFound();
			
			/* Checks if Buyer is the owner of the Article Order */
			if( user.getUserInfo().getId().longValue() != entity.getUser().getUserInfo().getId().longValue()) {
				return Tools.forbidden();
			}
			
			/* Update Reception Date and Status */
			entity.setReceptionDate( new Timestamp( System.currentTimeMillis()));
			entity.setStatus( OrderStatus.DELIVERED);
			
			pRESTAPI.updateArticleOrder( entity);
			
			websocketService.userArticleOrderUpdated( entity.getUser().getUsername(), id);
			
			/*
			 * Notifiy Manager and User
			 */
			notifications.deliveredArticleOrder(entity);
			/* Email */
			emails.deliveredArticleOrder(entity);
			
			/* Generate Notification About Article Pending rating */
			notifications.buyerPendingRating(user.getUserInfo().getId());
			
			return Tools.ok();
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@PostMapping
	@JsonView( Views.Public.class)
	@RequestMapping( value="/group-article-orders")
	public ResponseEntity<?> groupArticleOrderByUser( @RequestBody final Collection<CartItem> ungroupedEntities)
	{
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			
			if( ungroupedEntities.isEmpty())
				return new ResponseEntity<Void>( HttpStatus.BAD_REQUEST);
			
			Collection<PreArticleOrder> grouped = new ArrayList<PreArticleOrder>();
			
			for( CartItem cartItem : ungroupedEntities) {

				boolean eShopFound = false;
				PreArticleOrder sibling = null;
				
				/* Search for ArticleOrder with same EShop */
				for( PreArticleOrder pre : grouped) {
					if( pre.geteShop().getId().equals( pRESTAPI.getArticleEShop( cartItem.getArticle().getId()).getId())) {
						eShopFound = true;
						sibling = pre;
					}
				}
				
				if( eShopFound) {
					/*
					 * TODO
					 * Optim
					 */
					OrderedArticle orderedArticle = new OrderedArticle();
					orderedArticle.setArticle( cartItem.getArticle());
					orderedArticle.setQuantity( cartItem.getQuantity());
					
					if( cartItem.getId() == null)
					{
						for( ArticleFeature ordArFeat : cartItem.getArticle().getFeatures())
						{
							boolean isFeatureSelected = false;
							Collection<FeatureValue> featVals = new ArrayList<FeatureValue>();
							
							for( FeatureValue featVal : ordArFeat.getValues())
							{
								if( featVal.getIsSelected())
								{
									isFeatureSelected = true;
									featVals.add( restBL.getFeatureValue( featVal.getId()));
								}
							}
							
							if( isFeatureSelected)
							{
								OrderedArticleFeature orderedArticleFeature = new OrderedArticleFeature( featVals, restBL.getFeature( ordArFeat.getFeature().getId()));
								orderedArticle.getOrderedArticleFeatures().add( orderedArticleFeature);
							}
						}
					}
					else
					{
						for( OrderedArticleFeature ordArFeat : cartItem.getOrderedArticleFeatures())
						{
							orderedArticle.getOrderedArticleFeatures().add( ordArFeat);
						}
					}
					
					sibling.addOrderedArticle( orderedArticle);
					sibling.addCartItemId( cartItem.getId());
				}
				else {
					PreArticleOrder entity = new PreArticleOrder();
					entity.setId( grouped.size() + 1L);
					
					/* Article Features */
					OrderedArticle orderedArticle = new OrderedArticle();
					
					if( cartItem.getId() == null)
					{
						for( ArticleFeature ordArFeat : cartItem.getArticle().getFeatures())
						{
							boolean isFeatureSelected = false;
							Collection<FeatureValue> featVals = new ArrayList<FeatureValue>();
							
							for( FeatureValue featVal : ordArFeat.getValues())
							{
								if( featVal.getIsSelected())
								{
									isFeatureSelected = true;
									featVals.add( restBL.getFeatureValue( featVal.getId()));
								}
							}
							
							if( isFeatureSelected)
							{
								OrderedArticleFeature orderedArticleFeature = new OrderedArticleFeature( featVals, restBL.getFeature( ordArFeat.getFeature().getId()));
								orderedArticle.getOrderedArticleFeatures().add( orderedArticleFeature);
							}
						}
					}
					else
					{
						for( OrderedArticleFeature ordArFeat : cartItem.getOrderedArticleFeatures())
						{
							orderedArticle.getOrderedArticleFeatures().add( ordArFeat);
						}
						
						entity.addCartItemId( cartItem.getId());
					}

					orderedArticle.setArticle( cartItem.getArticle());
					orderedArticle.setQuantity( cartItem.getQuantity());
					entity.addOrderedArticle( orderedArticle);
					entity.seteShop( getPreArticleOrderEShop( entity));
					
					grouped.add( entity);
				}
			}
			
			return new ResponseEntity<Collection<PreArticleOrder>>( grouped, HttpStatus.OK);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
		
	}
	/* End ArticleOrder */
	
	/* EMoney Provider */
	@GetMapping
	@JsonView( Views.Public.class)
	@RequestMapping( value="/e-money-providers")
	public ResponseEntity<?> listEMoneyProviders( @RequestParam( name="page", defaultValue="1") final Integer page,
												   @RequestParam( name="pageSize", defaultValue="10") final Integer pageSize)
	{
		return Tools.handlePagedListJSON( pRESTAPI.loadEMoneyProviders(page, pageSize));
	}	
	@GetMapping
	@JsonView( Views.Public.class)
	@RequestMapping( value="/e-money-provider/{id}")
	public ResponseEntity<?> getEMoneyProvider(@PathVariable( value="id") final Long id)
	{
		EMoneyProvider entity = pRESTAPI.loadEMoneyProvider(id);

		if( entity == null)
			return new ResponseEntity<EMoneyProvider>( HttpStatus.NOT_FOUND);

		return new ResponseEntity<EMoneyProvider>( entity, HttpStatus.OK);
	}
	@GetMapping
	@RequestMapping( value="/e-money-provider/{id}/fetch-logo", produces=MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<?> fetchEMPLogoFile( @PathVariable Long id)
	{	
		try
		{
			if( pRESTAPI.loadEMoneyProvider( id) == null)
			{
				return new ResponseEntity<Void>( HttpStatus.NOT_FOUND);
			}

			if( !pRESTAPI.loadEMoneyProvider( id).hasLogo())
			{
				//Return a default picture as logo when the entity hasn't one
				File defaultFile = new File( Tools.uploadPath + "/default/emp.png");
				return new ResponseEntity<byte[]>( IOUtils.toByteArray( new FileInputStream( defaultFile)), HttpStatus.OK);
			}

			File file = new File( pRESTAPI.loadEMoneyProvider(id).getLogoFile());

			return new ResponseEntity<byte[]>( IOUtils.toByteArray( new FileInputStream( file)), HttpStatus.OK);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	/* End EMoney Provider */
	
	/* SubOffers */
	@GetMapping
	@JsonView( Views.Public.class)
	@RequestMapping( value="/sub-offer/get/{id}")
	public ResponseEntity<?> getSubOffer( @PathVariable( "id") final Long id)
	{
		try
		{
			SubOffer entity = pRESTAPI.getSubOffer( id);

			if( entity == null)
				return new ResponseEntity<SubOffer>( HttpStatus.NOT_FOUND);

			return new ResponseEntity<SubOffer>( entity, HttpStatus.OK);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@GetMapping
	@JsonView( Views.Public.class)
	@RequestMapping( value="/sub-offers")
	public ResponseEntity<?> listSubOffer( @RequestParam( name="page", defaultValue="1") final Integer page,
											@RequestParam( name="pageSize", defaultValue="10") final Integer pageSize)
	{
		try
		{
			return Tools.handlePagedListJSON( pRESTAPI.listSubOffer(page, pageSize));
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}	
	/* End SubOffers */
	
	/* Categories */
	@GetMapping
	@JsonView( Views.Public.class)
	@RequestMapping( value="/category/{id}", method=RequestMethod.GET)
	public ResponseEntity<?> loadCategory( @PathVariable( "id") final Long id)
	{
		try
		{
			Category entity = pRESTAPI.loadCategory( id);
			
			if( entity == null)
				return Tools.entityNotFound();
			
			return new ResponseEntity<Category>( entity, HttpStatus.OK);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<Void>( HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping
	@JsonView( Views.Admin.class)
	@RequestMapping( value="/category/{id}/root")
	public ResponseEntity<?> loadCategoryRoot( @PathVariable( "id") final Long id)
	{
		Category entity = pRESTAPI.loadCategory( id);

		if( entity == null)
			return Tools.entityNotFound();
		
		return new ResponseEntity<Category>( entity.getRoot(), HttpStatus.OK);
	}
	@GetMapping
	@JsonView( Views.Public.class)
	@RequestMapping( value="/categories", method=RequestMethod.GET)
	public ResponseEntity<?> loadCategories( @RequestParam( name="pageSize", defaultValue="10") final Integer pageSize,
  			 								 @RequestParam( name="page", defaultValue="1") final Integer page)
	{
		try
		{
			return Tools.handlePagedListJSON( pRESTAPI.loadCategories(page, pageSize));
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<Void>( HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping
	@JsonView( Views.Public.class)
	@RequestMapping( value="/category/{id}/articles", method=RequestMethod.GET)
	public ResponseEntity<?> loadArticlesByCategory( @PathVariable( "id") final Long id,
													 @RequestParam( name="pageSize", defaultValue="10") final Integer pageSize,
													 @RequestParam( name="page", defaultValue="1") final Integer page)
	{
		try
		{
			return Tools.handlePagedListJSON( pRESTAPI.loadCategoryArticlesStrict( id, page, pageSize));
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@GetMapping
	@JsonView( Views.Admin.class)
	@SuppressWarnings("unchecked")
	@RequestMapping( "/category/jstree-list")
	public ResponseEntity<?> loadCategoryJSTree()
	{
		try
		{
			List<CategoryTreeJSON> entities = new ArrayList<CategoryTreeJSON>();
			
			for( Category category : (List<Category>) pRESTAPI.loadCategories( 1, 0).getEntities())
			{
				entities.add( new CategoryTreeJSON( category));
			}
			
			return new ResponseEntity<List<CategoryTreeJSON>>( entities, HttpStatus.OK);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<Void>( HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping
	@JsonView( Views.Admin.class)
	@RequestMapping( value="/categories/level/{level}")
	public ResponseEntity<?> loadCategories( @PathVariable( "level") final Integer level,
														  @RequestParam( name="page", defaultValue="1") final Integer page,
														  @RequestParam( name="pageSize", defaultValue="0") final Integer pageSize)
	{
		return Tools.handlePagedListJSON( pRESTAPI.loadCategories(level, page, pageSize));
	}
	/* End Categories */
	
	/* EShops */
	@GetMapping
	@JsonView( Views.Public.class)
	@RequestMapping( value="/e-shop/{id}")
	public ResponseEntity<EShop> loadEShop( @PathVariable( "id") final Long id)
	{
		EShop entity = pRESTAPI.loadEShop( id);

		if( entity == null)
			return new ResponseEntity<EShop>( HttpStatus.NOT_FOUND);

		HttpHeaders headers = new HttpHeaders();

		return new ResponseEntity<EShop>( entity, headers, HttpStatus.OK);
	}
		@GetMapping
		@RequestMapping( value="/e-shop/{id}/fetch-logo", produces=MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<byte[]> fetchEShopLogo(@PathVariable Long id) throws Exception
		{	
			EShop entity = restBL.getEShop(id);
			
			if( entity == null)
			{
				return new ResponseEntity<byte[]>( HttpStatus.NOT_FOUND);
			}
			
			if( entity.getLogoFile() == null || entity.getLogoFile() == "")
			{
				File file = new File( Tools.uploadPath + "default/e-shop.jpg");
				return new ResponseEntity<byte[]>( IOUtils.toByteArray( new FileInputStream( file)), HttpStatus.OK);
			}
			
			File file = new File( entity.getLogoFile());
	
			return new ResponseEntity<byte[]>( IOUtils.toByteArray( new FileInputStream( file)), HttpStatus.OK);
		}
		/* Articles */
		@GetMapping
		@JsonView( Views.Public.class)
		@RequestMapping( value="/e-shop/{id}/latest-articles", method=RequestMethod.GET)
		public ResponseEntity<?> listRecentArticlesByEShop( @PathVariable( "id") final Long id,
														   @RequestParam( name="pageSize", defaultValue="10") final Integer pageSize,
														   @RequestParam( name="page", defaultValue="1") final Integer page)
		{
			try
			{
				//Check if EShop exists ???
				PagedListJSON result = pRESTAPI.loadRecentArticlesByEShop( id, page, pageSize);
				
				return Tools.handlePagedListJSON( result);
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return new ResponseEntity<Void>( HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}	
		@GetMapping
		@JsonView( Views.Public.class)
		@RequestMapping( value="/e-shop/{id}/best-sellers", method=RequestMethod.GET)
		public ResponseEntity<?> listBestSellerArticlesByEShop( @PathVariable( "id") final Long id,
															   @RequestParam( name="pageSize", defaultValue="8") final Integer pageSize,
															   @RequestParam( name="page", defaultValue="1") final Integer page)
		{
			try
			{
				//Check if EShop exists ???
				PagedListJSON result = pRESTAPI.loadBestSellerArticlesByEShop(id, page, pageSize);
				
				if( result.getEntities().isEmpty())
					return new ResponseEntity<Void>( HttpStatus.NO_CONTENT);
				
				return new ResponseEntity<PagedListJSON>( result, HttpStatus.OK);
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return new ResponseEntity<Void>( HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}	
		@GetMapping
		@JsonView( Views.Public.class)
		@RequestMapping( value="/e-shop/{id}/top-articles", method=RequestMethod.GET)
		public ResponseEntity<?> listTopArticlesByEShop( @PathVariable( "id") final Long id,
												   @RequestParam( name="pageSize", defaultValue="8") final Integer pageSize,
												   @RequestParam( name="page", defaultValue="1") final Integer page)
		{
			try
			{
				//Check if EShop exists ???
				PagedListJSON result = pRESTAPI.loadTopArticlesByEShop(id, page, pageSize);
				
				return Tools.handlePagedListJSON( result);
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return new ResponseEntity<Void>( HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		@GetMapping
		@JsonView( Views.Manager.class)
		@RequestMapping( "/e-shop/{id}/articles")
		public ResponseEntity<?> listArticlesByEshop( @PathVariable( "id") final Long id,
																  @RequestParam( name="page", defaultValue="1") final Integer page,
																  @RequestParam( name="pageSize", defaultValue="12") final Integer pageSize)
		{
			try
			{
				return Tools.handlePagedListJSON( pRESTAPI.loadEShopArticles(id, page, pageSize));
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
	/* End EShops */
	
	/* Registration */
	@PostMapping
	@RequestMapping( value="/register/buyer")
	public ResponseEntity<?> registerBuyer( @RequestBody BuyerRegistration entity)
	{
		if( !isAnonymous())
			return Tools.forbidden();
		
		try
		{
			List<FieldErrorResource> fErrors = new ArrayList<FieldErrorResource>();
			
			/* Level 1 */ 
			if( entity.getUsername() == null || entity.getUsername().isEmpty())
				fErrors.add( new FieldErrorResource( "User", "username", "NotNull", "This field is mandatory."));
			if( entity.getPassword() == null || entity.getPassword().isEmpty())
				fErrors.add( new FieldErrorResource( "User", "password", "NotNull", "This field is mandatory."));
			if( entity.getConfirmPassword() == null || entity.getConfirmPassword().isEmpty())
				fErrors.add( new FieldErrorResource( "User", "confirmPassword", "NotNull", "This field is mandatory."));
			if( entity.getUserInfo().getEmail() == null || entity.getUserInfo().getEmail().isEmpty())
				fErrors.add( new FieldErrorResource( "User", "email", "NotNull", "This field is mandatory."));
			if( entity.getMobile() == null || entity.getMobile().isEmpty())
				fErrors.add( new FieldErrorResource( "User", "mobile", "NotNull", "This field is mandatory."));
			if( entity.getCaptcha() == null || entity.getCaptcha().isEmpty())
				fErrors.add( new FieldErrorResource( "User", "captcha", "NotNull", "This field is mandatory."));
			/* Note: Never let a null value pass this step, otherwise, NullPointerException */
			if( !fErrors.isEmpty())
				return Tools.handleMultipleFieldErrors(fErrors);
			
			/* Level 2 Checks */
			if( pRESTAPI.loadUserByUsername( entity.getUsername()) != null)
				fErrors.add( new FieldErrorResource( "User", "username", "Conflict", "This username is already taken."));
			if( !entity.getUserInfo().getEmail().matches( ".+@.+\\..+"))
				fErrors.add( new FieldErrorResource( "User", "email", "Invalid", "This email is invalid."));
			if( entity.getPassword().length() < 8)
				fErrors.add( new FieldErrorResource( "User", "password", "Short", "The passwords must contain at least 8 characters."));
			if( !ReCaptchaVerificationServiceImpl.verify( entity.getCaptcha()))
				fErrors.add( new FieldErrorResource( "User", "captcha", "Invalid", "This catcha is invalid."));
			/*
			 * TODO
			 * Invalid username like prefixed with admin, etc ...
			 */
			if( pRESTAPI.loadUserByEMail( entity.getUserInfo().getEmail()) != null)
				fErrors.add( new FieldErrorResource( "User", "email", "Conflict", "This email is already registered."));
			if( pRESTAPI.isEAccountNumberTaken( entity.getMobile()))
				fErrors.add( new FieldErrorResource( "User", "mobile", "Conflict", "This number is already registered."));
			
			boolean hasMatch = false;
			
			/* Check if mobile matches any EMP */
			@SuppressWarnings("unchecked")
			List<EMoneyProvider> emps = (List<EMoneyProvider>) restBL.listEMoneyProviders( 1, 0).getEntities();
			
			Long empId = 0L;
			for( EMoneyProvider emp : emps)
			{
				if( entity.getMobile().matches( emp.getNumPattern()))
				{
					//eAccount.seteMoneyProvider( restBL.getEMoneyProvider( emp.getId()));
					empId = emp.getId();
					hasMatch = true;
				}
			}

			if( !hasMatch)
				fErrors.add( new FieldErrorResource( "User", "mobile", "Invalid", "The specified number is not supported at this moment."));
			
			if( !fErrors.isEmpty())
				return Tools.handleMultipleFieldErrors(fErrors);
			
			//Register UserInfo for New User
			UserInfo userInfo = new UserInfo( entity.getUserInfo());
			
			//Manual add phonenumbers
			restBL.addUserInfo( userInfo);
			
			/* EAccount Affectation */
			EAccount eAccount = new EAccount();
			
			if( pRESTAPI.isEAccountNumberRegistered( entity.getMobile())) {
				eAccount = pRESTAPI.loadEAccountByNumber( entity.getMobile());
			}
			else {
				eAccount.setAccount( entity.getMobile());
				eAccount.setIsDefault( true);
				eAccount.setRelId( 1L);
			}
				
			pRESTAPI.addEAccountToUserInfo( userInfo.getId(), empId, eAccount);
			
			userInfo.addEAccount( eAccount);
			pRESTAPI.updateUserInfo( userInfo);
			
			//Set base for User
			User user = new User();
			user.setUserInfo( userInfo);
			user.setUsername( entity.getUsername());

			//Adding Roles to user
			user.addRole( pRESTAPI.loadRole( "ROLE_BUYER"));

			//Generating salt and confirmation token for user
			user.setSalt( RandomStringUtils.random( 32, true, true));
			user.setConfToken( Tools.generateConfToken());

			//Encoding password using CustomPasswordEncoder
			CustomPasswordEncoder passwordEncoder = new CustomPasswordEncoder();
			user.setPassword( passwordEncoder.encode( user.getSalt() + entity.getPassword()));

			pRESTAPI.addUser( user);
			
			emails.activation(user);
			
			return new ResponseEntity<Void>( HttpStatus.CREATED);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@PostMapping
	@RequestMapping( value="/register/e-shop")
	public ResponseEntity<?> registerEShop( @RequestBody EShopRegistration entity)
	{	
		if( !isAnonymous())
			return Tools.forbidden();
		
		try
		{
			List<FieldErrorResource> fErrors = new ArrayList<FieldErrorResource>();
			
			/* Level 1 */ 
			if( entity.getUsername() == null || entity.getUsername().isEmpty())
				fErrors.add( new FieldErrorResource( "User", "username", "NotNull", "This field is mandatory."));
			if( entity.getPassword() == null || entity.getPassword().isEmpty())
				fErrors.add( new FieldErrorResource( "User", "password", "NotNull", "This field is mandatory."));
			if( entity.getConfirmPassword() == null || entity.getConfirmPassword().isEmpty())
				fErrors.add( new FieldErrorResource( "User", "confirmPassword", "NotNull", "This field is mandatory."));
			if( entity.getUserEmail() == null || entity.getUserEmail().isEmpty())
				fErrors.add( new FieldErrorResource( "User", "email", "NotNull", "This field is mandatory."));
			if( entity.getUserMobile() == null || entity.getUserMobile().isEmpty())
				fErrors.add( new FieldErrorResource( "User", "mobile", "NotNull", "This field is mandatory."));
			if( entity.getCaptcha() == null || entity.getCaptcha().isEmpty())
				fErrors.add( new FieldErrorResource( "User", "captcha", "NotNull", "This field is mandatory."));
			/* Note: Never let a null value pass this step, otherwise, NullPointerException */
			if( !fErrors.isEmpty())
				return Tools.handleMultipleFieldErrors(fErrors);
			

			/* Level 2 Checks */
			if( !entity.getUserEmail().matches( ".+@.+\\..+"))
				fErrors.add( new FieldErrorResource( "User", "email", "Invalid", "This email is invalid."));
			if( entity.getPassword().length() < 8)
				fErrors.add( new FieldErrorResource( "User", "password", "Short", "The passwords must contain at least 8 characters."));
			if( !ReCaptchaVerificationServiceImpl.verify( entity.getCaptcha()))
				fErrors.add( new FieldErrorResource( "User", "captcha", "Invalid", "This catcha is invalid."));
			
			/* Level 3 Checks */
			/*
			 * TODO
			 * Invalid username like prefixed with admin, etc ...
			 */
			if( pRESTAPI.loadEShop( entity.getEshopName()) != null)
				fErrors.add( new FieldErrorResource( "EShop", "eshopName", "Conflict", "This name is already registered."));
			if( pRESTAPI.loadUserByUsername( entity.getUsername()) != null)
				fErrors.add( new FieldErrorResource( "User", "username", "Conflict", "This username is already taken."));
			if( pRESTAPI.loadUserByEMail( entity.getUserEmail()) != null)
				fErrors.add( new FieldErrorResource( "User", "userEmail", "Conflict", "This email is already registered."));
			if( pRESTAPI.isEAccountNumberTaken( entity.getUserMobile()))
				fErrors.add( new FieldErrorResource( "User", "userMobile", "Conflict", "This number is already registered."));
			
			/* EAccount checks */
			boolean hasMatch = false;
			
			/* Check if mobile matches any EMP */
			@SuppressWarnings("unchecked")
			List<EMoneyProvider> emps = (List<EMoneyProvider>) restBL.listEMoneyProviders( 1, 0).getEntities();
			
			Long empId = 0L;
			for( EMoneyProvider emp : emps)
			{
				if( entity.getUserMobile().matches( emp.getNumPattern()))
				{
					//eAccount.seteMoneyProvider( restBL.getEMoneyProvider( emp.getId()));
					empId = emp.getId();
					hasMatch = true;
				}
			}

			if( !hasMatch)
				fErrors.add( new FieldErrorResource( "User", "userMobile", "Invalid", "The specified number is not supported at this moment."));
			
			if( !fErrors.isEmpty())
				return Tools.handleMultipleFieldErrors(fErrors);
				
			//Register UserInfo for New User
			UserInfo userInfo = new UserInfo();
			userInfo.setEmail( entity.getUserEmail());
			pRESTAPI.addUserInfo( userInfo);
			
			/* EAccount Affectation */
			EAccount eAccount = new EAccount();
			
			if( pRESTAPI.isEAccountNumberRegistered( entity.getUserMobile())) {
				eAccount = pRESTAPI.loadEAccountByNumber( entity.getUserMobile());
			}
			else {
				eAccount.setAccount( entity.getMobile());
				eAccount.setIsDefault( true);
				eAccount.setRelId( 1L);
			}
			
			pRESTAPI.addEAccountToUserInfo( userInfo.getId(), empId, eAccount);
			
			userInfo.addEAccount( eAccount);
			pRESTAPI.updateUserInfo( userInfo);

			//Set base for User
			User user = new User();
			user.setUserInfo( userInfo);
			user.setUsername( entity.getUsername());

			//Adding Roles to user
			user.addRole( pRESTAPI.loadRole( "ROLE_BUYER"));
			user.addRole( pRESTAPI.loadRole( "ROLE_MANAGER"));

			//Generating salt and confirmation token for user
			user.setSalt( RandomStringUtils.random( 32, true, true));
			user.setConfToken( RandomStringUtils.random( 64, true, true));

			//Encoding password using CustomPasswordEncoder
			CustomPasswordEncoder passwordEncoder = new CustomPasswordEncoder();
			user.setPassword( passwordEncoder.encode( user.getSalt() + entity.getPassword()));

			/* Registering the EShop */
			EShop eShop = new EShop();
			eShop.setName( entity.getEshopName());
			eShop.setEmail( entity.getEmail());
			eShop.setCustomerNum( entity.getMobile());
			eShop.setLogoGoogleId( GoogleDriveServiceImpl.defaultEShopPicId);
			eShop.setRelId( 1L);
			
			pRESTAPI.addEShop( eShop);

			user.addManagedEShop( eShop);

			pRESTAPI.addUser( user);

			emails.activation(user);
			
			return Tools.ok();
			
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	/* End Registration */
	
	/* Rating */
	@GetMapping
	@JsonView( Views.Public.class)
	@RequestMapping( "/ordered-article/pending-rating")
	public ResponseEntity<?> listOrderedArticleToRate( @RequestParam( name="page", defaultValue="1") final Integer page,
													   @RequestParam( name="pageSize", defaultValue="8") final Integer pageSize)
	{
		try
		{
			if( SecurityContextHolder.getContext().getAuthentication().getName() == "anonymousUser")
				return new ResponseEntity<Void>( HttpStatus.UNAUTHORIZED);
			
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			if( !user.hasRole( "ROLE_BUYER"))
				return new ResponseEntity<Void>( HttpStatus.UNAUTHORIZED);
			
			return Tools.handlePagedListJSON( pRESTAPI.listOrderedArticleToRate( user.getUserInfo().getId(), page, pageSize));
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@GetMapping
	@JsonView( Views.Public.class)
	@RequestMapping( "/ordered-article/pending-rating-count")
	public ResponseEntity<?> getNumberOfArticlesToRate()
	{
		try
		{
			if( SecurityContextHolder.getContext().getAuthentication().getName() == "anonymousUser")
				return new ResponseEntity<Void>( HttpStatus.UNAUTHORIZED);
			
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			if( !user.hasRole( "ROLE_BUYER"))
				return new ResponseEntity<Void>( HttpStatus.UNAUTHORIZED);
			
			return new ResponseEntity<Long>( pRESTAPI.getNumberOfArticlesToRate( user.getUserInfo().getId()), HttpStatus.OK);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	/* End Rating */
	
	/* User (Logged In) */
	@PutMapping
	@RequestMapping( "/logged-user/update-profile")
	public ResponseEntity<?> updateProfile( @RequestBody User entity)
	{
		if( isAnonymous())
			return Tools.unauthorized();
		
		try
		{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			User loggedUser = (User) auth.getPrincipal();
			UserInfo originalUserInfo = loggedUser.getUserInfo();

			User originalEntity = pRESTAPI.loadUser( loggedUser.getUserInfo().getId());

			if( originalEntity == null)
				return new ResponseEntity<Map<String, Object>>( HttpStatus.BAD_REQUEST);

			BeanUtils.copyProperties( entity.getUserInfo(), originalUserInfo, Tools.getNullPropertyNames( entity.getUserInfo()));

			if( originalEntity.hasRole( "ROLE_BUYER") || loggedUser.hasRole( "ROLE_MANAGER"))
				originalEntity.setApproved( false);
			
			pRESTAPI.updateUserInfo( originalUserInfo);
			
			/*BeanUtils.copyProperties( entity, originalEntity, Tools.getNullPropertyNames( entity));*/
			/*originalEntity.setIsEnabled( true);*/
			pRESTAPI.updateUser( originalEntity);

			SecurityContextHolder.getContext().setAuthentication( new UsernamePasswordAuthenticationToken( auth.getPrincipal(), auth.getCredentials(), auth.getAuthorities()));

			//Get the successfully logged user
			loggedUser = (User) auth.getPrincipal();
			//Instantiate and feed the logged User proxy which will be returned by the web service in case of
			
			/* Broadcast a new updated profile on websocket */
			websocketService.buyerProfileUpdated();
			/* Notify user */
			/* TODO: Add websocket support */
			notifications.entityApprovalReady(loggedUser);
			
			/* Notify the user that his profile is updated */
			websocketService.userProfileUpdated( getLoggedUserFromPrincipal().getUsername());
			
			return new ResponseEntity<LoggedUser>( generateLoggedUserJSON( loggedUser.getUserInfo().getId()), HttpStatus.OK);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
		/* Cart */
		@GetMapping
		@JsonView( Views.Public.class)
		@RequestMapping( "/logged-user/cart-items")
		public ResponseEntity<?> loadLoggedUserCartItems( @RequestParam( name="page", defaultValue="1") final Integer page,
														  @RequestParam( name="pageSize", defaultValue="5") final Integer pageSize)
		{
			try
			{
				/* Kick if User is not logged */
				if( isAnonymous())
					return Tools.unauthorized();
				
				/* Get Logged User */
				User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				
				return Tools.handlePagedListJSON( pRESTAPI.loadCartItems( loggedUser.getUserInfo().getId(), page, pageSize));
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		@PostMapping
		@JsonView( Views.Public.class)
		@RequestMapping( "/logged-user/clear-cart-items")
		public ResponseEntity<?> clearLoggedUserCartItems()
		{
			try
			{
				/* Kick if User is not logged */
				if( isAnonymous())
					return Tools.unauthorized();
				
				pRESTAPI.emptyUserCart( getLoggedUserFromPrincipal().getUserInfo().getId());
				
				/* Websocket Notifyf */
				websocketService.userCartUpdated( getLoggedUserFromPrincipal().getUsername());
				
				return Tools.ok();
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		@PostMapping
		@JsonView( Views.Public.class)
		@RequestMapping( "/logged-user/clear-cart-selection")
		public ResponseEntity<?> clearLoggedUserCartSelection( @RequestBody final long[] ids)
		{
			try
			{
				/* Kick if User is not logged */
				if( isAnonymous())
					return Tools.unauthorized();
				
				/*
				 * TODO
				 * Check if buyer owns cart item
				 */
				
				for( long tmp : ids)
				{
					pRESTAPI.removeCartItemFromUserCart( getLoggedUserFromPrincipal().getUserInfo().getId(), tmp);
				}
				

				/* Websocket Notifyf */
				websocketService.userCartUpdated( getLoggedUserFromPrincipal().getUsername());
				
				return Tools.ok();
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		@SuppressWarnings("unchecked")
		@PostMapping
		@RequestMapping( "/logged-user/cart/add-article")
		public ResponseEntity<?> addArticleToCart( @RequestParam( "entity") final String entity,
												   @RequestParam( "quantity") final String quantity)
		{
			try
			{
				ObjectMapper mapper = new ObjectMapper();
				
				//Check if user is logged
				if( isAnonymous())
					return Tools.unauthorized();

				//Get the logged user
				User user = getLoggedUserFromPrincipal();

				//Checks if user is a Buyer
				if( !user.hasRole( "ROLE_BUYER"))
					return Tools.unauthorized();
				
				if( pRESTAPI.loadCartItems( user.getUserInfo().getId(), 1, 0).getEntities().size() >= Tools.cartLimit) {
					return Tools.handleSingleFieldError( new FieldErrorResource( "user", "cart", "Full", "Cart is full. Current limit is + " + Tools.cartLimit));
				}
				
				Article article =  mapper.readValue( entity, Article.class);
				
				if( article == null)
					return Tools.entityNotFound();
				
				final Integer articleUnits = mapper.readValue(quantity, Integer.class);
				/* Checking if such article with exctly same caracteristics
				 * exists
				 */
				
				/* TODO Check if Article is ready for Cart, especially if Features are selected
				 */
				
				List<CartItem> cartItems = (List<CartItem>) pRESTAPI.loadCartItems( user.getUserInfo().getId(), 1, 0).getEntities();
				Long matchingCartItemId = null;
				
				/* Subconsciencly written algorithm */
				for( CartItem cartItem : cartItems) {
					if( cartItem.getArticle().getId().equals( article.getId())) {
						
						List<OrderedArticleFeature> orderedArticleFeatures = (List<OrderedArticleFeature>) cartItem.getOrderedArticleFeatures();
						
						int matchingFeaturesCount = 0;
						for( OrderedArticleFeature ordArtFeat : orderedArticleFeatures)
						{
							for( ArticleFeature artFeat : article.getFeatures())
							{
								if( artFeat.getFeature().getId().equals(ordArtFeat.getFeature().getId()))
								{
									int matchingFeatVals = 0;
									
									for( FeatureValue ordFeatVal : ordArtFeat.getFeatureValues())
									{
										for( FeatureValue featVal : artFeat.getValues())
										{
											if( featVal.getId().equals(ordFeatVal.getId()) && featVal.getIsSelected())
											{
												matchingFeatVals++;
											}
										}
									}
									
									if( matchingFeatVals == ordArtFeat.getFeatureValues().size())
										matchingFeaturesCount++;
								}
							}
						}
						if( matchingFeaturesCount == orderedArticleFeatures.size())
						{
							/* Update the quantity of matched article */
							System.out.println( "DEBUG: Perfect Matching Article detected in cart");
							matchingCartItemId = cartItem.getId();
						}
					}
				}
				
				if( matchingCartItemId != null) 
				{
					CartItem matched = pRESTAPI.loadCartItem(matchingCartItemId);
					matched.setQuantity( matched.getQuantity() + articleUnits);
					
					pRESTAPI.updateCartItem(matched);
					
					return Tools.ok();
				}
				
				/* If Matching Article was not found in cart, just creat a new instance */
				CartItem cartItem = new CartItem( articleUnits, article);
				
				//Save the specific feature the user selected
				for( ArticleFeature arfeat : article.getFeatures())
				{	
					boolean isFeatureSelected = false;
					Collection<FeatureValue> featVals = new ArrayList<FeatureValue>();
					
					for( FeatureValue featVal : arfeat.getValues())
					{	
						if( featVal.getIsSelected())
						{
							isFeatureSelected = true;
							featVals.add( restBL.getFeatureValue( featVal.getId()));
						}
					}
					
					if( isFeatureSelected)
					{
						OrderedArticleFeature orderedArticleFeature = new OrderedArticleFeature( featVals, restBL.getFeature( arfeat.getFeature().getId()));
						restBL.addOrderedArticleFeature( orderedArticleFeature);
						cartItem.addOrderedArticleFeature( orderedArticleFeature);
					}
					
					featVals.clear();
				}
				
				pRESTAPI.addCartItemToUserCart( user.getUserInfo().getId(), cartItem);
				
				((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).setCart( (List<CartItem>) pRESTAPI.loadCartItems( user.getUserInfo().getId(), 1, 0).getEntities());
				
				/* Websocket Notifyf */
				websocketService.userCartUpdated( getLoggedUserFromPrincipal().getUsername());
				
				return Tools.ok();
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		@SuppressWarnings("unchecked")
		@PutMapping
		@RequestMapping( "/logged-user/cart/remove/{id}")
		public ResponseEntity<?> removeArticleFromCart( @PathVariable( "id") final Long id)
		{
			try
			{
				if( isAnonymous())
					return Tools.unauthorized();
				
				CartItem cartItem = restBL.getCartItem(id);
				
				if( cartItem == null)
					return Tools.entityNotFound();
				
				//Check if the user really owns the cart item
				User user = getLoggedUserFromPrincipal();
				
				CartItem entity = null;
				
				for( CartItem tmp : ( List<CartItem>) pRESTAPI.loadCartItems( user.getUserInfo().getId(), 1, 0).getEntities())
				{
					if( tmp.getId().longValue() == cartItem.getId().longValue())
						entity = tmp;
				}
				
				if( entity == null)
					return Tools.unauthorized();
				
				pRESTAPI.removeCartItemFromUserCart( user.getUserInfo().getId(), entity.getId());

				/* Websocket Notifyf */
				websocketService.userCartUpdated( getLoggedUserFromPrincipal().getUsername());
				
				return Tools.ok();
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return new ResponseEntity<Void>( HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		/* ArticleOrder */
		@GetMapping
		@JsonView( Views.Public.class)
		@RequestMapping( value="/logged-user/article-orders", method=RequestMethod.GET)
		public ResponseEntity<?> loadBuyerArticleOrders( @RequestParam( name="page", defaultValue="1") final Integer page,
														 @RequestParam( name="pageSize", defaultValue="6") final Integer pageSize,
														 @RequestParam( name="orderStatus", defaultValue="0") final Integer orderStatusInt,
														 @RequestParam( name="orderByIdAsc", defaultValue="true") final boolean orderByIdAsc)
		{
			try
			{
				/* Kick if User is not logged */
				if( isAnonymous())
					return Tools.unauthorized();
				
				OrderStatus orderStatus = OrderStatus.ALL;
				switch( orderStatusInt)
				{
					case 1:
						orderStatus = OrderStatus.PENDING_PAYMENT;
						break;
					case 2:
						orderStatus = OrderStatus.PENDING_PAYMENT_CONFIRMATION;
						break;
					case 3:
						orderStatus = OrderStatus.PAID;
						break;
					case 4:
						orderStatus = OrderStatus.DELIVERING;
						break;
					case 5:
						orderStatus = OrderStatus.DELIVERED;
						break;
					default:
						orderStatus = OrderStatus.ALL;
						break;
						
				}
				PagedListJSON result = pRESTAPI.loadBuyerArticleOrders( getLoggedUserFromPrincipal().getUserInfo().getId(), page, pageSize, orderStatus, orderByIdAsc);
				
				return Tools.handlePagedListJSON(result);
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return new ResponseEntity<Void>( HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		}
		/* EAccounts */
		
		/* Favorites EShop */
		@GetMapping
		@JsonView( Views.Public.class)
		@RequestMapping( "/logged-user/favorites-e-shops")
		public ResponseEntity<?> loadUserFavEShop( @RequestParam( name="page", defaultValue="1") final Integer page,
												   @RequestParam( name="pageSize", defaultValue="5") final Integer pageSize)
		{
			try
			{
				/* Kick if User is not logged */
				if( isAnonymous())
					return Tools.unauthorized();
				
				/* Get Logged User */
				User user = getLoggedUserFromPrincipal();
				
				if( user.hasRole( "ROLE_ADMIN"))
					return Tools.unauthorized();
				
				return Tools.handlePagedListJSON( pRESTAPI.loadFavoritesEShops( user.getUserInfo().getId(), page, pageSize));
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		@PostMapping
		@JsonView( Views.Public.class)
		@RequestMapping( "/logged-user/add-favorite-e-shop")
		public ResponseEntity<?> addEShopToFavorites( @RequestParam( name="id", required=true) final Long id)
		{
			try
			{
				/* Kick if User is not logged */
				if( pRESTAPI.loadEShop(id) == null)
					return Tools.entityNotFound();
				
				if( isAnonymous())
					return Tools.unauthorized();
				
				/* Get Logged User */
				User user = getLoggedUserFromPrincipal();
				
				if( user.hasRole( "ROLE_ADMIN"))
					return Tools.unauthorized();
				
				pRESTAPI.addEShopToUserFav( getLoggedUserFromPrincipal().getUserInfo().getId(), id);
				
				return Tools.ok();
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		@PostMapping
		@JsonView( Views.Public.class)
		@RequestMapping( "/logged-user/remove-favorite-e-shop")
		public ResponseEntity<?> removeEShopFromFavorites( @RequestParam( name="id", required=true) final Long id)
		{
			try
			{
				/* Kick if User is not logged */
				if( pRESTAPI.loadEShop(id) == null)
					return Tools.entityNotFound();
				
				if( isAnonymous())
					return Tools.unauthorized();
				
				/* Get Logged User */
				User user = getLoggedUserFromPrincipal();
				
				if( user.hasRole( "ROLE_ADMIN"))
					return Tools.unauthorized();
				
				pRESTAPI.removeEShopFromUserFav( getLoggedUserFromPrincipal().getUserInfo().getId(), id);
				
				return Tools.ok();
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		/* Password Recovery & Account Validation */
		@PostMapping
		@RequestMapping( "/logged-user/change-password")
		public ResponseEntity<?> changePassword( @RequestBody final PasswordChangeForm entity)
		{
			try
			{
				if( isAnonymous())
					return Tools.unauthorized();
				
				List<FieldErrorResource> fErrors = new ArrayList<FieldErrorResource>();
				
				/* Level One Checks */
				if( entity.getCurrentPassword() == null || entity.getCurrentPassword().isEmpty())
					fErrors.add( new FieldErrorResource( "PasswordChangeForm", "currentPassword", "Required", "This field is mandatory."));
				if( entity.getPassword() == null || entity.getPassword().isEmpty())
					fErrors.add( new FieldErrorResource( "PasswordChangeForm", "password", "Required", "This field is mandatory."));
				if( entity.getConfirmPassword() == null || entity.getConfirmPassword().isEmpty())
					fErrors.add( new FieldErrorResource( "PasswordChangeForm", "confirmPassword", "Required", "This field is mandatory."));
				if( !fErrors.isEmpty())
					return Tools.handleMultipleFieldErrors(fErrors);
				
				/* Level 2 Checks */
				if( entity.getPassword().length() < 8)
					fErrors.add( new FieldErrorResource( "PasswordChangeForm", "password", "Minlength", "This field must be at least 8 characters long."));
				if( entity.getConfirmPassword().length() < 8)
					fErrors.add( new FieldErrorResource( "PasswordChangeForm", "password", "Minlength", "This field must be at least 8 characters long."));
				if( !fErrors.isEmpty())
					return Tools.handleMultipleFieldErrors(fErrors);
				
				/* Level 3 Checks */
				if( !entity.getPassword().equals( entity.getConfirmPassword()))
					fErrors.add( new FieldErrorResource( "PasswordChangeForm", "password", "Mismatch", "The passwords don't match."));
				if( !fErrors.isEmpty())
					return Tools.handleMultipleFieldErrors(fErrors);
					
				/* Level 4 Checks */
				User user = pRESTAPI.loadUser( getLoggedUserFromPrincipal().getUserInfo().getId());
				
				CustomPasswordEncoder passwordEncoder = new CustomPasswordEncoder();
				
				if( !passwordEncoder.matches( user.getSalt() + entity.getCurrentPassword(), user.getPassword())) 
					fErrors.add( new FieldErrorResource( "PasswordChangeForm", "currentPassword", "Invalid", "This password is not valid."));
				if( !fErrors.isEmpty())
					return Tools.handleMultipleFieldErrors(fErrors);
				
				user.setSalt( RandomStringUtils.random( 32, true, true));
				/* Encode new password */
				user.setPassword( passwordEncoder.encode( user.getSalt() + entity.getPassword()));
				
				pRESTAPI.updateUser(user);
				
				return Tools.ok();
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		@PostMapping
		@RequestMapping( "/request-password")
		public ResponseEntity<?> requestPassword( @RequestBody final PWRequest request)
		{
			try
			{	
				List<FieldErrorResource> fErrors = new ArrayList<FieldErrorResource>();
				
				if( request.getEmail() == null || request.getEmail().isEmpty())
					fErrors.add( new FieldErrorResource( "PWRequest", "email", "NotNull", "This field is mandatory."));
				if( request.getCaptcha() == null || request.getCaptcha().isEmpty())
					fErrors.add( new FieldErrorResource( "PWRequest", "captcha", "NotNull", "This field is mandatory."));
				
				if( !fErrors.isEmpty())
					return Tools.handleMultipleFieldErrors(fErrors);

				if( !ReCaptchaVerificationServiceImpl.verify( request.getCaptcha()))
					fErrors.add( new FieldErrorResource( "PWRequest", "captcha", "Invalid", "This catcha is invalid."));

				if( !fErrors.isEmpty())
					return Tools.handleMultipleFieldErrors(fErrors);
				
				User user = pRESTAPI.loadUserByEMail( request.getEmail());
				
				if( user == null || user.hasRole( "ROLE_ADMIN")) {
					return Tools.handleSingleFieldError(  new FieldErrorResource( "user", "email", "NotFound", "not found"));
				}
				
				user.setConfToken( Tools.generateConfToken());
				
				pRESTAPI.updateUser( user);
				
				/* Fire Email */
				emails.passwordResetRequest(user);
				
				return Tools.ok();
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		@PostMapping
		@RequestMapping( "/reset-password")
		public ResponseEntity<?> resetPassword( @RequestParam( name="token", required=true) final String token,
												@RequestParam( name="pw", required=true) final String pw,
												@RequestParam( name="pwConf", required=true) final String pwConf)
		{
			try
			{
				User user = pRESTAPI.loadUserByToken(token);
				
				if( user == null || user.hasRole( "ROLE_ADMIN")) {
					return Tools.handleSingleFieldError( new FieldErrorResource( "user", "token", "NotFound", "not found"));
				}
				
				if( !pw.equals( pwConf)) {
					return Tools.handleSingleFieldError( new FieldErrorResource( "user", "password", "Mismatch", "mismatch"));
				}
				
				if( pw.length() < 8) {
					return Tools.handleSingleFieldError( new FieldErrorResource( "user", "password", "MinLength", "must be at least 8 characters"));
				}
				
				//Generating salt and confirmation token for user
				user.setSalt( RandomStringUtils.random( 32, true, true));

				//Encoding password using CustomPasswordEncoder
				CustomPasswordEncoder passwordEncoder = new CustomPasswordEncoder();
				user.setPassword( passwordEncoder.encode( user.getSalt() + pw));
				
				/* No more token */
				user.setConfToken( null);
				pRESTAPI.updateUser( user);
				
				emails.passwordUpdated(user);
				
				return Tools.ok();
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		@PostMapping
		@RequestMapping( "/validate-account")
		public ResponseEntity<?> validateAccount( @RequestParam( name="token", required=true) final String token)
		{
			try
			{
				User user = pRESTAPI.loadUserByToken(token);
				
				if( user == null || user.hasRole( "ROLE_ADMIN") || pRESTAPI.loadUserByToken( token) == null) {
					return Tools.handleSingleFieldError( new FieldErrorResource( "user", "token", "NotFound", "not found"));
				}
				/* No more token */
				user.setIsEnabled( true);
				user.setConfToken( null);
				pRESTAPI.updateUser( user);
				
				
				return Tools.ok();
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		@PostMapping
		@RequestMapping( "/request-upgrade")
		public ResponseEntity<?> requestUpgradeToManager()
		{
			try
			{
				if( isAnonymous())
					return Tools.unauthorized();
				
				User loggedUser = getLoggedUserFromPrincipal();
				
				if( !loggedUser.hasRole( "ROLE_BUYER"))
					return Tools.handleSingleFieldError( new FieldErrorResource( "user", "role", "NotAllowed", "This account doesn't meet the requirements for this operation"));
				
				if( loggedUser.hasRole( "ROLE_MANAGER"))
					return Tools.handleSingleFieldError( new FieldErrorResource( "user", "role", "AlreadyManager", "This account is already MANAGER"));
				
				if( loggedUser.getProfileCompletion() < 100) {
					return Tools.handleSingleFieldError( new FieldErrorResource( "user", "profile", "Incomplete", "This account's profile is not complete. Current completion level is: " + loggedUser.getProfileCompletion() + " %"));
				}
				
				if( pRESTAPI.hasAlreadyRequested( loggedUser.getUserInfo().getId()))
					return Tools.handleSingleFieldError( new FieldErrorResource( "user" , "request", "AlreadyRequested", "This account has already requested upgrade. Please wait."));
				
				pRESTAPI.addUpgradeRequest( loggedUser.getUserInfo().getId(), new UpgradeRequest());
				
				return Tools.ok();
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		/* ArticleOrders */
		@PostMapping
		@RequestMapping( "/article-order/add")
		public ResponseEntity<?> registerArticleOrder( @RequestParam( name="entities", required=true) final String entities,
													   @RequestParam( name="eAccountId", required=true) final long eAccountId,
													   @RequestParam( name="paymentRef", required=true) final String paymentRef,
													   @RequestParam( name="adminEAccountId", required=true) final long adminEAccountId,
													   @RequestParam( name="useDefaultDeliveryAddress", required=true) final boolean useDefaultDeliveryAddress,
													   @RequestParam( name="deliveryAddress", required=true) final String deliveryAddress)
		{
			try
			{
				if( isAnonymous())
					return Tools.unauthorized();
				
				if( !getLoggedUserFromPrincipal().hasRole( "ROLE_BUYER"))
					return Tools.forbidden();
				
				User user = pRESTAPI.loadUser( getLoggedUserFromPrincipal().getUserInfo().getId());
				
				if( user.getProfileCompletion() < 100 || !user.isApproved())
					return Tools.handleSingleFieldError( new FieldErrorResource( "user", "profile", "NotApproved", "This account's profile was not validated by admin."));
				
				ObjectMapper mapper = new ObjectMapper();
				
				Collection<PreArticleOrder> preOrders = mapper.readValue(entities, new TypeReference<Collection<PreArticleOrder>>() {} );
				
				/* Payment expected total */
				Float total = 0F;

				/* Map Buyer used EAccount */
				EAccount usedEAccount = pRESTAPI.loadEAccount( eAccountId);
				/* Map Admin used EAccount */
				EAccount adminUsedEAccount = pRESTAPI.loadEAccount( adminEAccountId);
				
				Payment payment = new Payment( total, paymentRef, restBL.getPaymentType( 1L));
				/* Set User and Relative Id */
				payment.setUser(user);
				payment.setRelId( pRESTAPI.loadUserPayments( user.getUserInfo().getId(), 1, 0, 0, true).getItemsNumber() + 1L);
				
				pRESTAPI.addPayment( payment, usedEAccount.getId(), adminUsedEAccount.getId());
				
				/* Notify Admin */
				notifications.newRegisteredPayment(payment);
				
				for( PreArticleOrder preOrder : preOrders)
				{
					ArticleOrder articleOrder = new ArticleOrder( preOrder);
					
					/* Persist Article Order */
					articleOrder.setStatus( OrderStatus.PENDING_PAYMENT);
					articleOrder.setBuyerRelId( pRESTAPI.loadBuyerArticleOrders( getLoggedUserFromPrincipal().getUserInfo().getId(), 1, 0, OrderStatus.ALL, true).getItemsNumber() + 1L);
					articleOrder.seteShopRelId( pRESTAPI.loadEShopArticleOrders( preOrder.geteShop().getId(), 1, 0, OrderStatus.ALL, true).getItemsNumber() + 1L);

					/* Set Delvery Address */
					if( useDefaultDeliveryAddress)
						articleOrder.setDeliveryAddress( getLoggedUserFromPrincipal().getUserInfo().getFullAddress());
					else
						articleOrder.setDeliveryAddress( deliveryAddress);
					
					pRESTAPI.addArticleOrderToUser( getLoggedUserFromPrincipal().getUserInfo().getId(), articleOrder);
					Collection<OrderedArticle> orderedArticles = new ArrayList<OrderedArticle>( preOrder.getOrderedArticles());
					preOrder.getOrderedArticles().clear();
					
					for( OrderedArticle orderedArticle : orderedArticles)
					{
						/* Perists Ordered Article if it doesn't come from Cart */
						if( orderedArticle.getId() == null)
						{
							/* Persist OrderedArticle if not already*/
							orderedArticle.setArticleOrder( articleOrder);
							
							for( OrderedArticleFeature orderedArticleFeature : orderedArticle.getOrderedArticleFeatures())
							{
								if( orderedArticleFeature.getId() == null)
									pRESTAPI.addOrderedArticleFeature( orderedArticleFeature);
							}
							
							pRESTAPI.addOrderedArticle( orderedArticle);
							articleOrder.addOrderedArticle( orderedArticle);
							
							total += ( orderedArticle.getArticle().getPrice() * orderedArticle.getQuantity());
						}
						else
						{
							/* Attach articleOrder to OrderedArticle*/
							orderedArticle.setArticleOrder( articleOrder);
							/* Persist Ordered Article */
							pRESTAPI.addOrderedArticle( orderedArticle);
							/* Link OrderedArticle to ArticleOrder */
							articleOrder.addOrderedArticle( orderedArticle);
							
							/* Compute total */
							if( orderedArticle.getArticle().getIsDiscounted())
								total += (( orderedArticle.getArticle().getDiscountPrice() + orderedArticle.getArticle().getDeliveryFee()) * orderedArticle.getQuantity());
							else
								total += (( orderedArticle.getArticle().getPrice() + orderedArticle.getArticle().getDeliveryFee()) * orderedArticle.getQuantity());
						}
					}

					articleOrder.addPayment( payment);
					articleOrder.setStatus(OrderStatus.PENDING_PAYMENT_CONFIRMATION);
					
					/* Update ArticleOrders with OrderedArticle */
					pRESTAPI.updateArticleOrder(articleOrder);
					
					payment.addArticleOrder(articleOrder);
					payment.setAmount(total);
					pRESTAPI.updatePayment( payment);
					
					/* Delete CartItems if PreORders come from it */
					/* TODO
					 * Checks if legit owner
					 */
					if( !preOrder.getCartItemIds().isEmpty())
					{
						for( long cartItemId : preOrder.getCartItemIds())
							pRESTAPI.removeCartItemFromUserCart( getLoggedUserFromPrincipal().getUserInfo().getId(), cartItemId);
						
						/* Websocket Notifyf */
						websocketService.userCartUpdated( getLoggedUserFromPrincipal().getUsername());
					}
					
					emails.registeredArticleOrder(articleOrder);

					websocketService.articleOrderToConfirm( articleOrder.getId());
					/* Notify Admin of new Order */
//					notifications.entityPayConfReady(articleOrder);
				}
				
				return Tools.created();
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		/* Articles */
		@GetMapping
		@RequestMapping( "/logged-user/visited-articles")
		public ResponseEntity<?> loadUserVisitedArticle( @RequestParam( name="page", defaultValue="1") final Integer page,
														 @RequestParam( name="pageSize", defaultValue="6") final Integer pageSize,
														 @RequestParam( name="orderByIdAsc", defaultValue="false") final boolean orderByIdAsc)
		{
			try
			{
				/* Kick if User is not logged */
				if( isAnonymous())
					return Tools.unauthorized();
				
				/* Get Logged User */
				User user = getLoggedUserFromPrincipal();
				
				if( user.hasRole( "ROLE_ADMIN"))
					return Tools.unauthorized();
				
				return Tools.handlePagedListJSON( pRESTAPI.loadUserVisitedArticles(user.getUserInfo().getId(), page, pageSize, orderByIdAsc));
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		@PostMapping
		@JsonView( Views.Public.class)
		@RequestMapping( "/logged-user/add-visited-article")
		public ResponseEntity<?> addVisitedArticle( @RequestParam( name="id", required=true) final Long id)
		{
			try
			{
				if( isAnonymous())
					return Tools.unauthorized();
				
				Article article = pRESTAPI.loadArticle(id);
				/* Kick if User is not logged */
				if( article == null)
					return Tools.entityNotFound();
				
				/* Get Logged User */
				User user = getLoggedUserFromPrincipal();
				
				if( user.hasRole( "ROLE_ADMIN"))
					return Tools.forbidden();
				
				VisitedArticle entity = new VisitedArticle();
				entity.setUser( pRESTAPI.loadUser( getLoggedUserFromPrincipal().getUserInfo().getId()));
				entity.setArticleId( article.getId());
				
				pRESTAPI.addVisitedArticle(entity);
				
				return Tools.ok();
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		/* Notifications */
		@GetMapping
		@JsonView( Views.Public.class)
		@RequestMapping( "/logged-user/notifications")
		public ResponseEntity<?> loadNotifications( @RequestParam( name="page", defaultValue="1") final Integer page,
												    @RequestParam( name="pageSize", defaultValue="8") final Integer pageSize,
												    @RequestParam( name="status", defaultValue="0") final Integer status,
												    @RequestParam( name="orderByIdAsc", defaultValue="false") final boolean orderByIdAsc,
												    @RequestParam( name="restrict", defaultValue="0") final Integer restrict)
		{
			try
			{
				/* Kick if User is not logged */
				if( isAnonymous())
					return Tools.unauthorized();
				
				/* Get Logged User */
				User user = getLoggedUserFromPrincipal();

				return Tools.handlePagedListJSON( pRESTAPI.loadNotifications(user.getUserInfo().getId(), status, page, pageSize, orderByIdAsc, restrict));
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		@PutMapping
		@JsonView( Views.Public.class)
		@RequestMapping( "/logged-user/notification/{id}/read")
		public ResponseEntity<?> readNotification( @PathVariable( "id") final Long id)
		{
			try
			{
				/* Kick if User is not logged */
				if( isAnonymous())
					return Tools.unauthorized();

				Notification entity = pRESTAPI.loadNotification(id);
				
				if( entity == null)
					return Tools.entityNotFound();
				
				entity.setRead(true);
				pRESTAPI.updateNotification(entity);
				
				return Tools.ok();
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		@GetMapping
		@JsonView( Views.Public.class)
		@RequestMapping( "/logged-user/read-notifications")
		public ResponseEntity<?> readNotifications( @RequestBody final long[] ids)
		{
			try
			{
				/* Kick if User is not logged */
				if( isAnonymous())
					return Tools.unauthorized();
				
				/*
				 * TODO
				 * Check if buyer owns cart item
				 */
				System.out.println( "DEBUG: Long[] size: " + ids.length);
				
				for( long tmp : ids)
				{
					Notification n = pRESTAPI.loadNotification(tmp);
					n.setRead( true);
					
					pRESTAPI.updateNotification( n);
				}
				
				return Tools.ok();
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		/* Rating */
		@PostMapping
		@RequestMapping( "/ordered-article/{id}/rate")
		public ResponseEntity<?> rateArticle( @PathVariable( name="id") final Long id,
											  @RequestBody final Integer rating)
		{
			if( isAnonymous())
				return Tools.unauthorized();
			
			/* Check if User owns Ordered Article */
			try
			{
				OrderedArticle entity = restBL.getOrderedArticle( id);

				//Return entity not found
				if( entity == null)
					return Tools.entityNotFound();
				
				//Return JSON to say already rated
				if( entity.getRating() != null)
					return new ResponseEntity<Void>( HttpStatus.BAD_REQUEST);
				
				if( rating < 0 || rating > 5)
				{
					return Tools.handleSingleFieldError(new FieldErrorResource("Rating", "rating", "Invalid", "Rating must be an Integer in [0;5]"));
				}
				
				entity.setRating( rating);
				restBL.editOrderedArticle(entity);
				
				return Tools.ok();
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		/* Payment */
		@GetMapping
		@JsonView( Views.Public.class)
		@RequestMapping( "/logged-user/payments")
		public ResponseEntity<?> loadUserPayments( @RequestParam( name="page", defaultValue="1") final Integer page,
												   @RequestParam( name="pageSize", defaultValue="10") final Integer pageSize,
												   @RequestParam( name="status", defaultValue="0") final int status,
												   @RequestParam( name="orderByIdAsc", defaultValue="true") final boolean orderByIdAsc) 
		{
			try
			{
				if( isAnonymous())
					return Tools.unauthorized();
				
				return Tools.handlePagedListJSON( pRESTAPI.loadUserPayments( getLoggedUserFromPrincipal().getUserInfo().getId(), status, page, pageSize, orderByIdAsc));
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
	/* End User */
	
	/* EAccount */
	@PostMapping
	@RequestMapping( "/logged-user/e-account/add")
	public ResponseEntity<?> addEAccountToLoggedUser( @RequestBody final EAccount entity)
	{
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			
			boolean hasMatch = false;
			
			/* Check if mobile matches any EMP */
			//Check if mobile matches any EMP
			@SuppressWarnings("unchecked")
			List<EMoneyProvider> emps = (List<EMoneyProvider>) restBL.listEMoneyProviders( 1, 0).getEntities();
			
			Long empId = 0L;
			for( EMoneyProvider emp : emps)
			{
				if( entity.getAccount().matches( emp.getNumPattern()))
				{
					empId = emp.getId();
					hasMatch = true;
				}
			}

			if( !hasMatch)
				return Tools.handleSingleFieldError( new FieldErrorResource( "EAccount", "number", "Invalid", "This account is not supported by the system."));

			if( pRESTAPI.isEAccountNumberTaken( entity.getAccount()))
				return Tools.handleSingleFieldError( new FieldErrorResource( "EAccount", "number", "Conflict", "This account is already taken."));

			EAccount safeEntity = new EAccount();
			
			if( pRESTAPI.isEAccountNumberRegistered( entity.getAccount()))
			{
				safeEntity = pRESTAPI.loadEAccountByNumber( entity.getAccount());
			}
			else
			{
				safeEntity.setAccount( entity.getAccount());
			}
			
			safeEntity.setRelId( getLoggedUserFromPrincipal().getUserInfo().geteAccounts().size() + 1L);
			safeEntity.setUser( pRESTAPI.loadUserInfo( getLoggedUserFromPrincipal().getUserInfo().getId()));
			
			if( entity.getIsDefault())
			{
				safeEntity.setIsDefault(true);
				for( EAccount eAccount : getLoggedUserFromPrincipal().getUserInfo().geteAccounts())
				{
					eAccount.setIsDefault( false);
					pRESTAPI.updateEAccount( eAccount);
				}
			}
			
			if( pRESTAPI.isEAccountNumberRegistered( entity.getAccount()))
			{
				UserInfo userInfo = pRESTAPI.loadUserInfo( getLoggedUserFromPrincipal().getUserInfo().getId());
				
				safeEntity.setUser( userInfo);
				safeEntity.setRelId( userInfo.geteAccounts().size() +1L);
				
				pRESTAPI.updateEAccount( safeEntity);
				
				userInfo.addEAccount( safeEntity);
				
				pRESTAPI.updateUserInfo( userInfo);
			}
			else
			{
				pRESTAPI.addEAccountToUserInfo( getLoggedUserFromPrincipal().getUserInfo().getId(), empId, safeEntity);
			}
			
			websocketService.userProfileUpdated( getLoggedUserFromPrincipal().getUsername());
			
			return Tools.created();
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@PostMapping
	@RequestMapping( "/logged-user/e-account/{id}/delete")
	public ResponseEntity<?> deleteEAccountFromLoggedUser( @PathVariable( "id") final Long id)
	{
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			
			if( pRESTAPI.loadEAccount(id) == null)
				return Tools.entityNotFound();
			
			boolean isOwner = false;
			
			for( EAccount eAccount : pRESTAPI.loadUser( getLoggedUserFromPrincipal().getUserInfo().getId()).getUserInfo().geteAccounts())
			{
				if( eAccount.getId() == id)
					isOwner = true;
			}
			
			if( !isOwner)
				return Tools.unauthorized();
			
			UserInfo userInfo = pRESTAPI.loadUserInfo( getLoggedUserFromPrincipal().getUserInfo().getId());
			userInfo.removeEAccount( pRESTAPI.loadEAccount(id));
			
			pRESTAPI.updateUserInfo( userInfo);
			
			EAccount eAccount = pRESTAPI.loadEAccount(id);
			eAccount.setUser( null);
			eAccount.setIsDefault( false);
			eAccount.setRelId( 0L);
			
			pRESTAPI.updateEAccount( eAccount);
			
			websocketService.userProfileUpdated( getLoggedUserFromPrincipal().getUsername());
			
			return Tools.ok();
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@PostMapping
	@RequestMapping( "/logged-user/e-account/{id}/set-default")
	public ResponseEntity<?> setEAccountAsDefault( @PathVariable( "id") final Long id)
	{
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			
			if( pRESTAPI.loadEAccount(id) == null)
				return Tools.entityNotFound();
			
			boolean isOwner = false;
			
			for( EAccount eAccount : getLoggedUserFromPrincipal().getUserInfo().geteAccounts())
			{
				if( eAccount.getId() == id)
					isOwner = true;
			}
			
			if( !isOwner)
				return Tools.unauthorized();
			
			for( EAccount eAccount : pRESTAPI.loadUserInfo( getLoggedUserFromPrincipal().getUserInfo().getId()).geteAccounts())
			{
				if( eAccount.getId().longValue() == id.longValue())
					eAccount.setIsDefault(true);
				else
					eAccount.setIsDefault( false);
				
				pRESTAPI.updateEAccount(eAccount);
			}
			
			websocketService.userProfileUpdated( getLoggedUserFromPrincipal().getUsername());
			
			return Tools.ok();
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@GetMapping
	@JsonView( Views.Public.class)
	@RequestMapping( "e-accounts-for-payments")
	public ResponseEntity<?> loadAdminEAccountForPayments( @RequestParam( name="page", defaultValue="1") final Integer page,
													  @RequestParam( name="pageSize", defaultValue="5") final Integer pageSize)
	{
		try
		{
			/* Kick if User is not logged */
			if( isAnonymous())
				return Tools.unauthorized();
			
			return Tools.handlePagedListJSON( pRESTAPI.loadAdminEAccountsForPayments(page, pageSize));
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	/* End EAccount */
	
	/* Slide ( Slides) */
	@GetMapping
	@JsonView( Views.Public.class)
	@RequestMapping( value="/slides")
	public ResponseEntity<?> loadSlides( @RequestParam( name="page", defaultValue="1") final Integer page,
									     @RequestParam( name="pageSize", defaultValue="0") final Integer pageSize,
									     @RequestParam( name="orderByDispOrderAsc", defaultValue="true") final boolean orderByDispOrderAsc)
	{
		try
		{
			return Tools.handlePagedListJSON( pRESTAPI.loadDisplayedHomePictures(1, 0));	
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@GetMapping
	@RequestMapping( value="/slide/{id}/fetch-picture", produces=MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<?> fetchSlidePicture( @PathVariable Long id) throws Exception
	{	
		Slide entity = pRESTAPI.loadSlide(id);
		
		if( entity == null) 
			return Tools.entityNotFound();
			
		File file = new File( entity.getFilename());

		return new ResponseEntity<byte[]>( IOUtils.toByteArray( new FileInputStream( file)), HttpStatus.OK);
	}
	/* End Slide */
	
	/* Login */
	@PostMapping
	@RequestMapping( value="/admin/login")
	public ResponseEntity<Map<String, Object>> adminLogin( @RequestBody UserLogin userLogin )
	{
		User user = restBL.getUserByUsername( userLogin.getUsername());

		Map<String, Object> result = new HashMap<String, Object>();

		CustomPasswordEncoder passwordEncoder = new CustomPasswordEncoder();

		if (user == null || !user.getUsername().equalsIgnoreCase(userLogin.getUsername()))
		{
			result.put( "code", 0);
			result.put( "message", "Invalid Username / Password");

			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.UNAUTHORIZED);
		}
		//Control on password
		if( !passwordEncoder.matches( user.getSalt() + userLogin.getPassword(), user.getPassword())) 
		{
			result.put( "code", 0);
			result.put( "errorMessage", "Invalid Username / Password");
			return new ResponseEntity<Map<String, Object>>( result, HttpStatus.UNAUTHORIZED);
		}
		//Checks is Accout is enabled
		if( !user.hasRole( "ROLE_ADMIN"))
		{
			result.put( "code", -2);
			result.put( "message", "Unauthorized");

			return new ResponseEntity<Map<String, Object>>( result, HttpStatus.UNAUTHORIZED);
		}
		//Checks is Accout is enabled
		if( !user.getIsEnabled())
		{
			result.put( "code", -1);
			result.put( "message", "Account disabled");

			return new ResponseEntity<Map<String, Object>>( result, HttpStatus.UNAUTHORIZED);
		}

		Collection<? extends GrantedAuthority> authorities = user.getRoles();

		Authentication auth = new UsernamePasswordAuthenticationToken(user, userLogin.getPassword(), authorities);

		SecurityContextHolder.getContext().setAuthentication( auth);

		result.put( "code", 1);
		result.put( "message", "Success");

		//Get the successfully logged user
		User loggedUser = (User) auth.getPrincipal();

		//Instantiate and feed the logged User proxy which will be returned by the web service in case of
		//successful login
		LoggedUser currentUser = new LoggedUser();
		currentUser.setUserInfo(loggedUser.getUserInfo());
		currentUser.setUsername( loggedUser.getUsername());

		result.put( "user", loggedUser);

		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
	@PostMapping
	@RequestMapping( value="/login")
	public ResponseEntity<Map<String, Object>> buyerLogin( @RequestBody UserLogin userLogin )
	{
		User user = restBL.getUserByUsername( userLogin.getUsername());

		Map<String, Object> result = new HashMap<String, Object>();

		CustomPasswordEncoder passwordEncoder = new CustomPasswordEncoder();

		if (user == null || !user.getUsername().equalsIgnoreCase(userLogin.getUsername()))
		{
			result.put( "code", 0);
			result.put( "message", "Invalid Username / Password");

			System.out.println( "0");
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.UNAUTHORIZED);
		}
		//Control on password
		if( !passwordEncoder.matches( user.getSalt() + userLogin.getPassword(), user.getPassword())) 
		{
			System.out.println( userLogin.getPassword());
			result.put( "code", 0);
			result.put( "message", "Invalid Username / Password");

			System.out.println( "1");
			return new ResponseEntity<Map<String, Object>>( result, HttpStatus.UNAUTHORIZED);
		}
		//Checks is Accout is enabled
		if( !user.hasRole( "ROLE_BUYER"))
		{
			result.put( "code", -2);
			result.put( "message", "Unauthorized");

			System.out.println( "2");
			return new ResponseEntity<Map<String, Object>>( result, HttpStatus.UNAUTHORIZED);
		}
		//Checks is Accout is enabled
		if( !user.getIsEnabled())
		{
			result.put( "code", -1);
			result.put( "message", "Account disabled");

			System.out.println( "3");
			return new ResponseEntity<Map<String, Object>>( result, HttpStatus.UNAUTHORIZED);
		}

		Collection<? extends GrantedAuthority> authorities = user.getRoles();

		Authentication auth = new UsernamePasswordAuthenticationToken(user, userLogin.getPassword(), authorities);

		SecurityContextHolder.getContext().setAuthentication( auth);

		result.put( "code", 1);
		result.put( "message", "Success");

		//Get the successfully logged user
		User loggedUser = (User) auth.getPrincipal();

		//Instantiate and feed the logged User proxy which will be returned by the web service in case of
		//successful login
		LoggedUser currentUser = new LoggedUser();
		currentUser.setUserInfo(loggedUser.getUserInfo());
		currentUser.setUsername( loggedUser.getUsername());
		
		result.put( "user", loggedUser);

		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
	@GetMapping
	@RequestMapping( value="/logout")
	@JsonView( Views.Public.class)
	public ResponseEntity<Object> logout()
	{
		SecurityContextHolder.getContext().setAuthentication( null);

		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	@GetMapping
	@RequestMapping( value="/logged-user")
	@JsonView( Views.Public.class)
	public ResponseEntity<?> loadLoggedUser()
	{
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			LoggedUser loggedUser = generateLoggedUserJSON( user.getUserInfo().getId());
			
			return new ResponseEntity<Object>( loggedUser, HttpStatus.OK);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	/* End Login */
	
	/* WebSockets */
	@JsonView( Views.Public.class)
	@SendToUser( "/topic/logged-user/article-order/{id}")
	@MessageMapping( "/article-order/{id}")
	public ArticleOrder websocketLoadArticleOrder( @DestinationVariable final Long id) {
		return pRESTAPI.loadArticleOrder(id);
	}
	@JsonView( Views.Public.class)
	@SendToUser( "/topic/logged-user/payment/{id}")
	@MessageMapping( "/payment/{id}")
	public Payment websocketLoadPayment( @DestinationVariable final Long id) {
		return pRESTAPI.loadPayment(id);
	}
	/* End WebSockets */
	
	/* Debug */
	/* TODO Remove all below */
	@GetMapping
	@RequestMapping( "/debug/init-db")
	public ResponseEntity<?> initDB()
	{
		
		/* Init EMPs */
		if( pRESTAPI.loadEMoneyProviderByName( "MTN") == null)
			restBL.addEMoneyProvider( new EMoneyProvider( "MTN","\\+?(?:229)?(?:97|96|61|62|66|67)\\d{6}", null));
		
		if( pRESTAPI.loadEMoneyProviderByName( "Moov") == null)
			restBL.addEMoneyProvider( new EMoneyProvider( "Moov","\\+?(?:229)?(?:95|94)\\d{6}", null));
		
		/* Init roles */
		Collection<String> roles = new ArrayList<String>();
		roles.add( "ROLE_BUYER");
		roles.add( "ROLE_MANAGER");
		roles.add( "ROLE_ADMIN");
		roles.add( "ROLE_SUPER_ADMIN");
		
		for( String role : roles)
		{
			if( restBL.getRole( role) == null)
				restBL.addRole( new Role( role));
		}
		
		/* Init Payment Types */
		if( restBL.getPaymentType( 1L) == null)
		{
			restBL.addPaymentType( new PaymentType( "En ligne - Monnaie Numérique"));
		}
		
		/* Create Default Admin User */
		if( pRESTAPI.loadUserByUsername( "admin0") == null)
		{
			try
			{
				//Register UserInfo for New User
				UserInfo userInfo = new UserInfo();
				userInfo.setEmail( "admin0@admins.com");
				pRESTAPI.addUserInfo( userInfo);
				
				EAccount eAccount = new EAccount();
				eAccount.setAccount( "97000097");
				eAccount.setRelId( userInfo.geteAccounts().size() + 1L);
				pRESTAPI.addEAccountToUserInfo(userInfo.getId(), pRESTAPI.loadEMoneyProviderByName( "MTN").getId(), eAccount);
				
				EAccount eAccount2 = new EAccount();
				eAccount2.setAccount( "95000095");
				eAccount2.setRelId( userInfo.geteAccounts().size() + 1L);
				pRESTAPI.addEAccountToUserInfo( userInfo.getId(), pRESTAPI.loadEMoneyProviderByName( "Moov").getId(), eAccount2);
				
				//Set base for User
				User user = new User();
				user.setUserInfo( userInfo);
				user.setUsername( "admin0");
				
				//Adding Roles to user
				user.addRole( pRESTAPI.loadRole( "ROLE_ADMIN"));
				
				//Generating salt and confirmation token for user
				user.setSalt( RandomStringUtils.random( 32, true, true));
				//user.setConfToken( RandomStringUtils.random( 64, true, true));
				
				//Encoding password using CustomPasswordEncoder
				CustomPasswordEncoder passwordEncoder = new CustomPasswordEncoder();
				user.setPassword( passwordEncoder.encode( user.getSalt() + "admin0"));
				
				//Admin is enabled by default
				user.setIsEnabled( true);
				
				pRESTAPI.addUser( user);
			}
			catch( Exception e)
			{
				e.printStackTrace();
			}try
			{
				if (!aRESTAPI.isSubOfferRegistered("Discovery Basic - 1 Semaine"))
				{
					System.out.print( "DEBUG: SubOffer Not Found");
					
					if (!aRESTAPI.isShopStatusRegistered("Basic"))
					{
						ShopStatus s = new ShopStatus();
						s.setArticleLimit(10);
						s.setDescription("Une boutique basique");
						s.setIsEnabled(true);
						s.setTitle("Basic");

						aRESTAPI.addShopStatus(s);
					}

					SubOffer s = new SubOffer();
					s.setDuration(1);
					s.setDurationType(DurationType.WEEK);
					s.setPrice(0F);
					s.setTitle("Discovery Basic - 1 Semaine");
					s.setIsEnabled(true);
					s.setShopStatus(aRESTAPI.loadShopStatus("Basic"));

					aRESTAPI.addSubOffer(s);
				} 
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			
			try
			{
				if (!aRESTAPI.isSubOfferRegistered("Discovery Basic - 1 Minute"))
				{
					if (!aRESTAPI.isShopStatusRegistered("Basic"))
					{
						ShopStatus s = new ShopStatus();
						s.setArticleLimit(10);
						s.setDescription("Une boutique basique");
						s.setIsEnabled(true);
						s.setTitle("Basic");

						aRESTAPI.addShopStatus(s);
					}

					SubOffer s = new SubOffer();
					s.setDuration(1);
					s.setDurationType(DurationType.MINUTE);
					s.setPrice(0F);
					s.setTitle("Discovery Basic - 1 Minute");
					s.setIsEnabled(true);
					s.setShopStatus(aRESTAPI.loadShopStatus("Basic"));

					aRESTAPI.addSubOffer(s);
				} 
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			
			try
			{
				if (!aRESTAPI.isAdvOptionRegistered("Pub Basic - 1 Minute - Code 1"))
				{
					AdvOption a = new AdvOption();
					a.setDuration(1);
					a.setDurationType(DurationType.MINUTE);
					a.setIsEnabled(true);
					a.setPrice(0F);
					a.setTitle("Pub Basic - 1 Minute - Code 1");

					aRESTAPI.addAdvOption(a);
				} 
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		return new ResponseEntity<String>( "ok", HttpStatus.OK);
	}
	@GetMapping
	@RequestMapping( "/debug/schedule")
	public ResponseEntity<?> scheduleTest()
	{
		
		return Tools.ok();
	}
	@GetMapping
	@RequestMapping( "/debug/sendgrid")
	public ResponseEntity<?> sendgridApiKeyTest()
	{
		return new ResponseEntity<String>( System.getenv( "SENDGRID_API_KEY"), HttpStatus.OK);
	}
	@GetMapping
	@RequestMapping( "/debug/drive-secret")
	public ResponseEntity<?> checkDriveSecret()
	{
		File file = new File( GoogleDriveServiceBuilder.pathToDriveSecret + "preta-svc-account.p12");
		if( file.exists())
			return new ResponseEntity<String>( "Found", HttpStatus.OK);
		else
			return new ResponseEntity<String>( "NotFound", HttpStatus.OK);
	}
	@GetMapping
	@RequestMapping( "/debug/captcha-secret")
	public ResponseEntity<?> checkCaptchaKey()
	{
		return new ResponseEntity<String>( System.getenv( "GOOGLE_RECAPTCHA_KEY"), HttpStatus.OK);
	}
	@GetMapping
	@RequestMapping( "/debug/init-e-shops/{count}/{articleCount}")
	public ResponseEntity<?> initEshops( @PathVariable(name="count") final int count,
										 @PathVariable( name="articleCount") final int articleCount)
	{	
		if( count < 1)
			return new ResponseEntity<Void>( HttpStatus.BAD_REQUEST);
		if( articleCount < 1)
			return new ResponseEntity<Void>( HttpStatus.BAD_REQUEST);
		
		Random rnd = new Random();
		
		HashMap<String, String> ents = new HashMap<String, String>();
		
		for( int c = 0; c < count; c++) {
			
			/* Generate Username */
			int managerNum;
			String username;
			
			/* Checks if username is free */
			boolean isFree = true;
			do
			{
				managerNum =  Math.abs( rnd.nextInt(500));
				username = "manager" + managerNum;
				if( mRESTAPI.loadUserByUsername(username) != null)
					isFree = false;
				else
					isFree = true;
				
			} while (!isFree);

			System.out.println( "DEBUG: Generated Username => " + username);
			String managerEmail = username + "@managers.com";
			
			//Register UserInfo for New User
			UserInfo userInfo = new UserInfo();
			userInfo.setEmail( managerEmail);
			mRESTAPI.addUserInfo( userInfo);
			
			String phone;
			isFree = true;
			do
			{
				phone = "97" + RandomStringUtils.randomNumeric(6);
				if( pRESTAPI.isEAccountNumberRegistered(phone))
					isFree = false;
				else
					isFree = true;
			} while (!isFree);
			
			System.out.println( "DEBUG: Generated EAccount => " + phone);

			/* EAccount Affectation */
			EAccount eAccount = new EAccount();
			eAccount.setAccount( phone);
			eAccount.setIsDefault( true);
			eAccount.setRelId( 1L);
			
			mRESTAPI.addEAccountToUserInfo( userInfo.getId(), pRESTAPI.loadEMoneyProviderByName( "MTN").getId(), eAccount);

			//Set base for User
			User user = new User();
			user.setUserInfo( userInfo);
			user.setUsername( username);

			//Adding Roles to user
			user.addRole( mRESTAPI.loadRole( "ROLE_BUYER"));
			user.addRole( mRESTAPI.loadRole( "ROLE_MANAGER"));
			user.setIsEnabled(true);
			//Generating salt and confirmation token for user
			user.setSalt( RandomStringUtils.random( 32, true, true));
//			user.setConfToken( RandomStringUtils.random( 64, true, true));

			//Encoding password using CustomPasswordEncoder
			CustomPasswordEncoder passwordEncoder = new CustomPasswordEncoder();
			user.setPassword( passwordEncoder.encode( user.getSalt() + username));

			/* EShop Generation */
			String eshopName;
			
			eshopName = "EShop " + managerNum;
			if( mRESTAPI.loadEShop(eshopName) != null)
				return new ResponseEntity<String>( "Wrong EShop Name", HttpStatus.BAD_REQUEST);
			
			
			System.out.println( "DEBUG: Genertaed EShop Name: " + eshopName);
			String eshopEmail = "eshop" + managerNum + "@eshops.com";
			
			/* Registering the EShop */
			EShop eShop = new EShop();
			eShop.setName( eshopName);
			eShop.setEmail( eshopEmail);
			eShop.setCustomerNum( "Coming Soon");
			eShop.setLogoGoogleId( GoogleDriveServiceImpl.defaultEShopPicId);
			eShop.setRelId( 1L);
			eShop.setIsEnabled(true);
			
			mRESTAPI.addEShop( eShop);

			user.addManagedEShop( eShop);

			mRESTAPI.addUser( user);
			
			/* ShopSub Entity */
			ShopSub sub = new ShopSub();
			sub.seteShop(eShop);
			sub.setRelId( mRESTAPI.loadShopSubsByEShop( eShop.getId(), 1, 0, true).getEntities().size() + 1l);
			sub.setStartDate( new Timestamp( System.currentTimeMillis()));
			sub.setSubOffer( mRESTAPI.loadSubOffer(1L));
			sub.setSubStatus( GenericStatus.ONGOING);
			mRESTAPI.addShopSubToEShop(eShop.getId(), sub);
			
			eShop.setCurrentShopSub(sub);
			
			mRESTAPI.updateEShop(eShop);
			
			String[] nameParts = { "Knob", "Bread", "Fanta", "Beatmania", "SDVX", "WOZ", "Apple", "IPhone", "Sam Sung Galaxios", "Rick", "Morty",
					"Bra", "Pantsu", "Deception", "Arduino", "Terraformars", "Hadoken", "Dakimakura", "Battlefield", "Call of Moron" };
			
			/* Article Picture list  From Drive */
			List<String> list = new ArrayList<String>();
			list.add("0B0W0ktANhIydamNLR0Q2NllDZ2c");
			list.add("0B0W0ktANhIydc1VpY3pNcnNVNWc");
			list.add("0B0W0ktANhIydQmYxZFlxRlg1bVU");
			list.add("0B0W0ktANhIydVS03RzRlU2Vnalk");
			list.add("0B0W0ktANhIydSl9sRWttdUtveDA");
			list.add("0B0W0ktANhIydVmZpZFRpX1JidzQ");
			list.add("0B0W0ktANhIydeUQ3a1RtZUtPMTg");
			list.add("0B0W0ktANhIydTXhGdlMwMlBleUE");
			list.add("0B0W0ktANhIydeHJjS0I3S1plaEk");
			list.add("0B0W0ktANhIydM2ZWNXhtaWhxeGM");
			list.add("0B0W0ktANhIydaGFZRC1mQ256eXc");
			list.add("0B0W0ktANhIydRXMxSFNCS3hLM28");
			list.add("0B0W0ktANhIydZUh4YUdCX3FUU1E");
			list.add("0B0W0ktANhIydek94S0FGLThOVzg");
			list.add("0B0W0ktANhIydUFpSYWZiU2NrYXc");
			list.add("0B0W0ktANhIydS05fRm1nUUdEcms");
			list.add("0B0W0ktANhIydNnBobXliMmd6WUk");
			list.add("0B0W0ktANhIydNU92b251TUlqZGc");
			list.add("0B0W0ktANhIydTjFUWTZBRGJXS3c");
			list.add("0B0W0ktANhIydQUkwZTVOOFlJbjQ");
			list.add("0B0W0ktANhIydTmphUDk3X0NuNjg");
			list.add("0B0W0ktANhIydcko5c0pCNXVqQTQ");
			list.add("0B0W0ktANhIydN2JBSUlBUkZxcGs");
			list.add("0B0W0ktANhIydYmFEQV9hUTlpak0");
			list.add("0B0W0ktANhIydb2NGaVdYUGY4bTg");
			list.add("0B0W0ktANhIydcmtHMW9KS0kwSWc");
			list.add("0B0W0ktANhIydVEpOYVBiWXQ3ZzQ");
			list.add("0B0W0ktANhIydWk9yamladEJNczg");
			list.add("0B0W0ktANhIyddnlmUlltRlkyZ28");
			list.add("0B0W0ktANhIydZERrWHBkT1FaVkk");
			list.add("0B0W0ktANhIydTDlZRE9NeS16WEU");
			list.add("0B0W0ktANhIydSXhIRzRyQlRlaHM");
			list.add("0B0W0ktANhIydZWN0N3NrTHNfMkE");
			list.add("0B0W0ktANhIydSXJrYW14bVZmdVk");
			list.add("0B0W0ktANhIydMThBTV9QS0RXblk");
			list.add("0B0W0ktANhIydNTlBVlFWMzlVSVE");
			list.add("0B0W0ktANhIydT3ZLYnNLM0Jabzg");
			list.add("0B0W0ktANhIydNV9hd3l4U1QzUlk");
			list.add("0B0W0ktANhIydczN2Y3NTQ0dVVkE");
			list.add("0B0W0ktANhIydNktsRlJIY0tmdjQ");
			list.add("0B0W0ktANhIydbnczU1UxWGJWcDQ");
			list.add("0B0W0ktANhIydVXdUWDVfdmtlN1k");
			list.add("0B0W0ktANhIydZXkyVjBjZHZTLTA");
			list.add("0B0W0ktANhIydVjNYMWhkaEtYVTg");
			list.add("0B0W0ktANhIydVmNsNThydjRZdE0");
			list.add("0B0W0ktANhIydek1DbFUzeVZQU2s");
			list.add("0B0W0ktANhIydVTIwbGx3WjR4LVU");
			list.add("0B0W0ktANhIydYXFhNEpfd1FhczQ");
			list.add("0B0W0ktANhIydMXF5WjdHZ3ZMWWs");
			list.add("0B0W0ktANhIydYTN4ekdXQnRWVE0");
			list.add("0B0W0ktANhIyddmZqSlJxZl9SU2M");
			list.add("0B0W0ktANhIydWDVkYS1JNFh4VjQ");
			list.add("0B0W0ktANhIydSjdBaXNPSkJQZzg");
			list.add("0B0W0ktANhIydYk5kT3o5ZTRYRkk");
			list.add("0B0W0ktANhIydLUVlYnJEU3E0blU");
			list.add("0B0W0ktANhIydR0Jjd1pabmFuYVU");
			list.add("0B0W0ktANhIydbWR1RnFISmFHcFk");
			list.add("0B0W0ktANhIydYmJhcS1hWXE3RVU");
			list.add("0B0W0ktANhIydRF9GTFpzcXJ2TDA");
			list.add("0B0W0ktANhIydcGJBYXNoQ3pKazA");
			list.add("0B0W0ktANhIydWXZZS2o1X0U5Wm8");
			list.add("0B0W0ktANhIydLXk3b3AxeU5MamM");
			list.add("0B0W0ktANhIydZlk5dmxPOGotbXM");
			list.add("0B0W0ktANhIydQzloUGhuUXc5Nlk");
			list.add("0B0W0ktANhIydSnpTUjA1WTNUNXM");
			
			SecureRandom randGen = new SecureRandom();
			/* Adding Articles */
			for( int ac = 0; ac < articleCount; ac++)
			{
				Article article = new Article();
				article.setName( nameParts[ Math.abs( randGen.nextInt()) % nameParts.length] + " " +
							     nameParts[ Math.abs( randGen.nextInt()) % nameParts.length] + " " +
							     nameParts[ Math.abs( randGen.nextInt()) % nameParts.length]);
				
				article.setPrice( (Math.abs( randGen.nextInt()) % 10000) + 1F);
				article.setState( ArticleState.NEW);
				article.setDeliveryMode( DeliveryMode.FREE);
				/*article.seteShop( eShops.get( Math.abs( randGen.nextInt()) % eShops.size()));*/
				article.setStock( 100);
				article.setThreshold( 10);
				
				Picture picture = new Picture( "/random/preta/uploads/articles/article_3541017176454806279");
				picture.setGoogleId( list.get(  Math.abs( randGen.nextInt()) % list.size()));
				picture.setIsDefault( true);
				restBL.addPicture( picture);
				article.addPicture(picture);
				
				for( int i = 0; i < Math.abs( randGen.nextInt()) % 4; i++)
				{
					Picture pic = new Picture( "/random/preta/uploads/articles/article_3541017176454806279");
					picture.setGoogleId( list.get(  Math.abs( randGen.nextInt()) % list.size()));
					picture.setIsDefault( false);
					restBL.addPicture( pic);
					article.addPicture(pic);
				}
				
				article.setRelId( mRESTAPI.loadEShopArticles( eShop.getId(), 1, 0).getItemsNumber() + 1L);
				mRESTAPI.addArticleToEShop( eShop.getId(), article);
			}
			
			ents.put( user.getUsername(), eshopName);
		}
		
		return new ResponseEntity<HashMap<String, String>>( ents, HttpStatus.OK);
	}
	/* End Debug */
	
	/* Error Handling */
	@ExceptionHandler( { Exception.class })
	private ResponseEntity<?> exceptionHanlder( Exception e)
	{
		System.err.println( "Exception Caught");
		e.printStackTrace();
		emails.sendErrorReport(e);
		return Tools.internalServerError();
	}
	/* End Error Handling */
	
	/* Tools & DRY */
	private EShop getPreArticleOrderEShop( final PreArticleOrder entity) {		
		EShop eShop = null;
		
		for( OrderedArticle orderedArticle : entity.getOrderedArticles()) {
			eShop = pRESTAPI.getArticleEShop( orderedArticle.getArticle().getId());
		}
		
		return eShop;
	}
	private User getLoggedUserFromPrincipal() {
		if( isAnonymous())
			return null;
		
		return ( User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	private boolean isAnonymous() {
		return SecurityContextHolder.getContext().getAuthentication().getName() == "anonymousUser";
	}
	private LoggedUser generateLoggedUserJSON( final Long userId) {
		
		User user = pRESTAPI.loadUser( userId);
		return new LoggedUser( user.getUserInfo(), user.getUsername(), user.getProfileCompletion(), user.isApproved(), user.getUserInfo().geteAccounts(), user.isManager());
	}
	@SuppressWarnings("unused")
	private void updateLoggedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		SecurityContextHolder.getContext().setAuthentication( new PreAuthenticatedAuthenticationToken( auth.getPrincipal(), auth.getCredentials(), auth.getAuthorities()));
	}
}
