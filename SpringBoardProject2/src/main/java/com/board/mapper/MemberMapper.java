package com.board.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.board.dto.MemberDTO;

@Mapper
public interface MemberMapper {

	MemberDTO login(HashMap<String, Object> map);
	List<MemberDTO> selectAllMember();
	int insertMember(MemberDTO dto);
	int deleteMember(String id);
	MemberDTO selectMember(String id);
	int updateMember(MemberDTO dto);
	List<MemberDTO> searchMember(HashMap<String, Object> map);
}









