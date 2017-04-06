package com.collectinfo.repository.jpa.dao;

import com.collectinfo.domain.db.Menu;
import com.collectinfo.dto.query.PageResult;
import com.collectinfo.dto.query.QueryInfo;

public interface MenuDao {
	PageResult<Menu> find(QueryInfo<Menu> queryInfo);
}
