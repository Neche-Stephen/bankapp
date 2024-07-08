package com.neche.bankapp.infrastructure.controller;

import com.neche.bankapp.payload.request.LoginRequest;
import com.neche.bankapp.payload.request.UserRequest;
import com.neche.bankapp.payload.respond.ApiResponse;
import com.neche.bankapp.payload.respond.BankResponse;
import com.neche.bankapp.payload.respond.JwtAuthResponse;
import com.neche.bankapp.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/register-user")
    public BankResponse createUserAccount(@Valid @RequestBody UserRequest userRequest){

        return  authService.registerUser(userRequest);
    }

    @PostMapping("/login-user")
    public ResponseEntity<ApiResponse<JwtAuthResponse>> loginUser(@Valid @RequestBody LoginRequest loginRequest){

        return authService.loginUser(loginRequest);
    }
}
