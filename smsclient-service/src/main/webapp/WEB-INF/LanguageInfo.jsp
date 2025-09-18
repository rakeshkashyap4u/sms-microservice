<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>	
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<link rel="stylesheet" href="css/config.css"/>
<link href="<c:url value="/promotion/css/table.css" />" rel="stylesheet">
<link href="<c:url value="/promotion/css/config.css" />" rel="stylesheet">
<div id="edit">
<c:forEach items="${LanguageList}" var="languageList">
	 <c:set var="continueloop" value="true"/>
	<c:if test="${languageList.language ne languagename and continueloop eq true}">
	   <c:set var="continueloop" value="false"/>
	</c:if>

	
<div id="edit1" align="center">
<c:if test="${continueloop}">
<form action="<%= request.getContextPath() %>/editLanguage" method="post" >
				 <div id="${languageList.language}" > 
				<table>
				<th colspan="2" style="text-align:center">Edit Language</th>
					<tr style="display:none;">
						<td>ID:</td>
						<td><input type="text" value="${languageList.lid}" name="lid"/>
					</tr>
					<tr>
						<td>language Name:</td>
						<%-- <td class="view"><c:out value="${languageList.language}"/></td> --%>
						<td class="row"><input type="text" value="${languageList.language}" id="languageName" name="languageName"  title="languageName"  required /></td>
					</tr>
					<tr>
						<td>language data coding:</td>
						<%-- <td class="view"><c:out value="${languageList.dataCoding}"/></td> --%>
						<td class="row"><input type="text" value="${languageList.dataCoding}" id="dataCoding" name="dataCoding" pattern="[0-9]*"title="dataCoding" required /></td>
					</tr>
					<tr>
						<td>Service type:</td>
						<%-- <td class="view"><c:out value="${languageList.serviceType}"/></td> --%>
						<td class="row"><input type="text" value="${languageList.serviceType}" id="serviceType" name="serviceType" pattern="[A-Z]*" title="serviceType" required /></td>
					</tr>
					<tr>
						<td>encoding:</td>
						<%-- <td class="view"><c:out value="${languageList.encoding}"/></td> --%>
						<td class="row"><input type="text" value="${languageList.encoding}" id="encoding" name="encoding" title="encoding" required /></td>
					</tr>
					<tr>
						<td>script:</td>
						<%-- <td class="view"><c:out value="${languageList.script}"/></td> --%>
						<td class="row"><input type="text" value="${languageList.script}" id="script" name="script" pattern="[0-9]*" title="script" required /></td>
					</tr>
				
				</table>
				 <input type="submit" name="Edit" />
				
			</form>
			</c:if>
			</div>
			
</c:forEach>
</div>