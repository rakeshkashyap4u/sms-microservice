<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri = "http://www.springframework.org/tags/form" prefix = "form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link href="<c:url value="/resources/css/config.css" />" rel="stylesheet">
<link href="<c:url value="/promotion/css/table.css" />" rel="stylesheet">
</head>
<body>

<%-- <form  method="POST" action="<%= request.getContextPath() %>/updateConfig" modelattribute="All"> --%>
<form:form method="POST"  action="./updateConfig" commandName="all">
<h1><span>Edit SMS Configuration</span></h1>
	<c:forEach items="${Configuration}" var="config">
		
		<c:set var="continueloop" value="true" />
			<c:if test="${config.circle ne circlename and continueloop eq true}">
			
			 <c:set var="continueloop" value="false" />
			</c:if>
			
			
		<c:if test ="${continueloop}" >		
			<c:set var="opId" value="${config.opId}" />
			<div id="${circlename}" > 
				<table>
					<tr style="display:none;">
						<td>ID:</td>
						<td><input type="text" name= "cid" value="${config.cid}" name="cid"/>
					</tr>
					<tr style="display:none;">
						<td>ID:</td>
						<td><input type="text" name="opId" value="${config.opId}" name="opId"/>
					</tr>
					<tr>
						<td>Circle:</td>
							<td class="row"><input type="text" value="${config.circle}" id="circle" name="circle"  title="circle"  required /></td>
					</tr>
					<tr>
						<td>serverIP:</td>
						<td class="row"><input type="text" value="${config.serverIp}" id="serverIp" name="serverIp" title="serverIp" required /></td>
					</tr>
					<tr>
						<td>serverPort:</td>
						<td class="row"><input type="text" value="${config.serverPort}" id="serverPort" name="serverPort"  title="serverPort" required /></td>
					</tr>
					<tr>
						<td>userid:</td>
						<td class="row"><input type="text" value="${config.userid}" id="userid" name="userid" title="userid" required /></td>
					</tr>
					<tr>
						<td>password:</td>
						<td class="row"><input type="text" value="${config.password}" id="password" name="password"  title="password" required /></td>
					</tr>
					<tr>
						 <td><form:label cssClass="BindMode" cssErrorClass="BindMode error" path="bindMode">
							BindMode:
						 </form:label></td>
						 
						 <td>
						 <form:select path="bindMode">
						 	<c:choose>
						 	<c:when test="${config.bindMode eq '0'}">
								<form:option value="0" selected="selected">MT</form:option>
								<form:option value="1">MO</form:option>
								<form:option value="2">MT-MO</form:option>
							</c:when>
							<c:when test="${config.bindMode eq '1'}">
								<form:option value="0">MT</form:option>
								<form:option value="1" selected="selected">MO</form:option>
								<form:option value="2">MT-MO</form:option>
							</c:when>
							<c:when test="${config.bindMode eq '2'}">
								<form:option value="0">MT</form:option>
								<form:option value="1">MO</form:option>
								<form:option value="2" selected="selected">MT-MO</form:option>
							</c:when>
						 	</c:choose>
						 </form:select>
						 </td>
						<%-- <td>bindMode:</td>
						<td class="row"><input type="text" value="${config.bindMode}" id="bindMode" name="bindMode" pattern="[0-9]*" title="bindMode" required /></td> --%>
					</tr>
					
					
					<tr>
					
						<td>serviceURI:</td>
						<td class="row"><input type="text" value="${config.serviceUri}" id="serviceUri" name="serviceUri"  title="serviceUri"  /></td>
					</tr>
					
					
					
					
				
				</table>
			</div>
	</c:if>
	</c:forEach>
		<c:forEach items="${Details}" var="detail">
			<c:set var="loop" value="false"/>
			<c:if test="${detail.id eq opId  and loop eq false}">
				<c:set var="loop" value="true"/>
			</c:if>
			<c:if test="${loop}">
			<div id="${opId}" > 
				<table>
					<tr>
						<td>Operator:</td>
						<td class="row"><input type="text" value="${detail.operator}" id="operator" name="operator"  title="operator" required /></td>
					</tr>
					<tr>
						<td>Country:</td>
						<td class="row"><input type="text" value="${detail.country}" id="country" name="country"  title="country" required /></td>
					</tr>
					<tr>
						<td>Protocol:</td>
						<td class="row"><input type="text" value="${detail.protocol}" id="protocol" name="protocol"  title="protocol" required /></td>
					</tr>
				
				</table>
			</div>
		</c:if>
		</c:forEach> 
		<input type="submit" value="update" class="button"/>
				</form:form>

</body>
</html>