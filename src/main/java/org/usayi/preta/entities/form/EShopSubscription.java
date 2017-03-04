package org.usayi.preta.entities.form;

import org.usayi.preta.entities.EShop;
import org.usayi.preta.entities.Payment;
import org.usayi.preta.entities.ShopSub;

public class EShopSubscription
{
	private EShop eShop;
	
	private ShopSub shopSub;
	
	public EShopSubscription()
	{
		super();
		this.eShop = new EShop();
		this.shopSub = new ShopSub();
		this.payment = new Payment();
	}

	private Payment payment;

	public EShop geteShop()
	{
		return eShop;
	}

	public void seteShop(EShop eShop)
	{
		this.eShop = eShop;
	}

	public ShopSub getShopSub()
	{
		return shopSub;
	}

	public void setShopSub(ShopSub shopSub)
	{
		this.shopSub = shopSub;
	}

	public Payment getPayment()
	{
		return payment;
	}

	public void setPayment(Payment payment)
	{
		this.payment = payment;
	}
}
