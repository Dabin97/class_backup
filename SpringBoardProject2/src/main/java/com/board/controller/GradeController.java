package com.board.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.board.service.GradeService;

@Controller
@RequestMapping("/grade")
public class GradeController {
	private GradeService gradeService;
	
	public GradeController(GradeService gradeService) {
		this.gradeService = gradeService;
	}
	
	@GetMapping("/admin/list")
	public ModelAndView gradeView(ModelAndView mv) {
		ArrayList<HashMap<String, Object>> list = gradeService.selectAllGrade();
		System.out.println(list.toString());
		mv.setViewName("grade_manage");
		mv.addObject("list", list);
		return mv;
	}
	
	@PostMapping("/add")
	public ResponseEntity<String> gradeAppend(@RequestParam(name = "grade_no") String gradeNo, 
			@RequestParam(name = "grade_name")String gradeName){
		
		int result = gradeService.insertGrade(gradeNo,gradeName);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("code", result);
		return new ResponseEntity(map,HttpStatus.OK);
	}
	@RequestMapping(value = "/search" , method = RequestMethod.POST)
	public ResponseEntity<String> selectGradeList(String val){
		ArrayList<HashMap<String, Object>> list = gradeService.selectGrade(val);
		
		return new ResponseEntity(list,HttpStatus.OK);
	}
	@RequestMapping("/delete/{grade_no}")
	public ResponseEntity<String> deleteGrade(@PathVariable("grade_no") int grade_no){
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("code", gradeService.deleteGrade(grade_no));
		return new ResponseEntity(map,HttpStatus.OK);
	}
	@RequestMapping("/update")
	public ResponseEntity<String> updateGrade(int grade_no, String grade_name){
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("code", gradeService.updateGrade(grade_no,grade_name));
		return new ResponseEntity(map,HttpStatus.OK);
	}
}
