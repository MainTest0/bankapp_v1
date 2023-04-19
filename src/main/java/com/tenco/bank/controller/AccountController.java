package com.tenco.bank.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tenco.bank.dto.SaveFormDto;
import com.tenco.bank.dto.WithDrawFormDto;
import com.tenco.bank.handler.exception.CustomRestfullException;
import com.tenco.bank.handler.exception.UnAuthorizedException;
import com.tenco.bank.repository.model.Account;
import com.tenco.bank.repository.model.User;
import com.tenco.bank.service.AccountService;
import com.tenco.bank.utils.Define;

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
	@Autowired
	private HttpSession session;
	@Autowired
	private AccountService accountService;

	/*
	 * 계좌 목록 페이지
	 * 
	 * @return 목록 페이지 이동
	 */
	// http://localhost:8080/account/list
	// http://localhost:8080/account/
	@GetMapping({ "/list", "/" })
	public String list(Model model) {

		// 유효성 검사 전 인증검사하기
		// (주소를 치고 들어가면 바로 로그인 이후 화면으로 넘어가는거 방지)
		/**
		 * 계좌 목록 페이지
		 * 
		 * @return 목룍 페이지 이동
		 */

		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		if (principal == null) {
			throw new UnAuthorizedException("로그인 먼저 해주세요.", HttpStatus.UNAUTHORIZED);
		}

		// view 화면으로 데이터를 내려주는 기술
		// 1. Model 2. ModelAndView
		// 권장은 model, modelandview는 동적으로 화면을 내려줄 때

		List<Account> accountList = accountService.readAccountList(principal.getId());
		if (accountList.isEmpty()) {
			model.addAttribute("accountList", null);
		} else {
			model.addAttribute("accountList", accountList);
		}

		return "/account/list";
	}

	// 출금페이지
	@GetMapping("/withdraw")
	public String withdraw() {

		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		if (principal == null) {
			throw new UnAuthorizedException("로그인 먼저 해주세요", HttpStatus.UNAUTHORIZED);
		}

		return "/account/withdrawForm";
	}

	// 출금 처리 기능
	@PostMapping("/withthdraw-proc")
	public String withthdrawProc(WithDrawFormDto withDrawFormDto) {

		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		if (principal == null) {
			throw new UnAuthorizedException("로그인 먼저 해주세요.", HttpStatus.UNAUTHORIZED);
		}
		// 유효성 검사
		if (withDrawFormDto.getAmount() == null) {
			throw new CustomRestfullException("금액을 입력하세요", HttpStatus.BAD_REQUEST);
		}
		if (withDrawFormDto.getAmount().longValue() <= 0) {
			throw new CustomRestfullException("출금액이 0원 이하일 수는 없습니다.", HttpStatus.BAD_REQUEST);
		}
		if(withDrawFormDto.getWAccountNumber() == null || 
				withDrawFormDto.getWAccountNumber().isEmpty()) {
			throw new CustomRestfullException("계좌번호를 입력해주세요.", HttpStatus.BAD_REQUEST);
		}
		if(withDrawFormDto.getWAccountPassword() == null ||
				withDrawFormDto.getWAccountPassword().isEmpty()) {
			throw new CustomRestfullException("계좌 비밀 번호를 입력해주세요.", HttpStatus.BAD_REQUEST);
		}
		
		// todo 서비스 호출 예정
		accountService.updateAccountwithDraw(withDrawFormDto, principal.getId());
		return "redirect:/account/list";
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
		// 인증 검사 처리
		User user = (User) session.getAttribute(Define.PRINCIPAL);
		if (user == null) {
			throw new UnAuthorizedException("로그인 먼저 해주세요", HttpStatus.UNAUTHORIZED);
		}

		return "/account/save";
	}

	/**
	 * 계좌 생성 인증 검사 유효성 검사 처리: 0원 입력 가능, (-)입력 불가
	 * 
	 * @param saveFormDto
	 * @return 계좌 목록 페이지
	 */
	@PostMapping("/save-proc")
	public String saveProc(SaveFormDto saveFormDto) {
		User user = (User) session.getAttribute(Define.PRINCIPAL);
		if (user == null) {
			throw new UnAuthorizedException("로그인 먼저 해주세요", HttpStatus.UNAUTHORIZED);
		}
		// 유효성 검사하기
		if (saveFormDto.getNumber() == null || saveFormDto.getNumber().isEmpty()) {
			throw new CustomRestfullException("계좌번호를 입력해주세요", HttpStatus.BAD_REQUEST);
		}
		if (saveFormDto.getPassword() == null || saveFormDto.getPassword().isEmpty()) {
			throw new CustomRestfullException("계좌 비밀번호를 입력해주세요", HttpStatus.BAD_REQUEST);
		}
		if (saveFormDto.getBalance() == null || saveFormDto.getBalance() < 0) {
			throw new CustomRestfullException("잘못된 금액입니다.", HttpStatus.BAD_REQUEST);
		}

		// 서비스 호출
		accountService.createAccount(saveFormDto, user.getId());

		return "redirect:/account/list";
	}

	// 계좌 상세 보기 페이지
	@GetMapping("/detail")
	public String detail() {

		return "";
	}
}
