package webshop.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.net.URLEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.JsonObject;

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
import webshop.entity.Cart;
import webshop.entity.Customer;
import webshop.entity.Order;
import webshop.entity.OrderDetail;
import webshop.entity.OrderDetailId;
import webshop.entity.OrderStatus;
import webshop.entity.Product;
import webshop.entity.ProductDetail;
import webshop.paymentMethod.Config;
import webshop.paymentMethod.PaymentService;

@Controller
@RequestMapping(value = "payment")
public class PaymentController {
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

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String perform(HttpSession session, ModelMap model) {
		String email = (String) session.getAttribute("user");
		if (email == null) {
			return "redirect:/login.htm";
		}
		Account account = accountDAO.getAccountByEmail(email);
		Customer customer = customerDAO.getCustomerById(account.getId());

		// Lấy danh sách idCart từ session
		@SuppressWarnings("unchecked")
		List<Integer> selectedCartIds = (List<Integer>) session.getAttribute("selectedCartIds");
		if (selectedCartIds == null) {
			return "redirect:/home.htm";
		}
		List<Map<String, Object>> selectProducts = new ArrayList<>();
		List<Product> dsProduct = productDAO.getAllProducts();
		List<ProductDetail> dsDetail = productDetailDAO.getAllProductDetails();
		int givenCustomerId = customer.getId();

		for (int idCart : selectedCartIds) {
			Cart cart = cartDAO.getCartById(idCart); // Lấy Cart từ DAO
			if (cart.getCustomer().getId() == givenCustomerId) {
				// Tìm ProductDetail tương ứng với product_detailsID trong Cart
				ProductDetail productDetail = dsDetail.stream()
						.filter(detail -> detail.getId() == cart.getProductDetail().getId()) // so sánh ID
						.findFirst().orElse(null);

				if (productDetail != null) {
					// Tìm Product tương ứng với productID trong ProductDetail
					Product product = dsProduct.stream().filter(p -> p.getId() == productDetail.getProduct().getId())
							.findFirst().orElse(null);

					if (product != null) {
						// Tính tổng tiền
						int quantity = cart.getQuantity();
						int price = productDetail.getPrice();
						int total = price * quantity;

						// Lưu thông tin vào danh sách
						Map<String, Object> selectProduct = new HashMap<>();
						selectProduct.put("cartid", cart.getID());
						selectProduct.put("status", cart.getStatus());
						selectProduct.put("image", product.getImage());
						selectProduct.put("name", product.getName());
						selectProduct.put("size", productDetail.getSize().getId());
						selectProduct.put("price", price);
						selectProduct.put("quantity", quantity);
						selectProduct.put("maxQuantity", productDetail.getQuantity());
						selectProduct.put("total", total);

						selectProducts.add(selectProduct);
					}
				}
			}
		}
		model.addAttribute("customer", customer);
		model.addAttribute("paymentMethods", paymentMethodDAO.getAllPaymentMethods());
		model.addAttribute("selectProducts", selectProducts);
		model.addAttribute("idOrder", 0);
		return "payment/index";
	}

