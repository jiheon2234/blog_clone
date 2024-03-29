package com.blog.api.domain;

import static jakarta.persistence.GenerationType.*;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Post {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private String title;
	@Lob
	private String content;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn
	private User user;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
	private List<Comment> comments = new ArrayList<>();

	@Builder
	public Post(String title, String content, User user) {
		this.title = title;
		this.content = content;
		this.user = user;
	}

	public void change(String title, String content, User user) {
		this.title = title;
		this.content = content;
	}

	public PostEditor.PostEditorBuilder toEditor() {
		return PostEditor.builder()
			.title(title)
			.content(content);
	}

	public void edit(PostEditor postEditor) {
		this.title = postEditor.getTitle();
		this.content = postEditor.getContent();
	}

	public Long getUserId() {
		return this.user.getId();
	}

	public void addComment(Comment comment) {
		comment.setPost(this);
		this.comments.add(comment);
	}
}
