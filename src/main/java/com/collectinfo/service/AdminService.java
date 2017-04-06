package com.collectinfo.service;

import com.collectinfo.domain.db.Role;

public interface AdminService {

	void initSuperAdmin();
	
	Role initClientRole();
}