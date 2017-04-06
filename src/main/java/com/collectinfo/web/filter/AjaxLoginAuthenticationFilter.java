package com.collectinfo.web.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import com.collectinfo.dto.BaseResult;
import com.collectinfo.util.CommonUtil;
import com.collectinfo.util.HttpSessionContext;
import com.collectinfo.util.UserSessionContext;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AjaxLoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	@Autowired
	private RememberMeServices rememberMeServices;

	@Value("${security.login.check}")
	private String url;
	
	@Value("${security.entry.uri}")
	private String entryUri;

	@Override
	public void afterPropertiesSet() {
		setRememberMeServices(rememberMeServices);
		setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(url, "POST"));
		setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				try {
					ObjectMapper mapper = new ObjectMapper();
					String json = mapper.writeValueAsString(new BaseResult<>(entryUri));
					response.setContentType("application/json; charset=utf-8");
					response.getWriter().print(json);
					response.getWriter().flush();
					response.getWriter().close();
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage(), e);
				} finally {
					HttpSessionContext.set(null);
					UserSessionContext.set(null);
				}
			}
		});
		setAuthenticationFailureHandler(new AuthenticationFailureHandler() {

			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException exception) throws IOException, ServletException {
				String id = CommonUtil.generateErrorId();
				logger.error("[id=" + id + "], " + exception.getMessage(), exception);
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(new BaseResult<>(id, exception.getMessage()));
				response.setContentType("application/json; charset=utf-8");
				response.getWriter().print(json);
				response.getWriter().flush();
				response.getWriter().close();
			}
		});
	}

//	@Override
//	protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
//		try {
//			ObjectMapper obj = new ObjectMapper();
//			Map<String, Object> map = obj.readValue(request.getInputStream(), new TypeReference<HashMap<String, Object>>() {
//			});
//			String captcha = (String) map.get("captcha");
//			authRequest.setDetails(captcha);
//		} catch (Exception e) {
//			throw new IllegalArgumentException(e.getMessage(), e);
//		}
//	}

	@Override
	protected String obtainPassword(HttpServletRequest request) {
		try {
			ObjectMapper obj = new ObjectMapper();
			Map<String, Object> map = obj.readValue(request.getInputStream(), new TypeReference<HashMap<String, Object>>() {
			});
			String base64Code = (String) map.get(getPasswordParameter());
			return new String(Base64Utils.decodeFromString(base64Code));
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		}
	}

	@Override
	protected String obtainUsername(HttpServletRequest request) {
		try {
			ObjectMapper obj = new ObjectMapper();
			Map<String, Object> map = obj.readValue(request.getInputStream(), new TypeReference<HashMap<String, Object>>() {
			});
			String base64Code = (String) map.get(getUsernameParameter());
			return new String(Base64Utils.decodeFromString(base64Code));

		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		}
	}

}
