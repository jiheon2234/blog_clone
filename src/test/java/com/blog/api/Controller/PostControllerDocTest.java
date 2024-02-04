package com.blog.api.Controller;

import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.blog.api.domain.Post;
import com.blog.api.repository.UserRepository;
import com.blog.api.repository.post.PostRepository;
import com.blog.api.request.post.PostCreate;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.jiheon.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class PostControllerDocTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;

	@AfterEach
	void clean() {
		postRepository.deleteAll();
		userRepository.deleteAll();
	}

	//    @BeforeEach
	//    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
	//        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
	//                .apply(documentationConfiguration(restDocumentation))
	//                .build();
	//    }

	@Test
	@DisplayName("글 단건 조회 테스트")
	void test1() throws Exception {

		Post post = Post.builder()
			.title("제목")
			.content("내용")
			.build();
		postRepository.save(post);

		//expected
		this.mockMvc.perform(RestDocumentationRequestBuilders.get("/posts/{postId}", 1L).accept(APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("post-inquiry",
				RequestDocumentation.pathParameters(
					RequestDocumentation.parameterWithName("postId").description("계시글ID")
				),
				PayloadDocumentation.responseFields(
					PayloadDocumentation.fieldWithPath("id").description("계시글 ID"),
					PayloadDocumentation.fieldWithPath("title").description("제목"),
					PayloadDocumentation.fieldWithPath("content").description("내용")
				)

			));
	}

	@Test
	@DisplayName("글 등록")
	@WithMockUser
	void test2() throws Exception {

		PostCreate request = PostCreate.builder()
			.title("제목")
			.content("내용")
			.build();

		String json = objectMapper.writeValueAsString(request);

		//expected
		mockMvc.perform(RestDocumentationRequestBuilders.post("/posts")
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.content(json))
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("post-create",
				PayloadDocumentation.requestFields(
					PayloadDocumentation.fieldWithPath("title").description("제목")
						.attributes(Attributes.key("constraint").value("제목입력")),
					PayloadDocumentation.fieldWithPath("content").description("내용").optional()
				)
			));
	}

}
