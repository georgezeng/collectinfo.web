package com.collectinfo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.collectinfo.service.MailService;

@Service
public class MailServiceImpl implements MailService {
	@Value("${spring.mail.username}")
	private String mailFrom;

	@Value("${system.title}")
	private String systemTitle;

	@Autowired
	private JavaMailSender javaMailSender;

	@Async
	@Override
	public void sendRestPassword(String to, String password) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(to);
		msg.setSubject(systemTitle + " - 重置密码");
		msg.setFrom(mailFrom);
		StringBuilder content = new StringBuilder();
		content.append("你的新密码为: " + password + ", 请尽快登录系统修改密码");
		msg.setText(content.toString());
		javaMailSender.send(msg);
	}

	@Async
	@Override
	public void sendCreateUser(String to, String username, String password) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(to);
		msg.setSubject(systemTitle + " - 创建用户信息");
		msg.setFrom(mailFrom);
		StringBuilder content = new StringBuilder();
		content.append("你好，你的用户信息为: 用户名["+username +"], 密码[" + password + "], 请尽快登录系统修改密码");
		msg.setText(content.toString());
		javaMailSender.send(msg);
	}

}
