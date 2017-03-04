package org.usayi.preta.service;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

@Service
public class EMailTemplateServiceImpl implements EMailTemplateService
{
	/* Activation */
	@Override
	public String activationTmpl()
	{
		try
		{
			String tmpl = FileUtils.readFileToString(new File( EMailTemplateService.baseTmplPath), "UTF-8");
			tmpl = tmpl.replaceAll( "%mailcontent%", FileUtils.readFileToString(new File( EMailTemplateService.activationContentPath), "UTF-8"));
			
			return tmpl;
		}
		catch( Exception e)
		{
			e.printStackTrace();
			return "";
		}
		
	}
	@Override
	public String activatedAccountTmpl() throws IOException
	{
		String tmpl = FileUtils.readFileToString(new File( EMailTemplateService.baseTmplPath), "UTF-8");
		return tmpl.replaceAll("%mailcontent%", FileUtils.readFileToString( new File( EMailTemplateService.activatedAccountContentPath)));
	}
	@Override
	public String passwordRequestTmpl() throws IOException
	{
		String tmpl = FileUtils.readFileToString(new File( EMailTemplateService.baseTmplPath), "UTF-8");
		return tmpl.replaceAll("%mailcontent%", FileUtils.readFileToString( new File( EMailTemplateService.passwordRequestContentPath)));
	}
	@Override
	public String passwordUpdatedTmpl() throws IOException
	{
		String tmpl = FileUtils.readFileToString(new File( EMailTemplateService.baseTmplPath), "UTF-8");
		return tmpl.replaceAll("%mailcontent%", FileUtils.readFileToString( new File( EMailTemplateService.passwordUpdatedContentPath)));
	}
	/* End Activation */
	
	/* ArticleOrder */
	@Override
	public String registeredOrderTmpl() throws IOException
	{
		String tmpl = FileUtils.readFileToString(new File( EMailTemplateService.baseTmplPath), "UTF-8");
		return tmpl.replaceAll("%mailcontent%", FileUtils.readFileToString( new File( EMailTemplateService.registeredOrderContentPath)));
	}
	@Override
	public String deliveredOrderTmpl() throws IOException
	{
		String tmpl = FileUtils.readFileToString(new File( EMailTemplateService.baseTmplPath), "UTF-8");
		return tmpl.replaceAll("%mailcontent%", FileUtils.readFileToString( new File( EMailTemplateService.deliveredOrderContentPath)));
	}
	@Override
	public String deliveringOrderTmpl() throws IOException
	{
		String tmpl = FileUtils.readFileToString(new File( EMailTemplateService.baseTmplPath), "UTF-8");
		return tmpl.replaceAll("%mailcontent%", FileUtils.readFileToString( new File( EMailTemplateService.deliveringOrderContentPath)));
	}
	/* End ArticleOrder */
	
	/* Approved */
	@Override
	public String approvedEShopTmpl() throws IOException
	{
		String tmpl = FileUtils.readFileToString(new File( EMailTemplateService.baseTmplPath), "UTF-8");
		return tmpl.replaceAll("%mailcontent%", FileUtils.readFileToString( new File( EMailTemplateService.approvedEShopContentPath)));
	}
	@Override
	public String approvedProfileTmpl() throws IOException
	{
		String tmpl = FileUtils.readFileToString(new File( EMailTemplateService.baseTmplPath), "UTF-8");
		return tmpl.replaceAll("%mailcontent%", FileUtils.readFileToString( new File( EMailTemplateService.approvedProfileContentPath)));
	}
	@Override
	public String approvedUpgradeRequestTmpl() throws IOException
	{
		String tmpl = FileUtils.readFileToString(new File( EMailTemplateService.baseTmplPath), "UTF-8");
		return tmpl.replaceAll("%mailcontent%", FileUtils.readFileToString( new File( EMailTemplateService.approvedUpgradeRequestContentPath)));
	}
	/* End Approved */
	
