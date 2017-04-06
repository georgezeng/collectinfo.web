package com.collectinfo.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.crypto.codec.Base64;

public class CookieUtil {
	public static String getValue(HttpServletRequest req, String name) {
		return getValue(req, name, true);
	}

	public static String getValue(HttpServletRequest req, String name, boolean decode) {
		Cookie[] cookies = req.getCookies();
		String decodedValue = null;
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (name.equals(cookie.getName())) {
					decodedValue = cookie.getValue();
					if (decode) {
						if (Base64.isBase64(decodedValue.getBytes())) {
							decodedValue = new String(Base64.decode(decodedValue.getBytes()));
						}
					}
					return decodedValue;
				}
			}
		}
		return null;
	}

	public static void writeCookie(HttpServletResponse res, String name, String value, boolean isSecure, int expiry) {
		writeCookie(res, name, value, isSecure, expiry, true, true);
	}

	public static void writeCookie(HttpServletResponse res, String name, String value, boolean isSecure, int expiry,
			boolean isEncode, boolean isHttpOnly) {
		String encodedValue = value;
		if (isEncode) {
			encodedValue = new String(Base64.encode(value.getBytes()));
		}
		Cookie cookie = new Cookie(name, encodedValue);
		if (expiry != 0) {
			cookie.setMaxAge(expiry);
		}
		cookie.setSecure(isSecure);
		cookie.setHttpOnly(isHttpOnly);
		cookie.setPath("/");
		res.addCookie(cookie);
	}
}