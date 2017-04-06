package com.collectinfo.service.impl;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collectinfo.domain.db.base.BaseEntity;
import com.collectinfo.repository.jpa.dao.CommonDao;
import com.collectinfo.service.CRUDService;

@Service
public class CRUDServiceImpl implements CRUDService {

	@Autowired
	private CommonDao dao;

	@Override
	public <T extends BaseEntity> void save(T o) {
		dao.save(o);
	}

	@Override
	public <T extends BaseEntity, K extends Serializable> T findOne(K id, Class<T> clazz) {
		return dao.findOne(id, clazz);
	}

	@Override
	public <T extends BaseEntity> void delete(T o) {
		dao.delete(o);
	}

	@Override
	public <T extends BaseEntity> void delete(Collection<T> c) {
		dao.delete(c);
	}

	@Override
	public <T extends BaseEntity, K extends Serializable> void delete(K id, Class<T> clazz) {
		dao.delete(id, clazz);
	}

	@Override
	public <T extends BaseEntity, K extends Serializable> void delete(Collection<K> c, Class<T> clazz) {
		dao.delete(c, clazz);
	}

}
