<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Messages</title>
</head>
<link rel="stylesheet" href="${contextPath}/postBar-web/css/jquery.mobile-1.4.5.min.css">
<script src="${contextPath}/postBar-web/js/jquery.min.js"></script>
<script src="${contextPath}/postBar-web/js/jquery.mobile-1.4.5.min.js"></script>
<body>
	<div data-role="page" id="pageone">
		<div data-role="main" class="ui-content">
			<table data-role="table" data-mode="columntoggle" class="ui-responsive" id="myTable">
				<thead>
					<tr>
						<th data-priority="1">Message Name</th>
						<th data-priority="2">Message Content</th>
						<th data-priority="3">Message Board Name</th>
						<th data-priority="4">Publish Date</th>
						<th data-priority="5">Operation</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${messages}" var="item">
		            	<tr>
		            		<td>${item.name}</td>
		            		<td>${item.content}</td>
		            		<td>${item.boardName}</td>
		            		<td>${item.publishDate}</td>
		            		<td>
		            			<div data-role="controlgroup" data-type="horizontal">
			            			<button class="ui-btn" onclick="delMessage('${item.id}')">DELETE</button>
			            			<button class="ui-btn" onclick="updMessage('${item.id}')">UPDATE</button>
		            			</div>
		            		</td>
		            		<td>
            		</td>
		            	</tr>
		        	</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
<script type="text/javascript">
	function delMessage(id){
		$.ajax({
			 type: 'DELETE',
			 url: "message/"+id,
			 success:function(result){
				 window.location.reload();
			 }
		});
	};
	function updMessage(id){
		window.location.href="message/toUpdate/"+id;
	};
</script>
</html>