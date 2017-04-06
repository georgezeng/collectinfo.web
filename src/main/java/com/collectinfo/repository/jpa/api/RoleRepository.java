package com.collectinfo.repository.jpa.api;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.collectinfo.domain.db.Role;

public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {
	Role findByName(String name);

	@Modifying
	@Query(value = "delete from user_role where user_id=?1", nativeQuery = true)
	void removeUserFromAllRoles(Long userId);
}