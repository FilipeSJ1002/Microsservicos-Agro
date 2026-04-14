package com.bovexo.auth.config;

import com.bovexo.auth.model.Role;
import com.bovexo.auth.model.User;
import com.bovexo.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Value("${seeder.admin.username}")
  private String adminUsername;

  @Value("${seeder.admin.password}")
  private String adminPassword;

  @Value("${seeder.guest.username}")
  private String guestUsername;

  @Value("${seeder.guest.password}")
  private String guestPassword;

  @Value("${seeder.nutrition.username}")
  private String nutritionUsername;

  @Value("${seeder.nutrition.password}")
  private String nutritionPassword;

  @Override
  public void run(String... args) throws Exception {
    
    if (userRepository.findByUsername(adminUsername).isEmpty()) {
      User admin = new User();
      admin.setUsername(adminUsername);
      admin.setPassword(passwordEncoder.encode(adminPassword));
      admin.setRole(Role.ROLE_ADMIN);
      userRepository.save(admin);
      System.out.println("Usuário administrador (" + adminUsername + ") criado com sucesso!");
    }

    if (userRepository.findByUsername(guestUsername).isEmpty()) {
      User guest = new User();
      guest.setUsername(guestUsername);
      guest.setPassword(passwordEncoder.encode(guestPassword));
      guest.setRole(Role.ROLE_USER);
      userRepository.save(guest);
      System.out.println("Usuário visitante (" + guestUsername + ") criado com sucesso!");
    }

    if (userRepository.findByUsername(nutritionUsername).isEmpty()) {
      User nutritionAnalysis = new User();
      nutritionAnalysis.setUsername(nutritionUsername);
      nutritionAnalysis.setPassword(passwordEncoder.encode(nutritionPassword));
      nutritionAnalysis.setRole(Role.ROLE_ADMIN);
      userRepository.save(nutritionAnalysis);
      System.out.println("Usuário de serviço (" + nutritionUsername + ") criado com sucesso!");
    }
  }
}