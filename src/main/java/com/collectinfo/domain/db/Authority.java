package com.collectinfo.domain.db;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import com.collectinfo.domain.db.base.BaseAuthority;
import com.collectinfo.dto.AuthorityDTO;

@Entity
@Table(name = "authority")
public class Authority extends BaseAuthority {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "URI不能为空")
	private String uri;
	@NotEmpty(message = "描述不能为空")
	private String description;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AuthorityDTO asDTO() {
		AuthorityDTO dto = new AuthorityDTO();
		BeanUtils.copyProperties(this, dto);
		return dto;
	}

}