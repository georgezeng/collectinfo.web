--liquibase formatted sql

--changeset collectinfo:2 failOnError:true

INSERT INTO `authority` (`id`, `name`, `uri`, `description`, `create_by`, `edit_by`, `create_time`, `edit_time`)
VALUES
	(1, 'AUTH_SUPER_ADMIN', '/**', '超级管理员权限', 'System', 'System', '2017-04-05 11:14:10', '2017-04-05 11:14:10'),
	(2, 'AUTH_CLIENT', '/**', '客户基础权限', 'System', 'System', '2017-04-05 11:14:10', '2017-04-05 11:14:10'),
	(3, 'AUTH_USER_EDIT', '/user/edit.html', '编辑用户页面', 'admin', 'admin', '2017-04-05 11:20:02', '2017-04-05 11:20:02'),
	(4, 'AUTH_USER_LIST', '/user/list.html', '用户列表页面', 'admin', 'admin', '2017-04-05 11:17:40', '2017-04-05 11:17:40'),
	(5, 'AUTH_USER_MVC_SAVE', '/mvc/user/save', '保存用户', 'admin', 'admin', '2017-04-05 11:22:21', '2017-04-05 11:22:21'),
	(6, 'AUTH_USER_MVC_DELETE', '/mvc/user/remove', '删除用户', 'admin', 'admin', '2017-04-05 11:22:29', '2017-04-05 11:22:29'),
	(7, 'AUTH_USER_MVC_DETAIL', '/mvc/user/one/**', '查询用户详情', 'admin', 'admin', '2017-04-05 11:21:09', '2017-04-05 11:21:09'),
	(8, 'AUTH_USER_MVC_LIST', '/mvc/user/list', '查询用户列表', 'admin', 'admin', '2017-04-05 11:20:58', '2017-04-05 11:20:58'),
	(9, 'AUTH_ROLE_LIST', '/role/list.html', '角色列表页面', 'admin', 'admin', '2017-04-05 11:23:42', '2017-04-05 11:23:42'),
	(10, 'AUTH_ROLE_EDIT', '/role/edit.html', '编辑角色页面', 'admin', 'admin', '2017-04-05 11:24:21', '2017-04-05 11:24:21'),
	(11, 'AUTH_ROLE_MVC_LIST', '/mvc/role/list', '查询角色列表', 'admin', 'admin', '2017-04-05 11:26:02', '2017-04-05 11:26:02'),
	(12, 'AUTH_ROLE_MVC_SAVE', '/mvc/role/save', '保存角色', 'admin', 'admin', '2017-04-05 11:26:26', '2017-04-05 11:26:26'),
	(13, 'AUTH_ROLE_MVC_DETAIL', '/mvc/role/one/**', '查询角色详情', 'admin', 'admin', '2017-04-05 11:26:56', '2017-04-05 11:26:56'),
	(14, 'AUTH_ROLE_MVC_DELETE', '/mvc/role/remove', '删除角色', 'admin', 'admin', '2017-04-05 11:30:13', '2017-04-05 11:30:13'),
	(15, 'AUTH_MENU_LIST', '/menu/list.html', '菜单列表页面', 'admin', 'admin', '2017-04-05 11:36:20', '2017-04-05 11:36:20'),
	(16, 'AUTH_MENU_EDIT', '/menu/edit.html', '编辑菜单页面', 'admin', 'admin', '2017-04-05 11:36:43', '2017-04-05 11:36:43'),
	(17, 'AUTH_MENU_MVC_LIST', '/mvc/menu/list', '查询菜单列表', 'admin', 'admin', '2017-04-05 11:37:14', '2017-04-05 11:37:14'),
	(18, 'AUTH_MENU_MVC_SAVE', '/mvc/menu/save', '保存菜单', 'admin', 'admin', '2017-04-05 11:37:36', '2017-04-05 11:37:36'),
	(19, 'AUTH_MENU_MVC_DETAIL', '/mvc/menu/one/**', '查询菜单详情', 'admin', 'admin', '2017-04-05 11:38:11', '2017-04-05 11:38:11'),
	(20, 'AUTH_MENU_MVC_DELETE', '/mvc/menu/remove', '删除菜单', 'admin', 'admin', '2017-04-05 11:38:54', '2017-04-05 11:38:54'),
	(21, 'AUTH_AUTHORITY_LIST', '/authority/list.html', '权限列表页面', 'admin', 'admin', '2017-04-05 11:39:35', '2017-04-05 11:39:35'),
	(22, 'AUTH_AUTHORITY_EDIT', '/authority/edit.html', '编辑权限页面', 'admin', 'admin', '2017-04-05 11:39:55', '2017-04-05 11:39:55'),
	(23, 'AUTH_AUTHORITY_MVC_LIST', '/mvc/authority/list', '查询权限列表', 'admin', 'admin', '2017-04-05 11:40:19', '2017-04-05 11:40:19'),
	(24, 'AUTH_AUTHORITY_MVC_DETAIL', '/mvc/authority/one/**', '查询权限详情', 'admin', 'admin', '2017-04-05 11:41:13', '2017-04-05 11:41:13'),
	(25, 'AUTH_AUTHORITY_MVC_SAVE', '/mvc/authority/save', '保存权限', 'admin', 'admin', '2017-04-05 11:41:36', '2017-04-05 11:41:36'),
	(26, 'AUTH_AUTHORITY_DELETE', '/mvc/authority/remove', '删除权限', 'admin', 'admin', '2017-04-05 11:41:59', '2017-04-05 11:41:59'),
	(27, 'AUTH_USER_PROFILE', '/user/profile.html', '个人资料页面', 'admin', 'admin', '2017-04-05 17:14:10', '2017-04-05 17:14:10'),
	(28, 'AUTH_USER_CHANGE_PWD', '/user/changePwd.html', '修改个人密码页面', 'admin', 'admin', '2017-04-05 17:14:44', '2017-04-05 17:14:44'),
	(29, 'AUTH_USER_MVC_PROFILE', '/mvc/user/profile', '查询个人资料', 'admin', 'admin', '2017-04-05 17:15:10', '2017-04-05 17:15:10'),
	(30, 'AUTH_USER_MVC_CHANGE_PWD', '/mvc/user/changePwd', '修改个人密码', 'admin', 'admin', '2017-04-05 17:15:54', '2017-04-05 17:15:54'),
	(31, 'AUTH_USER_MVC_SAVE_PROFILE', '/mvc/user/saveProfile', '保存个人资料', 'admin', 'admin', '2017-04-05 17:16:36', '2017-04-05 17:16:36');


