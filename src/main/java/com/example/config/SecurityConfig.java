package com.example.config;

import com.example.core.Encoder;
import com.example.user.MD5Encoder;

import io.github.dependency4j.Managed;
import io.github.dependency4j.Virtual;

@Managed(disposable = true)
public class SecurityConfig {

	@Virtual
	public Encoder md5Enconder() {
		return new MD5Encoder();
	}

}
