package org.usayi.preta.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.usayi.preta.Views;
import org.usayi.preta.buzlayer.IRESTAPI;
import org.usayi.preta.config.CustomPasswordEncoder;
import org.usayi.preta.config.Tools;
import org.usayi.preta.entities.Article;
import org.usayi.preta.entities.ArticleOrder;
import org.usayi.preta.entities.EAccount;
import org.usayi.preta.entities.OrderStatus;
import org.usayi.preta.entities.OrderedArticle;
import org.usayi.preta.entities.Picture;
import org.usayi.preta.entities.ShopSub;
import org.usayi.preta.entities.User;
import org.usayi.preta.entities.UserInfo;

import com.fasterxml.jackson.annotation.JsonView;


@RestController
@RequestMapping( value="/rest")
public class WebServiceControllerR
{
	@Autowired
	private IRESTAPI restBL;

	@GetMapping
	@JsonView( Views.Public.class)
	@RequestMapping( value="/emoney-provider/get/{id}/revs")
	public ResponseEntity<List<?>> getEMoneyProviderRevs(@PathVariable( value="id") final Long id)
	{	
		HttpHeaders headers = new HttpHeaders();
		List<?> lst = restBL.getEMoneyProviderRevs(id);

		return new ResponseEntity<List<?>>( lst, headers, HttpStatus.OK);
	}

