package com.mvc;

public class RegisterDTO {
	private String id;
	private String pass;
	private String name;
	private int age;
	
	
	public RegisterDTO() {	}
	
	
	
	public RegisterDTO(String id, String pass, String name, int age) {
		super();
		this.id = id;
		this.pass = pass;
		this.name = name;
		this.age = age;
	}



	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "RegisterDTO [id=" + id + ", pass=" + pass + ", name=" + name + ", age=" + age + "]";
	}
	
	
	
}
