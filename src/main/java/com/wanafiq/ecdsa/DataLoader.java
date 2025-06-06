package com.wanafiq.ecdsa;

import com.wanafiq.ecdsa.model.Role;
import com.wanafiq.ecdsa.model.User;
import com.wanafiq.ecdsa.repository.RoleRepository;
import com.wanafiq.ecdsa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepo;
    private final UserRepository userRepo;

    @Override
    public void run(String... args) {
        log.info("Loading sample data...");

        List<String> users = List.of("user", "admin");

        users.forEach(u -> {
            String roleName = String.format("ROLE_%s", u.toUpperCase());

            Role role = Role.builder()
                    .name(roleName)
                    .build();
            roleRepo.save(role);

            String email = String.format("%s@gmail.com", u);
            String password = passwordEncoder.encode("password");

            User user = User.builder()
                    .email(email)
                    .password(password)
                    .fullName("User")
                    .roles(List.of(role))
                    .build();

            userRepo.save(user);
        });

        log.info("Data loaded");
    }

}
