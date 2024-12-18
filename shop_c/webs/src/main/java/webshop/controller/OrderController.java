package webshop.controller;

import java.io.IOException;
import java.net.Authenticator;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import webshop.dao.PaymentMethodDAO;
import webshop.dao.ProductDAO;
import webshop.dao.ProductDetailDAO;
import webshop.dao.ReasonDAO;
import webshop.entity.Account;
import webshop.entity.Cart;
import webshop.entity.Customer;
import webshop.entity.Order;
import webshop.entity.OrderDetail;
import webshop.entity.OrderStatus;
import webshop.entity.Product;
import webshop.entity.ProductDetail;
import webshop.entity.Reason;
import webshop.security.Authentication;
import webshop.security.Roles;

@Controller
@RequestMapping(value = "order")
public class OrderController {

	@Autowired
	AccountDAO accountDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	ProductDAO productDAO;
	@Autowired
	ProductDetailDAO productDetailDAO;
	@Autowired
	CartDAO cartDAO;
	@Autowired
	PaymentMethodDAO paymentMethodDAO;
	@Autowired
	OrderDAO orderDAO;
	@Autowired
	OrderDetailDAO orderDetailDAO;
	@Autowired
	OrderStatusDAO orderStatusDAO;
	@Autowired
	ReasonDAO reasonDAO;

	@RequestMapping("")
	public String order(HttpSession session, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) throws IOException {

		int auth = Authentication.redirectAuthen(request, response);
		if(auth != 3 ) return "redirect:home.htm";

		String email = (String) session.getAttribute("user");
		Account account = accountDAO.getAccountByEmail(email);
		Customer customer = null;
		List<Customer> dsCus = customerDAO.getAllCustomers();
		for(Customer c: dsCus) {
			if(c.getAccount().getId() == account.getId()) {
				customer = c;
				break;
			}
		}

		List<Order> orders = orderDAO.getOrdersByCustomerId(customer.getId());
		List<Map<String, Object>> ordersList = new ArrayList<Map<String, Object>>();

		if (orders != null) {
			for (Order order : orders) {
				Map<String, Object> ordersMap = new HashMap<String, Object>();
				int totalQuantity = 0;
				List<OrderDetail> orderDetails = orderDetailDAO.getOrderDetailsByOrderId(order.getId());

				for (OrderDetail orderDetail : orderDetails) {
					totalQuantity += orderDetail.getQuantity();
				}
				ordersMap.put("order", order);
				ordersMap.put("totalQuantity", totalQuantity);
				ordersList.add(ordersMap);
			}
		}

		Collections.reverse(ordersList);
		model.addAttribute("orders", ordersList);
		return "user/order/index";
	}

	@RequestMapping("orderdetail/{idOrder}")
	public String orderdetail(@PathVariable("idOrder") Integer idOrder, HttpSession session, 
			HttpServletRequest request, HttpServletResponse response, ModelMap model) throws IOException {

		
		int auth = Authentication.redirectAuthen(request, response);
		if(auth != 3 ) return "redirect:/home.htm";
		
		if (idOrder == null) {
			return "redirect:/order.htm";
		}

		String email = (String) session.getAttribute("user");
		Account account = accountDAO.getAccountByEmail(email);
		Customer customer = null;
		List<Customer> dsCus = customerDAO.getAllCustomers();
		for(Customer c: dsCus) {
			if(c.getAccount().getId() == account.getId()) {
				customer = c;
				break;
			}
		}


		Order order = orderDAO.getOrderById(idOrder);
		if (order == null) {
			return "redirect:/order.htm";
		}
		
		//Thêm ràng buộc đơn hàng phải của chủ nhân đang đăng nhập không?
		if(customer!=null) {
			if(order.getCustomer().getId() != customer.getId()) {
				return "redirect:/order.htm";
			}
		}
		

		List<OrderDetail> orderDetails = orderDetailDAO.getOrderDetailsByOrderId(idOrder);
		List<Map<String, Object>> orderDetailsList = new ArrayList<Map<String, Object>>();

		if (orderDetails != null) {
			for (OrderDetail orderDetail : orderDetails) {
				Map<String, Object> orderDetailsMap = new HashMap<String, Object>();
				ProductDetail productDetail = orderDetail.getProductDetail();
				Product product = productDetail.getProduct();

				orderDetailsMap.put("product", product);
				orderDetailsMap.put("productDetail", productDetail);
				orderDetailsMap.put("orderDetail", orderDetail);
				orderDetailsList.add(orderDetailsMap);
			}
		} else {
			return "redirect:/order.htm";
		}

		model.addAttribute("order", order);
		model.addAttribute("orderDetails", orderDetailsList);
		model.addAttribute("cancelReasons", reasonDAO.getAllReasons());

		return "user/order/detail";
	}

	@RequestMapping("cancel/{idOrder}")
	public String cancel(@PathVariable("idOrder") Integer idOrder, @RequestParam("IdReason") Integer IdReason,
			HttpSession session, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws IOException {

		int auth = Authentication.redirectAuthen(request, response);
		if(auth != 3 ) return "redirect:/home.htm";
		
		if (idOrder == null) {
			return "redirect:/order.htm";
		} else if (IdReason == null) {
			return "redirect:/order.htm";
		}

		String email = (String) session.getAttribute("user");
		Account account = accountDAO.getAccountByEmail(email);
		Customer customer = null;
		List<Customer> dsCus = customerDAO.getAllCustomers();
		for(Customer c: dsCus) {
			if(c.getAccount().getId() == account.getId()) {
				customer = c;
				break;
			}
		}

		Order order = orderDAO.getOrderById(idOrder);
		if (order == null || (order.getOrderStatus().getId() != 2 && order.getOrderStatus().getId() != 1)) {
			return "redirect:/order.htm";
		}
		//Thêm ràng buộc đơn hàng phải của chủ nhân đang đăng nhập không?
		if(customer!=null) {
			if(order.getCustomer().getId() != customer.getId()) {
				return "redirect:/order.htm";
			}
		}

		List<OrderDetail> orderDetails = orderDetailDAO.getOrderDetailsByOrderId(idOrder);
		List<Cart> carts = cartDAO.getCartsByCustomerId(customer.getId());

		if (orderDetails != null) {
			for (OrderDetail orderDetail : orderDetails) {
				ProductDetail productDetail = orderDetail.getProductDetail();
				Product product = productDetail.getProduct();

				Integer total = (int) (orderDetail.getQuantity() * orderDetail.getUnitPrice());
				Boolean flag = true;

				if (carts != null) {
					for (Cart cart : carts) {
						if (cart.getProductDetail() == productDetail) {
							cart.setQuantity(orderDetail.getQuantity());
							cart.setTotalPrice(total);

							cartDAO.updateCart(cart);
							flag = false;
							break;
						}
					}
				}
				if (flag) {
					Cart cart = new Cart(orderDetail.getQuantity(), 0, total, customer, productDetail);
					cartDAO.createCart(cart);
				}

				productDetail.setQuantity(productDetail.getQuantity() + orderDetail.getQuantity());
				productDetailDAO.updateProductDetail(productDetail);
			}
		} else {
			return "redirect:/order.htm";
		}

		Date currentDate = new Date();
		order.setOrderStatus(orderStatusDAO.getOrderStatusById(5));
		order.setUpdateTime(currentDate);
		order.setReason(reasonDAO.getReasonById(IdReason));
		orderDAO.updateOrder(order);
		
		

		return "redirect:/order/orderdetail/" + idOrder + ".htm";
	}
}
