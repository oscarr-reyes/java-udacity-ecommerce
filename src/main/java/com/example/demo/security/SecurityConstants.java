package com.example.demo.security;

public class SecurityConstants {
	public static final String SECRET = "JWTSecret";
	public static final String HEADER_STRING = "Authorization";
	public static final String TOKEN_PREFIX = "Bearer";
	public static final String SIGN_UP_URL = "/api/user/create";
	public static final Long EXPIRATION_TIME = 864_000_000L; // 10 days
}
