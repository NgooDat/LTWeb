<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html style="font-size: 16px;" lang="en">

<head>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta charset="utf-8">
  <meta name="keywords" content="Related Products, INTUITIVE">
  <meta name="description" content="">
  <title>Home</title>
  <link rel="stylesheet" href="css/page.css" media="screen">
  <link rel="stylesheet" href="css/index.css" media="screen">
  <meta name="generator" content="Nicepage 7.0.3, nicepage.com">
  <link id="u-theme-google-font" rel="stylesheet"
    href="https://fonts.googleapis.com/css?family=Roboto:100,100i,300,300i,400,400i,500,500i,700,700i,900,900i|Open+Sans:300,300i,400,400i,500,500i,600,600i,700,700i,800,800i">
  <link id="u-page-google-font" rel="stylesheet"
    href="https://fonts.googleapis.com/css?family=Ubuntu:300,300i,400,400i,500,500i,700,700i">

  <meta name="theme-color" content="#478ac9">
  <meta property="og:title" content="Home">
  <meta property="og:type" content="website">
  <meta data-intl-tel-input-cdn-path="intlTelInput/">
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
    		margin: 0!important;
    	}

        .form-container,
        .result {
            max-width: 1000px!important;
            margin: 60px auto!important;
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
            text-align: left!important;
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



  <section class="skrollable u-align-center u-clearfix u-grey-5 u-lightbox u-parallax u-section-1" id="carousel_93bd"
    data-href="#" data-image-width="2000" data-image-height="1333">

<div class="form-container">
	<c:if test="${not empty message}">
		<div class="alert alert-success">${message}</div>
	</c:if>

	<h2
				class=" gioHang u-align-center-lg u-align-center-md u-align-center-sm u-align-center-xs u-custom-font u-font-ubuntu u-text u-text-default u-text-1">
				Cập nhật giá sản phẩm</h2>
	<form action="emaddproductdetail.htm" method="POST" class="form">
		<!-- Dropdown sản phẩm -->
		<label for="productID">Sản phẩm:</label>
		<select class="select" id="productID" name="productID" required>

					<option value="${product.id}">
						${product.name}
					</option>

		</select>

		<!-- Dropdown size -->
		<label for="sizeID">Size:</label>
		<select class="select" id="sizeID" name="sizeID" required>
			<option value="" disabled selected>Chọn size</option>
			<c:if test="${not empty sizes}">
				<c:forEach var="size" items="${sizes}">
					<option value="${size.id}">${size.name}</option>
				</c:forEach>
			</c:if>
		</select>

		<!-- Trường nhập giá -->
		<label for="price">Giá bán:</label>
		<input class="input" id="price" name="price" placeholder="Nhập giá bán" type="number" min="0" required>

		<!-- Trường nhập số lượng -->
		<label for="quantity">Số lượng:</label>
		<input class="input" id="quantity" name="quantity" placeholder="Nhập số lượng" type="number" min="1" required>

		<button class="button" type="submit">Cập nhật</button>
	</form>
</div>

  </section>
  <style>
    .u-align-center.u-border-1.u-border-palette-5-light-2 {

      border-radius: 8px;
    }
    
    .form-container {
            max-width: 600px;
            margin: auto;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            background-color: #f9f9f9;
        }

        h1 {
            color: #007BFF;
        }

        label {
            font-weight: bold;
            display: block;
            margin: 10px 0 5px;
        }





        button:hover {
            background-color: #0056b3;
        }
    
    .add{
    	max-width: 140px!important;
    	max-height: 140px!important;
    	margin-left: auto !important; 
    	margin-top: auto!important;
    	margin-right: auto !important;
    
    }

    .u-align-center.u-product-control.u-text {
      display: block;
      margin: 0 !important;
      margin-top: 15px !important;
      width: 100%;
    }

    .u-product-title-link {
      font-size: 20px !important;
      display: block !important;
      text-align: center;
    }

    .u-align-center.u-border-1.u-border-palette-5-light-2:hover {
      transform: translateY(-4px);
      box-shadow: 0px 0px 8px 0px rgba(160, 160, 160, 2);
    }
  </style>
  <style class="u-overlap-style">
    .u-overlap:not(.u-sticky-scroll) .u-header {
      background-color: #333333 !important
    }
  </style>



  <jsp:include page="/WEB-INF/views/layout/footer.jsp" />


  <style>
    h2 {
      color: #478ac9 !important;
      opacity: 0.8 !important;
    }

    .u-section-1 .u-container-layout-1 {
      padding: 20px;
      ]
    }

    .u-dialog-section-4 .u-dialog-1 {
      width: 570px;
      min-height: 578px;
      height: auto;
      box-shadow: 0px 0px 8px 0px rgba(0, 0, 0, 0.2);
      margin: 60px auto;
    }

    .u-dialog-section-4 .u-container-layout-1 {
      padding: 34px 35px;
    }

    .u-dialog-section-4 .u-text-1 {
      font-weight: 700;
      margin: 0 165px 0 0;
    }

    .u-dialog-section-4 .u-products-1 {
      margin-top: 30px;
      margin-bottom: 0;
    }

    .u-dialog-section-4 .u-repeater-1 {
      grid-template-columns: 100%;
      min-height: 206px;
      grid-gap: 10px;
    }

    .u-dialog-section-4 .u-container-layout-2 {
      padding: 0 0 30px;
    }

    .u-dialog-section-4 .u-text-2 {
      background-image: none;
      margin: 0;
    }

    .u-dialog-section-4 .u-text-3 {
      font-size: 0.875rem;
      margin: 20px 0 0;
    }

    .u-dialog-section-4 .u-product-quantity-1 {
      width: 125px;
      margin: 30px auto 0 0;
    }

    .u-dialog-section-4 .u-product-price-1 {
      margin: -34px 0 0 auto;
    }

    .u-dialog-section-4 .u-payment-services-1 {
      min-height: 250px;
      min-width: 50px;
      margin: 0;
    }

    .u-dialog-section-4 .u-container-layout-3 {
      padding: 20px 0 0;
    }

    .u-dialog-section-4 .u-container-layout-4 {
      padding: 20px 0 0;
    }

    .u-dialog-section-4 .u-btn-1 {
      width: 100%;
      margin: 0 auto;
    }

    .u-dialog-section-4 .u-icon-1 {
      width: 20px;
      height: 20px;
      left: auto;
      top: 36px;
      position: absolute;
      right: 35px;
      padding: 0;
    }

    @media (max-width: 1199px) {
      .u-dialog-section-4 .u-text-1 {
        margin-right: 165px;
      }

      .u-dialog-section-4 .u-payment-services-1 {
        margin-right: initial;
        margin-left: initial;
      }
    }

    @media (max-width: 991px) {
      .u-dialog-section-4 .u-container-layout-1 {
        padding: 30px;
      }

      .u-dialog-section-4 .u-container-layout-3 {
        padding-bottom: 0;
        padding-left: 0;
        padding-right: 0;
      }

      .u-dialog-section-4 .u-container-layout-4 {
        padding-bottom: 0;
        padding-left: 0;
        padding-right: 0;
      }

      .u-dialog-section-4 .u-icon-1 {
        top: 32px;
        right: 30px;
      }
    }

    @media (max-width: 767px) {
      .u-dialog-section-4 .u-dialog-1 {
        width: 540px;
      }
    }

    @media (max-width: 575px) {
      .u-dialog-section-4 .u-dialog-1 {
        width: 340px;
      }

      .u-dialog-section-4 .u-container-layout-1 {
        padding-left: 20px;
        padding-right: 20px;
      }

      .u-dialog-section-4 .u-text-1 {
        margin-right: 5px;
      }

      .u-dialog-section-4 .u-icon-1 {
        right: 20px;
      }
    }
  </style>
</body>

</html>