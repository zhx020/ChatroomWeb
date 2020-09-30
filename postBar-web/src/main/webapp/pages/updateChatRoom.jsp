<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>chatroom</title>
</head>
<link rel="stylesheet" href="${contextPath}/postBar-web/css/bootstrap.min.css">
<script src="${contextPath}/postBar-web/js/jquery.min.js"></script>
<script src="${contextPath}/postBar-web/js/bootstrap.min.js"></script>
<body>
	<form class="form-horizontal" role="form" action="../chatRoom" method="put">
		<div class="form-group">
			<label for="name" class="col-sm-2 control-label">chatroom name</label>
			<div class="col-sm-3">
				<input type="text" class="form-control" id="name" name="name" value="${chatRoom.name}"
					placeholder="Please enter a name for chatroom">
				<input type="hidden" name="id" id="id" value="${chatRoom.id}">
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-1">
				<button type="button" onclick="clearName()" class="btn btn-default">reset</button>
			</div>
			<div class="col-sm-offset-1 col-sm-1">
				<button type="button"  onclick="update()" class="btn btn-default">update</button>
			</div>
		</div>
	</form>

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
			 url: "../"+idValue+ "?name="+nameValue,
			 success:function(result){
				 window.location.href="/postBar-web/chatRoom"
			 }
		});
	}
</script>
</html>