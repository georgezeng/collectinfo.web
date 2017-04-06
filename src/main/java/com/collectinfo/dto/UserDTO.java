package com.collectinfo.dto;

import java.util.ArrayList;
import java.util.List;

public class UserDTO {
	private Long id;
	private String username;
	private String nickname;
	private String email;
	private Boolean enabled;
	private List<RoleDTO> roles;
	private List<RoleDTO> unselectedRoles;

	public List<RoleDTO> getUnselectedRoles() {
		return unselectedRoles;
	}

	public void setUnselectedRoles(List<RoleDTO> unselectedRoles) {
		this.unselectedRoles = unselectedRoles;
	}

	public List<RoleDTO> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleDTO> roles) {
		this.roles = roles;
	}

	public void addUnselectedRole(RoleDTO role) {
		if (unselectedRoles == null) {
			unselectedRoles = new ArrayList<RoleDTO>();
		}
		unselectedRoles.add(role);
	}

	public void addRole(RoleDTO role) {
		if (roles == null) {
			roles = new ArrayList<RoleDTO>();
		}
		roles.add(role);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
}
