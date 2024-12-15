package webshop.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {

	@RequestMapping("adhome")
	public String homee(ModelMap model, HttpSession ses) {
		
		if(ses.getAttribute("rules")!=null) {
			if(((String)ses.getAttribute("rules")).equals("3")) {
				return "redirect:home.htm";
				
			}else if(((String)ses.getAttribute("rules")).equals("2")){
				return "redirect:emhome.htm";
			}
		}

		return "admin/home";
	}
	
}
