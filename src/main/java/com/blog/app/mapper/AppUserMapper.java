package com.blog.app.mapper;

import com.blog.app.dto.AppUserCreateDTO;
import com.blog.app.dto.AppUserDetailDTO;
import com.blog.app.entity.AppUser;

public class AppUserMapper {


	public static AppUserDetailDTO mapToAppUserDTO(AppUser appUser, AppUserDetailDTO appUserDetailDTO) {
		appUserDetailDTO.setUsername(appUser.getUsername());
		appUserDetailDTO.setEmail(appUser.getEmail());
		appUserDetailDTO.setPosts(PostMapper.mapToListOfPostDetailDTO(appUser.getPosts()));
		appUserDetailDTO.setComments(CommentMapper.mapToListOfCommentDetailDTO(appUser.getComments()));

		return appUserDetailDTO;
	}

	public static AppUser mapToAppUser(AppUserCreateDTO appUserCreateDTO, AppUser appUser) {
		appUser.setUsername(appUserCreateDTO.getUsername());
		appUser.setPassword(appUserCreateDTO.getPassword());
		appUser.setEmail(appUserCreateDTO.getEmail());

		return appUser;
	}

}
