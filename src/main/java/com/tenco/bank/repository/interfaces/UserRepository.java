package com.tenco.bank.repository.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.tenco.bank.dto.SignInFormDto;
import com.tenco.bank.dto.SignUpFormDto;
import com.tenco.bank.repository.model.User;

@Mapper // MyBatis 의존 설정이 되어있어서 선언 가능하게 됨
		// build.gradle 파일
public interface UserRepository {

	public int insert(SignUpFormDto signUpFormDto);

	public int updateById(User user); // 한사람만 변경하게끔 설계

	public int deleteById(Integer id);

	public User findById(Integer id); // 한사람만 조회

	public List<User> findAll(); // 모든 사람 조회
	
	// 추가 작업
	public User findByUsernameAndPassword(SignInFormDto signInFormDto);
	
	public User findByPassword(SignInFormDto signInFormDto);
	
}
