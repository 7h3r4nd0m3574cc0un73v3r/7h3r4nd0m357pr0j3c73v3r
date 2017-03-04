package org.usayi.preta.controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.usayi.preta.Views;
import org.usayi.preta.buzlayer.IManagerRESTAPI;
import org.usayi.preta.config.Tools;
import org.usayi.preta.entities.AdvOffer;
import org.usayi.preta.entities.AdvOption;
import org.usayi.preta.entities.Article;
import org.usayi.preta.entities.ArticleFeature;
import org.usayi.preta.entities.ArticleOrder;
import org.usayi.preta.entities.Category;
import org.usayi.preta.entities.EAccount;
import org.usayi.preta.entities.EShop;
import org.usayi.preta.entities.Feature;
import org.usayi.preta.entities.FeatureValue;
import org.usayi.preta.entities.GenericStatus;
import org.usayi.preta.entities.OrderStatus;
import org.usayi.preta.entities.Payment;
import org.usayi.preta.entities.Picture;
import org.usayi.preta.entities.ShopSub;
import org.usayi.preta.entities.User;
import org.usayi.preta.entities.form.EShopSubscription;
import org.usayi.preta.entities.json.CategoryMultiSelectTreeJSON;
import org.usayi.preta.entities.json.LoggedUser;
import org.usayi.preta.entities.json.PagedListJSON;
import org.usayi.preta.exceptions.FieldErrorResource;
import org.usayi.preta.service.EMailService;
import org.usayi.preta.service.GoogleDriveService;
import org.usayi.preta.service.GoogleDriveUploadType;
import org.usayi.preta.service.NotificationService;
import org.usayi.preta.service.SchedulerService;
import org.usayi.preta.service.WebSocketService;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@Secured( { "ROLE_MANAGER" })
@RequestMapping( path="/rest-api/manager")
public class ManagerRESTAPIController
{

	@Autowired
	private IManagerRESTAPI mRESTAPI;
	
	private final WebSocketService service;
	
	@Autowired
	NotificationService notifications;
	
	@Autowired
	EMailService emails;
	
	@Autowired
	GoogleDriveService drive;
	
	@Autowired
	SchedulerService scheduler;
	
	public ManagerRESTAPIController( WebSocketService service)
	{
		this.service = service;
	}
	
	/*
	 * TODO: Expiring Entities Notification firing
	 * TODO: Firing Article Threshold notificaations 
	 */
	
