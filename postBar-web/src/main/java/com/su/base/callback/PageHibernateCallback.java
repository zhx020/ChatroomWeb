package com.su.base.callback;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;

public class PageHibernateCallback <T> implements HibernateCallback<List<T>>{
    private String hql;
    private Object[] params;
    private int currentPage;
    private int perPageRows;
    
    public PageHibernateCallback(String hql, Object[] params,
            int currentPage, int perPageRows) {
        super();
        this.hql = hql;
        this.params = params;
        this.currentPage = currentPage;
        this.perPageRows = perPageRows;
    }
    public List<T> doInHibernate(Session session) throws HibernateException {
        Query query = session.createQuery(hql);
        if(params != null){
            for(int i = 0 ; i < params.length ; i ++){
                query.setParameter(i, params[i]);
            }
        }
        query.setFirstResult(getStartIndex());
        query.setMaxResults(perPageRows);
        return query.list();
    }
    
    public int getStartIndex() {
    	return (currentPage - 1) * perPageRows ;
    }

}