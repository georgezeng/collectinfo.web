package com.collectinfo.repository.jpa.dao;

import com.collectinfo.domain.db.Authority;
import com.collectinfo.dto.query.PageResult;
import com.collectinfo.dto.query.QueryInfo;

public interface AuthorityDao {
	PageResult<Authority> find(QueryInfo<Authority> queryInfo);
}
