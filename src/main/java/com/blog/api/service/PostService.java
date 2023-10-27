package com.blog.api.service;

import com.blog.api.repository.PostRepository;
import com.blog.api.request.PostCreate;
import com.blog.api.domain.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate) {
        //postcreate->Entity;
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();
        postRepository.save(post);
    }
}
