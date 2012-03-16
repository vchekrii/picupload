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

</head>
<body>
	<div id=wrap>
		
		<c:import url="top.jsp"></c:import>		
		
		<c:choose>
			<c:when test="${empty reCaptcha}">
				<div id=upload-block>
			
					<spring:message code="label.upload" var="upload"/>
					<spring:message code="label.image" var="image"/>
					<form:form modelAttribute="uploadItem" action="upload" name="frm" method="post"	enctype="multipart/form-data" >
					
						<table>
							<tr>
								<td width=250><form:input path="fileData" id="image" type="file" value="${image}" /></td>
								<td width=230 align=right><input type="submit" value="${upload}"></td>
							</tr>
								
						</table>
							<form:errors path="fileData" />		
							
					</form:form>
				</div>
			</c:when>
			<c:when test="${not empty reCaptcha}">
				<div id=upload-block-large>
			
					<spring:message code="label.upload" var="upload"/>
					<spring:message code="label.image" var="image"/>
					<form:form modelAttribute="uploadItem" action="upload" name="frm" method="post"	enctype="multipart/form-data" >
					
						<table>
							<tr>
								<td width=250><form:input path="fileData" id="image" type="file" value="${image}" /></td>
								<td width=230 align=right><input type="submit" value="${upload}"></td>
							</tr>
								
						</table>
							<form:errors path="fileData" />	
							<br>
							<spring:message code="label.showCaptcha" />	
							<div align="center">${reCaptcha}</div>
					</form:form>
				</div>
			</c:when>
		</c:choose>
		
		<br>
		<div style="padding-left:10px;">
			<c:forEach items="${pic106}" var="file">
				<tr>
					<td><a href="image/${file}"><img src="resources/uploadedImages/${file} /"></a></td>
				</tr>
			</c:forEach>
		</div>

	</div>
</body>
</html>