package com.tenco.bank.handler.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

//IoC 대상이 아님 ( 필요할 때 직접 new 처리 )
@Getter
public class CustomRestfullException extends RuntimeException {

	private HttpStatus status;

	// throw new CustomRestfullException('페이지 못 찾음', 404);
	public CustomRestfullException(String message, HttpStatus status) {
		// 어떤 예외인지 메세지
		super(message);
		// 에러 코드 정할 수 있게(404,500)
		this.status = status;
	}

}
