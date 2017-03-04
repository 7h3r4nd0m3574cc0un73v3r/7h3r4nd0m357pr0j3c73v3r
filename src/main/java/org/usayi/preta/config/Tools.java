package org.usayi.preta.config;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.usayi.preta.entities.DurationType;
import org.usayi.preta.entities.json.PagedListJSON;
import org.usayi.preta.exceptions.FieldErrorResource;

public class Tools
{
	//Path to upload folder
	public static final String uploadPath ="/home/jboss/hobossa/";
	
	public static boolean isValidImage( MultipartFile file) {
		try
		{
			BufferedImage bi = ImageIO.read( file.getInputStream());
			if( bi == null)
				return false;
			
			return true;
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/* Keep uploaded files on local server ? For Dev purpose only */
	public static final boolean keepLocalFiles = false;
	
	/* Local FilePaths */
	public static final String visitedArticlesPath = uploadPath + "stats/visited-articles.json";
	
	/* Extracts Null Property from an object */
	public static String[] getNullPropertyNames (Object source) {
	    final BeanWrapper src = new BeanWrapperImpl(source);
	    java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

	    Set<String> emptyNames = new HashSet<String>();
	    for(java.beans.PropertyDescriptor pd : pds) {
	        Object srcValue = src.getPropertyValue(pd.getName());
	        if (srcValue == null) emptyNames.add(pd.getName());
	    }
	    String[] result = new String[emptyNames.size()];
	    return emptyNames.toArray(result);
	}
	
	/* Conf Token */
	public static String generateConfToken() {
		return  RandomStringUtils.random( 64, true, true);
	}
	/* Cart Config */
	public static final int cartLimit = 15;
	
	/* User */
	public static int computeUserProfileComp( org.usayi.preta.entities.User entity) {
		try
		{
			int propCount = 0, nullPropCount = 0;
			
			for( Field field : entity.getUserInfo().getClass().getDeclaredFields()) {
				
				if( !field.getName().equals( "id") &&
					!field.getName().equals( "middleName") &&
					!field.getName().equals( "email") &&
					!field.getName().equals( "eAccounts") &&
					!field.getName().equals( "serialVersionUID")) {
					
					propCount++;
					
					field.setAccessible( true);
					
					if( String.class.isAssignableFrom( field.getType()) ) {
						if( field.get( entity.getUserInfo()) == null || ((String) field.get( entity.getUserInfo())).isEmpty()) {
							nullPropCount++;
						}
					}
					else {
						if( field.get( entity.getUserInfo()) == null)
							nullPropCount++;
					}
				}
			}
			
			return (propCount - nullPropCount) * 100 / propCount;
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	/* EShop */
	public static int computeEShopProfileComp( org.usayi.preta.entities.EShop entity) {
		try
		{
			int propCount = 0, nullPropCount = 0;
			
			for( Field field : entity.getClass().getDeclaredFields()) {
				
				if( 
					!field.getName().equals( "id") &&
					!field.getName().equals( "regDate") &&
					!field.getName().equals( "logoFile") &&
					!field.getName().equals( "logoGoogleId") &&
					!field.getName().equals( "serialVersionUID") &&
					!field.getName().equals( "isEnabled") && 
					!field.getName().equals( "profileCompletion") && 
					!field.getName().equals( "isInMarket") &&
					!field.getName().equals( "shopSubs") &&
					!field.getName().equals( "paymentTypes") &&
					!field.getName().equals( "articles") &&
					!field.getName().equals( "manager") &&
					!field.getName().equals( "articlesCount") &&
					!field.getName().equals( "currentShopSub")
				 ) {
					propCount++;
					
					field.setAccessible( true);
					
					if( String.class.isAssignableFrom( field.getType()) ) {
						if( field.get( entity) == null || ((String) field.get( entity)).isEmpty()) {
							nullPropCount++;
						}
					}
					else {
						if( field.get( entity) == null)
							nullPropCount++;
					}
				}
			}
			
			return (propCount - nullPropCount) * 100 / propCount;
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	/* Times Entities */
	public static Timestamp computeEndDate( Timestamp startDate, DurationType durationType, Integer duration)
	{
		if( durationType == DurationType.DAY)
			return new Timestamp( new Date( DateUtils.addDays(startDate, duration).getTime()).getTime());
		else if( durationType == DurationType.WEEK)
			return new Timestamp( new Date( DateUtils.addWeeks(startDate, duration).getTime()).getTime());
		else if( durationType == DurationType.MONTH)
			return new Timestamp( new Date(DateUtils.addMonths( startDate, duration).getTime()).getTime());
		else if( durationType == DurationType.YEAR)
			return new Timestamp( new Date(DateUtils.addYears(startDate, duration).getTime()).getTime());
		/* Debug */
		else
			return new Timestamp( new Date(DateUtils.addMinutes( startDate, duration).getTime()).getTime());
	}
	
	/* Web Services Tools - DRY */
	@SuppressWarnings({ "rawtypes" })
	public static PagedListJSON generatePagedList( List<?> entities, Integer page, Integer pageSize)
	{
		try
		{	
			if( entities.size() <= 0)
				return new PagedListJSON( 0, 0, new ArrayList());
			
			PagedListJSON result = new PagedListJSON();
			result.setItemsNumber( entities.size());
			
			if( pageSize > 0) {
				result.setPagesNumber( computePagesNumber( entities.size(), pageSize));
				int from = (page - 1) * pageSize;
				result.setEntities( entities.subList(from, from+pageSize -1));
			} else {
				result.setPagesNumber( 1);
				result.setEntities(entities);
			}
			
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
	public static ResponseEntity<?> handlePagedListJSON( final PagedListJSON result) {		
		return new ResponseEntity<PagedListJSON>( result, HttpStatus.OK);
	}
	public static ResponseEntity<Void> internalServerError() {
		return new ResponseEntity<Void>( HttpStatus.INTERNAL_SERVER_ERROR);
	}
	public static ResponseEntity<?> entityNotFound() {
		return new ResponseEntity<Void>( HttpStatus.NOT_FOUND);
	}
	public static ResponseEntity<?> unauthorized() {
		return new ResponseEntity<Void>( HttpStatus.UNAUTHORIZED);
	}
	public static ResponseEntity<?> ok() {
		return new ResponseEntity<Void>( HttpStatus.OK);
	}
	public static ResponseEntity<?> created() {
		return new ResponseEntity<Void>( HttpStatus.CREATED);
	}
	public static ResponseEntity<?> forbidden() {
		return new ResponseEntity<Void>( HttpStatus.FORBIDDEN);
	}
	public static ResponseEntity<?> badRequest() {
		return new ResponseEntity<Void>( HttpStatus.BAD_REQUEST);
	}
	public static ResponseEntity<?> handleMultipleFieldErrors( List<FieldErrorResource> fErrors)
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		result.put( "code", "InvalidEntity");
		result.put( "message", "Invalid Entity");
		
		result.put( "errors", fErrors);

		return new ResponseEntity<HashMap<String, Object>>( result, HttpStatus.BAD_REQUEST);
	}
	public static ResponseEntity<?> handleSingleFieldError( FieldErrorResource fError)
	{
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		List<FieldErrorResource> fErrors = new ArrayList<FieldErrorResource>();
		fErrors.add( fError);
		
		result.put( "code", "InvalidEntity");
		result.put( "message", "Invalid Entity");
		
		result.put( "errors", fErrors);

		return new ResponseEntity<HashMap<String, Object>>( result, HttpStatus.BAD_REQUEST);
	}
	/* Error Handling */
	
	/* End Tools - DRY*/
}