	@RequestMapping(value = "/repurchase/{idOrder}", method = RequestMethod.GET)
	public String repurchase(@PathVariable("idOrder") Integer idOrder, HttpSession session, ModelMap model) {
		String email = (String) session.getAttribute("user");
		if (email == null) {
			return "redirect:/login.htm";
		}
		if (idOrder == null) {
			return "redirect:/home.htm";
		}
		Order order = orderDAO.getOrderById(idOrder);
		List<OrderDetail> orderDetails = orderDetailDAO.getOrderDetailsByOrderId(idOrder);

		if (order == null || (order.getOrderStatus().getId() != 4 && order.getOrderStatus().getId() != 5)) {
			return "redirect:/home.htm";
		}

		Account account = accountDAO.getAccountByEmail(email);
		Customer customer = customerDAO.getCustomerById(account.getId());

		List<Map<String, Object>> selectProducts = new ArrayList<>();
		List<Product> dsProduct = productDAO.getAllProducts();
		List<ProductDetail> dsDetail = productDetailDAO.getAllProductDetails();

		for (OrderDetail orderDetail : orderDetails) {
			// Tìm ProductDetail tương ứng với product_detailsID trong orderDetail
			ProductDetail productDetail = dsDetail.stream()
					.filter(detail -> detail.getId() == orderDetail.getProductDetail().getId()).findFirst()
					.orElse(null);

			if (productDetail != null) {
				// Tìm Product tương ứng với productID trong ProductDetail
				Product product = dsProduct.stream().filter(p -> p.getId() == productDetail.getProduct().getId())
						.findFirst().orElse(null);

				if (product != null) {
					// Tính tổng tiền
					int quantity = orderDetail.getQuantity();
					int price = productDetail.getPrice();
					int total = price * quantity;

					// Lưu thông tin vào danh sách
					Map<String, Object> selectProduct = new HashMap<>();
					selectProduct.put("cartid", 0);
					selectProduct.put("image", product.getImage());
					selectProduct.put("name", product.getName());
					selectProduct.put("size", productDetail.getSize().getId());
					selectProduct.put("productDetailId", productDetail.getId());
					selectProduct.put("price", price);
					selectProduct.put("quantity", quantity);
					selectProduct.put("maxQuantity", productDetail.getQuantity());
					selectProduct.put("total", total);

					selectProducts.add(selectProduct);
				}
			}
		}
		model.addAttribute("customer", customer);
		model.addAttribute("idOrder", idOrder);
		model.addAttribute("paymentMethods", paymentMethodDAO.getAllPaymentMethods());
		model.addAttribute("selectProducts", selectProducts);
		return "payment/index";
	}

	@RequestMapping(value = "/result", method = RequestMethod.POST)
	public String cash(@RequestParam("phone") String phone, @RequestParam("name") String name,
			@RequestParam("address") String address, @RequestParam("note") String note,
			@RequestParam("paymentMethod") int paymentMethod, Model model, HttpSession session) {
		String email = (String) session.getAttribute("user");
		Account account = accountDAO.getAccountByEmail(email);
		Customer customer = customerDAO.getCustomerById(account.getId());

		List<Integer> selectedCartIds = (List<Integer>) session.getAttribute("selectedCartIds");

		if (selectedCartIds.isEmpty()) {
			model.addAttribute("message", "Đặt hàng không thành công!!!");
			return "payment/success";
		}

		Date currentDate = new Date();

		Set<OrderDetail> orderDetails = new HashSet<>();
		double totalProductFee = 0.0;

		for (int idCart : selectedCartIds) {

			Cart cart = cartDAO.getCartById(idCart);
			if (cart == null || cart.getProductDetail() == null) {
				model.addAttribute("message", "Đặt hàng không thành công!!!");
				return "payment/success";
			}

			OrderDetail orderDetail = new OrderDetail();
			ProductDetail productDetail = cart.getProductDetail();

			OrderDetailId orderDetailId = new OrderDetailId();
			orderDetailId.setOrdersID(0);
			orderDetailId.setProduct_detailsID(productDetail.getId());
			orderDetail.setId(orderDetailId);

			orderDetail.setQuantity(cart.getQuantity());
			if (productDetail.getQuantity() < cart.getQuantity()) {
				model.addAttribute("message", "Xin lỗi số lượng hàng không đủ!!!");
				return "payment/success";
			}
			productDetail.setQuantity(productDetail.getQuantity() - cart.getQuantity());
			productDetailDAO.updateProductDetail(productDetail);
			orderDetail.setUnitPrice(cart.getTotalPrice() / cart.getQuantity());
			orderDetail.setProductDetail(productDetail);

			cart.setStatus(1);
			cartDAO.updateCart(cart);

			orderDetails.add(orderDetail);
			totalProductFee += cart.getTotalPrice();
		}

		// Create order
		Order order = new Order();
		order.setAddress(address);
		order.setCreateTime(currentDate);
		order.setUpdateTime(currentDate);
		order.setPaymentStatus(0);
		order.setCustomer(customer);
		order.setDescription(note);
		order.setOrderStatus(orderStatusDAO.getOrderStatusById(2));
		order.setPaymentMethod(paymentMethodDAO.getPaymentMethodById(paymentMethod));
		order.setProductFee(totalProductFee);
		order.setShipFee(16000);
		order.setTotal(totalProductFee + 16000);

		orderDAO.addOrder(order);

		for (OrderDetail orderDetail : orderDetails) {
			orderDetail.getId().setOrdersID(order.getId());
			orderDetailDAO.addOrderDetail(orderDetail);
		}

		order.setOrderDetails(orderDetails);
		orderDAO.updateOrder(order);

		customer.setName(name);
		customer.setPhone(phone);
		customerDAO.updateCustomer(customer);

		session.removeAttribute("selectedCartIds");

		model.addAttribute("message", "Đặt hàng thành công!!!");
		model.addAttribute("newOrderId", order.getId());

		return "payment/success";
	}

