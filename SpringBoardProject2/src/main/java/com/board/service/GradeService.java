package com.board.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.board.mapper.GradeMapper;

@Service
public class GradeService {
	private GradeMapper mapper;

	public GradeService(GradeMapper mapper) {
		super();
		this.mapper = mapper;
	}
	
	public ArrayList<HashMap<String, Object>> selectAllGrade() {
		return mapper.selectAllGrade();
	}

	public int insertGrade(String gradeNo, String gradeName) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("grade_no", gradeNo);
		map.put("grade_name", gradeName);
		return mapper.insertGrade(map);
	}

	public ArrayList<HashMap<String, Object>> selectGrade(String val) {
		return mapper.selectGrade(val);
	}

	public int deleteGrade(int grade_no) {
		return mapper.deleteGrade(grade_no);
	}

	public int updateGrade(int grade_no, String grade_name) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("grade_no", grade_no);
		map.put("grade_name", grade_name);
		return mapper.updateGrade(map);
	}
}