	//ShopSub
	@GetMapping
	@JsonView( Views.Public.class)
	@RequestMapping( value="/shop-subs")
	public ResponseEntity<?> listShopSub( @RequestParam( name="page", defaultValue="1") final Integer page,
										  @RequestParam( name="pageSize", defaultValue="10") final Integer pageSize)
	{
		try
		{
			return Tools.handlePagedListJSON( restBL.listShopSub(page, pageSize));
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@GetMapping
	@JsonView( Views.Public.class)
	@RequestMapping( value="/shop-subs/pending")
	public ResponseEntity<?> listPendingShopSub( @RequestParam( name="page", defaultValue="1") final Integer page,
			   									 @RequestParam( name="pageSize", defaultValue="10") final Integer pageSize)
	{
		try
		{
			return Tools.handlePagedListJSON( restBL.listPendingShopSub(page, pageSize));
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	
	@PutMapping
	@RequestMapping( value="/shop-sub/edit/{id}")
	public ResponseEntity<Void> editShopSub( @RequestBody ShopSub entity, @PathVariable( "id") final Long id)
	{
		ShopSub originalEntity = restBL.getShopSub(id);

		if( originalEntity == null)
			return new ResponseEntity<Void>( HttpStatus.NOT_FOUND);

		BeanUtils.copyProperties(entity, originalEntity, Tools.getNullPropertyNames(entity));

		restBL.editShopSub( originalEntity);

		HttpHeaders headers = new HttpHeaders();
		return new ResponseEntity<Void>( headers, HttpStatus.OK);

	}
	
	//Users
	@GetMapping
	@JsonView( Views.Public.class)
	@RequestMapping( value="/managers")
	public ResponseEntity<List<User>> listManager()
	{
		List<User> entity = restBL.listManager();

		if( entity.isEmpty())
			return new ResponseEntity<List<User>>( HttpStatus.NO_CONTENT);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType( MediaType.APPLICATION_JSON);

		return new ResponseEntity<List<User>> (entity, headers, HttpStatus.OK) ;
	}
	@GetMapping
	@JsonView( Views.Public.class)
	@RequestMapping( value="/admins")
	public ResponseEntity<List<User>> listAdmin()
	{
		List<User> entity = restBL.listAdmin();

		if( entity.isEmpty())
			return new ResponseEntity<List<User>>( HttpStatus.NO_CONTENT);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType( MediaType.APPLICATION_JSON);

		return new ResponseEntity<List<User>> (entity, headers, HttpStatus.OK) ;
	}
	@GetMapping
	@JsonView( Views.Public.class)
	@RequestMapping( value="/user/get/conf-token/{token}")
	public ResponseEntity<User> getUserByConfToken( @PathVariable( "token") final String token)
	{
		User entity = restBL.getUserByConfToken( token);

		if( entity == null)
			return new ResponseEntity<User>( HttpStatus.NO_CONTENT);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType( MediaType.APPLICATION_JSON);

		return new ResponseEntity<User>(entity, headers, HttpStatus.OK) ;
	}
	@GetMapping
	@JsonView( Views.Public.class)
	@RequestMapping( value="/user/get/username/{username}")
	public ResponseEntity<User> getUserByUsername( @PathVariable( "username") final String username)
	{
		User entity = restBL.getUserByUsername( username);

		if( entity == null)
			return new ResponseEntity<User>( HttpStatus.NO_CONTENT);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType( MediaType.APPLICATION_JSON);

		return new ResponseEntity<User>(entity, headers, HttpStatus.OK) ;
	}
	@GetMapping
	@JsonView( Views.Public.class)
	@RequestMapping( value="/user/get/email/{email}")
	public ResponseEntity<?> getUserByEMail( @PathVariable( "email") final String email)
	{
		try
		{
			User entity = restBL.getUserByEMail( email);

			if( entity == null)
				return new ResponseEntity<User>( HttpStatus.NO_CONTENT);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType( MediaType.APPLICATION_JSON);

			return new ResponseEntity<User>(entity, headers, HttpStatus.OK) ;
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return Tools.internalServerError();
		}
	}
	@GetMapping
	@JsonView( Views.Public.class)
	@RequestMapping( value="/user/get/{id}")
	public ResponseEntity<User> getUser( @PathVariable( "id") final Long id)
	{
		User entity = restBL.getUser( id);

		if( entity == null)
			return new ResponseEntity<User>( HttpStatus.NO_CONTENT);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType( MediaType.APPLICATION_JSON);

		return new ResponseEntity<User>(entity, headers, HttpStatus.OK) ;
	}
	@PostMapping
	@RequestMapping( value="/user/admin/add")
	public ResponseEntity<Void> registerAdmin( @RequestBody User entity)
	{
		//Registers a new admin
		try
		{
			//Register UserInfo for New User
			UserInfo userInfo = new UserInfo( entity.getUserInfo());
			restBL.addUserInfo( userInfo);
			//Set base for User
			
			//Create new EAccount
			EAccount eAccount = new EAccount(entity.getMobile(), userInfo, restBL.getEMoneyProvider(1L));
			restBL.addEAccount( eAccount);
			
			userInfo.addEAccount(eAccount);
			restBL.editUserInfo( userInfo);
			
			User user = new User();
			user.setUserInfo( userInfo);

			user.setUsername( entity.getUsername());

			//Adding Roles to user
			user.addRole( restBL.getRole( "ROLE_ADMIN"));

			//Generating salt and confirmation token for user
			user.setSalt( RandomStringUtils.random( 32, true, true));
			//user.setConfToken( RandomStringUtils.random( 64, true, true));

			//Encoding password using CustomPasswordEncoder
			CustomPasswordEncoder passwordEncoder = new CustomPasswordEncoder();
			user.setPassword( passwordEncoder.encode( user.getSalt() + entity.getPassword()));

			//Admin is enabled by default
			user.setIsEnabled( true);

			restBL.addUser( user);

			//Controls: Conflit on ShopName
			HttpHeaders headers = new HttpHeaders();
			return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
		}
		catch( ConstraintViolationException e)
		{
			//Handle exceptions related to validation
			System.out.println( "Validation Exeptions");
			e.printStackTrace();
			System.out.println( "End Validation Exeptions");
			return new ResponseEntity<Void>( HttpStatus.BAD_REQUEST);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<Void>( HttpStatus.BAD_REQUEST);
		}
	}

	//Article
	@PutMapping
	@RequestMapping( value="/article/{articleId}/remove-picture/{pictureId}")
	public ResponseEntity<?> removeArticlePicture(@PathVariable( "articleId") final Long articleId,
											         @PathVariable( "pictureId") final Long pictureId)
	{
		try
		{

			if( SecurityContextHolder.getContext().getAuthentication().getName() == "anonymousUser")
				return new ResponseEntity<Void>( HttpStatus.UNAUTHORIZED);
			
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			if( !user.hasRole( "ROLE_MANAGER"))
				return new ResponseEntity<Void>( HttpStatus.UNAUTHORIZED);
			
			if( restBL.getArticle( articleId) == null)
				return new ResponseEntity<Void>( HttpStatus.NOT_FOUND);
			
			if( restBL.getPicture( pictureId) == null)
				return new ResponseEntity<Void>( HttpStatus.NOT_FOUND);

			Article article = restBL.getArticle( articleId);
			
			Picture articlePicture = null;
			for( Picture picture : article.getPictures())
			{
				if( picture.getId().longValue() == pictureId.longValue())
				{
					articlePicture = picture;
				}
			}
			
			if( articlePicture != null)
			{
				article.removePicture( articlePicture);
				article.updateDefaultPicture();
				restBL.editArticle( article);
				
				restBL.deletePicture( pictureId);
				
				return new ResponseEntity<Void>( HttpStatus.OK);
			}
			
			return new ResponseEntity<Void>( HttpStatus.BAD_REQUEST);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<Void>( HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping
	@RequestMapping( value="/article/fetchDefaultPic/{id}")
	public ResponseEntity<byte[]> fetchArticleDefaultPic(@PathVariable Long id) throws Exception
	{	
		if( restBL.getArticle( id) == null)
		{
			return new ResponseEntity<byte[]>( HttpStatus.NOT_FOUND);
		}

		if( restBL.getDefaultPicture( id) == null)
		{
			/* TODO
			 * Return default is doesn't have logo
			 */
			File defaultFile = new File( Tools.uploadPath + "default/article.jpg");
			return new ResponseEntity<byte[]>( IOUtils.toByteArray( new FileInputStream(defaultFile)), HttpStatus.OK);
		}


		File file = new File( restBL.getDefaultPicture(id).getFilename());

		return new ResponseEntity<byte[]>( IOUtils.toByteArray( new FileInputStream( file)), HttpStatus.OK);
	}
	@GetMapping
	@RequestMapping( value="/article/fetch-picture/{id}", produces=MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<byte[]> fetchPicture(@PathVariable Long id) throws Exception
	{	
		Picture entity = restBL.getPicture(id);
		
		if( entity == null)
		{
			return new ResponseEntity<byte[]>( HttpStatus.NOT_FOUND);
		}

		File file = new File( entity.getFilename());

		return new ResponseEntity<byte[]>( IOUtils.toByteArray( new FileInputStream( file)), HttpStatus.OK);
	}
	@GetMapping
	@RequestMapping( "article/{id}/rating")
	public ResponseEntity<?> getArticleRating( @PathVariable( "id") final Long id)
	{
		try
		{
			if( restBL.getArticle( id) == null)
				return new ResponseEntity<Void>( HttpStatus.NOT_FOUND);
			
			return new ResponseEntity<Integer>( restBL.getArticleRating( id), HttpStatus.OK);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<Void>( HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//ArticleOrder	
	@GetMapping
	@RequestMapping( "/article-order/{articleOrderId}/ordered-articles")
	public ResponseEntity<List<OrderedArticle>> listOrderedArticleByOrder( @PathVariable( "articleOrderId") final Long articleOrderId)
	{
		try
		{
			List<OrderedArticle> entities = restBL.listOrderedArticleByOrder(articleOrderId);
			
			return new ResponseEntity<List<OrderedArticle>>( entities, HttpStatus.OK);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<List<OrderedArticle>>( HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping
	@JsonView( Views.Public.class)
	@RequestMapping( "/e-shop/{eShopId}/article-orders")
	public ResponseEntity<List<ArticleOrder>> listArticleOrderByEShop( @PathVariable( "eShopId") final Long userId)
	{
		try
		{
			if( restBL.getUser(userId) == null)
				return new ResponseEntity<List<ArticleOrder>>( HttpStatus.NOT_FOUND);
			
			if( restBL.listArticleOrderByEShop(userId).isEmpty())
				return new ResponseEntity<List<ArticleOrder>>( HttpStatus.NO_CONTENT);
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType( MediaType.APPLICATION_JSON);
			
			List<ArticleOrder> entities = restBL.listArticleOrderByEShop( userId);
			
			return new ResponseEntity<List<ArticleOrder>>( entities, headers, HttpStatus.OK);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<List<ArticleOrder>>( HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}	
	@GetMapping
	@JsonView( Views.Public.class)
	@RequestMapping( "/article-order/{id}/buyer")
	public ResponseEntity<?> getArticleOrderUser( @PathVariable( "id") final Long id)
	{
		try
		{
			if( restBL.getArticleOrderUser( id) == null)
				return new ResponseEntity<Void>( HttpStatus.NOT_FOUND);
			
			return new ResponseEntity<User>( restBL.getArticleOrderUser( id), HttpStatus.OK);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<Void>( HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping
	@JsonView( Views.Public.class)
	@RequestMapping( "/article-order/status/{status}")
	public ResponseEntity<?> listArticleOrderByStatus( @PathVariable( "status") final Integer status)
	{
		try
		{
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
				default:
					orderStatus = null;
					break;
			}
			
			List<ArticleOrder> entities = restBL.listArticleOrderByStatus( orderStatus);
			
			if( entities.isEmpty())
				return new ResponseEntity<Void>( HttpStatus.NO_CONTENT);
			
			return new ResponseEntity<List<ArticleOrder>>( entities, HttpStatus.OK);
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<Void>( HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
//	@ExceptionHandler( { PersistenceException.class} )
//	protected ResponseEntity<Void> handleException( PersistenceException e)
//	{
//		e.printStackTrace();
//		System.out.println( "Handled smooth");
//		return new ResponseEntity<Void>( HttpStatus.INTERNAL_SERVER_ERROR);
//	}
}