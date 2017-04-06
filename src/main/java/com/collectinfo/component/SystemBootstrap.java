package com.collectinfo.component;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.collectinfo.constant.CommonStoreKeyConstant;
import com.collectinfo.domain.db.User;
import com.collectinfo.domain.redis.RedisCommonStore;
import com.collectinfo.repository.redis.api.RedisCommonStoreRepository;
import com.collectinfo.service.AdminService;
import com.collectinfo.util.UserSessionContext;

@Component
public class SystemBootstrap {
	@Autowired
	private AdminService adminService;

	@Autowired
	private RedisCommonStoreRepository redisCommonStoreRepository;

	@PostConstruct
	public void init() {
		try {
			UserSessionContext.set(User.SYSTEM_USER);
			adminService.initSuperAdmin();
			adminService.initClientRole();
			RedisCommonStore config = redisCommonStoreRepository.findOne(CommonStoreKeyConstant.NEED_REFRESH_MENU);
			if (config == null) {
				config = new RedisCommonStore();
				config.setKey(CommonStoreKeyConstant.NEED_REFRESH_MENU);
			}
			config.setValue(Boolean.TRUE.toString());
			redisCommonStoreRepository.save(config);
		} finally {
			UserSessionContext.set(null);
		}
	}
}