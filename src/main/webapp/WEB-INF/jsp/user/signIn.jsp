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
			<input type="text" class="form-control" placeholder="Username">
		</div>
		<div class="input-group mb-3">
			<div class="input-group-prepend">
				<span class="input-group-text" id="password-addon">
					<img src="/static/img/passwordIcon.png" width="30">
				</span>
			</div>
			<input type="text" class="form-control" placeholder="password">
		</div>
		<button type="button" class="btn loginBtn btn-primary w-100 mb-3">로그인</button>
		<br>
		<button type="button" class="btn signUpBtn btn-secondary w-100 mb-3">회원가입</button>
	</form>
</div>