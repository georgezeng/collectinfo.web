package com.collectinfo.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.collectinfo.domain.db.User;
import com.collectinfo.dto.RoleDTO;
import com.collectinfo.dto.UserDTO;
import com.collectinfo.dto.query.PageResult;
import com.collectinfo.dto.query.QueryInfo;

public interface UserService extends UserDetailsService {
	void createClient(User client);

	void create(User user);

	void update(User user);

	UserDTO findDTO(Long id);

	PageResult<UserDTO> find(QueryInfo<User> queryInfo);
	
	List<UserDTO> transferToDTO(Iterable<User> users);
	
	void getUnselectedUsers(RoleDTO role);
	
	void resetPassword(Long userId, String password);
	
	void updateProfile(User newInfo);
}
