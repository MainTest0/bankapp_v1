package com.tenco.bank.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tenco.bank.dto.DepositFormDto;
import com.tenco.bank.dto.SaveFormDto;
import com.tenco.bank.dto.TransferFormDto;
import com.tenco.bank.dto.WithDrawFormDto;
import com.tenco.bank.dto.response.HistoryDto;
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
		if (withDrawFormDto.getWAccountNumber() == null || withDrawFormDto.getWAccountNumber().isEmpty()) {
			throw new CustomRestfullException("계좌번호를 입력해주세요.", HttpStatus.BAD_REQUEST);
		}
		if (withDrawFormDto.getWAccountPassword() == null || withDrawFormDto.getWAccountPassword().isEmpty()) {
			throw new CustomRestfullException("계좌 비밀 번호를 입력해주세요.", HttpStatus.BAD_REQUEST);
		}

		// todo 서비스 호출 예정
		accountService.updateAccountwithDraw(withDrawFormDto, principal.getId());
		return "redirect:/account/list";
	}

	// 입금 페이지
	@GetMapping("/deposit")
	public String deposit() {

		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		if (principal == null) {
			throw new UnAuthorizedException("로그인 먼저 해주세요", HttpStatus.UNAUTHORIZED);
		}

		return "/account/depositForm";
	}

	// 입금 처리 기능
	@PostMapping("/deposit-proc")
	public String depositProc(DepositFormDto depositFormDto) {
		// 인증 처리
		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		if (principal == null) {
			throw new UnAuthorizedException("로그인 먼저 해주세요", HttpStatus.UNAUTHORIZED);
		}
		// 유효성 검사
		if (depositFormDto.getAmount() == null || depositFormDto.getAmount().longValue() <= 0) {
			throw new CustomRestfullException("금액을 입력해주세요", HttpStatus.BAD_REQUEST);
		}
		if (depositFormDto.getAmount().longValue() <= 0) {
			throw new CustomRestfullException("입금 금액이 0원 이하일 수 없습니다.", HttpStatus.BAD_REQUEST);
		}
		if (depositFormDto.getDAccountNumber() == null || depositFormDto.getDAccountNumber().isEmpty()) {
			throw new CustomRestfullException("계좌 번호를 입력하세요", HttpStatus.BAD_REQUEST);
		}

		// todo 서비스 호출
		accountService.updateAccountDeposit(depositFormDto);

		return "redirect:/account/list";
	}

	// 이체 페이지
	@GetMapping("/transfer")
	public String transper() {

		if (session.getAttribute(Define.PRINCIPAL) == null) {
			throw new CustomRestfullException("로그인 먼저 해주세요", HttpStatus.UNAUTHORIZED);
		}

		return "/account/transferForm";
	}

	// 이체 기능 만들기
	@PostMapping("/transfer-proc")
	public String transferProc(TransferFormDto transferFormDto) {

		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		if (principal == null) {
			throw new CustomRestfullException("로그인 먼저 해주세요", HttpStatus.UNAUTHORIZED);
		}
		// ------유효성 검사만 계좌있는지는 서비스쪽에서

		// 6. 이체금액 안적었을 때
		if (transferFormDto.getAmount() == null) {
			throw new CustomRestfullException("이체 금액을 입력해주세요", HttpStatus.BAD_REQUEST);
		}
		// 1. 출금 계좌 번호 입력 여부
		if (transferFormDto.getWAccountNumber() == null || transferFormDto.getWAccountNumber().isEmpty()) {
			throw new CustomRestfullException("출금 계좌 번호를 입력해주세요", HttpStatus.BAD_REQUEST);
		}
		// 2. 입금 계좌 번호 입력 여부
		if (transferFormDto.getDAccountNumber() == null || transferFormDto.getDAccountNumber().isEmpty()) {
			throw new CustomRestfullException("입금 계좌 번호를 입력해주세요", HttpStatus.BAD_REQUEST);
		}
		// 3. 출금 계좌 비밀번호 입력 여부
		if (transferFormDto.getWAccountPassword() == null || transferFormDto.getWAccountPassword().isEmpty()) {
			throw new CustomRestfullException("출금 계좌 비밀번호를 입력해주세요", HttpStatus.BAD_REQUEST);
		}
		// 4. 이체 금액 0원 이상 확인
		if (transferFormDto.getAmount() <= 0) {
			throw new CustomRestfullException("이체 금액이 0원 이하일 수 없습니다.", HttpStatus.BAD_REQUEST);
		}
		// 5. 출금계좌 입금계좌 번호 동일 여부 확인
		if (transferFormDto.getWAccountNumber().equals(transferFormDto.getDAccountNumber())) {
			throw new CustomRestfullException("출금계좌와 입금계좌는 동일할 수 없습니다.", HttpStatus.BAD_REQUEST);
		}

		// 서비스 호출
		accountService.updateAccountTransfer(transferFormDto, principal.getId());

		return "redirect:/account/list";
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
	@GetMapping("/detail/{id}")
	public String detail(@PathVariable Integer id, @RequestParam(name = "type", defaultValue = "all", required = false) String type, Model model) {
		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new CustomRestfullException("로그인 먼저 해주세요", HttpStatus.UNAUTHORIZED);
		}
		System.out.println("type : " + type);
		// 거래내역 결과 집합 = 서비스.메서드();
		List<HistoryDto> historyList = accountService.readHistoryListByAccount(type, id);
		Account account = accountService.readAccount(id);
		
		// 화면을 구성하기 위해 필요한 데이터 살펴보기
		// 소유자 이름
		// 계좌번호(1개)상세보기
		// 계좌 잔액
		// 거래 내역
		model.addAttribute("principal", principal);
		model.addAttribute("account", account);
		model.addAttribute("historyList", historyList);
		
		
		return "/account/detail";
	}
}
