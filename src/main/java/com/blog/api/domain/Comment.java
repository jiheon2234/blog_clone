package com.blog.api.domain;

import static jakarta.persistence.GenerationType.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
	indexes = {
		@Index(name = "IDX_COMMENT_POST_ID", columnList = "post_id")
	}
)
@Getter
@NoArgsConstructor
public class Comment {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String author;

	private String password;

	@Column(nullable = false)
	private String content;

	@ManyToOne
	@JoinColumn
	private Post post;

	@Builder
	public Comment(Long id, String author, String password, String content, Post post) {
		this.author = author;
		this.password = password;
		this.content = content;
		this.post = post;
	}

	public void setPost(Post post) {
		this.post = post;
	}
}
