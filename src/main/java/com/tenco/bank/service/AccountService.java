package com.tenco.bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.tenco.bank.dto.SaveFormDto;
import com.tenco.bank.handler.exception.CustomRestfullException;
import com.tenco.bank.repository.interfaces.AccountRepository;
import com.tenco.bank.repository.model.Account;

@Service	//IoC 대상 + 싱글톤으로 관리
public class AccountService {
	
	@Autowired		// DI 처리
	private AccountRepository accountRepository;
	
	/**
	 * 계좌 생성 기능
	 * @param saveFormDto
	 * @param principalId
	 */
	
	// 서비스 로직 만들기
	public void createAccount(SaveFormDto saveFormDto, Integer principalId) {
		
		//SaveFormDto -> 변경, 신규 생성
		Account account = new Account();
		account.setNumber(saveFormDto.getNumber());
		account.setPassword(saveFormDto.getPassword());
		account.setBalance(saveFormDto.getBalance());
		account.setUserId(principalId);
		int resultRowCount = accountRepository.insert(account);
		if(resultRowCount != 1) {
			throw new CustomRestfullException("계좌 생성 실패", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

}
