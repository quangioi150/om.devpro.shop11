package com.devpro.shop;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneratePassword {
	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(5);
		String result = encoder.encode("user");
		System.out.println(result);
	}
}
