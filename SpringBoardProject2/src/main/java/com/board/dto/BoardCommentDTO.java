package com.board.dto;

import org.apache.ibatis.type.Alias;

@Alias("comment")
public class BoardCommentDTO {
	private int cno;
	private int bno;
	private String writer;
	private String cdate;
	private String content;
	private int clike;
	private int chate;
	
	public BoardCommentDTO() {	}

	public BoardCommentDTO(int bno, String writer, String content) {
		this.bno = bno;
		this.writer = writer;
		this.content = content;
	}

	public int getCno() {
		return cno;
	}

	public void setCno(int cno) {
		this.cno = cno;
	}

	public int getBno() {
		return bno;
	}

	public void setBno(int bno) {
		this.bno = bno;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getCdate() {
		return cdate;
	}

	public void setCdate(String cdate) {
		this.cdate = cdate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getClike() {
		return clike;
	}

	public void setClike(int clike) {
		this.clike = clike;
	}

	public int getChate() {
		return chate;
	}

	public void setChate(int chate) {
		this.chate = chate;
	}

	@Override
	public String toString() {
		return "BoardCommentDTO [cno=" + cno + ", bno=" + bno + ", writer=" + writer + ", cdate=" + cdate + ", content="
				+ content + ", clike=" + clike + ", chate=" + chate + "]";
	}
	
	
	
	
	
	
	
}
