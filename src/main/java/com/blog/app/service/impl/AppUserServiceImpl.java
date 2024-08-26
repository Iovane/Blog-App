package com.blog.app.service.impl;

import com.blog.app.dto.AppUserCreateDTO;
import com.blog.app.entity.AppUser;
import com.blog.app.entity.Authority;
import com.blog.app.exceptions.UserNotFoundException;
import com.blog.app.mapper.AppUserMapper;
import com.blog.app.repository.AppUserRepository;
import com.blog.app.repository.AuthorityRepository;
import com.blog.app.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {

	private final AppUserRepository appUserRepository;
	private final AuthorityRepository authorityRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public AppUser findByUsername(String username) {
		return appUserRepository.findByUsername(username).orElse(null);
	}

	@Override
	public void createUser(AppUserCreateDTO appUserCreateDTO) {

		AppUser appUser = AppUserMapper.mapToAppUser(appUserCreateDTO, new AppUser());
		appUser.setPassword(passwordEncoder.encode(appUserCreateDTO.getPassword()));
		appUser.setAuthorities(getAuthorities(appUser));

		appUserRepository.save(appUser);
		authorityRepository.saveAll(appUser.getAuthorities());
	}

	/**
	 * @param username
	 * @param currentUserUsername
	 */
	@Override
	public void deleteUserByUsername(String username, String currentUserUsername) {

		AppUser user = appUserRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));

		AppUser currentUser = appUserRepository.findByUsername(currentUserUsername).get();

		if (user.getUsername().equals(currentUserUsername) || currentUser.isAdmin()) {
			appUserRepository.delete(user);
		} else throw new AccessDeniedException("");

	}

	/**
	 * Sets default authority for newly registered users to "USER"
	 * @param appUser
	 * @return Set<Authority> with default authority "USER"
	 */
	private static Set<Authority> getAuthorities(AppUser appUser) {
		Set<Authority> authorities = new HashSet<>(1);
		authorities.add(new Authority(0L, "USER", appUser));

		return authorities;
	}
}
