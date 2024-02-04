package com.blog.api.config.handler;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import com.blog.api.config.UserPrincipal;
import com.blog.api.domain.Post;
import com.blog.api.exception.PostNotFound;
import com.blog.api.repository.PostRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class myPermissionEvaluator implements PermissionEvaluator {

	private final PostRepository postRepository;

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
		Object permission) {
		var userPrincipal = (UserPrincipal)authentication.getPrincipal();
		Post post = postRepository.findById((Long)targetId)
			.orElseThrow(PostNotFound::new);

		if (!post.getUserId().equals(userPrincipal.getUserId())) {
			log.error("[인가실패] 해당 사용자가 작성한 글이 아닙니다", targetId);
			return false;
		}
		;

		return true;
	}
}
