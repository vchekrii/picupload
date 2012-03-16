<%@ page language="java" contentType="text/html; charset=utf8" pageEncoding="utf8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html				>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf8">
	<title><spring:message code="label.title" /></title>
	<link rel="stylesheet" type="text/css" href="resources/main.css">
	<script type="text/javascript" src="resources/js/jquery.min.js"></script>

</head>
<body>
	<div id=wrap>
		<c:import url="top.jsp"></c:import>	
		
		<spring:message code="label.submit" var="submit"/>	
		<form method="POST" action="<c:url value="/j_spring_security_check" />">
			<table>
				<tr>
					<td align="right"><spring:message code="label.login" /></td>
					<td><input type="text" name="j_username" /></td>
				</tr>
				<tr>
					<td align="right"><spring:message code="label.password" /></td>
					<td><input type="password" name="j_password" /></td>
				</tr>
				<tr>
					<td align="right"><spring:message code="label.remember" /></td>
					<td><input type="checkbox" name="_spring_security_remember_me" /></td>
				</tr>
				<tr>
					<td colspan="2" align="right">
						<input type="submit" value="${submit}" />
					</td>
				</tr>
			</table>
			 
		</form>
													
	<c:if test="${not empty param.error}">
			<font size=6pt  color="red">
				<spring:message code="label.credentials" />
			</font>
	</c:if>
	</div>
</body>
</html>