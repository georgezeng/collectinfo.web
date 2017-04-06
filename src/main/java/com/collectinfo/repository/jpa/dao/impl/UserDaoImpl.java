package com.collectinfo.repository.jpa.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.collectinfo.domain.db.User;
import com.collectinfo.dto.query.PageResult;
import com.collectinfo.dto.query.QueryInfo;
import com.collectinfo.repository.jpa.dao.UserDao;
import com.collectinfo.util.DatabaseUtil;
import com.collectinfo.util.QueryUtil;

@Repository
@SuppressWarnings("unchecked")
public class UserDaoImpl implements UserDao {

	@Autowired
	private EntityManager em;

	@Override
	public PageResult<User> find(QueryInfo<User> queryInfo) {
		PageRequest pageRequest = queryInfo.getPageInfo().toPageRequest();
		StringBuilder condition = new StringBuilder();
		condition.append("from user").append("\n");
		condition.append("where 1=1").append("\n");
		List<Object> args = new ArrayList<Object>();
		if (queryInfo.getObject() != null) {
			String value = queryInfo.getObject().getUsername();
			if (!StringUtils.isEmpty(value)) {
				condition.append("and (username like ?").append("\n");
				condition.append("or nickname like ?").append("\n");
				condition.append("or email like ?)").append("\n");
				value = "%" + value + "%";
				args.add(value);
				args.add(value);
				args.add(value);
			}
		}
		StringBuilder countSql = new StringBuilder("select count(id)");
		countSql.append("\n").append(condition);
		Query countQuery = em.createNativeQuery(countSql.toString());
		QueryUtil.setArgs(args, countQuery);
		long count = DatabaseUtil.getCount(countQuery.getSingleResult());
		if (count > 0) {
			StringBuilder sql = new StringBuilder("select *");
			sql.append("\n").append(condition);
			QueryUtil.setOrder(sql, pageRequest);
			Query query = em.createNativeQuery(sql.toString(), User.class);
			QueryUtil.setArgs(args, query);
			QueryUtil.setPage(pageRequest, query);
			List<User> list = query.getResultList();
			return new PageResult<User>(list, count, queryInfo.getPageInfo());
		}
		return new PageResult<User>(new ArrayList<User>(), 0, queryInfo.getPageInfo());
	}

}
