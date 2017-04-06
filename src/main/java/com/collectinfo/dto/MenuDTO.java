package com.collectinfo.dto;

import java.util.ArrayList;
import java.util.List;

public class MenuDTO {
	private Long id;
	private String name;
	private AuthorityDTO authority;
	private MenuDTO parent;
	private Integer sort;
	private List<MenuDTO> children;
	private List<AuthorityDTO> allAuthorities;
	private List<MenuDTO> allParents;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public void addChild(MenuDTO child) {
		if (children == null) {
			children = new ArrayList<MenuDTO>();
		}
		children.add(child);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AuthorityDTO getAuthority() {
		return authority;
	}

	public void setAuthority(AuthorityDTO authority) {
		this.authority = authority;
	}

	public MenuDTO getParent() {
		return parent;
	}

	public void setParent(MenuDTO parent) {
		this.parent = parent;
	}

	public List<AuthorityDTO> getAllAuthorities() {
		return allAuthorities;
	}

	public void setAllAuthorities(List<AuthorityDTO> allAuthorities) {
		this.allAuthorities = allAuthorities;
	}

	public List<MenuDTO> getAllParents() {
		return allParents;
	}

	public void setAllParents(List<MenuDTO> allParents) {
		this.allParents = allParents;
	}

	public List<MenuDTO> getChildren() {
		return children;
	}

	public void setChildren(List<MenuDTO> children) {
		this.children = children;
	}
}
