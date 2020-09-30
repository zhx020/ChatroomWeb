<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Add MessageBoard</title>
</head>
<link rel="stylesheet" href="${contextPath}/postBar-web/css/jquery.mobile-1.4.5.min.css">
<script src="${contextPath}/postBar-web/js/jquery.min.js"></script>
<script src="${contextPath}/postBar-web/js/jquery.mobile-1.4.5.min.js"></script>
<body>
	<div data-role="page">
	  <div data-role="main" class="ui-content">
	    <form method="post" action="/postBar-web/messageBoard">
	      <label for="name">MessageBoard Name</label>
	      <input type="text" name="name" id="name" placeholder="MessageBoard Name">
	      <input type="reset" data-inline="true" value="Reset">
	      <input type="button" class="ui-btn" onclick="onSubmit()" data-inline="true" value="Submit">
	    </form>
	  </div>
	</div>
</body>
<script type="text/javascript">
	function onSubmit(){
		var name = $("#name")[0];
		var nameValue = name.value;
		$.ajax({
			 type: 'POST',
			 url: "/postBar-web/messageBoard",
		     data:{"name":nameValue},
			 success:function(result){
				 window.location.href="/postBar-web/messageBoard"
			 }
		});
	}
</script>
</html>