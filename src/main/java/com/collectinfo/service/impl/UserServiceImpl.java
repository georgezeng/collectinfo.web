package com.collectinfo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import com.collectinfo.domain.db.Role;
import com.collectinfo.domain.db.User;
import com.collectinfo.dto.RoleDTO;
import com.collectinfo.dto.UserDTO;
import com.collectinfo.dto.query.PageResult;
import com.collectinfo.dto.query.QueryInfo;
import com.collectinfo.repository.jpa.api.RoleRepository;
import com.collectinfo.repository.jpa.api.UserRepository;
import com.collectinfo.repository.jpa.dao.UserDao;
import com.collectinfo.service.MailService;
import com.collectinfo.service.UserService;
import com.collectinfo.util.AssertUtil;
import com.collectinfo.util.UserSessionContext;

@Service
public class UserServiceImpl implements UserService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserRepository repository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private MailService mailService;

	@Value("${client.role}")
	private String clientRole;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repository.findByUsername(username);
		if (user != null) {
			user.getAuthorities();
		}
		return user;
	}

	@Override
	public void createClient(User client) {
		client.addRole(roleRepository.findByName(clientRole));
		create(client, false);
		client.getAuthorities();
	}

	@Override
	public void create(User user) {
		create(user, true);
	}

	private void create(User user, boolean sendEmail) {
		User oldData = repository.findByUsername(user.getUsername());
		AssertUtil.isNullForBusiness(oldData, "用户已存在");
		User otherEmailData = repository.findByEmail(user.getEmail());
		AssertUtil.isNullForBusiness(otherEmailData, "邮箱已存在");
		AssertUtil.isTrueForBusiness(user.getPassword() != null && user.getPassword().length() >= 6, "密码长度不能小于6");
		AssertUtil.isNotEmptyForBusiness(user.getRoles(), "至少关联一个角色");
		user.enabled();
		String password = user.getPassword();
		if (logger.isDebugEnabled()) {
			logger.debug(user.getUsername() + "的新密码是: " + password);
		}
		user.setPassword(passwordEncoder.encode(password));
		repository.save(user);
		List<Role> relatedRoles = new ArrayList<Role>();
		for (Role role : user.getRoles()) {
			Role persistRole = roleRepository.findOne(role.getId());
			AssertUtil.notNullForBusiness(persistRole, "角色不存在");
			persistRole.addUser(user);
			relatedRoles.add(persistRole);
		}
		if (!relatedRoles.isEmpty()) {
			roleRepository.save(relatedRoles);
		}
		if (sendEmail) {
			mailService.sendCreateUser(user.getEmail(), user.getUsername(), password);
		}
	}

	@Override
	public void update(User user) {
		User oldData = repository.findOne(user.getId());
		AssertUtil.notNullForBusiness(oldData, "用户不存在");
		User otherUser = repository.findByUsername(user.getUsername());
		AssertUtil.isTrueForBusiness(otherUser == null || otherUser.getId().equals(user.getId()), "用户名已存在");
		BeanUtils.copyProperties(user, oldData, "id", "password", "roles", "accountNonExpired", "accountNonLocked",
				"credentialsNonExpired");
		repository.save(oldData);
		if (user.getRoles() != null && !user.getRoles().isEmpty()) {
			List<Role> relatedRoles = new ArrayList<Role>();
			for (Role role : user.getRoles()) {
				Role persistRole = roleRepository.findOne(role.getId());
				AssertUtil.notNullForBusiness(persistRole, "角色不存在");
				persistRole.addUser(user);
				relatedRoles.add(persistRole);
			}
			if (!relatedRoles.isEmpty()) {
				roleRepository.save(relatedRoles);
			}
		} else {
			roleRepository.removeUserFromAllRoles(user.getId());
		}
	}

	@Override
	public UserDTO findDTO(Long id) {
		User user = repository.findOne(id);
		AssertUtil.notNullForBusiness(user, "用户不存在");
		return user.asDTO();
	}

	@Override
	public PageResult<UserDTO> find(QueryInfo<User> queryInfo) {
		PageResult<User> pr = userDao.find(queryInfo);
		return new PageResult<UserDTO>(transferToDTO(pr.getList()), pr.getTotal(), queryInfo.getPageInfo());
	}

	@Override
	public List<UserDTO> transferToDTO(Iterable<User> users) {
		List<UserDTO> dtos = new ArrayList<UserDTO>();
		if (users != null) {
			for (User data : users) {
				dtos.add(data.asDTO());
			}
		}
		return dtos;
	}

	@Override
	public void getUnselectedUsers(RoleDTO role) {
		if (role.getUsers() != null) {
			for (User user : repository.findAll()) {
				boolean isFound = false;
				for (UserDTO selectedUser : role.getUsers()) {
					if (user.getId().equals(selectedUser.getId())) {
						isFound = true;
						break;
					}
				}
				if (!isFound) {
					role.addUnselectedUser(user.asDTO());
				}
			}
		} else {
			role.setUnselectedUsers(transferToDTO(repository.findAll()));
		}
	}

	@Override
	public void resetPassword(Long userId, String password) {
		User oldData = repository.findOne(userId);
		AssertUtil.notNullForBusiness(oldData, "用户不存在");
		if (StringUtils.isEmpty(password)) {
			password = StringUtils.randomAlphanumeric(6);
		} else {
			AssertUtil.isTrueForBusiness(password.length() > 5, "密码长度必须6位或以上");
		}
		if (logger.isDebugEnabled()) {
			logger.debug(oldData.getUsername() + "的新密码是: " + password);
		}
		oldData.setPassword(passwordEncoder.encode(password));
		mailService.sendRestPassword(oldData.getEmail(), password);
	}

	@Override
	public void updateProfile(User newInfo) {
		User currentUser = UserSessionContext.current();
		User otherUser = repository.findByUsername(newInfo.getUsername());
		AssertUtil.isTrueForBusiness(otherUser.getId().equals(currentUser.getId()), "用户名已存在");
		otherUser = repository.findByEmail(newInfo.getEmail());
		AssertUtil.isTrueForBusiness(otherUser.getId().equals(currentUser.getId()), "邮箱已存在");
		currentUser = repository.findOne(currentUser.getId());
		currentUser.setUsername(newInfo.getUsername());
		currentUser.setNickname(newInfo.getNickname());
		currentUser.setEmail(newInfo.getEmail());
		repository.save(currentUser);
		currentUser.getAuthorities();
		UserSessionContext.set(currentUser);
	}

}