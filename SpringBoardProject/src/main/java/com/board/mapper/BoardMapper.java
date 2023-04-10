package com.board.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.board.dto.BoardCommentDTO;
import com.board.dto.BoardDTO;
import com.board.dto.FileDTO;
@Mapper
public interface BoardMapper {

	List<BoardDTO> selectBoardList(HashMap<String, Object> map);
	int selectBoardCount();
	int selectImageFileNo();
	int insertBoardImage(HashMap<String, Object> map);
	FileDTO selectImageFile(int fno);
	int insertBoard(BoardDTO dto);
	int selectBoardBno();
	int insertFile(FileDTO fileDTO);
	BoardDTO selectBoard(int bno);
	List<FileDTO> selectFileList(int bno);
	List<BoardCommentDTO> selectCommentList(int bno);
	FileDTO selectFile(HashMap<String, Object> map);
	void updateBoardCount(int bno);
	int insertBoardLike(HashMap<String, Object> map);
	int deleteBoardLike(HashMap<String, Object> map);
	int selectBoardLike(int bno);
	int insertBoardHate(HashMap<String, Object> map);
	void deleteBoardHate(HashMap<String, Object> map);
	int selectBoardHate(int bno);
	int updateBoard(BoardDTO dto);
	List<String> deleteFileList(HashMap<String, Object> map);
	int deleteFile(HashMap<String, Object> map);
	void deleteBoard(int bno);
	int insertBoardComment(BoardCommentDTO comment);
	int insertCommentLikeHate(HashMap<String, Object> map);
	int deleteCommentLikeHate(HashMap<String, Object> map);
	int deleteBoardComment(int cno);



}
