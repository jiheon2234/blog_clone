package com.blog.api.controller;

import com.blog.api.config.data.UserSession;
import com.blog.api.request.PostCreate;
import com.blog.api.request.PostEdit;
import com.blog.api.request.PostSearch;
import com.blog.api.response.PostResponse;
import com.blog.api.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    @GetMapping("/foo")
    public String foo(UserSession userSession) {
        log.info(">>>{}", userSession.id);
        return "foo";
    }

    @GetMapping("/bar")
    public String bar() {

        return "인증이 필요없는 bar";
    }


    @PostMapping("/posts")
    public void post(@Valid @RequestBody PostCreate request, @RequestHeader String authorization) {
        if (authorization.equals("jiheon")) {
            request.validate();
            postService.write(request);
        }
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
    public void edit(@PathVariable Long postId, @Valid @RequestBody PostEdit request, @RequestHeader String authorization) {
        if (authorization.equals("jiheon")) {
            postService.edit(postId, request);
        }
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }


}
