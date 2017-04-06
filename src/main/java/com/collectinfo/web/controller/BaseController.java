package com.collectinfo.web.controller;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.collectinfo.domain.db.User;
import com.collectinfo.dto.BaseResult;
import com.collectinfo.dto.RemoveDTO;
import com.collectinfo.util.AssertUtil;
import com.collectinfo.util.HttpSessionContext;

public abstract class BaseController {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${error.uri}")
	protected String errorUri;

	protected void refreshUserContext(User user) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(),
				user.getPassword(), user.getAuthorities());
		token.setDetails(user);
		SecurityContextHolder.getContext().setAuthentication(token);
		HttpSessionContext.current().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, null);
	}

	protected <T extends CrudRepository<?, Serializable>> T getMainRepository() {
		throw new UnsupportedOperationException("Please implement this method by your self");
	}

	@RequestMapping("/remove")
	public BaseResult<?> remove(@RequestBody RemoveDTO dto) {
		AssertUtil.isNotEmptyForBusiness(dto.getIds(), "请选择至少一条记录");
		for (Long id : dto.getIds()) {
			getMainRepository().delete(id);
		}
		return new BaseResult<>();
	}

}
