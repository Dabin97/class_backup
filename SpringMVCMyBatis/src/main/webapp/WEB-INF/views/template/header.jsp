<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 
	메뉴
		홈 	회원목록	회원추가 	등급관리
		
	html/css 처리
-->
<style>
	.main_menu{
		list-style-type: none;
		display: flex;
		flex-direction: row;
		justify-content: center;
		width: 1200px;
		margin:0 auto;
		border:1px solid black;
	}
	li{
		width: 200px;
	}
	.main_menu a{
		color : black;
		text-decoration: none;
		font-size: 20px;
		text-align: center;
		display: inline-block;
		width: 100%;
		padding:20px 0px;
	}
</style>
<ul class="main_menu">
  <li><a href="/">홈</a></li>
  <li><a href="/main">회원목록</a></li>
  <li><a href="/register/view">회원추가</a></li>
  <li><a href="/grade/view">등급관리</a></li>
</ul>
<!-- 
	로그인 정보가 없거나 또는 false이면 index.jsp로 이동하게끔 처리
	이동하기 전에는 반드시 '로그인 정보가 없습니다. 로그인 페이지로 이동합니다.'
	경고창으로 출력 후 이동 
 -->
 <%
 	if(session.getAttribute("dto") == null){
 %>
 		<script>
 			alert('로그인정보가 없습니다. 로그인 페이지로 이동합니다.');
 			location.href = '/';
 		</script>
 <%
 	}
 %>














