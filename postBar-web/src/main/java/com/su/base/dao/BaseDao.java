package com.su.base.dao;

import java.util.List;

import com.su.base.po.Page;

public interface BaseDao<T> {
	
	public List<T> queryList(String sql,List<Object> params);
	
	public void save(T t);
	
	public Page<T> queryList(String sql,Page<T> page,List<Object> params);
	
	public int getResultCount(String sql,List<Object> params);
	
	void deleteById(Class<T> clazz, int id);
	
	void update(T t);
	
	T get(Class<T> clazz, Integer id);
	
	void executeSql(String sql,List<Object> params);
}
