package com.tenco.bank.repository.model;

import java.sql.Timestamp;

import org.springframework.http.HttpStatus;

import com.tenco.bank.handler.exception.CustomRestfullException;

import lombok.Data;

/*
 * 자기 자신을 바라봤을 때 출금하는 기능 있을 수 있다.
 * 입금하는 기능도 만들 수 있다.
 * 처음에 스타크래프트 피빼는걸로 연습했음
 */

@Data
public class Account {

	private Integer id;
	private String number;
	private String password;
	private Long balance;
	private Integer userId;
	private Timestamp createdAt;

	public void withDraw(Long amount) {
		this.balance -= amount;
	}

	public void deposit(Long amount) { // 거래량
		this.balance += amount;
	}

	// 패스워드 체크
	public void checkPassword(String password) {
		if (this.password.equals(password) == false) {
			throw new CustomRestfullException("계좌 비밀번호가 틀렸습니다.", HttpStatus.BAD_REQUEST);
		}
	}

	// 잔액 여부 확인 (출금 시)
	public void checkBalance(Long amount) {
		if (this.balance < amount) {
			throw new CustomRestfullException("출금 잔액이 부족합니다.", HttpStatus.BAD_REQUEST);
		}
	}

	// 계좌 소유자 확인
	public void checkOwner(Integer principalId) {

		if (userId != principalId) {
			throw new CustomRestfullException("계좌 소유주가 아닙니다.", HttpStatus.FORBIDDEN);
		}
	}

} // end of class
