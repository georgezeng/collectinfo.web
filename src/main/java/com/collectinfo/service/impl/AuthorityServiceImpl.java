package com.collectinfo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collectinfo.domain.db.Authority;
import com.collectinfo.dto.AuthorityDTO;
import com.collectinfo.dto.RoleDTO;
import com.collectinfo.dto.query.PageResult;
import com.collectinfo.dto.query.QueryInfo;
import com.collectinfo.repository.jpa.api.AuthorityRepository;
import com.collectinfo.repository.jpa.dao.AuthorityDao;
import com.collectinfo.service.AuthorityService;
import com.collectinfo.util.AssertUtil;

@Service
public class AuthorityServiceImpl implements AuthorityService {

	@Autowired
	private AuthorityRepository authorityRepository;

	@Autowired
	private AuthorityDao authorityDao;

	@Override
	public void save(Authority authority) {
		Authority persistAuth = authority;
		if (authority != null && authority.getId() != null) {
			persistAuth = authorityRepository.findOne(authority.getId());
		}
		AssertUtil.notNullForBusiness(persistAuth, "权限不存在");
		BeanUtils.copyProperties(authority, persistAuth, "id");
		authorityRepository.save(persistAuth);
	}

	@Override
	public void getUnselectedAuthorities(RoleDTO role) {
		for (Authority auth : authorityRepository.findAll()) {
			boolean isFound = false;
			for (AuthorityDTO selectedAuthority : role.getAuthorities()) {
				if (auth.getId().equals(selectedAuthority.getId())) {
					isFound = true;
					break;
				}
			}
			if (!isFound) {
				role.addUnselectedAuthority(auth.asDTO());
			}
		}
	}

	@Override
	public PageResult<AuthorityDTO> find(QueryInfo<Authority> queryInfo) {
		PageResult<Authority> pr = authorityDao.find(queryInfo);
		return new PageResult<AuthorityDTO>(transferToDTO(pr.getList()), pr.getTotal(), queryInfo.getPageInfo());
	}

	@Override
	public AuthorityDTO findDTO(Long id) {
		Authority auth = authorityRepository.findOne(id);
		AssertUtil.notNullForBusiness(auth, "权限不存在");
		return auth.asDTO();
	}

	@Override
	public List<AuthorityDTO> transferToDTO(Iterable<Authority> auths) {
		List<AuthorityDTO> dtos = new ArrayList<AuthorityDTO>();
		if (auths != null) {
			for (Authority data : auths) {
				dtos.add(data.asDTO());
			}
		}
		return dtos;
	}

	@Override
	public List<AuthorityDTO> findAll() {
		return transferToDTO(authorityRepository.findAll());
	}

}