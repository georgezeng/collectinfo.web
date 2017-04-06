package com.collectinfo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collectinfo.domain.db.Authority;
import com.collectinfo.domain.db.Role;
import com.collectinfo.domain.db.User;
import com.collectinfo.dto.RoleDTO;
import com.collectinfo.dto.UserDTO;
import com.collectinfo.dto.query.PageResult;
import com.collectinfo.dto.query.QueryInfo;
import com.collectinfo.repository.jpa.api.AuthorityRepository;
import com.collectinfo.repository.jpa.api.RoleRepository;
import com.collectinfo.repository.jpa.api.UserRepository;
import com.collectinfo.repository.jpa.dao.RoleDao;
import com.collectinfo.service.RoleService;
import com.collectinfo.util.AssertUtil;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private AuthorityRepository authorityRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleDao roleDao;

	@Override
	public void save(Role role) {
		AssertUtil.isNotEmptyForBusiness(role.getAuthorities(), "至少关联一个权限");
		Role persistRole = roleRepository.findByName(role.getName());
		if (persistRole == null) {
			persistRole = role;
		}
		Set<Authority> auths = role.getAuthorities();
		persistRole.setAuthorities(null);
		for (Authority auth : auths) {
			Authority persistAuth = authorityRepository.findOne(auth.getId());
			AssertUtil.notNullForBusiness(persistAuth, "权限不存在");
			persistRole.addAuthority(persistAuth);
		}
		Set<User> newUsers = role.getUsers();
		persistRole.setUsers(null);
		for (User user : newUsers) {
			User persistUser = userRepository.findOne(user.getId());
			AssertUtil.notNullForBusiness(persistRole, "用户不存在");
			persistRole.addUser(persistUser);
		}
		roleRepository.save(persistRole);
	}

	@Override
	public void getUnselectedRoles(UserDTO user) {
		if (user.getRoles() != null) {
			for (Role role : roleRepository.findAll()) {
				boolean isFound = false;
				for (RoleDTO selectedRole : user.getRoles()) {
					if (role.getId().equals(selectedRole.getId())) {
						isFound = true;
						break;
					}
				}
				if (!isFound) {
					user.addUnselectedRole(role.asDTO());
				}
			}
		} else {
			user.setUnselectedRoles(transferToDTO(roleRepository.findAll()));
		}
	}

	@Override
	public PageResult<RoleDTO> find(QueryInfo<Role> queryInfo) {
		PageResult<Role> pr = roleDao.find(queryInfo);
		return new PageResult<RoleDTO>(transferToDTO(pr.getList()), pr.getTotal(), queryInfo.getPageInfo());
	}

	@Override
	public List<RoleDTO> transferToDTO(Iterable<Role> roles) {
		List<RoleDTO> dtos = new ArrayList<RoleDTO>();
		if (roles != null) {
			for (Role data : roles) {
				dtos.add(data.asDTO());
			}
		}
		return dtos;
	}

	@Override
	public RoleDTO findDTO(Long id) {
		Role role = roleRepository.findOne(id);
		AssertUtil.notNullForBusiness(role, "角色不存在");
		return role.asDTO();
	}

}