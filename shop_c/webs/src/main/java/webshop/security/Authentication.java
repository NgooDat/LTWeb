package webshop.security;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import webshop.dao.AccountDAO;
import webshop.dao.CustomerDAO;
import webshop.entity.Account;
import webshop.entity.Customer;

@Controller
public class Authentication {
	

	public static void adminAuthen(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	Cookie[] cookies = request.getCookies();
    	String jwtToken = "";
    	
   
        
        if (cookies != null) {
            // Duyệt qua từng cookie
            for (Cookie cookie : cookies) {
                // Kiểm tra xem cookie có tên "JWT" không
                if ("JWT".equals(cookie.getName())) {
                    jwtToken = (String)cookie.getValue();  // Trả về giá trị của cookie (token JWT)
                    break;
                }
            }
        }
                
        
        resetToken(jwtToken, request, response);
        
        String token = JwtUtil.extractJWT(jwtToken);
        if(!token.equals(Roles.getAdmin())) {
        	response.sendRedirect("home.htm"); 
        	return;
        }
        return ;

    }
	
	
	//Tạm thời dùng này vì khi code trong quá trình chạy sẽ bị mất cookie, nên phải reset
	public static void resetToken(String jwtToken, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
        if(!JwtUtil.validateToken(jwtToken)) {
        	deleteJWT(request, response);
        	response.sendRedirect("home.htm");
        	return;
        }

	}
	
	
	public static void resetTokenForHome(String jwtToken, HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(false);

        if(!jwtToken.equals("")) {
        	if(!JwtUtil.validateToken(jwtToken)) {
            	deleteJWT(request, response);
            	response.sendRedirect("home.htm");
            	return;
            }
        }
		
	}
	
