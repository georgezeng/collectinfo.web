package com.collectinfo.repository.jpa.api;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.collectinfo.domain.db.Authority;

public interface AuthorityRepository extends PagingAndSortingRepository<Authority, Long> {
	Authority findByName(String name);

	Authority findByUri(String uri);

}