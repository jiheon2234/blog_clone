package com.blog.api.repository.post;

import static com.blog.api.domain.QPost.*;

import java.util.List;

import com.blog.api.domain.Post;
import com.blog.api.request.post.PostSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Post> getList(PostSearch postSearch) {
		return jpaQueryFactory.selectFrom(post)
			.limit(postSearch.getSize())
			//                .offset(postSearch.getOffset())
			.offset(0L)
			.orderBy(post.id.desc())
			.fetch();
	}
}
