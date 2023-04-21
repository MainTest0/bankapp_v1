package com.tenco.bank;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {

	public static void main(String[] args) {
	
		// 기능 확인
		String password = "p1234";
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);
		
		System.out.println("원래 비밀번호 : " + password);
		System.out.println("암호화 된 비밀번호 : " + hashedPassword);

		/*
		 * 원래 비밀번호 : p1234
		   암호화 된 비밀번호 : $2a$10$NJE6qOOeyQeiNhs09TJt5uws5cmPGSo2z7NtSoDzTvQ.fRpFLRsYm
		 */
		
		// 판별하는 기능
		boolean isMatched = passwordEncoder.matches(password, hashedPassword);
		boolean isMatched2 = passwordEncoder.matches("aaa", hashedPassword);
		System.out.println("isMatched 비밀번호 일치 여부 : " + isMatched);
		System.out.println("isMatched2 비밀번호 일치 여부 : " + isMatched2);
		
		/*
		 * isMatched 비밀번호 일치 여부 : true
		   isMatched2 비밀번호 일치 여부 : false
		 */
		
	}
		
}
