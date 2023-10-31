package com.memo.user;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
@Controller
public class UserController {

	// 화면 설계
	
	/**
	 * 회원가입 화면
	 * @param model
	 * @return
	 */
	@GetMapping("/sign-up-view")
	public String signUpView(Model model) {
		// jsp에서 page 주소를 동적으로 변경하기 위해 주소의 경로를 model에 담아서 전송
		model.addAttribute("viewName", "user/signUp");
		return "template/layout";
	}
	
	
	/**
	 * 로그인 화면
	 * @param model
	 * @return
	 */
	@GetMapping("/sign-in-view")
	public String signInView(Model model) {
		// jsp에서 page 주소를 동적으로 변경하기 위해 주소의 경로를 model에 담아서 전송
		model.addAttribute("viewName", "user/signIn");
		return "template/layout";
	}
	
	
	@RequestMapping("/sign-out")
	public String signOut(HttpSession session) {
		// 세션에 있는 내용을 비운다
		session.removeAttribute("userId");
		session.removeAttribute("userName");
		session.removeAttribute("userLoginId");
		
		// 로그아웃 클릭 후 로그인 화면으로 이동
		return "redirect:/user/sign-in-view";
	}
}
