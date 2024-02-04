package com.blog.api.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blog.api.domain.Comment;
import com.blog.api.exception.CommentNotFound;
import com.blog.api.exception.InvalidPassword;
import com.blog.api.exception.PostNotFound;
import com.blog.api.repository.comment.CommentRepository;
import com.blog.api.repository.post.PostRepository;
import com.blog.api.request.comment.CommentCreate;
import com.blog.api.request.comment.CommentDelete;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {

	private final PostRepository postRepository;
	private final CommentRepository commentRepository;

	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void write(Long postId, CommentCreate request) {
		var post = postRepository.findById(postId)
			.orElseThrow(PostNotFound::new);

		String encryptedPassword = passwordEncoder.encode(request.getPassword());

		Comment comment = Comment.builder()
			// .post(post)
			.author(request.getAuthor())
			.password(encryptedPassword)
			.content(request.getContent())
			.build();

		post.addComment(comment);
	}

	public void delete(Long commentId, CommentDelete request) {
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(CommentNotFound::new);

		String encryptedPassword = comment.getPassword();
		if (!passwordEncoder.matches(request.getPassword(), encryptedPassword)) {
			throw new InvalidPassword();
		}

		commentRepository.delete(comment);
	}

}
