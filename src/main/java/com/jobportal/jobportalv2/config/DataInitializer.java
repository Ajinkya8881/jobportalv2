package com.jobportal.jobportalv2.config;


import com.jobportal.jobportalv2.entity.User;
import com.jobportal.jobportalv2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args){
        String adminEmail = "admin@jobportal.com";

        if(!userRepository.existsByEmail(adminEmail)){
            User admin = User.builder()
                    .name("Admin")
                    .email(adminEmail)
                    .password(passwordEncoder.encode("admin123"))
                    .role("ROLE_ADMIN")
                    .build();

            userRepository.save(admin);

            System.out.println("Admin has been created");
        }
    }
}
