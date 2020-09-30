package com.su.base.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class SessionFilter implements Filter{
	
	private static ThreadLocal<Long> users;
	private Set<String> passUrls;
	
	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
        Object user = httpRequest.getSession().getAttribute("user");
        if(user != null){
            users.set((Long)user);
        } else {
        	String requestPath = httpRequest.getServletPath();
        	if(!passUrls.contains(requestPath)) {
//        		throw new RuntimeException("暂未登陆！");
        	}
        }
        chain.doFilter(httpRequest,response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		passUrls = new HashSet<>();
		users = new ThreadLocal<>();
		String strPassUrls = filterConfig.getInitParameter("passUrls");
		if(StringUtils.isNotBlank(strPassUrls)) {
			String[] ss = strPassUrls.split(",");
			for(int i = 0;i < ss.length;i ++) {
				passUrls.add(ss[i]);
			}
		}
	}
	
	public static Long getUserId(){
        return users.get();
    }
}
