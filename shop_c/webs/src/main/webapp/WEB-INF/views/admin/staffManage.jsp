<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html style="font-size: 16px;">

<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta charset="utf-8">
	<meta name="keywords" content="Product 1 Title, Product 2 Title, Product 3 Title">
	<meta name="description" content="">
	<title>Quản lý nhân viên</title>
	<link rel="stylesheet" href="css/page.css" media="screen">

</head>

<body class="u-body u-xl-mode" data-style="shopping-cart-template-1" data-posts=""
	data-global-section-properties="{&quot;code&quot;:&quot;CART&quot;,&quot;colorings&quot;:{&quot;light&quot;:[&quot;clean&quot;,&quot;clean&quot;],&quot;colored&quot;:[&quot;clean&quot;,&quot;clean&quot;,&quot;clean&quot;],&quot;dark&quot;:[&quot;clean&quot;,&quot;clean&quot;,&quot;clean&quot;]},&quot;isPreset&quot;:true}"
	data-source="fix" data-lang="en"
	data-page-sections-style="[{&quot;name&quot;:&quot;shopping-cart-1&quot;,&quot;margin&quot;:&quot;both&quot;,&quot;repeat&quot;:false}]"
	data-page-coloring-types="{&quot;light&quot;:[&quot;clean&quot;,&quot;clean&quot;],&quot;colored&quot;:[&quot;clean&quot;,&quot;clean&quot;,&quot;clean&quot;],&quot;dark&quot;:[&quot;clean&quot;,&quot;clean&quot;,&quot;clean&quot;]}"
	data-page-category="&quot;Cart&quot;">

	<jsp:include page="/WEB-INF/views/layout/aheader.jsp" />

	<section style= "min-height: 80vh;" class="u-clearfix u-block-90d4-1" custom-posts-hash="[T,[T,T],[[[I,T],[I,T],[I,T]],[],[],[]]]"
		data-section-properties="{&quot;margin&quot;:&quot;both&quot;,&quot;stretch&quot;:true}" data-id="90d4"
		data-style="shopping-cart-1" id="sec-dd88">




		<div class="notmargin u-clearfix u-sheet u-block-90d4-2">

			<c:if test="${not empty staffList}">
				<h2
					class=" gioHang u-align-center-lg u-align-center-md u-align-center-sm u-align-center-xs u-custom-font u-font-ubuntu u-text u-text-default u-text-1">
					Quản lý nhân viên</h2>
			</c:if>




			<c:if test="${not empty staffList}">
				<div style="min-height: 0;" class="u-cart u-expanded-width u-layout-grid u-block-90d4-6">
					<div class="u-cart-products-table u-table u-table-responsive u-block-90d4-7">
						<table class="u-table-entity u-block-90d4-12">
							<colgroup>
								<col width="25%">
								<col width="20%">
								<col width="20%">
								<col width="20%">
								<col width="15%">
							</colgroup>
							<thead class="u-custom-color-1 u-table-header u-block-90d4-13">
								<tr style="height: 33px;">
									<th class="u-border-3 u-border-grey-15 u-table-cell u-block-90d4-14">Tên nhân viên
										</th>
									<th class="center u-border-3 u-border-grey-15 u-table-cell u-block-90d4-12">
										Số điện thoại
									</th>
									<th class="center u-border-3 u-border-grey-15 u-table-cell u-block-90d4-15">
										Email tài khoản
									</th>
									<th class="center u-border-3 u-border-grey-15 u-table-cell u-block-90d4-16">
										Trạng thái tài khoản</th>
									<th class="center u-border-3 u-border-grey-15 u-table-cell u-block-90d4-17">
										Hành động</th>

								</tr>
							</thead>
							<tbody class="u-align-left u-table-alt-grey-5 u-table-body u-block-90d4-18">

								<!--Sản phẩm nè-->
								<c:forEach var="staff" items="${staffList}">
									<tr id="${staff.name}" style="height: 100px;" class="cartProduct">
										<td id=""
											class="center u-border-1 u-border-grey-15 u-table-cell u-block-90d4-45">
											<div
												class="center u-cart-product-price u-product-control u-product-price u-block-90d4-46">
												<div class="u-price-wrapper">
													<div class="u-old-price"
														style="text-decoration: line-through !important;"></div>
													<div class="u-price" style="font-weight: 700; font-size: 1rem;">
														${staff.name}</div>
												</div>
											</div>
										</td>
										<td id="}"
											class="center u-border-1 u-border-grey-15 u-table-cell u-block-90d4-45">
											<div
												class="center u-cart-product-price u-product-control u-product-price u-block-90d4-46">
												<div class="u-price-wrapper">
													<div class="u-old-price"
														style="text-decoration: line-through !important;"></div>
													<div class="u-price" style="font-weight: 700; font-size: 1rem;">
														${staff.phone}</div>
												</div>
											</div>
										</td>
										<td id=""
											class="center u-border-1 u-border-grey-15 u-table-cell u-block-90d4-45">
											<div
												class="center u-cart-product-price u-product-control u-product-price u-block-90d4-46">
												<div class="u-price-wrapper">
													<div class="u-old-price"
														style="text-decoration: line-through !important;"></div>
													<div class="u-price" style="font-weight: 700; font-size: 1rem;">
														${staff.account.email}</div>
												</div>
											</div>
										</td>
										<td id=""
											class="center u-border-1 u-border-grey-15 u-table-cell u-block-90d4-45">
											<div
												class="center u-cart-product-price u-product-control u-product-price u-block-90d4-46">
												<div class="u-price-wrapper">
													<div class="u-old-price"
														style="text-decoration: line-through !important;"></div>
													<div class="u-price" style="font-weight: 700; font-size: 1rem;">
														<c:choose>
								                            <c:when test="${staff.account.status == 1}">Hoạt động</c:when>
								                            <c:otherwise>Không hoạt động</c:otherwise>
								                        </c:choose>
							                        </div>
												</div>
											</div>
										</td>
										<td id=""
											class="center u-border-1 u-border-grey-15 u-table-cell u-block-90d4-50">
											<div
												class="center u-cart-product-subtotal u-product-control u-product-price u-block-90d4-51">
												<div class="u-price-wrapper">
													<div class="u-old-price"
														style="text-decoration: line-through !important;"></div>
													<div class="u-price" style="font-weight: 400;" data-total>
													<form action="staffInfo.htm" method="post">
														<input type="hidden" value="${staff.account.id}" name="id">
														<button class="save">Xem chi tiết</button></div>
													</form>
														
												</div>
											</div>
										</td>
										
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					
					
			</c:if>
			<style>
				
				.save{
					margin-right: 5px;
					background: #1877f2;
					padding: 6px 20px;
					border: none;
					border-radius: 5px;
					cursor: pointer;
					color: #fff;
					font-size: 14px;
				}
				
				.save:hover{
				opacity: 0.7;
				}
			
				.u-button-style.u-quantity-button {
					cursor: pointer;
				}
				
				th{
				text-align: center;
				height: 40px!important;
				}
				
				tr{
				height: 40px!important;
				}

				.center.delete {
					color: black !important;
					cursor: pointer;
				}

				.center.delete:hover {
					color: red;
					cursor: pointer;
				}

				.center.delete svg:hover {
					fill: red;
					cursor: pointer;
				}

				.center.delete span {
					margin: 0 auto;
				}

				.u-align-center.u-border-1.u-border-palette-5-light-2 {
					margin: 4px 2px;
					border-radius: 8px;
				}

				.u-border-3.u-border-grey-15.u-table-cell {
					font-size: 14px;
				}

				.u-align-center.u-product-control.u-text {
					display: block;
					margin: 0 !important;
					margin-top: 15px !important;
					width: 100%;
				}

				.center {
					text-align: center;
					margin: 0 auto;
				}

				.u-product-title-link {
					font-size: 17px !important;
					display: block !important;
					margin-left: 4px;
					width: 100px;
					overflow: hidden;
					white-space: nowrap;
					text-overflow: ellipsis;
					align-items: center;
					display: flex;
					flex-grow: 1;
				}

				.products-cart {
					display: flex;
				}

				.products-cart span {
					display: flex !important;
				}

				.products-cart h2 {
					width: 100px;
					overflow: hidden;
					white-space: nowrap;
					text-overflow: ellipsis;
					align-items: center;
					display: flex;
					flex-grow: 1;
				}

				.u-align-center.u-border-1.u-border-palette-5-light-2:hover {
					transform: translateY(-4px);
					box-shadow: 0px 0px 8px 0px rgba(160, 160, 160, 2);
				}

				.u-cart-product-image.u-image.u-image-round {
					height: 80px;
					width: 80px;
				}

				body {
					background-color: #eee !important;
				}

				.u-clearfix.u-sheet.u-block-90d4-2 {
					margin-top: 40px;
					margin-bottom: 40px;
					border-radius: 10px !important;
					overflow: hidden;
					background: #fff;
					min-height: 0 !important;
					-webkit-box-shadow: 10px 10px 5px 0px #cccccc;
					-moz-box-shadow: 10px 10px 5px 0px #cccccc;
					box-shadow: 10px 10px 5px 0px #cccccc;
				}

				.u-cart-remove-item {
					cursor: pointer;
					color: red;
				}

				.gioHang {
					font-size: 20px !important;
					font-weight: 700 !important;
					margin-top: 24px !important;
					text-decoration: underline;
					margin: 0 auto;
				}

				.u-cart.u-expanded-width.u-layout-grid.u-block-90d4-6 {
					padding: 45px;
					padding-top: 0;
					margin: 0;
				}
			</style>
		</div>
		</div>


		<style data-mode="XL" data-visited="true">
			@media (min-width : 1200px) {
				.u-block-90d4-2 {
					min-height: 100vh;
				}

				.u-block-90d4-6 {
					width: 1140px;
					min-height: 375px;
					margin-top: 38px;
					margin-left: 0;
					margin-right: 0;
					margin-bottom: 60px;
				}

				.u-block-90d4-13 {
					background-image: none;
					font-weight: 700;
				}

				.u-block-90d4-14 {
					border-style: double;
				}

				.u-block-90d4-15 {
					border-style: double;
				}

				.u-block-90d4-16 {
					border-style: double;
				}

				.u-block-90d4-17 {
					border-style: double;
				}

				.u-block-90d4-20 {
					font-size: 1.5em;
					margin-right: 10px;
					padding-top: 3px;
					padding-bottom: 3px;
					padding-left: 3px;
					padding-right: 3px;
				}

				.u-block-90d4-22 {
					font-size: 1.125rem;
				}

				.u-block-90d4-90 {
					background-image: none;
					padding-top: 0;
					padding-bottom: 0;
					padding-left: 0;
					padding-right: 0;
				}

				.u-block-90d4-26 {
					width: 100px;
				}

				.u-block-90d4-31 {
					font-size: 1.5em;
					margin-right: 10px;
				}

				.u-block-90d4-32 {
					width: 100px;
					height: 100px;
				}

				.u-block-90d4-33 {
					font-size: 1.125rem;
				}

				.u-block-90d4-37 {
					width: 100px;
				}

				.u-block-90d4-42 {
					font-size: 1.5em;
					margin-right: 10px;
					padding-top: 3px;
					padding-bottom: 3px;
					padding-left: 3px;
					padding-right: 3px;
				}

				.u-block-90d4-43 {
					width: 100px;
					height: 100px;
				}

				.u-block-90d4-44 {
					font-size: 1.125rem;
				}

				.u-block-90d4-48 {
					width: 100px;
				}

				.u-block-90d4-58 {
					padding-top: 10px;
					padding-bottom: 10px;
					padding-left: 14px;
					padding-right: 14px;
				}

				.u-block-90d4-60 {
					font-size: 0.875rem;
					text-transform: uppercase;
					letter-spacing: 1px;
				}

				.u-block-90d4-68 {
					text-transform: uppercase;
					font-size: 0.875rem;
					font-weight: 700;
					letter-spacing: 1px;
				}

				.u-block-90d4-80 {
					background-image: none;
				}

				.u-block-90d4-81 {
					font-weight: 700;
				}

				.u-block-90d4-83 {
					font-weight: 700;
				}

				.u-block-90d4-84 {
					font-weight: 700;
				}

				.u-block-90d4-89 {
					margin-top: 20px;
					letter-spacing: 1px;
					font-size: 0.875rem;
					font-weight: 700;
					text-transform: uppercase;
				}
			}
		</style>
		<style data-mode="LG">
			@media (max-width : 1199px) and (min-width: 992px) {
				.u-block-90d4-2 {
					min-height: 100vh;
				}

				.u-block-90d4-6 {
					min-height: 375px;
					margin-top: 102px;
					margin-bottom: 60px;
				}

				.u-block-90d4-13 {
					background-image: none;
					font-weight: 700;
				}

				.u-block-90d4-14 {
					border-style: double;
				}

				.u-block-90d4-15 {
					border-style: double;
				}

				.u-block-90d4-16 {
					border-style: double;
				}

				.u-block-90d4-17 {
					border-style: double;
				}

				.u-block-90d4-20 {
					font-size: 1.5em;
					margin-right: 10px;
					padding-top: 3px;
					padding-bottom: 3px;
					padding-left: 3px;
					padding-right: 3px;
				}

				.u-block-90d4-22 {
					font-size: 1.125rem;
				}

				.u-block-90d4-90 {
					background-image: none;
					padding-top: 0;
					padding-bottom: 0;
					padding-left: 0;
					padding-right: 0;
				}

				.u-block-90d4-26 {
					width: 100px;
				}

				.u-block-90d4-31 {
					font-size: 1.5em;
					margin-right: 10px;
				}

				.u-block-90d4-32 {
					width: 100px;
					height: 100px;
				}

				.u-block-90d4-33 {
					font-size: 1.125rem;
				}

				.u-block-90d4-37 {
					width: 100px;
				}

				.u-block-90d4-42 {
					font-size: 1.5em;
					margin-right: 10px;
					padding-top: 3px;
					padding-bottom: 3px;
					padding-left: 3px;
					padding-right: 3px;
				}

				.u-block-90d4-43 {
					width: 100px;
					height: 100px;
				}

				.u-block-90d4-44 {
					font-size: 1.125rem;
				}

				.u-block-90d4-48 {
					width: 100px;
				}

				.u-block-90d4-58 {
					padding-top: 10px;
					padding-right: 14px;
					padding-bottom: 10px;
					padding-left: 14px;
				}

				.u-block-90d4-60 {
					font-size: 0.875rem;
					text-transform: uppercase;
					letter-spacing: 1px;
				}

				.u-block-90d4-68 {
					text-transform: uppercase;
					font-size: 0.875rem;
					font-weight: 700;
					letter-spacing: 1px;
				}

				.u-block-90d4-80 {
					background-image: none;
				}

				.u-block-90d4-81 {
					font-weight: 700;
				}

				.u-block-90d4-83 {
					font-weight: 700;
				}

				.u-block-90d4-84 {
					font-weight: 700;
				}

				.u-block-90d4-89 {
					margin-top: 20px;
					letter-spacing: 1px;
					font-size: 0.875rem;
					font-weight: 700;
					text-transform: uppercase;
				}
			}
		</style>
		<style data-mode="MD">
			@media (max-width : 991px) and (min-width: 768px) {
				.u-block-90d4-2 {
					min-height: 100vh;
				}

				.u-block-90d4-6 {
					min-height: 375px;
					margin-top: 102px;
					margin-bottom: 60px;
				}

				.u-block-90d4-13 {
					background-image: none;
					font-weight: 700;
				}

				.u-block-90d4-14 {
					border-style: double;
				}

				.u-block-90d4-15 {
					border-style: double;
				}

				.u-block-90d4-16 {
					border-style: double;
				}

				.u-block-90d4-17 {
					border-style: double;
				}

				.u-block-90d4-20 {
					font-size: 1.5em;
					margin-right: 10px;
					padding-top: 3px;
					padding-bottom: 3px;
					padding-left: 3px;
					padding-right: 3px;
				}

				.u-block-90d4-22 {
					font-size: 1.125rem;
				}

				.u-block-90d4-90 {
					background-image: none;
					padding-top: 0;
					padding-bottom: 0;
					padding-left: 0;
					padding-right: 0;
				}

				.u-block-90d4-26 {
					width: 100px;
				}

				.u-block-90d4-31 {
					font-size: 1.5em;
					margin-right: 10px;
				}

				.u-block-90d4-32 {
					width: 100px;
					height: 100px;
				}

				.u-block-90d4-33 {
					font-size: 1.125rem;
				}

				.u-block-90d4-37 {
					width: 100px;
				}

				.u-block-90d4-42 {
					font-size: 1.5em;
					margin-right: 10px;
					padding-top: 3px;
					padding-bottom: 3px;
					padding-left: 3px;
					padding-right: 3px;
				}

				.u-block-90d4-43 {
					width: 100px;
					height: 100px;
				}

				.u-block-90d4-44 {
					font-size: 1.125rem;
				}

				.u-block-90d4-48 {
					width: 100px;
				}

				.u-block-90d4-58 {
					padding-top: 10px;
					padding-right: 14px;
					padding-bottom: 10px;
					padding-left: 14px;
				}

				.u-block-90d4-60 {
					font-size: 0.875rem;
					text-transform: uppercase;
					letter-spacing: 1px;
				}

				.u-block-90d4-68 {
					text-transform: uppercase;
					font-size: 0.875rem;
					font-weight: 700;
					letter-spacing: 1px;
				}

				.u-block-90d4-80 {
					background-image: none;
				}

				.u-block-90d4-81 {
					font-weight: 700;
				}

				.u-block-90d4-83 {
					font-weight: 700;
				}

				.u-block-90d4-84 {
					font-weight: 700;
				}

				.u-block-90d4-89 {
					margin-top: 20px;
					letter-spacing: 1px;
					font-size: 0.875rem;
					font-weight: 700;
					text-transform: uppercase;
				}
			}
		</style>
		<style data-mode="SM">
			@media (max-width : 767px) and (min-width: 576px) {
				.u-block-90d4-2 {
					min-height: 100vh;
				}

				.u-block-90d4-6 {
					min-height: 375px;
					margin-top: -278px;
					margin-bottom: -299px;
				}

				.u-block-90d4-13 {
					background-image: none;
					font-weight: 700;
				}

				.u-block-90d4-14 {
					border-style: double;
				}

				.u-block-90d4-15 {
					border-style: double;
				}

				.u-block-90d4-16 {
					border-style: double;
				}

				.u-block-90d4-17 {
					border-style: double;
				}

				.u-block-90d4-20 {
					font-size: 1.5em;
					margin-right: 10px;
					padding-top: 3px;
					padding-bottom: 3px;
					padding-left: 3px;
					padding-right: 3px;
				}

				.u-block-90d4-22 {
					font-size: 1.125rem;
				}

				.u-block-90d4-90 {
					background-image: none;
					padding-top: 0;
					padding-bottom: 0;
					padding-left: 0;
					padding-right: 0;
				}

				.u-block-90d4-26 {
					width: 100px;
				}

				.u-block-90d4-31 {
					font-size: 1.5em;
					margin-right: 10px;
				}

				.u-block-90d4-32 {
					width: 100px;
					height: 100px;
				}

				.u-block-90d4-33 {
					font-size: 1.125rem;
				}

				.u-block-90d4-37 {
					width: 100px;
				}

				.u-block-90d4-42 {
					font-size: 1.5em;
					margin-right: 10px;
					padding-top: 3px;
					padding-bottom: 3px;
					padding-left: 3px;
					padding-right: 3px;
				}

				.u-block-90d4-43 {
					width: 100px;
					height: 100px;
				}

				.u-block-90d4-44 {
					font-size: 1.125rem;
				}

				.u-block-90d4-48 {
					width: 100px;
				}

				.u-block-90d4-58 {
					padding-top: 10px;
					padding-right: 14px;
					padding-bottom: 10px;
					padding-left: 14px;
				}

				.u-block-90d4-60 {
					font-size: 0.875rem;
					text-transform: uppercase;
					letter-spacing: 1px;
				}

				.u-block-90d4-68 {
					text-transform: uppercase;
					font-size: 0.875rem;
					font-weight: 700;
					letter-spacing: 1px;
				}

				.u-block-90d4-80 {
					background-image: none;
				}

				.u-block-90d4-81 {
					font-weight: 700;
				}

				.u-block-90d4-83 {
					font-weight: 700;
				}

				.u-block-90d4-84 {
					font-weight: 700;
				}

				.u-block-90d4-89 {
					margin-top: 20px;
					letter-spacing: 1px;
					font-size: 0.875rem;
					font-weight: 700;
					text-transform: uppercase;
				}
			}
		</style>
		<style data-mode="XS">
			@media (max-width : 575px) {
				.u-block-90d4-2 {
					min-height: 100vh;
				}

				.u-block-90d4-6 {
					min-height: 375px;
					margin-top: -461px;
					margin-bottom: -483px;
				}

				.u-block-90d4-13 {
					background-image: none;
					font-weight: 700;
				}

				.u-block-90d4-14 {
					border-style: double;
				}

				.u-block-90d4-15 {
					border-style: double;
				}

				.u-block-90d4-16 {
					border-style: double;
				}

				.u-block-90d4-17 {
					border-style: double;
				}

				.u-block-90d4-20 {
					font-size: 1.5em;
					margin-right: 10px;
					padding-top: 3px;
					padding-bottom: 3px;
					padding-left: 3px;
					padding-right: 3px;
				}

				.u-block-90d4-22 {
					font-size: 1.125rem;
				}

				.u-block-90d4-90 {
					background-image: none;
					padding-top: 0;
					padding-bottom: 0;
					padding-left: 0;
					padding-right: 0;
				}

				.u-block-90d4-26 {
					width: 100px;
				}

				.u-block-90d4-31 {
					font-size: 1.5em;
					margin-right: 10px;
				}

				.u-block-90d4-32 {
					width: 100px;
					height: 100px;
				}

				.u-block-90d4-33 {
					font-size: 1.125rem;
				}

				.u-block-90d4-37 {
					width: 100px;
				}

				.u-block-90d4-42 {
					font-size: 1.5em;
					margin-right: 10px;
					padding-top: 3px;
					padding-bottom: 3px;
					padding-left: 3px;
					padding-right: 3px;
				}

				.u-block-90d4-43 {
					width: 100px;
					height: 100px;
				}

				.u-block-90d4-44 {
					font-size: 1.125rem;
				}

				.u-block-90d4-48 {
					width: 100px;
				}

				.u-block-90d4-58 {
					padding-top: 10px;
					padding-right: 14px;
					padding-bottom: 10px;
					padding-left: 14px;
				}

				.u-block-90d4-60 {
					font-size: 0.875rem;
					text-transform: uppercase;
					letter-spacing: 1px;
				}

				.u-block-90d4-68 {
					text-transform: uppercase;
					font-size: 0.875rem;
					font-weight: 700;
					letter-spacing: 1px;
				}

				.u-block-90d4-80 {
					background-image: none;
				}

				.u-block-90d4-81 {
					font-weight: 700;
				}

				.u-block-90d4-83 {
					font-weight: 700;
				}

				.u-block-90d4-84 {
					font-weight: 700;
				}

				.u-block-90d4-89 {
					margin-top: 20px;
					letter-spacing: 1px;
					font-size: 0.875rem;
					font-weight: 700;
					text-transform: uppercase;
				}
			}
		</style>
	</section>

	<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	<script src="js/cart.js"></script>
</body>

</html>