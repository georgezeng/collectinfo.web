package com.collectinfo.repository.redis.api;

import org.springframework.data.repository.CrudRepository;

import com.collectinfo.domain.redis.RedisAuthority;

public interface RedisAuthorityRepository extends CrudRepository<RedisAuthority, String> {
	RedisAuthority findByName(String name);

	RedisAuthority findByUri(String uri);
}