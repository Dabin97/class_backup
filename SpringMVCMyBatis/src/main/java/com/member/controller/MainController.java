package com.member.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.member.dto.MemberDTO;
import com.member.service.MemberService;

@Controller
public class MainController {
	private MemberService service;
	
	public MainController(MemberService service) {
		this.service = service;
	}

	@RequestMapping("/") 
	public String main() {
		return "index"; 
	}
	
	@RequestMapping("/login")
	public String login(String id, String passwd,HttpSession session) { 
		MemberDTO dto = service.login(id,passwd);
		session.setAttribute("dto", dto);
		if(dto != null)
			return "redirect:/main"; //Redirect로 보내는 방법
		else
			return "redirect:/"; //로그인 정보가 없으면 / = index.jsp로 보냄
	}
	
	@RequestMapping("/main") 
	public String mainView(HttpServletRequest request, HttpServletResponse response) {
		List<MemberDTO> list = service.selectAllMember();
		request.setAttribute("list", list);
		return "main"; 
	}
	
	@RequestMapping("/register/view") 
	public String registerView() {
		return "member_register"; 
	}
	
	@RequestMapping("/register/action") //절대경로로 잡아준다.
	public String register(MemberDTO dto) { //dto만 받아서 넣으면 된다. 
		int result = service.insertMember(dto);
		return "redirect:/main"; 
	}
	
//	@RequestMapping("/delete") --> 기존 delete 메소드
//	public ResponseEntity<String> delete(String id) { 
//		int result = service.deleteMember(id);
//		HashMap<String, String> map = new HashMap<String, String>();  //map으로 하는 방법, but JSONObject로 해도된다.
//		map.put("count", String.valueOf(result)); //result를 string으로 
//		if(result != 0)
//			map.put("message", "데이터 삭제 성공");
//		else
//			map.put("message", "데이터 삭제 실패");
//		
//		return new ResponseEntity(map,HttpStatus.OK);
//	}
	
	
	@RequestMapping("/delete/{id}") //새로운 방법
	public ResponseEntity<String> delete(@PathVariable String id) {
		int result = service.deleteMember(id);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("count", String.valueOf(result));
		if(result != 0) {
			map.put("message", "데이터 삭제 성공");
		}else {
			map.put("message", "데이터 삭제 실패");
		}
		return new ResponseEntity(map,HttpStatus.OK);
	}


	@RequestMapping("/detail/{id}")
	public ModelAndView updateView(@PathVariable String id, ModelAndView mv) {
		MemberDTO dto = service.selectMember(id);
		
		mv.addObject("dto", dto);
		mv.setViewName("member_update_view");
		
		return mv;
	}

	@RequestMapping("/update")
	public String update(MemberDTO dto) {
		int result = service.updateMember(dto);
		return "redirect:/main"; 
	}
	
	@RequestMapping("/grade/view")
	public ModelAndView gradeView(ModelAndView mv) {
		ArrayList<HashMap<String, Object>> list = service.selectAllGrade();
		System.out.println(list.toString());
		mv.addObject("list", list);
		mv.setViewName("grade_manage");
		return mv; 
	}
	
	@RequestMapping("/grade/add")
	public ResponseEntity<String> gradeAppend(@RequestParam(name="grade_no") String gradeNo, 
			@RequestParam(name="grade_name")String gradeName) {
		int result = service.insertGrade(gradeNo, gradeName);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("code", result);
		
		return new ResponseEntity(map,HttpStatus.OK);
	}
	
	@RequestMapping("/grade/search")
	public ResponseEntity<String> selectGradeList(String val) {
		ArrayList<HashMap<String, Object>> list = service.selectGrade(val);
		return new ResponseEntity(list,HttpStatus.OK);
	}
	
	
	@RequestMapping("/grade/update")
	public ResponseEntity<String> updateGrade(int grade_no, String grade_name) {  //앞단에서 ajax가 있는 애들 ResponseEntity
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("code", service.updateGrade(grade_no, grade_name)); //result안 거치고 바로 code에 넣어주기
		return new ResponseEntity(map,HttpStatus.OK);
	}
	
	
	@RequestMapping("/grade/delete/{grade_no}") 
	public ResponseEntity<String> deleteGrade(@PathVariable("grade_no") int grade_no) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("code", service.deleteGrade(grade_no)); //result안 거치고 바로 code에 넣어주기
		return new ResponseEntity(map,HttpStatus.OK);
	}
	
	
	
	
	
}


//메소드 하나가 컨트롤러 하나라고 생각하면 된다.