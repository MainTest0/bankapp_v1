package com.tenco.bank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenco.bank.dto.DepositFormDto;
import com.tenco.bank.dto.SaveFormDto;
import com.tenco.bank.dto.TransferFormDto;
import com.tenco.bank.dto.WithDrawFormDto;
import com.tenco.bank.handler.exception.CustomRestfullException;
import com.tenco.bank.repository.interfaces.AccountRepository;
import com.tenco.bank.repository.interfaces.HistoryRepository;
import com.tenco.bank.repository.model.Account;
import com.tenco.bank.repository.model.History;

@Service // IoC 대상 + 싱글톤으로 관리
public class AccountService {

	@Autowired // DI 처리
	private AccountRepository accountRepository;

	@Autowired
	private HistoryRepository historyRepository;

	/**
	 * 계좌 생성 기능
	 * 
	 * @param saveFormDto
	 * @param principalId
	 */

	// 서비스 로직 만들기
	public void createAccount(SaveFormDto saveFormDto, Integer principalId) {

		// SaveFormDto -> 변경, 신규 생성
		Account account = new Account();
		account.setNumber(saveFormDto.getNumber());
		account.setPassword(saveFormDto.getPassword());
		account.setBalance(saveFormDto.getBalance());
		account.setUserId(principalId);
		int resultRowCount = accountRepository.insert(account);
		if (resultRowCount != 1) {
			throw new CustomRestfullException("계좌 생성 실패", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// 계좌 목록 보기 기능
	@Transactional
	public List<Account> readAccountList(Integer userId) {

		List<Account> list = accountRepository.findByUserId(userId);

		return list;
	}

	// 출금 기능 로직 고민해보기
	// 1. 계좌 존재 여부 확인 -> select query
	// 2. 본인 계좌 여부 확인
	// 3. 계좌 비번 확인
	// 4. 잔액 여부 확인(계좌에 출금하려는 돈이 없는데 출금하려할 때)
	// 5. 출금 처리 -> update query
	// 6. 거래 내역 등록 -> insert query
	// 7. 트랜잭션 처리
	@SuppressWarnings("unused")
	@Transactional
	public void updateAccountwithDraw(WithDrawFormDto withDrawFormDto, Integer principalId) {

		Account accountEntity = accountRepository.findByNumber(withDrawFormDto.getWAccountNumber());
		System.out.println(accountEntity.toString());
		// 1.
		if (accountEntity == null) {
			throw new CustomRestfullException("해당 계좌가 없습니다.", HttpStatus.BAD_REQUEST);
		}
		// 2.
		if (accountEntity.getUserId() != principalId) {
			throw new CustomRestfullException("본인 소유 계좌가 아닙니다.", HttpStatus.UNAUTHORIZED);
		}
		// 3. 이퀄스가 T고 비교가 F면 == false이다.
		if (accountEntity.getPassword().equals(withDrawFormDto.getWAccountPassword()) == false) {
			throw new CustomRestfullException("출금 계좌 비밀번호가 틀렸습니다.", HttpStatus.UNAUTHORIZED);
		}
		// 4.
		if (accountEntity.getBalance() < withDrawFormDto.getAmount()) {
			throw new CustomRestfullException("계좌 잔액이 부족합니다.", HttpStatus.BAD_REQUEST);
		}
		// 5. 모델 객체 상태값 변경 처리
		accountEntity.withDraw(withDrawFormDto.getAmount());
		// 아래코드는 위 기능 선언한거 풀어쓴거
//		accountEntity.setBalance(accountEntity.getBalance() - withDrawFormDto.getAmount());
		accountRepository.updateById(accountEntity);

		// 6. 거래내역 등록
		History history = new History();
		history.setAmount(withDrawFormDto.getAmount());
		history.setWAccountId(accountEntity.getId());
		history.setDAccountId(null);
		history.setWBalance(accountEntity.getBalance()); // 고민하기
		history.setDBalance(null);

		int resultRowCount = historyRepository.insert(history);
		if (resultRowCount != 1) {
			throw new CustomRestfullException("정상 처리되지 않았습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// 입금 처리 기능
	// 트랜잭션 처리
	// 1. 계좌 존재 여부 확인 -> select query
	// 2. 입금 처리 -> update query
	// 3. 거래 내역 등록 처리 -> insert query
	@Transactional
	public void updateAccountDeposit(DepositFormDto depositFormDto) {
		Account accountEntity = accountRepository.findByNumber(depositFormDto.getDAccountNumber());
		if (accountEntity == null) {
			throw new CustomRestfullException("해당 계좌가 존재하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// 객체 상태값 변경
		accountEntity.deposit(depositFormDto.getAmount());
		accountRepository.updateById(accountEntity);

		// 거래내역 등록 처리
		History history = new History();
		history.setAmount(depositFormDto.getAmount());
		history.setWBalance(null);
		history.setDBalance(accountEntity.getBalance());
		history.setWAccountId(null);
		history.setDAccountId(accountEntity.getId());

		int resultRowCount = historyRepository.insert(history);

		if (resultRowCount != 1) {
			throw new CustomRestfullException("정상 처리되지 않았습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 이체 기능 만들기
	// 트랜잭션 처리
	// 1. 출금 계좌 존재 여부 확인 - select
	// 2. 입금 계좌 존재 여부 확인 - select
	// 3. 출금 계좌 본인 소유 확인 - 1번에서 뽑은 데이터(entity) == principal(session에 id값이랑 비교)
	// 4. 출금 계좌 비번 확인 - 1번에서 뽑은 entity == 넘겨받은 dto 비교
	// 5. 출금 계좌 잔액 여부 확인 - 1번에서 뽑은 entity == dto 비교
	// 6. 출금 계좌 잔액 변경 - update
	// 7. 입금 계좌 잔액 변경 - update
	// 8. 거래 내역 저장 - insert
	@Transactional
	public void updateAccountTransfer(TransferFormDto transferFormDto, Integer principalId) {
		// 1.
		Account withdrawAccountEntity = accountRepository.findByNumber(transferFormDto.getWAccountNumber());
		if(withdrawAccountEntity == null) {
			throw new CustomRestfullException("출금 계좌가 존재하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// 2.
		Account depositAccountEntity = accountRepository.findByNumber(transferFormDto.getDAccountNumber());
		if(depositAccountEntity == null) {
			throw new CustomRestfullException("입금 계좌가 존재하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// 3.
		withdrawAccountEntity.checkOwner(principalId);
		// 4.
		withdrawAccountEntity.checkPassword(transferFormDto.getWAccountPassword());
		// 5.
		withdrawAccountEntity.checkBalance(transferFormDto.getAmount());
		// 6. 출금 객체 상태값 변경 - 계좌 잔액 수정 처리
		withdrawAccountEntity.withDraw(transferFormDto.getAmount());
		// 변경된 객체 상태 값으로 DB 업데이트 처리
		accountRepository.updateById(withdrawAccountEntity);
		// 7. 입금 객체 상태값 변경 - 계좌 잔액 수정 처리
		depositAccountEntity.deposit(transferFormDto.getAmount());
		// 변경된 입금 객체 상태 값으로 DB 업데이트 처리
		accountRepository.updateById(depositAccountEntity);
		// 8.
		History history = new History();
		history.setAmount(transferFormDto.getAmount());
		history.setWAccountId(withdrawAccountEntity.getId());
		history.setDAccountId(depositAccountEntity.getId());
		history.setWBalance(withdrawAccountEntity.getBalance());
		history.setDBalance(depositAccountEntity.getBalance());
		
		int resultRowCount = historyRepository.insert(history);
		if(resultRowCount != 1) {
			throw new CustomRestfullException("정상 처리 되지않았습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}// end of class
