package com.tenco.bank.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.tenco.bank.handler.AuthInterceptor;

// 유저가 만든 인터셉터 클래스 등록 + 패턴 설정
@Configuration	// IoC 등록 - 2개이상 빈으로 등록될 때 사용
public class WebMvcConfig implements WebMvcConfigurer{

	// 다른곳에서도 쓸거라서 멤버변수로 선언
	@Autowired	//DI
	private AuthInterceptor authInterceptor;
	
	// 인터셉터를 등록하는 메서드
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 여기서 멤버변수로 바로 AuthInterceptor 뉴 때리면 싱글톤 패턴

		// /account/** 아래에 있는 모든 주소를 확인해
		// + path 더 추가하는 방법
		registry.addInterceptor(authInterceptor).addPathPatterns("/account/**").addPathPatterns("/auth/**");
		
		// todo 나중
//		registry.addInterceptor(new )
	}
	
	// 인터셉터 등록
	
} // end of class
