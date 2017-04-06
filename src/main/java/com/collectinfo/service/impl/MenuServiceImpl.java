package com.collectinfo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.collectinfo.domain.db.Authority;
import com.collectinfo.domain.db.Menu;
import com.collectinfo.domain.db.Role;
import com.collectinfo.domain.db.User;
import com.collectinfo.dto.MenuDTO;
import com.collectinfo.dto.query.PageResult;
import com.collectinfo.dto.query.QueryInfo;
import com.collectinfo.repository.jpa.api.AuthorityRepository;
import com.collectinfo.repository.jpa.api.MenuRepository;
import com.collectinfo.repository.jpa.api.UserRepository;
import com.collectinfo.repository.jpa.dao.MenuDao;
import com.collectinfo.service.MenuService;
import com.collectinfo.util.AssertUtil;

@Service
public class MenuServiceImpl implements MenuService {

	@Value("${superadmin.role}")
	private String superAdminRole;

	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private AuthorityRepository authorityRepository;

	@Autowired
	private MenuDao menuDao;

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<MenuDTO> findByUser(User user) {
		user = userRepository.findOne(user.getId());
		AssertUtil.notNullForBusiness(user, "用户不存在");
		return findByUserAndParent(user, null);
	}

	public List<MenuDTO> findByUserAndParent(User user, Menu parent) {
		List<MenuDTO> dtos = new ArrayList<MenuDTO>();
		List<Menu> menus = menuRepository.findByParent(parent);
		if (menus != null) {
			for (Menu menu : menus) {
				boolean hasAuth = false;
				if (user.hasRole(superAdminRole)) {
					hasAuth = true;
				} else {
					for (Role role : user.getRoles()) {
						if (role.getAuthorities().contains(menu.getAuthority())) {
							hasAuth = true;
							break;
						}
					}
				}
				if (hasAuth) {
					MenuDTO dto = menu.asDTO();
					if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
						dto.setChildren(findByUserAndParent(user, menu));
					}
					dtos.add(dto);
				}

			}
		}
		return dtos;
	}

	@Override
	public List<MenuDTO> transferToDTO(Iterable<Menu> menus) {
		List<MenuDTO> dtos = new ArrayList<MenuDTO>();
		if (menus != null) {
			for (Menu data : menus) {
				dtos.add(data.asDTO());
			}
		}
		return dtos;
	}

	@Override
	public PageResult<MenuDTO> find(QueryInfo<Menu> queryInfo) {
		PageResult<Menu> pr = menuDao.find(queryInfo);
		return new PageResult<MenuDTO>(transferToDTO(pr.getList()), pr.getTotal(), queryInfo.getPageInfo());
	}

	@Override
	public MenuDTO findDTO(Long id) {
		Menu menu = menuRepository.findOne(id);
		AssertUtil.notNullForBusiness(menu, "菜单不存在");
		return menu.asDTO();
	}

	@Override
	public void save(Menu menu) {
		Menu persistMenu = menu;
		if (menu != null && menu.getId() != null) {
			persistMenu = menuRepository.findOne(menu.getId());
		}
		AssertUtil.notNullForBusiness(persistMenu, "菜单不存在");
		BeanUtils.copyProperties(menu, persistMenu, "id", "children", "parent", "authority");
		if (menu.getParent() != null) {
			Menu parentMenu = menuRepository.findOne(menu.getParent().getId());
			AssertUtil.notNullForBusiness(parentMenu, "父节点不存在");
			AssertUtil.isNullForBusiness(parentMenu.getAuthority(), "父节点不能有权限");
			persistMenu.setParent(parentMenu);
		}
		if (menu.getAuthority() != null) {
			Authority auth = authorityRepository.findOne(menu.getAuthority().getId());
			AssertUtil.notNullForBusiness(auth, "权限不存在");
			persistMenu.setAuthority(auth);
		}
		menuRepository.save(persistMenu);
	}

	@Override
	public List<MenuDTO> findAllParents() {
		return transferToDTO(menuRepository.findByAuthorityIsNull());
	}

}
