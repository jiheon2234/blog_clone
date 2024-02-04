package com.blog.api.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.api.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
}
