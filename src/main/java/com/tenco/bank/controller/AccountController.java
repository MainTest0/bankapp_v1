package com.tenco.bank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class AccountController {
	
	// todo
	// 계좌 목록 페이지
	// 입금 페이지
	// 출금 페이지
	// 이체 페이지
	// 계좌 상세보기 페이지
	// 계좌 생성 페이지
	
	/*
	 * 계좌 목록 페이지
	 * @return 목록 페이지 이동
	 */
	// http://localhost:8080/account/list
	// http://localhost:8080/account/
	@GetMapping({"/list", "/"})
	public String list() {
		
		
		
		return "/account/list";
	}
	
	// 출금페이지
	@GetMapping("/withdraw")
	public String withdraw() {
		
		return"/account/withdrawForm";
	}
	
	// 입금 페이지
	@GetMapping("/deposit")
	public String deposit() {
		
		return "/account/depositForm";
	}
	
	// 이체 페이지
	@GetMapping("/transfer")
	public String transper() {
		
		return "/account/transferForm";
	}
	
	// 계좌 생성 페이지
	@GetMapping("/save")
	public String save() {
		
		return "/account/save";
	}
	
	// 계좌 상세 보기 페이지
	@GetMapping("/detail")
	public String detail() {
		
		return "";
	}
}