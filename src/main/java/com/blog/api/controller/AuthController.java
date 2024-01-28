package com.blog.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.blog.api.request.Login;
import com.blog.api.response.SessionResponse;
import com.blog.api.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public SessionResponse  login(@RequestBody Login login){ ////json id/pw => DB => token
        log.info(">>>login={}",login);
        String accessToken = authService.signIn(login);
        return new SessionResponse(accessToken);
    }

}
