package webshop.controller;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import webshop.entity.Product;
import webshop.entity.ProductDetail;
import webshop.dao.AccountDAO;
import webshop.dao.CartDAO;
import webshop.dao.CustomerDAO;
import webshop.dao.OrderDAO;
import webshop.dao.OrderDetailDAO;
import webshop.dao.OrderStatusDAO;
import webshop.dao.ProductDAO;
import webshop.dao.ProductDetailDAO;
import webshop.dao.RuleDAO;
import webshop.dao.BrandDAO;
import webshop.dao.TypeDAO;
import webshop.dao.SizeDAO;
import webshop.dao.StaffDAO;
import webshop.dao.OriginDAO;
import webshop.dao.MaterialDAO;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import webshop.entity.Account;
import webshop.entity.Brand;
import webshop.entity.Customer;
import webshop.entity.Type;
import webshop.security.Authentication;
import webshop.security.JwtUtil;
import webshop.security.XSS;
import webshop.security.Roles;
import webshop.entity.Origin;
import webshop.entity.Material;
import webshop.entity.Size;
import webshop.entity.Staff;
import webshop.entity.Product;




@Controller
public class EmployeeController {

    
    @Autowired
    BrandDAO brand;
    @Autowired
    TypeDAO type;
    @Autowired
    OriginDAO origin;
    @Autowired
    MaterialDAO material;
    @Autowired
    SizeDAO size;
    @Autowired
    ProductDAO productDAO; // Ensure ProductDAO is injecte
    @Autowired
	ProductDetailDAO prdd;
    @Autowired
	ProductDAO product;
    @Autowired
	AccountDAO accd;
	@Autowired
	StaffDAO stafd;
	@Autowired
	CustomerDAO cusd;
	

    
    @RequestMapping("emhome")
	public String homee(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	
    	int auth = Authentication.redirectAuthen(request, response);
		
		if(auth!=2) return "redirect:home.htm";

		return "employee/home";
	}
    
    
    @RequestMapping("emproduct")
	public String emproduct(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws IOException {
    	
    	int auth = Authentication.redirectAuthen(request, response);
		
		if(auth!=2) return "redirect:home.htm";

		List<Product> dsProduct = product.getAllProducts();

		List<ProductDetail> dsDetail = prdd.getAllProductDetails();

		List<Object[]> productInfoList = new ArrayList<Object[]>();

		for (Product p : dsProduct) {
			Double minPrice = dsDetail.stream().filter(detail -> detail.getProduct().getId() == p.getId())
					.mapToDouble(ProductDetail::getPrice).min() // Lấy giá nhỏ nhất
					.orElse(0); // Nếu không tìm thấy, trả về NaN

			productInfoList.add(new Object[] { p.getId(), p.getName(), minPrice, p.getImage() });
		}

		model.addAttribute("message", "Sản phẩm của shop");
		model.addAttribute("productInfo", productInfoList);

		return "employee/qlproduct";
	}
    
   
    
    @RequestMapping("empersonal")
	public String personal(HttpServletRequest request, HttpSession session,  HttpServletResponse response) throws IOException {
    	int auth = Authentication.redirectAuthen(request, response);
		
		if(auth!=2) return "redirect:home.htm";


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

		return "employee/personal";
	}
 
    
    @RequestMapping(value = "emsearch", method = RequestMethod.POST)
	public String search(@RequestParam(value = "emsearch", required = false) String search, ModelMap model,  
			HttpServletRequest request, HttpServletResponse response) throws IOException {
    	int auth = Authentication.redirectAuthen(request, response);
		
		if(auth!=2) return "redirect:home.htm";

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
		model.addAttribute("message", "Sản phẩm cần tìm");
		model.addAttribute("productInfo", productInfoList);

		return "employee/qlproduct";
	}


    @RequestMapping("emaddproduct")
    public String home(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws IOException {
    	int auth = Authentication.redirectAuthen(request, response);
		
		if(auth!=2) return "redirect:home.htm";
    	System.out.println("Thêm sản phẩm thành công: với GET ");
        // Lấy các thông tin về loại sản phẩm, xuất xứ, thương hiệu và chất liệu
        List<Type> dsTypes = type.getAllTypes(); // Lấy tất cả loại sản phẩm
        List<Size> dsSizes = size.getAllSizes(); // Lấy tất cả kích thước
        List<Origin> dsOrigins = origin.getAllOrigins(); // Lấy tất cả xuất xứ
        List<Brand> dsBrands = brand.getAllBrands(); // Lấy tất cả thương hiệu
        List<Material> dsMaterials = material.getAllMaterials(); // Lấy tất cả chất liệu

        // Thêm các thông tin vào model
        model.addAttribute("types", dsTypes);
        model.addAttribute("sizes", dsSizes);
        model.addAttribute("origins", dsOrigins);
        model.addAttribute("brands", dsBrands);
        model.addAttribute("materials", dsMaterials);
        
     


        // Xử lý thêm logic để lưu sản phẩm vào cơ sở dữ liệu

        // Trả về trang addproduct nếu không có lỗi
        return "employee/addproduct";
    }
    

    @RequestMapping(value = "emaddproduct", method = RequestMethod.POST)
    public String addProduct(HttpServletRequest request, Model model, HttpServletResponse response,
    		@RequestParam(value = "file", required = false) MultipartFile file
    		) throws IOException {
    	
    	int auth = Authentication.redirectAuthen(request, response);
		
		if(auth!=2) return "redirect:home.htm";
    	
        System.out.println("Thêm sản phẩm thành công: với POST ");

        // Lấy các thông tin từ form
        String name = XSS.sanitizeInput(request.getParameter("name"));
        String description = request.getParameter("description");
        
        String image = request.getParameter("image"); // Lấy tên file từ form
        String typesID = request.getParameter("typesID");
        String originsID = request.getParameter("originsID");
        String brandsID = request.getParameter("brandsID");
        String materialsID = request.getParameter("materialsID");
        
        // Debug: Kiểm tra giá trị lấy được
        System.out.println("Tên sản phẩm: " + name);
        System.out.println("Mô tả sản phẩm: " + description);
        System.out.println("Tên file ảnh: " + image);
        System.out.println("Loại sản phẩm (ID): " + typesID);
        System.out.println("Xuất xứ (ID): " + originsID);
        System.out.println("Thương hiệu (ID): " + brandsID);
        System.out.println("Chất liệu (ID): " + materialsID);

        // Tạo đối tượng từ ID
        Type type = new Type();
        type.setId(Integer.parseInt(typesID));
        
      
        Origin origin = new Origin();
        origin.setId(Integer.parseInt(originsID));

        Brand brand = new Brand();
        brand.setId(Integer.parseInt(brandsID));

        Material material = new Material();
        material.setId(Integer.parseInt(materialsID));
        
        String fileName ="";
        
        if (file != null && !file.isEmpty()) {
	        try {
	            fileName = file.getOriginalFilename();
	            String uploadDir = request.getServletContext().getRealPath("/images/products/");
	            File uploadFile = new File(uploadDir, fileName);
	            file.transferTo(uploadFile);

	        } catch (Exception e) {
	            request.setAttribute("errorMessage", "Lỗi khi upload ảnh: " + e.getMessage());
	            return "user/personal";
	        }
	    }

        // Lưu thông tin vào cơ sở dữ liệu
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setImage(fileName); // Chỉ lưu tên file vào cơ sở dữ liệu
        product.setType(type); // Gán đối tượng Type vào sản phẩm
        product.setOrigin(origin); // Gán đối tượng Origin vào sản phẩm
        product.setBrand(brand); // Gán đối tượng Brand vào sản phẩm
        product.setMaterial(material); // Gán đối tượng Material vào sản phẩm
        boolean isSuccess = productDAO.addProduct(product);
        
     // Xử lý upload ảnh (nếu có)
	    

        // Xử lý kết quả
        if (isSuccess) {
            model.addAttribute("message", "Sản phẩm đã được thêm thành công!");
            return "redirect:emproduct.htm"; // Trả về trang hiển thị thành công
        } else {
            model.addAttribute("message", "Đã xảy ra lỗi khi thêm sản phẩm!");
            return "error"; // Trả về trang hiển thị lỗi
        }
    }
    
    
    @RequestMapping("updateprod")
    public String update(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws IOException {
    	int auth = Authentication.redirectAuthen(request, response);
		
		if(auth!=2) return "redirect:home.htm";
    	System.out.println("Thêm sản phẩm thành công: với GET ");
        // Lấy các thông tin về loại sản phẩm, xuất xứ, thương hiệu và chất liệu
        List<Type> dsTypes = type.getAllTypes(); // Lấy tất cả loại sản phẩm
        List<Size> dsSizes = size.getAllSizes(); // Lấy tất cả kích thước
        List<Origin> dsOrigins = origin.getAllOrigins(); // Lấy tất cả xuất xứ
        List<Brand> dsBrands = brand.getAllBrands(); // Lấy tất cả thương hiệu
        List<Material> dsMaterials = material.getAllMaterials(); // Lấy tất cả chất liệu
        
        String sid = request.getParameter("productId");
        int id = Integer.parseInt(sid);
        Product pr = product.getProductById(id);

        // Thêm các thông tin vào model
        model.addAttribute("product", pr);
        model.addAttribute("types", dsTypes);
        model.addAttribute("sizes", dsSizes);
        model.addAttribute("origins", dsOrigins);
        model.addAttribute("brands", dsBrands);
        model.addAttribute("materials", dsMaterials);
        
     


        // Xử lý thêm logic để lưu sản phẩm vào cơ sở dữ liệu

        // Trả về trang addproduct nếu không có lỗi
        return "employee/updateproduct";
    }
    

    @RequestMapping(value = "updateprod", method = RequestMethod.POST)
    public String updateProduct(HttpServletRequest request, Model model, HttpServletResponse response,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
    	
    	int auth = Authentication.redirectAuthen(request, response);
		
		if(auth!=2) return "redirect:home.htm";

        System.out.println("Cập nhật sản phẩm với POST");

        // Lấy các thông tin từ form
        String name = XSS.sanitizeInput(request.getParameter("name"));
        String description = request.getParameter("description");
        String typesID = request.getParameter("typesID");
        String originsID = request.getParameter("originsID");
        String brandsID = request.getParameter("brandsID");
        String materialsID = request.getParameter("materialsID");

        // Debug: Kiểm tra giá trị lấy được
        System.out.println("Tên sản phẩm: " + name);
        System.out.println("Mô tả sản phẩm: " + description);
        System.out.println("Loại sản phẩm (ID): " + typesID);
        System.out.println("Xuất xứ (ID): " + originsID);
        System.out.println("Thương hiệu (ID): " + brandsID);
        System.out.println("Chất liệu (ID): " + materialsID);

        // Tạo đối tượng từ ID
        Type type = new Type();
        type.setId(Integer.parseInt(typesID));

        Origin origin = new Origin();
        origin.setId(Integer.parseInt(originsID));

        Brand brand = new Brand();
        brand.setId(Integer.parseInt(brandsID));

        Material material = new Material();
        material.setId(Integer.parseInt(materialsID));

        // Lấy sản phẩm từ cơ sở dữ liệu
        String sid = request.getParameter("productId");
        int id = Integer.parseInt(sid);
        Product product = productDAO.getProductById(id);

        // Cập nhật các thông tin cơ bản
        product.setName(name);
        product.setDescription(description);
        product.setType(type);
        product.setOrigin(origin);
        product.setBrand(brand);
        product.setMaterial(material);

        // Kiểm tra và xử lý file ảnh
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = file.getOriginalFilename();
                String uploadDir = request.getServletContext().getRealPath("/images/products/");
                File uploadFile = new File(uploadDir, fileName);
                file.transferTo(uploadFile);

                // Cập nhật tên file ảnh nếu upload thành công
                product.setImage(fileName);
                System.out.println("Ảnh mới được tải lên: " + fileName);
            } catch (Exception e) {
                request.setAttribute("errorMessage", "Lỗi khi upload ảnh: " + e.getMessage());
                return "user/personal";
            }
        } else {
            System.out.println("Không có ảnh mới được tải lên.");
        }

        // Cập nhật sản phẩm vào cơ sở dữ liệu
        boolean isSuccess = productDAO.updateProduct(product);


        return "redirect:emproductinfo.htm?proid="+sid;
    }

    
    @RequestMapping("emproductinfo")
	public String productinfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	
    	int auth = Authentication.redirectAuthen(request, response);
		
		if(auth!=2) return "redirect:home.htm";

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

		return "employee/product";
	}

    
    //theem chi tiet san pham
    @RequestMapping("emaddproductdetail")
    public String home_addPrD(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws IOException {
    	
    	int auth = Authentication.redirectAuthen(request, response);
		
		if(auth!=2) return "redirect:home.htm";
        // Lấy danh sách sản phẩm
    	String id = request.getParameter("productid");
    	int idd = Integer.parseInt(id);
        Product prdct = productDAO.getProductById(idd);
        
        model.addAttribute("product", prdct);
        
        // Lấy danh sách kích thước
        List<Size> sizes = size.getAllSizes(); // Service để lấy danh sách kích thước
        model.addAttribute("sizes", sizes);

        return "employee/addProductDetail";
    }
    
    
  //theem chi tiet san pham
    @RequestMapping("emdelprod")
    public String delprod(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws IOException {
    	
    	int auth = Authentication.redirectAuthen(request, response);
		
		if(auth!=2) return "redirect:home.htm";
        // Lấy danh sách sản phẩm
    	String id = request.getParameter("productid");
    	int idd = Integer.parseInt(id);
        Product prdct = productDAO.getProductById(idd);
        
        List<ProductDetail> listprdd = prdd.getAllProductDetails();
        
        for(ProductDetail prd: listprdd) {
        	if(prd.getProduct().getId() == prdct.getId()) {
        		prdd.deleteProductDetail(prd.getId());
        	}
        }
        
        product.deleteProduct(idd);

        return "redirect:emproduct.htm";
    }

    
    @RequestMapping(value = "emaddproductdetail", method = RequestMethod.POST)
    public String addProductDetail(HttpServletRequest request, Model model, 
    		RedirectAttributes redirectAttributes, HttpServletResponse response) throws IOException {
    	
    	int auth = Authentication.redirectAuthen(request, response);
		
		if(auth!=2) return "redirect:home.htm";
    	int productId = Integer.parseInt(request.getParameter("productID"));
    	int price=Integer.parseInt(request.getParameter("price"));
    	int quantity= Integer.parseInt(request.getParameter("quantity"));
    	String sizeId = request.getParameter("sizeID");
    	
    	List<ProductDetail> list = prdd.getAllProductDetails();
    	ProductDetail productDetail = null;
        boolean flag = false;
        boolean isS = false;
        
        //Nếu size đã có thì cập nhật
        for(ProductDetail d: list) {
        	if(d.getProduct().getId() == productId && d.getSize().getId().equals(sizeId)) {
        		productDetail = prdd.getProductDetailById(d.getId());
        		flag = true;
        		continue;
        	}
        }
        
        if(productDetail == null) {
        	productDetail = new ProductDetail();
        	flag = false;
        }
        
        // Tạo đối tượng ProductDetail
        
        productDetail.setProduct(productDAO.getProductById(productId));
        productDetail.setSize(size.getSizeById(sizeId));
        productDetail.setPrice(price);
        productDetail.setQuantity(quantity);
        
        
        
        //Chưa có size thì thêm mới
        if(flag) {
        	isS= prdd.updateProductDetail(productDetail); 
        }else {
        	isS= prdd.addProductDetail(productDetail); 
        }

        // Lưu vào cơ sở dữ liệu
        
        if(isS) {
        	redirectAttributes.addFlashAttribute("message", "Thêm chi tiết sản phẩm thành công!");
        return "redirect:emproduct.htm";
        }
        else {
        	return "error"; // Trả về trang hiển thị lỗi
        }
        
    }
    
    //thao tác với đặt tính của sản phẩm
    @RequestMapping("emprodattribute")
    public String prodAttributeG(HttpServletRequest request, ModelMap model, HttpServletResponse response) throws IOException {
    	
    	int auth = Authentication.redirectAuthen(request, response);
		
		if(auth!=2) return "redirect:home.htm";
    	List<Type> dsType = type.getAllTypes();
    	List<Origin> dsOrigins = origin.getAllOrigins();
    	List<Brand> dsBrand = brand.getAllBrands();
    	List<Material> dsMaterials = material.getAllMaterials();
    	
    	model.addAttribute("types", dsType);
    	model.addAttribute("origins", dsOrigins);
    	model.addAttribute("brands", dsBrand);
    	model.addAttribute("materials", dsMaterials);
    	return "employee/proAttributes";
    }
    
    @RequestMapping(value = "emprodattribute", method = RequestMethod.POST)
    public String prodAttributeP(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam("name") String name,
            @RequestParam("action") String action,
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("type") String tp) throws IOException {
    	
    	int auth = Authentication.redirectAuthen(request, response);
		
		if(auth!=2) return "redirect:home.htm";
    	
    	String move = "";

        try {
            if ("category".equals(tp)) { // Xử lý loại sản phẩm
                if ("add".equals(action) && name != null) {
                	Type category = new Type();
                    category.setName(name);
                    type.addType(category);
                } else if ("update".equals(action) && id != null && name != null) {
                	Type category = type.getTypeById(id);
                    if (category != null) {
                        category.setName(name);
                        type.updateType(category);
                    }
                } else if ("delete".equals(action) && id != null) {
                    type.deleteType(id);
                }
                move = "class1";
            }
            if ("material".equals(tp)) { // Xử lý chất liệu sản phẩm
                if ("add".equals(action) && name != null) {
                    Material mt = new Material();
                    mt.setName(name);
                    material.addMaterial(mt);
                } else if ("update".equals(action) && id != null && name != null) {
                    Material mt = material.getMaterialById(id);
                    if (mt != null) {
                        mt.setName(name);
                        material.updateMaterial(mt);
                    }
                } else if ("delete".equals(action) && id != null) {
                    material.deleteMaterial(id);
                }
                move = "class2";
            }
            if ("origin".equals(tp)) { // Xử lý xuất xứ sản phẩm
                if ("add".equals(action) && name != null) {
                    Origin og = new Origin();
                    og.setName(name);
                    origin.addOrigin(og);
                } else if ("update".equals(action) && id != null && name != null) {
                    Origin og = origin.getOriginById(id);
                    if (og != null) {
                        og.setName(name);
                        origin.updateOrigin(og);
                    }
                } else if ("delete".equals(action) && id != null) {
                    origin.deleteOrigin(id);
                }
                move = "class3";
            }
            if ("brand".equals(tp)) { // Xử lý thương hiệu sản phẩm
                if ("add".equals(action) && name != null) {
                    Brand br = new Brand();
                    br.setName(name);
                    brand.addBrand(br);
                } else if ("update".equals(action) && id != null && name != null) {
                    Brand br = brand.getBrandById(id);
                    if (br != null) {
                        br.setName(name);
                        brand.updateBrand(br);
                    }
                } else if ("delete".equals(action) && id != null) {
                    brand.deleteBrand(id);
                }
                move = "class4";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:emprodattribute.htm#"+move; 
    }


    
//    @RequestMapping("feedback")
//	public String feedback(ModelMap model) {
//
//    	List<Type> dsType = type.getAllTypes();
//    	List<Origin> dsOrigins = origin.getAllOrigins();
//    	List<Brand> dsBrand = brand.getAllBrands();
//    	List<Material> dsMaterials = material.getAllMaterials();
//    	
//    	model.addAttribute("types", dsType);
//    	model.addAttribute("origins", dsOrigins);
//    	model.addAttribute("brands", dsBrand);
//    	model.addAttribute("materials", dsMaterials);
//		
//
//		return "employee/feedback";
//	}

}