package com.neche.bankapp.service;

import com.neche.bankapp.payload.request.LoginRequest;
import com.neche.bankapp.payload.request.UserRequest;
import com.neche.bankapp.payload.respond.ApiResponse;
import com.neche.bankapp.payload.respond.BankResponse;
import com.neche.bankapp.payload.respond.JwtAuthResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    BankResponse registerUser(UserRequest userRequest);

    ResponseEntity<ApiResponse<JwtAuthResponse>> loginUser(LoginRequest loginRequest);
}
