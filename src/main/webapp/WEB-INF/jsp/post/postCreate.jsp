<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="d-flex justify-content-center">
	<div class="w-50">
		<h1>글쓰기</h1>
		
		<%-- 제목 & 내용 --%>
		<input type="text" id="subject" class="form-control" placeholder="제목을 입력하세요">
		<textarea id="content" class="form-control" rows="10" placeholder="내용을 입력하세요"></textarea>
		
		<%-- 파일 업로드 --%>
		<div class="d-flex justify-content-end my-4">
			<input type="file" id="file" accept=".jpg, .jpeg, .png, .gif">
		</div>
		
		<%-- 목록, 지우기, 저장 버튼 --%>
		<div class="d-flex justify-content-between">
			<button type="button" id="postListBtn" class="btn btn-dark">목록</button>
			
			<div>
				<button type="button" id="clearBtn" class="btn btn-secondary">모두 지우기</button>
				<button type="button" id="saveBtn" class="btn btn-primary">저장</button>
			</div>
		</div>
	</div>
</div>

<script>
$(document).ready(function() {
	
	// 목록 버튼 클릭 시 => 글 목록 화면으로 이동
	$('#postListBtn').on('click', function() {
		location.href = "/post/post-list-view";
	});
	
	// 모두 지우기 버튼 클릭 시 => 제목(subject)과 내용(content)을 모두 지우기
	$('#clearBtn').on('click', function() {
		$('#subject').val("");
		$('#content').val("");
	});
	
	// 저장 버튼 클릭 시
	$('#saveBtn').on('click', function() {
		let subject = $('#subject').val().trim();
		let content = $('#content').val();
		let fileName = $('#file').val();	// C:\fakepath\chess-8348280_640.jpg

		// validation check
		if (!subject) {
			alert("제목을 입력하세요.");
			return;
		}
		
		if (!content) {
			alert("내용을 입력하세요.");
			return;
		}
		
		// 파일이 업로드 된 경우에만 확장자 체크
		if (fileName) {
			// alert("파일 있음");
			// C:\fakepath\chess-8348280_640.jpg
			// 확장자만 뽑은 후 소문자로 변경한다.
			let ext = fileName.split(".").pop().toLowerCase();
			// alert(ext);
			
			if ($.inArray(ext, ['jpg', 'jpeg', 'png', 'gif']) == -1) {	// 배열에 해당 값이 없으면 -1 return
				alert("이미지 파일만 업로드할 수 있습니다.");
				$('#file').val("");	// 파일을 비운다.
				return;
			}
		}
		
		// request param 구성
		// 이미지 업로드 시 반드시 폼태그로(javascript로 폼태그 만들기)
		let formData = new FormData();	// form 태그 생성
		formData.append("subject", subject);	// key는 form태그의 name속성과 같고 Request Parameter명이 된다.
		formData.append("content", content);
		formData.append("file", $('#file')[0].files[0]);
		
		$.ajax({
			// request
			type:"post"
			, url:"/post/create"
			, data:formData
			, enctype:"multipart/form-data"		// 파일 업로드를 위한 필수 설정
			, processData:false					// 파일 업로드를 위한 필수 설정
			, contentType:false					// 파일 업로드를 위한 필수 설정
			
			
			// response
			, success:function(data) {
				if (data.code == 200) {
					// 글 목록 화면으로 이동
					alert("메모가 저장되었습니다.");
					location.href = "/post/post-list-view";
				} else {
					// 로직 상 실패
					alert(data.errorMessage)
				}
			}
			, error:function(request, status, error) {
				alert("글을 저장하는데 실패하였습니다.");
			}
		});
	});
});
</script>