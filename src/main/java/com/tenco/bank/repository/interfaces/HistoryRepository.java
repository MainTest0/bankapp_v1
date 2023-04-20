package com.tenco.bank.repository.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tenco.bank.dto.response.HistoryDto;
import com.tenco.bank.repository.model.History;

@Mapper		// 반드시 지정하기 !
public interface HistoryRepository {
	
	public int insert(History history);
	public int updateById(History id);
	public int deleteById(int id);
	
	public History findById(int id);
	public List<History> fineAll();
	
	// 매개 변수 개수가 2개 이상이면 반드시 파라미터 이름을 명시해주자!
	public List<HistoryDto> findByIdHistoryType(@Param("type") String type, @Param("id") Integer id);
	
}
