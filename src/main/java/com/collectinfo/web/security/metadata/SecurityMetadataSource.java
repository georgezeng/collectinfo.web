package com.collectinfo.web.security.metadata;

import java.util.Collection;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import com.collectinfo.domain.db.Authority;
import com.collectinfo.domain.redis.RedisAuthority;
import com.collectinfo.repository.jpa.api.AuthorityRepository;
import com.collectinfo.repository.redis.api.RedisAuthorityRepository;

@Component
public class SecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
	@Value("${superadmin.authority}")
	private String superAdminAuth;

	@Autowired
	private RedisAuthorityRepository redisAuthorityRepository;

	@Autowired
	private AuthorityRepository authorityRepository;

	private FilterInvocationSecurityMetadataSource original;

	public void setOriginal(FilterInvocationSecurityMetadataSource original) {
		this.original = original;
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		FilterInvocation fi = (FilterInvocation) object;
		RedisAuthority redisAuth = redisAuthorityRepository.findByUri(fi.getHttpRequest().getRequestURI());
		if (redisAuth != null) {
			return SecurityConfig.createList(redisAuth.getName(), superAdminAuth);
		}
		Authority auth = authorityRepository.findByUri(fi.getHttpRequest().getRequestURI());
		if (auth != null) {
			redisAuth = new RedisAuthority();
			BeanUtils.copyProperties(auth, redisAuth);
			redisAuthorityRepository.save(redisAuth);
			return SecurityConfig.createList(redisAuth.getName(), superAdminAuth);
		}
		return this.original.getAttributes(object);
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return this.original.getAllConfigAttributes();
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return this.original.supports(clazz);
	}

}