package org.usayi.preta.controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.usayi.preta.Views;
import org.usayi.preta.buzlayer.IAdminRESTAPI;
import org.usayi.preta.config.CustomPasswordEncoder;
import org.usayi.preta.config.Tools;
import org.usayi.preta.entities.AdvOffer;
import org.usayi.preta.entities.AdvOption;
import org.usayi.preta.entities.ArticleOrder;
import org.usayi.preta.entities.Category;
import org.usayi.preta.entities.EAccount;
import org.usayi.preta.entities.EMoneyProvider;
import org.usayi.preta.entities.EShop;
import org.usayi.preta.entities.Expense;
import org.usayi.preta.entities.GenericStatus;
import org.usayi.preta.entities.OrderStatus;
import org.usayi.preta.entities.Payment;
import org.usayi.preta.entities.PaymentType;
import org.usayi.preta.entities.Role;
import org.usayi.preta.entities.ShopStatus;
import org.usayi.preta.entities.ShopSub;
import org.usayi.preta.entities.Slide;
import org.usayi.preta.entities.SubOffer;
import org.usayi.preta.entities.UpgradeRequest;
import org.usayi.preta.entities.User;
import org.usayi.preta.entities.UserInfo;
import org.usayi.preta.entities.json.CategoryTreeJSON;
import org.usayi.preta.entities.json.LoggedUser;
import org.usayi.preta.entities.json.PagedListJSON;
import org.usayi.preta.exceptions.FieldErrorResource;
import org.usayi.preta.exceptions.InvalidEntityFieldError;
import org.usayi.preta.exceptions.InvalidRequestException;
import org.usayi.preta.service.EMailService;
import org.usayi.preta.service.GoogleDriveService;
import org.usayi.preta.service.GoogleDriveUploadType;
import org.usayi.preta.service.NotificationService;
import org.usayi.preta.service.WebSocketService;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@Secured( "ROLE_ADMIN")
@RequestMapping( "/rest-api/admin")
public class AdminRESTAPIController
{
	@Autowired
	@Qualifier( "sessionRegistry")
	private SessionRegistry sessionRegistry;
	
	private SimpUserRegistry userRegistry;

	@Autowired
	private WebSocketService websocketService; 

	@Autowired
	NotificationService notifications;
	
	@Autowired
	EMailService emails;
	
	@Autowired
	GoogleDriveService drive;
	
	@Autowired
	private IAdminRESTAPI aRESTAPI;	
	