	@RequestMapping(value = "/repurchase/{idOrder}", method = RequestMethod.POST)
	public String cash(@PathVariable("idOrder") Integer idOrder, @RequestParam("phone") String phone,
			@RequestParam("name") String name, @RequestParam("address") String address,
			@RequestParam("note") String note, @RequestParam("paymentMethod") int paymentMethod, Model model,
			HttpSession session) {
		String email = (String) session.getAttribute("user");
		if (email == null) {
			return "redirect:/login.htm";
		}
		if (idOrder == null) {
			return "redirect:/home.htm";
		}
		Order order = orderDAO.getOrderById(idOrder);
		List<OrderDetail> orderDetails = orderDetailDAO.getOrderDetailsByOrderId(idOrder);

		if (order == null || (order.getOrderStatus().getId() != 4 && order.getOrderStatus().getId() != 5)) {
			return "redirect:/home.htm";
		}

		Account account = accountDAO.getAccountByEmail(email);
		Customer customer = customerDAO.getCustomerById(account.getId());

		Date currentDate = new Date();

		Set<OrderDetail> setOrderDetails = new HashSet<>();
		double totalProductFee = 0.0;
		for (OrderDetail orderDetail : orderDetails) {
			ProductDetail productDetail = orderDetail.getProductDetail();

			OrderDetailId orderDetailId = new OrderDetailId();
			orderDetailId.setOrdersID(0);
			orderDetailId.setProduct_detailsID(productDetail.getId());
			orderDetail.setId(orderDetailId);

			orderDetail.setQuantity(orderDetail.getQuantity());
			if (productDetail.getQuantity() < orderDetail.getQuantity()) {
				model.addAttribute("message", "Xin lỗi số lượng hàng không đủ!!!");
				return "payment/success";
			}
			productDetail.setQuantity(productDetail.getQuantity() - orderDetail.getQuantity());
			productDetailDAO.updateProductDetail(productDetail);

			setOrderDetails.add(orderDetail);
		}
		order.setAddress(address);
		order.setCreateTime(currentDate);
		order.setUpdateTime(currentDate);
		order.setPaymentStatus(0);
		order.setCustomer(customer);
		order.setDescription(note);
		order.setOrderStatus(orderStatusDAO.getOrderStatusById(2));
		order.setPaymentMethod(paymentMethodDAO.getPaymentMethodById(paymentMethod));
		order.setProductFee(totalProductFee);
		order.setShipFee(16000);
		order.setTotal(totalProductFee + 16000);

		orderDAO.addOrder(order);

		for (OrderDetail detail : orderDetails) {
			detail.getId().setOrdersID(order.getId());
			orderDetailDAO.addOrderDetail(detail);
		}

		order.setOrderDetails(setOrderDetails);
		orderDAO.updateOrder(order);

		customer.setName(name);
		customer.setPhone(phone);
		customerDAO.updateCustomer(customer);

		model.addAttribute("message", "Đặt hàng thành công!!!");
		model.addAttribute("newOrderId", order.getId());

		return "payment/success";
	}

