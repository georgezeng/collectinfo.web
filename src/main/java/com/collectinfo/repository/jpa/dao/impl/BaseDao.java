package com.collectinfo.repository.jpa.dao.impl;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.collectinfo.domain.db.base.BaseEntity;
import com.collectinfo.repository.jpa.dao.CommonDao;

@Repository
public class BaseDao implements CommonDao {
	@Autowired
	private EntityManager em;

	@Override
	public <T extends BaseEntity> void save(T o) {
		em.persist(o);
	}

	@Override
	public <T extends BaseEntity, K extends Serializable> T findOne(K id, Class<T> clazz) {
		return em.find(clazz, id);
	}

	@Override
	public <T extends BaseEntity> void delete(T o) {
		em.remove(o);
	}

	@Override
	public <T extends BaseEntity, K extends Serializable> void delete(K id, Class<T> clazz) {
		T o = em.find(clazz, id);
		if (o != null) {
			em.remove(o);
		}
	}

	@Override
	public <T extends BaseEntity> void delete(Collection<T> c) {
		for (T o : c) {
			delete(o);
		}
	}

	@Override
	public <T extends BaseEntity, K extends Serializable> void delete(Collection<K> c, Class<T> clazz) {
		for (K id : c) {
			delete(id, clazz);
		}
	}
}
