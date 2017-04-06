package com.collectinfo.domain.db.listener;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.util.StringUtils;

import com.collectinfo.domain.db.base.BaseEntity;
import com.collectinfo.domain.db.base.BaseUser;
import com.collectinfo.util.AssertUtil;
import com.collectinfo.util.UserSessionContext;

public class LastUpdateListener {
	@PrePersist
	@PreUpdate
	public void onPrePersist(BaseEntity o) {
		BaseUser user = UserSessionContext.current();
		AssertUtil.notNull(user, "Current user cannot be null");
		String username = user.getUsername();
		Date now = new Date();
		if (StringUtils.isEmpty(o.getCreateBy())) {
			o.setCreateBy(username);
			o.setCreateTime(now);
		}
		o.setEditBy(username);
		o.setEditTime(now);
	}
}
