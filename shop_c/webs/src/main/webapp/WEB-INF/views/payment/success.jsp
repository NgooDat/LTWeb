<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<base href="${pageContext.servletContext.contextPath}/">
</head>
<body>
	<h1>${message}</h1>
	<c:choose>
		<c:when test="${not empty newOrderId}">
			<a href="order/orderdetail/${newOrderId}.htm">Xem đơn hàng</a>
		</c:when>
		<c:otherwise>
			<a href="home.htm">Về trang chủ</a>
		</c:otherwise>
	</c:choose>
</body>
</html>