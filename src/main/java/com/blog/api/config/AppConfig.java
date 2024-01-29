package com.blog.api.config;

import java.util.Base64;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "jiheon")
public class AppConfig {

	private byte[] jwtKey;

	public void setJwtKey(String jwtKey) {

		this.jwtKey = Base64.getDecoder().decode(jwtKey);
	}
}
