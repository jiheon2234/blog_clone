package com.blog.api.controller;

import com.blog.api.request.PostCreate;
import com.blog.api.response.PostResponse;
import com.blog.api.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public void post(@Valid @RequestBody PostCreate request  )  {
         postService.write(request);
    }


    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable(name="postId") Long id){
        PostResponse response = postService.get(id);
        return response;


    }


}
