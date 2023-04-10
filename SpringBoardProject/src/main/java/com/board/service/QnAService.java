package com.board.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.board.dto.QnADTO;
import com.board.mapper.QnAMapper;

@Service
public class QnAService {
	private QnAMapper mapper;

	public QnAService(QnAMapper mapper) {
		this.mapper = mapper;
	}

	public int insertMemberQnA(QnADTO dto) {
		return mapper.insertMemberQnA(dto);
	}

	public List<QnADTO> selectMemberQnAList(String id, int pageNo) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("writer", id);
		map.put("pageNo", pageNo);
		return mapper.selectMemberQnAList(map);
	}

	public List<QnADTO> selectAdminQnAList(int pageNo) {
		return mapper.selectAdminQnAList(pageNo);
	}

	public int selectQnACount() {
		return mapper.selectQnACount();
	}

	public int updateQnAStatus(int qno) {
		return mapper.updateQnAStatus(qno);
	}

	public QnADTO selectQnA(int qno) {
		return mapper.selectQnA(qno);
	}

	public int updateResponse(int qno, String response) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("qno", qno);
		map.put("response", response);
		
		return mapper.updateResponse(map);
	}
	
	
}

