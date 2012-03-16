<%@ page language="java" contentType="text/html; charset=utf8" pageEncoding="utf8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf8">
	<title><spring:message code="label.title" /></title>
	<link rel="stylesheet" type="text/css" href="../resources/main.css">
</head>
<body>
	<div id="wrap">
		<c:import url="top.jsp"></c:import>
		<div style="text-align:right; float:right;"><a href="<c:url value="/logout" />"><spring:message code="label.logout" /></a></div>
		
		<!-- TODO on deployment: change a href to actual domain name -->
		<a href="/picload/admin"><spring:message code="label.back" /></a>
		<div style="width:65%; margin:0 auto; float:top;">
			<c:forEach items="${onePicture}" var="onePicture">
				<c:choose>
					<c:when test="${onePicture.banned=='true'}">
						<tr>
							<td><h3><spring:message code="label.640" /></h3></td>
						</tr>
						<tr>
							<td><img src="../resources/uploadedImages/banned/${onePicture.pic640} /"></td>
						</tr>
						<tr>
							<td><input type="text" size="100" value="${url}banned/${onePicture.pic640}"></td>
						</tr>
						
						<hr align="center" width="650px" color="5a5a5a">
						<tr>
							<td><h3><spring:message code="label.800" /></h3></td>
						</tr>
						<tr>
							<td><input type="text" size="100" value="${url}banned/${onePicture.pic800}"></td>
						</tr>
						
						<hr align="center" width="650px" color="5a5a5a">
						<tr>
							<td><h3><spring:message code="label.original" /></h3></td>
						</tr>
						<tr>
							<td><input type="text" size="100" value="${url}banned/${onePicture.pic}"></td>
						</tr>
					</c:when>
					<c:otherwise>
						<tr>
							<td><h3><spring:message code="label.640" /></h3></td>
						</tr>
						<tr>
							<td><img src="../resources/uploadedImages/${onePicture.pic640} /"></td>
						</tr>
						<tr>
							<td><input type="text" size="100" value="${url}${onePicture.pic640}"></td>
						</tr>
						
						<hr align="center" width="650px" color="5a5a5a">
						<tr>
							<td><h3><spring:message code="label.800" /></h3></td>
						</tr>
						<tr>
							<td><input type="text" size="100" value="${url}${onePicture.pic800}"></td>
						</tr>
						
						<hr align="center" width="650px" color="5a5a5a">
						<tr>
							<td><h3><spring:message code="label.original" /></h3></td>
						</tr>
						<tr>
							<td><input type="text" size="100" value="${url}${onePicture.pic}"></td>
						</tr>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</div>
		<br>
		
	</div>
</body>
</html>