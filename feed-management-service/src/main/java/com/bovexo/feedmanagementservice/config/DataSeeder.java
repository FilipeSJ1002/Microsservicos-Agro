package com.bovexo.feedmanagementservice.config;

import com.bovexo.feedmanagementservice.model.User;
import com.bovexo.feedmanagementservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void run(String... args) {
    if (userRepository.findByUsername("visitante").isEmpty()) {
      User guest = new User();
      guest.setUsername("visitante");
      guest.setPassword(passwordEncoder.encode("bovexo123"));
      guest.setRole("GUEST");
      userRepository.save(guest);
      System.out.println("[SEED] Usuário visitante criado com sucesso!");
    }
  }
}