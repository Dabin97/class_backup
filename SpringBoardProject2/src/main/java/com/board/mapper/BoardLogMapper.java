package com.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.board.dto.LogDTO;

@Mapper
public interface BoardLogMapper {

	int insertLog(LogDTO log);
	List<LogDTO> selectLogList();
	List<LogDTO> selectCallList(String date);




} 



