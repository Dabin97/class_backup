package com.member.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.member.dto.MemberDTO;

@Mapper
public interface MemberMapper {

	MemberDTO login(HashMap<String, Object> map);
	List<MemberDTO> selectAllMember();
	int insertMember(MemberDTO dto);
	int deleteMember(String id);
	MemberDTO selectMember(String id);
	int updateMember(MemberDTO dto);
	ArrayList<HashMap<String, Object>> selectAllGrade();
	int insertGrade(HashMap<String, Object> map);
	ArrayList<HashMap<String, Object>> selectGrade(String val);
	int updateGrade(HashMap<String, Object> map);
	int deleteGrade(int grade_no);


}
