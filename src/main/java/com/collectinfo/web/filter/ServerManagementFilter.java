package com.collectinfo.web.filter;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class ServerManagementFilter extends GenericFilterBean {
	@Value("${management.context-path}")
	private String managementContextPath;

	@Value("${superadmin.username}")
	private String adminUsername;

	@Value("${superadmin.password}")
	private String adminPassword;

	@Autowired
	private PasswordEncoder encoder;

	private AntPathRequestMatcher matcher;

	@PostConstruct
	public void init() {
		matcher = new AntPathRequestMatcher(managementContextPath + "/**");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest) request;
		if (matcher.matches(httpReq)) {
			if (!httpReq.getMethod().equalsIgnoreCase("post") || !(adminUsername.equals(request.getParameter("username"))
					&& encoder.matches(request.getParameter("password").replaceAll("-", ""), adminPassword))) {
				((HttpServletResponse) response).sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
		}
		chain.doFilter(request, response);
	}

}
