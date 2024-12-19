package webshop.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import webshop.dao.AccountDAO;
import webshop.entity.Account;
import webshop.entity.Brand;
import webshop.entity.Material;
import webshop.entity.Origin;
import webshop.entity.Type;
import webshop.security.Authentication;
import webshop.security.JwtUtil;
import webshop.security.Roles;

@Controller
public class AdminController {
	
	@Autowired
	AccountDAO account;
	

	@RequestMapping("adhome")
	public String homee(ModelMap model, HttpSession ses,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		int auth = Authentication.redirectAuthen(request, response);
		
		if(auth!=1) return "redirect:home.htm";

		return "admin/home";
	}
	
	
	//thao tác với đặt tính của sản phẩm
    @RequestMapping("account")
    public String prodAttributeG(ModelMap model, HttpServletRequest request,
    		HttpServletResponse response) throws IOException {
    	int auth = Authentication.redirectAuthen(request, response);
		
		if(auth!=1) return "redirect:home.htm";
    	
    	
    	return "admin/account";
    }
	
}
