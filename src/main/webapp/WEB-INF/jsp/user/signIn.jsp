<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="form-box" class="d-flex justify-content-center align-items-center w-100">
	<form class="signInForm">
		<div class="input-group mb-3">
			<div class="input-group-prepend">
				<span class="input-group-text" id="id-addon">
					<img src="/static/img/userIcon.png" width="30">
				</span>
			</div>
			<input type="text" id="loginId" class="form-control" placeholder="Username">
		</div>
		<div class="input-group mb-3">
			<div class="input-group-prepend">
				<span class="input-group-text" id="password-addon">
					<img src="/static/img/passwordIcon.png" width="30">
				</span>
			</div>
			<input type="password" id="password" class="form-control" placeholder="password">
		</div>
		<button type="button" class="btn loginBtn btn-primary w-100 mb-3">로그인</button>
		<div class="sign-up-btn-box">
			<a href="/user/sign-up-view" class="btn signUpBtn btn-secondary w-100">회원가입</a>
		</div>
	</form>
</div>

<script>
$(document).ready(function() {
	
	// 로그인 버튼 클릭 시
	$('.loginBtn').on('click', function() {
		// alert('click');
		
		let loginId = $('#loginId').val().trim();
		let password = $('#password').val();
		
		if (!loginId) {
			alert("아이디를 입력해주세요.");
			return;
		}
		
		if (!password) {
			alert("비밀번호를 입력해주세요.");
			return;
		}
		
		$.ajax({
			// request
			type:"post"
			,url:"/user/sign-in"
			,data:{"loginId":loginId, "password":password}
			
			// response
			// {"code" : 200, "result" : "성공"}
			// {"code" : 500, "errorMessage" : "에러 이유"}
			, success:function(data) {
				if (data.code == 200) {
					// 게시판 목록으로 이동
					alert("로그인 성공");
				} else {
					alert(data.errorMessage);
				}
			}
			
			, error:function(request, status, error) {
				alert("로그인 실패");
			}
		
		});
		
	});
});
</script>