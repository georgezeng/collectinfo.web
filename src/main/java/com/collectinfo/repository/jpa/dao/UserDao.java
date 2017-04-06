package com.collectinfo.repository.jpa.dao;

import com.collectinfo.domain.db.User;
import com.collectinfo.dto.query.PageResult;
import com.collectinfo.dto.query.QueryInfo;

public interface UserDao {
	PageResult<User> find(QueryInfo<User> queryInfo);
}
