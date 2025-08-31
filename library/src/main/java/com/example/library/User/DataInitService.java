package com.example.library.User;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DataInitService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initData() {
        // Skapa testanvändare om de inte finns
        if (!userRepository.existsByEmail("user@test.com")) {
            User user = new User();
            user.setEmail("user@test.com");
            user.setPassword(passwordEncoder.encode("password123"));  // Kryptera lösenord
            user.setRoles("USER");
            user.setEnabled(true);
            userRepository.save(user);
        }

        if (!userRepository.existsByEmail("admin@test.com")) {
            User admin = new User();
            admin.setEmail("admin@test.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles("ADMIN");
            admin.setEnabled(true);
            userRepository.save(admin);
        }

        System.out.println("✅ Testanvändare skapade:");
        System.out.println("   👤 user@test.com / password123 (USER)");
        System.out.println("   👑 admin@test.com / admin123 (ADMIN)");
    }
}
