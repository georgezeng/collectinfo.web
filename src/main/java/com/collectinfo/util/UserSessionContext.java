package com.collectinfo.util;

import com.collectinfo.domain.db.base.BaseUser;

public class UserSessionContext {
	private static final ThreadLocal<BaseUser> USER = new ThreadLocal<BaseUser>();

	@SuppressWarnings("unchecked")
	public static <T extends BaseUser> T current() {
		return (T) USER.get();
	}

	public static void set(BaseUser current) {
		USER.set(current);
	}

}
