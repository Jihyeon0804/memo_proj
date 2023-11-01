<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="form-box" class="d-flex justify-content-center align-items-center w-100">
	<form id="loginForm" action="/user/sign-in" method="post">
		<div class="input-group mb-3">
			<div class="input-group-prepend">
				<span class="input-group-text" id="id-addon">
					<img src="/static/img/userIcon.png" width="30">
				</span>
			</div>
			<input type="text" id="loginId" name="loginId" class="form-control" placeholder="Username">
		</div>
		<div class="input-group mb-3">
			<div class="input-group-prepend">
				<span class="input-group-text" id="password-addon">
					<img src="/static/img/passwordIcon.png" width="30">
				</span>
			</div>
			<input type="password" id="password" name="password" class="form-control" placeholder="password">
		</div>
		<button type="submit" class="btn loginBtn btn-primary w-100 mb-3">로그인</button>
		<a class="btn signUpBtn btn-block btn-dark" href="/user/sign-up-view">회원가입</a>
	</form>
</div>

<script>
$(document).ready(function() {
	
	// 로그인 버튼 클릭 시
	$('#loginForm').on('submit', function(e) {
		e.preventDefault();
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

		// AJAX - 응답값이 JSON
		// form url, parasm
		let url = $(this).attr('action');
		let params = $(this).serialize();	// name 속성 반드시 있어야 함
		
		
		$.post(url, params) // request
		.done(function(data) {	// response
			if(data.code == 200) {
				// 성공 => 글 목록으로 이동
				location.href="/post/post-list-view";
				
			} else {
				// 로직 상 실패
				alert(data.errorMessage);
			}
		});
		
		
	});
});
</script>