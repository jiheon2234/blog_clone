package com.blog.api.controller;

import javax.crypto.SecretKey;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.blog.api.config.AppConfig;
import com.blog.api.request.Login;
import com.blog.api.response.SessionResponse;
import com.blog.api.service.AuthService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final AppConfig appConfig;

	@PostMapping("/auth/login")
	public SessionResponse login(@RequestBody Login login) { ////json id/pw => DB => token

		Long userId = authService.signIn(login);
		SecretKey key = Keys.hmacShaKeyFor(
			appConfig.getJwtKey());
		String jws = Jwts.builder()
			.subject(String.valueOf(userId))
			.signWith(key)
			.compact();

		return new SessionResponse(jws);

	}

}
