package com.blog.api.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.blog.api.request.PostCreate;
import com.blog.api.request.PostEdit;
import com.blog.api.request.PostSearch;
import com.blog.api.response.PostResponse;
import com.blog.api.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/posts")
	public void post(@Valid @RequestBody PostCreate request) {
		request.validate();
		postService.write(request);
	}

	@GetMapping("/posts/{postId}")
	public PostResponse get(@PathVariable(name = "postId") Long id) {
		return postService.get(id);
	}

	@GetMapping("/posts")
	public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
		return postService.getList(postSearch);
	}

	@PatchMapping("/posts/{postId}")
	public void edit(@PathVariable Long postId, @Valid @RequestBody PostEdit request) {
		postService.edit(postId, request);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/posts/{postId}")
	public void delete(@PathVariable Long postId) {
		postService.delete(postId);
	}

}
