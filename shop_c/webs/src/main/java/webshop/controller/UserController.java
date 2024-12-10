package webshop.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import webshop.dao.AccountDAO;
import webshop.dao.CartDAO;
import webshop.dao.CustomerDAO;
import webshop.dao.OrderDAO;
import webshop.dao.OrderDetailDAO;
import webshop.dao.OrderStatusDAO;
import webshop.dao.ProductDAO;
import webshop.dao.ProductDetailDAO;
import webshop.entity.Account;
import webshop.entity.Cart;
import webshop.entity.Customer;
import webshop.entity.Order;
import webshop.entity.OrderDetail;
import webshop.entity.Product;
import webshop.entity.ProductDetail;

@Controller
public class UserController {

	@Autowired
	AccountDAO accd;
	@Autowired
	CustomerDAO cusd;
	@Autowired
	ProductDAO product;
	@Autowired
	ProductDetailDAO prdd;
	@Autowired
	CartDAO cartd;
	@Autowired
	OrderDAO ordd;
	@Autowired
	OrderDetailDAO ordetail;
	@Autowired
	OrderStatusDAO osd;

	@RequestMapping("home")
	public String home(ModelMap model) {

		List<Product> dsProduct = product.getAllProducts();

		List<ProductDetail> dsDetail = prdd.getAllProductDetails();

		List<Object[]> productInfoList = new ArrayList<Object[]>();

		for (Product p : dsProduct) {
			Double minPrice = dsDetail.stream().filter(detail -> detail.getProduct().getId() == p.getId())
					.mapToDouble(ProductDetail::getPrice).min() // Lấy giá nhỏ nhất
					.orElse(0); // Nếu không tìm thấy, trả về NaN

			productInfoList.add(new Object[] { p.getId(), p.getName(), minPrice, p.getImage() });
		}

		model.addAttribute("productInfo", productInfoList);

		return "user/home";
	}

	@RequestMapping("search")
	public String search(@RequestParam(value = "search", required = false) String search, ModelMap model) {

		List<Product> dsProduct = product.getAllProducts(); // Lấy tất cả sản phẩm
		List<ProductDetail> dsDetail = prdd.getAllProductDetails(); // Lấy chi tiết sản phẩm

		// Lọc sản phẩm dựa trên chuỗi search nếu được cung cấp
		if (search != null && !search.isEmpty()) {
			dsProduct = dsProduct.stream().filter(p -> p.getName().toLowerCase().contains(search.toLowerCase()))
					.collect(Collectors.toList());
		}

		List<Object[]> productInfoList = new ArrayList<>();

		for (Product p : dsProduct) {
			// Tính giá thấp nhất cho sản phẩm
			Double minPrice = dsDetail.stream().filter(detail -> detail.getProduct().getId() == p.getId())
					.mapToDouble(ProductDetail::getPrice).min().orElse(0.0); // Giá mặc định nếu không tìm thấy

			// Thêm thông tin vào danh sách
			productInfoList.add(new Object[] { p.getId(), p.getName(), minPrice, p.getImage() });
		}

		// Gắn danh sách vào Model
		model.addAttribute("productInfo", productInfoList);

		return "user/home";
	}

	@RequestMapping("login")
	public String login(HttpSession ses) {

		if (ses.getAttribute("user") != null) {
			return "redirect:/home.htm";
		}
		return "login/login";
	}

	@RequestMapping("error")
	public String error() {
		return "error";
	}

	@RequestMapping("productinfo")
	public String productinfo(HttpServletRequest request) {

		if (request.getParameter("proid") == null) {
			return "redirect:/home.htm";
		}
		int proid = Integer.parseInt(request.getParameter("proid"));

		Product prod = product.getProductById(proid);

		List<ProductDetail> dsDe = prdd.getAllProductDetails();

		List<ProductDetail> toRemove = new ArrayList<>();
		for (ProductDetail d : dsDe) {
			if (d.getProduct().getId() == proid) {
				toRemove.add(d);
			}
		}
		request.setAttribute("productDetail", toRemove);
		request.setAttribute("product", prod);

		return "user/product";
	}

	@RequestMapping("personal")
	public String personal(HttpServletRequest request, HttpSession session) {

		if (session.getAttribute("user") == null) {
			return "redirect:/login.htm";
		}

		String email = (String) session.getAttribute("user");

		Account acc = accd.getAccountByEmail(email);

		Customer cs = null;

		if (acc != null) {
			cs = cusd.getCustomerById(acc.getId());
		}

		if (cs != null) {
			request.setAttribute("acc", acc);
			request.setAttribute("personal", cs);
		}

		return "user/personal";
	}

	@RequestMapping("resetpass")
	public String resetpass(HttpServletRequest request, HttpSession ses) {
		if (ses.getAttribute("user") == null) {
			return "redirect:/login.htm";
		}

		// tiếp tục đổi mk gì đó
		return "login/resetpass";
	}

}
