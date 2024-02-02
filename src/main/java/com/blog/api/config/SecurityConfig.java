package com.blog.api.config;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.blog.api.domain.User;
import com.blog.api.repository.UserRepository;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return new WebSecurityCustomizer() {  //lambda 변경 가능
			@Override
			public void customize(WebSecurity web) {
				web.ignoring()
					.requestMatchers(antMatcher("/favicon.ico"), antMatcher("/error"), antMatcher("/h2-console/**"));
			}
		};
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(
				(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
					.requestMatchers(antMatcher("/auth/login"), antMatcher(HttpMethod.POST, "/auth/signup")).permitAll()
					.anyRequest().authenticated()))
			.formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
				.usernameParameter("username")
				.passwordParameter("password")
				.loginPage("/auth/login")
				.loginProcessingUrl("/auth/login")
				.defaultSuccessUrl("/"))
			.rememberMe(rm -> rm.rememberMeParameter("remember")
				.alwaysRemember(false)
				.tokenValiditySeconds(2592000));
		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService(UserRepository userRepository) {
		return new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				User user = userRepository.findByEmail(username)
					.orElseThrow(() -> new UsernameNotFoundException(username + "을 찾을 수 없습니다"));
				return new UserPrincipal(user);
			}
		};
	}

	@Bean
	public PasswordEncoder encoder() {
		return new SCryptPasswordEncoder(
			16,
			8,
			1,
			32,
			64);
	}
}