	@Autowired
	private PaymentService paymentService;
	@Autowired
	private Config config;

	@RequestMapping(value = "/vnpay", method = RequestMethod.GET)
	public String vnpay(@RequestParam("phone") String phone, @RequestParam("name") String name,
			@RequestParam("totalAmount") long totalAmount, @RequestParam("address") String address,
			@RequestParam("note") String note, @RequestParam("paymentMethod") int paymentMethod, HttpSession session,
			HttpServletRequest request, Model model) {
		String language = "vn";

		try {
			// Gọi service tạo URL thanh toán
			JsonObject paymentResponse = paymentService.createPayment(request, totalAmount, "", language);

			// Kiểm tra kết quả trả về
			if (paymentResponse != null && "00".equals(paymentResponse.get("code").getAsString())) {

				String email = (String) session.getAttribute("user");
				Account account = accountDAO.getAccountByEmail(email);
				Customer customer = customerDAO.getCustomerById(account.getId());

				Date currentDate = new Date();

				List<Integer> selectedCartIds = (List<Integer>) session.getAttribute("selectedCartIds");

				if (selectedCartIds.isEmpty()) {
					throw new IllegalArgumentException("No cart items selected");
				}

				Set<OrderDetail> orderDetails = new HashSet<>();

				for (int idCart : selectedCartIds) {

					Cart cart = cartDAO.getCartById(idCart);
					if (cart == null || cart.getProductDetail() == null) {
						throw new IllegalStateException("Invalid cart or product detail");
					}

					OrderDetail orderDetail = new OrderDetail();
					ProductDetail productDetail = cart.getProductDetail();

					OrderDetailId orderDetailId = new OrderDetailId();
					orderDetailId.setOrdersID(0);
					orderDetailId.setProduct_detailsID(productDetail.getId());
					orderDetail.setId(orderDetailId);

					orderDetail.setQuantity(cart.getQuantity());
					if (productDetail.getQuantity() < cart.getQuantity()) {
						model.addAttribute("message", "Xin lỗi số lượng hàng không đủ!!!");
						return "payment/success";
					}
					productDetail.setQuantity(productDetail.getQuantity() - cart.getQuantity());
					productDetailDAO.updateProductDetail(productDetail);
					orderDetail.setUnitPrice(cart.getTotalPrice() / cart.getQuantity());
					orderDetail.setProductDetail(productDetail);

					cart.setStatus(1);
					cartDAO.updateCart(cart);

					orderDetails.add(orderDetail);
				}

				Order order = new Order();
				order.setAddress(address);
				order.setCreateTime(currentDate);
				order.setUpdateTime(currentDate);
				order.setPaymentStatus(0);
				order.setCustomer(customer);
				order.setDescription(note);
				order.setOrderStatus(orderStatusDAO.getOrderStatusById(1));
				order.setPaymentMethod(paymentMethodDAO.getPaymentMethodById(paymentMethod));
				order.setProductFee(totalAmount);
				order.setShipFee(16000);
				order.setTotal(totalAmount + 16000);

				orderDAO.addOrder(order);

				for (OrderDetail detail : orderDetails) {
					detail.getId().setOrdersID(order.getId());
					orderDetailDAO.addOrderDetail(detail);
				}

				order.setOrderDetails(orderDetails);
				orderDAO.updateOrder(order);

				customer.setName(name);
				customer.setPhone(phone);
				customerDAO.updateCustomer(customer);

				session.removeAttribute("selectedCartIds");

				session.setAttribute("newOrderId", order.getId());

				// Lấy URL thanh toán
				String paymentUrl = paymentResponse.get("data").getAsString();
				// Chuyển hướng người dùng tới trang thanh toán
				return "redirect:" + paymentUrl;

			} else {
				// Xử lý lỗi nếu có
				model.addAttribute("error", "Không thể tạo thanh toán. Vui lòng thử lại!");
				return "error"; // Trang hiển thị lỗi
			}
		} catch (Exception e) {
			// Log lỗi và hiển thị thông báo lỗi
			e.printStackTrace();
			model.addAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
			return "error"; // Trang hiển thị lỗi
		}
	}

