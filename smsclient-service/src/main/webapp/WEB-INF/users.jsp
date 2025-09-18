<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Users</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
</head>
<body onload="load();">
		callerId: <input type="text" id="callerID" required="required" name="callerId"><br>
		operator: <input type="text" id="operator" required="required" name="operator"><br>
		protocol: <input type="text" id="protocol" required="required" name="protocol"><br>
		country: <input type="text" id="country" required="required" name="country"><br>
		countryCodes:<input type="text" id="countryCodes" required="required" name="countryCodes"><br>
		msisdnLength:<input type="text" id="msisdnLength" required="required" name="msisdnLength"><br>
		
		
		<button onclick="submit();">Submit</button>
	
		<table id="table" border=1>
			<tr> <th> key</th> <th> value </th> </tr> 
		
		</table>
		
		
		
		
		
		
				
	<script type="text/javascript">
	data = "";
	submit = function(){
		 
			$.ajax({
				url:'saveOrUpdate',
				type:'POST',
				data:{callerID:$("#callerID ").val(),operator:$('#operator').val(),protocol:$('#protocol').val(),country:$('#country'),countryCodes:$('#countryCodes'),msisdnLength:$('#msisdnLength')},
				success: function(response){
						alert(response.message);
						load();		
				}				
			});			
	}
	
	delete_ = function(id){		 
		 $.ajax({
			url:'delete',
			type:'POST',
			data:{callerId:id},
			success: function(response){
					alert(response.message);
					load();
			}				
		});
}
	

	edit = function (index){
		$("#callerId").val(data[index].callerId);
		$("#operator").val(data[index].operator);
		$("#protocol").val(data[index].protocol);
		$("#country").val(data[index].country);
		$("#countryCodes").val(data[index].countryCodes);
		$("#msisdnLength").val(data[index].msisdnLength);
		
		
	}

	
	</script>
	
</body>
</html>
