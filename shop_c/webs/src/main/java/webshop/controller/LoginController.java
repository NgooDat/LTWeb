package webshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import webshop.dao.AccountDAO;
import webshop.dao.CartDAO;
import webshop.dao.CustomerDAO;
import webshop.dao.RuleDAO;
import webshop.entity.Account;
import webshop.entity.Cart;
import webshop.entity.Customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;

@Controller
public class LoginController {
	@Autowired
	private AccountDAO accountDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	CartDAO cartDAO;
	@Autowired
	private RuleDAO rule;

	@RequestMapping(value = "/login-auth", method = RequestMethod.POST)
	public String login(@RequestParam("username") String username, @RequestParam("password") String password,
			HttpServletRequest request, @RequestParam(value = "remember", required = false) String remember,
			HttpSession session, Model model) {

		Optional<Account> account = accountDAO.findAccountByUsernameAndPassword(username, password);
		session = request.getSession(false);
		if (account.isPresent()) {

			Account acc = accountDAO.getAccountByEmail(username);
			Customer customer = customerDAO.getCustomerById(acc.getId());
			int rules = acc.getRule().getId();
			// phân quyền các kiểu
			// sau này có Mã hóa sau session + pass
			session.setAttribute("user", username);
			List<Cart> carts = (List<Cart>) session.getAttribute("carts");
			List<Cart> customerCarts = cartDAO.getCartsByCustomerId(customer.getId());

			if (carts != null) {
				for (Cart cart : carts) {
					boolean flag = false;
					if (customerCarts != null) {
						for (Cart customerCart : customerCarts) {
							if (customerCart.getProductDetail().getId() == cart.getProductDetail().getId()
									&& customerCart.getStatus() == cart.getStatus()) {
								customerCart.setQuantity(customerCart.getQuantity() + cart.getQuantity());
								cartDAO.updateCart(customerCart);
								flag = true;
								break;
							}
						}
					}
					if (!flag) {
						cart.setCustomer(customer);
						cartDAO.createCart(cart);
					}
				}
			}

			return "redirect:/home.htm";
		} else {

			model.addAttribute("message", "Tên đăng nhập hoặc mật khẩu không đúng!");
			return "login/login";
		}
	}

	@RequestMapping("logout")
	public String logout(HttpServletRequest request, HttpSession session) {
		if (session != null) {
			session.invalidate();
		}
		return "redirect:/login.htm";
	}
}