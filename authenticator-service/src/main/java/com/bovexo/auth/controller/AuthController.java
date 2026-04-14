package com.bovexo.auth.controller;

import com.bovexo.auth.dto.LoginRequest;
import com.bovexo.auth.dto.LoginResponse;
import com.bovexo.auth.dto.MessageResponse;
import com.bovexo.auth.dto.RegisterRequest;
import com.bovexo.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    LoginResponse response = authService.login(request);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/register")
  public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterRequest request) {
    authService.register(request);
    return ResponseEntity.status(201).body(new MessageResponse("Conta criada com sucesso. Faça login para entrar!"));
  }
}