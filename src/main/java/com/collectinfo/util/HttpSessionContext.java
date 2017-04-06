package com.collectinfo.util;

import javax.servlet.http.HttpSession;

public class HttpSessionContext {
	private static final ThreadLocal<HttpSession> SESSION = new ThreadLocal<HttpSession>();

	public static HttpSession current() {
		return SESSION.get();
	}

	public static void set(HttpSession current) {
		SESSION.set(current);
	}
}
