package com.collectinfo.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.collectinfo.domain.db.base.BaseUser;
import com.collectinfo.util.UserSessionContext;

@Component
public class UserSessionFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication.getDetails() != null
					&& BaseUser.class.isAssignableFrom(authentication.getDetails().getClass())) {
				BaseUser current = (BaseUser) authentication.getDetails();
				UserSessionContext.set(current);
			}
		}
		try {
			chain.doFilter(request, response);
		} finally {
			UserSessionContext.set(null);
		}
	}

}
