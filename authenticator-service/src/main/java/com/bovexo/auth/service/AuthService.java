package com.bovexo.auth.service;

import com.bovexo.auth.dto.LoginRequest;
import com.bovexo.auth.dto.LoginResponse;
import com.bovexo.auth.dto.RegisterRequest;
import com.bovexo.auth.model.Role;
import com.bovexo.auth.model.User;
import com.bovexo.auth.repository.UserRepository;
import com.bovexo.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public LoginResponse login(LoginRequest request) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.username(), request.password()));

    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    String token = jwtService.generateToken(userDetails);

    return new LoginResponse(token, "Bearer", jwtService.getExpirationTime());
  }

  public void register(RegisterRequest request) {
    if (userRepository.findByUsername(request.username()).isPresent()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este nome de usuário já está em uso.");
    }
    User newUser = new User();
    newUser.setUsername(request.username());
    newUser.setPassword(passwordEncoder.encode(request.password()));
    newUser.setRole(Role.ROLE_USER);

    userRepository.save(newUser);
  }
}