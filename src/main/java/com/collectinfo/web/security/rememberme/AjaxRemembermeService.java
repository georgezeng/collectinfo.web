package com.collectinfo.web.security.rememberme;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AjaxRemembermeService extends TokenBasedRememberMeServices {

	public AjaxRemembermeService(@Value("${security.rememberme.key}") String key,
			@Autowired UserDetailsService userDetailsService) {
		super(key, userDetailsService);
		setParameter(key);
	}

	@Override
	protected String extractRememberMeCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();

		if ((cookies == null) || (cookies.length == 0)) {
			return null;
		}

		for (Cookie cookie : cookies) {
			if (getCookieName().equals(cookie.getName())) {
				if (cookie.getPath() == null && request.getContextPath().equals("")
						|| request.getContextPath().equals(cookie.getPath())) {
					return cookie.getValue();
				}
			}
		}

		return null;
	}

	@Override
	protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
		boolean toRememberme = super.rememberMeRequested(request, parameter);
		if (!toRememberme) {
			try {
				ObjectMapper obj = new ObjectMapper();
				Map<String, Object> map = obj.readValue(request.getInputStream(), new TypeReference<HashMap<String, Object>>() {
				});
				Boolean value = (Boolean) map.get(getParameter());
				return value != null ? value.booleanValue() : false;
			} catch (Exception e) {
				throw new IllegalArgumentException(e.getMessage(), e);
			}
		}
		return toRememberme;
	}

	@Override
	protected Authentication createSuccessfulAuthentication(HttpServletRequest request, UserDetails user) {
		RememberMeAuthenticationToken auth = (RememberMeAuthenticationToken) super.createSuccessfulAuthentication(request,
				user);
		auth.setDetails(user);
		return auth;
	}
}