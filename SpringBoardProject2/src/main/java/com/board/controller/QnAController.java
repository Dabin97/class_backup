package com.board.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.board.dto.MemberDTO;
import com.board.dto.QnADTO;
import com.board.service.QnAService;
import com.board.vo.PaggingVO;

@Controller
@RequestMapping("/qna")
public class QnAController {
	private QnAService qnaService;
	
	public QnAController(QnAService qnaService) {
		this.qnaService = qnaService;
	}
	
	@RequestMapping("/member")
	public ModelAndView qnaMemberView(HttpSession session,
			@RequestParam(name = "pageNo", defaultValue = "1") int pageNo) {
		ModelAndView view = new ModelAndView();
		MemberDTO dto = (MemberDTO) session.getAttribute("dto");
		List<QnADTO> list = qnaService.selectMemberQnAList(dto.getId(),pageNo);
		view.addObject("list", list);
		view.setViewName("member_qna");
		return view;
	}
	
	@RequestMapping("/member/register")
	public String qnaMemberRegister(QnADTO dto) {
		qnaService.insertMemberQnA(dto);
		return "redirect:/qna/member";
	}
	
	@RequestMapping("/member/more")
	public ResponseEntity<String> nextQnA(int pageNo, HttpSession session) {
		MemberDTO dto = (MemberDTO) session.getAttribute("dto");
		List<QnADTO> list = qnaService.selectMemberQnAList(dto.getId(),pageNo);
		int nextPage = 0;
		if(!qnaService.selectMemberQnAList(dto.getId(), pageNo+1).isEmpty())
			nextPage = pageNo+1;
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("nextPage", nextPage);
		return new ResponseEntity(map,HttpStatus.OK);
	}
	
	@RequestMapping("/admin/list")
	public ModelAndView qnaAdminView(
			@RequestParam(name = "pageNo", defaultValue = "1") int pageNo) {
		ModelAndView view = new ModelAndView();
		List<QnADTO> list = qnaService.selectAdminQnAList(pageNo);
		view.addObject("list", list);
		PaggingVO pagging = new  PaggingVO(qnaService.selectQnACount(),	pageNo, 5);
		view.addObject("pagging", pagging);
		view.setViewName("admin_qna");
		return view;
	}
	
	@RequestMapping("/admin/detail/{qno}")
	public ModelAndView adminQnAView(@PathVariable("qno") int qno) {
		ModelAndView view = new ModelAndView();
		view.setViewName("admin_qna_view");
		//해당 문의 상태값을 변경
		qnaService.updateQnAStatus(qno);
		//문의내용 읽어오는 부분
		QnADTO dto = qnaService.selectQnA(qno);
		view.addObject("qna", dto);
		
		return view;
	}
	
	@RequestMapping("/admin/response")
	public String responseQnA(int qno, String response, HttpSession session) {
		MemberDTO dto = (MemberDTO) session.getAttribute("dto");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		response = "답변자 : " + dto.getId() + 
				" 작성일 : "+sdf.format(Calendar.getInstance().getTime()) + "<br>" 
				+ response + "<br>";

		qnaService.updateResponse(qno,response);
		return "redirect:/qna/admin/detail/"+qno;
	}
}
