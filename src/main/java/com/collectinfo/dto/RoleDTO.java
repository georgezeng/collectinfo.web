package com.collectinfo.dto;

import java.util.ArrayList;
import java.util.List;

public class RoleDTO {
	private Long id;
	private String name;
	private String description;
	private List<UserDTO> users;
	private List<UserDTO> unselectedUsers;
	private List<AuthorityDTO> authorities;
	private List<AuthorityDTO> unselectedAuthorities;

	public List<AuthorityDTO> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<AuthorityDTO> authorities) {
		this.authorities = authorities;
	}

	public List<AuthorityDTO> getUnselectedAuthorities() {
		return unselectedAuthorities;
	}

	public void setUnselectedAuthorities(List<AuthorityDTO> unselectedAuthorities) {
		this.unselectedAuthorities = unselectedAuthorities;
	}

	public List<UserDTO> getUsers() {
		return users;
	}

	public void setUsers(List<UserDTO> users) {
		this.users = users;
	}

	public List<UserDTO> getUnselectedUsers() {
		return unselectedUsers;
	}

	public void setUnselectedUsers(List<UserDTO> unselectedUsers) {
		this.unselectedUsers = unselectedUsers;
	}

	public void addAuthority(AuthorityDTO auth) {
		if (authorities == null) {
			authorities = new ArrayList<AuthorityDTO>();
		}
		authorities.add(auth);
	}

	public void addUnselectedAuthority(AuthorityDTO auth) {
		if (unselectedAuthorities == null) {
			unselectedAuthorities = new ArrayList<AuthorityDTO>();
		}
		unselectedAuthorities.add(auth);
	}

	public void addUnselectedUser(UserDTO user) {
		if (unselectedUsers == null) {
			unselectedUsers = new ArrayList<UserDTO>();
		}
		unselectedUsers.add(user);
	}

	public void addUser(UserDTO user) {
		if (users == null) {
			users = new ArrayList<UserDTO>();
		}
		users.add(user);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
