package com.memo.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
@Controller
public class UserController {

	// 화면 설계
	
	@GetMapping("/sign-up-view")
	public String signUpView(Model model) {
		// jsp에서 page 주소를 동적으로 변경하기 위해 주소의 경로를 model에 담아서 전송
		model.addAttribute("viewName", "user/signUp");
		return "template/layout";
	}
}
