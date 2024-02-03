package com.blog.api.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.api.config.UserPrincipal;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class MainController {
	@GetMapping("/")
	public String main() {
		return "메인 페이지입니다";
	}

	@GetMapping("/user")
	public String user(@AuthenticationPrincipal UserPrincipal userPrincipal) {
		System.out.println("userPrincipal = " + userPrincipal);
		return "사용자 페이지😎";
	}

	@GetMapping("/admin")
	public String admin() {
		return "관리자 페이지";
	}
}
