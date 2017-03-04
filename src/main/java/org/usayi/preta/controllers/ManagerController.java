package org.usayi.preta.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( "/preta-manager")
public class ManagerController {
	
	//AngularJS : Manager Application
	@RequestMapping( value="")
	public String dashboard()
	{	
		return "preta-manager/index";
	}

	@RequestMapping( value="/logout")
	public String logout()
	{
		SecurityContextHolder.getContext().setAuthentication( null);

		return "redirect:/";
	}
}
