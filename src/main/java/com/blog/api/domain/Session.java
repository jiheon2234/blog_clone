package com.blog.api.domain;

import static java.util.UUID.*;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Session {

	@Id
	@GeneratedValue
	private Long id;

	private String accessToken;

	@ManyToOne
	private User user;

	@Builder
	public Session(User user) {
		this.accessToken = randomUUID().toString();
		this.user = user;
	}
}
