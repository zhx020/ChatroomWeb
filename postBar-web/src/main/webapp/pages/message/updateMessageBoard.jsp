<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>聊天室</title>
</head>
<link rel="stylesheet" href="${contextPath}/postBar-web/css/jquery.mobile-1.4.5.min.css">
<script src="${contextPath}/postBar-web/js/jquery.min.js"></script>
<script src="${contextPath}/postBar-web/js/jquery.mobile-1.4.5.min.js"></script>
<body>
	<div data-role="page">
	  <div data-role="main" class="ui-content">
	    <form>
	      <label for="name">MessageBoard Name</label>
	      <input type="text" name="name" id="name" value="${messageBoard.name}" placeholder="MessageBoard Name">
	      <input type="hidden" name="id" id="id" value="${messageBoard.id}">
	      <input type="button" onclick="clearName()" data-inline="true" value="reset">
	      <input type="button" onclick="update()" data-inline="true" value="Submit">
	    </form>
	  </div>
	</div>
</body>
<script type="text/javascript">
	function clearName(){
		var name = $("#name")[0];
		name.value = "";
	};
	function update(){
		var name = $("#name")[0];
		var nameValue = name.value;
		var id = $("#id")[0];
		var idValue = id.value;
		$.ajax({
			 type: 'PUT',
			 url: "/postBar-web/messageBoard/"+idValue+ "?name="+nameValue,
			 success:function(result){
				 window.location.href="/postBar-web/messageBoard"
			 }
		});
	}
</script>
</html>