package com.collectinfo.enums;

public enum Env {
	local("local"), Dev("dev"), Uat("uat"), Staging("staging"), Prod("prod");

	private String code;

	private Env(String code) {
		this.code = code;
	}

	public static Env codeOf(String code) {
		for (Env env : Env.values()) {
			if (env.code.equalsIgnoreCase(code)) {
				return env;
			}
		}
		return null;
	}

	public String getCode() {
		return code;
	}

}
