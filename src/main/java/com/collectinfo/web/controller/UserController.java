package com.collectinfo.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

import com.collectinfo.domain.db.User;
import com.collectinfo.dto.BaseResult;
import com.collectinfo.dto.UserDTO;
import com.collectinfo.dto.query.QueryInfo;
import com.collectinfo.repository.jpa.api.UserRepository;
import com.collectinfo.service.RoleService;
import com.collectinfo.service.UserService;
import com.collectinfo.util.AssertUtil;
import com.collectinfo.util.UserSessionContext;

@RestController
@RequestMapping("/mvc/user")
@SuppressWarnings("unchecked")
public class UserController extends BaseController {
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserRepository userRepository;

	@RequestMapping("/save")
	public BaseResult<?> save(@RequestBody User user) {
		if (user.getId() == null) {
			String password = StringUtils.randomAlphanumeric(6);
			user.setPassword(password);
			user.setConfirmPassword(password);
			userService.create(user);
		} else {
			userService.update(user);
		}
		return new BaseResult<>();
	}

	@RequestMapping("/{id}")
	public BaseResult<?> one(@PathVariable Long id) {
		UserDTO dto = userService.findDTO(id);
		roleService.getUnselectedRoles(dto);
		return new BaseResult<>(dto);
	}

	@RequestMapping("/list")
	public BaseResult<?> list(@RequestBody QueryInfo<User> queryInfo) {
		return new BaseResult<>(userService.find(queryInfo));
	}

	@RequestMapping("/all")
	public BaseResult<?> all() {
		return new BaseResult<>(userService.transferToDTO(userRepository.findByEnabled(true)));
	}

	@Override
	protected UserRepository getMainRepository() {
		return userRepository;
	}

	@RequestMapping("/resetPwd/{id}")
	public BaseResult<?> resetPwd(@PathVariable Long id) {
		userService.resetPassword(id, null);
		return new BaseResult<>();
	}

	@RequestMapping("/changePwd")
	public BaseResult<?> changePwd(@RequestBody User user) {
		User currentUser = UserSessionContext.current();
		String oldPassword = new String(Base64Utils.decodeFromString(user.getOldPassword()));
		String password = new String(Base64Utils.decodeFromString(user.getPassword()));
		String confirmPassword = new String(Base64Utils.decodeFromString(user.getConfirmPassword()));
		AssertUtil.isTrueForBusiness(passwordEncoder.matches(oldPassword, currentUser.getPassword()), "原密码不正确");
		AssertUtil.isEqualsForBusiness(password, confirmPassword, "两次密码不同");
		userService.resetPassword(currentUser.getId(), password);
		return new BaseResult<>();
	}

	@RequestMapping("/profile")
	public BaseResult<?> profile() {
		return new BaseResult<>(UserSessionContext.<User>current().asDTO(false));
	}

	@RequestMapping("/saveProfile")
	public BaseResult<?> profile(@RequestBody User user) {
		userService.updateProfile(user);
		refreshUserContext(UserSessionContext.current());
		return new BaseResult<>();
	}
}
