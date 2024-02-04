package com.blog.api.repository.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.api.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
