package com.board.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.board.dto.MemberDTO;
import com.board.mapper.MemberMapper;

@Service
public class MemberService {
	private MemberMapper mapper;
	
	public MemberService(MemberMapper mapper) {
		this.mapper = mapper;
	}

	public MemberDTO login(String id, String passwd, String ip) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("passwd", passwd);
		return mapper.login(map);
	}
	
	public List<MemberDTO> selectAllMember(HttpSession session) {
		return mapper.selectAllMember();
	}

	public int insertMember(MemberDTO dto,HttpSession session) {
		return mapper.insertMember(dto);
	}

	public int deleteMember(String id,HttpSession session) {
		return mapper.deleteMember(id);
	}

	public MemberDTO selectMember(String id,HttpSession session) {
		return mapper.selectMember(id);
	}

	public int updateMember(MemberDTO dto,HttpSession session) {
		return mapper.updateMember(dto);
	}

	public List<MemberDTO> searchMember(String kind, String search,HttpSession session) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("kind", kind);
		map.put("search", search);
		return mapper.searchMember(map);
	}
	
}





