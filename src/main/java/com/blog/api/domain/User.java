package com.blog.api.domain;

import static lombok.AccessLevel.*;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "Users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String email;

	private String password;

	private LocalDateTime createdAt;

	@Builder
	public User(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.createdAt = LocalDateTime.now();
	}

}
