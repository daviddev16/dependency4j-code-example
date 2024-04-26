package com.example.user;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.example.core.Encoder;
import com.example.core.exception.ServiceException;
import io.github.dependency4j.util.Checks;

public class MD5Encoder implements Encoder {

	private MessageDigest messageDigest;

	public MD5Encoder() {
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new ServiceException("Failed to initialize MD5Encoder.", e);
		}
	}

	private byte[] md5ByteArray(String text) {
		Checks.nonNull(text, "Unable to hash a text using MD5. The text must not be null.");
		byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
		return messageDigest.digest(textBytes);
	}

	private String md5(String text) {
		return new String(md5ByteArray(text), StandardCharsets.UTF_8);
	}

	@Override
	public String encode(String text) {
		return md5(text);
	}

	@Override
	public boolean match(String text, String encodedText) {
		return md5(text).equals(encodedText);
	}

}
