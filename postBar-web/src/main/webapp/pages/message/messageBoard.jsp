<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Message Board</title>
</head>
<link rel="stylesheet" href="${contextPath}/postBar-web/css/jquery.mobile-1.4.5.min.css">
<script src="${contextPath}/postBar-web/js/jquery.min.js"></script>
<script src="${contextPath}/postBar-web/js/jquery.mobile-1.4.5.min.js"></script>
</head>
<body>
	<div data-role="page" id="pageone">
		<div data-role="main" class="ui-content">
			<div data-role="controlgroup" data-type="horizontal">
				<button class="ui-btn" onclick="addMessageBoard()">Add</button>
			</div>
			<p/>
			<table data-role="table" data-mode="columntoggle" class="ui-responsive" id="myTable">
				<thead>
					<tr>
						<th data-priority="1">Message Board Name</th>
						<th data-priority="4">Message Counts</th>
						<th data-priority="5">Operation</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${messageBoards}" var="item">
		            	<tr>
		            		<td>${item.name}</td>
		            		<td>${item.messageCount}</td>
		            		<td>
		            			<div data-role="controlgroup" data-type="horizontal">
			            			<button class="ui-btn" onclick="delMessageBoard('${item.id}')">DELETE</button>
			            			<button class="ui-btn" onclick="updMessageBoard('${item.id}')">UPDATE</button>
			            			<button class="ui-btn" onclick="addMessage('${item.id}')">POST MESSAGE</button>
			            			<button class="ui-btn" onclick="viewMessages('${item.id}')">VIEW MESSAGES</button>
		            			</div>
		            		</td>
		            	</tr>
		        	</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
<script type="text/javascript">
	//var chatRooms = ${chatRooms};
	function addMessageBoard() {
		window.location.href = "pages/message/addMessageBoard.jsp";
	};
	function delMessageBoard(id) {
		$.ajax({
			type : 'DELETE',
			url : "messageBoard/" + id,
			success : function(result) {
				window.location.reload();
			}
		});
	};
	function updMessageBoard(id) {
		window.location.href = "messageBoard/toUpdate/" + id;
	};
	function addMessage(id) {
		window.location.href = "pages/message/addMessage.jsp?boardId=" + id;
	};
	function viewMessages(id) {
		window.location.href = "message?boardId=" + id;
	};
</script>
</html>