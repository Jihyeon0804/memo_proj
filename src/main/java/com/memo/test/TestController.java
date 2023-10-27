package com.memo.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.memo.post.mapper.PostMapper;

@Controller
public class TestController {

	@Autowired
	private PostMapper postMapper;
	
	@ResponseBody
	@GetMapping("/test1")
	public String test01() {
		return "Hello world";
	}
	
	@ResponseBody
	@GetMapping("/test2")
	public Map<String, Object> test02() {
		Map<String, Object> map = new HashMap<>();
		map.put("a", 1);
		map.put("b", "테스트");
		map.put("c", "test");
		return map;
	}
	
	@GetMapping("/test3")
	public String test03() {
		return "test/test";
	}
	
	
	@ResponseBody
	@GetMapping("/test4")
	public List<Map<String, Object>> test04() {
		return postMapper.selectPostList(); //json
	}
}
