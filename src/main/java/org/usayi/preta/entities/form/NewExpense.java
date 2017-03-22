package org.usayi.preta.entities.form;

import java.util.ArrayList;
import java.util.Collection;

import org.usayi.preta.entities.ArticleOrder;
import org.usayi.preta.entities.EAccount;

public class NewExpense
{
	private Float amount;
	
	private String expenseRef;
	
	private EAccount eAccount;
	
	private EAccount adminEAccount;
	
	private Collection<ArticleOrder> articleOrders = new ArrayList<ArticleOrder>();

	public NewExpense(Float amount, String expenseRef, EAccount eAccount, EAccount adminEAccount,
			Collection<ArticleOrder> articleOrders)
	{
		super();
		this.amount = amount;
		this.expenseRef = expenseRef;
		this.eAccount = eAccount;
		this.adminEAccount = adminEAccount;
		this.articleOrders = articleOrders;
	}

	public NewExpense()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public Float getAmount()
	{
		return amount;
	}

	public void setAmount(Float amount)
	{
		this.amount = amount;
	}

	public String getExpenseRef()
	{
		return expenseRef;
	}

	public void setExpenseRef(String expenseRef)
	{
		this.expenseRef = expenseRef;
	}

	public EAccount geteAccount()
	{
		return eAccount;
	}

	public void seteAccount(EAccount eAccount)
	{
		this.eAccount = eAccount;
	}

	public EAccount getAdminEAccount()
	{
		return adminEAccount;
	}

	public void setAdminEAccount(EAccount adminEAccount)
	{
		this.adminEAccount = adminEAccount;
	}

	public Collection<ArticleOrder> getArticleOrders()
	{
		return articleOrders;
	}

	public void setArticleOrders(Collection<ArticleOrder> articleOrders)
	{
		this.articleOrders = articleOrders;
	}
	
}
