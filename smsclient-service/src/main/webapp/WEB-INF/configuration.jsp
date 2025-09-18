
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>	
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
 <link href="<c:url value="/promotion/css/table.css" />" rel="stylesheet">
 <link href="<c:url value="/resources/css/config.css" />" rel="stylesheet">
 <style>
 .button {
  display: inline-block;
  padding: 10px 15px;
  font-size: 24px;
  cursor: pointer;
  text-align: center;
  text-decoration: none;
  outline: none;
  color: #D3D3D3;
  background-color: #D3D3D3;
  border: none;
  border-radius: 15px;
  box-shadow: 0 9px #D3D3D3;
}

.button:hover {background-color: #D3D3D3}

.button:active {
  background-color: #D3D3D3;
  box-shadow: 0 5px #666;
  transform: translateY(4px);
}
 
 </style>
 
 </head>
<body>
<div id="service-dashboard">
	<p class="header"><b>SMSC Configuration</b></p>
	<hr>
	<p class="button"><img class="plus-icon" src="<c:url value="/promotion/images/icon_plus.gif" />"/><a href="<%= request.getContextPath() %>/getSmscDetails" style="text-color:#000000">Add New configuration</a></p>


<c:if test="${empty Configuration}"> 
 			<p>No Configuration added yet. <a>Click here to add a new Configuration.</a></p> 
 		</c:if>  
 		<c:if test="${not empty Configuration}">
 		<table class="service-list-table">
		<tr>
			<th>circle</th>
			<th>serverIp</th>
			<th>serverPort</th>
			<th>serviceUri</th>
			<th>userid</th>
			<th>password</th>
			<th>bindMode</th>
			<th>operator</th>
			<th>country</th>
			<th>protocol</th>
		</tr>
		
		 <c:forEach items="${Configuration}" var="Config">
		 	<c:set var="opId" value="${Config.opId}"/>
			<tr class="tbl-row">
			<td><a href="<%= request.getContextPath() %>/ConfigInfo?circle=${Config.circle}"><c:out value="${Config.circle}"/></td>	
			<td><c:out value="${Config.serverIp}"/></td>
			<td><c:out value="${Config.serverPort}"/></td>	
			<td><c:out value="${Config.serviceUri}"/></td>
			<td><c:out value="${Config.userid}"/></td>
			<td><c:out value="${Config.password}"/></td>
			<td><c:out value="${Config.bindMode}"/></td> 
		
		
		<c:forEach items="${detaillist}" var="detail">
			<c:set var="loop" value="false"/>
			<c:if test="${detail.id eq opId  and loop eq false}">
				<c:set var="loop" value="true"/>
			</c:if>
			<c:if test="${loop}">
			<div id="${opId}" > 
			<td><c:out value="${detail.operator}"/></td>
			<td><c:out value="${detail.country}"/></td>	
			<td><c:out value="${detail.protocol}"/></td>
			
			</div>
		</c:if>
		</c:forEach> 
		</tr>
		</c:forEach> 
		
		</table>
		</c:if>
		


</div>
</body>
</html>