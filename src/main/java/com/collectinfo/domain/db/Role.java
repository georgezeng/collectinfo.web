package com.collectinfo.domain.db;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import com.collectinfo.domain.db.base.BaseRole;
import com.collectinfo.dto.RoleDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "role")
public class Role extends BaseRole {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "描述不能为空")
	private String description;

	@ManyToMany
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	@JsonProperty(access = Access.WRITE_ONLY)
	private Set<User> users;

	@ManyToMany
	@JoinTable(name = "role_authority", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
	@JsonProperty(access = Access.WRITE_ONLY)
	private Set<Authority> authorities;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	public void addAuthority(Authority authority) {
		if (authorities == null) {
			authorities = new HashSet<Authority>();
		}
		authorities.add(authority);
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public void addUser(User user) {
		if (users == null) {
			users = new HashSet<User>();
		}
		users.add(user);
	}

	public Authority getAuthority(String authName) {
		if (authorities != null) {
			for (Authority authority : authorities) {
				if (authority.getName().equalsIgnoreCase(authName)) {
					return authority;
				}
			}
		}
		return null;
	}

	public void clearAuthorities() {
		if (authorities != null) {
			authorities.clear();
		}
		authorities = null;
	}

	public RoleDTO asDTO() {
		return asDTO(true);
	}

	public RoleDTO asDTO(boolean withUser) {
		RoleDTO dto = new RoleDTO();
		BeanUtils.copyProperties(this, dto);
		if (withUser && users != null) {
			for (User user : users) {
				dto.addUser(user.asDTO(false));
			}
		}
		if (authorities != null) {
			for (Authority auth : authorities) {
				dto.addAuthority(auth.asDTO());
			}
		}
		return dto;
	}

}