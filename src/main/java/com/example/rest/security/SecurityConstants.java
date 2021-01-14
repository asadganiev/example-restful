package com.example.rest.security;

public class SecurityConstants {

    public static final String API_VERSION = "/v1";
    public static final String SECRET = "nej6k2WZPSAzgsegzK6hYMtN3fg7KWVV";
    public static final long EXPIRATION_TIME = 30 * 60 * 1000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = API_VERSION + "/users/signup";
}