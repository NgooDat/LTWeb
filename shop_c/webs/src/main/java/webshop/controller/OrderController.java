package webshop.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import webshop.dao.AccountDAO;
import webshop.dao.CartDAO;
import webshop.dao.CustomerDAO;
import webshop.dao.OrderDAO;
import webshop.dao.OrderDetailDAO;
import webshop.dao.OrderStatusDAO;
import webshop.dao.PaymentMethodDAO;
import webshop.dao.ProductDAO;
import webshop.dao.ProductDetailDAO;
import webshop.entity.Account;
import webshop.entity.Customer;
import webshop.entity.Order;
import webshop.entity.OrderDetail;
import webshop.entity.Product;
import webshop.entity.ProductDetail;

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

	@RequestMapping("")
	public String order(HttpSession session, ModelMap model) {

		if (session.getAttribute("user") == null) {
			return "redirect:/login.htm";
		}

		String email = (String) session.getAttribute("user");
		Account account = accountDAO.getAccountByEmail(email);
		Customer customer = customerDAO.getCustomerById(account.getId());

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
	public String orderdetail(@PathVariable("idOrder") Integer idOrder, HttpSession session, ModelMap model) {

		if (session.getAttribute("user") == null) {
			return "redirect:/login.htm";
		}else if(idOrder == null) {
			return "redirect:/order.htm";
		}

		String email = (String) session.getAttribute("user");
		Account account = accountDAO.getAccountByEmail(email);
		Customer customer = customerDAO.getCustomerById(account.getId());

		Order order = orderDAO.getOrderById(idOrder);
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
		}else {
			return "redirect:/order.htm";
		}

		model.addAttribute("order", order);
		model.addAttribute("orderDetails", orderDetailsList);
		return "user/order/detail";
	}
}
