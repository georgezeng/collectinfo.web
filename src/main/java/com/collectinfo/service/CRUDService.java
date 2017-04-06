package com.collectinfo.service;

import java.io.Serializable;
import java.util.Collection;

import com.collectinfo.domain.db.base.BaseEntity;

public interface CRUDService {
	<T extends BaseEntity> void save(T o);

	<T extends BaseEntity, K extends Serializable> T findOne(K id, Class<T> clazz);

	<T extends BaseEntity> void delete(T o);

	<T extends BaseEntity> void delete(Collection<T> c);

	<T extends BaseEntity, K extends Serializable> void delete(K id, Class<T> clazz);

	<T extends BaseEntity, K extends Serializable> void delete(Collection<K> c, Class<T> clazz);
}
