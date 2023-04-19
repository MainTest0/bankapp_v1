package com.tenco.bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenco.bank.dto.SignInFormDto;
import com.tenco.bank.dto.SignUpFormDto;
import com.tenco.bank.handler.exception.CustomRestfullException;
import com.tenco.bank.repository.interfaces.UserRepository;
import com.tenco.bank.repository.model.User;

@Service // IoC 대상 싱글톤 패턴으로 관리
public class UserService {

	@Autowired // DI 처리 (객체 생성시(메모리 올라갈 때) 의존 주의 처리)
				// 주소값을 갖다 끼워넣어줌
	private UserRepository userRepository;

	// 기능
	@Transactional // 메서드 호출이 시작될 때 트랜잭션에 시작
					// 메서드 종료시 트랜잭션 종료되면 commit한다
					// commit:저장장치에 실제로 저장하는 것
	public void createUser(SignUpFormDto signUpFormDto) {

		// DAO 가져와서 메서드 호출했었음
		// 지금은 MyBatis 쓰고있음

		int result = userRepository.insert(signUpFormDto);
		if (result != 1) {
			throw new CustomRestfullException("회원가입 실패", HttpStatus.INTERNAL_SERVER_ERROR); // 연산잘못된 에러
		}

	}
	/**
	 * 로그인 서비스 처리
	 * @param signInFormDto
	 * @return userEntity 응답
	 */
	public User signIn(SignInFormDto signInFormDto) {
		
		User userEntity = userRepository.findByUsernameAndPassword(signInFormDto);
		if(userEntity == null) {
			throw new CustomRestfullException("아이디 혹은 비번이 틀렸습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return userEntity;
	}

}// end of class
