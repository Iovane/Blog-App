package com.blog.app.service;

import com.blog.app.dto.AppUserCreateDTO;
import com.blog.app.entity.AppUser;

public interface AppUserService {

	AppUser findByUsername(String username);

	void createUser(AppUserCreateDTO appUserCreateDTO);

	void deleteUserByUsername(String username, String currentUserUsername);
}
