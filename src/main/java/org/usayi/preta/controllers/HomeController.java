package org.usayi.preta.controllers;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController
{
	@RequestMapping( value="/")
	public String home( Model model)
	{		
		return "preta/index";
	}
	
	/* Logins */
	@RequestMapping( value="/admin-login")
	public String adminlogin()
	{	
		return "preta-admin/auth";
	}
	@RequestMapping( value="/manager-login")
	public String managerlogin()
	{	
		return "preta-manager/auth";
	}
	
	/* Timers */
	
}
