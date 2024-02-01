package com.blog.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class MainController {
	@GetMapping("/")
	public String main() {
		return "메인 페이지입니다";
	}
}
