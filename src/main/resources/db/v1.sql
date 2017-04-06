--liquibase formatted sql

--changeset collectinfo:1 failOnError:true

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `nickname` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `account_non_expired` bit(1) NOT NULL,
  `account_non_locked` bit(1) NOT NULL,
  `credentials_non_expired` bit(1) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `create_by` varchar(100) NOT NULL,
  `edit_by` varchar(100) NOT NULL,
  `create_time` datetime NOT NULL,
  `edit_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx_User_username` (`username`),
  UNIQUE KEY `uidx_User_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` varchar(100) NOT NULL,
  `create_by` varchar(100) NOT NULL,
  `edit_by` varchar(100) NOT NULL,
  `create_time` datetime NOT NULL,
  `edit_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx_Role_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  UNIQUE KEY `uidx_User_Role_user_id_role_id` (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `authority` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `uri` varchar(255) NULL,
  `description` varchar(255) NOT NULL,
  `create_by` varchar(100) NOT NULL,
  `edit_by` varchar(100) NOT NULL,
  `create_time` datetime NOT NULL,
  `edit_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx_Authority_name` (`name`),
  KEY `idx_Authority_uri` (`uri`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `role_authority` (
  `role_id` bigint(20) NOT NULL,
  `authority_id` bigint(20) NOT NULL,
  UNIQUE KEY `uidx_Role_Authority_role_id_authority_id` (`role_id`,`authority_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `authority_id` varchar(255) NULL,
  `parent_id` bigint(20) NULL,
  `sort` int(11) NOT NULL,
  `create_by` varchar(100) NOT NULL,
  `edit_by` varchar(100) NOT NULL,
  `create_time` datetime NOT NULL,
  `edit_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx_Menu_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
