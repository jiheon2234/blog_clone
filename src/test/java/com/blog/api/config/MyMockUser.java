package com.blog.api.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MyMockSecurityContext.class)
public @interface MyMockUser {

	String name() default "jiheon";

	String email() default "aaaa@aaaa.com";

	String password() default "";

	// String role() default "ROLE_ADMIN";
}
