package com.collectinfo.repository.jpa.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.collectinfo.domain.db.Menu;
import com.collectinfo.dto.query.PageResult;
import com.collectinfo.dto.query.QueryInfo;
import com.collectinfo.dto.query.SortInfo;
import com.collectinfo.enums.SortOrder;
import com.collectinfo.repository.jpa.dao.MenuDao;
import com.collectinfo.util.DatabaseUtil;
import com.collectinfo.util.QueryUtil;

@Repository
@SuppressWarnings("unchecked")
public class MenuDaoImpl implements MenuDao {

	@Autowired
	private EntityManager em;

	@Override
	public PageResult<Menu> find(QueryInfo<Menu> queryInfo) {
		queryInfo.getPageInfo().addSortInfo(new SortInfo("m.parent_id", SortOrder.ASC));
		PageRequest pageRequest = queryInfo.getPageInfo().toPageRequest();
		StringBuilder condition = new StringBuilder();
		condition.append("from Menu m").append("\n");
		condition.append("left join Authority a").append("\n");
		condition.append("on m.authority_id=a.id").append("\n");
		condition.append("where 1=1").append("\n");
		List<Object> args = new ArrayList<Object>();
		if (queryInfo.getObject() != null) {
			String value = queryInfo.getObject().getName();
			if (!StringUtils.isEmpty(value)) {
				condition.append("and (m.name like ?").append("\n");
				condition.append("or a.name like ?").append("\n");
				condition.append("or a.uri like ?)").append("\n");
				value = "%" + value + "%";
				args.add(value);
				args.add(value);
				args.add(value);
			}
		}
		StringBuilder countSql = new StringBuilder("select count(m.id)");
		countSql.append("\n").append(condition);
		Query countQuery = em.createNativeQuery(countSql.toString());
		QueryUtil.setArgs(args, countQuery);
		long count = DatabaseUtil.getCount(countQuery.getSingleResult());
		if (count > 0) {
			StringBuilder sql = new StringBuilder("select m.*");
			sql.append("\n").append(condition);
			QueryUtil.setOrder(sql, pageRequest);
			Query query = em.createNativeQuery(sql.toString(), Menu.class);
			QueryUtil.setArgs(args, query);
			QueryUtil.setPage(pageRequest, query);
			List<Menu> list = query.getResultList();
			return new PageResult<Menu>(list, count, queryInfo.getPageInfo());
		}
		return new PageResult<Menu>(new ArrayList<Menu>(), 0, queryInfo.getPageInfo());
	}

}
