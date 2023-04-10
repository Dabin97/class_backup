package com.board.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.board.dto.BoardCommentDTO;
import com.board.dto.BoardDTO;
import com.board.dto.FileDTO;
import com.board.dto.LogDTO;
import com.board.dto.MemberDTO;
import com.board.service.BoardService;
import com.board.vo.PaggingVO;
@Controller
@RequestMapping("/board")
public class BoardController {
	private BoardService boardService;

	public BoardController(BoardService boardService) {
		this.boardService = boardService;
	}
	
	@GetMapping("/main")
	public ModelAndView main(@RequestParam(name = "pageNo", defaultValue = "1") int pageNo) {
		ModelAndView view = new ModelAndView();
		view.setViewName("main");
		// 게시판 글목록
		List<BoardDTO> list = boardService.selectBoardList(pageNo, 7);
		// 페이징 정보
		int count = boardService.selectBoardCount();
		PaggingVO pagging = new PaggingVO(count, pageNo, 7);

		view.addObject("list", list);
		view.addObject("pagging", pagging);

		return view;
	}

	@GetMapping("/write")
	public String boardWriteView() {
		return "board_write_view";
	}

	@PostMapping("/fileupload")
	public ResponseEntity<String> fileUpload(@RequestParam(value = "upload") MultipartFile file,
			HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		// 원본 파일명
		String originFileName = file.getOriginalFilename();
		// upload 경로 설정
		String root = "c:\\fileupload\\";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
		String date = sdf.format(Calendar.getInstance().getTime());

		MemberDTO dto = (MemberDTO) session.getAttribute("dto");
		String fileName = date + "_" + dto.getId() + originFileName.substring(originFileName.lastIndexOf('.'));

		File savefile = new File(root + fileName);
		// 저장한 파일의 경로를 테이블에 저장
		int fno = boardService.uploadImage(savefile.getAbsolutePath());
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			file.transferTo(savefile);
			map.put("uploaded", true);
			map.put("url", "/board/image/" + fno);
			map.put("bi_no", fno);

		} catch (IOException e) {
			map.put("uploaded", false);
			map.put("message", "파일 업로드 중 에러 발생");
		}
		return new ResponseEntity(map, HttpStatus.OK);
	}

	@GetMapping("/image/{fno}")
	public void imageDown(@PathVariable("fno") int fno, HttpServletResponse response) {
		FileDTO dto = boardService.selectImageFile(fno);

		String path = dto.getPath();
		File file = new File(path);
		String fileName = dto.getFileName();

		try {
			fileName = URLEncoder.encode(fileName, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		response.setHeader("Content-Disposition", "attachement;fileName=" + fileName);
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setContentLength((int) file.length());
		try (FileInputStream fis = new FileInputStream(file);
				BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());) {

			byte[] buffer = new byte[1024 * 1024];

			while (true) {
				int size = fis.read(buffer);
				if (size == -1)
					break;
				bos.write(buffer, 0, size);
				bos.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostMapping("/add")
	public String boardWrite(BoardDTO dto, @RequestParam("file") MultipartFile[] file) {
		int bno = boardService.insertBoard(dto);
			
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
				boardService.insertFile(new FileDTO(saveFile, bno, i));
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return "redirect:/board/main";
	}

	@GetMapping("/content/{bno}")
	public ModelAndView boardView(@PathVariable("bno") int bno,HttpSession session) {
		ModelAndView view = new ModelAndView();
		view.setViewName("board_view");
		//게시글 조회수 증가
		HashSet<Integer> set = (HashSet<Integer>) session.getAttribute("history");
		if(set == null) {
			set = new HashSet<Integer>();
			session.setAttribute("history", set);
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
	
	@GetMapping("/filedown")
	public void fileDownload(int bno, int fno, HttpServletResponse response) {
		FileDTO dto = boardService.selectFile(bno, fno);
		try(BufferedOutputStream bos = 
				new BufferedOutputStream(response.getOutputStream());
				FileInputStream fis = new FileInputStream(dto.getPath())){
			byte[] buffer = new byte[1024*1024];
			
			while(true) {
				int count = fis.read(buffer);
				if(count == -1) break;
				bos.write(buffer,0,count);
				bos.flush();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	@PostMapping("/like/{bno}")
	public ResponseEntity<String> boardLike(@PathVariable(name = "bno") int bno, 
			HttpSession session){
		HashMap<String, Object> map = new HashMap<String, Object>();
		MemberDTO dto = (MemberDTO) session.getAttribute("dto");
		
		int result = boardService.insertBoardLike(bno,dto.getId());
		
		if(result == 0)
			map.put("msg", "좋아요를 취소하셨습니다.");
		else
			map.put("msg", "좋아요를 하셨습니다.");
		
		map.put("blike",boardService.selectBoardLike(bno));
		
		return new ResponseEntity(map,HttpStatus.OK);
	}
	@PostMapping("/hate/{bno}")
	public ResponseEntity<String> boardHate(@PathVariable(name = "bno") int bno, 
			HttpSession session){
		HashMap<String, Object> map = new HashMap<String, Object>();
		MemberDTO dto = (MemberDTO) session.getAttribute("dto");
		
		int result = boardService.insertBoardHate(bno,dto.getId());
		
		if(result == 0)
			map.put("msg", "싫어요를 취소하셨습니다.");
		else
			map.put("msg", "싫어요를 하셨습니다.");
		
		map.put("bhate",boardService.selectBoardHate(bno));
		
		return new ResponseEntity(map,HttpStatus.OK);
	}
	
	@GetMapping("/update/view/{bno}")
	public ModelAndView boardUpdateView(@PathVariable(name = "bno") int bno) {
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
	
	@GetMapping("/update")
	public String boardUpdate(BoardDTO dto, String[] del_file,
			@RequestParam("file") MultipartFile[] file) {
		boardService.updateBoard(dto);
		//파일 삭제 - 물리적
		//	삭제할 파일 목록 받기
		if(del_file != null && del_file.length != 0) {
			List<String> filePath = boardService.deleteFileList(dto.getBno(),del_file);
			for(String f : filePath) {
				File dFile = new File(f);
				dFile.delete();
			}
			//파일 삭제 - DB
			boardService.deleteFile(dto.getBno(),del_file);
		}
		//새 첨부파일 업로드처리
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
		return "redirect:/board/content/"+dto.getBno();
	}
	
	@GetMapping("/delete/{bno}")
	public String deleteBoard(@PathVariable(name = "bno") int bno) {
		//첨부파일 목록 조회
		List<FileDTO> fList = boardService.selectFileList(bno);
		//첨부파일 삭제
		for(FileDTO f : fList) {
			File d = new File(f.getPath());
			d.delete();
		}
		boardService.deleteFile(bno, null);
		
		//게시글 삭제
		boardService.deleteBoard(bno);
				
		return "redirect:/main";
	}
	@PostMapping("/comment/add")
	public String appendComment(BoardCommentDTO comment, HttpSession session) {
		//댓글 작성자 정보 추가
		MemberDTO dto = (MemberDTO) session.getAttribute("dto");
		comment.setWriter(dto.getId());
		
		boardService.insertBoardComment(comment);		
		
		return "redirect:/board/content/"+comment.getBno();
	}
	
	@PostMapping("/comment/{mode}/{cno}")
	public ResponseEntity<String> commentLikeHate(HttpSession session,
			@PathVariable(name = "mode")String mode, @PathVariable(name="cno") int cno){
		HashMap<String, Object> map = new HashMap<String, Object>();
		MemberDTO dto = (MemberDTO) session.getAttribute("dto");
		int result = boardService.insertBoardCommentLikeHate(mode,cno,dto.getId());
		String msg = null;
		if(mode.equals("btn_comment_like")) {
			msg = result == 1 ? "해당 댓글에 좋아요 하셨습니다." : "해당 댓글에 좋아요 해제 했습니다.";
		}else {
			msg = result == 1 ? "해당 댓글에 싫어요 하셨습니다." : "해당 댓글에 싫어요 해제 했습니다.";
		}
		map.put("msg", msg);
		
		return new ResponseEntity(map,HttpStatus.OK);
	}
	
	@GetMapping("/comment/delete")
	public String deleteComment(int bno, int cno) {
		boardService.deleteBoardComment(cno);
		return "redirect:/board/content/"+bno;
	}
	
	
	
	

	
}
