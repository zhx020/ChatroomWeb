<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>修改帖子</title>
</head>
<link rel="stylesheet" href="${contextPath}/postBar-web/css/jquery.mobile-1.4.5.min.css">
<script src="${contextPath}/postBar-web/js/jquery.min.js"></script>
<script src="${contextPath}/postBar-web/js/jquery.mobile-1.4.5.min.js"></script>
<body>
	<div data-role="page">
	  <div data-role="main" class="ui-content">
	    <form>
	      <label for="name">Message Name</label>
	      <input type="text" name="name" id="name" value="${message.name}" placeholder="Message Name">
	      <label for="name">Message Content</label>
	      <input type="text" name="content" id="content" value="${message.content}" placeholder="Message Content">
	      
	      <input type="hidden" name="id" id="id" value="${message.id}">
	      
	      <input type="button" onclick="clearInputs()" data-inline="true" value="Reset">
	      <input type="button" onclick="update()" data-inline="true" value="Submit">
	    </form>
	  </div>
	</div>
</body>
<script type="text/javascript">
	function clearInputs(){
		var name = $("#name")[0];
		name.value = "";
		var content = $("#content")[0];
		content.value = "";
	};
	function update(){
		var name = $("#name")[0];
		var nameValue = name.value;
		var id = $("#id")[0];
		var idValue = id.value;
		var content = $("#content")[0];
		var contentValue = content.value;
		$.ajax({
			 type: 'PUT',
			 url: "/postBar-web/message/"+idValue+ "?name="+nameValue+"&content="+contentValue,
			 success:function(result){
				 window.location.href="/postBar-web/message"
			 }
		});
	}
</script>
</html>