package com.collectinfo.web.security.voter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;

public class SecurityVoter implements AccessDecisionVoter<FilterInvocation> {
	@Override
	public int vote(Authentication authentication, FilterInvocation fi, Collection<ConfigAttribute> attributes) {
		List<SecurityConfig> configs = findSecurityConfigs(attributes);
		for (SecurityConfig config : configs) {
			for (GrantedAuthority auth : authentication.getAuthorities()) {
				if (auth.getAuthority().equals(config.getAttribute())) {
					return ACCESS_GRANTED;
				}
			}
		}
		return ACCESS_DENIED;
	}

	private List<SecurityConfig> findSecurityConfigs(Collection<ConfigAttribute> attributes) {
		List<SecurityConfig> configs = new ArrayList<SecurityConfig>();
		for (ConfigAttribute attribute : attributes) {
			if (attribute instanceof SecurityConfig) {
				configs.add((SecurityConfig) attribute);
			}
		}
		return configs;
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return attribute instanceof SecurityConfig;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}
}
