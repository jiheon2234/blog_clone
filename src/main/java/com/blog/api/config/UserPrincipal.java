package com.blog.api.config;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserPrincipal extends User {

	// public UserPrincipal(String username, String password, Collection<? extends GrantedAuthority> authorities) {
	// 	super(username, password, authorities);
	// }

	private final Long userId;

	public UserPrincipal(com.blog.api.domain.User user) {
		super(user.getEmail(), user.getPassword(),
			List.of(
				new SimpleGrantedAuthority("ROLE_ADMIN")
			));
		this.userId = user.getId();
	}

	public Long getUserId() {
		return userId;
	}
}
