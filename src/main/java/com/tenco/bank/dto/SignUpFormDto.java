package com.tenco.bank.dto;

import lombok.Data;

@Data
public class SignUpFormDto {
	// dto는 상속구조를 권장하지 않음
	// 포함관계를 많이 쓴다.
	private String username;
	private String password;
	private String fullname;

}
