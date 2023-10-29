package com.blog.api.service;

import com.blog.api.domain.Post;
import com.blog.api.repository.PostRepository;
import com.blog.api.request.PostCreate;
import com.blog.api.request.PostEdit;
import com.blog.api.request.PostSearch;
import com.blog.api.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean(){
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1(){
        //given
        PostCreate post = PostCreate.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        //when
        postService.write(post);

        //then
        Assertions.assertEquals(1L,postRepository.count() );
        Post findPost = postRepository.findAll().get(0);
        assertEquals("제목입니다", findPost.getTitle());
        assertEquals("내용입니다", findPost.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test2(){
        //given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);

        Long postId = 1L;
        //when
        PostResponse post = postService.get(requestPost.getId());

        //then
        assertNotNull(post);
        assertEquals("foo",post.getTitle());
        assertEquals("bar",post.getContent());
    }

    @Test
    @DisplayName("글 여러개 조회")
    void test3(){
        //given
        List<Post> requestPosts = IntStream.range(0,20)
                        .mapToObj(i->
                            Post.builder()
                                    .title("제목"+i)
                                    .content("컨텐츠" + i)
                                    .build()
                        ).toList();
        postRepository.saveAll(requestPosts);

        //when

        PostSearch postSearch = PostSearch.builder()
                .page(1)
//                .size(10)
                .build();

        List<PostResponse> posts = postService.getList(postSearch);

        for (PostResponse post : posts) {
            System.out.println("post.getId() = " + post.getId());
        }

        //then
        assertEquals(10L, posts.size());
        assertEquals("제목19",posts.get(0).getTitle());
//        assertEquals("컨텐츠15",posts.get(4).getContent());
    }


    @Test
    @DisplayName("글 제목 수정")
    void test4(){
        //given
        Post post = Post.builder()
                .title("수정전제목")
                .content("수정전컨텐츠")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("수정후제목")
                .build();
        //when
        postService.edit(post.getId(),postEdit);
        //then
        Post changePost = postRepository.findById(post.getId()).orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));
        assertEquals("수정후제목",changePost.getTitle());
    }

    @Test
    @DisplayName("글 내용 수정")
    void test5(){
        //given
        Post post = Post.builder()
                .title("수정전제목")
                .content("수정전컨텐츠")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title(null)
                .content("수정후컨텐츠")
                .build();
        //when
        postService.edit(post.getId(),postEdit);
        //then
        Post changePost = postRepository.findById(post.getId()).orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));
        assertEquals("수정전제목",changePost.getTitle()); //null들어와도 안바뀌게
        assertEquals("수정후컨텐츠",changePost.getContent());
    }



}