	/* AdvOption */
	@GetMapping
	@JsonView( Views.Admin.class)
	@RequestMapping( value="/adv-option/get/{id}")
	public ResponseEntity<?> getAdvOption( @PathVariable( "id") final Long id)
	{
		AdvOption entity = aRESTAPI.loadAdvOption( id);

		if( entity == null)
			return new ResponseEntity<Void>( HttpStatus.NOT_FOUND);

		return new ResponseEntity<AdvOption>( entity, HttpStatus.OK);
	}
	@GetMapping
	@JsonView( Views.Admin.class)
	@RequestMapping( value="/adv-options", method=RequestMethod.GET)
	public ResponseEntity<?> loadAdvOptions( @RequestParam( name="page", defaultValue="1") final Integer page,
											@RequestParam( name="pageSize", defaultValue="0") final Integer pageSize)
	{
		try
		{
			PagedListJSON result = aRESTAPI.loadAdvOptions( page, pageSize);

			return Tools.handlePagedListJSON( result);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@PostMapping
	@RequestMapping( value="/adv-option/add")
	public ResponseEntity<?> addAdvOption( @RequestBody @Valid AdvOption entity, BindingResult br)
	{        
		if (br.hasErrors()) {
			throw new InvalidRequestException("Invalid entity", br);
		}
		
		try
		{
			aRESTAPI.addAdvOption(entity);
			
			return new ResponseEntity<Void>( HttpStatus.CREATED);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@PostMapping
	@RequestMapping( value="/adv-option/update/{id}")
	public ResponseEntity<?> updateAdvOption( @PathVariable( "id") final Long id, @RequestBody AdvOption entity)
	{
		AdvOption originalEntity = aRESTAPI.loadAdvOption( id);

		if( originalEntity == null)
			return Tools.entityNotFound();

		BeanUtils.copyProperties( entity, originalEntity, Tools.getNullPropertyNames( entity));

		aRESTAPI.updateAdvOption( originalEntity);

		return new ResponseEntity<Void>( HttpStatus.OK);

	}
	@DeleteMapping
	@RequestMapping( value="/adv-option/delete/{id}")
	public ResponseEntity<Void> deleteAdvOption(@PathVariable( "id") final Long id)
	{
		if( aRESTAPI.loadAdvOption( id) == null)
			return new ResponseEntity<Void>( HttpStatus.NOT_FOUND);

		aRESTAPI.deleteAdvOption( id);

		HttpHeaders headers = new HttpHeaders();

		return new ResponseEntity<Void>( headers, HttpStatus.NO_CONTENT);
	}
	/* End AdvOption */
	
	/* EMoneyProvider */
	@GetMapping
	@JsonView( Views.Admin.class)
	@RequestMapping( value="/e-money-provider/{id}")
	public ResponseEntity<?> getEMoneyProvider(@PathVariable( value="id") final Long id)
	{
		EMoneyProvider entities = aRESTAPI.loadEMoneyProvider(id);

		if( entities == null)
			return new ResponseEntity<EMoneyProvider>( HttpStatus.NOT_FOUND);

		HttpHeaders headers = new HttpHeaders();

		return new ResponseEntity<EMoneyProvider>( entities, headers, HttpStatus.OK);
	}
	@GetMapping
	@JsonView( Views.Admin.class)
	@RequestMapping( value="/e-money-providers")
	public ResponseEntity<?> listEMoneyProviders( @RequestParam( name="page", defaultValue="1") final Integer page,
												   @RequestParam( name="pageSize", defaultValue="10") final Integer pageSize)
	{
		return Tools.handlePagedListJSON( aRESTAPI.loadEMoneyProviders(page, pageSize));
	}
	@PostMapping
	@RequestMapping( value="/e-money-provider/add")
	public ResponseEntity<?> addEMoneyProvider( @RequestParam( value="entity") String entity,
												@RequestParam( name="file", required=false) MultipartFile file) throws Exception
	{
		HttpHeaders headers = new HttpHeaders();

		ObjectMapper mapper = new ObjectMapper();

		EMoneyProvider emp = mapper.readValue( entity, EMoneyProvider.class);

		if( aRESTAPI.loadEMoneyProviderByName( emp.getName()) != null)
		{
			return new ResponseEntity<Void>(headers, HttpStatus.CONFLICT);
		}
		try
		{
			if( file != null && !file.isEmpty())
			{
				if( !Tools.isValidImage(file))
					return Tools.handleSingleFieldError( new FieldErrorResource( "EMP", "file", "Invalid", "Invalid file."));
				
				HashMap<String, String> fileNames = new HashMap<String, String>();
				
				fileNames = drive.uploadFileDebug(file, GoogleDriveUploadType.EMP);
				
				emp.setLogoFile( fileNames.get( "localFileName"));
				emp.setLogoGoogleId( fileNames.get( "googleId"));
			}

			aRESTAPI.addEMoneyProvider( emp);

			return Tools.created();
		}
		catch( Exception e)
		{
			//Implement more accurate Exeception Management
			e.printStackTrace();
			return new ResponseEntity<Void>( HttpStatus.BAD_REQUEST);
		}
	}
	@PostMapping
	@RequestMapping( value="e-money-provider/{id}/update")
	public ResponseEntity<?> updateEMoneyProvider( @PathVariable( "id") final Long id, 
			@RequestParam( "entity") String entity,
			@RequestParam( name="file", required=false) MultipartFile file)
	{
		EMoneyProvider originalEntity = aRESTAPI.loadEMoneyProvider( id);

		if( originalEntity == null)
			return new ResponseEntity<Void>( HttpStatus.NOT_FOUND);

		ObjectMapper mapper = new ObjectMapper();

		try
		{
			EMoneyProvider parsedEntity = mapper.readValue( entity, EMoneyProvider.class);

			//Changes only the fields that where update in the form
			BeanUtils.copyProperties( parsedEntity, originalEntity, Tools.getNullPropertyNames( parsedEntity));

			if( file != null && !file.isEmpty())
			{
				if( !Tools.isValidImage(file))
					return Tools.handleSingleFieldError( new FieldErrorResource( "EMP", "file", "Invalid", "Invalid file."));
				
				HashMap<String, String> fileNames = new HashMap<String, String>();
				
				fileNames = drive.uploadFileDebug(file, GoogleDriveUploadType.EMP);
				
				originalEntity.setLogoFile( fileNames.get( "localFileName"));
				originalEntity.setLogoGoogleId( fileNames.get( "googleId"));
			}

			aRESTAPI.updateEMoneyProvider( originalEntity);

			return Tools.ok();
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<Void>( HttpStatus.BAD_REQUEST);
		}
	}
	@DeleteMapping
	@RequestMapping( value="e-money-provider/{id}/delete")
	public ResponseEntity<?> deleteEMoneyProvider( @PathVariable( "id") final Long id)
	{
		if( aRESTAPI.loadEMoneyProvider( id) == null)
			return new ResponseEntity<Void>( HttpStatus.NOT_FOUND);

		aRESTAPI.deleteEMoneyProvider( id);

		HttpHeaders headers = new HttpHeaders();

		return new ResponseEntity<Void>( headers, HttpStatus.NO_CONTENT);
	}
	@GetMapping
	@JsonView( Views.Admin.class)
	@RequestMapping( value="/e-money-provider/{id}/revs")
	public ResponseEntity<?> getEMoneyProviderRevs(@PathVariable( value="id") final Long id)
	{	
		HttpHeaders headers = new HttpHeaders();
		List<?> lst = aRESTAPI.getEMoneyProviderRevs(id);

		return new ResponseEntity<List<?>>( lst, headers, HttpStatus.OK);
	}
	/* End EMoney Provider */

	/* ShopStatus */
	@GetMapping
	@JsonView( Views.Admin.class)
	@RequestMapping( value="/shop-status/{id}")
	public ResponseEntity<?> loadShopStatus( @PathVariable( "id") final Long id)
	{
		ShopStatus entity = aRESTAPI.loadShopStatus( id);

		if( entity == null)
			return new ResponseEntity<ShopStatus>( HttpStatus.NOT_FOUND);

		HttpHeaders headers = new HttpHeaders();

		return new ResponseEntity<ShopStatus>( entity, headers, HttpStatus.OK);
	}
	@GetMapping
	@JsonView( Views.Admin.class)
	@RequestMapping( value="/shop-statuses")
	public ResponseEntity<?> loadShopStatuses( @RequestParam( name="page", defaultValue="1") final Integer page,
											 @RequestParam( name="pageSize", defaultValue="10") final Integer pageSize)
	{
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN"))
				return Tools.unauthorized();
			
			return Tools.handlePagedListJSON( aRESTAPI.listShopStatus( page, pageSize));
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@PostMapping
	@RequestMapping( value="/shop-status/add")
	public ResponseEntity<?> addShopStatus( @RequestBody @Valid ShopStatus entity,
											   BindingResult br)
	{
		try
		{	
			if( isAnonymous())
				return Tools.unauthorized();
			if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN"))
				return Tools.unauthorized();
			
			aRESTAPI.addShopStatus(entity);

			return new ResponseEntity<Void>( HttpStatus.CREATED);
		}
		catch( Exception e)
		{
			//Implement more accurate Exeception Management
			e.printStackTrace();
			return new ResponseEntity<Void>( HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PutMapping
	@RequestMapping( value="/shop-status/{id}/update")
	public ResponseEntity<?> updateShopStatus( @RequestBody ShopStatus entity, @PathVariable( "id") final Long id)
	{
		if( isAnonymous())
			return Tools.unauthorized();
		if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN"))
			return Tools.unauthorized();
		
		ShopStatus originalEntity = aRESTAPI.loadShopStatus( id);

		if( originalEntity == null)
			return new ResponseEntity<Void>( HttpStatus.NOT_FOUND);

		BeanUtils.copyProperties(entity, originalEntity, Tools.getNullPropertyNames(entity));

		aRESTAPI.updateShopStatus( originalEntity);

		HttpHeaders headers = new HttpHeaders();
		return new ResponseEntity<Void>( headers, HttpStatus.OK);

	}
	@DeleteMapping
	@RequestMapping( value="/shop-status/{id}/delete")
	public ResponseEntity<?> deleteShopStatus(@PathVariable( "id") final Long id)
	{
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN"))
				return Tools.unauthorized();
			
			if( aRESTAPI.loadShopStatus( id) == null)
				return Tools.entityNotFound();

			aRESTAPI.deleteShopStatus( id);

			return new ResponseEntity<Void>( HttpStatus.NO_CONTENT);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	/* End ShopStatus */
	
	/* SubOffers */
	@GetMapping
	@JsonView( Views.Admin.class)
	@RequestMapping( value="/sub-offer/get/{id}")
	public ResponseEntity<?> getSubOffer( @PathVariable( "id") final Long id)
	{
		try
		{
			SubOffer entity = aRESTAPI.loadSubOffer( id);

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
	@JsonView( Views.Admin.class)
	@RequestMapping( value="/sub-offers")
	public ResponseEntity<?> listSubOffer( @RequestParam( name="page", defaultValue="1") final Integer page,
											@RequestParam( name="pageSize", defaultValue="10") final Integer pageSize)
	{
		try
		{
			return Tools.handlePagedListJSON( aRESTAPI.listSubOffer(page, pageSize));
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@PostMapping
	@RequestMapping( value="/sub-offer/add")
	public ResponseEntity<Void> addSubOffer( @RequestBody SubOffer entity)
	{
		try
		{
			aRESTAPI.addSubOffer(entity);

			HttpHeaders headers = new HttpHeaders();
			return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
		}
		catch( ConstraintViolationException e)
		{
			e.printStackTrace();
			return new ResponseEntity<Void>( HttpStatus.BAD_REQUEST);
		}
		catch( Exception e)
		{
			//Implement more accurate Exeception Management
			System.out.println( e.getMessage());
			return new ResponseEntity<Void>( HttpStatus.BAD_REQUEST);
		}
	}
	@PutMapping
	@RequestMapping( value="/sub-offer/update/{id}")
	public ResponseEntity<Void> updateSubOffer( @RequestBody SubOffer entity, @PathVariable( "id") final Long id)
	{
		SubOffer originalEntity = aRESTAPI.loadSubOffer( id);

		if( originalEntity == null)
			return new ResponseEntity<Void>( HttpStatus.NOT_FOUND);

		BeanUtils.copyProperties(entity, originalEntity, Tools.getNullPropertyNames(entity));

		aRESTAPI.updateSubOffer( originalEntity);

		HttpHeaders headers = new HttpHeaders();
		return new ResponseEntity<Void>( headers, HttpStatus.OK);

	}
	@DeleteMapping
	@RequestMapping( value="/sub-offer/delete/{id}")
	public ResponseEntity<Void> deleteSubOffer( @PathVariable( "id") final Long id)
	{
		if( aRESTAPI.loadSubOffer( id) == null)
			return new ResponseEntity<Void>( HttpStatus.NOT_FOUND);

		aRESTAPI.deleteSubOffer( id);

		HttpHeaders headers = new HttpHeaders();

		return new ResponseEntity<Void>( headers, HttpStatus.NO_CONTENT);
	}
	/* End SubOffers */
	
	/* Shop Sub */
	@GetMapping
	@JsonView( Views.Admin.class)
	@RequestMapping( value="/shop-sub/status/{status}", method=RequestMethod.GET)

	public ResponseEntity<?> listShopSubByStatus( @PathVariable( "status") final Integer status,
												  @RequestParam( name="page", defaultValue="1") final Integer page,
												  @RequestParam( name="pageSize", defaultValue="0") final Integer pageSize)
	{
		try
		{
			GenericStatus genericStatus;
			switch( status)
			{
				case 0:
					genericStatus = GenericStatus.PENDING_PAYMENT;
					break;
				case 1:
					genericStatus = GenericStatus.PENDING_PAYMENT_CONFIRMATION;
					break;
				case 2:
					genericStatus = GenericStatus.PAID;
					break;
				case 3:
					genericStatus = GenericStatus.ONGOING;
					break;
				case 4:
					genericStatus = GenericStatus.EXPIRED;
					break;
				case 5:
					genericStatus = GenericStatus.CANCELLED;
					break;
				default:
					genericStatus = null;
					break;
			}
			
			PagedListJSON result = aRESTAPI.listShopSubByStatus( genericStatus, page, pageSize);

			return Tools.handlePagedListJSON( result);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@GetMapping
	@JsonView( Views.Admin.class)
	@RequestMapping( value="/shop-sub/{id}")
	public ResponseEntity<?> loadShopSub( @PathVariable( "id") final Long id)
	{
		ShopSub entity = aRESTAPI.loadShopSub( id);

		if( entity == null)
			return Tools.entityNotFound();

		return new ResponseEntity<ShopSub>( entity, HttpStatus.OK);
	}
	@GetMapping
	@JsonView( Views.Admin.class)
	@RequestMapping( value="/shop-subs")
	public ResponseEntity<?> listShopSub( @RequestParam( name="page", defaultValue="1") final Integer page,
													  @RequestParam( name="pageSize", defaultValue="10") final Integer pageSize)
	{
		return Tools.handlePagedListJSON(aRESTAPI.listShopSub( page, pageSize));
	}
	@PutMapping
	@RequestMapping( value="/shop-sub/update/{id}")
	public ResponseEntity<Void> updateShopSub( @RequestBody ShopSub entity, @PathVariable( "id") final Long id)
	{
		ShopSub originalEntity = aRESTAPI.loadShopSub(id);

		if( originalEntity == null)
			return new ResponseEntity<Void>( HttpStatus.NOT_FOUND);

		BeanUtils.copyProperties(entity, originalEntity, Tools.getNullPropertyNames(entity));

		aRESTAPI.updateShopSub( originalEntity);

		HttpHeaders headers = new HttpHeaders();
		return new ResponseEntity<Void>( headers, HttpStatus.OK);

	}
	/* End Shop Sub */
	
	/* EShop */
	@GetMapping
	@JsonView( Views.Admin.class)
	@RequestMapping( value="/e-shop/{id}")
	public ResponseEntity<?> loadEShop( @PathVariable( "id") final Long id)
	{
		EShop entity = aRESTAPI.loadEShop(id);

		if( entity == null)
			return Tools.entityNotFound();

		return new ResponseEntity<EShop>( entity, HttpStatus.OK);
	}
	@GetMapping
	@JsonView( Views.Admin.class)
	@RequestMapping( value="/e-shops")
	public ResponseEntity<?> loadEShops( @RequestParam( name="page", defaultValue="1") final Integer page,
										 @RequestParam( name="pageSize", defaultValue="10") final Integer pageSize)
	{
		try
		{
			return Tools.handlePagedListJSON( aRESTAPI.listEShops( page, pageSize));
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@PostMapping
	@RequestMapping( value="/e-shop/{id}/validate")
	public ResponseEntity<?> validateEShop( @PathVariable( "id") final Long id)
	{
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			
			if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN"))
				return Tools.unauthorized();
			
			EShop originalEntity = aRESTAPI.loadEShop(id);
			if (originalEntity == null)
				return Tools.entityNotFound();
			
			originalEntity.setIsEnabled( true);
			aRESTAPI.updateEShop( originalEntity);
			
			/* Fire Notification */
			notifications.approvedEntity( originalEntity);
			/* Fire Email */
			emails.approvedEntity( originalEntity);
			
			return Tools.ok();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
		/* Shop Subs */
		@GetMapping
		@JsonView( Views.Admin.class)
		@RequestMapping( value="/e-shop/{id}/shop-subs")
		public ResponseEntity<?> listShopSubByEshop( @PathVariable( "id") final Long id,
																 @RequestParam( name="page", defaultValue="1") final Integer page,
																 @RequestParam( name="pageSize", defaultValue="10") final Integer pageSize,
																 @RequestParam( name="orderByIdAsc", defaultValue="true") final boolean orderByIdAsc)
		{
			try
			{
				EShop eShop = aRESTAPI.loadEShop(id);
	
				if( eShop == null)
					return new ResponseEntity<Void>( HttpStatus.NOT_FOUND);
				
				return Tools.handlePagedListJSON( aRESTAPI.listShopSubsByEShop(id, page, pageSize, orderByIdAsc));
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		/* Manager */
		@GetMapping
		@JsonView( Views.Admin.class)
		@RequestMapping( value="/e-shop/{id}/manager")
		public ResponseEntity<?> loadEShopManager( @PathVariable( "id") final Long id)
		{
			try
			{
				if( isAnonymous())
					return Tools.unauthorized();
				
				if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN"))
					return Tools.unauthorized();
				
				EShop eShop = aRESTAPI.loadEShop(id);
	
				if( eShop == null)
					return Tools.entityNotFound();
				
				/* TODO:
				 * Check if User Admin is logged in
				 */
				return new ResponseEntity<User>( aRESTAPI.loadEShopManager(id), HttpStatus.OK);
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
	/* End EShop */
	
	/* Upgrade Requests */
	@GetMapping
	@JsonView( Views.Admin.class)
	@RequestMapping( "/upgrade-requests")
	public ResponseEntity<?> loadUpgradeRequests( @RequestParam( name="page", defaultValue="1") final Integer page,
												  @RequestParam( name="pageSize", defaultValue="10") final Integer pageSize,
												  @RequestParam( name="orderByIdAsc", defaultValue="true") final boolean orderByIdAsc,
												  @RequestParam( name="status", defaultValue="0") final Integer status)
	{
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN"))
				return Tools.unauthorized();
			
			return Tools.handlePagedListJSON( aRESTAPI.loadUpgradeRequests(page, pageSize, orderByIdAsc, status));
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@PostMapping
	@RequestMapping( "/upgrade-request/{id}/approve")
	public ResponseEntity<?> approveUpgradeRequest( @PathVariable( "id") final Long id)
	{
		try
		{
			if( isAnonymous() || !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN"))
				return Tools.unauthorized();
			
			UpgradeRequest entity = aRESTAPI.loadUpgradeRequest(id);
			
			if( entity.isValidated())
				return Tools.handleSingleFieldError( new FieldErrorResource( "upgrade-request", "validated", "AlreadyValidated", "This request was already validated"));
			
			entity.setValidated( true);
			
			aRESTAPI.updateUpgradeRequest( entity);
			
			User user = aRESTAPI.loadUser( entity.getBuyer().getUserInfo().getId());
			
			user.setApproved( true);
			user.addRole( aRESTAPI.loadRole( "ROLE_MANAGER"));
			
			aRESTAPI.updateUser(user);
			
			/* Notify User */
			notifications.buyerUpReqApproved(user.getUserInfo().getId());
			/*
			 * TODO
			 * Broadcast WebSocket
			 */
			/* EMail User */
			emails.approvedEntity( entity);
			
			return Tools.ok();
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@GetMapping
	@RequestMapping( "/upgrade-request/{id}")
	public ResponseEntity<?> loadUpgradeRequest( @PathVariable( "id") final Long id)
	{
		try
		{
			return new ResponseEntity<UpgradeRequest>( aRESTAPI.loadUpgradeRequest(id), HttpStatus.OK);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	/* End Upgrade Requests */
	
	/* Payments */
	@GetMapping
	@JsonView( Views.Admin.class)
	@RequestMapping( "/payment/{id}")
	public ResponseEntity<?> loadPayment( @PathVariable( "id") final Long id)
	{
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN"))
				return Tools.unauthorized();
			
			return new ResponseEntity<Payment>( aRESTAPI.loadPayment(id), HttpStatus.OK);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@GetMapping
	@JsonView( Views.Admin.class)
	@RequestMapping( "/payments")
	public ResponseEntity<?> loadPayments( @RequestParam( name="page", defaultValue="1") final Integer page,
										   @RequestParam( name="pageSize", defaultValue="10") final Integer pageSize,
										   @RequestParam( name="orderByIdAsc", defaultValue="true") final boolean orderByIdAsc,
										   @RequestParam( name="status", defaultValue="0") final Integer status)
	{
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN"))
				return Tools.unauthorized();
			
			return Tools.handlePagedListJSON( aRESTAPI.loadPayments(page, pageSize, status, orderByIdAsc));
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@PutMapping
	@RequestMapping( "/payment/{id}/accept")
	public ResponseEntity<?> acceptPayment( @PathVariable( "id") final Long id)
	{	
		try
		{
			//Reject user if not logged in
			if( SecurityContextHolder.getContext().getAuthentication().getPrincipal() == "anonymousUser")
				return new ResponseEntity<Void>( HttpStatus.UNAUTHORIZED);

			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			//Reject if user is not admin
			if( !user.hasRole("ROLE_ADMIN"))
				return Tools.forbidden();

			Payment payment = aRESTAPI.loadPayment( id);
			if( payment == null)
				return new ResponseEntity<Void>( HttpStatus.NOT_FOUND);

			//Control: Does the concerned entity required a payment ?
			
			payment.setValidationDate( new Timestamp( System.currentTimeMillis()));
			payment.setIsValid( true);
			aRESTAPI.updatePayment( payment);
			
			@SuppressWarnings("unchecked")
			List<ArticleOrder> paymentArticleOrders = (List<ArticleOrder>) aRESTAPI.loadPaymentArticleOrders(payment.getId(), 1, 0).getEntities();
			
			System.out.println( "ArticleOrder Count => " + paymentArticleOrders.size());
			if( paymentArticleOrders.size() > 0)
			{
				Long buyerId = null;
				for( ArticleOrder articleOrder : paymentArticleOrders)
				{
					ArticleOrder entity = aRESTAPI.loadArticleOrder( articleOrder.getId());
					entity.setStatus( OrderStatus.PAID);
					aRESTAPI.updateArticleOrder(entity);
					buyerId = entity.getUser().getUserInfo().getId();

					/* Websocket Updates */
					websocketService.userArticleOrderUpdated( aRESTAPI.loadUser( buyerId).getUsername(), entity.getId());
					websocketService.updatedArticleOrder( entity.getId());
					
					/* Notifications */
					notifications.entityPaymentAltered( payment, entity);
					/* Email */
					emails.alteredPayment(payment, entity);
				}

				if( buyerId != null)
					websocketService.userArticleOrdersUpdated( aRESTAPI.loadUser( buyerId).getUsername());
			}
			
			ShopSub paymentShopSub = aRESTAPI.loadPaymentShopSub( payment.getId());
			
			if( paymentShopSub != null)
			{
				paymentShopSub.setSubStatus( GenericStatus.PAID);
				aRESTAPI.updateShopSub( paymentShopSub);
				/* Notify ShopSub */
				notifications.entityPaymentAltered(payment, paymentShopSub);
				
				/* EMail Manager */
				emails.alteredPayment(payment, paymentShopSub);
			}

			AdvOffer paymentAdvOffer = aRESTAPI.loadPaymentAdvOffer( payment.getId());
			if( paymentAdvOffer != null)
			{
				if( paymentAdvOffer.isAutoEnabled())
					paymentAdvOffer.setStatus( GenericStatus.ONGOING);
				else
					paymentAdvOffer.setStatus( GenericStatus.PAID);
				
				aRESTAPI.updateAdvOffer( paymentAdvOffer);
				/* Notification */
				notifications.entityPaymentAltered(payment, paymentAdvOffer);
				/* EMail Manager */
				emails.alteredPayment(payment, paymentAdvOffer);
			}
			
			/* Admin Websocket */
			websocketService.alteredPayment(payment);
			
			return Tools.ok();
			
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@PutMapping
	@RequestMapping( "/payment/{id}/reject")
	public ResponseEntity<?> rejectPayment( @PathVariable( "id") final Long id)
	{		
		//Reject user if not logged in
		if( SecurityContextHolder.getContext().getAuthentication().getPrincipal() == "anonymousUser")
			return new ResponseEntity<Void>( HttpStatus.UNAUTHORIZED);

		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		//Reject if user is not admin
		if( !user.hasRole("ROLE_ADMIN"))
			return new ResponseEntity<Void>( HttpStatus.UNAUTHORIZED);

		Payment payment = aRESTAPI.loadPayment( id);
		if( payment == null)
			return new ResponseEntity<Void>( HttpStatus.NOT_FOUND);

		payment.setValidationDate( new Timestamp( System.currentTimeMillis()));
		payment.setIsValid( false);
		
		aRESTAPI.updatePayment( payment);
		//Control: Does the concerned entity required a payment ?

		@SuppressWarnings("unchecked")
		List<ArticleOrder> paymentArticleOrders = (List<ArticleOrder>) aRESTAPI.loadPaymentArticleOrders(payment.getId(), 1, 0).getEntities();
		
		if( paymentArticleOrders.size() > 0)
		{
			for( ArticleOrder articleOrder : paymentArticleOrders)
			{
				ArticleOrder entity = aRESTAPI.loadArticleOrder( articleOrder.getId());
				entity.setStatus( OrderStatus.PENDING_PAYMENT);
				aRESTAPI.updateArticleOrder(entity);
				
				websocketService.userArticleOrderUpdated( entity.getUser().getUsername(), entity.getId());
				websocketService.updatedArticleOrder( entity.getId());
				
				/* Notifications */
				notifications.entityPaymentAltered( payment, entity);
				/* Email */
				emails.alteredPayment(payment, entity);
			}
		}

		ShopSub paymentShopSub = aRESTAPI.loadPaymentShopSub( payment.getId());
		
		if( paymentShopSub != null)
		{
			paymentShopSub.setSubStatus( GenericStatus.PENDING_PAYMENT);
			aRESTAPI.updateShopSub( paymentShopSub);
			
			/* Notification */
			notifications.entityPaymentAltered(payment, paymentShopSub);
			/* Emails */
			emails.alteredPayment(payment, paymentShopSub);
		}

		AdvOffer paymentAdvOffer = aRESTAPI.loadPaymentAdvOffer( payment.getId());
		
		if( paymentAdvOffer != null)
		{
			paymentAdvOffer.setStatus( GenericStatus.PENDING_PAYMENT);
			aRESTAPI.updateAdvOffer( paymentAdvOffer);
			
			/* Notifications */
			notifications.entityPaymentAltered(payment, paymentAdvOffer);
			/* Emails */
			emails.alteredPayment(payment, paymentAdvOffer);
		}

		/* Admin Websocket */
		websocketService.alteredPayment(payment);
		
		return Tools.ok();
	}
		/* Article Order */
		@GetMapping
		@JsonView( Views.Admin.class)
		@RequestMapping( "/payment/{id}/article-orders")
		public ResponseEntity<?> loadPaymentArticleOrders(
											   @PathVariable( name="id", required=true) final Long id,
											   @RequestParam( name="page", defaultValue="1") final Integer page,
											   @RequestParam( name="pageSize", defaultValue="10") final Integer pageSize)
		{
			try
			{
				if( isAnonymous())
					return Tools.unauthorized();
				if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN"))
					return Tools.unauthorized();
				
				return Tools.handlePagedListJSON( aRESTAPI.loadPaymentArticleOrders(id, page, pageSize));
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		/* ShopSub */
		@GetMapping
		@JsonView( Views.Admin.class)
		@RequestMapping( "/payment/{id}/shop-sub")
		public ResponseEntity<?> loadPaymentShopSub(
											   @PathVariable( name="id", required=true) final Long id)
		{
			try
			{
				if( isAnonymous())
					return Tools.unauthorized();
				if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN"))
					return Tools.unauthorized();
				
				return new ResponseEntity<ShopSub>( aRESTAPI.loadPaymentShopSub(id), HttpStatus.OK);
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		/* ShopSub */
		@GetMapping
		@JsonView( Views.Admin.class)
		@RequestMapping( "/payment/{id}/adv-offer")
		public ResponseEntity<?> loadPaymentAdvOffer( @PathVariable( name="id", required=true) final Long id)
		{
			try
			{
				if( isAnonymous())
					return Tools.unauthorized();
				if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN"))
					return Tools.unauthorized();
				
				return new ResponseEntity<AdvOffer>( aRESTAPI.loadPaymentAdvOffer(id), HttpStatus.OK);
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
	/* End Payments */
	
	/* Exception Handler */
	@ExceptionHandler( { InvalidRequestException.class})
	protected ResponseEntity<?> handleInvalidRequest( InvalidRequestException ire, WebRequest request)
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		result.put( "code", "InvalidEntity");
		result.put( "message", "Invalid Entity");
		
		List<InvalidEntityFieldError> fieldErrors = new ArrayList<InvalidEntityFieldError>();
		
		for( FieldError fieldError : ire.getErrors().getFieldErrors())
		{
			fieldErrors.add( new InvalidEntityFieldError( fieldError));
		}
		
		result.put( "errors", fieldErrors);

		return new ResponseEntity<HashMap<String, Object>>( result, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	@ExceptionHandler( { ConstraintViolationException.class} )
	protected ResponseEntity<?> handleConstraintViolationException( ConstraintViolationException cve)
	{
		return null;
	}
	/* End Exception Handler */
	
	/* Articles */
	
	/* End Articles */
	
	/* Article Orders */
	@GetMapping
	@JsonView( Views.Admin.class)
	@RequestMapping( "/article-orders")
	public ResponseEntity<?> loadArticleOrders( @RequestParam( name="page", defaultValue="1") final Integer page,
											    @RequestParam( name="pageSize", defaultValue="0") final Integer pageSize,
											    @RequestParam( name="orderByIdAsc", defaultValue="true") final boolean orderByIdAsc,
											    @RequestParam( name="orderStatus", defaultValue="-1") final Integer status)
	{
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			
			if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN"))
				return Tools.unauthorized();
			
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
				case 5:
					orderStatus = OrderStatus.PENDING_EXPENSE;
					break;
				case 6:
					orderStatus = OrderStatus.ESHOP_PAID;
					break;
				case -1:
					orderStatus = OrderStatus.ALL;
					break;
				default:
					orderStatus = OrderStatus.ALL;
					break;
					
			}
			
			return Tools.handlePagedListJSON( aRESTAPI.loadArticleOrders(page, pageSize, orderStatus, orderByIdAsc));
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@GetMapping
	@JsonView( Views.Admin.class)
	@RequestMapping( "/article-order/{id}")
	public ResponseEntity<?> loadArticleOrder( @PathVariable( "id") final Long id) 
	{
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			
			if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN"))
				return Tools.forbidden();
			
			ArticleOrder entity = aRESTAPI.loadArticleOrder( id);
			
			if( entity == null)
				return Tools.entityNotFound();
			
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
			return Tools.handlePagedListJSON(aRESTAPI.loadArticleOrderPayments(id, page, pageSize));
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	/* End Article Orders */
	
	/* AdvOffer */
	@GetMapping
	@JsonView( Views.Admin.class)
	@RequestMapping( "/adv-offers")
	public ResponseEntity<?> loadAdvOffers( @RequestParam( name="page", defaultValue="1") final Integer page,
										    @RequestParam( name="pageSize", defaultValue="0") final Integer pageSize,
										    @RequestParam( name="orderByIdAsc", defaultValue="true") final boolean orderByIdAsc,
										    @RequestParam( name="status", defaultValue="-1") final Integer status)
	{
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			
			if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN"))
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
			
			return Tools.handlePagedListJSON( aRESTAPI.loadAdvOffers( page, pageSize, advOfferStatus, orderByIdAsc));
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<List<ArticleOrder>>( HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping
	@JsonView( Views.Admin.class)
	@RequestMapping( "/adv-offer/{id}")
	public ResponseEntity<?> loadAdvOffer( @PathVariable( name="id", required=true) final Long id)
	{
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			
			if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN"))
				return Tools.unauthorized();
			
			return new ResponseEntity<AdvOffer>( aRESTAPI.loadAdvOffer(id), HttpStatus.OK);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
		/* Payments */
		@GetMapping
		@JsonView( Views.Admin.class)
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
				
				if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN"))
					return Tools.unauthorized();
				
				if( aRESTAPI.loadAdvOffer(id) == null)
					return Tools.entityNotFound();
				
				return Tools.handlePagedListJSON( aRESTAPI.loadAdvOfferPayments(id, page, pageSize, orderByIdAsc));
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
	/* End AdvOffer */
	
	/* Users */
	@GetMapping
	@JsonView( Views.Admin.class)
	@RequestMapping( "/user/{id}")
	public ResponseEntity<?> loadUser( @PathVariable( name="id", required=true) final Long id)
	{
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN"))
				return Tools.unauthorized();
			
			User user = aRESTAPI.loadUser(id);
			
			if( user == null)
				return Tools.entityNotFound();
			
			return new ResponseEntity<User>( user, HttpStatus.OK);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
		/* Buyers */
		@GetMapping
		@JsonView( Views.Admin.class)
		@RequestMapping( "/buyers")
		public ResponseEntity<?> loadBuyers( @RequestParam( name="page", defaultValue="1") final Integer page,
											 @RequestParam( name="pageSize", defaultValue="10") final Integer pageSize,
											 @RequestParam( name="orderByIdAsc", defaultValue="true") final boolean orderByIdAsc)
		{
			try
			{
				return Tools.handlePagedListJSON( aRESTAPI.loadBuyers(page, pageSize, orderByIdAsc));
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		/* Buyer Profile Validation */
		@GetMapping
		@JsonView( Views.Admin.class)
		@RequestMapping( "/profiles-pending-validation")
		public ResponseEntity<?> loadProfilesPendingValidation( @RequestParam( name="page", defaultValue="1") final Integer page,
													  			@RequestParam( name="pageSize", defaultValue="10") final Integer pageSize)
		{
			try
			{
				return Tools.handlePagedListJSON( aRESTAPI.loadBuyerPendingProfileValidation(page, pageSize));
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		@PostMapping
		@RequestMapping( "/user/{id}/validate-profile")
		public ResponseEntity<?> validateBuyerProfile( @PathVariable( "id") final Long id)
		{
			try
			{
				if( isAnonymous())
					return Tools.unauthorized();
				
				if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN"))
					return Tools.unauthorized();
				
				User user = aRESTAPI.loadUser(id);
				
				if( user == null)
					return Tools.entityNotFound();
				
				user.setApproved(true);
				aRESTAPI.updateUser( user);
				
				if( user.hasRole( "ROLE_MANAGER")) {
					aRESTAPI.addSupervisedManagerToAdmin( getLoggedUserFromPrincipal().getUserInfo().getId(), user.getUserInfo().getId());
				}
				
				/* Broadcast on websocket */
	            websocketService.userProfileUpdated( user.getUsername());
	            /* Save notification */
	            notifications.approvedEntity( user);
	            /* Email */
	            emails.approvedEntity(user);
	            
				return Tools.ok();
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		/* Admin - Supervised Managers */
		@GetMapping
		@JsonView( Views.Admin.class)
		@RequestMapping( "/logged-user/supervised-managers")
		public ResponseEntity<?> loadSupervisedManagers( @RequestParam( name="page", defaultValue="1") final Integer page,
														 @RequestParam( name="pageSize", defaultValue="10") final Integer pageSize)
		{
			try
			{
				if( isAnonymous())
					return Tools.unauthorized();
				
				if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN"))
					return Tools.unauthorized();
				
				return Tools.handlePagedListJSON( aRESTAPI.loadSupervisedManagers( getLoggedUserFromPrincipal().getUserInfo().getId(), page, pageSize));
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		@PostMapping
		@JsonView( Views.Admin.class)
		@RequestMapping( "/logged-user/add-supervised-manager")
		public ResponseEntity<?> addSupervisedManager( @RequestParam( name="adminId", required=true) final Long adminId,
													   @RequestParam( name="managerId", required=true) final Long managerId)
		{
			try
			{
				if( isAnonymous())
					return Tools.unauthorized();
				
				if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN"))
					return Tools.unauthorized();
				
				aRESTAPI.addSupervisedManagerToAdmin(adminId, managerId);
				
				return Tools.ok();
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		@PostMapping
		@JsonView( Views.Admin.class)
		@RequestMapping( "/logged-user/remove-supervised-manager")
		public ResponseEntity<?> removeSupervisedManager( @RequestParam( name="adminId", required=true) final Long adminId,
													   @RequestParam( name="managerId", required=true) final Long managerId)
		{
			try
			{
				if( isAnonymous())
					return Tools.unauthorized();
				
				if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN"))
					return Tools.unauthorized();
				
				aRESTAPI.removeSupervisedManagerFromAdmin(adminId, managerId);
				
				return Tools.ok();
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
	/* End Users */
	
	/* Logged User */
		/* Payments */
		@GetMapping
		@JsonView( Views.Admin.class)
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
				
				if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN"))
					return Tools.unauthorized();
				
				return Tools.handlePagedListJSON( aRESTAPI.loadPayments(page, pageSize, status, orderByIdAsc));
				/* TODO: Change to Addressed Payments */
				//return Tools.handlePagedListJSON( aRESTAPI.loadAdminPayments( getLoggedUserFromPrincipal().getUserInfo().getId(), status, page, pageSize, orderByIdAsc));
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
		/* ArticleOrders */
		@GetMapping
		@JsonView( Views.Admin.class)
		@RequestMapping( "/logged-user/article-orders")
		public ResponseEntity<?> loadAdminArticleOrders( @RequestParam( name="page", defaultValue="1") final Integer page,
													    @RequestParam( name="pageSize", defaultValue="0") final Integer pageSize,
													    @RequestParam( name="orderByIdAsc", defaultValue="true") final boolean orderByIdAsc,
													    @RequestParam( name="orderStatus", defaultValue="-1") final Integer status)
		{
			try
			{
				if( isAnonymous())
					return Tools.unauthorized();
				
				if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN"))
					return Tools.forbidden();
				
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
					case 5:
						orderStatus = OrderStatus.PENDING_EXPENSE;
						break;
					case 6:
						orderStatus = OrderStatus.ESHOP_PAID;
						break;
					case -1:
						orderStatus = OrderStatus.ALL;
						break;
					default:
						orderStatus = OrderStatus.ALL;
						break;
				}
				
				return Tools.handlePagedListJSON( aRESTAPI.loadAdminArticleOrders( getLoggedUserFromPrincipal().getUserInfo().getId(), page, pageSize, orderStatus, orderByIdAsc));
			}
			catch( Exception e)
			{
				e.printStackTrace();
				return Tools.internalServerError();
			}
		}
	/* End Logged User */
	
	/* Category */
	@GetMapping
	@JsonView( Views.Admin.class)
	@RequestMapping( value="/category/{id}")
	public ResponseEntity<?> loadCategory( @PathVariable( "id") final Long id)
	{
		Category entity = aRESTAPI.loadCategory( id);

		if( entity == null)
			return Tools.entityNotFound();

		return new ResponseEntity<Category>( entity, HttpStatus.OK);
	}
	@GetMapping
	@JsonView( Views.Admin.class)
	@RequestMapping( value="/category/{id}/root")
	public ResponseEntity<?> loadCategoryRoot( @PathVariable( "id") final Long id)
	{
		Category entity = aRESTAPI.loadCategory( id);

		if( entity == null)
			return Tools.entityNotFound();
		
		return new ResponseEntity<Category>( entity.getRoot(), HttpStatus.OK);
	}
	@GetMapping
	@JsonView( Views.Admin.class)
	@RequestMapping( value="/categories")
	public ResponseEntity<?> loadCategories( @RequestParam( name="page", defaultValue="1") final Integer page,
											 @RequestParam( name="pageSize", defaultValue="0") final Integer pageSize)
	{
		return Tools.handlePagedListJSON( aRESTAPI.loadCategories(page, pageSize));
	}
	@GetMapping
	@JsonView( Views.Admin.class)
	@RequestMapping( value="/categories/level/{level}")
	public ResponseEntity<?> loadCategories( @PathVariable( "level") final Integer level,
														  @RequestParam( name="page", defaultValue="1") final Integer page,
														  @RequestParam( name="pageSize", defaultValue="0") final Integer pageSize)
	{
		return Tools.handlePagedListJSON( aRESTAPI.loadCategories(level, page, pageSize));
	}
	@PostMapping
	@RequestMapping( value="/category/add")
	public ResponseEntity<?> addCategory( @RequestParam( name="label") String label,
										  @RequestParam( name="description") String description,
										  @RequestParam( name="parent", required = false) String parent)
	{
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			
			if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN")) {
				return Tools.unauthorized();
			}
			
			ObjectMapper mapper = new ObjectMapper();
			
			Category category = new Category( label, true);
			category.setDescription(description);
			
			if( parent != null)
			{
				Category parentCategory = aRESTAPI.loadCategory( mapper.readValue(parent, Long.class));
				if( parentCategory == null)
					return new ResponseEntity<Void>( HttpStatus.BAD_REQUEST);
				
				category.setRoot( parentCategory);
			}
			
			aRESTAPI.addCategory( category);

			return Tools.created();
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@PutMapping
	@RequestMapping( value="/category/{id}/update")
	public ResponseEntity<?> updateCategory( @RequestBody Category entity, @PathVariable( "id") final Long id)
	{
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			
			if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN")) {
				return Tools.unauthorized();
			}
			
			Category originalEntity = aRESTAPI.loadCategory( id);

			if( originalEntity == null)
				return Tools.entityNotFound();

			BeanUtils.copyProperties( entity, originalEntity, Tools.getNullPropertyNames(entity));
			aRESTAPI.updateCategory( originalEntity);

			return Tools.ok();
			
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}

	}
	@DeleteMapping
	@RequestMapping( value="/category/{id}/delete")
	public ResponseEntity<?> deleteCategory(@PathVariable( "id") final Long id)
	{
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			
			if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN")) {
				return Tools.unauthorized();
			}
			
			if( aRESTAPI.loadCategory( id) == null)
				return Tools.entityNotFound();

			aRESTAPI.deleteCategory( id);

			return Tools.ok();
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.unauthorized();
		}
		
	}
	@GetMapping
	@JsonView( Views.Admin.class)
	@SuppressWarnings("unchecked")
	@RequestMapping( "/categories/jstree-formated")
	public ResponseEntity<?> loadJSTreeFormatedCategories()
	{
		/* Specially format for the JSTree plugin */
		try
		{
			List<CategoryTreeJSON> entities = new ArrayList<CategoryTreeJSON>();
			
			for( Category category : (List<Category>) aRESTAPI.loadCategories( 1, 0).getEntities())
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
	/* End Category */
	
	/* Slide */
	@GetMapping
	@JsonView( Views.Admin.class)
	@RequestMapping( value="/slides")
	public ResponseEntity<?> loadSlides( @RequestParam( name="page", defaultValue="1") final Integer page,
									     @RequestParam( name="pageSize", defaultValue="0") final Integer pageSize,
									     @RequestParam( name="orderByDispOrderAsc", defaultValue="true") final boolean orderByDispOrderAsc)
	{
		try
		{
			return Tools.handlePagedListJSON( aRESTAPI.loadSlides( page, pageSize, orderByDispOrderAsc));	
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@PostMapping
	@RequestMapping( value="/slide/add")
	public ResponseEntity<?> addSlide( @RequestParam( name="entity", required=true) final String unsafeEntity,
									   @RequestParam( name="file", required = true) MultipartFile file)
	{
		try
		{
			ObjectMapper mapper = new ObjectMapper();
			Slide entity = mapper.readValue( unsafeEntity, Slide.class);
			
			if( isAnonymous())
				return Tools.unauthorized();
			
			if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN")) {
				return Tools.unauthorized();
			}
			
			/* TODO More controls */
			if( !Tools.isValidImage(file))
				return Tools.handleSingleFieldError( new FieldErrorResource( "Slide", "file", "Invalid", "Invalid file."));
			
			HashMap<String, String> fileNames = drive.uploadFileDebug( file, GoogleDriveUploadType.SLIDE);
			entity.setFilename( fileNames.get( "localFileName"));
			entity.setGoogleId( fileNames.get( "googleId"));
			
			if( entity.isDisplayed())
				entity.setDisplayOrder( aRESTAPI.loadDisplayedSlides(1, 0).getEntities().size() + 1);
			
			aRESTAPI.addSlide(entity);
			
			return Tools.created();
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@SuppressWarnings("unchecked")
	@PutMapping
	@RequestMapping( value="/slide/{id}/changed-displayed")
	public ResponseEntity<?> changeSlideDisplay( @PathVariable( "id") final Long id)
	{
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			
			if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN"))
				return Tools.unauthorized();
			
			Slide entity = aRESTAPI.loadSlide(id);
			if( entity == null)
				return Tools.entityNotFound();
			
			entity.setDisplayed( !entity.isDisplayed());
			Integer dispOrder = entity.getDisplayOrder();
			if( !entity.isDisplayed()) {
				entity.setDisplayOrder(0);
				/* Correctly reorder slides */
				List<Slide> slides = (List<Slide>) aRESTAPI.loadSlides(1, 0, true).getEntities();
				for( Slide slide : slides)
				{
					if( slide.isDisplayed()) {
						if( slide.getDisplayOrder() > dispOrder) {
							slide.setDisplayOrder( slide.getDisplayOrder() - 1);
							aRESTAPI.updateSlide(slide);
						}
					}
				}
			}
			else
				/* Display slide as last */
				entity.setDisplayOrder( aRESTAPI.loadDisplayedSlides(1, 0).getEntities().size() + 1);
			
			aRESTAPI.updateSlide(entity);
			
			return Tools.ok();
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@PostMapping
	@RequestMapping( value="/slide/{id}/move")
	public ResponseEntity<?> moveSlide( @PathVariable( "id") final Long id,
										@RequestParam( name="up", required=true) final boolean moveUp)
	{
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			
			if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN"))
				return Tools.unauthorized();
			
			Slide entity = aRESTAPI.loadSlide(id);
			if( entity == null)
				return Tools.entityNotFound();
			
			if( !entity.isDisplayed())
				return Tools.handleSingleFieldError( new FieldErrorResource( "Slide", "displayed", "Invalid", "This slide is not displayed to begin with."));
			
			if( moveUp) {
				if( entity.getDisplayOrder() > 1) {
					int nextDispOrder = entity.getDisplayOrder() - 1;
					Slide alteredEntity = aRESTAPI.loadSlide( nextDispOrder);
					alteredEntity.setDisplayOrder( nextDispOrder + 1);
					
					entity.setDisplayOrder( nextDispOrder);
					aRESTAPI.updateSlide(alteredEntity);
					aRESTAPI.updateSlide(entity);
				}
			}
			else {
				int nextDispOrder = entity.getDisplayOrder() + 1;
				Slide alteredEntity = aRESTAPI.loadSlide( nextDispOrder);
				alteredEntity.setDisplayOrder( nextDispOrder - 1);
				
				entity.setDisplayOrder( nextDispOrder);
				aRESTAPI.updateSlide(alteredEntity);
				aRESTAPI.updateSlide(entity);
			}
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
	@RequestMapping( value="/slide/{id}/delete")
	public ResponseEntity<?> deleteSlide( @PathVariable( "id") final Long id)
	{
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			
			if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN"))
				return Tools.unauthorized();
			
			Slide entity = aRESTAPI.loadSlide(id);
			if( entity == null)
				return Tools.entityNotFound();
			
			Integer currentDispOrder = entity.getDisplayOrder();
			entity.setDeleted(true);
			entity.setDisplayed(false);
			entity.setDisplayOrder(0);
			
			aRESTAPI.updateSlide(entity);
			
			/* TODO Reecalculate order for all stuff */
			List<Slide> slides = (List<Slide>) aRESTAPI.loadSlides(1, 0, true).getEntities();
			for( Slide slide : slides)
			{
				if( slide.isDisplayed()) {
					if( slide.getDisplayOrder() > currentDispOrder) {
						slide.setDisplayOrder( slide.getDisplayOrder() - 1);
						aRESTAPI.updateSlide(slide);
					}
				}
					
			}
			
			return Tools.ok();
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	/* End Slide */
	
	/* Expense */
	@RequestMapping( "expense/add")
	public ResponseEntity<?> addExpense( final Expense entity)
	{
		try
		{
			
			return Tools.ok();
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@GetMapping
	@JsonView( Views.Admin.class)
	@RequestMapping( "/logged-user/expenses")
	public ResponseEntity<?> loadAdminExpense( @RequestParam( name="page", defaultValue="1") final Integer page,
											   @RequestParam( name="pageSize", defaultValue="10") final Integer pageSize,
											   @RequestParam( name="orderByIdAsc", defaultValue="false") final boolean orderByIdAsc)
	{
		try
		{
			if( isAnonymous())
				return Tools.unauthorized();
			
			if( !getLoggedUserFromPrincipal().hasRole( "ROLE_ADMIN"))
				return Tools.forbidden();
			
			return Tools.handlePagedListJSON( aRESTAPI.loadAdminExpenses( getLoggedUserFromPrincipal().getUserInfo().getId(), 1, 10, orderByIdAsc));
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@GetMapping
	@RequestMapping( "/expense/{id}")
	@JsonView( Views.Admin.class)
	public ResponseEntity<?> loadExpense( @PathVariable( "id") final Long id)
	{
		try
		{
			return new ResponseEntity<Expense>( aRESTAPI.loadExpense(id), HttpStatus.OK);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
		/* EAccount for Payment */
//		@GetMapping
//		@JsonView( Views.Public.class)
//		@RequestMapping( "e-accounts-for-expenses")
//		public ResponseEntity<?> loadAdminEAccountForPayments( @RequestParam( name="")
//															   @RequestParam( name="page", defaultValue="1") final Integer page,
//														  	   @RequestParam( name="pageSize", defaultValue="5") final Integer pageSize)
//		{
//			try
//			{
//				/* Kick if User is not logged */
//				if( isAnonymous())
//					return Tools.unauthorized();
//				
//				return Tools.handlePagedListJSON( pRESTAPI.loadAdminEAccountsForPayments(page, pageSize));
//			}
//			catch( Exception e)
//			{
//				e.printStackTrace();
//				return Tools.internalServerError();
//			}
//		}
	/* End Expense */
	
	/* WebSocket */
	@GetMapping
	@RequestMapping( "/logged-users")
	public ResponseEntity<?> loadConnectUsers()
	{
		try
		{
			List<String> result = new ArrayList<String>();
			
			for (SimpUser user : this.userRegistry.getUsers()) {
				result.add(user.toString());
			}
			
			return new ResponseEntity<List<String>>( result, HttpStatus.OK);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	/* End WebSockets */
	
	@Autowired
	public AdminRESTAPIController(SimpUserRegistry userRegistry, SessionRegistry sessionRegistry)
	{
		super();
		this.userRegistry = userRegistry;
		this.sessionRegistry = sessionRegistry;
	}
	/* End WebSocket Tools  */
	
	/* DRY */
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
	
	/* Debug */
	@ExceptionHandler( { Exception.class })
	protected ResponseEntity<?> debugExceptionHandler( Exception e) {
		e.printStackTrace();
		emails.sendErrorReport(e);
		return Tools.internalServerError();
	}
	@GetMapping
	@RequestMapping( "/debug/init-db")
	public ResponseEntity<?> initDB()
	{
		if( aRESTAPI.loadEMoneyProviderByName( "MTN") == null)
			aRESTAPI.addEMoneyProvider( new EMoneyProvider( "MTN","\\+?(?:229)?(?:97|96|61|62|66|67)\\d{6}", null));
		
		if( aRESTAPI.loadEMoneyProviderByName( "Moov") == null)
			aRESTAPI.addEMoneyProvider( new EMoneyProvider( "Moov","\\+?(?:229)?(?:95|94)\\d{6}", null));
		
		Collection<String> roles = new ArrayList<String>();
		roles.add( "ROLE_BUYER");
		roles.add( "ROLE_MANAGER");
		roles.add( "ROLE_ADMIN");
		
		for( String role : roles)
		{
			if( aRESTAPI.loadRole( role) == null)
				aRESTAPI.addRole( new Role( role));
		}
		
		if( aRESTAPI.loadPaymentType( 1L) == null)
		{
			aRESTAPI.addPaymentType( new PaymentType( "Mobile"));
		}
		
		if( aRESTAPI.loadUserByUsername( "admin0") == null)
		{
			try
			{
				//Register UserInfo for New User
				UserInfo userInfo = new UserInfo();
				userInfo.setEmail( "admin0@admins.com");
				aRESTAPI.addUserInfo( userInfo);
				
				EAccount eAccount = new EAccount();
				eAccount.setAccount( "97000097");
				eAccount.setRelId( userInfo.geteAccounts().size() + 1L);
				aRESTAPI.addEAccountToUserInfo(userInfo.getId(), aRESTAPI.loadEMoneyProviderByName( "MTN").getId(), eAccount);
				
				EAccount eAccount2 = new EAccount();
				eAccount2.setAccount( "95000095");
				eAccount2.setRelId( userInfo.geteAccounts().size() + 1L);
				aRESTAPI.addEAccountToUserInfo( userInfo.getId(), aRESTAPI.loadEMoneyProviderByName( "Moov").getId(), eAccount2);
				
				//Set base for User
				User user = new User();
				user.setUserInfo( userInfo);
				user.setUsername( "admin0");
				
				//Adding Roles to user
				user.addRole( aRESTAPI.loadRole( "ROLE_ADMIN"));
				
				//Generating salt and confirmation token for user
				user.setSalt( RandomStringUtils.random( 32, true, true));
				//user.setConfToken( RandomStringUtils.random( 64, true, true));
				
				//Encoding password using CustomPasswordEncoder
				CustomPasswordEncoder passwordEncoder = new CustomPasswordEncoder();
				user.setPassword( passwordEncoder.encode( user.getSalt() + "admin0"));
				
				//Admin is enabled by default
				user.setIsEnabled( true);
				
				aRESTAPI.addUser( user);
			}
			catch( Exception e)
			{
				e.printStackTrace();
			}
		}
		
		return new ResponseEntity<String>( "Inited DB", HttpStatus.OK);
	}
}