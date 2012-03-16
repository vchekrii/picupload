<%@ page language="java" contentType="text/html; charset=utf8" pageEncoding="utf8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf8">
	<title><spring:message code="label.title" /></title>
	<link rel="stylesheet" type="text/css" href="<%=getServletContext().getContextPath()%>/resources/main.css">

</head>
<body>
	<div id=wrap style="text-align:center;">
		<c:import url="WEB-INF/views/top.jsp"></c:import>	

			<font size=6pt  color="red">
				<spring:message code="label.mistake" />
			</font>

	</div>
</body>
</html>