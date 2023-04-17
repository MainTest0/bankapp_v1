package com.tenco.bank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller	//페이지 반환
@RequestMapping("/test")
public class TestController {
	
	@GetMapping("/main")
	public String mainTest() {
		
		// ViewResolver 동작
		// prefix: /WEB-INF/view
		// suffix: .jsp
		
		// /WEB-INF/view/layout/main
		// .jsp
		// -> layout/main
		// 파일이름 리턴 처리
		return "layout/main";
	}

}
