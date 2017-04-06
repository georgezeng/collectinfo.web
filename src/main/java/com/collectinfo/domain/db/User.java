package com.collectinfo.domain.db;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.collectinfo.domain.db.base.BaseUser;
import com.collectinfo.dto.UserDTO;

@Entity
@Table(name = "user")
public class User extends BaseUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToMany(mappedBy = "users")
	private Set<Role> roles;

	@NotEmpty(message = "邮箱不能为空")
	@Email(message = "邮箱格式有误")
	private String email;

	@Transient
	private String captcha;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		if (roles != null) {
			for (Role role : roles) {
				if (role.getAuthorities() != null) {
					for (Authority auth : role.getAuthorities()) {
						authorities.add(new SimpleGrantedAuthority(auth.getName()));
					}
				}
			}
		}
		return authorities;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public void addRole(Role role) {
		if (roles == null) {
			roles = new HashSet<Role>();
		}
		roles.add(role);
	}

	public boolean hasRole(String roleName) {
		for (Role r : roles) {
			if (r.getName().equals(roleName)) {
				return true;
			}
		}
		return false;
	}

	public UserDTO asDTO() {
		return asDTO(true);
	}

	public UserDTO asDTO(boolean withRole) {
		UserDTO dto = new UserDTO();
		BeanUtils.copyProperties(this, dto, "roles");
		if (withRole && roles != null) {
			for (Role role : roles) {
				dto.addRole(role.asDTO(false));
			}
		}
		return dto;
	}
}
