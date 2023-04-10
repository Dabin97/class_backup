package com.board.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.board.dto.LogDTO;
import com.board.mapper.BoardLogMapper;

@Controller
@RequestMapping("/log")
public class LogController {
	private BoardLogMapper mapper; //service말고 바로 mapper를 받음


	public LogController(BoardLogMapper mapper) {
		this.mapper = mapper;
	}

	@GetMapping("/main")
	public ModelAndView logMain(ModelAndView view) {
		// 조회
		List<LogDTO> list = mapper.selectLogList();
		System.out.println(list.toString());
		view.addObject("list", list);
		
		view.setViewName("admin_log");
		return view;
	}
	
	@PostMapping("/new_log")
	public ResponseEntity<String> selectNewLog(String date){
		System.out.println(date);
		List<LogDTO> list = mapper.selectCallList(date);
		return new ResponseEntity(list, HttpStatus.OK);
	}


}
