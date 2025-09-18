<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>	
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
<link href="<c:url value="/promotion/css/table.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/config.css" />" rel="stylesheet"> 
 
</head>
<div id="service-dashboard">
	<p class="header"><b>LANGUAGES</b></p>
	<hr>
	<p class="add-button"><img class="plus-icon" src="<c:url value="/resources/css/image/icon_plus.gif" />"/><a href="<%= request.getContextPath() %>/newLanguage">Add New language</a></p> 
	
		<c:if test="${empty languageList}"> 
 			<p>No languages added yet. <a>Click here to add a new language.</a></p> 
 		</c:if>  
 		<c:if test="${not empty languageList}">
 		<table class="service-list-table">
		<tr>
			<th>Language Name</th>
			<th>data Coding</th>
			<th>service Type</th>
			<th>encoding</th>
			<th>script</th>
		</tr>
		<c:forEach items="${languageList}" var="language">
	
			
		<tr class="tbl-row">
			
			<td><a href="<%= request.getContextPath() %>/languageInfo?languagename=${language.language}">
			<c:choose>
			
			<c:when test="${language.language eq '_F'}">
			<c:out value="French"/></a>
			</c:when>
			<c:when test="${language.language eq '_S'}">
			<c:out value="Spanish"/></a>
			</c:when>
			<c:when test ="${language.language eq '_A'}">
			<c:out value="Arabic"/></a>
			</c:when>
			<c:when test="${language.language eq '_P'}">
			<c:out value="Portugese"/></a>
			</c:when>
			<c:when test="${language.language eq '_E'}">
			<c:out value="English"/></a>
			</c:when>
			<c:otherwise>
			<c:out value="${language.language}"/></a>
			</c:otherwise>
			</c:choose>
			</td>
			<td><c:out value="${language.dataCoding}"/></td>
			<td><c:out value="${language.serviceType}"/></td>
			<td><c:out value="${language.encoding}"/></td>	
			<td><c:out value="${language.script}"/></td>	
		</tr>
		</c:forEach>
		</table>
		</c:if>
	
</div>
</html>