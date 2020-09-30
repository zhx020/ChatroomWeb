<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>帖子</title>
</head>
<link rel="stylesheet" href="${contextPath}/postBar-web/css/bootstrap.min.css">
<script src="${contextPath}/postBar-web/js/jquery.min.js"></script>
<script src="${contextPath}/postBar-web/js/bootstrap.min.js"></script>
<body>
	<!-- 增删改查的列表 -->
	<!-- 日期:<input type="text" id="begDate">
	至<input type="text" id="endDate">
	<button onclick="onQuery()">发布帖子</button>
	<br> -->
	<table class="table">
		<thead>
			<tr>
				<th>post name</th>
				<th>post content</th>
				<th>belong to which room</th>
				<th>post time</th>
				<th>click time</th>
				<th>action</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${posts}" var="item">
            	<tr>
            		<td><a href="post/toDeail/${item.id}">${item.name}</a></td>
            		<td>${item.content}</td>
            		<td>${item.roomName}</td>
            		<td>${item.publishDate}</td>
            		<td>${item.clickTimes}</td>
            		<td>
            			<button onclick="delPost('${item.id}')">delete</button>
            			<button onclick="updPost('${item.id}')">update</button>
            		</td>
            	</tr>
        	</c:forEach>
		</tbody>
	</table>
</body>
<script type="text/javascript">
	function delPost(id){
		$.ajax({
			 type: 'DELETE',
			 url: "post/"+id,
			 success:function(result){
				 window.location.reload();
			 }
		});
	};
	function updPost(id){
		window.location.href="post/toUpdate/"+id;
	};
</script>
</html>