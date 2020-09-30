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
	<form class="form-horizontal" role="form" action="/postBar-web/chatRoom" method="post">
		<div class="form-group">
			<label for="name" class="col-sm-2 control-label">chatroom name</label>
			<div class="col-sm-3">
				<input type="text" class="form-control" id="name" name="name"
					placeholder="Please enter a name for chatroom">
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-1">
				<button type="reset" class="btn btn-cancel">reset</button>
			</div>
			<div class="col-sm-offset-1 col-sm-1">
				<button type="submit" class="btn btn-default">submit</button>
			</div>
		</div>
	</form>

</body>
<script type="text/javascript">
</script>
</html>