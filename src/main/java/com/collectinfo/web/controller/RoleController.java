package com.collectinfo.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.collectinfo.domain.db.Role;
import com.collectinfo.dto.BaseResult;
import com.collectinfo.dto.RoleDTO;
import com.collectinfo.dto.query.QueryInfo;
import com.collectinfo.repository.jpa.api.RoleRepository;
import com.collectinfo.service.AuthorityService;
import com.collectinfo.service.RoleService;
import com.collectinfo.service.UserService;

@RestController
@RequestMapping("/mvc/role")
@SuppressWarnings("unchecked")
public class RoleController extends BaseController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private RoleRepository roleRepository;

	@RequestMapping("/all")
	public BaseResult<?> all() {
		return new BaseResult<>(roleRepository.findAll());
	}

	@RequestMapping("/list")
	public BaseResult<?> list(@RequestBody QueryInfo<Role> queryInfo) {
		return new BaseResult<>(roleService.find(queryInfo));
	}

	@RequestMapping("/{id}")
	public BaseResult<?> one(@PathVariable Long id) {
		RoleDTO dto = roleService.findDTO(id);
		userService.getUnselectedUsers(dto);
		authorityService.getUnselectedAuthorities(dto);
		return new BaseResult<>(dto);
	}

	@RequestMapping("/save")
	public BaseResult<?> save(@RequestBody Role role) {
		roleService.save(role);
		return new BaseResult<>();
	}

	@Override
	protected RoleRepository getMainRepository() {
		return roleRepository;
	}

}
