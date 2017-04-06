package com.collectinfo.domain.db;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import com.collectinfo.domain.db.base.LongKeyEntity;
import com.collectinfo.dto.MenuDTO;

@Entity
@Table(name = "menu")
public class Menu extends LongKeyEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "名称不能为空")
	private String name;
	@OneToOne
	@JoinColumn(name = "authority_id")
	private Authority authority;
	@Min(message = "序号必须大于等于0", value = 0)
	private Integer sort;
	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Menu parent;
	@OneToMany
	@JoinColumn(name = "parent_id", referencedColumnName = "id")
	private Set<Menu> children;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Authority getAuthority() {
		return authority;
	}

	public void setAuthority(Authority authority) {
		this.authority = authority;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Menu getParent() {
		return parent;
	}

	public void setParent(Menu parent) {
		this.parent = parent;
	}

	public Set<Menu> getChildren() {
		return children;
	}

	public void setChildren(Set<Menu> children) {
		this.children = children;
	}

	public MenuDTO asDTO() {
		return asDTO(true);
	}

	public MenuDTO asDTO(boolean onlyOriginal) {
		MenuDTO dto = new MenuDTO();
		BeanUtils.copyProperties(this, dto, "parent", "authority", "children");
		if (onlyOriginal && parent != null) {
			dto.setParent(parent.asDTO(!onlyOriginal));
		}
		if (authority != null) {
			dto.setAuthority(authority.asDTO());
		}
		if (onlyOriginal && children != null) {
			for (Menu child : children) {
				dto.addChild(child.asDTO(!onlyOriginal));
			}
		}
		return dto;
	}

}