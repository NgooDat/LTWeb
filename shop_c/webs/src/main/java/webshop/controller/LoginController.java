package webshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import webshop.dao.AccountDAO;
import webshop.dao.CartDAO;
import webshop.dao.CustomerDAO;
import webshop.dao.RuleDAO;
import webshop.dao.UploadFile;
import webshop.entity.Account;
import webshop.entity.Cart;
import webshop.entity.Customer;
import webshop.entity.Rule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.IOException;
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
	@Autowired
    private UploadFile uploadFile;
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
	@RequestMapping(value = "register", method = RequestMethod.POST)
	public String register(
	        @RequestParam("email") String email,
	        @RequestParam("password") String password,
	        @RequestParam("confirmPassword") String confirmPassword,
	        Model model) {

	    // Kiểm tra xem mật khẩu nhập lại có khớp không
	    if (!password.equals(confirmPassword)) {
	        model.addAttribute("message", "Mật khẩu không khớp!");
	        return "login/register";
	    }

	    // Kiểm tra xem email đã tồn tại chưa
	    if (accountDAO.getAccountByEmail(email) != null) {
	        model.addAttribute("message", "Email đã tồn tại!");
	        return "login/register";
	    }
	    // Lấy quyền mặc định từ RuleDAO
	    Rule defaultRule = rule.getRuleById(2); // ID 2 là quyền mặc định
	    if (defaultRule == null) {
	        model.addAttribute("message", "Không thể tìm thấy quyền mặc định.");
	        return "login/register";
	    }
	    // Tạo tài khoản mới
	    Account account = new Account();
	    account.setEmail(email);
	    account.setPassword(password); // Bạn nên mã hóa mật khẩu trước khi lưu
	    account.setStatus(1); // Đặt trạng thái mặc định (hoạt động)
	    account.setRule(defaultRule); // Quyền mặc định

	    boolean success = accountDAO.saveAccount(account);
	    if (success) {
	        model.addAttribute("message", "Đăng ký thành công! Hãy đăng nhập.");
	        return "login/login";
	    } else {
	        model.addAttribute("message", "Có lỗi xảy ra. Vui lòng thử lại.");
	        return "login/register";
	    }
	}
	@RequestMapping(value = "register", method = RequestMethod.GET)
	public String showRegisterPage(Model model) {
	    System.out.println("Accessing /register"); // Log để kiểm tra
	    return "login/register"; // Trả về file JSP trong thư mục WEB-INF/views/
	}
	
	
	 @RequestMapping(value = "update-profile", method = RequestMethod.POST)
	    public String updateProfile(@RequestParam("name") String name,
	                                 @RequestParam("phone") String phone,
	                                 @RequestParam("email") String email,
	                                 @RequestParam(value = "file", required = false) MultipartFile file,
	                                 HttpSession session, Model model) {
		 // Kiểm tra định dạng email
		    String emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
		    if (!email.matches(emailPattern)) {
		        model.addAttribute("message", "Email không hợp lệ.");
		        return "user/personal";
		    }

		    // Kiểm tra định dạng số điện thoại (10 chữ số)
		    String phonePattern = "^\\d{10}$";
		    if (!phone.matches(phonePattern)) {
		        model.addAttribute("message", "Số điện thoại phải có 10 chữ số.");
		        return "user/personal";
		    }
	        // Lấy thông tin tài khoản từ session
	        String username = (String) session.getAttribute("user");
	        Account account = accountDAO.getAccountByEmail(username);
	        if (account == null) {
	            model.addAttribute("message", "Không tìm thấy tài khoản.");
	            return "login/login";
	        }

	        // Cập nhật thông tin cá nhân
	        Customer customer = customerDAO.getCustomerById(account.getId());
	        if (customer == null) {
	            model.addAttribute("message", "Không tìm thấy thông tin người dùng.");
	            return "login/login";
	        }

	        // Cập nhật các trường thông tin cá nhân
	        customer.setName(name);
	        customer.setPhone(phone);
	        
	        // Cập nhật email từ Account
	        account.setEmail(email);  // Cập nhật email của Account
	        accountDAO.updateAccount(account); // Lưu lại thông tin email của Account
	     // Kiểm tra nếu có ảnh mới
	        if (file != null && !file.isEmpty()) {
	            try {
	                // Xử lý tải lên ảnh
	                String fileName = uploadFile.handleFileUpload(file); // Gọi phương thức xử lý file
	                if (!fileName.equals("Vui lòng chọn file!" ) && !fileName.equals("Loại file không hợp lệ!")) {
	                    customer.setImage(fileName);  // Cập nhật ảnh vào trường image
	                } else {
	                    model.addAttribute("message", "Lỗi khi tải lên ảnh.");
	                    return "user/personal"; // Quay lại trang cập nhật thông tin cá nhân
	                }
	            } catch (IOException e) {
	                model.addAttribute("message", "Lỗi khi tải lên ảnh.");
	                return "user/personal"; // Quay lại trang cập nhật thông tin cá nhân
	            }
	        }

	     // Cập nhật lại thông tin trong cơ sở dữ liệu
	        if (customerDAO.updateCustomer(customer)) {
	            // Cập nhật lại thông tin session sau khi cập nhật
	            model.addAttribute("message", "Cập nhật thông tin thành công!");
	            session.setAttribute("user", username); // Đảm bảo session giữ tài khoản người dùng
	            model.addAttribute("personal", customer);  // Truyền lại thông tin cá nhân đã cập nhật
	            model.addAttribute("acc", account);  // Truyền lại thông tin tài khoản đã cập nhật
	            return "user/personal";  // Chuyển hướng về trang thông tin cá nhân
	        } else {
	            model.addAttribute("message", "Có lỗi xảy ra. Vui lòng thử lại.");
	            return "user/personal"; // Quay lại trang nếu có lỗi
	        }
	    }
	 @RequestMapping(value = "update-profile", method = RequestMethod.GET)
	 public String showUpdateProfilePage(HttpSession session, Model model) {
	     // Lấy thông tin tài khoản từ session
	     String username = (String) session.getAttribute("user");
	     Account account = accountDAO.getAccountByEmail(username);
	     if (account == null) {
	         model.addAttribute("message", "Không tìm thấy tài khoản.");
	         return "login/login";  // Chuyển hướng đến trang login nếu không tìm thấy tài khoản
	     }

	     // Lấy thông tin cá nhân
	     Customer customer = customerDAO.getCustomerById(account.getId());
	     if (customer == null) {
	         model.addAttribute("message", "Không tìm thấy thông tin người dùng.");
	         return "login/login";  // Chuyển hướng đến trang login nếu không tìm thấy thông tin cá nhân
	     }

	     // Đưa thông tin vào model để hiển thị trên form
	     model.addAttribute("personal", customer);  // Thông tin cá nhân của người dùng
	     model.addAttribute("acc", account);  // Thông tin tài khoản (email)
	     return "user/personal";  // Trả về trang personal.jsp để hiển thị form
	 }
	 
	@RequestMapping("logout")
	public String logout(HttpServletRequest request, HttpSession session) {
		if (session != null) {
			session.invalidate();
		}
		return "redirect:/login.htm";
	}
}