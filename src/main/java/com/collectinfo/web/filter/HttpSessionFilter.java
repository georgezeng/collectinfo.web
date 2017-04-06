package com.collectinfo.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.collectinfo.util.HttpSessionContext;

@Component
public class HttpSessionFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			HttpSessionContext.set(((HttpServletRequest) request).getSession());
			chain.doFilter(request, response);
		} finally {
			HttpSessionContext.set(null);
		}
	}

}
