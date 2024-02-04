package com.blog.api.service;

import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blog.api.domain.Post;
import com.blog.api.domain.PostEditor;
import com.blog.api.domain.User;
import com.blog.api.exception.PostNotFound;
import com.blog.api.exception.UserNotFound;
import com.blog.api.repository.PostRepository;
import com.blog.api.repository.UserRepository;
import com.blog.api.request.PostCreate;
import com.blog.api.request.PostEdit;
import com.blog.api.request.PostSearch;
import com.blog.api.response.PostResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

	private final UserRepository userRepository;

	private final PostRepository postRepository;

	public void write(Long userId, PostCreate postCreate) {
		User user = userRepository.findById(userId)
			.orElseThrow(UserNotFound::new);
		//postcreate->Entity;
		Post post = Post.builder()
			.user(user)
			.title(postCreate.getTitle())
			.content(postCreate.getContent())
			.build();
		postRepository.save(post);
	}

	public PostResponse get(Long id) {
		Post post = postRepository.findById(id)
			.orElseThrow(PostNotFound::new);

		return PostResponse.builder()
			.id(post.getId())
			.title(post.getTitle())
			.content(post.getContent())
			.build();
	}

	public List<PostResponse> getList(PostSearch postSearch) {
		//        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC,"id"));
		return postRepository.getList(postSearch).stream()
			.map(PostResponse::new)
			.collect(toList());
	}

	@Transactional
	public void edit(Long id, PostEdit postEdit) {
		Post post = postRepository.findById(id)
			.orElseThrow(PostNotFound::new);

		PostEditor.PostEditorBuilder editorBuilder = post.toEditor();

		PostEditor postEditor = editorBuilder.title(postEdit.getTitle())
			.content(postEdit.getContent())
			.build();
		post.edit(postEditor);

	}

	public void delete(Long id) {
		Post post = postRepository.findById(id).orElseThrow(PostNotFound::new);

		postRepository.delete(post);
	}
}
