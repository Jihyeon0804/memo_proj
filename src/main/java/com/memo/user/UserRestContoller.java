package com.memo.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.memo.common.EncryptUtils;
import com.memo.user.bo.UserBO;
import com.memo.user.entity.UserEntity;

@RequestMapping("/user")
@RestController
public class UserRestContoller {

	@Autowired
	private UserBO userBO;
	
	// API 설계
	
	/**
	 * 로그인 아이디 중복 확인 API
	 * @param loginId
	 * @return
	 * */
	@RequestMapping("/is-duplicated-id")
	public Map<String, Object> isDuplicatedId(
			@RequestParam("loginId") String loginId) {
		
		// db 조회
		UserEntity user = userBO.getUserEntityByLoginId(loginId);
		
		
		// 응답값 만들고 리턴 => json
		Map<String, Object> result = new HashMap<>();
		result.put("code", 200);
		
		if (user == null) {
			// 중복 아님
			result.put("isDuplicated", false);
		} else {
			// 중복
			result.put("isDuplicated", true);
		}
		return result;
	}
	
	
	/**
	 * 회원가입 API
	 * @param loginId
	 * @param password
	 * @param name
	 * @param email
	 * @return
	 */
	@PostMapping("/sign-up")
	public Map<String, Object> signUp(
			@RequestParam("loginId") String loginId
			, @RequestParam("password") String password
			, @RequestParam("name") String name
			, @RequestParam("email") String email
			) {
		
		// password 해싱 - md5 알고리즘(보안 다소 취약)
		// 알고리즘 여러가지 있으니 나중에 다른 방식 적용해보기
		// aaaa => 74b8733745420d4d33f80c4663dc5e5
		String hashedPassword = EncryptUtils.md5(password);
		
		
		// DB insert
		Integer id = userBO.addUser(loginId, hashedPassword, name, email);
		
		// 응답값
		Map<String, Object> result = new HashMap<>();
		if (id == null) {
			result.put("code", 500);
			result.put("errorMessage", "회원가입 하는데 실패했습니다.");
		} else {
			result.put("code", 200);
			result.put("result", "성공");
		}
		
		return result;	// json
	}
	
	
	/**
	 * 로그인 API
	 * @param loginId
	 * @param password
	 * @return
	 */
	@PostMapping("/sign-in")
	public Map<String, Object> signIn(
			@RequestParam("loginId") String loginId
			, @RequestParam("password") String password
			, HttpServletRequest request) {
		
		// db 조회
		String hashedPassword = EncryptUtils.md5(password);
		UserEntity user = userBO.getUserEntityByLoginIdPassword(loginId, hashedPassword);
//		Integer id = userBO.getUserEntityByLoginIdPassword(loginId, hashedPassword);
		
		// 응답값
		// {"code" : 200, "result" : "성공"}
		// {"code" : 500, "errorMessage" : "에러 이유"}
		Map<String, Object> result = new HashMap<>();
		if (user != null) {
			// 로그인 처리
			HttpSession session = request.getSession();
			// session에 지금 로그인한 사람의 정보를 담음
			session.setAttribute("userId", user.getId());
			session.setAttribute("userName", user.getName());
			session.setAttribute("userLoginId", user.getLoginId());
			
			result.put("code", 200);
			result.put("result", "성공");
		} else {
			// 로그인 불가
			result.put("code", 500);
			result.put("errorMessage", "아이디 또는 비밀번호가 올바르지 않습니다.");
		}
		
		return result;
	}
	
}
