<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Add Message</title>
</head>
<link rel="stylesheet" href="${contextPath}/postBar-web/css/jquery.mobile-1.4.5.min.css">
<script src="${contextPath}/postBar-web/js/jquery.min.js"></script>
<script src="${contextPath}/postBar-web/js/jquery.mobile-1.4.5.min.js"></script>
<body>
	<div data-role="page">
	  <div data-role="main" class="ui-content">
	    <form action="/postBar-web/message" method="post">
	      <label for="name">Message Name</label>
	      <input type="text" name="name" id="name" placeholder="Message Name">
	      
	      <label for="content">Message Content</label>
	      <input type="text" name="content" id="content" placeholder="Message Content">
	      
	      <input type="hidden" name="boardId" id="boardId" value="${param.boardId}">
	      
	      <input type="button" class="ui-btn" onclick="clearInputs()" data-inline="true" value="Reset">
	      <input type="button" class="ui-btn" onclick="onSubmit()"data-inline="true" value="Submit">
	    </form>
	  </div>
	</div>
</body>
<script type="text/javascript">
function clearInputs(){
	var content = $("#content")[0];
	content.value = "";
	var name = $("#name")[0];
	name.value = "";
};
function onSubmit(){
	var name = $("#name")[0];
	var nameValue = name.value;
	var content = $("#content")[0];
	var contentValue = content.value;
	var boardId = $("#boardId")[0];
	var boardIdValue = boardId.value;
	$.ajax({
		 type: 'POST',
		 url: "/postBar-web/message",
	     data:{"name":nameValue,"content":contentValue,"boardId":boardIdValue},
		 success:function(result){
			 window.location.href="/postBar-web/messageBoard"
		 }
	});
}
</script>
</html>