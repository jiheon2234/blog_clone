package com.blog.api.config;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

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
					.requestMatchers(antMatcher("/auth/login")).permitAll()
					.anyRequest().authenticated()))
			.formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
				.usernameParameter("username")
				.passwordParameter("password")
				.loginPage("/auth/login")
				.loginProcessingUrl("/auth/login")
				.defaultSuccessUrl("/"))
			.userDetailsService(userDetailsService())
			.rememberMe(rm -> rm.rememberMeParameter("remember")
				.alwaysRemember(false)
				.tokenValiditySeconds(2592000));
		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		UserDetails user = User.withUsername("jiheon").password("2234").roles("ADMIN").build();
		manager.createUser(user);
		return manager;
	}

	@Bean
	public PasswordEncoder encoder() {
		return NoOpPasswordEncoder.getInstance();
	}
}
