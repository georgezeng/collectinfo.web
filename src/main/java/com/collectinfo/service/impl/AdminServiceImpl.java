package com.collectinfo.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.collectinfo.domain.db.Authority;
import com.collectinfo.domain.db.Role;
import com.collectinfo.domain.db.User;
import com.collectinfo.repository.jpa.api.AuthorityRepository;
import com.collectinfo.repository.jpa.api.RoleRepository;
import com.collectinfo.repository.jpa.api.UserRepository;
import com.collectinfo.service.AdminService;
import com.collectinfo.service.RoleService;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private RoleService roleService;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private AuthorityRepository authRepository;

	@Autowired
	private PasswordEncoder passwrodEncoder;

	@Autowired
	private UserRepository userRepository;

	@Value("${superadmin.role}")
	private String superAdminRole;

	@Value("${superadmin.nickname}")
	private String superAdminNickname;

	@Value("${superadmin.authority}")
	private String superAdminAuthority;

	@Value("${superadmin.username}")
	private String superAdminUsername;

	@Value("${superadmin.password}")
	private String superAdminPassword;

	@Value("${superadmin.email}")
	private String superAdminEmail;

	@Override
	public void initSuperAdmin() {
		User admin = userRepository.findByUsername(superAdminUsername);
		if (admin == null) {
			Role role = createRoleAndAuthorityIfNecessary(superAdminRole, superAdminNickname, superAdminAuthority, "/**",
					"超级管理员权限");
			Set<Role> roles = new HashSet<Role>();
			roles.add(role);
			admin = new User();
			admin.setRoles(roles);
			admin.enabled();
			role.addUser(admin);
		}
		admin.setUsername(superAdminUsername);
		admin.setPassword(passwrodEncoder.encode(superAdminPassword));
		admin.setNickname(superAdminNickname);
		admin.setEmail(superAdminEmail);
		userRepository.save(admin);
	}

	private Role createRoleAndAuthorityIfNecessary(String roleName, String roleDesc, String auth, String uri,
			String authDesc) {
		Role role = roleRepository.findByName(roleName);
		if (role == null) {
			role = new Role();
		}
		role.setName(roleName);
		role.setDescription(roleDesc);
		Authority authority = role.getAuthority(auth);
		if (authority == null) {
			authority = new Authority();
			authority.setName(auth);
			authority.setUri(uri);
			authority.setDescription(authDesc);
			authRepository.save(authority);
			role.clearAuthorities();
			role.addAuthority(authority);
		}
		roleService.save(role);
		return role;
	}

	@Value("${client.role}")
	private String clientRole;

	@Value("${client.role.description}")
	private String clientDescription;

	@Value("${client.authority}")
	private String clientAuthority;

	@Override
	public Role initClientRole() {
		return createRoleAndAuthorityIfNecessary(clientRole, clientDescription, clientAuthority, "/**", "客户基础权限");
	}

}