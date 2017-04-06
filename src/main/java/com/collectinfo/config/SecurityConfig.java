package com.collectinfo.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.collectinfo.web.filter.AjaxLoginAuthenticationFilter;
import com.collectinfo.web.filter.ErrorHandlerFilter;
import com.collectinfo.web.filter.HttpSessionFilter;
import com.collectinfo.web.filter.MultiReadFilter;
import com.collectinfo.web.filter.ServerManagementFilter;
import com.collectinfo.web.filter.UserSessionFilter;
import com.collectinfo.web.security.metadata.SecurityMetadataSource;
import com.collectinfo.web.security.session.strategy.RetryRedirectInvalidSessionStrategy;
import com.collectinfo.web.security.voter.SecurityVoter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private RememberMeServices remembermeService;

	@Autowired
	private MultiReadFilter multiReadFilter;

	@Autowired
	private AjaxLoginAuthenticationFilter ajaxLoginFilter;

	@Autowired
	private ErrorHandlerFilter errorHandlerFilter;

	@Autowired
	private UserSessionFilter userSessionFilter;
	
	@Autowired
	private HttpSessionFilter httpSessionFilter;

	@Autowired
	private ServerManagementFilter serverManagementFilter;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Value("${security.login.uri}")
	private String loginUri;
	
	@Value("${security.register.uri}")
	private String registerUri;
	
	@Value("${security.findPassword.uri}")
	private String findPasswordUri;
	
	@Value("${static.resource.uri}")
	private String resourceUri;
	
	@Value("${security.logout.uri}")
	private String logoutUri;
	
	@Value("${security.entry.uri}")
	private String entryUri;
	
	@Value("${management.context-path}")
	private String manageUri;

	@Value("${superadmin.authority}")
	private String superadminAuthority;

	@Value("${security.rememberme.key}")
	private String remembermeKey;
	
	@Value("${superadmin.authority}")
	private String superAdminAuth;
	
	@Value("${security.captcha.uri}")
	private String captchaUri;
	
	@Value("${error.uri}")
	private String errorUri;
	
	@Autowired
	private RetryRedirectInvalidSessionStrategy invalidSessionStrategy;
	
	@Autowired
	private SecurityMetadataSource securityMetadataSource;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.sessionManagement().invalidSessionUrl(loginUri);
		http.formLogin().disable();
		http.logout().logoutUrl(logoutUri).logoutSuccessUrl(loginUri).addLogoutHandler((LogoutHandler)remembermeService);
		http.exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(loginUri));
		http.rememberMe().key(remembermeKey).rememberMeServices(remembermeService);
		http.authorizeRequests()
			.antMatchers(loginUri).permitAll()
			.antMatchers(loginUri+".html").permitAll()
			.antMatchers(loginUri+"/**").permitAll()
			.antMatchers(registerUri).permitAll()
			.antMatchers(registerUri + ".html").permitAll()
			.antMatchers(registerUri + "/**").permitAll()
			.antMatchers(findPasswordUri).permitAll()
			.antMatchers(findPasswordUri + ".html").permitAll()
			.antMatchers(findPasswordUri + "/**").permitAll()
			.antMatchers(captchaUri).permitAll()
			.antMatchers(captchaUri + ".jpg").permitAll()
			.antMatchers(captchaUri + "/**").permitAll()
			.antMatchers(resourceUri + "/lib/**").permitAll()
			.antMatchers(resourceUri + "/system/css/**").permitAll()
			.antMatchers(resourceUri + "/system/js/**").permitAll()
			.antMatchers(resourceUri + "/system/login/**").permitAll()
			.antMatchers(resourceUri + "/system/password/find/**").permitAll()
			.antMatchers(resourceUri + "/system/register/**").permitAll()
			.antMatchers(manageUri + "/**").permitAll()
			.antMatchers(errorUri + "/**").permitAll()
			.antMatchers(entryUri).authenticated()
			.antMatchers(resourceUri + "/system/**").authenticated()
			.anyRequest().hasAuthority(superAdminAuth);
		http.sessionManagement().invalidSessionStrategy(invalidSessionStrategy);
		http.authorizeRequests().withObjectPostProcessor(new ObjectPostProcessor<AffirmativeBased>() {
			
			@Override
			public <O extends AffirmativeBased> O postProcess(O accessManager) {
				accessManager.getDecisionVoters().add(new SecurityVoter());
				return accessManager;
			}
		});

		http.addFilterBefore(multiReadFilter, ChannelProcessingFilter.class);
		http.addFilterBefore(httpSessionFilter, multiReadFilter.getClass());
		http.addFilterBefore(errorHandlerFilter, MultiReadFilter.class);
		http.addFilterBefore(ajaxLoginFilter, UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(userSessionFilter, FilterSecurityInterceptor.class);
		http.addFilterBefore(serverManagementFilter, userSessionFilter.getClass());
		
	}

	public void configure(WebSecurity web) throws Exception {
		final HttpSecurity http = getHttp();
		web.postBuildAction(new Runnable() {
			public void run() {
				FilterSecurityInterceptor fi = http.getSharedObject(FilterSecurityInterceptor.class);
				securityMetadataSource.setOriginal(fi.getSecurityMetadataSource());
				fi.setSecurityMetadataSource(securityMetadataSource);
			}
		});
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth, AuthenticationProvider authenticationProvider)
			throws Exception {
		auth.authenticationProvider(authenticationProvider);
	}

	@PostConstruct
	public void init() {
		ajaxLoginFilter.setAuthenticationManager(authenticationManager);
	}

}