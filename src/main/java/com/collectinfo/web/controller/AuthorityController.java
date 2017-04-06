package com.collectinfo.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.collectinfo.domain.db.Authority;
import com.collectinfo.dto.BaseResult;
import com.collectinfo.dto.query.QueryInfo;
import com.collectinfo.repository.jpa.api.AuthorityRepository;
import com.collectinfo.service.AuthorityService;

@RestController
@RequestMapping("/mvc/authority")
@SuppressWarnings("unchecked")
public class AuthorityController extends BaseController {

	@Autowired
	private AuthorityRepository authorityRepository;

	@Autowired
	private AuthorityService authorityService;

	@RequestMapping("/all")
	public BaseResult<?> all() {
		return new BaseResult<>(authorityService.transferToDTO(authorityRepository.findAll()));
	}

	@RequestMapping("/list")
	public BaseResult<?> list(@RequestBody QueryInfo<Authority> queryInfo) {
		return new BaseResult<>(authorityService.find(queryInfo));
	}

	@RequestMapping("/{id}")
	public BaseResult<?> one(@PathVariable Long id) {
		return new BaseResult<>(authorityService.findDTO(id));
	}

	@RequestMapping("/save")
	public BaseResult<?> save(@RequestBody Authority auth) {
		authorityService.save(auth);
		return new BaseResult<>();
	}

	@Override
	protected AuthorityRepository getMainRepository() {
		return authorityRepository;
	}

}
