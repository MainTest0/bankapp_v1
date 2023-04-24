package com.tenco.bank.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class SignUpFormDto {
	// dto는 상속구조를 권장하지 않음
	// 포함관계를 많이 쓴다.
	private String username;
	private String password;
	private String fullname;
	// private MultipartFile[] 다중처리는 배열로 사용하자
	private MultipartFile file;	//file 은 name file 속성과 일치 시켜야 함
	// 원래 이미지 명
	// 실제 업로드 된 이미지 명 -> 컬럼 2개
	private String originFileName;
	private String uploadFileName;
}
