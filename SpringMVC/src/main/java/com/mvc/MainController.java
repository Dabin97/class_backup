package com.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {
	private RegisterService service;
	
	public MainController(RegisterService service) {
		this.service = service;
	}

	@RequestMapping("/") //기본페이지(/)랑 연결하겠다.
	public String main() {
		return "login";
	}
	
	//최초 컨트롤러로 배운 형태
//	@RequestMapping("/login.do")
//	public String login(HttpServletRequest request) {
//		String id = request.getParameter("id");
//		String pass = request.getParameter("pass");
//		
//		HttpSession session = request.getSession();
//		session.setAttribute("id", id);
//		session.setAttribute("pass", pass);
//		
//		return "login_result";
//	}
	
	//세션을 역주입으로 받아옴
//	@RequestMapping("/login.do")
//	public String login(HttpServletRequest request, HttpSession session) {
//		String id = request.getParameter("id");
//		String pass = request.getParameter("pass");
//		
//		session.setAttribute("id", id);
//		session.setAttribute("pass", pass);
//		
//		return "login_result";
//	}
	
	//파라미터값을 받아오는 방법 
//	@RequestMapping("/login.do")
//	public String login(@RequestParam(name="id", defaultValue = "user")String id, 
//			@RequestParam(name="pass", defaultValue = "123456")String pass,
//			HttpSession session) {
//		
//		session.setAttribute("id", id);
//		session.setAttribute("pass", pass);
//		
//		return "login_result";
//	}
	
	
	@RequestMapping("/login.do")
	public String login(String id, String pass,HttpSession session) { //디폴트값을 주지않고 그냥 로그인버튼을 누르면 빈 문자열이 들어온다.
		System.out.println(id + " : " + pass);
		session.setAttribute("id", id);
		session.setAttribute("pass", pass);
		
		return "login_result";
	}
	
	//registerView.do
	//register.jsp 페이지로 이동

	@RequestMapping("/registerView.do")
	public String registerView() { 
		return "register";	
	}
	
	//register.do
	//회원 가입정보를 받아서 가입정보를 저장할 RegisterDTO 클래스 작성을 해서
	//request 영역에 저장, 이동할 페이지는 regiter_result.jsp로 이동
	//request영역에 객치를 저장할 때는 이름을 dto로 저장
	
//	@RequestMapping("/register.do")
//	public String register(RegisterDTO dto, HttpServletRequest request) { 
//		request.setAttribute("dto", dto); 
//		return "register_result";	
//	}

	//ModelAndView 추가
	@RequestMapping("/register.do")
	public ModelAndView register(RegisterDTO dto, ModelAndView model) { 
		model.addObject("dto",dto);
		model.setViewName("register_result");
		service.testService();
		return model;	
	}
	
	
	
	
	
	
	
	
	
	
}
