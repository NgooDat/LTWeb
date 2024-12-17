package webshop.controller;

import java.util.List;

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

@Controller
public class AdminController {
	
	@Autowired
	AccountDAO account;
	

	@RequestMapping("adhome")
	public String homee(ModelMap model, HttpSession ses) {
		


		return "admin/home";
	}
	
	//thao tác với đặt tính của sản phẩm
    @RequestMapping("account")
    public String prodAttributeG(ModelMap model) {
    	//List<Account> dsAcc = ?
    	
    	
    	return "admin/account";
    }
	
}
