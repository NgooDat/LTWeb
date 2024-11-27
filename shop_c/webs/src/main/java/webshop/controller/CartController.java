package webshop.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import webshop.dao.AccountDAO;
import webshop.dao.CartDAO;
import webshop.dao.CustomerDAO;
import webshop.dao.ProductDetailDAO;
import webshop.entity.Account;
import webshop.entity.Cart;
import webshop.entity.Customer;
import webshop.entity.ProductDetail;

@Controller
@RequestMapping(value = "cart")
public class CartController {
	@Autowired
	AccountDAO accountDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	ProductDetailDAO productDetailDAO;
	@Autowired
	CartDAO cartDAO;

	@ResponseBody // Để trả về dữ liệu trực tiếp (JSON hoặc string)
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String insert(@RequestParam("price") int price, @RequestParam("pdid") int pdid,
			@RequestParam("quantity") int quantity, HttpSession ses) {
		String email = (String) ses.getAttribute("user");
		Account account = accountDAO.getAccountByEmail(email);
		Customer customer = customerDAO.getCustomerById(account.getId());
		ProductDetail productDetail = productDetailDAO.getProductDetailById(pdid);
		List<Cart> listCarts = cartDAO.getCartsByCustomerId(customer.getId());

		for (Cart cart : listCarts) {
			if (cart.getProductDetail().getId() == pdid) {
				return "{\"message\": \"Sản phẩm đã có trong giỏ hàng!\", \"status\": \"false\"}";
			}
		}

		Cart cart = new Cart(quantity, 1, quantity * price, customer, productDetail);

		cartDAO.createCart(cart);
		return "{\"message\": \"Sản phẩm đã được thêm vào giỏ hàng!\", \"status\": \"true\"}";
	}

	@ResponseBody // Để trả về dữ liệu trực tiếp (JSON hoặc string)
	@RequestMapping(value = "/del", method = RequestMethod.GET)
	public String delete(@RequestParam("idCart") int idCart) {

		cartDAO.deleteCart(idCart);

		return "{\"message\": \"Xóa giỏ hàng thành công!\", \"status\": \"success\"}";
	}
	
	@ResponseBody // Để trả về dữ liệu trực tiếp (JSON hoặc string)
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public String update(@RequestParam("idCart") int idCart, @RequestParam("quantity") int quantity, @RequestParam("total") int total) {

		Cart cart = cartDAO.getCartById(idCart);
		cart.setQuantity(quantity);
		cart.setTotalPrice(total);
		cartDAO.updateCart(cart);

		return "{\"message\": \"Xóa giỏ hàng thành công!\", \"status\": \"success\"}";
	}
}