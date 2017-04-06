package com.collectinfo.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.collectinfo.constant.SystemConstant;
import com.collectinfo.dto.BaseResult;
import com.collectinfo.exception.BusinessException;
import com.collectinfo.util.CommonUtil;
import com.collectinfo.util.JacksonUtil;

@Component
public class ErrorHandlerFilter extends GenericFilterBean {

	@Value("${error.uri}")
	protected String errorUri;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			chain.doFilter(request, response);
		} catch (Exception e) {
			String id = CommonUtil.generateErrorId();
			String msg = e.getMessage();
			logger.error("[id=" + id + "] " + msg, e);

			if (!response.isCommitted()) {
				if (ValidationException.class.isAssignableFrom(e.getClass())) {
					if (ConstraintViolationException.class.isAssignableFrom(e.getClass())) {
						msg = "";
						ConstraintViolationException cve = (ConstraintViolationException) e;
						for (ConstraintViolation<?> cv : cve.getConstraintViolations()) {
							msg += cv.getMessage() + "\n";
						}
					}
				} else if (!BusinessException.class.isAssignableFrom(e.getClass())) {
					msg = "系统错误";
				}
				if (request.getContentType() != null && request.getContentType().contains("json")) {
					String result = JacksonUtil.createMapper(false).writeValueAsString(new BaseResult<>(id, msg));
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().print(result);
					response.getWriter().flush();
					response.getWriter().close();
				} else {
					request.setAttribute(SystemConstant.ERROR_CODE_ID_KEY, id);
					request.setAttribute(SystemConstant.ERROR_MSG, msg);
					request.getRequestDispatcher(errorUri + "/500.html").forward(request, response);
				}
			}
		}
	}

}
