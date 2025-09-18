<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib uri = "http://www.springframework.org/tags/form" prefix = "form"%>

<html>
<head>
 <link href="<c:url value="/resources/css/table.css" />" rel="stylesheet">
<link href="<c:url value="/promotion/css/table.css" />" rel="stylesheet">
 
</head>


<div id="Languageform-dashboard" align="center">
	<p class="header"><strong>Add New Language</strong></p>
	
	<hr>
	
	<%-- <form:form action="<%=request.getContextPath()%>/addLanguage" method="post" id="myForm"> --%>
		 <form:form action="./addLanguage" method="post" id="myForm" commandName="language">
		<table >
			<th colspan="2" style="text-align:center">Fill the details to add new Language</th>
			<tr
				title="Please select the language name from the list. This name is used for reports.">
				<td>Language name:</td>
				<td>
				<input id="LanguageName" type="text" name="language" pattern="[_A-Z]{1,2}" />
				</td>
			</tr>
			<tr id="dataCoding"  title="Please fill the dataCoding here.">
				<td>dataCoding:</td>
				<td>
 					<input type="text" id ="dataCoding" name="dataCoding"  pattern="[\d]{1}" value="8" />
 				</td> 
			</tr>
			<!-- <tr
				title="Please fill the serviceType here">
				<td>Service Type:</td>
				<td><input type="text" id="serviceType" name="serviceType" value="CMT"
					pattern="[A-Z]{1,3}" required /></td>
				
			</tr> -->
			
			 <tr
				title="Please fill the serviceType here">
				<td>Service Type:</td>
				<td><form:select path="serviceType">
		 			<form:option value="" label="--SELECT SERVICE TYPE--" />
 					<form:option value="CMT" label="CMT" />
 					<form:option value="USSD" label="USSD" />
 					</form:select>
			</td>
			
			 <tr
				title="Please fill a encoding for the Language. Ex. UTF -8,UTF-16BE">
				<td>Encoding:</td>
				<td><input type="radio" id="encoding" name="encoding" value="true" checked required />True
					<input type="radio" id="encoding" name="encoding" value="false" required />False
				</td>
			</tr> 
			
			<!-- <tr
				title="Please fill a encoding for the Language. Ex. UTF -8,UTF-16BE">
				<td>Encoding:</td>
				<td><input type="text" id="encoding" name="encoding"
					pattern="[a-z]{1,25}" required /></td>
			</tr> -->
			<tr title="This shortcode is used for sending SMS.">
				<td>Script:</td>
				<td>
					<form:select path="script">
		 			<form:option value="" label="--SELECT SCRIPT--" />
 					<form:option value="0" label="UNKNOWN-0" />
 					<form:option value="1" label="ARABIC-1" />
 					<form:option value="2" label="LATIN-2" />
 					</form:select>
			 </td>
			</tr>
		</table>
		<button type="submit" style="background:black;color:#FFFFCC;font-size:15px;margin-top:5px;padding:7px;" id="newlanguagesubmit">Submit</button>
	</form:form>
</div>
