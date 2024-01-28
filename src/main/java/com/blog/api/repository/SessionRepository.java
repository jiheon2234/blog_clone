package com.blog.api.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.blog.api.domain.Session;

public interface SessionRepository extends CrudRepository<Session, Long> {

	Optional<Session> findByAccessToken(String accessToken);
}
