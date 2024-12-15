<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <title>Thêm sản phẩm</title>
    <link rel="stylesheet" href="css/page.css" media="screen">
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        
        h2{
    		top: 100%;
    		z-index: 1;
    		font-weight: 600!important;
   			color: #478ac9!important;
    		font-size: 22px!important;
    		display: block!important;
    		width: 100%!important;
    		color: #555555;
    		text-decoration: none!important;
    		text-align: center;
    	}

        .form-container,
        .result {
            max-width: 1000px;
            margin: 60px auto;
            padding: 20px;
            
            border: 1px solid #ccc;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            background-color: #f9f9f9;
        }
        
        .wrap{
        width: 100%;
        }


        label {
            font-weight: bold;
            display: block;
            margin: 10px 0 5px;
        }

        .input,
        .textarea,
        .select,
        .button {
            width: 100%;
            padding: 10px;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 16px;
        }
        
   

        .textarea {
            resize: vertical;
            height: 100px;
        }

        .button {
            background-color: #007BFF;
            color: white;
            border: none;
            cursor: pointer;
            font-size: 16px;
            font-weight: bold;
        }

        .button:hover {
            background-color: #0056b3;
        }
    </style>
    
</head>

<body class="u-body u-xl-mode" data-style="shopping-cart-template-1" data-posts=""
	data-global-section-properties="{&quot;code&quot;:&quot;CART&quot;,&quot;colorings&quot;:{&quot;light&quot;:[&quot;clean&quot;,&quot;clean&quot;],&quot;colored&quot;:[&quot;clean&quot;,&quot;clean&quot;,&quot;clean&quot;],&quot;dark&quot;:[&quot;clean&quot;,&quot;clean&quot;,&quot;clean&quot;]},&quot;isPreset&quot;:true}"
	data-source="fix" data-lang="en"
	data-page-sections-style="[{&quot;name&quot;:&quot;shopping-cart-1&quot;,&quot;margin&quot;:&quot;both&quot;,&quot;repeat&quot;:false}]"
	data-page-coloring-types="{&quot;light&quot;:[&quot;clean&quot;,&quot;clean&quot;],&quot;colored&quot;:[&quot;clean&quot;,&quot;clean&quot;,&quot;clean&quot;],&quot;dark&quot;:[&quot;clean&quot;,&quot;clean&quot;,&quot;clean&quot;]}"
	data-page-category="&quot;Cart&quot;">
	<jsp:include page="/WEB-INF/views/layout/eheader.jsp" />

    <div class="form-container">
        <h2
				class=" gioHang u-align-center-lg u-align-center-md u-align-center-sm u-align-center-xs u-custom-font u-font-ubuntu u-text u-text-default u-text-1">
				Cập nhật thông tin sản phẩm</h2>
        <form action="updateprod.htm" method="POST" enctype="multipart/form-data" class="form">
            <label for="name">Tên sản phẩm:</label>
            <input class="input" type="hidden" value="${product.id }" name="productId">
            <input class="input" type="text" id="name" value="${product.name }" name="name" placeholder="Nhập tên sản phẩm" required>
            
            <label for="image">Hình ảnh:</label>
            <input class="input" type="file" id="file" name="file" accept="image/*"  onchange="previewFile(event)">

            <label for="typesID">Loại sản phẩm:</label>
            <select class="select" id="typesID" name="typesID" required>
					<c:if test="${not empty types}">
					    <c:forEach var="type" items="${types}">
					        <option value="${type.id}" 
					                <c:if test="${type.id == product.type.id}">selected</c:if>>
					            ${type.name}
					        </option>
					    </c:forEach>
					</c:if>
            </select>

            <label for="originsID">Xuất xứ:</label>
            <select class="select" id="originsID" name="originsID" required>
                <c:if test="${not empty origins}">
                    <c:forEach var="origin" items="${origins}">
                        <option value="${origin.id}" 
				                <c:if test="${origin.id == product.origin.id}">selected</c:if>>
				            ${origin.name}
				        </option>
                    </c:forEach>
                </c:if>
            </select>

            <label for="brandsID">Thương hiệu:</label>
            <select class="select" id="brandsID" name="brandsID" required>

                <c:if test="${not empty brands}">
                    <c:forEach var="brand" items="${brands}">
                        <option value="${brand.id}" 
				                <c:if test="${brand.id == product.brand.id}">selected</c:if>>
				            ${brand.name}
                    </c:forEach>
                </c:if>
            </select>

            <label for="materialsID">Chất liệu:</label>
            <select class="select" id="materialsID" name="materialsID" required>

                <c:if test="${not empty materials}">
                    <c:forEach var="material" items="${materials}">
                        <option value="${material.id}" 
				                <c:if test="${material.id == product.material.id}">selected</c:if>>
				            ${material.name}
                    </c:forEach>
                </c:if>
            </select>

            <button class="button" type="submit">Cập nhật</button>
        </form>
    </div>
    <jsp:include page="/WEB-INF/views/layout/footer.jsp" />




</body>

</html>