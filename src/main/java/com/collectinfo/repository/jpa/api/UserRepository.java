package com.collectinfo.repository.jpa.api;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import com.collectinfo.domain.db.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long>, QueryByExampleExecutor<User> {
	User findByUsername(String username);

	User findByEmail(String email);

	List<User> findByEnabled(Boolean enabled);
}
