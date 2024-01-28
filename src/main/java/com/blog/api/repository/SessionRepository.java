package com.blog.api.repository;

import org.springframework.data.repository.CrudRepository;

import com.blog.api.domain.Session;

public interface SessionRepository extends CrudRepository<Session, Long> {
}
