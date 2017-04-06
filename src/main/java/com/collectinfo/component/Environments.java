package com.collectinfo.component;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.collectinfo.enums.Env;

@Component
public class Environments implements EnvironmentAware {

	private Environment springEnv;

	@Override
	public void setEnvironment(Environment env) {
		this.springEnv = env;
	}

	public boolean inAny(Env... envs) {
		for (Env env : envs) {
			for (String profile : springEnv.getActiveProfiles())
				if (env.equals(Env.codeOf(profile))) {
					return true;
				}
		}
		return false;
	}

	public boolean notInAny(Env... envs) {
		return !inAny(envs);
	}

}
