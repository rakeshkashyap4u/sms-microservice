<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@taglib uri = "http://www.springframework.org/tags/form" prefix = "form"%>
   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
 <link href='http://fonts.googleapis.com/css?family=Bitter' rel='stylesheet' type='text/css'>
<link href="<c:url value="/promotion/css/table.css" />" rel="stylesheet"> 
<script src="more/js/jquery-1.9.1.js"></script>
<style type="text/css">
.form-style-10{
    width:450px;
    padding:30px;
    margin:40px auto;
    background: #ffffff;
    border-radius: 10px;
    -webkit-border-radius:10px;
    -moz-border-radius: 10px;
    box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.13);
    -moz-box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.13);
    -webkit-box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.13);
}
.form-style-10 .inner-wrap{
    padding: 30px;
    background: #F8F8F8;
    border-radius: 6px;
    margin-bottom: 15px;
}
.form-style-10 h1{
    background: black;
    padding: 20px 30px 15px 30px;
    margin: -30px -30px 30px -30px;
    border-radius: 10px 10px 0 0;
    -webkit-border-radius: 10px 10px 0 0;
    -moz-border-radius: 10px 10px 0 0;
    color: #fff;
    text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.12);
    font: normal 30px 'Bitter', serif;
    -moz-box-shadow: inset 0px 2px 2px 0px rgba(255, 255, 255, 0.17);
    -webkit-box-shadow: inset 0px 2px 2px 0px rgba(255, 255, 255, 0.17);
    box-shadow: inset 0px 2px 2px 0px rgba(255, 255, 255, 0.17);
    border: 1px solid #257C9E;
}
.form-style-10 h1 > span{
    display: block;
    margin-top: 2px;
    font: 13px Arial, Helvetica, sans-serif;
}
.form-style-10 label{
    display: block;
    font: 13px Arial, Helvetica, sans-serif;
    color: #888;
    margin-bottom: 15px;
}
.form-style-10 input[type="text"],
.form-style-10 input[type="date"],
.form-style-10 input[type="datetime"],
.form-style-10 input[type="email"],
.form-style-10 input[type="number"],
.form-style-10 input[type="search"],
.form-style-10 input[type="time"],
.form-style-10 input[type="url"],
.form-style-10 input[type="password"],
.form-style-10 textarea,
.form-style-10 select {
    display: block;
    box-sizing: border-box;
    -webkit-box-sizing: border-box;
    -moz-box-sizing: border-box;
    width: 100%;
    padding: 8px;
    border-radius: 6px;
    -webkit-border-radius:6px;
    -moz-border-radius:6px;
    border: 2px solid #fff;
    box-shadow: inset 0px 1px 1px rgba(0, 0, 0, 0.33);
    -moz-box-shadow: inset 0px 1px 1px rgba(0, 0, 0, 0.33);
    -webkit-box-shadow: inset 0px 1px 1px rgba(0, 0, 0, 0.33);
}

.form-style-10 .section{
    font: normal 20px 'Bitter', serif;
    color: black;
    margin-bottom: 5px;
}
.form-style-10 .section span {
    background: black;
    padding: 5px 10px 5px 10px;
    position: absolute;
    border-radius: 50%;
    -webkit-border-radius: 50%;
    -moz-border-radius: 50%;
    border: 4px solid #fff;
    font-size: 14px;
    margin-left: -45px;
    color: #fff;
    margin-top: -3px;
}
.form-style-10 input[type="button"], 
.form-style-10 input[type="submit"]{
    background:black;
    padding: 8px 20px 8px 20px;
    border-radius: 5px;
    -webkit-border-radius: 5px;
    -moz-border-radius: 5px;
    color: #fff;
    text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.12);
    font: normal 30px 'Bitter', serif;
    -moz-box-shadow: inset 0px 2px 2px 0px rgba(255, 255, 255, 0.17);
    -webkit-box-shadow: inset 0px 2px 2px 0px rgba(255, 255, 255, 0.17);
    box-shadow: inset 0px 2px 2px 0px rgba(255, 255, 255, 0.17);
    border: 1px solid #257C9E;
    font-size: 15px;
}
.form-style-10 input[type="button"]:hover, 
.form-style-10 input[type="submit"]:hover{
    background: #2A6881;
    -moz-box-shadow: inset 0px 2px 2px 0px rgba(255, 255, 255, 0.28);
    -webkit-box-shadow: inset 0px 2px 2px 0px rgba(255, 255, 255, 0.28);
    box-shadow: inset 0px 2px 2px 0px rgba(255, 255, 255, 0.28);
}
.form-style-10 .privacy-policy{
    float: right;
    width: 250px;
    font: 12px Arial, Helvetica, sans-serif;
    color: #4D4D4D;
    margin-top: 10px;
    text-align: right;
}
</style>

