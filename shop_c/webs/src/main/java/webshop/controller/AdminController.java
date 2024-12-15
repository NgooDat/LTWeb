package webshop.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {
	
	private String emrule  = "2";
	private String userrule  = "3";

	@RequestMapping("adhome")
	public String homee(ModelMap model, HttpSession ses) {
		
		if(ses.getAttribute("rules")!=null) {
			if(((String)ses.getAttribute("rules")).equals(userrule)) {
				return "redirect:home.htm";
				
			}else if(((String)ses.getAttribute("rules")).equals(emrule)){
				return "redirect:emhome.htm";
			}
		}

		return "admin/home";
	}
	
}
