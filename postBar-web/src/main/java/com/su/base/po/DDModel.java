package com.su.base.po;

public class DDModel {
	private int code;
	private String scode;
	private String name;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public DDModel(int code, String name) {
		super();
		this.code = code;
		this.name = name;
	}
	public DDModel(String scode, String name) {
		super();
		this.scode = scode;
		this.name = name;
	}
	public String getScode() {
		return scode;
	}
	public void setScode(String scode) {
		this.scode = scode;
	}
}	