	/* Expiry */
	@Override
	public String expiredShopSubTmpl() throws IOException
	{
		String tmpl = FileUtils.readFileToString(new File( EMailTemplateService.baseTmplPath), "UTF-8");
		return tmpl.replaceAll("%mailcontent%", FileUtils.readFileToString( new File( EMailTemplateService.expiredShopSubContentPath)));
	}
	@Override
	public String expiredAdvOffer() throws IOException
	{
		String tmpl = FileUtils.readFileToString(new File( EMailTemplateService.baseTmplPath), "UTF-8");
		return tmpl.replaceAll("%mailcontent%", FileUtils.readFileToString( new File( EMailTemplateService.expiredAdvOfferContentPath)));
	}
	@Override
	public String expiryReminderShopSub() throws IOException
	{
		String tmpl = FileUtils.readFileToString(new File( EMailTemplateService.baseTmplPath), "UTF-8");
		return tmpl.replaceAll("%mailcontent%", FileUtils.readFileToString( new File( EMailTemplateService.expiryReminderShopSubContentPath)));
	}
	@Override
	public String expiryReminderAdvOffer() throws IOException
	{
		String tmpl = FileUtils.readFileToString(new File( EMailTemplateService.baseTmplPath), "UTF-8");
		return tmpl.replaceAll("%mailcontent%", FileUtils.readFileToString( new File( EMailTemplateService.expiryReminderAdvOfferContentPath)));
	}
	/* End Expiry */
	
	/* Altered Payment */
	@Override
	public String orderStartDeliveryTmpl() throws IOException
	{
		String tmpl = FileUtils.readFileToString(new File( EMailTemplateService.baseTmplPath), "UTF-8");
		return tmpl.replaceAll("%mailcontent%", FileUtils.readFileToString( new File( EMailTemplateService.orderStartDeliveryContentPath)));
	}
	@Override
	public String validOrderPaymentTmpl() throws IOException
	{
		String tmpl = FileUtils.readFileToString(new File( EMailTemplateService.baseTmplPath), "UTF-8");
		return tmpl.replaceAll("%mailcontent%", FileUtils.readFileToString( new File( EMailTemplateService.validOrderPaymentContentPath)));
	}
	@Override
	public String invalidOrderPaymentTmpl() throws IOException
	{
		String tmpl = FileUtils.readFileToString(new File( EMailTemplateService.baseTmplPath), "UTF-8");
		return tmpl.replaceAll("%mailcontent%", FileUtils.readFileToString( new File( EMailTemplateService.invalidOrderPaymentContentPath)));
	}
	@Override
	public String validShopSubPaymentTmpl() throws IOException
	{
		String tmpl = FileUtils.readFileToString(new File( EMailTemplateService.baseTmplPath), "UTF-8");
		return tmpl.replaceAll("%mailcontent%", FileUtils.readFileToString( new File( EMailTemplateService.validShopSubPaymentContentPath)));
	}
	@Override
	public String invalidShopSubPaymentTmpl() throws IOException
	{
		String tmpl = FileUtils.readFileToString(new File( EMailTemplateService.baseTmplPath), "UTF-8");
		return tmpl.replaceAll("%mailcontent%", FileUtils.readFileToString( new File( EMailTemplateService.invalidShopSubPaymentContentPath)));
	}
	@Override
	public String validAdvOfferPaymentTmpl() throws IOException
	{
		String tmpl = FileUtils.readFileToString(new File( EMailTemplateService.baseTmplPath), "UTF-8");
		return tmpl.replaceAll("%mailcontent%", FileUtils.readFileToString( new File( EMailTemplateService.validAdvOfferPaymentContentPath)));
	}
	@Override
	public String invalidAdvOfferPaymentTmpl() throws IOException
	{
		String tmpl = FileUtils.readFileToString(new File( EMailTemplateService.baseTmplPath), "UTF-8");
		return tmpl.replaceAll("%mailcontent%", FileUtils.readFileToString( new File( EMailTemplateService.invalidAdvOfferPaymentContentPath)));
	}
	/* End Altered Payment */
}
