package com.blog.api.request.post;

import com.blog.api.exception.InvalidRequest;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PostCreate {

	@NotBlank(message = "타이틀을 입력해주세요")
	private String title;

	@NotBlank(message = "컨텐트를 입력해주세요")
	private String content;

	@Builder
	public PostCreate(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public void validate() {
		if (title.contains("바보")) {
			throw new InvalidRequest("title", "제목에 바보 포함 불가");
		}
	}
}


