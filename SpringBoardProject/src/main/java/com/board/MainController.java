package com.board;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.board.dto.BoardCommentDTO;
import com.board.dto.BoardDTO;
import com.board.dto.FileDTO;
import com.board.dto.MemberDTO;
import com.board.dto.QnADTO;
import com.board.service.BoardService;
import com.board.service.GradeService;
import com.board.service.MemberService;
import com.board.service.QnAService;
import com.board.vo.PaggingVO;

@Controller
public class MainController {
	private MemberService memberService;
	private BoardService boardService;
	private QnAService qnaService;
	private GradeService gradeService;

	

	public MainController(MemberService memberService, BoardService boardService, QnAService qnaService,
			GradeService gradeService) {
		this.memberService = memberService;
		this.boardService = boardService;
		this.qnaService = qnaService;
		this.gradeService = gradeService;
	}

	@RequestMapping("/")
	public String main() {
		return "index";
	}
	
	@RequestMapping("/member/register/view")
	public String memberRegisterView() {
		return "member_register";
	}
	
	@RequestMapping("/login")
	public String login(String id, String passwd, HttpSession session) {
		MemberDTO dto = memberService.login(id, passwd);
		session.setAttribute("dto", dto);		
		return "redirect:/main";
	}

	
	@RequestMapping("/main")
	public ModelAndView main(@RequestParam(name = "pageNo", defaultValue = "1")int pageNo) {
		ModelAndView view = new ModelAndView();
		view.setViewName("main");
		//게시판 글목록
		List<BoardDTO> list = boardService.selectBoardList(pageNo, 7);
		
		//페이징 정보
		int count = boardService.selectBoardCount();
		PaggingVO pagging = new PaggingVO(count, pageNo, 7);
		
		view.addObject("list",list); 
		view.addObject("pagging",pagging); 
		
		return view;
	}
	
	@RequestMapping("/board/write")
	public String boardWriteView() {
		return "board_write_view";
	}


