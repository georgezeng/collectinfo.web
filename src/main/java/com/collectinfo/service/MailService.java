package com.collectinfo.service;

public interface MailService {
	void sendRestPassword(String to, String password);
	void sendCreateUser(String to, String username, String password);
}
