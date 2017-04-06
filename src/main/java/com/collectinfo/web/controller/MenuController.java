package com.collectinfo.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.collectinfo.constant.CommonStoreKeyConstant;
import com.collectinfo.domain.db.Menu;
import com.collectinfo.domain.redis.RedisCommonStore;
import com.collectinfo.dto.BaseResult;
import com.collectinfo.dto.MenuDTO;
import com.collectinfo.dto.query.QueryInfo;
import com.collectinfo.repository.jpa.api.MenuRepository;
import com.collectinfo.repository.redis.api.RedisCommonStoreRepository;
import com.collectinfo.service.AuthorityService;
import com.collectinfo.service.MenuService;

@RestController
@RequestMapping("/mvc/menu")
@SuppressWarnings("unchecked")
public class MenuController extends BaseController {

	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private MenuService menuService;

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private RedisCommonStoreRepository redisCommonStoreRepository;

	@RequestMapping("/all")
	public BaseResult<?> list() {
		return new BaseResult<>(fetchAllOptions(new MenuDTO()));
	}

	@RequestMapping("/list")
	public BaseResult<?> list(@RequestBody QueryInfo<Menu> queryInfo) {
		return new BaseResult<>(menuService.find(queryInfo));
	}

	@RequestMapping("/{id}")
	public BaseResult<?> one(@PathVariable Long id) {
		MenuDTO dto = menuService.findDTO(id);
		return new BaseResult<>(fetchAllOptions(dto));
	}

	private MenuDTO fetchAllOptions(MenuDTO dto) {
		dto.setAllAuthorities(authorityService.findAll());
		dto.setAllParents(menuService.findAllParents());
		return dto;
	}

	@RequestMapping("/save")
	public BaseResult<?> save(@RequestBody Menu menu) {
		menuService.save(menu);
		RedisCommonStore config = redisCommonStoreRepository.findOne(CommonStoreKeyConstant.NEED_REFRESH_MENU);
		config.setValue(Boolean.TRUE.toString());
		redisCommonStoreRepository.save(config);
		return new BaseResult<>();
	}

	@Override
	protected MenuRepository getMainRepository() {
		return menuRepository;
	}

}
