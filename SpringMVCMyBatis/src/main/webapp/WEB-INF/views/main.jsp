<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	table {
		border-collapse: collapse;
		margin:30px auto;
	}
	td, th{
		padding: 10px;
		font-size: 1.3em;
		border : 1px solid black;
		text-align: center;
	}
</style>
<script src="js/jquery-3.6.3.js"></script>
<script>
	function delete_member(){
		let id = $(this).parent().parent().children().first().text();
		let obj = $(this).parent().parent();
		let param = "id="+id;
		$.ajax({
			url : '/delete', //PathVariable을 사용하면 param을 보내지않아도 된다.
			type : 'post',
			dataType:'json',
			success:function(r){
				console.log(r);
				alert(r.message);//삭제 결과를 경고창으로 출력
				//회원 목록 표에서 삭제한 데이터를 제거
				if(r.count == '1'){
					//location.reload();//새로고침
					$(obj).remove();//해당 태그 삭제
				}
			}
		});
	}
	$(function() {
		$(".btn_delete").click(delete_member);
		$('.btn_search').click(function(){
			let param = '';
			$('.search_form input,.search_form select').each(function(i, obj){
				console.log($(obj).attr('name'),$(obj).val());
				param += $(obj).attr('name')+"="+$(obj).val() + "&"
			});
			console.log(param);
			//ajax로 search로 param을 전달하고
			//검색 결과를 받아서 tbody에 출력
			$.ajax({
				url : 'search',
				data : param,
				type : 'post',
				dataType : 'json',
				success:function(r){
					console.log(r);
					//결과값 저장할 태그를 조립
					let tag = '';
					for(i=0;i<r.length;i++){
						tag += '<tr>';
//						tag += '<td><a href="detail?id='+r[i].id+'">'+r[i].id+'</a></td>';
						//EL과 백틱을 겹쳐서 쓸때는 \를 추가
						tag += `<td><a href='detail?id=\${r[i].id}'>\${r[i].id}</a></td>`;
						tag += '<td>'+r[i].passwd+'</td>';
						tag += '<td>'+r[i].name+'</td>';
						tag += '<td>'+r[i].nick+'</td>';
						tag += '<td>'+r[i].gradeNo+'</td>';
						tag += '<td><button class="btn_delete">삭제</button></td>';
						tag += '</tr>';
					}
					$('tbody').html(tag);
					//삭제 버튼에 대한 이벤트 처리 진행
					$(".btn_delete").click(delete_member);
				}
			});
		});
		$(".btn_reset").click(function(){
			$('input[name=search]').val('');
			$('.btn_search').click();//클릭이벤트 실행
			//location.reload();//새로고침
		});
	});
</script>
</head>
<body>
	<!-- header.jsp include -->
	<jsp:include page="template/header.jsp"></jsp:include>
	<table>
		<thead>
			<tr>
				<td colspan="6" class='search_form'>
					<select name="kind">
						<option value="id">아이디</option>
						<option value="nick">닉네임</option>
						<option value="name">이름</option>
					</select>
					<input type="text" name="search" placeholder="검색어 입력">
					<button type="button" class="btn_search">검색</button>
					<button type="button" class="btn_reset">검색 초기화</button>
				</td>
			</tr>
			<tr>
				<th>아이디</th>
				<th>암호</th>
				<th>이름</th>
				<th>닉네임</th>
				<th>회원등급</th>
				<th>작업</th>
			</tr>
		</thead>
		<tbody>
			<!-- request 영역에 저장된 list를 출력 -->
			<c:forEach items="${requestScope.list }" var="member">
				<tr>
					<td><a href="detail/${member.id }">${member.id }</a></td> 
					<td>${member.passwd }</td>
					<td>${member.name }</td>
					<td>${member.nick }</td>
					<td>${member.gradeNo }</td>
					<td><button class="btn_delete">삭제</button></td>
				</tr>					
			</c:forEach>
		</tbody>
	</table>
	 
</body>
</html>















