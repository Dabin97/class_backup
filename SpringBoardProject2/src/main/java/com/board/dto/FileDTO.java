package com.board.dto;

import java.io.File;

import org.apache.ibatis.type.Alias;

@Alias("file")
public class FileDTO {
	private String path;
	private String fileName;
	private String type;
	private int bno;
	private int fno;
	public FileDTO() {	}
	public FileDTO(File file, int bno, int fno) {
		this.path = file.getAbsolutePath();
		this.fileName = file.getName();
		switch(fileName.substring(fileName.lastIndexOf('.')+1).toLowerCase()) {
		case "png":
		case "jpg":
		case "bmp":
		case "gif":
			this.type="image";
			break;
		default:
			this.type="normal";				
		}
		this.bno = bno;
		this.fno = fno;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		File file = new File(path);
		this.fileName = file.getName();
		switch(fileName.substring(fileName.lastIndexOf('.')+1).toLowerCase()) {
		case "png":
		case "jpg":
		case "bmp":
		case "gif":
			this.type="image";
			break;
		default:
			this.type="normal";				
		}
		this.path = path;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getBno() {
		return bno;
	}
	public void setBno(int bno) {
		this.bno = bno;
	}
	public int getFno() {
		return fno;
	}
	public void setFno(int fno) {
		this.fno = fno;
	}
	@Override
	public String toString() {
		return "FileDTO [path=" + path + ", fileName=" + fileName + ", type=" + type + ", bno=" + bno + ", fno=" + fno
				+ "]";
	}
	
	
	
	
}







