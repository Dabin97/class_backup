<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
	<style>
	.container {
		width: 1200px;
		margin: 100px auto;
	}
	
	h2 {
		text-align: center;
	}
	
	input {
		width: 300px;
		height: 40px;
		font-size: 18px;
		outline: none;
		border-radius: 5px;
		box-sizing: border-box;
		margin-bottom: 5px;
		border: 1px solid gray;
	}
	
	button {
		width: 300px;
		height: 40px;
		font-size: 18px;
		background-color: rgb(52, 152, 219);
		outline: none;
		box-sizing: border-box;
		border-radius: 5px;
		border: 1px solid rgb(52, 120, 255);
		color: white;
		font-weight: bold;
	}
	
	form {
		width: 300px;
		display: flex;
		margin: 0 auto;
		flex-direction: column;
	}
	</style>
	
<body>
	<div class="container">
		<h2>회원 관리 시스템</h2>
		<form action="login" method="post">
			<input type="text" name="id" placeholder="아이디를 입력하세요"> 
			<input type="password" name="passwd" placeholder="암호를 입력하세요">
			<button>로그인</button>
		</form>
	</div>
</body>
</html>







