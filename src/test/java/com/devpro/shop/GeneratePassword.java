package com.devpro.shop;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneratePassword {
	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(9);
		String result = encoder.encode("tranduong");
		System.out.println(result);
	}
}
