package com.collectinfo.dto.query;

public class QueryInfo<T> {
	private T object;
	private PageInfo pageInfo;

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public PageInfo getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}

}