<script type="text/javascript">

$(document).ready(function(){
    $('#protocolValue').on('change', function() {
    	
      if ( this.value == 'SMPP')
      {
    	 $("#uri").hide();
      }
      else
      {
        $("#uri").show();
      }
    });
});


</script>


</head>
<body>
<div class="form-style-10" style="color:yellow;">
<h1>SMSC Configuration<span>Configure SMS Configuration here!</span></h1>

<form:form method="POST"  commandName="All" action="./save">

    <div class="section"><span>1</span>SMSC IP Configuration &amp; </div>
    <div class="inner-wrap">
    	 <form:label cssClass="protocol" cssErrorClass="protocol error" path="protocol">
        Protocol:
        </form:label>
        
        <form:select path="protocol" id="protocolValue">
		 <form:option value="" label="--SELECT PROTOCOL--" />
 		<form:options items="${protocolList}"/>
		 </form:select>
		 
		 
        <label>circle <input type="text" name="circle" /></label>
        <label>Server IP <input type="text" name="serverIp" /></label>
        <label>Port <input type="text" name="serverPort" /></label>
        <div id='uri' style='display:none;'>
        <label>serviceURI<input type="text" name="serviceUri" /></label>
        </div>
        <label>User Id<input type="text" name="userid" /></label>
        <label>Password<input type="text" name="password" /></label>
       
      </div>
      <div class="form-style-10">
	<div class="section"><span>2</span>SMSC Details Configuration &amp;</div>
    <div class="inner-wrap">
    	 <form:label cssClass="BindMode" cssErrorClass="BindMode error" path="bindMode">
        BindMode:
        </form:label>
        <form:select path="bindMode">
 		<form:option value="${BindMode}" label="--SELECT BIND MODE--" />
		 <form:options items="${BindMode}"/>
 		</form:select> 
    
    
        <form:label cssClass="operator" cssErrorClass="operator error" path="operator">
        Operator:
        </form:label>
        <form:select path="operator">
 		<form:option value="" label="--SELECT OPERATOR--" />
		 <form:options items="${operatorList}" itemLabel="operatorName" itemValue="operatorName" />
 		</form:select>
        
        <form:label cssClass="country" cssErrorClass="country error" path="country">
        Country:
        </form:label>
        
        <form:select path="country">
		 <form:option value="" label="--SELECT COUNTRY--" />
 		<form:options items="${countryList}" itemLabel="countryName" itemValue="countryName" />
		 </form:select>
	</div>
	
	<%-- <div class="form-style-11">
	 <div class="section"><span>2</span>SMSC Formats Configuration&amp;</div>
    <div class="inner-wrap">
    	 <form:label cssClass="requestFormat"  path="requestFormat">
        requestFormat:
        </form:label>
        <form:textarea path="requestFormat" />
 			
 		
 		
 		<form:label cssClass="requestFormat"  path="responseFormat">
        responseFormat:
        </form:label>
        <form:textarea path="responseFormat" />
 	
 		
 		<form:select path="mode">
 		<form:option value="" label="--SELECT MODE--" />
		 <form:option value="GET"/>
		 <form:option value="POST"/>
 		</form:select>  
    
	</div>
	 --%>
    <div class="button-section">
     <input type="submit" name="Sign Up" />
    
    </div>
</form:form>

</div>


</body>
</html>