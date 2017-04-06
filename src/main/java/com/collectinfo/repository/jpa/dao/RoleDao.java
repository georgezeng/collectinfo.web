package com.collectinfo.repository.jpa.dao;

import com.collectinfo.domain.db.Role;
import com.collectinfo.dto.query.PageResult;
import com.collectinfo.dto.query.QueryInfo;

public interface RoleDao {
	PageResult<Role> find(QueryInfo<Role> queryInfo);
}
