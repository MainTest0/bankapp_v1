package com.tenco.bank.repository.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.tenco.bank.repository.model.Account;

@Mapper
public interface AccountRepository {
	
	public int insert(Account account);
	public int updateById(Account account);
	public int deleteById(int id);
	
	// 관리자용 모드 계좌 확인 (+ 나중에 추가)
	public List<Account> findAll();	
	public Account fintById(int id);
	
}
