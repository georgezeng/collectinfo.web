package com.collectinfo.domain.db.base;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import com.collectinfo.domain.db.listener.LastUpdateListener;
import com.collectinfo.util.ObjectUtil;

@EntityListeners(LastUpdateListener.class)
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String createBy;
	private String editBy;
	private Date createTime;
	private Date editTime;

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getEditBy() {
		return editBy;
	}

	public void setEditBy(String editBy) {
		this.editBy = editBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getEditTime() {
		return editTime;
	}

	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o == this) {
			return true;
		}
		if (BaseEntity.class.isAssignableFrom(o.getClass()) && getClass().equals(o.getClass())) {
			try {
				for (Field f : ObjectUtil.getAllDeclareFields(getClass())) {
					f.setAccessible(true);
					if (!ObjectUtil.equals(f.get(this), f.get(o))) {
						return false;
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			return true;
		}
		return false;
	}

	public int hashCode() {
		int hash = 0;
		try {
			for (Field f : ObjectUtil.getAllDeclareFields(getClass())) {
				f.setAccessible(true);
				if (f.getType().isPrimitive() && f.get(this) != null) {
					hash += f.get(this).hashCode();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return hash;
	}
}
