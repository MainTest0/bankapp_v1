package com.tenco.bank.repository.model;

import java.sql.Timestamp;

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
	
	// Account의 기능 
	// 패스워드 체크
	// 잔액 여부 확인 (출금 시)
	// 계좌 소유자 확인
	
} //end of class
