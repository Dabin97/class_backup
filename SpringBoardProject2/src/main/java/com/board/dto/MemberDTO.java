package com.board.dto;

import org.apache.ibatis.type.Alias;

@Alias("member")
public class MemberDTO {
	private String id;
	private String passwd;
	private String name;
	private String nick;
	private int gradeNo;
	//생성자
	public MemberDTO() {}
	public MemberDTO(String id, String passwd, String name, String nick, int gradeNo) {
		this.id = id;
		this.passwd = passwd;
		this.name = name;
		this.nick = nick;
		this.gradeNo = gradeNo;
	}
	//setter/getter
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public int getGradeNo() {
		return gradeNo;
	}
	public void setGradeNo(int gradeNo) {
		this.gradeNo = gradeNo;
	}
	//toString
	@Override
	public String toString() {
		return "MemberDTO [id=" + id + ", passwd=" + passwd + ", name=" + name + ", nick=" + nick + ", gradeNo="
				+ gradeNo + "]";
	}
	
	
}
