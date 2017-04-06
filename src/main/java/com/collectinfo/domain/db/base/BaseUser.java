package com.collectinfo.domain.db.base;

import java.util.Collection;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.collectinfo.constant.SystemConstant;

@MappedSuperclass
public abstract class BaseUser extends LongKeyEntity implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final BaseUser SYSTEM_USER = new SimpleUser(0l, SystemConstant.USERNAME_OF_SYSTEM);
	public static final BaseUser ALIPAY_USER = new SimpleUser(-1l, SystemConstant.USERNAME_OF_ALIPAY);

	private static class SimpleUser extends BaseUser {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public SimpleUser(long id, String username) {
			setId(id);
			setUsername(username);
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	@NotEmpty(message = "用户名不能为空")
	@Size(message = "用户名不能小于3位", min = 3)
	private String username;
	@NotEmpty(message = "密码不能为空")
	private String password;
	@NotEmpty(message = "昵称不能为空")
	private String nickname;
	private Boolean accountNonExpired;
	private Boolean accountNonLocked;
	private Boolean credentialsNonExpired;
	private Boolean enabled;

	@Transient
	private String confirmPassword;

	@Transient
	private String oldPassword;

	public void enabled() {
		accountNonExpired = true;
		accountNonLocked = true;
		credentialsNonExpired = true;
		enabled = true;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Boolean getAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(Boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public Boolean getAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(Boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public Boolean getCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}
}
