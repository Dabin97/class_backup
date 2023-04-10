package com.member.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.member.dto.MemberDTO;
import com.member.mapper.MemberMapper;

@Service
public class MemberService {
	private MemberMapper mapper;

	public MemberService(MemberMapper mapper) {
		this.mapper = mapper;
	}

	public MemberDTO login(String id, String passwd) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id",id);
		map.put("passwd",passwd);
		return mapper.login(map);
	}

	public List<MemberDTO> selectAllMember() {
		return mapper.selectAllMember();
	}

	public int insertMember(MemberDTO dto) {
		return mapper.insertMember(dto);
	}

	public int deleteMember(String id) {
		return mapper.deleteMember(id);
	}

	public MemberDTO selectMember(String id) {
		return mapper.selectMember(id);
	}

	public int updateMember(MemberDTO dto) {
		return mapper.updateMember(dto);
	}

	public ArrayList<HashMap<String, Object>> selectAllGrade() {
		return mapper.selectAllGrade();
	}

	public int insertGrade(String gradeNo, String gradeName) {
		HashMap<String, Object> map = new HashMap<String,Object>();
		map.put("grade_no", gradeNo);
		map.put("grade_name", gradeName);
		return mapper.insertGrade(map);
	}

	public ArrayList<HashMap<String, Object>> selectGrade(String val) {
		return mapper.selectGrade(val);
	}

	public int updateGrade(int grade_no, String grade_name) {
		HashMap<String, Object> map = new HashMap<String,Object>();
		map.put("grade_no", grade_no);
		map.put("grade_name", grade_name);
		return mapper.updateGrade(map);
	}

	public int deleteGrade(int grade_no) {
		return mapper.deleteGrade(grade_no);
	}

	

	
}
