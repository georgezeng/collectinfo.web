package com.collectinfo.service;

import java.util.List;

import com.collectinfo.domain.db.Role;
import com.collectinfo.dto.RoleDTO;
import com.collectinfo.dto.UserDTO;
import com.collectinfo.dto.query.PageResult;
import com.collectinfo.dto.query.QueryInfo;

public interface RoleService {
	void save(Role role);

	void getUnselectedRoles(UserDTO user);

	PageResult<RoleDTO> find(QueryInfo<Role> queryInfo);

	List<RoleDTO> transferToDTO(Iterable<Role> roles);

	RoleDTO findDTO(Long id);
}