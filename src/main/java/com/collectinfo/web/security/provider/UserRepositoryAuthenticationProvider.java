package com.collectinfo.web.security.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.collectinfo.util.AssertUtil;
import com.collectinfo.util.HttpSessionContext;
import com.google.code.kaptcha.Constants;

@Service
public class UserRepositoryAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserDetailsService service;

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		AssertUtil.isNotEmptyForBusiness(username, "用户名不能为空");
		AssertUtil.isNotEmptyForBusiness((String) authentication.getCredentials(), "密码不能为空");
		UserDetails user = service.loadUserByUsername(username);
		if (user == null) {
			throw new BadCredentialsException("用户名或密码有误");
		}
		if (!user.isEnabled()) {
			throw new BadCredentialsException("用户状态异常");
		}
		String rawPassword = (String) authentication.getCredentials();
		if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
			throw new BadCredentialsException("用户名或密码有误");
		}
		HttpSessionContext.current().setAttribute(Constants.KAPTCHA_SESSION_KEY, null);
		authentication.setDetails(user);
		return user;
	}

	@Override
	protected void doAfterPropertiesSet() throws Exception {
		AssertUtil.notNull(this.passwordEncoder, "A password encoder must be set");
	}

}