	/* Manager */
		/* Managed EShops */
		@GetMapping
		@JsonView( Views.Manager.class)
		@RequestMapping( "/logged-user/e-shops")
		public ResponseEntity<?> loadManagedEShops( @RequestParam( name="page", defaultValue="1") final Integer page,
												    @RequestParam( name="pageSize", defaultValue="0") final Integer pageSize)
		{
			try
			{
				if( SecurityContextHolder.getContext().getAuthentication().getPrincipal() == "anonymousUser") 
				{
					return Tools.unauthorized();
				}
				
				User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				
				return Tools.handlePagedListJSON( mRESTAPI.loadManagedEShops( loggedUser.getUserInfo().getId(), page, pageSize));
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		@GetMapping
		@RequestMapping( "/logged-user/e-shops-count")
		public ResponseEntity<?> getManagedEShopsCount()
		{
			try
			{
				if( SecurityContextHolder.getContext().getAuthentication().getPrincipal() == "anonymousUser") 
				{
					return Tools.unauthorized();
				}
				
				User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				
				return new ResponseEntity<Integer>( mRESTAPI.loadManagedEShops( loggedUser.getUserInfo().getId(), 1, 0).getEntities().size(), HttpStatus.OK);
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		/* Articles Orders By Status */
		@GetMapping
		@JsonView( Views.Manager.class)
		@RequestMapping( "/logged-user/article-orders")
		public ResponseEntity<?> loadArticleOrdersByManagerAndByStatus( @RequestParam( name="page", defaultValue="1") final Integer page,
																	    @RequestParam( name="pageSize", defaultValue="0") final Integer pageSize,
																	    @RequestParam( name="orderByIdAsc", defaultValue="true") final boolean orderByIdAsc,
																	    @RequestParam( name="orderStatus", defaultValue="-1") final Integer status)
		{
			try
			{
				if( SecurityContextHolder.getContext().getAuthentication().getPrincipal() == "anonymousUser") 
				{
					return Tools.unauthorized();
				}
				User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				
				OrderStatus orderStatus;
				switch( status)
				{
					case 0:
						orderStatus = OrderStatus.PENDING_PAYMENT;
						break;
					case 1:
						orderStatus = OrderStatus.PENDING_PAYMENT_CONFIRMATION;
						break;
					case 2:
						orderStatus = OrderStatus.PAID;
						break;
					case 3:
						orderStatus = OrderStatus.DELIVERING;
						break;
					case 4:
						orderStatus = OrderStatus.DELIVERED;
						break;
					case -1:
						orderStatus = OrderStatus.ALL;
						break;
					default:
						orderStatus = OrderStatus.ALL;
						break;
				}
				
				return Tools.handlePagedListJSON( mRESTAPI.loadArticlesOrderByManagerAndByStatus( loggedUser.getUserInfo().getId(), page, pageSize, orderByIdAsc, orderStatus));
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return new ResponseEntity<List<ArticleOrder>>( HttpStatus.INTERNAL_SERVER_ERROR);
			}	
		}
		/* End Manager */
	
	/* EShops */
	@GetMapping
	@JsonView( Views.Manager.class)
	@RequestMapping( value="/e-shop/{id}")
	public ResponseEntity<EShop> loadEShop( @PathVariable( "id") final Long id)
	{
		EShop entity = mRESTAPI.loadEShop( id);

		if( entity == null)
			return new ResponseEntity<EShop>( HttpStatus.NOT_FOUND);

		return new ResponseEntity<EShop>( entity, HttpStatus.OK);
	}
	@PostMapping
	@RequestMapping( value="/e-shop/add")
	public ResponseEntity<?> addEShop( @RequestParam( "entity") final String dataString,
									   @RequestParam( name="file", required=false) final MultipartFile file)
	{
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			
			if( !getLoggedUserFromPrincipal().hasRole( "ROLE_MANAGER"))
				return Tools.unauthorized();
			
			User user = mRESTAPI.loadUser( getLoggedUserFromPrincipal().getUserInfo().getId());
			
			ObjectMapper mapper = new ObjectMapper();
			EShopSubscription entity = mapper.readValue( dataString, EShopSubscription.class);
			
			/* Lvl 1 Checks */
			List<FieldErrorResource> fErrors = new ArrayList<FieldErrorResource>();
			EShop eShop = entity.geteShop();
			
			if( eShop == null)
				fErrors.add( new FieldErrorResource( "EShooSubscription", "EShop", "Invalid", "This entity is required"));
			
			if( !fErrors.isEmpty())
				return Tools.handleMultipleFieldErrors(fErrors);
			
			if( file != null)
			{
				if( !Tools.isValidImage(file))
					return Tools.handleSingleFieldError( new FieldErrorResource( "EShop", "file", "Invalid", "Invalid file"));
				
				HashMap<String, String> fileNames = drive.uploadFileDebug(file, GoogleDriveUploadType.ESHOP);
				
				eShop.setLogoFile( fileNames.get( "localFileName"));
				eShop.setLogoGoogleId( fileNames.get( "googleId"));
			}
			
			eShop.setRelId( mRESTAPI.loadManagedEShops( user.getUserInfo().getId(), 1, 0).getEntities().size() + 1L);
			mRESTAPI.addEShopToUser( user.getUserInfo().getId(), eShop);
			
			/* Initial ShopSub */
			ShopSub shopSub = new ShopSub( entity.getShopSub(), eShop);
			/* Check if StartDate is earlier than now 
			 * In that case, the start date becomes now */
			shopSub.setStartDate( new Timestamp( System.currentTimeMillis()));
			shopSub.setRelId( mRESTAPI.loadShopSubsByEShop( eShop.getId(), 1, 0, true).getEntities().size() + 1L);
			
			if( shopSub.getSubOffer().getPrice() == 0F) {
				shopSub.setSubStatus( GenericStatus.PAID);
				mRESTAPI.addShopSub( shopSub);
				
			} else {
				Payment payment = new Payment( entity.getPayment());
				payment.setShopSub( shopSub);
				payment.setAmount( shopSub.getSubOffer().getPrice());
				/*
				 * TODO
				 */
				mRESTAPI.addPayment(payment, payment.geteAccount().getId(), payment.getAdminEAccount().getId());
				
				shopSub.addPayment( payment);
				shopSub.setSubStatus( GenericStatus.PENDING_PAYMENT_CONFIRMATION);
				mRESTAPI.addShopSub( shopSub);
				
			}
			
			eShop.addShopSub( shopSub);
			mRESTAPI.updateEShop( eShop);

			if( eShop.getProfileCompletion() >= 100)
				notifications.entityApprovalReady(eShop);
			
			notifications.entityPayConfReady(shopSub);

			/* Schedule Expiry
			 * Email & Notifications Embeded
			 */
			scheduler.scheduleExpiring(shopSub);
			
			return Tools.created();
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<Void>( HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@SuppressWarnings("unchecked")
	@PostMapping
	@RequestMapping( value="/e-shop/{id}/update")
	public ResponseEntity<?> updateEShop( @RequestParam( "entity") final String dataString,
										   @PathVariable( "id") final Long id,
										   @RequestParam( name="file", required=false) final MultipartFile file)
	{
		try
		{
			if (SecurityContextHolder.getContext().getAuthentication().getName() == "anonymousUser")
				return Tools.unauthorized();
			
			User user = mRESTAPI.loadUser(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
					.getUserInfo().getId());
			
			EShop originalEntity = mRESTAPI.loadEShop(id);
			if (originalEntity == null)
				return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
			/*
			 * Checks if Manager is legit owner
			 */
			boolean isOwner = false;
			for (EShop eShop : (List<EShop>) mRESTAPI.loadManagedEShops( user.getUserInfo().getId(), 1, 0).getEntities())
			{
				if (eShop.getId().longValue() == id.longValue())
					isOwner = true;
			}
			if (!isOwner)
				return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
			
			ObjectMapper mapper = new ObjectMapper();
			EShop entity = mapper.readValue( dataString, EShop.class);
			BeanUtils.copyProperties(entity, originalEntity, Tools.getNullPropertyNames(entity));
			
			if( file != null)
			{

				if( !Tools.isValidImage(file))
					return Tools.handleSingleFieldError( new FieldErrorResource( "EShop", "file", "Invalid", "Invalid file"));
				
				HashMap<String, String> fileNames = drive.uploadFileDebug(file, GoogleDriveUploadType.ESHOP);
				
				originalEntity.setLogoFile( fileNames.get( "localFileName"));
				originalEntity.setLogoGoogleId( fileNames.get( "googleId"));
			}
			
			originalEntity.setIsEnabled(false);
			mRESTAPI.updateEShop(originalEntity);
			
			/* TODO: Fire websocket and Email update too */
			if( Tools.computeEShopProfileComp( originalEntity) >= 100) {
				notifications.entityApprovalReady(originalEntity);
			}
			
			return Tools.ok();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
		/* ShopSubs */
		@GetMapping
		@JsonView( Views.Manager.class)
		@RequestMapping( value="/e-shop/{id}/shop-subs")
		public ResponseEntity<?> loadShopSubByEshop( @PathVariable( "id") final Long id,
													 @RequestParam( name="page", defaultValue="1") final Integer page,
													 @RequestParam( name="pageSize", defaultValue="0") final Integer pageSize,
													 @RequestParam( name="orderByIdAsc", defaultValue="false") final boolean orderByIdAsc)
		{
			try
			{
				EShop eShop = mRESTAPI.loadEShop(id);
				
				if( eShop == null)
					return Tools.entityNotFound();
				
				return Tools.handlePagedListJSON( mRESTAPI.loadShopSubsByEShop(eShop.getId(), page, pageSize, orderByIdAsc));
				
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		@PostMapping
		@JsonView( Views.Manager.class)
		@RequestMapping( "e-shop/{id}/current-shop-sub")
		public ResponseEntity<?> loadEShopCurrentShopSub(@PathVariable( "id") final Long id )
		{
			try
			{
				if( mRESTAPI.loadEShop( id) == null)
					return Tools.entityNotFound();
				
				ShopSub entity = mRESTAPI.getCurrentShopSub(id);
				
				if( entity == null)
					return new ResponseEntity<Void>( HttpStatus.NOT_FOUND);
				
				return new ResponseEntity<ShopSub>( entity, HttpStatus.OK);
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return new ResponseEntity<Void>( HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		/* Articles */
		@GetMapping
		@JsonView( Views.Manager.class)
		@RequestMapping( value="/e-shop/{id}/articles-count")
		public ResponseEntity<?> getArticlesCount( @PathVariable( "id") final Long id)
		{
			try
			{
				EShop eShop = mRESTAPI.loadEShop(id);
				
				if( eShop == null)
					return Tools.entityNotFound();
				
				return new ResponseEntity<Integer>( mRESTAPI.loadEShopArticles( eShop.getId(), 1, 0).getItemsNumber(), HttpStatus.OK);
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
	/* End EShops */
	
	/* Shop Subs */
	@PostMapping
	@RequestMapping( value="/shop-sub/add")
	public ResponseEntity<?> addShopSub( @RequestParam( name="shopSub", required=true) final String shopSubData,
									     @RequestParam( name="eAccountId", required=false) final Long eAccountId,
									     @RequestParam( name="adminEAccountId", required=false) final Long adminEAccountId,
									     @RequestParam( name="eShopId", required=true) final Long eShopId,
									     @RequestParam( name="paymentRef", required=true) final String paymentRef)
	{
		try
		{
			ObjectMapper mapper = new ObjectMapper();
			
			/* Checks if EShop exists */
			EShop eShop = mRESTAPI.loadEShop(eShopId);
			if( eShop == null)
				return Tools.handleSingleFieldError( new FieldErrorResource("ShopSub", "eShop", "NotFound", "This field is required"));
			
			ShopSub unsafeEntity = new ShopSub( mapper.readValue( shopSubData, ShopSub.class));
			
			ShopSub entity = new ShopSub();

			/* Check if StartDate is earlier than now 
			 * In that case, the start date becomes now */
			if( unsafeEntity.getStartDate().before( new Timestamp( System.currentTimeMillis())))
				entity.setStartDate( new Timestamp( System.currentTimeMillis()));
			else
				entity.setStartDate( unsafeEntity.getStartDate());
			entity.setSubOffer( mRESTAPI.loadSubOffer( unsafeEntity.getSubOffer().getId()));
			
			/* Set Relative Id */
			entity.setRelId( mRESTAPI.loadShopSubsByEShop(eShop.getId(), 1, 0, false).getEntities().size() + 1L);
			
			if( entity.getSubOffer().getPrice() == 0F) {
				entity.setSubStatus( GenericStatus.PAID);
			} else {
				entity.setSubStatus( GenericStatus.PENDING_PAYMENT);
			}
			
			mRESTAPI.addShopSubToEShop( eShop.getId(), entity);

			if( entity.getSubOffer().getPrice() > 0F) {
				
				/* Check if EAccount Exists */
				EAccount eAccount = mRESTAPI.loadEAccount(eAccountId);		
				if( eAccount == null)
					return Tools.handleSingleFieldError( new FieldErrorResource("ShopSub", "eAccount", "NotFound", "This field is required"));
				
				/* Check if Admin EAccount exists */
				EAccount adminEAccount = mRESTAPI.loadEAccount( adminEAccountId);
				if( adminEAccount == null)
					return Tools.handleSingleFieldError( new FieldErrorResource("ShopSub", "adminEAccount", "NotFound", "This field is required"));
				
				Payment payment = new Payment();
				payment.setAmount( entity.getSubOffer().getPrice());
				payment.setShopSub( entity);
				payment.setPaymentRef(paymentRef);
				
				/* Persist Payment */
				mRESTAPI.addPayment( payment, eAccount.getId(), adminEAccount.getId());
				
				entity.addPayment( payment);
				entity.setSubStatus( GenericStatus.PENDING_PAYMENT_CONFIRMATION);
				mRESTAPI.updateShopSub( entity);
			}
			
			/* Schedule Expiry */
			scheduler.scheduleExpiring(entity);
			
			return Tools.created();
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<Void>( HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping
	@JsonView( Views.Manager.class)
	@RequestMapping( value="/shop-sub/get/{id}")
	public ResponseEntity<?> getShopSub( @PathVariable( "id") final Long id)
	{
		ShopSub entity = mRESTAPI.loadShopSub( id);

		if( entity == null)
			return Tools.internalServerError();

		return new ResponseEntity<ShopSub>( entity, HttpStatus.OK);
	}
	@PostMapping
	@RequestMapping( value="/shop-sub/{id}/activate")
	public ResponseEntity<?> activateShopSub( @PathVariable( "id") final Long id)
	{
		try
		{
			/* TODO
			 * Check if logged user is legit
			 */
			ShopSub entity = mRESTAPI.loadShopSub( id);
			
			if( entity == null)
				return Tools.entityNotFound();
			
			long now = System.currentTimeMillis();
			
			/* Time lapse is valid ? */
			if( now < entity.getStartDate().getTime() || now > entity.getEndDate().getTime())
				return Tools.handleSingleFieldError( new FieldErrorResource( "ShopSub", "Period", "Invalid", "The entity either expired or not valid yet."));
			
			/* Is it at least paid ? */
			/*TODO
			 * Say why
			 */
			if( entity.getSubStatus() == GenericStatus.PENDING_PAYMENT || entity.getSubStatus() == GenericStatus.PENDING_PAYMENT_CONFIRMATION)
				return Tools.handleSingleFieldError( new FieldErrorResource( "ShopSub", "Paiement", "Invalid", "The entity was not paid for"));
			
			/*
			 * Checks if current article count doensn't exceed subscription allowed article number
			 */
			if( entity.getSubOffer().getShopStatus().getArticleLimit() < mRESTAPI.loadEShopArticles( entity.geteShop().getId(), 1, 0).getEntities().size())
				return Tools.handleSingleFieldError( new FieldErrorResource( "ShopSub", "ArticleLimit", "Reached", "The article limit is reached for this entity."));
			
			@SuppressWarnings( "unchecked")
			List<ShopSub> shopSubs = (List<ShopSub>) mRESTAPI.loadShopSubsByEShop( entity.geteShop().getId(), 1, 0, false).getEntities();
			
			/* Disable all other EShops */
			for( ShopSub shopSub : shopSubs)
			{
				if( shopSub.getId() != id) {
					if( shopSub.getSubStatus() == GenericStatus.ONGOING) {
						shopSub.setSubStatus( GenericStatus.PAID);
						mRESTAPI.updateShopSub( shopSub);
					}
				}
			}
			
			entity.setSubStatus( GenericStatus.ONGOING);
			mRESTAPI.updateShopSub( entity);
			
			EShop eShop = mRESTAPI.loadEShop( entity.geteShop().getId());
			eShop.setCurrentShopSub(entity);
			
			mRESTAPI.updateEShop(eShop);
			
			return Tools.ok();
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<Void>( HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	/* End Shop Subs */
	
	
	/* Articles */
	@GetMapping
	@JsonView( Views.Manager.class)
	@RequestMapping( method=RequestMethod.GET, value="/article/{id}")
	public ResponseEntity<?> loadArticle( @PathVariable( "id") final Long id)
	{
		try
		{
			Article entity = mRESTAPI.loadArticle(id);
			
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
	@PostMapping
	@RequestMapping( value="/article/add")
	public ResponseEntity<?> addArticle( @RequestParam( "entity") String entity,
			@RequestParam( "eShopId") final String eShopId,
			@RequestParam( name="file0", required = false) MultipartFile file0,
			@RequestParam( name="file1", required = false) MultipartFile file1,
			@RequestParam( name="file2", required = false) MultipartFile file2,
			@RequestParam( name="file3", required = false) MultipartFile file3)
	{
		/*Add an article to an EShop.*/
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			
			if( !getLoggedUserFromPrincipal().hasRole( "ROLE_MANAGER"))
				return Tools.unauthorized();
			
			/* TODO Control on ESHop owner */
			
			ObjectMapper mapper = new ObjectMapper();

			//ESHop the article will be assigned to
			EShop eShop = mRESTAPI.loadEShop( mapper.readValue(eShopId, Long.class));
			
			if( eShop == null)
				return new ResponseEntity<Void>( HttpStatus.BAD_REQUEST);
			
			/* Check if ESHop has valid sub */
			if( mRESTAPI.getCurrentShopSub( eShop.getId()) == null)
				return Tools.handleSingleFieldError( new FieldErrorResource( "EShop", "ShopSub", "Invalid", "This EShop must have a valid subscription."));
			
			/* TODO
			 * Explicit, return reason of bad request
			 */
			if( mRESTAPI.getCurrentShopSub( eShop.getId()).getSubOffer().getShopStatus().getArticleLimit() <=
				mRESTAPI.loadEShopArticles( eShop.getId(), 1, 0).getItemsNumber())
				return Tools.handleSingleFieldError( new FieldErrorResource( "EShop", "ShopSub", "LimitReached", "The article limit has been reached for this EShop"));

			Article article = mapper.readValue( entity, Article.class);
			
			//Instantiate Feature collection to persist them first
			Collection<ArticleFeature> articlesFeatures = new ArrayList<ArticleFeature>( article.getFeatures());
			article.getFeatures().clear();

			/* TODO Check is article feature are valid */
			for( ArticleFeature arfeat : articlesFeatures)
			{
				if( mRESTAPI.getFeatureByLabel( arfeat.getFeature().getLabel()) == null)
				{
					mRESTAPI.addFeature( new Feature( arfeat.getFeature().getLabel()));
				}

				Feature feature = mRESTAPI.getFeatureByLabel( arfeat.getFeature().getLabel());
				
				Collection<FeatureValue> featVals = new ArrayList<FeatureValue>( arfeat.getValues());
				for( FeatureValue featVal : featVals)
				{
					if( mRESTAPI.getFeatureValue( featVal.getValue()) == null) {
						mRESTAPI.addFeatureValue( featVal);
					}
					else {
						featVal.setId( mRESTAPI.getFeatureValue( featVal.getValue()).getId());
					}
				}
				
				ArticleFeature articleFeature = new ArticleFeature( featVals, feature);
				mRESTAPI.addArticleFeature( articleFeature);
				
				//Add the Persisted Feature to the article
				article.addFeature( articleFeature);
			}
			
			/* Special filling method from recursive strcuture to linear one */
			fillArticleCategoriesFormMST( article, article.getSelectedCategories());
			
			/* TODO Checks on files */
			MultipartFile[] files = { file0, file1, file2, file3 };
			
			List<FieldErrorResource> fErrors = new ArrayList<FieldErrorResource>();
			
			for( MultipartFile file : files)
			{
				int index = 0;
				if( file != null) {
					if( !Tools.isValidImage(file))
						fErrors.add( new FieldErrorResource( "Article", "file" + index, "Invalid", "Invalid File"));
				}
			}
			
			if( !fErrors.isEmpty())
				return Tools.handleMultipleFieldErrors(fErrors);
			
			if( files.length > 0 )
			{
				for( MultipartFile file : files)
				{
					if( file != null)
					{
						HashMap<String, String> fileNames = new HashMap<String, String>();
						
						fileNames = drive.uploadFileDebug(file, GoogleDriveUploadType.ARTICLE);
						
						Picture picture = new Picture( fileNames.get( "localFileName"));
						picture.setGoogleId( fileNames.get( "googleId"));
						
						if( file == files[0])
							picture.setIsDefault(true);

						mRESTAPI.addPicture( picture);

						article.addPicture( picture);
						
					}
				}
			}
			
			article.setRelId( mRESTAPI.loadEShopArticles( eShop.getId(), 1, 0).getItemsNumber() + 1L);
			mRESTAPI.addArticleToEShop( mapper.readValue(eShopId, Long.class), article);
			
			HttpHeaders headers = new HttpHeaders();
			return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<Void>( HttpStatus.BAD_REQUEST);
		}
	}
	@PutMapping
	@RequestMapping( value="/article/{id}/update")
	public ResponseEntity<?> updateArticle( @RequestParam( "entity") String entity,
											 @PathVariable( "id") final Long id,
											 @RequestParam( name="file0", required = false) MultipartFile file0,
											 @RequestParam( name="file1", required = false) MultipartFile file1,
											 @RequestParam( name="file2", required = false) MultipartFile file2,
											 @RequestParam( name="file3", required = false) MultipartFile file3)
	{
		try
		{
			Article article = mRESTAPI.loadArticle( id);
			
			if( article == null)
				return new ResponseEntity<Void>( HttpStatus.NOT_FOUND);

			ObjectMapper mapper = new ObjectMapper();
			Article editedArticle = mapper.readValue( entity, Article.class);
			
			BeanUtils.copyProperties( editedArticle, article, Tools.getNullPropertyNames(entity));
			
			//Instantiate Feature collection to persist them first
			Collection<ArticleFeature> articlesFeatures = new ArrayList<ArticleFeature>( article.getFeatures());
			article.getFeatures().clear();

			for( ArticleFeature arfeat : articlesFeatures)
			{
				if( mRESTAPI.getFeatureByLabel( arfeat.getFeature().getLabel()) == null)
				{
					mRESTAPI.addFeature( new Feature( arfeat.getFeature().getLabel()));
				}

				Feature feature = mRESTAPI.getFeatureByLabel( arfeat.getFeature().getLabel());
				
				Collection<FeatureValue> featVals = new ArrayList<FeatureValue>( arfeat.getValues());
				for( FeatureValue featVal : featVals)
				{
					if( mRESTAPI.getFeatureValue( featVal.getValue()) == null) {
						mRESTAPI.addFeatureValue( featVal);
					}
					else {
						featVal.setId( mRESTAPI.getFeatureValue( featVal.getValue()).getId());
					}
				}
				
				ArticleFeature articleFeature = new ArticleFeature(featVals, feature);
				mRESTAPI.addArticleFeature( articleFeature);

				//Add the Persisted Feature to the article
				article.addFeature( articleFeature);
			}
			
			//Set categories
			article.getCategories().clear();
			fillArticleCategoriesFormMST( article, article.getSelectedCategories());
			
			MultipartFile[] files = { file0, file1, file2, file3 };

			List<FieldErrorResource> fErrors = new ArrayList<FieldErrorResource>();
			
			for( MultipartFile file : files)
			{
				int index = 0;
				if( file != null) {
					if( !Tools.isValidImage(file))
						fErrors.add( new FieldErrorResource( "Article", "file" + index, "Invalid", "Invalid File"));
				}
			}
			
			if( !fErrors.isEmpty())
				return Tools.handleMultipleFieldErrors(fErrors);
			
			if( files.length > 0 )
			{
				for( MultipartFile file : files)
				{
					if( file != null)
					{
						HashMap<String, String> fileNames = new HashMap<String, String>();
						
						fileNames = drive.uploadFileDebug(file, GoogleDriveUploadType.ARTICLE);
						
						Picture picture = new Picture( fileNames.get( "localFileName"));
						picture.setGoogleId( fileNames.get( "googleId"));
						
						if( file == files[0])
							picture.setIsDefault(true);

						mRESTAPI.addPicture( picture);

						article.addPicture( picture);
					}
				}
			}
			
			mRESTAPI.updateArticle( article);

			return new ResponseEntity<Void>(  HttpStatus.OK);
			
		} catch (Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<Void>( HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
		/* AdvOffers */
		@SuppressWarnings("unchecked")
		@GetMapping
		@JsonView( Views.Manager.class)
		@RequestMapping( "/article/{id}/adv-offers")
		public ResponseEntity<?> loadArticleAdvOffers( @PathVariable( name="id", required=true) final Long id,
												@RequestParam( name="page", defaultValue="1") final Integer page,
											    @RequestParam( name="pageSize", defaultValue="0") final Integer pageSize,
											    @RequestParam( name="orderByIdAsc", defaultValue="true") final boolean orderByIdAsc,
											    @RequestParam( name="status", defaultValue="-1") final Integer status)
		{
			try
			{
				if( isAnonymous())
					return Tools.unauthorized();
				
				if( !getLoggedUserFromPrincipal().hasRole( "ROLE_MANAGER"))
					return Tools.unauthorized();
				
				/* Checks if Manager owns aticle */
				boolean isOwner = false;
				for( EShop eShop : (List<EShop>) mRESTAPI.loadManagedEShops( getLoggedUserFromPrincipal().getUserInfo().getId(), 1, 0).getEntities())
				{
					for( Article article : ( List<Article>) mRESTAPI.loadEShopArticles( eShop.getId(), 1, 0).getEntities())
							if( article.getId().longValue() == id)
								isOwner = true;
				}
				if( !isOwner)
					return Tools.unauthorized();
				
				GenericStatus advOfferStatus;
				switch( status)
				{
					case 0:
						advOfferStatus = GenericStatus.PENDING_PAYMENT;
						break;
					case 1:
						advOfferStatus = GenericStatus.PENDING_PAYMENT_CONFIRMATION;
						break;
					case 2:
						advOfferStatus = GenericStatus.PAID;
						break;
					case 3:
						advOfferStatus = GenericStatus.ONGOING;
						break;
					case 4:
						advOfferStatus = GenericStatus.EXPIRED;
						break;
					case 5:
						advOfferStatus = GenericStatus.CANCELLED;
						break;
					case -1:
						advOfferStatus = GenericStatus.ALL;
						break;
					default:
						advOfferStatus = GenericStatus.ALL;
						break;
						
				}
				
				return Tools.handlePagedListJSON( mRESTAPI.loadArticleAdvOffers( id, page, pageSize, advOfferStatus, orderByIdAsc));
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
	/* End Articles */
	
	/* ArticleOrders */	
	@PostMapping
	@RequestMapping( "/article-order/deliver")
	public ResponseEntity<?> deliverArticleOrder( @RequestParam( "id") final String id,
												  @RequestParam( "packageId") final String packageId)
	{
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			if( !user.hasRole( "ROLE_MANAGER"))
				return Tools.unauthorized();
			
			ObjectMapper mapper = new ObjectMapper();
			
			ArticleOrder entity = mRESTAPI.loadArticleOrder( mapper.readValue( id, Long.class));
			
			//Check if the manager really owns the eshop owning the orderedArticles
			
			entity.setDeliveryDate( new Timestamp( System.currentTimeMillis()));
			entity.setPackageId( packageId);
			entity.setStatus( OrderStatus.DELIVERING);
			
			mRESTAPI.updateArticleOrder( entity);
			
			service.userArticleOrderUpdated( entity.getUser().getUsername(), entity.getId());
			
			/*
			 * Notify User
			 */
			notifications.buyerOrderDelivering(entity);
			/* Email User */
			emails.deliveringArticleOrder(entity);
			
			return Tools.ok();
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.unauthorized();
		}
	}
	@GetMapping
	@SuppressWarnings("unchecked")
	@JsonView( Views.Manager.class)
	@RequestMapping( "/article-order/{id}")
	public ResponseEntity<?> loadArticleOrder( @PathVariable( "id") final Long id) 
	{
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			
			if( !getLoggedUserFromPrincipal().hasRole( "ROLE_MANAGER"))
				return Tools.unauthorized();

			ArticleOrder entity = mRESTAPI.loadArticleOrder( id);
			if( entity == null)
				return new ResponseEntity<Void>( HttpStatus.NOT_FOUND);
			
			/*
			 * Checks if Manager really owns the eshop
			 */
			boolean isManagedEShop = false;
			
			for( EShop eShop :( List<EShop>) mRESTAPI.loadManagedEShops( getLoggedUserFromPrincipal().getUserInfo().getId(), 1, 0).getEntities())
			{
				if( eShop.getId().longValue() == mRESTAPI.loadArticleOrderEShop( entity.getId()).getId().longValue())
					isManagedEShop = true;
			}
			
			if( !isManagedEShop)
				return Tools.unauthorized();
			
			return new ResponseEntity<ArticleOrder>( entity, HttpStatus.OK);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<Void>( HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
		/* Payments */
		@GetMapping
		@JsonView( Views.Admin.class)
		@RequestMapping( "/article-order/{id}/payments")
			public ResponseEntity<?> loadArticleOrderPayments( @PathVariable( "id") final Long id,
														   @RequestParam( name="page", defaultValue="1") final Integer page,
														   @RequestParam( name="pageSize", defaultValue="10") final Integer pageSize)
		{
			if( isAnonymous())
				return Tools.unauthorized();
			
			if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN"))
				return Tools.unauthorized();
			
			try
			{
				return Tools.handlePagedListJSON(mRESTAPI.loadArticleOrderPayments(id, page, pageSize));
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		/* EShop */
		@GetMapping
		@RequestMapping( "/article-order/{id}/e-shop")
		public ResponseEntity<?> loadArticleOrderEShop( @PathVariable( "id") final Long id)
		{
			try
			{
				ArticleOrder entity = mRESTAPI.loadArticleOrder(id);
				
				if( entity == null)
					return Tools.entityNotFound();
				
				return new ResponseEntity<EShop>( mRESTAPI.loadArticleOrderEShop( entity.getId()), HttpStatus.OK);
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
	/* End ArticleOrders */
	
	/* AdvOffer */
	@GetMapping
	@JsonView( Views.Manager.class)
	@RequestMapping( "/logged-user/adv-offers")
	public ResponseEntity<?> loadManagerAdvOffers(	@RequestParam( name="page", defaultValue="1") final Integer page,
												    @RequestParam( name="pageSize", defaultValue="0") final Integer pageSize,
												    @RequestParam( name="orderByIdAsc", defaultValue="true") final boolean orderByIdAsc,
												    @RequestParam( name="status", defaultValue="-1") final Integer status)
	{
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			
			if( !getLoggedUserFromPrincipal().hasRole( "ROLE_MANAGER"))
				return Tools.unauthorized();
			
			GenericStatus advOfferStatus;
			switch( status)
			{
				case 0:
					advOfferStatus = GenericStatus.PENDING_PAYMENT;
					break;
				case 1:
					advOfferStatus = GenericStatus.PENDING_PAYMENT_CONFIRMATION;
					break;
				case 2:
					advOfferStatus = GenericStatus.PAID;
					break;
				case 3:
					advOfferStatus = GenericStatus.ONGOING;
					break;
				case 4:
					advOfferStatus = GenericStatus.EXPIRED;
					break;
				case 5:
					advOfferStatus = GenericStatus.CANCELLED;
					break;
				case -1:
					advOfferStatus = GenericStatus.ALL;
					break;
				default:
					advOfferStatus = GenericStatus.ALL;
					break;
					
			}
			
			return Tools.handlePagedListJSON( mRESTAPI.loadManagerAdvOffers( getLoggedUserFromPrincipal().getUserInfo().getId(), page, pageSize, advOfferStatus, orderByIdAsc));
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<List<ArticleOrder>>( HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@SuppressWarnings("unchecked")
	@PostMapping
	@RequestMapping( value="/adv-offer/add")
	public ResponseEntity<?> addAdvOffer( @RequestParam( name="advOffer", required=true) final String advOfferData,
										      @RequestParam( name="eAccountId", required=false) final Long eAccountId,
										      @RequestParam( name="adminEAccountId", required=false) final Long adminEAccountId,
		 								      @RequestParam( name="paymentRef", required=true) final String paymentRef)
	{
		try
		{
			/* TODO Urgent: Control everything before a single ent is persisted */
			/* TODO Control endDate must be superior to now for at least 1 hour */
			ObjectMapper mapper = new ObjectMapper();
			
			AdvOffer unsafeEntity = mapper.readValue( advOfferData, AdvOffer.class);
			
			List<FieldErrorResource> fErrors = new ArrayList<FieldErrorResource>();
			
			/* Lvl 1 Checks */
			if( unsafeEntity.getStartDate() == null)
				fErrors.add( new FieldErrorResource( "AdvOffer", "startDate", "Invalid", "This field is mandatory."));
			AdvOption opt = mRESTAPI.loadAdvOption( unsafeEntity.getAdvOption().getId());
			if( opt == null)
				fErrors.add( new FieldErrorResource( "AdvOffer", "AdvOption", "NotFound", "This field is mandatory."));
			/* Chek if Payment infos are provided in case of charged sub */
			if( opt.getPrice() > 0) {
				EAccount e = mRESTAPI.loadEAccount(eAccountId);
				if( e == null)
					fErrors.add( new FieldErrorResource( "AdvOffer", "EAccountId", "Invalid", "This field is required."));
				EAccount ae = mRESTAPI.loadEAccount(adminEAccountId);
				if( ae == null)
					fErrors.add( new FieldErrorResource( "AdvOffer", "AdminEAccountId", "Invalid", "This field is required"));
				if( paymentRef == null || paymentRef.isEmpty())
					fErrors.add( new FieldErrorResource( "AdvOffer", "PaymentRef", "Invalid", "This field is mandatory."));
			}
			
			if( !fErrors.isEmpty())
				return Tools.handleMultipleFieldErrors(fErrors);
			
			/* Lvl 2 Checks */
			if( mRESTAPI.loadArticle( unsafeEntity.getArticle().getId()) == null)
				fErrors.add( new FieldErrorResource( "AdvOffer", "article", "NotFound", "Article not found"));
			
			/* Checks if Manager owns aticle */
			boolean isOwner = false;
			for( EShop eShop : (List<EShop>) mRESTAPI.loadManagedEShops( getLoggedUserFromPrincipal().getUserInfo().getId(), 1, 0).getEntities())
			{
				for( Article article : ( List<Article>) mRESTAPI.loadEShopArticles( eShop.getId(), 1, 0).getEntities())
						if( article.getId().longValue() == unsafeEntity.getArticle().getId().longValue())
							isOwner = true;
			}
			if( !isOwner)
				return Tools.unauthorized();
			
			AdvOffer entity = new AdvOffer();
			
			/* Check if StartDate is earlier than now 
			 * In that case, the start date becomes now */
			if( unsafeEntity.getStartDate().before( new Timestamp( System.currentTimeMillis())))
				entity.setStartDate( new Timestamp( System.currentTimeMillis()));
			else
				entity.setStartDate( unsafeEntity.getStartDate());
			
			entity.setAdvOption( mRESTAPI.loadAdvOption( unsafeEntity.getAdvOption().getId()));
			entity.setArticle( mRESTAPI.loadArticle( unsafeEntity.getArticle().getId()));
			
			/* Set Relative Id */
			entity.setRelId( mRESTAPI.loadArticleAdvOffers( entity.getArticle().getId(), 1, 0, GenericStatus.ALL, true).getItemsNumber() + 1L);
			
			if( entity.getAdvOption().getPrice() == 0F) {
				if( entity.isAutoEnabled())
					entity.setStatus( GenericStatus.ONGOING);
				else
					entity.setStatus( GenericStatus.PAID);
			} else {
				entity.setStatus( GenericStatus.PENDING_PAYMENT);
			}
			
			mRESTAPI.addAdvOffer( entity);

			/* Schedule Expiry
			 * Email & Notifications Embeded
			 */
			scheduler.scheduleExpiring(entity);
			
			if( entity.getAdvOption().getPrice() > 0F) {
				EAccount eAccount = mRESTAPI.loadEAccount(eAccountId);
				EAccount adminEAccount = mRESTAPI.loadEAccount( adminEAccountId);
				
				Payment payment = new Payment();
				payment.setAmount( entity.getAdvOption().getPrice());
				payment.setAdvOffer( entity);
				payment.setPaymentRef(paymentRef);
				
				/* Persist Payment */
				mRESTAPI.addPayment( payment, eAccount.getId(), adminEAccount.getId());
				
				entity.addPayment( payment);
				entity.setStatus( GenericStatus.PENDING_PAYMENT_CONFIRMATION);
				mRESTAPI.updateAdvOffer( entity);
			}
			
			/* Notifiy Admins */
			notifications.entityPayConfReady(entity);
			
			return Tools.created();
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@PostMapping
	@SuppressWarnings( "unchecked")
	@RequestMapping( value="/adv-offer/{id}/act-deact")
	public ResponseEntity<?> advDeactAdvOffer( @PathVariable( name="id", required=true) final Long id)
	{
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			
			if( !getLoggedUserFromPrincipal().hasRole( "ROLE_MANAGER"))
				return Tools.unauthorized();
			
			/* Checks if Manager is owner */
			boolean isFound = false;
			for( AdvOffer advOffer : (List<AdvOffer>) mRESTAPI.loadManagerAdvOffers( getLoggedUserFromPrincipal().getUserInfo().getId(), 1, 0, GenericStatus.ALL, true).getEntities())
			{
				if( advOffer.getId().longValue() == id.longValue())
					isFound = true;
			}
			
			if( !isFound)
				return Tools.unauthorized();
			
			AdvOffer entity = mRESTAPI.loadAdvOffer(id);
			
			long now = System.currentTimeMillis();
			
			/* TODO Change error format */
			/* Time lapse is valid ? */
			if( now < entity.getStartDate().getTime() || now > entity.getEndDate().getTime())
				return Tools.handleSingleFieldError( new FieldErrorResource( "AdvOffer", "Period", "Invalid", "Too early or expired"));
			
			if( now > entity.getEndDate().getTime()) {
				entity.setStatus( GenericStatus.EXPIRED);
				mRESTAPI.updateAdvOffer( entity);
				
				return Tools.handleSingleFieldError( new FieldErrorResource( "AdvOffer", "Period", "Invalid", "Too late."));
			}
			
			/* Is it at least paid ? */
			/*TODO
			 * Say why
			 */
			if( entity.getStatus() == GenericStatus.PENDING_PAYMENT || entity.getStatus() == GenericStatus.PENDING_PAYMENT_CONFIRMATION)
				return Tools.handleSingleFieldError( new FieldErrorResource( "AdvOffer", "Payment", "Unpaid", "This AdvOffer wasn't paid for yet"));
			
			if( entity.getStatus() == GenericStatus.ONGOING)
				entity.setStatus( GenericStatus.PAID);
			else if( entity.getStatus() == GenericStatus.PAID)
				entity.setStatus( GenericStatus.ONGOING);
			
			mRESTAPI.updateAdvOffer(entity);
			
			return Tools.ok();
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<Void>( HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping
	@JsonView( Views.Manager.class)
	@RequestMapping( "/adv-offer/{id}")
	public ResponseEntity<?> loadAdvOffer( @PathVariable( name="id", required=true) final Long id)
	{
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			
			if( !getLoggedUserFromPrincipal().hasRole( "ROLE_MANAGER"))
				return Tools.unauthorized();
			
			return new ResponseEntity<AdvOffer>( mRESTAPI.loadAdvOffer(id), HttpStatus.OK);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
		/* Payments */
		@GetMapping
		@JsonView( Views.Manager.class)
		@RequestMapping( "/adv-offer/{id}/payments")
		public ResponseEntity<?> loadAdvOfferPayments( @PathVariable( name="id", required=true) final Long id,
													   @RequestParam( name="page", defaultValue="1") final Integer page,
													   @RequestParam( name="pageSize", defaultValue="0") final Integer pageSize,
													   @RequestParam( name="orderByIdAsc", defaultValue="true") final boolean orderByIdAsc)
		{
			try
			{
				if( isAnonymous())
					return Tools.unauthorized();
				
				if( !getLoggedUserFromPrincipal().hasRole( "ROLE_MANAGER"))
					return Tools.unauthorized();
				
				if( mRESTAPI.loadAdvOffer(id) == null)
					return Tools.entityNotFound();
				
				return Tools.handlePagedListJSON( mRESTAPI.loadAdvOfferPayments(id, page, pageSize, orderByIdAsc));
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
	/* End AdvOffer */
	
	/* AdvOption */
	@GetMapping
	@JsonView( Views.Manager.class)
	@RequestMapping( value="/adv-options", method=RequestMethod.GET)
	public ResponseEntity<?> loadAdvOptions( @RequestParam( name="page", defaultValue="1") final Integer page,
											 @RequestParam( name="pageSize", defaultValue="0") final Integer pageSize)
	{
		try
		{
			PagedListJSON result = mRESTAPI.loadAdvOptions( page, pageSize);

			return Tools.handlePagedListJSON( result);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	/* TODO Special Method to load AdvOption for Article / EShop 
	 * where we count if there is already too much free Ads */
	/* End AdvOption */
	
	/* Category */
	@GetMapping
	@JsonView( Views.Manager.class)
	@SuppressWarnings("unchecked")
	@RequestMapping( "/categories/mst-formated")
	public ResponseEntity<?> loadMSTFormatedCategories()
	{
		try
		{
			/* This is for the special format needed by AngularJS plugin
			 * Multi Select Tree used when affecting categories to an article
			 */
			List<CategoryMultiSelectTreeJSON> entities = new ArrayList<CategoryMultiSelectTreeJSON>();
			
			for( Category category : (List<Category>) mRESTAPI.loadCategories(0, 1, 0).getEntities())
			{
				entities.add( new CategoryMultiSelectTreeJSON( category));
			}
			
			return new ResponseEntity<List<CategoryMultiSelectTreeJSON>>( entities, HttpStatus.OK);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@GetMapping
	@SuppressWarnings("unchecked")
	@JsonView( Views.Manager.class)
	@RequestMapping( "/article/{id}/mst-formated-categories")
	public ResponseEntity<?> loadMSTFormatedArticleCategories( @PathVariable( "id") final Long id)
	{
		try
		{
			/* This is for the special format needed by AngularJS plugin
			 * Multi Select Tree used when affecting categories to an article
			 */
			if( mRESTAPI.loadArticle( id) == null)
				return Tools.entityNotFound();
			
			List<CategoryMultiSelectTreeJSON> entities = new ArrayList<CategoryMultiSelectTreeJSON>();
			
			for( Category category : (List<Category>) mRESTAPI.loadCategories(0, 1, 0).getEntities()) {
				entities.add( new CategoryMultiSelectTreeJSON( category, (Collection<Category>) mRESTAPI.loadArticleCategories( id, 1, 0).getEntities()));
			}
			
			return new ResponseEntity<List<CategoryMultiSelectTreeJSON>>( entities, HttpStatus.OK);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	/* End Category */
	
	/* Tools - Dry */
	public void fillArticleCategoriesFormMST( Article article, Collection<CategoryMultiSelectTreeJSON> categories)
	{
		for( CategoryMultiSelectTreeJSON category : categories)
		{
			if( category.isSelected())
				article.addCategory( mRESTAPI.loadCategory( category.getId()));
			fillArticleCategoriesFormMST( article, category.getChildren());
		}
	}
	private User getLoggedUserFromPrincipal() {
		if( isAnonymous())
			return null;
		
		return ( User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	private boolean isAnonymous() {
		return SecurityContextHolder.getContext().getAuthentication().getName() == "anonymousUser";
	}
	@SuppressWarnings("unused")
	private LoggedUser generateLoggedUserJSON( final User user) {
		
		return new LoggedUser( user.getUserInfo(), user.getUsername(), user.getProfileCompletion(), user.getUserInfo().geteAccounts(), user.isManager());
	}

	/* DEBUG */
	@GetMapping
	@RequestMapping( "/debug/schedule")
	public ResponseEntity<?> scheduleTest()
	{	
		AdvOffer entity = new AdvOffer();
		entity.setStartDate( new Timestamp( System.currentTimeMillis()));
		entity.setAdvOption( mRESTAPI.loadAdvOption( 1L));
		entity.setArticle( mRESTAPI.loadArticle( 1L));
		entity.setAutoEnabled(true);
		
		/* Set Relative Id */
		entity.setRelId( mRESTAPI.loadArticleAdvOffers( entity.getArticle().getId(), 1, 0, GenericStatus.ALL, true).getItemsNumber() + 1L);
		
		if( entity.getAdvOption().getPrice() == 0F) {
			if( entity.isAutoEnabled())
				entity.setStatus( GenericStatus.ONGOING);
			else
				entity.setStatus( GenericStatus.PAID);
		} else {
			entity.setStatus( GenericStatus.PENDING_PAYMENT);
		}
		
		mRESTAPI.addAdvOffer( entity);
		
		/* Schedule Expiry
		 * Email & Notifications Embeded
		 */
		scheduler.scheduleExpiring(entity);
		
		return Tools.ok();
	}
	
	/* Exception Handling */
	@ExceptionHandler( { Exception.class })
	private ResponseEntity<?> exceptionHanlder( Exception e)
	{
		System.err.println( "DEBUG: Exception Caught");
		e.printStackTrace();
		emails.sendErrorReport(e);
		return Tools.internalServerError();
	}
	/* End Exception Handling */
}
