<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>modify post</title>
</head>
<link rel="stylesheet" href="${contextPath}/postBar-web/css/bootstrap.min.css">
<script src="${contextPath}/postBar-web/js/jquery.min.js"></script>
<script src="${contextPath}/postBar-web/js/bootstrap.min.js"></script>
<body>
	<form class="form-horizontal" role="form" action="../chatRoom" method="put">
		<div class="form-group">
			<label for="name" class="col-sm-2 control-label">post name</label>
			<div class="col-sm-3">
				<input type="text" class="form-control" id="name" name="name" value="${post.name}"
					placeholder="Please enter a name for post">
				<input type="hidden" name="id" id="id" value="${post.id}">
			</div>
		</div>
		<div class="form-group">
			<label for="content" class="col-sm-2 control-label">post content</label>
			<div class="col-sm-3">
				<input type="text" class="form-control" id="content" name="content" value="${post.content}"
					placeholder="Please enter content">
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-1">
				<button type="button" onclick="clearInputs()" class="btn btn-default">reset</button>
			</div>
			<div class="col-sm-offset-1 col-sm-1">
				<button type="button"  onclick="update()" class="btn btn-default">update</button>
			</div>
		</div>
	</form>

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
			 url: "../"+idValue+ "?name="+nameValue+"&content="+contentValue,
			 success:function(result){
				 window.location.href="/postBar-web/post"
			 }
		});
	}
</script>
</html>