package com.collectinfo.repository.redis.api;

import org.springframework.data.repository.CrudRepository;

import com.collectinfo.domain.redis.RedisCommonStore;

public interface RedisCommonStoreRepository extends CrudRepository<RedisCommonStore, String> {
}