	@RequestMapping(value = "/repurchase/vnpay/{idOrder}", method = RequestMethod.GET)
	public String vnpay(@PathVariable("idOrder") Integer idOrder, @RequestParam("phone") String phone,
			@RequestParam("name") String name, @RequestParam("totalAmount") long totalAmount,
			@RequestParam("address") String address, @RequestParam("note") String note,
			@RequestParam("paymentMethod") int paymentMethod, HttpSession session, HttpServletRequest request,
			Model model) {
		String language = "vn";
		String email = (String) session.getAttribute("user");
		if (email == null) {
			return "redirect:/login.htm";
		}
		if (idOrder == null) {
			return "redirect:/home.htm";
		}
		Order order = orderDAO.getOrderById(idOrder);
		List<OrderDetail> orderDetails = orderDetailDAO.getOrderDetailsByOrderId(idOrder);

		if (order == null || (order.getOrderStatus().getId() != 4 && order.getOrderStatus().getId() != 5)) {
			return "redirect:/home.htm";
		}

		Account account = accountDAO.getAccountByEmail(email);
		Customer customer = customerDAO.getCustomerById(account.getId());

		try {
			// Gọi service tạo URL thanh toán
			JsonObject paymentResponse = paymentService.createPayment(request, totalAmount, "", language);

			// Kiểm tra kết quả trả về
			if (paymentResponse != null && "00".equals(paymentResponse.get("code").getAsString())) {

				Date currentDate = new Date();

				Set<OrderDetail> setOrderDetails = new HashSet<>();

				for (OrderDetail orderDetail : orderDetails) {
					ProductDetail productDetail = orderDetail.getProductDetail();

					OrderDetailId orderDetailId = new OrderDetailId();

					if (productDetail.getQuantity() < orderDetail.getQuantity()) {
						model.addAttribute("message", "Xin lỗi số lượng hàng không đủ!!!");
						return "payment/success";
					}
					productDetail.setQuantity(productDetail.getQuantity() - orderDetail.getQuantity());
					productDetailDAO.updateProductDetail(productDetail);

					setOrderDetails.add(orderDetail);
				}

				order.setAddress(address);
				order.setCreateTime(currentDate);
				order.setUpdateTime(currentDate);
				order.setPaymentStatus(0);
				order.setCustomer(customer);
				order.setDescription(note);
				order.setOrderStatus(orderStatusDAO.getOrderStatusById(1));
				order.setPaymentMethod(paymentMethodDAO.getPaymentMethodById(paymentMethod));
				order.setProductFee(totalAmount);
				order.setShipFee(16000);
				order.setTotal(totalAmount + 16000);

				orderDAO.addOrder(order);

				for (OrderDetail orderDetail : orderDetails) {
					orderDetail.getId().setOrdersID(order.getId());
					orderDetailDAO.addOrderDetail(orderDetail);
				}

				order.setOrderDetails(setOrderDetails);
				orderDAO.updateOrder(order);

				customer.setName(name);
				customer.setPhone(phone);
				customerDAO.updateCustomer(customer);

				session.setAttribute("newOrderId", order.getId());

				// Lấy URL thanh toán
				String paymentUrl = paymentResponse.get("data").getAsString();
				// Chuyển hướng người dùng tới trang thanh toán
				return "redirect:" + paymentUrl;

			} else {
				// Xử lý lỗi nếu có
				model.addAttribute("error", "Không thể tạo thanh toán. Vui lòng thử lại!");
				return "error"; // Trang hiển thị lỗi
			}
		} catch (Exception e) {
			// Log lỗi và hiển thị thông báo lỗi
			e.printStackTrace();
			model.addAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
			return "error"; // Trang hiển thị lỗi
		}
	}

