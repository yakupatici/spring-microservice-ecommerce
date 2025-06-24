package com.code.springboot.springmicroserviceecommerce;

import com.code.springboot.springmicroserviceecommerce.model.Role;
import com.code.springboot.springmicroserviceecommerce.model.User;
import com.code.springboot.springmicroserviceecommerce.repository.RoleRepository;
import com.code.springboot.springmicroserviceecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

// Main entry point class for Spring Boot application
@SpringBootApplication
public class SpringMicroserviceEcommerceApplication implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Main method to start the application
    public static void main(String[] args) {
        SpringApplication.run(SpringMicroserviceEcommerceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Create roles if not exist
        Role adminRole = roleRepository.findByName("ADMIN");
        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);
        }

        Role userRole = roleRepository.findByName("USER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);
        }

        // Create admin user if not exist
        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoles(Arrays.asList(adminRole));
            userRepository.save(admin);
        }
    }
}
