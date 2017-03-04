package org.usayi.preta.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( value="/admin")
public class AdminController
{
	//AngularJS : Admin Application
	@RequestMapping( value="")
	public String dash()
	{	
		return "preta-admin/index";
	}
	//Logout Generic for all users
	@RequestMapping( value="/logout")
	public String logout()
	{
		SecurityContextHolder.getContext().setAuthentication( null);
		
		return "redirect:/admin-login";
	}
}