	@RequestMapping(value = "/{idOrder}", method = RequestMethod.GET)
	public String repayment(@PathVariable("idOrder") Integer idOrder, HttpSession session, HttpServletRequest request,
			Model model) {
		String language = "vn";
		String email = (String) session.getAttribute("user");
		if (email == null) {
			return "redirect:/login.htm";
		}
		if (idOrder == null) {
			return "redirect:/home.htm";
		}
		Order order = orderDAO.getOrderById(idOrder);
		if (order == null || order.getOrderStatus().getId() != 1) {
			return "redirect:/home.htm";
		}
		long totalAmount = (long) order.getTotal();

		try {
			// Gọi service tạo URL thanh toán
			JsonObject paymentResponse = paymentService.createPayment(request, totalAmount, "", language);

			// Kiểm tra kết quả trả về
			if (paymentResponse != null && "00".equals(paymentResponse.get("code").getAsString())) {

				session.setAttribute("newOrderId", idOrder);
				// Lấy URL thanh toán
				String paymentUrl = paymentResponse.get("data").getAsString();
				// Chuyển hướng người dùng tới trang thanh toán
				return "redirect:" + paymentUrl;

			} else {
				// Xử lý lỗi nếu có
				model.addAttribute("error", "Không thể tạo thanh toán. Vui lòng thử lại!");
				return "error"; // Trang hiển thị lỗi
			}
		} catch (Exception e) {
			// Log lỗi và hiển thị thông báo lỗi
			e.printStackTrace();
			model.addAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
			return "error"; // Trang hiển thị lỗi
		}
	}

	@RequestMapping(value = "/result", method = RequestMethod.GET)
	public String vnpay(@RequestParam("vnp_Amount") double vnp_Amount,
			@RequestParam("vnp_TransactionStatus") String vnp_TransactionStatus, HttpServletRequest request,
			Model model, HttpSession session) {
		int newOrderId = (int) session.getAttribute("newOrderId");
		// Khởi tạo Map để lưu các tham số
		Map<String, String> fields = new HashMap<>();

		try {
			// Lấy tất cả các tham số từ request và mã hóa chúng
			for (Enumeration<?> params = request.getParameterNames(); params.hasMoreElements();) {
				String fieldName = URLEncoder.encode((String) params.nextElement(),
						StandardCharsets.US_ASCII.toString());
				String fieldValue = URLEncoder.encode(request.getParameter(fieldName),
						StandardCharsets.US_ASCII.toString());
				if ((fieldValue != null) && (fieldValue.length() > 0)) {
					fields.put(fieldName, fieldValue);
				}
			}
		} catch (UnsupportedEncodingException e) {
			// Xử lý ngoại lệ nếu có
			model.addAttribute("message", "Lỗi mã hóa: " + e.getMessage());
			return "payment/result"; // Trả về trang lỗi nếu có lỗi mã hóa
		}

		// Lấy giá trị vnp_SecureHash từ request
		String vnp_SecureHash = request.getParameter("vnp_SecureHash");

		// Xóa các tham số không cần thiết khỏi Map
		fields.remove("vnp_SecureHashType");
		fields.remove("vnp_SecureHash");

		// Tạo chữ ký từ các tham số
		String signValue = Config.hashAllFields(fields);

		// So sánh chữ ký đã tạo với vnp_SecureHash nhận được
		if (signValue.equals(vnp_SecureHash)) {
			// Kiểm tra trạng thái giao dịch
			if ("00".equals(vnp_TransactionStatus)) {
				Date currentDate = new Date();
				Order order = orderDAO.getOrderById(newOrderId);
				order.setOrderStatus(orderStatusDAO.getOrderStatusById(2));
				order.setUpdateTime(currentDate);
				orderDAO.updateOrder(order);

			} else {
				// Giao dịch không thành công
				model.addAttribute("message", "Thanh toán không thành công!");
			}
		} else {
			// Nếu chữ ký không khớp
			model.addAttribute("message", "Lỗi xác thực chữ ký!");
		}

		model.addAttribute("newOrderId", newOrderId);
		session.removeAttribute("newOrderId");

		return "payment/result"; // Trả về trang kết quả
	}

}