INSERT INTO `role` (`id`, `name`, `description`, `create_by`, `edit_by`, `create_time`, `edit_time`)
VALUES
	(1, 'SuperAdmin', '超级管理员', 'System', 'System', '2017-04-05 11:14:10', '2017-04-05 11:14:10'),
	(2, 'Client', '客户', 'System', 'System', '2017-04-05 11:14:10', '2017-04-05 13:24:24');

INSERT INTO `role_authority` (`role_id`, `authority_id`)
VALUES
	(1, 1),
	(2, 2),
	(2, 27),
	(2, 28),
	(2, 29),
	(2, 30),
	(2, 31);


INSERT INTO `user` (`id`, `username`, `password`, `nickname`, `email`, `account_non_expired`, `account_non_locked`, `credentials_non_expired`, `enabled`, `create_by`, `edit_by`, `create_time`, `edit_time`)
VALUES
	(1, 'admin', '$2a$10$zRNj.UYGVviqH3Pe9aROOeUX7ZSq9Z7fpDIw.c4uEnVTIHNtC9ETK', '超级管理员', 'feleio@qq.com', 1, 1, 1, 1, 'System', 'System', '2017-04-05 11:14:10', '2017-04-05 13:24:24');

INSERT INTO `user_role` (`user_id`, `role_id`)
VALUES
	(1, 1);

INSERT INTO `menu` (`id`, `name`, `authority_id`, `parent_id`, `sort`, `create_by`, `edit_by`, `create_time`, `edit_time`)
VALUES
	(1, '系统设置', NULL, NULL, 0, 'admin', 'admin', '2017-04-05 11:42:33', '2017-04-05 11:42:33'),
	(2, '用户列表', '4', 1, 0, 'admin', 'admin', '2017-04-05 11:42:50', '2017-04-05 11:42:50'),
	(3, '角色列表', '9', 1, 1, 'admin', 'admin', '2017-04-05 11:43:08', '2017-04-05 11:43:08'),
	(4, '权限列表', '21', 1, 2, 'admin', 'admin', '2017-04-05 11:48:15', '2017-04-05 11:48:15'),
	(5, '菜单列表', '15', 1, 3, 'admin', 'admin', '2017-04-05 11:48:30', '2017-04-05 11:48:30');