	@RequestMapping("/fileUpload") //UploadAdapter.js에 해당 경로가 연결되어있다. 그리고 해당 js는 board_write_view.html에 제이쿼리로 적용되어있다.
	public ResponseEntity<String> fileUpload(@RequestParam(value="upload") MultipartFile file, 
			HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		//원본 파일명
		String originFileName = file.getOriginalFilename();
		//upload 경로 설정
		String root = "c:\\fileupload\\";
		
		//저장할 파일명
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
		String date = sdf.format(Calendar.getInstance().getTime());		
		
		MemberDTO dto = (MemberDTO)session.getAttribute("dto");
		String fileName = date + "_" + dto.getId() + originFileName.substring(originFileName.lastIndexOf('.'));
		System.out.println("저장할 파일명 : " + fileName);
		
		File savefile =  new File(root + fileName); 
		//저장한 파일의 경로를 테이블에 저장
		int fno = boardService.uploadImage(savefile.getAbsolutePath());
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			file.transferTo(savefile); //파일 업로드
			map.put("uploaded", true);
			map.put("url", "/image/"+fno);
			map.put("bi_no", fno); //만든 파일 경로를 bi_no로 보냄. uploadAdater.js에서 bi_no는 파일번호이고 json객체로 받아야하기때문에 hashmap으로 보냄
			
		} catch (IOException e) {
			map.put("uploaded", false);
			map.put("message", "파일 업로드 중 에러 발생");
		}
		return new ResponseEntity(map,HttpStatus.OK);
	}
	
	@RequestMapping("/image/{fno}")
	public void imageDown(@PathVariable("fno") int fno, HttpServletResponse response) {
		FileDTO dto = boardService.selectImageFile(fno);
		
		String path = dto.getPath();
		File file = new File(path);
		String fileName = dto.getFileName();
		
		try {
			fileName = URLEncoder.encode(fileName,"utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		response.setHeader("Content-Disposition", "attachement;fileName="+fileName);
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setContentLength((int)file.length());
		try(FileInputStream fis = new FileInputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());) {
			
			byte[] buffer = new byte[1024*1024];
			
			while(true) {
				int size = fis.read(buffer);
				if(size == -1) break;
				bos.write(buffer,0,size);
				bos.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/board/add")
	public String boardWrite(BoardDTO dto, @RequestParam("file") MultipartFile[] file) {
			int bno = boardService.insertBoard(dto);
			
			//파일 업로드할 경로 설정
			String root = "c:\\fileupload\\";
			//현재 날짜 시간
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
			String date = sdf.format(Calendar.getInstance().getTime());
			
			for(int i=0;i<file.length;i++) {
				if(file[i].getSize() == 0) continue; 
				//서버에 파일을 저장할때 파일명을 날짜시간으로 변경
				//DB에 저장할 때는 원본파일명과 변경된 파일명 모두 저장
				//원본 파일명 뽑음
				String originFileName = file[i].getOriginalFilename();
				//저장할 파일명
				String fileName = date + "_" + i  + originFileName.substring(originFileName.lastIndexOf('.'));
				System.out.println("저장할 파일명 : " + fileName);
				
				//실제 파일이 업로드 되는 부분
				try {
					File saveFile = new File(root + fileName); 
					file[i].transferTo(saveFile);
					boardService.insertFile(new FileDTO(saveFile, bno, i));
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
				
		return "redirect:/main";
	}
	
	
	@RequestMapping("/board/content/{bno}") //detail 
	public ModelAndView boardView(@PathVariable("bno") int bno, HttpSession session) {
		ModelAndView view = new ModelAndView();
		view.setViewName("board_view");
		//게시글 조회수 증가 - boardViewController 참고
		HashSet<Integer> set = (HashSet<Integer>) session.getAttribute("history"); //id당 조회수가 겹치면 안되니까 Hashset사용
		if(set == null ) {
			set = new HashSet<Integer>();
			session.setAttribute("history",set);
		}
		if(set.add(bno)) boardService.updateBoardCount(bno);
		
		//게시글 조회
		BoardDTO board = boardService.selectBoard(bno);
		//첨부파일 목록 조회
		List<FileDTO> fList = boardService.selectFileList(bno);
		//댓글 목록 조회
		List<BoardCommentDTO> cList = boardService.selectCommentList(bno);
		
		view.addObject("board", board);
		view.addObject("fList", fList);
		view.addObject("cList", cList);
		
		return view;
	}

	
	@RequestMapping("/filedown") //borad_view 첨부파일 목록 출력
	public void fileDown(int bno, int fno, HttpServletResponse response) { //되돌려줄것없이 write로 뿌릴것만 있으므로 void
		FileDTO dto = boardService.selectFile(bno, fno);	//fileUpload와 중간은 비슷함, bno와 fno를 둘다 보냄줌
		
		try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream()); 
			 FileInputStream fis = new FileInputStream(dto.getPath());) {

			byte[] buffer = new byte[1024 * 1024];

			while (true) {
				int count = fis.read(buffer);
				if (count == -1) break;
				bos.write(buffer, 0, count);
				bos.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/board/like/{bno}")
	public ResponseEntity<String> boardCotentLike(@PathVariable(name ="bno") int bno,HttpSession session) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		MemberDTO dto = (MemberDTO) session.getAttribute("dto");
		
		int result = boardService.insertBoardLike(bno, dto.getId());
		if(result == 0)
			map.put("msg", "해당 게시글에 좋아요를 해제하셨습니다.");
		else
			map.put("msg", "해당 게시글에 좋아요를 하셨습니다.");
		
		map.put("blike", boardService.selectBoardLike(bno));
		
		return new ResponseEntity(map, HttpStatus.OK);
	}
	
	@RequestMapping("/board/hate/{bno}")
	public ResponseEntity<String> boardCotentHate(@PathVariable(name ="bno") int bno,HttpSession session) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		MemberDTO dto = (MemberDTO) session.getAttribute("dto");
		
		int result = boardService.insertBoardHate(bno, dto.getId());
		if(result == 0)
			map.put("msg", "해당 게시글에 싫어요를 해제하셨습니다.");
		else
			map.put("msg", "해당 게시글에 싫어요를 하셨습니다.");
		
		map.put("bhate", boardService.selectBoardHate(bno));
		
		return new ResponseEntity(map, HttpStatus.OK);
	}
	
	@RequestMapping("/board/update/view/{bno}") //boardView메소드와 동일 필요없는것만 제거
	public ModelAndView boardUpdateView(@PathVariable(name ="bno")int bno) {
		ModelAndView view = new ModelAndView();
		view.setViewName("board_update_view");

		//게시글 조회
		BoardDTO board = boardService.selectBoard(bno);
		//첨부파일 목록 조회
		List<FileDTO> fList = boardService.selectFileList(bno);
		
		view.addObject("board", board);
		view.addObject("fList", fList);
		
		return view;
	}
	
	@RequestMapping("/board/update")
	public String boardUpdate(BoardDTO dto, String[] del_file, @RequestParam("file") MultipartFile[] file) { //del_file : 삭제할 파일번호, 여러개받을거라 배열로
		boardService.updateBoard(dto);
		//파일 삭제 - 물리적
		//삭제할 파일 목록 받기 -- del_file에 아무것도 없을때 터지므로 if문으로 걸러준다.
		if(del_file != null && del_file.length != 0) { 
			List<String> filePath = boardService.deleteFileList(dto.getBno(),del_file);
			for(String f : filePath) {
				File dFile = new File(f);
				dFile.delete();
				}
			//파일 삭제 - DB
			boardService.deleteFile(dto.getBno(),del_file);
		}
		
		//새 첨부파일 업로드처리(board-mapper insertFile의 메소드에 서브쿼리 추가함) -boardWrite를 복사해온다.
		// 파일 업로드할 경로 설정
		String root = "c:\\fileupload\\";
		// 현재 날짜 시간
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
		String date = sdf.format(Calendar.getInstance().getTime());
		// 저장한 파일경로

		for (int i = 0; i < file.length; i++) {
			if (file[i].getSize() == 0)
				continue;
			// 서버에 파일을 저장할때 파일명을 날짜시간으로 변경
			// DB에 저장할 때는 원본파일명과 변경된 파일명 모두 저장
			// 원본 파일명 뽑음
			String originFileName = file[i].getOriginalFilename();
			// 저장할 파일명
			String fileName = date + "_" + i + originFileName.substring(originFileName.lastIndexOf('.'));
			System.out.println("저장할 파일명 : " + fileName);

			try {
				// 실제 파일이 업로드 되는 부분
				File saveFile = new File(root + fileName);
				file[i].transferTo(saveFile);
				boardService.insertFile(new FileDTO(saveFile, dto.getBno(), 0));
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "redirect:/board/content/" + dto.getBno();
	}

	
	@RequestMapping("/board/delete/{bno}") //게시글 첨부파일 댓글삭제 모두 
	public String deleteBoard(@PathVariable(name ="bno")int bno) {
		//첨부파일 목록 조회
		List<FileDTO> fList = boardService.selectFileList(bno);
		//첨부파일 삭제 - bno가 외래키로 설정이 되어있다면 bno만 지워도 다 같이 지워질것이다.
		for(FileDTO f : fList) {
			File d = new File(f.getPath());
			d.delete();
		}
		boardService.deleteFile(bno, null); //d를 null로 넣고 deleteFile sql문을 고친다.
		
		//게시글 삭제
		boardService.deleteBoard(bno);
		return "redirect:/main";
		//bno삭제시 comment도 삭제되도록 외래키로 묶어줌. 그러나 파일삭제는 외래키지정으로는 안되고 따로 메인컨트롤러 메소드에서 설정해주어야한다.
	}
	

	@RequestMapping("/comment/add")
	public String appendComment(BoardCommentDTO comment, HttpSession session) {
		//댓글 작성자 정보 추가
		MemberDTO dto = (MemberDTO)session.getAttribute("dto");
		comment.setWriter(dto.getId());
		
		boardService.insertBoardComment(comment);
		return "redirect:/board/content/" + comment.getBno();
	}
	
	
	
	@RequestMapping("/comment/{mode}/{cno}")
	public ResponseEntity<String> commentLikeHate(@PathVariable(name ="cno") int cno,@PathVariable(name ="mode") String mode,
			HttpSession session) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		MemberDTO dto = (MemberDTO)session.getAttribute("dto");
		int result = boardService.insertBoardCommentLikeHate( mode, cno,dto.getId()); //result를 boolean말고 int로 함
		String msg = null;
		if(mode.equals("btn_comment_like")) {
			msg = result == 1 ? "해당 댓글에 좋아요 하였습니다." : "해당 댓글에 좋아요를 해제했습니다."; 
		}else {
			msg = result == 1 ? "해당 댓글에 싫어요 하였습니다." : "해당 댓글에 싫어요를 해제했습니다."; //.?
		}
		map.put("msg",msg);
		
		return new ResponseEntity(map, HttpStatus.OK);
		}
		
	@RequestMapping("/comment/delete")
	public String deleteComment(int bno, int cno){
		boardService.deleteBoardComment(cno);
		return "redirect:/board/content/" + bno;
	}
	
	@RequestMapping("/logout")
	public String logout(HttpSession session){
		session.invalidate();
		return "redirect:/"; //index로 보내서 오류뜸
	}
	

	@RequestMapping("/qna/member")
	public ModelAndView qnaMemberView(HttpSession session,@RequestParam(name ="pageNo", defaultValue = "1")int pageNo) {
		ModelAndView view = new ModelAndView();
		MemberDTO dto = (MemberDTO)session.getAttribute("dto");
		List<QnADTO> list = qnaService.selectMemberQnAList(dto.getId(), pageNo);
		view.addObject("list", list);
		view.setViewName("member_qna");
		return view;
	}

	@RequestMapping("/qna/member/register")
	public String qnaMemberRegister(QnADTO dto) {
		qnaService.insertMemberQnA(dto);
		return "redirect:/qna/member";
	}
	
	
	@RequestMapping("/qna/member/more")
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

	@RequestMapping("/qna/admin")
	public ModelAndView qnaAdminView(@RequestParam(name ="pageNo", defaultValue = "1")int pageNo) {
		ModelAndView view = new ModelAndView();
		List<QnADTO> list = qnaService.selectAdminQnAList(pageNo);
		view.addObject("list", list);
		PaggingVO pagging = new PaggingVO(qnaService.selectQnACount(), pageNo, 5);
		view.addObject("pagging",pagging); 
		view.setViewName("admin_qna");
		return view;
	}
	
	@RequestMapping("/qna/admin/detail/{qno}") //admin_qna와 연결 해당 게시글을 눌렀을때 admin_qna_view 이동 + status변경
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
	
	@RequestMapping("/qna/admin/response") //admin_qna_view와 연결, 답변자의 정보와 답변내용 전송
	public String responseQnA(int qno, String response, HttpSession session) {
		MemberDTO dto = (MemberDTO) session.getAttribute("dto");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		response = "답변자 : " + dto.getId() + 
				" 작성일 : "+sdf.format(Calendar.getInstance().getTime()) + "<br>" 
				+ response + "<br>";

		qnaService.updateResponse(qno,response);
		return "redirect:/qna/admin/detail/"+qno; //답변완료하면 detail화면으로 이동
	}
	
	@RequestMapping("/grade/view") //header 등급관리에 연결 - 모든 등급 select
	public ModelAndView gradeView(ModelAndView mv) {
		ArrayList<HashMap<String, Object>> list = gradeService.selectAllGrade();
		System.out.println(list.toString());
		mv.setViewName("grade_manage"); //grade_manage로 이동
		mv.addObject("list", list);
		return mv;
	}
	
	@RequestMapping("/grade/add") //grade_manage - #register_grade 추가부분
	public ResponseEntity<String> gradeAppend(@RequestParam(name = "grade_no") String gradeNo, 
			@RequestParam(name = "grade_name")String gradeName){
		
		int result = gradeService.insertGrade(gradeNo,gradeName);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("code", result);  //ajax에서 결과를 code로 받음 
		return new ResponseEntity(map,HttpStatus.OK);
	}
	
	@RequestMapping("/grade/search") //grade_manage - search 부분
	public ResponseEntity<String> selectGradeList(String val){
		ArrayList<HashMap<String, Object>> list = gradeService.selectGrade(val);
		
		return new ResponseEntity(list,HttpStatus.OK);
	}
	
	@RequestMapping("/grade/delete/{grade_no}") //grade_manage - delete 부분
	public ResponseEntity<String> deleteGrade(@PathVariable("grade_no") int grade_no){
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("code", gradeService.deleteGrade(grade_no));
		return new ResponseEntity(map,HttpStatus.OK);
	}
	
	@RequestMapping("/grade/update") //grade_manage - update 부분
	public ResponseEntity<String> updateGrade(int grade_no, String grade_name){
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("code", gradeService.updateGrade(grade_no,grade_name));
		return new ResponseEntity(map,HttpStatus.OK);
	}
	
	@RequestMapping("/member/admin") //header회원관리경로 -> admin_member_manage list출력부분
	public ModelAndView memberManageView(ModelAndView view) {
		List<MemberDTO> list = memberService.selectAllMember();
		view.addObject("list", list);
		view.setViewName("admin_member_manage");
		return view;
	}
	
	@RequestMapping("/register/view")
	public String registerView() {
		return "member_register";
	}
	
	@RequestMapping("/register/action") //추가버튼을 눌렀을때 나오는 url?
	public String register(MemberDTO dto) {
		int result = memberService.insertMember(dto);
		return "redirect:/main";
	}
	
	@RequestMapping("/member/admin/register/view")
	public String registerAdminView() {
		return "admin_member_register";
	}
	
	@RequestMapping("/member/admin/register/action") //admin_member_register
	public String registerAdmin(MemberDTO dto) {
		int result = memberService.insertMember(dto);
		return "redirect:/member/admin";
	}
	
	@RequestMapping("/delete/{id}") // admin_member_manage delete 부분
	public ResponseEntity<String> delete(@PathVariable String id) {
		int result = memberService.deleteMember(id);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("count", String.valueOf(result)); //ajax에서 결과 문자열을 count로 받음
		if(result != 0) {
			map.put("message", "데이터 삭제 성공");
		}else {
			map.put("message", "데이터 삭제 실패");
		}
		return new ResponseEntity(map,HttpStatus.OK);
	}
	
	@RequestMapping("/member/search") // admin_member_manage search 부분 - 검색할것을 입력받으면 부분일치하더라도 출력함
	public ResponseEntity<String> searchMember(String kind, String search){
		List<MemberDTO> list = memberService.searchMember(kind,search);
		return new ResponseEntity(list,HttpStatus.OK);
	}
	
	
	@RequestMapping("/member/admin/detail/{id}") // admin_member_manage 해당 유저의 id를 누르면 detail페이지로 이동(디테일페이지=유저 정보를 수정할수있는 페이지)
	public ModelAndView memberDetailView(@PathVariable String id, ModelAndView mv) {
		MemberDTO dto = memberService.selectMember(id);
		mv.addObject("dto", dto);
		mv.setViewName("admin_member_view");
		return mv;
	}

	
	@RequestMapping("/member/admin/update") //유저 정보 수정완료버튼을 누르면 수정이 되고, 수정된 유저정보가 반영된 리스트테이블로 다시 이동
	public String update(MemberDTO dto) {
		int result = memberService.updateMember(dto);
		return "redirect:/member/admin";
	}

		
	}

