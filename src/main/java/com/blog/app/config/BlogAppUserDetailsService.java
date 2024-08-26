package com.blog.app.config;

import com.blog.app.entity.AppUser;
import com.blog.app.entity.Authority;
import com.blog.app.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogAppUserDetailsService implements UserDetailsService {

	private final AppUserRepository appUserRepository;

	/**
	 * @param username the username identifying the author whose data is required.
	 * @return
	 * @throws UsernameNotFoundException
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		AppUser appUser = appUserRepository.findByUsername(username).orElseThrow(() -> new
				UsernameNotFoundException("User details not found for the author: " + username));

		return new User(appUser.getUsername(), appUser.getPassword(), getGrantedAuthorities(appUser.getAuthorities()));
	}


	private List<GrantedAuthority> getGrantedAuthorities(Set<Authority> authorities) {

		return authorities.stream().map(authority ->
				new SimpleGrantedAuthority(authority.getName())).collect(Collectors.toList());
	}
}
