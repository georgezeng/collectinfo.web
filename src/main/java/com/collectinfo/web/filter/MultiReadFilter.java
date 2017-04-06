package com.collectinfo.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.collectinfo.web.request.MultiReadHttpServletRequest;

@Component
public class MultiReadFilter extends GenericFilterBean {

	@Value("${multiread.contenttypes}")
	private String contentTypes;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (request.getContentType() != null && contentTypes != null) {
			for (String contentType : contentTypes.split(",\\s*")) {
				if (request.getContentType().toLowerCase().contains(contentType.toLowerCase())) {
					request = new MultiReadHttpServletRequest((HttpServletRequest) request);
					break;
				}
			}
		}
		chain.doFilter(request, response);
	}

}
