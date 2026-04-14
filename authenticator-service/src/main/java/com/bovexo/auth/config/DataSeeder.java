package com.bovexo.auth.config;

import com.bovexo.auth.model.Role;
import com.bovexo.auth.model.User;
import com.bovexo.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) throws Exception {
    if (userRepository.findByUsername("admin").isEmpty()) {
      User admin = new User();
      admin.setUsername("admin");
      admin.setPassword(passwordEncoder.encode("admin123"));
      admin.setRole(Role.ROLE_ADMIN);

      userRepository.save(admin);
      System.out.println("Usuário administrador criado com sucesso no banco de dados!");
    }

    if (userRepository.findByUsername("guest").isEmpty()) {
      User guest = new User();
      guest.setUsername("guest");
      guest.setPassword(passwordEncoder.encode("guest123"));
      guest.setRole(Role.ROLE_USER);

      userRepository.save(guest);
      System.out.println("Usuário visitante (guest) criado com sucesso!");
    }
  }
}