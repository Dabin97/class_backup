<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	.container{
		width:1200px;
		margin: 50px auto;	
		font-size: 0px;
	}
	form{
		text-align: center;
	}
	input, select, button{
		width: 200px;
		height : 50px;
		font-size: 1.5rem;
		box-sizing: border-box;
	}
	button{
		display : inline-block;
		box-sizing: border-box;
		font-size: 1.5rem;	
		height : 50px;	
	}
</style>
</head>
<body>
	<jsp:include page="template/header.jsp"></jsp:include>
	<div class="container">
		<form action="/update" method="post">
			<input type="text" name="id" placeholder="아이디 입력" value="${dto.id }" readonly><br>
			<input type="password" name="passwd" placeholder="새 암호 입력" required><br>
			<input type="text" name="name" placeholder="이름 입력" value="${dto.name }"><br>
			<input type="text" name="nick" placeholder="닉네임 입력" value="${dto.nick }"><br>
			<select name="gradeNo">
				<option value="1" ${dto.gradeNo == 1 ? "selected" : ""}>Bronze</option>
				<option value="2" ${dto.gradeNo == 2 ? "selected" : ""}>Silver</option>
				<option value="3" ${dto.gradeNo == 3 ? "selected" : ""}>Gold</option>
				<option value="4" ${dto.gradeNo == 4 ? "selected" : ""}>VIP</option>
				<option value="5" ${dto.gradeNo == 5 ? "selected" : ""}>VVIP</option>
				<option value="6" ${dto.gradeNo == 6 ? "selected" : ""}>Master</option>
			</select><br>
			<button>수정</button>
		</form>
	</div>
</body>
</html>