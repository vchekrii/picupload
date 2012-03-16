<%@ page language="java" contentType="text/html; charset=utf8" pageEncoding="utf8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page isELIgnored="false" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html				>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf8">
	<title><spring:message code="label.title" /></title>
	<link rel="stylesheet" type="text/css" href="../resources/main.css">
	<script type="text/javascript" src="../resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="../resources/js/jscharts.js"></script>
	
	
</head>
<body>
	<div id=wrap>
		
		<c:import url="top.jsp"></c:import>		
		<div style="text-align:right; float:right;"><a href="<c:url value="/logout" />"><spring:message code="label.logout" /></a></div>
		
		<!-- TODO on deployment: change a href to actual domain name -->
		<a href="/picload/admin"><spring:message code="label.back" /></a>

		<div id=upload-block >
			<div style="font-size:18px;"><spring:message code="label.admin" /></div>
		</div>
		
		<br>
		
		<!-- List of users -->
		<div style="padding-left:10px; width:38%; float:left;">
			<table>
				<c:forEach items="${allUsers}" var="users">
					<tr>
						<c:choose>
							<c:when test="${users.banned=='true'}">
								<td><font color=red><a href="../user/${users.id}" class="banned"><spring:message code="label.user" />${users.ip}</a></font></td>
								<td><a href="../unbanUser/${url}/${users.id}"><spring:message code="label.unbanUser" /></a></td>
							</c:when>
							<c:otherwise>
								<td><a href="../user/${users.id}"><spring:message code="label.user" />${users.ip}</a></td>
								<td><a href="../banUser/${url}/${users.id}"><spring:message code="label.banUser" /></a></td>
							</c:otherwise>
						</c:choose>
						<td><a href="../deleteUser/${url}/${users.id}"><spring:message code="label.deleteUser" /></a></td>
					</tr>
				</c:forEach>
			</table>
			
			<br>
			<!-- Disk space -->
			<spring:message code="label.disk" />
			<div id="chartcontainer">
				<script type="text/javascript">
				var myData = new Array(['Free space', ${freeSpace}], ['Used space', ${usedSpace}]);
				var myChart = new JSChart('chartcontainer', 'pie');
				var colors = ['#660000', '#CCCC99'];
				myChart.setDataArray(myData);
				myChart.colorizePie(colors);
				myChart.setTitle('');
				myChart.setTitleColor('#7F0000');
				myChart.setTextPaddingTop(10);
				myChart.setSize(320, 200);
				myChart.setPieUnitsFontSize(9);
				myChart.setPieValuesFontSize(9);
				myChart.setPieRadius(80);
				myChart.setPieUnitsColor('#555');
				myChart.draw();
				</script>
			</div>
			<!-- /Disk space -->
		</div>
		<!-- /List of users -->
		
		<!-- Images list -->
		<div style="padding-left:10px; width:60%; float:right;">
			<c:forEach items="${allPictures}" var="pictures">
				<c:choose>
					<c:when test="${pictures.banned=='true'}">
						<a href="#" rel="shareit${pictures.pic}"><img class="border" src="../resources/uploadedImages/${pictures.pic106} /"></a>	
					</c:when>	
					<c:otherwise>
						<a href="#" rel="shareit${pictures.pic}"><img class="border" src="../resources/uploadedImages/${pictures.pic106} /"></a>
					</c:otherwise>
				</c:choose>
						<div id="${pictures.pic}" style="position:absolute;	display:none;">
							<div id="shareit-header"></div>
							<div id="shareit-body">
								<div id="shareit-blank"></div>
								<div id="shareit-icon">
								<ul>
									<li><a href="../imageAdmin/${pictures.pic}"><spring:message code="label.showImage" /></a></li>
									<c:choose>
										<c:when test="${pictures.banned=='true'}">
											<li><a href="../unbanImage/${url}/${pictures.pic}"><spring:message code="label.unbanImage" /></a></li>
										</c:when>	
										<c:otherwise>
											<li><a href="../banImage/${url}/${pictures.pic}"><spring:message code="label.banImage" /></a></li>
										</c:otherwise>
									</c:choose>
									<li><a href="../image/delete/${url}/${pictures.pic}.jpg"><spring:message code="label.deleteImage" /></a></li>
									
								</ul>
								</div>
							</div>
						</div>

					<script type="text/javascript">
						$(document).ready(function() {
							$('a[rel="shareit<c:out value="${pictures.pic}"/>"], #<c:out value="${pictures.pic}"/>').click(function() {		
								var height = $(this).height();
								var top = $(this).offset().top - 17;
								var left = $(this).offset().left + ($(this).width() /2) - ($('<c:out value="${pictures.pic}"/>').width() / 2) -20;		
								$('#shareit-header').height(height);
								$('#<c:out value="${pictures.pic}"/>').show();
								$('#<c:out value="${pictures.pic}"/>').css({'top':top, 'left':left});
							});
							$('#<c:out value="${pictures.pic}"/>').mouseleave(function () {
								$('#shareit-field').val('');
								$(this).hide();
							});
						});
					</script>
					
			</c:forEach>
		</div>
		<!-- /Images list -->

	</div>
</body>
</html>