	public static void deleteJWT(HttpServletRequest request, HttpServletResponse response) {
		
		HttpSession session = request.getSession(false);
		if(session!= null) {
			if(session.getAttribute("user")!=null ) {
				session.invalidate();
			}
		}
		
		 
		 Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            // 3. Tìm cookie có tên "JWT"
	            if ("JWT".equals(cookie.getName())) {
	                // 4. Cập nhật giá trị của cookie
	                cookie.setValue(""); // Hoặc có thể đặt giá trị mới nếu cần
	                cookie.setMaxAge(0); // Đặt maxAge là 0 để xóa cookie
	                cookie.setPath("/"); // Đảm bảo path khớp với khi tạo
	                cookie.setHttpOnly(true); // Đảm bảo HttpOnly khớp
	                cookie.setSecure(false); // Đặt false nếu không dùng HTTPS
	                response.addCookie(cookie); // Cập nhật cookie trong response
	                return;
	            }
	        }
	    }
	 }
	
	public static void addToken(String role, HttpServletResponse response) {
		//Lưu vào session bằng JWT
		String token = JwtUtil.generateToken(role);
		
		//Lưu jwttoken vào Cookies
		Cookie cookie = new Cookie("JWT", token);  // "JWT" là tên của cookie
	    cookie.setHttpOnly(true);  // Đảm bảo cookie không thể bị truy cập qua JavaScript
	    cookie.setSecure(false);  // Đảm bảo cookie chỉ được gửi qua HTTPS
	    //cookie.setDomain("abc.com");   
	    cookie.setPath("/");  // Đảm bảo cookie có thể truy cập trên toàn bộ ứng dụng
	    cookie.setMaxAge(-1);    // Thời gian sống của cookie khi tắt trình duyệt
	    response.addCookie(cookie);
	}
	
	public static void employAuthen(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	Cookie[] cookies = request.getCookies();
    	String jwtToken = "";
    	
        
        if (cookies != null) {
            // Duyệt qua từng cookie
            for (Cookie cookie : cookies) {
                // Kiểm tra xem cookie có tên "JWT" không
                if ("JWT".equals(cookie.getName())) {
                    jwtToken = (String)cookie.getValue();  // Trả về giá trị của cookie (token JWT)
                    break;
                }
            }
        }

        resetToken(jwtToken, request, response);
        
        
        String token = JwtUtil.extractJWT(jwtToken);
        if(!token.equals(Roles.getEmployee())) {
        	response.sendRedirect("home.htm"); 
        	return;
        }
        return ;
    }
	
	public static void userAuthen(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	Cookie[] cookies = request.getCookies();
    	String jwtToken = "";
    	
        
        if (cookies != null) {
            // Duyệt qua từng cookie
            for (Cookie cookie : cookies) {
                // Kiểm tra xem cookie có tên "JWT" không
                if ("JWT".equals(cookie.getName())) {
                    jwtToken = (String)cookie.getValue();  // Trả về giá trị của cookie (token JWT)
                    break;
                }
            }
        }
        
       
        resetToken(jwtToken, request, response);
        
        String token = JwtUtil.extractJWT(jwtToken);
        if(token.equals(Roles.getAdmin())) {
        	response.sendRedirect("adhome.htm"); 
        	return;
        }
        if(token.equals(Roles.getEmployee())) {
        	response.sendRedirect("emhome.htm"); 
        	return;
        }
        return;
    }
	
	
	public static boolean userAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	Cookie[] cookies = request.getCookies();
    	String jwtToken = "";
    	
        
        if (cookies != null) {
            // Duyệt qua từng cookie
            for (Cookie cookie : cookies) {
                // Kiểm tra xem cookie có tên "JWT" không
                if ("JWT".equals(cookie.getName())) {
                    jwtToken = (String)cookie.getValue();  // Trả về giá trị của cookie (token JWT)
                    break;
                }
            }
        }
        
       
        if(!JwtUtil.validateToken(jwtToken)) {
        	deleteJWT(request, response);
        	response.sendRedirect("home.htm");
        	return false;
        }
        
        String token = JwtUtil.extractJWT(jwtToken);
        if(token.equals(Roles.getAdmin())) {
        	return false;
        }
        if(token.equals(Roles.getEmployee())) {
        	return false;
        }
        return true;
    }
	public static void userHomeAuthen(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	Cookie[] cookies = request.getCookies();
    	String jwtToken = "";
    	
        
        
        if (cookies != null) {
            // Duyệt qua từng cookie
            for (Cookie cookie : cookies) {
                // Kiểm tra xem cookie có tên "JWT" không
                if ("JWT".equals(cookie.getName())) {
                    jwtToken = (String)cookie.getValue();  // Trả về giá trị của cookie (token JWT)
                    break;
                }
            }
        }
        
        resetTokenForHome(jwtToken, request, response);
        
        
        String token = JwtUtil.extractJWT(jwtToken);
        if(token.equals(Roles.getAdmin())) {
        	response.sendRedirect("adhome.htm"); 
        	return;
        }
        if(token.equals(Roles.getEmployee())) {
        	response.sendRedirect("emhome.htm"); 
        	return;
        }
        return ;
    }
	
	
	
	public static boolean isLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	Cookie[] cookies = request.getCookies();
    	String jwtToken = "";
    	
        
        if (cookies != null) {
            // Duyệt qua từng cookie
            for (Cookie cookie : cookies) {
                // Kiểm tra xem cookie có tên "JWT" không
                if ("JWT".equals(cookie.getName())) {
                    jwtToken = (String)cookie.getValue();  // Trả về giá trị của cookie (token JWT)
                    break;
                }
            }
        }
        
        System.out.println(jwtToken);
        
        if(!JwtUtil.validateToken(jwtToken)) {
        	
        	return false;
        }
        return true;
	
	}
	
	public static void loggedIn(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if(isLogin(request, response)) {
			response.sendRedirect("home.htm");
			return;
		}
	}
	
	public static void loggedOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if(!isLogin(request, response)) {
			response.sendRedirect("login.htm");
			return;
		}
	}
}
