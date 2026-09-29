package com.bloggingplatformapi.service;

import com.bloggingplatformapi.dto.AuthResponse;
import com.bloggingplatformapi.dto.LoginRequest;
import com.bloggingplatformapi.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
