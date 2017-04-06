package com.collectinfo.util;

import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;

public class QueryUtil {
	public static void setOrder(StringBuilder sql, Pageable page) {
		if (page.getSort() != null) {
			Iterator<Order> orders = page.getSort().iterator();
			if (orders != null) {
				String orderStr = "";
				while (orders.hasNext()) {
					Order order = orders.next();
					orderStr += order.getProperty() + " " + order.getDirection().name() + ", ";
				}
				if (orderStr.endsWith(", ")) {
					sql.append("order by ");
					orderStr = orderStr.substring(0, orderStr.length() - 2);
					sql.append(orderStr);
				}
			}
		}
	}

	public static void setArgs(List<Object> args, Query query) {
		for (int i = 0; i < args.size(); i++) {
			query.setParameter(i + 1, args.get(i));
		}
	}

	public static void setPage(Pageable page, Query query) {
		query.setFirstResult(page.getOffset());
		query.setMaxResults(page.getPageSize());
	}

}
