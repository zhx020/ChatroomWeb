<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Chatroom</title>
</head>
<link rel="stylesheet" href="${contextPath}/postBar-web/css/bootstrap.min.css">
<script src="${contextPath}/postBar-web/js/jquery.min.js"></script>
<script src="${contextPath}/postBar-web/js/bootstrap.min.js"></script>
<body>
	<!-- 增删改查的列表 -->
	<button onclick="addChatRoom()">add chatroom</button>
	
	<!-- 增删改查的列表 -->
	time interval:<input type="text" id="begDate" value="${param.begDate}">
	to<input type="text" id="endDate" value="${param.endDate}">
	<button onclick="onQuery()">search</button>
	<br>
	<br>
	<table class="table">
		<thead>
			<tr>
				<th>chatroom name</th>
				<th>modified or not</th>
				<th>number of posts</th>
				<th>total click</th>
				<th>action</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${chatRooms}" var="item">
            	<tr>
            		<td>${item.name}</td>
            		<td>${item.isUpdate}</td>
            		<td>${item.postCount}</td>
            		<td>${item.clickTimes}</td>
            		<td>
            			<button onclick="delChatRoom('${item.id}')">delete</button>
            			<button onclick="updChatRoom('${item.id}')">update</button>
            			<button onclick="addPost('${item.id}')">add post</button>
            			<button onclick="viewPosts('${item.id}')">view post</button>
            		</td>
            	</tr>
        	</c:forEach>
		</tbody>
	</table>
</body>
<script type="text/javascript">
	//var chatRooms = ${chatRooms};
	function addChatRoom(){
		window.location.href="pages/addChatRoom.jsp";
	};
	function delChatRoom(id){
		$.ajax({
			 type: 'DELETE',
			 url: "chatRoom/"+id,
			 success:function(result){
				 window.location.reload();
			 }
		});
	};
	function updChatRoom(id){
		window.location.href="chatRoom/toUpdate/"+id;
	};
	function addPost(id){
		window.location.href="pages/addPost.jsp?roomId="+id;
	};
	function viewPosts(id){
		window.location.href="post?roomId=" + id;
	};
	function onQuery(){
		var begDate = $("#begDate")[0];
		var begDateValue = begDate.value;
		var endDate = $("#endDate")[0];
		var endDateValue = endDate.value;
		window.location.href="chatRoom?begDate=" + begDateValue + "&endDate="+endDateValue;
	};
</script>
</html>