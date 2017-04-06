package com.collectinfo.dto.query;

import com.collectinfo.enums.SortOrder;

public class SortInfo {

	private SortOrder order;
	private String property;
	
	public SortInfo() {
		
	}

	public SortInfo(String property, SortOrder order) {
		this.order = order;
		this.property = property;
	}

	public SortOrder getOrder() {
		return order;
	}

	public void setOrder(SortOrder order) {
		this.order = order;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

}