package com.collectinfo.service;

import java.util.List;

import com.collectinfo.domain.db.Authority;
import com.collectinfo.dto.AuthorityDTO;
import com.collectinfo.dto.RoleDTO;
import com.collectinfo.dto.query.PageResult;
import com.collectinfo.dto.query.QueryInfo;

public interface AuthorityService {
	void save(Authority authority);

	void getUnselectedAuthorities(RoleDTO role);

	PageResult<AuthorityDTO> find(QueryInfo<Authority> queryInfo);

	List<AuthorityDTO> transferToDTO(Iterable<Authority> auths);

	AuthorityDTO findDTO(Long id);

	List<AuthorityDTO> findAll();
}