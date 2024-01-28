package com.blog.api.config;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.blog.api.config.data.UserSession;
import com.blog.api.exception.Unauthorized;
import com.blog.api.repository.SessionRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
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
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

		String jws = webRequest.getHeader("Authorization");
		if (jws == null || jws.isEmpty()) {
			throw new Unauthorized();
		}
		try {
			Jws<Claims> claims = Jwts.parser()
				.verifyWith(Keys.hmacShaKeyFor(
					Decoders.BASE64.decode("5e543e5b002425esffsfafeasfasefaewsfaes69afff816b83b677a")))
				.build()
				.parseSignedClaims(jws);
			log.info("claims={}",claims);
			String userId = claims.getPayload().getSubject();
			log.info("userid={}",userId);
			return new UserSession(Long.parseLong(userId));
		} catch (JwtException e) {
			throw new Unauthorized();
		}catch (Exception e){
			log.error(e.getMessage());
			throw new Unauthorized();
		}

	}
}
