package com.collectinfo.service;

import java.util.List;

import com.collectinfo.domain.db.Menu;
import com.collectinfo.domain.db.User;
import com.collectinfo.dto.MenuDTO;
import com.collectinfo.dto.query.PageResult;
import com.collectinfo.dto.query.QueryInfo;

public interface MenuService {
	List<MenuDTO> findByUser(User user);

	List<MenuDTO> transferToDTO(Iterable<Menu> menus);

	PageResult<MenuDTO> find(QueryInfo<Menu> queryInfo);

	MenuDTO findDTO(Long id);

	void save(Menu menu);
	
	List<MenuDTO> findAllParents();
}
