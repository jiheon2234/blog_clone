package com.blog.api.request.comment;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCreate {

	@Length(min = 1, max = 8, message = "작성자는 1~8글자로 입력해주세요")
	@NotBlank(message = "작성자를 입력해주세요")
	private String author;

	@Length(min = 1, max = 30, message = "비밀번호는 6~30글자로 입력해주세요")
	@NotBlank(message = "비밀번호 입력해주세요")
	private String password;

	@Length(min = 10, max = 500, message = "내용은 1~500글자로 입력해주세요")
	@NotBlank(message = "내용 입력해주세요")
	private String content;

	@Builder
	public CommentCreate(String author, String password, String content) {
		this.author = author;
		this.password = password;
		this.content = content;
	}

	// public Comment toEntity() {
	// 	return Comment.builder()
	// 		.author(this.getAuthor())
	// 		.password(this.getPassword())
	// 		.content(this.getContent())
	// 		.build();
	// }
}
