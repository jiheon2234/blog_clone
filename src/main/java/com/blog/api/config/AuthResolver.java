package com.blog.api.config;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.blog.api.config.data.UserSession;
import com.blog.api.domain.Session;
import com.blog.api.exception.Unauthorized;
import com.blog.api.repository.SessionRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        if(servletRequest == null){
            log.error("servletRequest is null!!!!");
            throw new Unauthorized();
        }
        Cookie[] cookies = servletRequest.getCookies();

        if(cookies.length ==0){
            log.error("cookie 없음");
            throw new Unauthorized();
        }

        String accessToken = cookies[0].getValue();

        //데이터베이스 사용자 확인작업

        Session session = sessionRepository.findByAccessToken(accessToken)
            .orElseThrow(Unauthorized::new);

        return new UserSession(session.getUser().getId());
    }
}
