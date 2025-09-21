package com.example.infsecuritylab1.config;

import com.example.infsecuritylab1.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Supplier;

@Configuration
public class AppConfig {

    @Bean
    public Supplier<UserService> userServiceSupplier(UserService userService) {
        return () -> userService;
    }
}
