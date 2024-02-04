package com.blog.api.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.blog.api.domain.Post;
import com.blog.api.domain.User;
import com.blog.api.exception.PostNotFound;
import com.blog.api.repository.UserRepository;
import com.blog.api.repository.post.PostRepository;
import com.blog.api.request.post.PostCreate;
import com.blog.api.request.post.PostEdit;
import com.blog.api.request.post.PostSearch;
import com.blog.api.response.PostResponse;

@SpringBootTest
class PostServiceTest {

	@Autowired
	private PostService postService;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void clean() {
		postRepository.deleteAll();
		userRepository.deleteAll();
	}

	@Test
	@DisplayName("글 작성")
	void test1() {
		var user = User.builder()
			.name("jjjj")
			.email("jjjj@jjjj.com")
			.password("0000")
			.build();
		userRepository.save(user);
		//given
		PostCreate post = PostCreate.builder()
			.title("제목입니다")
			.content("내용입니다")
			.build();

		//when
		postService.write(user.getId(), post);

		//then
		Assertions.assertEquals(1L, postRepository.count());
		Post findPost = postRepository.findAll().get(0);
		assertEquals("제목입니다", findPost.getTitle());
		assertEquals("내용입니다", findPost.getContent());
	}

	@Test
	@DisplayName("글 1개 조회")
	void test2() {
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
		assertEquals("foo", post.getTitle());
		assertEquals("bar", post.getContent());
	}

	@Test
	@DisplayName("글 여러개 조회")
	void test3() {
		//given
		List<Post> requestPosts = IntStream.range(0, 20)
			.mapToObj(i ->
				Post.builder()
					.title("제목" + i)
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
		assertEquals("제목19", posts.get(0).getTitle());
		//        assertEquals("컨텐츠15",posts.get(4).getContent());
	}

	@Test
	@DisplayName("글 제목 수정")
	void test4() {
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
		postService.edit(post.getId(), postEdit);
		//then
		Post changePost = postRepository.findById(post.getId())
			.orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));
		assertEquals("수정후제목", changePost.getTitle());
	}

	@Test
	@DisplayName("글 내용 수정")
	void test5() {
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
		postService.edit(post.getId(), postEdit);
		//then
		Post changePost = postRepository.findById(post.getId())
			.orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));
		assertEquals("수정전제목", changePost.getTitle()); //null들어와도 안바뀌게
		assertEquals("수정후컨텐츠", changePost.getContent());
	}

	@Test
	@DisplayName("계시글 삭제")
	void test6() {

		//given
		Post post = Post.builder()
			.title("수정전제목")
			.content("수정전컨텐츠")
			.build();
		postRepository.save(post);

		//when
		postService.delete(post.getId());

		//then
		assertEquals(0, postRepository.count());
	}

	@Test
	@Rollback(value = false)
	@DisplayName("계시글 조회-존재하지 않는 글")
	void test7() {

		//given
		Post post = Post.builder()
			.title("제목")
			.content("컨텐츠")
			.build();
		postRepository.save(post);

		//expected
		assertThrows(PostNotFound.class, () -> {
			postService.get(post.getId() + 1L);
		}, "예외처리가 잘못됨");

	}

	@Test
	@DisplayName("계시글 삭제- 존재하지 않는 글")
	void test8() {

		//given
		Post post = Post.builder()
			.title("제목")
			.content("컨텐츠")
			.build();
		postRepository.save(post);

		//expected
		assertThrows(PostNotFound.class, () -> {
			postService.delete(post.getId() + 1L);
		}, "예외처리가 잘못됨");

	}

	@Test
	@DisplayName("계시글 수정 - 존재하지 않는 글")
	void test9() {

		//given
		Post post = Post.builder()
			.title("제목")
			.content("컨텐츠")
			.build();
		postRepository.save(post);

		PostEdit postEdit = PostEdit.builder()
			.title("title")
			.content("content")
			.build();

		//expected
		assertThrows(PostNotFound.class, () -> {
			postService.edit(post.getId() + 1L, postEdit);
		}, "예외처리가 잘못됨");

	}

}