package com.board.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.board.dto.QnADTO;

@Mapper
public interface QnAMapper {

	int insertMemberQnA(QnADTO dto);
	QnADTO selectQnA(int qno);
	List<QnADTO> selectMemberQnAList(HashMap<String, Object> map);
	List<QnADTO> selectAdminQnAList(int pageNo);
	int selectQnACount();
	int updateResponse(HashMap<String, Object> map);
	int updateQnAStatus(int qno);

} 
