package com.tenco.bank.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tenco.bank.handler.exception.CustomRestfullException;

/*
 * 예외 시
 * 데이터를 내려 줄 수 있다.
 */

@RestControllerAdvice // IoC 대상 -> AOP기반(관점지향 프로그램)
public class MyRestfullExceptionHandler {

	@ExceptionHandler(Exception.class)
	public void exception(Exception e) {
		System.out.println(e.getClass().getName());
		System.out.println(e.getMessage());
	}

	// 사용자 정의 예외 클래스 활용
	// 예외 발생 감시하기
	@ExceptionHandler(CustomRestfullException.class)
	public String basicException(CustomRestfullException e) {
		StringBuffer sb = new StringBuffer();
		sb.append("<script>");
		// 반드시 마지막에 세미콜론을 붙여서 사용하자
		sb.append("alert('" + e.getMessage() + "');");
		sb.append("history.back();");
		sb.append("</script>");

		return sb.toString();
	}

}
