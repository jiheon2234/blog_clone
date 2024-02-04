package com.blog.api.config;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.transaction.annotation.Transactional;

import com.blog.api.domain.User;
import com.blog.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MyMockSecurityContext implements WithSecurityContextFactory<MyMockUser> {

	private final UserRepository userRepository;

	@Override
	@Transactional
	public SecurityContext createSecurityContext(MyMockUser annotation) {
		var user = User.builder()
			.email(annotation.email())
			.name(annotation.name())
			.password(annotation.password())
			.build();

		userRepository.save(user);

		var principal = new UserPrincipal(user);

		var role = new SimpleGrantedAuthority("ROLE_ADMIN");
		var autenticationToken = new UsernamePasswordAuthenticationToken(principal, user.getPassword(),
			List.of(role));

		var context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(autenticationToken);
		return context;
	}
}
