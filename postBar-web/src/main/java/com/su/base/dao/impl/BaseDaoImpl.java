package com.su.base.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;

import com.su.base.callback.PageHibernateCallback;
import com.su.base.dao.BaseDao;
import com.su.base.po.Page;

public class BaseDaoImpl<T> implements BaseDao<T>{
	@Autowired
	protected HibernateTemplate hibernateTemplate;
	
	@Override
	public List<T> queryList(String sql,List<Object> params){
		if(params == null) {
			params = new ArrayList<Object>();
		}
		return (List<T>) hibernateTemplate.find(sql, params.toArray());
	}
	
	@Override
	public void save(T t) {
		hibernateTemplate.save(t);			
	}
	@Override
	public Page<T> queryList(String sql,Page<T> page,List<Object> params){
		if(params == null) {
			params = new ArrayList<Object>();
		}
		//1.
		int count = getResultCount(sql, params);
		page.setTotalRows(count);
		List<T> datas = hibernateTemplate.execute(new PageHibernateCallback<T>(sql, params.toArray(),page.getCurrentPage(), page.getPerPageRows()));
		page.setDatas(datas);
        return page;
	}


	@Override
	public int getResultCount(String sql,List<Object> params) {
		if(params == null) {
			params = new ArrayList<Object>();
		}
		String countSql;
		if(sql.trim().startsWith("select")) {
			countSql = "select count(*) from (" + sql + ")";
		} else {
			countSql = "select count(*) " + sql;
		}
		Long count = (Long) hibernateTemplate.iterate(countSql, params.toArray()).next();
	    return count.intValue();
	}

	@Override
	public void deleteById(Class<T> clazz, int id) {
		T load = hibernateTemplate.load(clazz, id);
		hibernateTemplate.delete(load);
	}

	@Override
	public void update(T t) {
		hibernateTemplate.update(t);
	}
	
	@Override
	public T get(Class<T> clazz, Integer id) {
		return hibernateTemplate.get(clazz, id);
	}

	@Override
	public void executeSql(String sql, List<Object> params) {
		hibernateTemplate.find(sql, params.toArray());
	}
}
