package com.board.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.board.dto.BoardCommentDTO;
import com.board.dto.BoardDTO;
import com.board.dto.FileDTO;
import com.board.mapper.BoardMapper;
@Service
public class BoardService {
	private BoardMapper mapper;

	public BoardService(BoardMapper mapper) {
		this.mapper = mapper;
	}

	public List<BoardDTO> selectBoardList(int pageNo, int i) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("pageNo",pageNo);
		map.put("contentCount", i);
		return mapper.selectBoardList(map);
	}

	public int selectBoardCount() {
		return mapper.selectBoardCount();
	}

	public int uploadImage(String absolutePath) {
		//이미지 파일 번호 시퀸스로 생성한 결과를 받아옴
		int fno = mapper.selectImageFileNo();
		//받아온 파일 경로를 board_image 폴더 저장
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("fno", fno);
		map.put("path", absolutePath);
		mapper.insertBoardImage(map);
		return fno;
	}

	public FileDTO selectImageFile(int fno) {
		return mapper.selectImageFile(fno);
	}

	public int insertBoard(BoardDTO dto) {
		int bno = mapper.selectBoardBno();
		dto.setBno(bno);
		mapper.insertBoard(dto);
		return bno; 
	}

	public int insertFile(FileDTO fileDTO) {
		return mapper.insertFile(fileDTO);
	}

	public BoardDTO selectBoard(int bno) {
		return mapper.selectBoard(bno);
	}

	public List<FileDTO> selectFileList(int bno) {
		return mapper.selectFileList(bno);
	}

	public List<BoardCommentDTO> selectCommentList(int bno) {
		return mapper.selectCommentList(bno);
	}

	public FileDTO selectFile(int bno, int fno) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("bno",bno);
		map.put("fno", fno);
		return mapper.selectFile(map);
	}

	public void updateBoardCount(int bno) {
		mapper.updateBoardCount(bno);
	}

	public int insertBoardLike(int bno, String id) {
		int r = 0;
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("bno",bno);
		map.put("id",id);
		try {
		r = mapper.insertBoardLike(map);
		}catch(Exception e){
		mapper.deleteBoardLike(map);
		}
		
		return r;
	}

	public int selectBoardLike(int bno) {
		return mapper.selectBoardLike(bno);
	}

	public int insertBoardHate(int bno, String id) {
		int r = 0;
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("bno",bno);
		map.put("id",id);
		try {
		r = mapper.insertBoardHate(map);
		}catch(Exception e){
		mapper.deleteBoardHate(map);
		}
		
		return r;
	}

	public int selectBoardHate(int bno) {
		return mapper.selectBoardHate(bno);
	}

	public int updateBoard(BoardDTO dto) {
		return mapper.updateBoard(dto);
	}

	public List<String> deleteFileList(int bno, String[] del_file) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("bno",bno);
		map.put("fno",del_file);
		return mapper.deleteFileList(map);
	}

	public int deleteFile(int bno, String[] del_file) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("bno",bno);
		map.put("fno",del_file);
		return mapper.deleteFile(map);
	}

	public void deleteBoard(int bno) {
		mapper.deleteBoard(bno);
	}

	public int insertBoardComment(BoardCommentDTO comment) {
		return mapper.insertBoardComment(comment);
	}

	public int insertBoardCommentLikeHate(String mode, int cno, String id) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("cno", cno);
		map.put("mode", mode);
		map.put("writer", id);
		int result = 0;
		try {
			//댓글 좋아요/싫어요 설정
			result = mapper.insertCommentLikeHate(map);
		}catch (Exception e) {
			//댓글 좋아요/싫어요 해제
			mapper.deleteCommentLikeHate(map);
		}
		return result;
	}

	public int deleteBoardComment(int cno) {
		return mapper.deleteBoardComment(cno);
	}

	

 
	

	




}
