package com.collectinfo.dto.query;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

public class PageInfo {
	private int pageNo;
	private int pageSize;
	private List<SortInfo> sorts = new ArrayList<SortInfo>();

	public PageInfo() {

	}

	public PageInfo(int pageNo, int pageSize) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	public PageInfo(int pageNo, int pageSize, SortInfo sortInfo) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		sorts.add(sortInfo);
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void addSortInfo(SortInfo sortInfo) {
		sorts.add(sortInfo);
	}

	public List<SortInfo> getSorts() {
		return sorts;
	}

	public void setSorts(List<SortInfo> sorts) {
		this.sorts = sorts;
	}

	public PageRequest toPageRequest() {
		if (sorts != null && sorts.size() > 0) {
			List<Order> orders = new ArrayList<Order>();
			for (SortInfo sort : sorts) {
				orders.add(new Order(Direction.valueOf(sort.getOrder().name()), sort.getProperty()));
			}
			return new PageRequest(pageNo - 1, pageSize, new Sort(orders.toArray(new Order[] {})));
		}
		return new PageRequest(pageNo - 1, pageSize);
	}

}
