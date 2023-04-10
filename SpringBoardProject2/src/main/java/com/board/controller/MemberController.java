package com.board.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.board.dto.MemberDTO;
import com.board.service.MemberService;

@Controller
@RequestMapping("/member")
public class MemberController {
	private MemberService memberService;

	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}
	
	@GetMapping("/register/view")
	public String memberRegisterView() {
		return "member_register";
	}

	@PostMapping("/login")
	public String login(String id, String passwd, HttpSession session, 
			HttpServletRequest request) {
		System.out.println(request.getRemoteAddr());
		MemberDTO dto = memberService.login(id, passwd, request.getRemoteAddr());
		session.setAttribute("dto", dto);
		return "redirect:/board/main";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	@GetMapping("/admin/list")
	public ModelAndView memberManageView(ModelAndView view, HttpSession session) {
		List<MemberDTO> list = memberService.selectAllMember(session);
		view.addObject("list", list);
		view.setViewName("admin_member_manage");
		return view;
	}
	@GetMapping("/admin/register/view")
	public String registerAdminView() {
		return "admin_member_register";
	}
	@PostMapping("/admin/register/action")
	public String registerAdmin(MemberDTO dto,  HttpSession session) {
		int result = memberService.insertMember(dto, session);
		return "redirect:/member/admin/list";
	}
	
	@PostMapping("/admin/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable String id,  HttpSession session) {
		int result = memberService.deleteMember(id, session);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("count", String.valueOf(result));
		if(result != 0) {
			map.put("message", "데이터 삭제 성공");
		}else {
			map.put("message", "데이터 삭제 실패");
		}
		return new ResponseEntity(map,HttpStatus.OK);
	}
	@PostMapping("/admin/search")
	public ResponseEntity<String> searchMember(String kind, String search, HttpSession session){
		List<MemberDTO> list = memberService.searchMember(kind,search,session);
		System.out.println(list);
		return new ResponseEntity(list,HttpStatus.OK);
	}
	
	@GetMapping("/admin/detail/{id}")
	public ModelAndView updateView(@PathVariable String id, ModelAndView mv, HttpSession session) {
		MemberDTO dto = memberService.selectMember(id, session);
		mv.addObject("dto", dto);
		mv.setViewName("admin_member_view");
		return mv;
	}
	@PostMapping("/admin/update")
	public String update(MemberDTO dto,HttpSession session) {
		int result = memberService.updateMember(dto,session);
		return "redirect:/member/admin/list";
	}
}

