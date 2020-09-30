package com.su.base.po;

import java.util.List;

public class Result<T> {
	private static final int SUCCESS = 0;
	private static final int FAILURE = -1;
	private int code;
	
	private String msg;
	
	private List<T> datas;
	
	private T data;
	
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public List<T> getDatas() {
		return datas;
	}
	public void setDatas(List<T> datas) {
		this.datas = datas;
	}
	public static <T> Result<T> success(T data,List<T> datas) {
		Result<T> result = new Result<>();
		result.setCode(SUCCESS);
		result.setData(data);
		result.setDatas(datas);
		return result;
	}
	public static <T> Result<T> success() {
		Result<T> result = new Result<>();
		result.setCode(SUCCESS);
		result.setMsg("成功！");
		return result;
	}
	public static <T> Result<T> faliure(String msg) {
		Result<T> result = new Result<>();
		result.setCode(FAILURE);
		result.setMsg(msg);
		return result;
	} 
}
