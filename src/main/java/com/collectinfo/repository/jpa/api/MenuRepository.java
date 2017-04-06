package com.collectinfo.repository.jpa.api;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.collectinfo.domain.db.Menu;

public interface MenuRepository extends PagingAndSortingRepository<Menu, Long> {
	List<Menu> findByParent(Menu parent);

	List<Menu> findByAuthorityIsNull();
}