package com.board.mapper;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GradeMapper {
	ArrayList<HashMap<String, Object>> selectAllGrade();
	int insertGrade(HashMap<String, Object> map);
	ArrayList<HashMap<String, Object>> selectGrade(String val);
	int deleteGrade(int grade_no);
	int updateGrade(HashMap<String, Object> map);
}
