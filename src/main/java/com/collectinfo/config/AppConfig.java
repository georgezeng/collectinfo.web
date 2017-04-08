package com.collectinfo.config;

import java.util.concurrent.Executor;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableAsync
@EnableRedisHttpSession
@EnableRedisRepositories(basePackages = { "com.collectinfo.repository.redis.api" })
@EnableJpaRepositories(basePackages = { "com.collectinfo.repository.jpa.api" })
//@ImportResource("classpath:/application-tx-aop.xml")
public class AppConfig extends AsyncConfigurerSupport {
	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(30);
		executor.setQueueCapacity(200);
		executor.setThreadNamePrefix("Async-Executor-");
		executor.initialize();
		return executor;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * fix for redis cloud service to ignore the keyspace nitification configs
	 * 
	 * @return
	 */
	@Bean
	public static ConfigureRedisAction configureRedisAction() {
		return ConfigureRedisAction.NO_OP;
	}

	@Value("${spring.session.redis.namespace}")
	private String sessionKeyNamespace;

	@Autowired
	private RedisOperationsSessionRepository sessionReporitory;

	@PostConstruct
	public void init() {
		sessionReporitory.setRedisKeyNamespace(sessionKeyNamespace);
	}
}
