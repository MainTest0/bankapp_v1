package com.tenco.bank.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tenco.bank.dto.SignInFormDto;
import com.tenco.bank.dto.SignUpFormDto;
import com.tenco.bank.handler.exception.CustomRestfullException;
import com.tenco.bank.repository.model.User;
import com.tenco.bank.service.UserService;
import com.tenco.bank.utils.Define;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired // DI 처리
	private UserService userService;
	
	@Autowired	// DI 처리
	private HttpSession session;

	// http://localhost:8080/user/sign-up
	@GetMapping("/sign-up")
	public String singUp() {
		// prefix
		// suffix

		return "/user/signUp";
	}
	// MIME TYPE : x-www-form-urlencoded
	// form : query String 방식으로 들어온다.
	// dto : object mapper 처리

	/*
	 * 회원 가입 처리
	 * 
	 * @param signUpFormDto
	 * 
	 * @return 리다이렉트 로그인 페이지
	 */
	@PostMapping("/sign-up")
	public String singUpProc(SignUpFormDto signUpFormDto) {

		// 유효성 검사
		if (signUpFormDto.getUsername() == null || signUpFormDto.getUsername().isEmpty()) {
			throw new CustomRestfullException("username을 입력해주세요", HttpStatus.BAD_REQUEST);
		}
		if (signUpFormDto.getPassword() == null || signUpFormDto.getPassword().isEmpty()) {
			throw new CustomRestfullException("password를 입력해주세요", HttpStatus.BAD_REQUEST);
		}
		if (signUpFormDto.getFullname() == null || signUpFormDto.getFullname().isEmpty()) {
			throw new CustomRestfullException("fullname을 입력해주세요", HttpStatus.BAD_REQUEST);
		}

		// 서비스 호출
		userService.createUser(signUpFormDto);

		return "redirect:/user/sign-in";
	}

	/*
	 * 로그인 폼
	 * 
	 * @return 로그인 페이지
	 */

	@GetMapping("/sign-in")
	public String signIn() {

		return "/user/signIn";
	}

	/*
	 * 로그인 처리
	 * 
	 * @param signInFormDto
	 * 
	 * @return 메인페이지 이동 ( 수정 예정 )
	 * 생각해보기
	 * GET방식 처리는 브라우저 history에 남겨지기 때문에
	 * 예외적으로 로그인은 POST 방식으로 처리한다. (보안)
	 */
	@PostMapping("/sign-in")
	public String signInProc(SignInFormDto signInFormDto) {
		
		// 1. 유효성 검사 (인증 검사가 더 우선)
		if(signInFormDto.getUsername() == null || signInFormDto.getUsername().isEmpty()) {
			throw new CustomRestfullException("username을 입력하세요", HttpStatus.BAD_REQUEST);
		}
		if(signInFormDto.getPassword() == null || signInFormDto.getPassword().isEmpty()) {
			throw new CustomRestfullException("Password을 입력하세요", HttpStatus.BAD_REQUEST);
		}
		
		User principal = userService.signIn(signInFormDto);
		principal.setPassword(null);
		session.setAttribute(Define.PRINCIPAL, principal);
		
		return "/account/list";
	}
	@GetMapping("/logout")
	public String logout() {
		
		// 세션 완전 삭제하기
		session.invalidate();
		
		return "redirect:/user/sign-in";
		
	}

}
