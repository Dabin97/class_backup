package com.board.dto;

import org.apache.ibatis.type.Alias;

@Alias("log")
public class LogDTO {
	private String logTime;
	private String writer;
	private String updateData;
	private String runMethod;
	public LogDTO() {	}
	public String getLogTime() {
		return logTime;
	}
	public void setLogTime(String logTime) {
		this.logTime = logTime;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getUpdateData() {
		return updateData;
	}
	public void setUpdateData(String updateData) {
		this.updateData = updateData;
	}
	public String getRunMethod() {
		return runMethod;
	}
	public void setRunMethod(String runMethod) {
		this.runMethod = runMethod;
	}

	


}
