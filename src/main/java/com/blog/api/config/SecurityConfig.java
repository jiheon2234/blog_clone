package com.blog.api.config;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;

import com.blog.api.config.filter.EmailPasswordAuthFilter;
import com.blog.api.config.handler.Http401Handler;
import com.blog.api.config.handler.Http403Handler;
import com.blog.api.config.handler.LoginFailHandler;
import com.blog.api.config.handler.LoginSuccessHandler;
import com.blog.api.domain.User;
import com.blog.api.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity(debug = true)
@Slf4j
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	private final ObjectMapper objectMapper;
	private final UserRepository userRepository;

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
					.requestMatchers(antMatcher("/auth/login"), antMatcher("/auth/signup")).permitAll()
					// .requestMatchers(antMatcher("/user")).hasRole("USER")
					// .requestMatchers(antMatcher("/admin")).hasRole("ADMIN")
					.anyRequest().permitAll()))
			.addFilterBefore(usernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
			// .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
			// 	.usernameParameter("username")
			// 	.passwordParameter("password")
			// 	.loginPage("/auth/login")
			// 	.loginProcessingUrl("/auth/login")
			// 	.defaultSuccessUrl("/")
			// 	.failureHandler(new LoginFailHandler(objectMapper))
			// )
			.exceptionHandling(e -> {
				e.accessDeniedHandler(new Http403Handler(objectMapper));
				e.authenticationEntryPoint(new Http401Handler(objectMapper));
			})
			.rememberMe(rm -> rm.rememberMeParameter("remember")
				.alwaysRemember(false)
				.tokenValiditySeconds(2592000));
		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService(userRepository));
		provider.setPasswordEncoder(passwordEncoder());
		return new ProviderManager(provider);
	}

	@Bean
	public EmailPasswordAuthFilter usernamePasswordAuthenticationFilter() {
		EmailPasswordAuthFilter filter = new EmailPasswordAuthFilter("/auth/login", objectMapper);
		filter.setAuthenticationManager(authenticationManager());
		filter.setAuthenticationSuccessHandler(new LoginSuccessHandler(objectMapper));
		filter.setAuthenticationFailureHandler(new LoginFailHandler(objectMapper));
		filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());

		SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
		rememberMeServices.setAlwaysRemember(true);
		rememberMeServices.setValiditySeconds(3600 * 24 * 30);
		filter.setRememberMeServices(rememberMeServices);
		return filter;
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
	public PasswordEncoder passwordEncoder() {
		return new SCryptPasswordEncoder(
			16,
			8,
			1,
			32,
			64);
	}
}
